import request from '@/utils/request'

// 查询家庭交易记录列表
export function listTransactions(query) {
  return request({
    url: '/family/transactions/list',
    method: 'get',
    params: query
  })
}

// 查询家庭交易记录详细
export function getTransaction(id) {
  return request({
    url: '/family/transactions/' + id,
    method: 'get'
  })
}

// 新增家庭交易记录
export function addTransaction(data) {
  return request({
    url: '/family/transactions',
    method: 'post',
    data: data
  })
}

// 修改家庭交易记录
export function updateTransaction(data) {
  return request({
    url: '/family/transactions',
    method: 'put',
    data: data
  })
}

// 删除家庭交易记录
export function delTransaction(id) {
  return request({
    url: '/family/transactions/' + id,
    method: 'delete'
  })
}

// 根据家庭ID查询交易记录
export function getTransactionsByFamilyId(familyId) {
  return request({
    url: '/family/transactions/family/' + familyId,
    method: 'get'
  })
}

// 根据成员ID查询交易记录
export function getTransactionsByMemberId(memberId) {
  return request({
    url: '/family/transactions/member/' + memberId,
    method: 'get'
  })
}

// 根据交易类型查询记录
export function getTransactionsByType(txnType) {
  return request({
    url: '/family/transactions/type/' + txnType,
    method: 'get'
  })
}
