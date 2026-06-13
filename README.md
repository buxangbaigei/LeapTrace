# LeapTrace（跃动健康）- AI驱动的智慧健康管家

> 基于 uni-app + Spring Boot + 通义千问大模型 | 拍照识食 + 自然语言交互

## 项目简介

**LeapTrace** 是一款由 **AI 深度驱动**的智慧健康管家应用。通过集成**通义千问大模型**的视觉与语言处理能力，实现从"拍照识食"到"自然语言交互"的便捷录入体验，彻底打破数据门槛。

### 核心亮点
-  **拍照识食**：拍照自动识别食物、计算热量
- **自然语言录入**：像聊天一样记录饮食运动
- **实时能量平衡**：动态追踪摄入 vs 消耗
- **视觉避坑**：高热量食物即时提醒

## 技术栈

### 前端（fit2）
| 技术 | 说明 |
|------|------|
| uni-app | 跨平台开发框架 |
| Vue 3 | 前端框架 |
| Vite | 构建工具 |

### 后端（demo）
| 技术 | 说明 |
|------|------|
| Spring Boot | 后端框架 |
| 通义千问 API | AI 视觉 + 语言能力 |
| MySQL | 数据持久化 |
| Maven | 依赖管理 |

## 项目结构
LeapTrace/
├── fit2/ # uni-app 前端
│ ├── pages/ # 页面文件
│ ├── components/ # 组件
│ ├── utils/ # 工具函数
│ └── static/ # 静态资源
├── demo/ # Spring Boot 后端
│ ├── src/main/java/ # Java 源码
│ └── src/main/resources/ # 配置文件
└── database/ # 数据库脚本


### 环境要求
- JDK 1.8+
- MySQL 5.7+
- Node.js 14+
- 通义千问 API Key

### 1. 克隆项目
```bash
git clone https://github.com/buxangbaigei/LeapTrace.git
2. 数据库配置
sql
CREATE DATABASE leaptrace;
-- 执行 database/schema.sql
3. 配置 AI API Key
修改 demo/src/main/resources/application.yml

4. 启动后端
bash
cd demo
mvn spring-boot:run
5. 启动前端
bash
cd fit2
npm install
npm run dev:h5
