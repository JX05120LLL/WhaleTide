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

 Date: 16/04/2025 17:19:44
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
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员与角色关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ams_admin_role_relations
-- ----------------------------
INSERT INTO `ams_admin_role_relations` VALUES (9, 12, 1, '2025-03-16 14:00:47');
INSERT INTO `ams_admin_role_relations` VALUES (10, 13, 2, '2025-03-16 14:01:12');
INSERT INTO `ams_admin_role_relations` VALUES (11, 14, 3, '2025-03-16 14:01:18');

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
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ams_admins
-- ----------------------------
INSERT INTO `ams_admins` VALUES (12, 'admin', '$2a$10$reSZltiq/qBlHZf3kzEyAeVuJo5iaI0GDjryct15VftfQwieYCTKK', '超级管理员', '123456', NULL, NULL, 0, 1, '2025-03-30 09:01:38', NULL, '超级管理员，拥有所有权限', 1, NULL, NULL, 0, '2025-03-15 18:28:59', '2025-03-17 09:51:25');
INSERT INTO `ams_admins` VALUES (13, 'merchant', '123456', '商家', NULL, NULL, NULL, 0, 1, '2025-03-16 15:10:25', NULL, '拥有 商品 和订单 模块 权限', 0, NULL, NULL, 0, '2025-03-15 18:28:59', '2025-03-16 14:36:29');
INSERT INTO `ams_admins` VALUES (14, 'user', '123456', '普通用户', NULL, NULL, NULL, 0, 1, NULL, NULL, NULL, 0, NULL, NULL, 0, '2025-03-15 18:28:59', '2025-03-15 18:28:59');
INSERT INTO `ams_admins` VALUES (17, 'admin1', '$2a$10$reSZltiq/qBlHZf3kzEyAeVuJo5iaI0GDjryct15VftfQwieYCTKK', NULL, '', NULL, NULL, 0, 1, '2025-03-25 16:35:16', NULL, 'testNote', 0, NULL, NULL, 0, '2025-03-17 09:48:23', '2025-03-17 09:48:23');

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
) ENGINE = InnoDB AUTO_INCREMENT = 42 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ams_menus
-- ----------------------------
INSERT INTO `ams_menus` VALUES (20, 0, '首页', 0, 0, 'home', '/home', 'Layout', '/home', 0, 1, '系统首页', '2025-03-15 18:44:35', '2025-03-18 14:29:06');
INSERT INTO `ams_menus` VALUES (21, 0, '商品', 0, 10, 'product', '/pms', 'Layout', '/pms/product', 0, 1, '商品管理模块', '2025-03-15 18:44:35', '2025-03-15 23:12:57');
INSERT INTO `ams_menus` VALUES (22, 0, '订单', 0, 20, 'order', '/oms', 'Layout', '/oms/order', 0, 1, '订单管理模块', '2025-03-15 18:44:35', '2025-03-15 18:44:35');
INSERT INTO `ams_menus` VALUES (23, 0, '营销', 0, 30, 'sms', '/sms', 'Layout', '/sms/flash', 0, 1, '营销管理模块', '2025-03-15 18:44:35', '2025-03-15 23:13:57');
INSERT INTO `ams_menus` VALUES (24, 0, '权限', 0, 40, 'ums', '/ums', 'Layout', '/ums/admin', 0, 1, '权限管理模块', '2025-03-15 18:44:35', '2025-03-15 23:13:58');
INSERT INTO `ams_menus` VALUES (25, 1, '仪表盘', 1, 1, 'dashboard', 'dashboard', 'home/index', NULL, 0, 1, '系统仪表盘', '2025-03-15 18:44:35', '2025-03-15 18:44:35');
INSERT INTO `ams_menus` VALUES (26, 2, '商品列表', 1, 1, 'product-list', 'product', 'pms/product/index', NULL, 0, 1, '商品列表', '2025-03-15 18:44:35', '2025-03-15 18:44:35');
INSERT INTO `ams_menus` VALUES (27, 2, '添加商品', 1, 2, 'product-add', 'addProduct', 'pms/product/add', NULL, 0, 1, '添加商品', '2025-03-15 18:44:35', '2025-03-15 18:44:35');
INSERT INTO `ams_menus` VALUES (28, 2, '商品分类', 1, 3, 'product-cate', 'productCate', 'pms/productCate/index', NULL, 0, 1, '商品分类', '2025-03-15 18:44:35', '2025-03-15 18:44:35');
INSERT INTO `ams_menus` VALUES (29, 2, '品牌管理', 1, 4, 'product-brand', 'brand', 'pms/brand/index', NULL, 0, 1, '品牌管理', '2025-03-15 18:44:35', '2025-03-15 18:44:35');
INSERT INTO `ams_menus` VALUES (30, 2, '商品类型', 1, 5, 'product-attr', 'productAttr', 'pms/productAttr/index', NULL, 0, 1, '商品类型', '2025-03-15 18:44:35', '2025-03-15 18:44:35');
INSERT INTO `ams_menus` VALUES (31, 2, '修改商品', 1, 6, 'product-update', 'updateProduct', 'pms/product/update', NULL, 1, 1, '修改商品', '2025-03-15 18:44:35', '2025-03-15 18:44:35');
INSERT INTO `ams_menus` VALUES (32, 3, '订单列表', 1, 1, 'product-list', 'order', 'oms/order/index', NULL, 0, 1, '订单列表', '2025-03-15 18:44:35', '2025-03-15 18:44:35');
INSERT INTO `ams_menus` VALUES (33, 3, '订单设置', 1, 2, 'order-setting', 'orderSetting', 'oms/order/setting', NULL, 0, 1, '订单设置', '2025-03-15 18:44:35', '2025-03-15 18:44:35');
INSERT INTO `ams_menus` VALUES (34, 3, '退货申请处理', 1, 3, 'order-return', 'returnApply', 'oms/apply/index', NULL, 0, 1, '退货申请处理', '2025-03-15 18:44:35', '2025-03-15 18:44:35');
INSERT INTO `ams_menus` VALUES (35, 3, '退货原因设置', 1, 4, 'order-return-reason', 'returnReason', 'oms/apply/reason', NULL, 0, 1, '退货原因设置', '2025-03-15 18:44:35', '2025-03-15 18:44:35');
INSERT INTO `ams_menus` VALUES (36, 4, '秒杀活动列表', 1, 1, 'sms-flash', 'flash', 'sms/flash/index', NULL, 0, 1, '秒杀活动列表', '2025-03-15 18:44:35', '2025-03-15 18:44:35');
INSERT INTO `ams_menus` VALUES (37, 4, '优惠券列表', 1, 2, 'sms-coupon', 'coupon', 'sms/coupon/index', NULL, 0, 1, '优惠券列表', '2025-03-15 18:44:35', '2025-03-15 18:44:35');
INSERT INTO `ams_menus` VALUES (38, 4, '添加优惠券', 1, 3, 'sms-add', 'addCoupon', 'sms/coupon/add', NULL, 1, 1, '添加优惠券', '2025-03-15 18:44:35', '2025-03-15 18:44:35');
INSERT INTO `ams_menus` VALUES (39, 5, '用户列表', 1, 1, 'ums-admin', 'admin', 'ums/admin/index', NULL, 0, 1, '用户列表', '2025-03-15 18:44:35', '2025-03-15 18:44:35');
INSERT INTO `ams_menus` VALUES (40, 5, '角色列表', 1, 2, 'ums-role', 'role', 'ums/role/index', NULL, 0, 1, '角色列表', '2025-03-15 18:44:35', '2025-03-15 18:44:35');
INSERT INTO `ams_menus` VALUES (41, 5, '菜单列表', 1, 3, 'ums-menu', 'menu', 'ums/menu/index', NULL, 0, 1, '菜单列表', '2025-03-15 18:44:35', '2025-03-15 18:44:35');
INSERT INTO `ams_menus` VALUES (42, 5, '资源列表', 1, 4, 'ums-resource', 'resource', 'ums/resource/index', NULL, 0, 1, '资源列表', '2025-03-15 18:44:35', '2025-03-15 18:44:35');

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
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ams_permissions
-- ----------------------------
INSERT INTO `ams_permissions` VALUES (1, '查看仪表盘', 'DASHBOARD_VIEW', 2, 0, '/dashboard', NULL, NULL, NULL, 0, 0, 1, '只允许查看仪表盘', '2025-03-16 14:21:52', '2025-03-16 14:21:52');
INSERT INTO `ams_permissions` VALUES (2, '商品只读', 'PRODUCT_READ_ONLY', 2, 0, '/product', NULL, NULL, NULL, 0, 0, 1, '只允许查看商品，不允许修改', '2025-03-16 14:21:52', '2025-03-16 14:21:52');

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
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '资源分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ams_resource_categories
-- ----------------------------
INSERT INTO `ams_resource_categories` VALUES (9, '商品模块', 1, '2025-03-15 18:45:34', '2025-03-15 18:45:34');
INSERT INTO `ams_resource_categories` VALUES (10, '订单模块', 2, '2025-03-15 18:45:34', '2025-03-15 18:45:34');
INSERT INTO `ams_resource_categories` VALUES (11, '营销模块', 3, '2025-03-15 18:45:34', '2025-03-15 18:45:34');
INSERT INTO `ams_resource_categories` VALUES (12, '权限模块', 4, '2025-03-15 18:45:34', '2025-03-15 18:45:34');

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
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '资源表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ams_resources
-- ----------------------------
INSERT INTO `ams_resources` VALUES (11, 1, '商品列表', '/product/list', 'GET', '获取商品列表', 1, '2025-03-15 18:45:34', '2025-03-15 18:45:34');
INSERT INTO `ams_resources` VALUES (12, 1, '添加商品', '/product/create', 'POST', '创建新商品', 1, '2025-03-15 18:45:34', '2025-03-15 18:45:34');
INSERT INTO `ams_resources` VALUES (13, 1, '修改商品', '/product/update', 'POST', '更新商品信息', 1, '2025-03-15 18:45:34', '2025-03-15 18:45:34');
INSERT INTO `ams_resources` VALUES (14, 1, '删除商品', '/product/delete', 'POST', '删除商品', 1, '2025-03-15 18:45:34', '2025-03-15 18:45:34');
INSERT INTO `ams_resources` VALUES (15, 1, '商品详情', '/product/detail', 'GET', '获取商品详情', 1, '2025-03-15 18:45:34', '2025-03-15 18:45:34');
INSERT INTO `ams_resources` VALUES (16, 1, '商品分类列表', '/productCate/list', 'GET', '获取商品分类', 1, '2025-03-15 18:45:34', '2025-03-15 18:45:34');
INSERT INTO `ams_resources` VALUES (17, 1, '品牌列表', '/brand/list', 'GET', '获取品牌列表', 1, '2025-03-15 18:45:34', '2025-03-15 18:45:34');
INSERT INTO `ams_resources` VALUES (18, 2, '订单列表', '/order/list', 'GET', '获取订单列表', 1, '2025-03-15 18:45:34', '2025-03-15 18:45:34');
INSERT INTO `ams_resources` VALUES (19, 2, '订单详情', '/order/detail', 'GET', '获取订单详情', 1, '2025-03-15 18:45:34', '2025-03-15 18:45:34');
INSERT INTO `ams_resources` VALUES (20, 2, '订单发货', '/order/delivery', 'POST', '订单发货', 1, '2025-03-15 18:45:34', '2025-03-15 18:45:34');
INSERT INTO `ams_resources` VALUES (21, 2, '关闭订单', '/order/close', 'POST', '关闭订单', 1, '2025-03-15 18:45:34', '2025-03-15 18:45:34');
INSERT INTO `ams_resources` VALUES (22, 2, '订单设置', '/order/setting', 'GET', '获取订单设置', 1, '2025-03-15 18:45:34', '2025-03-15 18:45:34');
INSERT INTO `ams_resources` VALUES (23, 2, '退货申请列表', '/returnApply/list', 'GET', '获取退货申请列表', 1, '2025-03-15 18:45:34', '2025-03-15 18:45:34');
INSERT INTO `ams_resources` VALUES (24, 3, '秒杀活动列表', '/flash/list', 'GET', '获取秒杀活动列表', 1, '2025-03-15 18:45:34', '2025-03-15 18:45:34');
INSERT INTO `ams_resources` VALUES (25, 3, '添加秒杀活动', '/flash/create', 'POST', '创建秒杀活动', 1, '2025-03-15 18:45:34', '2025-03-15 18:45:34');
INSERT INTO `ams_resources` VALUES (26, 3, '优惠券列表', '/coupon/list', 'GET', '获取优惠券列表', 1, '2025-03-15 18:45:34', '2025-03-15 18:45:34');
INSERT INTO `ams_resources` VALUES (27, 3, '添加优惠券', '/coupon/create', 'POST', '创建优惠券', 1, '2025-03-15 18:45:34', '2025-03-15 18:45:34');
INSERT INTO `ams_resources` VALUES (28, 4, '用户列表', '/admin/list', 'GET', '获取用户列表', 1, '2025-03-15 18:45:34', '2025-03-15 18:45:34');
INSERT INTO `ams_resources` VALUES (29, 4, '添加用户', '/admin/create', 'POST', '创建用户', 1, '2025-03-15 18:45:34', '2025-03-15 18:45:34');
INSERT INTO `ams_resources` VALUES (30, 4, '角色列表', '/role/list', 'GET', '获取角色列表', 1, '2025-03-15 18:45:34', '2025-03-15 18:45:34');
INSERT INTO `ams_resources` VALUES (31, 4, '添加角色', '/role/create', 'POST', '创建角色', 1, '2025-03-15 18:45:34', '2025-03-15 18:45:34');
INSERT INTO `ams_resources` VALUES (32, 4, '菜单列表', '/menu/list', 'GET', '获取菜单列表', 1, '2025-03-15 18:45:34', '2025-03-15 18:45:34');
INSERT INTO `ams_resources` VALUES (33, 4, '资源列表', '/resource/list', 'GET', '获取资源列表', 1, '2025-03-15 18:45:34', '2025-03-15 18:45:34');

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
) ENGINE = InnoDB AUTO_INCREMENT = 153 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色与菜单关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ams_role_menu_relations
-- ----------------------------
INSERT INTO `ams_role_menu_relations` VALUES (116, 1, 20, '2025-03-16 14:21:52');
INSERT INTO `ams_role_menu_relations` VALUES (117, 1, 21, '2025-03-16 14:21:52');
INSERT INTO `ams_role_menu_relations` VALUES (118, 1, 22, '2025-03-16 14:21:52');
INSERT INTO `ams_role_menu_relations` VALUES (119, 1, 23, '2025-03-16 14:21:52');
INSERT INTO `ams_role_menu_relations` VALUES (120, 1, 24, '2025-03-16 14:21:52');
INSERT INTO `ams_role_menu_relations` VALUES (121, 1, 25, '2025-03-16 14:21:52');
INSERT INTO `ams_role_menu_relations` VALUES (122, 1, 26, '2025-03-16 14:21:52');
INSERT INTO `ams_role_menu_relations` VALUES (123, 1, 27, '2025-03-16 14:21:52');
INSERT INTO `ams_role_menu_relations` VALUES (124, 1, 28, '2025-03-16 14:21:52');
INSERT INTO `ams_role_menu_relations` VALUES (125, 1, 29, '2025-03-16 14:21:52');
INSERT INTO `ams_role_menu_relations` VALUES (126, 1, 30, '2025-03-16 14:21:52');
INSERT INTO `ams_role_menu_relations` VALUES (127, 1, 31, '2025-03-16 14:21:52');
INSERT INTO `ams_role_menu_relations` VALUES (128, 1, 32, '2025-03-16 14:21:52');
INSERT INTO `ams_role_menu_relations` VALUES (129, 1, 33, '2025-03-16 14:21:52');
INSERT INTO `ams_role_menu_relations` VALUES (130, 1, 34, '2025-03-16 14:21:52');
INSERT INTO `ams_role_menu_relations` VALUES (131, 1, 35, '2025-03-16 14:21:52');
INSERT INTO `ams_role_menu_relations` VALUES (132, 1, 36, '2025-03-16 14:21:52');
INSERT INTO `ams_role_menu_relations` VALUES (133, 1, 37, '2025-03-16 14:21:52');
INSERT INTO `ams_role_menu_relations` VALUES (134, 1, 38, '2025-03-16 14:21:52');
INSERT INTO `ams_role_menu_relations` VALUES (135, 1, 39, '2025-03-16 14:21:52');
INSERT INTO `ams_role_menu_relations` VALUES (136, 1, 40, '2025-03-16 14:21:52');
INSERT INTO `ams_role_menu_relations` VALUES (137, 1, 41, '2025-03-16 14:21:52');
INSERT INTO `ams_role_menu_relations` VALUES (138, 2, 20, '2025-03-16 14:21:52');
INSERT INTO `ams_role_menu_relations` VALUES (139, 2, 21, '2025-03-16 14:21:52');
INSERT INTO `ams_role_menu_relations` VALUES (140, 2, 22, '2025-03-16 14:21:52');
INSERT INTO `ams_role_menu_relations` VALUES (141, 2, 25, '2025-03-16 14:21:52');
INSERT INTO `ams_role_menu_relations` VALUES (142, 2, 26, '2025-03-16 14:21:52');
INSERT INTO `ams_role_menu_relations` VALUES (143, 2, 27, '2025-03-16 14:21:52');
INSERT INTO `ams_role_menu_relations` VALUES (144, 2, 28, '2025-03-16 14:21:52');
INSERT INTO `ams_role_menu_relations` VALUES (145, 2, 29, '2025-03-16 14:21:52');
INSERT INTO `ams_role_menu_relations` VALUES (146, 2, 30, '2025-03-16 14:21:52');
INSERT INTO `ams_role_menu_relations` VALUES (147, 2, 31, '2025-03-16 14:21:52');
INSERT INTO `ams_role_menu_relations` VALUES (148, 2, 32, '2025-03-16 14:21:52');
INSERT INTO `ams_role_menu_relations` VALUES (149, 2, 33, '2025-03-16 14:21:52');
INSERT INTO `ams_role_menu_relations` VALUES (150, 2, 34, '2025-03-16 14:21:52');
INSERT INTO `ams_role_menu_relations` VALUES (151, 2, 35, '2025-03-16 14:21:52');
INSERT INTO `ams_role_menu_relations` VALUES (152, 3, 20, '2025-03-16 14:21:52');
INSERT INTO `ams_role_menu_relations` VALUES (153, 3, 25, '2025-03-16 14:21:52');

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色与权限关系表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 132 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色与资源关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ams_role_resource_relations
-- ----------------------------
INSERT INTO `ams_role_resource_relations` VALUES (95, 1, 11, '2025-03-16 14:21:52');
INSERT INTO `ams_role_resource_relations` VALUES (96, 1, 12, '2025-03-16 14:21:52');
INSERT INTO `ams_role_resource_relations` VALUES (97, 1, 13, '2025-03-16 14:21:52');
INSERT INTO `ams_role_resource_relations` VALUES (98, 1, 14, '2025-03-16 14:21:52');
INSERT INTO `ams_role_resource_relations` VALUES (99, 1, 15, '2025-03-16 14:21:52');
INSERT INTO `ams_role_resource_relations` VALUES (100, 1, 16, '2025-03-16 14:21:52');
INSERT INTO `ams_role_resource_relations` VALUES (101, 1, 17, '2025-03-16 14:21:52');
INSERT INTO `ams_role_resource_relations` VALUES (102, 1, 18, '2025-03-16 14:21:52');
INSERT INTO `ams_role_resource_relations` VALUES (103, 1, 19, '2025-03-16 14:21:52');
INSERT INTO `ams_role_resource_relations` VALUES (104, 1, 20, '2025-03-16 14:21:52');
INSERT INTO `ams_role_resource_relations` VALUES (105, 1, 21, '2025-03-16 14:21:52');
INSERT INTO `ams_role_resource_relations` VALUES (106, 1, 22, '2025-03-16 14:21:52');
INSERT INTO `ams_role_resource_relations` VALUES (107, 1, 23, '2025-03-16 14:21:52');
INSERT INTO `ams_role_resource_relations` VALUES (108, 1, 24, '2025-03-16 14:21:52');
INSERT INTO `ams_role_resource_relations` VALUES (109, 1, 25, '2025-03-16 14:21:52');
INSERT INTO `ams_role_resource_relations` VALUES (110, 1, 26, '2025-03-16 14:21:52');
INSERT INTO `ams_role_resource_relations` VALUES (111, 1, 27, '2025-03-16 14:21:52');
INSERT INTO `ams_role_resource_relations` VALUES (112, 1, 28, '2025-03-16 14:21:52');
INSERT INTO `ams_role_resource_relations` VALUES (113, 1, 29, '2025-03-16 14:21:52');
INSERT INTO `ams_role_resource_relations` VALUES (114, 1, 30, '2025-03-16 14:21:52');
INSERT INTO `ams_role_resource_relations` VALUES (115, 1, 31, '2025-03-16 14:21:52');
INSERT INTO `ams_role_resource_relations` VALUES (116, 1, 32, '2025-03-16 14:21:52');
INSERT INTO `ams_role_resource_relations` VALUES (117, 1, 33, '2025-03-16 14:21:52');
INSERT INTO `ams_role_resource_relations` VALUES (118, 2, 11, '2025-03-16 14:21:52');
INSERT INTO `ams_role_resource_relations` VALUES (119, 2, 12, '2025-03-16 14:21:52');
INSERT INTO `ams_role_resource_relations` VALUES (120, 2, 13, '2025-03-16 14:21:52');
INSERT INTO `ams_role_resource_relations` VALUES (121, 2, 14, '2025-03-16 14:21:52');
INSERT INTO `ams_role_resource_relations` VALUES (122, 2, 15, '2025-03-16 14:21:52');
INSERT INTO `ams_role_resource_relations` VALUES (123, 2, 16, '2025-03-16 14:21:52');
INSERT INTO `ams_role_resource_relations` VALUES (124, 2, 17, '2025-03-16 14:21:52');
INSERT INTO `ams_role_resource_relations` VALUES (125, 2, 18, '2025-03-16 14:21:52');
INSERT INTO `ams_role_resource_relations` VALUES (126, 2, 19, '2025-03-16 14:21:52');
INSERT INTO `ams_role_resource_relations` VALUES (127, 2, 20, '2025-03-16 14:21:52');
INSERT INTO `ams_role_resource_relations` VALUES (128, 2, 21, '2025-03-16 14:21:52');
INSERT INTO `ams_role_resource_relations` VALUES (129, 2, 22, '2025-03-16 14:21:52');
INSERT INTO `ams_role_resource_relations` VALUES (130, 2, 23, '2025-03-16 14:21:52');
INSERT INTO `ams_role_resource_relations` VALUES (131, 3, 11, '2025-03-16 14:21:52');
INSERT INTO `ams_role_resource_relations` VALUES (132, 3, 15, '2025-03-16 14:21:52');

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
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ams_roles
-- ----------------------------
INSERT INTO `ams_roles` VALUES (1, '超级管理员', 'SUPER_ADMIN', '拥有所有操作权限，可以管理整个系统', 0, 1, 0, '2025-03-16 13:58:05', '2025-03-16 13:58:05');
INSERT INTO `ams_roles` VALUES (2, '商家', 'MERCHANT', '拥有商品、订单、库存、营销管理权限', 1, 1, 0, '2025-03-16 13:58:05', '2025-03-16 13:58:05');
INSERT INTO `ams_roles` VALUES (3, '普通用户', 'NORMAL_USER', '只具有基本查看权限，无修改权限', 2, 1, 0, '2025-03-16 13:58:05', '2025-03-24 18:13:55');

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
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '购物车表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oms_cart_items
-- ----------------------------
INSERT INTO `oms_cart_items` VALUES (4, 13, 16, 'Apple MacBook Pro 16', 'https://store.storeimages.cdn-apple.com/8756/as-images.apple.com/is/mbp16-witb-spaceblack-202410?wid=1216&hei=784&fmt=p-jpg&qlt=95&.v=1728330927913', 29, '机身', 14999.00, 1, 1, '2025-04-05 10:45:00', '2025-04-05 10:45:00');
INSERT INTO `oms_cart_items` VALUES (5, 13, 20, '戴森吸尘器V15 Detect', 'https://m.media-amazon.com/images/I/619XJLzSXJL._AC_SX679_.jpg', 26, 'Pro版', 4999.00, 1, 1, '2025-04-05 11:30:00', '2025-04-05 11:30:00');
INSERT INTO `oms_cart_items` VALUES (6, 14, 17, 'LG C3 OLED电视 65英寸', 'https://www.lg.com/cn/images/tvs/md07572084/gallery/1100-1.jpg', 25, '标准版', 13999.00, 1, 1, '2025-04-06 14:20:00', '2025-04-06 14:20:00');
INSERT INTO `oms_cart_items` VALUES (7, 14, 23, '华为Watch GT 4智能手表', 'https://consumer.huawei.com/content/dam/huawei-cbg-site/cn/mkt/pdp/wearables/watch-gt4-new/images/sec1/huawei-watch-gt4-2x.jpg', 22, '蓝色', 1299.00, 1, 1, '2025-04-06 14:25:00', '2025-04-06 14:25:00');
INSERT INTO `oms_cart_items` VALUES (8, 14, 19, '佳能EOS R5微单相机', 'https://www.canon.com.cn/Upload/product/74XRU3SGAU/1589531788.jpg', 30, '标准镜头套装', 17999.00, 1, 0, '2025-04-06 15:10:00', '2025-04-06 15:10:00');
INSERT INTO `oms_cart_items` VALUES (15, 12, 18, '松下冰箱NR-W56S1', 'https://consumer.panasonic.cn/static/upload/image/20221127/1669524295425533.png', 0, '默认规格', 8999.00, 1, 0, '2025-04-06 17:25:05', '2025-04-06 17:25:05');

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单物流信息表' ROW_FORMAT = Dynamic;

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
  `sku_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'SKU编码',
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
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单商品项表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oms_order_items
-- ----------------------------
INSERT INTO `oms_order_items` VALUES (13, 12, '17439268686192195', 15, '三星Galaxy S24 Ultra', 'https://images.samsung.com/is/image/samsung/assets/tw/smartphones/galaxy-s24-ultra/images/galaxy-s24-ultra-highlights-kv.jpg?imbypass=true', '三星', 'SAMS-S24ULTRA', 5, 18, ' ', '黑色', 1, 9999.00, 9999.00, 9999.00, 0.00, 0.00, '', 0, 0, 0, 0, '2025-04-06 16:07:49');
INSERT INTO `oms_order_items` VALUES (14, 12, '17439268686192195', 21, '小米智能空气净化器Pro 3', 'https://c1.mifile.cn/f/i/16/chain/airpro//mj-gallery-01.jpg', '小米', 'MI-AIRPRO3', 16, 27, ' ', '3个灯泡套装', 3, 1299.00, 3897.00, 3897.00, 0.00, 0.00, '', 0, 0, 0, 0, '2025-04-06 16:07:49');
INSERT INTO `oms_order_items` VALUES (15, 13, '17439302252613443', 16, 'Apple MacBook Pro 16', 'https://store.storeimages.cdn-apple.com/8756/as-images.apple.com/is/mbp16-witb-spaceblack-202410?wid=1216&hei=784&fmt=p-jpg&qlt=95&.v=1728330927913', '苹果', 'APPL-MBP16-M2', 6, 0, ' ', '默认规格', 1, 19999.00, 19999.00, 19999.00, 0.00, 0.00, '', 0, 0, 0, 0, '2025-04-06 17:03:45');
INSERT INTO `oms_order_items` VALUES (16, 13, '17439302252613443', 23, '华为Watch GT 4智能手表', 'https://consumer.huawei.com/content/dam/huawei-cbg-site/cn/mkt/pdp/wearables/watch-gt4-new/images/sec1/huawei-watch-gt4-2x.jpg', '华为', 'HW-WATCHGT4', 10, 0, ' ', '默认规格', 1, 1499.00, 1499.00, 1499.00, 0.00, 0.00, '', 0, 0, 0, 0, '2025-04-06 17:03:45');
INSERT INTO `oms_order_items` VALUES (17, 13, '17439302252613443', 24, '小米扫地机器人X20', 'https://i02.appmifile.com/mi-com-product/fly-birds/xiaomi-robot-vacuum-x20/pc/d5c5dafb772de049aca294626748a712.jpg?f=webp', '小米', 'MI-ROBOT-X20', 16, 0, ' ', '默认规格', 1, 2999.00, 2999.00, 2999.00, 0.00, 0.00, '', 0, 0, 0, 0, '2025-04-06 17:03:45');
INSERT INTO `oms_order_items` VALUES (18, 23, '17439327771594760', 15, '三星Galaxy S24 Ultra', 'https://images.samsung.com/is/image/samsung/assets/tw/smartphones/galaxy-s24-ultra/images/galaxy-s24-ultra-highlights-kv.jpg?imbypass=true', '三星', 'SAMS-S24ULTRA', 5, 0, ' ', '默认规格', 1, 9999.00, 9999.00, 9999.00, 0.00, 0.00, '', 0, 0, 0, 0, '2025-04-06 17:46:17');

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
  `action` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作类型',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作备注',
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作IP',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE,
  CONSTRAINT `fk_order_log_order` FOREIGN KEY (`order_id`) REFERENCES `oms_orders` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单操作日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oms_order_logs
-- ----------------------------
INSERT INTO `oms_order_logs` VALUES (1, 12, '17439268686535357', NULL, 0, NULL, NULL, '用户ID：12 创建订单', NULL, '2025-04-06 16:07:49');
INSERT INTO `oms_order_logs` VALUES (2, 13, '17439302253049154', NULL, 0, NULL, NULL, '用户ID：12 创建订单', NULL, '2025-04-06 17:03:45');
INSERT INTO `oms_order_logs` VALUES (3, 23, '17439327772022930', NULL, 0, NULL, NULL, '用户ID：12 创建订单', NULL, '2025-04-06 17:46:17');

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单退货申请表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for oms_order_status_history
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_status_history`;
CREATE TABLE `oms_order_status_history`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` bigint(0) NOT NULL COMMENT '订单ID',
  `order_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `previous_status` tinyint(0) NULL DEFAULT NULL COMMENT '前置状态',
  `current_status` tinyint(0) NULL DEFAULT NULL COMMENT '当前状态',
  `operator_id` bigint(0) NULL DEFAULT NULL COMMENT '操作人ID',
  `operator_type` tinyint(0) NULL DEFAULT 0 COMMENT '操作人类型：0-系统，1-用户，2-商家，3-管理员',
  `operator_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作人名称',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '状态变更备注',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE,
  CONSTRAINT `fk_status_history_order` FOREIGN KEY (`order_id`) REFERENCES `oms_orders` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单状态变更历史表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oms_order_status_history
-- ----------------------------
INSERT INTO `oms_order_status_history` VALUES (7, 12, '17439268686192195', 0, 0, NULL, 0, NULL, '创建订单', '2025-04-06 16:07:49');
INSERT INTO `oms_order_status_history` VALUES (8, 13, '17439302252613443', 0, 0, NULL, 0, NULL, '创建订单', '2025-04-06 17:03:45');
INSERT INTO `oms_order_status_history` VALUES (18, 23, '17439327771594760', 0, 0, NULL, 0, NULL, '创建订单', '2025-04-06 17:46:17');

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
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单主表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oms_orders
-- ----------------------------
INSERT INTO `oms_orders` VALUES (1, '20250329123456', 12, 1, 0, 0, 3, 2699.00, 2699.00, 0.00, 0.00, 0.00, 0.00, 0.00, 2, '2025-03-29 14:30:00', NULL, NULL, NULL, NULL, 7, 0, '2025-03-30 20:27:55', '2025-03-30 20:27:55');
INSERT INTO `oms_orders` VALUES (2, '20250329987654', 12, 2, 0, 0, 3, 6999.00, 6899.00, 0.00, 0.00, 0.00, 0.00, 0.00, 1, '2025-03-29 15:00:00', NULL, NULL, NULL, NULL, 7, 0, '2025-03-30 20:27:55', '2025-03-30 20:27:55');
INSERT INTO `oms_orders` VALUES (12, '17439268686192195', 12, 1, 0, 0, 0, 13896.00, 13896.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0, NULL, NULL, NULL, NULL, '', 7, 0, '2025-04-06 16:07:49', '2025-04-06 16:07:49');
INSERT INTO `oms_orders` VALUES (13, '17439302252613443', 12, 1, 0, 0, 0, 24497.00, 24497.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0, NULL, NULL, NULL, NULL, '', 7, 0, '2025-04-06 17:03:45', '2025-04-06 17:03:45');
INSERT INTO `oms_orders` VALUES (23, '17439327771594760', 12, 1, 0, 0, 0, 9999.00, 9999.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0, NULL, NULL, NULL, NULL, '', 7, 0, '2025-04-06 17:46:17', '2025-04-06 17:46:17');

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '支付记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pms_brands
-- ----------------------------
DROP TABLE IF EXISTS `pms_brands`;
CREATE TABLE `pms_brands`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '品牌ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '品牌名称',
  `logo` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '品牌Logo',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '品牌描述',
  `first_letter` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '品牌首字母',
  `sort` int(0) NULL DEFAULT 0 COMMENT '排序',
  `is_featured` tinyint(0) NULL DEFAULT 0 COMMENT '是否推荐：0-否，1-是',
  `status` tinyint(0) NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '品牌表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pms_brands
-- ----------------------------
INSERT INTO `pms_brands` VALUES (1, '小米', 'https://upload.wikimedia.org/wikipedia/commons/thumb/a/ae/Xiaomi_logo_%282021-%29.svg/768px-Xiaomi_logo_%282021-%29.svg.png', '小米科技有限责任公司成立于2010年4月，是一家以智能手机、智能硬件和IoT平台为核心的消费电子及智能制造公司', 'X', 1, 1, 1, '2025-04-02 18:45:06', '2025-04-02 20:45:05');
INSERT INTO `pms_brands` VALUES (2, '华为', 'https://www.huawei.com/-/media/corporate/images/home/logo/huawei_logo.png', '华为技术有限公司成立于1987年，是全球领先的ICT（信息与通信）基础设施和智能终端提供商', 'H', 2, 1, 1, '2025-04-02 18:45:06', '2025-04-02 18:45:06');
INSERT INTO `pms_brands` VALUES (3, '苹果', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS50ch4zAcUjDa_DI1MCYITICMa8HoZEkugwQ&s', 'Apple Inc.（苹果公司）成立于1976年，是一家美国跨国技术公司，设计、开发和销售消费电子产品、计算机软件和在线服务', 'A', 3, 1, 1, '2025-04-02 18:45:06', '2025-04-02 20:43:34');
INSERT INTO `pms_brands` VALUES (4, '三星', 'https://images.samsung.com/is/image/samsung/assets/global/about-us/brand/logo/360_197_1.png', '三星电子有限公司创建于1969年，是韩国最大的电子工业企业', 'S', 4, 0, 1, '2025-04-02 18:45:06', '2025-04-02 18:45:06');
INSERT INTO `pms_brands` VALUES (5, '戴尔', 'https://i.dell.com/sites/csimages/Banner_Imagery/all/dell-logo-500x500-10th.png', '戴尔科技集团（Dell Technologies）是全球领先的IT基础设施和技术解决方案提供商', 'D', 5, 0, 1, '2025-04-02 18:45:06', '2025-04-02 18:45:06');
INSERT INTO `pms_brands` VALUES (6, '联想', 'https://upload.wikimedia.org/wikipedia/commons/thumb/b/bd/Branding_lenovo-logo_lenovologoposred_low_res.png/1200px-Branding_lenovo-logo_lenovologoposred_low_res.png', '联想集团是一家全球化的科技公司，致力于智能化转型', 'L', 6, 0, 1, '2025-04-02 18:45:06', '2025-04-02 20:46:08');
INSERT INTO `pms_brands` VALUES (7, '索尼', 'https://upload.wikimedia.org/wikipedia/commons/c/ca/Sony_logo.svg', '索尼公司是全球领先的电子产品制造商之一，提供音频、视频、游戏、通信和信息技术产品', 'S', 7, 1, 1, '2025-04-02 18:45:06', '2025-04-02 20:44:21');
INSERT INTO `pms_brands` VALUES (8, '微软', 'https://img-prod-cms-rt-microsoft-com.akamaized.net/cms/api/am/imageFileData/RE1Mu3b?ver=5c31', '微软公司是世界个人计算机软件开发的先导，创建于1975年', 'W', 8, 1, 1, '2025-04-02 18:45:06', '2025-04-02 18:45:06');
INSERT INTO `pms_brands` VALUES (9, '松下', 'https://upload.wikimedia.org/wikipedia/commons/thumb/b/b0/Panasonic_logo_%28Blue%29.svg/1200px-Panasonic_logo_%28Blue%29.svg.png', '松下电器(Panasonic)创立于1918年，是世界知名的综合性电子产品制造商，提供包括家电、电子元器件、电池等多种产品及解决方案', 'S', 9, 1, 1, '2025-04-06 21:14:44', '2025-04-06 21:14:44');
INSERT INTO `pms_brands` VALUES (10, 'NIKE', NULL, NULL, 'N', 10, 0, 1, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_brands` VALUES (11, 'OPPO', NULL, NULL, 'O', 11, 0, 1, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_brands` VALUES (12, '七匹狼', NULL, NULL, '七', 12, 0, 1, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_brands` VALUES (13, '万和', NULL, NULL, '万', 13, 0, 1, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_brands` VALUES (14, '海澜之家', NULL, NULL, '海', 14, 0, 1, '2025-04-07 17:45:01', '2025-04-07 17:45:01');

-- ----------------------------
-- Table structure for pms_product_attribute_categories
-- ----------------------------
DROP TABLE IF EXISTS `pms_product_attribute_categories`;
CREATE TABLE `pms_product_attribute_categories`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
  `attribute_count` int(0) NULL DEFAULT 0 COMMENT '属性数量',
  `param_count` int(0) NULL DEFAULT 0 COMMENT '参数数量',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品属性分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pms_product_attribute_categories
-- ----------------------------
INSERT INTO `pms_product_attribute_categories` VALUES (1, '服装', 2, 3, '2025-03-16 15:31:53', '2025-03-16 15:31:53');
INSERT INTO `pms_product_attribute_categories` VALUES (2, '电子产品', 3, 4, '2025-03-16 15:31:53', '2025-03-16 15:31:53');
INSERT INTO `pms_product_attribute_categories` VALUES (3, '家具', 1, 2, '2025-03-16 15:31:53', '2025-03-16 15:31:53');
INSERT INTO `pms_product_attribute_categories` VALUES (4, '食品', 2, 1, '2025-03-16 15:31:53', '2025-03-16 15:31:53');
INSERT INTO `pms_product_attribute_categories` VALUES (5, '玩具', 1, 1, '2025-03-16 15:31:53', '2025-03-16 15:31:53');

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
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品属性表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pms_product_attributes
-- ----------------------------
INSERT INTO `pms_product_attributes` VALUES (1, 4, '屏幕尺寸', 0, 1, '5.5英寸,6.1英寸,6.7英寸,7.0英寸', 1, 1, 1, 1, '2025-03-16 13:15:47', '2025-03-16 13:15:47');
INSERT INTO `pms_product_attributes` VALUES (2, 4, '内存容量', 0, 1, '4GB,6GB,8GB,12GB,16GB', 2, 1, 1, 1, '2025-03-16 13:15:47', '2025-03-16 13:15:47');
INSERT INTO `pms_product_attributes` VALUES (3, 4, '存储容量', 0, 1, '64GB,128GB,256GB,512GB,1TB', 3, 1, 1, 1, '2025-03-16 13:15:47', '2025-03-16 13:15:47');
INSERT INTO `pms_product_attributes` VALUES (4, 4, '电池容量', 1, 0, NULL, 4, 0, 0, 0, '2025-03-16 13:15:47', '2025-03-16 13:15:47');
INSERT INTO `pms_product_attributes` VALUES (5, 4, '处理器', 1, 0, NULL, 5, 0, 1, 0, '2025-03-16 13:15:47', '2025-03-16 13:15:47');
INSERT INTO `pms_product_attributes` VALUES (6, 4, '摄像头像素', 1, 0, NULL, 6, 0, 0, 0, '2025-03-16 13:15:47', '2025-03-16 13:15:47');
INSERT INTO `pms_product_attributes` VALUES (7, 5, '屏幕尺寸', 0, 1, '13.3英寸,14英寸,15.6英寸,16英寸', 1, 1, 1, 1, '2025-03-16 13:15:47', '2025-03-16 13:15:47');
INSERT INTO `pms_product_attributes` VALUES (8, 5, '处理器', 0, 1, 'Intel i5,Intel i7,Intel i9,AMD Ryzen 5,AMD Ryzen 7', 2, 1, 1, 1, '2025-03-16 13:15:47', '2025-03-16 13:15:47');
INSERT INTO `pms_product_attributes` VALUES (9, 5, '内存容量', 0, 1, '8GB,16GB,32GB,64GB', 3, 1, 1, 1, '2025-03-16 13:15:47', '2025-03-16 13:15:47');
INSERT INTO `pms_product_attributes` VALUES (10, 5, '固态硬盘', 0, 1, '256GB,512GB,1TB,2TB', 4, 1, 1, 1, '2025-03-16 13:15:47', '2025-03-16 13:15:47');
INSERT INTO `pms_product_attributes` VALUES (20, 12, '屏幕尺寸', 0, 1, '1.2英寸,1.4英寸,1.6英寸,1.8英寸', 1, 1, 1, 1, '2025-03-16 13:24:09', '2025-03-16 13:24:09');
INSERT INTO `pms_product_attributes` VALUES (21, 12, '续航时间', 0, 1, '1天,3天,7天,14天', 2, 1, 1, 1, '2025-03-16 13:24:09', '2025-03-16 13:24:09');
INSERT INTO `pms_product_attributes` VALUES (22, 12, '防水等级', 0, 1, 'IP67,IP68,5ATM', 3, 1, 0, 0, '2025-03-16 13:24:09', '2025-03-16 13:24:09');
INSERT INTO `pms_product_attributes` VALUES (23, 12, '健康功能', 1, 0, NULL, 4, 0, 1, 0, '2025-03-16 13:24:09', '2025-03-16 13:24:09');
INSERT INTO `pms_product_attributes` VALUES (24, 13, '连接方式', 0, 1, 'Wi-Fi,蓝牙,Zigbee,Z-Wave', 1, 1, 1, 1, '2025-03-16 13:24:09', '2025-03-16 13:24:09');
INSERT INTO `pms_product_attributes` VALUES (25, 13, '兼容系统', 0, 1, 'iOS,Android,Windows,HomeKit,Alexa,Google Assistant', 2, 1, 1, 1, '2025-03-16 13:24:09', '2025-03-16 13:24:09');
INSERT INTO `pms_product_attributes` VALUES (26, 15, '屏幕类型', 0, 1, 'LED,OLED,QLED,Mini LED', 1, 1, 1, 1, '2025-03-16 13:24:09', '2025-03-16 13:24:09');
INSERT INTO `pms_product_attributes` VALUES (27, 15, '分辨率', 0, 1, '1080P,4K,8K', 2, 1, 1, 1, '2025-03-16 13:24:09', '2025-03-16 13:24:09');
INSERT INTO `pms_product_attributes` VALUES (28, 15, '智能系统', 0, 1, 'Android TV,webOS,VIDAA', 3, 1, 1, 0, '2025-03-16 13:24:09', '2025-03-16 13:24:09');

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
  `nav_status` tinyint(0) NULL DEFAULT NULL COMMENT '是否显示导航栏 0-否，1-是',
  `is_featured` tinyint(0) NULL DEFAULT 0 COMMENT '是否推荐：0-否，1-是',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pms_product_categories
-- ----------------------------
INSERT INTO `pms_product_categories` VALUES (1, 0, '手机数码', 1, 'phone', 1, '手机、平板电脑、智能设备等数码产品', '手机,数码,智能设备', 1, 1, 1, '2025-03-01 10:00:00', '2025-03-01 10:00:00');
INSERT INTO `pms_product_categories` VALUES (2, 0, '家用电器', 1, 'appliance', 2, '电视、冰箱、洗衣机等家用电器', '家电,电器,智能家居', 1, 1, 1, '2025-03-01 10:01:00', '2025-03-01 10:01:00');
INSERT INTO `pms_product_categories` VALUES (3, 0, '电脑办公', 1, 'computer', 3, '笔记本、台式机、办公设备等产品', '电脑,办公,外设', 1, 1, 1, '2025-03-01 10:02:00', '2025-03-01 10:02:00');
INSERT INTO `pms_product_categories` VALUES (4, 0, '智能家居', 1, 'smart-home', 4, '智能音箱、智能灯具等智能家居产品', '智能家居,物联网,智能设备', 1, 1, 1, '2025-03-01 10:03:00', '2025-03-01 10:03:00');
INSERT INTO `pms_product_categories` VALUES (5, 1, '手机', 2, 'mobile', 1, '各品牌智能手机', '手机,智能手机,5G', 1, 1, 1, '2025-03-01 10:05:00', '2025-03-01 10:05:00');
INSERT INTO `pms_product_categories` VALUES (6, 1, '笔记本', 2, 'laptop', 2, '各品牌笔记本电脑', '笔记本,轻薄本,游戏本', 1, 1, 1, '2025-03-01 10:06:00', '2025-03-01 10:06:00');
INSERT INTO `pms_product_categories` VALUES (7, 1, '平板电脑', 2, 'tablet', 3, '各品牌平板电脑', '平板,iPad,安卓平板', 1, 1, 1, '2025-03-01 10:07:00', '2025-03-01 10:07:00');
INSERT INTO `pms_product_categories` VALUES (8, 1, '智能穿戴', 2, 'wearable', 4, '智能手表、手环等穿戴设备', '智能手表,手环,穿戴设备', 1, 1, 1, '2025-03-01 10:08:00', '2025-03-01 10:08:00');
INSERT INTO `pms_product_categories` VALUES (9, 2, '电视', 2, 'tv', 1, '智能电视、激光电视等', '电视,智能电视,OLED', 1, 1, 1, '2025-03-01 10:10:00', '2025-03-01 10:10:00');
INSERT INTO `pms_product_categories` VALUES (10, 2, '空调', 2, 'air-conditioner', 2, '壁挂式、柜式空调等', '空调,变频,智能空调', 1, 1, 1, '2025-03-01 10:11:00', '2025-03-01 10:11:00');
INSERT INTO `pms_product_categories` VALUES (11, 2, '冰箱', 2, 'refrigerator', 3, '多门冰箱、对开门冰箱等', '冰箱,对开门,智能冰箱', 1, 1, 1, '2025-03-01 10:12:00', '2025-03-01 10:12:00');
INSERT INTO `pms_product_categories` VALUES (12, 2, '洗衣机', 2, 'washing-machine', 4, '滚筒洗衣机、波轮洗衣机等', '洗衣机,滚筒,烘干', 1, 1, 1, '2025-03-01 10:13:00', '2025-03-01 10:13:00');
INSERT INTO `pms_product_categories` VALUES (13, 3, '台式电脑', 2, 'desktop', 1, '台式机、一体机等', '台式机,一体机,组装机', 1, 1, 1, '2025-03-01 10:15:00', '2025-03-01 10:15:00');
INSERT INTO `pms_product_categories` VALUES (14, 3, '显示器', 2, 'monitor', 2, '各种尺寸显示器', '显示器,4K,曲面', 1, 1, 1, '2025-03-01 10:16:00', '2025-03-01 10:16:00');
INSERT INTO `pms_product_categories` VALUES (15, 3, '打印机', 2, 'printer', 3, '喷墨打印机、激光打印机等', '打印机,激光,一体机', 1, 1, 1, '2025-03-01 10:17:00', '2025-03-01 10:17:00');
INSERT INTO `pms_product_categories` VALUES (16, 3, '外设配件', 2, 'accessories', 4, '键盘、鼠标、耳机等外设配件', '键盘,鼠标,耳机,外设', 1, 1, 1, '2025-03-01 10:18:00', '2025-03-01 10:18:00');
INSERT INTO `pms_product_categories` VALUES (17, 4, '智能音箱', 2, 'smart-speaker', 1, '各品牌智能音箱', '智能音箱,语音助手,AI', 1, 1, 1, '2025-03-01 10:20:00', '2025-03-01 10:20:00');
INSERT INTO `pms_product_categories` VALUES (18, 4, '智能灯具', 2, 'smart-light', 2, '智能灯泡、智能灯带等', '智能灯,灯带,情景照明', 1, 1, 1, '2025-03-01 10:21:00', '2025-03-01 10:21:00');
INSERT INTO `pms_product_categories` VALUES (19, 4, '智能门锁', 2, 'smart-lock', 3, '指纹锁、人脸识别门锁等', '智能锁,指纹锁,人脸识别', 1, 1, 1, '2025-03-01 10:22:00', '2025-03-01 10:22:00');
INSERT INTO `pms_product_categories` VALUES (20, 4, '智能摄像头', 2, 'smart-camera', 4, '家用监控摄像头等', '摄像头,监控,安防', 1, 1, 1, '2025-03-01 10:23:00', '2025-03-01 10:23:00');
INSERT INTO `pms_product_categories` VALUES (21, 0, 'T恤', 1, NULL, 5, NULL, NULL, 1, NULL, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_categories` VALUES (22, 0, '厨卫大电', 1, NULL, 6, NULL, NULL, 1, NULL, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_categories` VALUES (23, 0, '外套', 1, NULL, 7, NULL, NULL, 1, NULL, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_categories` VALUES (24, 0, '手机通讯', 1, NULL, 8, NULL, NULL, 1, NULL, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_categories` VALUES (25, 0, '男鞋', 1, NULL, 9, NULL, NULL, 1, NULL, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_categories` VALUES (26, 0, '硬盘', 1, NULL, 10, NULL, NULL, 1, NULL, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');

-- ----------------------------
-- Table structure for pms_product_comments
-- ----------------------------
DROP TABLE IF EXISTS `pms_product_comments`;
CREATE TABLE `pms_product_comments`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '评价ID',
  `product_id` bigint(0) NOT NULL COMMENT '商品ID',
  `sku_id` bigint(0) NULL DEFAULT NULL COMMENT 'SKU ID',
  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
  `order_id` bigint(0) NULL DEFAULT NULL COMMENT '订单ID',
  `order_item_id` bigint(0) NULL DEFAULT NULL COMMENT '订单项ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '评价内容',
  `images` json NULL COMMENT '评价图片（JSON格式）',
  `videos` json NULL COMMENT '评价视频（JSON格式）',
  `rating` decimal(2, 1) NOT NULL COMMENT '评分',
  `is_anonymous` tinyint(0) NULL DEFAULT 0 COMMENT '是否匿名：0-否，1-是',
  `is_show` tinyint(0) NULL DEFAULT 1 COMMENT '是否显示：0-否，1-是',
  `is_reply` tinyint(0) NULL DEFAULT 0 COMMENT '是否回复：0-否，1-是',
  `reply_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '回复内容',
  `reply_time` datetime(0) NULL DEFAULT NULL COMMENT '回复时间',
  `reply_user` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '回复人',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_product_id`(`product_id`) USING BTREE,
  INDEX `idx_sku_id`(`sku_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE,
  CONSTRAINT `fk_comment_product` FOREIGN KEY (`product_id`) REFERENCES `pms_products` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_comment_sku` FOREIGN KEY (`sku_id`) REFERENCES `pms_product_skus` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品评价表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pms_product_comments
-- ----------------------------
INSERT INTO `pms_product_comments` VALUES (2, 15, NULL, 12, NULL, NULL, '太好了，物超所值', NULL, NULL, 5.0, 0, 1, 0, NULL, NULL, NULL, '2025-04-02 23:34:27', '2025-04-02 23:34:26');
INSERT INTO `pms_product_comments` VALUES (3, 16, NULL, 12, NULL, NULL, '太好了，物超所值', NULL, NULL, 5.0, 0, 1, 0, NULL, NULL, NULL, '2025-04-02 23:37:49', '2025-04-02 23:37:48');
INSERT INTO `pms_product_comments` VALUES (4, 16, NULL, 12, NULL, NULL, '太好了，物超所值', NULL, NULL, 5.0, 0, 1, 0, NULL, NULL, NULL, '2025-04-02 23:43:22', '2025-04-02 23:43:22');
INSERT INTO `pms_product_comments` VALUES (5, 19, NULL, 12, NULL, NULL, '太好了，物超所值', NULL, NULL, 5.0, 0, 1, 0, NULL, NULL, NULL, '2025-04-02 23:50:19', '2025-04-02 23:50:18');
INSERT INTO `pms_product_comments` VALUES (6, 20, NULL, 12, NULL, NULL, '太好了，物超所值', NULL, NULL, 5.0, 0, 1, 0, NULL, NULL, NULL, '2025-04-02 23:54:31', '2025-04-02 23:54:31');

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
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品详情表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pms_product_details
-- ----------------------------
INSERT INTO `pms_product_details` VALUES (1, 26, '<div><p><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180607/5ad44f1cNf51f3bb0.jpg\" /><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180607/5ad44fa8Nfcf71c10.jpg\" /><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180607/5ad44fa9N40e78ee0.jpg\" /><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180607/5ad457f4N1c94bdda.jpg\" /><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180607/5ad457f5Nd30de41d.jpg\" /><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180607/5b10fb0eN0eb053fb.jpg\" /></p></div><div><p><img src=\"//img20.360buyimg.com/vc/jfs/t1/81293/35/5822/369414/5d3fe77cE619c5487/6e775a52850feea5.jpg!q70.dpg.webp\" alt=\"\" width=\"750\" height=\"776\" /></p>\n<p><img src=\"//img20.360buyimg.com/vc/jfs/t1/45300/25/11592/3658871/5d85ef66E92a8a911/083e47d8f662c582.jpg!q70.dpg.webp\" alt=\"\" width=\"596\" height=\"16383\" /></p></div>', '', NULL, NULL, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_details` VALUES (2, 27, '小米8 全面屏游戏智能手机 6GB+64GB 黑色 全网通4G 双卡双待<div><p style=\"text-align: center;\"><img src=\"//img30.360buyimg.com/popWareDetail/jfs/t1/95935/9/19330/159477/5e9ecc13E5b8db8ae/8e954021a0835fb7.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWareDetail/jfs/t1/99224/22/19596/137593/5e9ecc13E34ef2113/2b362b90d8378ee1.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWareDetail/jfs/t1/93131/25/19321/107691/5e9ecc13E41e8addf/202a5f84d9129878.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWareDetail/jfs/t1/101843/19/19533/66831/5e9ecc13Ecb7f9d53/4fdd807266583c1e.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWareDetail/jfs/t1/99629/36/19016/59882/5e9ecc13E1f5beef5/1e5af3528f366e70.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/72343/29/8945/84548/5d6e5c67Ea07b1125/702791816b90eb3d.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/65403/35/9017/129532/5d6e5c68E3f2d0546/9ec771eb6e04a75a.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/58261/33/9380/106603/5d6e5c68E05a99177/2b5b9e29eed05b08.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/51968/40/9688/113552/5d6e5c68E5052b312/8969d83124cb78a4.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/75491/9/9101/146883/5d6e5c68E3db89775/c1aa57e78ded4e44.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/75063/11/9107/127874/5d6e5c68E0b1dfc61/10dd6000ce213375.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/47452/25/9579/108279/5d6e5c68Ea9002f3b/865b5d32ffd9da75.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/82146/26/9077/87075/5d6e5c68Ef63bccc8/556de5665a35a3f2.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/82804/21/9019/124404/5d6e5c69E06a8f575/0f861f8c4636c546.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/46044/10/9734/107686/5d6e5c69Edd5e66c7/a8c7b9324e271dbd.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/40729/32/13755/45997/5d6e5c69Eafee3664/6a3457a4efdb79c5.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/76254/34/9039/96195/5d6e5c69E3c71c809/49033c0b7024c208.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/79825/20/9065/90864/5d6e5c69E1e62ef89/a4d3ce383425a666.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/49939/21/9618/106207/5d6e5c6aEf7b1d4fd/0f5e963c66be3d0c.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/64035/7/9001/115159/5d6e5c6aE6919dfdf/39dfe4840157ad81.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/53389/3/9616/99637/5d6e5c6aEa33b9f35/b8f6aa26e72616a3.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/63219/6/9026/81576/5d6e5c6aEa9c74b49/b4fa364437531012.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/42146/27/13902/80437/5d6e5c6bE30c31ce9/475d4d54c7334313.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/45317/28/9596/78175/5d6e5c6bEce31e4b7/5675858b6933565c.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/60080/1/9112/138722/5d6e5c6bEefd9fc62/7ece7460a36d2fcc.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/51525/13/9549/36018/5d6e5c73Ebbccae6c/99bc2770dccc042b.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/61948/20/9088/72918/5d6e5c73Eab7aef5c/6f21e2f85cf478fa.jpg!q70.dpg.webp\" /></p></div><div><p style=\"text-align: center;\"><img src=\"//img30.360buyimg.com/popWareDetail/jfs/t1/95935/9/19330/159477/5e9ecc13E5b8db8ae/8e954021a0835fb7.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWareDetail/jfs/t1/99224/22/19596/137593/5e9ecc13E34ef2113/2b362b90d8378ee1.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWareDetail/jfs/t1/93131/25/19321/107691/5e9ecc13E41e8addf/202a5f84d9129878.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWareDetail/jfs/t1/101843/19/19533/66831/5e9ecc13Ecb7f9d53/4fdd807266583c1e.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWareDetail/jfs/t1/99629/36/19016/59882/5e9ecc13E1f5beef5/1e5af3528f366e70.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/72343/29/8945/84548/5d6e5c67Ea07b1125/702791816b90eb3d.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/65403/35/9017/129532/5d6e5c68E3f2d0546/9ec771eb6e04a75a.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/58261/33/9380/106603/5d6e5c68E05a99177/2b5b9e29eed05b08.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/51968/40/9688/113552/5d6e5c68E5052b312/8969d83124cb78a4.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/75491/9/9101/146883/5d6e5c68E3db89775/c1aa57e78ded4e44.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/75063/11/9107/127874/5d6e5c68E0b1dfc61/10dd6000ce213375.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/47452/25/9579/108279/5d6e5c68Ea9002f3b/865b5d32ffd9da75.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/82146/26/9077/87075/5d6e5c68Ef63bccc8/556de5665a35a3f2.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/82804/21/9019/124404/5d6e5c69E06a8f575/0f861f8c4636c546.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/46044/10/9734/107686/5d6e5c69Edd5e66c7/a8c7b9324e271dbd.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/40729/32/13755/45997/5d6e5c69Eafee3664/6a3457a4efdb79c5.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/76254/34/9039/96195/5d6e5c69E3c71c809/49033c0b7024c208.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/79825/20/9065/90864/5d6e5c69E1e62ef89/a4d3ce383425a666.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/49939/21/9618/106207/5d6e5c6aEf7b1d4fd/0f5e963c66be3d0c.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/64035/7/9001/115159/5d6e5c6aE6919dfdf/39dfe4840157ad81.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/53389/3/9616/99637/5d6e5c6aEa33b9f35/b8f6aa26e72616a3.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/63219/6/9026/81576/5d6e5c6aEa9c74b49/b4fa364437531012.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/42146/27/13902/80437/5d6e5c6bE30c31ce9/475d4d54c7334313.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/45317/28/9596/78175/5d6e5c6bEce31e4b7/5675858b6933565c.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/60080/1/9112/138722/5d6e5c6bEefd9fc62/7ece7460a36d2fcc.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/51525/13/9549/36018/5d6e5c73Ebbccae6c/99bc2770dccc042b.jpg!q70.dpg.webp\" /><img src=\"//img30.360buyimg.com/popWaterMark/jfs/t1/61948/20/9088/72918/5d6e5c73Eab7aef5c/6f21e2f85cf478fa.jpg!q70.dpg.webp\" /></p></div>', '', NULL, NULL, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_details` VALUES (3, 28, '<div></div><div><div><img src=\"//img12.360buyimg.com/imgzone/jfs/t1/67362/12/14546/708984/5dc28512Eefdd817d/c82503af0da6c038.gif\" /><img src=\"//img13.360buyimg.com/imgzone/jfs/t1/61488/17/14551/995918/5dc28512E821c228d/41e52005ea5b1f36.gif\" /><img src=\"//img14.360buyimg.com/imgzone/jfs/t1/72961/36/14769/305883/5dc28512E65d77261/3df6be29e3d489d1.gif\" />\n<p>&nbsp;</p>\n</div></div>', '', NULL, NULL, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_details` VALUES (4, 29, '<div></div><div><div><img src=\"//img10.360buyimg.com/cms/jfs/t1/20020/38/9725/228440/5c7f9208Eeaf4bf87/13a713066f71791d.jpg!q70.dpg.webp\" /> <img src=\"//img12.360buyimg.com/cms/jfs/t1/12128/39/9607/265349/5c7f9209Ecff29b88/08620ba570705634.jpg!q70.dpg.webp\" /> <img src=\"//img14.360buyimg.com/cms/jfs/t1/22731/40/9578/345163/5c7f9209E9ba056e5/a8a557060b84447e.jpg!q70.dpg.webp\" /> <img src=\"//img14.360buyimg.com/cms/jfs/t1/29506/38/9439/299492/5c7f9209E0e51eb29/15dedd95416f3c68.jpg!q70.dpg.webp\" /> <img src=\"//img14.360buyimg.com/cms/jfs/t1/26766/28/9574/257101/5c7f9209Eaca1b317/c7caa047b1566cf1.jpg!q70.dpg.webp\" /> <img src=\"//img13.360buyimg.com/cms/jfs/t1/11059/8/10473/286531/5c7f9208E05da0120/9034ad8799ad9553.jpg!q70.dpg.webp\" /> <img src=\"//img14.360buyimg.com/cms/jfs/t1/25641/2/9557/268826/5c7f9208Efbf0dc50/399580629e05e733.jpg!q70.dpg.webp\" /> <img src=\"//img13.360buyimg.com/cms/jfs/t1/28964/25/9527/305902/5c7f9208E275ffb9c/8464934d67e69b7a.jpg!q70.dpg.webp\" /></div></div>', '', NULL, NULL, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_details` VALUES (5, 30, '<div></div><div></div>', '', NULL, NULL, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_details` VALUES (6, 31, '<div></div><div></div>', '', NULL, NULL, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_details` VALUES (7, 32, '<div></div><div></div>', '', NULL, NULL, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_details` VALUES (8, 33, '<div></div><div></div>', '', NULL, NULL, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_details` VALUES (9, 34, '<div></div><div></div>', '', NULL, NULL, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_details` VALUES (10, 35, '<div></div><div></div>', '', NULL, NULL, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_details` VALUES (11, 36, '<div></div><div></div>', '', NULL, NULL, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_details` VALUES (12, 37, '<div></div><div><div style=\"margin: 0 auto;\"><img style=\"max-width: 390px;\" src=\"//img13.360buyimg.com/cms/jfs/t1/58071/39/19839/119559/63190110Edac0cea7/b62a84f1b8775fef.jpg!q70.dpg.webp\" /> <img style=\"max-width: 390px;\" src=\"//img20.360buyimg.com/cms/jfs/t1/138903/36/29400/86115/63190110E0a98c819/d2efbef39eeb2995.jpg!q70.dpg.webp\" /> <img style=\"max-width: 390px;\" src=\"//img30.360buyimg.com/cms/jfs/t1/176347/20/28995/115695/63190110Ef5d766f9/aee3d2d866522f66.jpg!q70.dpg.webp\" /> <img style=\"max-width: 390px;\" src=\"//img14.360buyimg.com/cms/jfs/t1/120515/39/28721/142961/63190110Eec31e31a/3486d6a065a18ddc.jpg!q70.dpg.webp\" /> <img style=\"max-width: 390px;\" src=\"//img13.360buyimg.com/cms/jfs/t1/30161/12/17674/81508/63190110E1385cf61/f05a2a43f4d304ff.jpg!q70.dpg.webp\" /> <img style=\"max-width: 390px;\" src=\"//img14.360buyimg.com/cms/jfs/t1/100037/16/31071/62177/6319010fE871efe01/b01a6f981c268e38.jpg!q70.dpg.webp\" /> <img style=\"max-width: 390px;\" src=\"//img14.360buyimg.com/cms/jfs/t1/90843/33/25852/74752/63190110E373559f4/74b6b52a3fb08c66.jpg!q70.dpg.webp\" /> <img style=\"max-width: 390px;\" src=\"//img14.360buyimg.com/cms/jfs/t1/181914/22/28400/126094/63190110Edefb838c/802a16e758be2b1d.jpg!q70.dpg.webp\" /></div></div>', '', NULL, NULL, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_details` VALUES (13, 38, '<div></div><div><div style=\"margin: 0 auto;\"><img style=\"max-width: 390px;\" src=\"//img12.360buyimg.com/cms/jfs/t1/75040/28/21026/295081/634ed154E981e4d10/2ceef91d6f2b2273.jpg!q70.dpg.webp\" /> <img style=\"max-width: 390px;\" src=\"//img13.360buyimg.com/cms/jfs/t1/191028/1/28802/401291/634ed15eEb234dc40/5ab89f83531e1023.jpg!q70.dpg.webp\" /> <img style=\"max-width: 390px;\" src=\"//img14.360buyimg.com/cms/jfs/t1/32758/24/18599/330590/634ed15eEc3db173c/c52953dc8008a576.jpg!q70.dpg.webp\" /></div></div>', '', NULL, NULL, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_details` VALUES (14, 39, '<div></div><div><div class=\"ssd-module-mobile-wrap\">\n<div class=\"ssd-module M16667778180631\" data-id=\"M16667778180631\"><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221028/xiaomi_computer_snipaste_01.png\" /><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221028/xiaomi_computer_snipaste_02.png\" /><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221028/xiaomi_computer_snipaste_03.png\" /><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221028/xiaomi_computer_snipaste_04.png\" /><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221028/xiaomi_computer_snipaste_05.png\" /><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221028/xiaomi_computer_snipaste_06.png\" /></div>\n<div class=\"ssd-module M16667778180631\" data-id=\"M16667778180631\">&nbsp;</div>\n<div class=\"ssd-module M16534569204241\" data-id=\"M16534569204241\">&nbsp;</div>\n<div class=\"ssd-module M16667778416884\" data-id=\"M16667778416884\">\n<div class=\"ssd-widget-text W16667778440496\">&nbsp;</div>\n</div>\n<div class=\"ssd-module M165303211067557\" data-id=\"M165303211067557\">&nbsp;</div>\n<div class=\"ssd-module M16530320459861\" data-id=\"M16530320459861\">&nbsp;</div>\n<div class=\"ssd-module M16530320460062\" data-id=\"M16530320460062\">&nbsp;</div>\n<div class=\"ssd-module M16530320460153\" data-id=\"M16530320460153\">&nbsp;</div>\n<div class=\"ssd-module M16530320460366\" data-id=\"M16530320460366\">&nbsp;</div>\n<div class=\"ssd-module M16530320460477\" data-id=\"M16530320460477\">&nbsp;</div>\n<div class=\"ssd-module M16530320460578\" data-id=\"M16530320460578\">&nbsp;</div>\n<div class=\"ssd-module M16530320460699\" data-id=\"M16530320460699\">&nbsp;</div>\n<div class=\"ssd-module M165303204608110\" data-id=\"M165303204608110\">&nbsp;</div>\n<div class=\"ssd-module M165303204609511\" data-id=\"M165303204609511\">&nbsp;</div>\n<div class=\"ssd-module M165303204611213\" data-id=\"M165303204611213\">&nbsp;</div>\n<div class=\"ssd-module M165303204612714\" data-id=\"M165303204612714\">&nbsp;</div>\n<div class=\"ssd-module M165303204614115\" data-id=\"M165303204614115\">&nbsp;</div>\n<div class=\"ssd-module M165303204615516\" data-id=\"M165303204615516\">&nbsp;</div>\n<div class=\"ssd-module M165303204617417\" data-id=\"M165303204617417\">&nbsp;</div>\n<div class=\"ssd-module M165303204618818\" data-id=\"M165303204618818\">&nbsp;</div>\n<div class=\"ssd-module M165303204620219\" data-id=\"M165303204620219\">&nbsp;</div>\n<div class=\"ssd-module M165303204621620\" data-id=\"M165303204621620\">&nbsp;</div>\n<div class=\"ssd-module M165303204622921\" data-id=\"M165303204622921\">&nbsp;</div>\n<div class=\"ssd-module M165303204624522\" data-id=\"M165303204624522\">&nbsp;</div>\n<div class=\"ssd-module M165303204626024\" data-id=\"M165303204626024\">&nbsp;</div>\n<div class=\"ssd-module M165303204627525\" data-id=\"M165303204627525\">&nbsp;</div>\n<div class=\"ssd-module M165303204629127\" data-id=\"M165303204629127\">&nbsp;</div>\n<div class=\"ssd-module M165303204632330\" data-id=\"M165303204632330\">&nbsp;</div>\n<div class=\"ssd-module M165303204634031\" data-id=\"M165303204634031\">&nbsp;</div>\n<div class=\"ssd-module M165303204635832\" data-id=\"M165303204635832\">&nbsp;</div>\n<div class=\"ssd-module M165303204637533\" data-id=\"M165303204637533\">&nbsp;</div>\n<div class=\"ssd-module M165303204639334\" data-id=\"M165303204639334\">&nbsp;</div>\n<div class=\"ssd-module M165303204642235\" data-id=\"M165303204642235\">&nbsp;</div>\n<div class=\"ssd-module M165303204647936\" data-id=\"M165303204647936\">&nbsp;</div>\n<div class=\"ssd-module M165303204651037\" data-id=\"M165303204651037\">&nbsp;</div>\n<div class=\"ssd-module M165303204653838\" data-id=\"M165303204653838\">&nbsp;</div>\n<div class=\"ssd-module M165303204656239\" data-id=\"M165303204656239\">&nbsp;</div>\n<div class=\"ssd-module M165303204659141\" data-id=\"M165303204659141\">&nbsp;</div>\n<div class=\"ssd-module M165303204661943\" data-id=\"M165303204661943\">&nbsp;</div>\n<div class=\"ssd-module M165303204665844\" data-id=\"M165303204665844\">&nbsp;</div>\n<div class=\"ssd-module M165303204668045\" data-id=\"M165303204668045\">&nbsp;</div>\n<div class=\"ssd-module M165303204670146\" data-id=\"M165303204670146\">&nbsp;</div>\n<div class=\"ssd-module M165303204672147\" data-id=\"M165303204672147\">&nbsp;</div>\n<div class=\"ssd-module M165303204674348\" data-id=\"M165303204674348\">&nbsp;</div>\n<div class=\"ssd-module M165303204676749\" data-id=\"M165303204676749\">&nbsp;</div>\n<div class=\"ssd-module M165303204681352\" data-id=\"M165303204681352\">&nbsp;</div>\n<div class=\"ssd-module M165303204683553\" data-id=\"M165303204683553\">&nbsp;</div>\n<div class=\"ssd-module M165303204685855\" data-id=\"M165303204685855\">&nbsp;</div>\n<div class=\"ssd-module M165303204688356\" data-id=\"M165303204688356\">&nbsp;</div>\n</div></div>', '', NULL, NULL, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_details` VALUES (15, 40, '<div></div><div><p><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221104/xiaomi_12_pro_snipaste_01.png\" /><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221104/xiaomi_12_pro_snipaste_02.png\" /><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221104/xiaomi_12_pro_snipaste_03.png\" /><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221104/xiaomi_12_pro_snipaste_04.png\" /><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221104/xiaomi_12_pro_snipaste_05.png\" /></p></div>', '', NULL, NULL, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_details` VALUES (16, 41, '<div></div><div><p><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221104/redmi_k50_snipaste_01.png\" /><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221104/redmi_k50_snipaste_02.png\" /><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221104/redmi_k50_snipaste_03.png\" /><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221104/redmi_k50_snipaste_04.png\" /><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221104/redmi_k50_snipaste_05.png\" /></p></div>', '', NULL, NULL, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_details` VALUES (17, 42, '<div></div><div><p><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221104/huawei_mate50_snipaste_01.png\" /><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221104/huawei_mate50_snipaste_02.png\" /><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221104/huawei_mate50_snipaste_03.png\" /><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221104/huawei_mate50_snipaste_04.png\" /><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221104/huawei_mate50_snipaste_05.png\" /></p></div>', '', NULL, NULL, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_details` VALUES (18, 43, '<div></div><div><p><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221104/wanhe_water_snipaste_01.png\" /><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221104/wanhe_water_snipaste_02.png\" /><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221104/wanhe_water_snipaste_03.png\" /><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221104/wanhe_water_snipaste_04.png\" /><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221104/wanhe_water_snipaste_05.png\" /></p></div>', '', NULL, NULL, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_details` VALUES (19, 44, '<div></div><div><p><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221108/sanxing_ssd_snipaste_01.png\" /><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221108/sanxing_ssd_snipaste_02.png\" /><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221108/sanxing_ssd_snipaste_03.png\" /></p></div>', '', NULL, NULL, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_details` VALUES (20, 45, '<div></div><div><p><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221108/oppo_r8_snipaste_01.png\" /><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221108/oppo_r8_snipaste_02.png\" /><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221108/oppo_r8_snipaste_03.png\" /><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221108/oppo_r8_snipaste_04.png\" /><img class=\"wscnph\" src=\"http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221108/oppo_r8_snipaste_05.png\" /></p></div>', '', NULL, NULL, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_details` VALUES (21, 46, '1111111111111111111<div></div><div></div>', '', NULL, NULL, '2025-04-07 17:45:01', '2025-04-07 17:45:01');

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
) ENGINE = InnoDB AUTO_INCREMENT = 60 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品图片表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pms_product_images
-- ----------------------------
INSERT INTO `pms_product_images` VALUES (17, 26, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180607/5ac1bf58Ndefaac16.jpg', 0, 1, '2025-04-07 17:45:01');
INSERT INTO `pms_product_images` VALUES (18, 27, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180615/xiaomi.jpg', 0, 1, '2025-04-07 17:45:01');
INSERT INTO `pms_product_images` VALUES (19, 28, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180615/5a9d248cN071f4959.jpg', 0, 1, '2025-04-07 17:45:01');
INSERT INTO `pms_product_images` VALUES (20, 29, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180615/5acc5248N6a5f81cd.jpg', 0, 1, '2025-04-07 17:45:01');
INSERT INTO `pms_product_images` VALUES (21, 30, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180615/5ad83a4fN6ff67ecd.jpg!cc_350x449.jpg', 0, 1, '2025-04-07 17:45:01');
INSERT INTO `pms_product_images` VALUES (22, 31, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180615/5ac98b64N70acd82f.jpg!cc_350x449.jpg', 0, 1, '2025-04-07 17:45:01');
INSERT INTO `pms_product_images` VALUES (23, 32, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180615/5a51eb88Na4797877.jpg', 0, 1, '2025-04-07 17:45:01');
INSERT INTO `pms_product_images` VALUES (24, 33, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180615/5b02804dN66004d73.jpg', 0, 1, '2025-04-07 17:45:01');
INSERT INTO `pms_product_images` VALUES (25, 34, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180615/5b028530N51eee7d4.jpg', 0, 1, '2025-04-07 17:45:01');
INSERT INTO `pms_product_images` VALUES (26, 35, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180615/5b235bb9Nf606460b.jpg', 0, 1, '2025-04-07 17:45:01');
INSERT INTO `pms_product_images` VALUES (27, 36, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180615/5b19403eN9f0b3cb8.jpg', 0, 1, '2025-04-07 17:45:01');
INSERT INTO `pms_product_images` VALUES (28, 37, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221028/iphone14_001.jpg', 0, 1, '2025-04-07 17:45:01');
INSERT INTO `pms_product_images` VALUES (29, 38, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221028/ipad_001.jpg', 0, 1, '2025-04-07 17:45:01');
INSERT INTO `pms_product_images` VALUES (30, 39, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221028/xiaomi_computer_001.jpg', 0, 1, '2025-04-07 17:45:01');
INSERT INTO `pms_product_images` VALUES (31, 40, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221104/xiaomi_12_pro_01.jpg', 0, 1, '2025-04-07 17:45:01');
INSERT INTO `pms_product_images` VALUES (32, 41, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221104/redmi_k50_01.jpg', 0, 1, '2025-04-07 17:45:01');
INSERT INTO `pms_product_images` VALUES (33, 42, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221104/huawei_mate50_01.jpg', 0, 1, '2025-04-07 17:45:01');
INSERT INTO `pms_product_images` VALUES (34, 43, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221104/wanhe_13L_01.png', 0, 1, '2025-04-07 17:45:01');
INSERT INTO `pms_product_images` VALUES (35, 44, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221108/sanxing_ssd_02.jpg', 0, 1, '2025-04-07 17:45:01');
INSERT INTO `pms_product_images` VALUES (36, 45, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221108/oppo_r8_01.jpg', 0, 1, '2025-04-07 17:45:01');
INSERT INTO `pms_product_images` VALUES (48, 26, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180607/5ab46a3cN616bdc41.jpg', 1, 0, '2025-04-07 17:45:01');
INSERT INTO `pms_product_images` VALUES (49, 37, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221028/iphone14_002.jpg', 1, 0, '2025-04-07 17:45:01');
INSERT INTO `pms_product_images` VALUES (50, 38, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221028/ipad_002.jpg', 1, 0, '2025-04-07 17:45:01');
INSERT INTO `pms_product_images` VALUES (51, 39, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221028/xiaomi_computer_002.jpg', 1, 0, '2025-04-07 17:45:01');
INSERT INTO `pms_product_images` VALUES (52, 40, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221104/xiaomi_12_pro_02.jpg', 1, 0, '2025-04-07 17:45:01');
INSERT INTO `pms_product_images` VALUES (53, 41, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221104/redmi_k50_02.jpg', 1, 0, '2025-04-07 17:45:01');
INSERT INTO `pms_product_images` VALUES (54, 42, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221104/huawei_mate50_02.jpg', 1, 0, '2025-04-07 17:45:01');
INSERT INTO `pms_product_images` VALUES (55, 43, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221104/wanhe_16L_01.jpg', 1, 0, '2025-04-07 17:45:01');
INSERT INTO `pms_product_images` VALUES (56, 44, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221108/sanxing_ssd_01.jpg', 1, 0, '2025-04-07 17:45:01');
INSERT INTO `pms_product_images` VALUES (57, 45, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221108/oppo_r8_02.jpg', 1, 0, '2025-04-07 17:45:01');
INSERT INTO `pms_product_images` VALUES (58, 26, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180607/5ac1bf5fN2522b9dc.jpg', 2, 0, '2025-04-07 17:45:01');
INSERT INTO `pms_product_images` VALUES (59, 37, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221028/iphone14_003.jpg', 2, 0, '2025-04-07 17:45:01');
INSERT INTO `pms_product_images` VALUES (60, 37, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221028/iphone14_004.jpg', 3, 0, '2025-04-07 17:45:01');

-- ----------------------------
-- Table structure for pms_product_promotions
-- ----------------------------
DROP TABLE IF EXISTS `pms_product_promotions`;
CREATE TABLE `pms_product_promotions`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '促销ID',
  `product_id` bigint(0) NOT NULL COMMENT '商品ID',
  `sku_id` bigint(0) NULL DEFAULT NULL COMMENT 'SKU ID',
  `promotion_type` tinyint(0) NOT NULL COMMENT '促销类型：0-无促销，1-特价促销，2-会员价格，3-阶梯价格，4-满减价格',
  `promotion_price` decimal(10, 2) NOT NULL COMMENT '促销价格',
  `promotion_start_time` datetime(0) NOT NULL COMMENT '促销开始时间',
  `promotion_end_time` datetime(0) NOT NULL COMMENT '促销结束时间',
  `promotion_limit` int(0) NULL DEFAULT NULL COMMENT '促销限购数量',
  `promotion_rules` json NULL COMMENT '促销规则（JSON格式）',
  `promotion_status` tinyint(0) NULL DEFAULT 0 COMMENT '促销状态：0-未开始，1-进行中，2-已结束',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_product_id`(`product_id`) USING BTREE,
  INDEX `idx_sku_id`(`sku_id`) USING BTREE,
  CONSTRAINT `fk_promotion_product` FOREIGN KEY (`product_id`) REFERENCES `pms_products` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_promotion_sku` FOREIGN KEY (`sku_id`) REFERENCES `pms_product_skus` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品促销表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pms_product_promotions
-- ----------------------------
INSERT INTO `pms_product_promotions` VALUES (1, 26, NULL, 1, 3659.00, '2023-01-10 15:49:38', '2023-01-31 00:00:00', 0, NULL, 1, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_promotions` VALUES (2, 29, NULL, 1, 4799.00, '2020-05-04 15:12:54', '2020-05-30 00:00:00', 0, NULL, 1, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_promotions` VALUES (3, 45, NULL, 4, 999.00, '2022-11-09 16:15:50', '2022-11-25 00:00:00', 0, NULL, 1, '2025-04-07 17:45:01', '2025-04-07 17:45:01');

-- ----------------------------
-- Table structure for pms_product_service_guarantees
-- ----------------------------
DROP TABLE IF EXISTS `pms_product_service_guarantees`;
CREATE TABLE `pms_product_service_guarantees`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '服务保障ID',
  `product_id` bigint(0) NOT NULL COMMENT '商品ID',
  `sku_id` bigint(0) NULL DEFAULT NULL COMMENT 'SKU ID',
  `service_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '服务类型',
  `service_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '服务名称',
  `service_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '服务描述',
  `service_rules` json NULL COMMENT '服务规则（JSON格式）',
  `service_status` tinyint(0) NULL DEFAULT 0 COMMENT '服务状态：0-禁用，1-启用',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_product_id`(`product_id`) USING BTREE,
  INDEX `idx_sku_id`(`sku_id`) USING BTREE,
  CONSTRAINT `fk_service_product` FOREIGN KEY (`product_id`) REFERENCES `pms_products` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_service_sku` FOREIGN KEY (`sku_id`) REFERENCES `pms_product_skus` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品服务保障表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pms_product_skus
-- ----------------------------
DROP TABLE IF EXISTS `pms_product_skus`;
CREATE TABLE `pms_product_skus`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'SKU ID',
  `product_id` bigint(0) NOT NULL COMMENT '商品ID',
  `sku_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'SKU编码',
  `sku_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'SKU名称',
  `sku_brief` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'SKU简介',
  `sku_keywords` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'SKU关键词',
  `price` decimal(10, 2) NOT NULL COMMENT '价格',
  `stock` int(0) NULL DEFAULT 0 COMMENT '库存',
  `low_stock` int(0) NULL DEFAULT 0 COMMENT '预警库存',
  `image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'SKU图片',
  `sku_images` json NULL COMMENT 'SKU图片（JSON格式）',
  `sku_videos` json NULL COMMENT 'SKU视频（JSON格式）',
  `sku_details` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT 'SKU详情',
  `sku_attributes` json NULL COMMENT 'SKU属性（JSON格式）',
  `sku_promotion_type` tinyint(0) NULL DEFAULT 0 COMMENT 'SKU促销类型：0-无促销，1-特价促销，2-会员价格，3-阶梯价格，4-满减价格',
  `sku_promotion_price` decimal(10, 2) NULL DEFAULT NULL COMMENT 'SKU促销价格',
  `sku_promotion_start_time` datetime(0) NULL DEFAULT NULL COMMENT 'SKU促销开始时间',
  `sku_promotion_end_time` datetime(0) NULL DEFAULT NULL COMMENT 'SKU促销结束时间',
  `sku_promotion_limit` int(0) NULL DEFAULT NULL COMMENT 'SKU促销限购数量',
  `sku_service_guarantees` json NULL COMMENT 'SKU服务保障（JSON格式）',
  `sku_rating` decimal(2, 1) NULL DEFAULT 0.0 COMMENT 'SKU评分',
  `sku_rating_count` int(0) NULL DEFAULT 0 COMMENT 'SKU评分数量',
  `sku_comment_count` int(0) NULL DEFAULT 0 COMMENT 'SKU评价数量',
  `sku_view_count` int(0) NULL DEFAULT 0 COMMENT 'SKU浏览量',
  `sku_favorite_count` int(0) NULL DEFAULT 0 COMMENT 'SKU收藏量',
  `sku_share_count` int(0) NULL DEFAULT 0 COMMENT 'SKU分享量',
  `sku_sale_count` int(0) NULL DEFAULT 0 COMMENT 'SKU销量',
  `sku_return_count` int(0) NULL DEFAULT 0 COMMENT 'SKU退货量',
  `sku_refund_count` int(0) NULL DEFAULT 0 COMMENT 'SKU退款量',
  `sku_complaint_count` int(0) NULL DEFAULT 0 COMMENT 'SKU投诉量',
  `sku_audit_status` tinyint(0) NULL DEFAULT 0 COMMENT 'SKU审核状态：0-待审核，1-审核通过，2-审核不通过',
  `sku_audit_time` datetime(0) NULL DEFAULT NULL COMMENT 'SKU审核时间',
  `sku_audit_user` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'SKU审核人',
  `sku_audit_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'SKU审核备注',
  `sku_status` tinyint(0) NULL DEFAULT 0 COMMENT 'SKU状态：0-下架，1-上架，2-删除',
  `sku_status_time` datetime(0) NULL DEFAULT NULL COMMENT 'SKU状态变更时间',
  `sku_status_user` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'SKU状态变更人',
  `sku_status_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'SKU状态变更备注',
  `sku_version` int(0) NULL DEFAULT 1 COMMENT 'SKU版本号',
  `sku_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'SKU备注',
  `sku_extra` json NULL COMMENT 'SKU扩展信息（JSON格式）',
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
) ENGINE = InnoDB AUTO_INCREMENT = 52 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品SKU表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pms_product_skus
-- ----------------------------
INSERT INTO `pms_product_skus` VALUES (18, 6, 'WH1000XM5-BLACK', NULL, NULL, NULL, 2699.00, 100, 20, '/uploads/skus/wh1000xm5-black.png', NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, '{\"颜色\": \"黑色\"}', '黑色', 0.00, 0.25, 500, 0, '2025-03-16 13:24:09', '2025-03-16 13:24:09');
INSERT INTO `pms_product_skus` VALUES (19, 6, 'WH1000XM5-WHITE', NULL, NULL, NULL, 2699.00, 100, 20, '/uploads/skus/wh1000xm5-white.png', NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, '{\"颜色\": \"白色\"}', '白色', 0.00, 0.25, 356, 0, '2025-03-16 13:24:09', '2025-03-16 13:24:09');
INSERT INTO `pms_product_skus` VALUES (20, 6, 'WH1000XM5-BLUE', NULL, NULL, NULL, 2799.00, 100, 20, '/uploads/skus/wh1000xm5-blue.png', NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, '{\"颜色\": \"蓝色\"}', '蓝色', 0.00, 0.25, 0, 0, '2025-03-16 13:24:09', '2025-03-16 13:24:09');
INSERT INTO `pms_product_skus` VALUES (21, 7, 'MI-BAND8PRO-BLACK', NULL, NULL, NULL, 399.00, 300, 50, '/uploads/skus/miband8pro-black.png', NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, '{\"颜色\": \"黑色\"}', '黑色', 0.00, 0.03, 600, 0, '2025-03-16 13:24:09', '2025-03-16 13:24:09');
INSERT INTO `pms_product_skus` VALUES (22, 7, 'MI-BAND8PRO-BLUE', NULL, NULL, NULL, 399.00, 300, 50, '/uploads/skus/miband8pro-blue.png', NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, '{\"颜色\": \"蓝色\"}', '蓝色', 0.00, 0.03, 520, 0, '2025-03-16 13:24:09', '2025-03-16 13:24:09');
INSERT INTO `pms_product_skus` VALUES (23, 7, 'MI-BAND8PRO-GREEN', NULL, NULL, NULL, 399.00, 300, 50, '/uploads/skus/miband8pro-green.png', NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, '{\"颜色\": \"绿色\"}', '绿色', 0.00, 0.03, 400, 0, '2025-03-16 13:24:09', '2025-03-16 13:24:09');
INSERT INTO `pms_product_skus` VALUES (24, 7, 'MI-BAND8PRO-RED', NULL, NULL, NULL, 459.00, 100, 20, '/uploads/skus/miband8pro-red.png', NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, '{\"颜色\": \"红色\"}', '红色限定版', 0.00, 0.03, 0, 0, '2025-03-16 13:24:09', '2025-03-16 13:24:09');
INSERT INTO `pms_product_skus` VALUES (25, 8, 'HW-TV-V75S-STD', NULL, NULL, NULL, 12999.00, 50, 10, '/uploads/skus/huawei-v75s.png', NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, '{\"版本\": \"标准版\"}', '标准版', 0.50, 30.00, 156, 0, '2025-03-16 13:24:09', '2025-03-16 13:24:09');
INSERT INTO `pms_product_skus` VALUES (26, 8, 'HW-TV-V75S-PRO', NULL, NULL, NULL, 14999.00, 50, 10, '/uploads/skus/huawei-v75s-pro.png', NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, '{\"版本\": \"Pro版\"}', 'Pro版', 0.50, 30.50, 100, 0, '2025-03-16 13:24:09', '2025-03-16 13:24:09');
INSERT INTO `pms_product_skus` VALUES (27, 9, 'PHILIPS-HUE-3BULB', NULL, NULL, NULL, 799.00, 100, 20, '/uploads/skus/philips-hue-3.png', NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, '{\"套装\": \"3个灯泡套装\"}', '3个灯泡套装', 0.01, 0.50, 250, 0, '2025-03-16 13:24:09', '2025-03-16 13:24:09');
INSERT INTO `pms_product_skus` VALUES (28, 9, 'PHILIPS-HUE-5BULB', NULL, NULL, NULL, 1299.00, 100, 20, '/uploads/skus/philips-hue-5.png', NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, '{\"套装\": \"5个灯泡套装\"}', '5个灯泡套装', 0.01, 0.80, 173, 0, '2025-03-16 13:24:09', '2025-03-16 13:24:09');
INSERT INTO `pms_product_skus` VALUES (29, 10, 'SONY-A7IV-BODY', NULL, NULL, NULL, 14999.00, 20, 5, '/uploads/skus/sony-a7iv-body.png', NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, '{\"套装\": \"机身\"}', '机身', 0.01, 0.70, 100, 0, '2025-03-16 13:24:09', '2025-03-16 13:24:09');
INSERT INTO `pms_product_skus` VALUES (30, 10, 'SONY-A7IV-KIT', NULL, NULL, NULL, 17999.00, 30, 5, '/uploads/skus/sony-a7iv-kit.png', NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, '{\"套装\": \"标准镜头套装\"}', '标准镜头套装', 0.01, 1.20, 89, 0, '2025-03-16 13:24:09', '2025-03-16 13:24:09');
INSERT INTO `pms_product_skus` VALUES (31, 14, 'SKU142406', NULL, NULL, NULL, 0.01, 100, 10, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, '{}', NULL, NULL, NULL, 0, 0, '2025-03-25 20:36:22', '2025-03-25 20:36:22');
INSERT INTO `pms_product_skus` VALUES (32, 26, '6946605-DEFAULT', NULL, NULL, NULL, 3788.00, 1000, 0, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, '{\"默认\": \"默认规格\"}', NULL, NULL, NULL, 0, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_skus` VALUES (33, 27, '7437788-DEFAULT', NULL, NULL, NULL, 2699.00, 100, 0, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, '{\"默认\": \"默认规格\"}', NULL, NULL, NULL, 0, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_skus` VALUES (34, 28, '7437789-DEFAULT', NULL, NULL, NULL, 649.00, 100, 0, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, '{\"默认\": \"默认规格\"}', NULL, NULL, NULL, 0, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_skus` VALUES (35, 29, '7437799-DEFAULT', NULL, NULL, NULL, 5499.00, 100, 0, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, '{\"默认\": \"默认规格\"}', NULL, NULL, NULL, 0, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_skus` VALUES (36, 30, 'HNTBJ2E042A-DEFAULT', NULL, NULL, NULL, 98.00, 100, 0, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, '{\"默认\": \"默认规格\"}', NULL, NULL, NULL, 0, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_skus` VALUES (37, 31, 'HNTBJ2E080A-DEFAULT', NULL, NULL, NULL, 98.00, 100, 0, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, '{\"默认\": \"默认规格\"}', NULL, NULL, NULL, 0, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_skus` VALUES (38, 32, 'HNTBJ2E153A-DEFAULT', NULL, NULL, NULL, 68.00, 100, 0, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, '{\"默认\": \"默认规格\"}', NULL, NULL, NULL, 0, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_skus` VALUES (39, 33, '4609652-DEFAULT', NULL, NULL, NULL, 2499.00, 100, 0, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, '{\"默认\": \"默认规格\"}', NULL, NULL, NULL, 0, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_skus` VALUES (40, 34, '4609660-DEFAULT', NULL, NULL, NULL, 3999.00, 100, 0, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, '{\"默认\": \"默认规格\"}', NULL, NULL, NULL, 0, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_skus` VALUES (41, 35, '6799342-DEFAULT', NULL, NULL, NULL, 369.00, 100, 0, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, '{\"默认\": \"默认规格\"}', NULL, NULL, NULL, 0, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_skus` VALUES (42, 36, '6799345-DEFAULT', NULL, NULL, NULL, 499.00, 100, 0, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, '{\"默认\": \"默认规格\"}', NULL, NULL, NULL, 0, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_skus` VALUES (43, 37, '100038005189-DEFAULT', NULL, NULL, NULL, 5999.00, 1000, 0, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, '{\"默认\": \"默认规格\"}', NULL, NULL, NULL, 0, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_skus` VALUES (44, 38, '100044025833-DEFAULT', NULL, NULL, NULL, 3599.00, 1000, 0, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, '{\"默认\": \"默认规格\"}', NULL, NULL, NULL, 0, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_skus` VALUES (45, 39, '100023207945-DEFAULT', NULL, NULL, NULL, 5599.00, 500, 0, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, '{\"默认\": \"默认规格\"}', NULL, NULL, NULL, 0, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_skus` VALUES (46, 40, '100027789721-DEFAULT', NULL, NULL, NULL, 2999.00, 500, 0, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, '{\"默认\": \"默认规格\"}', NULL, NULL, NULL, 0, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_skus` VALUES (47, 41, '100035246702-DEFAULT', NULL, NULL, NULL, 2099.00, 1000, 0, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, '{\"默认\": \"默认规格\"}', NULL, NULL, NULL, 0, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_skus` VALUES (48, 42, '100035295081-DEFAULT', NULL, NULL, NULL, 4999.00, 1000, 0, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, '{\"默认\": \"默认规格\"}', NULL, NULL, NULL, 0, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_skus` VALUES (49, 43, '10044060351752-DEFAULT', NULL, NULL, NULL, 1799.00, 1000, 0, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, '{\"默认\": \"默认规格\"}', NULL, NULL, NULL, 0, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_skus` VALUES (50, 44, '100018768480-DEFAULT', NULL, NULL, NULL, 369.00, 1000, 0, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, '{\"默认\": \"默认规格\"}', NULL, NULL, NULL, 0, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_skus` VALUES (51, 45, '10052147850350-DEFAULT', NULL, NULL, NULL, 2299.00, 1000, 0, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, '{\"默认\": \"默认规格\"}', NULL, NULL, NULL, 0, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_product_skus` VALUES (52, 46, '11111111111111111-DEFAULT', NULL, NULL, NULL, 1111.00, 0, 0, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, '{\"默认\": \"默认规格\"}', NULL, NULL, NULL, 0, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');

-- ----------------------------
-- Table structure for pms_products
-- ----------------------------
DROP TABLE IF EXISTS `pms_products`;
CREATE TABLE `pms_products`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称',
  `product_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品编号',
  `category_id` bigint(0) NOT NULL COMMENT '分类ID',
  `attribute_id` bigint(0) NOT NULL COMMENT '属性ID',
  `brand_id` bigint(0) NULL DEFAULT NULL COMMENT '品牌ID',
  `merchant_id` bigint(0) NULL DEFAULT NULL COMMENT '商家ID',
  `price` decimal(10, 2) NOT NULL COMMENT '商品价格',
  `original_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '原价',
  `main_image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '主图URL',
  `album_pics` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '画册图片，连产品图片限制为5张，以逗号分割',
  `detail_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品详情标题',
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
  `promotion_type` tinyint(0) NULL DEFAULT 0 COMMENT '促销类型：0-无促销，1-特价促销，2-会员价格，3-阶梯价格，4-满减价格',
  `promotion_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '促销价格',
  `promotion_start_time` datetime(0) NULL DEFAULT NULL COMMENT '促销开始时间',
  `promotion_end_time` datetime(0) NULL DEFAULT NULL COMMENT '促销结束时间',
  `promotion_limit` int(0) NULL DEFAULT NULL COMMENT '促销限购数量',
  `service_guarantees` json NULL COMMENT '服务保障（JSON格式）',
  `product_attributes` json NULL COMMENT '商品属性（JSON格式）',
  `product_images` json NULL COMMENT '商品图片（JSON格式）',
  `product_videos` json NULL COMMENT '商品视频（JSON格式）',
  `product_details` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '商品详情',
  `product_specs` json NULL COMMENT '商品规格（JSON格式）',
  `product_subjects` json NULL COMMENT '商品专题（JSON格式）',
  `product_tags` json NULL COMMENT '商品标签（JSON格式）',
  `product_related` json NULL COMMENT '相关商品（JSON格式）',
  `product_recommend` json NULL COMMENT '推荐商品（JSON格式）',
  `product_comment` json NULL COMMENT '商品评价（JSON格式）',
  `product_rating` decimal(2, 1) NULL DEFAULT 0.0 COMMENT '商品评分',
  `product_rating_count` int(0) NULL DEFAULT 0 COMMENT '商品评分数量',
  `product_comment_count` int(0) NULL DEFAULT 0 COMMENT '商品评价数量',
  `product_view_count` int(0) NULL DEFAULT 0 COMMENT '商品浏览量',
  `product_favorite_count` int(0) NULL DEFAULT 0 COMMENT '商品收藏量',
  `product_share_count` int(0) NULL DEFAULT 0 COMMENT '商品分享量',
  `product_sale_count` int(0) NULL DEFAULT 0 COMMENT '商品销量',
  `product_return_count` int(0) NULL DEFAULT 0 COMMENT '商品退货量',
  `product_refund_count` int(0) NULL DEFAULT 0 COMMENT '商品退款量',
  `product_complaint_count` int(0) NULL DEFAULT 0 COMMENT '商品投诉量',
  `product_audit_status` tinyint(0) NULL DEFAULT 0 COMMENT '商品审核状态：0-待审核，1-审核通过，2-审核不通过',
  `product_audit_time` datetime(0) NULL DEFAULT NULL COMMENT '商品审核时间',
  `product_audit_user` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品审核人',
  `product_audit_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品审核备注',
  `product_status` tinyint(0) NULL DEFAULT 0 COMMENT '商品状态：0-下架，1-上架，2-删除',
  `product_status_time` datetime(0) NULL DEFAULT NULL COMMENT '商品状态变更时间',
  `product_status_user` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品状态变更人',
  `product_status_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品状态变更备注',
  `product_version` int(0) NULL DEFAULT 1 COMMENT '商品版本号',
  `product_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品备注',
  `product_extra` json NULL COMMENT '商品扩展信息（JSON格式）',
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
) ENGINE = InnoDB AUTO_INCREMENT = 46 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pms_products
-- ----------------------------
INSERT INTO `pms_products` VALUES (15, '三星Galaxy S24 Ultra', 'SAMS-S24ULTRA', 5, 0, 4, NULL, 9999.00, 10499.00, 'https://images.samsung.com/is/image/samsung/assets/tw/smartphones/galaxy-s24-ultra/images/galaxy-s24-ultra-highlights-kv.jpg?imbypass=true', NULL, '三星Galaxy S24 Ultra 旗舰手机', '三星,Galaxy,S24,Ultra,AI,高端', '三星年度旗舰手机，搭载骁龙8 Gen 3处理器和强大的AI功能', 843, 500, '台', 0.24, 4, 1, 1, 1, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 4.8, 320, 290, 15000, 750, 320, 840, 10, 5, 2, 1, '2025-04-01 10:00:00', 'admin', '审核通过', 1, '2025-04-01 10:00:00', 'admin', NULL, 1, NULL, NULL, 0, '2025-04-01 10:00:00', '2025-04-02 20:11:26');
INSERT INTO `pms_products` VALUES (16, 'Apple MacBook Pro 16', 'APPL-MBP16-M2', 6, 0, 3, NULL, 19999.00, 21999.00, 'https://store.storeimages.cdn-apple.com/8756/as-images.apple.com/is/mbp16-witb-spaceblack-202410?wid=1216&hei=784&fmt=p-jpg&qlt=95&.v=1728330927913', NULL, 'Apple MacBook Pro 16 M2 Pro', 'Apple,MacBook,Pro,M2,专业,笔记本', 'Apple最强大的专业笔记本，搭载M2 Pro芯片和16英寸Liquid视网膜XDR显示屏', 625, 300, '台', 2.20, 5, 1, 1, 1, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 4.9, 280, 230, 12000, 800, 250, 620, 5, 3, 0, 1, '2025-04-01 10:00:00', 'admin', '审核通过', 1, '2025-04-01 10:00:00', 'admin', NULL, 1, NULL, NULL, 0, '2025-04-01 10:00:00', '2025-04-02 20:12:37');
INSERT INTO `pms_products` VALUES (17, 'LG C3 OLED电视 65英寸', 'LG-OLED65C3', 13, 0, 8, NULL, 13999.00, 15999.00, 'https://www.lg.com/cn/images/tvs/md07572084/gallery/1100-1.jpg', NULL, 'LG C3系列 4K OLED电视', 'LG,OLED,C3,电视,4K,智能', 'LG 2023年C3系列OLED电视，4K分辨率，支持杜比视界和全面的游戏功能', 320, 100, '台', 25.00, 6, 1, 1, 1, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 4.7, 150, 120, 8000, 300, 120, 315, 8, 5, 1, 1, '2025-04-01 10:00:00', 'admin', '审核通过', 1, '2025-04-01 10:00:00', 'admin', NULL, 1, NULL, NULL, 0, '2025-04-01 10:00:00', '2025-04-02 20:13:52');
INSERT INTO `pms_products` VALUES (18, '松下冰箱NR-W56S1', 'PANA-NRW56S1', 8, 0, 9, NULL, 8999.00, 9999.00, 'https://consumer.panasonic.cn/static/upload/image/20221127/1669524295425533.png', NULL, '松下六门冰箱', '松下,冰箱,多门,变频,节能', '松下六门对开冰箱，563L大容量，nanoe™X纳米水离子抑菌，变频节能', 230, 80, '台', 95.00, 7, 1, 1, 1, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 4.6, 90, 75, 5000, 120, 50, 228, 3, 2, 0, 1, '2025-04-01 10:00:00', 'admin', '审核通过', 1, '2025-04-01 10:00:00', 'admin', NULL, 1, NULL, NULL, 0, '2025-04-01 10:00:00', '2025-04-02 20:26:57');
INSERT INTO `pms_products` VALUES (19, '佳能EOS R5微单相机', 'CANON-EOSR5', 12, 0, 11, NULL, 25999.00, 27999.00, 'https://cdn09.dcfever.com/media/cameras/images/2020/07/canon_2377_1594325121.jpg', NULL, '佳能EOS R5全画幅微单相机', '佳能,Canon,EOS,R5,微单,全画幅,相机', '佳能专业级全画幅微单相机，4500万像素，8K视频拍摄，高速连拍，专业摄影师首选', 145, 50, '台', 0.85, 8, 1, 1, 1, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 4.9, 65, 50, 4000, 180, 95, 142, 2, 1, 0, 1, '2025-04-01 10:00:00', 'admin', '审核通过', 1, '2025-04-01 10:00:00', 'admin', NULL, 1, NULL, NULL, 0, '2025-04-01 10:00:00', '2025-04-12 19:56:41');
INSERT INTO `pms_products` VALUES (20, '戴森吸尘器V15 Detect', 'DYSON-V15DETECT', 3, 0, 12, NULL, 4999.00, 5499.00, 'https://m.media-amazon.com/images/I/619XJLzSXJL._AC_SX679_.jpg', NULL, '戴森V15 Detect无线吸尘器', '戴森,Dyson,吸尘器,无线,智能', '戴森最新旗舰吸尘器，搭载激光颗粒探测技术，智能感应灰尘调节吸力，60分钟超长续航', 580, 200, '台', 3.10, 9, 1, 1, 1, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 5.0, 211, 181, 9500, 350, 160, 575, 12, 8, 3, 1, '2025-04-01 10:00:00', 'admin', '审核通过', 1, '2025-04-01 10:00:00', 'admin', NULL, 1, NULL, NULL, 0, '2025-04-01 10:00:00', '2025-04-02 20:29:03');
INSERT INTO `pms_products` VALUES (21, '小米智能空气净化器Pro 3', 'MI-AIRPRO3', 16, 0, 1, NULL, 1499.00, 1699.00, 'https://c1.mifile.cn/f/i/16/chain/airpro//mj-gallery-01.jpg', NULL, '小米空气净化器Pro 3', '小米,空气净化器,智能,OLED,净化', '小米高端空气净化器，OLED屏幕显示，CADR值高达800立方米/小时，适用面积高达92平方米', 1250, 500, '台', 9.80, 10, 1, 1, 1, 1, 1, 1299.00, '2025-04-01 00:00:00', '2025-04-15 23:59:59', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 4.5, 420, 380, 18000, 560, 280, 1240, 20, 15, 5, 1, '2025-04-01 10:00:00', 'admin', '审核通过', 1, '2025-04-01 10:00:00', 'admin', NULL, 1, NULL, NULL, 0, '2025-04-01 10:00:00', '2025-04-02 20:30:45');
INSERT INTO `pms_products` VALUES (22, 'Apple iPad Pro 12.9', 'APPL-IPADPRO-12', 7, 0, 3, NULL, 8999.00, 9499.00, 'https://store.storeimages.cdn-apple.com/1/as-images.apple.com/is/ipad-pro-model-select-gallery-1-202405?wid=5120&hei=2880&fmt=webp&qlt=70&.v=cXN0QTVTNDBtbGIzcy91THBPRThnNE5sSFgwakNWNmlhZ2d5NGpHdllWY09WV3R2ZHdZMXRzTjZIcWdMTlg4eUJQYkhSV3V1dC9oa0s5K3lqMGtUaFMvR01EVDlzK0hIS1J2bTdpY0pVeTF1Yy9kL1dQa3EzdWh4Nzk1ZnZTYWY&traceId=1', NULL, 'Apple iPad Pro 12.9英寸 M2芯片', 'Apple,iPad,Pro,平板,M2,专业', 'Apple新一代iPad Pro，搭载M2芯片，12.9英寸超视网膜XDR显示屏，专业创作者的理想选择', 720, 300, '台', 0.68, 11, 1, 1, 1, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 4.8, 250, 220, 13500, 630, 310, 715, 8, 5, 1, 1, '2025-04-01 10:00:00', 'admin', '审核通过', 1, '2025-04-01 10:00:00', 'admin', NULL, 1, NULL, NULL, 0, '2025-04-01 10:00:00', '2025-04-02 20:31:26');
INSERT INTO `pms_products` VALUES (23, '华为Watch GT 4智能手表', 'HW-WATCHGT4', 10, 0, 2, NULL, 1499.00, 1699.00, 'https://consumer.huawei.com/content/dam/huawei-cbg-site/cn/mkt/pdp/wearables/watch-gt4-new/images/sec1/huawei-watch-gt4-2x.jpg', NULL, '华为Watch GT 4智能手表', '华为,智能手表,健康,运动,监测', '华为最新一代智能手表，支持心率、血氧、睡眠监测，提供100多种运动模式，14天超长续航', 980, 400, '个', 0.05, 12, 1, 1, 1, 1, 1, 1299.00, '2025-04-01 00:00:00', '2025-04-15 23:59:59', 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 4.6, 320, 290, 16500, 480, 250, 970, 15, 12, 3, 1, '2025-04-01 10:00:00', 'admin', '审核通过', 1, '2025-04-01 10:00:00', 'admin', NULL, 1, NULL, NULL, 0, '2025-04-01 10:00:00', '2025-04-02 20:31:50');
INSERT INTO `pms_products` VALUES (24, '小米扫地机器人X20', 'MI-ROBOT-X20', 16, 0, 1, NULL, 2999.00, 3299.00, 'https://i02.appmifile.com/mi-com-product/fly-birds/xiaomi-robot-vacuum-x20/pc/d5c5dafb772de049aca294626748a712.jpg?f=webp', NULL, '小米扫地机器人X20', '小米,扫地机器人,智能,清洁,拖地', '小米高端扫地机器人，6000Pa超强吸力，AI避障，智能规划清扫路线，支持扫拖一体和自动回充', 850, 300, '台', 3.50, 13, 1, 1, 1, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 4.5, 280, 250, 12500, 380, 190, 840, 12, 10, 4, 1, '2025-04-01 10:00:00', 'admin', '审核通过', 1, '2025-04-01 10:00:00', 'admin', NULL, 1, NULL, NULL, 0, '2025-04-01 10:00:00', '2025-04-02 20:32:15');
INSERT INTO `pms_products` VALUES (26, '华为 HUAWEI P20 ', '6946605', 24, 0, 2, NULL, 3788.00, 4288.00, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180607/5ac1bf58Ndefaac16.jpg', NULL, '', '', 'AI智慧全面屏 6GB +64GB 亮黑色 全网通版 移动联通电信4G手机 双卡双待手机 双卡双待', 100, 1000, '件', 0.00, 100, 1, 1, 1, 0, 1, 3659.00, '2023-01-10 15:49:38', '2023-01-31 00:00:00', 0, NULL, NULL, NULL, NULL, '', NULL, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, 1, NULL, NULL, NULL, 1, NULL, NULL, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_products` VALUES (27, '小米8 全面屏游戏智能手机 6GB+64GB 黑色 全网通4G 双卡双待', '7437788', 24, 0, 1, NULL, 2699.00, 2699.00, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180615/xiaomi.jpg', NULL, '', '', '骁龙845处理器，红外人脸解锁，AI变焦双摄，AI语音助手小米6X低至1299，点击抢购', 99, 100, '', 0.00, 0, 1, 1, 1, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '小米8 全面屏游戏智能手机 6GB+64GB 黑色 全网通4G 双卡双待', NULL, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, 1, NULL, NULL, NULL, 1, NULL, NULL, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_products` VALUES (28, '小米 红米5A 全网通版 3GB+32GB 香槟金 移动联通电信4G手机 双卡双待', '7437789', 24, 0, 1, NULL, 649.00, 649.00, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180615/5a9d248cN071f4959.jpg', NULL, '', '', '8天超长待机，137g轻巧机身，高通骁龙处理器小米6X低至1299，点击抢购', 98, 100, '', 0.00, 0, 1, 1, 1, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, 1, NULL, NULL, NULL, 1, NULL, NULL, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_products` VALUES (29, 'Apple iPhone 8 Plus 64GB 红色特别版 移动联通电信4G手机', '7437799', 24, 0, 3, NULL, 5499.00, 5499.00, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180615/5acc5248N6a5f81cd.jpg', NULL, '', '', '【限时限量抢购】Apple产品年中狂欢节，好物尽享，美在智慧！速来 >> 勾选[保障服务][原厂保2年]，获得AppleCare+全方位服务计划，原厂延保售后无忧。', 97, 100, '', 0.00, 0, 1, 1, 1, 0, 1, 4799.00, '2020-05-04 15:12:54', '2020-05-30 00:00:00', 0, NULL, NULL, NULL, NULL, '', NULL, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, 1, NULL, NULL, NULL, 1, NULL, NULL, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_products` VALUES (30, 'HLA海澜之家简约动物印花短袖T恤', 'HNTBJ2E042A', 21, 0, 14, NULL, 98.00, 98.00, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180615/5ad83a4fN6ff67ecd.jpg!cc_350x449.jpg', NULL, '', '', '2018夏季新品微弹舒适新款短T男生 6月6日-6月20日，满300减30，参与互动赢百元礼券，立即分享赢大奖', 0, 100, '', 0.00, 0, 1, 1, 1, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, 1, NULL, NULL, NULL, 1, NULL, NULL, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_products` VALUES (31, 'HLA海澜之家蓝灰花纹圆领针织布短袖T恤', 'HNTBJ2E080A', 21, 0, 14, NULL, 98.00, 98.00, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180615/5ac98b64N70acd82f.jpg!cc_350x449.jpg', NULL, '', '', '2018夏季新品短袖T恤男HNTBJ2E080A 蓝灰花纹80 175/92A/L80A 蓝灰花纹80 175/92A/L', 0, 100, '', 0.00, 0, 1, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, 1, NULL, NULL, NULL, 1, NULL, NULL, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_products` VALUES (32, 'HLA海澜之家短袖T恤男基础款', 'HNTBJ2E153A', 21, 0, 14, NULL, 68.00, 68.00, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180615/5a51eb88Na4797877.jpg', NULL, '', '', 'HLA海澜之家短袖T恤男基础款简约圆领HNTBJ2E153A藏青(F3)175/92A(50)', 0, 100, '', 0.00, 0, 1, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, 1, NULL, NULL, NULL, 1, NULL, NULL, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_products` VALUES (33, '小米（MI）小米电视4A ', '4609652', 9, 0, 1, NULL, 2499.00, 2499.00, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180615/5b02804dN66004d73.jpg', NULL, '', '', '小米（MI）小米电视4A 55英寸 L55M5-AZ/L55M5-AD 2GB+8GB HDR 4K超高清 人工智能网络液晶平板电视', 0, 100, '', 0.00, 0, 1, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, 1, NULL, NULL, NULL, 1, NULL, NULL, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_products` VALUES (34, '小米（MI）小米电视4A 65英寸', '4609660', 9, 0, 1, NULL, 3999.00, 3999.00, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180615/5b028530N51eee7d4.jpg', NULL, '', '', ' L65M5-AZ/L65M5-AD 2GB+8GB HDR 4K超高清 人工智能网络液晶平板电视', 0, 100, '', 0.00, 0, 1, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, 1, NULL, NULL, NULL, 1, NULL, NULL, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_products` VALUES (35, '耐克NIKE 男子 休闲鞋 ROSHE RUN 运动鞋 511881-010黑色41码', '6799342', 25, 0, 10, NULL, 369.00, 369.00, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180615/5b235bb9Nf606460b.jpg', NULL, '', '', '耐克NIKE 男子 休闲鞋 ROSHE RUN 运动鞋 511881-010黑色41码', 0, 100, '', 0.00, 0, 1, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, 1, NULL, NULL, NULL, 1, NULL, NULL, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_products` VALUES (36, '耐克NIKE 男子 气垫 休闲鞋 AIR MAX 90 ESSENTIAL 运动鞋 AJ1285-101白色41码', '6799345', 25, 0, 10, NULL, 499.00, 499.00, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180615/5b19403eN9f0b3cb8.jpg', NULL, '', '', '耐克NIKE 男子 气垫 休闲鞋 AIR MAX 90 ESSENTIAL 运动鞋 AJ1285-101白色41码', 0, 100, '', 0.00, 0, 1, 1, 1, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, 1, NULL, NULL, NULL, 1, NULL, NULL, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_products` VALUES (37, 'Apple iPhone 14 (A2884) 128GB 支持移动联通电信5G 双卡双待手机', '100038005189', 24, 0, 3, NULL, 5999.00, 5999.00, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221028/iphone14_001.jpg', NULL, '', '', '【11.11大爱超大爱】指定iPhone14产品限时限量领券立减601元！！！部分iPhone产品现货抢购确认收货即送原厂手机壳10元优惠券！！！猛戳 ', 0, 1000, '', 208.00, 200, 1, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, 1, NULL, NULL, NULL, 1, NULL, NULL, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_products` VALUES (38, 'Apple iPad 10.9英寸平板电脑 2022年款', '100044025833', 7, 0, 3, NULL, 3599.00, 3599.00, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221028/ipad_001.jpg', NULL, '', '', '【11.11大爱超大爱】iPad9代限量抢购，价格优惠，更享以旧换新至高补贴325元！！快来抢购吧！！ ', 0, 1000, '', 0.00, 0, 1, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, 1, NULL, NULL, NULL, 1, NULL, NULL, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_products` VALUES (39, '小米 Xiaomi Book Pro 14 2022 锐龙版 2.8K超清大师屏 高端轻薄笔记本电脑', '100023207945', 6, 0, 1, NULL, 5599.00, 5599.00, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221028/xiaomi_computer_001.jpg', NULL, '', '', '【双十一大促来袭】指定型号至高优惠1000，以旧换新至高补贴1000元，晒单赢好礼', 0, 500, '', 0.00, 0, 1, 0, 1, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, 1, NULL, NULL, NULL, 1, NULL, NULL, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_products` VALUES (40, '小米12 Pro 天玑版 天玑9000+处理器 5000万疾速影像 2K超视感屏 120Hz高刷 67W快充', '100027789721', 24, 0, 1, NULL, 2999.00, 2999.00, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221104/xiaomi_12_pro_01.jpg', NULL, '', '', '天玑9000+处理器、5160mAh大电量、2KAmoled超视感屏【点击购买小米11Ultra，戳】 ', 0, 500, '', 0.00, 0, 1, 0, 1, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, 1, NULL, NULL, NULL, 1, NULL, NULL, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_products` VALUES (41, 'Redmi K50 天玑8100 2K柔性直屏 OIS光学防抖 67W快充 5500mAh大电量', '100035246702', 24, 0, 1, NULL, 2099.00, 2099.00, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221104/redmi_k50_01.jpg', NULL, '', '', '【品质好物】天玑8100，2K直屏，5500mAh大电量【Note12Pro火热抢购中】 ', 0, 1000, '', 0.00, 0, 1, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, 1, NULL, NULL, NULL, 1, NULL, NULL, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_products` VALUES (42, 'HUAWEI Mate 50 直屏旗舰 超光变XMAGE影像 北斗卫星消息', '100035295081', 24, 0, 2, NULL, 4999.00, 4999.00, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221104/huawei_mate50_01.jpg', NULL, '', '', '【华为Mate50新品上市】内置66W华为充电套装，超光变XMAGE影像,北斗卫星消息，鸿蒙操作系统3.0！立即抢购！华为新品可持续计划，猛戳》 ', 0, 1000, '', 0.00, 0, 1, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, 1, NULL, NULL, NULL, 1, NULL, NULL, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_products` VALUES (43, '万和（Vanward)燃气热水器天然气家用四重防冻直流变频节能全新升级增压水伺服恒温高抗风', '10044060351752', 22, 0, 13, NULL, 1799.00, 1799.00, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221104/wanhe_13L_01.png', NULL, '', '', '【家电11.11享低价，抢到手价不高于1199】【发布种草秀享好礼！同价11.11买贵补差】爆款超一级能效零冷水】 ', 0, 1000, '', 0.00, 0, 1, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, 1, NULL, NULL, NULL, 1, NULL, NULL, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_products` VALUES (44, '三星（SAMSUNG）500GB SSD固态硬盘 M.2接口(NVMe协议)', '100018768480', 26, 0, 4, NULL, 369.00, 369.00, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221108/sanxing_ssd_02.jpg', NULL, '', '', '【满血无缓存！进店抽百元E卡，部分型号白条三期免息】兼具速度与可靠性！读速高达3500MB/s，全功率模式！点击 ', 0, 1000, '', 0.00, 0, 1, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, 1, NULL, NULL, NULL, 1, NULL, NULL, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_products` VALUES (45, 'OPPO Reno8 8GB+128GB 鸢尾紫 新配色上市 80W超级闪充 5000万水光人像三摄', '10052147850350', 24, 0, 11, NULL, 2299.00, 2299.00, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221108/oppo_r8_01.jpg', NULL, '', '', '【11.11提前购机享价保，好货不用等，系统申请一键价保补差!】【Reno8Pro爆款优惠】 ', 0, 1000, '', 0.00, 0, 1, 0, 0, 0, 4, 999.00, '2022-11-09 16:15:50', '2022-11-25 00:00:00', 0, NULL, NULL, NULL, NULL, '', NULL, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, 1, NULL, NULL, NULL, 1, NULL, NULL, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');
INSERT INTO `pms_products` VALUES (46, '11111111111', '11111111111111111', 25, 0, 1, NULL, 1111.00, 111.11, '', NULL, '', '', '1111111111', 0, 0, '1', 111.00, 2, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1111111111111111111', NULL, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, 0, '2025-04-07 17:45:01', '2025-04-07 17:45:01');

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
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '优惠券适用商品关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms_coupon_products
-- ----------------------------
INSERT INTO `sms_coupon_products` VALUES (1, 2, 3, '小米13', NULL, '2025-03-30 20:27:55');
INSERT INTO `sms_coupon_products` VALUES (2, 2, 4, '华为Mate60 Pro', NULL, '2025-03-30 20:27:55');

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
  `use_status` tinyint(0) NULL DEFAULT 0 COMMENT '优惠券状态，0->未使用；1->已使用；2->已过期',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `publish_status` tinyint(0) NULL DEFAULT 0 COMMENT '发布状态：0-未发布，1-已发布',
  `is_deleted` tinyint(0) NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_start_time`(`start_time`) USING BTREE,
  INDEX `idx_end_time`(`end_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '优惠券表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms_coupons
-- ----------------------------
INSERT INTO `sms_coupons` VALUES (1, '春季大促通用券', 0, 'SPRING2024', 0, 50.00, NULL, 200.00, '2025-03-01 00:00:00', '2025-04-30 23:59:59', 1000, 980, 0, 0, 2, 0, NULL, 1, 0, '2025-03-30 20:27:54', '2025-03-30 20:27:54');
INSERT INTO `sms_coupons` VALUES (2, '手机品类专享券', 2, 'PHONE100', 0, 100.00, NULL, 1000.00, '2025-03-15 00:00:00', '2025-03-31 23:59:59', 500, 500, 0, 0, 1, 0, NULL, 1, 0, '2025-03-30 20:27:54', '2025-03-30 20:27:54');

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
-- Table structure for ums_browse_history
-- ----------------------------
DROP TABLE IF EXISTS `ums_browse_history`;
CREATE TABLE `ums_browse_history`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `userId` bigint(0) NOT NULL COMMENT '用户ID',
  `productId` bigint(0) NOT NULL COMMENT '商品ID',
  `productName` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称',
  `browseCount` int(0) NOT NULL DEFAULT 1 COMMENT '浏览次数',
  `lastBrowseTime` datetime(0) NOT NULL COMMENT '最后浏览时间',
  `createTime` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`userId`) USING BTREE,
  INDEX `idx_product_id`(`productId`) USING BTREE,
  INDEX `idx_browse_time`(`lastBrowseTime`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '会员商品浏览历史记录' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商家表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户通知表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户地址表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ums_user_addresses
-- ----------------------------
INSERT INTO `ums_user_addresses` VALUES (6, 12, 'hcc', '19391629122', '陕西省', '汉中市', '汉台区', '汉中市陕西理工大学北校区', NULL, 0, NULL, 0, '2025-03-27 17:48:41', '2025-03-27 17:48:41');
INSERT INTO `ums_user_addresses` VALUES (7, 12, '张三', '13038534790', '陕西省', '汉中市', NULL, '陕西理工大学北校区', NULL, 1, NULL, 0, '2025-04-04 15:09:42', '2025-04-04 15:09:42');

-- ----------------------------
-- Table structure for ums_user_brand_attentions
-- ----------------------------
DROP TABLE IF EXISTS `ums_user_brand_attentions`;
CREATE TABLE `ums_user_brand_attentions`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '关注ID',
  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
  `brand_id` bigint(0) NOT NULL COMMENT '品牌ID',
  `brand_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '品牌名称',
  `brand_logo` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '品牌Logo',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '关注时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_brand`(`user_id`, `brand_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_brand_id`(`brand_id`) USING BTREE,
  CONSTRAINT `fk_brand_attention_brand` FOREIGN KEY (`brand_id`) REFERENCES `pms_brands` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_brand_attention_user` FOREIGN KEY (`user_id`) REFERENCES `ums_users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户品牌关注表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ums_user_browse_history
-- ----------------------------
DROP TABLE IF EXISTS `ums_user_browse_history`;
CREATE TABLE `ums_user_browse_history`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
  `product_id` bigint(0) NOT NULL COMMENT '商品ID',
  `product_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称',
  `browse_count` int(0) NOT NULL DEFAULT 1 COMMENT '浏览次数',
  `last_browse_time` datetime(0) NOT NULL COMMENT '最后浏览时间',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_product_id`(`product_id`) USING BTREE,
  INDEX `idx_browse_time`(`last_browse_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '会员商品浏览历史记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ums_user_browse_history
-- ----------------------------
INSERT INTO `ums_user_browse_history` VALUES (1, 12, 26, '华为 HUAWEI P20 ', 1, '2025-04-11 17:59:19', '2025-04-11 17:59:19');
INSERT INTO `ums_user_browse_history` VALUES (2, 12, 24, '小米扫地机器人X20', 2, '2025-04-11 17:59:41', '2025-04-11 17:59:29');
INSERT INTO `ums_user_browse_history` VALUES (3, 12, 19, '佳能EOS R5微单相机', 8, '2025-04-12 20:04:22', '2025-04-12 19:55:32');
INSERT INTO `ums_user_browse_history` VALUES (4, 12, 18, '松下冰箱NR-W56S1', 1, '2025-04-12 19:57:19', '2025-04-12 19:57:19');
INSERT INTO `ums_user_browse_history` VALUES (5, 12, 27, '小米8 全面屏游戏智能手机 6GB+64GB 黑色 全网通4G 双卡双待', 1, '2025-04-12 20:04:14', '2025-04-12 20:04:14');

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户角色表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户搜索历史表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ums_user_search_history
-- ----------------------------
INSERT INTO `ums_user_search_history` VALUES (4, 12, '鞋子', 1, '2025-03-29 17:30:52', '2025-03-29 17:30:52');
INSERT INTO `ums_user_search_history` VALUES (5, 12, '风衣', 1, '2025-03-29 17:31:04', '2025-03-29 17:31:04');
INSERT INTO `ums_user_search_history` VALUES (6, 12, '冲锋衣', 1, '2025-03-29 17:31:14', '2025-03-29 17:31:14');
INSERT INTO `ums_user_search_history` VALUES (7, 12, '三星Galaxy S24 Ultra', 34, '2025-04-06 17:45:59', '2025-04-04 18:38:36');
INSERT INTO `ums_user_search_history` VALUES (8, 12, 'Apple MacBook Pro 16', 1, '2025-04-06 17:02:58', '2025-04-06 17:02:58');
INSERT INTO `ums_user_search_history` VALUES (9, 12, '华为Watch GT 4智能手表', 1, '2025-04-06 17:03:21', '2025-04-06 17:03:21');
INSERT INTO `ums_user_search_history` VALUES (10, 12, '小米扫地机器人X20', 1, '2025-04-06 17:03:36', '2025-04-06 17:03:36');
INSERT INTO `ums_user_search_history` VALUES (11, 12, 'LG C3 OLED电视 65英寸', 2, '2025-04-06 17:26:38', '2025-04-06 17:24:12');
INSERT INTO `ums_user_search_history` VALUES (12, 12, '松下冰箱NR-W56S1', 2, '2025-04-06 17:25:04', '2025-04-06 17:24:16');
INSERT INTO `ums_user_search_history` VALUES (13, 12, '小米智能空气净化器Pro 3', 1, '2025-04-06 17:26:44', '2025-04-06 17:26:44');

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
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ums_users
-- ----------------------------
INSERT INTO `ums_users` VALUES (12, '张三', '$2a$10$4VVdd.KINiOILGhvDd2A4eEs7J/HDPguI5YJ2h0iQXGKO6GwC0Ljy', '31315616', NULL, '菜月昴', NULL, 1, '2025-04-04', NULL, 1, NULL, 0, '2025-04-12 19:55:12', NULL, 0, 0, 0, 0, '汉中', NULL, 0, '2025-03-18 17:42:20', '2025-03-18 17:50:26');
INSERT INTO `ums_users` VALUES (14, '19391629120', '$2a$10$xfqv4f9C60kNPzVFc9zi1uccCwnbXTFjlsUUxPzjtLL/8qRbzau62', '19391629120', NULL, NULL, NULL, 0, NULL, NULL, 1, NULL, 0, '2025-04-01 20:03:55', NULL, 0, 0, 0, 0, NULL, NULL, 0, '2025-04-01 20:03:44', '2025-04-01 20:03:44');

SET FOREIGN_KEY_CHECKS = 1;
