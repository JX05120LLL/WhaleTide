# JWT 和 HuTools 详解与商城系统应用指南

## 目录
- [JWT（JSON Web Token）详解](#jwt详解)
  - [什么是JWT](#什么是jwt)
  - [JWT的结构](#jwt的结构)
  - [JWT在商城系统中的应用场景](#jwt在商城系统中的应用场景)
  - [JWT的实现与使用](#jwt的实现与使用)
  - [JWT使用示例](#jwt使用示例)
- [HuTools（Hutool）详解](#hutools详解)
  - [什么是Hutool](#什么是hutool)
  - [Hutool的主要功能模块](#hutool的主要功能模块)
  - [Hutool在商城系统中的应用场景](#hutool在商城系统中的应用场景)
  - [Hutool常用工具使用示例](#hutool常用工具使用示例)

## JWT详解

### 什么是JWT

JWT（JSON Web Token）是一种开放标准（RFC 7519），它定义了一种紧凑且自包含的方式，用于在各方之间安全地传输信息作为JSON对象。这种信息可以被验证和信任，因为它是数字签名的。

JWT主要用于：
- **认证（Authentication）**：最常见的场景，一旦用户登录，每个后续请求都将包含JWT
- **信息交换**：JWT是在各方之间安全传输信息的一种好方法

### JWT的结构

JWT由三部分组成，用点（.）分隔：
1. **头部（Header）**：指定类型（typ）和签名算法（alg）
2. **载荷（Payload）**：包含声明（claims）的实际数据
3. **签名（Signature）**：用于验证消息的真实性

示例：
```
xxxxx.yyyyy.zzzzz
```

### JWT在商城系统中的应用场景

1. **用户认证与会话管理**
   - 用户登录后，服务器生成JWT并返回给客户端
   - 客户端在后续请求中携带JWT，无需服务端保存会话状态

2. **权限控制**
   - 在JWT的payload中包含用户角色和权限信息
   - 不同角色（普通用户、VIP、管理员）访问不同资源

3. **跨服务认证**
   - 微服务架构中，实现单点登录
   - 各个微服务可以独立验证token而无需查询认证服务

4. **购物车与订单处理**
   - 在分布式系统中标识用户身份
   - 确保订单操作的安全性和用户身份的一致性

5. **第三方API集成**
   - 与支付平台、物流服务等第三方API交互时的身份验证

### JWT的实现与使用

#### Maven依赖

```xml
<!-- JWT支持 -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
```

### JWT使用示例

#### 1. 生成JWT Token

```java
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

public class JwtUtil {
    // 密钥
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    // token有效期（毫秒）
    private static final long EXPIRATION_TIME = 86400000; // 24小时
    
    public static String generateToken(String username, Long userId, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }
    
    public static io.jsonwebtoken.Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
```

#### 2. 在商城系统中使用JWT

```java
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // 验证用户名和密码
        User user = userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        
        if (user != null) {
            // 生成JWT
            String token = JwtUtil.generateToken(user.getUsername(), user.getId(), user.getRole());
            
            // 返回JWT给客户端
            return ResponseEntity.ok(new JwtResponse(token));
        }
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
}
```

#### 3. 实现JWT拦截器

```java
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        
        final String authorizationHeader = request.getHeader("Authorization");
        
        String username = null;
        String jwt = null;
        
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                io.jsonwebtoken.Claims claims = JwtUtil.parseToken(jwt);
                username = claims.getSubject();
                
                // 设置用户认证信息
                if (username != null) {
                    UsernamePasswordAuthenticationToken authentication = 
                            new UsernamePasswordAuthenticationToken(username, null, 
                                    Collections.singletonList(new SimpleGrantedAuthority((String) claims.get("role"))));
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // Token解析错误
            }
        }
        
        chain.doFilter(request, response);
    }
}
```

## HuTools详解

### 什么是Hutool

Hutool是一个Java工具包，类似于Apache Commons和Google Guava，但更轻量级，功能更丰富。它致力于简化每一行代码，减少重复造轮子，提高Java开发效率。

### Hutool的主要功能模块

Hutool的功能非常丰富，主要包括以下模块：

1. **核心（core）**：日期、IO、反射、线程等工具类
2. **集合（collections）**：集合相关工具类
3. **缓存（cache）**：缓存工具类
4. **加密解密（crypto）**：提供对称、非对称和摘要算法
5. **数据库（db）**：JDBC封装
6. **日志（log）**：自动识别日志实现
7. **HTTP客户端（http）**：简化HTTP请求
8. **JSON处理（json）**：JSON序列化与反序列化
9. **定时任务（cron）**：定时任务工具
10. **系统信息（system）**：系统和运行时信息工具
11. **二维码（extra）**：二维码生成与解析
12. **邮件工具（extra）**：简化邮件发送
13. **Office文档（poi）**：Excel、Word等文档操作

### Hutool在商城系统中的应用场景

1. **数据处理与转换**
   - 处理订单、商品数据的格式转换
   - 日期时间处理（如订单时间、促销时间等）

2. **安全与加密**
   - 密码加密存储
   - 敏感数据加密（如用户支付信息）

3. **HTTP客户端**
   - 调用第三方API（如支付接口、物流查询）
   - 微服务间通信

4. **缓存处理**
   - 商品信息、促销活动缓存
   - 用户会话缓存

5. **Excel处理**
   - 订单导出
   - 商品批量导入导出

6. **二维码生成**
   - 商品二维码
   - 支付二维码

7. **文件操作**
   - 商品图片上传与处理
   - 日志文件操作

### Hutool常用工具使用示例

#### Maven依赖

```xml
<dependency>
    <groupId>cn.hutool</groupId>
    <artifactId>hutool-all</artifactId>
    <version>5.8.11</version>
</dependency>
```

#### 1. 日期时间处理

```java
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.DatePattern;

// 商城订单处理示例
public class OrderService {
    
    public Order createOrder(Long userId, List<OrderItem> items) {
        Order order = new Order();
        order.setUserId(userId);
        order.setItems(items);
        
        // 使用Hutool设置当前时间
        order.setCreateTime(DateUtil.date());
        
        // 计算订单过期时间（30分钟后）
        order.setExpireTime(DateUtil.offsetMinute(order.getCreateTime(), 30));
        
        // 格式化订单号（年月日时分秒毫秒+4位随机数）
        String orderNo = DateUtil.format(order.getCreateTime(), "yyyyMMddHHmmssSSS") 
                + RandomUtil.randomNumbers(4);
        order.setOrderNo(orderNo);
        
        return orderRepository.save(order);
    }
    
    public List<Order> getOrdersByDateRange(Date startDate, Date endDate) {
        // 格式化日期为开始和结束时间
        DateTime start = DateUtil.beginOfDay(startDate);
        DateTime end = DateUtil.endOfDay(endDate);
        
        return orderRepository.findByCreateTimeBetween(start, end);
    }
}
```

#### 2. 加密与安全

```java
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import cn.hutool.crypto.symmetric.AES;

// 用户服务示例
public class UserService {
    
    private static final byte[] KEY = "1234567890123456".getBytes(); // AES-128需要16字节
    
    public User register(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        
        // 使用Hutool对密码进行加密存储
        String encryptedPassword = DigestUtil.bcrypt(request.getPassword());
        user.setPassword(encryptedPassword);
        
        // 加密存储敏感信息
        if (StringUtils.isNotBlank(request.getPhoneNumber())) {
            AES aes = new AES(KEY);
            String encryptedPhone = aes.encryptHex(request.getPhoneNumber());
            user.setPhoneNumber(encryptedPhone);
        }
        
        return userRepository.save(user);
    }
    
    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        return DigestUtil.bcryptCheck(plainPassword, hashedPassword);
    }
    
    public String decryptPhoneNumber(String encryptedPhone) {
        AES aes = new AES(KEY);
        return aes.decryptStr(encryptedPhone);
    }
}
```

#### 3. HTTP请求处理

```java
import cn.hutool.http.HttpUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;

// 支付服务示例
public class PaymentService {
    
    private static final String PAYMENT_API_URL = "https://api.payment.com/v1/transactions";
    
    public PaymentResponse processPayment(PaymentRequest request) {
        // 构建请求参数
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", request.getOrderId());
        params.put("amount", request.getAmount());
        params.put("currency", "CNY");
        params.put("paymentMethod", request.getPaymentMethod());
        
        // 使用Hutool发送HTTP请求
        String result = HttpRequest.post(PAYMENT_API_URL)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + getApiToken())
                .body(JSONUtil.toJsonStr(params))
                .timeout(20000) // 设置超时时间
                .execute()
                .body();
        
        // 解析返回结果
        return JSONUtil.toBean(result, PaymentResponse.class);
    }
    
    public OrderTrackingInfo getLogisticsInfo(String trackingNumber) {
        // 简单GET请求获取物流信息
        String url = "https://api.logistics.com/tracking?number=" + trackingNumber;
        String result = HttpUtil.get(url);
        
        return JSONUtil.toBean(result, OrderTrackingInfo.class);
    }
}
```

#### 4. 商品数据导出Excel

```java
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

// 商品管理服务示例
public class ProductService {
    
    public File exportProductsToExcel(List<Product> products) {
        // 创建ExcelWriter
        ExcelWriter writer = ExcelUtil.getWriter();
        
        // 设置表头
        writer.addHeaderAlias("id", "商品ID");
        writer.addHeaderAlias("name", "商品名称");
        writer.addHeaderAlias("price", "价格");
        writer.addHeaderAlias("stock", "库存");
        writer.addHeaderAlias("category", "分类");
        writer.addHeaderAlias("createTime", "创建时间");
        
        // 写入数据
        writer.write(products, true);
        
        // 生成临时文件
        File tempFile = FileUtil.createTempFile("products_", ".xlsx", true);
        writer.flush(tempFile);
        writer.close();
        
        return tempFile;
    }
    
    public List<Product> importProductsFromExcel(InputStream inputStream) {
        // 读取Excel
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        reader.addHeaderAlias("商品名称", "name");
        reader.addHeaderAlias("价格", "price");
        reader.addHeaderAlias("库存", "stock");
        reader.addHeaderAlias("分类", "category");
        
        // 读取为Bean列表
        List<Product> products = reader.readAll(Product.class);
        
        // 批量保存
        return productRepository.saveAll(products);
    }
}
```

#### 5. 二维码生成

```java
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;

// 订单服务示例
public class OrderQrCodeService {
    
    public BufferedImage generateOrderQrCode(String orderNo) {
        // 生成订单查询链接
        String url = "https://mall.example.com/orders/" + orderNo;
        
        // 自定义二维码配置
        QrConfig config = new QrConfig(300, 300);
        config.setMargin(1);
        
        // 生成二维码图片
        return QrCodeUtil.generate(url, config);
    }
    
    public BufferedImage generatePaymentQrCode(Order order) {
        // 构建支付信息
        Map<String, Object> payInfo = new HashMap<>();
        payInfo.put("orderId", order.getOrderNo());
        payInfo.put("amount", order.getTotalAmount());
        payInfo.put("timestamp", System.currentTimeMillis());
        
        // 将支付信息转为JSON字符串
        String paymentInfo = JSONUtil.toJsonStr(payInfo);
        
        // 生成支付二维码
        return QrCodeUtil.generate(paymentInfo, 300, 300);
    }
}
```

以上示例涵盖了在商城系统中，JWT和Hutool的主要应用场景和具体用法。根据实际需求，还可以进一步扩展和深入使用这两个工具的更多功能。 