<template>
  <div class="app-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="mb20">
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>本月收入</span>
          </div>
          <div class="text-center">
            <div class="amount income">+￥{{ monthlyStats.income || 0 }}</div>
            <div class="change-text">
              <span :class="monthlyStats.incomeChange >= 0 ? 'text-success' : 'text-danger'">
                {{ monthlyStats.incomeChange >= 0 ? '+' : '' }}{{ monthlyStats.incomeChange || 0 }}%
              </span>
              <span class="text-muted"> 较上月</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>本月支出</span>
          </div>
          <div class="text-center">
            <div class="amount expense">-￥{{ monthlyStats.expense || 0 }}</div>
            <div class="change-text">
              <span :class="monthlyStats.expenseChange <= 0 ? 'text-success' : 'text-danger'">
                {{ monthlyStats.expenseChange >= 0 ? '+' : '' }}{{ monthlyStats.expenseChange || 0 }}%
              </span>
              <span class="text-muted"> 较上月</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>本月结余</span>
          </div>
          <div class="text-center">
            <div class="amount" :class="monthlyStats.balance >= 0 ? 'income' : 'expense'">
              {{ monthlyStats.balance >= 0 ? '+' : '' }}￥{{ monthlyStats.balance || 0 }}
            </div>
            <div class="change-text">
              <span class="text-muted">收入 - 支出</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>家庭成员</span>
          </div>
          <div class="text-center">
            <div class="amount">{{ familyMemberCount || 0 }}人</div>
            <div class="change-text">
              <span class="text-muted">{{ familyName || '我的家庭' }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="mb20">
      <el-col :span="12">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>近6个月收支趋势</span>
            <el-button style="float: right; padding: 3px 0" type="text">详细</el-button>
          </div>
          <div ref="trendChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>支出分类统计</span>
            <el-button style="float: right; padding: 3px 0" type="text">详细</el-button>
          </div>
          <div ref="categoryChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 最近交易记录 -->
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>最近交易记录</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="$router.push('/family/transactions')">查看全部</el-button>
      </div>
      <el-table :data="recentTransactions" style="width: 100%">
        <el-table-column prop="transactionDate" label="时间" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.transactionDate, '{m}-{d} {h}:{i}') }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="txnType" label="类型" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.txnType === '收入' ? 'success' : 'danger'" size="mini">
              {{ scope.row.txnType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="category" label="分类" width="120" />
        <el-table-column prop="amount" label="金额" width="120">
          <template slot-scope="scope">
            <span :class="scope.row.txnType === '收入' ? 'text-success' : 'text-danger'">
              {{ scope.row.txnType === '收入' ? '+' : '-' }}￥{{ scope.row.amount }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="memberName" label="操作人" width="100" />
        <el-table-column prop="note" label="备注" />
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { listTransactions } from "@/api/family/transactions";
import { listMembers } from "@/api/family/members";
import * as echarts from 'echarts';

export default {
  name: "FamilyDashboard",
  data() {
    return {
      // 月度统计数据
      monthlyStats: {
        income: 0,
        expense: 0,
        balance: 0,
        incomeChange: 0,
        expenseChange: 0
      },
      // 家庭成员数量
      familyMemberCount: 0,
      // 家庭名称
      familyName: '',
      // 最近交易记录
      recentTransactions: [],
      // 图表实例
      trendChart: null,
      categoryChart: null
    };
  },
  mounted() {
    this.loadDashboardData();
    this.initCharts();
  },
  beforeDestroy() {
    if (this.trendChart) {
      this.trendChart.dispose();
    }
    if (this.categoryChart) {
      this.categoryChart.dispose();
    }
  },
  methods: {
    /** 加载仪表板数据 */
    async loadDashboardData() {
      try {
        // 加载最近交易记录
        await this.loadRecentTransactions();
        // 加载月度统计
        await this.loadMonthlyStats();
        // 加载家庭成员信息
        await this.loadFamilyInfo();
        // 更新图表
        this.updateCharts();
      } catch (error) {
        console.error('加载数据失败:', error);
      }
    },
    
    /** 加载最近交易记录 */
    async loadRecentTransactions() {
      const response = await listTransactions({ pageNum: 1, pageSize: 10 });
      this.recentTransactions = response.rows || [];
    },
    
    /** 加载月度统计 */
    async loadMonthlyStats() {
      // 获取当前月份的数据
      const currentDate = new Date();
      const currentMonth = currentDate.getMonth() + 1;
      const currentYear = currentDate.getFullYear();
      
      // 查询当前月份的交易记录
      const response = await listTransactions({ 
        pageNum: 1, 
        pageSize: 1000,
        // 这里需要后端支持按月份查询的参数
      });
      
      const transactions = response.rows || [];
      let income = 0;
      let expense = 0;
      
      transactions.forEach(transaction => {
        const transactionDate = new Date(transaction.transactionDate);
        if (transactionDate.getMonth() + 1 === currentMonth && 
            transactionDate.getFullYear() === currentYear) {
          if (transaction.txnType === '收入') {
            income += parseFloat(transaction.amount);
          } else {
            expense += parseFloat(transaction.amount);
          }
        }
      });
      
      this.monthlyStats = {
        income: income.toFixed(2),
        expense: expense.toFixed(2),
        balance: (income - expense).toFixed(2),
        incomeChange: 0, // 需要与上月对比
        expenseChange: 0  // 需要与上月对比
      };
    },
    
    /** 加载家庭信息 */
    async loadFamilyInfo() {
      const response = await listMembers({ pageNum: 1, pageSize: 100 });
      this.familyMemberCount = response.total || 0;
      // 这里需要从用户信息中获取家庭名称
      this.familyName = '我的家庭';
    },
    
    /** 初始化图表 */
    initCharts() {
      this.trendChart = echarts.init(this.$refs.trendChart);
      this.categoryChart = echarts.init(this.$refs.categoryChart);
      
      // 监听窗口大小变化
      window.addEventListener('resize', () => {
        this.trendChart && this.trendChart.resize();
        this.categoryChart && this.categoryChart.resize();
      });
    },
    
    /** 更新图表 */
    updateCharts() {
      this.updateTrendChart();
      this.updateCategoryChart();
    },
    
    /** 更新趋势图表 */
    updateTrendChart() {
      const option = {
        title: {
          text: ''
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['收入', '支出']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: ['1月', '2月', '3月', '4月', '5月', '6月']
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '收入',
            type: 'line',
            data: [1200, 1320, 1010, 1340, 1290, 1330],
            itemStyle: {
              color: '#67C23A'
            }
          },
          {
            name: '支出',
            type: 'line',
            data: [820, 932, 901, 934, 1290, 1230],
            itemStyle: {
              color: '#F56C6C'
            }
          }
        ]
      };
      this.trendChart.setOption(option);
    },
    
    /** 更新分类图表 */
    updateCategoryChart() {
      const option = {
        title: {
          text: '',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '支出分类',
            type: 'pie',
            radius: '50%',
            data: [
              { value: 1048, name: '餐饮' },
              { value: 735, name: '交通' },
              { value: 580, name: '购物' },
              { value: 484, name: '娱乐' },
              { value: 300, name: '其他' }
            ],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      };
      this.categoryChart.setOption(option);
    }
  }
};
</script>

<style scoped>
.mb20 {
  margin-bottom: 20px;
}

.amount {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 8px;
}

.income {
  color: #67C23A;
}

.expense {
  color: #F56C6C;
}

.change-text {
  font-size: 12px;
}

.text-success {
  color: #67C23A;
}

.text-danger {
  color: #F56C6C;
}

.text-muted {
  color: #909399;
}

.text-center {
  text-align: center;
}

.box-card {
  height: 100%;
}
</style>
