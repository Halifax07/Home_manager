package com.ruoyi.system.service.family.impl;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.service.family.IAiAssistantService;
import com.ruoyi.system.service.ITransactionsService;
import com.ruoyi.system.domain.family.AiChatRequest;
import com.ruoyi.system.domain.family.AiChatResponse;
import com.ruoyi.system.domain.Transactions;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * AI助手服务业务层处理
 * 
 * @author ruoyi
 * @date 2025-01-09
 */
@Service
public class AiAssistantServiceImpl implements IAiAssistantService 
{
    @Autowired
    private ITransactionsService transactionsService;

    @Value("${ai.qianwen.api.key:}")
    private String apiKey;

    @Value("${ai.qianwen.api.url:https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 构造函数，配置RestTemplate超时时间
    public AiAssistantServiceImpl() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(30000); // 连接超时30秒
        factory.setReadTimeout(60000); // 读取超时60秒
        this.restTemplate = new RestTemplate(factory);
    }

    /**
     * 生成智能理财建议
     */
    @Override
    public String generateFinancialAdvice()
    {
        // 临时测试返回
        return "这是一个测试的理财建议内容。根据您的财务状况分析，建议您：\n1. 合理控制支出\n2. 增加储蓄比例\n3. 考虑投资理财产品\n4. 建立应急资金储备";
        
        /*
        try {
            String financialData = getFinancialDataSummary();
            String prompt = "作为一名专业的家庭理财顾问，请基于以下财务数据分析，提供详细的理财建议：\n\n" + financialData + 
                          "\n\n请从以下几个方面给出建议：\n1. 支出优化建议\n2. 储蓄目标设定\n3. 投资建议\n4. 风险控制\n5. 未来规划";
            
            return callQianwenAPI(prompt);
        } catch (Exception e) {
            return "生成理财建议时出现错误：" + e.getMessage();
        }
        */
    }

    /**
     * 生成预算分析
     */
    @Override
    public String generateBudgetAnalysis()
    {
        try {
            String financialData = getFinancialDataSummary();
            String prompt = "请作为专业的预算分析师，基于以下家庭财务数据进行详细的预算分析：\n\n" + financialData + 
                          "\n\n请分析：\n1. 当前预算执行情况\n2. 各类别支出占比分析\n3. 超支风险提醒\n4. 预算调整建议\n5. 下月预算建议";
            
            return callQianwenAPI(prompt);
        } catch (Exception e) {
            return "生成预算分析时出现错误：" + e.getMessage();
        }
    }

    /**
     * AI对话聊天
     */
    @Override
    public AiChatResponse chat(AiChatRequest request)
    {
        try {
            String prompt = request.getMessage();
            
            // 如果需要包含财务数据
            if (request.getIncludeFinancialData() != null && request.getIncludeFinancialData()) {
                String financialData = getFinancialDataSummary();
                prompt = "基于我的家庭财务数据：\n\n" + financialData + "\n\n用户问题：" + prompt;
            }
            
            String response = callQianwenAPI(prompt);
            
            return new AiChatResponse(response, request.getSessionId());
        } catch (Exception e) {
            AiChatResponse response = new AiChatResponse();
            response.setErrorMessage("AI对话失败：" + e.getMessage());
            return response;
        }
    }

    /**
     * 生成收支趋势分析
     */
    @Override
    public String generateTrendAnalysis()
    {
        try {
            String financialData = getFinancialDataSummary();
            String prompt = "请作为数据分析专家，基于以下家庭财务数据进行收支趋势分析：\n\n" + financialData + 
                          "\n\n请分析：\n1. 收入趋势分析\n2. 支出趋势分析\n3. 结余变化趋势\n4. 异常数据识别\n5. 未来趋势预测";
            
            return callQianwenAPI(prompt);
        } catch (Exception e) {
            return "生成趋势分析时出现错误：" + e.getMessage();
        }
    }

    /**
     * 生成智能预算建议
     */
    @Override
    public String generateBudgetSuggestion()
    {
        try {
            String financialData = getFinancialDataSummary();
            String prompt = "请作为预算规划专家，基于以下家庭财务数据制定智能预算建议：\n\n" + financialData + 
                          "\n\n请提供：\n1. 合理的月度预算分配\n2. 各类别支出限额建议\n3. 储蓄目标设定\n4. 紧急备用金规划\n5. 长期财务目标建议";
            
            return callQianwenAPI(prompt);
        } catch (Exception e) {
            return "生成预算建议时出现错误：" + e.getMessage();
        }
    }

    /**
     * 调用通义千问API
     */
    private String callQianwenAPI(String prompt) throws Exception 
    {
        System.out.println("=== AI API调用调试信息 ===");
        System.out.println("API Key: " + (apiKey != null ? apiKey.substring(0, 10) + "..." : "null"));
        System.out.println("API URL: " + apiUrl);
        
        if (apiKey == null || apiKey.isEmpty()) {
            System.out.println("API密钥为空，使用模拟响应");
            return "AI服务未配置，请联系管理员配置通义千问API密钥。";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "qwen-turbo");
        
        Map<String, Object> messageObj = new HashMap<>();
        messageObj.put("role", "user");
        messageObj.put("content", prompt);
        
        requestBody.put("messages", new Object[]{messageObj});
        requestBody.put("max_tokens", 2000);
        requestBody.put("temperature", 0.7);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        
        try {
            System.out.println("发送API请求到: " + apiUrl);
            System.out.println("请求体: " + requestBody);
            
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);
            
            System.out.println("API响应状态码: " + response.getStatusCode());
            System.out.println("API响应内容: " + response.getBody());
            
            if (response.getStatusCode().is2xxSuccessful()) {
                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                JsonNode choices = jsonNode.get("choices");
                if (choices != null && choices.isArray() && choices.size() > 0) {
                    JsonNode message = choices.get(0).get("message");
                    if (message != null && message.get("content") != null) {
                        return message.get("content").asText();
                    }
                }
                return "AI服务响应格式异常，请稍后重试。";
            } else {
                return "AI服务调用失败，HTTP状态码：" + response.getStatusCode();
            }
        } catch (Exception e) {
            System.out.println("API调用失败: " + e.getMessage());
            e.printStackTrace();
            // 如果API调用失败，返回模拟数据
            return generateMockResponse(prompt);
        }
    }

    /**
     * 生成模拟AI响应（当API不可用时）
     */
    private String generateMockResponse(String prompt) 
    {
        if (prompt.contains("理财建议")) {
            return "基于您的财务数据分析，我为您提供以下理财建议：\n\n" +
                   "1. **支出优化**：建议减少不必要的娱乐支出，控制在月收入的10%以内\n" +
                   "2. **储蓄目标**：建议每月储蓄收入的20-30%，建立紧急备用金\n" +
                   "3. **投资规划**：可考虑定投基金或稳健型理财产品\n" +
                   "4. **风险控制**：建议配置适当的保险产品\n" +
                   "5. **长期规划**：制定5-10年的财务目标\n\n" +
                   "*注：以上建议基于模拟数据生成，具体请咨询专业理财顾问*";
        } else if (prompt.contains("预算分析")) {
            return "根据您的财务数据，预算分析如下：\n\n" +
                   "1. **预算执行**：本月支出控制良好，未出现严重超支\n" +
                   "2. **支出结构**：生活必需品占比合理，娱乐支出可适当控制\n" +
                   "3. **风险提醒**：注意控制交通费用支出\n" +
                   "4. **调整建议**：建议增加储蓄预算比例\n" +
                   "5. **下月预算**：建议总支出控制在收入的70%以内\n\n" +
                   "*注：这是基于模拟数据的分析结果*";
        } else if (prompt.contains("趋势分析")) {
            return "您的收支趋势分析报告：\n\n" +
                   "1. **收入趋势**：收入相对稳定，建议寻找增收渠道\n" +
                   "2. **支出趋势**：支出有上升趋势，需要控制\n" +
                   "3. **结余变化**：结余率有所下降，建议提高储蓄意识\n" +
                   "4. **异常识别**：发现部分大额支出，请核实是否必要\n" +
                   "5. **未来预测**：预计下月可实现收支平衡\n\n" +
                   "*注：这是基于模拟数据的趋势分析*";
        } else {
            return "感谢您的咨询！作为您的AI小管家，我建议：\n\n" +
                   "1. 定期记录和分析您的收支情况\n" +
                   "2. 制定合理的预算计划\n" +
                   "3. 关注支出结构的合理性\n" +
                   "4. 建立良好的储蓄习惯\n\n" +
                   "如果您有具体的财务问题，请随时向我咨询！\n\n" +
                   "*注：当前为演示模式，建议配置通义千问API获得更准确的建议*";
        }
    }

    /**
     * 获取财务数据摘要
     */
    private String getFinancialDataSummary() 
    {
        try {
            // 获取当前用户的收支数据
            List<Transactions> transactions = transactionsService.selectTransactionsList(new Transactions());
            
            if (transactions == null || transactions.isEmpty()) {
                return "暂无财务数据记录，建议先添加一些收支记录。";
            }

            BigDecimal totalIncome = BigDecimal.ZERO;
            BigDecimal totalExpense = BigDecimal.ZERO;
            Map<String, BigDecimal> categoryExpenses = new HashMap<>();
            
            for (Transactions transaction : transactions) {
                BigDecimal amount = transaction.getAmount();
                if (amount == null) continue;
                
                if ("收入".equals(transaction.getTxnType())) {
                    totalIncome = totalIncome.add(amount);
                } else if ("支出".equals(transaction.getTxnType())) {
                    totalExpense = totalExpense.add(amount);
                    
                    String category = transaction.getCategory();
                    if (category != null) {
                        categoryExpenses.put(category, 
                            categoryExpenses.getOrDefault(category, BigDecimal.ZERO).add(amount));
                    }
                }
            }
            
            StringBuilder summary = new StringBuilder();
            summary.append("财务数据摘要（截至 ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("）：\n\n");
            summary.append("总收入：¥").append(totalIncome).append("\n");
            summary.append("总支出：¥").append(totalExpense).append("\n");
            summary.append("结余：¥").append(totalIncome.subtract(totalExpense)).append("\n\n");
            
            if (!categoryExpenses.isEmpty()) {
                summary.append("支出分类明细：\n");
                categoryExpenses.forEach((category, amount) -> {
                    summary.append("- ").append(category).append("：¥").append(amount).append("\n");
                });
            }
            
            summary.append("\n交易记录总数：").append(transactions.size()).append(" 笔");
            
            return summary.toString();
        } catch (Exception e) {
            return "获取财务数据时出现错误：" + e.getMessage();
        }
    }
}
