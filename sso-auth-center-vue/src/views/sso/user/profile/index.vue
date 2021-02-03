<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="6" :xs="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>个人22信息</span>
          </div>
          <div>
            <div class="text-center">
              <userAvatar :user="user"/>
            </div>
            <ul class="list-group list-group-striped">
              <li class="list-group-item">
                登录名
                <div class="pull-right">{{ user.username }}</div>
              </li>
              <li class="list-group-item" v-if="user.nickName!==''">
                用户昵称
                <div class="pull-right">{{ user.nickName }}</div>
              </li>
              <li class="list-group-item" v-if="user.realName!==''">
                真实姓名
                <div class="pull-right">{{ user.realName }}</div>
              </li>
              <li class="list-group-item">
                手机号码
                <div class="pull-right">{{ user.phone }}</div>
              </li>
              <li class="list-group-item">
                邮箱
                <div class="pull-right">{{ user.email }}</div>
              </li>
              <li class="list-group-item" v-if="user.loginIp!=='' && user.loginIp!==null">
                最后登录IP
                <div class="pull-right">{{ user.loginIp }}</div>
              </li>
              <li class="list-group-item" v-if="user.lastLoginTime!=='' && user.lastLoginTime!==null">
                最后登录时间
                <div class="pull-right">{{ user.lastLoginTime }}</div>
              </li>
              <li class="list-group-item">
                创建者
                <div class="pull-right">{{ user.createBy }}</div>
              </li>
              <li class="list-group-item">
                创建日期
                <div class="pull-right">{{ user.createTime }}</div>
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="18" :xs="24">
        <el-card>
          <div slot="header" class="clearfix">
            <span>基本资料</span>
          </div>
          <el-tabs v-model="activeTab">
            <el-tab-pane label="基本资料" name="userinfo">
              <userInfo :user="user" :oldUserJson="oldUserJson"/>
            </el-tab-pane>
            <el-tab-pane label="修改密码" name="resetPwd">
              <resetPwd :user="user"/>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import userAvatar from './userAvatar'
import userInfo from './userInfo'
import resetPwd from './resetPwd'
import { getUserProfile } from '@/api/user'

export default {
  name: 'Profile',
  components: { userAvatar, userInfo, resetPwd },
  data() {
    return {
      user: {},
      oldUserJson: '',
      roleGroup: {},
      postGroup: {},
      activeTab: 'userinfo'
    }
  },
  created() {
    this.getUser()
  },
  methods: {
    getUser() {
      getUserProfile().then(response => {
        this.user = response.data
        this.oldUserJson = JSON.stringify(this.user)
        this.roleGroup = response.roleGroup
        this.postGroup = response.postGroup
      })
    }
  }
}
</script>

<style scoped>

.list-group {
  padding-left: 0px;
  list-style: none;
}

.list-group-item {
  border-bottom: 1px solid #e7eaec;
  margin-bottom: -1px;
  padding: 11px 0px;
  font-size: 13px;
}

.pull-right {
  float: right !important;
}
</style>
