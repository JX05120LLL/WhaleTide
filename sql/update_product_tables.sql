/*
 更新商品相关表结构
 作者：WhaleTide
 日期：2025-03-25
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 更新商品表结构
-- ----------------------------
ALTER TABLE `pms_products`
    ADD COLUMN `album_pics` varchar(1000) DEFAULT NULL COMMENT '画册图片，连产品图片限制为5张，以逗号分割' AFTER `main_image`,
    ADD COLUMN `detail_title` varchar(255) DEFAULT NULL COMMENT '商品详情标题' AFTER `album_pics`,
    ADD COLUMN `detail_desc` text COMMENT '商品详情描述' AFTER `detail_title`,
    ADD COLUMN `detail_html` text COMMENT 'PC端商品详情' AFTER `detail_desc`,
    ADD COLUMN `detail_mobile_content` text COMMENT '移动端商品详情' AFTER `detail_html`,
    ADD COLUMN `promotion_type` tinyint(4) DEFAULT 0 COMMENT '促销类型：0->没有促销；1->单品促销；2->会员价；3->阶梯价格；4->满减价格' AFTER `detail_mobile_content`,
    ADD COLUMN `promotion_start_time` datetime DEFAULT NULL COMMENT '促销开始时间' AFTER `promotion_type`,
    ADD COLUMN `promotion_end_time` datetime DEFAULT NULL COMMENT '促销结束时间' AFTER `promotion_start_time`,
    ADD COLUMN `promotion_price` decimal(10,2) DEFAULT NULL COMMENT '促销价格' AFTER `promotion_end_time`,
    ADD COLUMN `promotion_per_limit` int(11) DEFAULT NULL COMMENT '活动限购数量' AFTER `promotion_price`,
    ADD COLUMN `new_status` tinyint(4) DEFAULT 0 COMMENT '新品状态:0->不是新品；1->新品' AFTER `publish_status`,
    ADD COLUMN `recommend_status` tinyint(4) DEFAULT 0 COMMENT '推荐状态；0->不推荐；1->推荐' AFTER `new_status`,
    ADD COLUMN `verify_status` tinyint(4) DEFAULT 0 COMMENT '审核状态：0->未审核；1->审核通过' AFTER `recommend_status`,
    ADD COLUMN `is_deleted` tinyint(4) DEFAULT 0 COMMENT '删除状态：0->未删除；1->已删除' AFTER `verify_status`,
    ADD COLUMN `sort` int(11) DEFAULT 0 COMMENT '排序' AFTER `is_deleted`,
    ADD COLUMN `service_guarantees` json NULL COMMENT '服务保障（JSON格式）' AFTER `promotion_limit`,
    ADD COLUMN `product_attributes` json NULL COMMENT '商品属性（JSON格式）' AFTER `service_guarantees`,
    ADD COLUMN `product_images` json NULL COMMENT '商品图片（JSON格式）' AFTER `product_attributes`,
    ADD COLUMN `product_videos` json NULL COMMENT '商品视频（JSON格式）' AFTER `product_images`,
    ADD COLUMN `product_details` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '商品详情' AFTER `product_videos`,
    ADD COLUMN `product_specs` json NULL COMMENT '商品规格（JSON格式）' AFTER `product_details`,
    ADD COLUMN `product_subjects` json NULL COMMENT '商品专题（JSON格式）' AFTER `product_specs`,
    ADD COLUMN `product_tags` json NULL COMMENT '商品标签（JSON格式）' AFTER `product_subjects`,
    ADD COLUMN `product_related` json NULL COMMENT '相关商品（JSON格式）' AFTER `product_tags`,
    ADD COLUMN `product_recommend` json NULL COMMENT '推荐商品（JSON格式）' AFTER `product_related`,
    ADD COLUMN `product_comment` json NULL COMMENT '商品评价（JSON格式）' AFTER `product_recommend`,
    ADD COLUMN `product_rating` decimal(2, 1) NULL DEFAULT 0.0 COMMENT '商品评分' AFTER `product_comment`,
    ADD COLUMN `product_rating_count` int(0) NULL DEFAULT 0 COMMENT '商品评分数量' AFTER `product_rating`,
    ADD COLUMN `product_comment_count` int(0) NULL DEFAULT 0 COMMENT '商品评价数量' AFTER `product_rating_count`,
    ADD COLUMN `product_view_count` int(0) NULL DEFAULT 0 COMMENT '商品浏览量' AFTER `product_comment_count`,
    ADD COLUMN `product_favorite_count` int(0) NULL DEFAULT 0 COMMENT '商品收藏量' AFTER `product_view_count`,
    ADD COLUMN `product_share_count` int(0) NULL DEFAULT 0 COMMENT '商品分享量' AFTER `product_favorite_count`,
    ADD COLUMN `product_sale_count` int(0) NULL DEFAULT 0 COMMENT '商品销量' AFTER `product_share_count`,
    ADD COLUMN `product_return_count` int(0) NULL DEFAULT 0 COMMENT '商品退货量' AFTER `product_sale_count`,
    ADD COLUMN `product_refund_count` int(0) NULL DEFAULT 0 COMMENT '商品退款量' AFTER `product_return_count`,
    ADD COLUMN `product_complaint_count` int(0) NULL DEFAULT 0 COMMENT '商品投诉量' AFTER `product_refund_count`,
    ADD COLUMN `product_audit_status` tinyint(0) NULL DEFAULT 0 COMMENT '商品审核状态：0-待审核，1-审核通过，2-审核不通过' AFTER `product_complaint_count`,
    ADD COLUMN `product_audit_time` datetime(0) NULL DEFAULT NULL COMMENT '商品审核时间' AFTER `product_audit_status`,
    ADD COLUMN `product_audit_user` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品审核人' AFTER `product_audit_time`,
    ADD COLUMN `product_audit_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品审核备注' AFTER `product_audit_user`,
    ADD COLUMN `product_status` tinyint(0) NULL DEFAULT 0 COMMENT '商品状态：0-下架，1-上架，2-删除' AFTER `product_audit_remark`,
    ADD COLUMN `product_status_time` datetime(0) NULL DEFAULT NULL COMMENT '商品状态变更时间' AFTER `product_status`,
    ADD COLUMN `product_status_user` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品状态变更人' AFTER `product_status_time`,
    ADD COLUMN `product_status_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品状态变更备注' AFTER `product_status_user`,
    ADD COLUMN `product_version` int(0) NULL DEFAULT 1 COMMENT '商品版本号' AFTER `product_status_remark`,
    ADD COLUMN `product_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品备注' AFTER `product_version`,
    ADD COLUMN `product_extra` json NULL COMMENT '商品扩展信息（JSON格式）' AFTER `product_remark`;

-- ----------------------------
-- 更新商品SKU表结构
-- ----------------------------
ALTER TABLE `pms_product_skus`
    ADD COLUMN `sku_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'SKU名称' AFTER `sku_code`,
    ADD COLUMN `sku_brief` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'SKU简介' AFTER `sku_name`,
    ADD COLUMN `sku_keywords` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'SKU关键词' AFTER `sku_brief`,
    ADD COLUMN `sku_images` json NULL COMMENT 'SKU图片（JSON格式）' AFTER `image`,
    ADD COLUMN `sku_videos` json NULL COMMENT 'SKU视频（JSON格式）' AFTER `sku_images`,
    ADD COLUMN `sku_details` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT 'SKU详情' AFTER `sku_videos`,
    ADD COLUMN `sku_attributes` json NULL COMMENT 'SKU属性（JSON格式）' AFTER `sku_details`,
    ADD COLUMN `sku_promotion_type` tinyint(0) NULL DEFAULT 0 COMMENT 'SKU促销类型：0-无促销，1-特价促销，2-会员价格，3-阶梯价格，4-满减价格' AFTER `sku_attributes`,
    ADD COLUMN `sku_promotion_price` decimal(10, 2) NULL DEFAULT NULL COMMENT 'SKU促销价格' AFTER `sku_promotion_type`,
    ADD COLUMN `sku_promotion_start_time` datetime(0) NULL DEFAULT NULL COMMENT 'SKU促销开始时间' AFTER `sku_promotion_price`,
    ADD COLUMN `sku_promotion_end_time` datetime(0) NULL DEFAULT NULL COMMENT 'SKU促销结束时间' AFTER `sku_promotion_start_time`,
    ADD COLUMN `sku_promotion_limit` int(0) NULL DEFAULT NULL COMMENT 'SKU促销限购数量' AFTER `sku_promotion_end_time`,
    ADD COLUMN `sku_service_guarantees` json NULL COMMENT 'SKU服务保障（JSON格式）' AFTER `sku_promotion_limit`,
    ADD COLUMN `sku_rating` decimal(2, 1) NULL DEFAULT 0.0 COMMENT 'SKU评分' AFTER `sku_service_guarantees`,
    ADD COLUMN `sku_rating_count` int(0) NULL DEFAULT 0 COMMENT 'SKU评分数量' AFTER `sku_rating`,
    ADD COLUMN `sku_comment_count` int(0) NULL DEFAULT 0 COMMENT 'SKU评价数量' AFTER `sku_rating_count`,
    ADD COLUMN `sku_view_count` int(0) NULL DEFAULT 0 COMMENT 'SKU浏览量' AFTER `sku_comment_count`,
    ADD COLUMN `sku_favorite_count` int(0) NULL DEFAULT 0 COMMENT 'SKU收藏量' AFTER `sku_view_count`,
    ADD COLUMN `sku_share_count` int(0) NULL DEFAULT 0 COMMENT 'SKU分享量' AFTER `sku_favorite_count`,
    ADD COLUMN `sku_sale_count` int(0) NULL DEFAULT 0 COMMENT 'SKU销量' AFTER `sku_share_count`,
    ADD COLUMN `sku_return_count` int(0) NULL DEFAULT 0 COMMENT 'SKU退货量' AFTER `sku_sale_count`,
    ADD COLUMN `sku_refund_count` int(0) NULL DEFAULT 0 COMMENT 'SKU退款量' AFTER `sku_return_count`,
    ADD COLUMN `sku_complaint_count` int(0) NULL DEFAULT 0 COMMENT 'SKU投诉量' AFTER `sku_refund_count`,
    ADD COLUMN `sku_audit_status` tinyint(0) NULL DEFAULT 0 COMMENT 'SKU审核状态：0-待审核，1-审核通过，2-审核不通过' AFTER `sku_complaint_count`,
    ADD COLUMN `sku_audit_time` datetime(0) NULL DEFAULT NULL COMMENT 'SKU审核时间' AFTER `sku_audit_status`,
    ADD COLUMN `sku_audit_user` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'SKU审核人' AFTER `sku_audit_time`,
    ADD COLUMN `sku_audit_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'SKU审核备注' AFTER `sku_audit_user`,
    ADD COLUMN `sku_status` tinyint(0) NULL DEFAULT 0 COMMENT 'SKU状态：0-下架，1-上架，2-删除' AFTER `sku_audit_remark`,
    ADD COLUMN `sku_status_time` datetime(0) NULL DEFAULT NULL COMMENT 'SKU状态变更时间' AFTER `sku_status`,
    ADD COLUMN `sku_status_user` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'SKU状态变更人' AFTER `sku_status_time`,
    ADD COLUMN `sku_status_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'SKU状态变更备注' AFTER `sku_status_user`,
    ADD COLUMN `sku_version` int(0) NULL DEFAULT 1 COMMENT 'SKU版本号' AFTER `sku_status_remark`,
    ADD COLUMN `sku_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'SKU备注' AFTER `sku_version`,
    ADD COLUMN `sku_extra` json NULL COMMENT 'SKU扩展信息（JSON格式）' AFTER `sku_remark`;

-- ----------------------------
-- 创建商品促销表
-- ----------------------------
DROP TABLE IF EXISTS `pms_product_promotions`;
CREATE TABLE `pms_product_promotions` (
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
-- 创建商品服务保障表
-- ----------------------------
DROP TABLE IF EXISTS `pms_product_service_guarantees`;
CREATE TABLE `pms_product_service_guarantees` (
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
-- 创建商品评价表
-- ----------------------------
DROP TABLE IF EXISTS `pms_product_comments`;
CREATE TABLE `pms_product_comments` (
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

SET FOREIGN_KEY_CHECKS = 1; 