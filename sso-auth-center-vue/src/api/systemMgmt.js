import request from '@/utils/request'

// 系统管理员-用户分页列表
export function listPageSystemMgmt(query) {
  return request({
    url: '/systemMgmt/listPage',
    method: 'get',
    params: query
  })
}

// 新增-管理员权限
export function addSystemMgmt(data) {
  return request({
    url: '/systemMgmt/add',
    method: 'post',
    data: data
  })
}

// 移除-用户管理员权限
export function removeUserSystemMgmt(sysCode, userId) {
  const data = { 'sysCode': sysCode, 'userId': userId }
  return request({
    url: '/systemMgmt/remove',
    method: 'put',
    data: data
  })
}

// 修改状态-用户管理员权限
export function updateSystemMgmtStatus(id, status) {
  const data = { 'id': id, 'status': status }
  return request({
    url: '/systemMgmt/updateStatus',
    method: 'put',
    data: data
  })
}

