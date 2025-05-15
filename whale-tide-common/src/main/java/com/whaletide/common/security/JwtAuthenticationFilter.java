package com.whaletide.common.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * JWT认证过滤器
 * 处理请求中的JWT令牌
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    
    // 定义不需要验证JWT令牌的公开API路径
    private final List<String> publicPaths = Arrays.asList(
        // 产品相关公开API
        "/api/product/detail/**",
        "/api/product/comment/list/**",
        "/api/product/related/**",
        "/api/product/categories",
        "/api/product/search",
        "/api/product/hotKeywords",
        "/api/product/suggestions",
        "/api/product/brands",
        "/api/product/category/**/products",
        
        // 用户认证相关公开API
        "/api/user/login",
        "/api/user/register",
        
        // 首页相关公开API
        "/api/home/**",
        
        // 订单相关公开API
        "/api/order/**"
    );

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        
        // 检查请求是否为公开API路径
        if (isPublicPath(path)) {
            log.debug("公开访问路径: {}, 跳过JWT认证", path);
            filterChain.doFilter(request, response);
            return;
        }
        
        try {
            // 优先从请求头获取token
            String token = jwtTokenProvider.resolveToken(request);
            
            // 如果请求头中没有token，尝试从URL参数中获取
            if (token == null) {
                token = request.getParameter("token");
                log.debug("从URL参数中获取令牌: {}", token);
            }
            
            if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
                log.debug("已设置用户'{}'认证信息到SecurityContext", auth.getName());
            } else {
                log.debug("请求中未找到有效令牌或令牌无效");
            }
        } catch (Exception e) {
            log.error("无法设置用户认证: {}", e.getMessage(), e);
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
    
    /**
     * 检查请求路径是否为公开API路径
     */
    private boolean isPublicPath(String requestPath) {
        return publicPaths.stream().anyMatch(pattern -> pathMatcher.match(pattern, requestPath));
    }
} 