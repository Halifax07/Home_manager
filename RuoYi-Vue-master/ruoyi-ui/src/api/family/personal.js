import request from '@/utils/request'

// 获取个人信息
export function getPersonalInfo() {
  return request({
    url: '/family/members/personal',
    method: 'get'
  })
}

// 更新个人信息
export function updatePersonalInfo(data) {
  return request({
    url: '/family/members/personal',
    method: 'put',
    data: data
  })
}
