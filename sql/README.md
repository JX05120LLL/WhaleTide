# WhaleTide 商城系统优化后的数据库结构

## 优化概述

基于对原数据库结构的分析和评估，针对以下几个方面进行了优化：

1. **表命名规范化**：采用模块前缀+复数名词的命名方式，如 `ums_users`、`pms_products` 等
2. **字段命名统一**：保持一致的命名风格，避免混用下划线和驼峰
3. **外键约束添加**：增加了表间的外键关系，确保数据完整性
4. **字符集统一**：所有表统一使用 `utf8mb4` 字符集
5. **索引优化**：增加了合理的索引，提高查询效率
6. **数据冗余优化**：将JSON格式数据（如商家信息）拆分为独立表
7. **字段注释完善**：为所有字段添加了详细注释
8. **添加时间戳**：为需要的表添加创建时间和更新时间
9. **状态字段规范**：统一使用 tinyint 类型表示状态，并明确状态值含义
10. **添加软删除**：使用 `is_deleted` 字段实现软删除
11. **模块化重组**：按业务模块划分表结构

## 模块划分

系统被划分为以下5个主要模块：

1. **用户管理系统(UMS)**：管理用户、商家、地址等信息
2. **商品管理系统(PMS)**：管理商品、分类、品牌等信息
3. **订单管理系统(OMS)**：管理订单、支付、物流等信息
4. **营销管理系统(SMS)**：管理优惠券、秒杀、促销等活动
5. **权限管理系统(AMS)**：管理后台用户、角色、权限等信息

## 文件结构

```
sql/optimized/
├── README.md                      # 本说明文件
├── ums_user_management.sql        # 用户管理系统相关表
├── pms_product_management.sql     # 商品管理系统相关表
├── oms_order_management.sql       # 订单管理系统相关表
├── sms_marketing_management.sql   # 营销管理系统相关表
├── ams_authority_management.sql   # 权限管理系统相关表
```

## 各模块表概览

### 用户管理系统(UMS)

- `ums_users`: 用户表
- `ums_merchants`: 商家表
- `ums_user_addresses`: 用户地址表
- `ums_user_favorites`: 用户收藏表
- `ums_user_search_history`: 用户搜索历史表
- `ums_user_coupons`: 用户优惠券表
- `ums_user_login_logs`: 用户登录日志表
- `ums_user_roles`: 用户角色表
- `ums_notifications`: 用户通知表

### 商品管理系统(PMS)

- `pms_brands`: 品牌表
- `pms_products`: 商品表
- `pms_product_categories`: 商品分类表
- `pms_product_images`: 商品图片表
- `pms_product_details`: 商品详情表
- `pms_product_attributes`: 商品属性表
- `pms_product_attribute_values`: 商品属性值表
- `pms_product_skus`: 商品SKU表
- `pms_product_comments`: 商品评价表
- `pms_ratings`: 评分表
- `pms_stock_logs`: 库存变更记录表

### 订单管理系统(OMS)

- `oms_orders`: 订单主表
- `oms_order_items`: 订单项表
- `oms_order_deliveries`: 订单物流表
- `oms_order_logs`: 订单操作日志表
- `oms_order_status_history`: 订单状态历史表
- `oms_order_returns`: 订单退货申请表
- `oms_payments`: 支付记录表
- `oms_cart_items`: 购物车表

### 营销管理系统(SMS)

- `sms_coupons`: 优惠券表
- `sms_coupon_history`: 优惠券领取历史表
- `sms_coupon_products`: 优惠券适用商品关联表
- `sms_coupon_categories`: 优惠券适用分类关联表
- `sms_flash_promotion`: 限时秒杀活动表
- `sms_flash_promotion_sessions`: 限时秒杀场次表
- `sms_flash_promotion_products`: 限时秒杀商品关联表
- `sms_promotions`: 促销活动表
- `sms_promotion_products`: 促销活动商品关联表
- `sms_promotion_logs`: 促销活动参与记录表

### 权限管理系统(AMS)

- `ams_admins`: 管理员表
- `ams_roles`: 角色表
- `ams_permissions`: 权限表
- `ams_menus`: 菜单表
- `ams_resources`: 资源表
- `ams_resource_categories`: 资源分类表
- `ams_admin_role_relations`: 管理员与角色关系表
- `ams_role_menu_relations`: 角色与菜单关系表
- `ams_role_resource_relations`: 角色与资源关系表
- `ams_admin_login_logs`: 管理员登录日志表
- `ams_admin_operation_logs`: 管理员操作日志表

## 使用说明

1. 请先确保已备份现有数据库
2. 建议分模块进行表结构升级
3. 对于带有初始数据的表，请根据实际情况调整初始数据内容
4. 升级过程中请注意处理外键约束关系，确保数据完整性

## 注意事项

1. 本优化主要针对表结构，未对存储过程和触发器进行优化
2. 请根据实际业务情况，调整相关字段的默认值、约束条件等
3. 部分表中的示例数据仅供参考，实际使用时请根据需要调整
4. 如有特殊需求，可在此基础上进一步优化调整 