# Swagger与Knife4j详解教程

## 目录
- [概述](#概述)
  - [什么是Swagger](#什么是swagger)
  - [什么是Knife4j](#什么是knife4j)
  - [Swagger与Knife4j的关系](#swagger与knife4j的关系)
- [集成方式](#集成方式)
  - [Spring Boot集成Swagger](#spring-boot集成swagger)
  - [Spring Boot集成Knife4j](#spring-boot集成knife4j)
- [常用注解详解](#常用注解详解)
  - [Swagger核心注解](#swagger核心注解)
  - [Swagger扩展注解](#swagger扩展注解)
  - [Knife4j特有注解](#knife4j特有注解)
- [实际应用案例](#实际应用案例)
  - [商城系统API文档示例](#商城系统api文档示例)
  - [最佳实践](#最佳实践)
- [Swagger与Knife4j对比](#swagger与knife4j对比)
- [常见问题与解决方案](#常见问题与解决方案)

## 概述

### 什么是Swagger

Swagger是一个开源的API文档工具，可以帮助开发人员设计、构建、记录和使用RESTful Web服务。它规范并简化了API开发流程，使API文档能够与API定义同步更新。Swagger通过注解的方式，可以直接在代码中定义API文档内容，自动生成可视化的API接口文档。

主要作用：
- **API设计与文档生成**：帮助团队设计和文档化API
- **接口测试**：提供界面直接测试API
- **代码生成**：可以根据API文档生成客户端代码
- **规范化**：使API的开发更加规范，便于团队协作

### 什么是Knife4j

Knife4j是基于Swagger的增强解决方案，前身是swagger-bootstrap-ui。它完全遵循了Swagger的规范，但提供了更强大的功能和更友好的交互体验，特别针对国人开发习惯做了优化。Knife4j保留了Swagger的所有功能，同时提供了更美观的界面和更丰富的功能。

主要特点：
- **更好的UI体验**：提供了更加美观、易用的界面
- **增强的功能**：离线文档、接口排序、搜索、自定义参数等
- **中文支持**：完善的中文支持和中文文档
- **文档导出**：支持导出多种格式的文档(Markdown、HTML、Word等)
- **更便捷的调试**：提供了更强大的接口调试功能

### Swagger与Knife4j的关系

Knife4j是基于Swagger的扩展和增强，两者的关系可以概括为：
- Knife4j完全兼容Swagger的规范和注解
- Knife4j是Swagger的UI增强实现，优化了前端交互体验
- 使用Knife4j时，底层仍然使用Swagger的核心库来生成API规范
- Knife4j在Swagger的基础上提供了更多便于国内开发者使用的特性

简单来说，**Knife4j = Swagger + 增强UI + 额外功能**。如果您已经使用了Swagger，可以很容易地迁移到Knife4j，享受更好的开发体验。

## 集成方式

### Spring Boot集成Swagger

#### 1. 添加依赖

在`pom.xml`中添加Swagger依赖：

```xml
<!-- Swagger2 -->
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.9.2</version>
</dependency>
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>2.9.2</version>
</dependency>

<!-- 或者使用最新的Swagger3（springfox 3.x） -->
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-boot-starter</artifactId>
    <version>3.0.0</version>
</dependency>
```

#### 2. 配置Swagger

创建Swagger配置类：

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 指定扫描的包路径来定义指定要建立API的目录
                .apis(RequestHandlerSelectors.basePackage("com.example.controller"))
                // 扫描所有
                // .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("API文档")
                .description("API接口文档")
                .contact(new Contact("开发者", "http://www.example.com", "developer@example.com"))
                .version("1.0")
                .build();
    }
}
```

#### 3. 启动应用并访问

启动应用后，可以通过以下URL访问Swagger UI：
- Swagger2: http://localhost:8080/swagger-ui.html
- Swagger3: http://localhost:8080/swagger-ui/index.html

### Spring Boot集成Knife4j

#### 1. 添加依赖

在`pom.xml`中添加Knife4j依赖：

```xml
<!-- Knife4j 2.x(基于Swagger 2) -->
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-spring-boot-starter</artifactId>
    <version>2.0.9</version>
</dependency>

<!-- 或者使用Knife4j 3.x(基于Swagger 3) -->
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-spring-boot-starter</artifactId>
    <version>3.0.3</version>
</dependency>
```

#### 2. 配置Knife4j

Knife4j完全兼容Swagger配置，只需稍作修改：

```java
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableKnife4j  // 启用Knife4j
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("API文档")
                .description("API接口文档")
                .contact(new Contact("开发者", "http://www.example.com", "developer@example.com"))
                .version("1.0")
                .build();
    }
}
```

#### 3. 添加配置（可选）

在`application.yml`中添加Knife4j配置：

```yaml
# knife4j配置
knife4j:
  enable: true  # 开启增强功能
  production: false  # 是否生产环境，生产环境会屏蔽接口文档
  basic:
    enable: false  # 是否开启Basic认证
    username: admin  # Basic认证用户名
    password: 123456  # Basic认证密码
```

#### 4. 启动应用并访问

启动应用后，可以通过以下URL访问Knife4j文档：
- http://localhost:8080/doc.html

## 常用注解详解

### Swagger核心注解

以下是Swagger最常用的核心注解，这些注解在Knife4j中同样适用：

#### 1. @Api

用于类上，表示对API类的说明。

```java
@Api(tags = "用户管理", description = "用户管理相关接口")
@RestController
@RequestMapping("/api/users")
public class UserController {
    // ...
}
```

参数说明：
- `tags`：API分组标签，在文档UI上会以这个标签对接口分组
- `description`：API的描述信息

#### 2. @ApiOperation

用于方法上，表示对API接口的说明。

```java
@ApiOperation(value = "创建用户", notes = "根据User对象创建用户")
@PostMapping("/")
public ResponseEntity<User> createUser(@RequestBody User user) {
    // ...
}
```

参数说明：
- `value`：接口名称/标题
- `notes`：接口的详细描述
- `response`：返回的对象类型
- `httpMethod`：HTTP请求方法（GET、POST等）

#### 3. @ApiParam

用于方法参数上，表示对参数的说明。

```java
@ApiOperation(value = "根据ID获取用户")
@GetMapping("/{id}")
public ResponseEntity<User> getUserById(
    @ApiParam(name = "id", value = "用户ID", required = true) 
    @PathVariable Long id) {
    // ...
}
```

参数说明：
- `name`：参数名称
- `value`：参数说明
- `required`：是否必须
- `example`：示例值
- `type`：参数类型

#### 4. @ApiModel

用于实体类上，表示对实体类的说明。

```java
@ApiModel(value = "用户实体", description = "用户信息")
public class User {
    // ...
}
```

参数说明：
- `value`：模型名称
- `description`：模型描述

#### 5. @ApiModelProperty

用于实体类属性上，表示对实体类属性的说明。

```java
@ApiModel(value = "用户实体", description = "用户信息")
public class User {
    @ApiModelProperty(value = "用户ID", example = "1", position = 1)
    private Long id;
    
    @ApiModelProperty(value = "用户名", required = true, example = "张三", position = 2)
    private String username;
    
    @ApiModelProperty(value = "密码", required = true, example = "123456", position = 3)
    private String password;
    
    // ...
}
```

参数说明：
- `value`：属性说明
- `name`：属性名称
- `required`：是否必须
- `example`：示例值
- `hidden`：是否隐藏
- `position`：排序位置

#### 6. @ApiResponse与@ApiResponses

用于描述接口的返回信息。

```java
@ApiOperation(value = "创建用户")
@ApiResponses({
    @ApiResponse(code = 201, message = "创建成功"),
    @ApiResponse(code = 400, message = "请求参数错误"),
    @ApiResponse(code = 500, message = "服务器内部错误")
})
@PostMapping("/")
public ResponseEntity<User> createUser(@RequestBody User user) {
    // ...
}
```

参数说明：
- `code`：HTTP状态码
- `message`：返回信息说明
- `response`：返回的对象类型

### Swagger扩展注解

除了核心注解外，还有一些扩展注解：

#### 1. @ApiImplicitParam与@ApiImplicitParams

用于方法上，描述接口的参数。

```java
@ApiOperation(value = "修改用户信息")
@ApiImplicitParams({
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, paramType = "path", dataType = "Long"),
    @ApiImplicitParam(name = "user", value = "用户实体", required = true, paramType = "body", dataType = "User")
})
@PutMapping("/{id}")
public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
    // ...
}
```

参数说明：
- `name`：参数名称
- `value`：参数说明
- `required`：是否必须
- `paramType`：参数类型（path、query、body、header、form）
- `dataType`：数据类型
- `example`：示例值

#### 2. @ApiIgnore

用于类或方法上，表示忽略这个API。

```java
@ApiIgnore
@GetMapping("/internal")
public String internalMethod() {
    // 该方法不会出现在API文档中
    return "内部方法";
}
```

### Knife4j特有注解

Knife4j在兼容Swagger注解的基础上，提供了一些特有的注解：

#### 1. @ApiOperationSupport

用于方法上，增强@ApiOperation的功能。

```java
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;

@ApiOperation(value = "创建用户")
@ApiOperationSupport(
    author = "张三",
    order = 1,
    ignoreParameters = {"id"}
)
@PostMapping("/")
public ResponseEntity<User> createUser(@RequestBody User user) {
    // ...
}
```

参数说明：
- `author`：接口作者
- `order`：接口排序
- `ignoreParameters`：忽略的参数

#### 2. @ApiSupport

用于类上，扩展API分组功能。

```java
import com.github.xiaoymin.knife4j.annotations.ApiSupport;

@Api(tags = "用户管理")
@ApiSupport(author = "李四", order = 1)
@RestController
@RequestMapping("/api/users")
public class UserController {
    // ...
}
```

参数说明：
- `author`：API作者
- `order`：API排序

#### 3. @DynamicParameter与@DynamicParameters

用于描述动态参数。

```java
import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
import com.github.xiaoymin.knife4j.annotations.DynamicParameters;

@ApiOperation(value = "创建用户")
@DynamicParameters(properties = {
    @DynamicParameter(name = "username", value = "用户名", required = true, dataTypeClass = String.class),
    @DynamicParameter(name = "password", value = "密码", required = true, dataTypeClass = String.class),
    @DynamicParameter(name = "age", value = "年龄", dataTypeClass = Integer.class)
})
@PostMapping("/dynamic")
public ResponseEntity<Object> createDynamicUser(@RequestBody Map<String, Object> params) {
    // ...
}
```

## 实际应用案例

### 商城系统API文档示例

以下是一个商城系统中用户模块的API文档示例：

```java
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

@Api(tags = "用户管理", description = "用户注册、登录、查询等接口")
@ApiSupport(author = "系统管理员", order = 1)
@RestController
@RequestMapping("/api/users")
public class UserController {

    @ApiOperation(value = "用户注册", notes = "注册新用户")
    @ApiOperationSupport(order = 1)
    @PostMapping("/register")
    public Result register(@RequestBody UserRegisterDTO userDTO) {
        // 注册实现...
        return Result.success();
    }
    
    @ApiOperation(value = "用户登录", notes = "用户登录并返回token")
    @ApiOperationSupport(order = 2)
    @PostMapping("/login")
    public Result<TokenVO> login(
            @ApiParam(value = "用户名", required = true) @RequestParam String username,
            @ApiParam(value = "密码", required = true) @RequestParam String password) {
        // 登录实现...
        return Result.success(new TokenVO("jwt-token-example"));
    }
    
    @ApiOperation(value = "获取用户信息", notes = "根据用户ID获取用户详细信息")
    @ApiOperationSupport(order = 3)
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, paramType = "path")
    @GetMapping("/{id}")
    public Result<UserVO> getUserInfo(@PathVariable Long id) {
        // 实现逻辑...
        return Result.success(new UserVO());
    }
    
    @ApiOperation(value = "更新用户信息", notes = "更新用户基本信息")
    @ApiOperationSupport(order = 4, ignoreParameters = {"id"})
    @PutMapping("/{id}")
    public Result updateUser(
            @ApiParam(value = "用户ID", required = true) @PathVariable Long id,
            @RequestBody UserUpdateDTO userDTO) {
        // 实现逻辑...
        return Result.success();
    }
    
    @ApiOperation(value = "删除用户", notes = "根据ID删除用户")
    @ApiOperationSupport(order = 5)
    @ApiResponses({
        @ApiResponse(code = 200, message = "删除成功"),
        @ApiResponse(code = 404, message = "用户不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @DeleteMapping("/{id}")
    public Result deleteUser(@PathVariable Long id) {
        // 实现逻辑...
        return Result.success();
    }
}

// DTO和VO类
@ApiModel(value = "用户注册对象", description = "用户注册时需要的信息")
class UserRegisterDTO {
    @ApiModelProperty(value = "用户名", required = true, example = "zhangsan")
    private String username;
    
    @ApiModelProperty(value = "密码", required = true, example = "123456")
    private String password;
    
    @ApiModelProperty(value = "手机号", required = true, example = "13800138000")
    private String phone;
    
    // getter和setter...
}

@ApiModel(value = "用户更新对象", description = "用户可更新的信息")
class UserUpdateDTO {
    @ApiModelProperty(value = "昵称", example = "张三")
    private String nickname;
    
    @ApiModelProperty(value = "头像URL", example = "http://example.com/avatar.jpg")
    private String avatar;
    
    // getter和setter...
}

@ApiModel(value = "Token返回对象", description = "登录成功后返回的token信息")
class TokenVO {
    @ApiModelProperty(value = "JWT Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
    
    public TokenVO(String token) {
        this.token = token;
    }
    
    // getter和setter...
}

@ApiModel(value = "用户信息对象", description = "用户详细信息")
class UserVO {
    @ApiModelProperty(value = "用户ID", example = "1")
    private Long id;
    
    @ApiModelProperty(value = "用户名", example = "zhangsan")
    private String username;
    
    @ApiModelProperty(value = "昵称", example = "张三")
    private String nickname;
    
    @ApiModelProperty(value = "头像URL", example = "http://example.com/avatar.jpg")
    private String avatar;
    
    @ApiModelProperty(value = "手机号", example = "138****8000")
    private String phone;
    
    // getter和setter...
}

// 统一返回结果
@ApiModel(value = "统一返回结果", description = "API统一返回结果")
class Result<T> {
    @ApiModelProperty(value = "状态码", example = "200")
    private Integer code;
    
    @ApiModelProperty(value = "返回消息", example = "操作成功")
    private String message;
    
    @ApiModelProperty(value = "返回数据")
    private T data;
    
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }
    
    public static Result success() {
        return success(null);
    }
    
    // getter和setter...
}
```

### 最佳实践

1. **分组管理**
   - 使用`@Api(tags = "xxx")`合理分组API
   - 复杂系统可以创建多个Docket对象进行分组

2. **统一响应**
   - 使用统一的响应对象包装返回结果
   - 明确定义错误码和错误信息

3. **完善文档信息**
   - 为每个接口提供详细的描述和注释
   - 为请求参数和返回值添加示例

4. **版本控制**
   - 通过配置区分不同版本的API
   - 为不同版本提供清晰的文档说明

5. **安全控制**
   - 在生产环境中考虑禁用或保护API文档
   - 使用Knife4j的Basic认证功能

## Swagger与Knife4j对比

| 特性 | Swagger | Knife4j |
| --- | --- | --- |
| 文档生成 | ✅ 支持 | ✅ 支持 |
| UI界面 | ✅ 基础界面 | ✅ 增强美观界面 |
| 接口调试 | ✅ 基础功能 | ✅ 增强功能 |
| 文档导出 | ❌ 不支持 | ✅ 支持Markdown/HTML/Word等 |
| 接口搜索 | ❌ 不支持 | ✅ 支持 |
| 中文支持 | ❌ 有限 | ✅ 完全支持 |
| 离线文档 | ❌ 不支持 | ✅ 支持 |
| 接口排序 | ❌ 有限 | ✅ 支持自定义排序 |
| 请求缓存 | ❌ 不支持 | ✅ 支持 |
| 参数说明增强 | ❌ 基础 | ✅ 增强 |

## 常见问题与解决方案

1. **Swagger2与SpringBoot 2.6.x不兼容问题**

SpringBoot 2.6.x改变了PathPattern的匹配策略，导致Swagger2无法正常工作。

解决方案：
```yaml
spring:
  mvc:
    pathmatch:
      matching-strategy: ant_path_matcher
```

2. **接口文档不显示**

可能原因：
- 配置问题
- 包路径扫描不正确
- SpringSecurity拦截

解决方案：
```java
// 放行Swagger相关路径
@Override
public void configure(WebSecurity web) throws Exception {
    web.ignoring()
       .antMatchers("/swagger-ui.html", "/swagger-resources/**", "/webjars/**", "/v2/**", "/doc.html");
}
```

3. **生产环境禁用Swagger/Knife4j**

```yaml
# Knife4j生产环境禁用
knife4j:
  production: true
```

或者通过配置类控制：
```java
@Configuration
@EnableSwagger2
@EnableKnife4j
@Profile({"dev", "test"}) // 只在开发和测试环境启用
public class SwaggerConfig {
    // ...
}
```

4. **自定义API排序**

使用Knife4j的`@ApiSupport`和`@ApiOperationSupport`注解控制排序：
```java
@Api(tags = "用户管理")
@ApiSupport(order = 1) // 控制类的排序
public class UserController {
    
    @ApiOperation("创建用户")
    @ApiOperationSupport(order = 1) // 控制方法的排序
    public void createUser() {
        // ...
    }
}
```

总结一下，Swagger和Knife4j都是优秀的API文档工具，Swagger更为国际化和通用，而Knife4j则针对国内开发者提供了更加友好的功能和界面。在实际项目中，特别是在国内的开发环境中，Knife4j通常是更好的选择，它在保持Swagger所有功能的同时，提供了更多的增强特性。 