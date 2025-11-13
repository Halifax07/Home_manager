<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="姓名" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="性别" prop="gender">
        <el-select v-model="queryParams.gender" placeholder="请选择性别" clearable>
          <el-option label="男" value="男" />
          <el-option label="女" value="女" />
        </el-select>
      </el-form-item>
      <el-form-item label="角色" prop="role">
        <el-select v-model="queryParams.role" placeholder="请选择角色" clearable>
          <el-option label="管理员" value="管理员" />
          <el-option label="普通成员" value="普通成员" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5" v-if="isAdmin">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['family:members:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5" v-if="isAdmin">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['family:members:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5" v-if="isAdmin">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['family:members:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['family:members:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="membersList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" v-if="isAdmin" />
      <el-table-column label="成员ID" align="center" prop="id" />
      <el-table-column label="姓名" align="center" prop="name" />
      <el-table-column label="性别" align="center" prop="gender">
        <template slot-scope="scope">
          <el-tag :type="scope.row.gender === '男' ? 'primary' : 'success'" disable-transitions>
            {{ scope.row.gender }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="亲属关系" align="center" prop="kinship" />
      <el-table-column label="职业" align="center" prop="occupation" />
      <el-table-column label="登录账号" align="center" prop="account" />
      <el-table-column label="角色" align="center" prop="role">
        <template slot-scope="scope">
          <el-tag :type="scope.row.role === '管理员' ? 'danger' : 'info'" disable-transitions>
            {{ scope.row.role }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
            v-hasPermi="['family:members:query']"
          >查看</el-button>
          <el-button
            v-if="isAdmin || isCurrentUser(scope.row)"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['family:members:edit']"
          >修改</el-button>
          <el-button
            v-if="isAdmin && scope.row.role !== '管理员' && !isCurrentUser(scope.row)"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['family:members:remove']"
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

    <!-- 添加或修改家庭成员对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="form.gender" placeholder="请选择性别">
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
          </el-select>
        </el-form-item>
        <el-form-item label="亲属关系" prop="kinship">
          <el-select v-model="form.kinship" placeholder="请选择亲属关系" style="width: 100%">
            <el-option label="父亲" value="父亲"></el-option>
            <el-option label="母亲" value="母亲"></el-option>
            <el-option label="儿子" value="儿子"></el-option>
            <el-option label="女儿" value="女儿"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="职业" prop="occupation">
          <el-select v-model="form.occupation" placeholder="请选择职业" style="width: 100%">
            <el-option label="教师" value="教师"></el-option>
            <el-option label="医生" value="医生"></el-option>
            <el-option label="公务员" value="公务员"></el-option>
            <el-option label="程序员" value="程序员"></el-option>
            <el-option label="其他" value="其他"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="登录账号" prop="account">
          <el-input v-model="form.account" placeholder="请输入登录账号" />
        </el-form-item>
        <el-form-item label="登录密码" prop="passwordHash" v-if="!form.id">
          <el-input v-model="form.passwordHash" placeholder="请输入登录密码" type="password" show-password />
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="form.role" placeholder="请选择角色">
            <el-option label="管理员" value="管理员" />
            <el-option label="普通成员" value="普通成员" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看家庭成员详情对话框 -->
    <el-dialog title="家庭成员详情" :visible.sync="viewOpen" width="600px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="成员ID">{{ viewForm.id }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ viewForm.name }}</el-descriptions-item>
        <el-descriptions-item label="性别">
          <el-tag :type="viewForm.gender === '男' ? 'primary' : 'success'">
            {{ viewForm.gender }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="亲属关系">{{ viewForm.kinship }}</el-descriptions-item>
        <el-descriptions-item label="职业">{{ viewForm.occupation }}</el-descriptions-item>
        <el-descriptions-item label="登录账号">{{ viewForm.account }}</el-descriptions-item>
        <el-descriptions-item label="角色">
          <el-tag :type="viewForm.role === '管理员' ? 'danger' : 'info'">
            {{ viewForm.role }}
          </el-tag>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { listMembers, getMember, delMember, addMember, updateMember, checkAdmin } from "@/api/family/members";
import { getPersonalInfo } from "@/api/family/personal";

export default {
  name: "Members",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 家庭成员表格数据
      membersList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示查看弹出层
      viewOpen: false,
      // 当前用户是否为家庭管理员
      isAdmin: false,
      // 当前登录用户名
      currentUsername: '',
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        gender: null,
        role: null
      },
      // 表单参数
      form: {},
      // 查看表单参数
      viewForm: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "姓名不能为空", trigger: "blur" }
        ],
        gender: [
          { required: true, message: "性别不能为空", trigger: "change" }
        ],
        account: [
          { required: true, message: "登录账号不能为空", trigger: "blur" }
        ],
        role: [
          { required: true, message: "角色不能为空", trigger: "change" }
        ]
      }
    };
  },
  created() {
    this.getList();
    this.checkUserRole();
  },
  methods: {
    /** 查询家庭成员列表 */
    getList() {
      this.loading = true;
      listMembers(this.queryParams).then(response => {
        this.membersList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 检查用户角色 */
    checkUserRole() {
      // 调用专门的管理员检查接口
      checkAdmin().then(response => {
        this.isAdmin = response.data === true;
      }).catch(() => {
        this.isAdmin = false;
      });
      
      // 获取当前用户名
      this.currentUsername = this.$store.state.user.name;
    },
    /** 判断是否为当前用户 */
    isCurrentUser(member) {
      return member.account === this.currentUsername;
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        familyId: null,
        name: null,
        gender: null,
        kinship: null,
        occupation: null,
        account: null,
        passwordHash: null,
        role: null
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加家庭成员";
    },
    /** 查看按钮操作 */
    handleView(row) {
      this.viewForm = row;
      this.viewOpen = true;
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getMember(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改家庭成员";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateMember(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addMember(this.form).then(response => {
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
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除家庭成员编号为"' + ids + '"的数据项？').then(function() {
        return delMember(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('family/members/export', {
        ...this.queryParams
      }, `members_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
