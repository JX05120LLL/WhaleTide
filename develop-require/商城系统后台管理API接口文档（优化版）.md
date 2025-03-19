# 商城系统后台管理API接口文档（优化版）

## 目录

- [1. 用户认证与管理接口](#1-用户认证与管理接口)
- [2. 产品管理接口](#2-产品管理接口)
- [3. 订单管理接口](#3-订单管理接口)
- [4. 营销管理接口](#4-营销管理接口)
- [5. 后端DTO设计建议](#5-后端dto设计建议)

## 1. 用户认证与管理接口

### 1.1 登录接口

- **URL**: `/admin/login`
- **方法**: POST
- **请求参数**:
  ```json
  {
    "username": "string", // 用户名
    "password": "string"  // 密码
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "string",
    "data": {
      "token": "string",      // 访问令牌
      "tokenHead": "string"   // 令牌前缀
    }
  }
  ```
- **后端DTO**: 需要创建 `AdminLoginParam` 用于接收登录请求参数，`AdminLoginResult` 用于返回登录结果

### 1.2 获取当前登录用户信息

- **URL**: `/admin/info`
- **方法**: GET
- **请求头**: 需要携带 Authorization 令牌
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "string",
    "data": {
      "username": "string",       // 用户名
      "roles": ["string"],        // 角色列表
      "menus": ["string"],        // 可访问菜单列表
      "icon": "string",           // 头像
      "permissions": ["string"]   // 权限列表
    }
  }
  ```
- **后端DTO**: 需要创建 `AdminInfoResult` 用于返回用户信息

### 1.3 登出

- **URL**: `/admin/logout`
- **方法**: POST
- **请求头**: 需要携带 Authorization 令牌
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "string",
    "data": null
  }
  ```

### 1.4 管理员列表（需要分页）

- **URL**: `/admin/list`
- **方法**: GET
- **请求参数**:
  ```
  keyword: string     // 搜索关键词
  pageNum: number     // 页码，必填
  pageSize: number    // 每页记录数，必填
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "string",
    "data": {
      "list": [
        {
          "id": "number",
          "username": "string",
          "email": "string",
          "createTime": "date",
          "status": "number",
          "note": "string"
        }
      ],
      "total": "number",
      "pageNum": "number",
      "pageSize": "number"
    }
  }
  ```
- **后端DTO**: 需要创建 `AdminListParam` 接收查询参数，`AdminResult` 返回管理员信息

### 1.5 创建管理员

- **URL**: `/admin/register`
- **方法**: POST
- **请求参数**:
  ```json
  {
    "username": "string",
    "password": "string",
    "email": "string",
    "note": "string",
    "status": "number"
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "string",
    "data": "number"  // 新创建的管理员ID
  }
  ```
- **后端DTO**: 需要创建 `AdminParam` 接收创建管理员的参数

### 1.6 更新管理员

- **URL**: `/admin/update/{id}`
- **方法**: POST
- **路径参数**: id - 管理员ID
- **请求参数**:
  ```json
  {
    "username": "string",
    "password": "string",
    "email": "string",
    "note": "string",
    "status": "number"
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "string",
    "data": "number"  // 更新成功的条数
  }
  ```
- **后端DTO**: 复用 `AdminParam` 接收更新管理员的参数

### 1.7 更新管理员状态

- **URL**: `/admin/updateStatus/{id}`
- **方法**: POST
- **路径参数**: id - 管理员ID
- **请求参数**:
  ```
  status: number  // 状态：0->禁用；1->启用
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "string",
    "data": "number"  // 更新成功的条数
  }
  ```
- **后端DTO**: 需要创建 `UpdateStatusParam` 接收状态更新参数

### 1.8 删除管理员

- **URL**: `/admin/delete/{id}`
- **方法**: POST
- **路径参数**: id - 管理员ID
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "string",
    "data": "number"  // 删除成功的条数
  }
  ```

### 1.9 获取管理员角色（不需要分页）

- **URL**: `/admin/role/{id}`
- **方法**: GET
- **路径参数**: id - 管理员ID
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "string",
    "data": [
      {
        "id": "number",
        "name": "string",
        "description": "string",
        "status": "number",
        "sort": "number",
        "checked": "boolean"  // 是否已分配该角色
      }
    ]
  }
  ```
- **后端DTO**: 需要创建 `RoleAssignResult` 返回角色分配信息

### 1.10 分配角色

- **URL**: `/admin/role/update`
- **方法**: POST
- **请求参数**:
  ```json
  {
    "adminId": "number",
    "roleIds": ["number"]
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "string",
    "data": "number"  // 更新成功的条数
  }
  ```
- **后端DTO**: 需要创建 `AdminRoleParam` 接收角色分配参数

## 2. 产品管理接口

### 2.1 产品列表（需要分页）

- **URL**: `/product/list`
- **方法**: GET
- **请求参数**:
  ```
  keyword: string          // 搜索关键词
  pageNum: number          // 页码，必填
  pageSize: number         // 每页记录数，必填
  publishStatus: number    // 上架状态，可选
  brandId: number          // 品牌ID，可选
  productCategoryId: number // 产品分类ID，可选
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "string",
    "data": {
      "list": [
        {
          "id": "number",
          "name": "string",
          "pic": "string",
          "price": "number",
          "sale": "number",
          "brandName": "string",
          "productCategoryName": "string",
          "publishStatus": "number",
          "newStatus": "number",
          "recommendStatus": "number"
        }
      ],
      "total": "number",
      "pageNum": "number",
      "pageSize": "number"
    }
  }
  ```
- **后端DTO**: 需要创建 `ProductQueryParam` 接收查询参数，`ProductListResult` 返回产品列表信息

### 2.2 简单产品列表（不需要分页）

- **URL**: `/product/simpleList`
- **方法**: GET
- **请求参数**:
  ```
  keyword: string  // 搜索关键词，可选
  ```
- **响应参数**:

  ```json
  {
    "code": 200,
    "message": "string",
    "data": [
      {
        "id": "number",
        "name": "string"
      }
    ]
  }
  ```
- **后端DTO**: 需要创建 `ProductSimpleResult` 返回简单产品信息

### 2.3 更新删除状态

- **URL**: `/product/update/deleteStatus`
- **方法**: POST
- **请求参数**:
  ```
  ids: string        // 产品ID，多个用逗号分隔，必填
  deleteStatus: number  // 删除状态：0->未删除；1->已删除，必填
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "string",
    "data": "number"  // 更新成功的条数
  }
  ```
- **后端DTO**: 需要创建 `UpdateDeleteStatusParam` 接收状态更新参数

### 2.4 更新新品状态

- **URL**: `/product/update/newStatus`
- **方法**: POST
- **请求参数**:
  ```
  ids: string     // 产品ID，多个用逗号分隔，必填
  newStatus: number  // 新品状态：0->不是新品；1->新品，必填
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "string",
    "data": "number"  // 更新成功的条数
  }
  ```
- **后端DTO**: 需要创建 `UpdateNewStatusParam` 接收状态更新参数

### 2.5 更新推荐状态

- **URL**: `/product/update/recommendStatus`
- **方法**: POST
- **请求参数**:
  ```
  ids: string             // 产品ID，多个用逗号分隔，必填
  recommendStatus: number  // 推荐状态：0->不推荐；1->推荐，必填
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "string",
    "data": "number"  // 更新成功的条数
  }
  ```
- **后端DTO**: 需要创建 `UpdateRecommendStatusParam` 接收状态更新参数

### 2.6 更新上架状态

- **URL**: `/product/update/publishStatus`
- **方法**: POST
- **请求参数**:
  ```
  ids: string          // 产品ID，多个用逗号分隔，必填
  publishStatus: number  // 上架状态：0->下架；1->上架，必填
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "string",
    "data": "number"  // 更新成功的条数
  }
  ```
- **后端DTO**: 需要创建 `UpdatePublishStatusParam` 接收状态更新参数

### 2.7 创建产品

- **URL**: `/product/create`
- **方法**: POST
- **请求参数**: 复杂对象，包含产品基本信息、规格参数、SKU库存等
  ```json
  {
    "productParam": {
      "name": "string",
      "productSn": "string",
      "brandId": "number",
      "productCategoryId": "number",
      "feightTemplateId": "number",
      "productAttributeCategoryId": "number",
      "price": "number",
      "originalPrice": "number",
      "stock": "number",
      "unit": "string",
      "weight": "number",
      "sort": "number",
      "description": "string",
      "albumPics": "string",
      "detailTitle": "string",
      "detailDesc": "string",
      "detailHtml": "string",
      "promotionType": "number",
      "keywords": "string",
      "note": "string",
      "promotionStartTime": "date",
      "promotionEndTime": "date",
      "promotionPerLimit": "number",
      "promotionPrice": "number"
    },
    "productAttributeValueList": [
      {
        "productAttributeId": "number",
        "value": "string"
      }
    ],
    "productFullReductionList": [
      {
        "fullPrice": "number",
        "reducePrice": "number"
      }
    ],
    "productLadderList": [
      {
        "count": "number",
        "discount": "number",
        "price": "number"
      }
    ],
    "skuStockList": [
      {
        "skuCode": "string",
        "price": "number",
        "stock": "number",
        "spData": "string"
      }
    ]
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "string",
    "data": "number"  // 新创建的产品ID
  }
  ```
- **后端DTO**: 需要创建 `ProductParam` 接收创建产品的参数，包含多个嵌套对象

### 2.8 更新产品

- **URL**: `/product/update/{id}`
- **方法**: POST
- **路径参数**: id - 产品ID
- **请求参数**: 与创建产品接口相同
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "string",
    "data": "number"  // 更新成功的条数
  }
  ```
- **后端DTO**: 复用 `ProductParam` 接收更新产品的参数

### 2.9 获取产品详情（不需要分页）

- **URL**: `/product/updateInfo/{id}`
- **方法**: GET
- **路径参数**: id - 产品ID
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "string",
    "data": {
      "productParam": {
        // 产品基本信息
      },
      "productAttributeValueList": [
        // 产品属性值列表
      ],
      "productFullReductionList": [
        // 满减价格列表
      ],
      "productLadderList": [
        // 阶梯价格列表
      ],
      "skuStockList": [
        // SKU库存列表
      ]
    }
  }
  ```
- **后端DTO**: 需要创建 `ProductResult` 返回产品详细信息

### 2.10 获取产品SKU（不需要分页）

- **URL**: `/product/sku/{id}`
- **方法**: GET
- **路径参数**: id - 产品ID
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "string",
    "data": {
      "skuStockList": [
        {
          "skuCode": "String",
          "price": "number",
          "stock": "number",
          "lowStock": "number"
        }
        // SKU列表
      ]
    }
  }
  ```
- **后端DTO**: 需要创建 `ProductSkuResult` 返回产品sku详细信息

## 3. 订单管理接口

### 3.1 订单列表（需要分页）

- **URL**: `/order/list`
- **方法**: GET
- **请求参数**:
  ```
  orderSn: string          // 订单编号，可选
  receiverKeyword: string  // 收货人关键词，可选
  status: number           // 订单状态，可选
  orderType: number        // 订单类型，可选
  sourceType: number       // 订单来源，可选
  createTime: string       // 提交时间，可选
  pageNum: number          // 页码，必填
  pageSize: number         // 每页记录数，必填
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "string",
    "data": {
      "list": [
        {
          "id": "number",
          "orderSn": "string",
          "createTime": "date",
          "memberUsername": "string",
          "totalAmount": "number",
          "payType": "number",
          "sourceType": "number",
          "status": "number",
          "receiverName": "string",
          "receiverPhone": "string"
        }
      ],
      "total": "number",
      "pageNum": "number",
      "pageSize": "number"
    }
  }
  ```
- **后端DTO**: 需要创建 `OrderQueryParam` 接收查询参数，`OrderResult` 返回订单列表信息

### 3.2 关闭订单

- **URL**: `/order/update/close`
- **方法**: POST
- **请求参数**:
  ```
  ids: string     // 订单ID，多个用逗号分隔，必填
  note: string    // 备注，可选
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "string",
    "data": "number"  // 关闭成功的条数
  }
  ```
- **后端DTO**: 需要创建 `CloseOrderParam` 接收关闭订单的参数

### 3.3 删除订单

- **URL**: `/order/delete`
- **方法**: POST
- **请求参数**:
  ```
  ids: string  // 订单ID，多个用逗号分隔，必填
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "string",
    "data": "number"  // 删除成功的条数
  }
  ```
- **后端DTO**: 需要创建 `DeleteOrderParam` 接收删除订单的参数

### 3.4 发货

- **URL**: `/order/update/delivery`
- **方法**: POST
- **请求参数**:
  ```json
  {
    "orderId": "number",  // 订单ID，必填
    "deliveryCompany": "string",  // 物流公司，必填
    "deliverySn": "string",  // 物流单号，必填
    "orderItemIds": ["number"]  // 发货商品项ID列表，必填
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "string",
    "data": "number"  // 发货成功的条数
  }
  ```
- **后端DTO**: 需要创建 `OrderDeliveryParam` 接收发货参数

### 3.5 订单详情（不需要分页）

- **URL**: `/order/{id}`
- **方法**: GET
- **路径参数**: id - 订单ID
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "string",
    "data": {
      "id": "number",
      "orderSn": "string",
      "createTime": "date",
      "memberUsername": "string",
      "totalAmount": "number",
      "payAmount": "number",
      "freightAmount": "number",
      "promotionAmount": "number",
      "integrationAmount": "number",
      "couponAmount": "number",
      "discountAmount": "number",
      "payType": "number",
      "sourceType": "number",
      "status": "number",
      "orderType": "number",
      "deliveryCompany": "string",
      "deliverySn": "string",
      "autoConfirmDay": "number",
      "integration": "number",
      "growth": "number",
      "promotionInfo": "string",
      "billType": "number",
      "billHeader": "string",
      "billContent": "string",
      "billReceiverPhone": "string",
      "billReceiverEmail": "string",
      "receiverName": "string",
      "receiverPhone": "string",
      "receiverPostCode": "string",
      "receiverProvince": "string",
      "receiverCity": "string",
      "receiverRegion": "string",
      "receiverDetailAddress": "string",
      "note": "string",
      "confirmStatus": "number",
      "deleteStatus": "number",
      "paymentTime": "date",
      "deliveryTime": "date",
      "receiveTime": "date",
      "commentTime": "date",
      "modifyTime": "date",
      "orderItemList": [
        {
          "id": "number",
          "orderId": "number",
          "orderSn": "string",
          "productId": "number",
          "productPic": "string",
          "productName": "string",
          "productBrand": "string",
          "productSn": "string",
          "productPrice": "number",
          "productQuantity": "number",
          "productSkuId": "number",
          "productSkuCode": "string",
          "productCategoryId": "number",
          "promotionName": "string",
          "promotionAmount": "number",
          "couponAmount": "number",
          "integrationAmount": "number",
          "realAmount": "number",
          "giftIntegration": "number",
          "giftGrowth": "number",
          "productAttr": "string"
        }
      ],
      "historyList": [
        {
          "id": "number",
          "orderId": "number",
          "operateMan": "string",
          "createTime": "date",
          "orderStatus": "number",
          "note": "string"
        }
      ]
    }
  }
  ```
- **后端DTO**: 需要创建 `OrderDetailResult` 返回订单详细信息

### 3.6 更新收货人信息

- **URL**: `/order/update/receiverInfo`
- **方法**: POST
- **请求参数**:
  ```json
  {
    "orderId": "number",              // 订单ID，必填
    "receiverName": "string",         // 收货人姓名，必填
    "receiverPhone": "string",        // 收货人电话，必填
    "receiverPostCode": "string",     // 收货人邮编，可选
    "receiverDetailAddress": "string", // 详细地址，必填
    "receiverProvince": "string",     // 省份，必填
    "receiverCity": "string",         // 城市，必填
    "receiverRegion": "string",       // 区域，必填
    "status": "number"                // 订单状态，可选
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "string",
    "data": "number"  // 更新成功的条数
  }
  ```
- **后端DTO**: 需要创建 `ReceiverInfoParam` 接收收货人信息更新参数

### 3.7 更新订单费用信息

- **URL**: `/order/update/moneyInfo`
- **方法**: POST
- **请求参数**:
  ```json
  {
    "orderId": "number",          // 订单ID，必填
    "freightAmount": "number",    // 运费金额，必填
    "discountAmount": "number",   // 折扣金额，必填
    "status": "number"            // 订单状态，可选
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "string",
    "data": "number"  // 更新成功的条数
  }
  ```
- **后端DTO**: 需要创建 `MoneyInfoParam` 接收费用信息更新参数

### 3.8 更新订单备注

- **URL**: `/order/update/note`
- **方法**: POST
- **请求参数**:
  ```
  id: number    // 订单ID，必填
  note: string  // 备注，必填
  status: number  // 订单状态，可选
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "string",
    "data": "number"  // 更新成功的条数
  }
  ```
- **后端DTO**: 需要创建 `OrderNoteParam` 接收订单备注更新参数

### 3.9 获取退货订单列表（需要分页）

- **URL**: `/order/return/list`
- **方法**: GET
- **请求参数**:
  ```
  pageNum: number          // 页码，必填
  pageSize: number         // 每页记录数，必填
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "string",
    "data": {
      "list": [
        {
          "id": "number",
          "orderId": "string",
          "createTime": "date",
          "memberUsername": "string",
          "returnAmount": "number",
          "status": "number",
          "handleTime": "date"
        }
      ],
      "total": "number",
      "pageNum": "number",
      "pageSize": "number"
    }
  }
  ```
- **后端DTO**: 需要创建 `OrderReturnResult` 返回订单列表信息

## 4. 营销管理接口

### 4.1 优惠券列表（需要分页）

- **URL**: `/coupon/list`
- **方法**: GET
- **请求参数**:
  ```
  name: string        // 优惠券名称，可选
  type: number        // 优惠券类型，可选
  pageNum: number     // 页码，必填
  pageSize: number    // 每页记录数，必填
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "string",
    "data": {
      "list": [
        {
          "id": "number",
          "name": "string",
          "type": "number",
          "amount": "number",
          "perLimit": "number",
          "minPoint": "number",
          "startTime": "date",
          "endTime": "date",
          "useType": "number",
          "count": "number",
          "useCount": "number"
        }
      ],
      "total": "number",
      "pageNum": "number",
      "pageSize": "number"
    }
  }
  ```
- **后端DTO**: 需要创建 `CouponQueryParam` 接收查询参数，`CouponResult` 返回优惠券列表信息

### 4.2 创建优惠券

- **URL**: `/coupon/create`
- **方法**: POST
- **请求参数**:
  ```json
  {
    "name": "string",           // 优惠券名称，必填
    "type": "number",           // 优惠券类型，必填
    "amount": "number",         // 金额，必填
    "perLimit": "number",       // 每人限领张数，必填
    "minPoint": "number",       // 使用门槛，必填
    "startTime": "date",        // 开始时间，必填
    "endTime": "date",          // 结束时间，必填
    "useType": "number",        // 使用类型，必填
    "note": "string",           // 备注，可选
    "count": "number",          // 总数量，必填
    "publishCount": "number",   // 发行数量，必填
    "productRelationList": [    // 商品关联列表，可选
      {
        "productId": "number"
      }
    ],
    "productCategoryRelationList": [  // 商品分类关联列表，可选
      {
        "productCategoryId": "number"
      }
    ]
  }
  ```
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "string",
    "data": "number"  // 新创建的优惠券ID
  }
  ```
- **后端DTO**: 需要创建 `CouponParam` 接收创建优惠券的参数

### 4.3 获取优惠券详情（不需要分页）

- **URL**: `/coupon/{id}`
- **方法**: GET
- **路径参数**: id - 优惠券ID
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "string",
    "data": {
      "id": "number",
      "name": "string",
      "type": "number",
      "amount": "number",
      "perLimit": "number",
      "minPoint": "number",
      "startTime": "date",
      "endTime": "date",
      "useType": "number",
      "note": "string",
      "count": "number",
      "useCount": "number",
      "productRelationList": [
        {
          "id": "number",
          "couponId": "number",
          "productId": "number",
          "productName": "string",
          "productSn": "string"
        }
      ],
      "productCategoryRelationList": [
        {
          "id": "number",
          "couponId": "number",
          "productCategoryId": "number",
          "productCategoryName": "string"
        }
      ]
    }
  }
  ```
- **后端DTO**: 需要创建 `CouponDetailResult` 返回优惠券详细信息

## 5. 商品分类管理接口

### 5.1 更新商品导航状态

- **URL**: `/productCategory/update/{id}/navStatus/{status}`
- **方法**: POST
- **路径参数**: id - 商品分类ID
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "string",
    "data": "number"  // 更新成功的条数
  }
  ```

### 6.2 更新商品显示状态

- **URL**: `/productCategory/update/{id}/showStatus/{status}`
- **方法**: POST
- **路径参数**: id - 商品分类ID
- **响应参数**:
  ```json
  {
    "code": 200,
    "message": "string",
    "data": "number"  // 更新成功的条数
  }
  ```

## 6. 后端DTO设计建议

### 6.1 通用响应DTO

所有接口应该使用统一的响应格式，建议创建通用响应类：

```java
/**
 * 通用响应对象
 */
public class CommonResult<T> {
    private long code;
    private String message;
    private T data;

    // 省略构造方法和getter/setter

    /**
     * 成功返回结果
     */
    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成功返回结果
     */
    public static <T> CommonResult<T> success(T data, String message) {
        return new CommonResult<T>(ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败返回结果
     */
    public static <T> CommonResult<T> failed(String message) {
        return new CommonResult<T>(ResultCode.FAILED.getCode(), message, null);
    }

    /**
     * 参数验证失败返回结果
     */
    public static <T> CommonResult<T> validateFailed(String message) {
        return new CommonResult<T>(ResultCode.VALIDATE_FAILED.getCode(), message, null);
    }

    /**
     * 未登录返回结果
     */
    public static <T> CommonResult<T> unauthorized(T data) {
        return new CommonResult<T>(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMessage(), data);
    }

    /**
     * 未授权返回结果
     */
    public static <T> CommonResult<T> forbidden(T data) {
        return new CommonResult<T>(ResultCode.FORBIDDEN.getCode(), ResultCode.FORBIDDEN.getMessage(), data);
    }
}
```

### 6.2 分页结果DTO

为分页查询结果创建通用分页结果类：

```java
/**
 * 分页数据封装类
 */
public class CommonPage<T> {
    private Integer pageNum;
    private Integer pageSize;
    private Integer totalPage;
    private Long total;
    private List<T> list;

    // 省略构造方法和getter/setter

    /**
     * 将MyBatis Plus分页后的对象转为分页信息
     */
    public static <T> CommonPage<T> restPage(Page<T> pageResult) {
        CommonPage<T> result = new CommonPage<T>();
        result.setTotalPage((int) Math.ceil((double) pageResult.getTotal() / pageResult.getSize()));
        result.setPageNum((int) pageResult.getCurrent());
        result.setPageSize((int) pageResult.getSize());
        result.setTotal(pageResult.getTotal());
        result.setList(pageResult.getRecords());
        return result;
    }
}
```

### 6.3 请求参数DTO建议

为每个接口创建对应的请求参数DTO，使用注解进行参数验证：

```java
/**
 * 订单查询参数
 */
@Data
public class OrderQueryParam {
    private String orderSn;
    private String receiverKeyword;
    private Integer status;
    private Integer orderType;
    private Integer sourceType;
    private String createTime;

    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码最小为1")
    private Integer pageNum;

    @NotNull(message = "每页条数不能为空")
    @Min(value = 1, message = "每页条数最小为1")
    @Max(value = 100, message = "每页条数最大为100")
    private Integer pageSize;
}

/**
 * 关闭订单参数
 */
@Data
public class CloseOrderParam {
    @NotEmpty(message = "订单ID不能为空")
    private String ids;
    private String note;
}
```

### 6.4 响应结果DTO建议

为每个接口创建对应的响应结果DTO，保持一致的命名规范：

```java
/**
 * 订单列表项结果
 */
@Data
public class OrderResult {
    private Long id;
    private String orderSn;
    private Date createTime;
    private String memberUsername;
    private BigDecimal totalAmount;
    private Integer payType;
    private Integer sourceType;
    private Integer status;
    private String receiverName;
    private String receiverPhone;
}

/**
 * 订单详细信息结果
 */
@Data
public class OrderDetailResult {
    // 订单基本信息
    private Long id;
    private String orderSn;
    private Date createTime;
    private String memberUsername;
    private BigDecimal totalAmount;
    // 其他订单属性...

    // 订单商品列表
    private List<OrderItem> orderItemList;
    // 订单操作历史列表
    private List<OrderOperateHistory> historyList;
}
```

### 6.5 后端实现优化建议

1. **参数验证**：
    - 所有必填参数应该添加 `@NotNull`, `@NotEmpty` 等验证注解
    - 对数值类型参数添加范围验证，如 `@Min`, `@Max`
    - 对字符串类型参数添加长度验证，如 `@Size`

2. **分页处理**：
    - 所有列表查询接口应明确是否需要分页
    - 需要分页的接口必须包含 `pageNum` 和 `pageSize` 参数
    - 使用MyBatis Plus的Page对象进行分页查询

3. **错误处理**：
    - 使用全局异常处理器捕获并统一处理各类异常
    - 对参数验证异常返回详细的错误信息
    - 对业务逻辑异常返回友好的错误提示

4. **日志记录**：
    - 关键操作（如订单状态变更）应记录操作日志
    - 记录操作人、操作时间、操作内容等信息
    - 设计合理的日志查询接口方便追溯

5. **接口性能**：
    - 对于大数据量的查询，应使用索引优化查询性能
    - 合理使用缓存减少数据库压力
    - 考虑使用异步处理方式处理耗时操作 