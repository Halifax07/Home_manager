<template>
  <div class="register">
    <el-form ref="registerForm" :model="registerForm" :rules="registerRules" class="register-form">
      <h3 class="title">{{title}}</h3>
      <el-form-item prop="username">
        <el-input v-model="registerForm.username" type="text" auto-complete="off" placeholder="账号">
          <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          v-model="registerForm.password"
          type="password"
          auto-complete="off"
          placeholder="密码"
          @keyup.enter.native="handleRegister"
        >
          <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="confirmPassword">
        <el-input
          v-model="registerForm.confirmPassword"
          type="password"
          auto-complete="off"
          placeholder="确认密码"
          @keyup.enter.native="handleRegister"
        >
          <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="userType">
        <el-select v-model="registerForm.userType" placeholder="请选择用户类型" style="width: 100%">
          <svg-icon slot="prefix" icon-class="peoples" class="el-input__icon input-icon" />
          <el-option
            v-for="item in userTypeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
            <span style="float: left">
              <i :class="item.value === 'admin' ? 'el-icon-star-on' : 'el-icon-user'" 
                 :style="{ color: item.value === 'admin' ? '#f56c6c' : '#909399', marginRight: '8px' }"></i>
              {{ item.label }}
            </span>
            <span style="float: right; color: #8492a6; font-size: 13px">{{ item.desc }}</span>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="code" v-if="captchaEnabled">
        <el-input
          v-model="registerForm.code"
          auto-complete="off"
          placeholder="验证码"
          style="width: 63%"
          @keyup.enter.native="handleRegister"
        >
          <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
        </el-input>
        <div class="register-code">
          <img :src="codeUrl" @click="getCode" class="register-code-img"/>
        </div>
      </el-form-item>
      <el-form-item style="width:100%;">
        <el-button
          :loading="loading"
          size="medium"
          type="primary"
          style="width:100%;"
          @click.native.prevent="handleRegister"
        >
          <span v-if="!loading">注 册</span>
          <span v-else>注 册 中...</span>
        </el-button>
        <div style="float: right;">
          <router-link class="link-type" :to="'/login'">使用已有账户登录</router-link>
        </div>
      </el-form-item>
    </el-form>
    <!--  底部  -->
    <div class="el-register-footer">
      <span>Copyright © 2025 段学琦 All Rights Reserved. 家庭财务管理系统</span>
    </div>
  </div>
</template>

<script>
import { getCodeImg, register } from "@/api/login"

export default {
  name: "Register",
  data() {
    const equalToPassword = (rule, value, callback) => {
      if (this.registerForm.password !== value) {
        callback(new Error("两次输入的密码不一致"))
      } else {
        callback()
      }
    }
    return {
      title: process.env.VUE_APP_TITLE,
      codeUrl: "",
      registerForm: {
        username: "",
        password: "",
        confirmPassword: "",
        code: "",
        uuid: "",
        userType: "member"
      },
      userTypeOptions: [
        {
          value: 'admin',
          label: '家庭管理员',
          desc: '拥有所有权限'
        },
        {
          value: 'member',
          label: '普通成员',
          desc: '基本功能权限'
        }
      ],
      registerRules: {
        username: [
          { required: true, trigger: "blur", message: "请输入您的账号" },
          { min: 2, max: 20, message: '用户账号长度必须介于 2 和 20 之间', trigger: 'blur' }
        ],
        password: [
          { required: true, trigger: "blur", message: "请输入您的密码" },
          { min: 5, max: 20, message: "用户密码长度必须介于 5 和 20 之间", trigger: "blur" },
          { pattern: /^[^<>"'|\\]+$/, message: "不能包含非法字符：< > \" ' \\\ |", trigger: "blur" }
        ],
        confirmPassword: [
          { required: true, trigger: "blur", message: "请再次输入您的密码" },
          { required: true, validator: equalToPassword, trigger: "blur" }
        ],
        code: [{ required: true, trigger: "change", message: "请输入验证码" }],
        userType: [{ required: true, message: "请选择用户类型", trigger: "change" }]
      },
      loading: false,
      captchaEnabled: true
    }
  },
  created() {
    this.getCode()
  },
  methods: {
    getCode() {
      getCodeImg().then(res => {
        this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled
        if (this.captchaEnabled) {
          this.codeUrl = "data:image/gif;base64," + res.img
          this.registerForm.uuid = res.uuid
        }
      })
    },
    handleRegister() {
      this.$refs.registerForm.validate(valid => {
        if (valid) {
          this.loading = true
          register(this.registerForm).then(res => {
            const username = this.registerForm.username
            this.$alert("<font color='red'>恭喜你，您的账号 " + username + " 注册成功！</font>", '系统提示', {
              dangerouslyUseHTMLString: true,
              type: 'success'
            }).then(() => {
              this.$router.push("/login")
            }).catch(() => {})
          }).catch(() => {
            this.loading = false
            if (this.captchaEnabled) {
              this.getCode()
            }
          })
        }
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
.register {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  background-size: cover;
  position: relative;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: url("../assets/images/login-background.jpg") center center;
    background-size: cover;
    opacity: 0.3;
    z-index: 0;
  }
}

.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #333;
  font-size: 24px;
  font-weight: 600;
  font-family: 'Microsoft YaHei', 'PingFang SC', sans-serif;
  letter-spacing: 1px;
}

.register-form {
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.95);
  width: 420px;
  padding: 40px 30px 30px 30px;
  z-index: 1;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1), 0 5px 15px rgba(0, 0, 0, 0.07);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  
  .el-input {
    height: 42px;
    input {
      height: 42px;
      border-radius: 8px;
      border: 1px solid #e0e6ed;
      transition: all 0.3s ease;
      
      &:focus {
        border-color: #667eea;
        box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.1);
      }
    }
  }
  
  .el-select {
    width: 100%;
    
    .el-input__inner {
      height: 42px;
      border-radius: 8px;
      border: 1px solid #e0e6ed;
      transition: all 0.3s ease;
      
      &:focus {
        border-color: #667eea;
        box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.1);
      }
    }
  }
  
  .input-icon {
    height: 42px;
    width: 16px;
    margin-left: 4px;
    color: #667eea;
  }
  
  .el-button--primary {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border: none;
    border-radius: 8px;
    height: 42px;
    font-size: 16px;
    font-weight: 600;
    transition: all 0.3s ease;
    
    &:hover {
      background: linear-gradient(135deg, #5a6fd8 0%, #6a4190 100%);
      transform: translateY(-1px);
      box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);
    }
  }
}

.register-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}

.register-code {
  width: 33%;
  height: 42px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
    border-radius: 6px;
    border: 1px solid #e0e6ed;
  }
}

.el-register-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: rgba(255, 255, 255, 0.8);
  font-family: 'Microsoft YaHei', Arial, sans-serif;
  font-size: 12px;
  letter-spacing: 1px;
  background: rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(5px);
}

.register-code-img {
  height: 42px;
  border-radius: 6px;
}

.link-type {
  color: #667eea;
  font-weight: 500;
  text-decoration: none;
  
  &:hover {
    color: #5a6fd8;
    text-decoration: underline;
  }
}
</style>
