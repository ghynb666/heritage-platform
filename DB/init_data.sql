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
