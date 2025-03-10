### whaleTide  鲸浪商城

目前现有表包括，详细情况可查看mall-tiny 原开源项目，这是mall-tiny地址

[mall-tiny]: https://github.com/macrozheng/mall-tiny

<img src="C:\Users\20526\AppData\Roaming\Typora\typora-user-images\image-20250310105330991.png" alt="image-20250310105330991" style="zoom:50%;" />

whaleTide是一款基于开源项目 mall-tiny进行二次开发的商城系统，一下部分均为原创

#### 一、数据库设计

##### 1.用户相关

###### 1.1 user用户表

```sql
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(64) NOT NULL COMMENT '用户名',
  `password` varchar(64) NOT NULL COMMENT '密码',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `nickname` varchar(200) DEFAULT NULL COMMENT '昵称',
  `gender` tinyint DEFAULT 0 COMMENT '性别：0-未知，1-男，2-女',
  `avatar` varchar(500) DEFAULT NULL COMMENT '头像',
  `status` tinyint DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime DEFAULT NULL COMMENT '注册时间',
  PRIMARY KEY (`id`)
) COMMENT='普通用户表';
```

###### 1.2 user_address 用户地址表

```sql
CREATE TABLE `user_address` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `name` varchar(64) NOT NULL COMMENT '收货人',
  `phone` varchar(20) NOT NULL COMMENT '联系电话',
  `province` varchar(50) DEFAULT NULL COMMENT '省',
  `city` varchar(50) DEFAULT NULL COMMENT '市',
  `district` varchar(50) DEFAULT NULL COMMENT '区',
  `detail_address` varchar(255) NOT NULL COMMENT '详细地址',
  `is_default` tinyint DEFAULT 0 COMMENT '是否默认地址：0-否，1-是',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) COMMENT='用户收货地址表';
```

##### 2.商品相关

###### 2.1product 商品表

```sql
CREATE TABLE `product` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL COMMENT '商品名称',
  `category_id` bigint NOT NULL COMMENT '分类ID',
  `brand_id` bigint DEFAULT NULL COMMENT '品牌ID',
  `price` decimal(10,2) NOT NULL COMMENT '商品价格',
  `stock` int DEFAULT 0 COMMENT '库存',
  `sales` int DEFAULT 0 COMMENT '销量',
  `description` text COMMENT '商品描述',
  `status` tinyint DEFAULT 1 COMMENT '状态：0-下架，1-上架',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_brand_id` (`brand_id`)
) COMMENT='商品信息表';
```

###### 2.2`product_category` 商品分类表

```sql
CREATE TABLE `product_sku` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `sku_code` varchar(50) NOT NULL COMMENT 'SKU编码',
  `price` decimal(10,2) NOT NULL COMMENT '价格',
  `stock` int DEFAULT 0 COMMENT '库存',
  `specs` json DEFAULT NULL COMMENT '规格属性（JSON格式）',
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`)
) COMMENT='商品SKU表';
```

###### 2.3`product_sku 商品SKU表

```sql
CREATE TABLE `product_sku` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `sku_code` varchar(50) NOT NULL COMMENT 'SKU编码',
  `price` decimal(10,2) NOT NULL COMMENT '价格',
  `stock` int DEFAULT 0 COMMENT '库存',
  `specs` json DEFAULT NULL COMMENT '规格属性（JSON格式）',
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`)
) COMMENT='商品SKU表';
```

##### 3.订单相关

###### 3.1  order 订单表

```sql
CREATE TABLE `order` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_sn` varchar(64) NOT NULL COMMENT '订单号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `total_amount` decimal(10,2) NOT NULL COMMENT '订单总金额',
  `status` tinyint DEFAULT 0 COMMENT '订单状态：0-待付款，1-待发货，2-已发货，3-已完成，4-已关闭',
  `pay_type` tinyint DEFAULT 0 COMMENT '支付方式：0-未支付，1-支付宝，2-微信',
  `receiver_address` varchar(255) NOT NULL COMMENT '收货地址',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `payment_time` datetime DEFAULT NULL COMMENT '支付时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_order_sn` (`order_sn`),
  KEY `idx_user_id` (`user_id`)
) COMMENT='订单主表';
```

###### 3.2 order_item 订单商品项表

```sql
CREATE TABLE `order_item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `sku_id` bigint NOT NULL COMMENT 'SKU ID',
  `quantity` int NOT NULL COMMENT '购买数量',
  `price` decimal(10,2) NOT NULL COMMENT '商品单价',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_product_id` (`product_id`)
) COMMENT='订单商品项表';
```

##### 4.其他辅助表

###### 4.1 cart 购物车表

```sql
CREATE TABLE `cart` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `sku_id` bigint NOT NULL COMMENT 'SKU ID',
  `quantity` int DEFAULT 1 COMMENT '数量',
  `selected` tinyint DEFAULT 1 COMMENT '是否选中：0-否，1-是',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) COMMENT='用户购物车表';
```

###### 4.2 brand 品牌表

```sql
CREATE TABLE `brand` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '品牌名称',
  `logo` varchar(500) DEFAULT NULL COMMENT '品牌Logo',
  `description` text COMMENT '品牌描述',
  PRIMARY KEY (`id`)
) COMMENT='品牌表';
```

------



[^]: **外键关联**： 在订单、商品等表中，通过外键（如 `user_id`、`product_id`）与用户表和商品表关联，确保数据一致性。
[^]: **状态字段**： 使用 `status` 字段标记订单、商品等的状态，便于业务逻辑处理。
[^]: **JSON字段**： 在 `product_sku` 表中，使用 `specs` 字段（JSON类型）存储商品规格，灵活支持多种属性组合。
[^]: **扩展性**： 可根据需求添加更多表（如优惠券、评论、物流信息等），或扩展现有表的字段（如订单增加运费字段）。



#### 二、任务需求







