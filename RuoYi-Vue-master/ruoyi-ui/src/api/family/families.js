import request from '@/utils/request'

// 查询家庭信息列表
export function listFamilies(query) {
  return request({
    url: '/family/families/list',
    method: 'get',
    params: query
  })
}

// 查询家庭信息详细
export function getFamily(familieId) {
  return request({
    url: '/family/families/' + familieId,
    method: 'get'
  })
}

// 新增家庭信息
export function addFamily(data) {
  return request({
    url: '/family/families',
    method: 'post',
    data: data
  })
}

// 修改家庭信息
export function updateFamily(data) {
  return request({
    url: '/family/families',
    method: 'put',
    data: data
  })
}

// 删除家庭信息
export function delFamily(familieId) {
  return request({
    url: '/family/families/' + familieId,
    method: 'delete'
  })
}
