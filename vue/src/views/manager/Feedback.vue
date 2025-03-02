<template>
    <div>
      <div class="search">
        <el-input placeholder="请输入主题查询" style="width: 200px" v-model="title"></el-input>
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
          <el-table-column prop="title" label="主题"  align="center" ></el-table-column>
          <el-table-column prop="content" label="内容"  align="center" ></el-table-column>
          <el-table-column prop="phone" label="联系方式"  align="center" ></el-table-column>
          <el-table-column prop="email" label="邮件"  align="center" ></el-table-column>
          <el-table-column prop="reply" label="回复"  align="center" ></el-table-column>
          <el-table-column prop="createTime" label="创建时间"  align="center" ></el-table-column>
          <el-table-column prop="userId" label="用户Id"  align="center" ></el-table-column>
          <el-table-column prop="userName" label="用户名称"  align="center" sortable></el-table-column>
          <el-table-column label="操作" width="220" align="center">
            <template v-slot="scope">
                <el-button plain type="primary" size="mini" @click=handleEdit(scope.row)>回复</el-button>
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
  
  
      <el-dialog name="反馈信息" :visible.sync="formVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
        <el-form ref="formRef" :model="form" label-width="100px">
          <el-form-item prop="reply" label="回复">
            <el-input type="textarea" v-model="form.reply" placeholder="回复"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="formVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </div>
      </el-dialog>
  
  
    </div>
  </template>
  
  <script>
  export default {
    name: "Feedback",
    data() {
      return {
        tableData: [],  // 所有的数据
        pageNum: 1,   // 当前的页码
        pageSize: 10,  // 每页显示的个数
        total: 0,
        title: null,
        formVisible: false,
        formVisible1:false,
        form: {},
        content:'',
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
        preview(content){
	        this.content = content
	        this.formVisible1 = true
        },
        handleFileSuccess(response, file, fileList) {
            this.form.img = response.data
        },
      handleAdd() {   // 新增数据
        this.form = {}  // 新增数据的时候清空数据
        this.formVisible = true   // 打开弹窗
      },
      handleEdit(row) {   // 编辑数据
        this.form = JSON.parse(JSON.stringify(row))  // 给form对象赋值  注意要深拷贝数据
        this.formVisible = true   // 打开弹窗
      },
      save() {   // 保存按钮触发的逻辑  它会触发新增或者更新
        this.$refs.formRef.validate((valid) => {
          if (valid) {
            this.$request({
              url: this.form.id ? '/feedback/update' : '/feedback/add',
              method: this.form.id ? 'PUT' : 'POST',
              data: this.form
            }).then(res => {
                console.log("提交的数据:", this.form);
              if (res.code === '200') {  // 表示成功保存
                this.$message.success('保存成功')
                this.load(1)
                this.formVisible = false
              } else {
                this.$message.error(res.msg)  // 弹出错误的信息
              }
            })
          }
        })
      },
      del(id) {   // 单个删除
        this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
          this.$request.delete('/feedback/delete/' + id).then(res => {
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
          this.$request.delete('/feedback/delete/batch', {data: this.ids}).then(res => {
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
        this.$request.get('/feedback/selectPage', {
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
  