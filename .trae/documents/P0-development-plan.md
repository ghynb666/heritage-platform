# P0 核心基础模块开发计划

## 概述

P0阶段是整个系统的基础，包含项目初始化、用户认证授权和传承人审核模块。

---

## 一、后端项目初始化

### 1.1 创建SpringBoot项目

**目标**: 创建SpringBoot 2.7.x项目，配置基础依赖

**步骤**:
1. 创建Maven项目 `heritage-backend`
2. 配置pom.xml依赖:
   - spring-boot-starter-web
   - spring-boot-starter-security
   - mybatis-plus-boot-starter
   - mysql-connector-java
   - jjwt (JWT)
   - knife4j-spring-boot-starter (API文档)
   - lombok
   - hutool (工具类)

### 1.2 配置application.yml

**内容**:
- 数据库连接配置
- MyBatis-Plus配置
- 文件上传配置
- JWT配置
- Swagger配置

### 1.3 基础框架搭建

**内容**:
- 统一返回结果封装类 `Result<T>`
- 全局异常处理器 `GlobalExceptionHandler`
- 自定义业务异常 `BusinessException`
- 跨域配置 `CorsConfig`

---

## 二、前端项目初始化

### 2.1 创建Vue3项目

**目标**: 使用Vite创建Vue3项目

**步骤**:
1. 执行 `npm create vite@latest heritage-frontend -- --template vue`
2. 安装依赖:
   - element-plus
   - vue-router
   - pinia
   - axios
   - @element-plus/icons-vue

### 2.2 配置项目结构

**目录结构**:
```
src/
├── api/           # API接口
├── assets/        # 静态资源
├── components/    # 公共组件
├── router/        # 路由配置
├── store/         # Pinia状态管理
├── utils/         # 工具函数
└── views/         # 页面组件
    ├── login/     # 登录注册
    ├── user/      # 普通用户页面
    ├── inheritor/ # 传承人页面
    └── admin/     # 管理员页面
```

### 2.3 配置axios封装

**内容**:
- 请求拦截器（添加Token）
- 响应拦截器（统一错误处理）
- API基础路径配置

---

## 三、数据库初始化

### 3.1 执行SQL脚本

**目标**: 执行 `DB/heritage_platform.sql`

**步骤**:
1. 创建数据库 `heritage_platform`
2. 执行所有建表语句

### 3.2 初始化基础数据

**内容**:
- 角色数据: 普通用户(USER)、传承人(INHERITOR)、管理员(ADMIN)
- 管理员账号: admin/admin123
- 非遗分类: 剪纸、刺绣、陶艺、皮影、木雕等
- 测试用户数据

---

## 四、用户实体与数据层

### 4.1 创建实体类

**文件**:
- `entity/SysUser.java` - 用户实体
- `entity/SysRole.java` - 角色实体
- `entity/SysUserRole.java` - 用户角色关联
- `entity/InheritorApply.java` - 传承人申请
- `entity/HeritageCategory.java` - 非遗分类

### 4.2 创建Mapper

**文件**:
- `mapper/UserMapper.java`
- `mapper/RoleMapper.java`
- `mapper/UserRoleMapper.java`
- `mapper/InheritorApplyMapper.java`
- `mapper/HeritageCategoryMapper.java`

### 4.3 创建DTO/VO

**文件**:
- `dto/LoginDTO.java` - 登录请求
- `dto/RegisterDTO.java` - 注册请求
- `dto/InheritorApplyDTO.java` - 传承人申请
- `vo/UserVO.java` - 用户信息
- `vo/LoginVO.java` - 登录返回

---

## 五、JWT认证实现

### 5.1 JWT工具类

**文件**: `util/JwtUtil.java`

**功能**:
- 生成Token
- 解析Token
- 验证Token有效性
- 从Token获取用户ID

### 5.2 Security配置

**文件**: `config/SecurityConfig.java`

**内容**:
- 配置白名单路径（登录、注册、静态资源）
- 禁用CSRF
- 配置Session策略（STATELESS）

### 5.3 认证过滤器

**文件**: `security/JwtAuthenticationFilter.java`

**功能**:
- 从Header获取Token
- 验证Token并设置认证信息

### 5.4 认证服务

**文件**: `service/AuthService.java`

**接口**:
- `login(LoginDTO)` - 用户登录
- `register(RegisterDTO)` - 用户注册
- `logout()` - 用户登出
- `getUserInfo()` - 获取当前用户信息

---

## 六、角色权限管理

### 6.1 权限注解

**文件**: `annotation/RequireRole.java`

**功能**: 标记接口所需角色

### 6.2 权限拦截器

**文件**: `security/RoleInterceptor.java`

**功能**: 验证用户角色是否满足要求

### 6.3 用户服务

**文件**: `service/UserService.java`

**接口**:
- `getUserById(Long)` - 根据ID获取用户
- `updateUserInfo(UserDTO)` - 更新用户信息
- `getUserRoles(Long)` - 获取用户角色

---

## 七、传承人申请功能（前台）

### 7.1 后端接口

**文件**: `controller/user/InheritorController.java`

**接口**:
- `POST /api/user/inheritor/apply` - 提交传承人申请
- `GET /api/user/inheritor/apply/status` - 查询申请状态

### 7.2 申请服务

**文件**: `service/InheritorApplyService.java`

**功能**:
- 提交申请（保存资质证明、证书图片）
- 查询申请状态
- 检查是否已申请

### 7.3 文件上传

**文件**: `controller/common/UploadController.java`

**接口**:
- `POST /api/common/upload` - 文件上传

**功能**:
- 上传资质证明图片
- 上传证书图片
- 返回图片URL

### 7.4 前端页面

**文件**: `views/user/InheritorApply.vue`

**功能**:
- 表单：真实姓名、身份证号、非遗类型选择
- 资质证明图片上传
- 证书图片上传
- 申请说明文本框
- 提交按钮

---

## 八、传承人审核功能（后台）

### 8.1 后端接口

**文件**: `controller/admin/InheritorAuditController.java`

**接口**:
- `GET /api/admin/inheritor/apply/list` - 申请列表（分页）
- `GET /api/admin/inheritor/apply/{id}` - 申请详情
- `POST /api/admin/inheritor/apply/{id}/approve` - 审核通过
- `POST /api/admin/inheritor/apply/{id}/reject` - 审核拒绝

### 8.2 审核服务

**文件**: `service/InheritorAuditService.java`

**功能**:
- 分页查询申请列表
- 查看申请详情
- 审核通过：更新用户角色、更新用户非遗类型
- 审核拒绝：记录拒绝原因

### 8.3 前端页面

**文件**: `views/admin/InheritorAudit.vue`

**功能**:
- 申请列表表格（申请人、申请类型、申请时间、状态）
- 查看详情弹窗（展示资质证明、证书图片）
- 审核操作（通过/拒绝按钮）
- 拒绝时填写拒绝原因

---

## 九、前端登录注册页面

### 9.1 登录页面

**文件**: `views/login/Login.vue`

**UI要求**:
- 用户名输入框
- 密码输入框
- 登录按钮（靠左）
- 忘记密码链接（右下角）
- 跳转注册链接

**功能**:
- 表单验证
- 登录成功存储Token
- 根据角色跳转对应页面

### 9.2 注册页面

**文件**: `views/login/Register.vue`

**UI要求**:
- 用户名输入框
- 密码输入框
- 确认密码输入框
- 角色选择（单选：普通用户/传承人）
- 注册按钮
- 跳转登录链接

**功能**:
- 表单验证
- 注册成功跳转登录

### 9.3 状态管理

**文件**: `store/user.js`

**状态**:
- token
- userInfo
- roles

**Actions**:
- login
- register
- logout
- getUserInfo

### 9.4 路由守卫

**文件**: `router/index.js`

**功能**:
- 验证Token有效性
- 根据角色控制页面访问
- 未登录跳转登录页

---

## 十、系统首页基础框架

### 10.1 前台首页布局

**文件**: `views/Home.vue`

**布局**:
- 顶部导航栏（Logo、菜单、用户信息）
- 轮播公告区域
- 内容区域（推荐非遗、热门商品、最新资讯）
- 底部版权信息

### 10.2 公告滚动

**文件**: `components/AnnouncementScroll.vue`

**功能**:
- 滚动展示公告标题
- 点击查看公告详情

### 10.3 后台管理框架

**文件**: `views/admin/Layout.vue`

**布局**:
- 左侧纵向菜单
- 顶部标题栏
- 主内容区域

**菜单结构**:
```
用户管理
  ├── 用户列表
  ├── 传承人账号
  ├── 传承人审核
  └── 地区管理
非遗项目管理
  ├── 项目列表
  └── 分类管理
文创管理
  ├── 商品列表
  └── 类型管理
资讯管理
  ├── 公告管理
  ├── 轮播图管理
  ├── 资讯管理
  └── 论坛管理
```

---

## 开发顺序

1. **后端初始化** → 数据库初始化 → 实体类和Mapper
2. **JWT认证** → 角色权限管理
3. **前端初始化** → axios封装 → 路由配置
4. **登录注册页面** → 状态管理 → 路由守卫
5. **传承人申请（前台）** → 文件上传
6. **传承人审核（后台）** → 后台框架
7. **前台首页** → 公告滚动

---

## 验收标准

详见 [checklist.md](.trae/specs/heritage-platform/P0/checklist.md)
