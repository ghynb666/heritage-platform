USE `heritage_platform`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- This seed file is intended for the admin "商品列表" and "类型管理" pages.
-- Recommended import order:
-- 1. heritage_platform.sql
-- 2. init_data.sql
-- 3. p3_test_data.sql
-- 4. product_seed_data.sql

-- Minimal inheritor users for product ownership display.
INSERT INTO `sys_user` (
  `id`, `username`, `password`, `nickname`, `avatar`, `phone`,
  `heritage_category_id`, `status`, `deleted`
) VALUES
  (2, 'inheritor_paper', '123456', '李剪春', '/uploads/avatar/inheritor_paper.png', '13800000002', 1, 1, 0),
  (3, 'inheritor_embroidery', '123456', '苏绣娘', '/uploads/avatar/inheritor_embroidery.png', '13800000003', 2, 1, 0),
  (4, 'inheritor_ceramic', '123456', '陶青匠', '/uploads/avatar/inheritor_ceramic.png', '13800000004', 3, 1, 0)
ON DUPLICATE KEY UPDATE
  `username` = VALUES(`username`),
  `nickname` = VALUES(`nickname`),
  `avatar` = VALUES(`avatar`),
  `phone` = VALUES(`phone`),
  `heritage_category_id` = VALUES(`heritage_category_id`),
  `status` = VALUES(`status`),
  `deleted` = VALUES(`deleted`);

-- Bind inheritor role to the sample users.
INSERT INTO `sys_user_role` (`user_id`, `role_id`)
SELECT 2, r.`id`
FROM `sys_role` r
WHERE r.`role_key` = 'INHERITOR'
  AND NOT EXISTS (
    SELECT 1 FROM `sys_user_role` ur WHERE ur.`user_id` = 2 AND ur.`role_id` = r.`id`
  );

INSERT INTO `sys_user_role` (`user_id`, `role_id`)
SELECT 3, r.`id`
FROM `sys_role` r
WHERE r.`role_key` = 'INHERITOR'
  AND NOT EXISTS (
    SELECT 1 FROM `sys_user_role` ur WHERE ur.`user_id` = 3 AND ur.`role_id` = r.`id`
  );

INSERT INTO `sys_user_role` (`user_id`, `role_id`)
SELECT 4, r.`id`
FROM `sys_role` r
WHERE r.`role_key` = 'INHERITOR'
  AND NOT EXISTS (
    SELECT 1 FROM `sys_user_role` ur WHERE ur.`user_id` = 4 AND ur.`role_id` = r.`id`
  );

-- Product categories for "类型管理".
INSERT INTO `product_category` (
  `id`, `name`, `icon`, `description`, `sort`, `status`, `create_time`, `update_time`, `deleted`
) VALUES
  (101, '剪纸摆件', 'scissors', '围绕剪纸元素设计的家居摆件与节庆装饰。', 1, 1, NOW(), NOW(), 0),
  (102, '刺绣配饰', 'thread', '以苏绣、蜀绣等工艺为灵感的随身配饰。', 2, 1, NOW(), NOW(), 0),
  (103, '陶瓷茶器', 'tea', '结合青花、紫砂等非遗技艺的茶具器皿。', 3, 1, NOW(), NOW(), 0),
  (104, '木艺文具', 'wood', '木雕和传统纹样结合的文创文具。', 4, 1, NOW(), NOW(), 0),
  (105, '皮影玩偶', 'shadow', '皮影人物与戏曲造型衍生的收藏玩偶。', 5, 1, NOW(), NOW(), 0),
  (106, '节庆年礼', 'gift', '适合节日送礼的组合型文创商品。', 6, 1, NOW(), NOW(), 0)
ON DUPLICATE KEY UPDATE
  `name` = VALUES(`name`),
  `icon` = VALUES(`icon`),
  `description` = VALUES(`description`),
  `sort` = VALUES(`sort`),
  `status` = VALUES(`status`),
  `update_time` = VALUES(`update_time`),
  `deleted` = VALUES(`deleted`);

-- Product records for "商品列表".
INSERT INTO `product` (
  `id`, `name`, `product_category_id`, `heritage_item_id`, `inheritor_id`,
  `cover_image`, `images`, `description`,
  `price`, `original_price`, `stock`, `sales`, `view_count`, `like_count`, `favorite_count`,
  `status`, `is_recommend`, `create_time`, `update_time`, `deleted`
) VALUES
  (1001, '窗花流光亚克力夜灯', 101, 1, 2,
   '/uploads/product/jianzhi_lamp_cover.jpg',
   '/uploads/product/jianzhi_lamp_1.jpg,/uploads/product/jianzhi_lamp_2.jpg,/uploads/product/jianzhi_lamp_3.jpg',
   '提取传统剪纸纹样做分层透光设计，适合作为书桌与玄关氛围灯。',
   79.00, 99.00, 120, 56, 886, 62, 41,
   1, 1, NOW(), NOW(), 0),

  (1002, '瑞兔剪纸冰箱贴礼盒', 101, 1, 2,
   '/uploads/product/jianzhi_magnet_cover.jpg',
   '/uploads/product/jianzhi_magnet_1.jpg,/uploads/product/jianzhi_magnet_2.jpg',
   '一套六枚剪纸主题冰箱贴，适合游客伴手礼和节庆陈列。',
   39.90, 49.90, 260, 132, 1245, 88, 59,
   1, 1, NOW(), NOW(), 0),

  (1003, '双面苏绣真丝书签', 102, 2, 3,
   '/uploads/product/suxiu_bookmark_cover.jpg',
   '/uploads/product/suxiu_bookmark_1.jpg,/uploads/product/suxiu_bookmark_2.jpg',
   '采用细密双面绣工艺，书签正反两面均可独立观赏。',
   58.00, 88.00, 180, 73, 968, 77, 52,
   1, 1, NOW(), NOW(), 0),

  (1004, '缠枝纹刺绣胸针', 102, 10, 3,
   '/uploads/product/embroidery_brooch_cover.jpg',
   '/uploads/product/embroidery_brooch_1.jpg,/uploads/product/embroidery_brooch_2.jpg',
   '小尺寸刺绣配饰，适合搭配西装、围巾或帆布包。',
   46.00, 66.00, 95, 38, 603, 41, 27,
   1, 0, NOW(), NOW(), 0),

  (1005, '青花纹便携旅行茶具', 103, 3, 4,
   '/uploads/product/qinghua_tea_set_cover.jpg',
   '/uploads/product/qinghua_tea_set_1.jpg,/uploads/product/qinghua_tea_set_2.jpg,/uploads/product/qinghua_tea_set_3.jpg',
   '将青花纹样压缩进便携茶具组合，适合差旅与露营场景。',
   168.00, 218.00, 64, 29, 752, 58, 33,
   1, 1, NOW(), NOW(), 0),

  (1006, '紫砂香插小摆台', 103, 11, 4,
   '/uploads/product/zisha_incense_cover.jpg',
   '/uploads/product/zisha_incense_1.jpg,/uploads/product/zisha_incense_2.jpg',
   '小型紫砂香插，兼顾日常焚香和案头陈设。',
   92.00, 118.00, 88, 24, 514, 36, 20,
   1, 0, NOW(), NOW(), 0),

  (1007, '黄杨木雕签字笔', 104, 12, 4,
   '/uploads/product/wood_pen_cover.jpg',
   '/uploads/product/wood_pen_1.jpg,/uploads/product/wood_pen_2.jpg',
   '在笔杆处融入黄杨木雕纹饰，适合商务礼赠。',
   128.00, 158.00, 70, 17, 467, 28, 19,
   0, 0, NOW(), NOW(), 0),

  (1008, '皮影戏人物桌面摆偶', 105, 4, 2,
   '/uploads/product/shadow_doll_cover.jpg',
   '/uploads/product/shadow_doll_1.jpg,/uploads/product/shadow_doll_2.jpg,/uploads/product/shadow_doll_3.jpg',
   '以经典皮影人物造型为原型，适合作为展示柜收藏摆件。',
   136.00, 168.00, 52, 21, 639, 49, 31,
   1, 0, NOW(), NOW(), 0),

  (1009, '年画红包台历套装', 106, 8, 2,
   '/uploads/product/newyear_gift_cover.jpg',
   '/uploads/product/newyear_gift_1.jpg,/uploads/product/newyear_gift_2.jpg',
   '把年画元素延展为红包、台历和贺卡，适合节日礼赠。',
   66.00, 88.00, 210, 95, 1096, 84, 57,
   1, 1, NOW(), NOW(), 0),

  (1010, '云锦纹收纳托盘', 106, 7, 3,
   '/uploads/product/yunjin_tray_cover.jpg',
   '/uploads/product/yunjin_tray_1.jpg,/uploads/product/yunjin_tray_2.jpg',
   '将云锦织纹转化为金属压印纹理，用于首饰与茶席收纳。',
   86.00, 109.00, 140, 44, 701, 53, 35,
   1, 0, NOW(), NOW(), 0)
ON DUPLICATE KEY UPDATE
  `name` = VALUES(`name`),
  `product_category_id` = VALUES(`product_category_id`),
  `heritage_item_id` = VALUES(`heritage_item_id`),
  `inheritor_id` = VALUES(`inheritor_id`),
  `cover_image` = VALUES(`cover_image`),
  `images` = VALUES(`images`),
  `description` = VALUES(`description`),
  `price` = VALUES(`price`),
  `original_price` = VALUES(`original_price`),
  `stock` = VALUES(`stock`),
  `sales` = VALUES(`sales`),
  `view_count` = VALUES(`view_count`),
  `like_count` = VALUES(`like_count`),
  `favorite_count` = VALUES(`favorite_count`),
  `status` = VALUES(`status`),
  `is_recommend` = VALUES(`is_recommend`),
  `update_time` = VALUES(`update_time`),
  `deleted` = VALUES(`deleted`);

SET FOREIGN_KEY_CHECKS = 1;
