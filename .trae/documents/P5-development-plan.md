# P5 开发计划 - 管理员后台-非遗项目管理

## 开发顺序

```
Phase 1: 后端补充 (Task 5.1 + 5.2 并行)
    ↓
Phase 2: 前端页面 (Task 5.3 + 5.4)
    ↓
Phase 3: 集成测试
```

***

## 现有代码分析

### 已有文件

| 文件                              | 状态      | 说明              |
| ------------------------------- | ------- | --------------- |
| AdminHeritageController.java    | ⚠️ 部分完成 | 缺少审核、推荐、删除、详情接口 |
| HeritageItemService.java        | ⚠️ 部分完成 | 缺少审核、推荐方法       |
| HeritageCategoryController.java | ⚠️ 部分完成 | 缺少排序接口          |
| HeritageCategoryService.java    | ✅ 已存在   | 基础CRUD已有        |
| HeritageItemVO.java             | ✅ 已存在   | 完整VO            |

### 需补充/新增文件

| 文件                              | 说明              |
| ------------------------------- | --------------- |
| AdminHeritageController.java    | 补充审核、推荐、删除、详情接口 |
| HeritageItemService.java        | 补充审核、推荐方法       |
| HeritageCategoryController.java | 补充排序接口          |
| HeritageCategoryService.java    | 补充排序方法          |
| HeritageList.vue                | 新建项目管理页面        |
| CategoryList.vue                | 新建分类管理页面        |

***

## Phase 1: 后端补充

### Task 5.1: 非遗项目管理后端

**已有接口**

| 方法   | 路径                                   | 说明      | 状态 |
| ---- | ------------------------------------ | ------- | -- |
| POST | /api/admin/heritage/item             | 管理员发布项目 | ✅  |
| GET  | /api/admin/heritage/item/list        | 管理列表    | ✅  |
| PUT  | /api/admin/heritage/item/{id}/status | 上下架     | ✅  |

**需补充接口**

| 方法     | 路径                                      | 说明      | 权限    |
| ------ | --------------------------------------- | ------- | ----- |
| GET    | /api/admin/heritage/item/{id}           | 项目详情    | ADMIN |
| PUT    | /api/admin/heritage/item/{id}/audit     | 审核通过/拒绝 | ADMIN |
| PUT    | /api/admin/heritage/item/{id}/recommend | 推荐/取消推荐 | ADMIN |
| DELETE | /api/admin/heritage/item/{id}           | 删除项目    | ADMIN |

**接口详情**

```
GET /api/admin/heritage/item/{id}
返回: HeritageItemVO（含图片、视频列表）

PUT /api/admin/heritage/item/{id}/audit
参数: status (2=审核通过, 3=审核拒绝)
返回: Result<?>

PUT /api/admin/heritage/item/{id}/recommend
参数: isRecommend (0=取消推荐, 1=推荐)
返回: Result<?>

DELETE /api/admin/heritage/item/{id}
返回: Result<?>
```

**HeritageItemService 补充方法**

```java
public HeritageItemVO getAdminItemDetail(Long id);
public void auditItem(Long id, Integer status);
public void updateRecommend(Long id, Integer isRecommend);
public void deleteByAdmin(Long id);
```

***

### Task 5.2: 非遗分类管理后端

**已有接口**

| 方法     | 路径                          | 说明   | 状态 |
| ------ | --------------------------- | ---- | -- |
| GET    | /api/heritage/category/list | 分类列表 | ✅  |
| GET    | /api/heritage/category/page | 分类分页 | ✅  |
| POST   | /api/heritage/category      | 新增分类 | ✅  |
| PUT    | /api/heritage/category      | 更新分类 | ✅  |
| DELETE | /api/heritage/category/{id} | 删除分类 | ✅  |

**需补充接口**

| 方法  | 路径                          | 说明   | 权限    |
| --- | --------------------------- | ---- | ----- |
| PUT | /api/heritage/category/sort | 分类排序 | ADMIN |

**接口详情**

```
PUT /api/heritage/category/sort
参数: List<{id: Long, sort: Integer}>
返回: Result<?>
```

**HeritageCategoryService 补充方法**

```java
public void updateSort(List<HeritageCategory> categories);
```

***

## Phase 2: 前端页面

### Task 5.3: 前端项目管理页面

**页面: /admin/heritage/list**

**功能清单**

* [x] 项目列表表格（支持分页）

* [x] 搜索框（标题关键词）

* [x] 状态筛选（全部/已发布/已下架/待审核）

* [x] 分类筛选

* [x] 项目详情弹窗（展示历史渊源、技艺特点、图片、视频）

* [x] 审核功能（通过/拒绝）

* [x] 上下架功能

* [x] 删除功能

* [x] 推荐功能

**表格字段**

| 字段   | 说明                                    |
| ---- | ------------------------------------- |
| ID   | 项目ID                                  |
| 封面图  | coverImage                            |
| 标题   | title                                 |
| 分类   | heritageCategoryName                  |
| 发布者  | publisherName                         |
| 状态   | status (1=已发布, 0=已下架, 2=审核通过, 3=审核拒绝) |
| 推荐   | isRecommend                           |
| 浏览量  | viewCount                             |
| 创建时间 | createTime                            |
| 操作   | 详情、审核、上下架、推荐、删除                       |

**详情弹窗字段**

* 基本信息：封面图、标题、分类、发布者、状态

* 内容：历史渊源、技艺特点、详细介绍

* 媒体：图片列表（支持预览）、视频列表（支持播放）

* 统计：浏览量、点赞数、收藏数、评论数

***

### Task 5.4: 前端分类管理页面

**页面: /admin/heritage/category**

**功能清单**

* [x] 分类列表表格

* [x] 新增分类弹窗

* [x] 编辑分类弹窗

* [x] 删除分类功能

* [x] 排序功能（上移/下移或拖拽）

**表格字段**

| 字段 | 说明          |
| -- | ----------- |
| ID | 分类ID        |
| 图标 | icon        |
| 名称 | name        |
| 描述 | description |
| 排序 | sort        |
| 状态 | status      |
| 操作 | 编辑、删除、上移、下移 |

***

## Phase 3: 集成测试

### 验收项目

* [x] 项目列表分页查询正常（支持搜索、筛选）

* [x] 项目详情查询正常

* [x] 项目审核通过/拒绝正常

* [x] 项目上下架正常

* [x] 项目删除正常

* [x] 项目推荐/取消推荐正常

* [x] 分类列表查询正常

* [x] 分类增删改正常

* [x] 分类排序正常

* [x] 前端项目管理页面功能正常

* [x] 前端分类管理页面功能正常

***

## 执行计划

| 步骤 | 任务          | 预计产出                                              |
| -- | ----------- | ------------------------------------------------- |
| 1  | Task 5.1 后端 | 补充AdminHeritageController接口、HeritageItemService方法 |
| 2  | Task 5.2 后端 | 补充分类排序接口                                          |
| 3  | Task 5.3 前端 | 新建HeritageList.vue                                |
| 4  | Task 5.4 前端 | 新建CategoryList.vue                                |
| 5  | 路由配置        | 添加项目管理和分类管理路由                                     |
| 6  | API封装       | 补充admin.js中的项目相关API                               |
| 7  | 集成测试        | 完整流程验证                                            |

***

## 接口汇总

### 项目管理

```
GET  /api/admin/heritage/item/list      项目列表（已有）
GET  /api/admin/heritage/item/{id}      项目详情（新增）
POST /api/admin/heritage/item           发布项目（已有）
PUT  /api/admin/heritage/item/{id}/status    上下架（已有）
PUT  /api/admin/heritage/item/{id}/audit     审核（新增）
PUT  /api/admin/heritage/item/{id}/recommend 推荐（新增）
DELETE /api/admin/heritage/item/{id}    删除项目（新增）
```

### 分类管理

```
GET  /api/heritage/category/list        分类列表（已有）
GET  /api/heritage/category/page        分类分页（已有）
POST /api/heritage/category             新增分类（已有）
PUT  /api/heritage/category             更新分类（已有）
PUT  /api/heritage/category/sort        分类排序（新增）
DELETE /api/heritage/category/{id}      删除分类（已有）
```

***

## 状态说明

| 状态值 | 说明   |
| --- | ---- |
| 0   | 已下架  |
| 1   | 已发布  |
| 2   | 审核通过 |
| 3   | 审核拒绝 |

## 推荐说明

| 推荐值 | 说明  |
| --- | --- |
| 0   | 未推荐 |
| 1   | 已推荐 |

