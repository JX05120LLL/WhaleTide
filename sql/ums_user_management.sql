/*
 优化后的商城系统数据库设计 - 用户管理系统(UMS)模块
 
 包含以下表:
 - ums_users: 用户表(原user)
 - ums_merchants: 商家表(新增，原来在user表中使用JSON存储)
 - ums_user_addresses: 用户地址表(原user_address)
 - ums_user_favorites: 用户收藏表(新增)
 - ums_user_search_history: 用户搜索历史表(新增)
 - ums_user_coupons: 用户优惠券表(原user_coupon)
 - ums_user_login_logs: 用户登录日志表(新增)
 - ums_user_roles: 用户角色表(新增)
 - ums_notifications: 用户通知表(原user_notification)
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ums_users
-- ----------------------------
DROP TABLE IF EXISTS `ums_users`;
CREATE TABLE `ums_users` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮箱',
  `nickname` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '昵称',
  `real_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '真实姓名',
  `gender` tinyint DEFAULT 0 COMMENT '性别：0-未知，1-男，2-女',
  `birth` date DEFAULT NULL COMMENT '生日',
  `avatar` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '头像',
  `status` tinyint DEFAULT 1 COMMENT '启用状态：0-禁用，1-启用',
  `status_reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '状态原因',
  `register_source` tinyint DEFAULT 0 COMMENT '注册来源：0-PC，1-APP，2-小程序，3-H5',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '最后登录IP',
  `is_merchant` tinyint DEFAULT 0 COMMENT '是否商家：0-否，1-是',
  `level` int DEFAULT 0 COMMENT '会员等级',
  `integration` int DEFAULT 0 COMMENT '积分',
  `growth` int DEFAULT 0 COMMENT '成长值',
  `region` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所在地区',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `is_deleted` tinyint DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_username` (`username`) USING BTREE,
  UNIQUE KEY `uk_phone` (`phone`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE,
  KEY `idx_is_merchant` (`is_merchant`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

-- ----------------------------
-- Records of ums_users
-- ----------------------------
INSERT INTO `ums_users` VALUES (1, 'user123', '$2a$10$NZ5o7r2E.ayT2ZoxgjlI.eJ6OEYqjH7INR/F.mXDbjZJi9HF0YCVG', '13800138000', 'user123@example.com', '张小明', '张明', 1, '1990-01-01', 'https://example.com/avatar/1.jpg', 1, NULL, 0, '2023-01-02 10:00:00', '192.168.1.1', 0, 0, 100, 20, '北京市', NULL, 0, '2023-01-01 00:00:00', '2023-01-02 10:00:00');
INSERT INTO `ums_users` VALUES (2, 'merchant456', '$2a$10$NZ5o7r2E.ayT2ZoxgjlI.eJ6OEYqjH7INR/F.mXDbjZJi9HF0YCVG', '13900139000', 'merchant456@example.com', '李大卖', '李卖', 1, '1985-05-05', 'https://example.com/avatar/2.jpg', 1, NULL, 1, '2023-01-02 11:00:00', '192.168.1.2', 1, 1, 200, 50, '上海市', NULL, 0, '2023-01-01 00:00:00', '2023-01-02 11:00:00');
INSERT INTO `ums_users` VALUES (3, 'user789', '$2a$10$NZ5o7r2E.ayT2ZoxgjlI.eJ6OEYqjH7INR/F.mXDbjZJi9HF0YCVG', '13700137000', 'user789@example.com', '王小红', '王红', 2, '1995-10-10', 'https://example.com/avatar/3.jpg', 1, NULL, 2, '2023-01-02 12:00:00', '192.168.1.3', 0, 0, 50, 10, '广州市', NULL, 0, '2023-01-01 00:00:00', '2023-01-02 12:00:00');
INSERT INTO `ums_users` VALUES (4, 'merchant321', '$2a$10$NZ5o7r2E.ayT2ZoxgjlI.eJ6OEYqjH7INR/F.mXDbjZJi9HF0YCVG', '13600136000', 'merchant321@example.com', '赵掌柜', '赵柜', 1, '1980-12-12', 'https://example.com/avatar/4.jpg', 1, NULL, 0, '2023-01-02 13:00:00', '192.168.1.4', 1, 2, 300, 100, '深圳市', NULL, 0, '2023-01-01 00:00:00', '2023-01-02 13:00:00');

-- ----------------------------
-- Table structure for ums_merchants
-- ----------------------------
DROP TABLE IF EXISTS `ums_merchants`;
CREATE TABLE `ums_merchants` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '商家ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `shop_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '店铺名称',
  `shop_logo` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '店铺logo',
  `shop_banner` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '店铺横幅',
  `business_license` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '营业执照',
  `identity_card_front` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '身份证正面',
  `identity_card_back` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '身份证背面',
  `contact_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '联系人姓名',
  `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '联系电话',
  `contact_email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '联系邮箱',
  `business_categories` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '经营类目，多个用逗号分隔',
  `business_scope` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '经营范围',
  `shop_address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '店铺地址',
  `shop_province` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '省份',
  `shop_city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '城市',
  `shop_district` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '区县',
  `shop_postal_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮政编码',
  `bank_account_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '开户名',
  `bank_account_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '银行账号',
  `bank_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '开户银行',
  `bank_branch` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '支行名称',
  `status` tinyint DEFAULT 0 COMMENT '状态：0-待审核，1-审核通过，2-审核拒绝，3-冻结',
  `status_reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '状态原因',
  `rating` decimal(2, 1) DEFAULT 5.0 COMMENT '店铺评分',
  `service_rating` decimal(2, 1) DEFAULT 5.0 COMMENT '服务评分',
  `delivery_rating` decimal(2, 1) DEFAULT 5.0 COMMENT '物流评分',
  `description_rating` decimal(2, 1) DEFAULT 5.0 COMMENT '描述相符评分',
  `total_sales` int DEFAULT 0 COMMENT '总销量',
  `total_product_count` int DEFAULT 0 COMMENT '商品总数',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `is_deleted` tinyint DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_user_id` (`user_id`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE,
  CONSTRAINT `fk_merchant_user` FOREIGN KEY (`user_id`) REFERENCES `ums_users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商家表';

-- ----------------------------
-- Records of ums_merchants
-- ----------------------------
INSERT INTO `ums_merchants` VALUES (1, 2, '李大卖的小店', 'https://example.com/shop/logo/1.jpg', 'https://example.com/shop/banner/1.jpg', 'https://example.com/license/1.jpg', 'https://example.com/id/front/1.jpg', 'https://example.com/id/back/1.jpg', '李卖', '13900139000', 'merchant456@example.com', '服装,鞋包', '服装、鞋包、配饰', '上海市浦东新区张江高科技园区', '上海市', '上海市', '浦东新区', '200120', '李卖', '6222021234567890123', '中国工商银行', '张江支行', 1, NULL, 4.8, 4.9, 4.7, 4.8, 1250, 78, NULL, 0, '2023-01-01 00:00:00', '2023-01-02 11:00:00');
INSERT INTO `ums_merchants` VALUES (2, 4, '赵掌柜精品铺', 'https://example.com/shop/logo/2.jpg', 'https://example.com/shop/banner/2.jpg', 'https://example.com/license/2.jpg', 'https://example.com/id/front/2.jpg', 'https://example.com/id/back/2.jpg', '赵柜', '13600136000', 'merchant321@example.com', '数码,家电', '数码产品、家用电器、智能设备', '深圳市南山区科技园', '广东省', '深圳市', '南山区', '518057', '赵柜', '6222021234567890456', '中国建设银行', '科技园支行', 1, NULL, 4.9, 5.0, 4.8, 4.9, 2380, 156, NULL, 0, '2023-01-01 00:00:00', '2023-01-02 13:00:00');

-- ----------------------------
-- Table structure for ums_user_addresses
-- ----------------------------
DROP TABLE IF EXISTS `ums_user_addresses`;
CREATE TABLE `ums_user_addresses` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '地址ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `receiver_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人电话',
  `province` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '省份',
  `city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '城市',
  `district` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '区县',
  `detail_address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '详细地址',
  `postal_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮政编码',
  `default_status` tinyint DEFAULT 0 COMMENT '是否默认：0-否，1-是',
  `tag` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '地址标签：家、公司等',
  `is_deleted` tinyint DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE,
  CONSTRAINT `fk_address_user` FOREIGN KEY (`user_id`) REFERENCES `ums_users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户地址表';

-- ----------------------------
-- Records of ums_user_addresses
-- ----------------------------
INSERT INTO `ums_user_addresses` VALUES (1, 1, '张明', '13800138000', '北京市', '北京市', '朝阳区', '朝阳路100号', '100000', 1, '家', 0, '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ums_user_addresses` VALUES (2, 1, '张明', '13800138000', '北京市', '北京市', '海淀区', '中关村科技园82号', '100080', 0, '公司', 0, '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ums_user_addresses` VALUES (3, 3, '王红', '13700137000', '广州市', '广州市', '天河区', '天河路200号', '510000', 1, '家', 0, '2023-01-01 00:00:00', '2023-01-01 00:00:00');

-- ----------------------------
-- Table structure for ums_user_favorites
-- ----------------------------
DROP TABLE IF EXISTS `ums_user_favorites`;
CREATE TABLE `ums_user_favorites` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `product_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商品名称',
  `product_image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商品图片',
  `product_price` decimal(10, 2) DEFAULT NULL COMMENT '商品价格',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_user_product` (`user_id`,`product_id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE,
  KEY `idx_product_id` (`product_id`) USING BTREE,
  CONSTRAINT `fk_favorite_user` FOREIGN KEY (`user_id`) REFERENCES `ums_users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户收藏表';

-- ----------------------------
-- Table structure for ums_user_search_history
-- ----------------------------
DROP TABLE IF EXISTS `ums_user_search_history`;
CREATE TABLE `ums_user_search_history` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '搜索ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `keyword` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '搜索关键词',
  `search_count` int DEFAULT 1 COMMENT '搜索次数',
  `last_search_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '最后搜索时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_user_keyword` (`user_id`,`keyword`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE,
  CONSTRAINT `fk_search_user` FOREIGN KEY (`user_id`) REFERENCES `ums_users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户搜索历史表';

-- ----------------------------
-- Table structure for ums_user_coupons
-- ----------------------------
DROP TABLE IF EXISTS `ums_user_coupons`;
CREATE TABLE `ums_user_coupons` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `coupon_id` bigint NOT NULL COMMENT '优惠券ID',
  `coupon_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '优惠券码',
  `coupon_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '优惠券名称',
  `amount` decimal(10, 2) DEFAULT NULL COMMENT '优惠金额',
  `discount` decimal(5, 2) DEFAULT NULL COMMENT '折扣',
  `min_point` decimal(10, 2) DEFAULT 0.00 COMMENT '使用门槛',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `status` tinyint DEFAULT 0 COMMENT '状态：0-未使用，1-已使用，2-已过期',
  `use_time` datetime DEFAULT NULL COMMENT '使用时间',
  `order_id` bigint DEFAULT NULL COMMENT '订单ID',
  `order_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '订单编号',
  `get_type` tinyint DEFAULT 0 COMMENT '获取类型：0-主动领取，1-后台发放，2-活动赠送',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '获取时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE,
  KEY `idx_coupon_id` (`coupon_id`) USING BTREE,
  CONSTRAINT `fk_user_coupon_user` FOREIGN KEY (`user_id`) REFERENCES `ums_users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户优惠券表';

-- ----------------------------
-- Table structure for ums_user_login_logs
-- ----------------------------
DROP TABLE IF EXISTS `ums_user_login_logs`;
CREATE TABLE `ums_user_login_logs` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `user_id` bigint DEFAULT NULL COMMENT '用户ID',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户名',
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '登录IP',
  `location` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '登录地点',
  `device` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '登录设备',
  `user_agent` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '浏览器类型',
  `login_type` tinyint DEFAULT 0 COMMENT '登录类型：0-普通登录，1-短信登录，2-第三方登录',
  `login_source` tinyint DEFAULT 0 COMMENT '登录来源：0-PC，1-APP，2-小程序，3-H5',
  `status` tinyint DEFAULT 0 COMMENT '登录状态：0-失败，1-成功',
  `message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '提示消息',
  `login_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE,
  CONSTRAINT `fk_login_log_user` FOREIGN KEY (`user_id`) REFERENCES `ums_users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户登录日志表';

-- ----------------------------
-- Table structure for ums_user_roles
-- ----------------------------
DROP TABLE IF EXISTS `ums_user_roles`;
CREATE TABLE `ums_user_roles` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `role_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色编码',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE,
  CONSTRAINT `fk_user_role_user` FOREIGN KEY (`user_id`) REFERENCES `ums_users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户角色表';

-- ----------------------------
-- Records of ums_user_roles
-- ----------------------------
INSERT INTO `ums_user_roles` VALUES (1, 1, '普通会员', 'NORMAL_MEMBER', '普通会员', '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ums_user_roles` VALUES (2, 2, '商家', 'MERCHANT', '商家角色', '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ums_user_roles` VALUES (3, 3, '普通会员', 'NORMAL_MEMBER', '普通会员', '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ums_user_roles` VALUES (4, 4, '商家', 'MERCHANT', '商家角色', '2023-01-01 00:00:00', '2023-01-01 00:00:00');

-- ----------------------------
-- Table structure for ums_notifications
-- ----------------------------
DROP TABLE IF EXISTS `ums_notifications`;
CREATE TABLE `ums_notifications` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '通知ID',
  `sender_id` bigint DEFAULT NULL COMMENT '发送者ID',
  `sender_type` tinyint DEFAULT 0 COMMENT '发送者类型：0-系统，1-用户，2-商家，3-管理员',
  `sender_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '发送者名称',
  `receiver_id` bigint NOT NULL COMMENT '接收者ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '通知标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '通知内容',
  `type` tinyint DEFAULT 0 COMMENT '通知类型：0-系统通知，1-订单通知，2-活动通知，3-物流通知，4-商家通知',
  `related_id` bigint DEFAULT NULL COMMENT '关联ID',
  `related_type` tinyint DEFAULT NULL COMMENT '关联类型：0-无，1-订单，2-商品，3-活动，4-物流',
  `read_status` tinyint DEFAULT 0 COMMENT '阅读状态：0-未读，1-已读',
  `read_time` datetime DEFAULT NULL COMMENT '阅读时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_receiver_id` (`receiver_id`) USING BTREE,
  KEY `idx_read_status` (`read_status`) USING BTREE,
  CONSTRAINT `fk_notification_user` FOREIGN KEY (`receiver_id`) REFERENCES `ums_users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户通知表';

-- ----------------------------
-- Records of ums_notifications
-- ----------------------------
INSERT INTO `ums_notifications` VALUES (1, NULL, 0, '系统通知', 1, '欢迎注册WhaleTide商城', '尊敬的用户您好，欢迎注册WhaleTide商城，感谢您的支持!', 0, NULL, NULL, 1, '2023-01-01 10:00:00', '2023-01-01 00:00:00', '2023-01-01 10:00:00');
INSERT INTO `ums_notifications` VALUES (2, NULL, 0, '系统通知', 2, '欢迎注册WhaleTide商城', '尊敬的商家您好，欢迎注册WhaleTide商城，感谢您的支持!', 0, NULL, NULL, 1, '2023-01-01 11:00:00', '2023-01-01 00:00:00', '2023-01-01 11:00:00');
INSERT INTO `ums_notifications` VALUES (3, NULL, 0, '系统通知', 3, '欢迎注册WhaleTide商城', '尊敬的用户您好，欢迎注册WhaleTide商城，感谢您的支持!', 0, NULL, NULL, 0, NULL, '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ums_notifications` VALUES (4, NULL, 0, '订单系统', 1, '订单已创建', '您的订单(订单号:WTO20230102001)已创建成功，请尽快付款。', 1, 1001, 1, 1, '2023-01-02 10:30:00', '2023-01-02 10:00:00', '2023-01-02 10:30:00');
INSERT INTO `ums_notifications` VALUES (5, 2, 2, '李大卖的小店', 1, '订单已发货', '您的订单(订单号:WTO20230102001)已发货，物流单号:SF1234567890。', 3, 1001, 1, 0, NULL, '2023-01-03 09:00:00', '2023-01-03 09:00:00');

SET FOREIGN_KEY_CHECKS = 1; 