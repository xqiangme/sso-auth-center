import Vue from 'vue'
import Vuex from 'vuex'
import getters from './getters'
import app from './modules/app'
import settings from './modules/settings'
import user from './modules/user'
import common from './modules/common'

Vue.use(Vuex)


// 这里放全局参数
const store = new Vuex.Store({
  modules: {
    app,
    settings,
    user,
    common
  },
  getters
})

export default store
