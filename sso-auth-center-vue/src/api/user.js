import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/login',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: '/getUserInfo',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/logout',
    method: 'post'
  })
}

// 查询用户列表
export function listUser(query) {
  return request({
    url: '/user/listPage',
    method: 'get',
    params: query
  })
}

// 根据用户名或者手机号-模糊查询用户下拉列表
export function listUserOption(keywords) {
  if (null === keywords || undefined === keywords) {
    keywords = ''
  }
  return request({
    url: '/user/listUserOption?keywords=' + keywords,
    method: 'get'
  })
}

// 查询用户详细
export function getDeptDetail(data) {
  return request({
    url: '/user/detail',
    method: 'post',
    data: data
  })
}

// 根据手机号查询
export function getUserByPhone(phone) {
  return request({
    url: '/user/getUserByPhone/' + phone,
    method: 'get'
  })
}

// 根据用户名查询
export function getUserByUserName(username) {
  return request({
    url: '/user/getUserByUserName/' + username,
    method: 'get'
  })
}

// 新增用户
export function addUser(data) {
  return request({
    url: '/user/add',
    method: 'post',
    data: data
  })
}

// 修改用户
export function updateUser(data) {
  return request({
    url: '/user/update',
    method: 'put',
    data: data
  })
}

// 移除用户系统关系
export function removeUserSystem(data) {
  return request({
    url: '/user/removeUserSystem',
    method: 'put',
    data: data
  })
}

// 删除用户
export function delUser(data) {
  return request({
    url: '/user/delete',
    method: 'put',
    data: data
  })
}

// 导出用户
export function exportUser(query) {
  return request({
    url: '/system/user/export',
    method: 'get',
    params: query
  })
}

// 用户密码重置
export function resetUserPwd(userId, password) {
  const data = {
    userId,
    password
  }
  return request({
    url: '/user/resetPwd',
    method: 'put',
    data: data
  })
}

// 用户状态修改
export function changeUserStatus(userId, status) {
  const data = {
    userId,
    status
  }
  return request({
    url: '/system/user/changeStatus',
    method: 'put',
    data: data
  })
}

// 查询用户个人信息
export function getUserProfile() {
  return request({
    url: '/user/profile/detail',
    method: 'get'
  })
}

// 修改用户个人信息
export function updateUserProfile(data) {
  return request({
    url: '/user/profile/update',
    method: 'put',
    data: data
  })
}

// 用户密码重置
export function updateUserPwd(userId, oldPassword, newPassword) {
  const data = { 'userId': userId, 'oldPassword': oldPassword, 'newPassword': newPassword }
  return request({
    url: '/user/profile/updatePwd',
    method: 'put',
    data: data
  })
}

// 用户头像上传
export function uploadAvatar(data) {
  return request({
    url: '/user/profile/avatar',
    method: 'post',
    data: data
  })
}

// 在线用户列表
export function listPageOnlineUser(query) {
  return request({
    url: '/user/online/listPage',
    method: 'get',
    params: query
  })
}

// 查看已登录子系统列表
export function listOnlineSys(userId) {
  return request({
    url: '/user/online/listOnlineSys/' + userId,
    method: 'get'
  })
}

// 强退用户
export function retreatUser(userId) {
  return request({
    url: '/user/online/retreatUser/' + userId,
    method: 'put'
  })
}
