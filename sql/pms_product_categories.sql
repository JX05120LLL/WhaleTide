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

 Date: 18/03/2025 16:02:15
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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

SET FOREIGN_KEY_CHECKS = 1;
