<template>
  <div class="app-container">
    <el-card class="ai-assistant-card">
      <div slot="header" class="clearfix">
        <span class="card-title">
          <i class="el-icon-cpu"></i>
          小管家 - AI智能理财助手
        </span>
      </div>
      
      <!-- 功能按钮区域 -->
      <div class="function-buttons">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-button 
              type="primary" 
              icon="el-icon-s-finance" 
              @click="getFinancialAdvice"
              :loading="loading.advice"
              class="function-btn">
              理财建议
            </el-button>
          </el-col>
          <el-col :span="6">
            <el-button 
              type="success" 
              icon="el-icon-pie-chart" 
              @click="getBudgetAnalysis"
              :loading="loading.budget"
              class="function-btn">
              预算分析
            </el-button>
          </el-col>
          <el-col :span="6">
            <el-button 
              type="warning" 
              icon="el-icon-trend-charts" 
              @click="getTrendAnalysis"
              :loading="loading.trend"
              class="function-btn">
              趋势分析
            </el-button>
          </el-col>
          <el-col :span="6">
            <el-button 
              type="info" 
              icon="el-icon-coin" 
              @click="getBudgetSuggestion"
              :loading="loading.suggestion"
              class="function-btn">
              预算建议
            </el-button>
          </el-col>
        </el-row>
      </div>

      <!-- 聊天对话区域 -->
      <div class="chat-section">
        <div class="chat-header">
          <h3><i class="el-icon-chat-dot-round"></i> AI对话</h3>
          <div>
            <el-button size="small" @click="forceScrollToBottom" type="text">滚动到底部</el-button>
            <el-button size="small" @click="clearChat" type="text">清空对话</el-button>
          </div>
        </div>
        
        <!-- 聊天消息列表 -->
        <div class="chat-messages" ref="chatMessages">
          <div class="messages-wrapper">
            <div v-for="(message, index) in chatMessages" :key="index" class="message-item">
              <div :class="['message', message.type]">
                <div class="message-avatar">
                  <i :class="message.type === 'user' ? 'el-icon-user' : 'el-icon-cpu'"></i>
                </div>
                <div class="message-content">
                  <div class="message-text" v-html="formatMessage(message.content)"></div>
                  <div class="message-time">{{ message.time }}</div>
                </div>
              </div>
            </div>
            <div v-if="loading.chat" class="message-item">
              <div class="message ai">
                <div class="message-avatar">
                  <i class="el-icon-cpu"></i>
                </div>
                <div class="message-content">
                  <div class="typing-indicator">
                    <span></span>
                    <span></span>
                    <span></span>
                  </div>
                </div>
              </div>
            </div>
            <!-- 滚动锚点 -->
            <div class="scroll-anchor" ref="scrollAnchor"></div>
          </div>
        </div>

        <!-- 输入区域 -->
        <div class="chat-input">
          <el-row :gutter="10">
            <el-col :span="20">
              <el-input
                v-model="userMessage"
                placeholder="向AI小管家提问，比如：我的支出结构合理吗？"
                @keyup.enter.native="handleEnterKey"
                :disabled="loading.chat">
                <template slot="prepend">
                  <el-checkbox v-model="includeFinancialData" size="small">
                    包含财务数据
                  </el-checkbox>
                </template>
              </el-input>
            </el-col>
            <el-col :span="4">
              <el-button 
                type="primary" 
                @click="sendMessage"
                :loading="loading.chat"
                :disabled="!userMessage.trim() || loading.chat"
                class="send-btn">
                {{ loading.chat ? '发送中...' : '发送' }}
              </el-button>
            </el-col>
          </el-row>
        </div>
      </div>

      <!-- 智能分析结果展示区域 -->
      <div class="analysis-section" v-if="analysisResult">
        <div class="analysis-header">
          <h3><i class="el-icon-data-analysis"></i> {{ analysisTitle }}</h3>
          <el-button size="small" @click="analysisResult = ''" type="text">关闭</el-button>
        </div>
        <div class="analysis-content">
          <el-card shadow="never">
            <div v-html="formatAnalysis(analysisResult)"></div>
          </el-card>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getFinancialAdvice, getBudgetAnalysis, getTrendAnalysis, getBudgetSuggestion, chatWithAI } from "@/api/family/ai";

export default {
  name: "AiAssistant",
  data() {
    return {
      loading: {
        advice: false,
        budget: false,
        trend: false,
        suggestion: false,
        chat: false
      },
      chatMessages: [
        {
          type: 'ai',
          content: '你好！我是您的AI小管家，可以为您提供智能理财建议、预算分析和收支管理建议。请问有什么可以帮助您的吗？',
          time: this.getCurrentTime()
        }
      ],
      userMessage: '',
      includeFinancialData: true,
      sessionId: this.generateSessionId(),
      analysisResult: '',
      analysisTitle: ''
    };
  },
  mounted() {
    this.scrollToBottom();
  },
  watch: {
    // 监听消息数组变化，自动滚动到底部
    chatMessages: {
      handler(newMessages, oldMessages) {
        // 只在消息增加时滚动，避免不必要的滚动
        if (newMessages.length > (oldMessages ? oldMessages.length : 0)) {
          this.scrollToBottom();
        }
      },
      deep: true
    },
    // 监听加载状态变化
    'loading.chat': {
      handler(isLoading) {
        if (!isLoading) {
          // 加载完成后滚动到底部
          this.scrollToBottom();
        }
      }
    }
  },
  methods: {
    // 获取理财建议
    async getFinancialAdvice() {
      this.loading.advice = true;
      try {
        const response = await getFinancialAdvice();
        this.showAnalysis('智能理财建议', response.data);
      } catch (error) {
        this.$modal.msgError('获取理财建议失败');
      } finally {
        this.loading.advice = false;
      }
    },

    // 获取预算分析
    async getBudgetAnalysis() {
      this.loading.budget = true;
      try {
        const response = await getBudgetAnalysis();
        this.showAnalysis('预算分析报告', response.data);
      } catch (error) {
        this.$modal.msgError('获取预算分析失败');
      } finally {
        this.loading.budget = false;
      }
    },

    // 获取趋势分析
    async getTrendAnalysis() {
      this.loading.trend = true;
      try {
        const response = await getTrendAnalysis();
        this.showAnalysis('收支趋势分析', response.data);
      } catch (error) {
        this.$modal.msgError('获取趋势分析失败');
      } finally {
        this.loading.trend = false;
      }
    },

    // 获取预算建议
    async getBudgetSuggestion() {
      this.loading.suggestion = true;
      try {
        const response = await getBudgetSuggestion();
        this.showAnalysis('智能预算建议', response.data);
      } catch (error) {
        this.$modal.msgError('获取预算建议失败');
      } finally {
        this.loading.suggestion = false;
      }
    },

    // 发送聊天消息
    async sendMessage() {
      if (!this.userMessage.trim()) return;

      const userMsg = {
        type: 'user',
        content: this.userMessage,
        time: this.getCurrentTime()
      };
      this.chatMessages.push(userMsg);

      const message = this.userMessage;
      this.userMessage = '';
      this.loading.chat = true;

      // 用户消息发送后立即滚动到底部
      this.$nextTick(() => {
        this.scrollToBottom();
      });

      try {
        const response = await chatWithAI({
          message: message,
          sessionId: this.sessionId,
          includeFinancialData: this.includeFinancialData
        });

        const aiMsg = {
          type: 'ai',
          content: response.data.message || response.data,
          time: this.getCurrentTime()
        };
        this.chatMessages.push(aiMsg);
      } catch (error) {
        const errorMsg = {
          type: 'ai',
          content: '抱歉，AI服务暂时不可用，请稍后重试。',
          time: this.getCurrentTime()
        };
        this.chatMessages.push(errorMsg);
      } finally {
        this.loading.chat = false;
        // AI回复后再次滚动到底部
        this.$nextTick(() => {
          this.scrollToBottom();
        });
      }
    },

    // 清空聊天记录
    clearChat() {
      this.chatMessages = [
        {
          type: 'ai',
          content: '对话已清空。我是您的AI小管家，请问有什么可以帮助您的吗？',
          time: this.getCurrentTime()
        }
      ];
      this.sessionId = this.generateSessionId();
    },

    // 显示分析结果
    showAnalysis(title, content) {
      this.analysisTitle = title;
      this.analysisResult = content;
    },

    // 格式化消息内容
    formatMessage(content) {
      if (!content) return '';
      return content
        .replace(/\n/g, '<br>')
        .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
        .replace(/\*(.*?)\*/g, '<em>$1</em>')
        .replace(/(\d+\.\s)/g, '<br>$1');
    },

    // 格式化分析内容
    formatAnalysis(content) {
      if (!content) return '';
      return content
        .replace(/\n/g, '<br>')
        .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
        .replace(/\*(.*?)\*/g, '<em>$1</em>')
        .replace(/(\d+\.\s.*?):/g, '<h4>$1</h4>')
        .replace(/^(.*?)：$/gm, '<h4>$1：</h4>');
    },

    // 获取当前时间
    getCurrentTime() {
      const now = new Date();
      return now.toLocaleTimeString('zh-CN', { hour12: false });
    },

    // 生成会话ID
    generateSessionId() {
      return 'session_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9);
    },

    // 滚动到底部
    scrollToBottom() {
      this.$nextTick(() => {
        setTimeout(() => {
          const scrollAnchor = this.$refs.scrollAnchor;
          const chatMessages = this.$refs.chatMessages;
          
          if (scrollAnchor && chatMessages) {
            // 先尝试使用scrollIntoView滚动到锚点
            scrollAnchor.scrollIntoView({ 
              behavior: 'smooth',
              block: 'end'
            });
          } else if (chatMessages) {
            // 备用方案：强制滚动到底部
            chatMessages.scrollTop = chatMessages.scrollHeight;
          }
        }, 50);
      });
    },

    // 处理Enter键提交
    handleEnterKey(event) {
      // 只有在不是加载状态且有输入内容时才发送消息
      if (!this.loading.chat && this.userMessage.trim()) {
        this.sendMessage();
      }
    },

    // 强制滚动到底部（用于调试和确保滚动）
    forceScrollToBottom() {
      const chatMessages = this.$refs.chatMessages;
      if (chatMessages) {
        chatMessages.scrollTop = chatMessages.scrollHeight;
      }
    }
  }
};
</script>

<style scoped>
.ai-assistant-card {
  min-height: 600px;
}

.card-title {
  font-size: 18px;
  font-weight: bold;
  color: #409EFF;
}

.function-buttons {
  margin-bottom: 20px;
}

.function-btn {
  width: 100%;
  height: 50px;
  font-size: 16px;
}

.chat-section {
  border: 1px solid #EBEEF5;
  border-radius: 4px;
  margin-bottom: 20px;
}

.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  border-bottom: 1px solid #EBEEF5;
  background-color: #F5F7FA;
}

.chat-header h3 {
  margin: 0;
  color: #409EFF;
}

.chat-messages {
  height: 400px;
  max-height: 400px;
  overflow-y: auto;
  overflow-x: hidden;
  padding: 15px 20px;
  background-color: #FAFAFA;
  scroll-behavior: smooth;
  /* 确保滚动条始终可见 */
  scrollbar-width: thin;
}

.messages-wrapper {
  min-height: 100%;
  display: flex;
  flex-direction: column;
}

.scroll-anchor {
  height: 1px;
  flex-shrink: 0;
}

.message-item {
  margin-bottom: 15px;
}

.message {
  display: flex;
  align-items: flex-start;
}

.message.user {
  flex-direction: row-reverse;
}

.message-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  margin: 0 10px;
}

.message.user .message-avatar {
  background-color: #409EFF;
  color: white;
}

.message.ai .message-avatar {
  background-color: #67C23A;
  color: white;
}

.message-content {
  max-width: 70%;
  background-color: white;
  padding: 12px 16px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.message.user .message-content {
  background-color: #409EFF;
  color: white;
}

.message-text {
  line-height: 1.5;
  word-wrap: break-word;
  word-break: break-word;
  white-space: pre-wrap;
  max-width: 100%;
  overflow-wrap: break-word;
}

.message-time {
  font-size: 12px;
  color: #999;
  margin-top: 5px;
}

.message.user .message-time {
  color: rgba(255,255,255,0.7);
}

.typing-indicator {
  display: flex;
  align-items: center;
}

.typing-indicator span {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background-color: #409EFF;
  margin: 0 2px;
  animation: typing 1.4s infinite;
}

.typing-indicator span:nth-child(2) {
  animation-delay: 0.2s;
}

.typing-indicator span:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes typing {
  0%, 60%, 100% {
    transform: translateY(0);
    opacity: 0.5;
  }
  30% {
    transform: translateY(-10px);
    opacity: 1;
  }
}

.chat-input {
  padding: 15px 20px;
  border-top: 1px solid #EBEEF5;
  background-color: white;
}

.send-btn {
  width: 100%;
}

.analysis-section {
  border: 1px solid #EBEEF5;
  border-radius: 4px;
}

.analysis-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  border-bottom: 1px solid #EBEEF5;
  background-color: #F0F9FF;
}

.analysis-header h3 {
  margin: 0;
  color: #409EFF;
}

.analysis-content {
  padding: 20px;
}

.analysis-content h4 {
  color: #409EFF;
  margin: 15px 0 10px 0;
}

.analysis-content strong {
  color: #E6A23C;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .function-buttons .el-col {
    margin-bottom: 10px;
  }
  
  .message-content {
    max-width: 85%;
  }
}
</style>
