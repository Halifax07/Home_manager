<template>
  <div class="app-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span class="card-title">AI小管家</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="refreshAllData">
          <i class="el-icon-refresh"></i> 刷新数据
        </el-button>
      </div>

      <!-- 功能选项卡 -->
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <!-- 智能理财建议 -->
        <el-tab-pane label="智能理财建议" name="advice">
          <div class="analysis-section">
            <el-button 
              type="primary" 
              :loading="loadingAdvice" 
              @click="getFinancialAdvice"
              icon="el-icon-magic-stick">
              生成理财建议
            </el-button>
            <el-card v-if="financialAdvice" class="result-card" shadow="never">
              <div class="advice-content" v-html="formatContent(financialAdvice)"></div>
            </el-card>
          </div>
        </el-tab-pane>

        <!-- 预算分析 -->
        <el-tab-pane label="预算分析" name="budget">
          <div class="analysis-section">
            <el-button 
              type="success" 
              :loading="loadingBudget" 
              @click="getBudgetAnalysis"
              icon="el-icon-pie-chart">
              生成预算分析
            </el-button>
            <el-card v-if="budgetAnalysis" class="result-card" shadow="never">
              <div class="analysis-content" v-html="formatContent(budgetAnalysis)"></div>
            </el-card>
          </div>
        </el-tab-pane>

        <!-- 收支趋势分析 -->
        <el-tab-pane label="收支趋势" name="trend">
          <div class="analysis-section">
            <el-button 
              type="warning" 
              :loading="loadingTrend" 
              @click="getTrendAnalysis"
              icon="el-icon-trend-charts">
              生成趋势分析
            </el-button>
            <el-card v-if="trendAnalysis" class="result-card" shadow="never">
              <div class="trend-content" v-html="formatContent(trendAnalysis)"></div>
            </el-card>
          </div>
        </el-tab-pane>

        <!-- AI对话 -->
        <el-tab-pane label="AI对话" name="chat">
          <div class="chat-section">
            <div class="chat-container">
              <div class="chat-messages" ref="chatMessages">
                <div 
                  v-for="(message, index) in chatMessages" 
                  :key="index" 
                  :class="['message', message.type]">
                  <div class="message-content">
                    <div class="message-text" v-html="formatContent(message.content)"></div>
                    <div class="message-time">{{ message.time }}</div>
                  </div>
                </div>
              </div>
              <div class="chat-input">
                <el-row :gutter="10">
                  <el-col :span="2">
                    <el-checkbox v-model="includeFinancialData">包含财务数据</el-checkbox>
                  </el-col>
                  <el-col :span="18">
                    <el-input
                      v-model="chatInput"
                      placeholder="请输入您的问题..."
                      @keyup.enter.native="handleEnterKey"
                      :disabled="loadingChat">
                    </el-input>
                  </el-col>
                  <el-col :span="4">
                    <el-button 
                      type="primary" 
                      :loading="loadingChat" 
                      @click="sendMessage"
                      :disabled="!chatInput.trim() || loadingChat"
                      icon="el-icon-s-promotion">
                      {{ loadingChat ? '发送中...' : '发送' }}
                    </el-button>
                  </el-col>
                </el-row>
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 智能预算建议 -->
        <el-tab-pane label="预算建议" name="budgetSuggestion">
          <div class="analysis-section">
            <el-button 
              type="info" 
              :loading="loadingBudgetSuggestion" 
              @click="getBudgetSuggestion"
              icon="el-icon-light-rain">
              生成预算建议
            </el-button>
            <el-card v-if="budgetSuggestion" class="result-card" shadow="never">
              <div class="suggestion-content" v-html="formatContent(budgetSuggestion)"></div>
            </el-card>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 使用说明 -->
    <el-card class="box-card usage-guide" style="margin-top: 20px;">
      <div slot="header" class="clearfix">
        <span class="card-title">使用说明</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="12">
          <h4>功能介绍：</h4>
          <ul>
            <li><strong>智能理财建议：</strong>基于您的收支数据，提供个性化的理财建议</li>
            <li><strong>预算分析：</strong>分析当前预算执行情况，识别超支风险</li>
            <li><strong>收支趋势：</strong>分析收支变化趋势，预测未来财务状况</li>
            <li><strong>AI对话：</strong>与AI助手自由对话，获取定制化财务建议</li>
            <li><strong>预算建议：</strong>根据历史数据智能推荐合适的预算方案</li>
          </ul>
        </el-col>
        <el-col :span="12">
          <h4>使用提示：</h4>
          <ul>
            <li>确保已有足够的收支记录数据，以获得更准确的分析</li>
            <li>AI对话时可选择是否包含财务数据进行更精准的分析</li>
            <li>建议定期查看分析结果，及时调整财务规划</li>
            <li>如需更详细的分析，可在AI对话中提出具体问题</li>
          </ul>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import { getFinancialAdvice, getBudgetAnalysis, getTrendAnalysis, chatWithAI, getBudgetSuggestion } from "@/api/family/aiAssistant";

export default {
  name: "AiAssistant",
  data() {
    return {
      activeTab: "advice",
      loadingAdvice: false,
      loadingBudget: false,
      loadingTrend: false,
      loadingChat: false,
      loadingBudgetSuggestion: false,
      financialAdvice: "",
      budgetAnalysis: "",
      trendAnalysis: "",
      budgetSuggestion: "",
      chatMessages: [],
      chatInput: "",
      includeFinancialData: false,
      sessionId: this.generateSessionId()
    };
  },
  watch: {
    // 监听消息数组变化，自动滚动到底部
    chatMessages: {
      handler() {
        this.$nextTick(() => {
          this.scrollToBottom();
        });
      },
      deep: true
    }
  },
  methods: {
    // 生成会话ID
    generateSessionId() {
      return 'session_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9);
    },

    // 获取智能理财建议
    async getFinancialAdvice() {
      this.loadingAdvice = true;
      try {
        const response = await getFinancialAdvice();
        this.financialAdvice = response.data;
        this.$message.success("理财建议生成成功");
      } catch (error) {
        this.$message.error("获取理财建议失败：" + (error.message || "未知错误"));
      } finally {
        this.loadingAdvice = false;
      }
    },

    // 获取预算分析
    async getBudgetAnalysis() {
      this.loadingBudget = true;
      try {
        const response = await getBudgetAnalysis();
        this.budgetAnalysis = response.data;
        this.$message.success("预算分析生成成功");
      } catch (error) {
        this.$message.error("获取预算分析失败：" + (error.message || "未知错误"));
      } finally {
        this.loadingBudget = false;
      }
    },

    // 获取收支趋势分析
    async getTrendAnalysis() {
      this.loadingTrend = true;
      try {
        const response = await getTrendAnalysis();
        this.trendAnalysis = response.data;
        this.$message.success("趋势分析生成成功");
      } catch (error) {
        this.$message.error("获取趋势分析失败：" + (error.message || "未知错误"));
      } finally {
        this.loadingTrend = false;
      }
    },

    // 获取智能预算建议
    async getBudgetSuggestion() {
      this.loadingBudgetSuggestion = true;
      try {
        const response = await getBudgetSuggestion();
        this.budgetSuggestion = response.data;
        this.$message.success("预算建议生成成功");
      } catch (error) {
        this.$message.error("获取预算建议失败：" + (error.message || "未知错误"));
      } finally {
        this.loadingBudgetSuggestion = false;
      }
    },

    // 发送消息
    async sendMessage() {
      if (!this.chatInput.trim()) {
        this.$message.warning("请输入消息内容");
        return;
      }

      const userMessage = {
        type: "user",
        content: this.chatInput,
        time: this.formatTime(new Date())
      };
      this.chatMessages.push(userMessage);

      const message = this.chatInput;
      this.chatInput = "";
      this.loadingChat = true;

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

        const aiMessage = {
          type: "ai",
          content: response.data.response || response.data,
          time: this.formatTime(new Date())
        };
        this.chatMessages.push(aiMessage);
        
        // AI回复后再次滚动到底部
        this.$nextTick(() => {
          this.scrollToBottom();
        });
      } catch (error) {
        const errorMessage = {
          type: "ai",
          content: "抱歉，AI服务暂时不可用：" + (error.message || "未知错误"),
          time: this.formatTime(new Date())
        };
        this.chatMessages.push(errorMessage);
        
        // 错误消息也需要滚动到底部
        this.$nextTick(() => {
          this.scrollToBottom();
        });
      } finally {
        this.loadingChat = false;
      }
    },

    // 滚动到底部
    scrollToBottom() {
      this.$nextTick(() => {
        setTimeout(() => {
          const container = this.$refs.chatMessages;
          if (container) {
            // 强制滚动到底部，确保能看到最新消息
            container.scrollTo({
              top: container.scrollHeight,
              behavior: 'smooth'
            });
          }
        }, 100); // 延迟100ms确保DOM完全渲染
      });
    },

    // 格式化时间
    formatTime(date) {
      const hours = date.getHours().toString().padStart(2, '0');
      const minutes = date.getMinutes().toString().padStart(2, '0');
      return `${hours}:${minutes}`;
    },

    // 格式化内容显示
    formatContent(content) {
      if (!content) return "";
      
      // 将换行符转换为HTML换行
      let formatted = content.replace(/\n/g, '<br>');
      
      // 处理数字列表
      formatted = formatted.replace(/(\d+\.\s)/g, '<strong>$1</strong>');
      
      // 处理项目符号
      formatted = formatted.replace(/([•·]\s)/g, '<strong>$1</strong>');
      
      // 处理标题（以冒号结尾的行）
      formatted = formatted.replace(/^(.+：)$/gm, '<h4 style="margin: 10px 0 5px 0; color: #409EFF;">$1</h4>');
      
      return formatted;
    },

    // 标签点击处理
    handleTabClick(tab) {
      this.activeTab = tab.name;
    },

    // 刷新所有数据
    refreshAllData() {
      this.financialAdvice = "";
      this.budgetAnalysis = "";
      this.trendAnalysis = "";
      this.budgetSuggestion = "";
      this.chatMessages = [];
      this.sessionId = this.generateSessionId();
      this.$message.success("数据已清空，请重新生成分析");
    },

    // 处理Enter键提交
    handleEnterKey(event) {
      // 只有在不是加载状态且有输入内容时才发送消息
      if (!this.loadingChat && this.chatInput.trim()) {
        this.sendMessage();
      }
    }
  }
};
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.card-title {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
}

.analysis-section {
  padding: 20px 0;
}

.result-card {
  margin-top: 20px;
  border: 1px solid #EBEEF5;
}

.advice-content,
.analysis-content,
.trend-content,
.suggestion-content {
  line-height: 1.8;
  font-size: 14px;
  color: #606266;
  white-space: pre-wrap;
}

.chat-section {
  padding: 20px 0;
}

.chat-container {
  border: 1px solid #EBEEF5;
  border-radius: 4px;
  height: 500px;
  display: flex;
  flex-direction: column;
}

.chat-messages {
  flex: 1;
  padding: 15px;
  overflow-y: auto;
  background-color: #fafafa;
  scroll-behavior: smooth;
}

.message {
  margin-bottom: 15px;
  display: flex;
}

.message.user {
  justify-content: flex-end;
}

.message.ai {
  justify-content: flex-start;
}

.message-content {
  max-width: 70%;
  padding: 10px 15px;
  border-radius: 10px;
  position: relative;
}

.message.user .message-content {
  background-color: #409EFF;
  color: white;
}

.message.ai .message-content {
  background-color: white;
  border: 1px solid #E4E7ED;
  color: #606266;
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
  margin-top: 5px;
  opacity: 0.7;
}

.chat-input {
  padding: 15px;
  border-top: 1px solid #EBEEF5;
  background-color: white;
}

.usage-guide h4 {
  color: #409EFF;
  margin-bottom: 10px;
}

.usage-guide ul {
  padding-left: 20px;
}

.usage-guide li {
  margin-bottom: 8px;
  line-height: 1.5;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .message-content {
    max-width: 85%;
  }
  
  .chat-container {
    height: 400px;
  }
}
</style>
