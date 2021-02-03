<template>
  <div class="app-container">
    <el-row v-if="platformList.length>0">
      <el-col :span="20">
        <el-button type="primary" icon="el-icon-sort" size="mini" style="margin-left: 15px" @click="handleSort">排序
        </el-button>
      </el-col>
    </el-row>
    <el-row>
      <el-col v-for="(item, index) in platformList" :key="item.id" :span="-8" :offset="index > 0 ? 2 : 0"
              class="bodyClass">
        <el-card :body-style="{ padding: '0px' }" class="elCardClass">
          <img
              :src="item.sysIcon"
              class="image"
              alt=""
              @click="clickPlatform(item)">
          <div style="padding: 15px;" @click="clickPlatform(item)">
            <span>{{ item.sysName }} <i style="float: right;border: none" class="el-icon-s-promotion"></i></span>
            <span></span>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row v-if="platformList.length<=0">
      <p style="text-align: center"> 您还没有平台,联系管理员添加吧</p>
    </el-row>
  </div>
</template>

<script>
import { platformMyList } from '@/api/system'
import { getToken } from '@/utils/token'
import Sortable from 'sortablejs'
import { isExternal } from '@/utils/validate'

export default {
  inject: ['reloadLeftMenu'],
  components: { Sortable },
  data() {
    return {
      list: [],
      sortOpen: true,
      //遮罩层
      loading: true,
      //查询参数
      queryParams: {},
      platformList: []
    }
  },
  created() {
    const token = getToken()
    //若存在跳转地址直接跳转
    let redirectUrl = this.$route.query.redirectUrl
    if (null != token && null !== redirectUrl && '' !== redirectUrl && redirectUrl !== undefined) {
      window.location.href = redirectUrl + '?ssoToken=' + token
    }

    //刷新左侧菜单栏
    this.reloadLeftMenu()
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      this.platformList = []
      platformMyList(this.queryParams).then((response) => {
            for (let key in response.data) {
              const itemDate = response.data[key]
              //处理图片地址
              if (null == itemDate.sysIcon || '' === itemDate.sysIcon) {
                itemDate.sysIcon = require('@/assets/image/default-system.jpg')
              } else {
                //系统图标-如果非外链则添加前缀处理
                if (!isExternal(itemDate.sysIcon)) {
                  itemDate.sysIcon = process.env.VUE_APP_BASE_API + itemDate.sysIcon
                }
              }
              this.platformList.push(itemDate)
            }
            this.loading = false
          }
      )
    },
    clickPlatform(item) {
      if (null !== item.sysUrl && '' !== item.sysUrl && item.sysUrl !== undefined) {
        window.open(item.sysUrl + '?ssoToken=' + getToken())
      } else {
        this.msgError('目标系统跳转链接为空,请联系管理员配置')
      }
    },
    handleSort() {
      this.$router.push({ path: '/platformSort/index' })
    },
    // 取消按钮
    cancel() {
      this.openAddForm = false
      this.reset()
    }
  }
}
</script>


<style scoped>

.bodyClass {
  padding: 0px;
  width: 245px;
  height: 220px;
  margin-top: 15px;
  margin-left: 15px;
  margin-right: 15px;
  cursor: pointer;
}

.bodyClass:hover {
  transform: translateY(-10px);
  transition: transform 0.2s;
}

.image {
  width: 100%;
  height: 150px;
  display: block;
}

</style>
