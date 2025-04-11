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

 Date: 10/04/2025 16:51:20
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '会员商品浏览历史记录' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
