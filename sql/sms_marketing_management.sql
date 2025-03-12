/*
 优化后的商城系统数据库设计 - 营销管理系统(SMS)模块
 
 包含以下表:
 - sms_coupons: 优惠券表
 - sms_coupon_history: 优惠券领取历史表
 - sms_coupon_products: 优惠券适用商品关联表
 - sms_coupon_categories: 优惠券适用分类关联表
 - sms_flash_promotion: 限时秒杀活动表
 - sms_flash_promotion_sessions: 限时秒杀场次表
 - sms_flash_promotion_products: 限时秒杀商品关联表
 - sms_promotions: 促销活动表
 - sms_promotion_products: 促销活动商品关联表
 - sms_promotion_logs: 促销活动参与记录表
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sms_coupons
-- ----------------------------
DROP TABLE IF EXISTS `sms_coupons`;
CREATE TABLE `sms_coupons` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '优惠券ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '优惠券名称',
  `type` tinyint NOT NULL COMMENT '优惠券类型：0-全场通用，1-指定商品，2-指定分类',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '优惠券码',
  `platform` tinyint DEFAULT 0 COMMENT '使用平台：0-全平台，1-移动端，2-PC端',
  `amount` decimal(10, 2) DEFAULT NULL COMMENT '金额',
  `discount` decimal(5, 2) DEFAULT NULL COMMENT '折扣',
  `min_point` decimal(10, 2) DEFAULT 0.00 COMMENT '使用门槛，0表示无门槛',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `total_count` int NOT NULL COMMENT '发行数量',
  `remaining_count` int DEFAULT 0 COMMENT '剩余数量',
  `receive_count` int DEFAULT 0 COMMENT '已领取数量',
  `used_count` int DEFAULT 0 COMMENT '已使用数量',
  `per_limit` int DEFAULT 1 COMMENT '每人限领张数',
  `use_type` tinyint DEFAULT 0 COMMENT '使用类型：0-通用，1-会员专享，2-新人专享',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `publish_status` tinyint DEFAULT 0 COMMENT '发布状态：0-未发布，1-已发布',
  `is_deleted` tinyint DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_start_time` (`start_time`) USING BTREE,
  KEY `idx_end_time` (`end_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='优惠券表';

-- ----------------------------
-- Table structure for sms_coupon_history
-- ----------------------------
DROP TABLE IF EXISTS `sms_coupon_history`;
CREATE TABLE `sms_coupon_history` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `coupon_id` bigint NOT NULL COMMENT '优惠券ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '领取码',
  `order_id` bigint DEFAULT NULL COMMENT '关联订单ID',
  `order_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '订单编号',
  `get_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '领取时间',
  `use_time` datetime DEFAULT NULL COMMENT '使用时间',
  `source_type` tinyint DEFAULT 0 COMMENT '获取来源：0-主动领取，1-后台赠送，2-活动赠送',
  `status` tinyint DEFAULT 0 COMMENT '状态：0-未使用，1-已使用，2-已过期',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_coupon_id` (`coupon_id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE,
  KEY `idx_order_id` (`order_id`) USING BTREE,
  CONSTRAINT `fk_history_coupon` FOREIGN KEY (`coupon_id`) REFERENCES `sms_coupons` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='优惠券领取历史表';

-- ----------------------------
-- Table structure for sms_coupon_products
-- ----------------------------
DROP TABLE IF EXISTS `sms_coupon_products`;
CREATE TABLE `sms_coupon_products` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `coupon_id` bigint NOT NULL COMMENT '优惠券ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `product_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商品名称',
  `product_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商品编号',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_coupon_id` (`coupon_id`) USING BTREE,
  KEY `idx_product_id` (`product_id`) USING BTREE,
  CONSTRAINT `fk_product_coupon` FOREIGN KEY (`coupon_id`) REFERENCES `sms_coupons` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='优惠券适用商品关联表';

-- ----------------------------
-- Table structure for sms_coupon_categories
-- ----------------------------
DROP TABLE IF EXISTS `sms_coupon_categories`;
CREATE TABLE `sms_coupon_categories` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `coupon_id` bigint NOT NULL COMMENT '优惠券ID',
  `category_id` bigint NOT NULL COMMENT '分类ID',
  `category_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '分类名称',
  `parent_category_id` bigint DEFAULT NULL COMMENT '父分类ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_coupon_id` (`coupon_id`) USING BTREE,
  KEY `idx_category_id` (`category_id`) USING BTREE,
  CONSTRAINT `fk_category_coupon` FOREIGN KEY (`coupon_id`) REFERENCES `sms_coupons` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='优惠券适用分类关联表';

-- ----------------------------
-- Table structure for sms_flash_promotion
-- ----------------------------
DROP TABLE IF EXISTS `sms_flash_promotion`;
CREATE TABLE `sms_flash_promotion` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '秒杀ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '活动标题',
  `start_date` date NOT NULL COMMENT '开始日期',
  `end_date` date NOT NULL COMMENT '结束日期',
  `status` tinyint DEFAULT 0 COMMENT '上下线状态：0-下线，1-上线',
  `create_admin` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建者',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '活动描述',
  `icon_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '活动图标',
  `is_deleted` tinyint DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_start_date` (`start_date`) USING BTREE,
  KEY `idx_end_date` (`end_date`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='限时秒杀活动表';

-- ----------------------------
-- Table structure for sms_flash_promotion_sessions
-- ----------------------------
DROP TABLE IF EXISTS `sms_flash_promotion_sessions`;
CREATE TABLE `sms_flash_promotion_sessions` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '场次ID',
  `flash_promotion_id` bigint NOT NULL COMMENT '秒杀活动ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '场次名称',
  `start_time` time NOT NULL COMMENT '每日开始时间',
  `end_time` time NOT NULL COMMENT '每日结束时间',
  `status` tinyint DEFAULT 0 COMMENT '启用状态：0-禁用，1-启用',
  `order_sort` int DEFAULT 0 COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_flash_promotion_id` (`flash_promotion_id`) USING BTREE,
  CONSTRAINT `fk_session_flash` FOREIGN KEY (`flash_promotion_id`) REFERENCES `sms_flash_promotion` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='限时秒杀场次表';

-- ----------------------------
-- Table structure for sms_flash_promotion_products
-- ----------------------------
DROP TABLE IF EXISTS `sms_flash_promotion_products`;
CREATE TABLE `sms_flash_promotion_products` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `flash_promotion_id` bigint NOT NULL COMMENT '秒杀活动ID',
  `flash_session_id` bigint NOT NULL COMMENT '秒杀场次ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `product_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商品名称',
  `flash_price` decimal(10, 2) NOT NULL COMMENT '秒杀价格',
  `original_price` decimal(10, 2) DEFAULT NULL COMMENT '原价',
  `flash_stock` int NOT NULL COMMENT '秒杀库存',
  `current_stock` int DEFAULT NULL COMMENT '当前库存',
  `sold_count` int DEFAULT 0 COMMENT '已售数量',
  `limit_count` int DEFAULT 1 COMMENT '每人限购数量',
  `sort` int DEFAULT 0 COMMENT '排序',
  `status` tinyint DEFAULT 0 COMMENT '上架状态：0-下架，1-上架',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_flash_promotion_id` (`flash_promotion_id`) USING BTREE,
  KEY `idx_flash_session_id` (`flash_session_id`) USING BTREE,
  KEY `idx_product_id` (`product_id`) USING BTREE,
  CONSTRAINT `fk_product_flash` FOREIGN KEY (`flash_promotion_id`) REFERENCES `sms_flash_promotion` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_product_session` FOREIGN KEY (`flash_session_id`) REFERENCES `sms_flash_promotion_sessions` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='限时秒杀商品关联表';

-- ----------------------------
-- Table structure for sms_promotions
-- ----------------------------
DROP TABLE IF EXISTS `sms_promotions`;
CREATE TABLE `sms_promotions` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '促销活动ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '活动名称',
  `type` tinyint NOT NULL COMMENT '活动类型：0-满减，1-满折，2-特价活动，3-团购活动，4-赠品活动',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `status` tinyint DEFAULT 0 COMMENT '活动状态：0-未开始，1-进行中，2-已结束，3-已取消',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '活动描述',
  `banner_image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '活动横幅图',
  `rules` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '活动规则，JSON格式',
  `target_type` tinyint DEFAULT 0 COMMENT '目标类型：0-全场，1-指定商品，2-指定分类',
  `platform` tinyint DEFAULT 0 COMMENT '使用平台：0-全平台，1-移动端，2-PC端',
  `priority` int DEFAULT 0 COMMENT '优先级',
  `create_admin` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建者',
  `is_deleted` tinyint DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_start_time` (`start_time`) USING BTREE,
  KEY `idx_end_time` (`end_time`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='促销活动表';

-- ----------------------------
-- Table structure for sms_promotion_products
-- ----------------------------
DROP TABLE IF EXISTS `sms_promotion_products`;
CREATE TABLE `sms_promotion_products` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `promotion_id` bigint NOT NULL COMMENT '促销活动ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `product_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商品名称',
  `promotion_price` decimal(10, 2) DEFAULT NULL COMMENT '促销价格',
  `promotion_stock` int DEFAULT NULL COMMENT '促销库存',
  `promotion_limit` int DEFAULT NULL COMMENT '促销限购',
  `sort` int DEFAULT 0 COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_promotion_id` (`promotion_id`) USING BTREE,
  KEY `idx_product_id` (`product_id`) USING BTREE,
  CONSTRAINT `fk_product_promotion` FOREIGN KEY (`promotion_id`) REFERENCES `sms_promotions` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='促销活动商品关联表';

-- ----------------------------
-- Table structure for sms_promotion_logs
-- ----------------------------
DROP TABLE IF EXISTS `sms_promotion_logs`;
CREATE TABLE `sms_promotion_logs` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `promotion_id` bigint NOT NULL COMMENT '促销活动ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `product_id` bigint DEFAULT NULL COMMENT '商品ID',
  `order_id` bigint DEFAULT NULL COMMENT '订单ID',
  `order_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '订单编号',
  `participation_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '参与时间',
  `participation_type` tinyint DEFAULT 0 COMMENT '参与类型：0-浏览，1-参与，2-购买',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_promotion_id` (`promotion_id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE,
  KEY `idx_order_id` (`order_id`) USING BTREE,
  CONSTRAINT `fk_log_promotion` FOREIGN KEY (`promotion_id`) REFERENCES `sms_promotions` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='促销活动参与记录表';

SET FOREIGN_KEY_CHECKS = 1; 