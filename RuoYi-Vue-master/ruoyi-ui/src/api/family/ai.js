import request from '@/utils/request'

// AI API超时时间设置为60秒
const AI_TIMEOUT = 60000

// 获取智能理财建议
export function getFinancialAdvice() {
  return request({
    url: '/family/ai/advice',
    method: 'get',
    timeout: AI_TIMEOUT
  })
}

// 获取预算分析
export function getBudgetAnalysis() {
  return request({
    url: '/family/ai/budget',
    method: 'get',
    timeout: AI_TIMEOUT
  })
}

// 获取收支趋势分析
export function getTrendAnalysis() {
  return request({
    url: '/family/ai/trend',
    method: 'get',
    timeout: AI_TIMEOUT
  })
}

// 获取智能预算建议
export function getBudgetSuggestion() {
  return request({
    url: '/family/ai/budget/suggestion',
    method: 'get',
    timeout: AI_TIMEOUT
  })
}

// AI对话聊天
export function aiChat(data) {
  return request({
    url: '/family/ai/chat',
    method: 'post',
    data: data,
    timeout: AI_TIMEOUT
  })
}
