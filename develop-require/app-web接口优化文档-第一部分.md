# WhaleTide App-Web 接口文档（第一部分-优化版）

## 目录
1. [用户认证接口](#1-用户认证接口)
2. [商品相关接口](#2-商品相关接口)
3. [订单相关接口](#3-订单相关接口)

## 通用响应格式

所有API接口应返回统一的响应格式：

```json
{
  "code": 200,     // 状态码：200表示成功，其他表示失败
  "message": "操作成功", // 消息
  "data": {}       // 实际数据，可能是对象、数组或基本类型
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

## 1. 用户认证接口

### 1.1 用户登录

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

### 1.2 获取用户信息

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

### 1.3 用户注册

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

### 1.4 获取短信验证码

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

## 2. 商品相关接口

### 2.1 商品搜索

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
    // getter和setter
}
```

**分页**: 是，需要分页处理

### 2.2 获取商品分类树

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

### 2.3 获取商品详情

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

### 2.4 获取品牌详情

- **接口地址**: `/brand/detail/{id}`
- **请求方式**: GET
- **请求参数**: 
  - id: 品牌ID（路径参数）
- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "name": "string",
    "logo": "string",
    "bigPic": "string",
    "productCount": 0,
    "productCommentCount": 0,
    "description": "string"
  }
}
```

**建议DTO设计**:

```java
public class BrandDetailResponse {
    private Long id;
    private String name;
    private String logo;
    private String bigPic;
    private Integer productCount;
    private Integer productCommentCount;
    private String description;
    // getter和setter
}
```

**分页**: 否，详情接口无需分页

### 2.5 获取品牌商品列表

- **接口地址**: `/brand/productList`
- **请求方式**: GET
- **请求参数**:

```json
{
  "brandId": 1,    // 品牌ID
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
public class BrandProductRequest extends PageRequest {
    @NotNull(message = "品牌ID不能为空")
    private Long brandId;
    // getter和setter
}

// 响应
public class ProductListItemResponse {
    // 同上
}
```

**分页**: 是，需要分页处理

### 2.6 获取推荐品牌列表

- **接口地址**: `/brand/recommendList`
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
      "name": "string",
      "logo": "string",
      "productCount": 0
    }
  ]
}
```

**建议DTO设计**:
```java
// 请求
public class RecommendBrandRequest extends PageRequest {
    // 无需额外参数
}

// 响应
public class BrandListItemResponse {
    private Long id;
    private String name;
    private String logo;
    private Integer productCount;
    // getter和setter
}
```

**分页**: 是，需要分页处理

## 3. 订单相关接口

### 3.1 生成确认订单

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

### 3.2 生成订单

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

### 3.3 获取订单列表

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

### 3.4 支付成功回调

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

### 3.5 获取订单详情

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

### 3.6 取消订单

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

### 3.7 确认收货

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

### 3.8 删除订单

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

### 3.9 支付宝订单状态查询

- **接口地址**: `/alipay/query`
- **请求方式**: GET
- **请求参数**:

```json
{
  "orderId": 1,    // 订单ID
  "outTradeNo": "string" // 商户订单号
}
```

- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "tradeStatus": "string", // 交易状态：WAIT_BUYER_PAY/TRADE_CLOSED/TRADE_SUCCESS/TRADE_FINISHED
    "tradeNo": "string"      // 支付宝交易号
  }
}
```

**建议DTO设计**:
```java
// 请求
public class AlipayQueryRequest {
    private Long orderId;
    private String outTradeNo;
    // getter和setter
}

// 响应
public class AlipayQueryResponse {
    private String tradeStatus;
    private String tradeNo;
    // getter和setter
}
```

**分页**: 否，状态查询无需分页 