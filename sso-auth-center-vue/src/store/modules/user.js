import { getUserInfo, login, logout } from '@/api/login'
import { getToken, removeToken, setToken } from '@/utils/token'
import { isExternal } from '@/utils/validate'

const getDefaultState = () => {
  return {
    token: getToken(),
    name: '',
    avatar: '',
    roles: null,
    permissions: [],
    //头部菜单(一级菜单)
    menuList: [],
    menuMap: []
  }
}

const state = getDefaultState()

const mutations = {
  // RESET_STATE: (state) => {
  //   Object.assign(state, getDefaultState())
  // },
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  },
  SET_PERMISSIONS: (state, permissions) => {
    state.permissions = permissions
  },
  SET_MENU_LIST: (state, menuList) => {
    state.menuList = menuList
  },
  SET_MENU_MAP: (state, menuMap) => {
    state.menuMap = menuMap
  }
}

const actions = {
  // 登录
  Login({ commit }, userInfo) {
    const username = userInfo.username.trim()
    const password = userInfo.password
    const captchaCode = userInfo.captchaCode
    const requestId = userInfo.requestId
    return new Promise((resolve, reject) => {
      login(username, password, captchaCode, requestId).then(res => {
        setToken(res.data.token)
        commit('SET_TOKEN', res.data.token)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  // 获取用户信息
  GetInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getUserInfo(state.token).then(res => {
        const user = res.data.user
        let avatar

        //用户头像-若不存在设置默认头像
        if (null == user.avatar || '' === user.avatar) {
          avatar = require('@/assets/image/profile.jpg')
        } else {
          //如果是外链则不处理
          if (isExternal(user.avatar)) {
            avatar = user.avatar
          } else {
            avatar = process.env.VUE_APP_BASE_API + user.avatar
          }
        }
        //角色数组
        if (res.data.roleKeyList && res.data.roleKeyList.length > 0) {
          commit('SET_ROLES', res.data.roleKeyList)
        } else {
          commit('SET_ROLES', [])
        }
        //权限数组
        if (res.data.permissionList && res.data.permissionList.length > 0) {
          commit('SET_PERMISSIONS', res.data.permissionList)
        }

        //菜单-数组
        if (res.data.menuList && res.data.menuList.length > 0) {
          commit('SET_MENU_LIST', res.data.menuList)
          let menuMap = new Map()
          for (let i in res.data.menuList) {
            const menu = res.data.menuList[i]
            menuMap.set(menu.path, menu)
          }
          commit('SET_MENU_MAP', menuMap)
        } else {
          commit('SET_MENU_LIST', [])
          commit('SET_MENU_MAP', new Map())
        }

        commit('SET_NAME', user.username)
        commit('SET_AVATAR', avatar)
        resolve(res)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // 退出系统
  LogOut({ commit, state }) {
    return new Promise((resolve, reject) => {
      logout(state.token).then(() => {
        commit('SET_TOKEN', '')
        commit('SET_ROLES', [])
        commit('SET_PERMISSIONS', [])
        removeToken()
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  // 前端 登出
  FedLogOut({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '')
      removeToken()
      resolve()
    })
  },
  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      removeToken() // must remove  token  first
      commit('RESET_STATE')
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
