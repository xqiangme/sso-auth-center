import request from '@/utils/request'

// 获取路由
export const getRouters = () => {
  return request({
    url: '/getRouters',
    method: 'get'
  })
}

// 查询菜单列表
export function listMenuTree(query) {
  return request({
    url: '/menu/listMenuTree',
    method: 'get',
    params: query
  })
}

// 查询菜单选项列表
export function listMenuOptionTree(query) {
  return request({
    url: '/menu/listMenuOptionTree',
    method: 'get',
    params: query
  })
}

// 查询菜单详细
export function getMenuDetail(menuId) {
  return request({
    url: '/menu/detail/' + menuId,
    method: 'get'
  })
}


// 新增菜单
export function addMenu(data) {
  return request({
    url: '/menu/add',
    method: 'post',
    data: data
  })
}

// 修改菜单
export function updateMenu(data) {
  return request({
    url: '/menu/update',
    method: 'put',
    data: data
  })
}

// 删除菜单
export function delMenu(menuId) {
  return request({
    url: '/menu/delete/' + menuId,
    method: 'delete'
  })
}

// 查询菜单下拉树结构
export function treeselect() {
  return request({
    url: '/menu/listMenuTree',
    method: 'get'
  })
}

// 根据角色ID查询菜单下拉树结构
export function roleMenuTreeselect(roleId) {
  return request({
    url: '/system/menu/roleMenuTreeselect/' + roleId,
    method: 'get'
  })
}

