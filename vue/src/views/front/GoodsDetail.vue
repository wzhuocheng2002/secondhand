<template>
    <div style="margin: 0 auto; padding: 10px 0; width: 50%">
      <div style="display: flex; grid-gap: 20px; margin-bottom: 40px">
        <img :src="goods.img" alt="" style="width: 50%; height: 400px; display: block">
        <div style="flex: 1;width: 0" >
          <el-tooltip :content="goods.name" placement="top-start">
            <div style="width: 100%; font-weight: bold; font-size: 26px; margin: 20px 0" class="line1">{{ goods.name }}</div>
          </el-tooltip>
          <div style="color: #666; font-size: 16px;">
            <span>浏览 {{ goods.readCount }}</span>
            <span style="margin-left: 20px">点赞 {{ goods.likesCount }}</span>
            <span style="margin-left: 20px">收藏 {{ goods.collectCount }}</span>
          </div>
          <div style="color: red; font-size: 24px; margin: 40px 0">￥{{ goods.price }}</div>
          <div style="margin-bottom: 20px"><span style="color: #666">发货地：</span> {{ goods.address }}</div>
          <div style="margin-bottom: 20px"><span style="color: #666">卖家：</span> {{ goods.userName }}</div>
          <div style="margin-bottom: 40px"><span style="color: #666">发布日期：</span> {{ goods.date }}</div>
          <div>
            <el-button v-if="!goods.userLikes" @click="addLikes()" size="medium" style="background-color: orangered; color: #eee; border-color: orangered">点赞</el-button>
            <el-button v-if="goods.userLikes" @click="addLikes()" size="medium" style="background-color: orangered; color: #eee; border-color: orangered">取消点赞</el-button>
            <el-button v-if="!goods.userCollect" @click="addCollect()" size="medium" style="background-color: orangered; color: #eee; border-color: orangered">收藏</el-button>
            <el-button v-if="goods.userCollect" @click="addCollect()" size="medium" style="background-color: orangered; color: #eee; border-color: orangered">取消收藏</el-button>
            <el-button @click="handleBuy()" size="medium" type="danger">立即购买</el-button>
          </div>
        </div>
      </div>
  
      <div>
        <div style="display: flex; border-bottom: 1px solid orangered; margin-bottom: 10px">
          <div style="padding: 10px 20px; cursor: pointer" :class="{ 'active' : current === '商品详情' }" @click="changeItem('商品详情')">商品详情</div>
          <div style="padding: 10px 20px; cursor: pointer" :class="{ 'active' : current === '商品评论' }" @click="changeItem('商品评论')">商品评论</div>

        </div>
  
        <div v-if="current === '商品详情'">
          <div v-html="goods.content"></div>
        </div>
  
        <div v-if="current === '商品评论'" class="card">
          <comment :fid="id" module="goods"/>
        </div>
        <el-dialog title="选择收货地址" :visible.sync="formVisible" width="30%" :close-on-click-modal="false" destroy-on-close>
          <div style="padding: 0 20px">
            <el-radio-group v-model="form.addressId">
              <el-radio v-for="item in addressList" :key="item.id" :label="item.id" style="margin-bottom: 10px">
                {{item.name + ' ' +item.address + ' ' + item.phone}}
              </el-radio>
            </el-radio-group>
            <a v-if="addressList.length === 0" href="/front/address" target="_blank"><div>还没有地址收货地址？去创建</div></a>
          </div>
          <div slot="footer" class="dialog-footer">
            <el-button @click="formVisible = false">取 消</el-button>
            <el-button type="primary" @click="addOrder">确 定</el-button>
          </div>
        </el-dialog>

      </div>
    </div>
  </template>
  
<script>
import Comment from"@/components/Comment";
export default{
    name:"GoodsDetail",
    components:{Comment},
    data(){
        return {
            id:this.$route.query.id,
            goods:{},
            current:'商品详情',
            form:{},
          formVisible:false,
          addressList:[]
        }
    },
    created(){
      //先更新浏览数，再加载商品数据
      this.$request.put('goods/updateReadCount/' + this.id).then(res => {
        this.load()
      })

      this.loadAddress()
    },
    methods:{
      addOrder(){
        if(!this.form.addressId){
          this.$message.warning('请选择收货地址')
          return
        }
        this.form.goodsId = this.id
        this.$request.post('/orders/add', this.form).then(res=>{
          if(res.code === '200'){
            this.$message.success('下单成功')
            this.$router.push('/front/orders')
          }else {
            this.$message.error(res.msg);
          }
        })
      },

      loadAddress(){
        this.$request.get('/address/selectAll').then(res=>{
          this.addressList=res.data || []
        })
      },
      handleBuy(){
        this.form = {}
        this.formVisible = true;
      },
      changeItem(current) {
        this.current = current
      },
      addCollect(){
        this.$request.post('collect/add', {fid: this.goods.id, module: "goods"}).then(res => {
          if(res.code ==='200'){
            this.$message.success('操作成功')
            this.load()
          }else{
            this.$message.error(res.msg)
          }
        })
      },
      addLikes(){
        this.$request.post('likes/add', {fid: this.goods.id, module: "goods"}).then(res => {
          if(res.code ==='200'){
            this.$message.success('操作成功')
            this.load()
          }else{
            this.$message.error(res.msg)
          }
        })
      },
        load() {

            this.$request.get('/goods/selectById/' + this.id).then(res => {
            this.goods = res.data || {}
              console.log(this.goods)
      })
    }
    }
}
</script>

<style scoped>
.active{
    background-color: orangered;
    color: #eee;
}
</style>
