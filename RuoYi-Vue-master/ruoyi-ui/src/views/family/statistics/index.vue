<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!-- 统计概览 -->
      <el-col :span="24" style="margin-bottom: 20px;">
        <el-card>
          <div slot="header" class="clearfix">
            <span>收支概览</span>
          </div>
          <el-row :gutter="16">
            <el-col :span="6">
              <div class="stat-item">
                <div class="stat-value income">{{ formatAmount(overview.totalIncome) }}</div>
                <div class="stat-label">总收入</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-item">
                <div class="stat-value expense">{{ formatAmount(overview.totalExpense) }}</div>
                <div class="stat-label">总支出</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-item">
                <div class="stat-value" :class="overview.balance >= 0 ? 'income' : 'expense'">
                  {{ formatAmount(overview.balance) }}
                </div>
                <div class="stat-label">结余</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-item">
                <div class="stat-value">{{ overview.totalTransactions }}</div>
                <div class="stat-label">交易笔数</div>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>

      <!-- 收入分类统计 -->
      <el-col :span="12">
        <el-card>
          <div slot="header" class="clearfix">
            <span>收入分类统计</span>
          </div>
          <div ref="incomeChart" style="height: 400px;"></div>
        </el-card>
      </el-col>

      <!-- 支出分类统计 -->
      <el-col :span="12">
        <el-card>
          <div slot="header" class="clearfix">
            <span>支出分类统计</span>
          </div>
          <div ref="expenseChart" style="height: 400px;"></div>
        </el-card>
      </el-col>

      <!-- 月度趋势 -->
      <el-col :span="24" style="margin-top: 20px;">
        <el-card>
          <div slot="header" class="clearfix">
            <span>近12个月收支趋势</span>
          </div>
          <div ref="trendChart" style="height: 400px;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getOverview, getIncomeCategoryStatistics, getExpenseCategoryStatistics, getMonthlyTrend } from "@/api/family/statistics";
import * as echarts from 'echarts';

export default {
  name: "Statistics",
  data() {
    return {
      overview: {
        totalIncome: 0,
        totalExpense: 0,
        balance: 0,
        totalTransactions: 0
      },
      incomeChart: null,
      expenseChart: null,
      trendChart: null
    };
  },
  created() {
    this.getStatistics();
  },
  mounted() {
    this.initCharts();
  },
  beforeDestroy() {
    if (this.incomeChart) {
      this.incomeChart.dispose();
    }
    if (this.expenseChart) {
      this.expenseChart.dispose();
    }
    if (this.trendChart) {
      this.trendChart.dispose();
    }
  },
  methods: {
    /** 获取统计数据 */
    getStatistics() {
      // 获取概览数据
      getOverview().then(response => {
        this.overview = response.data;
      });

      // 获取收入分类统计
      getIncomeCategoryStatistics().then(response => {
        this.updateIncomeChart(response.data);
      });

      // 获取支出分类统计
      getExpenseCategoryStatistics().then(response => {
        this.updateExpenseChart(response.data);
      });

      // 获取月度趋势
      getMonthlyTrend().then(response => {
        this.updateTrendChart(response.data);
      });
    },
    /** 初始化图表 */
    initCharts() {
      this.incomeChart = echarts.init(this.$refs.incomeChart);
      this.expenseChart = echarts.init(this.$refs.expenseChart);
      this.trendChart = echarts.init(this.$refs.trendChart);
    },
    /** 更新收入分类饼图 */
    updateIncomeChart(data) {
      if (!this.incomeChart) return;
      
      const option = {
        title: {
          text: '收入分类分布',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '收入分类',
            type: 'pie',
            radius: '50%',
            data: data.map(item => ({
              value: item.value,
              name: item.name
            })),
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
      this.incomeChart.setOption(option);
    },
    /** 更新支出分类饼图 */
    updateExpenseChart(data) {
      if (!this.expenseChart) return;
      
      const option = {
        title: {
          text: '支出分类分布',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
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
            data: data.map(item => ({
              value: item.value,
              name: item.name
            })),
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
      this.expenseChart.setOption(option);
    },
    /** 更新月度趋势图 */
    updateTrendChart(data) {
      if (!this.trendChart) return;
      
      // 处理数据
      const months = [...new Set(data.map(item => item.month))].sort();
      const incomeData = [];
      const expenseData = [];
      
      months.forEach(month => {
        const incomeItem = data.find(item => item.month === month && item.txn_type === '收入');
        const expenseItem = data.find(item => item.month === month && item.txn_type === '支出');
        incomeData.push(incomeItem ? incomeItem.amount : 0);
        expenseData.push(expenseItem ? expenseItem.amount : 0);
      });
      
      const option = {
        title: {
          text: '月度收支趋势',
          left: 'center'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['收入', '支出']
        },
        xAxis: {
          type: 'category',
          data: months
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '收入',
            type: 'line',
            data: incomeData,
            itemStyle: {
              color: '#67C23A'
            }
          },
          {
            name: '支出',
            type: 'line',
            data: expenseData,
            itemStyle: {
              color: '#F56C6C'
            }
          }
        ]
      };
      this.trendChart.setOption(option);
    },
    /** 格式化金额 */
    formatAmount(amount) {
      if (amount == null) return '¥0.00';
      return '¥' + Number(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      });
    }
  }
};
</script>

<style scoped>
.stat-item {
  text-align: center;
  padding: 20px;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 8px;
}

.stat-value.income {
  color: #67C23A;
}

.stat-value.expense {
  color: #F56C6C;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}
</style>
