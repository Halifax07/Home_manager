<template>
  <div class="app-container">
    <!-- 筛选条件表单 -->
    <el-card class="filter-card" shadow="never" style="margin-bottom: 20px;">
      <div slot="header" style="padding: 10px 20px;">
        <span style="font-weight: 600; color: #303133;">
          <i class="el-icon-search"></i> 筛选条件
        </span>
        <span style="float: right; font-size: 12px; color: #909399;">
          可按交易类型、分类、创建人、时间等条件筛选
        </span>
      </div>
      
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="68px" style="padding: 0 20px 10px 20px;">
      <el-form-item label="交易类型" prop="txnType">
        <el-select v-model="queryParams.txnType" placeholder="请选择交易类型" clearable>
          <el-option label="收入" value="收入" />
          <el-option label="支出" value="支出" />
        </el-select>
      </el-form-item>
      <el-form-item label="分类" prop="category">
        <el-select v-model="queryParams.category" placeholder="请选择分类" clearable filterable allow-create>
          <el-option label="全部" value=""></el-option>
          <el-option
            v-for="category in uniqueCategories"
            :key="category"
            :label="category"
            :value="category">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="创建人" prop="memberName">
        <el-select v-model="queryParams.memberName" placeholder="请选择创建人" clearable filterable>
          <el-option
            label="全部"
            value="">
          </el-option>
          <!-- 优先显示从当前交易记录中提取的创建人 -->
          <el-option
            v-for="memberName in uniqueMemberNames"
            :key="memberName"
            :label="memberName"
            :value="memberName">
          </el-option>
          <!-- 如果交易记录为空，从成员列表显示 -->
          <el-option
            v-if="uniqueMemberNames.length === 0"
            v-for="member in membersList"
            :key="member.id"
            :label="member.nickName"
            :value="member.nickName">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="交易时间" prop="transactionDate">
        <el-date-picker clearable
          v-model="queryParams.transactionDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择交易时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    </el-card>

    <!-- 快速筛选按钮 -->
    <el-card class="quick-filter-card" shadow="never" style="margin-bottom: 20px;">
      <div slot="header" style="padding: 10px 20px;">
        <span style="font-weight: 600; color: #303133;">
          <i class="el-icon-collection-tag"></i> 快速筛选
        </span>
      </div>
    <el-row class="mb8" style="padding: 0 20px 10px 20px;">
      <el-col :span="24">
        <div class="quick-filter">
          <span class="filter-label">快速筛选：</span>
          <el-button-group>
            <el-button 
              size="small" 
              :type="queryParams.txnType === null ? 'primary' : ''"
              @click="quickFilter('all')">
              全部
            </el-button>
            <el-button 
              size="small" 
              :type="queryParams.txnType === '收入' ? 'success' : ''"
              @click="quickFilter('income')">
              收入
            </el-button>
            <el-button 
              size="small" 
              :type="queryParams.txnType === '支出' ? 'danger' : ''"
              @click="quickFilter('expense')">
              支出
            </el-button>
          </el-button-group>
          
          <el-button-group style="margin-left: 15px;">
            <el-button 
              size="small" 
              :type="queryParams.transactionDate === getTodayDate() ? 'warning' : ''"
              @click="quickFilterDate('today')">
              今天
            </el-button>
            <el-button 
              size="small" 
              :type="queryParams.transactionDate === getYesterdayDate() ? 'warning' : ''"
              @click="quickFilterDate('yesterday')">
              昨天
            </el-button>
            <el-button 
              size="small" 
              :type="isThisWeek() ? 'warning' : ''"
              @click="quickFilterDate('week')">
              本周
            </el-button>
            <el-button 
              size="small" 
              :type="isThisMonth() ? 'warning' : ''"
              @click="quickFilterDate('month')">
              本月
            </el-button>
          </el-button-group>
          
          <el-button 
            size="small" 
            type="info"
            icon="el-icon-user"
            style="margin-left: 15px;"
            @click="filterMyRecords">
            我的记录
          </el-button>
        </div>
      </el-col>
    </el-row>
    </el-card>

    <!-- 统计信息 -->
    <el-row v-if="transactionsList.length > 0" class="mb8" style="background: #f5f7fa; padding: 10px; border-radius: 4px;">
      <el-col :span="24">
        <div class="statistics-info">
          <span class="stat-item">
            <i class="el-icon-document"></i>
            共 <strong style="color: #409EFF;">{{ total }}</strong> 笔交易
          </span>
          <span class="stat-item">
            <i class="el-icon-top"></i>
            收入 <strong style="color: #67C23A;">{{ incomeStats.count }}</strong> 笔，
            金额 <strong style="color: #67C23A;">￥{{ incomeStats.amount }}</strong>
          </span>
          <span class="stat-item">
            <i class="el-icon-bottom"></i>
            支出 <strong style="color: #F56C6C;">{{ expenseStats.count }}</strong> 笔，
            金额 <strong style="color: #F56C6C;">￥{{ expenseStats.amount }}</strong>
          </span>
          <span class="stat-item">
            <i class="el-icon-wallet"></i>
            结余 <strong :style="{color: netAmount >= 0 ? '#67C23A' : '#F56C6C'}">
              ￥{{ netAmount >= 0 ? '+' : '' }}{{ netAmount }}
            </strong>
          </span>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['family:transactions:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single || (!isAdmin && !canEditSelected)"
          @click="handleUpdate"
          v-hasPermi="['family:transactions:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple || (!isAdmin && !canDeleteSelected)"
          @click="handleDelete"
          v-hasPermi="['family:transactions:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['family:transactions:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="transactionsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="交易ID" align="center" prop="flowId" />
      <el-table-column label="交易金额" align="center" prop="amount" :formatter="amountFormatter" />
      <el-table-column label="交易类型" align="center" prop="txnType">
        <template slot-scope="scope">
          <el-tag :type="scope.row.txnType === '收入' ? 'success' : 'danger'" disable-transitions>
            {{ scope.row.txnType }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="分类" align="center" prop="category" />
      <el-table-column label="交易时间" align="center" prop="transactionDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.transactionDate, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建人" align="center" prop="memberName" />
      <el-table-column label="备注" align="center" prop="note" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
            v-hasPermi="['family:transactions:query']"
          >查看</el-button>
          <el-button
            v-if="isAdmin || canEditRecord(scope.row)"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['family:transactions:edit']"
          >修改</el-button>
          <el-button
            v-if="isAdmin || canDeleteRecord(scope.row)"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['family:transactions:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改家庭交易记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="交易金额" prop="amount">
          <el-input-number v-model="form.amount" :precision="2" :min="0.01" placeholder="请输入交易金额" />
        </el-form-item>
        <el-form-item label="交易类型" prop="txnType">
          <el-select v-model="form.txnType" placeholder="请选择交易类型">
            <el-option label="收入" value="收入" />
            <el-option label="支出" value="支出" />
          </el-select>
        </el-form-item>
        <el-form-item label="分类" prop="category">
          <el-select v-model="form.category" placeholder="请选择或输入分类" clearable filterable allow-create>
            <el-option
              v-for="category in uniqueCategories"
              :key="category"
              :label="category"
              :value="category">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="交易时间" prop="transactionDate">
          <el-date-picker clearable
            v-model="form.transactionDate"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择交易时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="备注" prop="note">
          <el-input v-model="form.note" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看交易记录详情对话框 -->
    <el-dialog title="交易记录详情" :visible.sync="viewOpen" width="600px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="交易ID">{{ viewForm.flowId }}</el-descriptions-item>
        <el-descriptions-item label="交易金额">
          <span :class="viewForm.txnType === '收入' ? 'text-success' : 'text-danger'">
            {{ viewForm.txnType === '收入' ? '+' : '-' }}￥{{ viewForm.amount }}
          </span>
        </el-descriptions-item>
        <el-descriptions-item label="交易类型">
          <el-tag :type="viewForm.txnType === '收入' ? 'success' : 'danger'">
            {{ viewForm.txnType }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="分类">{{ viewForm.category }}</el-descriptions-item>
        <el-descriptions-item label="交易时间">
          {{ parseTime(viewForm.transactionDate, '{y}-{m}-{d} {h}:{i}:{s}') }}
        </el-descriptions-item>
        <el-descriptions-item label="创建人">{{ viewForm.memberName }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ viewForm.note }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { listTransactions, getTransaction, delTransaction, addTransaction, updateTransaction } from "@/api/family/transactions";
import { checkAdmin, listMembers } from "@/api/family/members";

export default {
  name: "Transactions",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 选中的记录数组
      selectedRecords: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 家庭交易记录表格数据
      transactionsList: [],
      // 家庭成员列表
      membersList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示查看弹出层
      viewOpen: false,
      // 当前用户是否为家庭管理员
      isAdmin: false,
      // 当前登录用户信息
      currentUser: null,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        txnType: null,
        category: null,
        memberName: null,
        transactionDate: null
      },
      // 表单参数
      form: {},
      // 查看表单参数
      viewForm: {},
      // 表单校验
      rules: {
        amount: [
          { required: true, message: "交易金额不能为空", trigger: "blur" }
        ],
        txnType: [
          { required: true, message: "交易类型不能为空", trigger: "change" }
        ]
      }
    };
  },
  computed: {
    // 收入统计
    incomeStats() {
      const incomeTransactions = this.transactionsList.filter(item => item.txnType === '收入');
      return {
        count: incomeTransactions.length,
        amount: incomeTransactions.reduce((sum, item) => sum + parseFloat(item.amount || 0), 0).toFixed(2)
      };
    },
    // 支出统计
    expenseStats() {
      const expenseTransactions = this.transactionsList.filter(item => item.txnType === '支出');
      return {
        count: expenseTransactions.length,
        amount: expenseTransactions.reduce((sum, item) => sum + parseFloat(item.amount || 0), 0).toFixed(2)
      };
    },
    // 净收支
    netAmount() {
      const income = parseFloat(this.incomeStats.amount);
      const expense = parseFloat(this.expenseStats.amount);
      return (income - expense).toFixed(2);
    },
    // 从交易记录中提取唯一的创建人名称
    uniqueMemberNames() {
      const memberNames = new Set();
      this.transactionsList.forEach(transaction => {
        if (transaction.memberName && transaction.memberName.trim()) {
          memberNames.add(transaction.memberName);
        }
      });
      return Array.from(memberNames).sort();
    },
    // 从交易记录中提取唯一的分类
    uniqueCategories() {
      const categories = new Set();
      this.transactionsList.forEach(transaction => {
        if (transaction.category && transaction.category.trim()) {
          categories.add(transaction.category);
        }
      });
      return Array.from(categories).sort();
    },
    // 普通用户是否可以编辑选中的记录（所有选中记录都是自己创建的）
    canEditSelected() {
      if (this.selectedRecords.length !== 1) return false;
      return this.canEditRecord(this.selectedRecords[0]);
    },
    // 普通用户是否可以删除选中的记录（所有选中记录都是自己创建的）
    canDeleteSelected() {
      if (this.selectedRecords.length === 0) return false;
      return this.selectedRecords.every(record => this.canDeleteRecord(record));
    }
  },
  created() {
    this.getCurrentUser();
    this.getList();
    this.checkUserRole();
    this.getMembersList();
  },
  methods: {
    /** 查询家庭交易记录列表 */
    getList() {
      this.loading = true;
      listTransactions(this.queryParams).then(response => {
        this.transactionsList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 检查用户角色 */
    checkUserRole() {
      // 调用后端接口检查当前用户是否为家庭管理员
      checkAdmin().then(response => {
        this.isAdmin = response.data;
      }).catch(() => {
        this.isAdmin = false;
      });
    },
    /** 获取家庭成员列表 */
    getMembersList() {
      listMembers().then(response => {
        if (response.code === 200 && response.rows) {
          this.membersList = response.rows;
          console.log('成员列表加载成功:', this.membersList);
        } else {
          this.membersList = [];
          console.log('成员列表为空');
        }
      }).catch(error => {
        console.error('获取成员列表失败:', error);
        this.membersList = [];
        // 如果没有成员数据，添加一些默认选项用于测试
        this.membersList = [
          { id: 'wang', nickName: 'Wang' },
          { id: 'gongfang', nickName: '工芳' },
          { id: 'wangjing', nickName: '王净' },
          { id: 'wangfang', nickName: '王芳' }
        ];
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        flowId: null,
        familyId: null,
        amount: null,
        txnType: null,
        category: null,
        id: null,
        transactionDate: null,
        note: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.flowId)
      this.selectedRecords = selection;
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加家庭交易记录";
    },
    /** 查看按钮操作 */
    handleView(row) {
      this.viewForm = row;
      this.viewOpen = true;
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const flowId = row.flowId || this.ids
      getTransaction(flowId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改家庭交易记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.flowId != null) {
            updateTransaction(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addTransaction(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const flowIds = row.flowId || this.ids;
      this.$modal.confirm('是否确认删除家庭交易记录编号为"' + flowIds + '"的数据项？').then(function() {
        return delTransaction(flowIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('family/transactions/export', {
        ...this.queryParams
      }, `transactions_${new Date().getTime()}.xlsx`)
    },
    /** 金额格式化 */
    amountFormatter(row, column) {
      const sign = row.txnType === '收入' ? '+' : '-';
      return `${sign}￥${row.amount}`;
    },
    /** 快速筛选交易类型 */
    quickFilter(type) {
      switch(type) {
        case 'all':
          this.queryParams.txnType = null;
          break;
        case 'income':
          this.queryParams.txnType = '收入';
          break;
        case 'expense':
          this.queryParams.txnType = '支出';
          break;
      }
      this.handleQuery();
    },
    /** 快速筛选日期 */
    quickFilterDate(type) {
      const today = new Date();
      switch(type) {
        case 'today':
          this.queryParams.transactionDate = this.formatDate(today);
          break;
        case 'yesterday':
          const yesterday = new Date(today);
          yesterday.setDate(yesterday.getDate() - 1);
          this.queryParams.transactionDate = this.formatDate(yesterday);
          break;
        case 'week':
          // 本周的第一天（周一）
          const firstDayOfWeek = new Date(today);
          const dayOfWeek = today.getDay() || 7; // 将周日的0转换为7
          firstDayOfWeek.setDate(today.getDate() - dayOfWeek + 1);
          this.queryParams.transactionDate = this.formatDate(firstDayOfWeek);
          break;
        case 'month':
          // 本月的第一天
          const firstDayOfMonth = new Date(today.getFullYear(), today.getMonth(), 1);
          this.queryParams.transactionDate = this.formatDate(firstDayOfMonth);
          break;
      }
      this.handleQuery();
    },
    /** 格式化日期 */
    formatDate(date) {
      const year = date.getFullYear();
      const month = (date.getMonth() + 1).toString().padStart(2, '0');
      const day = date.getDate().toString().padStart(2, '0');
      return `${year}-${month}-${day}`;
    },
    /** 获取今天日期 */
    getTodayDate() {
      return this.formatDate(new Date());
    },
    /** 获取昨天日期 */
    getYesterdayDate() {
      const yesterday = new Date();
      yesterday.setDate(yesterday.getDate() - 1);
      return this.formatDate(yesterday);
    },
    /** 判断是否为本周筛选 */
    isThisWeek() {
      const today = new Date();
      const firstDayOfWeek = new Date(today);
      const dayOfWeek = today.getDay() || 7;
      firstDayOfWeek.setDate(today.getDate() - dayOfWeek + 1);
      return this.queryParams.transactionDate === this.formatDate(firstDayOfWeek);
    },
    /** 判断是否为本月筛选 */
    isThisMonth() {
      const today = new Date();
      const firstDayOfMonth = new Date(today.getFullYear(), today.getMonth(), 1);
      return this.queryParams.transactionDate === this.formatDate(firstDayOfMonth);
    },
    /** 筛选我的记录 */
    filterMyRecords() {
      // 获取当前用户昵称，如果没有则使用用户名
      const currentUser = this.$store.state.user.name || '';
      this.queryParams.memberName = currentUser;
      this.handleQuery();
    },
    /** 获取当前登录用户信息 */
    getCurrentUser() {
      this.currentUser = this.$store.state.user.name || this.$store.state.user.userName || '';
    },
    /** 普通用户是否可以编辑该记录（只能编辑自己创建的记录） */
    canEditRecord(record) {
      if (!record || !this.currentUser) return false;
      // 检查记录的创建人是否为当前用户
      return record.memberName === this.currentUser;
    },
    /** 普通用户是否可以删除该记录（只能删除自己创建的记录） */
    canDeleteRecord(record) {
      if (!record || !this.currentUser) return false;
      // 检查记录的创建人是否为当前用户
      return record.memberName === this.currentUser;
    }
  }
};
</script>

<style scoped>
.text-success {
  color: #67C23A;
}
.text-danger {
  color: #F56C6C;
}

.statistics-info {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 15px;
}

.stat-item {
  display: flex;
  align-items: center;
  font-size: 14px;
  color: #606266;
}

.stat-item i {
  margin-right: 5px;
  font-size: 16px;
}

.stat-item strong {
  margin: 0 2px;
  font-weight: 600;
}

.filter-card {
  border: 1px solid #e4e7ed;
}

.quick-filter-card {
  border: 1px solid #e4e7ed;
}

.quick-filter {
  display: flex;
  align-items: center;
  padding: 10px;
  background: #f8f9fa;
  border-radius: 4px;
  border-left: 4px solid #409EFF;
}

.filter-label {
  font-size: 14px;
  color: #606266;
  margin-right: 10px;
  font-weight: 500;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .statistics-info {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }
  
  .stat-item {
    width: 100%;
  }
  
  .quick-filter {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .filter-label {
    margin-bottom: 5px;
  }

  .filter-card .el-card__header,
  .quick-filter-card .el-card__header {
    padding: 10px;
  }

  .filter-card .el-form,
  .quick-filter-card .el-row {
    padding: 0 10px 10px 10px !important;
  }
}
</style>
