<template>
    <div>
      <div class="search">
        <el-input placeholder="请输入标题查询" style="width: 200px" v-model="title"></el-input>
        <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
        <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
      </div>
  
      <div class="operation">
        
        <el-button type="danger" plain @click="delBatch">批量删除</el-button>
      </div>
        <!-- id title content img user_id time circle comment_count-->
      <div class="table">
        <el-table :data="tableData" stripe  @selection-change="handleSelectionChange">
          <el-table-column type="selection"  align="center"></el-table-column>
          <el-table-column prop="id" label="id"  align="center" sortable></el-table-column>
          <el-table-column prop="title" label="标题"  align="center" sortable></el-table-column>
          <el-table-column label="内容">
	        <template v-slot="scope">
            <el-button @click="preview(scope.row.content)">查看内容</el-button>
            </template>
          </el-table-column>
          <el-table-column prop="img" label="缩略图"  align="center" sortable>
            <template v-slot="scope">
            <div style="display: flex; align-items: center">
            <el-image style="width: 40px; height: 40px;" v-if="scope.row.img"
                :src="scope.row.img" :preview-src-list="[scope.row.img]"></el-image>
            </div>
            </template>
          </el-table-column>
          <el-table-column prop="userId" label="用户Id"  align="center" sortable></el-table-column>
          <el-table-column prop="userName" label="用户名称"  align="center" sortable></el-table-column>
          <el-table-column prop="time" label="发布时间"  align="center" sortable></el-table-column>
          <el-table-column prop="circle" label="圈子"  align="center" sortable></el-table-column>
          <el-table-column prop="descr" label="圈子"  align="center" sortable></el-table-column>
          <el-table-column prop="readCount" label="阅读数量"  align="center" sortable></el-table-column>
          <el-table-column prop="status" label="审核状态"  align="center">
                <template v-slot="scope">
                    <el-tag type ="info" v-if="scope.row.status==='待审核'" >待审核</el-tag>
                    <el-tag type ="info" v-if="scope.row.status==='通过'" >通过</el-tag>
                    <el-tag type ="info" v-if="scope.row.status==='拒绝'" >拒绝</el-tag>
                </template>
          </el-table-column>
          <el-table-column label="操作" width="220" align="center">
            <template v-slot="scope">
                <el-button plain type="success" size="mini" @click="changeStatus(scope.row, '通过')">通过</el-button>
                <el-button plain type="danger" size="mini" @click="changeStatus(scope.row, '拒绝')">拒绝</el-button>
              <el-button plain type="danger" size="mini" @click=del(scope.row.id)>删除</el-button>
            </template>
          </el-table-column>
        </el-table>
  
        <div class="pagination">
          <el-pagination
              background
              @current-change="handleCurrentChange"
              :current-page="pageNum"
              :page-sizes="[5, 10, 20]"
              :page-size="pageSize"
              layout="total, prev, pager, next"
              :total="total">
          </el-pagination>
        </div>
      </div>
  
        <el-dialog title="文章内容" :visible.sync="formVisible1" width="40%" :close-on-click-modal="false" destroy-on-close>
            <div class="w-e-text">
                <div v-html="content"></div>
            </div>
        <div slot="footer" class="dialog-footer">
            <el-button @click="formVisible1=false">关 闭</el-button>
        </div>
        </el-dialog>
  
      <el-dialog name="圈子" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
        <el-form label-width="100px" style="padding-right: 50px" :model="form" :rules="rules" ref="formRef">
          <el-form-item prop="title" label="标题">
            <el-input v-model="form.title" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item prop="img" label="缩略图">
            <el-upload
                :action="$baseUrl + '/files/upload'"
                :headers="{ token: user.token }"
                :on-success="handleFileSuccess"
            >
            <el-button type="primary">上传</el-button>
            </el-upload>
          </el-form-item>

          <el-form-item prop="content" label="内容">
            <el-input v-model="form.content" autocomplete="off"></el-input>
          </el-form-item>

          <el-form-item prop="user_id" label="用户ID">
            <el-input v-model="form.user_id" autocomplete="off"></el-input>
          </el-form-item>
 
          <el-form-item prop="time" label="发布时间">
            <el-input v-model="form.time" autocomplete="off"></el-input>
          </el-form-item>

          <el-form-item prop="circle" label="圈子">
            <el-input v-model="form.circle" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item prop="comment_count" label="评论">
            <el-input v-model="form.comment_count" autocomplete="off"></el-input>
          </el-form-item>
          
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="fromVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </div>
      </el-dialog>
  
  
    </div>
  </template>
  
  <script>
  export default {
    name: "Posts",
    data() {
      return {
        tableData: [],  // 所有的数据
        pageNum: 1,   // 当前的页码
        pageSize: 10,  // 每页显示的个数
        total: 0,
        title: null,
        formVisible: false,
        form: {},
        content:'',
        formVisible1: false,
        user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
        rules: {
          title: [
            {required: true, message: '请输入标题', trigger: 'blur'},
          ]
        },
        ids: []
      }
    },
    created() {
      this.load(1)
    },
    methods: {
        changeStatus(row, status){
            this.$confirm('您确定' + status + '吗？','确认操作', {type:"warning"}).then(response => {
                this.form = JSON.parse(JSON.stringify(row))
                this.form.status = status
                this.$request.put('/posts/update', this.form).then(res => {
                if (res.code === '200') {  // 表示成功保存
                    this.$message.success('操作成功')
                    this.load(1)
                } else {
                    this.$message.error(res.msg)  // 弹出错误的信息
                }
            })
            }).catch(err => {})
        },
        preview(content){
	        this.content = content
	        this.formVisible1 = true
        },
        handleFileSuccess(response, file, fileList) {
            this.form.img = response.data
        },
      handleAdd() {   // 新增数据
        this.form = {}  // 新增数据的时候清空数据
        this.fromVisible = true   // 打开弹窗
      },
      handleEdit(row) {   // 编辑数据
        this.form = JSON.parse(JSON.stringify(row))  // 给form对象赋值  注意要深拷贝数据
        this.fromVisible = true   // 打开弹窗
      },
      save() {   // 保存按钮触发的逻辑  它会触发新增或者更新
        this.$refs.formRef.validate((valid) => {
          if (valid) {
            this.$request({
              url: this.form.id ? '/posts/update' : '/posts/add',
              method: this.form.id ? 'PUT' : 'POST',
              data: this.form
            }).then(res => {
              if (res.code === '200') {  // 表示成功保存
                this.$message.success('保存成功')
                this.load(1)
                this.fromVisible = false
              } else {
                this.$message.error(res.msg)  // 弹出错误的信息
              }
            })
          }
        })
      },
      del(id) {   // 单个删除
        this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
          this.$request.delete('/posts/delete/' + id).then(res => {
            if (res.code === '200') {   // 表示操作成功
              this.$message.success('操作成功')
              this.load(1)
            } else {
              this.$message.error(res.msg)  // 弹出错误的信息
            }
          })
        }).catch(() => {
        })
      },
      handleSelectionChange(rows) {   // 当前选中的所有的行数据
        this.ids = rows.map(v => v.id)   //  [1,2]
      },
      delBatch() {   // 批量删除
        if (!this.ids.length) {
          this.$message.warning('请选择数据')
          return
        }
        this.$confirm('您确定批量删除这些数据吗？', '确认删除', {type: "warning"}).then(response => {
          this.$request.delete('/posts/delete/batch', {data: this.ids}).then(res => {
            if (res.code === '200') {   // 表示操作成功
              this.$message.success('操作成功')
              this.load(1)
            } else {
              this.$message.error(res.msg)  // 弹出错误的信息
            }
          })
        }).catch(() => {
        })
      },
      load(pageNum) {  // 分页查询
        if (pageNum) this.pageNum = pageNum
        this.$request.get('/posts/selectPage', {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            title: this.title,
          }
        }).then(res => {
          this.tableData = res.data?.list
          this.total = res.data?.total
        })
      },
      reset() {
        this.title = null
        this.load(1)
      },
      handleCurrentChange(pageNum) {
        this.load(pageNum)
      },
    }
  }
  </script>
  
  <style scoped>
  
  </style>
  