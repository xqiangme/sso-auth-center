<template>
  <div class="body-container">
    <el-breadcrumb separator="/">
      <el-breadcrumb-item>{{ targetSysName }}</el-breadcrumb-item>
      <el-breadcrumb-item>菜单管理</el-breadcrumb-item>
    </el-breadcrumb>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch">
      <el-form-item label="菜单名称" prop="menuNameLike">
          <el-input
              v-model="queryParams.menuNameLike"
              placeholder="请输入菜单名称"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
          />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="状态" @keyup.enter.native="handleQuery" @change="handleQuery"
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
          <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermission="['menu:add']">
            新增
          </el-button>
        </el-form-item>
      </el-form>
    </el-row>

    <el-table border
              v-loading="loading"
              :data="menuList"
              row-key="menuId"
              :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column fixed prop="menuName" align="left" label="菜单名称" min-width="200" show-overflow-tooltip/>
      <el-table-column fixed prop="menuType" align="center" label="菜单类型" min-width="78" show-overflow-tooltip>
        <template slot-scope="scope">
          <span v-if="scope.row.menuType === 'M'"> 目录</span>
          <span v-if="scope.row.menuType === 'C'"> 菜单</span>
          <span v-if="scope.row.menuType === 'F'"> <el-tag size="mini">按钮</el-tag></span>
        </template>
      </el-table-column>
      <el-table-column prop="icon" label="图标" align="center" min-width="50" show-overflow-tooltip>
        <template slot-scope="scope">
          <svg-icon v-if="scope.row.icon && scope.row.icon.indexOf('el')===-1" :icon-class="scope.row.icon"/>
          <i v-if="scope.row.icon && scope.row.icon.indexOf('el')!==-1" slot="prefix" :class="scope.row.icon"/>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" align="center" width="65" show-overflow-tooltip>
        <template slot-scope="scope">
          <span v-if="scope.row.status === 0"> <el-tag type="success" size="mini">启用</el-tag></span>
          <span v-if="scope.row.status === 1"><el-tag type="info" size="mini">停用</el-tag></span>
        </template>
      </el-table-column>
      <el-table-column prop="path" label="路由地址" min-width="150" show-overflow-tooltip/>
      <el-table-column prop="component" label="组件路径" min-width="150" show-overflow-tooltip/>
      <el-table-column prop="permissions" label="权限标识" min-width="150" show-overflow-tooltip/>
      <el-table-column prop="sortNum" label="显示顺序" align="center" min-width="78" show-overflow-tooltip/>
      <el-table-column label="创建时间" align="center" width="165" prop="createTime"/>
      <el-table-column label="修改时间" align="center" width="165" prop="updateTime"/>
      <el-table-column
          fixed="right"
          label="操作"
          width="200">
        <template slot-scope="scope">
          <el-button size="mini"
                     type="text"
                     icon="el-icon-edit"
                     @click="handleUpdate(scope.row)"
                     v-hasPermission="['menu:update']"
          >修改
          </el-button>
          <el-button
              size="mini"
              type="text"
              icon="el-icon-plus"
              @click="handleAdd(scope.row)"
              v-hasPermission="['menu:add']"
          >新增
          </el-button>
          <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermission="['menu:delete']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改菜单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="上级菜单" prop="menuParentId">
              <MenuTreeOption
                  v-model="form.menuParentId"
                  :options="menuOptions"
                  :normalizer="normalizer"
                  :show-count="true"
                  placeholder="选择上级菜单"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="菜单类型" prop="menuType">
              <el-radio-group v-model="form.menuType">
                <el-radio label="M">目录</el-radio>
                <el-radio label="C">菜单</el-radio>
                <el-radio label="F">按钮</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="菜单名称" prop="menuName">
              <el-input v-model="form.menuName" placeholder="请输入菜单名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示顺序" prop="sortNum">
              <el-input-number v-model="form.sortNum" controls-position="right" :min="0" :max="999999999"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType !== 'F'" label="路由地址" prop="path">
              <el-input v-model="form.path" placeholder="请输入路由地址"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio :key="0" :label="0">正常</el-radio>
                <el-radio :key="1" :label="1">停用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item prop="icon" v-if="form.menuType !== 'F'" label="菜单图标">
              <el-popover
                  placement="bottom-start"
                  width="460"
                  trigger="click"
                  @show="$refs['iconSelect'].reset()"
              >
                <IconSelect ref="iconSelect" @selected="selected"/>
                <el-input slot="reference" v-model="form.icon" placeholder="可点击选择图标">
                  <svg-icon
                      v-if="form.icon && form.icon.indexOf('el')===-1"
                      slot="prefix"
                      :icon-class="form.icon"
                      class="el-input__icon"
                      style="height: 32px;width: 16px;"
                  />
                  <i v-if="form.icon && form.icon.indexOf('el')!==-1" slot="prefix" :class="form.icon"/>
                  <i v-if="form.icon===null || form.icon==='' || form.icon===undefined"
                     slot="prefix" class="el-icon-search el-input__icon"/>
                </el-input>
              </el-popover>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType === 'C'">
            <el-form-item label="组件路径" prop="component" style="width: 350px">
              <el-input v-model="form.component" placeholder="请输入组件名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="11" v-if="form.menuType === 'C' && form.menuParentId===0">
            <el-form-item label="" prop="layoutName">
              <el-input v-model="form.layoutName" placeholder="布局组件名称"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item v-if="form.menuType !== 'M'" prop="permissionList" label="权限标识" style="width: 500px">
              <el-select
                  v-model="form.permissionList"
                  multiple
                  filterable
                  allow-create
                  default-first-option
                  style="width:100%"
                  placeholder="请填写权限标识">
                <el-option
                    v-for="item in form.permissionList"
                    :key="item"
                    :label="item" :value="item">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="使用类型" prop="useType" v-if="form.menuType !== 'F'">
              <el-radio-group v-model="form.useType">
                <el-radio :key="0" :label="0">授权访问</el-radio>
                <el-radio :key="1" :label="1">开放访问</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType !== 'F'" label="是否显示">
              <el-radio-group v-model="form.visible" prop="visible">
                <el-radio :key="0" :label="0">是</el-radio>
                <el-radio :key="1" :label="1">否</el-radio>
              </el-radio-group>
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
import { addMenu, delMenu, getMenuDetail, listMenuOptionTree, listMenuTree, updateMenu } from '@/api/menu'
import MenuTreeOption from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import IconSelect from '@/components/IconSelect'

export default {
  name: 'Menu',
  components: { MenuTreeOption, IconSelect },
  inject: ['reloadLeftMenu'],
  data() {
    const validateMenuComponent = (rule, value, callback) => {
      if (null === value || undefined === value) {
        callback()
      } else {
        if (value.indexOf('>') !== -1) {
          callback(new Error('组件名称不能包含 >'))
        }
      }
      callback()
    }
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 菜单表格树数据
      menuList: [],
      // 菜单树选项
      menuOptions: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 显示状态数据字典
      visibleOptions: [],
      // 菜单状态数据字典
      statusOptions: [],
      // 查询参数
      queryParams: {
        sysCode: null,
        menuNameLike: null,
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
      // 表单校验
      rules: {
        menuParentId: [
          { required: true, message: '上级菜单不能为空', trigger: 'blur' }
        ],
        menuType: [{ required: true, message: '菜单类型不能为空', trigger: 'blur' }],
        menuName: [
          { required: true, message: '菜单名称不能为空', trigger: 'blur' },
          { max: 50, message: '最大长度 50 个字符', trigger: 'blur' }
        ],
        sortNum: [
          { required: true, message: '菜单顺序不能为空', trigger: 'blur' },
          { type: 'number', message: '菜单顺序必须为数字值', trigger: 'blur' }
        ],
        component: [
          { max: 200, message: '最大长度 200 个字符', trigger: 'blur' },
          { trigger: 'blur', validator: validateMenuComponent }
        ],
        status: [{ required: true, message: '状态不能为空', trigger: 'blur' }],
        useType: [{ required: true, message: '使用类型不能为空', trigger: 'blur' }],
        visible: [{ required: true, message: '是否显示不能为空', trigger: 'blur' }],
        path: [
          { required: true, message: '路由地址不能为空', trigger: 'blur' },
          { max: 150, message: '最大长度 150 个字符', trigger: 'blur' }
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
    // 选择图标
    selected(name) {
      this.form.icon = name
    },
    //查询菜单列表
    getList() {
      this.loading = true
      listMenuTree(this.queryParams).then(response => {
        this.menuList = this.handleTree(response.data, 'menuId', 'menuParentId')
        this.loading = false
      })
    },
    //转换菜单数据结构
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children
      }
      //不能选择自己为父级
      if (node.menuId === this.form.menuId) {
        node['isDisabled'] = true
      }
      return {
        id: node.menuId,
        label: node.menuName,
        children: node.children
      }
    },
    //查询菜单下拉树结构
    getMenuOptionTree() {
      listMenuOptionTree(this.queryParams).then(response => {
        this.menuOptions = []
        const menu = { menuId: 0, menuName: '顶级类目', children: [] }
        menu.children = this.handleTree(response.data, 'menuId', 'menuParentId')
        this.menuOptions.push(menu)
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
        menuId: undefined,
        menuParentId: 0,
        menuName: '',
        icon: '',
        menuType: 'M',
        component: null,
        layoutName: null,
        permissionList: [],
        sortNum: 0,
        useType: 0,
        visible: 0,
        status: 0,
        remarks: ''
      }
      this.resetForm('form')
    },
    //搜索按钮操作
    handleQuery() {
      this.getList()
    },
    //重置按钮操作
    resetQuery() {
      this.resetForm('queryForm')
      this.oldFormJson = ''
      this.handleQuery()
    },
    //新增按钮操作
    handleAdd(row) {
      this.reset()
      this.getMenuOptionTree()
      if (row != null && row.menuId) {
        this.form.menuParentId = row.menuId
      } else {
        this.form.menuParentId = 0
      }
      this.open = true
      this.title = '添加菜单'
    },
    //修改按钮操作
    handleUpdate(row) {
      this.reset()
      this.getMenuOptionTree()
      getMenuDetail(row.menuId).then(response => {
        const data = response.data
        if (null != data.component && '' !== data.component) {
          if (data.component.indexOf('>') !== -1) {
            const componentArray = data.component.split('>')
            data.layoutName = componentArray[0]
            if (componentArray.length > 1) {
              data.component = componentArray[1]
            }
          }
        }
        this.form = data
        this.oldFormJson = JSON.stringify(this.form)
        this.open = true
        this.title = '修改菜单'
      })
    },
    //提交按钮
    submitForm: function() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (null !== this.form.layoutName && undefined !== this.form.layoutName
              && '' !== this.form.layoutName) {
            this.form.component = this.form.layoutName + '>' + this.form.component
          }
          if (this.form.menuId !== undefined) {
            const newFormJson = JSON.stringify(this.form)
            if (this.oldFormJson === newFormJson) {
              this.msgError('您还未修改过')
              return
            }
            updateMenu(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess('修改成功')
                this.open = false
                this.getList()
              }
            })
          } else {
            addMenu(this.form).then(response => {
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
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm('是否确认删除名称为"' + row.menuName + '"的数据项?', '删除警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return delMenu(row.menuId)
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
