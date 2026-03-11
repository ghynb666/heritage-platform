# 非遗平台开发进度

## 项目概述

基于 SpringBoot + Vue3 的非物质文化遗产展示与交流平台

---

## 已完成功能 (P0 核心基础模块)

### 一、后端 (SpringBoot 2.7.18)

#### 1. 项目基础架构
- [x] SpringBoot 项目初始化
- [x] MyBatis-Plus 集成
- [x] Knife4j API 文档 (访问: http://localhost:8080/doc.html)
- [x] 统一返回结果封装 `Result<T>`
- [x] 全局异常处理 `GlobalExceptionHandler`
- [x] 跨域配置 `CorsConfig`

#### 2. 认证授权
- [x] JWT Token 认证
- [x] Spring Security 配置
- [x] 登录接口 `POST /api/auth/login`
- [x] 注册接口 `POST /api/auth/register`
- [x] 三角色权限控制 (USER/INHERITOR/ADMIN)

#### 3. 用户管理
- [x] 用户实体 `SysUser`
- [x] 角色实体 `SysRole`
- [x] 用户角色关联 `SysUserRole`
- [x] 获取用户信息接口 `GET /api/user/info`

#### 4. 传承人申请
- [x] 申请实体 `InheritorApply`
- [x] 提交申请接口 `POST /api/inheritor/apply`
- [x] 查询申请状态接口 `GET /api/inheritor/apply/status`

#### 5. 传承人审核 (管理员)
- [x] 申请列表接口 `GET /api/admin/inheritor/apply/list`
- [x] 审核通过接口 `POST /api/admin/inheritor/apply/{id}/approve`
- [x] 审核拒绝接口 `POST /api/admin/inheritor/apply/{id}/reject`

#### 6. 公共功能
- [x] 文件上传接口 `POST /api/common/upload`
- [x] 非遗分类列表 `GET /api/heritage/category/list`
- [x] 公告列表接口 `GET /api/announcement/list`
- [x] 静态资源映射 `/uploads/**`

#### 7. 自动初始化
- [x] 管理员账号自动创建 (admin/admin123)
- [x] 角色数据自动初始化

---

### 二、前端 (Vue3 + Element Plus)

#### 1. 项目基础架构
- [x] Vite + Vue3 项目
- [x] Element Plus 组件库
- [x] Vue Router 路由
- [x] Pinia 状态管理
- [x] Axios 请求封装 (含 JWT 拦截器)

#### 2. 页面组件

| 页面 | 路径 | 功能 |
|------|------|------|
| 首页 | `/` | Hero区、非遗分类、精选非遗、数据统计、公告滚动 |
| 登录 | `/login` | 用户登录、表单验证 |
| 注册 | `/register` | 用户注册、角色选择 |
| 传承人申请 | `/inheritor-apply` | 申请表单、资质上传、状态展示 |
| 管理后台布局 | `/admin` | 侧边栏导航、顶部栏 |
| 控制台 | `/admin/dashboard` | 统计卡片、欢迎区 |
| 传承人审核 | `/admin/inheritor/audit` | 申请列表、详情弹窗、审核操作 |
| 用户列表 | `/admin/user/list` | 基础结构 |
| 传承人列表 | `/admin/inheritor/list` | 基础结构 |

#### 3. 公共组件
- [x] `AnnouncementScroll.vue` - 公告滚动组件

#### 4. 状态管理
- [x] `store/user.js` - 用户状态 (token、userInfo、roles)
- [x] 登录/注册/登出 Actions
- [x] 角色判断方法 `hasRole()`

#### 5. 路由守卫
- [x] Token 验证
- [x] 角色权限控制
- [x] 登录状态持久化

---

### 三、数据库

#### 已创建表
| 表名 | 说明 |
|------|------|
| sys_user | 用户表 |
| sys_role | 角色表 |
| sys_user_role | 用户角色关联表 |
| inheritor_apply | 传承人申请表 |
| heritage_category | 非遗分类表 |
| announcement | 公告表 |

#### 初始数据
- 管理员账号: `admin` / `admin123`
- 三种角色: USER、INHERITOR、ADMIN
- 8个非遗分类: 剪纸、刺绣、陶艺、皮影、木雕、漆器、织锦、年画
- 3条公告数据

---

### 四、UI 设计风格

采用新中式设计风格:
- **配色**: 墨黑、朱砂红、金色、青玉色、宣纸色
- **字体**: Noto Serif SC 宋体
- **元素**: 水墨留白、印章图标、书法字间距

---

## 待开发功能

### P1 - 非遗展示模块
- [ ] 非遗项目列表页
- [ ] 非遗项目详情页
- [ ] 分类筛选功能
- [ ] 搜索功能
- [ ] 点赞/收藏功能
- [ ] 评论功能
- [ ] 传承人主页

### P2 - 文创商品模块
- [ ] 商品列表页
- [ ] 商品详情页
- [ ] 购物车功能
- [ ] 订单功能
- [ ] 传承人商品管理

### P3 - 辅助功能模块
- [ ] 资讯模块
- [ ] 论坛模块
- [ ] 留言板模块
- [ ] 在线咨询模块
- [ ] 个人中心完善
- [ ] 管理后台完善

---

## 启动说明

### 后端启动
```bash
cd backend
mvn spring-boot:run
```
访问 API 文档: http://localhost:8080/doc.html

### 前端启动
```bash
cd frontend
npm install
npm run dev
```
访问: http://localhost:3000

### 数据库初始化
1. 创建数据库 `heritage_platform`
2. 执行 `DB/heritage_platform.sql` 建表
3. 执行 `DB/init_data.sql` 初始化数据

---

## 账号信息

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | admin123 |

---

## 技术栈

### 后端
- SpringBoot 2.7.18
- MyBatis-Plus 3.5.x
- Spring Security
- JWT (jjwt)
- Knife4j (Swagger)
- MySQL 8.x
- Lombok
- Hutool

### 前端
- Vue 3.x
- Vite
- Element Plus
- Vue Router
- Pinia
- Axios
