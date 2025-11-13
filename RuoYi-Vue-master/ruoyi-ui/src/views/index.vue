<template>
  <div class="dashboard-container">
    <!-- 顶部横幅 -->
    <div class="hero-section">
      <div class="hero-content">
        <h1 class="main-title">家庭财务管理系统</h1>
        <p class="subtitle">基于Vue.js + Spring Boot构建的现代化财务管理解决方案</p>
        <div class="feature-badges">
          <el-tag type="primary" effect="plain">前后端分离</el-tag>
          <el-tag type="success" effect="plain">数据可视化</el-tag>
          <el-tag type="warning" effect="plain">权限控制</el-tag>
          <el-tag type="info" effect="plain">响应式设计</el-tag>
        </div>
      </div>
    </div>

    <!-- 功能特色 -->
    <div class="features-container">
      <div class="container">
        <h2 class="section-title">核心功能</h2>
        <el-row :gutter="24">
          <el-col :xs="24" :sm="12" :lg="8">
            <div class="feature-card">
              <div class="feature-icon">
                <i class="el-icon-s-finance"></i>
              </div>
              <h3>收支管理</h3>
              <p>全面记录家庭收入支出，支持多账户、多分类管理</p>
            </div>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="8">
            <div class="feature-card">
              <div class="feature-icon">
                <i class="el-icon-pie-chart"></i>
              </div>
              <h3>预算控制</h3>
              <p>智能预算设置与监控，实时预算执行情况分析</p>
            </div>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="8">
            <div class="feature-card">
              <div class="feature-icon">
                <i class="el-icon-s-data"></i>
              </div>
              <h3>数据分析</h3>
              <p>多维度财务数据可视化，趋势分析与报表生成</p>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>

    <!-- 技术架构 -->
    <div class="tech-container">
      <div class="container">
        <h2 class="section-title">技术架构</h2>
        <el-row :gutter="24">
          <el-col :xs="24" :md="8">
            <div class="tech-card">
              <h4>前端技术</h4>
              <ul>
                <li>Vue.js 2.6</li>
                <li>Element UI</li>
                <li>ECharts</li>
                <li>Axios</li>
              </ul>
            </div>
          </el-col>
          <el-col :xs="24" :md="8">
            <div class="tech-card">
              <h4>后端技术</h4>
              <ul>
                <li>Spring Boot</li>
                <li>Spring Security</li>
                <li>MyBatis Plus</li>
                <li>Redis</li>
              </ul>
            </div>
          </el-col>
          <el-col :xs="24" :md="8">
            <div class="tech-card">
              <h4>数据存储</h4>
              <ul>
                <li>MySQL 8.0</li>
                <li>Redis 缓存</li>
                <li>文件存储</li>
                <li>数据备份</li>
              </ul>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ProjectIntroduction",
  data() {
    return {
      // 项目介绍页面，暂无交互数据
    }
  },
  methods: {
    // 项目介绍页面，暂无交互方法
  }
}
</script>

<style scoped lang="scss">
.dashboard-container {
  min-height: 100vh;
  background: #f5f7fa;
}

// 顶部横幅
.hero-section {
  background: linear-gradient(135deg, #ff9800 0%, #f57c00 100%);
  padding: 80px 20px;
  text-align: center;
  color: white;
  
  .hero-content {
    max-width: 800px;
    margin: 0 auto;
    
    .main-title {
      font-size: 48px;
      font-weight: 700;
      margin: 0 0 20px 0;
      text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
    }
    
    .subtitle {
      font-size: 18px;
      margin: 0 0 30px 0;
      opacity: 0.9;
      line-height: 1.6;
    }
    
    .feature-badges {
      display: flex;
      justify-content: center;
      gap: 12px;
      flex-wrap: wrap;
      
      .el-tag {
        font-size: 14px;
        padding: 8px 16px;
      }
    }
  }
}

// 容器
.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

// 功能特色
.features-container {
  padding: 80px 0;
  background: white;
  
  .section-title {
    text-align: center;
    font-size: 36px;
    font-weight: 700;
    color: #2c3e50;
    margin: 0 0 50px 0;
  }
  
  .feature-card {
    text-align: center;
    padding: 40px 20px;
    height: 100%;
    transition: all 0.3s ease;
    
    &:hover {
      transform: translateY(-5px);
    }
    
    .feature-icon {
      width: 80px;
      height: 80px;
      background: linear-gradient(135deg, #ff9800 0%, #f57c00 100%);
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin: 0 auto 20px auto;
      
      i {
        font-size: 32px;
        color: white;
      }
    }
    
    h3 {
      font-size: 24px;
      font-weight: 600;
      color: #2c3e50;
      margin: 0 0 15px 0;
    }
    
    p {
      font-size: 16px;
      color: #666;
      line-height: 1.6;
      margin: 0;
    }
  }
}

// 技术架构
.tech-container {
  padding: 80px 0;
  background: #f8f9fa;
  
  .section-title {
    text-align: center;
    font-size: 36px;
    font-weight: 700;
    color: #2c3e50;
    margin: 0 0 50px 0;
  }
  
  .tech-card {
    background: white;
    padding: 30px;
    border-radius: 12px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
    height: 100%;
    transition: all 0.3s ease;
    
    &:hover {
      transform: translateY(-5px);
      box-shadow: 0 8px 30px rgba(0, 0, 0, 0.15);
    }
    
    h4 {
      font-size: 20px;
      font-weight: 600;
      color: #2c3e50;
      margin: 0 0 20px 0;
      text-align: center;
    }
    
    ul {
      list-style: none;
      padding: 0;
      margin: 0;
      
      li {
        padding: 8px 0;
        color: #666;
        font-size: 16px;
        border-bottom: 1px solid #f0f0f0;
        
        &:last-child {
          border-bottom: none;
        }
        
        &::before {
          content: '✓';
          color: #ff9800;
          font-weight: bold;
          margin-right: 10px;
        }
      }
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .hero-section {
    padding: 60px 20px;
    
    .main-title {
      font-size: 32px !important;
    }
    
    .subtitle {
      font-size: 16px !important;
    }
    
    .feature-badges {
      justify-content: center !important;
    }
  }
  
  .features-container,
  .tech-container {
    padding: 60px 0 !important;
  }
  
  .section-title {
    font-size: 28px !important;
  }
  
  .feature-card {
    padding: 30px 20px !important;
    margin-bottom: 30px;
  }
  
  .tech-card {
    margin-bottom: 20px;
  }
}
</style>
