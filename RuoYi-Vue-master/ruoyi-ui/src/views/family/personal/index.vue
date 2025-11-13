<template>
  <div class="app-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span class="header-title">个人信息</span>
      </div>
      
      <el-descriptions :column="2" border>
        <el-descriptions-item label="姓名">
          {{ personalInfo.name || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="性别">
          {{ personalInfo.gender || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="家庭关系">
          {{ personalInfo.kinship || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="职业">
          {{ personalInfo.occupation || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="登录账号">
          {{ personalInfo.account || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="家庭角色">
          <el-tag v-if="personalInfo.role === '管理员'" type="danger">家庭管理员</el-tag>
          <el-tag v-else type="primary">普通家庭成员</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="所属家庭">
          {{ personalInfo.familyName || '-' }}
        </el-descriptions-item>
      </el-descriptions>

      <div style="margin-top: 20px;">
        <el-button type="primary" @click="editPersonalInfo">修改个人信息</el-button>
      </div>
    </el-card>

    <!-- 修改个人信息对话框 -->
    <el-dialog title="修改个人信息" :visible.sync="editDialog" width="600px">
      <el-form ref="editForm" :model="editForm" :rules="editRules" label-width="100px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="editForm.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="editForm.gender">
            <el-radio label="男">男</el-radio>
            <el-radio label="女">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="家庭关系" prop="kinship">
          <el-select v-model="editForm.kinship" placeholder="请选择家庭关系" style="width: 100%">
            <el-option label="父亲" value="父亲"></el-option>
            <el-option label="母亲" value="母亲"></el-option>
            <el-option label="儿子" value="儿子"></el-option>
            <el-option label="女儿" value="女儿"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="职业" prop="occupation">
          <el-select v-model="editForm.occupation" placeholder="请选择职业" style="width: 100%">
            <el-option label="教师" value="教师"></el-option>
            <el-option label="医生" value="医生"></el-option>
            <el-option label="公务员" value="公务员"></el-option>
            <el-option label="程序员" value="程序员"></el-option>
            <el-option label="其他" value="其他"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editDialog = false">取 消</el-button>
        <el-button type="primary" @click="submitEdit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getPersonalInfo, updatePersonalInfo } from "@/api/family/personal"

export default {
  name: "Personal",
  data() {
    return {
      // 个人信息
      personalInfo: {},
      // 编辑对话框
      editDialog: false,
      // 编辑表单
      editForm: {},
      // 编辑表单验证规则
      editRules: {
        name: [
          { required: true, message: "姓名不能为空", trigger: "blur" }
        ],
        gender: [
          { required: true, message: "请选择性别", trigger: "change" }
        ],
        kinship: [
          { required: true, message: "家庭关系不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getPersonalInfo();
  },
  methods: {
    /** 获取个人信息 */
    getPersonalInfo() {
      getPersonalInfo().then(response => {
        this.personalInfo = response.data;
      });
    },
    /** 修改个人信息 */
    editPersonalInfo() {
      this.editForm = { ...this.personalInfo };
      this.editDialog = true;
    },
    /** 提交修改 */
    submitEdit() {
      this.$refs["editForm"].validate(valid => {
        if (valid) {
          updatePersonalInfo(this.editForm).then(response => {
            this.$modal.msgSuccess("修改成功");
            this.editDialog = false;
            this.getPersonalInfo();
          });
        }
      });
    }
  }
};
</script>

<style scoped>
.header-title {
  font-size: 18px;
  font-weight: bold;
}

.box-card {
  max-width: 800px;
  margin: 0 auto;
}
</style>
