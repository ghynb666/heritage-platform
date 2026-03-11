USE `heritage_platform`;

INSERT INTO `sys_role` (`role_name`, `role_key`, `description`, `status`) VALUES
('普通用户', 'USER', '普通用户角色', 1),
('传承人', 'INHERITOR', '传承人角色', 1),
('管理员', 'ADMIN', '管理员角色', 1);

INSERT INTO `sys_user` (`username`, `password`, `nickname`, `status`) VALUES
('admin', 'admin123', '管理员', 1);

INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES (1, 3);

INSERT INTO `heritage_category` (`name`, `description`, `sort`, `status`) VALUES
('剪纸', '中国剪纸是一种用剪刀或刻刀在纸上剪刻花纹的艺术', 1, 1),
('刺绣', '刺绣是用针线在织物上绣制各种装饰图案的总称', 2, 1),
('陶艺', '陶艺是陶瓷艺术的简称，是制作陶器和瓷器的艺术', 3, 1),
('皮影', '皮影戏是中国民间古老的传统艺术，老北京人都叫它"驴皮影"', 4, 1),
('木雕', '木雕是雕塑的一种，是以木材为材料的雕刻工艺', 5, 1),
('漆器', '漆器是用漆涂在各种器物的表面上所制成的日常器具及工艺品', 6, 1),
('织锦', '织锦是用染好颜色的彩色经纬线经提花、织造工艺织出图案的织物', 7, 1),
('年画', '年画是中国画的一种，始于古代的"门神画"', 8, 1);

INSERT INTO `announcement` (`title`, `content`, `status`, `sort`) VALUES
('热烈欢迎访问非物质文化遗产展示与交流平台', '致力于传承和弘扬中华优秀传统文化', 1, 1),
('新功能上线：传承人申请通道已开启', '欢迎各位传承人提交申请，共同守护非遗文化', 1, 2),
('平台升级公告', '系统持续优化中，为您提供更好的体验', 1, 3);
-- P3 非遗分类与项目展示 测试数据

-- 确保非遗分类数据存在
INSERT INTO heritage_category (id, name, icon, description, sort, status, create_time, update_time, deleted) VALUES
(1, '剪纸', '✂', '中国剪纸是一种用剪刀或刻刀在纸上剪刻花纹的民间艺术', 1, 1, NOW(), NOW(), 0),
(2, '刺绣', '🧵', '刺绣是用针线在织物上绣制各种装饰图案的手工艺术', 2, 1, NOW(), NOW(), 0),
(3, '陶艺', '🏺', '陶艺是制作陶器和瓷器的传统手工技艺', 3, 1, NOW(), NOW(), 0),
(4, '皮影', '🎭', '皮影戏是用灯光照射兽皮或纸板做成的人物剪影以表演故事的民间戏剧', 4, 1, NOW(), NOW(), 0),
(5, '木雕', '🪵', '木雕是雕塑的一种，以木材为材料进行雕刻的艺术', 5, 1, NOW(), NOW(), 0),
(6, '漆器', '🎨', '漆器是用漆涂在各种器物表面制成的器具', 6, 1, NOW(), NOW(), 0),
(7, '织锦', '🧶', '织锦是用染好颜色的彩色经纬线织出图案的织物', 7, 1, NOW(), NOW(), 0),
(8, '年画', '🖼', '年画是中国民间在年节之际用来迎新春、祈丰年的一种民间美术', 8, 1, NOW(), NOW(), 0)
ON DUPLICATE KEY UPDATE name=VALUES(name), icon=VALUES(icon), description=VALUES(description);

-- 非遗项目测试数据 (管理员发布)
INSERT INTO heritage_item (id, title, heritage_category_id, cover_image, history_origin, craft_feature, content, publisher_id, publisher_type, view_count, like_count, favorite_count, comment_count, status, is_recommend, create_time, update_time, deleted) VALUES
(1, '扬州剪纸艺术', 1, '/uploads/heritage/images/default/jianzhi.jpg', 
'扬州剪纸是中国剪纸艺术的重要流派之一，起源于唐代，至今已有一千多年的历史。扬州剪纸以其线条流畅、构图精巧、刀法细腻而著称，被誉为"剪中精品"。',
'扬州剪纸的特点是"线条如丝、连接不断"，艺人运用一把剪刀，一张纸，不画稿，不描样，仅凭一把剪刀就能剪出栩栩如生的图案。主要技法有阳剪、阴剪、阴阳结合剪等。',
'扬州剪纸题材广泛，包括花鸟鱼虫、人物故事、吉祥图案等。代表作品有《百鸟朝凤》、《龙凤呈祥》、《松鹤延年》等。扬州剪纸已被列入国家级非物质文化遗产名录。',
1, 0, 1256, 89, 45, 12, 1, 1, NOW(), NOW(), 0),

(2, '苏绣精品赏析', 2, '/uploads/heritage/images/default/suxiu.jpg',
'苏绣是苏州地区刺绣产品的总称，起源于苏州，已有两千多年的历史。苏绣以针法精细、色彩雅丽著称，与湘绣、粤绣、蜀绣并称为中国四大名绣。',
'苏绣的特点是"平、齐、和、光、顺、匀"，针法多达40多种，常用的有齐针、抢针、套针、施针等。苏绣艺人能以针代笔，以线代色，绣出如同画作般精美的作品。',
'苏绣代表作品有《猫》、《金鱼》、《牡丹》等，其中双面绣《猫》更是苏绣的代表作，正反两面图案相同，针法细腻，栩栩如生。苏绣已被列入国家级非物质文化遗产名录。',
1, 0, 2341, 156, 78, 23, 1, 1, NOW(), NOW(), 0),

(3, '景德镇青花瓷', 3, '/uploads/heritage/images/default/qinghua.jpg',
'青花瓷是中国陶瓷艺术的瑰宝，起源于唐代，成熟于元代，鼎盛于明清。景德镇青花瓷以其"白如玉、明如镜、薄如纸、声如磬"的特点闻名于世。',
'青花瓷采用钴料在瓷胎上绘画，施以透明釉，经高温一次烧成。其特点是釉质晶莹、青花鲜艳、图案清晰、永不褪色。主要装饰技法有青花、青花玲珑、青花釉里红等。',
'景德镇青花瓷代表作品有《青花缠枝莲纹瓶》、《青花龙纹盘》等。青花瓷不仅是实用器皿，更是艺术珍品，被誉为"国瓷"。景德镇青花瓷烧制技艺已被列入国家级非物质文化遗产名录。',
1, 0, 3567, 234, 123, 45, 1, 1, NOW(), NOW(), 0),

(4, '陕西皮影戏', 4, '/uploads/heritage/images/default/piying.jpg',
'皮影戏是中国民间古老的传统艺术，起源于西汉，兴盛于唐宋。陕西皮影戏以其造型生动、唱腔高亢、表演细腻而著称，是中国皮影戏的重要流派之一。',
'皮影戏的皮影是用牛皮或驴皮经过刮皮、浆皮、雕刻、着色、涂油等工序制作而成。皮影人物造型夸张，色彩鲜艳，关节灵活，能做出各种动作。',
'陕西皮影戏的剧目丰富，代表剧目有《白蛇传》、《西游记》、《三国演义》等。皮影戏艺人一人操纵多个皮影，同时演唱多个角色，技艺高超。陕西皮影戏已被列入国家级非物质文化遗产名录。',
1, 0, 1892, 98, 56, 18, 1, 0, NOW(), NOW(), 0),

(5, '东阳木雕艺术', 5, '/uploads/heritage/images/default/mudiao.jpg',
'东阳木雕是浙江省东阳市的传统雕刻艺术，起源于唐代，至今已有一千多年的历史。东阳木雕以其层次丰富、雕刻精细、题材广泛而著称，被誉为"雕花之乡"。',
'东阳木雕的技法包括浮雕、圆雕、透雕等，以浮雕为主。雕刻艺人以刀代笔，在木板上雕刻出层次分明、立体感强的图案。常用的木材有樟木、银杏木、椴木等。',
'东阳木雕广泛应用于建筑装饰、家具制作、工艺品雕刻等领域。代表作品有《清明上河图》、《百鸟朝凤》等大型木雕作品。东阳木雕已被列入国家级非物质文化遗产名录。',
1, 0, 1456, 87, 43, 15, 1, 0, NOW(), NOW(), 0),

(6, '福州脱胎漆器', 6, '/uploads/heritage/images/default/qiqi.jpg',
'福州脱胎漆器是福建省福州市的传统手工艺品，创始于清代乾隆年间，已有两百多年的历史。福州脱胎漆器以其质地轻巧、造型美观、色彩艳丽而著称。',
'脱胎漆器的制作工艺独特，先用泥土、石膏等材料制成胎模，然后在胎模上裱贴麻布、涂刷生漆，待干固后脱去胎模，再经过打磨、髹漆、装饰等工序制成成品。',
'福州脱胎漆器的装饰技法有描金、填彩、镶嵌等，色彩以朱红、黑色为主，配以金色装饰，富丽堂皇。代表作品有《脱胎菊瓣瓶》、《脱胎仿古铜器》等。福州脱胎漆器已被列入国家级非物质文化遗产名录。',
1, 0, 987, 65, 34, 9, 1, 0, NOW(), NOW(), 0),

(7, '南京云锦织造', 7, '/uploads/heritage/images/default/yunjin.jpg',
'南京云锦是中国传统的丝织工艺品，因其色彩绚丽、花纹如云而得名。云锦起源于元代，鼎盛于明清，是中国四大名锦之首，被誉为"东方瑰宝"。',
'云锦的织造工艺极为复杂，需要使用大花楼木织机，由两人配合操作。织造技法有妆花、织金、库缎等，其中妆花是云锦最具代表性的技法，能织出色彩丰富、图案精美的织物。',
'南京云锦代表作品有《龙袍》、《凤冠霞帔》等，曾为皇家御用贡品。云锦的图案寓意吉祥，如龙凤呈祥、富贵牡丹等。南京云锦织造技艺已被列入国家级非物质文化遗产名录。',
1, 0, 2134, 145, 89, 28, 1, 1, NOW(), NOW(), 0),

(8, '杨柳青木版年画', 8, '/uploads/heritage/images/default/nianhua.jpg',
'杨柳青木版年画是天津市杨柳青镇的传统民间艺术，起源于明代，已有四百多年的历史。杨柳青年画以其造型生动、色彩鲜艳、寓意吉祥而著称，是中国四大年画之一。',
'杨柳青年画的制作采用木版套印与手工彩绘相结合的方法，先在木版上刻出图案轮廓，然后套印颜色，最后由画师手工描绘面部、手部等细节。',
'杨柳青年画的题材广泛，包括历史故事、神话传说、吉祥图案等。代表作品有《连年有余》、《福寿三多》、《仕女图》等。杨柳青木版年画已被列入国家级非物质文化遗产名录。',
1, 0, 1678, 112, 67, 21, 1, 0, NOW(), NOW(), 0),

(9, '蔚县剪纸艺术', 1, '/uploads/heritage/images/default/weixian.jpg',
'蔚县剪纸是河北省蔚县的传统民间艺术，起源于明代，已有六百多年的历史。蔚县剪纸以其"刻纸"技法和点彩染色而独树一帜，被誉为"剪纸之花"。',
'蔚县剪纸采用刻刀雕刻，不同于一般剪纸的剪刀剪制。刻刀雕刻能做出更加精细的图案，线条流畅，连接自然。然后采用点彩染色技法，使作品色彩丰富、层次分明。',
'蔚县剪纸代表作品有《戏曲人物》、《十二生肖》、《花鸟虫鱼》等。蔚县剪纸已被列入国家级非物质文化遗产名录，是世界非物质文化遗产。',
1, 0, 892, 54, 28, 8, 1, 0, NOW(), NOW(), 0),

(10, '蜀绣传统技艺', 2, '/uploads/heritage/images/default/shuxiu.jpg',
'蜀绣是四川省成都市的传统刺绣艺术，起源于古蜀国，已有两千多年的历史。蜀绣以软缎和彩丝为主要原料，以针法严谨、针脚平齐、色彩明艳而著称。',
'蜀绣的针法多达100多种，常用的有晕针、铺针、滚针、截针等。蜀绣艺人能根据图案的需要，灵活运用各种针法，绣出层次丰富、立体感强的作品。',
'蜀绣代表作品有《芙蓉鲤鱼图》、《熊猫》、《蜀宫乐伎图》等。蜀绣已被列入国家级非物质文化遗产名录。',
1, 0, 1567, 98, 52, 16, 1, 0, NOW(), NOW(), 0),

(11, '宜兴紫砂陶艺', 3, '/uploads/heritage/images/default/zisha.jpg',
'宜兴紫砂陶是江苏省宜兴市的传统手工艺品，起源于宋代，兴盛于明清。宜兴紫砂以其独特的材质、精湛的工艺和深厚的文化内涵而著称于世。',
'紫砂陶采用宜兴特有的紫砂泥为原料，经过打泥片、围身筒、做壶嘴、装壶把、精加工等工序制成。紫砂壶具有良好的透气性，泡茶不失原味，越用越光润。',
'宜兴紫砂代表作品有《供春壶》、《仿古壶》、《石瓢壶》等。历代紫砂名家辈出，如供春、时大彬、陈鸣远、顾景舟等。宜兴紫砂陶制作技艺已被列入国家级非物质文化遗产名录。',
1, 0, 2890, 189, 98, 32, 1, 1, NOW(), NOW(), 0),

(12, '黄杨木雕技艺', 5, '/uploads/heritage/images/default/huangyang.jpg',
'黄杨木雕是以黄杨木为材料的传统雕刻艺术，主要流传于浙江乐清、温州一带。黄杨木雕以其质地细腻、色泽温润、雕刻精细而著称。',
'黄杨木生长缓慢，木质坚韧细腻，色泽淡黄如象牙，是雕刻的上等材料。黄杨木雕的技法以圆雕为主，辅以浮雕、镂空雕等，作品造型生动，神态逼真。',
'黄杨木雕代表作品有《观音》、《罗汉》、《仕女》等。黄杨木雕已被列入国家级非物质文化遗产名录。',
1, 0, 1234, 76, 41, 13, 1, 0, NOW(), NOW(), 0);

-- 项目图片测试数据
INSERT INTO heritage_image (id, heritage_item_id, image_url, description, sort, create_time, deleted) VALUES
(1, 1, '/uploads/heritage/images/jianzhi1.jpg', '扬州剪纸作品展示', 1, NOW(), 0),
(2, 1, '/uploads/heritage/images/jianzhi2.jpg', '剪纸艺人现场表演', 2, NOW(), 0),
(3, 1, '/uploads/heritage/images/jianzhi3.jpg', '传统剪纸图案', 3, NOW(), 0),
(4, 2, '/uploads/heritage/images/suxiu1.jpg', '苏绣双面绣作品', 1, NOW(), 0),
(5, 2, '/uploads/heritage/images/suxiu2.jpg', '苏绣猫作品', 2, NOW(), 0),
(6, 2, '/uploads/heritage/images/suxiu3.jpg', '苏绣牡丹作品', 3, NOW(), 0),
(7, 3, '/uploads/heritage/images/qinghua1.jpg', '青花瓷瓶', 1, NOW(), 0),
(8, 3, '/uploads/heritage/images/qinghua2.jpg', '青花瓷盘', 2, NOW(), 0),
(9, 3, '/uploads/heritage/images/qinghua3.jpg', '青花瓷制作过程', 3, NOW(), 0),
(10, 4, '/uploads/heritage/images/piying1.jpg', '皮影人物造型', 1, NOW(), 0),
(11, 4, '/uploads/heritage/images/piying2.jpg', '皮影戏表演场景', 2, NOW(), 0),
(12, 5, '/uploads/heritage/images/mudiao1.jpg', '东阳木雕作品', 1, NOW(), 0),
(13, 5, '/uploads/heritage/images/mudiao2.jpg', '木雕细节展示', 2, NOW(), 0),
(14, 6, '/uploads/heritage/images/qiqi1.jpg', '脱胎漆器花瓶', 1, NOW(), 0),
(15, 6, '/uploads/heritage/images/qiqi2.jpg', '漆器装饰细节', 2, NOW(), 0),
(16, 7, '/uploads/heritage/images/yunjin1.jpg', '云锦龙袍', 1, NOW(), 0),
(17, 7, '/uploads/heritage/images/yunjin2.jpg', '云锦织造过程', 2, NOW(), 0),
(18, 8, '/uploads/heritage/images/nianhua1.jpg', '杨柳青年画作品', 1, NOW(), 0),
(19, 8, '/uploads/heritage/images/nianhua2.jpg', '年画印制过程', 2, NOW(), 0),
(20, 11, '/uploads/heritage/images/zisha1.jpg', '紫砂壶作品', 1, NOW(), 0),
(21, 11, '/uploads/heritage/images/zisha2.jpg', '紫砂制作过程', 2, NOW(), 0);

-- 项目视频测试数据
INSERT INTO heritage_video (id, heritage_item_id, video_url, cover_url, title, description, duration, sort, create_time, deleted) VALUES
(1, 1, '/uploads/heritage/videos/jianzhi_demo.mp4', '/uploads/heritage/images/jianzhi_cover.jpg', '扬州剪纸技艺展示', '扬州剪纸传承人现场演示剪纸技艺', 180, 1, NOW(), 0),
(2, 2, '/uploads/heritage/videos/suxiu_demo.mp4', '/uploads/heritage/images/suxiu_cover.jpg', '苏绣针法演示', '苏绣艺人演示传统刺绣针法', 240, 1, NOW(), 0),
(3, 3, '/uploads/heritage/videos/qinghua_demo.mp4', '/uploads/heritage/images/qinghua_cover.jpg', '青花瓷制作工艺', '景德镇青花瓷从拉坯到烧制的完整过程', 360, 1, NOW(), 0),
(4, 4, '/uploads/heritage/videos/piying_demo.mp4', '/uploads/heritage/images/piying_cover.jpg', '皮影戏表演', '陕西皮影戏经典剧目片段', 300, 1, NOW(), 0),
(5, 7, '/uploads/heritage/videos/yunjin_demo.mp4', '/uploads/heritage/images/yunjin_cover.jpg', '云锦织造技艺', '南京云锦传统织造工艺展示', 420, 1, NOW(), 0),
(6, 11, '/uploads/heritage/videos/zisha_demo.mp4', '/uploads/heritage/images/zisha_cover.jpg', '紫砂壶制作', '宜兴紫砂壶手工制作全过程', 280, 1, NOW(), 0);
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
