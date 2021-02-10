<template>
  <div class="platform-detail-container">
    <div class="platformClass">
      <el-row :gutter="10">
        <div>
          <div style="float: right">
            <el-button type="primary" size="mini" @click="updateSystem" v-hasPermission="['system:update']">修改
            </el-button>
            <el-button type="success" size="mini" @click="updateSecret" v-hasPermission="['system:updateSecret']">修改秘钥
            </el-button>
            <el-button type="warning" size="mini" @click="gotoUserSystemMgmt"
                       v-hasPermission="['systemMgmt:updateSystemMgmt']">系统管理员
            </el-button>
          </div>
        </div>
      </el-row>
      <el-row :gutter="10">
        <el-col :md="8" :lg="8">
          <el-card shadow="never" style="border: none">
            平台名称 : {{ detailData.sysName }}
          </el-card>
        </el-col>
        <el-col :md="3" :lg="3">
          <el-card shadow="never" style="border: none">
            平台图标 :
          </el-card>
        </el-col>
        <el-col :md="3" :lg="3" v-if="hasUpdatePermission">
          <sysIcon v-if="detailData.sysIcon !=='' && detailData.sysIcon !==undefined"  :sysId="detailData.sysId" :imgUrl="detailData.sysIcon"/>
          <sysIcon  v-else :sysId="detailData.sysId" :imgUrl="detailData.sysDefaultIcon"/>
        </el-col>
        <el-col :md="3" :lg="3" v-if="!hasUpdatePermission">
          <img v-if="detailData.sysIcon !=='' && detailData.sysIcon !==undefined" :src="detailData.sysIcon"
               style="width: 260px; height: 160px"/>
          <img v-else :src="detailData.sysDefaultIcon" style="width: 260px; height: 160px"/>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :md="10" :lg="8">
          <el-card shadow="never" style="border: none">
            状态 :
            <el-tag type="success" v-if="detailData.status===0" size="mini">正常</el-tag>
            <el-tag type="danger" v-if="detailData.status===1" size="mini">停用</el-tag>
          </el-card>
        </el-col>
        <el-col :md="8" :lg="8">
          <el-card shadow="never" style="border: none">
            平台编码 : {{ detailData.sysCode }}
          </el-card>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :md="8" :lg="8">
          <el-card shadow="never" style="border: none">
            环境 :
            <el-tag v-if="detailData.sysEnv===0" size="mini">测试</el-tag>
            <el-tag v-if="detailData.sysEnv===1" size="mini">beta</el-tag>
            <el-tag v-if="detailData.sysEnv===2" size="mini">生产</el-tag>
          </el-card>
        </el-col>
        <el-col :md="8" :lg="8" style="width: 800px">
          <el-card shadow="never" style="border: none" class="cardText">
            平台链接 : {{ detailData.sysUrl }}
          </el-card>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :md="8" :lg="8">
          <el-card shadow="never" style="border: none">
            创建者: {{ detailData.createBy }}
          </el-card>
        </el-col>
        <el-col :md="8" :lg="8">
          <el-card shadow="never" style="border: none">
            创建时间: {{ detailData.createTime }}
          </el-card>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :md="8" :lg="8">
          <el-card shadow="never" style="border: none">
            修改者: {{ detailData.updateBy }}
          </el-card>
        </el-col>
        <el-col :md="8" :lg="8">
          <el-card shadow="never" style="border: none">
            修改时间: {{ detailData.updateTime }}
          </el-card>
        </el-col>
      </el-row>
      <el-row :gutter="10" v-hasPermission="['system:updateSecret']">
        <el-col :md="8" :lg="8" v-if="detailData.publicKey!==''">
          <el-card shadow="never" style="border: none" class="cardText">
            验签公钥 :
            <el-popover
                placement="top-start"
                title=""
                width="400"
                trigger="hover"
                :content="detailData.publicKey">
              <el-button slot="reference" size="small" style="border: none;color: #20a0ff">查看</el-button>
            </el-popover>
          </el-card>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :md="8" :lg="8" style="width: 800px">
          <el-card shadow="never" style="border: none" class="cardText">
            备注 : {{ detailData.remarks }}
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 添加或修改平台 -->
    <el-dialog :title="title" :visible.sync="openUpdateSystem" width="600px" append-to-body>
      <el-form ref="updateForm" :model="updateForm" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="系统名称" prop="sysName">
              <el-input v-model="updateForm.sysName" placeholder="请输入系统名称"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="系统编码" prop="sysCode">
              <el-input readonly="true" disabled="true" v-model="updateForm.sysCode" placeholder="请输入系统编码"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="updateForm.status">
                <el-radio :label="0">正常</el-radio>
                <el-radio :label="1">停用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="显示排序" prop="sortNum">
              <el-input-number v-model="updateForm.sortNum" controls-position="right" :min="0"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="系统环境" prop="sysEnv">
              <el-select v-model="updateForm.sysEnv" placeholder="系统环境"
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
              <el-input v-model="updateForm.sysUrl" type="textarea"
                        :autosize="{ minRows: 1, maxRows: 5}"
                        placeholder="请输入系统跳转地址" style="width: 400px"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remarks">
              <el-input v-model="updateForm.remarks" type="textarea"
                        :autosize="{ minRows: 2, maxRows: 5}"
                        placeholder="请输入内容"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitUpdateForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 修改秘钥 -->
    <el-dialog :title="title" :visible.sync="openUpdateSecret" width="600px" append-to-body>
      <el-form ref="updateSecretForm" :model="updateSecretForm" :rules="rulesSecret" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="签名类型" prop="signType" style="width: 400px">
              <el-radio-group v-model="updateSecretForm.signType" @input="changeSignType">
                <el-radio :label="0">无</el-radio>
                <el-radio :label="1">MD5</el-radio>
                <el-radio :label="2">RSA</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24" v-if="updateSecretForm.signType===1">
            <el-form-item label="秘钥" prop="publicKey">
              <el-input v-model="updateSecretForm.publicKey"
                        :autosize="{ minRows: 2, maxRows: 10}"
                        size="small" type="textarea" placeholder="请输入MD5加密秘钥"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24" v-if="updateSecretForm.signType!==1">
            <el-form-item label="验签公钥" prop="publicKey">
              <el-input v-model="updateSecretForm.publicKey"
                        :autosize="{ minRows: 2, maxRows: 10}"
                        size="small" type="textarea" placeholder="请输入验签公钥"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitUpdateSecretForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<style scoped>
.platformClass {

}
</style>

<script>
import sysIcon from './sysIcon'
import { getDetailBySysCode, updateSecret, updateSystem } from '@/api/system'
import { isExternal } from '@/utils/validate'
import { hasSomePermission } from '@/utils/admin-util'

export default {
  inject: ['reloadLeftMenu'],
  components: { sysIcon },
  data() {
    return {
      //是否显示弹出层
      openUpdateSystem: false,
      openUpdateSecret: false,
      //修改平台权限
      hasUpdatePermission: false,
      title: '',
      //查询参数
      queryParams: {
        sysCode: null
      },
      detailData: {},
      updateForm: {},
      // 表单JSON内容
      oldUpdateFormJson: '',
      //修改秘钥表单
      updateSecretForm: {},
      // 表单JSON内容
      oldUpdateSecretFormJson: '',
      targetSysCode: '',
      targetSysName: '',
      // 修改表单校验
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
      },
      //修改秘钥表单校验
      rulesSecret: {
        signType: [{ required: true, message: '签名类型不能为空', trigger: 'blur' }],
        publicKey: [{ max: 2048, message: '最大长度 2048 个字符', trigger: 'blur' }]
      }
    }
  },
  created() {
    //刷新左侧菜单栏
    this.reloadLeftMenu()
    this.hasUpdatePermission = hasSomePermission('system:update')
    this.targetSysCode = sessionStorage.getItem('targetSysCode')
    if (this.targetSysCode == null || '' === this.targetSysCode) {
      this.$router.push({ path: '/platformMgmt/index' })
      return
    }
    this.targetSysName = sessionStorage.getItem('targetSysName')
    this.queryParams.sysCode = this.targetSysCode
    this.getDetail()
  },
  methods: {
    getDetail() {
      getDetailBySysCode(this.targetSysCode).then((response) => {
            this.detailData = response.data
            //系统图标-如果非外链则添加前缀处理
            if ('' !== this.detailData.sysIcon && !isExternal(this.detailData.sysIcon)) {
              this.detailData.sysIcon = process.env.VUE_APP_BASE_API + this.detailData.sysIcon
            }
            //处理默认图片地址
            if (null == this.detailData.sysIcon || '' === this.detailData.sysIcon) {
              this.detailData.sysDefaultIcon = require('@/assets/image/default-system.jpg')
            }
          }
      )
    },
    updateSystem() {
      this.reset()
      this.getDetail()
      this.updateForm.sysId = this.detailData.sysId
      this.updateForm.sysCode = this.detailData.sysCode
      this.updateForm.sysName = this.detailData.sysName
      this.updateForm.sysUrl = this.detailData.sysUrl
      this.updateForm.sysIcon = this.detailData.sysIcon
      this.updateForm.sysEnv = this.detailData.sysEnv
      this.updateForm.status = this.detailData.status
      this.updateForm.sortNum = this.detailData.sortNum
      this.updateForm.remarks = this.detailData.remarks
      this.title = '修改平台信息'
      this.openUpdateSystem = true
      //记录旧值
      this.oldUpdateFormJson = JSON.stringify(this.updateForm)
    },
    updateSecret() {
      this.reset()
      this.getDetail()
      this.updateSecretForm.sysId = this.detailData.sysId
      this.updateSecretForm.sysName = this.detailData.sysName
      this.updateSecretForm.signType = this.detailData.signType
      this.updateSecretForm.publicKey = this.detailData.publicKey
      this.title = '修改' + this.detailData.sysName + '平台秘钥'
      this.openUpdateSecret = true
      //记录旧值
      this.oldUpdateSecretFormJson = JSON.stringify(this.updateSecretForm)
    },
    //修改平台提交
    submitUpdateForm() {
      this.$refs['updateForm'].validate(valid => {
        if (valid) {
          const newFormJson = JSON.stringify(this.updateForm)
          if (this.oldUpdateFormJson === newFormJson) {
            this.msgError('您还未修改过')
            return
          }
          updateSystem(this.updateForm).then(response => {
            if (response.code === 200) {
              this.restSysStorage(this.updateForm)
              this.msgSuccess('修改成功')
              this.openUpdateSystem = false
              this.getDetail()
            }
          })
        }
      })
    },
    //修改系统编码或系统名称后-修改sessionStorage
    restSysStorage(form) {
      if (undefined === form || null === form) {
        return
      }
      this.targetSysCode = sessionStorage.getItem('targetSysCode')
      this.targetSysName = sessionStorage.getItem('targetSysName')
      if (this.targetSysCode === form.sysCode && this.targetSysName === form.sysName) {
        return
      }
      this.targetSysCode = form.sysCode
      this.targetSysName = form.sysName
      sessionStorage.setItem('targetSysCode', form.sysCode)
      sessionStorage.setItem('targetSysName', form.sysName)
    },
    //修改秘钥提交
    submitUpdateSecretForm() {
      this.$refs['updateSecretForm'].validate(valid => {
        if (valid) {
          const newFormJson = JSON.stringify(this.updateSecretForm)
          if (this.oldUpdateSecretFormJson === newFormJson) {
            this.msgError('您还未修改过')
            return
          }
          this.$confirm(
              '若已在使用,请谨慎操作,是否继续?',
              '危险操作',
              {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              }
          ).then(() => {
            return this.handleUpdateSecretForm()
          }).catch(function() {
          })
        }
      })
    },
    //系统用户操作
    gotoUserSystemMgmt(row) {
      this.$router.push({
        path: '/systemMgmt/index',
        query: { 'sysCode': this.targetSysCode }
      })
    },
    changeSignType() {
      //更改签名类型为 - > 无签名
      if (this.updateSecretForm.signType === 0 && this.updateSecretForm.publicKey != null) {
        this.$confirm(
            '已存在签名,更改签名类型将会清除原先的签名,是否继续?',
            '危险操作',
            {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }
        ).then(() => {
          this.updateSecretForm.publicKey = null
        }).catch(function() {
        })
      }
    },
    handleUpdateSecretForm() {

      updateSecret(this.updateSecretForm).then(response => {
        if (response.code === 200) {
          this.openUpdateSecret = false
          this.getDetail()
          this.msgSuccess('修改成功')
        }
      })
    },
    cancel() {
      this.openUpdateSystem = false
      this.openUpdateSecret = false
      this.reset()
    },
    reset() {
      this.updateForm = {
        sysId: null,
        sysCode: '',
        sysName: '',
        sysUrl: '',
        sysIcon: '',
        sysEnv: 0,
        status: 0,
        sortNum: 100,
        remarks: ''
      }
      this.updateSecretForm = {
        sysId: null,
        sysName: '',
        signType: 0,
        publicKey: ''
      }
      this.resetForm('updateForm')
      this.resetForm('updateSecretForm')
    }
  },
  mounted() {

  }
}
</script>

