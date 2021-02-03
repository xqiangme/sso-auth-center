import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { getToken } from '@/utils/token'
// NProgress Configuration
NProgress.configure({ showSpinner: false })

//免登录白名单
const whiteList = ['/login']

router.beforeEach((to, from, next) => {
  NProgress.start()
  if (getToken()) {
    //has token
    if (to.path === '/login') {
      let redirectUrl = to.query.redirectUrl
      if (null !== redirectUrl && '' !== redirectUrl && redirectUrl !== undefined) {
        window.location.href = redirectUrl + '?ssoToken=' + getToken()
      }else {
        next({ path: '/' })
        NProgress.done()
      }
    } else {
      if (store.getters.name === '') {
        // 判断当前用户是否已拉取完user_info信息
        store.dispatch('user/GetInfo').then(res => {
          // 拉取user_info
          const roles = res.data.roles
          next({ ...to, replace: true })
        })
          .catch(err => {
            store.dispatch('user/FedLogOut').then(() => {
              Message.error(err)
              next({ path: '/' })
            })
          })
      } else {
        next()
      }
    }
  } else {
    // 没有token
    if (whiteList.indexOf(to.path) !== -1) {
      // 在免登录白名单，直接进入
      next()
    } else {
      next(`/login?redirect=${to.fullPath}`) // 否则全部重定向到登录页
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})
