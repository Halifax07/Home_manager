<template>
  <div class="app-container">
    <el-card class="chat-card">
      <div slot="header" class="chat-header">
        <span class="card-title">
          <i class="el-icon-chat-dot-square"></i>
          AI对话聊天
        </span>
        <el-button type="text" @click="clearChat" style="float: right;">
          <i class="el-icon-delete"></i>
          清空对话
        </el-button>
      </div>
      
      <!-- 聊天消息区域 -->
      <div class="chat-messages" ref="chatMessages">
        <div v-if="messages.length === 0" class="welcome-message">
          <i class="el-icon-service"></i>
          <p>您好！我是您的AI理财小助手</p>
          <p>您可以向我咨询任何关于家庭财务管理的问题</p>
          <div class="quick-questions">
            <el-tag 
              v-for="question in quickQuestions" 
              :key="question"
              @click="sendQuickQuestion(question)"
              class="quick-tag"
            >
              {{ question }}
            </el-tag>
          </div>
        </div>
        
        <div v-for="(message, index) in messages" :key="index" class="message-item" :class="message.type">
          <div class="message-avatar">
            <i v-if="message.type === 'user'" class="el-icon-user"></i>
            <i v-else class="el-icon-service"></i>
          </div>
          <div class="message-content">
            <div class="message-bubble" v-html="formatMessage(message.content)"></div>
            <div class="message-time">{{ message.time }}</div>
          </div>
        </div>
        
        <div v-if="typing" class="message-item assistant">
          <div class="message-avatar">
            <i class="el-icon-service"></i>
          </div>
          <div class="message-content">
            <div class="message-bubble typing-indicator">
              <span></span>
              <span></span>
              <span></span>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 输入区域 -->
      <div class="chat-input">
        <el-row :gutter="10">
          <el-col :span="2">
            <el-switch
              v-model="includeFinancialData"
              active-text="包含财务数据"
              inactive-text="普通对话"
              style="width: 100px;"
            ></el-switch>
          </el-col>
          <el-col :span="18">
            <el-input
              v-model="inputMessage"
              placeholder="请输入您想咨询的问题..."
              @keyup.enter="sendMessage"
              :disabled="sending"
              maxlength="500"
              show-word-limit
            />
          </el-col>
          <el-col :span="4">
            <el-button type="primary" @click="sendMessage" :loading="sending" :disabled="!inputMessage.trim()">
              发送
            </el-button>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>

<script>
import { aiChat } from "@/api/family/ai";

export default {
  name: "AiChat",
  data() {
    return {
      messages: [],
      inputMessage: '',
      sending: false,
      typing: false,
      includeFinancialData: false,
      sessionId: this.generateSessionId(),
      quickQuestions: [
        '如何制定合理的月度预算？',
        '我的支出结构是否合理？',
        '有什么好的理财建议吗？',
        '如何增加收入？'
      ]
    };
  },
  methods: {
    async sendMessage() {
      if (!this.inputMessage.trim() || this.sending) return;
      
      const userMessage = {
        type: 'user',
        content: this.inputMessage.trim(),
        time: this.formatTime(new Date())
      };
      
      this.messages.push(userMessage);
      const messageToSend = this.inputMessage.trim();
      this.inputMessage = '';
      this.sending = true;
      this.typing = true;
      
      this.scrollToBottom();
      
      try {
        const response = await aiChat({
          message: messageToSend,
          sessionId: this.sessionId,
          includeFinancialData: this.includeFinancialData
        });
        
        if (response.code === 200) {
          const aiMessage = {
            type: 'assistant',
            content: response.data.message,
            time: this.formatTime(new Date())
          };
          this.messages.push(aiMessage);
        } else {
          this.messages.push({
            type: 'assistant',
            content: '抱歉，我暂时无法回答您的问题，请稍后再试。',
            time: this.formatTime(new Date())
          });
          this.$message.error(response.msg || '发送消息失败');
        }
      } catch (error) {
        console.error('发送消息错误:', error);
        this.messages.push({
          type: 'assistant',
          content: '网络连接异常，请检查网络后重试。',
          time: this.formatTime(new Date())
        });
        this.$message.error('网络错误，请稍后重试');
      } finally {
        this.sending = false;
        this.typing = false;
        this.scrollToBottom();
      }
    },
    
    sendQuickQuestion(question) {
      this.inputMessage = question;
      this.sendMessage();
    },
    
    clearChat() {
      this.$confirm('确定要清空所有对话记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.messages = [];
        this.sessionId = this.generateSessionId();
        this.$message.success('对话记录已清空');
      }).catch(() => {});
    },
    
    formatMessage(text) {
      if (!text) return '';
      return text.replace(/\n/g, '<br/>').replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');
    },
    
    formatTime(date) {
      return date.toLocaleString('zh-CN', {
        hour: '2-digit',
        minute: '2-digit'
      });
    },
    
    generateSessionId() {
      return 'chat_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9);
    },
    
    scrollToBottom() {
      this.$nextTick(() => {
        const chatMessages = this.$refs.chatMessages;
        if (chatMessages) {
          chatMessages.scrollTop = chatMessages.scrollHeight;
        }
      });
    }
  }
};
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
}

.chat-card {
  height: calc(100vh - 160px);
  display: flex;
  flex-direction: column;
  
  .el-card__body {
    flex: 1;
    display: flex;
    flex-direction: column;
    padding: 0;
  }
}

.chat-header {
  .card-title {
    font-size: 18px;
    font-weight: 600;
    color: #303133;
  }
}

.chat-messages {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background-color: #f5f7fa;
  
  .welcome-message {
    text-align: center;
    padding: 60px 20px;
    color: #909399;
    
    i {
      font-size: 64px;
      color: #409EFF;
      margin-bottom: 20px;
    }
    
    p {
      margin: 10px 0;
      font-size: 16px;
      
      &:first-of-type {
        font-size: 18px;
        color: #303133;
        font-weight: 500;
      }
    }
    
    .quick-questions {
      margin-top: 30px;
      
      .quick-tag {
        margin: 5px;
        cursor: pointer;
        transition: all 0.3s;
        
        &:hover {
          background-color: #409EFF;
          color: white;
        }
      }
    }
  }
  
  .message-item {
    display: flex;
    margin-bottom: 20px;
    
    &.user {
      flex-direction: row-reverse;
      
      .message-content {
        align-items: flex-end;
        
        .message-bubble {
          background-color: #409EFF;
          color: white;
        }
      }
    }
    
    &.assistant {
      .message-bubble {
        background-color: white;
        color: #303133;
      }
    }
    
    .message-avatar {
      width: 40px;
      height: 40px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin: 0 10px;
      
      i {
        font-size: 20px;
        color: #909399;
      }
    }
    
    .message-content {
      flex: 1;
      display: flex;
      flex-direction: column;
      
      .message-bubble {
        max-width: 70%;
        padding: 12px 16px;
        border-radius: 18px;
        font-size: 14px;
        line-height: 1.5;
        word-wrap: break-word;
        box-shadow: 0 2px 4px rgba(0,0,0,0.1);
      }
      
      .message-time {
        font-size: 12px;
        color: #c0c4cc;
        margin-top: 5px;
        align-self: flex-end;
      }
    }
  }
}

.typing-indicator {
  display: flex;
  align-items: center;
  
  span {
    height: 8px;
    width: 8px;
    border-radius: 50%;
    background-color: #c0c4cc;
    margin: 0 2px;
    animation: typing 1.4s infinite ease-in-out;
    
    &:nth-child(1) { animation-delay: -0.32s; }
    &:nth-child(2) { animation-delay: -0.16s; }
  }
}

@keyframes typing {
  0%, 80%, 100% {
    transform: scale(0);
  }
  40% {
    transform: scale(1);
  }
}

.chat-input {
  padding: 20px;
  border-top: 1px solid #ebeef5;
  background-color: white;
}
</style>
