<template>
  <div class="body-container">
    <el-breadcrumb separator="/">
      <el-breadcrumb-item>{{ targetSysName }}</el-breadcrumb-item>
      <el-breadcrumb-item>部门管理</el-breadcrumb-item>
    </el-breadcrumb>
    <div class="app-container">
      <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch">
        <el-form-item label="部门名称" prop="deptNameLike">
          <el-input
              v-model="queryParams.deptNameLike"
              placeholder="请输入部门名称"
              clearable
              size="small"
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
            <el-button
                type="primary"
                icon="el-icon-plus"
                size="mini"
                @click="handleAdd(null)" v-hasPermission="['dept:add']">新增
            </el-button>
          </el-form-item>
        </el-form>
      </el-row>

      <el-table
          border
          v-loading="loading"
          :data="deptList"
          row-key="deptId"
          :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
        <el-table-column fixed prop="deptName" label="部门名称" min-width="180" show-overflow-tooltip></el-table-column>
        <el-table-column prop="status" label="状态" align="center" min-width="80" show-overflow-tooltip>
          <template slot-scope="scope">
            <span v-if="scope.row.status === 0"> <el-tag type="success" size="mini">启用</el-tag></span>
            <span v-if="scope.row.status === 1"><el-tag type="danger" size="mini">停用</el-tag></span>
          </template>
        </el-table-column>
        <el-table-column prop="sortNum" label="显示顺序" align="center" min-width="100"
                         show-overflow-tooltip></el-table-column>
        <el-table-column prop="createTime" min-width="165" label="创建时间" align="center"/>
        <el-table-column prop="updateTime" min-width="165" label="修改时间" align="center"/>
        <el-table-column fixed="right" label="操作" align="center" width="200">
          <template slot-scope="scope">
            <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermission="['dept:update']"
            >修改
            </el-button>
            <el-button
                v-if="scope.row.status===0"
                size="mini"
                type="text"
                icon="el-icon-plus"
                @click="handleAdd(scope.row)"
                v-hasPermission="['dept:add']"
            >新增
            </el-button>
            <el-button
                v-if="scope.row.children.length<=0"
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermission="['dept:delete']"
            >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 添加或修改部门对话框 -->
      <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
        <el-form ref="form" :model="form" :rules="rules" label-width="80px">
          <el-row>
            <el-col :span="24">
              <el-form-item label="上级部门" prop="deptParentId">
                <treeselect v-model="form.deptParentId" :options="deptOptions" :normalizer="normalizer"
                            placeholder="选择上级部门"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="部门名称" prop="deptName">
                <el-input v-model="form.deptName" placeholder="请输入部门名称"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="部门状态" prop="status">
                <el-radio-group v-model="form.status">
                  <el-radio :label="0">正常</el-radio>
                  <el-radio :label="1">停用</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="显示顺序" prop="sortNum">
                <el-input-number v-model="form.sortNum" controls-position="right" :min="0" :max="999999999"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="备注" prop="remarks">
                <el-input v-model="form.remarks" type="textarea"
                          :autosize="{ minRows: 2, maxRows: 5}"
                          placeholder="请输入内容"></el-input>
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
import { addDept, delDept, getDeptDetail, listDeptMgmtTree, listDeptOptionTree, updateDept } from '@/api/dept'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

export default {
  name: 'Dept',
  components: { Treeselect },
  inject: ['reloadLeftMenu'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      targetSysCode: '',
      targetSysName: '',
      // 表格树数据
      deptList: [],
      // 部门树选项
      rootOptions: [{ 'deptId': 0, 'deptParentId': -1, 'deptName': '顶级部门', 'children': [] }],
      deptOptions: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 状态数据字典
      statusOptions: [],
      // 查询参数
      queryParams: {
        sysCode: null,
        deptNameLike: null,
        status: -1
      },
      // 表单参数
      form: {},
      // 表单JSON内容
      oldFormJson: '',
      // 表单校验
      rules: {
        deptParentId: [
          { required: true, message: '上级部门不能为空', trigger: 'blur' }
        ],
        deptName: [
          { required: true, message: '部门名称不能为空', trigger: 'blur' },
          { max: 200, message: '最大长度 200 个字符', trigger: 'blur' }
        ],
        sortNum: [
          { required: true, message: '菜单顺序不能为空', trigger: 'blur' }
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
    this.getList()
  },
  methods: {
    // 查询部门列表
    getList() {
      this.loading = true
      listDeptMgmtTree(this.queryParams).then(response => {
        this.deptList = this.handleTree(response.data, 'deptId', 'deptParentId')
        this.loading = false
      })
    },
    //转换部门数据结构
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children
      }
      //不能选择自己为父级
      if (node.deptId === this.form.deptId) {
        node['isDisabled'] = true
      }
      return {
        id: node.deptId,
        label: node.deptName,
        children: node.children
      }
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        deptId: undefined,
        sysCode: this.targetSysCode,
        deptParentId: 0,
        deptName: undefined,
        sortNum: 0,
        status: 0,
        remarks: ''
      }
      this.resetForm('form')
    },
    //搜索按钮操作
    handleQuery() {
      this.getList()
    },
    //重置
    resetQuery() {
      this.resetForm('queryForm')
      this.oldFormJson = ''
      this.handleQuery()
    },
    //新增按钮操作
    handleAdd(row) {
      this.reset()
      if (null !== row && row !== undefined) {
        this.form.deptParentId = row.deptId
      }
      this.open = true
      this.title = '添加部门'
      listDeptOptionTree(this.queryParams).then(response => {
        this.deptOptions = this.rootOptions
        this.deptOptions = this.deptOptions.concat(this.handleTree(response.data, 'deptId', 'deptParentId'))
      })
    },
    //修改按钮操作
    handleUpdate(row) {
      this.reset()
      listDeptOptionTree(this.queryParams).then(response => {
        this.deptOptions = this.rootOptions
        this.deptOptions = this.deptOptions.concat(this.handleTree(response.data, 'deptId', 'deptParentId'))
      })
      getDeptDetail(row.deptId).then(response => {
        this.form = response.data
        this.oldFormJson = JSON.stringify(this.form)
        this.open = true
        this.title = '修改部门'
      })
    },
    //提交按钮
    submitForm: function() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.deptId !== undefined) {
            const newFormJson = JSON.stringify(this.form)
            if (this.oldFormJson === newFormJson) {
              this.msgError('您还未修改过')
              return
            }
            updateDept(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess('修改成功')
                this.open = false
                this.getList()
              }
            })
          } else {
            addDept(this.form).then(response => {
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
    //删除按钮操作
    handleDelete(row) {
      this.$confirm('是否确认删除名称为"' + row.deptName + '"的部门?', '删除警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return delDept(row.deptId)
      }).then(() => {
        this.getList()
        this.msgSuccess('删除成功')
      }).catch(function() {
      })
    }
  }
}
</script>
<style scoped>

</style>
