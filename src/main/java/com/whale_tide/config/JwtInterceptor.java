package com.whale_tide.config;

import com.whale_tide.common.exception.auth.TokenExpiredException;
import com.whale_tide.common.exception.auth.TokenInvalidException;
import com.whale_tide.common.exception.auth.UnauthorizedException;
import com.whale_tide.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT拦截器
 */
@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求头获取Authorization
        String authHeader = request.getHeader(tokenHeader);
        log.debug("JWT拦截器检查请求: {} - Authorization: {}", request.getRequestURI(), authHeader);

        // 判断是否存在token
        if (!StringUtils.hasText(authHeader)) {
            log.warn("请求没有携带token - URL: {}, IP: {}", request.getRequestURI(), getIpAddress(request));
            throw new UnauthorizedException("未授权，请先登录");
        }
        
        // 移除Bearer前缀
        String token = authHeader;
        if (authHeader.startsWith(tokenHead)) {
            token = authHeader.substring(tokenHead.trim().length()).trim();
        }
        
        // 验证token
        try {
            if (!jwtUtil.verify(token)) {
                log.warn("token无效 - URL: {}, IP: {}", request.getRequestURI(), getIpAddress(request));
                throw new TokenInvalidException("token无效，请重新登录");
            }
        } catch (Exception e) {
            if (e instanceof TokenInvalidException || e instanceof UnauthorizedException) {
                throw e;
            }
            // 其他验证异常，可能是token过期
            log.warn("token验证异常 - URL: {}, IP: {}, 异常: {}", request.getRequestURI(), getIpAddress(request), e.getMessage());
            throw new TokenExpiredException("token已过期，请重新登录");
        }
        
        // 将用户名放入请求属性，方便后续使用
        String username = jwtUtil.getUsernameFromToken(token);
        request.setAttribute("username", username);
        log.debug("JWT验证通过，用户: {}", username);
        
        return true;
    }
    
    /**
     * 获取请求的真实IP地址
     */
    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (!StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (!StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (!StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 多个代理的情况下，第一个IP为客户端真实IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
