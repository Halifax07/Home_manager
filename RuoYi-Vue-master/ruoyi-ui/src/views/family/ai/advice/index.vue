<template>
  <div class="app-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span class="card-title">
          <i class="el-icon-money"></i>
          智能理财建议
        </span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="getAdvice" :loading="loading">
          <i class="el-icon-refresh"></i>
          获取建议
        </el-button>
      </div>
      
      <div class="advice-content">
        <div v-if="!advice && !loading" class="empty-state">
          <i class="el-icon-lightbulb"></i>
          <p>点击"获取建议"按钮，AI将基于您的财务数据为您提供个性化理财建议</p>
        </div>
        
        <div v-if="loading" class="loading-state">
          <el-skeleton :rows="5" animated />
        </div>
        
        <div v-if="advice && !loading" class="advice-result">
          <div class="advice-text" v-html="formatAdvice(advice)"></div>
          <div class="advice-footer">
            <el-tag type="info" size="mini">
              <i class="el-icon-time"></i>
              生成时间: {{ currentTime }}
            </el-tag>
          </div>
        </div>
      </div>
    </el-card>
    
    <!-- 理财小贴士 -->
    <el-card class="tips-card" style="margin-top: 20px;">
      <div slot="header">
        <i class="el-icon-star-on"></i>
        理财小贴士
      </div>
      <el-row :gutter="20">
        <el-col :span="8" v-for="tip in tips" :key="tip.title">
          <div class="tip-item">
            <i :class="tip.icon"></i>
            <h4>{{ tip.title }}</h4>
            <p>{{ tip.content }}</p>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import { getFinancialAdvice } from "@/api/family/ai";

export default {
  name: "AiAdvice",
  data() {
    return {
      advice: '',
      loading: false,
      currentTime: '',
      tips: [
        {
          icon: 'el-icon-pie-chart',
          title: '合理分配',
          content: '建议将收入按50/30/20法则分配：50%生活必需，30%娱乐，20%储蓄。'
        },
        {
          icon: 'el-icon-bank-card',
          title: '应急基金',
          content: '保持3-6个月生活费的应急资金，应对突发情况。'
        },
        {
          icon: 'el-icon-trend-charts',
          title: '长期投资',
          content: '考虑长期投资规划，如基金定投、保险等理财产品。'
        }
      ]
    };
  },
  methods: {
    async getAdvice() {
      this.loading = true;
      try {
        console.log('开始请求理财建议...');
        const response = await getFinancialAdvice();
        console.log('API响应:', response);
        console.log('响应代码:', response.code);
        console.log('响应数据:', response.data);
        console.log('数据类型:', typeof response.data);
        console.log('数据长度:', response.data ? response.data.length : 0);
        
        if (response.code === 200) {
          this.advice = response.data;
          this.currentTime = this.formatTime(new Date());
          console.log('设置advice:', this.advice);
          this.$message.success('理财建议获取成功！');
        } else {
          console.log('API返回错误:', response.msg);
          this.$message.error(response.msg || '获取理财建议失败');
        }
      } catch (error) {
        console.error('获取理财建议错误:', error);
        this.$message.error('网络错误，请稍后重试');
      } finally {
        this.loading = false;
        console.log('请求完成，loading状态:', this.loading);
        console.log('当前advice值:', this.advice);
      }
    },
    
    formatAdvice(text) {
      if (!text) return '';
      return text.replace(/\n/g, '<br/>').replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');
    },
    
    formatTime(date) {
      return date.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      });
    }
  }
};
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
}

.card-title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.advice-content {
  min-height: 300px;
}

.empty-state {
  text-align: center;
  color: #909399;
  padding: 60px 0;
  
  i {
    font-size: 64px;
    color: #E6A23C;
    margin-bottom: 20px;
  }
  
  p {
    font-size: 14px;
    line-height: 1.6;
  }
}

.loading-state {
  padding: 20px;
}

.advice-result {
  .advice-text {
    font-size: 14px;
    line-height: 1.8;
    color: #606266;
    padding: 20px;
    background-color: #f8f9fa;
    border-radius: 6px;
    border-left: 4px solid #409EFF;
  }
  
  .advice-footer {
    text-align: right;
    margin-top: 15px;
  }
}

.tips-card {
  .tip-item {
    text-align: center;
    padding: 20px;
    
    i {
      font-size: 32px;
      color: #67C23A;
      margin-bottom: 10px;
    }
    
    h4 {
      margin: 10px 0;
      color: #303133;
      font-size: 16px;
    }
    
    p {
      color: #606266;
      font-size: 12px;
      line-height: 1.5;
    }
  }
}
</style>
