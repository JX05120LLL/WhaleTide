package com.whale_tide.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.whale_tide.common.exception.auth.TokenExpiredException;
import com.whale_tide.common.exception.auth.TokenInvalidException;
import com.whale_tide.config.TokenConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 * 用于生成、解析和验证JWT Token
 */
@Slf4j
@Component
public class JwtUtil {

    private final TokenConfig tokenConfig;

    @Autowired
    public JwtUtil(TokenConfig tokenConfig) {
        this.tokenConfig = tokenConfig;
    }

    /**
     * 生成JWT Token
     * @param username 用户名
     * @return JWT Token字符串
     */
    public String generateToken(String username) {
        return generateToken(username, null);
    }
    
    /**
     * 生成JWT Token，可附加额外的声明
     * @param username 用户名
     * @param additionalClaims 附加的声明信息
     * @return JWT Token字符串
     */
    public String generateToken(String username, Map<String, Object> additionalClaims) {
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + tokenConfig.getExpireHours() * 3600 * 1000);
        
        com.auth0.jwt.JWTCreator.Builder builder = JWT.create()
                .withSubject(username)
                .withIssuedAt(now)
                .withExpiresAt(expiresAt);
        
        // 添加额外的声明信息
        if (additionalClaims != null && !additionalClaims.isEmpty()) {
            additionalClaims.forEach((key, value) -> {
                if (value instanceof String) {
                    builder.withClaim(key, (String) value);
                } else if (value instanceof Integer) {
                    builder.withClaim(key, (Integer) value);
                } else if (value instanceof Long) {
                    builder.withClaim(key, (Long) value);
                } else if (value instanceof Boolean) {
                    builder.withClaim(key, (Boolean) value);
                } else if (value instanceof Double) {
                    builder.withClaim(key, (Double) value);
                } else if (value instanceof Date) {
                    builder.withClaim(key, (Date) value);
                }
            });
        }
        
        return builder.sign(Algorithm.HMAC256(tokenConfig.getSecret()));
    }

    /**
     * 解析JWT Token
     * @param token JWT Token字符串
     * @return 解码后的JWT对象
     * @throws TokenInvalidException 当token无效时抛出
     * @throws TokenExpiredException 当token过期时抛出
     */
    public DecodedJWT parseToken(String token) {
        try {
            // 输出token信息用于调试
            log.debug("开始解析token: {}, 长度: {}", token, token != null ? token.length() : 0);
            
            if (token == null || token.trim().isEmpty()) {
                log.warn("Token为空");
                throw new TokenInvalidException("Token不能为空");
            }
            
            // 移除Bearer前缀
            if (token.startsWith("Bearer ")) {
                token = token.substring(7).trim();
                log.debug("移除Bearer前缀后的token: {}", token);
            }
            
            // 检查token格式
            if (!token.contains(".") || token.split("\\.").length != 3) {
                log.warn("Token格式不正确，不是标准的JWT格式: {}", token);
                throw new TokenInvalidException("Token格式不正确，不是有效的JWT格式");
            }
            
            // 创建JWT验证器并验证token
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(tokenConfig.getSecret())).build();
            
            // 尝试验证token
            try {
                return verifier.verify(token);
            } catch (com.auth0.jwt.exceptions.JWTDecodeException e) {
                log.warn("Token解码失败: {}", e.getMessage());
                
                // 尝试修复Base64编码问题
                String[] parts = token.split("\\.");
                if (parts.length == 3) {
                    log.info("尝试修复token Base64编码");
                    
                    // 保持header和payload不变，只修改签名部分
                    String header = parts[0];
                    String payload = parts[1];
                    String fixedToken = header + "." + payload + "." + parts[2];
                    
                    try {
                        return verifier.verify(fixedToken);
                    } catch (Exception ex) {
                        log.warn("修复尝试失败: {}", ex.getMessage());
                        throw new TokenInvalidException("Token解码失败: " + e.getMessage());
                    }
                } else {
                    throw new TokenInvalidException("Token格式不正确: " + e.getMessage());
                }
            }
        } catch (com.auth0.jwt.exceptions.TokenExpiredException e) {
            log.warn("Token已过期: {}", e.getMessage());
            throw new TokenExpiredException("Token已过期，请重新登录");
        } catch (com.auth0.jwt.exceptions.SignatureVerificationException e) {
            log.warn("Token签名验证失败: {}", e.getMessage());
            throw new TokenInvalidException("Token签名验证失败");
        } catch (com.auth0.jwt.exceptions.AlgorithmMismatchException e) {
            log.warn("Token算法不匹配: {}", e.getMessage());
            throw new TokenInvalidException("Token算法不匹配");
        } catch (com.auth0.jwt.exceptions.InvalidClaimException e) {
            log.warn("Token声明无效: {}", e.getMessage());
            throw new TokenInvalidException("Token声明无效");
        } catch (JWTVerificationException e) {
            log.warn("Token验证失败: {}", e.getMessage());
            throw new TokenInvalidException("无效的Token: " + e.getMessage());
        } catch (Exception e) {
            log.error("解析Token时发生未知错误: {}", e.getMessage(), e);
            throw new TokenInvalidException("解析Token时发生未知错误: " + e.getMessage());
        }
    }

    /**
     * 验证JWT Token是否有效
     * @param token JWT Token字符串
     * @return 是否有效
     */
    public boolean verify(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 从JWT Token中获取用户名
     * @param token JWT Token字符串
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        DecodedJWT decodedJWT = parseToken(token);
        return decodedJWT.getSubject();
    }
    
    /**
     * 从JWT Token中获取声明
     * @param token JWT Token字符串
     * @param claimName 声明名称
     * @return 声明值
     */
    public String getClaimFromToken(String token, String claimName) {
        DecodedJWT decodedJWT = parseToken(token);
        return decodedJWT.getClaim(claimName).asString();
    }
    
    /**
     * 检查JWT Token是否过期
     * @param token JWT Token字符串
     * @return 是否过期
     */
    public boolean isTokenExpired(String token) {
        try {
            DecodedJWT decodedJWT = parseToken(token);
            return decodedJWT.getExpiresAt().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
    
    /**
     * 刷新JWT Token
     * @param token 原JWT Token字符串
     * @return 新的JWT Token字符串
     */
    public String refreshToken(String token) {
        try {
            DecodedJWT decodedJWT = parseToken(token);
            String username = decodedJWT.getSubject();
            
            // 保留原token中的自定义声明
            Map<String, Object> claims = new HashMap<>();
            decodedJWT.getClaims().forEach((key, claim) -> {
                if (!key.equals("sub") && !key.equals("iat") && !key.equals("exp")) {
                    if (claim.asString() != null) {
                        claims.put(key, claim.asString());
                    } else if (claim.asLong() != null) {
                        claims.put(key, claim.asLong());
                    } else if (claim.asInt() != null) {
                        claims.put(key, claim.asInt());
                    } else if (claim.asBoolean() != null) {
                        claims.put(key, claim.asBoolean());
                    } else if (claim.asDouble() != null) {
                        claims.put(key, claim.asDouble());
                    } else if (claim.asDate() != null) {
                        claims.put(key, claim.asDate());
                    }
                }
            });
            
            return generateToken(username, claims);
        } catch (Exception e) {
            throw new TokenInvalidException("无法刷新无效的Token");
        }
    }
}