<template>
  <div class="body-container">
    <el-breadcrumb separator="/">
      <el-breadcrumb-item>{{ targetSysName }}</el-breadcrumb-item>
      <el-breadcrumb-item>角色管理</el-breadcrumb-item>
    </el-breadcrumb>
    <div class="app-container">
      <el-form :model="queryParams" ref="queryForm" v-show="showSearch" :inline="true">
        <el-form-item label="角色名称" prop="roleNameLike">
          <el-input
              v-model="queryParams.roleNameLike"
              placeholder="请输入角色名称"
              clearable
              size="small"
              style="width: 180px"
              @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="角色Key" prop="roleKeyLike">
          <el-input
              v-model="queryParams.roleKeyLike"
              placeholder="请输入角色标识key"
              clearable
              size="small"
              style="width: 160px"
              @keyup.enter.native="handleQuery"
          />
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
      </el-form>

      <el-row>
        <el-form>
          <el-form-item style="margin: 0">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermission="['role:add']">
              新增
            </el-button>
          </el-form-item>
        </el-form>
      </el-row>

      <el-table border v-loading="loading" :data="roleList">
        <el-table-column label="角色ID" align="center" prop="roleId" min-width="120" show-overflow-tooltip/>
        <el-table-column label="角色名称" align="center" prop="roleName" min-width="150" show-overflow-tooltip/>
        <el-table-column prop="status" label="状态" align="center" width="60" show-overflow-tooltip>
          <template slot-scope="scope">
            <span v-if="scope.row.status === 0"> <el-tag type="success" size="mini">启用</el-tag></span>
            <span v-if="scope.row.status === 1"><el-tag type="danger" size="mini">停用</el-tag></span>
          </template>
        </el-table-column>
        <el-table-column label="角色key" align="center" prop="roleKey" min-width="150" show-overflow-tooltip/>
        <el-table-column label="显示顺序" align="center" prop="sortNum" min-width="120" width="150" show-overflow-tooltip/>
        <el-table-column label="用户数" align="center" prop="userCount" min-width="120" show-overflow-tooltip/>
        <el-table-column label="创建时间" align="center" prop="createTime" width="165" show-overflow-tooltip/>
        <el-table-column label="修改时间" align="center" prop="updateTime" width="165" show-overflow-tooltip/>
        <el-table-column label="操作" fixed="right" align="center" width="180" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermission="['role:update']"
            >修改
            </el-button>
            <el-button
                v-if="scope.row.userCount>0"
                size="mini"
                type="text"
                icon="el-icon-view"
                @click="gotoUserRole(scope.row)"
                v-hasPermission="['user:listPage']"
            >已关联用户
            </el-button>
            <el-button
                v-if="scope.row.userCount==0"
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermission="['role:delete']"
            >删除
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

      <!-- 添加或修改角色配置对话框 -->
      <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
        <el-form ref="form" :model="form" :rules="rules" label-width="80px">
          <el-row>
            <el-col :span="12">
              <el-form-item label="角色名称" prop="roleName">
                <el-input v-model="form.roleName" placeholder="请输入角色名称"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="标识key" prop="roleKey">
                <el-input v-model="form.roleKey" placeholder="请输入角色标识"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="状态" prop="status">
                <el-radio-group v-model="form.status">
                  <el-radio :key="0" :label="0">正常</el-radio>
                  <el-radio :key="1" :label="1">停用</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="角色顺序" prop="sortNum">
                <el-input-number v-model="form.sortNum" controls-position="right" :min="0" :max="999999999"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="菜单权限">
            <el-checkbox v-model="menuExpand" @change="handleCheckedTreeExpand($event)">展开/折叠</el-checkbox>
            <el-checkbox v-model="menuNodeAll" @change="handleCheckedTreeNodeAll($event)">全选/全不选</el-checkbox>
            <el-checkbox v-model="form.menuCheckStrictly" @change="handleCheckedTreeConnect($event)">父子联动
            </el-checkbox>
            <el-tree
                class="tree-border"
                :data="menuOptions"
                show-checkbox
                ref="menu"
                node-key="menuId"
                :check-strictly="!menuCheckAll"
                empty-text="加载中，请稍后"
                :props="defaultProps"
            ></el-tree>
          </el-form-item>
          <el-form-item label="备注" prop="remarks">
            <el-input v-model="form.remarks" type="textarea"
                      :autosize="{ minRows: 2, maxRows: 5}"
                      placeholder="请输入内容"></el-input>
          </el-form-item>
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
import { addRole, delRole, getRoleDetail, listPage, updateRole } from '@/api/role'
import { listMenuOptionTree } from '@/api/menu'

export default {
  name: 'Role',
  inject: ['reloadLeftMenu'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      targetSysCode: '',
      targetSysName: '',
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 角色表格数据
      roleList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 是否显示弹出层（数据权限）
      openDataScope: false,
      menuExpand: false,
      menuNodeAll: false,
      deptExpand: true,
      deptNodeAll: false,
      // 日期范围
      dateRange: [],
      // 状态数据字典
      statusOptions: [],
      // 菜单列表
      menuOptions: [],
      // 部门列表
      deptOptions: [],
      // 查询参数
      queryParams: {
        page: 1,
        pageSize: 10,
        sysCode: null,
        roleNameLike: null,
        roleKeyLike: null,
        status: -1
      },
      // 查询参数
      sysCodeParams: {
        sysCode: null
      },
      // 表单参数
      form: {},
      // 表单JSON内容
      oldFormJson: '',
      //选择框父子联动
      menuCheckAll: true,
      defaultProps: {
        children: 'children',
        label: 'menuName'
      },
      // 表单校验
      rules: {
        roleName: [
          { required: true, message: '角色名称不能为空', trigger: 'blur' },
          { max: 32, message: '最大长度 32 个字符', trigger: 'blur' }
        ],
        roleKey: [
          { max: 60, message: '最大长度 60 个字符', trigger: 'blur' }
        ],
        sortNum: [
          { required: true, message: '角色顺序不能为空', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '状态不能为空', trigger: 'blur' }
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
    this.sysCodeParams.sysCode = this.targetSysCode
    this.getList()
  },
  methods: {
    //查询角色列表
    getList() {
      this.loading = true
      listPage(this.queryParams).then(response => {
            this.roleList = response.data
            this.total = response.total
            this.loading = false
          }
      )
    },
    // 新增
    handleAdd() {
      this.reset()
      this.getMenuTreeOption()
      this.open = true
      this.title = '添加角色'
    },
    //修改
    handleUpdate(row) {
      this.reset()
      this.getMenuTreeOption()
      const roleId = row.roleId || this.ids
      getRoleDetail(roleId).then(response => {
        this.form = response.data
        this.form.menuCheckStrictly = true
        this.oldFormJson = JSON.stringify(this.form)
        this.open = true
        //处理已经选中
        this.$nextTick(() => {
          this.$refs.menu.setCheckedKeys(response.data.menuIdList)
        })
        this.title = '修改角色'
      })
    },
    //删除角色
    handleDelete(row) {
      this.$confirm('是否确认删除名称为"' + row.roleName + '"的角色?', '删除警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return delRole(row.roleId)
      }).then(() => {
        this.getList()
        this.msgSuccess('删除成功')
      }).catch(function() {
      })
    },
    //查询菜单树选项
    getMenuTreeOption() {
      listMenuOptionTree(this.sysCodeParams).then(response => {
        this.menuOptions = response.data
      })
    },
    // 所有菜单节点数据
    getMenuAllCheckedKeys() {
      // 目前被选中的菜单节点
      let checkedKeys = this.$refs.menu.getHalfCheckedKeys()
      // 半选中的菜单节点
      let halfCheckedKeys = this.$refs.menu.getCheckedKeys()
      checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys)
      return checkedKeys
    },
    //提交按钮
    submitForm: function() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.roleId !== undefined) {
            const menuIdList = this.getMenuAllCheckedKeys()
            //排序
            menuIdList.sort()
            this.form.menuIdList = menuIdList
            const newFormJson = JSON.stringify(this.form)
            if (this.oldFormJson === newFormJson) {
              this.msgError('您还未修改过')
              return
            }
            updateRole(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess('修改成功')
                this.open = false
                this.getList()
              }
            })
          } else {
            this.form.menuIdList = this.getMenuAllCheckedKeys()
            addRole(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess('新增成功')
                this.open = false
                this.getList()
              }
            })
          }
        }
      })
    },
    gotoUserRole(row) {
      this.$router.push({
        path: '/userRole/index',
        query: { 'roleId': row.roleId }
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 取消按钮（数据权限）
    cancelDataScope() {
      this.openDataScope = false
      this.reset()
    },
    // 表单重置
    reset() {
      if (this.$refs.menu !== undefined) {
        this.$refs.menu.setCheckedKeys([])
      }
      this.menuExpand = false,
          this.menuNodeAll = false,
          this.deptExpand = true,
          this.deptNodeAll = false,
          this.form = {
            sysCode: this.targetSysCode,
            roleId: undefined,
            roleName: undefined,
            roleKey: undefined,
            sortNum: 0,
            status: 0,
            menuIdList: [],
            deptIds: [],
            remarks: ''
          }
      this.resetForm('form')
    },
    //搜索按钮操作
    handleQuery() {
      this.queryParams.page = 1
      this.getList()
    },
    //重置按钮操作
    resetQuery() {
      this.dateRange = []
      this.resetForm('queryForm')
      this.oldFormJson = ''
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.roleId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    // 树权限（展开/折叠）
    handleCheckedTreeExpand(value) {
      let treeList = this.menuOptions
      for (let i = 0; i < treeList.length; i++) {
        this.$refs.menu.store.nodesMap[treeList[i].menuId].expanded = value
      }
    },
    // 树权限（全选/全不选）
    handleCheckedTreeNodeAll(value) {
      this.$refs.menu.setCheckedNodes(value ? this.menuOptions : [])
    },
    // 树权限（父子联动）
    handleCheckedTreeConnect(value, type) {
      this.menuCheckAll = value ? true : false
    }
  }
}
</script>

<style scoped>

.el-table__column :not(.is-hidden):last-child {
  right: -1px;
}

</style>
