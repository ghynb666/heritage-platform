# P1 开发计划 - 非遗展示模块

## 开发顺序

```
Phase 1: 后端基础 (Task 1.1 + 1.2 + 1.3 并行)
    ↓
Phase 2: 互动后端 (Task 1.8 + 1.9 并行)
    ↓
Phase 3: 前端展示 (Task 1.4 + 1.5 + 1.6)
    ↓
Phase 4: 前端管理 (Task 1.7 + 1.10)
```

---

## 数据库表 (已存在)

| 表名 | 说明 | 状态 |
|------|------|------|
| heritage_category | 非遗分类表 | ✓ |
| heritage_item | 非遗项目表 | ✓ |
| heritage_image | 项目图片表 | ✓ |
| heritage_video | 项目视频表 | ✓ |
| user_like | 点赞表 | ✓ |
| user_favorite | 收藏表 | ✓ |
| comment | 评论表 | ✓ |
| sensitive_word | 敏感词表 | ✓ |

---

## Phase 1: 后端基础

### Task 1.1: 非遗分类管理

**实体设计 (对应 heritage_category 表)**
```java
// HeritageCategory.java
- id, name, icon, description, sort, status
- createTime, updateTime, deleted
```

**接口清单**
| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | /api/heritage/category/list | 分类列表 | 公开 |
| POST | /api/admin/heritage/category | 新增分类 | ADMIN |
| PUT | /api/admin/heritage/category | 更新分类 | ADMIN |
| DELETE | /api/admin/heritage/category/{id} | 删除分类 | ADMIN |

---

### Task 1.2: 非遗项目管理

**实体设计 (对应 heritage_item 表)**
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
| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | /api/heritage/item/list | 项目列表(分页) | 公开 |
| GET | /api/heritage/item/{id} | 项目详情 | 公开 |
| GET | /api/heritage/item/search | 关键词搜索 | 公开 |
| POST | /api/inheritor/heritage/item | 发布项目 | INHERITOR |
| PUT | /api/inheritor/heritage/item | 编辑项目 | INHERITOR |
| DELETE | /api/inheritor/heritage/item/{id} | 删除项目 | INHERITOR |
| GET | /api/inheritor/heritage/item/list | 我的项目 | INHERITOR |
| POST | /api/admin/heritage/item | 管理员发布 | ADMIN |
| PUT | /api/admin/heritage/item/{id}/status | 上下架 | ADMIN |
| GET | /api/admin/heritage/item/list | 管理列表 | ADMIN |

---

### Task 1.3: 文件上传

**接口清单**
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/common/upload/image | 图片上传(多图) |
| POST | /api/common/upload/video | 视频上传 |

**配置**
- 图片限制: jpg/png/gif, 最大5MB
- 视频限制: mp4, 最大100MB
- 存储路径: uploads/heritage/

---

## Phase 2: 互动后端

### Task 1.8: 点赞收藏

**实体设计 (对应 user_like, user_favorite 表)**
```java
// UserLike.java
- id, userId, targetType (1非遗 2商品 3资讯 4帖子 5评论), targetId

// UserFavorite.java
- id, userId, targetType (1非遗 2商品 3资讯 4帖子), targetId
```

**接口清单**
| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| POST | /api/heritage/item/{id}/like | 点赞 | USER |
| DELETE | /api/heritage/item/{id}/like | 取消点赞 | USER |
| POST | /api/heritage/item/{id}/favorite | 收藏 | USER |
| DELETE | /api/heritage/item/{id}/favorite | 取消收藏 | USER |
| GET | /api/user/favorites | 我的收藏 | USER |

---

### Task 1.9: 评论功能

**实体设计 (对应 comment, sensitive_word 表)**
```java
// Comment.java
- id, content, userId, userName, userAvatar
- targetType (1非遗 2商品 3资讯 4帖子), targetId
- parentId, replyUserId, replyUserName
- likeCount, status (0隐藏 1显示)

// SensitiveWord.java
- id, word, type (0普通 1严重), replacement, status
```

**接口清单**
| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | /api/heritage/item/{id}/comments | 评论列表 | 公开 |
| POST | /api/heritage/item/{id}/comment | 发表评论 | USER |
| DELETE | /api/comment/{id} | 删除评论 | USER |

---

## Phase 3: 前端展示

### Task 1.4: 非遗展示页面

**页面: /heritage**
- 左侧: 分类导航 (剪纸、刺绣、陶艺、皮影、木雕、漆器、织锦、年画)
- 右侧: 项目列表 (卡片式)
- 顶部: 搜索框 + 分类筛选
- 排序: 点击量↑↓、收藏数↑↓、点赞数↑↓
- 按钮: "详情" (非"阅读更多")

---

### Task 1.5: 非遗详情页面

**页面: /heritage/:id**
- 标题区: 名称 + 发布者信息
- 内容区:
  - 历史渊源
  - 技艺特点
  - 高清大图 (图片预览)
  - 制作视频 (在线播放)
- 互动区: 点赞、收藏、评论
- 发布者:
  - 管理员发布: 不显示传承人信息
  - 传承人发布: 显示头像+用户名，可点击跳转主页

---

### Task 1.6: 传承人主页

**页面: /inheritor/:id**
- 传承人基本信息
- 该传承人发布的所有非遗项目

---

## Phase 4: 前端管理

### Task 1.7: 传承人非遗管理

**页面: /inheritor/heritage**
- 发布页面: 表单 + 图片上传(多图) + 视频上传
- 分类选择: 只能选择自己所属类型
- 管理列表: 我的项目，支持编辑、删除

---

### Task 1.10: 前端互动功能

**组件**
- LikeButton.vue: 点赞按钮，状态切换
- FavoriteButton.vue: 收藏按钮，状态切换
- CommentList.vue: 评论列表，分页
- CommentForm.vue: 评论发布，敏感词提示

---

## 执行计划

| 步骤 | 任务 | 预计产出 |
|------|------|----------|
| 1 | Task 1.1 后端 | HeritageCategoryController |
| 2 | Task 1.2 后端 | HeritageItem相关实体、Mapper、Service、Controller |
| 3 | Task 1.3 文件上传 | 多图上传、视频上传接口 |
| 4 | Task 1.8 点赞收藏 | UserLike、UserFavorite相关代码 |
| 5 | Task 1.9 评论 | Comment、SensitiveWord相关代码 |
| 6 | Task 1.4 前端展示 | Heritage.vue页面 |
| 7 | Task 1.5 前端详情 | HeritageDetail.vue页面 |
| 8 | Task 1.6 传承人主页 | InheritorHome.vue页面 |
| 9 | Task 1.7 传承人管理 | 发布、管理页面 |
| 10 | Task 1.10 互动组件 | 点赞、收藏、评论组件 |
| 11 | 集成测试 | 完整流程验证 |
