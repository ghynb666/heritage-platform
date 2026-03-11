# P0 - 项目初始化与基础架构

本阶段包含项目基础搭建和核心配置，是整个系统的基础。

---

## Task 0.1: 后端项目初始化

- [x] 创建SpringBoot项目，配置Maven依赖
- [x] 配置application.yml（数据库连接、文件上传等）
- [x] 集成MyBatis-Plus
- [x] 集成Knife4j API文档
- [x] 配置统一返回结果封装类
- [x] 配置全局异常处理器

---

## Task 0.2: 前端项目初始化

- [x] 使用Vite创建Vue3项目
- [x] 安装Element Plus组件库
- [x] 配置Vue Router路由
- [x] 配置Pinia状态管理
- [x] 配置Axios请求封装

---

## Task 0.3: 数据库设计与初始化

- [x] 创建数据库heritage_platform
- [x] 编写数据库初始化脚本（所有表结构）
- [x] 创建初始数据（管理员账号、角色、非遗分类、公告）

---

## Task 0.4: 基础配置

- [x] 配置跨域CORS
- [x] 配置静态资源映射
- [x] 配置文件上传路径

---

## Task Dependencies

- Task 0.2、0.3、0.4 可与 Task 0.1 并行执行
- 后续所有阶段依赖本阶段完成
