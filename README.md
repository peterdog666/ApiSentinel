# ApiSentinel 🛡️

<div align="center">

**轻量级 API 监控平台 | Lightweight API Monitoring Platform**

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Vue](https://img.shields.io/badge/Vue-3.x-42b883.svg)](https://vuejs.org/)
[![Docker](https://img.shields.io/badge/Docker-ready-2496ed.svg)](https://www.docker.com/)

[English](#english) | [中文](#中文)

</div>

---

## 中文

### 📖 项目介绍

ApiSentinel 是一个开箱即用的轻量级 API 监控平台。无需复杂配置，只需添加你的接口 URL，系统会自动定时检测接口健康状态，记录响应时间，并在异常时通过钉钉发送告警通知。

### ✨ 功能特性

- 🔍 **接口管理** — 添加/编辑/删除监控接口，支持 GET/POST/PUT/DELETE/HEAD 方法
- ⏱️ **定时检测** — 基于 Quartz 的定时任务，按配置间隔自动检测接口健康状态
- 📊 **可视化 Dashboard** — 总览卡片展示监控总数、正常/异常数量、今日检查次数
- 📈 **响应时间趋势图** — ECharts 折线图展示接口响应时间变化趋势
- 🔔 **钉钉告警** — 接口连续失败 N 次后自动发送钉钉 Webhook 告警
- 📋 **历史记录** — 查看每个接口的历史检查记录，支持分页
- 🐳 **Docker 一键部署** — 提供完整的 Docker Compose 配置

### 🚀 快速开始

#### 方式一：Docker Compose（推荐）

```bash
# 克隆项目
git clone https://github.com/your-username/ApiSentinel.git
cd ApiSentinel

# 一键启动（MySQL + 后端 + 前端）
docker-compose up -d

# 访问
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

### ⚙️ 配置说明

编辑 `backend/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/apisentinel
    username: your_username
    password: your_password
```

钉钉告警：在接口管理页面，为每个接口单独配置钉钉 Webhook 地址。

### 🛠️ 技术栈

| 层级 | 技术 |
|------|------|
| 后端框架 | Spring Boot 3 |
| ORM | MyBatis-Plus |
| 数据库 | MySQL 8 |
| 定时任务 | Quartz |
| 前端框架 | Vue 3 + Vite |
| UI 组件库 | Element Plus |
| 图表 | ECharts |
| HTTP 客户端 | Axios |
| 容器化 | Docker + Docker Compose |

---

## English

### 📖 Introduction

ApiSentinel is a lightweight, out-of-the-box API monitoring platform. No complex configuration needed — just add your API URLs, and the system will automatically check their health status on a schedule, record response times, and send DingTalk alerts when issues are detected.

### ✨ Features

- 🔍 **API Management** — Add/edit/delete monitored APIs, supporting GET/POST/PUT/DELETE/HEAD methods
- ⏱️ **Scheduled Checks** — Quartz-based scheduler automatically checks API health at configured intervals
- 📊 **Visual Dashboard** — Overview cards showing total monitors, normal/abnormal counts, and today's check count
- 📈 **Response Time Trends** — ECharts line charts showing response time trends
- 🔔 **DingTalk Alerts** — Automatically sends DingTalk Webhook alerts after N consecutive failures
- 📋 **History Records** — View historical check records for each API with pagination
- 🐳 **One-click Docker Deploy** — Complete Docker Compose configuration included

### 🚀 Quick Start

```bash
git clone https://github.com/your-username/ApiSentinel.git
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

## 📄 License

[MIT License](LICENSE)

## 🤝 Contributing

PRs and Issues are welcome! Feel free to open an issue or submit a pull request.

---

<div align="center">
Made with ❤️ | Star ⭐ if you find it useful!
</div>
