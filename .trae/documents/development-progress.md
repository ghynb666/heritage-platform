# 非遗平台开发进度

## 项目概述

基于 SpringBoot + Vue3 的非物质文化遗产展示与交流平台

---

## 开发进度总览

| 阶段 | 名称 | 状态 | 进度 |
|------|------|------|------|
| P0 | 项目初始化与基础架构 | ✅ 已完成 | 100% |
| P1 | 用户认证与授权 | ✅ 已完成 | 100% |
| P2 | 传承人申请与审核 | ✅ 已完成 | 100% |
| P3 | 非遗分类与项目展示 | ⏳ 未开始 | 0% |
| P4 | 非遗详情与传承人主页 | ⏳ 未开始 | 0% |
| P5 | 互动功能（点赞收藏评论） | ⏳ 未开始 | 0% |
| P6 | 文创商品展示 | ⏳ 未开始 | 0% |
| P7 | 购物车与订单交易 | ⏳ 未开始 | 0% |
| P8 | 资讯与论坛 | ⏳ 未开始 | 0% |
| P9 | 留言板与在线咨询 | ⏳ 未开始 | 0% |
| P10 | 个人中心与管理后台 | ⏳ 未开始 | 0% |

---

## 已完成功能 (P0-P2)

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

- [x] 用户信息获取接口 `GET /api/user/info`

#### 3. 传承人申请
- [x] 申请实体 `InheritorApply`
- [x] 提交申请接口 `POST /api/inheritor/apply`
- [x] 查询申请状态接口 `GET /api/inheritor/apply/status`

#### 4. 传承人审核 (管理员)
- [x] 申请列表接口 `GET /api/admin/inheritor/apply/list`
- [x] 审核通过接口 `POST /api/admin/inheritor/apply/{id}/approve`
- [x] 审核拒绝接口 `POST /api/admin/inheritor/apply/{id}/reject`

#### 5. 公共功能
- [x] 文件上传接口 `POST /api/common/upload`
- [x] 非遗分类列表 `GET /api/heritage/category/list`
- [x] 公告列表接口 `GET /api/announcement/list`
- [x] 静态资源映射 `/uploads/**`

#### 6. 自动初始化
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

| 页面 | 路径 | 功能 | 状态 |
|------|------|------|------|
| 首页 | `/` | Hero区、非遗分类、精选非遗、数据统计、公告滚动 | ✅ |
| 登录 | `/login` | 用户登录、表单验证 | ✅ |
| 注册 | `/register` | 用户注册、角色选择 | ✅ |
| 传承人申请 | `/inheritor-apply` | 申请表单、资质上传、状态展示 | ✅ |
| 管理后台布局 | `/admin` | 侧边栏导航、顶部栏 | ✅ |
| 控制台 | `/admin/dashboard` | 统计卡片、欢迎区 | ✅ |
| 传承人审核 | `/admin/inheritor/audit` | 申请列表、详情弹窗、审核操作 | ✅ |
| 用户列表 | `/admin/user/list` | 基础结构 | ✅ |
| 传承人列表 | `/admin/inheritor/list` | 基础结构 | ✅ |

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
| 表名 | 说明 | 状态 |
|------|------|------|
| sys_user | 用户表 | ✅ |
| sys_role | 角色表 | ✅ |
| sys_user_role | 用户角色关联表 | ✅ |
| inheritor_apply | 传承人申请表 | ✅ |
| heritage_category | 非遗分类表 | ✅ |
| announcement | 公告表 | ✅ |
| heritage_item | 非遗项目表 | ✅ 已建表 |
| heritage_image | 项目图片表 | ✅ 已建表 |
| heritage_video | 项目视频表 | ✅ 已建表 |
| product | 文创商品表 | ✅ 已建表 |
| product_category | 商品类型表 | ✅ 已建表 |
| cart | 购物车表 | ✅ 已建表 |
| order | 订单表 | ✅ 已建表 |
| order_item | 订单明细表 | ✅ 已建表 |
| news | 资讯表 | ✅ 已建表 |
| forum_post | 论坛帖子表 | ✅ 已建表 |
| message | 留言表 | ✅ 已建表 |
| banner | 轮播图表 | ✅ 已建表 |
| user_favorite | 用户收藏表 | ✅ 已建表 |
| user_like | 用户点赞表 | ✅ 已建表 |
| comment | 评论表 | ✅ 已建表 |
| consultation | 咨询表 | ✅ 已建表 |
| sensitive_word | 敏感词表 | ✅ 已建表 |

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

### P3 - 非遗分类与项目展示 (未开始)
- [ ] 非遗分类管理
- [ ] 非遗项目发布
- [ ] 文件上传功能
- [ ] 非遗展示页面

- [ ] 非遗详情页面
- [ ] 传承人主页
- [ ] 点赞/收藏功能
- [ ] 评论功能
- [ ] 敏感词过滤

- [ ] 商品分类管理
- [ ] 商品发布
- [ ] 商品展示页面
- [ ] 商品详情页面
- [ ] 购物车功能
- [ ] 订单功能
- [ ] 传承人商品管理

- [ ] 资讯发布
- [ ] 资讯列表页面
- [ ] 论坛帖子发布
- [ ] 论坛列表页面
- [ ] 留言发布
- [ ] 留言列表页面
- [ ] 全局咨询
- [ ] 专属咨询
- [ ] 个人信息管理
- [ ] 我的收藏/点赞/订单
- [ ] 传承人专属功能
- [ ] 用户管理
- [ ] 内容审核管理
- [ ] 数据统计

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
