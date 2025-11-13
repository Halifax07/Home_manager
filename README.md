# 家庭财务管理系统

<div align="center">

![License](https://img.shields.io/badge/license-MIT-blue.svg)
![Java](https://img.shields.io/badge/Java-17-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.5.15-brightgreen.svg)
![Vue](https://img.shields.io/badge/Vue.js-2.6.12-green.svg)

基于 RuoYi-Vue 开发的家庭财务管理系统，支持多家庭、多成员协同管理。

</div>

## 📋 项目简介

家庭财务管理系统采用 Spring Boot + Vue.js 前后端分离架构，实现家庭成员管理、收支记录、数据统计分析等功能，支持细粒度的权限控制。

### ✨ 核心特性

- 🏠 多家庭支持，数据隔离
- 👥 灵活的家庭成员管理，支持角色分配
- 🔐 管理员与普通成员分级权限控制
- 📊 丰富的图表展示和数据分析
- 📝 便捷的收支记录管理
- ✅ 新用户注册审批流程

##  快速开始

### 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 5.7+ / 8.0+
- Redis 6.x+
- Node.js 12+

### 后端启动

1. 克隆项目并导入数据库脚本（`sql` 目录下）
2. 修改配置文件 `application-druid.yml` 中的数据库和 Redis 连接信息
3. 编译打包：`mvn clean package`
4. 启动服务：`java -jar ruoyi-admin/target/ruoyi-admin.jar`

后端服务地址：`http://localhost:8080`

### 前端启动

```bash
cd ruoyi-ui
npm install
npm run dev
```

前端访问地址：`http://localhost:80`

### 默认账号

管理员：`admin` / `admin123`

## 🏗️ 技术架构

**后端：** Spring Boot 2.5.15 + Spring Security + MyBatis + MySQL + Redis + JWT  
**前端：** Vue.js 2.6.12 + Element UI + Axios + Vuex + ECharts

## 📖 功能模块

- **用户认证** - 登录/注册/验证码/JWT 认证/权限控制
- **成员管理** - 家庭创建/成员管理/角色分配/注册审批
- **收支记录** - 增删改查/分类管理/数据导出/权限控制
- **数据统计** - 收支概览/趋势图/分类统计/成员排行
- **系统管理** - 用户/角色/菜单/日志管理

## 🔐 权限设计

### 角色权限

| 角色 | 权限范围 |
|------|---------|
| 家庭管理员 | 可查看和管理所有家庭成员的数据、审批注册、管理成员 |
| 普通成员 | 仅可查看和管理自己的收支数据 |

系统采用双表认证机制（`sys_user` + `family_members`），通过角色和成员ID进行数据权限过滤。

## � 项目结构

```
RuoYi-Vue-master/
├── sql/                    # 数据库脚本
├── ruoyi-admin/           # 后台服务（启动入口）
├── ruoyi-framework/       # 框架核心
├── ruoyi-system/          # 系统管理
├── ruoyi-families/        # 家庭管理
├── ruoyi-transactions/    # 交易记录
├── ruoyi-ui/             # 前端 Vue 项目
└── pom.xml               # Maven 配置
```

## 📄 开源协议

本项目基于 MIT 协议开源。

## 🙏 致谢

感谢 [RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue) 提供的优秀基础框架。

---

<div align="center">



</div>
