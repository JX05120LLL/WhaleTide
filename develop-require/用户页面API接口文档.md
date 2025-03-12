# WhaleTide 用户页面 API 接口文档

## 基础信息

- 基础URL: `http://localhost:8085`
- 所有请求头中如需携带token，格式为：`Authorization: Bearer {token}`
- 响应格式：所有API返回统一格式的JSON对象
  ```json
  {
    "code": 200, // 状态码，200表示成功
    "message": "操作成功", // 提示信息
    "data": {} // 具体业务数据
  }
  ```

## 目录

- [用户认证](#用户认证)
- [首页](#首页)
- [商品](#商品)
- [品牌](#品牌)
- [购物车](#购物车)
- [订单](#订单)
- [会员地址](#会员地址)
- [优惠券](#优惠券)
- [收藏](#收藏)
- [浏览记录](#浏览记录)
- [品牌关注](#品牌关注)

## 用户认证

### 用户登录

- **接口URL**：`/sso/login`
- **请求方式**：`POST`
- **Content-Type**：`application/x-www-form-urlencoded;charset=utf-8`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | username | String | 是 | 用户名 |
  | password | String | 是 | 密码 |

- **返回示例**：
  ```json
  {
    "code": 200,
    "message": "操作成功",
    "data": {
      "token": "eyJhbGciOiJIUzI1NiJ9...",
      "tokenHead": "Bearer "
    }
  }
  ```

### 获取当前登录用户信息

- **接口URL**：`/sso/info`
- **请求方式**：`GET`
- **请求参数**：无
- **返回示例**：
  ```json
  {
    "code": 200,
    "message": "操作成功",
    "data": {
      "id": 1,
      "username": "test",
      "nickname": "测试用户",
      "phone": "13800138000",
      "status": 1
    }
  }
  ```

## 首页

### 获取首页内容

- **接口URL**：`/home/content`
- **请求方式**：`GET`
- **请求参数**：无
- **返回数据**：首页广告、推荐商品等信息

### 获取推荐商品列表

- **接口URL**：`/home/recommendProductList`
- **请求方式**：`GET`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | pageNum | Integer | 否 | 页码 |
  | pageSize | Integer | 否 | 每页记录数 |

### 获取商品分类列表

- **接口URL**：`/home/productCateList/{parentId}`
- **请求方式**：`GET`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | parentId | Long | 是 | 父级分类ID |

### 获取新品推荐

- **接口URL**：`/home/newProductList`
- **请求方式**：`GET`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | pageNum | Integer | 否 | 页码 |
  | pageSize | Integer | 否 | 每页记录数 |

### 获取人气推荐

- **接口URL**：`/home/hotProductList`
- **请求方式**：`GET`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | pageNum | Integer | 否 | 页码 |
  | pageSize | Integer | 否 | 每页记录数 |

## 商品

### 搜索商品

- **接口URL**：`/product/search`
- **请求方式**：`GET`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | keyword | String | 否 | 搜索关键字 |
  | pageNum | Integer | 否 | 页码 |
  | pageSize | Integer | 否 | 每页记录数 |
  | sort | String | 否 | 排序字段 |
  | categoryId | Long | 否 | 分类ID |

### 获取商品分类树形列表

- **接口URL**：`/product/categoryTreeList`
- **请求方式**：`GET`
- **请求参数**：无

### 获取商品详情

- **接口URL**：`/product/detail/{id}`
- **请求方式**：`GET`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | id | Long | 是 | 商品ID |

## 品牌

### 获取品牌详情

- **接口URL**：`/brand/detail/{id}`
- **请求方式**：`GET`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | id | Long | 是 | 品牌ID |

### 获取品牌相关商品

- **接口URL**：`/brand/productList`
- **请求方式**：`GET`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | brandId | Long | 是 | 品牌ID |
  | pageNum | Integer | 否 | 页码 |
  | pageSize | Integer | 否 | 每页记录数 |

### 获取推荐品牌列表

- **接口URL**：`/brand/recommendList`
- **请求方式**：`GET`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | pageNum | Integer | 否 | 页码 |
  | pageSize | Integer | 否 | 每页记录数 |

## 购物车

### 添加商品到购物车

- **接口URL**：`/cart/add`
- **请求方式**：`POST`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | productId | Long | 是 | 商品ID |
  | productSkuId | Long | 是 | 商品SKU ID |
  | quantity | Integer | 是 | 数量 |

### 获取购物车列表

- **接口URL**：`/cart/list`
- **请求方式**：`GET`
- **请求参数**：无

### 删除购物车商品

- **接口URL**：`/cart/delete`
- **请求方式**：`POST`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | ids | String | 是 | 购物车项ID，多个用逗号隔开 |

### 修改购物车商品数量

- **接口URL**：`/cart/update/quantity`
- **请求方式**：`GET`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | id | Long | 是 | 购物车项ID |
  | quantity | Integer | 是 | 修改后的数量 |

### 清空购物车

- **接口URL**：`/cart/clear`
- **请求方式**：`POST`
- **请求参数**：无

## 订单

### 生成确认单信息

- **接口URL**：`/order/generateConfirmOrder`
- **请求方式**：`POST`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | cartIds | List\<Long\> | 是 | 购物车项ID列表 |

### 生成订单

- **接口URL**：`/order/generateOrder`
- **请求方式**：`POST`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | memberReceiveAddressId | Long | 是 | 收货地址ID |
  | couponId | Long | 否 | 优惠券ID |
  | payType | Integer | 是 | 支付方式 |
  | cartIds | List\<Long\> | 是 | 购物车项ID列表 |

### 获取订单列表

- **接口URL**：`/order/list`
- **请求方式**：`GET`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | status | Integer | 否 | 订单状态 |
  | pageNum | Integer | 否 | 页码 |
  | pageSize | Integer | 否 | 每页记录数 |

### 支付成功回调

- **接口URL**：`/order/paySuccess`
- **请求方式**：`POST`
- **Content-Type**：`application/x-www-form-urlencoded;charset=utf-8`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | orderId | Long | 是 | 订单ID |
  | payType | Integer | 是 | 支付方式 |

### 获取订单详情

- **接口URL**：`/order/detail/{orderId}`
- **请求方式**：`GET`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | orderId | Long | 是 | 订单ID |

### 用户取消订单

- **接口URL**：`/order/cancelUserOrder`
- **请求方式**：`POST`
- **Content-Type**：`application/x-www-form-urlencoded;charset=utf-8`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | orderId | Long | 是 | 订单ID |

### 确认收货

- **接口URL**：`/order/confirmReceiveOrder`
- **请求方式**：`POST`
- **Content-Type**：`application/x-www-form-urlencoded;charset=utf-8`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | orderId | Long | 是 | 订单ID |

### 删除订单

- **接口URL**：`/order/deleteOrder`
- **请求方式**：`POST`
- **Content-Type**：`application/x-www-form-urlencoded;charset=utf-8`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | orderId | Long | 是 | 订单ID |

### 查询支付宝支付状态

- **接口URL**：`/alipay/query`
- **请求方式**：`GET`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | orderId | Long | 是 | 订单ID |
  | tradeNo | String | 否 | 支付宝交易号 |

## 会员地址

### 获取收货地址列表

- **接口URL**：`/member/address/list`
- **请求方式**：`GET`
- **请求参数**：无

### 获取收货地址详情

- **接口URL**：`/member/address/{id}`
- **请求方式**：`GET`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | id | Long | 是 | 地址ID |

### 添加收货地址

- **接口URL**：`/member/address/add`
- **请求方式**：`POST`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | name | String | 是 | 收货人姓名 |
  | phoneNumber | String | 是 | 手机号码 |
  | defaultStatus | Integer | 是 | 是否为默认 |
  | province | String | 是 | 省份 |
  | city | String | 是 | 城市 |
  | region | String | 是 | 区域 |
  | detailAddress | String | 是 | 详细地址 |

### 修改收货地址

- **接口URL**：`/member/address/update/{id}`
- **请求方式**：`POST`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | id | Long | 是 | 地址ID |
  | name | String | 是 | 收货人姓名 |
  | phoneNumber | String | 是 | 手机号码 |
  | defaultStatus | Integer | 是 | 是否为默认 |
  | province | String | 是 | 省份 |
  | city | String | 是 | 城市 |
  | region | String | 是 | 区域 |
  | detailAddress | String | 是 | 详细地址 |

### 删除收货地址

- **接口URL**：`/member/address/delete/{id}`
- **请求方式**：`POST`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | id | Long | 是 | 地址ID |

## 优惠券

### 获取指定商品相关优惠券

- **接口URL**：`/member/coupon/listByProduct/{productId}`
- **请求方式**：`GET`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | productId | Long | 是 | 商品ID |

### 领取优惠券

- **接口URL**：`/member/coupon/add/{couponId}`
- **请求方式**：`POST`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | couponId | Long | 是 | 优惠券ID |

### 获取会员优惠券列表

- **接口URL**：`/member/coupon/list`
- **请求方式**：`GET`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | useStatus | Integer | 是 | 使用状态：0->未使用；1->已使用；2->已过期 |

## 收藏

### 添加商品收藏

- **接口URL**：`/member/productCollection/add`
- **请求方式**：`POST`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | productId | Long | 是 | 商品ID |
  | productName | String | 是 | 商品名称 |
  | productPic | String | 是 | 商品图片 |
  | productPrice | BigDecimal | 是 | 商品价格 |
  | productSubTitle | String | 是 | 商品副标题 |

### 删除商品收藏

- **接口URL**：`/member/productCollection/delete`
- **请求方式**：`POST`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | productId | Long | 是 | 商品ID |

### 获取商品收藏列表

- **接口URL**：`/member/productCollection/list`
- **请求方式**：`GET`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | pageNum | Integer | 否 | 页码 |
  | pageSize | Integer | 否 | 每页记录数 |

### 获取商品收藏详情

- **接口URL**：`/member/productCollection/detail`
- **请求方式**：`GET`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | productId | Long | 是 | 商品ID |

### 清空商品收藏

- **接口URL**：`/member/productCollection/clear`
- **请求方式**：`POST`
- **请求参数**：无

## 浏览记录

### 添加浏览记录

- **接口URL**：`/member/readHistory/create`
- **请求方式**：`POST`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | productId | Long | 是 | 商品ID |
  | productName | String | 是 | 商品名称 |
  | productPic | String | 是 | 商品图片 |
  | productPrice | BigDecimal | 是 | 商品价格 |
  | productSubTitle | String | 是 | 商品副标题 |

### 获取浏览记录列表

- **接口URL**：`/member/readHistory/list`
- **请求方式**：`GET`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | pageNum | Integer | 否 | 页码 |
  | pageSize | Integer | 否 | 每页记录数 |

### 清空浏览记录

- **接口URL**：`/member/readHistory/clear`
- **请求方式**：`POST`
- **请求参数**：无

## 品牌关注

### 添加品牌关注

- **接口URL**：`/member/attention/add`
- **请求方式**：`POST`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | brandId | Long | 是 | 品牌ID |
  | brandName | String | 是 | 品牌名称 |
  | brandLogo | String | 是 | 品牌logo |
  | brandCity | String | 是 | 品牌所在城市 |

### 取消品牌关注

- **接口URL**：`/member/attention/delete`
- **请求方式**：`POST`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | brandId | Long | 是 | 品牌ID |

### 获取品牌关注列表

- **接口URL**：`/member/attention/list`
- **请求方式**：`GET`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | pageNum | Integer | 否 | 页码 |
  | pageSize | Integer | 否 | 每页记录数 |

### 获取品牌关注详情

- **接口URL**：`/member/attention/detail`
- **请求方式**：`GET`
- **请求参数**：
  | 参数名 | 类型 | 必填 | 描述 |
  | ------ | ---- | ---- | ---- |
  | brandId | Long | 是 | 品牌ID |

### 清空品牌关注

- **接口URL**：`/member/attention/clear`
- **请求方式**：`POST`
- **请求参数**：无 