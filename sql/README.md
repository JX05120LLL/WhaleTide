# WhaleTide 电商系统数据库设计文档

## 项目概述

WhaleTide 是一个全功能电商平台，采用模块化设计，实现了用户管理、商品管理、订单管理、营销活动和权限管理等核心功能。本文档详细说明数据库表结构设计及表之间的关系，为开发人员提供清晰的开发指导。

## 数据库模块划分

数据库采用前缀区分不同业务模块：

- **ams_**: 管理系统 (Admin Management System)
- **oms_**: 订单管理系统 (Order Management System)
- **pms_**: 商品管理系统 (Product Management System)
- **sms_**: 营销管理系统 (Sales Management System)
- **ums_**: 用户管理系统 (User Management System)

## 权限管理系统

### 核心表结构

#### 1. 管理员 (ams_admins)
| 字段名 | 类型 | 说明 | 备注 |
|-------|------|------|------|
| id | bigint | 管理员ID | 主键 |
| username | varchar(64) | 用户名 | 唯一索引 |
| password | varchar(64) | 密码 | 加密存储 |
| real_name | varchar(64) | 真实姓名 | |
| email | varchar(100) | 邮箱 | |
| phone | varchar(20) | 手机号 | |
| avatar | varchar(500) | 头像 | |
| gender | tinyint | 性别 | 0-未知，1-男，2-女 |
| status | tinyint | 状态 | 0-禁用，1-启用 |
| super_admin | tinyint | 是否超级管理员 | 0-否，1-是 |
| department | varchar(100) | 部门 | |
| position | varchar(100) | 职位 | |
| create_time | datetime | 创建时间 | |
| update_time | datetime | 更新时间 | |

#### 2. 角色 (ams_roles)
| 字段名 | 类型 | 说明 | 备注 |
|-------|------|------|------|
| id | bigint | 角色ID | 主键 |
| name | varchar(100) | 角色名称 | |
| code | varchar(50) | 角色编码 | 唯一索引 |
| description | varchar(500) | 角色描述 | |
| sort | int | 排序 | |
| status | tinyint | 状态 | 0-禁用，1-启用 |
| create_time | datetime | 创建时间 | |
| update_time | datetime | 更新时间 | |

#### 3. 权限 (ams_permissions)
| 字段名 | 类型 | 说明 | 备注 |
|-------|------|------|------|
| id | bigint | 权限ID | 主键 |
| name | varchar(100) | 权限名称 | |
| code | varchar(100) | 权限编码 | 唯一索引 |
| type | tinyint | 权限类型 | 0-目录，1-菜单，2-按钮，3-接口 |
| parent_id | bigint | 父级ID | |
| path | varchar(100) | 路径 | 前端路由路径 |
| component | varchar(100) | 组件 | 前端组件 |
| redirect | varchar(100) | 重定向 | |
| icon | varchar(100) | 图标 | |
| sort | int | 排序 | |
| hidden | tinyint | 是否隐藏 | 0-否，1-是 |
| status | tinyint | 状态 | 0-禁用，1-启用 |
| create_time | datetime | 创建时间 | |
| update_time | datetime | 更新时间 | |

#### 4. 菜单 (ams_menus)
| 字段名 | 类型 | 说明 | 备注 |
|-------|------|------|------|
| id | bigint | 菜单ID | 主键 |
| parent_id | bigint | 父级ID | |
| title | varchar(100) | 菜单名称 | |
| level | int | 菜单级别 | |
| sort | int | 排序 | |
| icon | varchar(200) | 图标 | |
| path | varchar(200) | 路径 | 前端路由路径 |
| component | varchar(100) | 组件 | 前端组件 |
| redirect | varchar(200) | 重定向 | |
| hidden | tinyint | 是否隐藏 | 0-否，1-是 |
| status | tinyint | 状态 | 0-禁用，1-启用 |
| create_time | datetime | 创建时间 | |
| update_time | datetime | 更新时间 | |

#### 5. 资源 (ams_resources)
| 字段名 | 类型 | 说明 | 备注 |
|-------|------|------|------|
| id | bigint | 资源ID | 主键 |
| category_id | bigint | 分类ID | 外键 |
| name | varchar(200) | 资源名称 | |
| url | varchar(200) | 资源URL | 接口地址 |
| method | varchar(10) | 请求方法 | GET、POST等 |
| description | varchar(500) | 描述 | |
| create_time | datetime | 创建时间 | |
| update_time | datetime | 更新时间 | |

### 关联关系表

#### 1. 管理员与角色关系 (ams_admin_role_relations)
| 字段名 | 类型 | 说明 | 备注 |
|-------|------|------|------|
| id | bigint | 主键ID | 主键 |
| admin_id | bigint | 管理员ID | 外键 |
| role_id | bigint | 角色ID | 外键 |
| create_time | datetime | 创建时间 | |

#### 2. 角色与菜单关系 (ams_role_menu_relations)
| 字段名 | 类型 | 说明 | 备注 |
|-------|------|------|------|
| id | bigint | 主键ID | 主键 |
| role_id | bigint | 角色ID | 外键 |
| menu_id | bigint | 菜单ID | 外键 |
| create_time | datetime | 创建时间 | |

#### 3. 角色与权限关系 (ams_role_permission_relations)
| 字段名 | 类型 | 说明 | 备注 |
|-------|------|------|------|
| id | bigint | 主键ID | 主键 |
| role_id | bigint | 角色ID | 外键 |
| permission_id | bigint | 权限ID | 外键 |
| create_time | datetime | 创建时间 | |

#### 4. 角色与资源关系 (ams_role_resource_relations)
| 字段名 | 类型 | 说明 | 备注 |
|-------|------|------|------|
| id | bigint | 主键ID | 主键 |
| role_id | bigint | 角色ID | 外键 |
| resource_id | bigint | 资源ID | 外键 |
| create_time | datetime | 创建时间 | |

### 权限系统关系图

```
+----------------+      +---------------+      +------------------+
|  ams_admins    |      |  ams_roles    |      |  ams_permissions |
+----------------+      +---------------+      +------------------+
| id             |      | id            |      | id               |
| username       |      | name          |      | name             |
| password       |      | code          |      | code             |
| ...            |      | ...           |      | type             |
+-------+--------+      +-------+-------+      | parent_id        |
        |                       |               | ...              |
        |                       |               +------------------+
        |                       |                        ^
        v                       v                        |
+----------------+      +-------+-------+      +---------+--------+
| admin_role_    |      | role_         |      | role_permission_ |
| relations      |----->| menu_         |      | relations        |
+----------------+      | relations     |      +------------------+
| admin_id       |      +---------------+      | role_id          |
| role_id        |      | role_id       |      | permission_id    |
+----------------+      | menu_id       |      +------------------+
                        +-------+-------+               ^
                                |                       |
                                v                       |
                        +-------+-------+      +--------+-------+
                        | ams_menus     |      | ams_resources  |
                        +---------------+      +----------------+
                        | id            |      | id             |
                        | parent_id     |      | name           |
                        | title         |      | url            |
                        | ...           |      | method         |
                        +---------------+      | ...            |
                                               +----------------+
                                                       ^
                                                       |
                                               +-------+--------+
                                               | role_resource_ |
                                               | relations      |
                                               +----------------+
                                               | role_id        |
                                               | resource_id    |
                                               +----------------+
```

### 权限系统开发指南

1. **权限验证流程**：
   - 用户登录后获取角色列表
   - 根据角色获取菜单权限
   - 根据角色获取操作权限
   - 根据角色获取API资源权限

2. **权限控制实现**：
   - 前端菜单权限：通过`ams_role_menu_relations`控制菜单显示
   - 按钮权限：通过`ams_role_permission_relations`控制按钮显示
   - 接口权限：通过`ams_role_resource_relations`控制接口访问

3. **权限缓存策略**：
   - 用户登录后缓存角色信息
   - 缓存菜单权限列表
   - 缓存操作权限列表
   - 缓存API资源权限列表

## 业务模块表关系

### 1. 订单管理系统(OMS)

#### 核心表结构

##### 1.1 订单主表 (oms_orders)
| 字段名 | 类型 | 说明 | 备注 |
|-------|------|------|------|
| id | bigint | 订单ID | 主键 |
| order_sn | varchar(64) | 订单编号 | 唯一索引 |
| user_id | bigint | 用户ID | 外键 |
| merchant_id | bigint | 商家ID | 外键 |
| order_type | tinyint | 订单类型 | 0-普通订单，1-秒杀订单，2-团购订单 |
| source_type | tinyint | 订单来源 | 0-PC, 1-App, 2-小程序, 3-H5 |
| status | tinyint | 订单状态 | 0-待付款，1-待发货，2-已发货，3-已完成，4-已关闭，5-已取消 |
| total_amount | decimal(10,2) | 订单总金额 | |
| pay_amount | decimal(10,2) | 实付金额 | |
| freight_amount | decimal(10,2) | 运费 | |
| discount_amount | decimal(10,2) | 优惠金额 | |
| coupon_amount | decimal(10,2) | 优惠券抵扣金额 | |
| promotion_amount | decimal(10,2) | 促销优惠金额 | |
| integration_amount | decimal(10,2) | 积分抵扣金额 | |
| order_note | varchar(500) | 订单备注 | |
| pay_type | tinyint | 支付方式 | 0-未支付，1-支付宝，2-微信，3-银联 |
| payment_time | datetime | 支付时间 | |
| delivery_time | datetime | 发货时间 | |
| receive_time | datetime | 确认收货时间 | |
| comment_time | datetime | 评价时间 | |
| auto_confirm_day | int | 自动确认收货天数 | 默认7天 |
| is_deleted | tinyint | 是否删除 | 0-否，1-是 |
| create_time | datetime | 创建时间 | |
| update_time | datetime | 更新时间 | |

##### 1.2 订单商品表 (oms_order_items)
| 字段名 | 类型 | 说明 | 备注 |
|-------|------|------|------|
| id | bigint | 订单项ID | 主键 |
| order_id | bigint | 订单ID | 外键 |
| order_sn | varchar(64) | 订单编号 | |
| product_id | bigint | 商品ID | 外键 |
| product_name | varchar(200) | 商品名称 | |
| product_image | varchar(500) | 商品图片 | |
| product_brand | varchar(100) | 品牌名称 | |
| product_sn | varchar(64) | 商品编号 | |
| product_category_id | bigint | 商品分类ID | |
| sku_id | bigint | SKU ID | 外键 |
| sku_code | varchar(50) | SKU编码 | |
| sku_specs | varchar(255) | 规格属性 | |
| quantity | int | 购买数量 | |
| price | decimal(10,2) | 商品单价 | |
| real_amount | decimal(10,2) | 实付金额 | |
| original_amount | decimal(10,2) | 原始金额 | 无优惠时的金额 |
| coupon_amount | decimal(10,2) | 优惠券优惠分摊金额 | |
| promotion_amount | decimal(10,2) | 促销优惠分摊金额 | |
| promotion_name | varchar(200) | 促销活动信息 | |
| gift_integration | int | 赠送积分 | |
| gift_growth | int | 赠送成长值 | |
| refund_status | tinyint | 退款状态 | 0-未退款，1-已申请，2-已退款 |
| comment_status | tinyint | 评价状态 | 0-未评价，1-已评价 |
| create_time | datetime | 创建时间 | |
| update_time | datetime | 更新时间 | |

##### 1.3 订单物流表 (oms_order_deliveries)
| 字段名 | 类型 | 说明 | 备注 |
|-------|------|------|------|
| id | bigint | 物流ID | 主键 |
| order_id | bigint | 订单ID | 外键 |
| delivery_sn | varchar(64) | 物流单号 | |
| delivery_company | varchar(100) | 物流公司 | |
| receiver_name | varchar(64) | 收件人姓名 | |
| receiver_phone | varchar(20) | 收件人电话 | |
| receiver_address | varchar(500) | 详细地址 | |
| delivery_status | tinyint | 物流状态 | 0-未发货，1-已发货，2-运输中，3-已签收，4-派送失败 |
| delivery_time | datetime | 发货时间 | |
| receive_time | datetime | 签收时间 | |
| create_time | datetime | 创建时间 | |
| update_time | datetime | 更新时间 | |

##### 1.4 订单支付表 (oms_payments)
| 字段名 | 类型 | 说明 | 备注 |
|-------|------|------|------|
| id | bigint | 支付ID | 主键 |
| order_id | bigint | 订单ID | 外键 |
| payment_sn | varchar(64) | 支付流水号 | |
| payment_method | tinyint | 支付方式 | 1-支付宝，2-微信，3-银联 |
| amount | decimal(10,2) | 支付金额 | |
| status | tinyint | 支付状态 | 0-待支付，1-支付中，2-支付成功，3-支付失败 |
| payment_time | datetime | 支付时间 | |
| create_time | datetime | 创建时间 | |
| update_time | datetime | 更新时间 | |

##### 1.5 订单退货表 (oms_order_returns)
| 字段名 | 类型 | 说明 | 备注 |
|-------|------|------|------|
| id | bigint | 退货ID | 主键 |
| order_id | bigint | 订单ID | 外键 |
| return_type | tinyint | 退货类型 | 0-仅退款，1-退货退款 |
| return_reason | varchar(500) | 退货原因 | |
| return_amount | decimal(10,2) | 退款金额 | |
| status | tinyint | 状态 | 0-待处理，1-处理中，2-已同意，3-已拒绝，4-已完成 |
| handler_id | bigint | 处理人ID | |
| handler_note | varchar(500) | 处理备注 | |
| create_time | datetime | 创建时间 | |
| update_time | datetime | 更新时间 | |

##### 1.6 订单日志表 (oms_order_logs)
| 字段名 | 类型 | 说明 | 备注 |
|-------|------|------|------|
| id | bigint | 日志ID | 主键 |
| order_id | bigint | 订单ID | 外键 |
| order_sn | varchar(64) | 订单编号 | |
| operator_id | bigint | 操作人ID | |
| operator_type | tinyint | 操作人类型 | 0-系统，1-用户，2-商家，3-管理员 |
| operator_name | varchar(100) | 操作人名称 | |
| action | varchar(100) | 操作类型 | |
| note | varchar(500) | 操作备注 | |
| ip | varchar(64) | 操作IP | |
| create_time | datetime | 操作时间 | |

##### 1.7 订单状态历史表 (oms_order_status_history)
| 字段名 | 类型 | 说明 | 备注 |
|-------|------|------|------|
| id | bigint | 主键ID | 主键 |
| order_id | bigint | 订单ID | 外键 |
| order_sn | varchar(64) | 订单编号 | |
| previous_status | tinyint | 前置状态 | |
| current_status | tinyint | 当前状态 | |
| operator_id | bigint | 操作人ID | |
| operator_type | tinyint | 操作人类型 | 0-系统，1-用户，2-商家，3-管理员 |
| operator_name | varchar(100) | 操作人名称 | |
| note | varchar(500) | 状态变更备注 | |
| create_time | datetime | 创建时间 | |

##### 1.8 购物车表 (oms_cart_items)
| 字段名 | 类型 | 说明 | 备注 |
|-------|------|------|------|
| id | bigint | 购物车项ID | 主键 |
| user_id | bigint | 用户ID | 外键 |
| product_id | bigint | 商品ID | 外键 |
| product_name | varchar(200) | 商品名称 | |
| product_image | varchar(500) | 商品图片 | |
| sku_id | bigint | SKU ID | 外键 |
| sku_specs | varchar(255) | 规格属性 | 文本表示 |
| price | decimal(10,2) | 商品单价 | |
| quantity | int | 数量 | 默认1 |
| checked | tinyint | 是否选中 | 0-否，1-是 |
| create_time | datetime | 创建时间 | |
| update_time | datetime | 更新时间 | |

#### 订单系统关系图

```
                        +---------------+
                        |  oms_orders   |
                        +---------------+
                        | id            |
                        | order_sn      |
                        | user_id       |
                        | ...           |
                        +-------+-------+
                                |
                                |
            +------------------+------------------+------------------+
            |                  |                  |                  |
            v                  v                  v                  v
  +-----------------+  +---------------+  +---------------+  +---------------+
  |  oms_order_     |  | oms_order_    |  | oms_payments  |  | oms_order_    |
  |  items          |  | deliveries    |  +---------------+  | logs          |
  +-----------------+  +---------------+  | order_id      |  +---------------+
  | order_id        |  | order_id      |  | payment_sn    |  | order_id      |
  | product_id      |  | delivery_sn   |  | ...           |  | action        |
  | ...             |  | ...           |  +---------------+  | ...           |
  +-----------------+  +---------------+                     +---------------+
            |                                                        |
            |                                                        |
            v                                                        v
  +-----------------+                                       +------------------+
  | oms_order_      |                                       | oms_order_       |
  | returns         |                                       | status_history   |
  +-----------------+                                       +------------------+
  | order_id        |                                       | order_id         |
  | return_reason   |                                       | previous_status  |
  | ...             |                                       | current_status   |
  +-----------------+                                       | ...              |
                                                            +------------------+
```

### 2. 商品管理系统(PMS)

#### 核心表结构

##### 2.1 商品主表 (pms_products)
| 字段名 | 类型 | 说明 | 备注 |
|-------|------|------|------|
| id | bigint | 商品ID | 主键 |
| name | varchar(200) | 商品名称 | |
| product_sn | varchar(64) | 商品编号 | 唯一索引 |
| category_id | bigint | 分类ID | 外键 |
| brand_id | bigint | 品牌ID | 外键 |
| merchant_id | bigint | 商家ID | 外键 |
| price | decimal(10,2) | 商品价格 | |
| original_price | decimal(10,2) | 原价 | |
| main_image | varchar(500) | 主图URL | |
| keywords | varchar(255) | 关键词 | |
| brief | varchar(255) | 简短描述 | |
| sale | int | 销量 | |
| stock | int | 库存 | |
| unit | varchar(20) | 单位 | 件/kg等 |
| weight | decimal(10,2) | 重量 | kg |
| sort | int | 排序 | |
| publish_status | tinyint | 上架状态 | 0-下架，1-上架 |
| new_status | tinyint | 新品状态 | 0-非新品，1-新品 |
| recommend_status | tinyint | 推荐状态 | 0-不推荐，1-推荐 |
| verify_status | tinyint | 审核状态 | 0-未审核，1-审核通过，2-审核不通过 |
| is_deleted | tinyint | 是否删除 | 0-否，1-是 |
| create_time | datetime | 创建时间 | |
| update_time | datetime | 更新时间 | |

##### 2.2 商品SKU表 (pms_product_skus)
| 字段名 | 类型 | 说明 | 备注 |
|-------|------|------|------|
| id | bigint | SKU ID | 主键 |
| product_id | bigint | 商品ID | 外键 |
| sku_code | varchar(50) | SKU编码 | 唯一索引 |
| price | decimal(10,2) | 价格 | |
| stock | int | 库存 | |
| specs | json | 规格属性 | JSON格式存储 |
| specs_text | varchar(255) | 规格文本 | 用于展示 |
| sale | int | 销量 | |
| lock_stock | int | 锁定库存 | 下单但未付款 |
| create_time | datetime | 创建时间 | |
| update_time | datetime | 更新时间 | |

##### 2.3 商品分类表 (pms_product_categories)
| 字段名 | 类型 | 说明 | 备注 |
|-------|------|------|------|
| id | bigint | 分类ID | 主键 |
| parent_id | bigint | 父分类ID | |
| name | varchar(100) | 分类名称 | |
| level | tinyint | 分类层级 | 1-一级分类，2-二级分类，3-三级分类 |
| icon | varchar(500) | 分类图标 | |
| sort | int | 排序 | |
| status | tinyint | 状态 | 0-禁用，1-启用 |
| is_featured | tinyint | 是否推荐 | 0-否，1-是 |
| create_time | datetime | 创建时间 | |
| update_time | datetime | 更新时间 | |

##### 2.4 商品属性表 (pms_product_attributes)
| 字段名 | 类型 | 说明 | 备注 |
|-------|------|------|------|
| id | bigint | 属性ID | 主键 |
| category_id | bigint | 所属分类ID | 外键 |
| name | varchar(100) | 属性名称 | |
| attribute_type | tinyint | 属性类型 | 0-规格，1-参数 |
| input_type | tinyint | 录入方式 | 0-手工输入，1-从列表选择 |
| input_options | varchar(500) | 可选值列表 | 以逗号分隔 |
| filter_type | tinyint | 检索类型 | 0-不需要，1-需要 |
| search_type | tinyint | 搜索类型 | 0-不需要，1-需要 |
| create_time | datetime | 创建时间 | |
| update_time | datetime | 更新时间 | |

##### 2.5 商品详情表 (pms_product_details)
| 字段名 | 类型 | 说明 | 备注 |
|-------|------|------|------|
| id | bigint | 详情ID | 主键 |
| product_id | bigint | 商品ID | 外键 |
| description | longtext | 商品详细描述 | |
| specs | text | 商品规格 | |
| packing_list | text | 包装清单 | |
| after_service | text | 售后服务 | |
| create_time | datetime | 创建时间 | |
| update_time | datetime | 更新时间 | |

##### 2.6 商品图片表 (pms_product_images)
| 字段名 | 类型 | 说明 | 备注 |
|-------|------|------|------|
| id | bigint | 图片ID | 主键 |
| product_id | bigint | 商品ID | 外键 |
| image_url | varchar(500) | 图片URL | |
| sort | int | 排序 | |
| is_main | tinyint | 是否主图 | 0-否，1-是 |
| create_time | datetime | 创建时间 | |
| update_time | datetime | 更新时间 | |

##### 2.7 商品评分表 (pms_ratings)
| 字段名 | 类型 | 说明 | 备注 |
|-------|------|------|------|
| id | bigint | 评分ID | 主键 |
| user_id | bigint | 用户ID | 外键 |
| product_id | bigint | 商品ID | 外键 |
| rating | tinyint | 评分 | 1-5分 |
| rating_type | tinyint | 评分类型 | 0-综合，1-服务，2-质量，3-物流 |
| create_time | datetime | 评分时间 | |

##### 2.8 库存变更日志表 (pms_stock_logs)
| 字段名 | 类型 | 说明 | 备注 |
|-------|------|------|------|
| id | bigint | 日志ID | 主键 |
| product_id | bigint | 商品ID | 外键 |
| sku_id | bigint | SKU ID | 外键 |
| change_type | tinyint | 变更类型 | 1-销售扣减，2-退货增加，3-后台调整 |
| change_amount | int | 变更数量 | |
| before_stock | int | 变更前库存 | |
| after_stock | int | 变更后库存 | |
| order_id | bigint | 关联订单ID | |
| order_item_id | bigint | 关联订单项ID | |
| operator | varchar(64) | 操作人 | |
| remark | varchar(500) | 备注 | |
| create_time | datetime | 操作时间 | |

#### 商品系统关系图

```
                   +----------------+
                   | pms_products   |
                   +----------------+
                   | id             |
                   | name           |
                   | category_id    |
                   | brand_id       |
                   | ...            |
                   +-------+--------+
                           |
         +----------------+-------------------+------------------+
         |                |                   |                  |
         v                v                   v                  v
+----------------+ +--------------+  +---------------+  +----------------+
| pms_product_   | | pms_product_ |  | pms_product_  |  | pms_stock_     |
| skus           | | images       |  | details       |  | logs           |
+----------------+ +--------------+  +---------------+  +----------------+
| product_id     | | product_id   |  | product_id    |  | product_id     |
| sku_code       | | image_url    |  | description   |  | sku_id         |
| ...            | | ...          |  | ...           |  | ...            |
+----------------+ +--------------+  +---------------+  +----------------+
                                                                |
                                                                v
                                                      +------------------+
                                                      | pms_ratings      |
                                                      +------------------+
                                                      | product_id       |
                                                      | user_id          |
                                                      | rating           |
                                                      | ...              |
                                                      +------------------+
```

### 3. 用户管理系统(UMS)

#### 核心表结构

##### 3.1 用户表 (ums_users)
| 字段名 | 类型 | 说明 | 备注 |
|-------|------|------|------|
| id | bigint | 用户ID | 主键 |
| username | varchar(64) | 用户名 | 唯一索引 |
| password | varchar(64) | 密码 | |
| phone | varchar(20) | 手机号 | 唯一索引 |
| email | varchar(100) | 邮箱 | |
| nickname | varchar(64) | 昵称 | |
| real_name | varchar(64) | 真实姓名 | |
| gender | tinyint | 性别 | 0-未知，1-男，2-女 |
| birth | date | 生日 | |
| avatar | varchar(500) | 头像 | |
| status | tinyint | 状态 | 0-禁用，1-启用 |
| status_reason | varchar(500) | 状态原因 | |
| register_source | tinyint | 注册来源 | 0-PC，1-APP，2-小程序，3-H5 |
| last_login_time | datetime | 最后登录时间 | |
| last_login_ip | varchar(64) | 最后登录IP | |
| is_merchant | tinyint | 是否商家 | 0-否，1-是 |
| level | int | 会员等级 | |
| integration | int | 积分 | |
| growth | int | 成长值 | |
| region | varchar(100) | 所在地区 | |
| note | varchar(500) | 备注 | |
| is_deleted | tinyint | 是否删除 | 0-否，1-是 |
| create_time | datetime | 创建时间 | |
| update_time | datetime | 更新时间 | |

##### 3.2 用户地址表 (ums_user_addresses)
| 字段名 | 类型 | 说明 | 备注 |
|-------|------|------|------|
| id | bigint | 地址ID | 主键 |
| user_id | bigint | 用户ID | 外键 |
| receiver_name | varchar(64) | 收货人姓名 | |
| receiver_phone | varchar(20) | 收货人电话 | |
| province | varchar(50) | 省份 | |
| city | varchar(50) | 城市 | |
| district | varchar(50) | 区县 | |
| detail_address | varchar(200) | 详细地址 | |
| is_default | tinyint | 是否默认 | 0-否，1-是 |
| create_time | datetime | 创建时间 | |
| update_time | datetime | 更新时间 | |

##### 3.3 用户收藏表 (ums_user_favorites)
| 字段名 | 类型 | 说明 | 备注 |
|-------|------|------|------|
| id | bigint | 收藏ID | 主键 |
| user_id | bigint | 用户ID | 外键 |
| product_id | bigint | 商品ID | 外键 |
| product_name | varchar(200) | 商品名称 | |
| product_image | varchar(500) | 商品图片 | |
| product_price | decimal(10,2) | 商品价格 | |
| create_time | datetime | 创建时间 | |
| update_time | datetime | 更新时间 | |

##### 3.4 用户优惠券表 (ums_user_coupons)
| 字段名 | 类型 | 说明 | 备注 |
|-------|------|------|------|
| id | bigint | 主键ID | 主键 |
| user_id | bigint | 用户ID | 外键 |
| coupon_id | bigint | 优惠券ID | 外键 |
| coupon_code | varchar(64) | 优惠券码 | |
| amount | decimal(10,2) | 优惠金额 | |
| discount | decimal(5,2) | 折扣 | |
| min_point | decimal(10,2) | 使用门槛 | |
| status | tinyint | 状态 | 0-未使用，1-已使用，2-已过期 |
| use_time | datetime | 使用时间 | |
| order_id | bigint | 订单ID | |
| create_time | datetime | 创建时间 | |
| update_time | datetime | 更新时间 | |

##### 3.5 商家表 (ums_merchants)
| 字段名 | 类型 | 说明 | 备注 |
|-------|------|------|------|
| id | bigint | 商家ID | 主键 |
| user_id | bigint | 用户ID | 外键 |
| shop_name | varchar(100) | 店铺名称 | |
| shop_logo | varchar(500) | 店铺logo | |
| shop_banner | varchar(500) | 店铺横幅 | |
| business_license | varchar(500) | 营业执照 | |
| identity_card_front | varchar(500) | 身份证正面 | |
| identity_card_back | varchar(500) | 身份证背面 | |
| contact_name | varchar(64) | 联系人姓名 | |
| contact_phone | varchar(20) | 联系电话 | |
| contact_email | varchar(100) | 联系邮箱 | |
| business_categories | varchar(500) | 经营类目 | 多个用逗号分隔 |
| business_scope | text | 经营范围 | |
| shop_address | varchar(500) | 店铺地址 | |
| shop_province | varchar(50) | 省份 | |
| shop_city | varchar(50) | 城市 | |
| shop_district | varchar(50) | 区县 | |
| status | tinyint | 状态 | 0-待审核，1-审核通过，2-审核拒绝，3-冻结 |
| rating | decimal(2,1) | 店铺评分 | |
| service_rating | decimal(2,1) | 服务评分 | |
| delivery_rating | decimal(2,1) | 物流评分 | |
| description_rating | decimal(2,1) | 描述相符评分 | |
| create_time | datetime | 创建时间 | |
| update_time | datetime | 更新时间 | |

##### 3.6 用户通知表 (ums_notifications)
| 字段名 | 类型 | 说明 | 备注 |
|-------|------|------|------|
| id | bigint | 通知ID | 主键 |
| sender_id | bigint | 发送者ID | |
| sender_type | tinyint | 发送者类型 | 0-系统，1-用户，2-商家，3-管理员 |
| sender_name | varchar(64) | 发送者名称 | |
| receiver_id | bigint | 接收者ID | 外键 |
| title | varchar(200) | 通知标题 | |
| content | text | 通知内容 | |
| type | tinyint | 通知类型 | 0-系统通知，1-订单通知，2-活动通知，3-物流通知，4-商家通知 |
| read_status | tinyint | 阅读状态 | 0-未读，1-已读 |
| read_time | datetime | 阅读时间 | |
| create_time | datetime | 创建时间 | |
| update_time | datetime | 更新时间 | |

##### 3.7 用户角色表 (ums_user_roles)
| 字段名 | 类型 | 说明 | 备注 |
|-------|------|------|------|
| id | bigint | 主键ID | 主键 |
| user_id | bigint | 用户ID | 外键 |
| role_name | varchar(100) | 角色名称 | |
| role_code | varchar(50) | 角色编码 | |
| note | varchar(500) | 备注 | |
| create_time | datetime | 创建时间 | |
| update_time | datetime | 更新时间 | |

##### 3.8 用户搜索历史表 (ums_user_search_history)
| 字段名 | 类型 | 说明 | 备注 |
|-------|------|------|------|
| id | bigint | 搜索ID | 主键 |
| user_id | bigint | 用户ID | 外键 |
| keyword | varchar(200) | 搜索关键词 | |
| search_count | int | 搜索次数 | 默认1 |
| last_search_time | datetime | 最后搜索时间 | |
| create_time | datetime | 创建时间 | |

#### 用户系统关系图

```
                   +----------------+
                   |  ums_users     |
                   +----------------+
                   | id             |
                   | username       |
                   | ...            |
                   +-------+--------+
                           |
         +----------------+-------------------+------------------+------------------+
         |                |                   |                  |                  |
         v                v                   v                  v                  v
+----------------+ +--------------+  +---------------+  +----------------+  +----------------+
| ums_user_      | | ums_user_    |  | ums_user_     |  | ums_merchants  |  | ums_user_      |
| addresses      | | favorites    |  | coupons       |  +----------------+  | roles          |
+----------------+ +--------------+  +---------------+  | user_id         |  +----------------+
| user_id        | | user_id      |  | user_id       |  | shop_name      |  | user_id         |
| receiver_name  | | product_id     |  | coupon_id     |  | ...            |  | role_name       |
| ...            | | ...          |  | ...           |  +----------------+  | ...             |
+----------------+ +--------------+  +---------------+           |          +----------------+
                                                                 |                  |
                                                                 v                  v
                                                      +------------------+  +------------------+
                                                      | ums_notifications |  | ums_user_search_ |
                                                      +------------------+  | history          |
                                                      | receiver_id      |  +------------------+
                                                      | sender_id        |  | user_id          |
                                                      | ...              |  | keyword          |
                                                      +------------------+  | ...              |
                                                                            +------------------+
```

## 开发指南

### 1. 权限系统开发

1. **权限验证流程**：
   ```java
   // 1. 用户登录后获取角色列表
   List<Role> roles = roleService.getRolesByAdminId(adminId);
   
   // 2. 根据角色获取菜单权限
   List<Menu> menus = menuService.getMenusByRoleIds(roleIds);
   
   // 3. 根据角色获取操作权限
   List<Permission> permissions = permissionService.getPermissionsByRoleIds(roleIds);
   
   // 4. 根据角色获取API资源权限
   List<Resource> resources = resourceService.getResourcesByRoleIds(roleIds);
   ```

2. **权限缓存策略**：
   ```java
   // 1. 用户登录后缓存角色信息
   @Cacheable(value = "admin_roles", key = "#adminId")
   public List<Role> getRolesByAdminId(Long adminId) {
       return roleMapper.selectByAdminId(adminId);
   }
   
   // 2. 缓存菜单权限列表
   @Cacheable(value = "role_menus", key = "#roleIds")
   public List<Menu> getMenusByRoleIds(List<Long> roleIds) {
       return menuMapper.selectByRoleIds(roleIds);
   }
   ```

3. **权限注解使用**：
   ```java
   // 1. 菜单权限注解
   @RequiresMenu("system:user:list")
   public String userList() {
       return "system/user/list";
   }
   
   // 2. 按钮权限注解
   @RequiresPermission("system:user:add")
   public Result addUser() {
       // 添加用户逻辑
   }
   
   // 3. 接口权限注解
   @RequiresResource("/api/user/add")
   public Result addUser() {
       // 添加用户逻辑
   }
   ```

### 2. 订单系统开发

1. **订单创建流程**：
   ```java
   // 1. 创建订单主表记录
   Order order = new Order();
   order.setOrderSn(generateOrderSn());
   order.setUserId(userId);
   order.setStatus(OrderStatus.UNPAID);
   orderMapper.insert(order);
   
   // 2. 创建订单商品记录
   for (OrderItem item : orderItems) {
       item.setOrderId(order.getId());
       item.setOrderSn(order.getOrderSn());
       orderItemMapper.insert(item);
   }
   
   // 3. 创建支付记录
   Payment payment = new Payment();
   payment.setOrderId(order.getId());
   payment.setPaymentSn(generatePaymentSn());
   paymentMapper.insert(payment);
   ```

2. **订单状态流转**：
   ```java
   // 1. 订单支付成功
   @Transactional
   public void paySuccess(Long orderId) {
       // 更新订单状态
       Order order = orderMapper.selectById(orderId);
       order.setStatus(OrderStatus.PAID);
       order.setPaymentTime(new Date());
       orderMapper.updateById(order);
       
       // 更新支付记录
       Payment payment = paymentMapper.selectByOrderId(orderId);
       payment.setStatus(PaymentStatus.SUCCESS);
       payment.setPaymentTime(new Date());
       paymentMapper.updateById(payment);
       
       // 创建发货记录
       Delivery delivery = new Delivery();
       delivery.setOrderId(orderId);
       delivery.setDeliverySn(generateDeliverySn());
       deliveryMapper.insert(delivery);
   }
   ```

### 3. 商品系统开发

1. **商品创建流程**：
   ```java
   // 1. 创建商品主表记录
   Product product = new Product();
   product.setName(productName);
   product.setProductSn(generateProductSn());
   productMapper.insert(product);
   
   // 2. 创建商品SKU记录
   for (ProductSku sku : skus) {
       sku.setProductId(product.getId());
       sku.setSkuCode(generateSkuCode());
       productSkuMapper.insert(sku);
   }
   
   // 3. 创建商品图片记录
   for (ProductImage image : images) {
       image.setProductId(product.getId());
       productImageMapper.insert(image);
   }
   ```

2. **商品库存管理**：
   ```java
   // 1. 锁定库存
   @Transactional
   public void lockStock(Long skuId, Integer quantity) {
       ProductSku sku = productSkuMapper.selectById(skuId);
       if (sku.getStock() < quantity) {
           throw new BusinessException("库存不足");
       }
       sku.setLockStock(sku.getLockStock() + quantity);
       sku.setStock(sku.getStock() - quantity);
       productSkuMapper.updateById(sku);
   }
   
   // 2. 释放库存
   @Transactional
   public void releaseStock(Long skuId, Integer quantity) {
       ProductSku sku = productSkuMapper.selectById(skuId);
       sku.setLockStock(sku.getLockStock() - quantity);
       sku.setStock(sku.getStock() + quantity);
       productSkuMapper.updateById(sku);
   }
   ```

### 4. 用户系统开发

1. **用户注册流程**：
   ```java
   // 1. 创建用户记录
   User user = new User();
   user.setUsername(username);
   user.setPassword(passwordEncoder.encode(password));
   user.setPhone(phone);
   userMapper.insert(user);
   
   // 2. 创建用户地址记录
   UserAddress address = new UserAddress();
   address.setUserId(user.getId());
   address.setIsDefault(1);
   userAddressMapper.insert(address);
   ```

2. **用户收藏管理**：
   ```java
   // 1. 添加收藏
   @Transactional
   public void addFavorite(Long userId, Long productId) {
       // 检查是否已收藏
       UserFavorite favorite = userFavoriteMapper.selectByUserIdAndProductId(userId, productId);
       if (favorite != null) {
           throw new BusinessException("已收藏该商品");
       }
       
       // 创建收藏记录
       favorite = new UserFavorite();
       favorite.setUserId(userId);
       favorite.setProductId(productId);
       userFavoriteMapper.insert(favorite);
   }
   ```

## 总结

WhaleTide电商系统数据库设计整体合理，表结构完整，关系清晰。权限管理系统采用RBAC模型，通过角色关联菜单、权限和资源，实现了完整的权限控制。各个业务模块的表结构设计合理，关系明确，能够满足电商系统的业务需求。

开发人员在进行开发时，需要：
1. 遵循数据库设计规范
2. 合理使用事务管理
3. 注意数据一致性
4. 考虑性能优化
5. 做好异常处理
6. 完善日志记录 