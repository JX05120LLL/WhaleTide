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

 Date: 10/04/2025 16:47:15
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户搜索历史表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ums_user_search_history
-- ----------------------------
INSERT INTO `ums_user_search_history` VALUES (19, 15, '华为 HUAWEI P20 ', 1, '2025-04-08 23:06:46', '2025-04-08 23:06:46');
INSERT INTO `ums_user_search_history` VALUES (20, 15, '小米智能空气净化器Pro 3', 1, '2025-04-08 23:06:52', '2025-04-08 23:06:52');
INSERT INTO `ums_user_search_history` VALUES (37, 12, '华为 HUAWEI P20 ', 1, '2025-04-10 13:07:19', '2025-04-10 13:07:19');
INSERT INTO `ums_user_search_history` VALUES (38, 12, 'Apple iPhone 14 (A2884) 128GB 支持移动联通电信5G 双卡双待手机', 1, '2025-04-10 13:07:29', '2025-04-10 13:07:29');

SET FOREIGN_KEY_CHECKS = 1;
