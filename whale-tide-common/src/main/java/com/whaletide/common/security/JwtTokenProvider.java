package com.whaletide.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT令牌提供者
 * 负责JWT的创建、解析和验证
 */
@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretString;
    
    @Value("${jwt.expiration}")
    private long expiration;
    
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    
    @Value("${jwt.tokenPrefix}")
    private String tokenPrefix;
    
    private Key secretKey;
    
    private final UserDetailsService userDetailsService;
    
    private final StringRedisTemplate redisTemplate;
    
    @Autowired
    public JwtTokenProvider(UserDetailsService userDetailsService, StringRedisTemplate redisTemplate) {
        this.userDetailsService = userDetailsService;
        this.redisTemplate = redisTemplate;
    }
    
    @PostConstruct
    protected void init() {
        // 支持短密钥，使用HS512算法并对密钥进行填充处理
        // 将字符串密钥转换为字节数组
        byte[] keyBytes = secretString.getBytes(StandardCharsets.UTF_8);
        
        // 创建一个适配HS256算法的密钥
        // 如果原始密钥太短，可以通过重复或填充来扩展它
        this.secretKey = new SecretKeySpec(extendKey(keyBytes, 64), SignatureAlgorithm.HS512.getJcaName());
    }
    
    /**
     * 扩展密钥长度到指定大小
     * @param originalKey 原始密钥
     * @param targetSize 目标大小
     * @return 扩展后的密钥
     */
    private byte[] extendKey(byte[] originalKey, int targetSize) {
        if (originalKey.length >= targetSize) {
            return originalKey;
        }
        
        byte[] newKey = new byte[targetSize];
        // 重复复制原始密钥直到达到目标大小
        int copied = 0;
        while (copied < targetSize) {
            int toCopy = Math.min(originalKey.length, targetSize - copied);
            System.arraycopy(originalKey, 0, newKey, copied, toCopy);
            copied += toCopy;
        }
        return newKey;
    }
    
    /**
     * 从令牌中获取用户名
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
    
    /**
     * 从令牌中获取过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    
    /**
     * 从令牌中获取指定声明
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    
    /**
     * 解析令牌，获取所有声明
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    /**
     * 检查令牌是否过期
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    
    /**
     * 为指定用户生成令牌
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }
    
    /**
     * 创建令牌
     */
    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration * 1000);
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }
    
    /**
     * 验证令牌
     */
    public boolean validateToken(String token) {
        try {
            log.debug("开始验证令牌");
            
            // 检查token是否在黑名单中
            Boolean isBlacklisted = redisTemplate.hasKey("blacklist:token:" + token);
            if (Boolean.TRUE.equals(isBlacklisted)) {
                log.warn("令牌已被加入黑名单");
                return false;
            }
            
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            
            log.debug("令牌解析成功，主题: {}", claims.getBody().getSubject());
            
            // 检查令牌是否过期
            boolean expired = isTokenExpired(token);
            if (expired) {
                log.warn("令牌已过期");
                return false;
            }
            
            return true;
        } catch (JwtException e) {
            log.error("JWT令牌验证失败: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            log.error("JWT令牌参数无效: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("JWT令牌验证过程中发生未知异常: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 从请求头获取令牌
     */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(tokenHeader);
        log.debug("从请求头获取令牌: {}", bearerToken);
        
        if (bearerToken != null && bearerToken.startsWith(tokenPrefix)) {
            String token = bearerToken.substring(tokenPrefix.length());
            log.debug("提取带前缀令牌: {}", token.substring(0, Math.min(token.length(), 15)) + "...");
            return token;
        }
        
        // 支持直接传递token的情况（不带Bearer前缀）
        if (bearerToken != null && !bearerToken.isEmpty()) {
            log.debug("检测到未带前缀的令牌: {}", bearerToken.substring(0, Math.min(bearerToken.length(), 15)) + "...");
            return bearerToken;
        }
        
        log.debug("请求中未找到有效令牌");
        return null;
    }
    
    /**
     * 根据令牌获取认证信息
     */
    public Authentication getAuthentication(String token) {
        try {
            String username = getUsernameFromToken(token);
            log.debug("从令牌中获取用户名: {}", username);
            
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (userDetails == null) {
                log.error("用户详情为空: {}", username);
                throw new UsernameNotFoundException("用户不存在: " + username);
            }
            
            log.debug("用户 '{}' 认证成功", username);
            return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        } catch (Exception e) {
            log.error("从令牌获取认证信息时出错: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 获取令牌过期时间（秒）
     * @return 令牌过期时间（秒）
     */
    public Long getExpirationSeconds(){
        return expiration;
    }
} 