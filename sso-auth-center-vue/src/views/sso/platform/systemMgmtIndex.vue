<template>
  <div class="app-container">
    <el-row :gutter="50">
      <el-col :span="6" :xs="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>平台信息</span>
          </div>
          <div>
            <ul class="list-group list-group-striped">
              <li class="list-group-item">
                <svg-icon icon-class="sysCode"/>
                系统编码
                <div class="pull-right">{{ detailData.sysCode }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="sysName"/>
                平台名称
                <div class="pull-right">{{ detailData.sysName }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="status"/>
                状态
                <div class="pull-right">
                  <span v-if="detailData.status === 0"> <el-tag type="success" size="mini">启用</el-tag></span>
                  <span v-if="detailData.status === 1"><el-tag type="danger" size="mini">停用</el-tag></span>
                </div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="createBy"/>
                创建者
                <div class="pull-right">{{ detailData.createBy }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="createTime"/>
                创建时间
                <div class="pull-right">{{ detailData.createTime }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="createBy"/>
                修改者
                <div class="pull-right">{{ detailData.updateBy }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="createTime"/>
                修改时间
                <div class="pull-right">{{ detailData.updateTime }}</div>
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="18" :xs="24">
        <el-card>
          <div slot="header" class="clearfix">
            <el-button icon="el-icon-back" size="small" @click="backRole">返回</el-button>
            <span>{{ detailData.sysName }} 管理员列表</span>
          </div>
          <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
            <el-form-item label="" prop="usernameLike">
              <el-input v-model="queryParams.usernameLike" placeholder="请输入用户名称" clearable size="small"
                        style="width: 180px"
                        @keyup.enter.native="handleQuery"/>
            </el-form-item>
            <el-form-item label="" prop="phoneLike">
              <el-input v-model="queryParams.phoneLike" placeholder="请输入手机号码" clearable size="small"
                        style="width: 180px" @keyup.enter.native="handleQuery"/>
            </el-form-item>
            <el-form-item label="" prop="realNameLike">
              <el-input v-model="queryParams.realNameLike" placeholder="请输入真实姓名" clearable size="small"
                        style="width: 180px"
                        @keyup.enter.native="handleQuery"/>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>

          <el-row>
            <el-form>
              <el-form-item style="margin: 0">
                <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd"
                           v-hasPermission="['systemMgmt:updateSystemMgmt']">新增管理员
                </el-button>
              </el-form-item>
            </el-form>
          </el-row>

          <el-table border v-loading="loading" :data="userList">
            <el-table-column fixed label="用户ID" align="center" prop="userId" min-width="100" show-overflow-tooltip/>
            <el-table-column fixed label="用户名称" align="center" prop="username" min-width="120" how-overflow-tooltip/>
            <el-table-column label="用户昵称" align="center" prop="nickName" min-width="120" show-overflow-tooltip/>
            <el-table-column label="手机号码" align="center" prop="phone" min-width="120" show-overflow-tooltip/>
            <el-table-column prop="relationStatus" label="管理员状态" align="center" width="100" show-overflow-tooltip>
              <template slot-scope="scope">
                <span v-if="scope.row.relationStatus === 0"> <el-tag type="success" size="mini">启用</el-tag></span>
                <span v-if="scope.row.relationStatus === 1"><el-tag type="danger" size="mini">停用</el-tag></span>
              </template>
            </el-table-column>
            <el-table-column label="创建者" align="center" prop="createBy" min-width="120" show-overflow-tooltip/>
            <el-table-column label="创建时间" align="center" prop="createTime" min-width="165" show-overflow-tooltip/>
            <el-table-column label="操作" fixed="right" align="center" width="150" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button v-if="scope.row.relationStatus ===1" size="mini" type="text" icon="el-icon-open"
                           @click="enableStatus(scope.row)">启用
                </el-button>
                <el-button v-if="scope.row.relationStatus ===0" size="mini" type="text" icon="el-icon-turn-off"
                           @click="disableStatus(scope.row)">停用
                </el-button>
                <el-button size="mini" type="text" icon="el-icon-delete"
                           @click="removeUserRole(scope.row)">移除用户
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

    <!-- 添加-->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="addForm" :model="addForm" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="用户" prop="userId">
              <el-select
                  v-model="addForm.userId"
                  filterable
                  remote
                  reserve-keyword
                  placeholder="请选择用户"
                  :remote-method="getUserOption"
                  :loading="loading">
                <el-option
                    v-for="item in userOptionList"
                    :key="item.userId"
                    :label="item.username+'-'+ item.realName+ '('+ item.phone+')' "
                    :value="item.userId">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="addForm.status">
                <el-radio :label="0">正常</el-radio>
                <el-radio :label="1">停用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getDetailBySysCode } from '@/api/system'
import { listUserOption } from '@/api/user'
import { listPageSystemMgmt, addSystemMgmt, updateSystemMgmtStatus, removeUserSystemMgmt } from '@/api/systemMgmt'
import { delRole } from '@/api/role'

export default {
  name: 'userRoleIndex',
  data() {
    return {
      // 遮罩层
      loading: true,
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      targetSysCode: this.$route.query.sysCode,
      targetSysName: '',
      detailData: {},
      userOptionList: [],
      //总条数
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
        nickNameLike: null
      },
      //表单参数
      addForm: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: '请先选择要添加的用户', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '状态不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    if (this.targetSysCode == null || '' === this.targetSysCode) {
      this.targetSysCode = sessionStorage.getItem('targetSysCode')
    }
    if (this.targetSysCode == null || '' === this.targetSysCode) {
      this.$router.push({ path: '/platformMgmt/index' })
      return
    }
    this.targetSysName = sessionStorage.getItem('targetSysName')
    this.queryParams.sysCode = this.targetSysCode
    this.getDetail()
    this.getList()
  },
  methods: {
    getDetail() {
      //若参数为空跳转回角色管理页面
      getDetailBySysCode(this.targetSysCode).then((response) => {
            this.detailData = response.data
          }
      )
    },
    //查询用户列表
    getList() {
      this.loading = true
      listPageSystemMgmt(this.queryParams).then(
          (response) => {
            this.userList = response.data
            this.total = response.total
            this.loading = false
          }
      )
    },
    //用户下拉选项
    getUserOption(query) {
      this.loading = true
      listUserOption(query).then((response) => {
        this.userOptionList = response.data
        this.loading = false
      })
    },
    //新增按钮操作
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加管理员账号'
      this.getUserOption()
    },
    //启用
    enableStatus(row) {
      const status = 0
      updateSystemMgmtStatus(row.id, status).then(response => {
        if (response.code === 200) {
          this.msgSuccess('操作成功')
          this.getList()
        }
      })
    },
    //停用
    disableStatus(row) {
      const status = 1
      this.$confirm('是否停用名称为"' + row.username + '"的管理员权限?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return updateSystemMgmtStatus(row.id, status)
      }).then(() => {
        this.getList()
        this.msgSuccess('操作成功')
      }).catch(function() {
      })
    },
    //提交按钮
    submitForm: function() {
      this.$refs['addForm'].validate(valid => {
        if (valid) {
          addSystemMgmt(this.addForm).then(response => {
            if (response.code === 200) {
              this.msgSuccess('新增成功')
              this.open = false
              this.getList()
            }
          })
        }
      })
    },
    //移除用户-该系统管理员绑定关系
    removeUserRole(row) {
      this.loading = true
      removeUserSystemMgmt(this.targetSysCode, row.userId).then(response => {
        if (response.code === 200) {
          this.msgSuccess('移除成功')
          this.loading = false
          this.getList()
        }
      })
    },
    //取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    //表单重置
    reset() {
      this.addForm = {
        sysCode: this.targetSysCode,
        userId: null,
        status: 0
      }
      this.resetForm('addForm')
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
    //返回平台管理详情
    backRole() {
      this.$router.push({
        path: '/platformMgmt/platformDetail'
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
