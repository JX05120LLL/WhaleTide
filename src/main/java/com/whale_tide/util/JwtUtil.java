package com.whale_tide.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.whale_tide.config.TokenConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private final TokenConfig tokenConfig;

    @Autowired
    public JwtUtil(TokenConfig tokenConfig) {
        this.tokenConfig = tokenConfig;
    }

    // 生成 JWT
    public String generateToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + tokenConfig.getExpireHours() * 3600 * 1000))
                .sign(Algorithm.HMAC256(tokenConfig.getSecret()));
    }

    // 解析 JWT
    public DecodedJWT parseToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(tokenConfig.getSecret())).build();
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new RuntimeException("Invalid token");
        }
    }

    // 验证 JWT
    public boolean verify(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 从 JWT 中获取用户名
    public String getUsernameFromToken(String token) {
        DecodedJWT decodedJWT = parseToken(token);
        return decodedJWT.getSubject();
    }
}