<template>
    <div style="width: 70%; margin: 10px auto; " class="card">
      <div class="table">
        <el-table :data="tableData" stripe >
          <el-table-column prop="name" label="商品名称"  align="center" show-overflow-tooltip></el-table-column>
          <el-table-column prop="price" label="价格"  align="center">
            <template v-slot="scope">
              <span style="color:red;"> ￥{{scope.row.price}}</span>
            </template>
          </el-table-column>
          <el-table-column prop="content" label="详情" width="150" align="center">
            <template v-slot="scope">
                <el-button @click = "preview(scope.row.content)">显示详情</el-button>
            </template>
          </el-table-column>
          <el-table-column prop="address" label="地址"  align="center"></el-table-column>
          <el-table-column prop="img" label="图片"  align="center">
            <template v-slot="scope">
            <div style="display: flex; align-items: center">
            <el-image style="width: 40px; height: 40px;" v-if="scope.row.img"
                :src="scope.row.img" :preview-src-list="[scope.row.img]"></el-image>
            </div>
            </template>
          </el-table-column>
          <el-table-column prop="date" label="上架日期"  align="center"></el-table-column>

          <el-table-column prop="status" label="审核状态"  align="center">
                <template v-slot="scope">
                    <el-tag type ="info" v-if="scope.row.status==='待审核'" >待审核</el-tag>
                    <el-tag type ="success" v-if="scope.row.status==='通过'" >通过</el-tag>
                    <el-tag type ="danger" v-if="scope.row.status==='拒绝'" >拒绝</el-tag>
                </template>
          </el-table-column>

          <el-table-column prop="category" label="类别"  align="center"></el-table-column>
          <el-table-column prop="userId" label="用户id"  align="center"></el-table-column>
          <el-table-column prop="userName" label="用户名"  align="center"></el-table-column>
          <el-table-column prop="saleStatus" label="出售状态"  align="center"></el-table-column>
          <el-table-column prop="readCount" label="浏览量"  align="center"></el-table-column>
          
  
          <el-table-column label="操作" width="142" align="center">
            <template v-slot="scope">
            <el-button plain type="primary" size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button plain type="danger" size="mini" @click="del(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
  
        <div style="margin: 15px 0">
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
  <el-dialog title="内容" :visible.sync="fromVisible1" width="40%" :close-on-click-modal="false" destroy-on-close>
    <div v-html="content"></div>
  </el-dialog>

    </div>
  </template>
  
  <script>
  export default {
    name: "FrontGoods",
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
      handleFileSuccess(response) {
    if (response.code === '200') {
      this.form.file = response.data
        this.$message.success('图片上传成功');
    } else {
        this.$message.error('图片上传失败');
    }

},

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
        this.$router.push('/front/addGoods?id=' + row.id)
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
  