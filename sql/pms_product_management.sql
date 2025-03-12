/*
 优化后的商城系统数据库设计 - 产品管理系统(PMS)模块
 
 包含以下表:
 - pms_brands: 品牌表(原brand)
 - pms_products: 商品表(原product)
 - pms_product_categories: 商品分类表(原product_category)
 - pms_product_images: 商品图片表(新增)
 - pms_product_details: 商品详情表(新增)
 - pms_product_attributes: 商品属性表(新增)
 - pms_product_attribute_values: 商品属性值表(新增)
 - pms_product_skus: 商品SKU表(原product_sku)
 - pms_product_comments: 商品评论表(原product_comment)
 - pms_ratings: 商品评分表(新增)
 - pms_stock_logs: 库存日志表(原stock_log)
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for pms_brands
-- ----------------------------
DROP TABLE IF EXISTS `pms_brands`;
CREATE TABLE `pms_brands` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '品牌ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '品牌名称',
  `logo` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '品牌Logo',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '品牌描述',
  `first_letter` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '品牌首字母',
  `sort` int DEFAULT 0 COMMENT '排序',
  `is_featured` tinyint DEFAULT 0 COMMENT '是否推荐：0-否，1-是',
  `status` tinyint DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='品牌表';

-- ----------------------------
-- Table structure for pms_product_categories
-- ----------------------------
DROP TABLE IF EXISTS `pms_product_categories`;
CREATE TABLE `pms_product_categories` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `parent_id` bigint DEFAULT 0 COMMENT '父分类ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
  `level` tinyint DEFAULT 1 COMMENT '分类层级：1-一级分类，2-二级分类，3-三级分类',
  `icon` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '分类图标',
  `sort` int DEFAULT 0 COMMENT '排序',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '分类描述',
  `keywords` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '关键词',
  `status` tinyint DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `is_featured` tinyint DEFAULT 0 COMMENT '是否推荐：0-否，1-是',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_parent_id` (`parent_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品分类表';

-- ----------------------------
-- Table structure for pms_products
-- ----------------------------
DROP TABLE IF EXISTS `pms_products`;
CREATE TABLE `pms_products` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称',
  `product_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品编号',
  `category_id` bigint NOT NULL COMMENT '分类ID',
  `brand_id` bigint DEFAULT NULL COMMENT '品牌ID',
  `merchant_id` bigint NOT NULL COMMENT '商家ID',
  `price` decimal(10, 2) NOT NULL COMMENT '商品价格',
  `original_price` decimal(10, 2) DEFAULT NULL COMMENT '原价',
  `main_image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '主图URL',
  `keywords` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '关键词',
  `brief` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '简短描述',
  `sale` int DEFAULT 0 COMMENT '销量',
  `stock` int DEFAULT 0 COMMENT '库存',
  `unit` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '单位(件/kg等)',
  `weight` decimal(10, 2) DEFAULT NULL COMMENT '重量(kg)',
  `sort` int DEFAULT 0 COMMENT '排序',
  `publish_status` tinyint DEFAULT 0 COMMENT '上架状态：0-下架，1-上架',
  `new_status` tinyint DEFAULT 0 COMMENT '新品状态：0-非新品，1-新品',
  `recommend_status` tinyint DEFAULT 0 COMMENT '推荐状态：0-不推荐，1-推荐',
  `verify_status` tinyint DEFAULT 0 COMMENT '审核状态：0-未审核，1-审核通过，2-审核不通过',
  `is_deleted` tinyint DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_product_sn` (`product_sn`) USING BTREE,
  KEY `idx_category_id` (`category_id`) USING BTREE,
  KEY `idx_brand_id` (`brand_id`) USING BTREE,
  KEY `idx_merchant_id` (`merchant_id`) USING BTREE,
  CONSTRAINT `fk_product_brand` FOREIGN KEY (`brand_id`) REFERENCES `pms_brands` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_product_category` FOREIGN KEY (`category_id`) REFERENCES `pms_product_categories` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品信息表';

-- ----------------------------
-- Table structure for pms_product_details
-- ----------------------------
DROP TABLE IF EXISTS `pms_product_details`;
CREATE TABLE `pms_product_details` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '详情ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `description` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '商品详细描述',
  `specs` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '商品规格',
  `packing_list` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '包装清单',
  `after_service` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '售后服务',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_product_id` (`product_id`) USING BTREE,
  CONSTRAINT `fk_detail_product` FOREIGN KEY (`product_id`) REFERENCES `pms_products` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品详情表';

-- ----------------------------
-- Table structure for pms_product_images
-- ----------------------------
DROP TABLE IF EXISTS `pms_product_images`;
CREATE TABLE `pms_product_images` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '图片ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `image_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片URL',
  `sort` int DEFAULT 0 COMMENT '排序',
  `is_main` tinyint DEFAULT 0 COMMENT '是否主图：0-否，1-是',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_product_id` (`product_id`) USING BTREE,
  CONSTRAINT `fk_image_product` FOREIGN KEY (`product_id`) REFERENCES `pms_products` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品图片表';

-- ----------------------------
-- Table structure for pms_product_attributes
-- ----------------------------
DROP TABLE IF EXISTS `pms_product_attributes`;
CREATE TABLE `pms_product_attributes` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '属性ID',
  `category_id` bigint NOT NULL COMMENT '所属分类ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '属性名称',
  `attribute_type` tinyint DEFAULT 0 COMMENT '属性类型：0-规格，1-参数',
  `input_type` tinyint DEFAULT 0 COMMENT '录入方式：0-手工输入，1-从列表选择',
  `input_options` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '可选值列表，以逗号分隔',
  `sort` int DEFAULT 0 COMMENT '排序',
  `filter_type` tinyint DEFAULT 0 COMMENT '检索类型：0-不需要，1-需要',
  `search_type` tinyint DEFAULT 0 COMMENT '搜索类型：0-不需要，1-需要',
  `is_required` tinyint DEFAULT 0 COMMENT '是否必填：0-否，1-是',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_category_id` (`category_id`) USING BTREE,
  CONSTRAINT `fk_attribute_category` FOREIGN KEY (`category_id`) REFERENCES `pms_product_categories` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品属性表';

-- ----------------------------
-- Table structure for pms_product_attribute_values
-- ----------------------------
DROP TABLE IF EXISTS `pms_product_attribute_values`;
CREATE TABLE `pms_product_attribute_values` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '属性值ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `attribute_id` bigint NOT NULL COMMENT '属性ID',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '属性值',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_product_id` (`product_id`) USING BTREE,
  KEY `idx_attribute_id` (`attribute_id`) USING BTREE,
  CONSTRAINT `fk_attr_value_attribute` FOREIGN KEY (`attribute_id`) REFERENCES `pms_product_attributes` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_attr_value_product` FOREIGN KEY (`product_id`) REFERENCES `pms_products` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品属性值表';

-- ----------------------------
-- Table structure for pms_product_skus
-- ----------------------------
DROP TABLE IF EXISTS `pms_product_skus`;
CREATE TABLE `pms_product_skus` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'SKU ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `sku_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'SKU编码',
  `price` decimal(10, 2) NOT NULL COMMENT '价格',
  `stock` int DEFAULT 0 COMMENT '库存',
  `low_stock` int DEFAULT 0 COMMENT '预警库存',
  `image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'SKU图片',
  `specs` json COMMENT '规格属性（JSON格式）',
  `specs_text` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '规格文本，用于展示',
  `volume` decimal(10, 2) DEFAULT NULL COMMENT '体积(m³)',
  `weight` decimal(10, 2) DEFAULT NULL COMMENT '重量(kg)',
  `sale` int DEFAULT 0 COMMENT '销量',
  `lock_stock` int DEFAULT 0 COMMENT '锁定库存(下单但未付款)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_sku_code` (`sku_code`) USING BTREE,
  KEY `idx_product_id` (`product_id`) USING BTREE,
  CONSTRAINT `fk_sku_product` FOREIGN KEY (`product_id`) REFERENCES `pms_products` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品SKU表';

-- ----------------------------
-- Table structure for pms_product_comments
-- ----------------------------
DROP TABLE IF EXISTS `pms_product_comments`;
CREATE TABLE `pms_product_comments` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `order_id` bigint DEFAULT NULL COMMENT '订单ID',
  `order_item_id` bigint DEFAULT NULL COMMENT '订单商品ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '评论标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评论内容',
  `rating` tinyint DEFAULT 5 COMMENT '评分：1-5分',
  `images` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '评论图片，多个以逗号分隔',
  `is_anonymous` tinyint DEFAULT 0 COMMENT '是否匿名：0-否，1-是',
  `is_show` tinyint DEFAULT 1 COMMENT '是否显示：0-不显示，1-显示',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_product_id` (`product_id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE,
  KEY `idx_order_id` (`order_id`) USING BTREE,
  CONSTRAINT `fk_comment_product` FOREIGN KEY (`product_id`) REFERENCES `pms_products` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品评论表';

-- ----------------------------
-- Table structure for pms_ratings
-- ----------------------------
DROP TABLE IF EXISTS `pms_ratings`;
CREATE TABLE `pms_ratings` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '评分ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `rating` tinyint DEFAULT 5 COMMENT '评分：1-5分',
  `rating_type` tinyint DEFAULT 0 COMMENT '评分类型：0-综合，1-服务，2-质量，3-物流',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '评分时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_user_product_type` (`user_id`,`product_id`,`rating_type`) USING BTREE,
  KEY `idx_product_id` (`product_id`) USING BTREE,
  CONSTRAINT `fk_rating_product` FOREIGN KEY (`product_id`) REFERENCES `pms_products` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品评分表';

-- ----------------------------
-- Table structure for pms_stock_logs
-- ----------------------------
DROP TABLE IF EXISTS `pms_stock_logs`;
CREATE TABLE `pms_stock_logs` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `sku_id` bigint DEFAULT NULL COMMENT 'SKU ID',
  `change_type` tinyint NOT NULL COMMENT '变更类型：1-销售扣减，2-退货增加，3-后台调整',
  `change_amount` int NOT NULL COMMENT '变更数量',
  `before_stock` int DEFAULT NULL COMMENT '变更前库存',
  `after_stock` int DEFAULT NULL COMMENT '变更后库存',
  `order_id` bigint DEFAULT NULL COMMENT '关联订单ID',
  `order_item_id` bigint DEFAULT NULL COMMENT '关联订单项ID',
  `operator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '操作人',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_product_id` (`product_id`) USING BTREE,
  KEY `idx_sku_id` (`sku_id`) USING BTREE,
  KEY `idx_order_id` (`order_id`) USING BTREE,
  CONSTRAINT `fk_stock_log_product` FOREIGN KEY (`product_id`) REFERENCES `pms_products` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_stock_log_sku` FOREIGN KEY (`sku_id`) REFERENCES `pms_product_skus` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='库存变更日志表';

SET FOREIGN_KEY_CHECKS = 1; 