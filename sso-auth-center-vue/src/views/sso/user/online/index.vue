<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" v-show="showSearch" :inline="true">
      <el-form-item label="登录名" prop="usernameLike">
        <el-input
            v-model="queryParams.usernameLike"
            placeholder="请输入登录名"
            clearable
            size="small"
            style="width: 180px"
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="登录时间">
        <el-date-picker v-model="loginDateRange" size="small" style="width: 240px" value-format="yyyy-MM-dd"
                        type="daterange" range-separator="-" start-placeholder="开始日期"
                        end-placeholder="结束日期"></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table border v-loading="loading" :data="pageList">
      <el-table-column fixed label="用户ID" align="center" prop="userId" width="100px" show-overflow-tooltip/>
      <el-table-column fixed label="登录名" align="center" prop="username" min-width="120px" show-overflow-tooltip/>
      <el-table-column label="用户昵称" align="center" prop="nickName" min-width="110px" show-overflow-tooltip/>
      <el-table-column label="真实姓名" align="center" prop="realName" min-width="100px" show-overflow-tooltip/>
      <el-table-column label="手机号码" align="center" prop="phone" min-width="120"/>
      <el-table-column label="登录时间" align="center" prop="loginTime" width="165" show-overflow-tooltip/>
      <el-table-column label="效期时间" align="center" prop="expireTime" width="165" show-overflow-tooltip/>
      <el-table-column label="来源IP" align="center" prop="sourceIp" min-width="150" show-overflow-tooltip/>
      <el-table-column label="IP地址" align="center" prop="sourceAddress" min-width="150" show-overflow-tooltip/>
      <el-table-column label="浏览器" align="center" prop="browserName" min-width="100" show-overflow-tooltip/>
      <el-table-column label="操作系统" align="center" prop="operateSystem" min-width="100" show-overflow-tooltip/>
     <el-table-column label="操作" fixed="right" align="center" width="180" class-name="small-padding fixed-width">
        <template slot-scope="scope" >
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleShowSysOnline(scope.row)"
                     v-hasPermission="['user:online:listOnlineSys']">已登子系统
          </el-button>

          <el-button size="mini" type="text" icon="el-icon-delete-solid" v-if="scope.row.supperAdminFlag!==true" @click="retreatUser(scope.row)"
                     v-hasPermission="['user:online:retreatUser']">强退
          </el-button>
        </template>
      </el-table-column>

    </el-table>

    <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryParams.page"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
    />

    <el-dialog title="已登录的子系统" :visible.sync="sysOnlineOpen">
      <el-table border v-loading="loading" :data="sysOnlineList" :show-overflow-tooltip="true">
        <el-table-column label="系统编码" align="center" prop="sysCode" min-width="120" show-overflow-tooltip/>
        <el-table-column label="系统名称" align="center" prop="sysName" min-width="120" show-overflow-tooltip/>
        <el-table-column label="登录时间" align="center" prop="loginTime" min-width="160" show-overflow-tooltip/>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import { listOnlineSys, listPageOnlineUser, retreatUser } from '@/api/user'

export default {
  name: 'UserOnlineIndex',
  inject: ['reloadLeftMenu'],
  data() {
    return {
      // 遮罩层
      loading: true,
      showSearch: true,
      sysOnlineOpen: false,
      // 总条数
      total: 0,
      //表格数据
      pageList: [],
      //子系统已登录列表
      sysOnlineList: [],
      // 登录时间日期范围
      loginDateRange: [],
      // 弹出层标题
      title: '',
      // 查询参数
      queryParams: {
        page: 1,
        pageSize: 5,
        usernameLike: null,
        loginStartTime: null,
        loginEndTime: null
      }
    }
  },
  created() {
    //刷新左侧菜单栏
    this.reloadLeftMenu()
    this.queryParams.sysCode = this.targetSysCode
    this.getList()
  },
  methods: {
    //查询角色列表
    getList() {
      this.loading = true
      this.buildTimeRange()
      listPageOnlineUser(this.queryParams).then(response => {
            this.pageList = response.data
            this.total = response.total
            this.loading = false
          }
      ).catch(function() {
        this.loading = false
      })
    },
    //处理时间参数
    buildTimeRange() {
      this.queryParams.loginStartTime = null
      this.queryParams.loginEndTime = null
      if (null != this.loginDateRange && '' !== this.loginDateRange) {
        this.queryParams.loginStartTime = this.loginDateRange[0]
        this.queryParams.loginEndTime = this.loginDateRange[1]
      }
    },
    //查看已登录子系统
    handleShowSysOnline(row) {
      this.loading = true
      listOnlineSys(row.userId).then(response => {
            this.sysOnlineList = response.data
            this.sysOnlineOpen = true
            this.loading = false
          }
      )
    },
    //强退用户
    retreatUser(row) {
      this.$confirm('是否确认将强制用户' + row.username + '退出所有系统?', '删除警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return retreatUser(row.userId)
      }).then(() => {
        this.getList()
        this.msgSuccess('强退成功')
      }).catch(function() {
      })
    },
    //搜索按钮操作
    handleQuery() {
      this.queryParams.page = 1
      this.getList()
    },
    //重置按钮操作
    resetQuery() {
      this.resetForm('queryForm')
      this.loginDateRange = []
      this.sysOnlineOpen = false
      this.handleQuery()
    }
  }
}
</script>

<style scoped>

.el-table__column :not(.is-hidden):last-child {
  right: -1px;
}


</style>

<style>

.el-table .warning-row {
  background: oldlace;
}

.el-table .success-row {
  background: #f0f9eb;
}

</style>
