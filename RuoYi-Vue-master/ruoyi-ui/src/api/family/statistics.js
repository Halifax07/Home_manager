import request from '@/utils/request'

// 查询收支统计概览
export function getOverview() {
  return request({
    url: '/family/statistics/overview',
    method: 'get'
  })
}

// 查询收入分类统计
export function getIncomeCategoryStatistics() {
  return request({
    url: '/family/statistics/income-category',
    method: 'get'
  })
}

// 查询支出分类统计
export function getExpenseCategoryStatistics() {
  return request({
    url: '/family/statistics/expense-category',
    method: 'get'
  })
}

// 查询月度收支趋势
export function getMonthlyTrend() {
  return request({
    url: '/family/statistics/monthly-trend',
    method: 'get'
  })
}
