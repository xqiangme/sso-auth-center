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
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="状态" @keyup.enter.native="handleQuery" @change="handleQuery"
                   size="small" style="width: 90px">
          <el-option :key="-1" label="全部" :value="-1"/>
          <el-option :key="0" label="成功" :value="0"/>
          <el-option :key="1" label="失败" :value="1"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table border v-loading="loading" :data="logList" :row-class-name="tableRowClassName">
      <el-table-column label="用户登录名" align="center" prop="username" min-width="120" show-overflow-tooltip/>
      <el-table-column label="登录时间" align="center" prop="loginTime" width="165" show-overflow-tooltip/>
      <el-table-column prop="status" label="状态" align="center" width="80" show-overflow-tooltip>
        <template slot-scope="scope">
          <span v-if="scope.row.status === 0"> <el-tag type="success" size="mini">成功</el-tag></span>
          <span v-if="scope.row.status === 1"><el-tag type="danger" size="mini">失败</el-tag></span>
        </template>
      </el-table-column>
      <el-table-column label="浏览器" align="center" prop="browserName" min-width="100" show-overflow-tooltip/>
      <el-table-column label="操作系统" align="center" prop="operateSystem" min-width="100" show-overflow-tooltip/>
      <el-table-column label="来源IP" align="center" prop="sourceIp" min-width="150" show-overflow-tooltip/>
      <el-table-column label="IP地址" align="center" prop="sourceAddress" min-width="150" show-overflow-tooltip/>
      <el-table-column label="失败原因" align="center" prop="operateMsg" min-width="160" show-overflow-tooltip/>
    </el-table>

    <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryParams.page"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
    />
  </div>
</template>

<script>
import { listLoginLog } from '@/api/loginlog'

export default {
  name: 'LonIndex',
  inject: ['reloadLeftMenu'],
  data() {
    return {
      // 遮罩层
      loading: true,
      showSearch: true,
      // 总条数
      total: 0,
      // 表格数据
      logList: [],
      // 登录时间日期范围
      loginDateRange: [],
      // 弹出层标题
      title: '',
      // 查询参数
      queryParams: {
        page: 1,
        pageSize: 5,
        sysCode: null,
        usernameLike: null,
        status: -1,
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
      this.buildTimeRange();
      listLoginLog(this.queryParams).then(response => {
            this.logList = response.data
            this.total = response.total
            this.loading = false
          }
      )
    },
    tableRowClassName({ row, rowIndex }) {
      if (row.status === 1) {
        return 'warning-row'
      }
      return ''
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
    //搜索按钮操作
    handleQuery() {
      this.queryParams.page = 1
      this.getList()
    },
    //重置按钮操作
    resetQuery() {
      this.resetForm('queryForm')
      this.loginDateRange = []
      this.expireDateRange = []
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
