# WhaleTide App-Web 开发文档（优化版）

## 项目概述

WhaleTide App-Web 是一个基于 uni-app 开发的移动端电商应用，包含了商品浏览、购物车、订单管理、用户中心等常见电商功能模块。本文档详细记录了项目的页面结构和 API 接口信息，方便开发人员进行功能开发和维护。

## 目录
1. [项目结构](#项目结构)
2. [基础配置](#基础配置)
3. [通用响应格式](#通用响应格式)
4. [用户认证接口](#1-用户认证接口)
5. [商品相关接口](#2-商品相关接口)
6. [订单相关接口](#3-订单相关接口)
7. [购物车接口](#4-购物车接口)
8. [首页内容接口](#5-首页内容接口)
9. [会员相关接口](#6-会员相关接口)
   - [会员浏览历史](#61-会员浏览历史)
   - [商品收藏](#62-商品收藏)
   - [品牌关注](#63-品牌关注)
   - [支付相关](#64-支付相关)
10. [DTO封装策略](#建议的dto封装策略)
11. [分页接口汇总](#需要分页的接口汇总)
12. [分页参数统一规范](#分页参数统一规范)

## 项目结构

项目采用 uni-app 框架开发，主要目录结构如下：

- `pages`: 包含所有页面文件
- `api`: 包含所有 API 接口调用
- `components`: 通用组件
- `utils`: 工具函数，包含请求封装等
- `static`: 静态资源文件
- `store`: Vuex 状态管理

## 基础配置

### API 基础路径

API 基础路径配置在 `utils/appConfig.js` 文件中：

```js
export const API_BASE_URL = 'http://localhost:8085';
export const USE_ALIPAY = false; // 是否启用支付宝支付
```

### 请求工具

项目使用 `luch-request` 库进行 HTTP 请求，封装在 `utils/requestUtil.js` 中，主要功能包括：

- 请求拦截器：自动添加 token 到请求头
- 响应拦截器：统一处理响应状态码，包括 401 未登录状态的处理
- 错误处理：统一展示错误提示

## 通用响应格式

所有API接口应返回统一的响应格式：

```json
{
  "code": 200,     // 状态码：200表示成功，其他表示失败
  "message": "操作成功", // 消息
  "data": {}       // 实际数据，可能是对象、数组或基本类型
}
```

**建议DTO设计**：创建统一的响应对象封装类

```java
public class ApiResult<T> {
    private Integer code;
    private String message;
    private T data;
    
    // 静态工厂方法
    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(200, "操作成功", data);
    }
    
    public static <T> ApiResult<T> failed(String message) {
        return new ApiResult<>(500, message, null);
    }
    
    // 构造函数、getter和setter
}
```

## 分页请求和响应格式

对于需要分页的接口，请求参数应包含：

```json
{
  "pageNum": 1,    // 当前页码，从1开始
  "pageSize": 10   // 每页记录数
}
```

分页响应格式应为：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "list": [],     // 当前页数据列表
    "pageNum": 1,   // 当前页码
    "pageSize": 10, // 每页记录数
    "total": 100,   // 总记录数
    "totalPage": 10 // 总页数
  }
}
```

**建议DTO设计**：创建分页请求和响应对象

```java
// 分页请求参数
public class PageRequest {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    // getter和setter
}

// 分页响应结果
public class PageResult<T> {
    private List<T> list;
    private Integer pageNum;
    private Integer pageSize;
    private Long total;
    private Integer totalPage;
    // getter和setter
}
```

## API 接口文档

### 1. 用户认证接口

#### 1.1 用户登录

- **接口地址**: `/sso/login`
- **请求方式**: POST
- **Content-Type**: application/x-www-form-urlencoded
- **请求参数**:

```json
{
  "username": "string", // 用户名/手机号
  "password": "string"  // 密码
}
```

- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "token": "string",      // JWT令牌
    "tokenHead": "Bearer "  // 令牌头部
  }
}
```

**建议DTO设计**:
```java
// 请求
public class LoginRequest {
    @NotEmpty(message = "用户名不能为空")
    private String username;
    
    @NotEmpty(message = "密码不能为空")
    private String password;
    // getter和setter
}

// 响应
public class LoginResponse {
    private String token;
    private String tokenHead;
    // getter和setter
}
```

#### 1.2 获取用户信息

- **接口地址**: `/sso/info`
- **请求方式**: GET
- **请求参数**: 无 (通过请求头传递 token)
- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "username": "string",
    "nickname": "string",
    "icon": "string",
    "integration": 0,
    "growth": 0
  }
}
```

**建议DTO设计**:
```java
public class UserInfoResponse {
    private Long id;
    private String username;
    private String nickname;
    private String icon;
    private Integer integration; // 积分
    private Integer growth;      // 成长值
    // getter和setter
}
```

#### 1.3 用户注册

- **接口地址**: `/sso/register`
- **请求方式**: POST
- **请求参数**:

```json
{
  "mobile": "string",  // 手机号
  "code": "string",    // 短信验证码
  "password": "string" // 密码
}
```

- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

**建议DTO设计**:
```java
public class RegisterRequest {
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String mobile;
    
    @NotEmpty(message = "验证码不能为空")
    private String code;
    
    @Pattern(regexp = "^[a-zA-Z0-9]{8,18}$", message = "密码必须为8-18位的字母或数字组合")
    private String password;
    // getter和setter
}
```

#### 1.4 获取短信验证码

- **接口地址**: `/sso/sms/code`
- **请求方式**: GET
- **请求参数**:

```json
{
  "mobile": "string" // 手机号
}
```

- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

**建议DTO设计**:
```java
public class SmsCodeRequest {
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String mobile;
    // getter和setter
}
```

### 2. 商品相关接口

#### 2.1 商品搜索

- **接口地址**: `/product/search`
- **请求方式**: GET
- **请求参数**:

```json
{
  "keyword": "string",   // 搜索关键词
  "brandId": 0,          // 品牌ID
  "productCategoryId": 0, // 商品分类ID
  "sort": "string",      // 排序字段
  "order": "string",     // 排序方式（asc/desc）
  "pageNum": 1,          // 当前页码
  "pageSize": 10         // 每页记录数
}
```

- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "list": [
      {
        "id": 1,
        "name": "string",
        "pic": "string",
        "price": 0.0,
        "sale": 0,
        "subTitle": "string"
      }
    ],
    "pageNum": 1,
    "pageSize": 10,
    "total": 100,
    "totalPage": 10
  }
}
```

**建议DTO设计**:
```java
// 请求
public class ProductSearchRequest extends PageRequest {
    private String keyword;
    private Long brandId;
    private Long productCategoryId;
    private String sort;
    private String order;
    // getter和setter
}

// 响应
public class ProductListItemResponse {
    private Long id;
    private String name;
    private String pic;
    private BigDecimal price;
    private Integer sale;
    private String subTitle;
    // getter和setter
}
```

**分页**: 是，需要分页处理

#### 2.2 获取商品分类树

- **接口地址**: `/product/categoryTreeList`
- **请求方式**: GET
- **请求参数**: 无
- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "name": "string",
      "level": 0,
      "children": [
        {
          "id": 2,
          "name": "string",
          "level": 1,
          "children": []
        }
      ]
    }
  ]
}
```

**建议DTO设计**:
```java
public class CategoryTreeResponse {
    private Long id;
    private String name;
    private Integer level;
    private List<CategoryTreeResponse> children;
    // getter和setter
}
```

**分页**: 否，树形结构无需分页

#### 2.3 获取商品详情

- **接口地址**: `/product/detail/{id}`
- **请求方式**: GET
- **请求参数**: 
  - id: 商品ID（路径参数）
- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "name": "string",
    "pic": "string",
    "pics": ["string"],
    "price": 0.0,
    "originalPrice": 0.0,
    "sale": 0,
    "stock": 0,
    "brand": {
      "id": 1,
      "name": "string"
    },
    "categoryName": "string",
    "attributes": [
      {
        "name": "string",
        "value": "string"
      }
    ],
    "description": "string"
  }
}
```

**建议DTO设计**:
```java
public class ProductDetailResponse {
    private Long id;
    private String name;
    private String pic;
    private List<String> pics;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer sale;
    private Integer stock;
    private BrandInfo brand;
    private String categoryName;
    private List<ProductAttribute> attributes;
    private String description;
    
    // 内部类
    @Data
    public static class BrandInfo {
        private Long id;
        private String name;
    }
    
    @Data
    public static class ProductAttribute {
        private String name;
        private String value;
    }
    // getter和setter
}
```

**分页**: 否，详情接口无需分页

### 3. 订单相关接口

#### 3.1 生成确认订单

- **接口地址**: `/order/generateConfirmOrder`
- **请求方式**: POST
- **请求参数**:

```json
{
  "cartIds": [1, 2],  // 购物车项ID列表
  "addressId": 1     // 收货地址ID，可选
}
```

- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "cartItems": [
      {
        "id": 1,
        "productId": 1,
        "productName": "string",
        "productPic": "string",
        "price": 0.0,
        "quantity": 1,
        "productAttr": "string"
      }
    ],
    "memberReceiveAddressList": [
      {
        "id": 1,
        "name": "string",
        "phoneNumber": "string",
        "defaultStatus": 0,
        "province": "string",
        "city": "string",
        "district": "string",
        "detailAddress": "string"
      }
    ],
    "couponList": [
      {
        "id": 1,
        "name": "string",
        "amount": 0.0,
        "minPoint": 0.0,
        "startTime": "2023-03-15",
        "endTime": "2023-03-31",
        "useType": 0
      }
    ],
    "calcAmount": {
      "totalAmount": 0.0,
      "freightAmount": 0.0,
      "promotionAmount": 0.0,
      "couponAmount": 0.0,
      "payAmount": 0.0
    }
  }
}
```

**建议DTO设计**:
```java
// 请求
public class GenerateConfirmOrderRequest {
    private List<Long> cartIds;
    private Long addressId;
    // getter和setter
}

// 响应
public class ConfirmOrderResponse {
    private List<CartItemResponse> cartItems;
    private List<AddressResponse> memberReceiveAddressList;
    private List<CouponResponse> couponList;
    private CalcAmountResponse calcAmount;
    
    // 内部类
    @Data
    public static class CartItemResponse {
        private Long id;
        private Long productId;
        private String productName;
        private String productPic;
        private BigDecimal price;
        private Integer quantity;
        private String productAttr;
    }
    
    @Data
    public static class AddressResponse {
        private Long id;
        private String name;
        private String phoneNumber;
        private Integer defaultStatus;
        private String province;
        private String city;
        private String district;
        private String detailAddress;
    }
    
    @Data
    public static class CouponResponse {
        private Long id;
        private String name;
        private BigDecimal amount;
        private BigDecimal minPoint;
        private Date startTime;
        private Date endTime;
        private Integer useType;
    }
    
    @Data
    public static class CalcAmountResponse {
        private BigDecimal totalAmount;
        private BigDecimal freightAmount;
        private BigDecimal promotionAmount;
        private BigDecimal couponAmount;
        private BigDecimal payAmount;
    }
    // getter和setter
}
```

**分页**: 否，订单确认无需分页

#### 3.2 生成订单

- **接口地址**: `/order/generateOrder`
- **请求方式**: POST
- **请求参数**:

```json
{
  "addressId": 1,     // 收货地址ID
  "couponId": 1,      // 优惠券ID，可选
  "payType": 0,       // 支付方式：0->未支付；1->支付宝；2->微信
  "memberMessage": "string", // 订单备注
  "cartIds": [1, 2]   // 购物车项ID列表
}
```

- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "orderId": 1,      // 订单ID
    "payAmount": 0.0,  // 应付金额
    "payType": 0       // 支付方式
  }
}
```

**建议DTO设计**:
```java
// 请求
public class GenerateOrderRequest {
    @NotNull(message = "收货地址不能为空")
    private Long addressId;
    
    private Long couponId;
    
    @NotNull(message = "支付方式不能为空")
    private Integer payType;
    
    private String memberMessage;
    
    @NotEmpty(message = "购物车项不能为空")
    private List<Long> cartIds;
    // getter和setter
}

// 响应
public class GenerateOrderResponse {
    private Long orderId;
    private BigDecimal payAmount;
    private Integer payType;
    // getter和setter
}
```

**分页**: 否，订单生成无需分页

#### 3.3 获取订单列表

- **接口地址**: `/order/list`
- **请求方式**: GET
- **请求参数**:

```json
{
  "status": 0,     // 订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单
  "pageNum": 1,    // 当前页码
  "pageSize": 10   // 每页记录数
}
```

- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "list": [
      {
        "id": 1,
        "orderSn": "string",
        "createTime": "2023-03-15 12:00:00",
        "payAmount": 0.0,
        "status": 0,
        "orderItemList": [
          {
            "id": 1,
            "productId": 1,
            "productName": "string",
            "productPic": "string",
            "productPrice": 0.0,
            "productQuantity": 1,
            "productAttr": "string"
          }
        ]
      }
    ],
    "pageNum": 1,
    "pageSize": 10,
    "total": 100,
    "totalPage": 10
  }
}
```

**建议DTO设计**:
```java
// 请求
public class OrderListRequest extends PageRequest {
    private Integer status;
    // getter和setter
}

// 响应
public class OrderListResponse {
    private Long id;
    private String orderSn;
    private Date createTime;
    private BigDecimal payAmount;
    private Integer status;
    private List<OrderItemResponse> orderItemList;
    
    @Data
    public static class OrderItemResponse {
        private Long id;
        private Long productId;
        private String productName;
        private String productPic;
        private BigDecimal productPrice;
        private Integer productQuantity;
        private String productAttr;
    }
    // getter和setter
}
```

**分页**: 是，需要分页处理

#### 3.4 支付成功回调

- **接口地址**: `/order/paySuccess`
- **请求方式**: POST
- **Content-Type**: application/x-www-form-urlencoded
- **请求参数**:

```json
{
  "orderId": 1,    // 订单ID
  "payType": 1     // 支付方式：1->支付宝；2->微信
}
```

- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

**建议DTO设计**:
```java
public class PaySuccessRequest {
    @NotNull(message = "订单ID不能为空")
    private Long orderId;
    
    @NotNull(message = "支付方式不能为空")
    private Integer payType;
    // getter和setter
}
```

**分页**: 否，支付回调无需分页

#### 3.5 获取订单详情

- **接口地址**: `/order/detail/{orderId}`
- **请求方式**: GET
- **请求参数**: 
  - orderId: 订单ID（路径参数）
- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "orderSn": "string",
    "createTime": "2023-03-15 12:00:00",
    "payAmount": 0.0,
    "status": 0,
    "memberMessage": "string",
    "payType": 0,
    "receiverName": "string",
    "receiverPhone": "string",
    "receiverAddress": "string",
    "orderItemList": [
      {
        "id": 1,
        "productId": 1,
        "productName": "string",
        "productPic": "string",
        "productPrice": 0.0,
        "productQuantity": 1,
        "productAttr": "string"
      }
    ]
  }
}
```

**建议DTO设计**:
```java
public class OrderDetailResponse {
    private Long id;
    private String orderSn;
    private Date createTime;
    private BigDecimal payAmount;
    private Integer status;
    private String memberMessage;
    private Integer payType;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private List<OrderItemResponse> orderItemList;
    
    @Data
    public static class OrderItemResponse {
        private Long id;
        private Long productId;
        private String productName;
        private String productPic;
        private BigDecimal productPrice;
        private Integer productQuantity;
        private String productAttr;
    }
    // getter和setter
}
```

**分页**: 否，详情接口无需分页

#### 3.6 取消订单

- **接口地址**: `/order/cancelUserOrder`
- **请求方式**: POST
- **Content-Type**: application/x-www-form-urlencoded
- **请求参数**:

```json
{
  "orderId": 1 // 订单ID
}
```

- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

**建议DTO设计**:
```java
public class CancelOrderRequest {
    @NotNull(message = "订单ID不能为空")
    private Long orderId;
    // getter和setter
}
```

**分页**: 否，取消订单无需分页

#### 3.7 确认收货

- **接口地址**: `/order/confirmReceiveOrder`
- **请求方式**: POST
- **Content-Type**: application/x-www-form-urlencoded
- **请求参数**:

```json
{
  "orderId": 1 // 订单ID
}
```

- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

**建议DTO设计**:
```java
public class ConfirmReceiveRequest {
    @NotNull(message = "订单ID不能为空")
    private Long orderId;
    // getter和setter
}
```

**分页**: 否，确认收货无需分页

#### 3.8 删除订单

- **接口地址**: `/order/deleteOrder`
- **请求方式**: POST
- **Content-Type**: application/x-www-form-urlencoded
- **请求参数**:

```json
{
  "orderId": 1 // 订单ID
}
```

- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

**建议DTO设计**:
```java
public class DeleteOrderRequest {
    @NotNull(message = "订单ID不能为空")
    private Long orderId;
    // getter和setter
}
```

**分页**: 否，删除订单无需分页

### 4. 购物车相关接口

#### 4.1 添加购物车

- **接口地址**: `/cart/add`
- **请求方式**: POST
- **请求参数**:

```json
{
  "productId": 1,       // 商品ID
  "quantity": 1,        // 数量
  "productAttr": "string" // 商品规格属性
}
```

- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": 1 // 购物车项ID
}
```

**建议DTO设计**:
```java
// 请求
public class AddCartItemRequest {
    @NotNull(message = "商品ID不能为空")
    private Long productId;
    
    @Min(value = 1, message = "商品数量必须大于0")
    private Integer quantity;
    
    private String productAttr;
    // getter和setter
}
```

**分页**: 否，添加购物车无需分页

#### 4.2 获取购物车列表

- **接口地址**: `/cart/list`
- **请求方式**: GET
- **请求参数**: 无
- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "productId": 1,
      "productName": "string",
      "productPic": "string",
      "price": 0.0,
      "quantity": 1,
      "productAttr": "string",
      "checked": true
    }
  ]
}
```

**建议DTO设计**:
```java
public class CartItemResponse {
    private Long id;
    private Long productId;
    private String productName;
    private String productPic;
    private BigDecimal price;
    private Integer quantity;
    private String productAttr;
    private Boolean checked;
    // getter和setter
}
```

**分页**: 否，购物车列表通常不需要分页

#### 4.3 删除购物车商品

- **接口地址**: `/cart/delete`
- **请求方式**: POST
- **请求参数**:

```json
{
  "ids": [1, 2] // 购物车项ID列表
}
```

- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

**建议DTO设计**:
```java
public class DeleteCartItemRequest {
    @NotEmpty(message = "购物车项ID不能为空")
    private List<Long> ids;
    // getter和setter
}
```

**分页**: 否，删除购物车无需分页

#### 4.4 更新商品数量

- **接口地址**: `/cart/update/quantity`
- **请求方式**: GET
- **请求参数**:

```json
{
  "id": 1,       // 购物车项ID
  "quantity": 2  // 更新的数量
}
```

- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

**建议DTO设计**:
```java
public class UpdateCartQuantityRequest {
    @NotNull(message = "购物车项ID不能为空")
    private Long id;
    
    @Min(value = 1, message = "商品数量必须大于0")
    private Integer quantity;
    // getter和setter
}
```

**分页**: 否，更新数量无需分页

#### 4.5 清空购物车

- **接口地址**: `/cart/clear`
- **请求方式**: POST
- **请求参数**: 无
- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

**分页**: 否，清空购物车无需分页

### 5. 首页内容接口

#### 5.1 获取首页内容

- **接口地址**: `/home/content`
- **请求方式**: GET
- **请求参数**: 无
- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "advertiseList": [
      {
        "id": 1,
        "pic": "string",
        "url": "string"
      }
    ],
    "brandList": [
      {
        "id": 1,
        "name": "string",
        "logo": "string"
      }
    ],
    "newProductList": [
      {
        "id": 1,
        "name": "string",
        "pic": "string",
        "price": 0.0
      }
    ],
    "hotProductList": [
      {
        "id": 1,
        "name": "string",
        "pic": "string",
        "price": 0.0
      }
    ]
  }
}
```

**建议DTO设计**:
```java
public class HomeContentResponse {
    private List<AdvertiseResponse> advertiseList;
    private List<BrandResponse> brandList;
    private List<ProductResponse> newProductList;
    private List<ProductResponse> hotProductList;
    
    @Data
    public static class AdvertiseResponse {
        private Long id;
        private String pic;
        private String url;
    }
    
    @Data
    public static class BrandResponse {
        private Long id;
        private String name;
        private String logo;
    }
    
    @Data
    public static class ProductResponse {
        private Long id;
        private String name;
        private String pic;
        private BigDecimal price;
    }
    // getter和setter
}
```

**分页**: 否，首页内容无需分页

#### 5.2 获取热门商品列表

- **接口地址**: `/home/hotProductList`
- **请求方式**: GET
- **请求参数**:

```json
{
  "pageNum": 1,
  "pageSize": 6
}
```

- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "name": "string",
      "pic": "string",
      "price": 0.0,
      "sale": 0,
      "subTitle": "string"
    }
  ]
}
```

**建议DTO设计**:
```java
// 请求
public class HotProductRequest extends PageRequest {
    // 无需额外参数
}

// 响应
public class ProductListItemResponse {
    private Long id;
    private String name;
    private String pic;
    private BigDecimal price;
    private Integer sale;
    private String subTitle;
    // getter和setter
}
```

**分页**: 是，需要分页处理

## 6. 会员相关接口

### 6.1 会员浏览历史

#### 6.1.1 创建浏览历史

- **接口地址**: `/member/readHistory/create`
- **请求方式**: POST
- **Content-Type**: application/json
- **请求参数**:

```json
{
  "productId": 1, // 商品ID
  "productName": "string", // 商品名称
  "productPic": "string", // 商品图片
  "productSubTitle": "string", // 商品副标题
  "productPrice": 0.0 // 商品价格
}
```

- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

**建议DTO设计**:
```java
// 请求
public class CreateReadHistoryRequest {
    @NotNull(message = "商品ID不能为空")
    private Long productId;
    
    @NotBlank(message = "商品名称不能为空")
    private String productName;
    
    private String productPic;
    private String productSubTitle;
    private BigDecimal productPrice;
    // getter和setter
}
```

**分页**: 否，创建操作无需分页

#### 6.1.2 获取浏览历史列表

- **接口地址**: `/member/readHistory/list`
- **请求方式**: GET
- **请求参数**:

```json
{
  "pageNum": 1,
  "pageSize": 10
}
```

- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "productId": 1,
      "productName": "string",
      "productPic": "string",
      "productSubTitle": "string",
      "productPrice": 0.0,
      "createTime": "2023-03-15 12:00:00"
    }
  ]
}
```

**建议DTO设计**:
```java
// 请求
public class ReadHistoryListRequest extends PageRequest {
    // 无需额外参数
}

// 响应
public class ReadHistoryResponse {
    private Long id;
    private Long productId;
    private String productName;
    private String productPic;
    private String productSubTitle;
    private BigDecimal productPrice;
    private Date createTime;
    // getter和setter
}
```

**分页**: 是，需要分页处理

#### 6.1.3 清空浏览历史

- **接口地址**: `/member/readHistory/clear`
- **请求方式**: POST
- **请求参数**: 无

- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

**分页**: 否，清空操作无需分页

### 6.2 商品收藏

#### 6.2.1 添加商品收藏

- **接口地址**: `/member/productCollection/add`
- **请求方式**: POST
- **Content-Type**: application/json
- **请求参数**:

```json
{
  "productId": 1, // 商品ID
  "productName": "string", // 商品名称
  "productPic": "string", // 商品图片
  "productSubTitle": "string", // 商品副标题
  "productPrice": 0.0 // 商品价格
}
```

- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

**建议DTO设计**:
```java
// 请求
public class CreateProductCollectionRequest {
    @NotNull(message = "商品ID不能为空")
    private Long productId;
    
    @NotBlank(message = "商品名称不能为空")
    private String productName;
    
    private String productPic;
    private String productSubTitle;
    private BigDecimal productPrice;
    // getter和setter
}
```

**分页**: 否，添加操作无需分页

#### 6.2.2 删除商品收藏

- **接口地址**: `/member/productCollection/delete`
- **请求方式**: POST
- **Content-Type**: application/x-www-form-urlencoded
- **请求参数**:

```json
{
  "productId": 1 // 商品ID
}
```

- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

**建议DTO设计**:
```java
// 请求
public class DeleteProductCollectionRequest {
    @NotNull(message = "商品ID不能为空")
    private Long productId;
    // getter和setter
}
```

**分页**: 否，删除操作无需分页

#### 6.2.3 获取商品收藏列表

- **接口地址**: `/member/productCollection/list`
- **请求方式**: GET
- **请求参数**:

```json
{
  "pageNum": 1,
  "pageSize": 10
}
```

- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "productId": 1,
      "productName": "string",
      "productPic": "string",
      "productSubTitle": "string",
      "productPrice": 0.0,
      "createTime": "2023-03-15 12:00:00"
    }
  ]
}
```

**建议DTO设计**:
```java
// 请求
public class ProductCollectionListRequest extends PageRequest {
    // 无需额外参数
}

// 响应
public class ProductCollectionResponse {
    private Long id;
    private Long productId;
    private String productName;
    private String productPic;
    private String productSubTitle;
    private BigDecimal productPrice;
    private Date createTime;
    // getter和setter
}
```

**分页**: 是，需要分页处理

#### 6.2.4 获取商品收藏详情

- **接口地址**: `/member/productCollection/detail`
- **请求方式**: GET
- **请求参数**:

```json
{
  "productId": 1 // 商品ID
}
```

- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "productId": 1,
    "productName": "string",
    "productPic": "string",
    "productSubTitle": "string",
    "productPrice": 0.0,
    "createTime": "2023-03-15 12:00:00"
  }
}
```

**建议DTO设计**:
```java
// 请求
public class ProductCollectionDetailRequest {
    @NotNull(message = "商品ID不能为空")
    private Long productId;
    // getter和setter
}

// 响应
public class ProductCollectionDetailResponse {
    private Long id;
    private Long productId;
    private String productName;
    private String productPic;
    private String productSubTitle;
    private BigDecimal productPrice;
    private Date createTime;
    // getter和setter
}
```

**分页**: 否，详情操作无需分页

#### 6.2.5 清空商品收藏

- **接口地址**: `/member/productCollection/clear`
- **请求方式**: POST
- **请求参数**: 无

- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

**分页**: 否，清空操作无需分页

### 6.3 品牌关注

#### 6.3.1 添加品牌关注

- **接口地址**: `/member/attention/add`
- **请求方式**: POST
- **Content-Type**: application/json
- **请求参数**:

```json
{
  "brandId": 1, // 品牌ID
  "brandName": "string", // 品牌名称
  "brandLogo": "string", // 品牌logo
  "brandCity": "string" // 品牌所在城市
}
```

- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

**建议DTO设计**:
```java
// 请求
public class CreateBrandAttentionRequest {
    @NotNull(message = "品牌ID不能为空")
    private Long brandId;
    
    @NotBlank(message = "品牌名称不能为空")
    private String brandName;
    
    private String brandLogo;
    private String brandCity;
    // getter和setter
}
```

**分页**: 否，添加操作无需分页

#### 6.3.2 删除品牌关注

- **接口地址**: `/member/attention/delete`
- **请求方式**: POST
- **Content-Type**: application/x-www-form-urlencoded
- **请求参数**:

```json
{
  "brandId": 1 // 品牌ID
}
```

- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

**建议DTO设计**:
```java
// 请求
public class DeleteBrandAttentionRequest {
    @NotNull(message = "品牌ID不能为空")
    private Long brandId;
    // getter和setter
}
```

**分页**: 否，删除操作无需分页

#### 6.3.3 获取品牌关注列表

- **接口地址**: `/member/attention/list`
- **请求方式**: GET
- **请求参数**:

```json
{
  "pageNum": 1,
  "pageSize": 10
}
```

- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "brandId": 1,
      "brandName": "string",
      "brandLogo": "string",
      "brandCity": "string",
      "createTime": "2023-03-15 12:00:00"
    }
  ]
}
```

**建议DTO设计**:
```java
// 请求
public class BrandAttentionListRequest extends PageRequest {
    // 无需额外参数
}

// 响应
public class BrandAttentionResponse {
    private Long id;
    private Long brandId;
    private String brandName;
    private String brandLogo;
    private String brandCity;
    private Date createTime;
    // getter和setter
}
```

**分页**: 是，需要分页处理

#### 6.3.4 获取品牌关注详情

- **接口地址**: `/member/attention/detail`
- **请求方式**: GET
- **请求参数**:

```json
{
  "brandId": 1 // 品牌ID
}
```

- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "brandId": 1,
    "brandName": "string",
    "brandLogo": "string",
    "brandCity": "string",
    "createTime": "2023-03-15 12:00:00"
  }
}
```

**建议DTO设计**:
```java
// 请求
public class BrandAttentionDetailRequest {
    @NotNull(message = "品牌ID不能为空")
    private Long brandId;
    // getter和setter
}

// 响应
public class BrandAttentionDetailResponse {
    private Long id;
    private Long brandId;
    private String brandName;
    private String brandLogo;
    private String brandCity;
    private Date createTime;
    // getter和setter
}
```

**分页**: 否，详情操作无需分页

#### 6.3.5 清空品牌关注

- **接口地址**: `/member/attention/clear`
- **请求方式**: POST
- **请求参数**: 无

- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

**分页**: 否，清空操作无需分页

### 6.4 支付相关

#### 6.4.1 订单支付成功

- **接口地址**: `/order/payOrderSuccess`
- **请求方式**: POST
- **Content-Type**: application/json
- **请求参数**:

```json
{
  "orderId": 1, // 订单ID
  "payType": 1 // 支付类型：1-支付宝，2-微信
}
```

- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

**建议DTO设计**:
```java
// 请求
public class PayOrderSuccessRequest {
    @NotNull(message = "订单ID不能为空")
    private Long orderId;
    
    @NotNull(message = "支付类型不能为空")
    private Integer payType;
    // getter和setter
}
```

**分页**: 否，支付操作无需分页

#### 6.4.2 支付宝网页支付

- **接口地址**: `/alipay/webPay`
- **请求方式**: GET
- **请求参数**:

```json
{
  "outTradeNo": "string", // 订单编号
  "subject": "string", // 订单主题
  "totalAmount": 0.0 // 订单金额
}
```

- **响应参数**: 直接返回支付宝支付页面HTML内容

**建议DTO设计**:
```java
// 请求
public class AlipayWebPayRequest {
    @NotBlank(message = "订单编号不能为空")
    private String outTradeNo;
    
    @NotBlank(message = "订单主题不能为空")
    private String subject;
    
    @NotNull(message = "订单金额不能为空")
    private BigDecimal totalAmount;
    // getter和setter
}
```

**分页**: 否，支付操作无需分页

## 建议的DTO封装策略

1. **基础层次接口封装**：
   - 请求参数DTO：xxxRequest
   - 响应参数DTO：xxxResponse
   - 分页请求基类：PageRequest
   - 分页响应封装：PageResult<T>
   - 通用响应封装：ApiResult<T>

2. **DTO命名规范**：
   - 接口名 + Request/Response
   - 简单、清晰、见名知意

3. **DTO字段验证**：
   - 使用JSR-303注解进行参数验证
   - @NotNull、@NotEmpty、@Min、@Max、@Pattern等

4. **DTO转换**：
   - 建议使用MapStruct等工具实现DTO与实体类的转换
   - 控制层只操作DTO，不直接操作实体类

## 需要分页的接口汇总

1. 商品搜索接口: `/product/search`
2. 获取订单列表接口: `/order/list`
3. 获取热门商品列表: `/home/hotProductList`
4. 获取新品列表: `/home/newProductList`
5. 获取推荐商品列表: `/home/recommendProductList`
6. 获取品牌商品列表: `/brand/productList`
7. 获取用户浏览历史列表: `/member/readHistory/list`
8. 获取用户商品收藏列表: `/member/productCollection/list`
9. 获取用户品牌关注列表: `/member/attention/list`
10. 获取会员优惠券列表: `/member/coupon/list`

## 分页参数统一规范

所有需要分页的接口统一使用以下参数：

```json
{
  "pageNum": 1,    // 当前页码，从1开始
  "pageSize": 10   // 每页记录数
}
```

前端分页处理建议：
1. 初始页码从1开始
2. 下拉刷新时重置页码为1
3. 上拉加载更多时页码加1
4. 根据返回数据量判断是否还有更多数据 