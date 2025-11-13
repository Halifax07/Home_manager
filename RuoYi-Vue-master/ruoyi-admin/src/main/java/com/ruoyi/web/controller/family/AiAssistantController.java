package com.ruoyi.web.controller.family;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.family.IAiAssistantService;
import com.ruoyi.system.domain.family.AiChatRequest;
import com.ruoyi.system.domain.family.AiChatResponse;

/**
 * AI小管家Controller
 */
@RestController
@RequestMapping("/family/ai")
public class AiAssistantController extends BaseController {

    @Autowired
    private IAiAssistantService aiAssistantService;

    /**
     * 获取智能理财建议
     */
    @PreAuthorize("@ss.hasPermi('family:ai:advice')")
    @GetMapping("/advice")
    public AjaxResult getFinancialAdvice() {
        try {
            String advice = aiAssistantService.generateFinancialAdvice();
            return AjaxResult.success("理财建议获取成功", advice);
        } catch (Exception e) {
            logger.error("获取智能理财建议失败", e);
            return AjaxResult.error("获取智能理财建议失败: " + e.getMessage());
        }
    }

    /**
     * 获取预算分析
     */
    @PreAuthorize("@ss.hasPermi('family:ai:budget')")
    @GetMapping("/budget")
    public AjaxResult getBudgetAnalysis() {
        try {
            String analysis = aiAssistantService.generateBudgetAnalysis();
            return AjaxResult.success("预算分析获取成功", analysis);
        } catch (Exception e) {
            logger.error("获取预算分析失败", e);
            return AjaxResult.error("获取预算分析失败: " + e.getMessage());
        }
    }

    /**
     * AI对话聊天
     */
    @PreAuthorize("@ss.hasPermi('family:ai:chat')")
    @PostMapping("/chat")
    public AjaxResult chat(@RequestBody AiChatRequest request) {
        try {
            AiChatResponse response = aiAssistantService.chat(request);
            return AjaxResult.success(response);
        } catch (Exception e) {
            logger.error("AI对话失败", e);
            return AjaxResult.error("AI对话失败: " + e.getMessage());
        }
    }

    /**
     * 获取收支趋势分析
     */
    @PreAuthorize("@ss.hasPermi('family:ai:trend')")
    @GetMapping("/trend")
    public AjaxResult getTrendAnalysis() {
        try {
            String analysis = aiAssistantService.generateTrendAnalysis();
            return AjaxResult.success("收支趋势分析获取成功", analysis);
        } catch (Exception e) {
            logger.error("获取收支趋势分析失败", e);
            return AjaxResult.error("获取收支趋势分析失败: " + e.getMessage());
        }
    }

    /**
     * 获取智能预算建议
     */
    @PreAuthorize("@ss.hasPermi('family:ai:budget-suggestion')")
    @GetMapping("/budget/suggestion")
    public AjaxResult getBudgetSuggestion() {
        try {
            String suggestion = aiAssistantService.generateBudgetSuggestion();
            return AjaxResult.success("预算建议获取成功", suggestion);
        } catch (Exception e) {
            logger.error("获取智能预算建议失败", e);
            return AjaxResult.error("获取智能预算建议失败: " + e.getMessage());
        }
    }
}
