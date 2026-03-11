# P4 开发计划 - 管理员后台-用户管理

## 开发顺序

```
Phase 1: 后端基础 (Task 4.1 + 4.2 + 4.3 并行)
    ↓
Phase 2: 前端页面 (Task 4.4 + 4.5 + 4.6)
    ↓
Phase 3: 集成测试
```

***

## 现有代码分析

### 已有文件

| 文件                 | 状态      | 说明               |
| ------------------ | ------- | ---------------- |
| SysUser.java       | ✅ 已存在   | 用户实体，包含所有必要字段    |
| UserMapper.java    | ✅ 已存在   | 基础Mapper         |
| UserService.java   | ✅ 已存在   | 基础服务，需扩展         |
| UserList.vue       | ⚠️ 基础结构 | 需完善搜索、详情、删除、重置密码 |
| InheritorList.vue  | ⚠️ 基础结构 | 需完善功能            |
| InheritorAudit.vue | ✅ 已存在   | 传承人审核已完成         |

### 需新增文件

| 文件                       | 说明        |
| ------------------------ | --------- |
| AdminUserController.java | 管理员用户管理接口 |
| Region.java              | 地区实体      |
| RegionMapper.java        | 地区Mapper  |
| RegionService.java       | 地区服务      |
| RegionController.java    | 地区接口      |
| UserVO.java              | 扩展用户VO    |
| Region.vue               | 地区管理页面    |

***

## Phase 1: 后端基础

### Task 4.1: 用户列表管理后端

**接口清单**

| 方法     | 路径                                  | 说明              | 权限    |
| ------ | ----------------------------------- | --------------- | ----- |
| GET    | /api/admin/user/page                | 用户分页列表（支持搜索、筛选） | ADMIN |
| GET    | /api/admin/user/{id}                | 用户详情            | ADMIN |
| PUT    | /api/admin/user/{id}/status         | 禁用/启用用户         | ADMIN |
| DELETE | /api/admin/user/{id}                | 删除用户            | ADMIN |
| PUT    | /api/admin/user/{id}/reset-password | 重置密码            | ADMIN |

**查询参数**

```
page: 页码
size: 每页数量
keyword: 搜索关键词（用户名/昵称/手机号）
status: 状态筛选
role: 角色筛选（USER/INHERITOR）
```

**UserVO扩展**

```java
public class UserVO {
    // 基础字段（已有）
    private Long id;
    private String username;
    private String nickname;
    private String avatar;
    private String email;
    private String phone;
    private Integer gender;
    private LocalDateTime birthday;
    private String province;
    private String city;
    private String area;
    private String address;
    private String signature;
    private Long heritageCategoryId;
    private String heritageCategoryName;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime lastLoginTime;
    
    // 新增字段
    private List<String> roles;
    private String roleNames;
}
```

***

### Task 4.2: 传承人账号管理后端

**接口清单**

| 方法  | 路径                                 | 说明      | 权限    |
| --- | ---------------------------------- | ------- | ----- |
| GET | /api/admin/inheritor/page          | 传承人分页列表 | ADMIN |
| GET | /api/admin/inheritor/{id}          | 传承人详情   | ADMIN |
| PUT | /api/admin/inheritor/{id}/category | 修改非遗类型  | ADMIN |
| PUT | /api/admin/inheritor/{id}/revoke   | 取消传承人资格 | ADMIN |

**查询参数**

```
page: 页码
size: 每页数量
keyword: 搜索关键词
heritageCategoryId: 非遗分类筛选
```

**取消传承人资格逻辑**

1. 从sys\_user\_role删除INHERITOR角色
2. 清空heritage\_category\_id字段
3. 保留USER角色

***

### Task 4.3: 地区管理后端

**数据库表（需新建）**

```sql
CREATE TABLE `sys_region` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '地区ID',
  `parent_id` bigint DEFAULT 0 COMMENT '父级ID',
  `name` varchar(50) NOT NULL COMMENT '地区名称',
  `code` varchar(20) DEFAULT NULL COMMENT '地区编码',
  `level` tinyint DEFAULT 1 COMMENT '层级：1省 2市 3区',
  `sort` int DEFAULT 0 COMMENT '排序',
  `status` tinyint DEFAULT 1 COMMENT '状态',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='地区表';
```

**实体设计**

```java
// Region.java
- id, parentId, name, code, level, sort, status
- createTime, updateTime, deleted
```

**接口清单**

| 方法     | 路径                     | 说明     | 权限    |
| ------ | ---------------------- | ------ | ----- |
| GET    | /api/admin/region/tree | 地区树形结构 | ADMIN |
| POST   | /api/admin/region      | 新增地区   | ADMIN |
| PUT    | /api/admin/region      | 更新地区   | ADMIN |
| DELETE | /api/admin/region/{id} | 删除地区   | ADMIN |

***

## Phase 2: 前端页面

在开发前端页面时必须去调用front-design的skill，以便优化页面设计

### Task 4.4: 前端用户管理页面

**页面: /admin/user/list**

**功能清单**

* [x] 基础表格展示

* [ ] 搜索框（用户名/昵称/手机号）

* [ ] 状态筛选下拉

* [ ] 角色筛选下拉

* [ ] 用户详情弹窗

* [ ] 禁用/启用功能（已有）

* [ ] 删除功能

* [ ] 重置密码功能

**搜索筛选组件**

```vue
<div class="search-bar">
  <el-input v-model="keyword" placeholder="用户名/昵称/手机号" />
  <el-select v-model="status" placeholder="状态">
    <el-option label="全部" value="" />
    <el-option label="正常" :value="1" />
    <el-option label="禁用" :value="0" />
  </el-select>
  <el-select v-model="role" placeholder="角色">
    <el-option label="全部" value="" />
    <el-option label="普通用户" value="USER" />
    <el-option label="传承人" value="INHERITOR" />
  </el-select>
  <el-button type="primary" @click="handleSearch">搜索</el-button>
  <el-button @click="handleReset">重置</el-button>
</div>
```

**详情弹窗字段**

* 基本信息：头像、用户名、昵称、性别、生日、手机号、邮箱

* 地区信息：省/市/区、详细地址

* 传承人信息：非遗分类（如有）

* 账号信息：角色、状态、注册时间、最后登录时间

***

### Task 4.5: 前端传承人账号页面

**页面: /admin/inheritor/list**

**功能清单**

* [ ] 传承人列表表格

* [ ] 搜索框（用户名/昵称）

* [ ] 非遗分类筛选

* [ ] 传承人详情弹窗

* [ ] 修改非遗类型功能

* [ ] 取消传承人资格功能

**表格字段**

| 字段    | 说明                   |
| ----- | -------------------- |
| ID    | 用户ID                 |
| 用户名   | username             |
| 昵称    | nickname             |
| 头像    | avatar               |
| 非遗分类  | heritageCategoryName |
| 发布项目数 | 统计字段                 |
| 注册时间  | createTime           |
| 操作    | 详情、修改类型、取消资格         |

***

### Task 4.6: 前端地区管理页面

**页面: /admin/region**

**功能清单**

* [ ] 树形结构展示

* [ ] 新增地区（省/市/区）

* [ ] 编辑地区

* [ ] 删除地区

**树形组件**

```vue
<el-tree
  :data="regionTree"
  :props="{ label: 'name', children: 'children' }"
  node-key="id"
  default-expand-all
>
  <template #default="{ node, data }">
    <span class="tree-node">
      <span>{{ data.name }}</span>
      <span class="tree-actions">
        <el-button size="small" @click="handleAdd(data)">添加下级</el-button>
        <el-button size="small" @click="handleEdit(data)">编辑</el-button>
        <el-button size="small" type="danger" @click="handleDelete(data)">删除</el-button>
      </span>
    </span>
  </template>
</el-tree>
```

***

## Phase 3: 集成测试

### 验收项目

* [ ] 用户列表分页查询正常（支持搜索、筛选）

* [ ] 用户详情查询正常

* [ ] 用户禁用/启用正常

* [ ] 用户删除正常

* [ ] 用户重置密码正常

* [ ] 传承人列表查询正常

* [ ] 传承人详情查询正常

* [ ] 传承人非遗类型修改正常

* [ ] 传承人资格取消正常

* [ ] 地区树形结构查询正常

* [ ] 地区增删改正常

* [ ] 前端用户管理页面功能正常

* [ ] 前端传承人账号页面功能正常

* [ ] 前端地区管理页面功能正常

***

## 执行计划

| 步骤 | 任务          | 预计产出                                     |
| -- | ----------- | ---------------------------------------- |
| 1  | Task 4.1 后端 | AdminUserController、扩展UserService、UserVO |
| 2  | Task 4.2 后端 | 传承人管理接口（复用UserService）                   |
| 3  | Task 4.3 后端 | Region实体、Mapper、Service、Controller       |
| 4  | Task 4.4 前端 | 完善UserList.vue                           |
| 5  | Task 4.5 前端 | 完善InheritorList.vue                      |
| 6  | Task 4.6 前端 | 新建Region.vue                             |
| 7  | 路由配置        | 添加地区管理路由                                 |
| 8  | 集成测试        | 完整流程验证                                   |

***

## 接口汇总

### 用户管理

```
GET  /api/admin/user/page          用户分页列表
GET  /api/admin/user/{id}          用户详情
PUT  /api/admin/user/{id}/status   禁用/启用
DELETE /api/admin/user/{id}        删除用户
PUT  /api/admin/user/{id}/reset-password  重置密码
```

### 传承人管理

```
GET  /api/admin/inheritor/page     传承人分页列表
GET  /api/admin/inheritor/{id}     传承人详情
PUT  /api/admin/inheritor/{id}/category  修改非遗类型
PUT  /api/admin/inheritor/{id}/revoke    取消传承人资格
```

### 地区管理

```
GET  /api/admin/region/tree        地区树形结构
POST /api/admin/region             新增地区
PUT  /api/admin/region             更新地区
DELETE /api/admin/region/{id}      删除地区
```

