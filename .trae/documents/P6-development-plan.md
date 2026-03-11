# P6 开发计划 - 管理员后台-文创管理

## 开发顺序

```
Phase 1: 后端补充 (Task 6.1 + 6.2 并行)
    ↓
Phase 2: 前端页面 (Task 6.3 + 6.4)
    ↓
Phase 3: 集成测试
```

***

## 数据库表结构分析

### product 表（商品表）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | bigint | 主键 |
| name | varchar(200) | 商品名称 |
| product_category_id | bigint | 商品类型ID |
| heritage_item_id | bigint | 关联非遗项目ID |
| inheritor_id | bigint | 传承人ID |
| cover_image | varchar(255) | 封面图 |
| images | text | 图片列表(JSON) |
| description | text | 商品描述 |
| price | decimal(10,2) | 价格 |
| original_price | decimal(10,2) | 原价 |
| stock | int | 库存 |
| sales | int | 销量 |
| view_count | int | 浏览量 |
| like_count | int | 点赞数 |
| favorite_count | int | 收藏数 |
| status | tinyint | 状态(0-下架,1-上架) |
| is_recommend | tinyint | 是否推荐 |
| create_time | datetime | 创建时间 |
| update_time | datetime | 更新时间 |
| deleted | tinyint | 逻辑删除 |

### product_category 表（商品类型表）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | bigint | 主键 |
| name | varchar(50) | 类型名称 |
| icon | varchar(255) | 图标 |
| description | varchar(255) | 描述 |
| sort | int | 排序 |
| status | tinyint | 状态 |
| create_time | datetime | 创建时间 |
| update_time | datetime | 更新时间 |
| deleted | tinyint | 逻辑删除 |

***

## Phase 1: 后端补充

### Task 6.1: 商品管理后端

**需创建文件**

| 文件 | 说明 |
|------|------|
| Product.java | 实体类 |
| ProductImage.java | 商品图片实体 |
| ProductService.java | 业务逻辑 |
| AdminProductController.java | 管理员接口 |

**接口清单**

| 方法   | 路径                                        | 说明      | 权限    |
| ---- | ----------------------------------------- | ------- | ----- |
| GET  | /api/admin/product/list                  | 商品列表    | ADMIN |
| GET  | /api/admin/product/{id}                  | 商品详情    | ADMIN |
| PUT  | /api/admin/product/{id}/status          | 上下架     | ADMIN |
| PUT  | /api/admin/product/{id}/recommend       | 推荐/取消推荐 | ADMIN |
| DELETE | /api/admin/product/{id}                | 删除商品    | ADMIN |

**接口详情**

```
GET /api/admin/product/list
参数: page, size, status, categoryId, keyword
返回: Page<ProductVO>

GET /api/admin/product/{id}
返回: ProductVO
PUT /api/admin/product/{id}/status
参数: status (0=下架, 1=上架)
PUT /api/admin/product/{id}/recommend
参数: isRecommend (0=取消推荐, 1=推荐)
DELETE /api/admin/product/{id}
```

**ProductService 方法**

```java
public Page<ProductVO> getAdminProductList(Integer page, Integer size, Integer status, Long categoryId, String keyword);
public ProductVO getAdminProductDetail(Long id);
public void updateStatus(Long id, Integer status);
public void updateRecommend(Long id, Integer isRecommend);
public void deleteProduct(Long id);
```

**ProductVO 字段**

```java
private Long id;
private String name;
private Long productCategoryId;
private String productCategoryName;
private Long heritageItemId;
private String heritageItemTitle;
private Long inheritorId;
private String inheritorName;
private String inheritorAvatar;
private String coverImage;
private List<String> images;
private String description;
private BigDecimal price;
private BigDecimal originalPrice;
private Integer stock;
private Integer sales;
private Integer viewCount;
private Integer likeCount;
private Integer favoriteCount;
private Integer status;
private Integer isRecommend;
private LocalDateTime createTime;
```

---

### Task 6.2: 商品类型管理后端

**需创建文件**

| 文件 | 说明 |
|------|------|
| ProductCategory.java | 实体类 |
| ProductCategoryService.java | 业务逻辑 |
| AdminProductCategoryController.java | 管理员接口 |

**接口清单**

| 方法   | 路径                              | 说明   | 权限    |
| ---- | ------------------------------- | ---- | ----- |
| GET  | /api/admin/product/category/list | 类型列表 | ADMIN |
| GET  | /api/admin/product/category/{id} | 类型详情 | ADMIN |
| POST | /api/admin/product/category     | 新增类型 | ADMIN |
| PUT  | /api/admin/product/category     | 更新类型 | ADMIN |
| DELETE | /api/admin/product/category/{id} | 删除类型 | ADMIN |
| PUT  | /api/admin/product/category/sort | 排序    | ADMIN |

**ProductCategoryService 方法**

```java
public List<ProductCategory> getAllCategories();
public ProductCategory getById(Long id);
public void save(ProductCategory category);
public void update(ProductCategory category);
public void delete(Long id);
public void updateSort(List<ProductCategory> categories);
```

---

## Phase 2: 前端页面

### Task 6.3: 前端商品管理页面

**重要：在开发前端页面时必须调用 front-design 的 skill，以便优化界面**

**页面: /admin/product/list**

**功能清单**

* [ ] 商品列表表格（支持分页）
* [ ] 搜索框（商品名称关键词）
* [ ] 状态筛选（全部/已上架/已下架）
* [ ] 分类筛选
* [ ] 商品详情弹窗（展示图片、价格、库存、传承人信息）
* [ ] 上下架功能
* [ ] 删除功能
* [ ] 推荐功能

**表格字段**

| 字段 | 说明 |
|------|------|
| ID | 商品ID |
| 封面图 | cover_image |
| 商品名称 | name |
| 分类 | categoryName |
| 传承人 | inheritorName |
| 价格 | price |
| 库存 | stock |
| 销量 | sales |
| 状态 | status (0-下架, 1-上架) |
| 推荐 | isRecommend |
| 操作 | 详情、上/下架、推荐、删除 |

---

### Task 6.4: 前端商品类型管理页面

**重要：在开发前端页面时必须调用 front-design 的 skill，以便优化界面**

**页面: /admin/product/category**

**功能清单**

* [ ] 类型列表表格
* [ ] 新增类型弹窗
* [ ] 编辑类型弹窗
* [ ] 删除类型功能
* [ ] 排序功能

**表格字段**

| 字段 | 说明 |
|------|------|
| ID | 类型ID |
| 名称 | name |
| 图标 | icon |
| 描述 | description |
| 排序 | sort |
| 状态 | status |
| 操作 | 编辑、删除、上移、下移 |

---

## Phase 3: 集成测试

### 验收项目

* [ ] 商品列表分页查询正常（支持搜索、筛选）
* [ ] 商品详情查询正常
* [ ] 商品上下架正常
* [ ] 商品删除正常
* [ ] 商品推荐/取消推荐正常
* [ ] 类型列表查询正常
* [ ] 类型增删改正常
* [ ] 类型排序正常
* [ ] 前端商品管理页面功能正常
* [ ] 前端商品类型管理页面功能正常

---

## 执行计划

| 步骤 | 任务 | 预计产出 |
|------|------|----------|
| 1 | Task 6.1 后端 | 创建Product实体、Service、AdminProductController |
| 2 | Task 6.2 后端 | 创建ProductCategory实体、Service、Controller |
| 3 | Task 6.3 前端 | 新建ProductList.vue |
| 4 | Task 6.4 前端 | 新建ProductCategoryList.vue |
| 5 | 路由配置 | 添加商品管理和类型管理路由 |
| 6 | API封装 | 补充admin.js中的商品相关API |
| 7 | 集成测试 | 完整流程验证 |

---

## 状态说明

| 状态值 | 说明 |
|--------|------|
| 0 | 下架 |
| 1 | 上架 |

| 推荐值 | 说明 |
|--------|------|
| 0 | 未推荐 |
| 1 | 已推荐 |
