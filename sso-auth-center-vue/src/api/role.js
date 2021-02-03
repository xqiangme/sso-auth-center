import request from '@/utils/request'

export function listPage(query) {
  return request({
    url: '/role/listPage',
    method: 'get',
    params: query
  })
}

//下拉选项
export function listRoleOption(query) {
  return request({
    url: '/role/listOption',
    method: 'get',
    params: query
  })
}

// 查询角色详细
export function getRoleDetail(roleId) {
  return request({
    url: '/role/detail/' + roleId,
    method: 'get'
  })
}

// 新增角色
export function addRole(data) {
  return request({
    url: '/role/add',
    method: 'post',
    data: data
  })
}

// 修改角色
export function updateRole(data) {
  return request({
    url: '/role/update',
    method: 'put',
    data: data
  })
}

// 删除角色
export function delRole(menuId) {
  return request({
    url: '/role/delete/' + menuId,
    method: 'delete'
  })
}

// 移除角色用户绑定关系
export function removeUserRole(roleId, userId) {
  const data = { 'roleId': roleId, 'userId': userId }
  return request({
    url: '/role/removeUser',
    method: 'put',
    data: data
  })
}


