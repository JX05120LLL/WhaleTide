### whaleTide  鲸浪商城



目标要求：

**单商家多用户模式商城**

目前现有表包括，详细情况可查看mall-tiny 原开源项目，这是mall-tiny地址

[mall-tiny]: https://github.com/macrozheng/mall-tiny

<img src="C:\Users\20526\AppData\Roaming\Typora\typora-user-images\image-20250310105330991.png" alt="image-20250310105330991" style="zoom:50%;" />

whaleTide是一款基于开源项目 mall-tiny进行二次开发的商城系统，一下部分均为原创

#### 一、数据库设计

##### 1.用户相关

###### 1.1 user用户表

```sql
-- 用户表
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
  `is_merchant` tinyint DEFAULT 0 COMMENT '是否为商家：0-否，1-是',
  `merchant_info` json DEFAULT NULL COMMENT '商家信息（JSON格式）',
  PRIMARY KEY (`id`)
) COMMENT='用户表';
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

###### 1.3   user_coupon 用户优惠券表

```sql
CREATE TABLE `user_coupon` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `coupon_id` bigint NOT NULL COMMENT '优惠券ID',
  `status` tinyint DEFAULT 0 COMMENT '状态：0-未使用，1-已使用，2-已过期',
  `acquire_time` datetime DEFAULT NULL COMMENT '领取时间',
  `use_time` datetime DEFAULT NULL COMMENT '使用时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) COMMENT='用户优惠券领取记录表';
```

##### 2.商品相关

###### 2.1product 商品表

```sql
-- 商品表
CREATE TABLE `product` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL COMMENT '商品名称',
  `category_id` bigint NOT NULL COMMENT '分类ID',
  `brand_id` bigint DEFAULT NULL COMMENT '品牌ID',
  `merchant_id` bigint NOT NULL COMMENT '商家ID（关联user.id）',
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
CREATE TABLE `product_category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `parent_id` bigint DEFAULT 0 COMMENT '父分类ID',
  `name` varchar(100) NOT NULL COMMENT '分类名称',
  `level` tinyint DEFAULT 1 COMMENT '分类层级',
  `sort` int DEFAULT 0 COMMENT '排序',
  PRIMARY KEY (`id`)
) COMMENT='商品分类表';
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

###### 2.4 product_comment 商品评论表

```sql
CREATE TABLE `product_comment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `content` text NOT NULL COMMENT '评论内容',
  `rating` tinyint DEFAULT 5 COMMENT '评分：1-5分',
  `create_time` datetime DEFAULT NULL COMMENT '评论时间',
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`)
) COMMENT='商品评论表';
```

##### 3.订单相关

###### 3.1  order 订单表

```sql
-- 订单表（注意 ORDER 是保留字，需用反引号包裹）
CREATE TABLE `order` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_sn` varchar(64) NOT NULL COMMENT '订单号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `merchant_id` bigint NOT NULL COMMENT '商家ID（关联user.id）',
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

###### 3.3  order_return_apply   退货申请表

```sql
CREATE TABLE `order_return_apply` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `reason` varchar(500) NOT NULL COMMENT '退货原因',
  `status` tinyint DEFAULT 0 COMMENT '处理状态：0-待处理，1-已同意，2-已拒绝',
  `handle_note` varchar(500) DEFAULT NULL COMMENT '处理备注',
  `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
  `create_time` datetime DEFAULT NULL COMMENT '申请时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`)
) COMMENT='订单退货申请表';
```

###### 3.4 order_operation_log 订单操作日志表

```sql
CREATE TABLE `order_operation_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `operator` varchar(64) DEFAULT NULL COMMENT '操作人（用户或管理员）',
  `action` varchar(200) NOT NULL COMMENT '操作内容（如修改地址、取消订单）',
  `note` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`)
) COMMENT='订单操作日志表';
```



##### 4.物流支付相关

###### 4.1 order_delivery 物流信息表

```sql
CREATE TABLE `order_delivery` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `delivery_sn` varchar(64) NOT NULL COMMENT '物流单号',
  `delivery_company` varchar(100) DEFAULT NULL COMMENT '物流公司',
  `receiver_name` varchar(64) NOT NULL COMMENT '收货人',
  `receiver_phone` varchar(20) NOT NULL COMMENT '收货电话',
  `delivery_status` tinyint DEFAULT 0 COMMENT '物流状态：0-未发货，1-运输中，2-已签收',
  `delivery_time` datetime DEFAULT NULL COMMENT '发货时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`)
) COMMENT='订单物流信息表';
```

###### 4.2   payment_record 支付记录表

```sql
CREATE TABLE `payment_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `payment_sn` varchar(64) NOT NULL COMMENT '支付流水号',
  `amount` decimal(10,2) NOT NULL COMMENT '支付金额',
  `pay_type` tinyint DEFAULT 1 COMMENT '支付方式：1-支付宝，2-微信',
  `status` tinyint DEFAULT 0 COMMENT '状态：0-未支付，1-支付成功，2-支付失败',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_payment_sn` (`payment_sn`),
  KEY `idx_order_id` (`order_id`)
) COMMENT='支付记录表';
```

##### 5.库存相关

###### 5.1 stock_log  库存更新日志表

```sql
CREATE TABLE `stock_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `sku_id` bigint DEFAULT NULL COMMENT 'SKU ID',
  `change_type` tinyint NOT NULL COMMENT '变更类型：1-销售扣减，2-退货增加，3-手动调整',
  `change_amount` int NOT NULL COMMENT '变更数量',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `operator` varchar(64) DEFAULT NULL COMMENT '操作人',
  `create_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`)
) COMMENT='库存变更日志表';
```

##### 6.其他辅助表

###### 6.1 cart 购物车表

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

###### 6.2 brand 品牌表

```sql
CREATE TABLE `brand` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '品牌名称',
  `logo` varchar(500) DEFAULT NULL COMMENT '品牌Logo',
  `description` text COMMENT '品牌描述',
  PRIMARY KEY (`id`)
) COMMENT='品牌表';
```

###### 6.3  coupon 优惠券表

```sql
CREATE TABLE `coupon` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL COMMENT '优惠券名称',
  `type` tinyint DEFAULT 1 COMMENT '类型：1-满减，2-折扣',
  `amount` decimal(10,2) NOT NULL COMMENT '优惠金额/折扣比例',
  `min_amount` decimal(10,2) DEFAULT NULL COMMENT '最低消费金额',
  `start_time` datetime NOT NULL COMMENT '生效时间',
  `end_time` datetime NOT NULL COMMENT '过期时间',
  `total` int DEFAULT 0 COMMENT '发放总量',
  `used` int DEFAULT 0 COMMENT '已使用数量',
  `status` tinyint DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  PRIMARY KEY (`id`)
) COMMENT='优惠券信息表';
```

###### 6.4  flash_sale 秒杀活动表

```sql
CREATE TABLE `flash_sale` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL COMMENT '活动名称',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `price` decimal(10,2) NOT NULL COMMENT '秒杀价',
  `stock` int DEFAULT 0 COMMENT '秒杀库存',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `status` tinyint DEFAULT 0 COMMENT '状态：0-未开始，1-进行中，2-已结束',
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`)
) COMMENT='秒杀活动表';
```

###### 6.5 user_notification 消息通知表

```sql
CREATE TABLE `user_notification` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `title` varchar(200) NOT NULL COMMENT '通知标题',
  `content` text NOT NULL COMMENT '通知内容',
  `is_read` tinyint DEFAULT 0 COMMENT '是否已读：0-未读，1-已读',
  `create_time` datetime DEFAULT NULL COMMENT '发送时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) COMMENT='用户消息通知表';
```

**外键关联**： 在订单、商品等表中，通过外键（如 `user_id`、`product_id`）与用户表和商品表关联，确保数据一致性。

**状态字段**： 使用 `status` 字段标记订单、商品等的状态，便于业务逻辑处理。

**JSON字段**： 在 `product_sku` 表中，使用 `specs` 字段（JSON类型）存储商品规格，灵活支持多种属性组合。

**扩展性**： 可根据需求添加更多表（如优惠券、评论、物流信息等），或扩展现有表的字段（如订单增加运费字段）。

**product_sku表（Stock Keeping Unit，库存量单位）**主要用于管理同一商品的不同属性组合**，例如颜色、尺寸、版本等。





#### 二、任务需求









