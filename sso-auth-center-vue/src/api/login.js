import request from '@/utils/request'

// 登录方法
export function login(username, password, captchaCode, requestId) {
  const data = {
    username,
    password,
    captchaCode,
    requestId
  }
  return request({
    url: '/login',
    method: 'post',
    data: data
  })
}

// 获取用户详细信息
export function getUserInfo() {
  return request({
    url: '/getUserInfo',
    method: 'get'
  })
}

// 退出方法
export function logout() {
  return request({
    url: '/logout',
    method: 'post'
  })
}

// 获取验证码
export function getCaptchaImage() {
  return request({
    url: '/captchaImage',
    method: 'get'
  })
}
