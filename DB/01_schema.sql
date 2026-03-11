CREATE DATABASE IF NOT EXISTS `heritage_platform` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `heritage_platform`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '鐟欐帟澹奍D',
  `role_name` varchar(50) NOT NULL COMMENT '鐟欐帟澹婇崥宥囆?,
  `role_key` varchar(50) NOT NULL COMMENT '鐟欐帟澹婇弶鍐鐎涙顑佹稉?,
  `description` varchar(255) DEFAULT NULL COMMENT '鐟欐帟澹婇幓蹇氬牚',
  `status` tinyint DEFAULT 1 COMMENT '閻樿埖鈧緤绱?-缁備胶鏁ら敍?-閸氼垳鏁?,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '閸掓稑缂撻弮鍫曟？',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '閺囧瓨鏌婇弮鍫曟？',
  `deleted` tinyint DEFAULT 0 COMMENT '閸掔娀娅庨弽鍥х箶閿?-閺堫亜鍨归梽銈忕礉1-瀹告彃鍨归梽?,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_key` (`role_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='鐟欐帟澹婄悰?;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '閻劍鍩汭D',
  `username` varchar(50) NOT NULL COMMENT '閻劍鍩涢崥?,
  `password` varchar(255) NOT NULL COMMENT '鐎靛棛鐖?,
  `nickname` varchar(50) DEFAULT NULL COMMENT '閺勭數袨',
  `avatar` varchar(255) DEFAULT NULL COMMENT '婢舵潙鍎歎RL',
  `email` varchar(100) DEFAULT NULL COMMENT '闁喚顔?,
  `phone` varchar(20) DEFAULT NULL COMMENT '閹靛婧€閸?,
  `gender` tinyint DEFAULT NULL COMMENT '閹冨焼閿?-閺堫亞鐓￠敍?-閻㈠嚖绱?-婵?,
  `birthday` date DEFAULT NULL COMMENT '閻㈢喐妫?,
  `province` varchar(50) DEFAULT NULL COMMENT '閻椒鍞?,
  `city` varchar(50) DEFAULT NULL COMMENT '閸╁骸绔?,
  `area` varchar(50) DEFAULT NULL COMMENT '閸栧搫骞?,
  `address` varchar(255) DEFAULT NULL COMMENT '鐠囷妇绮忛崷鏉挎絻',
  `signature` varchar(255) DEFAULT NULL COMMENT '娑擃亝鈧咁劮閸?,
  `heritage_category_id` bigint DEFAULT NULL COMMENT '闂堢偤浠愮猾璇茬€稩D閿涘牅绱堕幍澶告眽娑撴挸鐫橀敍?,
  `status` tinyint DEFAULT 1 COMMENT '閻樿埖鈧緤绱?-缁備胶鏁ら敍?-閸氼垳鏁?,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '閸掓稑缂撻弮鍫曟？',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '閺囧瓨鏌婇弮鍫曟？',
  `last_login_time` datetime DEFAULT NULL COMMENT '閺堚偓閸氬海娅ヨぐ鏇熸闂?,
  `deleted` tinyint DEFAULT 0 COMMENT '閸掔娀娅庨弽鍥х箶閿?-閺堫亜鍨归梽銈忕礉1-瀹告彃鍨归梽?,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_heritage_category` (`heritage_category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='閻劍鍩涢崺铏诡攨娣団剝浼呯悰?;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '娑撳鏁璉D',
  `user_id` bigint NOT NULL COMMENT '閻劍鍩汭D',
  `role_id` bigint NOT NULL COMMENT '鐟欐帟澹奍D',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '閸掓稑缂撻弮鍫曟？',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='閻劍鍩涚憴鎺曞閸忓疇浠堢悰?;

-- ----------------------------
-- Table structure for inheritor_apply
-- ----------------------------
DROP TABLE IF EXISTS `inheritor_apply`;
CREATE TABLE `inheritor_apply` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '閻㈠疇顕琁D',
  `user_id` bigint NOT NULL COMMENT '閻㈠疇顕禍绡扗',
  `heritage_category_id` bigint NOT NULL COMMENT '閻㈠疇顕惃鍕姜闁琚崹濠璂',
  `real_name` varchar(50) NOT NULL COMMENT '閻喎鐤勬慨鎾虫倳',
  `id_card` varchar(18) DEFAULT NULL COMMENT '闊偂鍞ょ拠浣稿娇',
  `certificate_images` text COMMENT '鐠у嫯宸濈拠浣规閸ュ墽澧栭敍鍦漇ON閺佹壆绮嶉敍?,
  `description` text COMMENT '閻㈠疇顕拠瀛樻',
  `status` tinyint DEFAULT 0 COMMENT '鐎光剝鐗抽悩鑸碘偓渚婄窗0-瀵板懎顓搁弽闈╃礉1-闁俺绻冮敍?-閹锋帞绮?,
  `reject_reason` varchar(255) DEFAULT NULL COMMENT '閹锋帞绮烽崢鐔锋礈',
  `reviewer_id` bigint DEFAULT NULL COMMENT '鐎光剝鐗虫禍绡扗',
  `review_time` datetime DEFAULT NULL COMMENT '鐎光剝鐗抽弮鍫曟？',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '閻㈠疇顕弮鍫曟？',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '閺囧瓨鏌婇弮鍫曟？',
  `deleted` tinyint DEFAULT 0 COMMENT '閸掔娀娅庨弽鍥х箶閿?-閺堫亜鍨归梽銈忕礉1-瀹告彃鍨归梽?,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_heritage_category` (`heritage_category_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='娴肩姵澹欐禍铏规暤鐠囩柉銆?;

-- ----------------------------
-- Table structure for heritage_category
-- ----------------------------
DROP TABLE IF EXISTS `heritage_category`;
CREATE TABLE `heritage_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '閸掑棛琚獻D',
  `name` varchar(50) NOT NULL COMMENT '閸掑棛琚崥宥囆?,
  `icon` varchar(255) DEFAULT NULL COMMENT '閸掑棛琚崶鐐垼',
  `description` varchar(255) DEFAULT NULL COMMENT '閸掑棛琚幓蹇氬牚',
  `sort` int DEFAULT 0 COMMENT '閹烘帒绨?,
  `status` tinyint DEFAULT 1 COMMENT '閻樿埖鈧緤绱?-缁備胶鏁ら敍?-閸氼垳鏁?,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '閸掓稑缂撻弮鍫曟？',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '閺囧瓨鏌婇弮鍫曟？',
  `deleted` tinyint DEFAULT 0 COMMENT '閸掔娀娅庨弽鍥х箶閿?-閺堫亜鍨归梽銈忕礉1-瀹告彃鍨归梽?,
  PRIMARY KEY (`id`),
  KEY `idx_sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='闂堢偤浠愰崚鍡欒鐞?;

-- ----------------------------
-- Table structure for heritage_item
-- ----------------------------
DROP TABLE IF EXISTS `heritage_item`;
CREATE TABLE `heritage_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '妞ゅ湱娲癐D',
  `title` varchar(200) NOT NULL COMMENT '妞ゅ湱娲伴弽鍥暯',
  `heritage_category_id` bigint NOT NULL COMMENT '闂堢偤浠愰崚鍡欒ID',
  `cover_image` varchar(255) DEFAULT NULL COMMENT '鐏忎線娼伴崶鍓у',
  `history_origin` text COMMENT '閸樺棗褰跺〒濠冪爱',
  `craft_feature` text COMMENT '閹垛偓閼硅櫣澹掗悙?,
  `content` longtext COMMENT '鐠囷妇绮忛崘鍛啇',
  `publisher_id` bigint DEFAULT NULL COMMENT '閸欐垵绔烽懓鍖閿涘牏顓搁悶鍡楁喅閸欐垵绔锋稉绡楿LL閿?,
  `publisher_type` tinyint DEFAULT 0 COMMENT '閸欐垵绔烽懓鍛閸ㄥ绱?-缁狅紕鎮婇崨姗堢礉1-娴肩姵澹欐禍?,
  `view_count` int DEFAULT 0 COMMENT '濞村繗顫嶉柌?,
  `like_count` int DEFAULT 0 COMMENT '閻愮绂愰弫?,
  `favorite_count` int DEFAULT 0 COMMENT '閺€鎯版閺?,
  `comment_count` int DEFAULT 0 COMMENT '鐠囧嫯顔戦弫?,
  `status` tinyint DEFAULT 1 COMMENT '閻樿埖鈧緤绱?-娑撳鐏﹂敍?-娑撳﹥鐏?,
  `is_recommend` tinyint DEFAULT 0 COMMENT '閺勵垰鎯侀幒銊ㄥ礃閿?-閸氾讣绱?-閺?,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '閸掓稑缂撻弮鍫曟？',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '閺囧瓨鏌婇弮鍫曟？',
  `deleted` tinyint DEFAULT 0 COMMENT '閸掔娀娅庨弽鍥х箶閿?-閺堫亜鍨归梽銈忕礉1-瀹告彃鍨归梽?,
  PRIMARY KEY (`id`),
  KEY `idx_heritage_category` (`heritage_category_id`),
  KEY `idx_publisher` (`publisher_id`),
  KEY `idx_view_count` (`view_count`),
  KEY `idx_like_count` (`like_count`),
  KEY `idx_favorite_count` (`favorite_count`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='闂堢偤浠愭い鍦窗鐞?;

-- ----------------------------
-- Table structure for heritage_image
-- ----------------------------
DROP TABLE IF EXISTS `heritage_image`;
CREATE TABLE `heritage_image` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '閸ュ墽澧朓D',
  `heritage_item_id` bigint NOT NULL COMMENT '闂堢偤浠愭い鍦窗ID',
  `image_url` varchar(255) NOT NULL COMMENT '閸ュ墽澧朥RL',
  `description` varchar(255) DEFAULT NULL COMMENT '閸ュ墽澧栭幓蹇氬牚',
  `sort` int DEFAULT 0 COMMENT '閹烘帒绨?,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '閸掓稑缂撻弮鍫曟？',
  `deleted` tinyint DEFAULT 0 COMMENT '閸掔娀娅庨弽鍥х箶閿?-閺堫亜鍨归梽銈忕礉1-瀹告彃鍨归梽?,
  PRIMARY KEY (`id`),
  KEY `idx_heritage_item` (`heritage_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='闂堢偤浠愭い鍦窗閸ュ墽澧栫悰?;

-- ----------------------------
-- Table structure for heritage_video
-- ----------------------------
DROP TABLE IF EXISTS `heritage_video`;
CREATE TABLE `heritage_video` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '鐟欏棝顣禝D',
  `heritage_item_id` bigint NOT NULL COMMENT '闂堢偤浠愭い鍦窗ID',
  `video_url` varchar(255) NOT NULL COMMENT '鐟欏棝顣禪RL',
  `cover_url` varchar(255) DEFAULT NULL COMMENT '鐟欏棝顣剁亸渚€娼癠RL',
  `title` varchar(200) DEFAULT NULL COMMENT '鐟欏棝顣堕弽鍥暯',
  `description` varchar(255) DEFAULT NULL COMMENT '鐟欏棝顣堕幓蹇氬牚',
  `duration` int DEFAULT NULL COMMENT '鐟欏棝顣堕弮鍫曟毐閿涘牏顫楅敍?,
  `sort` int DEFAULT 0 COMMENT '閹烘帒绨?,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '閸掓稑缂撻弮鍫曟？',
  `deleted` tinyint DEFAULT 0 COMMENT '閸掔娀娅庨弽鍥х箶閿?-閺堫亜鍨归梽銈忕礉1-瀹告彃鍨归梽?,
  PRIMARY KEY (`id`),
  KEY `idx_heritage_item` (`heritage_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='闂堢偤浠愭い鍦窗鐟欏棝顣剁悰?;

-- ----------------------------
-- Table structure for product_category
-- ----------------------------
DROP TABLE IF EXISTS `product_category`;
CREATE TABLE `product_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '閸掑棛琚獻D',
  `name` varchar(50) NOT NULL COMMENT '閸掑棛琚崥宥囆?,
  `icon` varchar(255) DEFAULT NULL COMMENT '閸掑棛琚崶鐐垼',
  `description` varchar(255) DEFAULT NULL COMMENT '閸掑棛琚幓蹇氬牚',
  `sort` int DEFAULT 0 COMMENT '閹烘帒绨?,
  `status` tinyint DEFAULT 1 COMMENT '閻樿埖鈧緤绱?-缁備胶鏁ら敍?-閸氼垳鏁?,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '閸掓稑缂撻弮鍫曟？',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '閺囧瓨鏌婇弮鍫曟？',
  `deleted` tinyint DEFAULT 0 COMMENT '閸掔娀娅庨弽鍥х箶閿?-閺堫亜鍨归梽銈忕礉1-瀹告彃鍨归梽?,
  PRIMARY KEY (`id`),
  KEY `idx_sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='閸熷棗鎼х猾璇茬€风悰?;

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '閸熷棗鎼D',
  `name` varchar(200) NOT NULL COMMENT '閸熷棗鎼ч崥宥囆?,
  `product_category_id` bigint NOT NULL COMMENT '閸熷棗鎼х猾璇茬€稩D',
  `heritage_item_id` bigint DEFAULT NULL COMMENT '閸忓疇浠堥棃鐐轰粣妞ゅ湱娲癐D',
  `inheritor_id` bigint NOT NULL COMMENT '娴肩姵澹欐禍绡扗',
  `cover_image` varchar(255) DEFAULT NULL COMMENT '鐏忎線娼伴崶鍓у',
  `images` text COMMENT '閸熷棗鎼ч崶鍓у閿涘湞SON閺佹壆绮嶉敍?,
  `description` text COMMENT '閸熷棗鎼ч幓蹇氬牚',
  `price` decimal(10, 2) NOT NULL COMMENT '閸熷棗鎼ф禒閿嬬壐',
  `original_price` decimal(10, 2) DEFAULT NULL COMMENT '閸樼喍鐜?,
  `stock` int DEFAULT 0 COMMENT '鎼存挸鐡?,
  `sales` int DEFAULT 0 COMMENT '闁库偓闁?,
  `view_count` int DEFAULT 0 COMMENT '濞村繗顫嶉柌?,
  `like_count` int DEFAULT 0 COMMENT '閻愮绂愰弫?,
  `favorite_count` int DEFAULT 0 COMMENT '閺€鎯版閺?,
  `status` tinyint DEFAULT 1 COMMENT '閻樿埖鈧緤绱?-娑撳鐏﹂敍?-娑撳﹥鐏?,
  `is_recommend` tinyint DEFAULT 0 COMMENT '閺勵垰鎯侀幒銊ㄥ礃閿?-閸氾讣绱?-閺?,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '閸掓稑缂撻弮鍫曟？',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '閺囧瓨鏌婇弮鍫曟？',
  `deleted` tinyint DEFAULT 0 COMMENT '閸掔娀娅庨弽鍥х箶閿?-閺堫亜鍨归梽銈忕礉1-瀹告彃鍨归梽?,
  PRIMARY KEY (`id`),
  KEY `idx_product_category` (`product_category_id`),
  KEY `idx_heritage_item` (`heritage_item_id`),
  KEY `idx_inheritor` (`inheritor_id`),
  KEY `idx_price` (`price`),
  KEY `idx_sales` (`sales`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='閺傚洤鍨遍崯鍡楁惂鐞?;

-- ----------------------------
-- Table structure for cart
-- ----------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '鐠愵厾澧挎潪顨疍',
  `user_id` bigint NOT NULL COMMENT '閻劍鍩汭D',
  `product_id` bigint NOT NULL COMMENT '閸熷棗鎼D',
  `quantity` int DEFAULT 1 COMMENT '閸熷棗鎼ч弫浼村櫤',
  `selected` tinyint DEFAULT 1 COMMENT '閺勵垰鎯侀柅澶夎厬閿?-閸氾讣绱?-閺?,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '閸掓稑缂撻弮鍫曟？',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '閺囧瓨鏌婇弮鍫曟？',
  `deleted` tinyint DEFAULT 0 COMMENT '閸掔娀娅庨弽鍥х箶閿?-閺堫亜鍨归梽銈忕礉1-瀹告彃鍨归梽?,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_product` (`user_id`, `product_id`),
  KEY `idx_product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='鐠愵厾澧挎潪锕併€?;

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '鐠併垹宕烮D',
  `order_no` varchar(50) NOT NULL COMMENT '鐠併垹宕熺紓鏍у娇',
  `user_id` bigint NOT NULL COMMENT '閻劍鍩汭D',
  `inheritor_id` bigint NOT NULL COMMENT '娴肩姵澹欐禍绡扗',
  `total_amount` decimal(10, 2) NOT NULL COMMENT '鐠併垹宕熼幀濠氬櫨妫?,
  `pay_amount` decimal(10, 2) DEFAULT NULL COMMENT '鐎圭偘绮柌鎴︻杺',
  `freight_amount` decimal(10, 2) DEFAULT 0.00 COMMENT '鏉╂劘鍨?,
  `discount_amount` decimal(10, 2) DEFAULT 0.00 COMMENT '娴兼ɑ鍎柌鎴︻杺',
  `receiver_name` varchar(50) NOT NULL COMMENT '閺€鎯版彛娴滃搫顫橀崥?,
  `receiver_phone` varchar(20) NOT NULL COMMENT '閺€鎯版彛娴滆櫣鏁哥拠?,
  `receiver_province` varchar(50) DEFAULT NULL COMMENT '閻椒鍞?,
  `receiver_city` varchar(50) DEFAULT NULL COMMENT '閸╁骸绔?,
  `receiver_area` varchar(50) DEFAULT NULL COMMENT '閸栧搫骞?,
  `receiver_address` varchar(255) NOT NULL COMMENT '鐠囷妇绮忛崷鏉挎絻',
  `status` tinyint DEFAULT 0 COMMENT '鐠併垹宕熼悩鑸碘偓渚婄窗0-瀵板懍绮▎鎾呯礉1-瀵板懎褰傜拹褝绱?-瀵板懏鏁圭拹褝绱?-瀹告彃鐣幋鎰剁礉4-瀹告彃褰囧☉鍫礉5-瀹告煡鈧偓濞?,
  `pay_type` tinyint DEFAULT NULL COMMENT '閺€顖欑帛閺傜懓绱￠敍?-瀵邦喕淇婇敍?-閺€顖欑帛鐎?,
  `pay_time` datetime DEFAULT NULL COMMENT '閺€顖欑帛閺冨爼妫?,
  `deliver_time` datetime DEFAULT NULL COMMENT '閸欐垼鎻ｉ弮鍫曟？',
  `receive_time` datetime DEFAULT NULL COMMENT '閺€鎯版彛閺冨爼妫?,
  `cancel_reason` varchar(255) DEFAULT NULL COMMENT '閸欐牗绉烽崢鐔锋礈',
  `cancel_time` datetime DEFAULT NULL COMMENT '閸欐牗绉烽弮鍫曟？',
  `remark` varchar(255) DEFAULT NULL COMMENT '鐠併垹宕熸径鍥ㄦ暈',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '閸掓稑缂撻弮鍫曟？',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '閺囧瓨鏌婇弮鍫曟？',
  `deleted` tinyint DEFAULT 0 COMMENT '閸掔娀娅庨弽鍥х箶閿?-閺堫亜鍨归梽銈忕礉1-瀹告彃鍨归梽?,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user` (`user_id`),
  KEY `idx_inheritor` (`inheritor_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='鐠併垹宕熺悰?;

-- ----------------------------
-- Table structure for order_item
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '鐠併垹宕熼弰搴ｇ矎ID',
  `order_id` bigint NOT NULL COMMENT '鐠併垹宕烮D',
  `product_id` bigint NOT NULL COMMENT '閸熷棗鎼D',
  `product_name` varchar(200) NOT NULL COMMENT '閸熷棗鎼ч崥宥囆?,
  `product_image` varchar(255) DEFAULT NULL COMMENT '閸熷棗鎼ч崶鍓у',
  `price` decimal(10, 2) NOT NULL COMMENT '閸熷棗鎼ч崡鏇氱幆',
  `quantity` int NOT NULL COMMENT '鐠愵厺鎷遍弫浼村櫤',
  `total_amount` decimal(10, 2) NOT NULL COMMENT '鐏忓繗顓搁柌鎴︻杺',
  `inheritor_id` bigint NOT NULL COMMENT '娴肩姵澹欐禍绡扗',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '閸掓稑缂撻弮鍫曟？',
  `deleted` tinyint DEFAULT 0 COMMENT '閸掔娀娅庨弽鍥х箶閿?-閺堫亜鍨归梽銈忕礉1-瀹告彃鍨归梽?,
  PRIMARY KEY (`id`),
  KEY `idx_order` (`order_id`),
  KEY `idx_product` (`product_id`),
  KEY `idx_inheritor` (`inheritor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='鐠併垹宕熼弰搴ｇ矎鐞?;

-- ----------------------------
-- Table structure for news
-- ----------------------------
DROP TABLE IF EXISTS `news`;
CREATE TABLE `news` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '鐠у嫯顔咺D',
  `title` varchar(200) NOT NULL COMMENT '鐠у嫯顔嗛弽鍥暯',
  `cover_image` varchar(255) DEFAULT NULL COMMENT '鐏忎線娼伴崶鍓у',
  `summary` varchar(500) DEFAULT NULL COMMENT '閹芥顩?,
  `content` longtext COMMENT '鐠у嫯顔嗛崘鍛啇',
  `author_id` bigint DEFAULT NULL COMMENT '娴ｆ粏鈧將D',
  `author_type` tinyint DEFAULT 0 COMMENT '娴ｆ粏鈧懐琚崹瀣剁窗0-缁狅紕鎮婇崨姗堢礉1-娴肩姵澹欐禍?,
  `author_name` varchar(50) DEFAULT NULL COMMENT '娴ｆ粏鈧懎鎮曠粔?,
  `view_count` int DEFAULT 0 COMMENT '濞村繗顫嶉柌?,
  `like_count` int DEFAULT 0 COMMENT '閻愮绂愰弫?,
  `comment_count` int DEFAULT 0 COMMENT '鐠囧嫯顔戦弫?,
  `status` tinyint DEFAULT 1 COMMENT '閻樿埖鈧緤绱?-娑撳鐏﹂敍?-娑撳﹥鐏?,
  `is_top` tinyint DEFAULT 0 COMMENT '閺勵垰鎯佺純顕€銆婇敍?-閸氾讣绱?-閺?,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '閸掓稑缂撻弮鍫曟？',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '閺囧瓨鏌婇弮鍫曟？',
  `deleted` tinyint DEFAULT 0 COMMENT '閸掔娀娅庨弽鍥х箶閿?-閺堫亜鍨归梽銈忕礉1-瀹告彃鍨归梽?,
  PRIMARY KEY (`id`),
  KEY `idx_author` (`author_id`),
  KEY `idx_view_count` (`view_count`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='鐠у嫯顔嗙悰?;

-- ----------------------------
-- Table structure for forum_post
-- ----------------------------
DROP TABLE IF EXISTS `forum_post`;
CREATE TABLE `forum_post` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '鐢牕鐡橧D',
  `title` varchar(200) NOT NULL COMMENT '鐢牕鐡欓弽鍥暯',
  `content` longtext COMMENT '鐢牕鐡欓崘鍛啇',
  `images` text COMMENT '鐢牕鐡欓崶鍓у閿涘湞SON閺佹壆绮嶉敍?,
  `author_id` bigint NOT NULL COMMENT '娴ｆ粏鈧將D',
  `author_type` tinyint DEFAULT 0 COMMENT '娴ｆ粏鈧懐琚崹瀣剁窗0-閺咁噣鈧氨鏁ら幋鍑ょ礉1-娴肩姵澹欐禍?,
  `author_name` varchar(50) DEFAULT NULL COMMENT '娴ｆ粏鈧懎鎮曠粔?,
  `author_avatar` varchar(255) DEFAULT NULL COMMENT '娴ｆ粏鈧懎銇旈崓?,
  `view_count` int DEFAULT 0 COMMENT '濞村繗顫嶉柌?,
  `like_count` int DEFAULT 0 COMMENT '閻愮绂愰弫?,
  `comment_count` int DEFAULT 0 COMMENT '鐠囧嫯顔戦弫?,
  `status` tinyint DEFAULT 1 COMMENT '閻樿埖鈧緤绱?-闂呮劘妫岄敍?-閺勫墽銇?,
  `is_top` tinyint DEFAULT 0 COMMENT '閺勵垰鎯佺純顕€銆婇敍?-閸氾讣绱?-閺?,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '閸掓稑缂撻弮鍫曟？',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '閺囧瓨鏌婇弮鍫曟？',
  `deleted` tinyint DEFAULT 0 COMMENT '閸掔娀娅庨弽鍥х箶閿?-閺堫亜鍨归梽銈忕礉1-瀹告彃鍨归梽?,
  PRIMARY KEY (`id`),
  KEY `idx_author` (`author_id`),
  KEY `idx_view_count` (`view_count`),
  KEY `idx_like_count` (`like_count`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='鐠佸搫娼х敮鏍х摍鐞?;

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '閻ｆ瑨鈻圛D',
  `content` text NOT NULL COMMENT '閻ｆ瑨鈻堥崘鍛啇',
  `user_id` bigint NOT NULL COMMENT '閻ｆ瑨鈻堥悽銊﹀煕ID',
  `user_name` varchar(50) DEFAULT NULL COMMENT '閻ｆ瑨鈻堥悽銊﹀煕閸氬秶袨',
  `user_avatar` varchar(255) DEFAULT NULL COMMENT '閻ｆ瑨鈻堥悽銊﹀煕婢舵潙鍎?,
  `parent_id` bigint DEFAULT NULL COMMENT '閻栧墎鏆€鐟封偓ID閿涘牆娲栨径宥嗘娴ｈ法鏁ら敍?,
  `reply_user_id` bigint DEFAULT NULL COMMENT '鐞氼偄娲栨径宥囨暏閹寸īD',
  `reply_user_name` varchar(50) DEFAULT NULL COMMENT '鐞氼偄娲栨径宥囨暏閹村嘲鎮曠粔?,
  `status` tinyint DEFAULT 1 COMMENT '閻樿埖鈧緤绱?-闂呮劘妫岄敍?-閺勫墽銇?,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '閸掓稑缂撻弮鍫曟？',
  `deleted` tinyint DEFAULT 0 COMMENT '閸掔娀娅庨弽鍥х箶閿?-閺堫亜鍨归梽銈忕礉1-瀹告彃鍨归梽?,
  PRIMARY KEY (`id`),
  KEY `idx_user` (`user_id`),
  KEY `idx_parent` (`parent_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='閻ｆ瑨鈻堢悰?;

-- ----------------------------
-- Table structure for announcement
-- ----------------------------
DROP TABLE IF EXISTS announcement;
CREATE TABLE announcement (
  id bigint NOT NULL AUTO_INCREMENT COMMENT '鍏憡ID',
  title varchar(200) NOT NULL COMMENT '鍏憡鏍囬',
  content text COMMENT '鍏憡鍐呭',
  type tinyint DEFAULT 0 COMMENT '鍏憡绫诲瀷锛?-鏅€氬叕鍛婏紝1-閲嶈鍏憡',
  status tinyint DEFAULT 1 COMMENT '鐘舵€侊細0-鍏抽棴锛?-寮€鍚?,
  sort int DEFAULT 0 COMMENT '鎺掑簭',
  start_time datetime DEFAULT NULL COMMENT '寮€濮嬫椂闂?,
  end_time datetime DEFAULT NULL COMMENT '缁撴潫鏃堕棿',
  create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '鍒涘缓鏃堕棿',
  update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '鏇存柊鏃堕棿',
  deleted tinyint DEFAULT 0 COMMENT '鍒犻櫎鏍囧織锛?-鏈垹闄わ紝1-宸插垹闄?,
  PRIMARY KEY (id),
  KEY idx_sort (sort),
  KEY idx_status (status),
  KEY idx_time (start_time, end_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='鍏憡琛?;


-- ----------------------------
-- Table structure for banner
-- ----------------------------
DROP TABLE IF EXISTS `banner`;
CREATE TABLE `banner` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '鏉烆喗鎸遍崶缍',
  `title` varchar(100) DEFAULT NULL COMMENT '閺嶅洭顣?,
  `image_url` varchar(255) NOT NULL COMMENT '閸ュ墽澧朥RL',
  `link_url` varchar(255) DEFAULT NULL COMMENT '鐠哄疇娴嗛柧鐐复',
  `link_type` tinyint DEFAULT 0 COMMENT '闁剧偓甯寸猾璇茬€烽敍?-閺冪姾鐑︽潪顒婄礉1-闂堢偤浠愮拠锔藉剰閿?-閸熷棗鎼х拠锔藉剰閿?-鐠у嫯顔嗙拠锔藉剰閿?-婢舵牠鍎撮柧鐐复',
  `link_id` bigint DEFAULT NULL COMMENT '閸忓疇浠圛D',
  `sort` int DEFAULT 0 COMMENT '閹烘帒绨?,
  `status` tinyint DEFAULT 1 COMMENT '閻樿埖鈧緤绱?-闂呮劘妫岄敍?-閺勫墽銇?,
  `start_time` datetime DEFAULT NULL COMMENT '瀵偓婵妞傞梻?,
  `end_time` datetime DEFAULT NULL COMMENT '缂佹挻娼弮鍫曟？',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '閸掓稑缂撻弮鍫曟？',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '閺囧瓨鏌婇弮鍫曟？',
  `deleted` tinyint DEFAULT 0 COMMENT '閸掔娀娅庨弽鍥х箶閿?-閺堫亜鍨归梽銈忕礉1-瀹告彃鍨归梽?,
  PRIMARY KEY (`id`),
  KEY `idx_sort` (`sort`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='鏉烆喗鎸遍崶鎹愩€?;

-- ----------------------------
-- Table structure for user_favorite
-- ----------------------------
DROP TABLE IF EXISTS `user_favorite`;
CREATE TABLE `user_favorite` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '閺€鎯版ID',
  `user_id` bigint NOT NULL COMMENT '閻劍鍩汭D',
  `target_type` tinyint NOT NULL COMMENT '閻╊喗鐖ｇ猾璇茬€烽敍?-闂堢偤浠愭い鍦窗閿?-閺傚洤鍨遍崯鍡楁惂閿?-鐠у嫯顔嗛敍?-鐢牕鐡?,
  `target_id` bigint NOT NULL COMMENT '閻╊喗鐖D',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '閸掓稑缂撻弮鍫曟？',
  `deleted` tinyint DEFAULT 0 COMMENT '閸掔娀娅庨弽鍥х箶閿?-閺堫亜鍨归梽銈忕礉1-瀹告彃鍨归梽?,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_target` (`user_id`, `target_type`, `target_id`),
  KEY `idx_target` (`target_type`, `target_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='閻劍鍩涢弨鎯版鐞?;

-- ----------------------------
-- Table structure for user_like
-- ----------------------------
DROP TABLE IF EXISTS `user_like`;
CREATE TABLE `user_like` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '閻愮绂怚D',
  `user_id` bigint NOT NULL COMMENT '閻劍鍩汭D',
  `target_type` tinyint NOT NULL COMMENT '閻╊喗鐖ｇ猾璇茬€烽敍?-闂堢偤浠愭い鍦窗閿?-閺傚洤鍨遍崯鍡楁惂閿?-鐠у嫯顔嗛敍?-鐢牕鐡欓敍?-鐠囧嫯顔?,
  `target_id` bigint NOT NULL COMMENT '閻╊喗鐖D',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '閸掓稑缂撻弮鍫曟？',
  `deleted` tinyint DEFAULT 0 COMMENT '閸掔娀娅庨弽鍥х箶閿?-閺堫亜鍨归梽銈忕礉1-瀹告彃鍨归梽?,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_target` (`user_id`, `target_type`, `target_id`),
  KEY `idx_target` (`target_type`, `target_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='閻劍鍩涢悙纭呯鐞?;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '鐠囧嫯顔慖D',
  `content` text NOT NULL COMMENT '鐠囧嫯顔戦崘鍛啇',
  `user_id` bigint NOT NULL COMMENT '鐠囧嫯顔戦悽銊﹀煕ID',
  `user_name` varchar(50) DEFAULT NULL COMMENT '鐠囧嫯顔戦悽銊﹀煕閸氬秶袨',
  `user_avatar` varchar(255) DEFAULT NULL COMMENT '鐠囧嫯顔戦悽銊﹀煕婢舵潙鍎?,
  `target_type` tinyint NOT NULL COMMENT '閻╊喗鐖ｇ猾璇茬€烽敍?-闂堢偤浠愭い鍦窗閿?-閺傚洤鍨遍崯鍡楁惂閿?-鐠у嫯顔嗛敍?-鐢牕鐡?,
  `target_id` bigint NOT NULL COMMENT '閻╊喗鐖D',
  `parent_id` bigint DEFAULT NULL COMMENT '閻栨儼鐦庣拋绡扗',
  `reply_user_id` bigint DEFAULT NULL COMMENT '鐞氼偄娲栨径宥囨暏閹寸īD',
  `reply_user_name` varchar(50) DEFAULT NULL COMMENT '鐞氼偄娲栨径宥囨暏閹村嘲鎮曠粔?,
  `like_count` int DEFAULT 0 COMMENT '閻愮绂愰弫?,
  `status` tinyint DEFAULT 1 COMMENT '閻樿埖鈧緤绱?-闂呮劘妫岄敍?-閺勫墽銇?,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '閸掓稑缂撻弮鍫曟？',
  `deleted` tinyint DEFAULT 0 COMMENT '閸掔娀娅庨弽鍥х箶閿?-閺堫亜鍨归梽銈忕礉1-瀹告彃鍨归梽?,
  PRIMARY KEY (`id`),
  KEY `idx_user` (`user_id`),
  KEY `idx_target` (`target_type`, `target_id`),
  KEY `idx_parent` (`parent_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='鐠囧嫯顔戠悰?;

-- ----------------------------
-- Table structure for consultation
-- ----------------------------
DROP TABLE IF EXISTS `consultation`;
CREATE TABLE `consultation` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '閸溿劏顕桰D',
  `user_id` bigint NOT NULL COMMENT '閸溿劏顕楅悽銊﹀煕ID',
  `user_name` varchar(50) DEFAULT NULL COMMENT '閸溿劏顕楅悽銊﹀煕閸氬秶袨',
  `user_avatar` varchar(255) DEFAULT NULL COMMENT '閸溿劏顕楅悽銊﹀煕婢舵潙鍎?,
  `target_type` tinyint NOT NULL COMMENT '閸溿劏顕楃猾璇茬€烽敍?-閸忋劌鐪崪銊嚄閿涘牏顓搁悶鍡楁喅閿涘绱?-娑撴挸鐫橀崪銊嚄閿涘牅绱堕幍澶告眽閿?,
  `target_id` bigint DEFAULT NULL COMMENT '閻╊喗鐖D閿涘牅绱堕幍澶告眽ID閹存牜顓搁悶鍡楁喅ID閿?,
  `target_name` varchar(50) DEFAULT NULL COMMENT '閻╊喗鐖ｉ崥宥囆?,
  `content` text NOT NULL COMMENT '閸溿劏顕楅崘鍛啇',
  `reply_content` text COMMENT '閸ョ偛顦查崘鍛啇',
  `reply_user_id` bigint DEFAULT NULL COMMENT '閸ョ偛顦查悽銊﹀煕ID',
  `reply_user_name` varchar(50) DEFAULT NULL COMMENT '閸ョ偛顦查悽銊﹀煕閸氬秶袨',
  `reply_time` datetime DEFAULT NULL COMMENT '閸ョ偛顦查弮鍫曟？',
  `status` tinyint DEFAULT 0 COMMENT '閻樿埖鈧緤绱?-瀵板懎娲栨径宥忕礉1-瀹告彃娲栨径?,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '閸掓稑缂撻弮鍫曟？',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '閺囧瓨鏌婇弮鍫曟？',
  `deleted` tinyint DEFAULT 0 COMMENT '閸掔娀娅庨弽鍥х箶閿?-閺堫亜鍨归梽銈忕礉1-瀹告彃鍨归梽?,
  PRIMARY KEY (`id`),
  KEY `idx_user` (`user_id`),
  KEY `idx_target` (`target_type`, `target_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='閸溿劏顕楃悰?;

-- ----------------------------
-- Table structure for sensitive_word
-- ----------------------------
DROP TABLE IF EXISTS `sensitive_word`;
CREATE TABLE `sensitive_word` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '閺佸繑鍔呯拠宀籇',
  `word` varchar(100) NOT NULL COMMENT '閺佸繑鍔呯拠?,
  `type` tinyint DEFAULT 0 COMMENT '缁鐎烽敍?-閺咁噣鈧碍鏅遍幇鐔荤槤閿?-娑撱儵鍣搁弫蹇斿妳鐠?,
  `replacement` varchar(100) DEFAULT '*' COMMENT '閺囨寧宕茬€涙顑?,
  `status` tinyint DEFAULT 1 COMMENT '閻樿埖鈧緤绱?-缁備胶鏁ら敍?-閸氼垳鏁?,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '閸掓稑缂撻弮鍫曟？',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '閺囧瓨鏌婇弮鍫曟？',
  `deleted` tinyint DEFAULT 0 COMMENT '閸掔娀娅庨弽鍥х箶閿?-閺堫亜鍨归梽銈忕礉1-瀹告彃鍨归梽?,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_word` (`word`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='閺佸繑鍔呯拠宥堛€?;

SET FOREIGN_KEY_CHECKS = 1;


