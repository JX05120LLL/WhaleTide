# WhaleTide App-Web 接口文档（第二部分-优化版）

## 目录
1. [购物车相关接口](#1-购物车相关接口)
2. [收货地址相关接口](#2-收货地址相关接口)
3. [优惠券相关接口](#3-优惠券相关接口)
4. [首页内容相关接口](#4-首页内容相关接口)
5. [搜索历史相关接口](#5-搜索历史相关接口)
6. [会员积分相关接口](#6-会员积分相关接口)

## 1. 购物车相关接口

### 1.1 添加商品到购物车

- **接口地址**: `/cart/add`
- **请求方式**: POST
- **Content-Type**: application/json
- **请求参数**:

```json
{
  "productId": 1,     // 商品ID
  "productSkuId": 1,  // 商品SKU ID
  "quantity": 1       // 数量
}
```

- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": 1  // 购物车中商品数量
}
```

**建议DTO设计**:
```java
public class CartAddRequest {
    @NotNull(message = "商品ID不能为空")
    private Long productId;
    
    @NotNull(message = "商品SKU ID不能为空")
    private Long productSkuId;
    
    @NotNull(message = "数量不能为空")
    @Min(value = 1, message = "数量必须大于0")
    private Integer quantity;
    // getter和setter
}
```

**分页**: 否，添加购物车无需分页

### 1.2 获取购物车列表

- **接口地址**: `/cart/list`
- **请求方式**: GET
- **请求参数**: 无
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
        "productSkuId": 1,
        "productName": "string",
        "productPic": "string",
        "price": 0.0,
        "quantity": 1,
        "productAttr": "string",
        "checked": 1
      }
    ],
    "totalAmount": 0.0,
    "promotionAmount": 0.0,
    "payAmount": 0.0
  }
}
```

**建议DTO设计**:
```java
public class CartListResponse {
    private List<CartItemResponse> cartItems;
    private BigDecimal totalAmount;
    private BigDecimal promotionAmount;
    private BigDecimal payAmount;
    
    @Data
    public static class CartItemResponse {
        private Long id;
        private Long productId;
        private Long productSkuId;
        private String productName;
        private String productPic;
        private BigDecimal price;
        private Integer quantity;
        private String productAttr;
        private Integer checked; // 0->未选中；1->已选中
    }
    // getter和setter
}
```

**分页**: 否，购物车列表通常不需要分页

### 1.3 修改购物车中商品数量

- **接口地址**: `/cart/update/quantity`
- **请求方式**: POST
- **Content-Type**: application/x-www-form-urlencoded
- **请求参数**:

```json
{
  "id": 1,        // 购物车项ID
  "quantity": 2   // 数量
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
public class CartUpdateQuantityRequest {
    @NotNull(message = "购物车项ID不能为空")
    private Long id;
    
    @NotNull(message = "数量不能为空")
    @Min(value = 1, message = "数量必须大于0")
    private Integer quantity;
    // getter和setter
}
```

**分页**: 否，修改购物车无需分页

### 1.4 修改购物车中商品选中状态

- **接口地址**: `/cart/update/checked`
- **请求方式**: POST
- **Content-Type**: application/x-www-form-urlencoded
- **请求参数**:

```json
{
  "ids": "1,2,3",   // 购物车项ID，多个用逗号分隔
  "checked": 1      // 选中状态：0->未选中；1->已选中
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
public class CartUpdateCheckedRequest {
    @NotEmpty(message = "购物车项ID不能为空")
    private String ids;
    
    @NotNull(message = "选中状态不能为空")
    private Integer checked;
    // getter和setter
}
```

**分页**: 否，修改购物车无需分页

### 1.5 删除购物车中商品

- **接口地址**: `/cart/delete`
- **请求方式**: POST
- **Content-Type**: application/x-www-form-urlencoded
- **请求参数**:

```json
{
  "ids": "1,2,3"  // 购物车项ID，多个用逗号分隔
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
public class CartDeleteRequest {
    @NotEmpty(message = "购物车项ID不能为空")
    private String ids;
    // getter和setter
}
```

**分页**: 否，删除购物车无需分页

### 1.6 清空购物车

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

### 1.7 获取购物车商品数量

- **接口地址**: `/cart/count`
- **请求方式**: GET
- **请求参数**: 无
- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": 5  // 购物车中商品数量
}
```

**分页**: 否，获取购物车数量无需分页

## 2. 收货地址相关接口

### 2.1 获取收货地址列表

- **接口地址**: `/member/address/list`
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
      "phoneNumber": "string",
      "defaultStatus": 0,
      "province": "string",
      "city": "string",
      "district": "string",
      "detailAddress": "string"
    }
  ]
}
```

**建议DTO设计**:
```java
public class AddressResponse {
    private Long id;
    private String name;
    private String phoneNumber;
    private Integer defaultStatus; // 0->非默认；1->默认
    private String province;
    private String city;
    private String district;
    private String detailAddress;
    // getter和setter
}
```

**分页**: 否，收货地址列表通常不需要分页

### 2.2 获取收货地址详情

- **接口地址**: `/member/address/{id}`
- **请求方式**: GET
- **请求参数**: 
  - id: 地址ID（路径参数）
- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "name": "string",
    "phoneNumber": "string",
    "defaultStatus": 0,
    "province": "string",
    "city": "string",
    "district": "string",
    "detailAddress": "string"
  }
}
```

**建议DTO设计**:
```java
// 复用AddressResponse
```

**分页**: 否，详情接口无需分页

### 2.3 添加收货地址

- **接口地址**: `/member/address/add`
- **请求方式**: POST
- **Content-Type**: application/json
- **请求参数**:

```json
{
  "name": "string",
  "phoneNumber": "string",
  "defaultStatus": 0,
  "province": "string",
  "city": "string",
  "district": "string",
  "detailAddress": "string"
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
public class AddressRequest {
    @NotEmpty(message = "收货人姓名不能为空")
    private String name;
    
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phoneNumber;
    
    private Integer defaultStatus; // 0->非默认；1->默认
    
    @NotEmpty(message = "省份不能为空")
    private String province;
    
    @NotEmpty(message = "城市不能为空")
    private String city;
    
    @NotEmpty(message = "区域不能为空")
    private String district;
    
    @NotEmpty(message = "详细地址不能为空")
    private String detailAddress;
    // getter和setter
}
```

**分页**: 否，添加地址无需分页

### 2.4 修改收货地址

- **接口地址**: `/member/address/update/{id}`
- **请求方式**: POST
- **请求参数**:
  - id: 地址ID（路径参数）
  - 其他参数同添加收货地址

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
// 复用AddressRequest，但要添加id字段
public class AddressUpdateRequest extends AddressRequest {
    private Long id;
    // getter和setter
}
```

**分页**: 否，修改地址无需分页

### 2.5 删除收货地址

- **接口地址**: `/member/address/delete/{id}`
- **请求方式**: POST
- **请求参数**:
  - id: 地址ID（路径参数）
- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

**分页**: 否，删除地址无需分页

### 2.6 设置默认收货地址

- **接口地址**: `/member/address/setDefault/{id}`
- **请求方式**: POST
- **请求参数**:
  - id: 地址ID（路径参数）
- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

**分页**: 否，设置默认地址无需分页

## 3. 优惠券相关接口

### 3.1 获取可领取优惠券列表

- **接口地址**: `/member/coupon/list`
- **请求方式**: GET
- **请求参数**:

```json
{
  "useStatus": 0, // 使用状态：0->未使用；1->已使用；2->已过期
  "pageNum": 1,
  "pageSize": 10
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
        "couponId": 1,
        "name": "string",
        "amount": 0.0,
        "minPoint": 0.0,
        "startTime": "2023-03-15",
        "endTime": "2023-03-31",
        "useStatus": 0
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
public class UserCouponListRequest extends PageRequest {
    private Integer useStatus;
    // getter和setter
}

// 响应
public class UserCouponResponse {
    private Long id;
    private Long couponId;
    private String name;
    private BigDecimal amount;
    private BigDecimal minPoint;
    private Date startTime;
    private Date endTime;
    private Integer useStatus; // 0->未使用；1->已使用；2->已过期
    // getter和setter
}
```

**分页**: 是，需要分页处理

### 3.2 领取优惠券

- **接口地址**: `/member/coupon/add/{couponId}`
- **请求方式**: POST
- **请求参数**:
  - couponId: 优惠券ID（路径参数）
- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

**分页**: 否，领取优惠券无需分页

### 3.3 获取商品可用优惠券

- **接口地址**: `/member/coupon/listByProduct/{productId}`
- **请求方式**: GET
- **请求参数**:
  - productId: 商品ID（路径参数）
- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "name": "string",
      "amount": 0.0,
      "minPoint": 0.0,
      "startTime": "2023-03-15",
      "endTime": "2023-03-31",
      "useType": 0
    }
  ]
}
```

**建议DTO设计**:
```java
public class ProductCouponResponse {
    private Long id;
    private String name;
    private BigDecimal amount;
    private BigDecimal minPoint;
    private Date startTime;
    private Date endTime;
    private Integer useType; // 0->全场通用；1->指定品类；2->指定商品
    // getter和setter
}
```

**分页**: 否，获取商品可用优惠券无需分页

## 4. 首页内容相关接口

### 4.1 获取首页内容

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
        "url": "string",
        "name": "string"
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
        "price": 0.0,
        "sale": 0,
        "subTitle": "string"
      }
    ],
    "hotProductList": [
      {
        "id": 1,
        "name": "string",
        "pic": "string",
        "price": 0.0,
        "sale": 0,
        "subTitle": "string"
      }
    ],
    "subjectList": [
      {
        "id": 1,
        "title": "string",
        "pic": "string",
        "categoryId": 0,
        "description": "string"
      }
    ]
  }
}
```

**建议DTO设计**:
```java
public class HomeContentResponse {
    private List<HomeAdvertiseResponse> advertiseList;
    private List<HomeBrandResponse> brandList;
    private List<ProductListItemResponse> newProductList;
    private List<ProductListItemResponse> hotProductList;
    private List<HomeSubjectResponse> subjectList;
    
    @Data
    public static class HomeAdvertiseResponse {
        private Long id;
        private String pic;
        private String url;
        private String name;
    }
    
    @Data
    public static class HomeBrandResponse {
        private Long id;
        private String name;
        private String logo;
    }
    
    @Data
    public static class HomeSubjectResponse {
        private Long id;
        private String title;
        private String pic;
        private Long categoryId;
        private String description;
    }
    // getter和setter
}
```

**分页**: 否，首页内容无需分页

### 4.2 获取首页推荐商品

- **接口地址**: `/home/recommendProductList`
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
// 复用ProductListItemResponse
```

**分页**: 是，需要分页处理

### 4.3 获取首页新品

- **接口地址**: `/home/newProductList`
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
// 复用ProductListItemResponse
```

**分页**: 是，需要分页处理

### 4.4 获取首页热卖商品

- **接口地址**: `/home/hotProductList`
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
// 复用ProductListItemResponse
```

**分页**: 是，需要分页处理

### 4.5 获取商品分类列表

- **接口地址**: `/home/productCateList/{parentId}`
- **请求方式**: GET
- **请求参数**:
  - parentId: 父分类ID（路径参数，0表示获取一级分类）
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
      "icon": "string",
      "productCount": 0
    }
  ]
}
```

**建议DTO设计**:
```java
public class ProductCategoryResponse {
    private Long id;
    private String name;
    private Integer level;
    private String icon;
    private Integer productCount;
    // getter和setter
}
```

**分页**: 否，分类列表通常不需要分页

## 5. 搜索历史相关接口

### 5.1 获取搜索历史记录

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

### 5.2 添加搜索历史记录

- **接口地址**: `/member/readHistory/create`
- **请求方式**: POST
- **请求参数**:

```json
{
  "productId": 1,
  "productName": "string",
  "productPic": "string",
  "productSubTitle": "string",
  "productPrice": 0.0
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
public class ReadHistoryCreateRequest {
    @NotNull(message = "商品ID不能为空")
    private Long productId;
    
    private String productName;
    private String productPic;
    private String productSubTitle;
    private BigDecimal productPrice;
    // getter和setter
}
```

**分页**: 否，添加历史记录无需分页

### 5.3 删除搜索历史记录

- **接口地址**: `/member/readHistory/delete`
- **请求方式**: POST
- **请求参数**:

```json
{
  "ids": [1, 2, 3] // 历史记录ID列表
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
public class ReadHistoryDeleteRequest {
    @NotEmpty(message = "ID列表不能为空")
    private List<Long> ids;
    // getter和setter
}
```

**分页**: 否，删除历史记录无需分页

### 5.4 清空搜索历史记录

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

**分页**: 否，清空历史记录无需分页

## 6. 会员积分相关接口

### 6.1 获取积分记录

- **接口地址**: `/member/integration/history`
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
  "data": {
    "list": [
      {
        "id": 1,
        "integration": 0,
        "type": 0,
        "note": "string",
        "createTime": "2023-03-15 12:00:00"
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
public class IntegrationHistoryResponse {
    private Long id;
    private Integer integration;
    private Integer type; // 0->增加；1->减少
    private String note;
    private Date createTime;
    // getter和setter
}
```

**分页**: 是，需要分页处理

### 6.2 获取积分规则

- **接口地址**: `/member/integration/rule`
- **请求方式**: GET
- **请求参数**: 无
- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "consumePerIntegration": 0.0, // 每1元可获取的积分数量
    "maxPercentPerOrder": 0, // 每笔订单最高可用积分比例，单位为百分比
    "useIntegrationLimit": 0 // 积分使用最小单位
  }
}
```

**建议DTO设计**:
```java
public class IntegrationRuleResponse {
    private BigDecimal consumePerIntegration;
    private Integer maxPercentPerOrder;
    private Integer useIntegrationLimit;
    // getter和setter
}
```

**分页**: 否，获取积分规则无需分页

## 接口实现建议

### 后端实现建议

1. **统一URL路径前缀**：
   - 将所有会员相关操作统一为`/member/xxx`
   - 将所有首页相关接口统一为`/home/xxx`
   - 将所有购物车相关接口统一为`/cart/xxx`

2. **统一请求方式**：
   - 查询类操作使用GET方法
   - 修改类操作使用POST方法
   - 确保Content-Type设置正确

3. **统一接口命名规范**：
   - 列表查询接口使用`list`
   - 详情查询接口使用`detail`或直接使用ID路径参数
   - 添加操作使用`add`或`create`
   - 修改操作使用`update`
   - 删除操作使用`delete`

4. **接口安全性**：
   - 所有接口需要进行身份验证（除登录注册外）
   - 敏感操作需要进行权限验证
   - 接口参数需要进行验证，确保数据的正确性

5. **性能优化**：
   - 针对复杂查询应考虑缓存
   - 分页接口需要进行性能优化
   - 大数据量接口需要考虑分批处理

### 前端实现建议

1. **统一请求处理**：
   - 使用axios等工具统一处理API请求
   - 封装请求拦截器处理token和错误
   - 统一处理响应状态码

2. **数据缓存**：
   - 使用Vuex/Redux等状态管理工具管理全局状态
   - 对不常变化的数据进行缓存

3. **用户体验**：
   - 实现请求的防抖和节流，避免重复提交
   - 表单提交前进行前端验证
   - 长列表数据实现虚拟滚动，提高性能

4. **错误处理**：
   - 针对不同的响应状态码进行相应处理
   - 友好的错误提示
   - 网络异常时的重试机制 