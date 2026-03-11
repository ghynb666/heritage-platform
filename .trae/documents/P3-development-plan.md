# P3 开发计划 - 非遗分类与项目展示

## 开发顺序

```
Phase 1: 后端基础 (Task 3.1 + 3.2 + 3.3 并行)
    ↓
Phase 2: 前端展示 (Task 3.4)
    ↓
Phase 3: 集成测试
```

***

## 数据库表 (已存在)

| 表名                 | 说明    | 状态 |
| ------------------ | ----- | -- |
| heritage\_category | 非遗分类表 | ✓  |
| heritage\_item     | 非遗项目表 | ✓  |
| heritage\_image    | 项目图片表 | ✓  |
| heritage\_video    | 项目视频表 | ✓  |

***

## Phase 1: 后端基础

### Task 3.1: 非遗分类管理后端

**实体设计 (对应 heritage\_category 表)**

```java
// HeritageCategory.java
- id, name, icon, description, sort, status
- createTime, updateTime, deleted
```

**接口清单**

| 方法     | 路径                                | 说明   | 权限    |
| ------ | --------------------------------- | ---- | ----- |
| GET    | /api/heritage/category/list       | 分类列表 | 公开    |
| POST   | /api/admin/heritage/category      | 新增分类 | ADMIN |
| PUT    | /api/admin/heritage/category      | 更新分类 | ADMIN |
| DELETE | /api/admin/heritage/category/{id} | 删除分类 | ADMIN |

**分类数据**

* 剪纸、刺绣、陶艺、皮影、木雕、漆器、织锦、年画

***

### Task 3.2: 非遗项目管理后端

**实体设计 (对应 heritage\_item 表)**

```java
// HeritageItem.java
- id, title, heritageCategoryId, coverImage
- historyOrigin, craftFeature, content
- publisherId, publisherType (0管理员 1传承人)
- viewCount, likeCount, favoriteCount, commentCount
- status (0下架 1上架), isRecommend
- createTime, updateTime, deleted

// HeritageImage.java (对应 heritage_image 表)
- id, heritageItemId, imageUrl, description, sort

// HeritageVideo.java (对应 heritage_video 表)
- id, heritageItemId, videoUrl, coverUrl, title, description, duration, sort
```

**接口清单**

| 方法     | 路径                                   | 说明       | 权限        |
| ------ | ------------------------------------ | -------- | --------- |
| GET    | /api/heritage/item/list              | 项目列表(分页) | 公开        |
| GET    | /api/heritage/item/{id}              | 项目详情     | 公开        |
| GET    | /api/heritage/item/search            | 关键词搜索    | 公开        |
| POST   | /api/inheritor/heritage/item         | 发布项目     | INHERITOR |
| PUT    | /api/inheritor/heritage/item         | 编辑项目     | INHERITOR |
| DELETE | /api/inheritor/heritage/item/{id}    | 删除项目     | INHERITOR |
| GET    | /api/inheritor/heritage/item/list    | 我的项目     | INHERITOR |
| POST   | /api/admin/heritage/item             | 管理员发布    | ADMIN     |
| PUT    | /api/admin/heritage/item/{id}/status | 上下架      | ADMIN     |
| GET    | /api/admin/heritage/item/list        | 管理列表     | ADMIN     |

***

### Task 3.3: 文件上传功能

**接口清单**

| 方法   | 路径                       | 说明       |
| ---- | ------------------------ | -------- |
| POST | /api/common/upload/image | 图片上传(多图) |
| POST | /api/common/upload/video | 视频上传     |

**配置**

* 图片限制: jpg/png/gif, 最大5MB

* 视频限制: mp4, 最大100MB

* 存储路径: uploads/heritage/

**静态资源映射**

* 图片访问: /uploads/heritage/images/\*\*

* 视频访问: /uploads/heritage/videos/\*\*

* 视频播放: 支持Range请求头

***

## Phase 2: 前端展示

### Task 3.4: 前端非遗展示页面

**页面: /heritage**

* 左侧: 分类导航 (剪纸、刺绣、陶艺、皮影、木雕、漆器、织锦、年画)

* 右侧: 项目列表 (卡片式)

* 顶部: 搜索框 + 分类筛选

* 排序: 点击量↑↓、收藏数↑↓、点赞数↑↓

* 按钮: "详情" (非"阅读更多")

**组件清单**

| 组件                   | 说明     |
| -------------------- | ------ |
| HeritageCategory.vue | 分类导航组件 |
| HeritageList.vue     | 项目列表组件 |
| HeritageCard.vue     | 项目卡片组件 |
| HeritageSearch.vue   | 搜索筛选组件 |

***

## Phase 3: 集成测试

### 验收项目

* [x] 分类列表查询正常

* [x] 分类增删改查功能正常（管理员）

* [x] 分类排序功能正常

* [x] 非遗项目实体类创建

* [x] 项目图片实体类创建

* [x] 项目视频实体类创建

* [x] 项目发布接口正常（传承人）

* [x] 项目发布接口正常（管理员）

* [x] 项目列表分页查询接口正常

* [x] 图片上传接口正常

* [x] 视频上传接口正常

* [x] 静态资源映射正常

* [x] 视频在线播放支持正常

* [x] 非遗展示页面布局正确

* [x] 分类导航使用具体非遗类型名称

* [x] 项目列表展示正常

* [x] "详情"按钮显示正确

***

## 执行计划

| 步骤 | 任务            | 预计产出                                         |
| -- | ------------- | -------------------------------------------- |
| 1  | Task 3.1 后端   | HeritageCategory实体、Mapper、Service、Controller |
| 2  | Task 3.2 后端   | HeritageItem相关实体、Mapper、Service、Controller   |
| 3  | Task 3.3 文件上传 | 多图上传、视频上传接口，静态资源映射                           |
| 4  | Task 3.4 前端展示 | Heritage.vue页面及组件                            |
| 5  | 集成测试          | 完整流程验证                                       |
| 6  | 在进行前端开发时的要求   | 必须使用front-skill技能用于优化界面                      |

