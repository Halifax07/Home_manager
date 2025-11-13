import request from '@/utils/request'

// 查询家庭成员列表
export function listMembers(query) {
  return request({
    url: '/family/members/list',
    method: 'get',
    params: query
  })
}

// 查询家庭成员详细
export function getMember(id) {
  return request({
    url: '/family/members/' + id,
    method: 'get'
  })
}

// 新增家庭成员
export function addMember(data) {
  return request({
    url: '/family/members',
    method: 'post',
    data: data
  })
}

// 修改家庭成员
export function updateMember(data) {
  return request({
    url: '/family/members',
    method: 'put',
    data: data
  })
}

// 删除家庭成员
export function delMember(id) {
  return request({
    url: '/family/members/' + id,
    method: 'delete'
  })
}

// 根据家庭ID查询成员列表
export function getMembersByFamilyId(familyId) {
  return request({
    url: '/family/members/family/' + familyId,
    method: 'get'
  })
}

// 检查当前用户是否为家庭管理员
export function checkAdmin() {
  return request({
    url: '/family/members/check-admin',
    method: 'get'
  })
}
