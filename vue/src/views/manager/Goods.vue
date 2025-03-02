<template>
    <div>
      <div class="search">
        <el-input placeholder="请输入名称查询" style="width: 200px" v-model="name"></el-input>
        <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
        <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
      </div>
  
      <div class="operation">
        <el-button type="danger" plain @click="delBatch">批量删除</el-button>
      </div>
      <!--id,name,price,content,address,img,date,category,user_id.sale_status,read_count-->
      <div class="table">
        <el-table :data="tableData" stripe  @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" align="center"></el-table-column>
          <el-table-column prop="id" label="id"  align="center" sortable></el-table-column>
          <el-table-column prop="name" label="商品名称"  align="center" show-overflow-tooltip></el-table-column>
          <el-table-column prop="price" label="价格"  align="center"></el-table-column>
          <el-table-column prop="content" label="详情" width="200" align="center">
            <template v-slot="scope">
                <el-button @click = "preview(scope.row.content)">显示详情</el-button>
            </template>
          </el-table-column>
          <el-table-column prop="address" label="地址"  align="center"></el-table-column>
          <el-table-column prop="img" label="图片"  align="center" >
            <template v-slot="scope">
            <el-image v-if="scope.row.img" style="width:50px" :src="scope.row.img" :prewview-src-list="scope.row.img"></el-image>
          </template>
          </el-table-column>
          <el-table-column prop="date" label="上架日期"  align="center"></el-table-column>

          <el-table-column prop="status" label="审核状态"  align="center">
                <template v-slot="scope">
                    <el-tag type ="info" v-if="scope.row.status==='待审核'" >待审核</el-tag>
                    <el-tag type ="info" v-if="scope.row.status==='通过'" >通过</el-tag>
                    <el-tag type ="info" v-if="scope.row.status==='拒绝'" >拒绝</el-tag>
                </template>
          </el-table-column>

          <el-table-column prop="category" label="类别"  align="center"></el-table-column>
          <el-table-column prop="userId" label="用户id"  align="center"></el-table-column>
          <el-table-column prop="userName" label="用户名"  align="center"></el-table-column>
          <el-table-column prop="saleStatus" label="出售状态"  align="center"></el-table-column>
          <el-table-column prop="readCount" label="浏览量"  align="center"></el-table-column>
          
  
          <el-table-column label="操作" width="240" align="center">
            <template v-slot="scope">
            <el-button plain type="success" size="mini" @click="changeStatus(scope.row, '通过')">通过</el-button>
            <el-button plain type="danger" size="mini" @click="changeStatus(scope.row, '拒绝')">拒绝</el-button>
            <el-button plain type="danger" size="mini" @click="del(scope.row.id)">删除</el-button>
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
  <el-dialog tittle="内容" :visible.sync="fromVisible1" width="40%" :close-on-click-modal="false" destroy-on-close>
    <div v-html="content"></div>
  </el-dialog>
  <!--id,name,price,content,address,img,date,category,user_id.sale_status,read_count-->
      <el-dialog name="商品信息" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
        <el-form label-width="100px" style="padding-right: 50px" :model="form" :rules="rules" ref="formRef">
          <el-form-item prop="name" label="商品名称">
            <el-input v-model="form.name" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item prop="price" label="价格">
            <el-input v-model="form.price" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item prop="content" label="详情">
            <el-input v-model="form.content" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item prop="address" label="地址">
            <el-input v-model="form.address" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item prop="img" label="图片">
            <el-input v-model="form.img" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item prop="date" label="日期">
            <el-input v-model="form.date" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item prop="status" label="审核状态">
            <el-input v-model="form.date" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item prop="category" label="类别名称">
            <el-input v-model="form.category" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item prop="userId" label="用户id">
            <el-input v-model="form.userId" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item prop="saleStatus" label="售卖状态">
            <el-input v-model="form.saleStatus" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item prop="readCount" label="浏览量">
            <el-input v-model="form.readCount" autocomplete="off"></el-input>
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
    name: "Goods",
    data() {
      return {
        tableData: [],  // 所有的数据
        pageNum: 1,   // 当前的页码
        pageSize: 10,  // 每页显示的个数
        total: 0,
        name: null,
        fromVisible: false,
        form: {},
        user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
        rules: {
          name: [
            {required: true, message: '请输入商品名称', trigger: 'blur'},
          ]
        },
        ids: [],
        content:'',
        fromVisible1:false
      }
    },
    created() {
      this.load(1)
    },
    methods: {
        preview(content){
            this.content = content
            this.fromVisible1 = true
        },
        changeStatus(row, status){
            this.$confirm('您确定要操作吗？','确认操作', {type:"warning"}).then(response => {
                this.form = JSON.parse(JSON.stringify(row))
                this.form.status = status
                this.$request.put('/goods/update', this.form).then(res => {
                if (res.code === '200') {  // 表示成功保存
                    this.$message.success('保存成功')
                    this.load(1)
                } else {
                    this.$message.error(res.msg)  // 弹出错误的信息
                }
            })
            }).catch(err => {})
            
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
              url: this.form.id ? '/goods/update' : '/goods/add',
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
          this.$request.delete('/goods/delete/' + id).then(res => {
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
          this.$request.delete('/goods/delete/batch', {data: this.ids}).then(res => {
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
        this.$request.get('/goods/selectPage', {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            name: this.name,
          }
        }).then(res => {
            console.log(res);
          this.tableData = res.data?.list
          this.total = res.data?.total
        })
      },
      reset() {
        this.name = null
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
  