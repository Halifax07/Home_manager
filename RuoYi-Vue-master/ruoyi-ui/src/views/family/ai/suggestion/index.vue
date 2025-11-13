<template>
  <div class="app-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span class="card-title">
          <i class="el-icon-lightbulb"></i>
          智能预算建议
        </span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="getBudgetSuggestion" :loading="loading">
          <i class="el-icon-refresh"></i>
          获取建议
        </el-button>
      </div>
      
      <div class="suggestion-content">
        <div v-if="!suggestion && !loading" class="empty-state">
          <i class="el-icon-s-finance"></i>
          <p>点击"获取建议"按钮，AI将为您制定个性化预算方案</p>
        </div>
        
        <div v-if="loading" class="loading-state">
          <el-skeleton :rows="6" animated />
        </div>
        
        <div v-if="suggestion && !loading" class="suggestion-result">
          <div class="suggestion-text" v-html="formatSuggestion(suggestion)"></div>
          <div class="suggestion-footer">
            <el-tag type="primary" size="mini">
              <i class="el-icon-time"></i>
              生成时间: {{ currentTime }}
            </el-tag>
          </div>
        </div>
      </div>
    </el-card>
    
    <!-- 预算分配模板 -->
    <el-card class="template-card" style="margin-top: 20px;">
      <div slot="header">
        <i class="el-icon-document-copy"></i>
        经典预算分配模板
      </div>
      <el-row :gutter="20">
        <el-col :span="8" v-for="template in budgetTemplates" :key="template.name">
          <div class="template-item">
            <div class="template-header">
              <h3>{{ template.name }}</h3>
              <p class="template-desc">{{ template.description }}</p>
            </div>
            <div class="template-allocation">
              <div 
                v-for="item in template.allocation" 
                :key="item.category"
                class="allocation-item"
                :style="{ backgroundColor: item.color + '20', borderLeft: '3px solid ' + item.color }"
              >
                <span class="category">{{ item.category }}</span>
                <span class="percentage">{{ item.percentage }}</span>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>
    
    <!-- 预算制定步骤 -->
    <el-card class="steps-card" style="margin-top: 20px;">
      <div slot="header">
        <i class="el-icon-guide"></i>
        预算制定步骤
      </div>
      <el-steps :active="4" finish-status="success">
        <el-step
          v-for="step in budgetSteps"
          :key="step.title"
          :title="step.title"
          :description="step.description"
          :icon="step.icon"
        ></el-step>
      </el-steps>
    </el-card>
  </div>
</template>

<script>
import { getBudgetSuggestion } from "@/api/family/ai";

export default {
  name: "AiSuggestion",
  data() {
    return {
      suggestion: '',
      loading: false,
      currentTime: '',
      budgetTemplates: [
        {
          name: '50/30/20 法则',
          description: '经典理财法则，适合大多数人',
          allocation: [
            { category: '必需品', percentage: '50%', color: '#67C23A' },
            { category: '娱乐消费', percentage: '30%', color: '#E6A23C' },
            { category: '储蓄投资', percentage: '20%', color: '#409EFF' }
          ]
        },
        {
          name: '保守型预算',
          description: '适合收入不稳定或刚工作的人群',
          allocation: [
            { category: '生活必需', percentage: '60%', color: '#67C23A' },
            { category: '娱乐消费', percentage: '20%', color: '#E6A23C' },
            { category: '储蓄应急', percentage: '20%', color: '#409EFF' }
          ]
        },
        {
          name: '积极型预算',
          description: '适合有稳定收入且想快速积累财富',
          allocation: [
            { category: '基本开销', percentage: '40%', color: '#67C23A' },
            { category: '生活品质', percentage: '20%', color: '#E6A23C' },
            { category: '投资理财', percentage: '40%', color: '#409EFF' }
          ]
        }
      ],
      budgetSteps: [
        {
          title: '计算收入',
          description: '统计所有收入来源',
          icon: 'el-icon-plus'
        },
        {
          title: '列出支出',
          description: '记录所有必要和非必要支出',
          icon: 'el-icon-minus'
        },
        {
          title: '分类整理',
          description: '将支出按类别分组管理',
          icon: 'el-icon-menu'
        },
        {
          title: '设定比例',
          description: '为每个类别设定支出比例',
          icon: 'el-icon-pie-chart'
        },
        {
          title: '执行监控',
          description: '按预算执行并定期检查调整',
          icon: 'el-icon-view'
        }
      ]
    };
  },
  methods: {
    async getBudgetSuggestion() {
      this.loading = true;
      try {
        const response = await getBudgetSuggestion();
        if (response.code === 200) {
          this.suggestion = response.data;
          this.currentTime = this.formatTime(new Date());
          this.$message.success('预算建议生成成功！');
        } else {
          this.$message.error(response.msg || '获取预算建议失败');
        }
      } catch (error) {
        console.error('获取预算建议错误:', error);
        this.$message.error('网络错误，请稍后重试');
      } finally {
        this.loading = false;
      }
    },
    
    formatSuggestion(text) {
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

.suggestion-content {
  min-height: 300px;
}

.empty-state {
  text-align: center;
  color: #909399;
  padding: 60px 0;
  
  i {
    font-size: 64px;
    color: #909399;
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

.suggestion-result {
  .suggestion-text {
    font-size: 14px;
    line-height: 1.8;
    color: #606266;
    padding: 20px;
    background-color: #f9f9f9;
    border-radius: 6px;
    border-left: 4px solid #909399;
  }
  
  .suggestion-footer {
    text-align: right;
    margin-top: 15px;
  }
}

.template-card {
  .template-item {
    border: 1px solid #ebeef5;
    border-radius: 8px;
    overflow: hidden;
    margin-bottom: 20px;
    
    .template-header {
      padding: 15px;
      background-color: #fafafa;
      border-bottom: 1px solid #ebeef5;
      
      h3 {
        margin: 0 0 5px 0;
        color: #303133;
        font-size: 16px;
      }
      
      .template-desc {
        margin: 0;
        color: #606266;
        font-size: 12px;
      }
    }
    
    .template-allocation {
      padding: 15px;
      
      .allocation-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 8px 12px;
        margin-bottom: 8px;
        border-radius: 4px;
        
        .category {
          font-size: 14px;
          color: #303133;
        }
        
        .percentage {
          font-weight: 600;
          color: #409EFF;
        }
      }
    }
  }
}

.steps-card {
  .el-steps {
    margin: 20px 0;
  }
}
</style>
