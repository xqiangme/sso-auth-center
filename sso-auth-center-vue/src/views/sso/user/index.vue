<template>
  <div class="body-container">
    <el-breadcrumb separator="/">
      <el-breadcrumb-item>{{ targetSysName }}</el-breadcrumb-item>
      <el-breadcrumb-item>用户管理</el-breadcrumb-item>
    </el-breadcrumb>
    <div class="app-container">
      <!--部门数据-->
      <el-col :span="deptSpan" v-if="deptOptions.length>0">
        <div class="head-container">
          <el-input v-model="deptFilterName" placeholder="部门名称" clearable size="small" prefix-icon="el-icon-search"
                    style="margin-bottom: 20px"/>
        </div>
        <div class="head-container">
          <el-tree :data="deptOptions" :props="defaultProps" :expand-on-click-node="false"
                   :filter-node-method="filterDeptNode" ref="tree" default-expand-all @node-click="handleNodeClick"/>
        </div>
      </el-col>
      <!--用户数据-->
      <el-col :span="userSpan">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
          <el-row>
            <el-form-item label="登录名" prop="usernameLike">
              <el-input v-model="queryParams.usernameLike" placeholder="请输入用户登录名" clearable size="small"
                        style="width: 180px"
                        @keyup.enter.native="handleQuery"/>
            </el-form-item>
            <el-form-item label="手机号" prop="phoneLike">
              <el-input v-model="queryParams.phoneLike" placeholder="请输入手机号" clearable size="small"
                        style="width: 180px" @keyup.enter.native="handleQuery"/>
            </el-form-item>
            <el-form-item label="真实姓名" prop="realNameLike">
              <el-input v-model="queryParams.realNameLike" placeholder="请输入真实姓名" clearable size="small"
                        style="width: 180px"
                        @keyup.enter.native="handleQuery"/>
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item label="所属部门" prop="deptId" size="mini" v-if="deptOptions.length>0">
              <treeselect style="width: 180px;height: 20px" v-model="queryParams.deptId" :options="deptOptions"
                          :props="defaultProps"
                          size="small"
                          :normalizer="normalizer"
                          @change="handleQuery"
                          @keyup.enter.native="handleQuery"
                          placeholder="选择部门"/>
            </el-form-item>
            <el-form-item label="角色" prop="roleId">
              <el-select v-model="queryParams.roleId"
                         clearable
                         filterable
                         remote
                         reserve-keyword
                         size="small"
                         :remote-method="getRoleOption"
                         :loading="loading"
                         @change="handleQuery"
                         @keyup.enter.native="handleQuery"
                         placeholder="请选择角色">
                <el-option v-for="item in roleOptions" :key="item.roleId" :label="item.roleName"
                           :value="item.roleId"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="状态" prop="status">
              <el-select v-model="queryParams.status" placeholder="状态" @keyup.enter.native="handleQuery"
                         @change="handleQuery"
                         size="small" style="width: 90px">
                <el-option :key="-1" label="全部" :value="-1"/>
                <el-option :key="0" label="正常" :value="0"/>
                <el-option :key="1" label="停用" :value="1"/>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-row>
        </el-form>
        <el-row>
          <el-form>
            <el-form-item style="margin: 0">
              <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd"
                         v-hasPermission="['user:add']">新增
              </el-button>
              <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
                         v-hasPermission="['user:delete']">删除
              </el-button>
            </el-form-item>
          </el-form>
        </el-row>

        <el-table border v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
          <el-table-column fixed type="selection" width="40" align="center" :selectable='selectInit'/>
          <el-table-column fixed label="用户ID" align="center" prop="userId" min-width="100px" show-overflow-tooltip/>
          <el-table-column fixed label="登录名" align="center" prop="username" min-width="120px" show-overflow-tooltip/>
          <el-table-column label="用户昵称" align="center" prop="nickName" min-width="120px" show-overflow-tooltip/>
          <el-table-column label="真实姓名" align="center" prop="realName" min-width="120px" show-overflow-tooltip/>
          <el-table-column prop="status" label="状态" align="center" width="58" show-overflow-tooltip>
            <template slot-scope="scope">
              <span v-if="scope.row.status === 0"> <el-tag type="success" size="mini">启用</el-tag></span>
              <span v-if="scope.row.status === 1"><el-tag type="danger" size="mini">停用</el-tag></span>
            </template>
          </el-table-column>
          <el-table-column label="手机号码" align="center" prop="phone" min-width="120"/>
          <el-table-column label="角色" align="center" prop="roleNames" min-width="130" show-overflow-tooltip/>
          <el-table-column v-if="deptOptions.length>0"
                           label="部门" align="center" prop="deptName" min-width="120" show-overflow-tooltip/>
          <el-table-column label="创建者" align="center" prop="createBy" min-width="120" show-overflow-tooltip/>
          <el-table-column label="创建时间" align="center" prop="createTime" min-width="165"/>
          <el-table-column label="操作" fixed="right" align="center" width="230" class-name="small-padding fixed-width">
            <template slot-scope="scope" v-if="scope.row.supperAdminFlag!==true">
              <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                         v-hasPermission="['user:update']"
              >修改
              </el-button>
              <el-button v-if="scope.row.userId !== 1" size="mini" type="text" icon="el-icon-key"
                         @click="handleResetPwd(scope.row)" v-hasPermission="['user:resetPwd']"
              >重置
              </el-button>
              <el-button v-if="scope.row.sysBindFlag" size="mini" type="text" icon="el-icon-delete"
                         @click="handleRemoveUserSystem(scope.row)" v-hasPermission="['user:removeUserSystem']">移除
              </el-button>
              <el-button v-if="scope.row.userId !== 1" size="mini" type="text" icon="el-icon-delete"
                         @click="handleDelete(scope.row)" v-hasPermission="['user:delete']">删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="total>0"
                    :total="total"
                    :page.sync="queryParams.page"
                    :limit.sync="queryParams.pageSize"
                    @pagination="getList"/>
      </el-col>


      <!-- 添加或修改参数配置对话框 -->
      <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
        <el-form ref="form" :model="form" :rules="rules" label-width="80px">
          <el-row>
            <el-col :span="12">
              <el-form-item v-if="form.userId === undefined" label="登录名" prop="username">
                <el-input v-model="form.username" @change="usernameChange" placeholder="请输入用户登录名"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item v-if="form.userId === undefined" label="登录密码" prop="password">
                <el-input v-model="form.password" placeholder="请输入用户密码" :type="pwdInputType" style="width: 200px;">
                  <i slot="suffix" :class="pwdInputIcon" @click="showPass"></i>
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="用户昵称" prop="nickName">
                <el-input v-model="form.nickName" placeholder="请输入用户昵称"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="真实姓名" prop="realName">
                <el-input v-model="form.realName" placeholder="请输入真实姓名"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="手机号码" prop="phone">
                <el-input v-model="form.phone" @change="phoneChange" placeholder="请输入手机号码" maxlength="11"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="邮箱" prop="email">
                <el-input v-model="form.email" placeholder="请输入邮箱" maxlength="50"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="用户性别">
                <el-radio-group v-model="form.sex">
                  <el-radio :label="0">不详</el-radio>
                  <el-radio :label="1">男</el-radio>
                  <el-radio :label="2">女</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="状态">
                <el-radio-group v-model="form.status">
                  <el-radio :label="0">正常</el-radio>
                  <el-radio :label="1">停用</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="所属部门" prop="deptId">
                <treeselect style="width: 180px;height: 20px" v-model="form.deptId" :options="deptOptions"
                            :props="defaultProps"
                            :normalizer="normalizer"
                            noOptionsText="无部门信息,请先添加部门"
                            placeholder="选择部门"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="角色">
                <el-select v-model="form.roleIdList" multiple
                           filterable
                           remote
                           reserve-keyword
                           :remote-method="getRoleOption"
                           :loading="loading"
                           placeholder="请选择">
                  <el-option v-for="item in roleOptions" :key="item.roleId" :label="item.roleName" :value="item.roleId"
                             :disabled="item.status === 1"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="备注">
                <el-input v-model="form.remarks" type="textarea" placeholder="请输入内容"></el-input>
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
  </div>
</template>

<script>
import {
  addUser,
  changeUserStatus,
  delUser,
  getDeptDetail,
  getUserByPhone,
  getUserByUserName,
  listUser,
  removeUserSystem,
  resetUserPwd,
  updateUser
} from '@/api/user'
import { listDeptOptionTree } from '@/api/dept'
import { listRoleOption } from '@/api/role'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

export default {
  name: 'User',
  components: { Treeselect },
  inject: ['reloadLeftMenu'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 用户表格数据
      userList: null,
      // 弹出层标题
      title: '',
      targetSysCode: '',
      targetSysName: '',
      deptSpan: 3,
      userSpan: 21,
      // 部门树选项
      deptOptions: [],
      // 是否显示弹出层
      open: false,
      // 部门树名称检索
      deptFilterName: '',
      // 默认密码
      initPassword: 'Aa123456',
      // 状态数据字典
      statusOptions: [],
      // 性别状态字典
      sexOptions: [],
      // 岗位选项
      postOptions: [],
      // 角色选项
      roleOptions: [],
      // 表单参数
      form: {},
      // 表单JSON内容
      oldFormJson: '',
      defaultProps: {
        children: 'children',
        label: 'deptName'
      },
      //用于改变Input类型
      pwdInputType: 'password',
      //用于更换Input中的图标
      pwdInputIcon: 'el-input__icon el-icon-view',
      // 查询参数
      queryParams: {
        sysCode: null,
        page: 1,
        pageSize: 5,
        usernameLike: null,
        phoneLike: null,
        realNameLike: null,
        nickNameLike: null,
        status: -1,
        deptId: null,
        roleId: null
      },
      //查询详情参数
      queryDetailParams: {
        sysCode: null,
        userId: null
      },
      // 表单校验
      rules: {
        username: [
          { required: true, message: '用户登录名不能为空', trigger: 'blur' },
          { min: 3, max: 30, message: '长度在 3 到 30 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '用户密码不能为空', trigger: 'blur' },
          { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
        ],
        nickName: [{ max: 200, message: '长度不超过 30 个字符', trigger: 'blur' }],
        realName: [{ max: 200, message: '长度不超过 30 个字符', trigger: 'blur' }],
        email: [
          {
            type: 'email',
            message: '\'请输入正确的邮箱地址',
            trigger: ['blur', 'change']
          },
          { max: 200, message: '长度不超过 200 个字符', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '手机号不能为空', trigger: 'blur' },
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: '请输入正确的手机号码',
            trigger: 'blur'
          }
        ],
        remarks: [
          { max: 200, message: '最大长度 200 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.targetSysCode = sessionStorage.getItem('targetSysCode')
    if (this.targetSysCode == null || '' === this.targetSysCode) {
      this.$router.push({ path: '/platformMgmt/index' })
      return
    }
    //刷新左侧菜单栏
    this.reloadLeftMenu()
    this.targetSysName = sessionStorage.getItem('targetSysName')
    this.queryParams.sysCode = this.targetSysCode
    this.queryDetailParams.sysCode = this.targetSysCode
    this.getList()
    this.getTreeselect()
    this.getRoleOption()
  },
  watch: {
    deptFilterName(val) {
      this.$refs.tree.filter(val)
    }
  },
  methods: {
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
    //查询部门下拉树结构
    getTreeselect() {
      listDeptOptionTree({ 'sysCode': this.queryParams.sysCode }).then((response) => {
        this.deptOptions = response.data
        this.restDeptSpanSize()
      })
    },
    //重置Span
    restDeptSpanSize() {
      if (this.deptOptions.length === 0) {
        this.deptSpan = 4
        this.userSpan = 24
      }
    },
    // 部分树-筛选筛选节点
    filterDeptNode(value, data) {
      if (!value) return true
      return data.deptName.indexOf(value) !== -1
    },
    // 节点单击事件
    handleNodeClick(data) {
      this.queryParams.deptId = data.deptId
      this.getList()
    },
    // 用户状态修改
    handleStatusChange(row) {
      let text = row.status === '0' ? '启用' : '停用'
      this.$confirm(
          '确认要"' + text + '""' + row.userName + '"用户吗?',
          '警告',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
      )
          .then(function() {
            return changeUserStatus(row.userId, row.status)
          })
          .then(() => {
            this.msgSuccess(text + '成功')
          })
          .catch(function() {
            row.status = row.status === '0' ? '1' : '0'
          })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        sysCode: this.targetSysCode,
        userId: undefined,
        deptId: undefined,
        username: undefined,
        nickName: undefined,
        realName: undefined,
        password: undefined,
        phone: undefined,
        email: undefined,
        sex: 0,
        status: 0,
        roleIdList: [],
        remarks: undefined
      }
      this.resetForm('form')
    },
    //搜索按钮操作
    handleQuery() {
      this.queryParams.page = 1
      this.getList()
    },
    //重置
    resetQuery() {
      this.resetForm('queryForm')
      this.oldFormJson = ''
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.userId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    //新增按钮操作
    handleAdd() {
      this.reset()
      this.getTreeselect()
      this.getRoleOption()
      this.open = true
      this.title = '添加用户'
      this.form.password = this.initPassword
    },
    //修改按钮操作
    handleUpdate(row) {
      this.reset()
      this.getTreeselect()
      this.getRoleOption()
      this.queryDetailParams.userId = row.userId || this.ids
      getDeptDetail(this.queryDetailParams).then((response) => {
        this.form = response.data
        this.form.password = ''
        this.oldFormJson = JSON.stringify(this.form)
        this.open = true
        this.title = '修改用户'
        this.form.password = ''
      })
    },
    handleUpdateByUserId(userId) {
      this.reset()
      this.getTreeselect()
      this.getRoleOption()
      this.queryDetailParams.userId = userId
      getDeptDetail(this.queryDetailParams).then((response) => {
        this.form = response.data
        this.open = true
        this.title = '修改用户'
        this.form.password = ''
      })
    },
    getRoleOption(query) {
      this.loading = true
      const roleOptionReq = { 'sysCode': this.targetSysCode, 'pageSize': 30, 'roleNameLike': query }
      listRoleOption(roleOptionReq).then((response) => {
        this.roleOptions = response.data
        this.loading = false
      })
    },
    usernameChange() {
      if (null == this.form.username || '' === this.form.username) {
        return
      }
      if (this.form.username === 'admin') {
        this.$confirm('超级管理员账号已经存在,请更换用户名?',
            '警告',
            {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            })
            .then(function() {
            })
            .then(() => {
            }).catch(function() {
        })
        return
      }
      getUserByUserName(this.form.username).then((response) => {
        if (null == response.data) {
          return
        }
        this.$confirm('检测到用户名为"' + this.form.username + '"用户已经存在!是否切换到修改用户?',
            '警告',
            {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            })
            .then(function() {
            })
            .then(() => {
              this.open = false
              return this.handleUpdateByUserId(response.data.userId)
            }).catch(function() {
        })
      })
    },
    phoneChange() {
      if (null == this.form.phone || '' === this.form.phone) {
        return
      }
      getUserByPhone(this.form.phone).then((response) => {
        if (null == response.data) {
          return
        }
        this.$confirm('检测到手机号为"' + this.form.phone + '"用户已经存在!是否切换到修改用户?',
            '警告',
            {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            })
            .then(function() {
            })
            .then(() => {
              this.open = false
              return this.handleUpdateByUserId(response.data.userId)
            }).catch(function() {
        })
      })
    },
    //重置密码按钮操作
    handleResetPwd(row) {
      this.$prompt('请输入"' + row.username + '"的新密码', '重置密码', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({ value }) => {
        if (null == value || value === '') {
          this.msgError('重置密码失败,新密码不能为空')
          return
        }
        resetUserPwd(row.userId, value).then((response) => {
          if (response.code === 200) {
            this.msgSuccess('修改成功，新密码是：' + value)
          }
        })
      }).catch(() => {
      })
    },
    //提交按钮
    submitForm: function() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (this.form.userId !== undefined) {
            const newFormJson = JSON.stringify(this.form)
            if (this.oldFormJson === newFormJson) {
              this.msgError('您还未修改过')
              return
            }
            updateUser(this.form).then((response) => {
              if (response.code === 200) {
                this.msgSuccess('修改成功')
                this.open = false
                this.getList()
              }
            })
          } else {
            addUser(this.form).then((response) => {
              if (response.code === 200) {
                this.msgSuccess('新增成功')
                this.open = false
                this.getList()
              }
            })
          }
          //用于改变Input类型
          this.pwdInputType = 'password'
          //用于更换Input中的图标
          this.pwdInputIcon = 'el-input__icon el-icon-view'
        }
      })
    },
    //移除用户系统关系操作
    handleRemoveUserSystem(row) {
      const deleteParam = { 'sysCode': this.targetSysCode, 'userId': row.userId }
      this.$confirm(
          '是否确认移除，将移除与改系统的所有绑定关系',
          '警告',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
      ).then(function() {
        return removeUserSystem(deleteParam)
      })
          .then(() => {
            this.getList()
            this.msgSuccess('移除成功')
          })
          .catch(function() {
          })
    },
    //删除按钮操作
    handleDelete(row) {
      const userIdList = []
      if (null != row.userId) {
        userIdList.push(row.userId)
      }
      for (let i = 0; i < this.ids.length; i++) {
        userIdList.push(this.ids[i])
      }
      const deleteParam = { 'sysCode': this.targetSysCode, 'userIdList': userIdList }
      this.$confirm(
          '是否确认删除用户编号为"' + userIdList + '"的数据项?',
          '警告',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
      ).then(function() {
        return delUser(deleteParam)
      })
          .then(() => {
            this.getList()
            this.msgSuccess('删除成功')
          })
          .catch(function() {
          })
    },
    //转换部门数据结构
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children
      }
      return {
        id: node.deptId,
        label: node.deptName,
        children: node.children
      }
    },
    //密码的隐藏和显示
    showPass() {
      //点击图标是密码隐藏或显示
      if (this.pwdInputType === 'text') {
        this.pwdInputType = 'password'
        //更换图标
        this.pwdInputIcon = 'el-input__icon el-icon-view'
      } else {
        this.pwdInputType = 'text'
        this.pwdInputIcon = 'el-input__icon el-icon-loading'
      }
    },
    selectInit(row, index) {
      //超级管理员不可勾选
      return !row.supperAdminFlag
    }

  }
}
</script>
