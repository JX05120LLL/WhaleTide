/*
 优化后的商城系统数据库设计 - 权限管理系统(AMS)模块
 
 包含以下表:
 - ams_admins: 管理员表(原ums_admin)
 - ams_roles: 角色表(原ums_role)
 - ams_permissions: 权限表(新增)
 - ams_menus: 菜单表(原ums_menu)
 - ams_resources: 资源表(原ums_resource)
 - ams_resource_categories: 资源分类表(原ums_resource_category)
 - ams_admin_role_relations: 管理员与角色关系表(原ums_admin_role_relation)
 - ams_role_menu_relations: 角色与菜单关系表(原ums_role_menu_relation)
 - ams_role_resource_relations: 角色与资源关系表(原ums_role_resource_relation)
 - ams_admin_login_logs: 管理员登录日志表(原ums_admin_login_log)
 - ams_admin_operation_logs: 管理员操作日志表(新增)
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ams_admins
-- ----------------------------
DROP TABLE IF EXISTS `ams_admins`;
CREATE TABLE `ams_admins` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `real_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '真实姓名',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '手机号',
  `avatar` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '头像',
  `gender` tinyint DEFAULT 0 COMMENT '性别：0-未知，1-男，2-女',
  `status` tinyint DEFAULT 1 COMMENT '帐号启用状态：0-禁用，1-启用',
  `login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `login_ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '最后登录IP',
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注信息',
  `super_admin` tinyint DEFAULT 0 COMMENT '是否超级管理员：0-否，1-是',
  `department` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '部门',
  `position` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '职位',
  `is_deleted` tinyint DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_username` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='管理员表';

-- ----------------------------
-- Records of ams_admins
-- ----------------------------
INSERT INTO `ams_admins` VALUES (1, 'admin', '$2a$10$NZ5o7r2E.ayT2ZoxgjlI.eJ6OEYqjH7INR/F.mXDbjZJi9HF0YCVG', '超级管理员', 'admin@whaletide.com', '13888888888', NULL, 1, 1, NULL, NULL, '超级管理员', 1, '技术部', 'CTO', 0, '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_admins` VALUES (2, 'productAdmin', '$2a$10$NZ5o7r2E.ayT2ZoxgjlI.eJ6OEYqjH7INR/F.mXDbjZJi9HF0YCVG', '商品管理员', 'product@whaletide.com', '13866666666', NULL, 1, 1, NULL, NULL, '商品管理员', 0, '商品部', '主管', 0, '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_admins` VALUES (3, 'orderAdmin', '$2a$10$NZ5o7r2E.ayT2ZoxgjlI.eJ6OEYqjH7INR/F.mXDbjZJi9HF0YCVG', '订单管理员', 'order@whaletide.com', '13855555555', NULL, 1, 1, NULL, NULL, '订单管理员', 0, '运营部', '主管', 0, '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_admins` VALUES (4, 'marketingAdmin', '$2a$10$NZ5o7r2E.ayT2ZoxgjlI.eJ6OEYqjH7INR/F.mXDbjZJi9HF0YCVG', '营销管理员', 'marketing@whaletide.com', '13844444444', NULL, 2, 1, NULL, NULL, '营销管理员', 0, '营销部', '主管', 0, '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_admins` VALUES (5, 'authAdmin', '$2a$10$NZ5o7r2E.ayT2ZoxgjlI.eJ6OEYqjH7INR/F.mXDbjZJi9HF0YCVG', '权限管理员', 'auth@whaletide.com', '13833333333', NULL, 1, 1, NULL, NULL, '权限管理员', 0, '人事部', '主管', 0, '2023-01-01 00:00:00', '2023-01-01 00:00:00');

-- ----------------------------
-- Table structure for ams_roles
-- ----------------------------
DROP TABLE IF EXISTS `ams_roles`;
CREATE TABLE `ams_roles` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色编码',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '角色描述',
  `sort` int DEFAULT 0 COMMENT '排序',
  `status` tinyint DEFAULT 1 COMMENT '启用状态：0-禁用，1-启用',
  `is_deleted` tinyint DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_code` (`code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色表';

-- ----------------------------
-- Records of ams_roles
-- ----------------------------
INSERT INTO `ams_roles` VALUES (1, '超级管理员', 'SUPER_ADMIN', '超级管理员，拥有所有权限', 0, 1, 0, '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_roles` VALUES (2, '商品管理员', 'PRODUCT_ADMIN', '商品管理员，负责商品管理相关操作', 1, 1, 0, '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_roles` VALUES (3, '订单管理员', 'ORDER_ADMIN', '订单管理员，负责订单管理相关操作', 2, 1, 0, '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_roles` VALUES (4, '营销管理员', 'MARKETING_ADMIN', '营销管理员，负责营销活动管理相关操作', 3, 1, 0, '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_roles` VALUES (5, '权限管理员', 'AUTH_ADMIN', '权限管理员，负责权限管理相关操作', 4, 1, 0, '2023-01-01 00:00:00', '2023-01-01 00:00:00');

-- ----------------------------
-- Table structure for ams_permissions
-- ----------------------------
DROP TABLE IF EXISTS `ams_permissions`;
CREATE TABLE `ams_permissions` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '权限ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限名称',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限编码',
  `type` tinyint NOT NULL COMMENT '权限类型：0-目录，1-菜单，2-按钮，3-接口',
  `parent_id` bigint DEFAULT 0 COMMENT '父级权限ID',
  `path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '路径',
  `component` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '组件',
  `redirect` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '重定向',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '图标',
  `sort` int DEFAULT 0 COMMENT '排序',
  `hidden` tinyint DEFAULT 0 COMMENT '是否隐藏：0-否，1-是',
  `status` tinyint DEFAULT 1 COMMENT '启用状态：0-禁用，1-启用',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '权限描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_code` (`code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='权限表';

-- ----------------------------
-- Table structure for ams_menus
-- ----------------------------
DROP TABLE IF EXISTS `ams_menus`;
CREATE TABLE `ams_menus` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `parent_id` bigint DEFAULT 0 COMMENT '父级ID',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名称',
  `level` int DEFAULT 0 COMMENT '菜单级别',
  `sort` int DEFAULT 0 COMMENT '菜单排序',
  `icon` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '前端图标',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '前端路由路径',
  `component` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '前端组件',
  `redirect` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '重定向路径',
  `hidden` tinyint DEFAULT 0 COMMENT '是否隐藏：0-否，1-是',
  `status` tinyint DEFAULT 1 COMMENT '菜单状态：0-禁用，1-启用',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '菜单描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_parent_id` (`parent_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单表';

-- ----------------------------
-- Records of ams_menus
-- ----------------------------
INSERT INTO `ams_menus` VALUES (1, 0, '系统管理', 0, 0, 'setting', '/system', 'Layout', '/system/users', 0, 1, '系统管理目录', '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_menus` VALUES (2, 1, '用户管理', 1, 1, 'user', '/system/users', 'system/users/index', NULL, 0, 1, '用户管理菜单', '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_menus` VALUES (3, 1, '角色管理', 1, 2, 'role', '/system/roles', 'system/roles/index', NULL, 0, 1, '角色管理菜单', '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_menus` VALUES (4, 1, '菜单管理', 1, 3, 'menu', '/system/menus', 'system/menus/index', NULL, 0, 1, '菜单管理菜单', '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_menus` VALUES (5, 1, '资源管理', 1, 4, 'resource', '/system/resources', 'system/resources/index', NULL, 0, 1, '资源管理菜单', '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_menus` VALUES (6, 0, '商品管理', 0, 1, 'product', '/product', 'Layout', '/product/list', 0, 1, '商品管理目录', '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_menus` VALUES (7, 6, '商品列表', 1, 1, 'list', '/product/list', 'product/list/index', NULL, 0, 1, '商品列表菜单', '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_menus` VALUES (8, 6, '商品分类', 1, 2, 'category', '/product/categories', 'product/categories/index', NULL, 0, 1, '商品分类菜单', '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_menus` VALUES (9, 6, '品牌管理', 1, 3, 'brand', '/product/brands', 'product/brands/index', NULL, 0, 1, '品牌管理菜单', '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_menus` VALUES (10, 0, '订单管理', 0, 2, 'order', '/order', 'Layout', '/order/list', 0, 1, '订单管理目录', '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_menus` VALUES (11, 10, '订单列表', 1, 1, 'list', '/order/list', 'order/list/index', NULL, 0, 1, '订单列表菜单', '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_menus` VALUES (12, 10, '退货管理', 1, 2, 'return', '/order/returns', 'order/returns/index', NULL, 0, 1, '退货管理菜单', '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_menus` VALUES (13, 0, '营销管理', 0, 3, 'marketing', '/marketing', 'Layout', '/marketing/coupons', 0, 1, '营销管理目录', '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_menus` VALUES (14, 13, '优惠券管理', 1, 1, 'coupon', '/marketing/coupons', 'marketing/coupons/index', NULL, 0, 1, '优惠券管理菜单', '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_menus` VALUES (15, 13, '秒杀活动', 1, 2, 'flash', '/marketing/flash', 'marketing/flash/index', NULL, 0, 1, '秒杀活动菜单', '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_menus` VALUES (16, 13, '促销活动', 1, 3, 'promotion', '/marketing/promotions', 'marketing/promotions/index', NULL, 0, 1, '促销活动菜单', '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_menus` VALUES (17, 0, '用户管理', 0, 4, 'member', '/member', 'Layout', '/member/list', 0, 1, '用户管理目录', '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_menus` VALUES (18, 17, '用户列表', 1, 1, 'list', '/member/list', 'member/list/index', NULL, 0, 1, '用户列表菜单', '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_menus` VALUES (19, 17, '商家管理', 1, 2, 'merchant', '/member/merchants', 'member/merchants/index', NULL, 0, 1, '商家管理菜单', '2023-01-01 00:00:00', '2023-01-01 00:00:00');

-- ----------------------------
-- Table structure for ams_resources
-- ----------------------------
DROP TABLE IF EXISTS `ams_resources`;
CREATE TABLE `ams_resources` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '资源ID',
  `category_id` bigint DEFAULT NULL COMMENT '资源分类ID',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '资源名称',
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '资源URL',
  `method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'HTTP请求方法',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '资源描述',
  `status` tinyint DEFAULT 1 COMMENT '启用状态：0-禁用，1-启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_category_id` (`category_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='资源表';

-- ----------------------------
-- Records of ams_resources
-- ----------------------------
INSERT INTO `ams_resources` VALUES (1, 1, '获取管理员列表', '/api/admin/admins', 'GET', '获取管理员列表接口', 1, '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_resources` VALUES (2, 1, '创建管理员', '/api/admin/admins', 'POST', '创建管理员接口', 1, '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_resources` VALUES (3, 1, '更新管理员', '/api/admin/admins/{id}', 'PUT', '更新管理员接口', 1, '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_resources` VALUES (4, 1, '删除管理员', '/api/admin/admins/{id}', 'DELETE', '删除管理员接口', 1, '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_resources` VALUES (5, 2, '获取角色列表', '/api/admin/roles', 'GET', '获取角色列表接口', 1, '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_resources` VALUES (6, 2, '创建角色', '/api/admin/roles', 'POST', '创建角色接口', 1, '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_resources` VALUES (7, 2, '更新角色', '/api/admin/roles/{id}', 'PUT', '更新角色接口', 1, '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_resources` VALUES (8, 2, '删除角色', '/api/admin/roles/{id}', 'DELETE', '删除角色接口', 1, '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_resources` VALUES (9, 3, '获取菜单列表', '/api/admin/menus', 'GET', '获取菜单列表接口', 1, '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_resources` VALUES (10, 3, '创建菜单', '/api/admin/menus', 'POST', '创建菜单接口', 1, '2023-01-01 00:00:00', '2023-01-01 00:00:00');

-- ----------------------------
-- Table structure for ams_resource_categories
-- ----------------------------
DROP TABLE IF EXISTS `ams_resource_categories`;
CREATE TABLE `ams_resource_categories` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
  `sort` int DEFAULT 0 COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='资源分类表';

-- ----------------------------
-- Records of ams_resource_categories
-- ----------------------------
INSERT INTO `ams_resource_categories` VALUES (1, '管理员模块', 0, '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_resource_categories` VALUES (2, '角色模块', 1, '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_resource_categories` VALUES (3, '菜单模块', 2, '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_resource_categories` VALUES (4, '资源模块', 3, '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_resource_categories` VALUES (5, '商品模块', 4, '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_resource_categories` VALUES (6, '订单模块', 5, '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_resource_categories` VALUES (7, '营销模块', 6, '2023-01-01 00:00:00', '2023-01-01 00:00:00');
INSERT INTO `ams_resource_categories` VALUES (8, '用户模块', 7, '2023-01-01 00:00:00', '2023-01-01 00:00:00');

-- ----------------------------
-- Table structure for ams_admin_role_relations
-- ----------------------------
DROP TABLE IF EXISTS `ams_admin_role_relations`;
CREATE TABLE `ams_admin_role_relations` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `admin_id` bigint NOT NULL COMMENT '管理员ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_admin_id` (`admin_id`) USING BTREE,
  KEY `idx_role_id` (`role_id`) USING BTREE,
  CONSTRAINT `fk_relation_admin` FOREIGN KEY (`admin_id`) REFERENCES `ams_admins` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_relation_role` FOREIGN KEY (`role_id`) REFERENCES `ams_roles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='管理员与角色关系表';

-- ----------------------------
-- Records of ams_admin_role_relations
-- ----------------------------
INSERT INTO `ams_admin_role_relations` VALUES (1, 1, 1, '2023-01-01 00:00:00');
INSERT INTO `ams_admin_role_relations` VALUES (2, 2, 2, '2023-01-01 00:00:00');
INSERT INTO `ams_admin_role_relations` VALUES (3, 3, 3, '2023-01-01 00:00:00');
INSERT INTO `ams_admin_role_relations` VALUES (4, 4, 4, '2023-01-01 00:00:00');
INSERT INTO `ams_admin_role_relations` VALUES (5, 5, 5, '2023-01-01 00:00:00');

-- ----------------------------
-- Table structure for ams_role_menu_relations
-- ----------------------------
DROP TABLE IF EXISTS `ams_role_menu_relations`;
CREATE TABLE `ams_role_menu_relations` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_role_id` (`role_id`) USING BTREE,
  KEY `idx_menu_id` (`menu_id`) USING BTREE,
  CONSTRAINT `fk_menu_relation_menu` FOREIGN KEY (`menu_id`) REFERENCES `ams_menus` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_menu_relation_role` FOREIGN KEY (`role_id`) REFERENCES `ams_roles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色与菜单关系表';

-- ----------------------------
-- Table structure for ams_role_resource_relations
-- ----------------------------
DROP TABLE IF EXISTS `ams_role_resource_relations`;
CREATE TABLE `ams_role_resource_relations` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `resource_id` bigint NOT NULL COMMENT '资源ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_role_id` (`role_id`) USING BTREE,
  KEY `idx_resource_id` (`resource_id`) USING BTREE,
  CONSTRAINT `fk_resource_relation_resource` FOREIGN KEY (`resource_id`) REFERENCES `ams_resources` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_resource_relation_role` FOREIGN KEY (`role_id`) REFERENCES `ams_roles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色与资源关系表';

-- ----------------------------
-- Table structure for ams_admin_login_logs
-- ----------------------------
DROP TABLE IF EXISTS `ams_admin_login_logs`;
CREATE TABLE `ams_admin_login_logs` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `admin_id` bigint DEFAULT NULL COMMENT '管理员ID',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '管理员账号',
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '登录IP',
  `user_agent` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '浏览器类型',
  `status` tinyint DEFAULT 0 COMMENT '登录状态：0-失败，1-成功',
  `message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '提示消息',
  `login_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_admin_id` (`admin_id`) USING BTREE,
  CONSTRAINT `fk_login_log_admin` FOREIGN KEY (`admin_id`) REFERENCES `ams_admins` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='管理员登录日志表';

-- ----------------------------
-- Table structure for ams_admin_operation_logs
-- ----------------------------
DROP TABLE IF EXISTS `ams_admin_operation_logs`;
CREATE TABLE `ams_admin_operation_logs` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `admin_id` bigint DEFAULT NULL COMMENT '管理员ID',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '管理员账号',
  `module` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '操作模块',
  `method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '请求方法',
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '请求URL',
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '操作IP',
  `user_agent` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '浏览器类型',
  `request_param` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '请求参数',
  `response_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '响应数据',
  `status` tinyint DEFAULT 0 COMMENT '操作状态：0-失败，1-成功',
  `error_message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '错误消息',
  `operation_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_admin_id` (`admin_id`) USING BTREE,
  CONSTRAINT `fk_operation_log_admin` FOREIGN KEY (`admin_id`) REFERENCES `ams_admins` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='管理员操作日志表';

SET FOREIGN_KEY_CHECKS = 1; 