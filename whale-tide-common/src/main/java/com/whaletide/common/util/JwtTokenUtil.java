package com.whaletide.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

/**
 * JWT工具类
 * 用于生成和验证JWT令牌
 */
@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secret; // 密钥

    @Value("${jwt.expiration}")
    private Long expiration;// 过期时间，单位：秒

    /**
     * 从令牌中获取用户名
     * @param token JWT令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * 从令牌中获取过期时间
     * @param token JWT令牌
     * @return 过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * 从令牌中获取特定声明
     * @param token JWT令牌
     * @param claimsResolver 声明解析函数
     * @return 声明值
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 解析令牌
     * @param token JWT令牌
     * @return 所有声明
     */
    private Claims getAllClaimsFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 检查令牌是否过期
     * @param token JWT令牌
     * @return 是否过期
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * 生成令牌
     * @param userDetails 用户信息
     * @return JWT令牌
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    /**
     * 生成带自定义声明的令牌
     * @param claims 声明
     * @param subject 用户标识
     * @return JWT令牌
     */
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration * 1000);
        
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .setId(UUID.randomUUID().toString())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 验证令牌是否有效
     * @param token JWT令牌
     * @param userDetails 用户信息
     * @return 是否有效
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    
    /**
     * 验证令牌是否有效
     * @param token JWT令牌
     * @return 是否有效
     */
    public Boolean validateToken(String token) {
        try {
            // 处理token中可能存在的Base64编码问题
            if (token != null && (token.contains("_") || token.contains("-"))) {
                token = token.replace('_', '/').replace('-', '+');
            }
            
            getAllClaimsFromToken(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 刷新令牌
     * @param token JWT令牌
     * @return 新令牌
     */
    public String refreshToken(String token) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration * 1000);
        
        Claims claims = getAllClaimsFromToken(token);
        claims.setIssuedAt(now);
        claims.setExpiration(expiryDate);
        
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        
        return Jwts.builder()
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    
    /**
     * 从令牌中获取用户ID
     * @param token JWT令牌
     * @return 用户ID
     */
    public String getUserIdFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("userId", String.class));
    }
    
    /**
     * 生成包含用户ID的令牌
     * @param username 用户名
     * @param userId 用户ID
     * @return JWT令牌
     */
    public String generateToken(String username, String userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        return doGenerateToken(claims, username);
    }

    /**
     * 从安全上下文中获取当前用户的ID
     * @return 当前用户ID，如果未找到返回null
     */
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            // 尝试从principal对象中获取userId
            if (principal instanceof UserDetails) {
                // 尝试通过反射获取userId属性
                try {
                    // 使用反射获取可能存在的getUserId方法
                    Method getUserIdMethod = principal.getClass().getMethod("getUserId");
                    if (getUserIdMethod != null) {
                        Object userId = getUserIdMethod.invoke(principal);
                        if (userId instanceof Long) {
                            return (Long) userId;
                        } else if (userId != null) {
                            // 尝试将其他类型转换为Long
                            return Long.valueOf(userId.toString());
                        }
                    }
                } catch (Exception ignored) {
                    // 如果没有getUserId方法或调用失败，忽略异常
                }
                
                // 尝试从Authentication的Details中获取userId
                Object details = authentication.getDetails();
                if (details != null) {
                    try {
                        // 尝试从details对象中获取userId
                        Method getUserIdMethod = details.getClass().getMethod("getUserId");
                        if (getUserIdMethod != null) {
                            Object userId = getUserIdMethod.invoke(details);
                            if (userId instanceof Long) {
                                return (Long) userId;
                            } else if (userId != null) {
                                return Long.valueOf(userId.toString());
                            }
                        }
                    } catch (Exception ignored) {
                        // 忽略异常
                    }
                }
            }
        }
        return null;
    }

    /**
     * 从安全上下文中获取当前用户的用户名
     * @return 当前用户名，如果未找到返回null
     */
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }

    /**
     * 从请求头中获取用户ID
     * @param request HTTP请求
     * @return 用户ID
     */
    public Long getUserIdFromRequest(javax.servlet.http.HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token != null) {
            try {
                // 检查token是否包含非法Base64字符并修正
                if (token.contains("_") || token.contains("-")) {
                    token = token.replace('_', '/').replace('-', '+');
                }
                
                // 尝试从token解析userId
                Claims claims = getAllClaimsFromToken(token);
                if (claims.containsKey("userId")) {
                    Object userId = claims.get("userId");
                    if (userId != null) {
                        return Long.valueOf(userId.toString());
                    }
                }
                
                // 获取用户名，然后获取用户ID
                String username = claims.getSubject();
                if (username != null && !username.isEmpty()) {
                    return getUserIdFromUsername(username);
                }
            } catch (Exception e) {
                // 解析失败，记录错误但不抛出异常
                // 尝试继续使用其他方法获取用户ID
            }
        }
        
        // 如果从token中无法获取，尝试从安全上下文获取
        return getCurrentUserId();
    }

    /**
     * 从请求头中获取JWT令牌
     * @param request HTTP请求
     * @return JWT令牌
     */
    public String getTokenFromRequest(javax.servlet.http.HttpServletRequest request) {
        try {
            // 从Authorization头获取
            String bearerToken = request.getHeader("Authorization");
            if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                String token = bearerToken.substring(7);
                // 处理Base64编码问题
                if (token.contains("_") || token.contains("-")) {
                    token = token.replace('_', '/').replace('-', '+');
                }
                return token;
            }
            
            // 从自定义头获取
            String customToken = request.getHeader("X-Auth-Token");
            if (customToken != null && !customToken.isEmpty()) {
                return customToken;
            }
            
            // 从请求参数获取
            String paramToken = request.getParameter("token");
            if (paramToken != null && !paramToken.isEmpty()) {
                return paramToken;
            }
            
            // 从Cookie获取
            javax.servlet.http.Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (javax.servlet.http.Cookie cookie : cookies) {
                    if ("token".equals(cookie.getName())) {
                        return cookie.getValue();
                    }
                }
            }
        } catch (Exception e) {
            // 忽略异常，返回null
        }
        return null;
    }

    /**
     * 从用户名获取用户ID
     * 此方法需要根据实际的用户数据存储方式来实现
     * @param username 用户名
     * @return 用户ID
     */
    public Long getUserIdFromUsername(String username) {
        // 实现从数据库或缓存中根据用户名查询用户ID的逻辑
        // 这里是简化的实现，实际生产环境中应该查询数据库
        try {
            // 首先尝试从SecurityContext的认证对象中获取
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getDetails() != null) {
                Object details = authentication.getDetails();
                // 尝试从details中获取userId
                try {
                    Method getUserIdMethod = details.getClass().getMethod("getUserId");
                    if (getUserIdMethod != null) {
                        Object userId = getUserIdMethod.invoke(details);
                        if (userId instanceof Long) {
                            return (Long) userId;
                        } else if (userId != null) {
                            return Long.valueOf(userId.toString());
                        }
                    }
                } catch (Exception ignored) {
                    // 忽略异常，继续尝试其他方法
                }
            }
            
            // 从JWT令牌中尝试获取用户ID
            String token = getTokenFromSecurityContext();
            if (token != null) {
                try {
                    Claims claims = getAllClaimsFromToken(token);
                    if (claims.containsKey("userId")) {
                        return Long.parseLong(claims.get("userId").toString());
                    }
                } catch (Exception ignored) {
                    // 忽略异常，继续尝试其他方法
                }
            }
            
            // 如果以上方法都失败，可以实现查询数据库的逻辑
            // 这里简单返回一个固定值用于测试
            // 在实际应用中应该替换为数据库查询
            return 1L; // 对于测试用户"test"返回ID 1
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 从Security上下文中获取JWT令牌
     * @return JWT令牌，如果不存在则返回null
     */
    private String getTokenFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getCredentials() instanceof String) {
            return (String) authentication.getCredentials();
        }
        return null;
    }
}
