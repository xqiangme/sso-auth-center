import request from '@/utils/request'

// 分页列表
export function listLoginLog(query) {
  return request({
    url: '/login/log/listPage',
    method: 'get',
    params: query
  })
}

