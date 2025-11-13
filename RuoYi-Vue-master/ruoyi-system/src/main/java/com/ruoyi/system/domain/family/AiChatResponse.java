package com.ruoyi.system.domain.family;

import java.util.Date;

/**
 * AI聊天响应对象
 * 
 * @author ruoyi
 * @date 2025-01-09
 */
public class AiChatResponse
{
    /** AI回复消息 */
    private String message;
    
    /** 会话ID */
    private String sessionId;
    
    /** 响应时间 */
    private Date responseTime;
    
    /** 是否成功 */
    private Boolean success;
    
    /** 错误信息 */
    private String errorMessage;

    public AiChatResponse()
    {
        this.responseTime = new Date();
        this.success = true;
    }

    public AiChatResponse(String message)
    {
        this();
        this.message = message;
    }

    public AiChatResponse(String message, String sessionId)
    {
        this(message);
        this.sessionId = sessionId;
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

    public Date getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Date responseTime) {
        this.responseTime = responseTime;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        this.success = false;
    }

    @Override
    public String toString() {
        return "AiChatResponse{" +
                "message='" + message + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", responseTime=" + responseTime +
                ", success=" + success +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
