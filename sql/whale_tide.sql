/*
 Navicat Premium Data Transfer

 Source Server         : mysql_3306
 Source Server Type    : MySQL
 Source Server Version : 80039
 Source Host           : localhost:3306
 Source Schema         : whale_tide

 Target Server Type    : MySQL
 Target Server Version : 80039
 File Encoding         : 65001

 Date: 15/03/2025 18:36:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ams_admin_login_logs
-- ----------------------------
DROP TABLE IF EXISTS `ams_admin_login_logs`;
CREATE TABLE `ams_admin_login_logs`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `admin_id` bigint(0) NULL DEFAULT NULL COMMENT '管理员ID',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '管理员账号',
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '登录IP',
  `user_agent` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '浏览器类型',
  `status` tinyint(0) NULL DEFAULT 0 COMMENT '登录状态：0-失败，1-成功',
  `message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '提示消息',
  `login_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_admin_id`(`admin_id`) USING BTREE,
  CONSTRAINT `fk_login_log_admin` FOREIGN KEY (`admin_id`) REFERENCES `ams_admins` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员登录日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ams_admin_operation_logs
-- ----------------------------
DROP TABLE IF EXISTS `ams_admin_operation_logs`;
CREATE TABLE `ams_admin_operation_logs`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `admin_id` bigint(0) NULL DEFAULT NULL COMMENT '管理员ID',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '管理员账号',
  `module` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作模块',
  `method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求方法',
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求URL',
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作IP',
  `user_agent` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '浏览器类型',
  `request_param` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '请求参数',
  `response_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '响应数据',
  `status` tinyint(0) NULL DEFAULT 0 COMMENT '操作状态：0-失败，1-成功',
  `error_message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '错误消息',
  `operation_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_admin_id`(`admin_id`) USING BTREE,
  CONSTRAINT `fk_operation_log_admin` FOREIGN KEY (`admin_id`) REFERENCES `ams_admins` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员操作日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ams_admin_role_relations
-- ----------------------------
DROP TABLE IF EXISTS `ams_admin_role_relations`;
CREATE TABLE `ams_admin_role_relations`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `admin_id` bigint(0) NOT NULL COMMENT '管理员ID',
  `role_id` bigint(0) NOT NULL COMMENT '角色ID',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_admin_id`(`admin_id`) USING BTREE,
  INDEX `idx_role_id`(`role_id`) USING BTREE,
  CONSTRAINT `fk_relation_admin` FOREIGN KEY (`admin_id`) REFERENCES `ams_admins` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_relation_role` FOREIGN KEY (`role_id`) REFERENCES `ams_roles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员与角色关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ams_admin_role_relations
-- ----------------------------
INSERT INTO `ams_admin_role_relations` VALUES (6, 12, 6, '2025-03-15 18:28:59');
INSERT INTO `ams_admin_role_relations` VALUES (7, 13, 7, '2025-03-15 18:28:59');
INSERT INTO `ams_admin_role_relations` VALUES (8, 14, 8, '2025-03-15 18:28:59');

-- ----------------------------
-- Table structure for ams_admins
-- ----------------------------
DROP TABLE IF EXISTS `ams_admins`;
CREATE TABLE `ams_admins`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `real_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `avatar` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `gender` tinyint(0) NULL DEFAULT 0 COMMENT '性别：0-未知，1-男，2-女',
  `status` tinyint(0) NULL DEFAULT 1 COMMENT '帐号启用状态：0-禁用，1-启用',
  `login_time` datetime(0) NULL DEFAULT NULL COMMENT '最后登录时间',
  `login_ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最后登录IP',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注信息',
  `super_admin` tinyint(0) NULL DEFAULT 0 COMMENT '是否超级管理员：0-否，1-是',
  `department` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '部门',
  `position` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '职位',
  `is_deleted` tinyint(0) NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ams_admins
-- ----------------------------
INSERT INTO `ams_admins` VALUES (12, 'admin', '123456', '超级管理员', NULL, NULL, NULL, 0, 1, NULL, NULL, NULL, 1, NULL, NULL, 0, '2025-03-15 18:28:59', '2025-03-15 18:30:22');
INSERT INTO `ams_admins` VALUES (13, 'merchant', '123456', '商家', NULL, NULL, NULL, 0, 1, NULL, NULL, NULL, 0, NULL, NULL, 0, '2025-03-15 18:28:59', '2025-03-15 18:28:59');
INSERT INTO `ams_admins` VALUES (14, 'user', '123456', '普通用户', NULL, NULL, NULL, 0, 1, NULL, NULL, NULL, 0, NULL, NULL, 0, '2025-03-15 18:28:59', '2025-03-15 18:28:59');

-- ----------------------------
-- Table structure for ams_menus
-- ----------------------------
DROP TABLE IF EXISTS `ams_menus`;
CREATE TABLE `ams_menus`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `parent_id` bigint(0) NULL DEFAULT 0 COMMENT '父级ID',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名称',
  `level` int(0) NULL DEFAULT 0 COMMENT '菜单级别',
  `sort` int(0) NULL DEFAULT 0 COMMENT '菜单排序',
  `icon` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '前端图标',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '前端路由路径',
  `component` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '前端组件',
  `redirect` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '重定向路径',
  `hidden` tinyint(0) NULL DEFAULT 0 COMMENT '是否隐藏：0-否，1-是',
  `status` tinyint(0) NULL DEFAULT 1 COMMENT '菜单状态：0-禁用，1-启用',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单描述',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ams_permissions
-- ----------------------------
DROP TABLE IF EXISTS `ams_permissions`;
CREATE TABLE `ams_permissions`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '权限ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限名称',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限编码',
  `type` tinyint(0) NOT NULL COMMENT '权限类型：0-目录，1-菜单，2-按钮，3-接口',
  `parent_id` bigint(0) NULL DEFAULT 0 COMMENT '父级权限ID',
  `path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路径',
  `component` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组件',
  `redirect` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '重定向',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图标',
  `sort` int(0) NULL DEFAULT 0 COMMENT '排序',
  `hidden` tinyint(0) NULL DEFAULT 0 COMMENT '是否隐藏：0-否，1-是',
  `status` tinyint(0) NULL DEFAULT 1 COMMENT '启用状态：0-禁用，1-启用',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限描述',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_code`(`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ams_resource_categories
-- ----------------------------
DROP TABLE IF EXISTS `ams_resource_categories`;
CREATE TABLE `ams_resource_categories`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
  `sort` int(0) NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '资源分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ams_resources
-- ----------------------------
DROP TABLE IF EXISTS `ams_resources`;
CREATE TABLE `ams_resources`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '资源ID',
  `category_id` bigint(0) NULL DEFAULT NULL COMMENT '资源分类ID',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '资源名称',
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '资源URL',
  `method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'HTTP请求方法',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '资源描述',
  `status` tinyint(0) NULL DEFAULT 1 COMMENT '启用状态：0-禁用，1-启用',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_category_id`(`category_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '资源表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ams_role_menu_relations
-- ----------------------------
DROP TABLE IF EXISTS `ams_role_menu_relations`;
CREATE TABLE `ams_role_menu_relations`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` bigint(0) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(0) NOT NULL COMMENT '菜单ID',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_role_id`(`role_id`) USING BTREE,
  INDEX `idx_menu_id`(`menu_id`) USING BTREE,
  CONSTRAINT `fk_menu_relation_menu` FOREIGN KEY (`menu_id`) REFERENCES `ams_menus` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_menu_relation_role` FOREIGN KEY (`role_id`) REFERENCES `ams_roles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色与菜单关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ams_role_permission_relations
-- ----------------------------
DROP TABLE IF EXISTS `ams_role_permission_relations`;
CREATE TABLE `ams_role_permission_relations`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` bigint(0) NOT NULL COMMENT '角色ID',
  `permission_id` bigint(0) NOT NULL COMMENT '权限ID',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_role_id`(`role_id`) USING BTREE,
  INDEX `idx_permission_id`(`permission_id`) USING BTREE,
  CONSTRAINT `fk_permission_relation_permission` FOREIGN KEY (`permission_id`) REFERENCES `ams_permissions` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_permission_relation_role` FOREIGN KEY (`role_id`) REFERENCES `ams_roles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色与权限关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ams_role_permission_relations
-- ----------------------------
INSERT INTO `ams_role_permission_relations` VALUES (1, 1, 1, '2023-01-01 00:00:00');
INSERT INTO `ams_role_permission_relations` VALUES (2, 1, 2, '2023-01-01 00:00:00');
INSERT INTO `ams_role_permission_relations` VALUES (3, 1, 3, '2023-01-01 00:00:00');
INSERT INTO `ams_role_permission_relations` VALUES (4, 2, 1, '2023-01-01 00:00:00');
INSERT INTO `ams_role_permission_relations` VALUES (5, 3, 2, '2023-01-01 00:00:00');

-- ----------------------------
-- Table structure for ams_role_resource_relations
-- ----------------------------
DROP TABLE IF EXISTS `ams_role_resource_relations`;
CREATE TABLE `ams_role_resource_relations`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` bigint(0) NOT NULL COMMENT '角色ID',
  `resource_id` bigint(0) NOT NULL COMMENT '资源ID',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_role_id`(`role_id`) USING BTREE,
  INDEX `idx_resource_id`(`resource_id`) USING BTREE,
  CONSTRAINT `fk_resource_relation_resource` FOREIGN KEY (`resource_id`) REFERENCES `ams_resources` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_resource_relation_role` FOREIGN KEY (`role_id`) REFERENCES `ams_roles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色与资源关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ams_roles
-- ----------------------------
DROP TABLE IF EXISTS `ams_roles`;
CREATE TABLE `ams_roles`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色编码',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色描述',
  `sort` int(0) NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint(0) NULL DEFAULT 1 COMMENT '启用状态：0-禁用，1-启用',
  `is_deleted` tinyint(0) NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_code`(`code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ams_roles
-- ----------------------------
INSERT INTO `ams_roles` VALUES (6, '超级管理员', 'ADMIN', '拥有所有操作权限', 0, 1, 0, '2025-03-15 18:23:32', '2025-03-15 18:30:44');
INSERT INTO `ams_roles` VALUES (7, '商家', 'MERCHANT', '拥有商品和订单管理权限', 0, 1, 0, '2025-03-15 18:23:32', '2025-03-15 18:23:32');
INSERT INTO `ams_roles` VALUES (8, '普通用户', 'USER', '拥有基本查看权限', 0, 1, 0, '2025-03-15 18:23:32', '2025-03-15 18:24:03');

-- ----------------------------
-- Table structure for oms_cart_items
-- ----------------------------
DROP TABLE IF EXISTS `oms_cart_items`;
CREATE TABLE `oms_cart_items`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '购物车项ID',
  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
  `product_id` bigint(0) NOT NULL COMMENT '商品ID',
  `product_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品名称',
  `product_image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品图片',
  `sku_id` bigint(0) NOT NULL COMMENT 'SKU ID',
  `sku_specs` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '规格属性(文本表示)',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品单价',
  `quantity` int(0) NULL DEFAULT 1 COMMENT '数量',
  `checked` tinyint(0) NULL DEFAULT 1 COMMENT '是否选中：0-否，1-是',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_product_id`(`product_id`) USING BTREE,
  INDEX `idx_sku_id`(`sku_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '购物车表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for oms_order_deliveries
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_deliveries`;
CREATE TABLE `oms_order_deliveries`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '物流ID',
  `order_id` bigint(0) NOT NULL COMMENT '订单ID',
  `order_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `delivery_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '物流单号',
  `delivery_company` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '物流公司',
  `delivery_company_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '物流公司代码',
  `receiver_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收件人姓名',
  `receiver_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收件人电话',
  `receiver_province` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '省份',
  `receiver_city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '城市',
  `receiver_district` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '区县',
  `receiver_address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '详细地址',
  `receiver_postal_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮政编码',
  `delivery_status` tinyint(0) NULL DEFAULT 0 COMMENT '物流状态：0-未发货，1-已发货，2-运输中，3-已签收，4-派送失败',
  `delivery_time` datetime(0) NULL DEFAULT NULL COMMENT '发货时间',
  `receive_time` datetime(0) NULL DEFAULT NULL COMMENT '签收时间',
  `delivery_note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '物流备注',
  `is_split` tinyint(0) NULL DEFAULT 0 COMMENT '是否拆单：0-否，1-是',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE,
  INDEX `idx_delivery_sn`(`delivery_sn`) USING BTREE,
  CONSTRAINT `fk_delivery_order` FOREIGN KEY (`order_id`) REFERENCES `oms_orders` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单物流信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for oms_order_items
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_items`;
CREATE TABLE `oms_order_items`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '订单项ID',
  `order_id` bigint(0) NOT NULL COMMENT '订单ID',
  `order_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `product_id` bigint(0) NOT NULL COMMENT '商品ID',
  `product_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称',
  `product_image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品图片',
  `product_brand` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '品牌名称',
  `product_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品编号',
  `product_category_id` bigint(0) NULL DEFAULT NULL COMMENT '商品分类ID',
  `sku_id` bigint(0) NOT NULL COMMENT 'SKU ID',
  `sku_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'SKU编码',
  `sku_specs` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '规格属性(文本表示)',
  `quantity` int(0) NOT NULL COMMENT '购买数量',
  `price` decimal(10, 2) NOT NULL COMMENT '商品单价',
  `real_amount` decimal(10, 2) NOT NULL COMMENT '实付金额',
  `original_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '原始金额(无优惠)',
  `coupon_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '优惠券优惠分摊金额',
  `promotion_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '促销优惠分摊金额',
  `promotion_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '促销活动信息',
  `gift_integration` int(0) NULL DEFAULT 0 COMMENT '赠送积分',
  `gift_growth` int(0) NULL DEFAULT 0 COMMENT '赠送成长值',
  `refund_status` tinyint(0) NULL DEFAULT 0 COMMENT '退款状态：0-未退款，1-已申请，2-已退款',
  `comment_status` tinyint(0) NULL DEFAULT 0 COMMENT '评价状态：0-未评价，1-已评价',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE,
  INDEX `idx_product_id`(`product_id`) USING BTREE,
  INDEX `idx_sku_id`(`sku_id`) USING BTREE,
  CONSTRAINT `fk_order_item_order` FOREIGN KEY (`order_id`) REFERENCES `oms_orders` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单商品项表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for oms_order_logs
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_logs`;
CREATE TABLE `oms_order_logs`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `order_id` bigint(0) NOT NULL COMMENT '订单ID',
  `order_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `operator_id` bigint(0) NULL DEFAULT NULL COMMENT '操作人ID',
  `operator_type` tinyint(0) NULL DEFAULT 0 COMMENT '操作人类型：0-系统，1-用户，2-商家，3-管理员',
  `operator_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作人名称',
  `action` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作类型',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作备注',
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作IP',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE,
  CONSTRAINT `fk_order_log_order` FOREIGN KEY (`order_id`) REFERENCES `oms_orders` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单操作日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for oms_order_returns
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_returns`;
CREATE TABLE `oms_order_returns`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '退货申请ID',
  `order_id` bigint(0) NOT NULL COMMENT '订单ID',
  `order_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
  `return_type` tinyint(0) NULL DEFAULT 0 COMMENT '退货类型：0-仅退款，1-退货退款',
  `return_reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '退货原因',
  `return_amount` decimal(10, 2) NOT NULL COMMENT '退款金额',
  `return_images` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退货凭证图片，多个以逗号分隔',
  `return_description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退货说明',
  `contact_person` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系人',
  `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `status` tinyint(0) NULL DEFAULT 0 COMMENT '申请状态：0-待处理，1-处理中，2-已同意，3-已拒绝，4-已完成',
  `handler_id` bigint(0) NULL DEFAULT NULL COMMENT '处理人ID',
  `handler_note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '处理备注',
  `handler_time` datetime(0) NULL DEFAULT NULL COMMENT '处理时间',
  `delivery_company` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退货物流公司',
  `delivery_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退货物流单号',
  `receive_time` datetime(0) NULL DEFAULT NULL COMMENT '收货时间',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '申请时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  CONSTRAINT `fk_return_order` FOREIGN KEY (`order_id`) REFERENCES `oms_orders` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单退货申请表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for oms_order_status_history
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_status_history`;
CREATE TABLE `oms_order_status_history`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` bigint(0) NOT NULL COMMENT '订单ID',
  `order_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `previous_status` tinyint(0) NOT NULL COMMENT '前置状态',
  `current_status` tinyint(0) NOT NULL COMMENT '当前状态',
  `operator_id` bigint(0) NULL DEFAULT NULL COMMENT '操作人ID',
  `operator_type` tinyint(0) NULL DEFAULT 0 COMMENT '操作人类型：0-系统，1-用户，2-商家，3-管理员',
  `operator_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作人名称',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '状态变更备注',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE,
  CONSTRAINT `fk_status_history_order` FOREIGN KEY (`order_id`) REFERENCES `oms_orders` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单状态变更历史表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for oms_orders
-- ----------------------------
DROP TABLE IF EXISTS `oms_orders`;
CREATE TABLE `oms_orders`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
  `merchant_id` bigint(0) NOT NULL COMMENT '商家ID',
  `order_type` tinyint(0) NULL DEFAULT 0 COMMENT '订单类型：0-普通订单，1-秒杀订单，2-团购订单',
  `source_type` tinyint(0) NULL DEFAULT 0 COMMENT '订单来源：0-PC, 1-App, 2-小程序, 3-H5',
  `status` tinyint(0) NULL DEFAULT 0 COMMENT '订单状态：0-待付款，1-待发货，2-已发货，3-已完成，4-已关闭，5-已取消',
  `total_amount` decimal(10, 2) NOT NULL COMMENT '订单总金额',
  `pay_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '实际支付金额',
  `freight_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '运费',
  `discount_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '优惠金额',
  `coupon_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '优惠券抵扣金额',
  `promotion_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '促销优惠金额',
  `integration_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '积分抵扣金额',
  `pay_type` tinyint(0) NULL DEFAULT 0 COMMENT '支付方式：0-未支付，1-支付宝，2-微信，3-银联',
  `payment_time` datetime(0) NULL DEFAULT NULL COMMENT '支付时间',
  `delivery_time` datetime(0) NULL DEFAULT NULL COMMENT '发货时间',
  `receive_time` datetime(0) NULL DEFAULT NULL COMMENT '确认收货时间',
  `comment_time` datetime(0) NULL DEFAULT NULL COMMENT '评价时间',
  `order_note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订单备注',
  `auto_confirm_day` int(0) NULL DEFAULT 7 COMMENT '自动确认收货天数',
  `is_deleted` tinyint(0) NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_order_sn`(`order_sn`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_merchant_id`(`merchant_id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单主表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for oms_payments
-- ----------------------------
DROP TABLE IF EXISTS `oms_payments`;
CREATE TABLE `oms_payments`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '支付ID',
  `order_id` bigint(0) NOT NULL COMMENT '订单ID',
  `order_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
  `payment_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付流水号',
  `payment_method` tinyint(0) NOT NULL COMMENT '支付方式：1-支付宝，2-微信，3-银联',
  `payment_platform` tinyint(0) NULL DEFAULT 0 COMMENT '支付平台：0-PC，1-APP，2-小程序',
  `amount` decimal(10, 2) NOT NULL COMMENT '支付金额',
  `status` tinyint(0) NULL DEFAULT 0 COMMENT '支付状态：0-待支付，1-支付中，2-支付成功，3-支付失败',
  `payment_time` datetime(0) NULL DEFAULT NULL COMMENT '支付时间',
  `callback_time` datetime(0) NULL DEFAULT NULL COMMENT '回调时间',
  `callback_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '回调内容',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_payment_sn`(`payment_sn`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  CONSTRAINT `fk_payment_order` FOREIGN KEY (`order_id`) REFERENCES `oms_orders` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '支付记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pms_brands
-- ----------------------------
DROP TABLE IF EXISTS `pms_brands`;
CREATE TABLE `pms_brands`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '品牌ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '品牌名称',
  `logo` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '品牌Logo',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '品牌描述',
  `first_letter` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '品牌首字母',
  `sort` int(0) NULL DEFAULT 0 COMMENT '排序',
  `is_featured` tinyint(0) NULL DEFAULT 0 COMMENT '是否推荐：0-否，1-是',
  `status` tinyint(0) NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_name`(`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '品牌表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pms_product_attribute_values
-- ----------------------------
DROP TABLE IF EXISTS `pms_product_attribute_values`;
CREATE TABLE `pms_product_attribute_values`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '属性值ID',
  `product_id` bigint(0) NOT NULL COMMENT '商品ID',
  `attribute_id` bigint(0) NOT NULL COMMENT '属性ID',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '属性值',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_product_id`(`product_id`) USING BTREE,
  INDEX `idx_attribute_id`(`attribute_id`) USING BTREE,
  CONSTRAINT `fk_attr_value_attribute` FOREIGN KEY (`attribute_id`) REFERENCES `pms_product_attributes` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_attr_value_product` FOREIGN KEY (`product_id`) REFERENCES `pms_products` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品属性值表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pms_product_attributes
-- ----------------------------
DROP TABLE IF EXISTS `pms_product_attributes`;
CREATE TABLE `pms_product_attributes`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '属性ID',
  `category_id` bigint(0) NOT NULL COMMENT '所属分类ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '属性名称',
  `attribute_type` tinyint(0) NULL DEFAULT 0 COMMENT '属性类型：0-规格，1-参数',
  `input_type` tinyint(0) NULL DEFAULT 0 COMMENT '录入方式：0-手工输入，1-从列表选择',
  `input_options` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '可选值列表，以逗号分隔',
  `sort` int(0) NULL DEFAULT 0 COMMENT '排序',
  `filter_type` tinyint(0) NULL DEFAULT 0 COMMENT '检索类型：0-不需要，1-需要',
  `search_type` tinyint(0) NULL DEFAULT 0 COMMENT '搜索类型：0-不需要，1-需要',
  `is_required` tinyint(0) NULL DEFAULT 0 COMMENT '是否必填：0-否，1-是',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_category_id`(`category_id`) USING BTREE,
  CONSTRAINT `fk_attribute_category` FOREIGN KEY (`category_id`) REFERENCES `pms_product_categories` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品属性表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pms_product_categories
-- ----------------------------
DROP TABLE IF EXISTS `pms_product_categories`;
CREATE TABLE `pms_product_categories`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `parent_id` bigint(0) NULL DEFAULT 0 COMMENT '父分类ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
  `level` tinyint(0) NULL DEFAULT 1 COMMENT '分类层级：1-一级分类，2-二级分类，3-三级分类',
  `icon` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分类图标',
  `sort` int(0) NULL DEFAULT 0 COMMENT '排序',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '分类描述',
  `keywords` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '关键词',
  `status` tinyint(0) NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `is_featured` tinyint(0) NULL DEFAULT 0 COMMENT '是否推荐：0-否，1-是',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pms_product_comments
-- ----------------------------
DROP TABLE IF EXISTS `pms_product_comments`;
CREATE TABLE `pms_product_comments`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
  `product_id` bigint(0) NOT NULL COMMENT '商品ID',
  `order_id` bigint(0) NULL DEFAULT NULL COMMENT '订单ID',
  `order_item_id` bigint(0) NULL DEFAULT NULL COMMENT '订单商品ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '评论标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评论内容',
  `rating` tinyint(0) NULL DEFAULT 5 COMMENT '评分：1-5分',
  `images` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '评论图片，多个以逗号分隔',
  `is_anonymous` tinyint(0) NULL DEFAULT 0 COMMENT '是否匿名：0-否，1-是',
  `is_show` tinyint(0) NULL DEFAULT 1 COMMENT '是否显示：0-不显示，1-显示',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '评论时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_product_id`(`product_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE,
  CONSTRAINT `fk_comment_product` FOREIGN KEY (`product_id`) REFERENCES `pms_products` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pms_product_details
-- ----------------------------
DROP TABLE IF EXISTS `pms_product_details`;
CREATE TABLE `pms_product_details`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '详情ID',
  `product_id` bigint(0) NOT NULL COMMENT '商品ID',
  `description` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '商品详细描述',
  `specs` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '商品规格',
  `packing_list` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '包装清单',
  `after_service` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '售后服务',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_product_id`(`product_id`) USING BTREE,
  CONSTRAINT `fk_detail_product` FOREIGN KEY (`product_id`) REFERENCES `pms_products` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品详情表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pms_product_images
-- ----------------------------
DROP TABLE IF EXISTS `pms_product_images`;
CREATE TABLE `pms_product_images`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '图片ID',
  `product_id` bigint(0) NOT NULL COMMENT '商品ID',
  `image_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片URL',
  `sort` int(0) NULL DEFAULT 0 COMMENT '排序',
  `is_main` tinyint(0) NULL DEFAULT 0 COMMENT '是否主图：0-否，1-是',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_product_id`(`product_id`) USING BTREE,
  CONSTRAINT `fk_image_product` FOREIGN KEY (`product_id`) REFERENCES `pms_products` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品图片表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pms_product_skus
-- ----------------------------
DROP TABLE IF EXISTS `pms_product_skus`;
CREATE TABLE `pms_product_skus`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'SKU ID',
  `product_id` bigint(0) NOT NULL COMMENT '商品ID',
  `sku_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'SKU编码',
  `price` decimal(10, 2) NOT NULL COMMENT '价格',
  `stock` int(0) NULL DEFAULT 0 COMMENT '库存',
  `low_stock` int(0) NULL DEFAULT 0 COMMENT '预警库存',
  `image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'SKU图片',
  `specs` json NULL COMMENT '规格属性（JSON格式）',
  `specs_text` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '规格文本，用于展示',
  `volume` decimal(10, 2) NULL DEFAULT NULL COMMENT '体积(m³)',
  `weight` decimal(10, 2) NULL DEFAULT NULL COMMENT '重量(kg)',
  `sale` int(0) NULL DEFAULT 0 COMMENT '销量',
  `lock_stock` int(0) NULL DEFAULT 0 COMMENT '锁定库存(下单但未付款)',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_sku_code`(`sku_code`) USING BTREE,
  INDEX `idx_product_id`(`product_id`) USING BTREE,
  CONSTRAINT `fk_sku_product` FOREIGN KEY (`product_id`) REFERENCES `pms_products` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品SKU表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pms_products
-- ----------------------------
DROP TABLE IF EXISTS `pms_products`;
CREATE TABLE `pms_products`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称',
  `product_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品编号',
  `category_id` bigint(0) NOT NULL COMMENT '分类ID',
  `brand_id` bigint(0) NULL DEFAULT NULL COMMENT '品牌ID',
  `merchant_id` bigint(0) NOT NULL COMMENT '商家ID',
  `price` decimal(10, 2) NOT NULL COMMENT '商品价格',
  `original_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '原价',
  `main_image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '主图URL',
  `keywords` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '关键词',
  `brief` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '简短描述',
  `sale` int(0) NULL DEFAULT 0 COMMENT '销量',
  `stock` int(0) NULL DEFAULT 0 COMMENT '库存',
  `unit` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '单位(件/kg等)',
  `weight` decimal(10, 2) NULL DEFAULT NULL COMMENT '重量(kg)',
  `sort` int(0) NULL DEFAULT 0 COMMENT '排序',
  `publish_status` tinyint(0) NULL DEFAULT 0 COMMENT '上架状态：0-下架，1-上架',
  `new_status` tinyint(0) NULL DEFAULT 0 COMMENT '新品状态：0-非新品，1-新品',
  `recommend_status` tinyint(0) NULL DEFAULT 0 COMMENT '推荐状态：0-不推荐，1-推荐',
  `verify_status` tinyint(0) NULL DEFAULT 0 COMMENT '审核状态：0-未审核，1-审核通过，2-审核不通过',
  `is_deleted` tinyint(0) NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_product_sn`(`product_sn`) USING BTREE,
  INDEX `idx_category_id`(`category_id`) USING BTREE,
  INDEX `idx_brand_id`(`brand_id`) USING BTREE,
  INDEX `idx_merchant_id`(`merchant_id`) USING BTREE,
  CONSTRAINT `fk_product_brand` FOREIGN KEY (`brand_id`) REFERENCES `pms_brands` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_product_category` FOREIGN KEY (`category_id`) REFERENCES `pms_product_categories` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pms_ratings
-- ----------------------------
DROP TABLE IF EXISTS `pms_ratings`;
CREATE TABLE `pms_ratings`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '评分ID',
  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
  `product_id` bigint(0) NOT NULL COMMENT '商品ID',
  `rating` tinyint(0) NULL DEFAULT 5 COMMENT '评分：1-5分',
  `rating_type` tinyint(0) NULL DEFAULT 0 COMMENT '评分类型：0-综合，1-服务，2-质量，3-物流',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '评分时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_product_type`(`user_id`, `product_id`, `rating_type`) USING BTREE,
  INDEX `idx_product_id`(`product_id`) USING BTREE,
  CONSTRAINT `fk_rating_product` FOREIGN KEY (`product_id`) REFERENCES `pms_products` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品评分表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pms_stock_logs
-- ----------------------------
DROP TABLE IF EXISTS `pms_stock_logs`;
CREATE TABLE `pms_stock_logs`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `product_id` bigint(0) NOT NULL COMMENT '商品ID',
  `sku_id` bigint(0) NULL DEFAULT NULL COMMENT 'SKU ID',
  `change_type` tinyint(0) NOT NULL COMMENT '变更类型：1-销售扣减，2-退货增加，3-后台调整',
  `change_amount` int(0) NOT NULL COMMENT '变更数量',
  `before_stock` int(0) NULL DEFAULT NULL COMMENT '变更前库存',
  `after_stock` int(0) NULL DEFAULT NULL COMMENT '变更后库存',
  `order_id` bigint(0) NULL DEFAULT NULL COMMENT '关联订单ID',
  `order_item_id` bigint(0) NULL DEFAULT NULL COMMENT '关联订单项ID',
  `operator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作人',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_product_id`(`product_id`) USING BTREE,
  INDEX `idx_sku_id`(`sku_id`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE,
  CONSTRAINT `fk_stock_log_product` FOREIGN KEY (`product_id`) REFERENCES `pms_products` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_stock_log_sku` FOREIGN KEY (`sku_id`) REFERENCES `pms_product_skus` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '库存变更日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sms_coupon_categories
-- ----------------------------
DROP TABLE IF EXISTS `sms_coupon_categories`;
CREATE TABLE `sms_coupon_categories`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `coupon_id` bigint(0) NOT NULL COMMENT '优惠券ID',
  `category_id` bigint(0) NOT NULL COMMENT '分类ID',
  `category_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分类名称',
  `parent_category_id` bigint(0) NULL DEFAULT NULL COMMENT '父分类ID',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_coupon_id`(`coupon_id`) USING BTREE,
  INDEX `idx_category_id`(`category_id`) USING BTREE,
  CONSTRAINT `fk_category_coupon` FOREIGN KEY (`coupon_id`) REFERENCES `sms_coupons` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '优惠券适用分类关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sms_coupon_history
-- ----------------------------
DROP TABLE IF EXISTS `sms_coupon_history`;
CREATE TABLE `sms_coupon_history`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `coupon_id` bigint(0) NOT NULL COMMENT '优惠券ID',
  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '领取码',
  `order_id` bigint(0) NULL DEFAULT NULL COMMENT '关联订单ID',
  `order_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订单编号',
  `get_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '领取时间',
  `use_time` datetime(0) NULL DEFAULT NULL COMMENT '使用时间',
  `source_type` tinyint(0) NULL DEFAULT 0 COMMENT '获取来源：0-主动领取，1-后台赠送，2-活动赠送',
  `status` tinyint(0) NULL DEFAULT 0 COMMENT '状态：0-未使用，1-已使用，2-已过期',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_coupon_id`(`coupon_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE,
  CONSTRAINT `fk_history_coupon` FOREIGN KEY (`coupon_id`) REFERENCES `sms_coupons` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '优惠券领取历史表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sms_coupon_products
-- ----------------------------
DROP TABLE IF EXISTS `sms_coupon_products`;
CREATE TABLE `sms_coupon_products`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `coupon_id` bigint(0) NOT NULL COMMENT '优惠券ID',
  `product_id` bigint(0) NOT NULL COMMENT '商品ID',
  `product_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品名称',
  `product_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品编号',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_coupon_id`(`coupon_id`) USING BTREE,
  INDEX `idx_product_id`(`product_id`) USING BTREE,
  CONSTRAINT `fk_product_coupon` FOREIGN KEY (`coupon_id`) REFERENCES `sms_coupons` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '优惠券适用商品关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sms_coupons
-- ----------------------------
DROP TABLE IF EXISTS `sms_coupons`;
CREATE TABLE `sms_coupons`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '优惠券ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '优惠券名称',
  `type` tinyint(0) NOT NULL COMMENT '优惠券类型：0-全场通用，1-指定商品，2-指定分类',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '优惠券码',
  `platform` tinyint(0) NULL DEFAULT 0 COMMENT '使用平台：0-全平台，1-移动端，2-PC端',
  `amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '金额',
  `discount` decimal(5, 2) NULL DEFAULT NULL COMMENT '折扣',
  `min_point` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '使用门槛，0表示无门槛',
  `start_time` datetime(0) NOT NULL COMMENT '开始时间',
  `end_time` datetime(0) NOT NULL COMMENT '结束时间',
  `total_count` int(0) NOT NULL COMMENT '发行数量',
  `remaining_count` int(0) NULL DEFAULT 0 COMMENT '剩余数量',
  `receive_count` int(0) NULL DEFAULT 0 COMMENT '已领取数量',
  `used_count` int(0) NULL DEFAULT 0 COMMENT '已使用数量',
  `per_limit` int(0) NULL DEFAULT 1 COMMENT '每人限领张数',
  `use_type` tinyint(0) NULL DEFAULT 0 COMMENT '使用类型：0-通用，1-会员专享，2-新人专享',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `publish_status` tinyint(0) NULL DEFAULT 0 COMMENT '发布状态：0-未发布，1-已发布',
  `is_deleted` tinyint(0) NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_start_time`(`start_time`) USING BTREE,
  INDEX `idx_end_time`(`end_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '优惠券表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sms_flash_promotion
-- ----------------------------
DROP TABLE IF EXISTS `sms_flash_promotion`;
CREATE TABLE `sms_flash_promotion`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '秒杀ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '活动标题',
  `start_date` date NOT NULL COMMENT '开始日期',
  `end_date` date NOT NULL COMMENT '结束日期',
  `status` tinyint(0) NULL DEFAULT 0 COMMENT '上下线状态：0-下线，1-上线',
  `create_admin` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '活动描述',
  `icon_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '活动图标',
  `is_deleted` tinyint(0) NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_start_date`(`start_date`) USING BTREE,
  INDEX `idx_end_date`(`end_date`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '限时秒杀活动表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sms_flash_promotion_products
-- ----------------------------
DROP TABLE IF EXISTS `sms_flash_promotion_products`;
CREATE TABLE `sms_flash_promotion_products`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `flash_promotion_id` bigint(0) NOT NULL COMMENT '秒杀活动ID',
  `flash_session_id` bigint(0) NOT NULL COMMENT '秒杀场次ID',
  `product_id` bigint(0) NOT NULL COMMENT '商品ID',
  `product_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品名称',
  `flash_price` decimal(10, 2) NOT NULL COMMENT '秒杀价格',
  `original_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '原价',
  `flash_stock` int(0) NOT NULL COMMENT '秒杀库存',
  `current_stock` int(0) NULL DEFAULT NULL COMMENT '当前库存',
  `sold_count` int(0) NULL DEFAULT 0 COMMENT '已售数量',
  `limit_count` int(0) NULL DEFAULT 1 COMMENT '每人限购数量',
  `sort` int(0) NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint(0) NULL DEFAULT 0 COMMENT '上架状态：0-下架，1-上架',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_flash_promotion_id`(`flash_promotion_id`) USING BTREE,
  INDEX `idx_flash_session_id`(`flash_session_id`) USING BTREE,
  INDEX `idx_product_id`(`product_id`) USING BTREE,
  CONSTRAINT `fk_product_flash` FOREIGN KEY (`flash_promotion_id`) REFERENCES `sms_flash_promotion` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_product_session` FOREIGN KEY (`flash_session_id`) REFERENCES `sms_flash_promotion_sessions` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '限时秒杀商品关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sms_flash_promotion_sessions
-- ----------------------------
DROP TABLE IF EXISTS `sms_flash_promotion_sessions`;
CREATE TABLE `sms_flash_promotion_sessions`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '场次ID',
  `flash_promotion_id` bigint(0) NOT NULL COMMENT '秒杀活动ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '场次名称',
  `start_time` time(0) NOT NULL COMMENT '每日开始时间',
  `end_time` time(0) NOT NULL COMMENT '每日结束时间',
  `status` tinyint(0) NULL DEFAULT 0 COMMENT '启用状态：0-禁用，1-启用',
  `order_sort` int(0) NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_flash_promotion_id`(`flash_promotion_id`) USING BTREE,
  CONSTRAINT `fk_session_flash` FOREIGN KEY (`flash_promotion_id`) REFERENCES `sms_flash_promotion` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '限时秒杀场次表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sms_promotion_logs
-- ----------------------------
DROP TABLE IF EXISTS `sms_promotion_logs`;
CREATE TABLE `sms_promotion_logs`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `promotion_id` bigint(0) NOT NULL COMMENT '促销活动ID',
  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
  `product_id` bigint(0) NULL DEFAULT NULL COMMENT '商品ID',
  `order_id` bigint(0) NULL DEFAULT NULL COMMENT '订单ID',
  `order_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订单编号',
  `participation_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '参与时间',
  `participation_type` tinyint(0) NULL DEFAULT 0 COMMENT '参与类型：0-浏览，1-参与，2-购买',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_promotion_id`(`promotion_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE,
  CONSTRAINT `fk_log_promotion` FOREIGN KEY (`promotion_id`) REFERENCES `sms_promotions` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '促销活动参与记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sms_promotion_products
-- ----------------------------
DROP TABLE IF EXISTS `sms_promotion_products`;
CREATE TABLE `sms_promotion_products`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `promotion_id` bigint(0) NOT NULL COMMENT '促销活动ID',
  `product_id` bigint(0) NOT NULL COMMENT '商品ID',
  `product_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品名称',
  `promotion_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '促销价格',
  `promotion_stock` int(0) NULL DEFAULT NULL COMMENT '促销库存',
  `promotion_limit` int(0) NULL DEFAULT NULL COMMENT '促销限购',
  `sort` int(0) NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_promotion_id`(`promotion_id`) USING BTREE,
  INDEX `idx_product_id`(`product_id`) USING BTREE,
  CONSTRAINT `fk_product_promotion` FOREIGN KEY (`promotion_id`) REFERENCES `sms_promotions` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '促销活动商品关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sms_promotions
-- ----------------------------
DROP TABLE IF EXISTS `sms_promotions`;
CREATE TABLE `sms_promotions`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '促销活动ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '活动名称',
  `type` tinyint(0) NOT NULL COMMENT '活动类型：0-满减，1-满折，2-特价活动，3-团购活动，4-赠品活动',
  `start_time` datetime(0) NOT NULL COMMENT '开始时间',
  `end_time` datetime(0) NOT NULL COMMENT '结束时间',
  `status` tinyint(0) NULL DEFAULT 0 COMMENT '活动状态：0-未开始，1-进行中，2-已结束，3-已取消',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '活动描述',
  `banner_image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '活动横幅图',
  `rules` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '活动规则，JSON格式',
  `target_type` tinyint(0) NULL DEFAULT 0 COMMENT '目标类型：0-全场，1-指定商品，2-指定分类',
  `platform` tinyint(0) NULL DEFAULT 0 COMMENT '使用平台：0-全平台，1-移动端，2-PC端',
  `priority` int(0) NULL DEFAULT 0 COMMENT '优先级',
  `create_admin` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `is_deleted` tinyint(0) NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_start_time`(`start_time`) USING BTREE,
  INDEX `idx_end_time`(`end_time`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '促销活动表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ums_merchants
-- ----------------------------
DROP TABLE IF EXISTS `ums_merchants`;
CREATE TABLE `ums_merchants`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '商家ID',
  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
  `shop_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '店铺名称',
  `shop_logo` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '店铺logo',
  `shop_banner` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '店铺横幅',
  `business_license` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '营业执照',
  `identity_card_front` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '身份证正面',
  `identity_card_back` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '身份证背面',
  `contact_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系人姓名',
  `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `contact_email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系邮箱',
  `business_categories` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '经营类目，多个用逗号分隔',
  `business_scope` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '经营范围',
  `shop_address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '店铺地址',
  `shop_province` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '省份',
  `shop_city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '城市',
  `shop_district` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '区县',
  `shop_postal_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮政编码',
  `bank_account_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '开户名',
  `bank_account_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '银行账号',
  `bank_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '开户银行',
  `bank_branch` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支行名称',
  `status` tinyint(0) NULL DEFAULT 0 COMMENT '状态：0-待审核，1-审核通过，2-审核拒绝，3-冻结',
  `status_reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '状态原因',
  `rating` decimal(2, 1) NULL DEFAULT 5.0 COMMENT '店铺评分',
  `service_rating` decimal(2, 1) NULL DEFAULT 5.0 COMMENT '服务评分',
  `delivery_rating` decimal(2, 1) NULL DEFAULT 5.0 COMMENT '物流评分',
  `description_rating` decimal(2, 1) NULL DEFAULT 5.0 COMMENT '描述相符评分',
  `total_sales` int(0) NULL DEFAULT 0 COMMENT '总销量',
  `total_product_count` int(0) NULL DEFAULT 0 COMMENT '商品总数',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `is_deleted` tinyint(0) NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_id`(`user_id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  CONSTRAINT `fk_merchant_user` FOREIGN KEY (`user_id`) REFERENCES `ums_users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商家表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ums_merchants
-- ----------------------------
INSERT INTO `ums_merchants` VALUES (1, 2, '李大卖的小店', 'https://example.com/shop/logo/1.jpg', 'https://example.com/shop/banner/1.jpg', 'https://example.com/license/1.jpg', 'https://example.com/id/front/1.jpg', 'https://example.com/id/back/1.jpg', '李卖', '13900139000', 'merchant456@example.com', '服装,鞋包', '服装、鞋包、配饰', '上海市浦东新区张江高科技园区', '上海市', '上海市', '浦东新区', '200120', '李卖', '6222021234567890123', '中国工商银行', '张江支行', 1, NULL, 4.8, 4.9, 4.7, 4.8, 1250, 78, NULL, 0, '2023-01-01 00:00:00', '2023-01-02 11:00:00');
INSERT INTO `ums_merchants` VALUES (2, 4, '赵掌柜精品铺', 'https://example.com/shop/logo/2.jpg', 'https://example.com/shop/banner/2.jpg', 'https://example.com/license/2.jpg', 'https://example.com/id/front/2.jpg', 'https://example.com/id/back/2.jpg', '赵柜', '13600136000', 'merchant321@example.com', '数码,家电', '数码产品、家用电器、智能设备', '深圳市南山区科技园', '广东省', '深圳市', '南山区', '518057', '赵柜', '6222021234567890456', '中国建设银行', '科技园支行', 1, NULL, 4.9, 5.0, 4.8, 4.9, 2380, 156, NULL, 0, '2023-01-01 00:00:00', '2023-01-02 13:00:00');

-- ----------------------------
-- Table structure for ums_notifications
-- ----------------------------
DROP TABLE IF EXISTS `ums_notifications`;
CREATE TABLE `ums_notifications`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '通知ID',
  `sender_id` bigint(0) NULL DEFAULT NULL COMMENT '发送者ID',
  `sender_type` tinyint(0) NULL DEFAULT 0 COMMENT '发送者类型：0-系统，1-用户，2-商家，3-管理员',
  `sender_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发送者名称',
  `receiver_id` bigint(0) NOT NULL COMMENT '接收者ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '通知标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '通知内容',
  `type` tinyint(0) NULL DEFAULT 0 COMMENT '通知类型：0-系统通知，1-订单通知，2-活动通知，3-物流通知，4-商家通知',
  `related_id` bigint(0) NULL DEFAULT NULL COMMENT '关联ID',
  `related_type` tinyint(0) NULL DEFAULT NULL COMMENT '关联类型：0-无，1-订单，2-商品，3-活动，4-物流',
  `read_status` tinyint(0) NULL DEFAULT 0 COMMENT '阅读状态：0-未读，1-已读',
  `read_time` datetime(0) NULL DEFAULT NULL COMMENT '阅读时间',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_receiver_id`(`receiver_id`) USING BTREE,
  INDEX `idx_read_status`(`read_status`) USING BTREE,
  CONSTRAINT `fk_notification_user` FOREIGN KEY (`receiver_id`) REFERENCES `ums_users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户通知表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ums_notifications
-- ----------------------------
INSERT INTO `ums_notifications` VALUES (1, NULL, 0, '系统通知', 1, '欢迎注册WhaleTide商城', '尊敬的用户您好，欢迎注册WhaleTide商城，感谢您的支持!', 0, NULL, NULL, 1, '2023-01-01 10:00:00', '2023-01-01 00:00:00', '2023-01-01 10:00:00');
INSERT INTO `ums_notifications` VALUES (2, NULL, 0, '系统通知', 2, '欢迎注册WhaleTide商城', '尊敬的商家您好，欢迎注册WhaleTide商城，感谢您的支持!', 0, NULL, NULL, 1, '2023-01-01 11:00:00', '2023-01-01 00:00:00', '2023-01-01 11:00:00');
INSERT INTO `ums_notifications` VALUES (3, NULL, 0, '系统通知', 3, '欢迎注册WhaleTide商城', '尊敬的用户您好，欢迎注册WhaleTide商城，感谢您的支持!', 0, NULL, NULL, 0, NULL, '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ums_notifications` VALUES (4, NULL, 0, '订单系统', 1, '订单已创建', '您的订单(订单号:WTO20230102001)已创建成功，请尽快付款。', 1, 1001, 1, 1, '2023-01-02 10:30:00', '2023-01-02 10:00:00', '2023-01-02 10:30:00');
INSERT INTO `ums_notifications` VALUES (5, 2, 2, '李大卖的小店', 1, '订单已发货', '您的订单(订单号:WTO20230102001)已发货，物流单号:SF1234567890。', 3, 1001, 1, 0, NULL, '2023-01-03 09:00:00', '2023-01-03 09:00:00');

-- ----------------------------
-- Table structure for ums_user_addresses
-- ----------------------------
DROP TABLE IF EXISTS `ums_user_addresses`;
CREATE TABLE `ums_user_addresses`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '地址ID',
  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
  `receiver_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人电话',
  `province` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '省份',
  `city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '城市',
  `district` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '区县',
  `detail_address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '详细地址',
  `postal_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮政编码',
  `default_status` tinyint(0) NULL DEFAULT 0 COMMENT '是否默认：0-否，1-是',
  `tag` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地址标签：家、公司等',
  `is_deleted` tinyint(0) NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  CONSTRAINT `fk_address_user` FOREIGN KEY (`user_id`) REFERENCES `ums_users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户地址表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ums_user_addresses
-- ----------------------------
INSERT INTO `ums_user_addresses` VALUES (1, 1, '张明', '13800138000', '北京市', '北京市', '朝阳区', '朝阳路100号', '100000', 1, '家', 0, '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ums_user_addresses` VALUES (2, 1, '张明', '13800138000', '北京市', '北京市', '海淀区', '中关村科技园82号', '100080', 0, '公司', 0, '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ums_user_addresses` VALUES (3, 3, '王红', '13700137000', '广州市', '广州市', '天河区', '天河路200号', '510000', 1, '家', 0, '2023-01-01 00:00:00', '2023-01-01 00:00:00');

-- ----------------------------
-- Table structure for ums_user_coupons
-- ----------------------------
DROP TABLE IF EXISTS `ums_user_coupons`;
CREATE TABLE `ums_user_coupons`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
  `coupon_id` bigint(0) NOT NULL COMMENT '优惠券ID',
  `coupon_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '优惠券码',
  `coupon_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '优惠券名称',
  `amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠金额',
  `discount` decimal(5, 2) NULL DEFAULT NULL COMMENT '折扣',
  `min_point` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '使用门槛',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `status` tinyint(0) NULL DEFAULT 0 COMMENT '状态：0-未使用，1-已使用，2-已过期',
  `use_time` datetime(0) NULL DEFAULT NULL COMMENT '使用时间',
  `order_id` bigint(0) NULL DEFAULT NULL COMMENT '订单ID',
  `order_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订单编号',
  `get_type` tinyint(0) NULL DEFAULT 0 COMMENT '获取类型：0-主动领取，1-后台发放，2-活动赠送',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '获取时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_coupon_id`(`coupon_id`) USING BTREE,
  CONSTRAINT `fk_user_coupon_user` FOREIGN KEY (`user_id`) REFERENCES `ums_users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户优惠券表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ums_user_favorites
-- ----------------------------
DROP TABLE IF EXISTS `ums_user_favorites`;
CREATE TABLE `ums_user_favorites`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
  `product_id` bigint(0) NOT NULL COMMENT '商品ID',
  `product_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品名称',
  `product_image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品图片',
  `product_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品价格',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '收藏时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_product`(`user_id`, `product_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_product_id`(`product_id`) USING BTREE,
  CONSTRAINT `fk_favorite_user` FOREIGN KEY (`user_id`) REFERENCES `ums_users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户收藏表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ums_user_login_logs
-- ----------------------------
DROP TABLE IF EXISTS `ums_user_login_logs`;
CREATE TABLE `ums_user_login_logs`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `user_id` bigint(0) NULL DEFAULT NULL COMMENT '用户ID',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '登录IP',
  `location` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '登录地点',
  `device` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '登录设备',
  `user_agent` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '浏览器类型',
  `login_type` tinyint(0) NULL DEFAULT 0 COMMENT '登录类型：0-普通登录，1-短信登录，2-第三方登录',
  `login_source` tinyint(0) NULL DEFAULT 0 COMMENT '登录来源：0-PC，1-APP，2-小程序，3-H5',
  `status` tinyint(0) NULL DEFAULT 0 COMMENT '登录状态：0-失败，1-成功',
  `message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '提示消息',
  `login_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  CONSTRAINT `fk_login_log_user` FOREIGN KEY (`user_id`) REFERENCES `ums_users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户登录日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ums_user_roles
-- ----------------------------
DROP TABLE IF EXISTS `ums_user_roles`;
CREATE TABLE `ums_user_roles`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
  `role_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `role_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色编码',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  CONSTRAINT `fk_user_role_user` FOREIGN KEY (`user_id`) REFERENCES `ums_users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ums_user_roles
-- ----------------------------
INSERT INTO `ums_user_roles` VALUES (1, 1, '普通会员', 'NORMAL_MEMBER', '普通会员', '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ums_user_roles` VALUES (2, 2, '商家', 'MERCHANT', '商家角色', '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ums_user_roles` VALUES (3, 3, '普通会员', 'NORMAL_MEMBER', '普通会员', '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ums_user_roles` VALUES (4, 4, '商家', 'MERCHANT', '商家角色', '2023-01-01 00:00:00', '2023-01-01 00:00:00');

-- ----------------------------
-- Table structure for ums_user_search_history
-- ----------------------------
DROP TABLE IF EXISTS `ums_user_search_history`;
CREATE TABLE `ums_user_search_history`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '搜索ID',
  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
  `keyword` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '搜索关键词',
  `search_count` int(0) NULL DEFAULT 1 COMMENT '搜索次数',
  `last_search_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '最后搜索时间',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_keyword`(`user_id`, `keyword`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  CONSTRAINT `fk_search_user` FOREIGN KEY (`user_id`) REFERENCES `ums_users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户搜索历史表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ums_users
-- ----------------------------
DROP TABLE IF EXISTS `ums_users`;
CREATE TABLE `ums_users`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `nickname` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `real_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `gender` tinyint(0) NULL DEFAULT 0 COMMENT '性别：0-未知，1-男，2-女',
  `birth` date NULL DEFAULT NULL COMMENT '生日',
  `avatar` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `status` tinyint(0) NULL DEFAULT 1 COMMENT '启用状态：0-禁用，1-启用',
  `status_reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '状态原因',
  `register_source` tinyint(0) NULL DEFAULT 0 COMMENT '注册来源：0-PC，1-APP，2-小程序，3-H5',
  `last_login_time` datetime(0) NULL DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最后登录IP',
  `is_merchant` tinyint(0) NULL DEFAULT 0 COMMENT '是否商家：0-否，1-是',
  `level` int(0) NULL DEFAULT 0 COMMENT '会员等级',
  `integration` int(0) NULL DEFAULT 0 COMMENT '积分',
  `growth` int(0) NULL DEFAULT 0 COMMENT '成长值',
  `region` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所在地区',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `is_deleted` tinyint(0) NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '注册时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username`) USING BTREE,
  UNIQUE INDEX `uk_phone`(`phone`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_is_merchant`(`is_merchant`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ums_users
-- ----------------------------
INSERT INTO `ums_users` VALUES (1, 'user123', '$2a$10$NZ5o7r2E.ayT2ZoxgjlI.eJ6OEYqjH7INR/F.mXDbjZJi9HF0YCVG', '13800138000', 'user123@example.com', '张小明', '张明', 1, '1990-01-01', 'https://example.com/avatar/1.jpg', 1, NULL, 0, '2023-01-02 10:00:00', '192.168.1.1', 0, 0, 100, 20, '北京市', NULL, 0, '2023-01-01 00:00:00', '2023-01-02 10:00:00');
INSERT INTO `ums_users` VALUES (2, 'merchant456', '$2a$10$NZ5o7r2E.ayT2ZoxgjlI.eJ6OEYqjH7INR/F.mXDbjZJi9HF0YCVG', '13900139000', 'merchant456@example.com', '李大卖', '李卖', 1, '1985-05-05', 'https://example.com/avatar/2.jpg', 1, NULL, 1, '2023-01-02 11:00:00', '192.168.1.2', 1, 1, 200, 50, '上海市', NULL, 0, '2023-01-01 00:00:00', '2023-01-02 11:00:00');
INSERT INTO `ums_users` VALUES (3, 'user789', '$2a$10$NZ5o7r2E.ayT2ZoxgjlI.eJ6OEYqjH7INR/F.mXDbjZJi9HF0YCVG', '13700137000', 'user789@example.com', '王小红', '王红', 2, '1995-10-10', 'https://example.com/avatar/3.jpg', 1, NULL, 2, '2023-01-02 12:00:00', '192.168.1.3', 0, 0, 50, 10, '广州市', NULL, 0, '2023-01-01 00:00:00', '2023-01-02 12:00:00');
INSERT INTO `ums_users` VALUES (4, 'merchant321', '$2a$10$NZ5o7r2E.ayT2ZoxgjlI.eJ6OEYqjH7INR/F.mXDbjZJi9HF0YCVG', '13600136000', 'merchant321@example.com', '赵掌柜', '赵柜', 1, '1980-12-12', 'https://example.com/avatar/4.jpg', 1, NULL, 0, '2023-01-02 13:00:00', '192.168.1.4', 1, 2, 300, 100, '深圳市', NULL, 0, '2023-01-01 00:00:00', '2023-01-02 13:00:00');
INSERT INTO `ums_users` VALUES (5, 'lisi', '$2a$10$NZ5o7r2E.ayT2ZoxgjlI.eJ6OEYqjH7INR/F.mXDbjZJi9HF0YCVG', '18061581841', NULL, 'lisi', NULL, NULL, NULL, NULL, 1, NULL, NULL, '2018-11-12 14:12:38', NULL, 0, 4, NULL, NULL, NULL, NULL, 0, '2018-11-12 14:12:38', '2025-03-15 16:57:32');
INSERT INTO `ums_users` VALUES (6, 'wangwu', '$2a$10$NZ5o7r2E.ayT2ZoxgjlI.eJ6OEYqjH7INR/F.mXDbjZJi9HF0YCVG', '18061581842', NULL, 'wangwu', NULL, NULL, NULL, NULL, 1, NULL, NULL, '2018-11-12 14:13:09', NULL, 0, 4, NULL, NULL, NULL, NULL, 0, '2018-11-12 14:13:09', '2025-03-15 16:57:32');
INSERT INTO `ums_users` VALUES (7, 'lion', '$2a$10$NZ5o7r2E.ayT2ZoxgjlI.eJ6OEYqjH7INR/F.mXDbjZJi9HF0YCVG', '18061581845', NULL, 'lion', NULL, NULL, NULL, NULL, 1, NULL, NULL, '2018-11-12 14:21:39', NULL, 0, 4, NULL, NULL, NULL, NULL, 0, '2018-11-12 14:21:39', '2025-03-15 16:57:32');
INSERT INTO `ums_users` VALUES (8, 'shari', '$2a$10$NZ5o7r2E.ayT2ZoxgjlI.eJ6OEYqjH7INR/F.mXDbjZJi9HF0YCVG', '18061581844', NULL, 'shari', NULL, NULL, NULL, NULL, 1, NULL, NULL, '2018-11-12 14:22:00', NULL, 0, 4, NULL, NULL, NULL, NULL, 0, '2018-11-12 14:22:00', '2025-03-15 16:57:32');
INSERT INTO `ums_users` VALUES (9, 'aewen', '$2a$10$NZ5o7r2E.ayT2ZoxgjlI.eJ6OEYqjH7INR/F.mXDbjZJi9HF0YCVG', '18061581843', NULL, 'aewen', NULL, NULL, NULL, NULL, 1, NULL, NULL, '2018-11-12 14:22:55', NULL, 0, 4, NULL, NULL, NULL, NULL, 0, '2018-11-12 14:22:55', '2025-03-15 16:57:32');
INSERT INTO `ums_users` VALUES (10, 'guest', '$2a$10$WQiD4RzEs1iJVWU.2HVu8OdSlExJHWKmwndaw3SUfMyqfKZmXe1vq', '18911111111', NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, '2020-03-14 14:52:18', NULL, 0, 4, NULL, NULL, NULL, NULL, 0, '2020-03-14 14:52:18', '2025-03-15 16:57:32');
INSERT INTO `ums_users` VALUES (11, 'member', '$2a$10$Q08uzqvtPj61NnpYQZsVvOnyilJ3AU4VdngAcJFGvPhEeqhhC.hhS', '18961511111', NULL, 'member', NULL, 1, '2009-06-01', 'https://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/icon/github_icon_02.png', 1, NULL, NULL, '2023-05-11 15:22:38', NULL, 0, 4, 5000, 1000, '上海', NULL, 0, '2023-05-11 15:22:38', '2025-03-15 16:57:32');

SET FOREIGN_KEY_CHECKS = 1;
