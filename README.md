# ApiSentinel 🛡️ 

<div align="center">

**你的 API 还活着吗？ApiSentinel 帮你 24/7 监控**

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Vue](https://img.shields.io/badge/Vue-3.x-42b883.svg)](https://vuejs.org/)
[![Docker](https://img.shields.io/badge/Docker-ready-2496ed.svg)](https://www.docker.com/)

[English](#english) | [中文](#中文)

</div>

---

## 中文

### 🎯 一句话介绍

**轻量级 API 监控平台**，无需复杂配置，一键启动，自动监控你的接口健康状态，异常时钉钉告警。比 Prometheus 轻，比 Datadog 便宜，比自己写脚本靠谱。

### 📊 与竞品对比

| 特性 | ApiSentinel | Prometheus + Grafana | Datadog | 自己写脚本 |
|------|:---:|:---:|:---:|:---:|
| **部署复杂度** | ⭐ 极简 | ⭐⭐⭐⭐ 复杂 | ⭐⭐ 中等 | ⭐⭐⭐ 复杂 |
| **学习成本** | ⭐ 无 | ⭐⭐⭐ 高 | ⭐⭐ 中 | ⭐⭐⭐ 高 |
| **成本** | 💰 免费 | 💰 免费 | 💰💰💰 昂贵 | 💰 免费 |
| **开箱即用** | ✅ 是 | ❌ 否 | ✅ 是 | ❌ 否 |
| **钉钉告警** | ✅ 内置 | ❌ 需配置 | ✅ 支持 | ❌ 需自己写 |
| **Web UI** | ✅ 美观 | ✅ 强大 | ✅ 专业 | ❌ 无 |
| **适合小团队** | ✅ 完美 | ⚠️ 可以 | ❌ 太贵 | ⚠️ 维护难 |

### 💡 使用场景

- 🚀 **创业公司**：快速搭建 API 监控，无需 DevOps 专家
- 👨‍💻 **个人开发者**：监控自己的 Side Project，及时发现问题
- 🏢 **小团队**：监控内部 API、第三方服务，钉钉告警通知全队
- 🔗 **SaaS 服务**：监控关键接口的可用性，保证 SLA
- 📊 **数据平台**：监控数据接口的响应时间，及时发现性能问题

### ✨ 核心功能

#### 1️⃣ 接口管理
- 添加/编辑/删除监控接口
- 支持 GET/POST/PUT/DELETE/HEAD 方法
- 自定义请求头和请求体
- 灵活的检查间隔配置（10秒 ~ 24小时）

#### 2️⃣ 定时检测
- 基于 Quartz 的精准定时任务
- 自动检测接口健康状态
- 记录响应时间、状态码、错误信息
- 支持启用/禁用单个接口

#### 3️⃣ 可视化 Dashboard
```
┌─────────────────────────────────────────┐
│  监控总数: 12  │  正常: 10  │  异常: 2  │
└─────────────────────────────────────────┘
│ 接口名称 │ 状态 │ 响应时间 │ 最后检查时间 │
├──────────┼──────┼──────────┼──────────────┤
│ API 1    │ ✅   │ 120ms    │ 2分钟前      │
│ API 2    │ ❌   │ -        │ 5分钟前      │
└──────────┴──────┴──────────┴──────────────┘
```

#### 4️⃣ 响应时间趋势图
- ECharts 折线图展示接口响应时间变化
- 一眼看出性能瓶颈
- 支持按时间范围筛选

#### 5️⃣ 钉钉告警
- 接口连续失败 N 次后自动告警
- 支持自定义告警阈值
- 钉钉群通知，全队实时掌握

#### 6️⃣ 历史记录
- 查看每个接口的完整检查历史
- 支持分页查询
- 导出数据用于分析

### 🚀 快速开始

#### 方式一：Docker Compose（推荐，一键启动）

```bash
# 克隆项目
git clone https://github.com/peterdog666/ApiSentinel.git
cd ApiSentinel

# 一键启动（MySQL + 后端 + 前端）
docker-compose up -d

# 等待 30 秒，然后访问
# 前端：http://localhost
# 后端 API：http://localhost:8080
```

#### 方式二：本地开发

**后端**
```bash
cd backend

# 修改数据库配置
vim src/main/resources/application.yml

# 启动
mvn spring-boot:run
```

**前端**
```bash
cd frontend

# 安装依赖
npm install

# 开发模式
npm run dev

# 构建
npm run build
```

### 📸 功能演示

> **Dashboard 总览** - 一眼看出所有接口的健康状态
> ![Dashboard](docs/screenshots/dashboard.png)

> **接口管理** - 轻松添加和配置监控接口
> ![API List](docs/screenshots/api-list.png)

> **接口详情** - 查看响应时间趋势和历史记录
> ![API Detail](docs/screenshots/api-detail.png)

### 🛠️ 技术栈

| 层级 | 技术 | 版本 |
|------|------|------|
| 后端框架 | Spring Boot | 3.x |
| ORM | MyBatis-Plus | 3.5+ |
| 数据库 | MySQL | 8.0+ |
| 定时任务 | Quartz | 2.3+ |
| 前端框架 | Vue | 3.x |
| UI 组件库 | Element Plus | 2.5+ |
| 图表 | ECharts | 5.x |
| HTTP 客户端 | Axios | 1.x |
| 容器化 | Docker | 20.10+ |

### 📖 详细文档

- [安装指南](docs/INSTALL.md)
- [配置说明](docs/CONFIG.md)
- [API 文档](docs/API.md)
- [常见问题](docs/FAQ.md)

### ⚙️ 配置说明

编辑 `backend/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/apisentinel
    username: root
    password: your_password
  jpa:
    hibernate:
      ddl-auto: update

# 钉钉告警配置（可选）
dingtalk:
  webhook: https://oapi.dingtalk.com/robot/send?access_token=YOUR_TOKEN
```

### ❓ FAQ

**Q: 为什么选择 ApiSentinel 而不是 Prometheus？**
A: Prometheus 功能强大但配置复杂，需要专业的 DevOps。ApiSentinel 专注于 API 监控，开箱即用，特别适合小团队。

**Q: 支持监控多少个接口？**
A: 理论上无限制，实际取决于你的服务器资源。单台服务器轻松支持 1000+ 接口监控。

**Q: 数据会保存多久？**
A: 默认保存 90 天，可在配置中修改。

**Q: 支持 HTTPS 接口吗？**
A: 完全支持，包括自签名证书。

**Q: 可以监控内网接口吗？**
A: 可以，只要 ApiSentinel 服务器能访问到即可。

### 🤝 贡献

欢迎提交 Issue 和 Pull Request！

- 🐛 发现 Bug？[提交 Issue](https://github.com/peterdog666/ApiSentinel/issues)
- 💡 有新想法？[讨论 Discussion](https://github.com/peterdog666/ApiSentinel/discussions)
- 🔧 想贡献代码？[Fork 并提交 PR](https://github.com/peterdog666/ApiSentinel/pulls)

### 📄 License

MIT License - 详见 [LICENSE](LICENSE) 文件

---

## English

### 🎯 One-liner

**Lightweight API monitoring platform** - No complex setup, one-click deployment, automatic health checks, DingTalk alerts. Lighter than Prometheus, cheaper than Datadog, more reliable than DIY scripts.

### ✨ Key Features

- 🔍 **API Management** - Add/edit/delete monitored APIs with GET/POST/PUT/DELETE/HEAD support
- ⏱️ **Scheduled Checks** - Quartz-based scheduler checks API health at configured intervals
- 📊 **Visual Dashboard** - Overview cards showing total monitors, normal/abnormal counts
- 📈 **Response Time Trends** - ECharts line charts showing response time changes
- 🔔 **DingTalk Alerts** - Automatic alerts after N consecutive failures
- 📋 **History Records** - View complete check history with pagination
- 🐳 **One-click Docker Deploy** - Complete Docker Compose configuration

### 🚀 Quick Start

```bash
git clone https://github.com/peterdog666/ApiSentinel.git
cd ApiSentinel
docker-compose up -d
# Frontend: http://localhost
# Backend API: http://localhost:8080
```

### 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Spring Boot 3 |
| ORM | MyBatis-Plus |
| Database | MySQL 8 |
| Scheduler | Quartz |
| Frontend | Vue 3 + Vite |
| UI Library | Element Plus |
| Charts | ECharts |
| HTTP Client | Axios |
| Container | Docker + Docker Compose |

---

<div align="center">

⭐ **如果觉得有帮助，请给个 Star！** ⭐

Made with ❤️ | [Issues](https://github.com/peterdog666/ApiSentinel/issues) | [Discussions](https://github.com/peterdog666/ApiSentinel/discussions)

</div>
