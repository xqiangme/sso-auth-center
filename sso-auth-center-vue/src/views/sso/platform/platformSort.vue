<template>
  <div class="app-container">
    <el-row v-if="platformList.length<=0">
      <el-alert
          title="您还没有系统权限，请联系管理员添加"
          type="success"
          center
          show-icon :closable="false" style="margin-bottom: 5px">
      </el-alert>
      <el-button icon="el-icon-back" size="mini" @click="back">返回</el-button>
    </el-row>
    <el-row v-if="platformList.length>0">
      <el-alert
          title="您可以拖动列进行排序，拖动完毕点击下方确认按钮生效"
          type="success"
          center
          show-icon :closable="false" style="margin-bottom: 5px">
      </el-alert>
      <el-button icon="el-icon-back" size="mini" @click="back">返回</el-button>
      <el-button type="primary" icon="el-icon-position" size="mini" @click="handleSortSubmit">确认排序</el-button>
      <el-button type="info" icon="el-icon-refresh" size="mini" @click="cancelSort">取消排序</el-button>
    </el-row>
    <el-row style="margin-top: 10px">
      <el-table
          v-loading="loading"
          :default-sort="{prop: 'sortNum', order: 'ascending'}"
          :data="platformList"
          border
          style="width: 100%"
          max-height="600"
          align="left">
        <el-table-column
            show-overflow-tooltip
            v-for="(item, index) in col"
            :key="`col_${index}`"
            :prop="col[index].prop"
            :min-width="col[index].width"
            :label="item.label">
          <template slot-scope="scope">
            <p>{{ scope.row[item.prop] }}</p>
          </template>
        </el-table-column>
      </el-table>
    </el-row>
  </div>
</template>

<script>
import { platformMyList, sortMySystem } from '@/api/system'
import Sortable from 'sortablejs'

export default {
  components: { Sortable },
  data() {
    return {
      //遮罩层
      loading: true,
      //查询参数
      queryParams: {},
      platformList: [],
      col: [
        {
          label: '排序值',
          prop: 'sortNum',
          width: '100'
        },
        {
          label: '平台ID',
          prop: 'sysId',
          width: '100'
        },
        {
          label: '平台名称',
          prop: 'sysName',
          width: '100'
        },
        {
          label: '系统编码',
          prop: 'sysCode',
          width: '100'
        }
      ]
    }
  },
  mounted() {
    this.rowDrop()
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      this.platformList = []
      platformMyList(this.queryParams).then((response) => {
            this.platformList = response.data
            this.loading = false
          }
      )
    },
    //返回
    back() {
      this.$router.push({ path: '/myPlatform/index' })
    },
    //确认排序
    handleSortSubmit() {
      if (this.platformList.length <= 0) {
        this.$router.push({ path: '/myPlatform/index' })
        return
      }

      const sysCodeList = []
      this.loading = true
      for (let key in this.platformList) {
        const itemDate = this.platformList[key]
        sysCodeList.push(itemDate.sysCode)
      }

      sortMySystem(sysCodeList).then((response) => {
            if (response.code === 200) {
              this.loading = false
              this.$router.push({ path: '/myPlatform/index' })
            }
          }
      )
    },
    //取消排序
    cancelSort() {
      this.getList()
    },
    rowDrop() {
      const that = this
      let thatPlatformList = this.platformList
      const tbody = document.querySelector('.el-table__body-wrapper tbody')
      Sortable.create(tbody, {
        onEnd({ newIndex, oldIndex }) {
          const currRow = that.platformList.splice(oldIndex, 1)[0]
          that.platformList.splice(newIndex, 0, currRow)
        }
      })
    }
  }
}
</script>


<style scoped>

</style>
