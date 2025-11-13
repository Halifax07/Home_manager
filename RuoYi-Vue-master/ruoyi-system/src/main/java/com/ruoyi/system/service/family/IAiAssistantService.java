package com.ruoyi.system.service.family;

import com.ruoyi.system.domain.family.AiChatRequest;
import com.ruoyi.system.domain.family.AiChatResponse;

/**
 * AI助手服务接口
 * 
 * @author ruoyi
 * @date 2025-01-09
 */
public interface IAiAssistantService 
{
    /**
     * 生成智能理财建议
     * 
     * @return 理财建议
     */
    public String generateFinancialAdvice();

    /**
     * 生成预算分析
     * 
     * @return 预算分析
     */
    public String generateBudgetAnalysis();

    /**
     * AI对话聊天
     * 
     * @param request 聊天请求
     * @return 聊天响应
     */
    public AiChatResponse chat(AiChatRequest request);

    /**
     * 生成收支趋势分析
     * 
     * @return 趋势分析
     */
    public String generateTrendAnalysis();

    /**
     * 生成智能预算建议
     * 
     * @return 预算建议
     */
    public String generateBudgetSuggestion();
}
