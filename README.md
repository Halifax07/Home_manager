# 家庭财务管理系统

<div align="center">

![License](https://img.shields.io/badge/license-MIT-blue.svg)
![Java](https://img.shields.io/badge/Java-17-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.5.15-brightgreen.svg)
![Vue](https://img.shields.io/badge/Vue.js-2.6.12-green.svg)

基于 RuoYi-Vue 开发的家庭财务管理系统，支持多家庭、多成员协同管理。

<img width="2550" height="1310" alt="image" src="https://github.com/user-attachments/assets/e335f0fe-3e99-47c5-bde9-22ff39ee0415" />


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

## 🚀 快速开始

### 环境要求

后端与前端构建运行所需的基础环境（也同步写入 `requirements.txt` 方便查看）：

- JDK 17+
- Maven 3.6.3+
- MySQL 5.7+ / 8.0+
- Redis 6.x+
- Node.js 16+ / npm 8+
- Vue CLI (可选, 用于本地调试脚手架)

> 若使用 Docker / 云环境，可按需映射 MySQL 与 Redis 服务，无强依赖本机安装。

### 数据库初始化

按以下顺序执行 `sql/` 目录中的脚本（避免外键或依赖缺失）：

1. `ry_20250522.sql` （系统基础表结构与初始数据）
2. `quartz.sql` （定时任务相关表）
3. `family_register_table.sql` （家庭注册申请表）
4. `family_management.sql` （家庭、成员、交易等业务表）
5. `family_register_setup.sql` （开启注册功能 + 初始化角色与菜单权限）

完成后确认：
```sql
SELECT config_value FROM sys_config WHERE config_key = 'sys.account.registerUser';
SELECT role_id, role_name FROM sys_role WHERE role_id IN (100,101);
```

### 配置调整

修改后端 `ruoyi-admin/src/main/resources/application-druid.yml`：

```yaml
spring:
	datasource:
		master:
			url: jdbc:mysql://127.0.0.1:3306/ruoyi?useSSL=false&useUnicode=true&characterEncoding=utf8
			username: root
			password: ****
	redis:
		host: 127.0.0.1
		port: 6379
```

如需关闭验证码（测试用），可在系统配置中修改 `sys.account.captchaEnabled=false`。

### 后端构建与启动
注意：启动后端前一定要开启redis！！！
```bash
# 1. 清理 & 编译（跳过测试可加 -DskipTests）
mvn clean package -DskipTests

# 2. 运行（开发期直接用 IDE 也可）
java -jar ruoyi-admin/target/ruoyi-admin.jar
```

后端默认端口：`http://localhost:8080`

常用 Maven 命令：
```bash
mvn clean              # 清理
mvn test               # 运行测试（若已编写）
mvn package -DskipTests # 打包跳过测试
mvn dependency:tree    # 查看依赖树
```

### 前端启动（开发模式）

```bash
cd ruoyi-ui
npm install            # 安装依赖
npm run dev            # 启动开发服务（含代理）
```




### 默认账号与注册

初始管理员：`admin / admin123`

普通用户注册流程：
1. 在前端注册页提交信息（选择“普通成员”并填写目标家庭名称）
2. 后端写入 `family_register` 状态为待审批（当前版本为自动审批）
3. 审批通过后生成 `sys_user` + `family_members` 记录
4. 登录后仅能查看、管理自己的收支与个人统计

家庭管理员注册：
1. 选择“家庭管理员”并填写家庭名称
2. 系统创建家庭（`families`），并建立管理员成员记录
3. 获得全家数据管理与成员审批权限

## 🏗️ 技术架构概述

**后端：** Spring Boot 2.5.15 + Spring Security + MyBatis + MySQL + Redis + JWT  
**前端：** Vue.js 2.6.12 + Element UI + Axios + Vuex + ECharts

## 📖 功能模块与说明

- **用户认证** - 登录/注册/验证码/JWT 认证/权限控制
- **成员管理** - 家庭创建/成员管理/角色分配/注册审批
- **收支记录** - 增删改查/分类管理/数据导出/权限控制
- **数据统计** - 收支概览/趋势图/分类统计/成员排行
- **系统管理** - 用户/角色/菜单/日志管理

## 🔐 权限与数据隔离

### 角色权限

| 角色 | 权限范围 |
|------|---------|
| 家庭管理员 | 可查看和管理所有家庭成员的数据、审批注册、管理成员 |
| 普通成员 | 仅可查看和管理自己的收支数据 |

系统采用双表认证机制（`sys_user` + `family_members`），通过角色和成员ID进行数据权限过滤。

## 🗂 项目结构

```
RuoYi-Vue-master/
├── sql/                  # 所有初始化与业务脚本
├── ruoyi-admin/          # 后端入口 (Controller 层)
├── ruoyi-framework/      # 框架封装（安全 / 异步 / 通用组件）
├── ruoyi-system/         # 业务与数据访问（家庭/成员/交易/统计）
├── ruoyi-quartz/         # 定时任务模块
├── ruoyi-common/         # 公共工具与基础类
├── ruoyi-ui/             # Vue 前端项目
├── README.md             # 项目说明
├── requirements.txt      # 环境依赖声明（非 Python）
└── pom.xml               # Maven 父配置
```

> 说明：原始的 `ruoyi-families / ruoyi-transactions / ruoyi-users` 等空模块已合并精简进 `ruoyi-system`，避免重复与维护负担。

## 🧪 常见验证步骤

```bash
# 1. 数据库脚本已导入
mysql -uroot -p -e "SHOW TABLES FROM ruoyi;"

# 2. Redis 正常
redis-cli PING

# 3. 后端服务端口开放
curl http://localhost:8080

# 4. 登录接口可访问
curl -X POST http://localhost:8080/login
```

## 🛠 常见问题排查

| 问题 | 原因 | 解决 |
|------|------|------|
| 登录验证码始终错误 | Redis 未启动 / 缓存键丢失 | 启动 Redis 或关闭验证码配置 |
| 注册后无法登录 | `family_register` 未自动审批 | 检查自动审批逻辑或手动更新状态 |
| 管理员无法看到成员统计 | 成员未正确关联家庭ID | 检查 `family_members.family_id` 字段 |
| 前端接口 404 | 代理路径与后端 `context-path` 不一致 | 确认 Nginx `/prod-api/` 转发配置 |

## 📦 生产部署简述

最简部署（单机）：
1. 安装并启动 MySQL + Redis
2. 导入 SQL 脚本
3. 后端：`mvn clean package -DskipTests && java -jar ruoyi-admin/target/ruoyi-admin.jar`
4. 前端：`npm run build:prod` → 将 `dist/` 放入 Nginx

## 📄 开源协议

## 📄 开源协议

本项目基于 MIT 协议开源。

## 🙏 致谢

感谢 [RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue) 基础框架支持；如需二次分发请保留原始协议与声明。

---

<div align="center">
如需新增功能（预算规划 / 发票 OCR / 多币种 / API 接口）欢迎提交 Issue 或 PR 🤝
</div>
