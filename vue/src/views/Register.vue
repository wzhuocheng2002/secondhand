<template>
  <div class="container">
  <div style = "width: 800px;">
    <img src="@/assets/imgs/loginbg.jpg" alt = "" style="width: 100%">
  </div>
  <div style="flex: 1">
      <div style="height: 50px; display: flex; align-items: center; padding-left: 20px;">
        <img src="@/assets/imgs/loginlogo.png" alt=""style="width: 40px">
        <span style="font-size: 25px; margin-left: 5px;">二手交易网</span>
      </div>
      <div style="height: calc(100vh - 50px); background-color:#f8f8f8; display: flex; align-items: center; justify-content: center;">
        <div style="width: 400px; padding: 40px; background-color:white; border-radius: 5px;">
          <div style = "display: flex; margin-bottom: 50px; font-size: 24px;">
            <a href="/login"><div style="margin-right: 10px; color: #333;">登录</div></a>
            <div style="border-bottom: 2px solid orangered;padding-bottom: 2px;">注册</div>
            
          </div>
            <el-form :model="form" :rules="rules" ref="formRef">
              <el-form-item prop="username">
                <el-input size="medium" prefix-icon="el-icon-user" placeholder="请输入账号" v-model="form.username"></el-input>
              </el-form-item>
            <el-form-item prop="password">
                <el-input size="medium" prefix-icon="el-icon-lock" placeholder="请输入密码" show-password  v-model="form.password"></el-input>
            </el-form-item>
            <el-form-item prop="confirmPassword">
                <el-input size="medium" prefix-icon="el-icon-lock" placeholder="请确认密码" show-password  v-model="form.confirmPassword"></el-input>
            </el-form-item>

        <el-form-item>
          <el-button size= "medium"style="width: 100%; background-color: orangered; border-color: #333; color: white" @click="login">注 册</el-button>
        </el-form-item> 
       </el-form>
    </div>
      </div>
  </div>

  </div>
</template>

<script>
export default {
  name: "Register",
  data() {
    const validatePassword = (rule, confirmPass,callback) => {
      if(confirmPass === ''){
        callback(new Error('请输入密码'))
      }else if(confirmPass !== this.form.password){
        callback(new Error('两次输入的密码不一致'))
      }else{
        callback()
      }
    }
    return {
      form: { role: 'USER' },
      rules: {
        username: [
          { required: true, message: '请输入账号', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ],
        confirmPassword: [
          { required: true, message: '请确认密码', trigger: 'blur' },
        ]
      }
    }
  },
  created() {

  },
  methods: {
    login() {
      this.$refs['formRef'].validate((valid) => {
        if (valid) {
          // 验证通过
          this.$request.post('/register', this.form).then(res => {
            if (res.code === '200') {
              this.$router.push('/login')  // 跳转主页
              this.$message.success('注册成功')
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.container {
  height: 100vh;
  overflow: hidden;
  display: flex;
}
a {
  color: #2a60c9;
}
</style>