<template>
  <div class="app-container">
    <el-row>
      <el-col :span="20">
        <el-button type="success" icon="el-icon-plus" size="mini" style="margin-left: 15px"
                   @click="handleAdd" v-hasPermission="['system:add']">新增
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
              alt="测试测试"
              @click="clickPlatformMgmt(item)"
          >
          <div style="padding: 15px;" @click="clickPlatformMgmt(item)">
            <span>{{ item.sysName }} <i style="float: right;border: none" class="el-icon-edit-outline"></i></span>
            <span></span>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 添加或修改平台 -->
    <el-dialog :title="title" :visible.sync="openAddForm" width="600px" append-to-body>
      <el-form ref="addForm" :model="addForm" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="系统名称" prop="sysName">
              <el-input v-model="addForm.sysName" placeholder="请输入系统名称"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="系统编码" prop="sysCode">
              <el-tooltip effect="dark" content="请谨慎设置，添加后不允许修改" placement="right-start">
                <el-input
                    v-model="addForm.sysCode"
                    placeholder="请输入系统编码">
                  <i slot="suffix" class="el-icon-question"></i>
                </el-input>
              </el-tooltip>
            </el-form-item>
          </el-col>
          <el-col :span="12">
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="addForm.status">
                <el-radio :label="0">正常</el-radio>
                <el-radio :label="1">停用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="显示排序" prop="sortNum">
              <el-input-number v-model="addForm.sortNum" controls-position="right" :min="0" :max="999999999"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="系统环境" prop="sysEnv">
              <el-select v-model="addForm.sysEnv" placeholder="系统环境"
                         size="small" style="width: 100px">
                <el-option :key="0" label="测试" :value="0"/>
                <el-option :key="1" label="beta" :value="1"/>
                <el-option :key="2" label="生产" :value="2"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="系统地址" prop="sysUrl">
              <el-input v-model="addForm.sysUrl" type="textarea"
                        :autosize="{ minRows: 1, maxRows: 5}"
                        placeholder="请输入系统跳转地址" style="width: 400px"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remarks">
              <el-input v-model="addForm.remarks" type="textarea"
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
    <el-row v-if="platformList.length<=0">
      <p style="text-align: center"> 您还没有平台管理权限,联系管理员添加吧</p>
    </el-row>
  </div>
</template>
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

<script>
import { addSystem, platformMgmtList } from '@/api/system'
import { isExternal } from '@/utils/validate'

export default {
  data() {
    return {
      //遮罩层
      loading: true,
      //查询参数
      queryParams: {},
      title: '',
      openAddForm: false,
      addForm: {},
      platformList: [],
      //表单校验
      rules: {
        sysName: [
          { required: true, message: '系统名称不能为空', trigger: 'blur' },
          { max: 100, message: '最大长度 100 个字符', trigger: 'blur' }
        ],
        sysCode: [
          { required: true, message: '系统编码不能为空', trigger: 'blur' },
          { min: 2, max: 36, message: '长度在 2 到 36 个字符', trigger: 'blur' }
        ],
        status: [{ required: true, message: '状态不能为空', trigger: 'blur' }],
        sortNum: [{ required: true, message: '显示排序不能为空', trigger: 'blur' }],
        sysEnv: [{ required: true, message: '系统环境不能为空', trigger: 'blur' }],
        realName: [{ max: 200, message: '长度不超过 30 个字符', trigger: 'blur' }],
        sysUrl: [
          { required: true, message: '系统地址不能为空', trigger: 'blur' },
          { max: 500, message: '最大长度 500 个字符', trigger: 'blur' }
        ],
        remarks: [
          { max: 200, message: '最大长度 200 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      this.platformList = []
      platformMgmtList(this.queryParams).then((response) => {
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
    handleAdd() {
      this.reset()
      this.openAddForm = true
      this.title = '添加平台'
    },
    submitForm() {
      this.$refs['addForm'].validate(valid => {
        if (valid) {
          this.addForm.sysIcon='';
          this.$confirm(
              '是否确认新增,请注意!新增后系统编码将不可修改?',
              '确认警告',
              {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              }
          ).then(() => {
            return this.doAddSystem()
          }).catch(function() {
          })
        }
      })
    },
    doAddSystem() {
      addSystem(this.addForm).then(response => {
        if (response.code === 200) {
          this.openAddForm = false
          this.getList()
          this.msgSuccess('新增成功')
        }
      })
    },
    clickPlatformMgmt(item) {
      sessionStorage.setItem('targetSysCode', item.sysCode)
      sessionStorage.setItem('targetSysName', item.sysName)
      // 跳转到平台管理页面
      // this.$router.push('/platformMgmt/platformDetail')
      const path = this.$route.path
      this.$router.push({
        path: '/platformMgmt/platformDetail',
        param: { 'sourcePath': path }
      })
    },
    handleClickOutside() {
      this.$store.dispatch('app/closeSideBar', { withoutAnimation: false })
    },
    // 取消按钮
    cancel() {
      this.openAddForm = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.addForm = {
        sysCode: '',
        sysName: '',
        sysUrl: '',
        sysIcon: '',
        sysEnv: 0,
        status: 0,
        sortNum: 100,
        remarks: ''
      }
      this.resetForm('addForm')
    }
  }

}
</script>

