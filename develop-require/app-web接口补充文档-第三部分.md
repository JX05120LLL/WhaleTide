# WhaleTide App-Web 接口文档（第三部分-补充接口）

## 目录
1. [商品收藏相关接口](#1-商品收藏相关接口)
2. [品牌关注相关接口](#2-品牌关注相关接口)
3. [商品搜索相关接口](#3-商品搜索相关接口)
4. [商品评论相关接口](#4-商品评论相关接口)
5. [个人中心其他接口](#5-个人中心其他接口)
6. [系统消息相关接口](#6-系统消息相关接口)

## 1. 商品收藏相关接口

根据源码分析，发现前端实现了商品收藏功能，但在前两份文档中未包含相关接口。

### 1.1 添加商品收藏

- **接口地址**: `/member/productCollection/add`
- **请求方式**: POST
- **Content-Type**: application/json
- **请求参数**:

```json
{
  "productId": 1,           // 商品ID
  "productName": "string",  // 商品名称
  "productPic": "string",   // 商品图片
  "productPrice": 0.0,      // 商品价格
  "productSubTitle": "string" // 商品副标题
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
public class ProductCollectionRequest {
    @NotNull(message = "商品ID不能为空")
    private Long productId;
    
    private String productName;
    private String productPic;
    private BigDecimal productPrice;
    private String productSubTitle;
    // getter和setter
}
```

**分页**: 否，添加收藏无需分页

### 1.2 取消商品收藏

- **接口地址**: `/member/productCollection/delete`
- **请求方式**: POST
- **Content-Type**: application/x-www-form-urlencoded
- **请求参数**:

```json
{
  "productId": 1  // 商品ID
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
public class ProductCollectionDeleteRequest {
    @NotNull(message = "商品ID不能为空")
    private Long productId;
    // getter和setter
}
```

**分页**: 否，删除收藏无需分页

### 1.3 获取商品收藏列表

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
  "data": {
    "list": [
      {
        "id": 1,
        "productId": 1,
        "productName": "string",
        "productPic": "string",
        "productPrice": 0.0,
        "productSubTitle": "string",
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
public class ProductCollectionResponse {
    private Long id;
    private Long productId;
    private String productName;
    private String productPic;
    private BigDecimal productPrice;
    private String productSubTitle;
    private Date createTime;
    // getter和setter
}
```

**分页**: 是，需要分页处理

### 1.4 获取商品收藏详情

- **接口地址**: `/member/productCollection/detail`
- **请求方式**: GET
- **请求参数**:

```json
{
  "productId": 1  // 商品ID
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
    "productPrice": 0.0,
    "productSubTitle": "string",
    "createTime": "2023-03-15 12:00:00"
  }
}
```

**建议DTO设计**:
```java
// 复用ProductCollectionResponse
```

**分页**: 否，详情查询无需分页

### 1.5 清空商品收藏

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

**分页**: 否，清空收藏无需分页

## 2. 品牌关注相关接口

根据源码分析，发现前端实现了品牌关注功能，但在前两份文档中未包含相关接口。

### 2.1 关注品牌

- **接口地址**: `/member/brand/attention/add`
- **请求方式**: POST
- **Content-Type**: application/json
- **请求参数**:

```json
{
  "brandId": 1,       // 品牌ID
  "brandName": "string", // 品牌名称
  "brandLogo": "string", // 品牌logo
  "brandCity": "string", // 品牌所在城市
  "brandDesc": "string"  // 品牌描述
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
public class BrandAttentionRequest {
    @NotNull(message = "品牌ID不能为空")
    private Long brandId;
    
    private String brandName;
    private String brandLogo;
    private String brandCity;
    private String brandDesc;
    // getter和setter
}
```

**分页**: 否，关注品牌无需分页

### 2.2 取消关注品牌

- **接口地址**: `/member/brand/attention/delete`
- **请求方式**: POST
- **Content-Type**: application/x-www-form-urlencoded
- **请求参数**:

```json
{
  "brandId": 1  // 品牌ID
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
public class BrandAttentionDeleteRequest {
    @NotNull(message = "品牌ID不能为空")
    private Long brandId;
    // getter和setter
}
```

**分页**: 否，取消关注无需分页

### 2.3 获取关注品牌列表

- **接口地址**: `/member/brand/attention/list`
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
        "brandId": 1,
        "brandName": "string",
        "brandLogo": "string",
        "brandCity": "string",
        "brandDesc": "string",
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
public class BrandAttentionResponse {
    private Long id;
    private Long brandId;
    private String brandName;
    private String brandLogo;
    private String brandCity;
    private String brandDesc;
    private Date createTime;
    // getter和setter
}
```

**分页**: 是，需要分页处理

### 2.4 获取关注品牌详情

- **接口地址**: `/member/brand/attention/detail`
- **请求方式**: GET
- **请求参数**:

```json
{
  "brandId": 1  // 品牌ID
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
    "brandDesc": "string",
    "createTime": "2023-03-15 12:00:00"
  }
}
```

**建议DTO设计**:
```java
// 复用BrandAttentionResponse
```

**分页**: 否，详情查询无需分页

### 2.5 清空关注品牌

- **接口地址**: `/member/brand/attention/clear`
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

**分页**: 否，清空关注无需分页

## 3. 商品搜索相关接口

### 3.1 搜索商品列表

- **接口地址**: `/product/search`
- **请求方式**: GET
- **请求参数**:

```json
{
  "keyword": "string",    // 搜索关键词
  "brandId": 1,           // 品牌ID
  "productCategoryId": 1, // 商品分类ID
  "sort": "string",       // 排序方式，可选值：price_asc, price_desc, sales_asc, sales_desc, create_time_asc, create_time_desc
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
        "name": "string",
        "pic": "string",
        "price": 0.0,
        "sale": 0,
        "subTitle": "string",
        "brandName": "string",
        "categoryName": "string",
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
// 请求
public class ProductSearchRequest extends PageRequest {
    private String keyword;
    private Long brandId;
    private Long productCategoryId;
    private String sort;
    // getter和setter
}

// 响应
public class ProductSearchResponse {
    private Long id;
    private String name;
    private String pic;
    private BigDecimal price;
    private Integer sale;
    private String subTitle;
    private String brandName;
    private String categoryName;
    private Date createTime;
    // getter和setter
}
```

**分页**: 是，需要分页处理

### 3.2 获取分类树列表

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
      "icon": "string",
      "productCount": 0,
      "children": [
        {
          "id": 2,
          "name": "string",
          "level": 1,
          "icon": "string",
          "productCount": 0,
          "children": []
        }
      ]
    }
  ]
}
```

**建议DTO设计**:
```java
public class ProductCategoryTreeResponse {
    private Long id;
    private String name;
    private Integer level;
    private String icon;
    private Integer productCount;
    private List<ProductCategoryTreeResponse> children;
    // getter和setter
}
```

**分页**: 否，分类树无需分页

## 4. 商品评论相关接口

品牌详情页面需要商品评论功能，补充如下接口：

### 4.1 获取商品评论列表

- **接口地址**: `/product/comment/list/{productId}`
- **请求方式**: GET
- **请求参数**:
  - productId: 商品ID（路径参数）
  - 分页参数:
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
        "productId": 1,
        "memberNickName": "string",
        "memberIcon": "string",
        "content": "string",
        "star": 5,
        "pics": ["string"],
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
public class ProductCommentResponse {
    private Long id;
    private Long productId;
    private String memberNickName;
    private String memberIcon;
    private String content;
    private Integer star; // 评分，1-5星
    private List<String> pics; // 评论图片
    private Date createTime;
    // getter和setter
}
```

**分页**: 是，需要分页处理

### 4.2 添加商品评论

- **接口地址**: `/product/comment/add`
- **请求方式**: POST
- **Content-Type**: application/json
- **请求参数**:

```json
{
  "productId": 1,
  "orderId": 1,
  "star": 5,
  "content": "string",
  "pics": ["string"] // 评论图片，可选
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
public class ProductCommentAddRequest {
    @NotNull(message = "商品ID不能为空")
    private Long productId;
    
    @NotNull(message = "订单ID不能为空")
    private Long orderId;
    
    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分最低为1星")
    @Max(value = 5, message = "评分最高为5星")
    private Integer star;
    
    @NotEmpty(message = "评论内容不能为空")
    private String content;
    
    private List<String> pics;
    // getter和setter
}
```

**分页**: 否，添加评论无需分页

## 5. 个人中心其他接口

### 5.1 修改用户信息

- **接口地址**: `/member/info/update`
- **请求方式**: POST
- **Content-Type**: application/json
- **请求参数**:

```json
{
  "username": "string",
  "nickname": "string",
  "phone": "string",
  "gender": 0,  // 性别：0->未知；1->男；2->女
  "birthday": "1990-01-01",
  "city": "string",
  "job": "string"
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
public class MemberInfoUpdateRequest {
    private String username;
    private String nickname;
    
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    
    private Integer gender;
    private Date birthday;
    private String city;
    private String job;
    // getter和setter
}
```

**分页**: 否，更新用户信息无需分页

### 5.2 上传用户头像

- **接口地址**: `/member/avatar/upload`
- **请求方式**: POST
- **Content-Type**: multipart/form-data
- **请求参数**:
  - file: 头像文件
- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "url": "string" // 头像URL
  }
}
```

**建议DTO设计**:
```java
public class AvatarUploadResponse {
    private String url;
    // getter和setter
}
```

**分页**: 否，上传头像无需分页

### 5.3 修改密码

- **接口地址**: `/member/password/update`
- **请求方式**: POST
- **Content-Type**: application/json
- **请求参数**:

```json
{
  "oldPassword": "string",
  "newPassword": "string"
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
public class PasswordUpdateRequest {
    @NotEmpty(message = "原密码不能为空")
    private String oldPassword;
    
    @NotEmpty(message = "新密码不能为空")
    @Size(min = 6, message = "密码长度不能小于6位")
    private String newPassword;
    // getter和setter
}
```

**分页**: 否，修改密码无需分页

### 5.4 获取用户积分详情

- **接口地址**: `/member/integration/detail`
- **请求方式**: GET
- **请求参数**: 无
- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "integration": 0, // 当前积分
    "historyIntegration": 0, // 历史总积分
    "consumePerIntegration": 0.0, // 每1元获取的积分数量
    "useIntegrationLimit": 0 // 最小使用单位
  }
}
```

**建议DTO设计**:
```java
public class IntegrationDetailResponse {
    private Integer integration;
    private Integer historyIntegration;
    private BigDecimal consumePerIntegration;
    private Integer useIntegrationLimit;
    // getter和setter
}
```

**分页**: 否，获取积分详情无需分页

## 6. 系统消息相关接口

### 6.1 获取系统消息列表

- **接口地址**: `/member/message/list`
- **请求方式**: GET
- **请求参数**:

```json
{
  "readStatus": 0, // 阅读状态：0->未读；1->已读
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
        "title": "string",
        "content": "string",
        "readStatus": 0,
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
public class MessageResponse {
    private Long id;
    private String title;
    private String content;
    private Integer readStatus; // 0->未读；1->已读
    private Date createTime;
    // getter和setter
}
```

**分页**: 是，需要分页处理

### 6.2 获取系统消息详情

- **接口地址**: `/member/message/{id}`
- **请求方式**: GET
- **请求参数**:
  - id: 消息ID（路径参数）
- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "title": "string",
    "content": "string",
    "readStatus": 0,
    "createTime": "2023-03-15 12:00:00"
  }
}
```

**建议DTO设计**:
```java
// 复用MessageResponse
```

**分页**: 否，详情查询无需分页

### 6.3 标记消息为已读

- **接口地址**: `/member/message/read/{id}`
- **请求方式**: POST
- **请求参数**:
  - id: 消息ID（路径参数）
- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

**分页**: 否，标记已读无需分页

### 6.4 删除系统消息

- **接口地址**: `/member/message/delete/{id}`
- **请求方式**: POST
- **请求参数**:
  - id: 消息ID（路径参数）
- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

**分页**: 否，删除消息无需分页

### 6.5 统计未读消息数量

- **接口地址**: `/member/message/unreadCount`
- **请求方式**: GET
- **请求参数**: 无
- **响应参数**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": 5 // 未读消息数量
}
```

**分页**: 否，统计数量无需分页 