import request from '@/utils/request'

// 我的平台列表
export function platformMyList(query) {
  return request({
    url: '/system/myList',
    method: 'get',
    params: query
  })
}

// 我的平台排序
export function sortMySystem(sysCodeList) {
  const data = { 'sysCodeList': sysCodeList }
  return request({
    url: '/system/sortMy',
    method: 'post',
    data: data
  })
}

// 平台管理列表
export function platformMgmtList(query) {
  return request({
    url: '/system/mgmtList',
    method: 'get',
    params: query
  })
}

// 系统详情
export function getDetailBySysCode(query) {
  return request({
    url: '/system/getDetailBySysCode?sysCode=' + query,
    method: 'get'
  })
}

// 新增平台
export function addSystem(data) {
  return request({
    url: '/system/add',
    method: 'post',
    data: data
  })
}

// 修改平台
export function updateSystem(data) {
  return request({
    url: '/system/update',
    method: 'put',
    data: data
  })
}

// 修改秘钥
export function updateSecret(data) {
  return request({
    url: '/system/updateSecret',
    method: 'put',
    data: data
  })
}

// 修改图标
export function uploadSysIcon(sysId, data) {
  return request({
    url: '/system/uploadIcon/' + sysId,
    method: 'post',
    data: data
  })
}

// 删除平台
export function delSystem(sysId) {
  return request({
    url: '/system/delete/' + sysId,
    method: 'delete'
  })
}
