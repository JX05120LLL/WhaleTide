package com.whaletide.client.cart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.whaletide.client.cart.dto.CartAddDTO;
import com.whaletide.client.cart.dto.CartUpdateDTO;
import com.whaletide.client.cart.service.ICartService;
import com.whaletide.client.cart.vo.CartItemVO;
import com.whaletide.common.entity.oms.OmsCartItems;
import com.whaletide.common.entity.pms.PmsProducts;
import com.whaletide.common.exception.base.BusinessException;
import com.whaletide.common.exception.base.ValidationException;
import com.whaletide.common.exception.product.ProductNotFoundException;
import com.whaletide.common.mapper.oms.OmsCartItemsMapper;
import com.whaletide.common.mapper.pms.PmsProductsMapper;
import com.whaletide.common.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * 购物车服务实现类
 */
@Service
@Slf4j
public class CartServiceImpl implements ICartService {

    @Autowired
    private OmsCartItemsMapper cartItemsMapper;

    @Autowired
    private PmsProductsMapper productsMapper;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.secret}")
    private String jwtSecret;

    /**
     * 添加商品到购物车
     */
    @Override
    @Transactional
    public Boolean add(CartAddDTO cartAddDTO) {
            // 参数校验
            if (cartAddDTO == null) {
            throw new ValidationException("购物车信息不能为空");
            }

            // 获取当前用户ID
            Long userId = getCurrentUserId();
            if (userId == null) {
            throw new BusinessException("用户未登录");
            }

            // 校验商品是否存在
            PmsProducts product = productsMapper.selectById(cartAddDTO.getProductId());
            if (product == null) {
            throw new ProductNotFoundException("商品不存在");
            }

            // 校验商品状态
            if (product.getPublishStatus() != 1) {
            throw new ValidationException("商品已下架");
            }

            // 校验库存
        if (product.getStock() < cartAddDTO.getQuantity()) {
            throw new ValidationException("商品库存不足");
            }

            // 查询购物车中是否已存在该商品
        OmsCartItems existCartItem = getCartItem(userId, cartAddDTO.getProductId());
            
            if (existCartItem != null) {
                // 更新购物车商品数量
                existCartItem.setQuantity(existCartItem.getQuantity() + cartAddDTO.getQuantity());
                existCartItem.setUpdateTime(LocalDateTime.now());
                cartItemsMapper.updateById(existCartItem);
            } else {
                // 创建购物车商品
                OmsCartItems cartItem = new OmsCartItems();
                cartItem.setUserId(userId);
                cartItem.setProductId(cartAddDTO.getProductId());
                cartItem.setProductName(product.getName());
                cartItem.setProductImage(product.getMainImage());
            // 将Double转换为BigDecimal
            cartItem.setPrice(new BigDecimal(product.getPrice().toString()));
                cartItem.setQuantity(cartAddDTO.getQuantity());
                cartItem.setChecked(1); // 默认选中
                cartItem.setCreateTime(LocalDateTime.now());
                cartItem.setUpdateTime(LocalDateTime.now());
                
                cartItemsMapper.insert(cartItem);
            }
            
        return true;
    }

    /**
     * 获取购物车列表
     */
    @Override
    public List<CartItemVO> list() {
            // 获取当前用户ID
            Long userId = getCurrentUserId();
            if (userId == null) {
            throw new BusinessException("用户未登录");
            }
            
            // 查询用户购物车列表
            LambdaQueryWrapper<OmsCartItems> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(OmsCartItems::getUserId, userId)
                    .orderByDesc(OmsCartItems::getCreateTime);
            
            List<OmsCartItems> cartItems = cartItemsMapper.selectList(queryWrapper);
            if (cartItems.isEmpty()) {
            return new ArrayList<>();
            }
            
            // 转换为VO
            List<CartItemVO> result = cartItems.stream().map(item -> {
                CartItemVO vo = new CartItemVO();
                vo.setId(item.getId());
                vo.setProductId(item.getProductId());
                vo.setProductName(item.getProductName());
                vo.setProductImage(item.getProductImage());
                vo.setPrice(item.getPrice());
                vo.setQuantity(item.getQuantity());
                vo.setSubtotal(item.getPrice().multiply(new BigDecimal(item.getQuantity())));
                vo.setChecked(item.getChecked());
                vo.setCreateTime(item.getCreateTime());
                return vo;
            }).collect(Collectors.toList());
            
        return result;
    }

    /**
     * 修改购物车商品数量
     */
    @Override
    @Transactional
    public Boolean updateQuantity(CartUpdateDTO cartUpdateDTO) {
            // 参数校验
            if (cartUpdateDTO == null || cartUpdateDTO.getId() == null) {
            throw new ValidationException("参数不能为空");
            }
            
            if (cartUpdateDTO.getQuantity() == null || cartUpdateDTO.getQuantity() <= 0) {
            throw new ValidationException("商品数量必须大于0");
            }
            
            // 获取当前用户ID
            Long userId = getCurrentUserId();
            if (userId == null) {
            throw new BusinessException("用户未登录");
            }
            
            // 查询购物车商品
            OmsCartItems cartItem = getCartItemById(cartUpdateDTO.getId());
            if (cartItem == null) {
            throw new ValidationException("购物车商品不存在");
            }
            
            // 校验是否是当前用户的购物车
            if (!Objects.equals(cartItem.getUserId(), userId)) {
            throw new BusinessException("无权操作他人购物车");
            }
            
            // 更新购物车商品数量
            cartItem.setQuantity(cartUpdateDTO.getQuantity());
            cartItem.setUpdateTime(LocalDateTime.now());
            cartItemsMapper.updateById(cartItem);
            
        return true;
    }

    /**
     * 修改购物车商品选中状态
     */
    @Override
    @Transactional
    public Boolean updateChecked(CartUpdateDTO cartUpdateDTO) {
            // 参数校验
            if (cartUpdateDTO == null || cartUpdateDTO.getId() == null) {
            throw new ValidationException("参数不能为空");
            }
            
            if (cartUpdateDTO.getChecked() == null) {
            throw new ValidationException("选中状态不能为空");
            }
            
            // 获取当前用户ID
            Long userId = getCurrentUserId();
            if (userId == null) {
            throw new BusinessException("用户未登录");
            }
            
            // 查询购物车商品
            OmsCartItems cartItem = getCartItemById(cartUpdateDTO.getId());
            if (cartItem == null) {
            throw new ValidationException("购物车商品不存在");
            }
            
            // 校验是否是当前用户的购物车
            if (!Objects.equals(cartItem.getUserId(), userId)) {
            throw new BusinessException("无权操作他人购物车");
            }
            
            // 更新购物车商品选中状态
            cartItem.setChecked(cartUpdateDTO.getChecked());
            cartItem.setUpdateTime(LocalDateTime.now());
            cartItemsMapper.updateById(cartItem);
            
        return true;
    }

    /**
     * 删除购物车商品
     */
    @Override
    @Transactional
    public Boolean delete(List<Long> ids) {
            // 参数校验
            if (ids == null || ids.isEmpty()) {
            throw new ValidationException("购物车商品ID不能为空");
            }
            
            // 获取当前用户ID
            Long userId = getCurrentUserId();
            if (userId == null) {
            throw new BusinessException("用户未登录");
            }
            
            // 删除购物车商品
            LambdaQueryWrapper<OmsCartItems> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(OmsCartItems::getUserId, userId)
                    .in(OmsCartItems::getId, ids);
            
            cartItemsMapper.delete(queryWrapper);
            
        return true;
    }

    /**
     * 清空购物车
     */
    @Override
    @Transactional
    public Boolean clear() {
        // 获取当前用户ID
        Long userId = getCurrentUserId();
        if (userId == null) {
            throw new BusinessException("用户未登录");
        }

        // 删除该用户的所有购物车商品
        LambdaQueryWrapper<OmsCartItems> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OmsCartItems::getUserId, userId);
        cartItemsMapper.delete(queryWrapper);

        log.info("清空购物车成功");
        return true;
    }

    /**
     * 获取购物车商品数量
     */
    @Override
    public Integer count() {
        // 获取当前用户ID
        Long userId = getCurrentUserId();
        if (userId == null) {
            return 0; // 未登录用户返回0，避免抛出异常
        }
        
        // 查询用户购物车所有商品
        LambdaQueryWrapper<OmsCartItems> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OmsCartItems::getUserId, userId);
        
        List<OmsCartItems> cartItems = cartItemsMapper.selectList(queryWrapper);
        
        // 累加所有商品的数量
        int totalQuantity = 0;
        if (cartItems != null && !cartItems.isEmpty()) {
            for (OmsCartItems item : cartItems) {
                totalQuantity += item.getQuantity();
            }
        }
        
        log.debug("用户 {} 购物车商品总数量为: {}", userId, totalQuantity);
        return totalQuantity;
    }

    /**
     * 获取当前用户ID
     * 使用JwtTokenUtil从安全上下文或HTTP请求获取用户ID
     */
    private Long getCurrentUserId() {
        try {
            // 首先尝试从Security上下文获取
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
                String username = null;
                if (authentication.getPrincipal() instanceof String) {
                    username = (String) authentication.getPrincipal();
                    log.debug("从Security上下文获取到用户名: {}", username);
                } else if (authentication.getPrincipal() instanceof UserDetails) {
                    username = ((UserDetails) authentication.getPrincipal()).getUsername();
                    log.debug("从UserDetails获取到用户名: {}", username);
                } else if (authentication.getName() != null) {
                    username = authentication.getName();
                    log.debug("从Authentication.getName获取到用户名: {}", username);
                }
                
                if (username != null && !username.isEmpty()) {
                    // 使用JwtTokenUtil工具类从用户名获取用户ID
                    Long userId = jwtTokenUtil.getUserIdFromUsername(username);
                    if (userId != null) {
                        log.debug("从用户名[{}]获取到用户ID: {}", username, userId);
                        return userId;
                    }
                }
            }
            
            // 直接使用工具类获取当前用户ID（从Security上下文或请求中）
            Long userId = jwtTokenUtil.getCurrentUserId();
            if (userId != null) {
                log.debug("从JwtTokenUtil.getCurrentUserId()获取到用户ID: {}", userId);
                return userId;
            }
            
            // 最后尝试从请求获取
            HttpServletRequest request = getCurrentRequest();
            if (request != null) {
                String token = extractTokenFromRequest(request);
                if (token != null && !token.isEmpty()) {
                    log.debug("从请求中提取到token: {}", token.substring(0, Math.min(token.length(), 10)) + "...");
                    // 使用JwtTokenUtil解析token，避免直接使用Jwts.parser
                    userId = jwtTokenUtil.getUserIdFromRequest(request);
                    if (userId != null) {
                        log.debug("从请求token中获取到用户ID: {}", userId);
                        return userId;
                    }
                }
            }
            
            log.warn("无法获取当前用户ID，用户可能未登录");
            return null;
        } catch (Exception e) {
            log.error("获取当前用户ID失败: {}", e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * 从请求中提取JWT令牌
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        try {
            String bearerToken = request.getHeader("Authorization");
            if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                String token = bearerToken.substring(7);
                // 检查token是否包含非法Base64字符
                if (token.contains("_") || token.contains("-")) {
                    log.warn("Token包含非法Base64字符: _ 或 -，将尝试进行URL解码");
                    // 尝试解决URL编码问题，替换特殊字符
                    token = token.replace('_', '/').replace('-', '+');
                }
                return token;
            }
            
            // 尝试从其他可能的位置获取token
            String tokenParam = request.getParameter("token");
            if (tokenParam != null && !tokenParam.isEmpty()) {
                log.debug("从请求参数中获取到token");
                return tokenParam;
            }
            
            // 尝试从Cookie中获取
            javax.servlet.http.Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (javax.servlet.http.Cookie cookie : cookies) {
                    if ("token".equals(cookie.getName())) {
                        log.debug("从Cookie中获取到token");
                        return cookie.getValue();
                    }
                }
            }
        } catch (Exception e) {
            log.error("从请求中提取token失败: {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取当前HTTP请求
     * @return 当前HTTP请求，如果不在HTTP上下文中返回null
     */
    private HttpServletRequest getCurrentRequest() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                return attributes.getRequest();
            }
        } catch (Exception e) {
            log.warn("获取当前请求失败", e);
        }
        return null;
    }

    /**
     * 根据ID查询购物车项
     */
    private OmsCartItems getCartItemById(Long id) {
        return cartItemsMapper.selectById(id);
    }

    /**
     * 查询购物车中是否已存在该商品
     */
    private OmsCartItems getCartItem(Long userId, Long productId) {
        LambdaQueryWrapper<OmsCartItems> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OmsCartItems::getUserId, userId)
                .eq(OmsCartItems::getProductId, productId);
        
        return cartItemsMapper.selectOne(queryWrapper);
    }
} 