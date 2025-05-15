/*
 Navicat Premium Dump SQL

 Source Server         : mysql_3306
 Source Server Type    : MySQL
 Source Server Version : 80039 (8.0.39)
 Source Host           : localhost:3306
 Source Schema         : whale_tide_db

 Target Server Type    : MySQL
 Target Server Version : 80039 (8.0.39)
 File Encoding         : 65001

 Date: 15/05/2025 19:19:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ams_admin_role_relations
-- ----------------------------
DROP TABLE IF EXISTS `ams_admin_role_relations`;
CREATE TABLE `ams_admin_role_relations`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `admin_id` bigint NOT NULL COMMENT '管理员ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_admin_id`(`admin_id` ASC) USING BTREE,
  INDEX `idx_role_id`(`role_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员角色关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ams_admin_role_relations
-- ----------------------------

-- ----------------------------
-- Table structure for ams_admins
-- ----------------------------
DROP TABLE IF EXISTS `ams_admins`;
CREATE TABLE `ams_admins`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `email` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `real_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ams_admins
-- ----------------------------
INSERT INTO `ams_admins` VALUES (1, 'admin', '123456', NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for ams_roles
-- ----------------------------
DROP TABLE IF EXISTS `ams_roles`;
CREATE TABLE `ams_roles`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色描述',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ams_roles
-- ----------------------------

-- ----------------------------
-- Table structure for oms_cart_items
-- ----------------------------
DROP TABLE IF EXISTS `oms_cart_items`;
CREATE TABLE `oms_cart_items`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '购物项ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `product_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品名称',
  `product_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品图片',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品价格',
  `quantity` int NULL DEFAULT NULL COMMENT '购买数量',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `checked` tinyint NULL DEFAULT 1 COMMENT '是否选中：0-未选中，1-已选中',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_product_id`(`product_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '购物车表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oms_cart_items
-- ----------------------------
INSERT INTO `oms_cart_items` VALUES (11, 10, '碳纤维潜水蛙鞋', '/images/products/dive-fins.png', 149.99, 2, 1, 1, '2025-05-14 19:07:13', '2025-05-14 19:53:26');
INSERT INTO `oms_cart_items` VALUES (13, 6, '专业潜水调节器', '/images/products/diving-regulator.png', 299.99, 2, 1, 1, '2025-05-15 11:55:02', '2025-05-15 11:57:59');

-- ----------------------------
-- Table structure for oms_order_deliveries
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_deliveries`;
CREATE TABLE `oms_order_deliveries`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物流ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `order_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `delivery_company` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '物流公司',
  `delivery_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '物流单号',
  `receiver_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人',
  `receiver_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人电话',
  `receiver_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货地址',
  `delivery_status` tinyint NULL DEFAULT 0 COMMENT '物流状态：0-运输中，1-已签收',
  `delivery_time` datetime NULL DEFAULT NULL COMMENT '发货时间',
  `receive_time` datetime NULL DEFAULT NULL COMMENT '签收时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单物流表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oms_order_deliveries
-- ----------------------------

-- ----------------------------
-- Table structure for oms_order_items
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_items`;
CREATE TABLE `oms_order_items`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单项ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `order_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `product_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称',
  `product_pic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品图片',
  `product_price` decimal(10, 2) NOT NULL COMMENT '商品单价',
  `product_quantity` int NOT NULL COMMENT '购买数量',
  `product_category_id` bigint NULL DEFAULT NULL COMMENT '商品分类ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE,
  INDEX `idx_product_id`(`product_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单项表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oms_order_items
-- ----------------------------
INSERT INTO `oms_order_items` VALUES (1, 1, 'WT17472174251317697', 7, '潜水电脑表', '/images/products/dive-computer.png', 459.99, 1, 6);
INSERT INTO `oms_order_items` VALUES (2, 2, 'WT17472186647801293', 7, '潜水电脑表', '/images/products/dive-computer.png', 459.99, 1, 6);
INSERT INTO `oms_order_items` VALUES (3, 3, 'WT17472304484807713', 18, '水下相机', '/images/products/underwater-camera.png', 299.99, 1, 6);
INSERT INTO `oms_order_items` VALUES (4, 4, 'WT17472814291762227', 5, '船用急救箱', '/images/products/marine-first-aid.png', 45.99, 2, 5);

-- ----------------------------
-- Table structure for oms_order_returns
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_returns`;
CREATE TABLE `oms_order_returns`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '退货ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `order_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `return_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '退款金额',
  `return_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退货人姓名',
  `return_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退货人电话',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '申请状态：0-待处理，1-已退货，2-已拒绝',
  `handle_time` datetime NULL DEFAULT NULL COMMENT '处理时间',
  `product_pic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品图片',
  `product_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品名称',
  `product_id` bigint NULL DEFAULT NULL COMMENT '商品ID',
  `product_count` int NULL DEFAULT NULL COMMENT '商品数量',
  `reason` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退货原因',
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '问题描述',
  `proof_pics` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '凭证图片',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单退货表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oms_order_returns
-- ----------------------------

-- ----------------------------
-- Table structure for oms_orders
-- ----------------------------
DROP TABLE IF EXISTS `oms_orders`;
CREATE TABLE `oms_orders`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
  `total_amount` decimal(10, 2) NOT NULL COMMENT '订单总金额',
  `pay_amount` decimal(10, 2) NOT NULL COMMENT '应付金额',
  `freight_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '运费金额',
  `discount_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠金额',
  `pay_type` tinyint NULL DEFAULT NULL COMMENT '支付方式：1-支付宝，2-微信',
  `source_type` tinyint NULL DEFAULT NULL COMMENT '订单来源：1-APP，2-网页',
  `status` tinyint NOT NULL COMMENT '订单状态：0-待付款，1-待发货，2-待收货，3-已完成，4-已取消，5-已关闭',
  `order_type` tinyint NULL DEFAULT 0 COMMENT '订单类型：0-普通订单，1-秒杀订单',
  `delivery_company` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '物流公司',
  `delivery_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '物流单号',
  `auto_confirm_day` int NULL DEFAULT NULL COMMENT '自动确认收货时间（天）',
  `receiver_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人电话',
  `receiver_province` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收货人省份',
  `receiver_city` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收货人城市',
  `receiver_region` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收货人区县',
  `receiver_detail_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收货人详细地址',
  `note` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订单备注',
  `confirm_status` tinyint NULL DEFAULT 0 COMMENT '确认收货状态：0-未确认，1-已确认',
  `delete_status` tinyint NULL DEFAULT 0 COMMENT '删除状态：0-未删除，1-已删除',
  `payment_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `delivery_time` datetime NULL DEFAULT NULL COMMENT '发货时间',
  `receive_time` datetime NULL DEFAULT NULL COMMENT '确认收货时间',
  `comment_time` datetime NULL DEFAULT NULL COMMENT '评价时间',
  `cancel_time` datetime NULL DEFAULT NULL COMMENT '取消订单时间',
  `cancel_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '取消订单原因',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_order_sn`(`order_sn` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oms_orders
-- ----------------------------
INSERT INTO `oms_orders` VALUES (1, 'WT17472174251317697', 1, 'test', 459.99, 459.99, 0.00, 0.00, 2, 1, 0, 0, NULL, NULL, NULL, 'test', '19391629123', '四川省', '成都市', '武侯区', '武侯祠大街', '尽快发货', 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, '2025-05-14 18:10:25', NULL);
INSERT INTO `oms_orders` VALUES (2, 'WT17472186647801293', 1, 'test', 459.99, 459.99, 0.00, 0.00, 1, 1, 2, 0, NULL, NULL, NULL, 'test', '19391629123', '四川省', '成都市', '武侯区', '武侯祠大街', '尽快发货', 0, 0, '2025-05-14 21:46:39', NULL, NULL, NULL, NULL, NULL, '2025-05-14 18:31:05', NULL);
INSERT INTO `oms_orders` VALUES (3, 'WT17472304484807713', 1, 'test', 299.99, 299.99, 0.00, 0.00, 1, 1, 1, 0, NULL, NULL, NULL, '张三', '123', '陕西省', '汉中市', '汉台区', '陕西理工大学', '尽快发货', 0, 0, '2025-05-14 21:47:31', NULL, NULL, NULL, NULL, NULL, '2025-05-14 21:47:28', NULL);
INSERT INTO `oms_orders` VALUES (4, 'WT17472814291762227', 1, 'test', 91.98, 91.98, 0.00, 0.00, 2, 1, 1, 0, NULL, NULL, NULL, 'test', '19391629123', '四川省', '成都市', '武侯区', '武侯祠大街', '尽快发货', 0, 0, '2025-05-15 11:57:11', NULL, NULL, NULL, NULL, NULL, '2025-05-15 11:57:09', NULL);

-- ----------------------------
-- Table structure for oms_payments
-- ----------------------------
DROP TABLE IF EXISTS `oms_payments`;
CREATE TABLE `oms_payments`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '支付ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `order_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `pay_amount` decimal(10, 2) NOT NULL COMMENT '支付金额',
  `pay_type` tinyint NOT NULL COMMENT '支付类型：1-支付宝，2-微信',
  `pay_status` tinyint NOT NULL COMMENT '支付状态：0-待支付，1-已支付，2-已退款',
  `trade_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '交易号',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '支付信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oms_payments
-- ----------------------------

-- ----------------------------
-- Table structure for pms_brands
-- ----------------------------
DROP TABLE IF EXISTS `pms_brands`;
CREATE TABLE `pms_brands`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '品牌ID',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '品牌名称',
  `logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '品牌logo',
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '品牌描述',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  `is_featured` tinyint NULL DEFAULT 0 COMMENT '是否为品牌制造商：0-不是，1-是',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '品牌表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pms_brands
-- ----------------------------
INSERT INTO `pms_brands` VALUES (1, '海洋之风', '/images/brands/ocean-breeze.png', '高品质海洋设备和配件', 1, 1, 1, '2025-05-13 19:49:00', NULL);
INSERT INTO `pms_brands` VALUES (2, '深蓝潜水', '/images/brands/deep-blue.png', '优质潜水和水下设备', 1, 2, 1, '2025-05-13 19:49:00', NULL);
INSERT INTO `pms_brands` VALUES (3, '海岸生活', '/images/brands/coastal-living.png', '海滩和沿海地区的生活方式产品', 1, 3, 1, '2025-05-13 19:49:00', NULL);
INSERT INTO `pms_brands` VALUES (4, '海洋科技', '/images/brands/marine-tech.png', '先进的海洋应用技术', 1, 4, 1, '2025-05-13 19:49:00', NULL);
INSERT INTO `pms_brands` VALUES (5, '海洋必备', '/images/brands/sea-essentials.png', '海洋活动必需品', 1, 5, 1, '2025-05-13 19:49:00', NULL);
INSERT INTO `pms_brands` VALUES (6, '航海装备', '/images/brands/nautical-gear.png', '专业航海设备和装备', 1, 6, 1, '2025-05-13 19:49:00', NULL);
INSERT INTO `pms_brands` VALUES (7, '水世界', '/images/brands/aqua-marine.png', '专业水上产品和解决方案', 1, 7, 1, '2025-05-13 19:49:00', NULL);
INSERT INTO `pms_brands` VALUES (8, '乘风浪', '/images/brands/wave-rider.png', '冲浪和水上运动设备', 1, 8, 1, '2025-05-13 19:49:00', NULL);

-- ----------------------------
-- Table structure for pms_product_categories
-- ----------------------------
DROP TABLE IF EXISTS `pms_product_categories`;
CREATE TABLE `pms_product_categories`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '上级分类ID，0表示一级分类',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
  `level` int NULL DEFAULT NULL COMMENT '分类级别：0->1级；1->2级',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图标',
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `is_nav` tinyint NULL DEFAULT 0 COMMENT '是否显示在导航栏：0-不显示，1-显示',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pms_product_categories
-- ----------------------------
INSERT INTO `pms_product_categories` VALUES (1, 0, '海洋设备', 0, '/images/categories/marine-equipment.png', '专业海洋和航海设备', 1, 1, 1, '2025-05-13 19:49:00', NULL);
INSERT INTO `pms_product_categories` VALUES (2, 0, '潜水装备', 0, '/images/categories/diving-gear.png', '水下活动设备', 2, 1, 1, '2025-05-13 19:49:00', NULL);
INSERT INTO `pms_product_categories` VALUES (3, 0, '海滩用品', 0, '/images/categories/beach-accessories.png', '海滩和沿海活动产品', 3, 1, 1, '2025-05-13 19:49:00', NULL);
INSERT INTO `pms_product_categories` VALUES (4, 1, '导航工具', 1, '/images/categories/navigation-tools.png', '海洋导航工具', 1, 1, 0, '2025-05-13 19:49:00', NULL);
INSERT INTO `pms_product_categories` VALUES (5, 1, '船只配件', 1, '/images/categories/boat-accessories.png', '船只和船舶配件', 2, 1, 0, '2025-05-13 19:49:00', NULL);
INSERT INTO `pms_product_categories` VALUES (6, 2, '水肺装备', 1, '/images/categories/scuba-equipment.png', '专业水肺潜水设备', 1, 1, 0, '2025-05-13 19:49:00', NULL);
INSERT INTO `pms_product_categories` VALUES (7, 2, '浮潜装备', 1, '/images/categories/snorkeling-gear.png', '浮潜活动装备', 2, 1, 0, '2025-05-13 19:49:00', NULL);
INSERT INTO `pms_product_categories` VALUES (8, 3, '海滩家具', 1, '/images/categories/beach-furniture.png', '海滩和户外使用的家具', 1, 1, 0, '2025-05-13 19:49:00', NULL);
INSERT INTO `pms_product_categories` VALUES (9, 3, '防晒用品', 1, '/images/categories/sun-protection.png', '海滩防晒产品', 2, 1, 0, '2025-05-13 19:49:00', NULL);

-- ----------------------------
-- Table structure for pms_product_comments
-- ----------------------------
DROP TABLE IF EXISTS `pms_product_comments`;
CREATE TABLE `pms_product_comments`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
  `user_avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户头像',
  `content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评论内容',
  `star` tinyint NOT NULL DEFAULT 5 COMMENT '评分，1-5星',
  `pics` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '评论图片',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：0-未显示，1-已显示',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_product_id`(`product_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pms_product_comments
-- ----------------------------
INSERT INTO `pms_product_comments` VALUES (1, 1, 1, '海洋探险家', '/images/avatars/user1.png', '优秀的指南针。非常准确且制作精良。完美适合我的小船。', 5, NULL, 1, '2025-05-13 19:49:00');
INSERT INTO `pms_product_comments` VALUES (2, 1, 2, '航海珍妮', '/images/avatars/user2.png', '合理价格的高品质产品。容易安装和读取。', 4, NULL, 1, '2025-05-13 19:49:00');
INSERT INTO `pms_product_comments` VALUES (3, 2, 3, '船长史密斯', '/images/avatars/user3.png', '这款GPS导航仪超出了我的期望。界面直观，地图非常详细。', 5, NULL, 1, '2025-05-13 19:49:00');
INSERT INTO `pms_product_comments` VALUES (4, 3, 4, '海洋爱好者', '/images/avatars/user4.png', '这款双筒望远镜非常适合海上使用。内置指南针是一个很棒的功能。', 5, NULL, 1, '2025-05-13 19:49:00');
INSERT INTO `pms_product_comments` VALUES (5, 4, 5, '船只大师', '/images/avatars/user5.png', '非常耐用的护舷。在几次靠岸中很好地保护了我的船。', 4, NULL, 1, '2025-05-13 19:49:00');
INSERT INTO `pms_product_comments` VALUES (6, 6, 6, '潜水专家迈克', '/images/avatars/user6.png', '专业品质的调节器。在所有深度下呼吸顺畅。强烈推荐给严肃的潜水员。', 5, NULL, 1, '2025-05-13 19:49:00');
INSERT INTO `pms_product_comments` VALUES (7, 7, 7, '深海潜水员', '/images/avatars/user7.png', '我拥有过的最好的潜水电脑。直观的界面和蓝牙连接是一个改变游戏规则的因素。', 5, NULL, 1, '2025-05-13 19:49:00');
INSERT INTO `pms_product_comments` VALUES (8, 9, 8, '海滩探险家', '/images/avatars/user8.png', '令人惊叹的全脸浮潜面镜！全景视野在水下带来巨大差异。', 5, NULL, 1, '2025-05-13 19:49:00');
INSERT INTO `pms_product_comments` VALUES (9, 11, 9, '阳光追求者', '/images/avatars/user9.png', '非常舒适的海滩椅。容易折叠和携带。性价比很高。', 4, NULL, 1, '2025-05-13 19:49:00');
INSERT INTO `pms_product_comments` VALUES (10, 14, 10, '生态旅行者', '/images/avatars/user10.png', '优秀的防晒霜，真正有效且不伤害环境。会再次购买！', 5, NULL, 1, '2025-05-13 19:49:00');

-- ----------------------------
-- Table structure for pms_products
-- ----------------------------
DROP TABLE IF EXISTS `pms_products`;
CREATE TABLE `pms_products`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称',
  `category_id` bigint NULL DEFAULT NULL COMMENT '分类ID',
  `brand_id` bigint NULL DEFAULT NULL COMMENT '品牌ID',
  `price` decimal(10, 2) NOT NULL COMMENT '商品价格',
  `main_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '主图URL',
  `brief` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '简短描述',
  `stock` int NULL DEFAULT 0 COMMENT '库存',
  `publish_status` tinyint NULL DEFAULT 0 COMMENT '上架状态：0-下架，1-上架',
  `new_status` tinyint NULL DEFAULT 0 COMMENT '新品状态：0-非新品，1-新品',
  `recommend_status` tinyint NULL DEFAULT 0 COMMENT '推荐状态：0-不推荐，1-推荐',
  `product_images` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品图片(副图)',
  `product_details` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '商品详情',
  `product_rating` decimal(3, 1) NULL DEFAULT 5.0 COMMENT '商品评分',
  `product_status` tinyint NULL DEFAULT 0 COMMENT '商品状态：0-下架，1-上架，2-删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_category_id`(`category_id` ASC) USING BTREE,
  INDEX `idx_brand_id`(`brand_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pms_products
-- ----------------------------
INSERT INTO `pms_products` VALUES (1, '专业航海指南针', 4, 1, 129.99, '/images/products/zhinanzhen.png', '高精度航海导航指南针', 100, 1, 0, 0, '/images/products/marine-compass-1.png,/images/products/marine-compass-2.png', '<p>专业航海指南针，具有高精度和耐用性。适合所有海上导航需求。</p><p>特点包括防雾玻璃、夜间照明和精确方位。</p>', 4.7, 1, '2025-05-13 19:49:00', NULL);
INSERT INTO `pms_products` VALUES (2, '先进GPS导航仪', 4, 4, 349.99, '/images/products/gps-navigator.png', '用于海洋的先进GPS导航系统', 50, 1, 1, 0, '/images/products/gps-navigator-1.png,/images/products/gps-navigator-2.png', '<p>专为海洋环境设计的最先进GPS导航系统。防水耐用，配备高分辨率显示屏。</p>', 4.8, 1, '2025-05-13 19:49:00', NULL);
INSERT INTO `pms_products` VALUES (3, '航海双筒望远镜', 4, 1, 199.99, '/images/products/marine-binoculars.png', '防水航海望远镜', 75, 1, 0, 0, '/images/products/marine-binoculars-1.png,/images/products/marine-binoculars-2.png', '<p>7x50防水双筒望远镜，配有指南针和测距仪。配备防雾镜片，是海上使用的理想选择。</p>', 4.5, 1, '2025-05-13 19:49:00', NULL);
INSERT INTO `pms_products` VALUES (4, '高级船舶护舷', 5, 1, 89.99, '/images/products/boat-fenders.png', '高质量船舶保护护舷', 200, 1, 0, 0, '/images/products/boat-fenders-1.png,/images/products/boat-fenders-2.png', '<p>优质船用护舷，设计用于保护您的船只。防紫外线和耐用。</p>', 4.6, 1, '2025-05-13 19:49:00', NULL);
INSERT INTO `pms_products` VALUES (5, '船用急救箱', 5, 5, 45.99, '/images/products/marine-first-aid.png', '全面的海上急救箱', 148, 1, 0, 1, '/images/products/marine-first-aid-1.png,/images/products/marine-first-aid-2.png', '<p>完整的防水急救箱，专为海上紧急情况设计。包含所有必要的物品。</p>', 4.9, 1, '2025-05-13 19:49:00', NULL);
INSERT INTO `pms_products` VALUES (6, '专业潜水调节器', 6, 2, 299.99, '/images/products/diving-regulator.png', '高性能潜水调节器', 30, 1, 0, 1, '/images/products/diving-regulator-1.png,/images/products/diving-regulator-2.png', '<p>专业级潜水调节器，配有平衡隔膜一级头和可调节二级头。</p>', 4.8, 1, '2025-05-13 19:49:00', NULL);
INSERT INTO `pms_products` VALUES (7, '潜水电脑表', 6, 2, 459.99, '/images/products/dive-computer.png', '多模式先进潜水电脑', 23, 1, 1, 1, '/images/products/dive-computer-1.png,/images/products/dive-computer-2.png', '<p>功能丰富的潜水电脑，具有空气、高氧和仪表模式。包括数字指南针和蓝牙连接。</p>', 4.9, 1, '2025-05-13 19:49:00', NULL);
INSERT INTO `pms_products` VALUES (8, '高级潜水面镜', 6, 2, 79.99, '/images/products/diving-mask.png', '超清晰镜片潜水面镜', 100, 1, 0, 0, '/images/products/diving-mask-1.png,/images/products/diving-mask-2.png', '<p>专业潜水面镜，配备钢化玻璃镜片和硅胶裙边，提供完美密封和舒适感。</p>', 4.7, 1, '2025-05-13 19:49:00', NULL);
INSERT INTO `pms_products` VALUES (9, '全脸浮潜面镜', 7, 8, 69.99, '/images/products/full-face-snorkel.png', '全景视野浮潜面镜', 120, 1, 1, 0, '/images/products/full-face-snorkel-1.png,/images/products/full-face-snorkel-2.png', '<p>创新的全脸浮潜面镜，具有180°全景视野和防雾技术。</p>', 4.5, 1, '2025-05-13 19:49:00', NULL);
INSERT INTO `pms_products` VALUES (10, '碳纤维潜水蛙鞋', 7, 2, 149.99, '/images/products/dive-fins.png', '轻巧强力潜水蛙鞋', 60, 1, 1, 0, '/images/products/dive-fins-1.png,/images/products/dive-fins-2.png', '<p>高性能碳纤维潜水蛙鞋，配有可调节带子和高效叶片设计。</p>', 4.6, 1, '2025-05-13 19:49:00', NULL);
INSERT INTO `pms_products` VALUES (11, '折叠海滩椅', 8, 3, 59.99, '/images/products/beach-chair.png', '舒适便携海滩椅', 200, 1, 0, 0, '/images/products/beach-chair-1.png,/images/products/beach-chair-2.png', '<p>轻巧耐用的海滩椅，具有可调节位置和内置杯架。</p>', 4.4, 1, '2025-05-13 19:49:00', NULL);
INSERT INTO `pms_products` VALUES (12, '带沙锚的海滩伞', 8, 3, 49.99, '/images/products/beach-umbrella.png', '防紫外线海滩伞', 150, 1, 0, 1, '/images/products/beach-umbrella-1.png,/images/products/beach-umbrella-2.png', '<p>大型海滩伞，具有UPF 50+防护和安全沙锚系统。</p>', 4.3, 1, '2025-05-13 19:49:00', NULL);
INSERT INTO `pms_products` VALUES (13, '防水海滩垫', 8, 3, 29.99, '/images/products/beach-blanket.png', '防沙防水海滩垫', 300, 1, 0, 0, '/images/products/beach-blanket-1.png,/images/products/beach-blanket-2.png', '<p>超大号海滩垫，由速干防沙材料制成，带有角落锚点。</p>', 4.5, 1, '2025-05-13 19:49:00', NULL);
INSERT INTO `pms_products` VALUES (14, '珊瑚友好防晒霜SPF50', 9, 5, 24.99, '/images/products/reef-sunscreen.png', '环保高防护防晒霜', 400, 1, 0, 0, '/images/products/reef-sunscreen-1.png,/images/products/reef-sunscreen-2.png', '<p>环保防晒霜，提供卓越的防护，同时不会伤害珊瑚礁。</p>', 4.7, 1, '2025-05-13 19:49:00', NULL);
INSERT INTO `pms_products` VALUES (15, '海滩遮阳帐篷', 9, 3, 79.99, '/images/products/sun-shelter.png', '便携式海滩防晒帐篷', 100, 1, 1, 1, '/images/products/sun-shelter-1.png,/images/products/sun-shelter-2.png', '<p>易于设置的弹出式遮阳帐篷，具有UPF 50+防护和通风窗口。</p>', 4.6, 1, '2025-05-13 19:49:00', NULL);
INSERT INTO `pms_products` VALUES (16, '海事无线电', 4, 4, 129.99, '/images/products/marine-radio.png', '防水漂浮海事无线电', 80, 1, 0, 0, '/images/products/marine-radio-1.png,/images/products/marine-radio-2.png', '<p>漂浮VHF海事无线电，配有NOAA气象频道和紧急功能。</p>', 4.5, 1, '2025-05-13 19:49:00', NULL);
INSERT INTO `pms_products` VALUES (17, '船舶清洁套件', 5, 5, 59.99, '/images/products/boat-cleaning.png', '完整的船舶清洁和维护套件', 120, 1, 0, 0, '/images/products/boat-cleaning-1.png,/images/products/boat-cleaning-2.png', '<p>全面的船舶清洁套件，配有专业清洁剂、蜡和工具。</p>', 4.4, 1, '2025-05-13 19:49:00', NULL);
INSERT INTO `pms_products` VALUES (18, '水下相机', 6, 4, 299.99, '/images/products/underwater-camera.png', '专业水下数码相机', 39, 1, 1, 1, '/images/products/underwater-camera-1.png,/images/products/underwater-camera-2.png', '<p>高分辨率水下相机，具有4K视频功能和防水外壳，深度可达60米。</p>', 4.8, 1, '2025-05-13 19:49:00', NULL);
INSERT INTO `pms_products` VALUES (19, '浮潜套装', 7, 8, 49.99, '/images/products/snorkel-set.png', '完整面镜、呼吸管和蛙鞋套装', 150, 1, 0, 0, '/images/products/snorkel-set-1.png,/images/products/snorkel-set-2.png', '<p>全合一浮潜套装，包括可调节面镜、干式呼吸管和便携式旅行蛙鞋。</p>', 4.5, 1, '2025-05-13 19:49:00', NULL);
INSERT INTO `pms_products` VALUES (20, '沙滩推车', 8, 3, 89.99, '/images/products/beach-cart.png', '全地形沙滩拖车', 70, 1, 0, 1, '/images/products/beach-cart-1.png,/images/products/beach-cart-2.png', '<p>重型沙滩推车，配有大轮子，方便在沙滩上运输海滩装备。</p>', 4.6, 1, '2025-05-13 19:49:00', NULL);

-- ----------------------------
-- Table structure for pms_ratings
-- ----------------------------
DROP TABLE IF EXISTS `pms_ratings`;
CREATE TABLE `pms_ratings`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '评分ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `total_count` int NULL DEFAULT 0 COMMENT '总评分人数',
  `rating_5` int NULL DEFAULT 0 COMMENT '5星数',
  `rating_4` int NULL DEFAULT 0 COMMENT '4星数',
  `rating_3` int NULL DEFAULT 0 COMMENT '3星数',
  `rating_2` int NULL DEFAULT 0 COMMENT '2星数',
  `rating_1` int NULL DEFAULT 0 COMMENT '1星数',
  `average_rating` decimal(3, 1) NULL DEFAULT 5.0 COMMENT '平均评分',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_product_id`(`product_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品评分表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pms_ratings
-- ----------------------------
INSERT INTO `pms_ratings` VALUES (1, 1, 120, 90, 20, 5, 3, 2, 4.7, '2025-05-13 19:49:00');
INSERT INTO `pms_ratings` VALUES (2, 2, 85, 70, 10, 5, 0, 0, 4.8, '2025-05-13 19:49:00');
INSERT INTO `pms_ratings` VALUES (3, 3, 95, 60, 25, 8, 2, 0, 4.5, '2025-05-13 19:49:00');
INSERT INTO `pms_ratings` VALUES (4, 4, 110, 75, 25, 7, 2, 1, 4.6, '2025-05-13 19:49:00');
INSERT INTO `pms_ratings` VALUES (5, 5, 75, 70, 5, 0, 0, 0, 4.9, '2025-05-13 19:49:00');
INSERT INTO `pms_ratings` VALUES (6, 6, 65, 55, 7, 3, 0, 0, 4.8, '2025-05-13 19:49:00');
INSERT INTO `pms_ratings` VALUES (7, 7, 50, 45, 5, 0, 0, 0, 4.9, '2025-05-13 19:49:00');
INSERT INTO `pms_ratings` VALUES (8, 8, 130, 100, 20, 8, 2, 0, 4.7, '2025-05-13 19:49:00');
INSERT INTO `pms_ratings` VALUES (9, 9, 95, 60, 25, 8, 2, 0, 4.5, '2025-05-13 19:49:00');
INSERT INTO `pms_ratings` VALUES (10, 10, 80, 55, 20, 3, 2, 0, 4.6, '2025-05-13 19:49:00');
INSERT INTO `pms_ratings` VALUES (11, 11, 150, 80, 40, 20, 10, 0, 4.4, '2025-05-13 19:49:00');
INSERT INTO `pms_ratings` VALUES (12, 12, 125, 60, 35, 25, 5, 0, 4.3, '2025-05-13 19:49:00');
INSERT INTO `pms_ratings` VALUES (13, 13, 110, 70, 30, 5, 5, 0, 4.5, '2025-05-13 19:49:00');
INSERT INTO `pms_ratings` VALUES (14, 14, 90, 65, 20, 5, 0, 0, 4.7, '2025-05-13 19:49:00');
INSERT INTO `pms_ratings` VALUES (15, 15, 75, 50, 20, 5, 0, 0, 4.6, '2025-05-13 19:49:00');
INSERT INTO `pms_ratings` VALUES (16, 16, 85, 50, 25, 10, 0, 0, 4.5, '2025-05-13 19:49:00');
INSERT INTO `pms_ratings` VALUES (17, 17, 60, 30, 25, 5, 0, 0, 4.4, '2025-05-13 19:49:00');
INSERT INTO `pms_ratings` VALUES (18, 18, 45, 40, 3, 2, 0, 0, 4.8, '2025-05-13 19:49:00');
INSERT INTO `pms_ratings` VALUES (19, 19, 130, 80, 35, 10, 5, 0, 4.5, '2025-05-13 19:49:00');
INSERT INTO `pms_ratings` VALUES (20, 20, 95, 65, 20, 10, 0, 0, 4.6, '2025-05-13 19:49:00');

-- ----------------------------
-- Table structure for sms_coupon_products
-- ----------------------------
DROP TABLE IF EXISTS `sms_coupon_products`;
CREATE TABLE `sms_coupon_products`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `coupon_id` bigint NOT NULL COMMENT '优惠券ID',
  `product_id` bigint NULL DEFAULT NULL COMMENT '商品ID',
  `product_category_id` bigint NULL DEFAULT NULL COMMENT '商品分类ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_coupon_id`(`coupon_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '优惠券和商品的关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms_coupon_products
-- ----------------------------

-- ----------------------------
-- Table structure for sms_coupons
-- ----------------------------
DROP TABLE IF EXISTS `sms_coupons`;
CREATE TABLE `sms_coupons`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '优惠券ID',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '优惠券名称',
  `type` tinyint NOT NULL COMMENT '优惠券类型：0-全场通用；1-指定分类；2-指定商品',
  `amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '金额',
  `min_point` decimal(10, 2) NULL DEFAULT NULL COMMENT '使用门槛，0表示无门槛',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `count` int NULL DEFAULT NULL COMMENT '优惠券数量',
  `used_count` int NULL DEFAULT 0 COMMENT '已使用数量',
  `per_limit` int NULL DEFAULT 1 COMMENT '每人限领张数',
  `note` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `publish_status` tinyint NULL DEFAULT 0 COMMENT '发布状态：0-未发布，1-已发布',
  `use_type` tinyint NULL DEFAULT 0 COMMENT '使用类型：0-全场通用；1-指定分类；2-指定商品',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '优惠券表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms_coupons
-- ----------------------------

-- ----------------------------
-- Table structure for sms_flash_promotion
-- ----------------------------
DROP TABLE IF EXISTS `sms_flash_promotion`;
CREATE TABLE `sms_flash_promotion`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '秒杀ID',
  `title` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '活动标题',
  `start_date` date NULL DEFAULT NULL COMMENT '开始日期',
  `end_date` date NULL DEFAULT NULL COMMENT '结束日期',
  `status` tinyint NULL DEFAULT 0 COMMENT '上下线状态：0-下线，1-上线',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '秒杀活动表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms_flash_promotion
-- ----------------------------

-- ----------------------------
-- Table structure for sms_flash_promotion_products
-- ----------------------------
DROP TABLE IF EXISTS `sms_flash_promotion_products`;
CREATE TABLE `sms_flash_promotion_products`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `flash_promotion_id` bigint NOT NULL COMMENT '秒杀活动ID',
  `flash_promotion_session_id` bigint NOT NULL COMMENT '秒杀场次ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `flash_price` decimal(10, 2) NOT NULL COMMENT '秒杀价格',
  `flash_count` int NOT NULL COMMENT '秒杀数量',
  `limit_count` int NULL DEFAULT 1 COMMENT '每人限购数量',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_flash_promotion_id`(`flash_promotion_id` ASC) USING BTREE,
  INDEX `idx_flash_promotion_session_id`(`flash_promotion_session_id` ASC) USING BTREE,
  INDEX `idx_product_id`(`product_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '秒杀商品关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms_flash_promotion_products
-- ----------------------------

-- ----------------------------
-- Table structure for sms_flash_promotion_sessions
-- ----------------------------
DROP TABLE IF EXISTS `sms_flash_promotion_sessions`;
CREATE TABLE `sms_flash_promotion_sessions`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '场次ID',
  `flash_promotion_id` bigint NULL DEFAULT NULL COMMENT '秒杀活动ID',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '场次名称',
  `start_time` time NULL DEFAULT NULL COMMENT '每日开始时间',
  `end_time` time NULL DEFAULT NULL COMMENT '每日结束时间',
  `status` tinyint NULL DEFAULT 0 COMMENT '启用状态：0-不启用，1-启用',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_flash_promotion_id`(`flash_promotion_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '秒杀活动场次表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms_flash_promotion_sessions
-- ----------------------------

-- ----------------------------
-- Table structure for sms_promotion_products
-- ----------------------------
DROP TABLE IF EXISTS `sms_promotion_products`;
CREATE TABLE `sms_promotion_products`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `promotion_id` bigint NOT NULL COMMENT '促销ID',
  `product_id` bigint NULL DEFAULT NULL COMMENT '商品ID',
  `product_category_id` bigint NULL DEFAULT NULL COMMENT '商品分类ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_promotion_id`(`promotion_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '促销和商品关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms_promotion_products
-- ----------------------------

-- ----------------------------
-- Table structure for sms_promotions
-- ----------------------------
DROP TABLE IF EXISTS `sms_promotions`;
CREATE TABLE `sms_promotions`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '促销ID',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '促销名称',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `status` tinyint NULL DEFAULT 0 COMMENT '状态：0-未开始，1-进行中，2-已结束',
  `type` tinyint NOT NULL COMMENT '促销类型：0-满减，1-折扣，2-直降',
  `discount_type` tinyint NULL DEFAULT NULL COMMENT '优惠方式：0-满额减，1-满件减',
  `discount_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠金额/折扣',
  `min_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '满足金额',
  `min_count` int NULL DEFAULT NULL COMMENT '满足件数',
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `apply_type` tinyint NULL DEFAULT 0 COMMENT '适用范围：0-全场，1-分类，2-商品',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '促销活动表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms_promotions
-- ----------------------------

-- ----------------------------
-- Table structure for ums_notifications
-- ----------------------------
DROP TABLE IF EXISTS `ums_notifications`;
CREATE TABLE `ums_notifications`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '通知ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标题',
  `content` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '内容',
  `type` tinyint NULL DEFAULT NULL COMMENT '通知类型：0-系统，1-物流，2-活动',
  `read_status` tinyint NULL DEFAULT 0 COMMENT '阅读状态：0-未读，1-已读',
  `send_time` datetime NULL DEFAULT NULL COMMENT '发送时间',
  `read_time` datetime NULL DEFAULT NULL COMMENT '阅读时间',
  `send_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发送者',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户通知表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ums_notifications
-- ----------------------------

-- ----------------------------
-- Table structure for ums_user_addresses
-- ----------------------------
DROP TABLE IF EXISTS `ums_user_addresses`;
CREATE TABLE `ums_user_addresses`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '地址ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `receiver_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人电话',
  `province` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '省份',
  `city` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '城市',
  `district` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '区县',
  `detail_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '详细地址',
  `postal_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮编',
  `default_status` tinyint NULL DEFAULT 0 COMMENT '是否默认：0-非默认，1-默认',
  `tag` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标签',
  `is_deleted` tinyint NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户地址表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ums_user_addresses
-- ----------------------------
INSERT INTO `ums_user_addresses` VALUES (1, 1, '张三', '123', '陕西省', '汉中市', '汉台区', '陕西理工大学', '000', 0, '', 0, '2025-05-13 19:26:17', '2025-05-14 16:33:08');
INSERT INTO `ums_user_addresses` VALUES (2, 1, 'test', '19391629123', '四川省', '成都市', '武侯区', '武侯祠大街', NULL, 1, NULL, 0, '2025-05-14 16:33:02', '2025-05-14 16:33:08');

-- ----------------------------
-- Table structure for ums_user_brand_attentions
-- ----------------------------
DROP TABLE IF EXISTS `ums_user_brand_attentions`;
CREATE TABLE `ums_user_brand_attentions`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `brand_id` bigint NOT NULL COMMENT '品牌ID',
  `brand_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '品牌名称',
  `brand_logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '品牌logo',
  `create_time` datetime NOT NULL COMMENT '关注时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_user_brand`(`user_id` ASC, `brand_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_brand_id`(`brand_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户关注品牌表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ums_user_brand_attentions
-- ----------------------------

-- ----------------------------
-- Table structure for ums_user_coupons
-- ----------------------------
DROP TABLE IF EXISTS `ums_user_coupons`;
CREATE TABLE `ums_user_coupons`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `coupon_id` bigint NOT NULL COMMENT '优惠券ID',
  `coupon_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '优惠券名称',
  `coupon_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠券金额',
  `min_point` decimal(10, 2) NULL DEFAULT NULL COMMENT '使用门槛',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `use_status` tinyint NULL DEFAULT 0 COMMENT '使用状态：0-未使用，1-已使用，2-已过期',
  `use_time` datetime NULL DEFAULT NULL COMMENT '使用时间',
  `order_id` bigint NULL DEFAULT NULL COMMENT '订单ID',
  `order_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订单编号',
  `get_time` datetime NULL DEFAULT NULL COMMENT '获取时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_coupon_id`(`coupon_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户优惠券表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ums_user_coupons
-- ----------------------------

-- ----------------------------
-- Table structure for ums_user_favorites
-- ----------------------------
DROP TABLE IF EXISTS `ums_user_favorites`;
CREATE TABLE `ums_user_favorites`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `product_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品名称',
  `product_pic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品图片',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品价格',
  `create_time` datetime NOT NULL COMMENT '收藏时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_user_product`(`user_id` ASC, `product_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_product_id`(`product_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户收藏表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ums_user_favorites
-- ----------------------------
INSERT INTO `ums_user_favorites` VALUES (2, 1, 7, '潜水电脑表', '/images/products/dive-computer.png', 459.99, '2025-05-14 16:59:39');
INSERT INTO `ums_user_favorites` VALUES (3, 1, 18, '水下相机', '/images/products/underwater-camera.png', 299.99, '2025-05-14 16:59:46');
INSERT INTO `ums_user_favorites` VALUES (4, 1, 6, '专业潜水调节器', '/images/products/diving-regulator.png', 299.99, '2025-05-14 19:33:26');
INSERT INTO `ums_user_favorites` VALUES (6, 1, 20, '沙滩推车', '/static/images/products/beach-cart.png', 89.99, '2025-05-15 11:55:14');

-- ----------------------------
-- Table structure for ums_user_search_history
-- ----------------------------
DROP TABLE IF EXISTS `ums_user_search_history`;
CREATE TABLE `ums_user_search_history`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `keyword` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '搜索关键词',
  `create_time` datetime NOT NULL COMMENT '搜索时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户搜索历史表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ums_user_search_history
-- ----------------------------

-- ----------------------------
-- Table structure for ums_users
-- ----------------------------
DROP TABLE IF EXISTS `ums_users`;
CREATE TABLE `ums_users`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `gender` tinyint NULL DEFAULT 0 COMMENT '性别：0-未知，1-男，2-女',
  `birth` date NULL DEFAULT NULL COMMENT '生日',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `status` tinyint NULL DEFAULT 1 COMMENT '启用状态：0-禁用，1-启用',
  `last_login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `is_merchant` tinyint NULL DEFAULT 0 COMMENT '是否商家：0-否，1-是',
  `level` int NULL DEFAULT 0 COMMENT '会员等级',
  `integration` int NULL DEFAULT 0 COMMENT '积分',
  `region` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所在地区',
  `create_time` datetime NULL DEFAULT NULL COMMENT '注册时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_username`(`username` ASC) USING BTREE,
  UNIQUE INDEX `idx_phone`(`phone` ASC) USING BTREE,
  UNIQUE INDEX `idx_email`(`email` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ums_users
-- ----------------------------
INSERT INTO `ums_users` VALUES (1, 'test', '$2a$10$xvPUxw3shdp638hg4nQuZOGyqdRm9eNQfsQqKwSMRHM.G9RTv7GBe', '19391629122', '2052619274@qq.com', 0, NULL, '', 1, '2025-05-15 11:54:49', 0, 0, 0, NULL, NULL, NULL);
INSERT INTO `ums_users` VALUES (2, '张三', '$2a$10$losbpH3Z.aupvPSsiTbZ.epsYuOM6ADdMbccV3CwPRegIMasJTn0m', NULL, NULL, 0, NULL, 'https://whaletide-mall.oss-cn-beijing.aliyuncs.com/default-avatar.png', 1, '2025-05-14 20:06:03', 0, 0, 0, NULL, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
