# WhaleTide项目依赖结构优化方案

## 一、多模块结构设计

为了更好地组织项目结构，我们将采用Maven多模块结构，具体如下：

```
whale-tide                    # 父模块
├── whale-tide-common         # 公共模块
├── whale-tide-client         # 客户端模块
└── whale-tide-admin          # 管理端模块
```

### 父模块POM配置

父模块负责统一管理依赖版本，但不直接引入依赖：

```xml
<groupId>com.whale_tide</groupId>
<artifactId>whale-tide</artifactId>
<version>1.0.0-SNAPSHOT</version>
<packaging>pom</packaging>

<modules>
    <module>whale-tide-common</module>
    <module>whale-tide-client</module>
    <module>whale-tide-admin</module>
</modules>

<properties>
    <java.version>11</java.version>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <spring-boot.version>2.7.9</spring-boot.version>
    <mybatis-plus.version>3.5.3</mybatis-plus.version>
    <druid.version>1.2.16</druid.version>
    <mysql.version>8.0.32</mysql.version>
    <knife4j.version>4.1.0</knife4j.version>
    <jwt.version>0.11.5</jwt.version>
    <commons-pool2.version>2.11.1</commons-pool2.version>
    <commons-lang3.version>3.12.0</commons-lang3.version>
    <commons-io.version>2.11.0</commons-io.version>
    <hutool.version>5.8.15</hutool.version>
    <kaptcha.version>2.3.2</kaptcha.version>
    <aliyun-oss.version>3.16.0</aliyun-oss.version>
    <rabbitmq.version>2.4.6</rabbitmq.version>
    <redisson.version>3.20.0</redisson.version>
</properties>

<dependencyManagement>
    <dependencies>
        <!-- Spring Boot 依赖管理 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-dependencies</artifactId>
            <version>${spring-boot.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
        
        <!-- 内部模块依赖 -->
        <dependency>
            <groupId>com.whale_tide</groupId>
            <artifactId>whale-tide-common</artifactId>
            <version>${project.version}</version>
        </dependency>
        
        <!-- 其他第三方依赖版本管理 -->
        <!-- ... 各种第三方依赖的版本 ... -->
    </dependencies>
</dependencyManagement>
```

## 二、各模块依赖分配

### 1. 公共模块依赖（whale-tide-common）

公共模块包含所有模块共享的依赖：

```xml
<dependencies>
    <!-- 核心依赖 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- 数据库相关 -->
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-boot-starter</artifactId>
        <version>${mybatis-plus.version}</version>
    </dependency>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>${mysql.version}</version>
    </dependency>
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid-spring-boot-starter</artifactId>
        <version>${druid.version}</version>
    </dependency>
    
    <!-- Redis相关 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
    <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-pool2</artifactId>
        <version>${commons-pool2.version}</version>
    </dependency>
    
    <!-- 安全相关 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>${jwt.version}</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-impl</artifactId>
        <version>${jwt.version}</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-jackson</artifactId>
        <version>${jwt.version}</version>
    </dependency>
    
    <!-- 工具类 -->
    <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-lang3</artifactId>
        <version>${commons-lang3.version}</version>
    </dependency>
    <dependency>
        <groupId>commons-io</groupId>
        <artifactId>commons-io</artifactId>
        <version>${commons-io.version}</version>
    </dependency>
    <dependency>
        <groupId>cn.hutool</groupId>
        <artifactId>hutool-all</artifactId>
        <version>${hutool.version}</version>
    </dependency>
    
    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    
    <!-- 验证相关 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
</dependencies>
```

### 2. 客户端模块依赖（whale-tide-client）

客户端模块特有的依赖：

```xml
<dependencies>
    <!-- 依赖公共模块 -->
    <dependency>
        <groupId>com.whale_tide</groupId>
        <artifactId>whale-tide-common</artifactId>
    </dependency>
    
    <!-- 验证码生成（图形验证码） -->
    <dependency>
        <groupId>com.github.penggle</groupId>
        <artifactId>kaptcha</artifactId>
        <version>${kaptcha.version}</version>
    </dependency>
    
    <!-- 支付相关依赖（如需） -->
    <!-- <dependency>
        <groupId>com.alipay.sdk</groupId>
        <artifactId>alipay-sdk-java</artifactId>
        <version>${alipay.version}</version>
    </dependency> -->
    
    <!-- 客户端特有依赖 -->
</dependencies>
```

### 3. 管理端模块依赖（whale-tide-admin）

管理端模块特有的依赖：

```xml
<dependencies>
    <!-- 依赖公共模块 -->
    <dependency>
        <groupId>com.whale_tide</groupId>
        <artifactId>whale-tide-common</artifactId>
    </dependency>
    
    <!-- API文档 -->
    <dependency>
        <groupId>com.github.xiaoymin</groupId>
        <artifactId>knife4j-openapi3-spring-boot-starter</artifactId>
        <version>${knife4j.version}</version>
    </dependency>
    
    <!-- 对象存储 -->
    <dependency>
        <groupId>com.aliyun.oss</groupId>
        <artifactId>aliyun-sdk-oss</artifactId>
        <version>${aliyun-oss.version}</version>
    </dependency>
    
    <!-- 导出Excel等功能可能需要的依赖 -->
    <!-- <dependency>
        <groupId>org.apache.poi</groupId>
        <artifactId>poi</artifactId>
        <version>${poi.version}</version>
    </dependency> -->
    
    <!-- 管理端特有依赖 -->
</dependencies>
```

## 三、消息队列和并发控制依赖

针对订单模块的消息队列和并发控制，建议在公共模块中引入相关依赖：

```xml
<!-- 在公共模块中添加 -->
<!-- 消息队列 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
    <version>${rabbitmq.version}</version>
</dependency>

<!-- 分布式锁和缓存 -->
<dependency>
    <groupId>org.redisson</groupId>
    <artifactId>redisson-spring-boot-starter</artifactId>
    <version>${redisson.version}</version>
</dependency>
```

## 四、各层调用关系与组件引用

### 1. 通用层次架构

每个业务模块遵循以下分层架构：

```
Controller层 -> Service层 -> Mapper层 -> 数据库
           |         |
           |         ↓
           |      Entity/DTO
           ↓
          VO层
```

- **Controller层**：处理HTTP请求，参数校验，调用Service层，返回VO对象
- **Service层**：实现业务逻辑，调用Mapper层，处理Entity/DTO对象
- **Mapper层**：数据访问层，与数据库交互
- **Entity层**：数据库实体对象
- **DTO层**：数据传输对象，用于服务间传递数据
- **VO层**：视图对象，用于返回给前端的数据

### 2. 公共组件使用

#### Redis使用位置

Redis依赖放在公共模块，主要用于以下场景：

1. **缓存处理**：Service层缓存查询结果
2. **会话管理**：Security配置中管理用户会话
3. **限流控制**：Controller层或拦截器中进行限流
4. **分布式锁**：Service层处理并发操作

实现示例：

```java
// 在Service层中使用Redis缓存
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Override
    public ProductVO getProductDetail(Long id) {
        // 缓存key
        String cacheKey = "product:detail:" + id;
        
        // 尝试从缓存获取
        ProductVO productVO = (ProductVO) redisTemplate.opsForValue().get(cacheKey);
        if (productVO != null) {
            return productVO;
        }
        
        // 缓存未命中，查询数据库
        Product product = productMapper.selectById(id);
        if (product == null) {
            return null;
        }
        
        // 转换为VO
        productVO = convertToVO(product);
        
        // 存入缓存，设置过期时间
        redisTemplate.opsForValue().set(cacheKey, productVO, 1, TimeUnit.HOURS);
        
        return productVO;
    }
}
```

#### 分布式锁使用

订单创建等并发场景使用分布式锁：

```java
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private RedissonClient redissonClient;
    
    @Override
    @Transactional
    public OrderVO createOrder(OrderCreateDTO orderCreateDTO) {
        // 获取分布式锁，锁定商品库存
        RLock lock = redissonClient.getLock("product:stock:" + orderCreateDTO.getProductId());
        try {
            // 尝试获取锁，最多等待3秒，锁30秒自动释放
            boolean locked = lock.tryLock(3, 30, TimeUnit.SECONDS);
            if (!locked) {
                throw new BusinessException("系统繁忙，请稍后重试");
            }
            
            // 检查库存
            Product product = productMapper.selectById(orderCreateDTO.getProductId());
            if (product.getStock() < orderCreateDTO.getQuantity()) {
                throw new BusinessException("库存不足");
            }
            
            // 减库存
            product.setStock(product.getStock() - orderCreateDTO.getQuantity());
            productMapper.updateById(product);
            
            // 创建订单
            Order order = new Order();
            // 设置订单属性...
            orderMapper.insert(order);
            
            // 返回订单VO
            return convertToVO(order);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BusinessException("创建订单失败");
        } finally {
            // 释放锁
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
```

#### 消息队列使用

订单异步处理使用消息队列：

```java
// 生产者配置
@Configuration
public class RabbitMQConfig {
    @Bean
    public Queue orderQueue() {
        return new Queue("order.queue", true);
    }
    
    @Bean
    public Exchange orderExchange() {
        return new DirectExchange("order.exchange", true, false);
    }
    
    @Bean
    public Binding orderBinding() {
        return BindingBuilder
            .bind(orderQueue())
            .to(orderExchange())
            .with("order.create")
            .noargs();
    }
}

// 在订单Service中发送消息
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @Override
    @Transactional
    public OrderVO createOrder(OrderCreateDTO orderCreateDTO) {
        // 创建订单
        Order order = new Order();
        // 设置订单属性...
        orderMapper.insert(order);
        
        // 发送创建订单消息到MQ
        rabbitTemplate.convertAndSend("order.exchange", "order.create", order);
        
        return convertToVO(order);
    }
}

// 消费者处理
@Component
public class OrderConsumer {
    @Autowired
    private OrderProcessService orderProcessService;
    
    @RabbitListener(queues = "order.queue")
    public void processOrder(Order order) {
        try {
            // 处理订单逻辑，如扣减库存、发送通知等
            orderProcessService.processNewOrder(order);
        } catch (Exception e) {
            // 异常处理...
        }
    }
}
```

## 五、扩展功能实现

### 1. B64图片加密存储

在公共模块中利用Hutool工具类实现：

```java
// 在公共模块的util包中创建ImageUtil类
@Component
public class ImageUtil {
    /**
     * 图片转Base64
     * @param file 图片文件
     * @return Base64编码字符串
     */
    public String imageToBase64(MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        return Base64.encode(bytes);
    }
    
    /**
     * Base64转图片
     * @param base64Str Base64编码字符串
     * @return 图片字节数组
     */
    public byte[] base64ToImage(String base64Str) {
        return Base64.decode(base64Str);
    }
    
    /**
     * 保存Base64图片到数据库
     * @param base64Str Base64编码字符串
     * @param entityId 关联的实体ID
     * @param type 图片类型
     */
    public void saveBase64Image(String base64Str, Long entityId, String type) {
        // 创建图片实体并保存到数据库的代码...
    }
}
```

### 2. 图形验证码实现

在客户端模块中实现：

```java
// 在client模块的配置包中添加KaptchaConfig
@Configuration
public class KaptchaConfig {
    @Bean
    public DefaultKaptcha producer() {
        Properties properties = new Properties();
        properties.put("kaptcha.border", "no");
        properties.put("kaptcha.textproducer.font.color", "black");
        properties.put("kaptcha.textproducer.char.space", "5");
        // 更多配置...
        
        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}

// 在client模块的controller包中添加CaptchaController
@RestController
@RequestMapping("/api/captcha")
public class CaptchaController {
    @Autowired
    private DefaultKaptcha captchaProducer;
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    @GetMapping("/generate")
    public void generateCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 设置响应类型
        response.setContentType("image/jpeg");
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        
        // 生成验证码文本
        String capText = captchaProducer.createText();
        
        // 生成唯一标识
        String captchaId = UUID.randomUUID().toString();
        
        // 将验证码存入Redis，设置过期时间5分钟
        redisTemplate.opsForValue().set("captcha:" + captchaId, capText, 5, TimeUnit.MINUTES);
        
        // 将唯一标识设置到Cookie
        Cookie cookie = new Cookie("captchaId", captchaId);
        cookie.setMaxAge(300); // 5分钟
        cookie.setPath("/");
        response.addCookie(cookie);
        
        // 生成验证码图片
        BufferedImage bi = captchaProducer.createImage(capText);
        
        // 输出图片
        try (ServletOutputStream out = response.getOutputStream()) {
            ImageIO.write(bi, "jpg", out);
            out.flush();
        }
    }
    
    // 验证验证码的方法
    public boolean validateCaptcha(String captchaId, String userInputCaptcha) {
        String captchaText = redisTemplate.opsForValue().get("captcha:" + captchaId);
        if (captchaText == null) {
            return false;
        }
        boolean valid = captchaText.equalsIgnoreCase(userInputCaptcha);
        if (valid) {
            // 验证通过后删除验证码
            redisTemplate.delete("captcha:" + captchaId);
        }
        return valid;
    }
}
```

## 六、模块优化详细说明

### 1. 项目结构分层

```
whale-tide                            # 父模块
│
├── whale-tide-common                 # 公共模块
│   ├── src/main/java
│   │   └── com.whale_tide.common
│   │       ├── config               # 公共配置
│   │       │   ├── RedisConfig.java
│   │       │   ├── RabbitMQConfig.java
│   │       │   ├── MyBatisConfig.java
│   │       │   └── SecurityConfig.java
│   │       │
│   │       ├── entity               # 实体类
│   │       │   ├── ums             # 用户管理系统
│   │       │   ├── pms             # 商品管理系统
│   │       │   ├── oms             # 订单管理系统
│   │       │   └── sms             # 营销管理系统
│   │       │
│   │       ├── mapper               # Mapper接口
│   │       │
│   │       ├── exception            # 异常处理
│   │       │   ├── BusinessException.java
│   │       │   ├── GlobalExceptionHandler.java
│   │       │   └── ApiException.java
│   │       │
│   │       ├── security             # 安全相关
│   │       │   ├── JwtAuthenticationFilter.java
│   │       │   └── UserDetailsServiceImpl.java
│   │       │
│   │       ├── util                 # 公共工具类
│   │       │   ├── JwtTokenUtil.java
│   │       │   ├── RedisUtil.java
│   │       │   ├── ImageUtil.java
│   │       │   ├── DateUtil.java
│   │       │   └── StringUtil.java
│   │       │
│   │       └── constant             # 常量定义
│   │           ├── RedisConstants.java
│   │           └── SystemConstants.java
│   │
│   └── pom.xml                      # 公共模块POM
│
├── whale-tide-client                 # 客户端模块
│   ├── src/main/java
│   │   └── com.whale_tide.client
│   │       ├── ClientApplication.java  # 客户端启动类
│   │       │
│   │       ├── user                    # 用户模块
│   │       │   ├── controller
│   │       │   ├── service
│   │       │   │   └── impl
│   │       │   ├── dto
│   │       │   └── vo
│   │       │
│   │       ├── product                 # 商品模块
│   │       │   ├── controller
│   │       │   ├── service
│   │       │   │   └── impl
│   │       │   ├── dto
│   │       │   └── vo
│   │       │
│   │       ├── order                   # 订单模块
│   │       │   ├── controller
│   │       │   ├── service
│   │       │   │   └── impl
│   │       │   ├── dto
│   │       │   ├── vo
│   │       │   └── mq               # MQ消息处理
│   │       │       ├── OrderProducer.java
│   │       │       └── OrderConsumer.java
│   │       │
│   │       └── config                  # 客户端特有配置
│   │           └── KaptchaConfig.java
│   │
│   ├── src/main/resources
│   │   ├── application.yml
│   │   ├── application-dev.yml
│   │   └── application-prod.yml
│   │
│   └── pom.xml                      # 客户端模块POM
│
├── whale-tide-admin                  # 管理端模块
│   ├── src/main/java
│   │   └── com.whale_tide.admin
│   │       ├── AdminApplication.java   # 管理端启动类
│   │       │
│   │       ├── user                    # 用户管理模块
│   │       │   ├── controller
│   │       │   ├── service
│   │       │   │   └── impl
│   │       │   ├── dto
│   │       │   └── vo
│   │       │
│   │       ├── product                 # 商品管理模块
│   │       │   ├── controller
│   │       │   ├── service
│   │       │   │   └── impl
│   │       │   ├── dto
│   │       │   └── vo
│   │       │
│   │       ├── order                   # 订单管理模块
│   │       │   ├── controller
│   │       │   ├── service
│   │       │   │   └── impl
│   │       │   ├── dto
│   │       │   └── vo
│   │       │
│   │       └── config                  # 管理端特有配置
│   │           └── SwaggerConfig.java
│   │
│   ├── src/main/resources
│   │   ├── application.yml
│   │   ├── application-dev.yml
│   │   └── application-prod.yml
│   │
│   └── pom.xml                      # 管理端模块POM
│
└── pom.xml                           # 父模块POM
```

### 2. 层次间调用关系

1. **Controller层**
   - 依赖：Service层、DTO层、VO层
   - 职责：处理请求、参数校验、调用服务、封装结果

2. **Service层**
   - 依赖：Mapper层、Entity层、DTO层
   - 职责：实现业务逻辑、事务控制、数据转换

3. **Mapper层**
   - 依赖：Entity层
   - 职责：数据库操作

4. **Entity层**
   - 依赖：无
   - 职责：映射数据库表结构

5. **DTO层**
   - 依赖：可能依赖其他DTO或基础类型
   - 职责：数据传输、参数校验

6. **VO层**
   - 依赖：可能依赖其他VO或基础类型
   - 职责：视图展示、数据脱敏

### 3. 多模块开发规范

1. **依赖原则**：
   - 遵循"依赖倒置原则"，上层依赖下层的抽象而非实现
   - 避免循环依赖，保持模块间单向依赖
   - 公共模块不应依赖业务模块

2. **包命名规范**：
   - 公共模块：`com.whale_tide.common.xxx`
   - 客户端模块：`com.whale_tide.client.xxx`
   - 管理端模块：`com.whale_tide.admin.xxx`

3. **资源隔离**：
   - 每个模块有自己的配置文件
   - 按功能职责划分资源，避免重复定义

4. **代码复用**：
   - 提取共用代码到公共模块
   - 尽量避免复制粘贴代码

