package com.ruoyi.system.domain.family;

/**
 * AI聊天请求对象
 * 
 * @author ruoyi
 * @date 2025-01-09
 */
public class AiChatRequest
{
    /** 用户消息 */
    private String message;
    
    /** 会话ID */
    private String sessionId;
    
    /** 是否包含财务数据分析 */
    private Boolean includeFinancialData;

    public AiChatRequest()
    {
    }

    public AiChatRequest(String message)
    {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Boolean getIncludeFinancialData() {
        return includeFinancialData;
    }

    public void setIncludeFinancialData(Boolean includeFinancialData) {
        this.includeFinancialData = includeFinancialData;
    }

    @Override
    public String toString() {
        return "AiChatRequest{" +
                "message='" + message + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", includeFinancialData=" + includeFinancialData +
                '}';
    }
}
