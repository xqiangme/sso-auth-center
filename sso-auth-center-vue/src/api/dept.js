import request from '@/utils/request'

// 查询部门管理树-列表
export function listDeptMgmtTree(query) {
  return request({
    url: '/dept/listDeptMgmtTree',
    method: 'get',
    params: query
  })
}

// 查询部门选项树-列表
export function listDeptOptionTree(query) {
  return request({
    url: '/dept/listDeptOptionTree',
    method: 'get',
    params: query
  })
}

// 查询部门列表（排除节点）
export function listDeptExcludeChild(deptId) {
  return request({
    url: '/system/dept/list/exclude/' + deptId,
    method: 'get'
  })
}

// 查询部门详细
export function getDeptDetail(deptId) {
  return request({
    url: '/dept/detail/' + deptId,
    method: 'get'
  })
}

// 查询部门下拉树结构
export function treeselect() {
  return request({
    url: '/dept/listDeptOptionTree',
    method: 'get'
  })
}

// 根据角色ID查询部门树结构
export function roleDeptTreeselect(roleId) {
  return request({
    url: '/system/dept/roleDeptTreeselect/' + roleId,
    method: 'get'
  })
}

// 新增部门
export function addDept(data) {
  return request({
    url: '/dept/add',
    method: 'post',
    data: data
  })
}

// 修改部门
export function updateDept(data) {
  return request({
    url: '/dept/update',
    method: 'put',
    data: data
  })
}

// 删除部门
export function delDept(deptId) {
  return request({
    url: '/dept/delete/' + deptId,
    method: 'delete'
  })
}
