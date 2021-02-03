<template>
  <div class="app-container">
    <el-row :gutter="50">
      <el-col :span="6" :xs="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>角色信息</span>
          </div>
          <div>
            <ul class="list-group list-group-striped">
              <li class="list-group-item">
                <svg-icon icon-class="roleName"/>
                角色ID
                <div class="pull-right">{{ roleForm.roleId }}</div>
              </li>
              <li class="list-group-item" v-if="roleForm.roleName !==''">
                <svg-icon icon-class="roleName"/>
                角色名称
                <div class="pull-right">{{ roleForm.roleName }}</div>
              </li>
              <li class="list-group-item" v-if="roleForm.roleKey!==''">
                <svg-icon icon-class="roleKey"/>
                角色key
                <div class="pull-right">{{ roleForm.roleKey }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="status"/>
                状态
                <div class="pull-right">
                  <span v-if="roleForm.status === 0"> <el-tag type="success" size="mini">启用</el-tag></span>
                  <span v-if="roleForm.status === 1"><el-tag type="danger" size="mini">停用</el-tag></span>
                </div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="createBy"/>
                创建者
                <div class="pull-right">{{ roleForm.createBy }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="createTime"/>
                创建时间
                <div class="pull-right">{{ roleForm.createTime }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="createBy"/>
                修改者
                <div class="pull-right">{{ roleForm.updateBy }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="createTime"/>
                修改时间
                <div class="pull-right">{{ roleForm.updateTime }}</div>
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="18" :xs="24">
        <el-card>
          <div slot="header" class="clearfix">
            <el-button icon="el-icon-back" size="mini" @click="backRole">返回</el-button>
            <span>绑定该角色的用户列表</span>
          </div>
          <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
            <el-form-item label="用户名称" prop="usernameLike">
              <el-input v-model="queryParams.usernameLike" placeholder="请输入用户名称" clearable size="small"
                        style="width: 180px"
                        @keyup.enter.native="handleQuery"/>
            </el-form-item>
            <el-form-item label="手机号码" prop="phoneLike">
              <el-input v-model="queryParams.phoneLike" placeholder="请输入手机号码" clearable size="small"
                        style="width: 180px" @keyup.enter.native="handleQuery"/>
            </el-form-item>
            <el-form-item label="真实姓名" prop="realNameLike">
              <el-input v-model="queryParams.realNameLike" placeholder="请输入真实姓名" clearable size="small"
                        style="width: 180px"
                        @keyup.enter.native="handleQuery"/>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>

          <el-table border v-loading="loading" :data="userList">
            <el-table-column fixed label="用户ID" align="center" prop="userId" width="100px" show-overflow-tooltip/>
            <el-table-column fixed label="用户名称" align="center" prop="username" show-overflow-tooltip/>
            <el-table-column label="用户昵称" align="center" prop="nickName" show-overflow-tooltip/>
            <el-table-column prop="status" label="状态" align="center" width="60" show-overflow-tooltip>
              <template slot-scope="scope">
                <span v-if="scope.row.status === 0"> <el-tag type="success" size="mini">启用</el-tag></span>
                <span v-if="scope.row.status === 1"><el-tag type="danger" size="mini">停用</el-tag></span>
              </template>
            </el-table-column>
            <el-table-column label="手机号码" align="center" prop="phone" width="120" show-overflow-tooltip/>
            <el-table-column label="角色" align="center" prop="roleNames" width="130" show-overflow-tooltip/>
            <el-table-column label="操作" fixed="right" align="center" width="200" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button v-if="scope.row.userId !== 1" size="mini" type="text" icon="el-icon-delete"
                           @click="removeUserRole(scope.row)" v-hasPermission="['role:removeUser']">移除用户
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <pagination v-show="total>0"
                      :total="total"
                      :page.sync="queryParams.page"
                      :limit.sync="queryParams.pageSize"
                      @pagination="getList"/>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listUser } from '@/api/user'
import { getRoleDetail, removeUserRole } from '@/api/role'
import { updateDept } from '@/api/dept'

export default {
  name: 'userRoleIndex',
  data() {
    return {
      // 遮罩层
      loading: true,
      roleId: this.$route.query.roleId,
      targetSysCode: '',
      targetSysName: '',
      // 总条数
      total: 0,
      //用户表格数据
      userList: null,
      //查询参数
      queryParams: {
        sysCode: null,
        page: 1,
        pageSize: 5,
        usernameLike: null,
        phoneLike: null,
        realNameLike: null,
        nickNameLike: null,
        status: -1,
        roleId: this.$route.query.roleId
      },
      roleForm: {
        roleId: null,
        roleName: null,
        roleKey: null,
        status: 0,
        createBy: '',
        createTime: '',
        updateBy: '',
        updateTime: ''
      },
      roleGroup: {},
      postGroup: {},
      activeTab: 'userinfo'
    }
  },
  created() {
    this.targetSysCode = sessionStorage.getItem('targetSysCode')
    if (this.targetSysCode == null || '' === this.targetSysCode) {
      this.$router.push({ path: '/platformMgmt/index' })
      return
    }
    this.targetSysName = sessionStorage.getItem('targetSysName')
    this.queryParams.sysCode = this.targetSysCode
    this.getRoleDetail()
    this.getList()
  },
  methods: {
    getRoleDetail() {
      //若参数为空跳转回角色管理页面
      if (null == this.roleId || '' === this.roleId) {
        this.$router.push({ path: '/role/index' })
        return
      }
      getRoleDetail(this.roleId).then(response => {
        this.roleForm = response.data
      })
    },
    //查询用户列表
    getList() {
      this.loading = true
      listUser(this.queryParams).then(
          (response) => {
            this.userList = response.data
            this.total = response.total
            this.loading = false
          }
      )
    },
    //移除用户角色绑定关系
    removeUserRole(row) {
      this.loading = true
      removeUserRole(this.roleId, row.userId).then(response => {
        if (response.code === 200) {
          this.msgSuccess('移除成功')
          this.loading = false
          this.getList()
        }
      })
    },
    //搜索
    handleQuery() {
      this.queryParams.page = 1
      this.getList()
    },
    //重置
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    //返回角色列表
    backRole() {
      this.$router.push({
        path: '/role/index'
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
