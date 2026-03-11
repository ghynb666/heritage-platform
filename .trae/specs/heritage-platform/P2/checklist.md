# P2 验收清单 - 传承人申请与审核

## 传承人申请实体

- [x] 申请实体 `InheritorApply` 创建（含资质证明、证书、非遗类型字段）
- [x] Mapper接口创建

## 传承人申请功能（前台）

- [x] 传承人申请页面正常，可上传资质证明、证书、选择非遗类型
- [x] 传承人申请提交接口 `POST /api/inheritor/apply` 正常
- [x] 申请状态查询接口 `GET /api/inheritor/apply/status` 正常
- [x] 已登录普通用户申请成为传承人功能正常

## 传承人审核功能（后台）

- [x] 申请列表接口 `GET /api/admin/inheritor/apply/list` 正常
- [x] 管理员可查看申请详情（资质证明、证书图片）
- [x] 审核通过接口 `POST /api/admin/inheritor/apply/{id}/approve` 正常
- [x] 审核拒绝接口 `POST /api/admin/inheritor/apply/{id}/reject` 正常
- [x] 审核通过后用户角色正确变更为传承人
- [x] 审核拒绝后用户收到拒绝通知

## 前端页面

- [x] 传承人申请页面功能正常
- [x] 管理后台传承人审核页面功能正常
- [x] 申请列表展示正常
- [x] 详情弹窗展示正常

## 系统首页

- [x] 前台首页布局正常，导航栏清晰
- [x] 公告滚动展示正常
- [x] 后台管理框架正常，左侧纵向菜单显示正确
