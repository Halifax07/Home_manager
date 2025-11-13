import request from '@/utils/request'

// 获取智能理财建议
export function getFinancialAdvice() {
  return request({
    url: '/family/ai/advice',
    method: 'get'
  })
}

// 获取预算分析
export function getBudgetAnalysis() {
  return request({
    url: '/family/ai/budget',
    method: 'get'
  })
}

// 获取收支趋势分析
export function getTrendAnalysis() {
  return request({
    url: '/family/ai/trend',
    method: 'get'
  })
}

// AI对话聊天
export function chatWithAI(data) {
  return request({
    url: '/family/ai/chat',
    method: 'post',
    data: data
  })
}

// 获取智能预算建议
export function getBudgetSuggestion() {
  return request({
    url: '/family/ai/budget/suggestion',
    method: 'get'
  })
}
