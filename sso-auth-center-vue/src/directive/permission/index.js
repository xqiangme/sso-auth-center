import hasRole from './hasRole'
import hasPermi from './hasPermission'

const install = function(Vue) {
  Vue.directive('hasRole', hasRole)
  Vue.directive('hasPermission', hasPermi)
}

if (window.Vue) {
  window['hasRole'] = hasRole
  window['hasPermission'] = hasPermi
  Vue.use(install); // eslint-disable-line
}

export default install
