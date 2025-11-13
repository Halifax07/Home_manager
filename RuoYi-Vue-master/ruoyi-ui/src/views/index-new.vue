<template>
  <div class="modern-dashboard">
    <!-- 顶部欢迎区域 -->
    <div class="welcome-section">
      <div class="welcome-content">
        <div class="welcome-text">
          <h1 class="main-title">家庭财务管理系统</h1>
          <p class="subtitle">现代化的家庭理财解决方案</p>
          <div class="feature-tags">
            <el-tag type="success" effect="light">✨ 个人短学期项目</el-tag>
            <el-tag type="warning" effect="light">📊 数据可视化</el-tag>
            <el-tag type="info" effect="light">👨‍👩‍👧‍👦 多用户协同</el-tag>
          </div>
        </div>
        <div class="welcome-actions">
          <el-button type="primary" size="medium" icon="el-icon-s-data">
            查看报表
          </el-button>
          <el-button type="success" size="medium" icon="el-icon-plus">
            记录收支
          </el-button>
        </div>
      </div>
    </div>

    <!-- 数据统计卡片 -->
    <div class="stats-section">
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="6">
          <div class="stat-card income">
            <div class="stat-icon">
              <i class="el-icon-s-finance"></i>
            </div>
            <div class="stat-content">
              <h3>本月收入</h3>
              <p class="stat-value">¥12,580</p>
              <span class="stat-trend up">+15.2%</span>
            </div>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="6">
          <div class="stat-card expense">
            <div class="stat-icon">
              <i class="el-icon-shopping-cart-full"></i>
            </div>
            <div class="stat-content">
              <h3>本月支出</h3>
              <p class="stat-value">¥8,425</p>
              <span class="stat-trend down">-3.8%</span>
            </div>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="6">
          <div class="stat-card balance">
            <div class="stat-icon">
              <i class="el-icon-wallet"></i>
            </div>
            <div class="stat-content">
              <h3>账户余额</h3>
              <p class="stat-value">¥45,280</p>
              <span class="stat-trend stable">持平</span>
            </div>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="6">
          <div class="stat-card budget">
            <div class="stat-icon">
              <i class="el-icon-pie-chart"></i>
            </div>
            <div class="stat-content">
              <h3>预算剩余</h3>
              <p class="stat-value">¥6,720</p>
              <span class="stat-trend up">67%</span>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 主要功能区域 -->
    <div class="main-content">
      <el-row :gutter="20">
        <!-- 快速操作 -->
        <el-col :xs="24" :lg="8">
          <div class="content-card">
            <h3 class="card-title">
              <i class="el-icon-s-operation"></i>
              快速操作
            </h3>
            <div class="quick-actions">
              <div class="action-item" @click="handleQuickAction('income')">
                <div class="action-icon income">
                  <i class="el-icon-plus"></i>
                </div>
                <span>记录收入</span>
              </div>
              <div class="action-item" @click="handleQuickAction('expense')">
                <div class="action-icon expense">
                  <i class="el-icon-minus"></i>
                </div>
                <span>记录支出</span>
              </div>
              <div class="action-item" @click="handleQuickAction('transfer')">
                <div class="action-icon transfer">
                  <i class="el-icon-sort"></i>
                </div>
                <span>账户转账</span>
              </div>
              <div class="action-item" @click="handleQuickAction('budget')">
                <div class="action-icon budget">
                  <i class="el-icon-s-flag"></i>
                </div>
                <span>设置预算</span>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 最近交易 -->
        <el-col :xs="24" :lg="8">
          <div class="content-card">
            <h3 class="card-title">
              <i class="el-icon-time"></i>
              最近交易
            </h3>
            <div class="transaction-list">
              <div class="transaction-item" v-for="item in recentTransactions" :key="item.id">
                <div class="transaction-icon" :class="item.type">
                  <i :class="item.icon"></i>
                </div>
                <div class="transaction-info">
                  <p class="transaction-desc">{{ item.description }}</p>
                  <span class="transaction-time">{{ item.time }}</span>
                </div>
                <div class="transaction-amount" :class="item.type">
                  {{ item.type === 'income' ? '+' : '-' }}¥{{ item.amount }}
                </div>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 技术栈 -->
        <el-col :xs="24" :lg="8">
          <div class="content-card">
            <h3 class="card-title">
              <i class="el-icon-cpu"></i>
              技术栈
            </h3>
            <div class="tech-stack">
              <div class="tech-category">
                <h4>前端技术</h4>
                <div class="tech-tags">
                  <el-tag size="small">Vue.js</el-tag>
                  <el-tag size="small">Element UI</el-tag>
                  <el-tag size="small">Axios</el-tag>
                  <el-tag size="small">ECharts</el-tag>
                </div>
              </div>
              <div class="tech-category">
                <h4>后端技术</h4>
                <div class="tech-tags">
                  <el-tag size="small" type="success">Spring Boot</el-tag>
                  <el-tag size="small" type="success">MyBatis</el-tag>
                  <el-tag size="small" type="success">Redis</el-tag>
                  <el-tag size="small" type="success">MySQL</el-tag>
                </div>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
export default {
  name: "ModernDashboard",
  data() {
    return {
      // 模拟数据
      recentTransactions: [
        {
          id: 1,
          type: 'income',
          icon: 'el-icon-s-finance',
          description: '工资收入',
          amount: '8500.00',
          time: '2小时前'
        },
        {
          id: 2,
          type: 'expense',
          icon: 'el-icon-shopping-cart-full',
          description: '超市购物',
          amount: '156.80',
          time: '5小时前'
        },
        {
          id: 3,
          type: 'expense',
          icon: 'el-icon-house',
          description: '房租支出',
          amount: '2800.00',
          time: '1天前'
        },
        {
          id: 4,
          type: 'income',
          icon: 'el-icon-coin',
          description: '投资收益',
          amount: '320.50',
          time: '2天前'
        }
      ]
    }
  },
  methods: {
    handleQuickAction(type) {
      this.$message.success(`点击了${type}操作`)
      // 这里可以添加具体的操作逻辑
    }
  }
}
</script>

<style scoped lang="scss">
.modern-dashboard {
  padding: 0;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  min-height: 100vh;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;

  // 欢迎区域
  .welcome-section {
    background: linear-gradient(135deg, #ff9800 0%, #f57c00 100%);
    padding: 40px 20px;
    color: white;
    position: relative;
    overflow: hidden;
    
    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: radial-gradient(circle at 30% 30%, rgba(255, 255, 255, 0.1) 0%, transparent 70%);
      opacity: 0.3;
    }
    
    .welcome-content {
      max-width: 1200px;
      margin: 0 auto;
      display: flex;
      justify-content: space-between;
      align-items: center;
      position: relative;
      z-index: 1;
      
      .welcome-text {
        .main-title {
          font-size: 36px;
          font-weight: 700;
          margin: 0 0 10px 0;
          text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
        }
        
        .subtitle {
          font-size: 18px;
          margin: 0 0 20px 0;
          opacity: 0.9;
        }
        
        .feature-tags {
          display: flex;
          gap: 10px;
          flex-wrap: wrap;
        }
      }
      
      .welcome-actions {
        display: flex;
        gap: 15px;
        
        .el-button {
          padding: 12px 24px;
          border-radius: 25px;
          font-weight: 500;
          box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
          transition: all 0.3s ease;
          
          &:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3);
          }
        }
      }
    }
  }

  // 统计卡片区域
  .stats-section {
    padding: 20px;
    margin-top: -20px;
    position: relative;
    z-index: 2;
    
    .stat-card {
      background: white;
      border-radius: 16px;
      padding: 24px;
      box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
      transition: all 0.3s ease;
      display: flex;
      align-items: center;
      margin-bottom: 20px;
      
      &:hover {
        transform: translateY(-4px);
        box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
      }
      
      .stat-icon {
        width: 60px;
        height: 60px;
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 20px;
        
        i {
          font-size: 24px;
          color: white;
        }
      }
      
      .stat-content {
        flex: 1;
        
        h3 {
          margin: 0 0 8px 0;
          font-size: 14px;
          color: #666;
          font-weight: 500;
        }
        
        .stat-value {
          margin: 0 0 8px 0;
          font-size: 24px;
          font-weight: 700;
          color: #2c3e50;
        }
        
        .stat-trend {
          font-size: 12px;
          padding: 4px 8px;
          border-radius: 12px;
          font-weight: 500;
          
          &.up {
            background: #e8f5e8;
            color: #52c41a;
          }
          
          &.down {
            background: #ffeaea;
            color: #ff4d4f;
          }
          
          &.stable {
            background: #f0f2f5;
            color: #8c8c8c;
          }
        }
      }
      
      &.income .stat-icon {
        background: linear-gradient(135deg, #52c41a 0%, #73d13d 100%);
      }
      
      &.expense .stat-icon {
        background: linear-gradient(135deg, #ff4d4f 0%, #ff7875 100%);
      }
      
      &.balance .stat-icon {
        background: linear-gradient(135deg, #1890ff 0%, #40a9ff 100%);
      }
      
      &.budget .stat-icon {
        background: linear-gradient(135deg, #722ed1 0%, #9254de 100%);
      }
    }
  }

  // 主要内容区域
  .main-content {
    padding: 0 20px 40px;
    
    .content-card {
      background: white;
      border-radius: 16px;
      padding: 24px;
      box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
      transition: all 0.3s ease;
      height: 100%;
      
      &:hover {
        box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
      }
      
      .card-title {
        margin: 0 0 20px 0;
        font-size: 18px;
        font-weight: 600;
        color: #2c3e50;
        display: flex;
        align-items: center;
        
        i {
          margin-right: 8px;
          color: #ff9800;
        }
      }
    }
    
    // 快速操作
    .quick-actions {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 16px;
      
      .action-item {
        display: flex;
        flex-direction: column;
        align-items: center;
        padding: 20px;
        border-radius: 12px;
        background: #f8f9fa;
        cursor: pointer;
        transition: all 0.3s ease;
        
        &:hover {
          background: #e9ecef;
          transform: translateY(-2px);
        }
        
        .action-icon {
          width: 48px;
          height: 48px;
          border-radius: 12px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-bottom: 8px;
          
          i {
            font-size: 20px;
            color: white;
          }
          
          &.income {
            background: linear-gradient(135deg, #52c41a 0%, #73d13d 100%);
          }
          
          &.expense {
            background: linear-gradient(135deg, #ff4d4f 0%, #ff7875 100%);
          }
          
          &.transfer {
            background: linear-gradient(135deg, #1890ff 0%, #40a9ff 100%);
          }
          
          &.budget {
            background: linear-gradient(135deg, #722ed1 0%, #9254de 100%);
          }
        }
        
        span {
          font-size: 14px;
          font-weight: 500;
          color: #2c3e50;
        }
      }
    }
    
    // 交易列表
    .transaction-list {
      .transaction-item {
        display: flex;
        align-items: center;
        padding: 16px 0;
        border-bottom: 1px solid #f0f0f0;
        
        &:last-child {
          border-bottom: none;
        }
        
        .transaction-icon {
          width: 40px;
          height: 40px;
          border-radius: 10px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 12px;
          
          i {
            font-size: 16px;
            color: white;
          }
          
          &.income {
            background: linear-gradient(135deg, #52c41a 0%, #73d13d 100%);
          }
          
          &.expense {
            background: linear-gradient(135deg, #ff4d4f 0%, #ff7875 100%);
          }
        }
        
        .transaction-info {
          flex: 1;
          
          .transaction-desc {
            margin: 0 0 4px 0;
            font-size: 14px;
            font-weight: 500;
            color: #2c3e50;
          }
          
          .transaction-time {
            font-size: 12px;
            color: #8c8c8c;
          }
        }
        
        .transaction-amount {
          font-size: 16px;
          font-weight: 600;
          
          &.income {
            color: #52c41a;
          }
          
          &.expense {
            color: #ff4d4f;
          }
        }
      }
    }
    
    // 技术栈
    .tech-stack {
      .tech-category {
        margin-bottom: 20px;
        
        &:last-child {
          margin-bottom: 0;
        }
        
        h4 {
          margin: 0 0 12px 0;
          font-size: 16px;
          font-weight: 600;
          color: #2c3e50;
        }
        
        .tech-tags {
          display: flex;
          flex-wrap: wrap;
          gap: 8px;
          
          .el-tag {
            border-radius: 16px;
            padding: 4px 12px;
            font-size: 12px;
            border: none;
          }
        }
      }
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .modern-dashboard {
    .welcome-section .welcome-content {
      flex-direction: column;
      text-align: center;
      
      .welcome-actions {
        margin-top: 20px;
      }
    }
    
    .stats-section {
      margin-top: -10px;
    }
    
    .quick-actions {
      grid-template-columns: 1fr !important;
    }
  }
}
</style>
