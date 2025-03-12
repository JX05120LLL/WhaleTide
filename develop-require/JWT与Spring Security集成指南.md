# WhaleTide商城系统 - JWT与Spring Security集成指南

## 目录

- [1. 概述](#1-概述)
- [2. 技术栈](#2-技术栈)
- [3. JWT认证流程](#3-jwt认证流程)
- [4. 项目集成实现](#4-项目集成实现)
  - [4.1 Maven依赖](#41-maven依赖)
  - [4.2 JWT工具类实现](#42-jwt工具类实现)
  - [4.3 Security配置](#43-security配置)
  - [4.4 自定义过滤器](#44-自定义过滤器)
  - [4.5 认证和授权处理器](#45-认证和授权处理器)
  - [4.6 用户详情服务](#46-用户详情服务)
- [5. 接口安全控制](#5-接口安全控制)
  - [5.1 后台管理接口](#51-后台管理接口)
  - [5.2 用户前台接口](#52-用户前台接口)
- [6. 注解权限控制](#6-注解权限控制)
- [7. 常见问题与解决方案](#7-常见问题与解决方案)
- [8. 安全最佳实践](#8-安全最佳实践)

## 1. 概述

在WhaleTide商城系统中，我们使用JWT(JSON Web Token)结合Spring Security来实现身份验证和授权管理。该方案提供了无状态的认证机制，适合前后端分离的架构，同时满足了商城系统中不同角色(管理员、普通用户、商家)的权限控制需求。

JWT认证用于两个主要场景：
- 后台管理系统(admin-web)的管理员登录认证与权限控制
- 用户前台(mall-app-web)的用户登录认证与权限控制

## 2. 技术栈

- **Spring Boot**: 2.6.x
- **Spring Security**: 5.6.x
- **JJWT**: 0.9.1 (JWT生成和解析库)
- **MySQL**: 8.0 (用户与权限数据存储)
- **Redis**: 6.x (Token黑名单和刷新Token存储)

## 3. JWT认证流程

WhaleTide商城系统的JWT认证流程如下：

1. **用户登录**: 
   - 用户提交用户名/密码到认证接口(`/admin/login`或`/sso/login`)
   - 服务器验证凭据

2. **Token生成**:
   - 认证成功后，服务器生成JWT Token
   - Token包含用户ID、用户名、角色、权限等信息
   - 返回token给客户端

3. **Token使用**:
   - 客户端将token存储在本地(localStorage或Cookie)
   - 后续请求在`Authorization`请求头中携带token: `Bearer {token}`

4. **Token验证**:
   - 服务器通过JWT过滤器验证token有效性
   - 解析token并加载用户权限信息
   - 将用户信息存入`SecurityContext`

5. **Token刷新**:
   - token接近过期时，客户端可请求刷新token
   - 服务器验证旧token并签发新token

6. **Token注销**:
   - 用户登出时，客户端删除本地token
   - 服务器将token加入黑名单(使用Redis存储)

## 4. 项目集成实现

### 4.1 Maven依赖

在项目的`pom.xml`中添加以下依赖：

```xml
<!-- Spring Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- JWT -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.9.1</version>
</dependency>

<!-- Redis -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

### 4.2 JWT工具类实现

在`com.whale_tide.common.util`包下创建JWT工具类：

```java
package com.whale_tide.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {
    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    private static final String CLAIM_KEY_USER_ID = "userId";
    private static final String CLAIM_KEY_ROLES = "roles";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 生成JWT令牌
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        
        // 如果是我们自定义的UserDetails实现，可以添加更多信息
        if (userDetails instanceof AdminUserDetails) {
            AdminUserDetails adminUserDetails = (AdminUserDetails) userDetails;
            claims.put(CLAIM_KEY_USER_ID, adminUserDetails.getUserId());
            claims.put(CLAIM_KEY_ROLES, adminUserDetails.getRoles());
        }
        
        return generateToken(claims);
    }

    /**
     * 根据负载生成JWT令牌
     */
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 生成令牌过期时间
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 从令牌中获取用户名
     */
    public String getUsernameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 从令牌中获取用户ID
     */
    public Long getUserIdFromToken(String token) {
        Long userId;
        try {
            Claims claims = getClaimsFromToken(token);
            userId = claims.get(CLAIM_KEY_USER_ID, Long.class);
        } catch (Exception e) {
            userId = null;
        }
        return userId;
    }

    /**
     * 从令牌中获取JWT中的负载
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            // 解析失败处理
        }
        return claims;
    }

    /**
     * 验证令牌
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * 判断令牌是否已经失效
     */
    private boolean isTokenExpired(String token) {
        Date expiredDate = getExpiredDateFromToken(token);
        return expiredDate.before(new Date());
    }

    /**
     * 从令牌中获取过期时间
     */
    private Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 判断令牌是否可以被刷新
     */
    public boolean canRefresh(String token) {
        return !isTokenExpired(token);
    }

    /**
     * 刷新令牌
     */
    public String refreshToken(String token) {
        Claims claims = getClaimsFromToken(token);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }
}
```

### 4.3 Security配置

Spring Security配置类：

```java
package com.whale_tide.config;

import com.whale_tide.common.util.component.JwtAuthenticationTokenFilter;
import com.whale_tide.common.util.component.RestAuthenticationEntryPoint;
import com.whale_tide.common.util.component.RestfulAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()  // 由于使用JWT，不需要CSRF
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // 不使用Session
                .and()
                .authorizeRequests()
                // 允许对于静态资源的无授权访问
                .antMatchers(HttpMethod.GET, 
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/swagger-resources/**",
                        "/v2/api-docs/**"
                ).permitAll()
                // 登录注册相关接口放行
                .antMatchers("/admin/login", "/admin/register", "/sso/login").permitAll()
                // 首页和商品浏览功能放行
                .antMatchers(HttpMethod.GET, "/home/**", "/product/**", "/brand/**").permitAll()
                // 跨域请求会先进行一次OPTIONS请求
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                // 对于后台管理接口进行角色控制
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/ams/**").hasRole("ADMIN")
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();
        
        // 禁用缓存
        httpSecurity.headers().cacheControl();
        // 添加JWT filter
        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        // 添加自定义未授权和未登录结果返回
        httpSecurity.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .authenticationEntryPoint(restAuthenticationEntryPoint);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
        return new JwtAuthenticationTokenFilter();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
```

### 4.4 自定义过滤器

JWT认证过滤器，用于拦截请求并验证JWT Token：

```java
package com.whale_tide.common.util.component;

import com.whale_tide.common.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);
    
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Value("${jwt.blacklist.prefix}")
    private String blacklistPrefix;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        // 获取请求头中的Authorization值
        String authHeader = request.getHeader(this.tokenHeader);
        
        // 判断是否有token且以Bearer开头
        if (authHeader != null && authHeader.startsWith(this.tokenHead)) {
            // 截取"Bearer "后的JWT令牌
            String authToken = authHeader.substring(this.tokenHead.length());
            
            // 检查token是否在黑名单中
            boolean isBlacklisted = stringRedisTemplate.hasKey(blacklistPrefix + authToken);
            if (isBlacklisted) {
                chain.doFilter(request, response);
                return;
            }
            
            // 从token中获取用户名
            String username = jwtTokenUtil.getUsernameFromToken(authToken);
            LOGGER.info("checking username:{}", username);
            
            // 如果能获取到用户名且当前SecurityContext中没有认证信息
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 根据用户名加载用户信息
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                
                // 验证token是否有效
                if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                    // 将用户信息和权限放入UsernamePasswordAuthenticationToken中
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    LOGGER.info("authenticated user:{}", username);
                    
                    // 将认证信息放入SecurityContext中
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        chain.doFilter(request, response);
    }
}
```

### 4.5 认证和授权处理器

当用户无权限或未认证时的处理：

```java
package com.whale_tide.common.util.component;

import cn.hutool.json.JSONUtil;
import com.whale_tide.common.api.CommonResult;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义返回结果：用户没有权限访问时
 */
@Component
public class RestfulAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException e) throws IOException, ServletException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(JSONUtil.parse(CommonResult.forbidden(e.getMessage())));
        response.getWriter().flush();
    }
}
```

```java
package com.whale_tide.common.util.component;

import cn.hutool.json.JSONUtil;
import com.whale_tide.common.api.CommonResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义返回结果：未登录或登录过期
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, 
                         HttpServletResponse response, 
                         AuthenticationException authException) throws IOException, ServletException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(JSONUtil.parse(CommonResult.unauthorized(authException.getMessage())));
        response.getWriter().flush();
    }
}
```

### 4.6 用户详情服务

实现Spring Security的UserDetailsService接口：

**后台管理员实现：**

```java
package com.whale_tide.service.impl;

import com.whale_tide.entity.AmsAdmin;
import com.whale_tide.entity.AmsResource;
import com.whale_tide.entity.AmsRole;
import com.whale_tide.mapper.AmsAdminMapper;
import com.whale_tide.service.AmsAdminRoleRelationService;
import com.whale_tide.service.AmsAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 后台管理员UserDetailsService实现
 */
@Service
public class AdminUserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private AmsAdminService adminService;
    @Autowired
    private AmsAdminRoleRelationService adminRoleRelationService;
    @Autowired
    private AmsAdminMapper adminMapper;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 获取管理员信息
        AmsAdmin admin = adminService.getAdminByUsername(username);
        if (admin == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        
        // 获取管理员角色
        List<AmsRole> roleList = adminRoleRelationService.getRoleListByAdminId(admin.getId());
        
        // 获取管理员权限资源
        List<AmsResource> resourceList = adminRoleRelationService.getResourceListByAdminId(admin.getId());
        
        // 创建并返回SpringSecurity所需的UserDetails对象
        return new AdminUserDetails(admin, roleList, resourceList);
    }
}
```

**普通用户实现：**

```java
package com.whale_tide.service.impl;

import com.whale_tide.entity.UmsUser;
import com.whale_tide.mapper.UmsUserMapper;
import com.whale_tide.service.UmsUserRoleService;
import com.whale_tide.service.UmsUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 前台用户UserDetailsService实现
 */
@Service("memberUserDetailsService")
public class MemberUserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UmsUserService umsUserService;
    @Autowired
    private UmsUserRoleService umsUserRoleService;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 获取用户信息
        UmsUser user = umsUserService.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        
        // 创建并返回SpringSecurity所需的UserDetails对象
        return new MemberUserDetails(user);
    }
}
```

**用户详情类**：

```java
package com.whale_tide.domain;

import com.whale_tide.entity.AmsAdmin;
import com.whale_tide.entity.AmsResource;
import com.whale_tide.entity.AmsRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SpringSecurity需要的用户详情
 */
public class AdminUserDetails implements UserDetails {
    private AmsAdmin admin;
    private List<AmsRole> roleList;
    private List<AmsResource> resourceList;

    public AdminUserDetails(AmsAdmin admin, List<AmsRole> roleList, List<AmsResource> resourceList) {
        this.admin = admin;
        this.roleList = roleList;
        this.resourceList = resourceList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 返回角色权限
        List<SimpleGrantedAuthority> roleAuthorities = roleList.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getCode()))
                .collect(Collectors.toList());
        
        // 返回资源权限
        List<SimpleGrantedAuthority> resourceAuthorities = resourceList.stream()
                .map(resource -> new SimpleGrantedAuthority(resource.getUrl()))
                .collect(Collectors.toList());
        
        // 合并权限列表
        roleAuthorities.addAll(resourceAuthorities);
        return roleAuthorities;
    }

    @Override
    public String getPassword() {
        return admin.getPassword();
    }

    @Override
    public String getUsername() {
        return admin.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return admin.getStatus().equals(1);
    }
    
    public Long getUserId() {
        return admin.getId();
    }
    
    public List<String> getRoles() {
        return roleList.stream().map(AmsRole::getCode).collect(Collectors.toList());
    }
}
```

## 5. 接口安全控制

在WhaleTide项目中，我们对接口的安全控制分为两类：

### 5.1 后台管理接口

后台管理接口主要基于角色和权限进行控制：

1. **基于URL的访问控制**：
   - 在SecurityConfig中配置URL级别的权限控制
   - 例如：`.antMatchers("/admin/**").hasRole("ADMIN")`

2. **基于方法的权限控制**：
   - 使用`@PreAuthorize`注解控制方法访问权限
   - 例如：`@PreAuthorize("hasRole('ADMIN')")`

```java
@RestController
@RequestMapping("/admin")
public class AdminController {
    
    // 需要ADMIN角色才能访问
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    public CommonResult<List<AmsAdmin>> list() {
        // 实现
    }
    
    // 需要ADMIN角色且有特定权限才能访问
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('/admin/update')")
    @PostMapping("/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody AmsAdmin admin) {
        // 实现
    }
}
```

### 5.2 用户前台接口

前台接口主要关注用户认证和个人资源访问权限：

1. **公开接口**：无需认证即可访问
   - 商品浏览
   - 首页内容
   - 品牌信息

2. **需认证接口**：用户登录后才能访问
   - 购物车操作
   - 订单管理
   - 个人信息管理

```java
@RestController
@RequestMapping("/cart")
public class CartController {
    
    // 添加商品到购物车(需要用户登录)
    @PostMapping("/add")
    public CommonResult add(@RequestBody CartItemParam param) {
        // 从SecurityContext获取当前用户ID
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = ((MemberUserDetails) userDetails).getUserId();
        
        // 业务实现
    }
}
```

## 6. 注解权限控制

Spring Security提供了丰富的注解用于权限控制，在WhaleTide项目中主要使用以下注解：

1. **@PreAuthorize**：方法执行前进行权限校验

```java
// 需要ADMIN角色
@PreAuthorize("hasRole('ADMIN')")

// 需要特定权限
@PreAuthorize("hasAuthority('/product/update')")

// 组合条件
@PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_ADMIN')")

// 使用SpEL表达式进行复杂判断
@PreAuthorize("authentication.principal.username == #username")
```

2. **@PostAuthorize**：方法执行后进行权限校验，可以基于返回值进行判断

```java
@PostAuthorize("returnObject.userId == authentication.principal.userId")
public Order getOrderById(Long id) {
    // 实现
}
```

3. **@Secured**：指定角色列表

```java
@Secured({"ROLE_ADMIN", "ROLE_PRODUCT_ADMIN"})
public void updateProduct() {
    // 实现
}
```

## 7. 常见问题与解决方案

1. **Token过期处理**
   - 问题：Token过期后用户需要重新登录，影响用户体验
   - 解决方案：实现Token刷新机制，在Token即将过期时自动刷新

```java
@RestController
@RequestMapping("/sso")
public class RefreshTokenController {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @GetMapping("/refreshToken")
    public CommonResult refreshToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        if(jwtTokenUtil.canRefresh(token)){
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return CommonResult.success(refreshedToken);
        }
        return CommonResult.failed("Token不能被刷新");
    }
}
```

2. **跨域请求问题**
   - 问题：前后端分离架构中，跨域请求被安全策略阻止
   - 解决方案：配置全局CORS支持

```java
@Configuration
public class GlobalCorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.setAllowCredentials(true);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
```

3. **登出处理**
   - 问题：JWT是无状态的，服务器无法直接使Token失效
   - 解决方案：使用Redis存储黑名单

```java
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    @Value("${jwt.blacklist.prefix}")
    private String blacklistPrefix;
    
    @Value("${jwt.expiration}")
    private Long expiration;
    
    @PostMapping("/logout")
    public CommonResult logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        // 将token加入黑名单，存活时间为token剩余有效期
        redisTemplate.opsForValue().set(blacklistPrefix + token, "1", expiration, TimeUnit.SECONDS);
        return CommonResult.success(null);
    }
}
```

## 8. 安全最佳实践

在WhaleTide项目中实施的安全最佳实践：

1. **敏感数据加密**：
   - 密码使用BCrypt加密存储
   - JWT Secret妥善保存在配置文件中

2. **HTTPS传输**：
   - 生产环境强制使用HTTPS
   - 配置SSL/TLS证书

3. **令牌安全**：
   - 设置合理的Token有效期(一般1-2小时)
   - 实现Token刷新机制
   - 将敏感信息从Token中剔除

4. **权限粒度控制**：
   - 实现细粒度的权限控制
   - 遵循最小权限原则

5. **安全日志**：
   - 记录关键安全事件
   - 实现登录审计

```java
@Aspect
@Component
public class SecurityAuditAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityAuditAspect.class);
    
    @Pointcut("execution(* com.whale_tide.controller.*.*(..)) && @annotation(org.springframework.security.access.prepost.PreAuthorize)")
    public void securityPointcut() {}
    
    @Around("securityPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String method = joinPoint.getSignature().toShortString();
        LOGGER.info("User [{}] accessing secured method: {}", username, method);
        return joinPoint.proceed();
    }
}
```

6. **防止常见攻击**：
   - XSS防护：输入输出过滤
   - CSRF防护：使用Token认证自然隔离
   - SQL注入防护：使用MyBatis参数化查询

7. **API限流**：
   - 使用Redis或Spring Cloud Gateway实现API限流

```java
@Component
public class RateLimiterInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    @Value("${rate.limit.enabled}")
    private boolean enabled;
    
    @Value("${rate.limit.requests}")
    private int maxRequests;
    
    @Value("${rate.limit.period}")
    private int period;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!enabled) {
            return true;
        }
        
        String ip = request.getRemoteAddr();
        String key = "rate:limit:" + ip;
        
        Long count = redisTemplate.opsForValue().increment(key, 1);
        if (count == 1) {
            redisTemplate.expire(key, period, TimeUnit.SECONDS);
        }
        
        if (count > maxRequests) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            return false;
        }
        
        return true;
    }
}
```

以上为WhaleTide商城系统的JWT与Spring Security集成指南，详细介绍了项目中的身份验证和授权管理实现方式。通过按照本文档进行实施，您可以为商城系统添加完善的安全保障。 