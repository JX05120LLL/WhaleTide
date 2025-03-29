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

 Date: 29/03/2025 17:36:22
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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员登录日志表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员操作日志表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员与角色关系表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ams_admins
-- ----------------------------
INSERT INTO `ams_admins` VALUES (12, 'admin', '$2a$10$reSZltiq/qBlHZf3kzEyAeVuJo5iaI0GDjryct15VftfQwieYCTKK', '超级管理员', '123456', NULL, NULL, 0, 1, '2025-03-27 18:11:47', NULL, '超级管理员，拥有所有权限', 1, NULL, NULL, 0, '2025-03-15 18:28:59', '2025-03-17 09:51:25');
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
) ENGINE = InnoDB AUTO_INCREMENT = 43 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '权限表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '资源分类表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '资源表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 154 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色与菜单关系表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色与权限关系表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 133 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色与资源关系表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '购物车表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oms_cart_items
-- ----------------------------
INSERT INTO `oms_cart_items` VALUES (2, 12, 3, '小米13', 'https://cdn.cnbj1.fds.api.mi-img.com/product-images/xiaomi-13kb7buy/11262.png?x-fds-process=image/resize,q_90,f_webp', 18, '黑色', 2699.00, 5, 0, '2025-03-28 19:23:03', '2025-03-28 19:23:03');
INSERT INTO `oms_cart_items` VALUES (3, 12, 3, '小米13', 'https://cdn.cnbj1.fds.api.mi-img.com/product-images/xiaomi-13kb7buy/11262.png?x-fds-process=image/resize,q_90,f_webp', 18, '黑色', 2699.00, 5, 0, '2025-03-28 19:23:04', '2025-03-28 19:23:04');

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
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '品牌表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pms_brands
-- ----------------------------
INSERT INTO `pms_brands` VALUES (1, '小米', '/uploads/brands/xiaomi.png', '小米科技有限责任公司成立于2010年4月，是一家专注于高端智能手机、智能硬件和IoT生态链建设的创新型科技企业。', 'X', 1, 1, 1, '2025-03-16 13:15:47', '2025-03-25 16:43:15');
INSERT INTO `pms_brands` VALUES (2, '华为', '/uploads/brands/huawei.png', '华为技术有限公司成立于1987年，是全球领先的ICT（信息与通信）基础设施和智能终端提供商。', 'H', 2, 1, 1, '2025-03-16 13:15:47', '2025-03-25 16:37:51');
INSERT INTO `pms_brands` VALUES (3, '苹果', '/uploads/brands/apple.png', '苹果公司是美国的一家高科技公司，2007年由苹果电脑公司更名而来，是世界领先的科技公司之一。', 'A', 3, 1, 1, '2025-03-16 13:15:47', '2025-03-16 13:15:47');
INSERT INTO `pms_brands` VALUES (4, '三星', '/uploads/brands/samsung.png', '三星集团是韩国最大的跨国企业集团，三星电子是三星集团的旗舰子公司。', 'S', 4, 0, 1, '2025-03-16 13:15:47', '2025-03-25 16:37:49');
INSERT INTO `pms_brands` VALUES (5, '戴尔', '/uploads/brands/dell.png', '戴尔科技集团是一家总部位于美国德克萨斯州朗德罗克的美国跨国科技公司。', 'D', 5, 0, 1, '2025-03-16 13:15:47', '2025-03-16 13:15:47');
INSERT INTO `pms_brands` VALUES (6, '联想', '/uploads/brands/lenovo.png', '联想集团是一家跨国科技公司，总部位于中国北京和美国北卡罗来纳州。', 'L', 6, 0, 1, '2025-03-16 13:15:47', '2025-03-16 13:15:47');
INSERT INTO `pms_brands` VALUES (7, '索尼', '/uploads/brands/sony.png', '索尼是一家总部位于日本的跨国公司，提供消费电子、游戏、娱乐和金融服务。', 'S', 7, 1, 1, '2025-03-16 13:19:44', '2025-03-16 13:19:44');
INSERT INTO `pms_brands` VALUES (8, 'LG', '/uploads/brands/lg.png', 'LG电子是LG集团的一部分，专注于家电和移动通信设备的制造。', 'L', 8, 0, 1, '2025-03-16 13:19:44', '2025-03-16 13:19:44');
INSERT INTO `pms_brands` VALUES (9, '松下', '/uploads/brands/panasonic.png', '松下是一家日本的跨国电子制造商，专注于电子设备、家电和工业设备。', 'S', 9, 0, 1, '2025-03-16 13:19:44', '2025-03-16 13:19:44');
INSERT INTO `pms_brands` VALUES (10, '飞利浦', '/uploads/brands/philips.png', '飞利浦是一家荷兰的健康科技公司，专注于健康科技和家用电器。', 'F', 10, 1, 1, '2025-03-16 13:19:44', '2025-03-16 13:19:44');
INSERT INTO `pms_brands` VALUES (11, '索尼', '/uploads/brands/sony.png', '索尼是一家总部位于日本的跨国公司，提供消费电子、游戏、娱乐和金融服务。', 'S', 7, 1, 1, '2025-03-16 13:24:09', '2025-03-16 13:24:09');
INSERT INTO `pms_brands` VALUES (13, '松下', '/uploads/brands/panasonic.png', '松下是一家日本的跨国电子制造商，专注于电子设备、家电和工业设备。', 'S', 9, 0, 1, '2025-03-16 13:24:09', '2025-03-16 13:24:09');
INSERT INTO `pms_brands` VALUES (14, '飞利浦', '/uploads/brands/philips.png', '飞利浦是一家荷兰的健康科技公司，专注于健康科技和家用电器。', 'F', 10, 1, 1, '2025-03-16 13:24:09', '2025-03-16 13:24:09');

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
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品属性分类表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品属性值表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品属性表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pms_product_categories
-- ----------------------------
INSERT INTO `pms_product_categories` VALUES (1, 0, '默认分类', 1, NULL, 0, NULL, NULL, 1, NULL, 0, '2025-03-15 20:57:12', '2025-03-15 20:57:12');
INSERT INTO `pms_product_categories` VALUES (2, 0, '电子产品', 1, '/uploads/categories/electronics.png', 1, '各类电子产品', '电子,数码,智能', 1, 1, 1, '2025-03-16 13:15:47', '2025-03-18 16:00:55');
INSERT INTO `pms_product_categories` VALUES (3, 0, '家用电器', 1, '/uploads/categories/appliances.png', 2, '家用电器产品', '电器,家电,厨房电器', 1, 1, 1, '2025-03-16 13:15:47', '2025-03-18 16:00:57');
INSERT INTO `pms_product_categories` VALUES (4, 0, '服装鞋包', 1, '/uploads/categories/clothing.png', 3, '服装鞋包产品', '服装,鞋子,包包', 1, 1, 0, '2025-03-16 13:15:47', '2025-03-18 16:01:00');
INSERT INTO `pms_product_categories` VALUES (5, 1, '智能手机', 2, '/uploads/categories/smartphone.png', 1, '智能手机产品', '手机,智能手机,5G', 1, 1, 1, '2025-03-16 13:15:47', '2025-03-18 16:01:01');
INSERT INTO `pms_product_categories` VALUES (6, 1, '笔记本电脑', 2, '/uploads/categories/laptop.png', 2, '笔记本电脑产品', '笔记本,电脑,便携', 1, 1, 1, '2025-03-16 13:15:47', '2025-03-18 16:01:04');
INSERT INTO `pms_product_categories` VALUES (7, 1, '平板电脑', 2, '/uploads/categories/tablet.png', 3, '平板电脑产品', '平板,iPad,触屏', 1, 1, 0, '2025-03-16 13:15:47', '2025-03-18 16:01:06');
INSERT INTO `pms_product_categories` VALUES (8, 2, '冰箱', 2, '/uploads/categories/refrigerator.png', 1, '冰箱产品', '冰箱,冷藏,保鲜', 1, 1, 0, '2025-03-16 13:15:47', '2025-03-18 16:01:08');
INSERT INTO `pms_product_categories` VALUES (9, 2, '洗衣机', 2, '/uploads/categories/washing_machine.png', 2, '洗衣机产品', '洗衣机,洗护,滚筒', 1, 1, 0, '2025-03-16 13:15:47', '2025-03-18 16:01:10');
INSERT INTO `pms_product_categories` VALUES (10, 1, '智能穿戴', 2, '/uploads/categories/wearable.png', 4, '智能穿戴设备', '智能手表,手环,耳机', 1, 1, 1, '2025-03-16 13:19:44', '2025-03-18 16:01:11');
INSERT INTO `pms_product_categories` VALUES (11, 1, '智能家居', 2, '/uploads/categories/smarthome.png', 5, '智能家居产品', '智能音箱,智能灯泡,智能插座', 1, 1, 1, '2025-03-16 13:19:44', '2025-03-18 16:01:13');
INSERT INTO `pms_product_categories` VALUES (12, 1, '相机', 2, '/uploads/categories/camera.png', 6, '相机产品', '相机,单反,微单', 1, 1, 0, '2025-03-16 13:19:44', '2025-03-18 16:01:15');
INSERT INTO `pms_product_categories` VALUES (13, 2, '电视', 2, '/uploads/categories/tv.png', 3, '电视产品', '电视,智能电视,OLED', 1, 1, 1, '2025-03-16 13:19:44', '2025-03-18 16:01:16');
INSERT INTO `pms_product_categories` VALUES (14, 2, '空调', 2, '/uploads/categories/aircon.png', 4, '空调产品', '空调,变频,冷暖', 1, 1, 0, '2025-03-16 13:19:44', '2025-03-18 16:01:17');
INSERT INTO `pms_product_categories` VALUES (15, 1, '智能穿戴', 2, '/uploads/categories/wearable.png', 4, '智能穿戴设备', '智能手表,手环,耳机', 1, 1, 1, '2025-03-16 13:24:09', '2025-03-18 16:01:18');
INSERT INTO `pms_product_categories` VALUES (16, 1, '智能家居', 2, '/uploads/categories/smarthome.png', 5, '智能家居产品', '智能音箱,智能灯泡,智能插座', 1, 1, 1, '2025-03-16 13:24:09', '2025-03-18 16:01:19');
INSERT INTO `pms_product_categories` VALUES (17, 1, '相机', 2, '/uploads/categories/camera.png', 6, '相机产品', '相机,单反,微单', 1, 1, 0, '2025-03-16 13:24:09', '2025-03-18 16:01:20');
INSERT INTO `pms_product_categories` VALUES (18, 2, '电视', 2, '/uploads/categories/tv.png', 3, '电视产品', '电视,智能电视,OLED', 1, 1, 1, '2025-03-16 13:24:09', '2025-03-18 16:01:21');
INSERT INTO `pms_product_categories` VALUES (19, 2, '空调', 2, '/uploads/categories/aircon.png', 4, '空调产品', '空调,变频,冷暖', 1, 1, 0, '2025-03-16 13:24:09', '2025-03-18 16:01:24');

-- ----------------------------
-- Table structure for pms_product_comments
-- ----------------------------
DROP TABLE IF EXISTS `pms_product_comments`;
CREATE TABLE `pms_product_comments`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '评价ID',
  `product_id` bigint(0) NOT NULL COMMENT '商品ID',
  `sku_id` bigint(0) NULL DEFAULT NULL COMMENT 'SKU ID',
  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
  `order_id` bigint(0) NOT NULL COMMENT '订单ID',
  `order_item_id` bigint(0) NOT NULL COMMENT '订单项ID',
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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品评价表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品详情表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品图片表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品促销表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品服务保障表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品SKU表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pms_products
-- ----------------------------
INSERT INTO `pms_products` VALUES (3, '小米13', 'MI13-2023', 4, 0, 1, NULL, 3999.00, 4299.00, 'https://cdn.cnbj1.fds.api.mi-img.com/product-images/xiaomi-13kb7buy/11262.png?x-fds-process=image/resize,q_90,f_webp', NULL, NULL, '小米,手机,5G,骁龙8', '小米年度旗舰手机，搭载骁龙8第二代处理器', 1258, 1000, '台', 0.20, 1, 1, 1, 1, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, 0, '2025-03-16 13:15:47', '2025-03-25 16:41:25');
INSERT INTO `pms_products` VALUES (4, '华为Mate60 Pro', 'HW-MATE60P', 4, 0, 2, NULL, 6999.00, 7299.00, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221104/huawei_mate50_01.jpg', NULL, NULL, '华为,手机,旗舰,麒麟9000', '华为年度旗舰手机，搭载麒麟9000S处理器', 2056, 500, '台', 0.23, 2, 1, 1, 0, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, 0, '2025-03-16 13:15:47', '2025-03-25 16:40:12');
INSERT INTO `pms_products` VALUES (5, 'iPhone 15 Pro', 'APPL-IP15P', 4, 0, 3, NULL, 7999.00, 8299.00, 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20221028/iphone14_001.jpg', NULL, NULL, 'iPhone,苹果,A17,Pro', 'Apple新一代旗舰手机，搭载A17 Pro芯片', 3102, 800, '台', 0.22, 3, 1, 1, 1, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, 0, '2025-03-16 13:15:47', '2025-03-25 16:42:21');
INSERT INTO `pms_products` VALUES (6, '联想ThinkPad X1 Carbon', 'LN-X1C2023', 5, 0, 6, NULL, 9999.00, 12999.00, '/uploads/products/thinkpadx1.png', NULL, NULL, '联想,ThinkPad,笔记本,轻薄', '联想商务旗舰笔记本，轻薄坚固', 458, 200, '台', 1.20, 4, 1, 1, 1, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, 0, '2025-03-16 13:15:47', '2025-03-16 15:20:00');
INSERT INTO `pms_products` VALUES (7, '戴尔XPS 15', 'DELL-XPS15', 5, 0, 5, NULL, 12999.00, 13999.00, '/uploads/products/dellxps15.png', NULL, NULL, '戴尔,XPS,笔记本,设计', '戴尔高性能创意设计笔记本', 325, 150, '台', 1.80, 5, 1, 0, 1, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, 0, '2025-03-16 13:15:47', '2025-03-16 15:48:50');
INSERT INTO `pms_products` VALUES (8, '索尼WH-1000XM5无线降噪耳机', 'SONY-WH1000XM5', 12, 0, 7, NULL, 2699.00, 2999.00, '/uploads/products/wh1000xm5.png', NULL, NULL, '索尼,耳机,降噪,蓝牙', '索尼旗舰级无线降噪耳机，提供出色的降噪体验', 856, 300, '台', 0.25, 6, 1, 1, 1, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, 0, '2025-03-16 13:24:09', '2025-03-16 13:24:09');
INSERT INTO `pms_products` VALUES (9, '小米手环8 Pro', 'MI-BAND8PRO', 12, 0, 1, NULL, 399.00, 459.00, '/uploads/products/miband8pro.png', NULL, NULL, '小米,手环,健康,运动', '小米全新智能手环，支持运动健康监测', 1520, 1000, '个', 0.03, 7, 1, 1, 1, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, 0, '2025-03-16 13:24:09', '2025-03-16 13:24:09');
INSERT INTO `pms_products` VALUES (10, '华为智慧屏V75 Super', 'HW-TV-V75S', 15, 0, 2, NULL, 12999.00, 13999.00, '/uploads/products/huawei-v75s.png', NULL, NULL, '华为,电视,智慧屏,大屏', '华为75英寸旗舰智慧屏，搭载鸿蒙系统', 256, 100, '台', 30.00, 8, 1, 1, 1, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, 0, '2025-03-16 13:24:09', '2025-03-16 13:24:09');
INSERT INTO `pms_products` VALUES (11, '飞利浦Hue智能灯泡套装', 'PHILIPS-HUE-KIT', 13, 0, 10, NULL, 799.00, 899.00, '/uploads/products/philips-hue.png', NULL, NULL, '飞利浦,Hue,灯泡,智能家居', '飞利浦Hue智能照明系统，可通过手机APP控制', 423, 200, '套', 0.50, 9, 1, 1, 1, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, 0, '2025-03-16 13:24:09', '2025-03-16 13:24:09');
INSERT INTO `pms_products` VALUES (12, '索尼Alpha 7 IV全画幅微单相机', 'SONY-A7IV', 14, 0, 7, NULL, 14999.00, 15999.00, '/uploads/products/sony-a7iv.png', NULL, NULL, '索尼,相机,微单,全画幅', '索尼专业级全画幅微单相机，3300万像素', 189, 50, '台', 0.70, 10, 1, 1, 1, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, 0, '2025-03-16 13:24:09', '2025-03-16 13:24:09');
INSERT INTO `pms_products` VALUES (13, '小米15', '555', 5, 0, 1, 1, 999.00, 5444.00, 'https://cdn.cnbj1.fds.api.mi-img.com/product-images/xiaomi-13kb7buy/11262.png?x-fds-process=image/resize,q_90,f_webp', '', NULL, '', NULL, 0, 1000, '50', 50.00, 50, 0, 1, 1, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, 0, '2025-03-25 19:33:37', '2025-03-25 19:33:37');
INSERT INTO `pms_products` VALUES (14, '未命名商品_1742906182359', 'P1742906182359', 1, 0, NULL, 1, 0.01, NULL, NULL, NULL, NULL, NULL, NULL, 0, 100, NULL, NULL, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, NULL, NULL, 0, '2025-03-25 20:36:22', '2025-03-25 20:36:22');

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品评分表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '库存变更日志表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '优惠券适用分类关联表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '优惠券领取历史表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '优惠券适用商品关联表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '优惠券表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '限时秒杀活动表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '限时秒杀商品关联表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '限时秒杀场次表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '促销活动参与记录表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '促销活动商品关联表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '促销活动表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商家表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户通知表' ROW_FORMAT = Dynamic;

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
INSERT INTO `ums_user_addresses` VALUES (4, 12, '介江性', '19391629120', '陕西省', '汉中市', '汉台区', '陕西理工大学北校区', NULL, 1, NULL, 0, '2025-03-27 17:45:55', '2025-03-27 17:45:55');
INSERT INTO `ums_user_addresses` VALUES (6, 12, 'hcc', '1939162', '陕西省', '汉中市', '汉台区', '汉中市陕西理工大学北校区', NULL, 1, NULL, 0, '2025-03-27 17:48:41', '2025-03-27 17:48:41');

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户优惠券表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户收藏表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户登录日志表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户角色表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户搜索历史表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ums_user_search_history
-- ----------------------------
INSERT INTO `ums_user_search_history` VALUES (4, 12, '鞋子', 1, '2025-03-29 17:30:52', '2025-03-29 17:30:52');
INSERT INTO `ums_user_search_history` VALUES (5, 12, '风衣', 1, '2025-03-29 17:31:04', '2025-03-29 17:31:04');
INSERT INTO `ums_user_search_history` VALUES (6, 12, '冲锋衣', 1, '2025-03-29 17:31:14', '2025-03-29 17:31:14');

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
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ums_users
-- ----------------------------
INSERT INTO `ums_users` VALUES (12, '张三', '$2a$10$reSZltiq/qBlHZf3kzEyAeVuJo5iaI0GDjryct15VftfQwieYCTKK', '123464984166', NULL, NULL, NULL, 0, NULL, NULL, 1, NULL, 0, '2025-03-29 16:49:55', NULL, 0, 0, 0, 0, NULL, NULL, 0, '2025-03-18 17:42:20', '2025-03-18 17:50:26');

SET FOREIGN_KEY_CHECKS = 1;
