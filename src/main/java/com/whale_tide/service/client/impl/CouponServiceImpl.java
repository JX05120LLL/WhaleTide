package com.whale_tide.service.client.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale_tide.common.api.PageResponse;
import com.whale_tide.common.exception.coupon.*;
import com.whale_tide.dto.client.coupon.ProductCouponResponse;
import com.whale_tide.dto.client.coupon.UserCouponListRequest;
import com.whale_tide.dto.client.coupon.UserCouponResponse;
import com.whale_tide.entity.pms.PmsProducts;
import com.whale_tide.entity.sms.SmsCoupons;
import com.whale_tide.entity.sms.SmsCouponProducts;
import com.whale_tide.entity.ums.UmsUserCoupons;
import com.whale_tide.entity.ums.UmsUsers;
import com.whale_tide.mapper.pms.PmsProductsMapper;
import com.whale_tide.mapper.sms.SmsCouponsMapper;
import com.whale_tide.mapper.sms.SmsCouponProductsMapper;
import com.whale_tide.mapper.ums.UmsUserCouponsMapper;
import com.whale_tide.mapper.ums.UmsUsersMapper;
import com.whale_tide.service.client.ICouponService;
import com.whale_tide.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 优惠券服务实现类
 */
@Service
@Slf4j
public class CouponServiceImpl implements ICouponService {

    @Autowired
    private UmsUserCouponsMapper umsUserCouponsMapper;
    
    @Autowired
    private SmsCouponsMapper smsCouponsMapper;
    
    @Autowired
    private PmsProductsMapper pmsProductsMapper;
    
    @Autowired
    private SmsCouponProductsMapper smsCouponProductsMapper;
    
    @Autowired
    private UmsUsersMapper umsUsersMapper;
    
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 获取可领取优惠券列表（含分页）
     */
    @Override
    public PageResponse<UserCouponResponse> getCouponList(UserCouponListRequest request) {
        // 获取当前用户ID
        Long userId = getCurrentUserId();
        
        // 解析请求参数
        Integer status = request.getUseStatus();
        Long pageNum = request.getPageNum();
        Long pageSize = request.getPageSize();
        
        // 设置默认值
        if (pageNum == null || pageNum < 1) {
            pageNum = 1L;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10L;
        }
        
        // 构建查询条件
        LambdaQueryWrapper<UmsUserCoupons> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UmsUserCoupons::getUserId, userId);
        
        // 根据状态筛选
        if (status != null) {
            queryWrapper.eq(UmsUserCoupons::getStatus, status);
        }
        
        // 按过期时间排序（越接近过期的排在前面）
        queryWrapper.orderByAsc(UmsUserCoupons::getEndTime);
        
        // 分页查询
        Page<UmsUserCoupons> page = new Page<>(pageNum, pageSize);
        Page<UmsUserCoupons> resultPage = umsUserCouponsMapper.selectPage(page, queryWrapper);
        
        // 转换为响应对象
        List<UserCouponResponse> responseList = resultPage.getRecords().stream()
                .map(this::convertToUserCouponResponse)
                .collect(Collectors.toList());
        
        // 封装分页结果
        int totalPage = (int) Math.ceil((double) resultPage.getTotal() / pageSize);
        return PageResponse.of(
                responseList,
                pageNum.intValue(),
                pageSize.intValue(),
                resultPage.getTotal(),
                totalPage
        );
    }

    /**
     * 领取优惠券
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void receiveCoupon(Long couponId) {
        // 获取当前用户ID
        Long userId = getCurrentUserId();
        
        // 查询优惠券信息
        SmsCoupons coupon = smsCouponsMapper.selectById(couponId);
        if (coupon == null) {
            throw new CouponNotFoundException("优惠券不存在");
        }
        
        // 检查优惠券是否可领取
        LocalDateTime now = LocalDateTime.now();
        if (coupon.getEndTime().isBefore(now)) {
            throw new CouponExpiredException("优惠券已过期");
        }
        
        // 检查优惠券库存
        if (coupon.getRemainingCount() != null && coupon.getRemainingCount() <= 0) {
            throw new CouponOutOfStockException("优惠券已领完");
        }
        
        // 检查用户是否已领取过该优惠券
        LambdaQueryWrapper<UmsUserCoupons> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UmsUserCoupons::getUserId, userId)
                  .eq(UmsUserCoupons::getCouponId, couponId);
        long count = umsUserCouponsMapper.selectCount(queryWrapper);
        
        if (count >= coupon.getPerLimit()) {
            throw new CouponReceiveLimitExceededException("已达到领取限制");
        }
        
        // 封装结果
        UmsUserCoupons userCoupon = new UmsUserCoupons();
        userCoupon.setUserId(userId);
        userCoupon.setCouponId(couponId);
        userCoupon.setCouponName(coupon.getName());
        userCoupon.setAmount(coupon.getAmount());
        userCoupon.setMinPoint(coupon.getMinPoint());
        userCoupon.setStartTime(now);
        userCoupon.setEndTime(coupon.getEndTime());
        userCoupon.setStatus(0); // 0-未使用
        userCoupon.setGetType(0); // 0-主动领取
        userCoupon.setCreateTime(now);
        userCoupon.setUpdateTime(now);
        
        // 插入数据库
        umsUserCouponsMapper.insert(userCoupon);
        
        // 更新优惠券领取数量
        coupon.setReceiveCount(coupon.getReceiveCount() + 1);
        // 更新剩余数量
        if (coupon.getRemainingCount() != null) {
            coupon.setRemainingCount(coupon.getRemainingCount() - 1);
        }
        smsCouponsMapper.updateById(coupon);
    }

    /**
     * 获取商品可用优惠券
     */
    @Override
    public List<ProductCouponResponse> getProductCoupon(Long productId) {
        // 参数校验
        if (productId == null) {
            throw new CouponInvalidParameterException("商品ID不能为空");
        }
        
        // 查询商品信息
        PmsProducts product = pmsProductsMapper.selectById(productId);
        if (product == null) {
            throw new CouponNotApplicableException("商品不存在");
        }
        
        // 查询商品可用的优惠券
        Long categoryId = product.getCategoryId();
        
        // 查询三类优惠券：全场通用、指定品类、指定商品
        List<SmsCoupons> couponList = new ArrayList<>();
        
        // 1. 查询全场通用优惠券
        LambdaQueryWrapper<SmsCoupons> commonQuery = new LambdaQueryWrapper<>();
        commonQuery.eq(SmsCoupons::getType, 0) // 0-全场通用
                  .eq(SmsCoupons::getIsDeleted, 0) // 未删除
                  .ge(SmsCoupons::getEndTime, LocalDateTime.now()); // 未过期
        List<SmsCoupons> commonCoupons = smsCouponsMapper.selectList(commonQuery);
        couponList.addAll(commonCoupons);
        
        // 2. 查询指定品类的优惠券
        if (categoryId != null) {
            LambdaQueryWrapper<SmsCoupons> categoryQuery = new LambdaQueryWrapper<>();
            categoryQuery.eq(SmsCoupons::getType, 2) // 2-指定分类
                        .eq(SmsCoupons::getIsDeleted, 0)
                        .ge(SmsCoupons::getEndTime, LocalDateTime.now());
            List<SmsCoupons> categoryCoupons = smsCouponsMapper.selectList(categoryQuery);
            // 过滤出适用于当前商品分类的优惠券（实际还需要与优惠券分类关联表结合查询）
            couponList.addAll(categoryCoupons);
        }
        
        // 3. 查询指定商品的优惠券
        LambdaQueryWrapper<SmsCouponProducts> relationQuery = new LambdaQueryWrapper<>();
        relationQuery.eq(SmsCouponProducts::getProductId, productId);
        List<SmsCouponProducts> relations = smsCouponProductsMapper.selectList(relationQuery);
        
        if (!relations.isEmpty()) {
            List<Long> couponIds = relations.stream()
                    .map(SmsCouponProducts::getCouponId)
                    .collect(Collectors.toList());
            
            LambdaQueryWrapper<SmsCoupons> productQuery = new LambdaQueryWrapper<>();
            productQuery.eq(SmsCoupons::getType, 1) // 1-指定商品
                       .in(SmsCoupons::getId, couponIds)
                       .eq(SmsCoupons::getIsDeleted, 0)
                       .ge(SmsCoupons::getEndTime, LocalDateTime.now());
            List<SmsCoupons> productCoupons = smsCouponsMapper.selectList(productQuery);
            couponList.addAll(productCoupons);
        }
        
        // 转换为响应对象
        return couponList.stream()
                .map(this::convertToProductCouponResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * 将UmsUserCoupons转换为UserCouponResponse
     */
    private UserCouponResponse convertToUserCouponResponse(UmsUserCoupons userCoupon) {
        UserCouponResponse response = new UserCouponResponse();
        response.setId(userCoupon.getId());
        response.setCouponId(userCoupon.getCouponId());
        response.setName(userCoupon.getCouponName());
        response.setAmount(userCoupon.getAmount());
        response.setMinPoint(userCoupon.getMinPoint());
        // 转换时间类型
        response.setStartTime(new Date(userCoupon.getStartTime().toEpochSecond(java.time.ZoneOffset.UTC) * 1000));
        response.setEndTime(new Date(userCoupon.getEndTime().toEpochSecond(java.time.ZoneOffset.UTC) * 1000));
        response.setUseStatus(userCoupon.getStatus());
        return response;
    }
    
    /**
     * 将SmsCoupons转换为ProductCouponResponse
     */
    private ProductCouponResponse convertToProductCouponResponse(SmsCoupons coupon) {
        ProductCouponResponse response = new ProductCouponResponse();
        response.setId(coupon.getId());
        response.setName(coupon.getName());
        response.setAmount(coupon.getAmount());
        response.setMinPoint(coupon.getMinPoint());
        // 转换时间类型
        response.setStartTime(new Date(coupon.getStartTime().toEpochSecond(java.time.ZoneOffset.UTC) * 1000));
        response.setEndTime(new Date(coupon.getEndTime().toEpochSecond(java.time.ZoneOffset.UTC) * 1000));
        response.setUseType(coupon.getType()); // 从type字段获取
        return response;
    }
    
    /**
     * 获取当前登录用户ID
     */
    private Long getCurrentUserId() {
        try {
            // 获取当前请求
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                // 从请求头中获取token
                String token = request.getHeader("Authorization");
                if (token != null) {
                    // 使用JwtUtil解析token获取用户名
                    String username = jwtUtil.getUsernameFromToken(token);
                    
                    // 根据用户名查询用户信息
                    LambdaQueryWrapper<UmsUsers> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(UmsUsers::getUsername, username);
                    UmsUsers user = umsUsersMapper.selectOne(queryWrapper);
                    
                    if (user != null) {
                        return user.getId();
                    }
                }
            }
            
            throw new CouponNotBelongToUserException("用户未登录");
        } catch (Exception e) {
            log.error("获取当前用户ID失败", e);
            throw new CouponInvalidParameterException("获取用户信息失败：" + e.getMessage());
        }
    }
}
