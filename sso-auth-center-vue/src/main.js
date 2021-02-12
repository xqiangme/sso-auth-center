import Vue from 'vue'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import '@/styles/index.scss'

import locale from 'element-ui/lib/locale/lang/zh-CN' // lang i18n
import Pagination from '@/components/Pagination'

import { handleTree, resetForm } from '@/utils/admin-util'

// global css

import App from './App'
import store from './store'
import router from './router'
import permission from './directive/permission'

import './assets/icons' // ico
import './permission'  // permission control

Vue.prototype.msgSuccess = function(msg) {
  this.$message({ showClose: true, message: msg, type: 'success' })
}

Vue.prototype.msgError = function(msg) {
  this.$message({ showClose: true, message: msg, type: 'error' })
}

Vue.prototype.msgInfo = function(msg) {
  this.$message.info(msg)
}

// 全局方法挂载
Vue.prototype.handleTree = handleTree
Vue.prototype.resetForm = resetForm

// 全局组件挂载
Vue.component('Pagination', Pagination)
Vue.use(permission)

// set ElementUI lang to EN
Vue.use(ElementUI, { locale })
// 如果想要中文版 element-ui，按如下方式声明
// Vue.use(ElementUI)

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
