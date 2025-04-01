package com.whale_tide.service.client.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.whale_tide.common.exception.cart.CartItemNotFoundException;
import com.whale_tide.common.exception.product.ProductNotFoundException;
import com.whale_tide.common.exception.user.UsernameNotExistException;
import com.whale_tide.common.exception.auth.TokenInvalidException;
import com.whale_tide.dto.client.cart.*;
import com.whale_tide.entity.oms.OmsCartItems;
import com.whale_tide.entity.pms.PmsProductSkus;
import com.whale_tide.entity.pms.PmsProducts;
import com.whale_tide.entity.ums.UmsUsers;
import com.whale_tide.mapper.oms.OmsCartItemsMapper;
import com.whale_tide.mapper.pms.PmsProductSkusMapper;
import com.whale_tide.mapper.pms.PmsProductsMapper;
import com.whale_tide.mapper.ums.UmsUsersMapper;
import com.whale_tide.service.client.ICartService;
import com.whale_tide.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 购物车实现类
 */
@Service
@Slf4j
public class CartServiceImpl implements ICartService {
    @Autowired
    private OmsCartItemsMapper omsCartItemsMapper;
    @Autowired
    private PmsProductsMapper pmsProductsMapper;
    @Autowired
    private PmsProductSkusMapper    pmsProductSkusMapper;

    @Autowired
    private UmsUsersMapper umsUsersMapper;
    @Autowired
    private JwtUtil jwtUtil;


    // 添加购物车
    @Override
    public int cartAdd(CartAddRequest request) {
        // 从token中获取用户ID
        Long userId = getCurrentUserId();
        if (userId == null) {
            throw new UsernameNotExistException("用户不存在");
        }
        // 根据产品ID和SKU ID查询产品信息
        PmsProducts pmsProducts = pmsProductsMapper.selectById(request.getProductId());
        if (pmsProducts == null) throw new ProductNotFoundException("产品不存在");
        PmsProductSkus pmsProductSkus = pmsProductSkusMapper.selectById(request.getProductSkuId());
        if (pmsProductSkus == null) throw new ProductNotFoundException("SKU不存在");

        // 封装购物车
        OmsCartItems cartItem = new OmsCartItems();
        cartItem.setUserId(userId);
        cartItem.setProductId(request.getProductId());
        cartItem.setSkuId(request.getProductSkuId());
        cartItem.setQuantity(request.getQuantity());
        cartItem.setPrice(pmsProductSkus.getPrice());
        cartItem.setProductName(pmsProducts.getName());
        cartItem.setProductImage(pmsProducts.getMainImage());
        cartItem.setSkuSpecs(pmsProductSkus.getSpecsText());//
        cartItem.setChecked(0);
        cartItem.setCreateTime(LocalDateTime.now());
        cartItem.setUpdateTime(LocalDateTime.now());

        // 插入数据库进行保存,返回影响行数
        return omsCartItemsMapper.insert(cartItem);

    }


    // 获取购物车列表
    @Override
    public CartListResponse getCartList() {
        // 从token中获取用户ID
        long userId = getCurrentUserId();
        UmsUsers user = umsUsersMapper.selectById(userId);
        if (user == null) throw new UsernameNotExistException("用户不存在");
        // 根据用户ID查询购物车列表
        LambdaQueryWrapper<OmsCartItems> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OmsCartItems::getUserId, userId);
        List<OmsCartItems> cartItems = omsCartItemsMapper.selectList(queryWrapper);

        if (cartItems == null) {
            throw new CartItemNotFoundException("购物车为空");
        }

        // 封装返回结果
        CartListResponse response = new CartListResponse();
        
        // 转换为DTO
        List<CartListResponse.CartItemResponse> cartItemResponses = cartItems.stream().map(item -> {
            CartListResponse.CartItemResponse dto = new CartListResponse.CartItemResponse();
            dto.setId(item.getId());
            dto.setProductId(item.getProductId());
            dto.setProductSkuId(item.getSkuId());
            dto.setProductName(item.getProductName());
            dto.setProductPic(item.getProductImage());
            dto.setPrice(item.getPrice());
            dto.setQuantity(item.getQuantity());
            dto.setProductAttr(item.getSkuSpecs());
            dto.setChecked(item.getChecked());
            // 如果选中，计入总价
            if (item.getChecked() == 1) {
                BigDecimal amount = item.getPrice().multiply(new BigDecimal(item.getQuantity()));
                response.setTotalAmount(amount);
                response.setPayAmount(amount);
            }
            return dto;
        }).collect(Collectors.toList());


        response.setCartItems(cartItemResponses);
        // 促销金额暂时不计算，默认为0，实付金额等于总金额
        BigDecimal promotionAmount = BigDecimal.ZERO;
        response.setPromotionAmount(promotionAmount);
        return response;
    }

    // 更新购物车项的数量
    @Override
    public void cartUpdateQuantity(CartUpdateQuantityRequest request) {
        //解析请求参数
        Long id = request.getId();
        Integer quantity = request.getQuantity();

        // 根据id查询购物车项
        OmsCartItems cartItem = omsCartItemsMapper.selectById(id);
        if (cartItem == null) throw new CartItemNotFoundException("购物车项不存在");

        // 根据商品id查询商品信息和SKU信息
        PmsProducts pmsProducts = pmsProductsMapper.selectById(cartItem.getProductId());
        if (pmsProducts == null) throw new ProductNotFoundException("产品不存在");
        LambdaQueryWrapper<PmsProductSkus> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PmsProductSkus::getProductId, cartItem.getProductId());
        PmsProductSkus pmsProductSkus = pmsProductSkusMapper.selectOne(queryWrapper);

        // 更新购物车项的数量,封装结果
        cartItem.setUserId(getCurrentUserId());
        cartItem.setProductId(pmsProducts.getId());
        cartItem.setSkuId(pmsProductSkus.getId());
        cartItem.setProductName(pmsProducts.getName());
        cartItem.setProductImage(pmsProducts.getMainImage());
        cartItem.setPrice(pmsProducts.getPrice());
        cartItem.setSkuSpecs(pmsProductSkus.getSpecsText());
        cartItem.setQuantity(quantity);
        cartItem.setChecked(0);
        cartItem.setCreateTime(LocalDateTime.now());
        cartItem.setUpdateTime(LocalDateTime.now());

        // 更新数据库
        omsCartItemsMapper.updateById(cartItem);
        log.info("购物车项数量更新成功");
    }

    // 更新购物车项的选中状态
    @Override
    public void cartUpdateChecked(CartUpdateCheckedRequest request) {
        //解析请求参数
        String ids = request.getIds();
        Integer checked = request.getChecked();

        //解析参数
        List<Long> idList = Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());

        // 创建更新包装器
        UpdateWrapper<OmsCartItems> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", idList);
        updateWrapper.set("checked", checked);
        updateWrapper.set("update_time", LocalDateTime.now());
        
        // 执行更新
        omsCartItemsMapper.update(null, updateWrapper);
        log.info("购物车项选中状态已更新，影响记录数：" + idList.size());
    }

    // 删除购物车
    @Override
    public void cartDelete(CartDeleteRequest request) {
        //解析请求参数
        String ids = request.getIds();

        //解析参数
        List<Long> idList = Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());

        // 创建更新包装器
        UpdateWrapper<OmsCartItems> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", idList);
        // 执行更新
        omsCartItemsMapper.delete(updateWrapper);
        log.info("购物车项已删除，影响记录数：" + idList.size());
    }

    // 清空购物车
    @Override
    public void cartClear() {
        // 从token中获取用户ID
        Long userId = getCurrentUserId();
        if(userId == null) throw new UsernameNotExistException("用户不存在");
        // 创建更新包装器
        UpdateWrapper<OmsCartItems> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", userId);
        // 执行更新
        omsCartItemsMapper.delete(updateWrapper);
        log.info("购物车已清空");
    }

    // 获取购物车中商品的数量
    @Override
    public int getCartProductCount() {
        // 从token中获取用户ID
        Long userId = getCurrentUserId();
        if(userId == null) throw new UsernameNotExistException("用户不存在");
        // 查询用户购物车中的商品数量
        LambdaQueryWrapper<OmsCartItems> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OmsCartItems::getUserId, userId);

        // 查询用户购物车中的所有项
        List<OmsCartItems> cartItems = omsCartItemsMapper.selectList(queryWrapper);
        
        // 计算所有购物车项的数量之和
        int totalCount = 0;
        if (cartItems != null && !cartItems.isEmpty()) {
            totalCount = cartItems.stream()
                    .mapToInt(OmsCartItems::getQuantity)
                    .sum();
        }
        
        log.info("用户[{}]购物车商品总数量: {}", userId, totalCount);
        return totalCount;
    }

    /**
     * 获取当前用户ID
     * 实际项目中应该从当前请求中获取
     *
     * @return 当前用户ID
     */
    private Long getCurrentUserId() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                
                // 从请求头中获取token
                String authHeader = request.getHeader("Authorization");
                if (authHeader != null) {
                    // 移除Bearer前缀
                    String token = authHeader;
                    if (authHeader.startsWith("Bearer ")) {
                        token = authHeader.substring(7).trim();
                    }
                    
                    // 空token检查
                    if (token == null || token.isEmpty()) {
                        log.warn("Authorization header存在但token为空");
                        throw new RuntimeException("用户未登录");
                    }

                    try {
                        // 使用JwtUtil解析token获取用户名
                        String username = jwtUtil.getUsernameFromToken(token);

                        // 根据用户名查询用户信息
                        LambdaQueryWrapper<UmsUsers> queryWrapper = new LambdaQueryWrapper<>();
                        queryWrapper.eq(UmsUsers::getUsername, username);
                        UmsUsers user = umsUsersMapper.selectOne(queryWrapper);

                        if (user != null) {
                            return user.getId();
                        }
                    } catch (TokenInvalidException e) {
                        log.warn("Token无效: {}", e.getMessage());
                        throw new RuntimeException("用户未登录或登录已过期");
                    }
                }
            }

            // 如果获取失败，抛出异常或返回默认值
            log.warn("无法获取当前用户ID，请检查用户是否已登录");
            throw new RuntimeException("用户未登录");
        } catch (Exception e) {
            log.error("获取当前用户ID失败", e);
            throw new RuntimeException("获取用户信息失败", e);
        }
    }
}
