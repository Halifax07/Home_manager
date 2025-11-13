<template>
  <div class="app-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span class="card-title">
          <i class="el-icon-trend-charts"></i>
          收支趋势分析
        </span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="getTrendAnalysis" :loading="loading">
          <i class="el-icon-refresh"></i>
          分析趋势
        </el-button>
      </div>
      
      <div class="trend-content">
        <div v-if="!analysis && !loading" class="empty-state">
          <i class="el-icon-data-line"></i>
          <p>点击"分析趋势"按钮，AI将分析您的收支趋势变化</p>
        </div>
        
        <div v-if="loading" class="loading-state">
          <el-skeleton :rows="6" animated />
        </div>
        
        <div v-if="analysis && !loading" class="trend-result">
          <div class="trend-text" v-html="formatAnalysis(analysis)"></div>
          <div class="trend-footer">
            <el-tag type="warning" size="mini">
              <i class="el-icon-time"></i>
              分析时间: {{ currentTime }}
            </el-tag>
          </div>
        </div>
      </div>
    </el-card>
    
    <!-- 趋势分析指南 -->
    <el-card class="guide-card" style="margin-top: 20px;">
      <div slot="header">
        <i class="el-icon-info"></i>
        趋势分析指南
      </div>
      <el-row :gutter="20">
        <el-col :span="8" v-for="guide in guides" :key="guide.title">
          <div class="guide-item">
            <div class="guide-icon">
              <i :class="guide.icon"></i>
            </div>
            <h4>{{ guide.title }}</h4>
            <p>{{ guide.content }}</p>
          </div>
        </el-col>
      </el-row>
    </el-card>
    
    <!-- 趋势改善建议 -->
    <el-card class="suggestions-card" style="margin-top: 20px;">
      <div slot="header">
        <i class="el-icon-magic-stick"></i>
        趋势改善建议
      </div>
      <el-timeline>
        <el-timeline-item
          v-for="suggestion in suggestions"
          :key="suggestion.title"
          :icon="suggestion.icon"
          :color="suggestion.color"
        >
          <h4>{{ suggestion.title }}</h4>
          <p>{{ suggestion.content }}</p>
        </el-timeline-item>
      </el-timeline>
    </el-card>
  </div>
</template>

<script>
import { getTrendAnalysis } from "@/api/family/ai";

export default {
  name: "AiTrend",
  data() {
    return {
      analysis: '',
      loading: false,
      currentTime: '',
      guides: [
        {
          icon: 'el-icon-arrow-up',
          title: '收入趋势',
          content: '观察收入的增长趋势，识别收入来源的稳定性和成长性。'
        },
        {
          icon: 'el-icon-arrow-down',
          title: '支出趋势',
          content: '分析支出变化模式，找出支出增长的主要原因和控制点。'
        },
        {
          icon: 'el-icon-pie-chart',
          title: '结构变化',
          content: '关注收支结构的变化，优化资金配置提高财务健康度。'
        }
      ],
      suggestions: [
        {
          title: '建立收入增长计划',
          content: '制定多元化收入策略，降低单一收入来源风险，寻找被动收入机会。',
          icon: 'el-icon-plus',
          color: '#67C23A'
        },
        {
          title: '优化支出结构',
          content: '区分必要支出和可选支出，优先保障必需品，控制非必要开销。',
          icon: 'el-icon-minus',
          color: '#E6A23C'
        },
        {
          title: '建立储蓄习惯',
          content: '设定固定储蓄比例，优先储蓄后消费，建立长期财富积累机制。',
          icon: 'el-icon-coin',
          color: '#409EFF'
        },
        {
          title: '定期调整策略',
          content: '根据趋势变化及时调整理财策略，保持财务计划的灵活性和有效性。',
          icon: 'el-icon-refresh',
          color: '#F56C6C'
        }
      ]
    };
  },
  methods: {
    async getTrendAnalysis() {
      this.loading = true;
      try {
        const response = await getTrendAnalysis();
        if (response.code === 200) {
          this.analysis = response.data;
          this.currentTime = this.formatTime(new Date());
          this.$message.success('趋势分析完成！');
        } else {
          this.$message.error(response.msg || '获取趋势分析失败');
        }
      } catch (error) {
        console.error('获取趋势分析错误:', error);
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

.trend-content {
  min-height: 300px;
}

.empty-state {
  text-align: center;
  color: #909399;
  padding: 60px 0;
  
  i {
    font-size: 64px;
    color: #F56C6C;
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

.trend-result {
  .trend-text {
    font-size: 14px;
    line-height: 1.8;
    color: #606266;
    padding: 20px;
    background-color: #fff5f5;
    border-radius: 6px;
    border-left: 4px solid #F56C6C;
  }
  
  .trend-footer {
    text-align: right;
    margin-top: 15px;
  }
}

.guide-card {
  .guide-item {
    text-align: center;
    padding: 20px;
    
    .guide-icon {
      margin-bottom: 15px;
      
      i {
        font-size: 36px;
        color: #E6A23C;
      }
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

.suggestions-card {
  .el-timeline-item {
    h4 {
      color: #303133;
      margin: 0 0 8px 0;
      font-size: 16px;
    }
    
    p {
      color: #606266;
      font-size: 14px;
      line-height: 1.6;
      margin: 0;
    }
  }
}
</style>
