<template>
  <div class="app-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span class="card-title">
          <i class="el-icon-pie-chart"></i>
          预算分析
        </span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="getBudgetAnalysis" :loading="loading">
          <i class="el-icon-refresh"></i>
          分析预算
        </el-button>
      </div>
      
      <div class="analysis-content">
        <div v-if="!analysis && !loading" class="empty-state">
          <i class="el-icon-data-analysis"></i>
          <p>点击"分析预算"按钮，AI将分析您的预算执行情况</p>
        </div>
        
        <div v-if="loading" class="loading-state">
          <el-skeleton :rows="6" animated />
        </div>
        
        <div v-if="analysis && !loading" class="analysis-result">
          <div class="analysis-text" v-html="formatAnalysis(analysis)"></div>
          <div class="analysis-footer">
            <el-tag type="success" size="mini">
              <i class="el-icon-time"></i>
              分析时间: {{ currentTime }}
            </el-tag>
          </div>
        </div>
      </div>
    </el-card>
    
    <!-- 预算管理技巧 -->
    <el-card class="tips-card" style="margin-top: 20px;">
      <div slot="header">
        <i class="el-icon-document"></i>
        预算管理技巧
      </div>
      <el-row :gutter="20">
        <el-col :span="12" v-for="technique in techniques" :key="technique.title">
          <div class="technique-item">
            <div class="technique-header">
              <i :class="technique.icon"></i>
              <h4>{{ technique.title }}</h4>
            </div>
            <p>{{ technique.content }}</p>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import { getBudgetAnalysis } from "@/api/family/ai";

export default {
  name: "AiBudget",
  data() {
    return {
      analysis: '',
      loading: false,
      currentTime: '',
      techniques: [
        {
          icon: 'el-icon-edit-outline',
          title: '记录每笔支出',
          content: '养成记录每笔收支的习惯，了解资金流向，为预算调整提供数据支持。'
        },
        {
          icon: 'el-icon-warning-outline',
          title: '设置预算警报',
          content: '为各个支出类别设置预算上限，接近限额时及时提醒自己控制开支。'
        },
        {
          icon: 'el-icon-date',
          title: '定期回顾调整',
          content: '每月回顾预算执行情况，根据实际生活需要调整下月预算安排。'
        },
        {
          icon: 'el-icon-present',
          title: '设立奖励机制',
          content: '完成预算目标时给自己小奖励，增强预算执行的积极性和持续性。'
        }
      ]
    };
  },
  methods: {
    async getBudgetAnalysis() {
      this.loading = true;
      try {
        const response = await getBudgetAnalysis();
        if (response.code === 200) {
          this.analysis = response.data;
          this.currentTime = this.formatTime(new Date());
          this.$message.success('预算分析完成！');
        } else {
          this.$message.error(response.msg || '获取预算分析失败');
        }
      } catch (error) {
        console.error('获取预算分析错误:', error);
        this.$message.error('网络错误，请稍后重试');
      } finally {
        this.loading = false;
      }
    },
    
    formatAnalysis(text) {
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

.analysis-content {
  min-height: 300px;
}

.empty-state {
  text-align: center;
  color: #909399;
  padding: 60px 0;
  
  i {
    font-size: 64px;
    color: #409EFF;
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

.analysis-result {
  .analysis-text {
    font-size: 14px;
    line-height: 1.8;
    color: #606266;
    padding: 20px;
    background-color: #f0f9ff;
    border-radius: 6px;
    border-left: 4px solid #409EFF;
  }
  
  .analysis-footer {
    text-align: right;
    margin-top: 15px;
  }
}

.tips-card {
  .technique-item {
    padding: 15px;
    margin-bottom: 15px;
    background-color: #fafafa;
    border-radius: 8px;
    border-left: 3px solid #67C23A;
    
    .technique-header {
      display: flex;
      align-items: center;
      margin-bottom: 8px;
      
      i {
        font-size: 20px;
        color: #67C23A;
        margin-right: 8px;
      }
      
      h4 {
        margin: 0;
        color: #303133;
        font-size: 14px;
      }
    }
    
    p {
      color: #606266;
      font-size: 12px;
      line-height: 1.5;
      margin: 0;
    }
  }
}
</style>
