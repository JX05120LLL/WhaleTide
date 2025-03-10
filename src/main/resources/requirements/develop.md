### whaleTide  鲸浪商城



### 项目简介：WhaleTide 鲸浪商城

​	**WhaleTide 鲸浪商城** 是一个单商家多用户的电商平台，旨在为用户提供便捷的在线购物体验。商家可以在平台上发布商品，用户可以通过浏览商品、加入购物车、下单购买等操作完成购物流程。平台支持用户与商家之间的互动，包括消息通知、订单状态更新、退货申请等功能。系统设计简洁，功能模块清晰，适合中小型电商业务。

目前现有表包括，详细情况可查看mall-tiny 原开源项目，这是mall-tiny项目的地址[mall-tiny](https://github.com/macrozheng/mall-tiny)





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

###### 1.4  user_notification用户信息通知表

```sql
CREATE TABLE `user_notification` (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `sender_id` bigint(0) NOT NULL COMMENT '发送者ID（用户或商家）',
  `receiver_id` bigint(0) NOT NULL COMMENT '接收者ID（用户或商家）',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '通知标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '通知内容',
  `message_type` tinyint(0) NOT NULL COMMENT '消息类型：1-系统通知，2-用户消息，3-商家消息',
  `related_order_id` bigint(0) NULL DEFAULT NULL COMMENT '关联订单ID',
  `is_read` tinyint(0) NULL DEFAULT 0 COMMENT '是否已读：0-未读，1-已读',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '发送时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sender_id`(`sender_id`) USING BTREE,
  INDEX `idx_receiver_id`(`receiver_id`) USING BTREE,
  INDEX `idx_related_order_id`(`related_order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户消息通知表' ROW_FORMAT = Dynamic;
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

------



#### 二、任务需求，接口设定

***所有接口实现统一规定使用如下样式,以用户注册为例.***

***Controller层统一加上@RestController和@RequestMapping注解，具体方法再加上对应的具体路径。***

***Service层注意加入@Service注解，根据接口实现对应的方法，注意返回的结果类型...***

***Mapper 层全部采用MybatisPlus，注意加入@Mapper注解***

​		***不要修改其他部分代码，每个人完成自己的任务即可，防止错的覆盖push,目前接口部分会根据需求不断更新，后面会根据需求不断添加新的接口...注意及时更新仓库，查看新的需求***

```java
@RestController
@RequestMapping("/api/user")
public class UserController {
    
    @PostMapping("/login")
    public String login(String username, String password) {
        return null;
    }
}
```

```java
public interface IUserService {
    void register(String username, String password);// 注册
}
```

```java
@Service
public class UserServiceImpl implements IUserService {
    @Override
    public void register(String username, String password) {

    }
}
```

##### 1.用户相关

###### 1.1 用户注册

- 接口名称：用户注册

- URL: **/api/user/register**

- 接口方法定义（service层接口）：

  ```java
  void register(String username,String phone, String password);//支持手机号注册
  ```

- 请求方法: `POST`

- 请求参数：

```json
{
  "phone": "12345678901", // 手机号（必填）
  "nickname": "jafh", // 昵称（必填）
  "password": "123456"  // 密码（必填）
}
```

- 响应数据：

```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "string", // JWT Token
    "user": {
      "id": 1,
      "phone": "12345678901",
      "nickname": "User One"
    }
  }
}
```

###### 1.2用户登录

- 接口名称：用户登录

- URL：**/api/user/register**

- 接口方法定义：

- ```java
  void login( String phone,String password);// 支持手机号登录
  ```

- 请求方法：POST

- 请求参数：

```json
{
  "phone": "string",    // 手机号
  "password": "string"  // 密码
}
```

- 响应数据：

```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "string", // JWT Token
    "user": {
      "id": 1,
      "phone": "12345678901",
      "nickname": "User One"
    }
  }
}
```

###### 1.3 更新用户信息

- 接口名称: 更新用户信息
- URL: **`/api/user/update`**
- 接口定义：

```java
void update(User user); //用于更新用户信息
```

- 请求方法: POST
- 请求参数:

```json
{
  "email": "string",    // 邮箱（可选）
  "gender": "int",      // 性别：0-未知，1-男，2-女（可选）
  "avatar": "string"    // 头像URL（可选）
   ....				//省略，对应User实体类
}
```

- 响应数据

```json
{
  "code": 200,
  "message": "更新成功",
  "data": null
}
```

**以下接口同上格式，简略的写了*

###### 1.4 更新用户密码

- 接口方法名称：

```java
void updatePassword(String phone,String password);//用于更新用户密码
```

URL: **/api/user/updatePassword**

请求方式：POST

###### 1.6 更新用户地址

- 接口方法名称：

```java
void updateAddress(UserAddress userAddress);//用于更新用户地址
```

- URL:  **/api/user/updateAddress**
- 请求方式：POST
- 注意：*UserAddress 对应用户地址实体类*

###### 1.7  获取所有用户列表

- 接口方法名称：

```java
List<User> getUserList(); //用于获取用户列表
```

- URL：/api/user/getUserList
- 请求方式：GET
- 注意：*采用分页插件查询，每页显示15条数据*

###### 1.8 删除用户信息

- 接口方法名称：

```Java
void deleteUserById(int id); //用于删除用户信息
```

- URL : **/api/user/deleteUser**
- 请求方式：POST

###### 1.9  根据ID查询用户信息

- 接口方法名称：

```Java
User getUserById(int id); //用于根据id获取用户信息
```

URL：**/api/user/getUserById**

请求方式：GET

###### 1.10 根据用户手机号查询订单

- 接口方法名称：

```java
Order getOrderByUserPhone(String phone); //用于根据手机号获取订单信息
```

​	URL：**/api/user/getOrderByUserId**

请求方式：GET

###### 1.11 根据用户ID查询 购物车信息

- 接口方法名称：

```java
Cart getCartByUserId(int userId); //用于根据用户id获取购物车信息
```

URL：**/api/user/getOrderByUserId**

请求方式：GET

###### 1.12 设置用户默认地址

- 接口方法名称：

```java
int  setDefalutAddress(User user,int addressId) ;   //设置用户的默认地址，返回int  0-否 1-是
```

- URL：**/api/user/setDefalutAddress**

- 请求方式：POST 

##### 2.商品相关

###### 2.1 添加商品

接口方法名称：

