package com.whale_tide.service.client.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale_tide.common.api.PageResponse;
import com.whale_tide.common.exception.address.AddressNotFoundException;
import com.whale_tide.common.exception.cart.CartItemNotFoundException;
import com.whale_tide.common.exception.order.OrderCreateFailedException;
import com.whale_tide.common.exception.product.ProductNotFoundException;
import com.whale_tide.dto.client.order.*;
import com.whale_tide.entity.oms.*;
import com.whale_tide.entity.pms.PmsProducts;
import com.whale_tide.entity.ums.UmsUserAddresses;
import com.whale_tide.entity.ums.UmsUserCoupons;
import com.whale_tide.entity.ums.UmsUsers;
import com.whale_tide.mapper.oms.*;
import com.whale_tide.mapper.pms.PmsBrandsMapper;
import com.whale_tide.mapper.pms.PmsProductsMapper;
import com.whale_tide.mapper.ums.UmsUserAddressesMapper;
import com.whale_tide.mapper.ums.UmsUserCouponsMapper;
import com.whale_tide.mapper.ums.UmsUsersMapper;
import com.whale_tide.service.client.IOrderService;
import com.whale_tide.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

/**
 * 客户端订单服务实现类
 */
@Slf4j
@Service("clientOrderService")
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private OmsOrdersMapper omsOrdersMapper;
    
    @Autowired
    private OmsOrderItemsMapper omsOrderItemsMapper;
    
    @Autowired
    private OmsCartItemsMapper omsCartItemsMapper;
    
    @Autowired
    private OmsOrderStatusHistoryMapper omsOrderStatusHistoryMapper;
    
    @Autowired
    private OmsOrderLogsMapper omsOrderLogsMapper;
    
    @Autowired
    private OmsPaymentsMapper omsPaymentsMapper;

    @Autowired
    private UmsUserCouponsMapper umsUserCouponsMapper;

    @Autowired
    private PmsBrandsMapper pmsBrandsMapper;
    
    @Autowired
    private OmsOrderDeliveriesMapper omsOrderDeliveriesMapper;
    
    @Autowired
    private OmsOrderReturnsMapper omsOrderReturnsMapper;
    
    @Autowired
    private PmsProductsMapper pmsProductsMapper;
    
    @Autowired
    private UmsUserAddressesMapper umsUserAddressesMapper;
    
    @Autowired
    private UmsUsersMapper umsUsersMapper;

    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * 生成确认订单
     *
     * @param request 生成确认订单请求
     * @return 确认订单响应
     */
    @Override
    public ConfirmOrderResponse generateConfirmOrder(GenerateConfirmOrderRequest request) {
        // 获取当前用户ID
        Long userId = getCurrentUserId();
        
        // 构建响应对象
        ConfirmOrderResponse response = new ConfirmOrderResponse();
        
        // 1. 获取并转换购物车列表
        List<OmsCartItems> cartItems = getCartItems(request.getCartIds(), userId);
        if (CollectionUtils.isEmpty(cartItems)) {
            log.warn("购物车项为空，userId: {}", userId);
            throw new CartItemNotFoundException("购物车项不存在");
        }
        
        // 转换购物车项为响应对象
        List<ConfirmOrderResponse.CartItemResponse> cartItemList = convertCartItems(cartItems);
        response.setCartItems(cartItemList); // 使用正确的字段名
        
        // 2. 获取用户收货地址列表
        List<UmsUserAddresses> addresses = getUserAddresses(userId);
        List<ConfirmOrderResponse.AddressResponse> addressList = convertAddresses(addresses);
        response.setMemberReceiveAddressList(addressList);
        
        // 3. 计算订单金额信息
        ConfirmOrderResponse.CalcAmountResponse calcAmount = calculateOrderAmount(cartItems);
        response.setCalcAmount(calcAmount);
        
        // 4. 获取用户可用优惠券列表
        List<ConfirmOrderResponse.CouponResponse> couponList = getUserAvailableCoupons(userId, calcAmount.getTotalAmount());
        response.setCouponList(couponList); // 使用正确的字段名
        
        return response;
    }
    
    /**
     * 生成订单
     *
     * @param request 生成订单请求
     * @return 生成订单响应
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public GenerateOrderResponse generateOrder(GenerateOrderRequest request) {
        // 参数检查
        if (request == null) {
            throw new OrderCreateFailedException("订单参数不能为空");
        }
        
        // 检查必要参数
        if (request.getAddressId() == null) {
            throw new AddressNotFoundException("收货地址不能为空");
        }

        if (CollectionUtils.isEmpty(request.getCartIds())) {
            throw new CartItemNotFoundException("购物车项不能为空");
        }

        // 获取收货地址
        UmsUserAddresses address = umsUserAddressesMapper.selectById(request.getAddressId());
        if (address == null) {
            throw new AddressNotFoundException("收货地址不存在: ID=" + request.getAddressId());
        }
        
        Long userId = address.getUserId();// 获取当前用户ID

        // 1. 获取购物车项
        List<OmsCartItems> cartItems = getCartItems(request.getCartIds(), userId);
        if (CollectionUtils.isEmpty(cartItems)) {
            throw new CartItemNotFoundException("购物车项不存在");
        }

        // 3. 计算订单金额
        BigDecimal totalAmount = calculateTotalAmount(cartItems);
        BigDecimal freightAmount = BigDecimal.ZERO  ; // 运费默认为0，需要根据实际业务计算
        BigDecimal discountAmount = BigDecimal.ZERO;


        // 获取优惠券信息计算折扣金额
        BigDecimal couponAmount = BigDecimal.ZERO; // 优惠券金额
        BigDecimal discount = BigDecimal.ZERO; // 优惠券折扣
        BigDecimal promotionAmount = BigDecimal.ZERO; // 促销金额默认0，需要根据实际业务计算
        
        // 处理优惠券
        Long couponId = request.getCouponId(); // 优惠券ID
        if (couponId != null) {
            UmsUserCoupons coupon = umsUserCouponsMapper.selectById(couponId); // 获取优惠券信息
            if (coupon != null) {
                couponAmount = coupon.getAmount() != null ? coupon.getAmount() : BigDecimal.ZERO; // 优惠券金额
                discount = coupon.getDiscount() != null ? coupon.getDiscount() : BigDecimal.ZERO; // 优惠券折扣
            }
        }
        
        // 应付金额 = 总金额 + 运费 - 折扣 - 优惠券 - 促销
        BigDecimal payAmount = totalAmount.add(freightAmount)
                                      .subtract(discountAmount)
                                      .subtract(couponAmount)
                                      .subtract(promotionAmount);
        
        // 4. 生成订单编号
        String orderSn = generateOrderSn();
        
        // 5. 创建订单
        OmsOrders order = new OmsOrders();

        order.setOrderSn(orderSn);
        order.setUserId(userId);
        order.setMerchantId(1L); // 假设只有一个商家
        order.setOrderType(0); // 普通订单
        order.setSourceType(0); // 来源PC
        order.setStatus(0); // 待付款状态
        order.setTotalAmount(totalAmount);
        order.setPayAmount(payAmount);
        order.setFreightAmount(freightAmount);
        order.setDiscountAmount(discountAmount);
        order.setCouponAmount(couponAmount);
        order.setPromotionAmount(promotionAmount);
        order.setIntegrationAmount(BigDecimal.ZERO);
        order.setPayType(request.getPayType());
        order.setOrderNote(request.getMemberMessage());
        order.setAutoConfirmDay(7); // 7天自动确认收货
        order.setIsDeleted(0); // 未删除
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        
        try {
            // 保存订单
            omsOrdersMapper.insert(order);
            
            // 6. 创建订单状态历史记录
            saveOrderStatusHistory(order.getId(), 0, "创建订单");
            
            // 7. 创建订单商品项
            saveOrderItems(order, cartItems);
            
            // 8. 删除购物车项
            deleteCartItems(request.getCartIds(), userId);
            
            // 9. 记录订单日志
            saveOrderLog(order.getId(), "创建订单", String.format("用户ID：%d 创建订单", userId));

            // 10. 返回订单创建结果
            GenerateOrderResponse response = new GenerateOrderResponse();
            response.setOrderId(order.getId());
            response.setPayAmount(payAmount);
            response.setPayType(request.getPayType());
            
            return response;
        } catch (Exception e) {
            log.error("创建订单过程中发生错误:", e);
            throw new OrderCreateFailedException("创建订单失败: " + e.getMessage());
        }
    }

    /**
     * 获取购物车项列表
     *
     * @param cartIds 购物车项ID列表
     * @param userId 用户ID
     * @return 购物车项列表
     */
    private List<OmsCartItems> getCartItems(List<Long> cartIds, Long userId) {
        if (CollectionUtils.isEmpty(cartIds)) {
            log.warn("购物车项ID列表为空，userId: {}", userId);
            throw new CartItemNotFoundException("购物车项不存在");
        }

        // 构建查询条件
        LambdaQueryWrapper<OmsCartItems> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(OmsCartItems::getId, cartIds).eq(OmsCartItems::getUserId, userId);
        
        return omsCartItemsMapper.selectList(queryWrapper);
    }
    
    /**
     * 将购物车项转换为响应对象
     *
     * @param cartItems 购物车项列表
     * @return 购物车项响应列表
     */
    private List<ConfirmOrderResponse.CartItemResponse> convertCartItems(List<OmsCartItems> cartItems) {
        return cartItems.stream().map(item -> {
            ConfirmOrderResponse.CartItemResponse response = new ConfirmOrderResponse.CartItemResponse();
            response.setId(item.getId());
            response.setProductId(item.getProductId());
            response.setProductName(item.getProductName());
            response.setProductPic(item.getProductImage());
            response.setPrice(item.getPrice());
            response.setQuantity(item.getQuantity());
            
            // 商品属性（如规格、颜色等）
            if (StringUtils.hasText(item.getSkuSpecs())) {
                response.setProductAttr(item.getSkuSpecs());
            } else {
                response.setProductAttr("");
            }
            
            return response;
        }).collect(Collectors.toList());
    }
    
    /**
     * 获取用户收货地址列表
     *
     * @param userId 用户ID
     * @return 收货地址列表
     */
    private List<UmsUserAddresses> getUserAddresses(Long userId) {
        LambdaQueryWrapper<UmsUserAddresses> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UmsUserAddresses::getUserId, userId)
                    .orderByDesc(UmsUserAddresses::getDefaultStatus);
        
        return umsUserAddressesMapper.selectList(queryWrapper);
    }
    

    /**
     * 计算订单金额信息
     *
     * @param cartItems 购物车项列表
     * @return 订单金额信息
     */
    private ConfirmOrderResponse.CalcAmountResponse calculateOrderAmount(List<OmsCartItems> cartItems) {
        ConfirmOrderResponse.CalcAmountResponse calcAmount = new ConfirmOrderResponse.CalcAmountResponse();
        
        // 计算商品总金额
        BigDecimal totalAmount = calculateTotalAmount(cartItems);
        
        // 运费（实际业务中根据运费规则计算）
        BigDecimal freightAmount = BigDecimal.ZERO;
        
        // 优惠（实际业务中根据促销规则计算）
        BigDecimal promotionAmount = BigDecimal.ZERO;
        
        // 优惠券抵扣（实际业务中根据优惠券规则计算）
        BigDecimal couponAmount = BigDecimal.ZERO;
        
        // 应付金额 = 总金额 + 运费 - 促销优惠 - 优惠券抵扣
        BigDecimal payAmount = totalAmount.add(freightAmount)
                                      .subtract(promotionAmount)
                                      .subtract(couponAmount);
        
        calcAmount.setTotalAmount(totalAmount);
        calcAmount.setFreightAmount(freightAmount);
        calcAmount.setPromotionAmount(promotionAmount);
        calcAmount.setCouponAmount(couponAmount);
        calcAmount.setPayAmount(payAmount);
        
        return calcAmount;
    }
    
    /**
     * 计算商品总金额
     *
     * @param cartItems 购物车项列表
     * @return 商品总金额
     */
    private BigDecimal calculateTotalAmount(List<OmsCartItems> cartItems) {
        if (CollectionUtils.isEmpty(cartItems)) {
            throw new CartItemNotFoundException("购物车项不存在");
        }
        
        // 累加每个购物车项的(单价 * 数量)
        return cartItems.stream()
            .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * 生成订单编号
     * @return 订单编号
     */
    private String generateOrderSn() {
        // 简单示例：时间戳 + 4位随机数
        return System.currentTimeMillis() + String.format("%04d", new Random().nextInt(10000));
    }
    
    /**
     * 保存订单状态历史记录
     * @param orderId 订单ID
     * @param status 订单状态
     * @param note 备注
     */
    private void saveOrderStatusHistory(Long orderId, Integer status, String note) {
        // 先查询订单获取订单号
        OmsOrders order = omsOrdersMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        OmsOrderStatusHistory history = new OmsOrderStatusHistory();
        history.setOrderId(orderId);
        history.setOrderSn(order.getOrderSn());  // 设置订单号
        history.setPreviousStatus(0);
        history.setPreviousStatus(status);
        history.setCurrentStatus(status);
        history.setNote(note);
        history.setCreateTime(LocalDateTime.now());

        omsOrderStatusHistoryMapper.insert(history);
    }

    
    /**
     * 保存订单商品项
     * @param order 订单
     * @param cartItems 购物车项列表
     */
    private void saveOrderItems(OmsOrders order, List<OmsCartItems> cartItems) {
        if (CollectionUtils.isEmpty(cartItems)) {
            throw new CartItemNotFoundException("购物车项不存在");
        }
        
        List<OmsOrderItems> orderItems = cartItems.stream().map(item -> {
            OmsOrderItems orderItem = new OmsOrderItems();
            orderItem.setOrderId(order.getId());
            orderItem.setOrderSn(order.getOrderSn());
            orderItem.setProductId(item.getProductId());
            orderItem.setProductName(item.getProductName());
            orderItem.setProductImage(item.getProductImage());
            
            // 获取商品更多信息
            PmsProducts product = pmsProductsMapper.selectById(item.getProductId());
            if (product == null) {
                throw new ProductNotFoundException("商品不存在");
            }

            // 获取品牌信息
            String BrandName = pmsBrandsMapper.selectById(product.getBrandId()).getName();
            orderItem.setProductBrand(BrandName);
            orderItem.setProductSn(product.getProductSn());orderItem.setProductCategoryId(product.getCategoryId());
            orderItem.setSkuId(item.getSkuId());
            orderItem.setSkuCode(" ");//设默认为空
            orderItem.setSkuSpecs(item.getSkuSpecs());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getPrice());
            
            // 计算实际金额 = 单价 * 数量
            BigDecimal realAmount = item.getPrice().multiply(new BigDecimal(item.getQuantity()));
            orderItem.setRealAmount(realAmount);
            orderItem.setOriginalAmount(realAmount);
            
            // 优惠信息（简化实现）
            orderItem.setCouponAmount(BigDecimal.ZERO);
            orderItem.setPromotionAmount(BigDecimal.ZERO);
            orderItem.setPromotionName("");
            
            // 积分和成长值（简化实现）
            orderItem.setGiftIntegration(0);
            orderItem.setGiftGrowth(0);
            
            // 退款和评论状态
            orderItem.setRefundStatus(0); // 未退款
            orderItem.setCommentStatus(0); // 未评论
            
            orderItem.setCreateTime(LocalDateTime.now());
            
            return orderItem;
        }).collect(Collectors.toList());
        
        // 批量保存订单商品项
        for (OmsOrderItems orderItem : orderItems) {
            omsOrderItemsMapper.insert(orderItem);
        }
    }


    /**
     * 删除购物车项
     *
     * @param cartIds 购物车项ID列表
     * @param userId 用户ID
     */
    private void deleteCartItems(List<Long> cartIds, Long userId) {
        if (CollectionUtils.isEmpty(cartIds)) {
            return;
        }
        
        LambdaQueryWrapper<OmsCartItems> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(OmsCartItems::getId, cartIds)
                   .eq(OmsCartItems::getUserId, userId);
        
        omsCartItemsMapper.delete(queryWrapper);
    }
    
    /**
     * 保存订单日志
     * 待实现
     * @param orderId 订单ID
     * @param operateType 操作类型
     * @param note 备注
     */
    private void saveOrderLog(Long orderId, String operateType, String note) {
        OmsOrderLogs orderLog = new OmsOrderLogs();
        orderLog.setOrderId(orderId);
        orderLog.setNote(note);
        orderLog.setOrderSn(generateOrderSn());
        orderLog.setCreateTime(LocalDateTime.now());
        omsOrderLogsMapper.insert(orderLog);
    }

    /**
     * 获取订单列表
     *
     * @param request 订单列表请求
     * @return 订单列表分页数据
     */
    @Override
    public PageResponse<OrderListResponse> getOrderList(OrderListRequest request) {
        Long userId = getCurrentUserId();
        
        // 构建查询条件
        LambdaQueryWrapper<OmsOrders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OmsOrders::getUserId, userId)
                    .eq(OmsOrders::getIsDeleted, 0);
        
        // 根据订单状态筛选
        if (request.getStatus() != -1) {
            queryWrapper.eq(OmsOrders::getStatus, request.getStatus());
        }
        
        // 按创建时间倒序排序
        queryWrapper.orderByDesc(OmsOrders::getCreateTime);
        
        // 分页查询
        Page<OmsOrders> page = new Page<>(request.getPageNum(), request.getPageSize());
        IPage<OmsOrders> resultPage = omsOrdersMapper.selectPage(page, queryWrapper);
        
        // 转换为响应对象
        List<OrderListResponse> responseList = resultPage.getRecords().stream().map(order -> {
            OrderListResponse response = new OrderListResponse();
            response.setId(order.getId());
            response.setOrderSn(order.getOrderSn());
            response.setCreateTime(Date.from(order.getCreateTime().atZone(ZoneId.systemDefault()).toInstant()));
            response.setPayAmount(order.getPayAmount());
            response.setStatus(order.getStatus());
            
            // 获取订单商品列表
            LambdaQueryWrapper<OmsOrderItems> itemsQuery = new LambdaQueryWrapper<>();
            itemsQuery.eq(OmsOrderItems::getOrderId, order.getId());
            List<OmsOrderItems> orderItems = omsOrderItemsMapper.selectList(itemsQuery);
            
            List<OrderListResponse.OrderItemResponse> itemResponses = orderItems.stream().map(item -> {
                OrderListResponse.OrderItemResponse itemResponse = new OrderListResponse.OrderItemResponse();
                itemResponse.setId(item.getId());
                itemResponse.setProductId(item.getProductId());
                itemResponse.setProductName(item.getProductName());
                itemResponse.setProductPic(item.getProductImage());
                itemResponse.setProductPrice(item.getPrice());
                itemResponse.setProductQuantity(item.getQuantity());
                itemResponse.setProductAttr(item.getSkuSpecs());
                return itemResponse;
            }).collect(Collectors.toList());
            
            response.setOrderItemList(itemResponses);
            
            return response;
        }).collect(Collectors.toList());
        
        // 构建分页响应
        PageResponse<OrderListResponse> pageResponse = new PageResponse<>();
        pageResponse.setList(responseList);
        pageResponse.setPageNum((int) resultPage.getCurrent());
        pageResponse.setPageSize((int) resultPage.getSize());
        pageResponse.setTotal(resultPage.getTotal());
        pageResponse.setTotalPage((int) resultPage.getPages());
        
        return pageResponse;
    }
    
    /**
     * 获取订单详情
     *
     * @param orderId 订单ID
     * @return 订单详情
     */
    @Override
    public OrderDetailResponse getOrderDetail(Long orderId) {
        Long userId = getCurrentUserId();
        
        // 查询订单基本信息
        LambdaQueryWrapper<OmsOrders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OmsOrders::getId, orderId)
                   .eq(OmsOrders::getUserId, userId)
                   .eq(OmsOrders::getIsDeleted, 0);
        
        OmsOrders order = omsOrdersMapper.selectOne(queryWrapper);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        OrderDetailResponse response = new OrderDetailResponse();
        response.setId(order.getId());
        response.setOrderSn(order.getOrderSn());
        response.setCreateTime(Date.from(order.getCreateTime().atZone(ZoneId.systemDefault()).toInstant()));
        response.setPayAmount(order.getPayAmount());
        response.setStatus(order.getStatus());
        response.setPayType(order.getPayType());
        response.setMemberMessage(order.getOrderNote());
        
        // 查询订单商品项
        LambdaQueryWrapper<OmsOrderItems> itemsQuery = new LambdaQueryWrapper<>();
        itemsQuery.eq(OmsOrderItems::getOrderId, orderId);
        List<OmsOrderItems> orderItems = omsOrderItemsMapper.selectList(itemsQuery);
        
        List<OrderDetailResponse.OrderItemResponse> itemResponses = orderItems.stream().map(item -> {
            OrderDetailResponse.OrderItemResponse itemResponse = new OrderDetailResponse.OrderItemResponse();
            itemResponse.setId(item.getId());
            itemResponse.setProductId(item.getProductId());
            itemResponse.setProductName(item.getProductName());
            itemResponse.setProductPic(item.getProductImage());
            itemResponse.setProductPrice(item.getPrice());
            itemResponse.setProductQuantity(item.getQuantity());
            itemResponse.setProductAttr(item.getSkuSpecs());
            return itemResponse;
        }).collect(Collectors.toList());
        
        response.setOrderItemList(itemResponses);
        
        return response;
    }
    
    /**
     * 取消订单
     *
     * @param request 取消订单请求
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelOrder(CancelOrderRequest request) {
        Long userId = getCurrentUserId();
        
        // 查询订单
        LambdaQueryWrapper<OmsOrders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OmsOrders::getId, request.getOrderId())
                   .eq(OmsOrders::getUserId, userId)
                   .eq(OmsOrders::getIsDeleted, 0);
        
        OmsOrders order = omsOrdersMapper.selectOne(queryWrapper);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        // 检查订单状态，只有待付款状态的订单才能取消
        if (order.getStatus() != 0) {
            throw new RuntimeException("订单状态不允许取消");
        }
        
        // 更新订单状态为已取消
        order.setStatus(4); // 假设4为已取消状态
        order.setUpdateTime(LocalDateTime.now());
        omsOrdersMapper.updateById(order);
        
        // 添加订单状态历史
        saveOrderStatusHistory(order.getId(), 4, "用户取消订单");
        
        // 记录订单日志
        saveOrderLog(order.getId(), "取消订单", String.format("用户ID：%d 取消订单", userId));
        
        // 如果需要恢复库存等操作，可以在这里实现
        
        return true;
    }



    /**
     * 确认收货
     *
     * @param request 确认收货请求
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmReceiveOrder(ConfirmReceiveRequest request) {
        Long userId = getCurrentUserId();
        
        // 查询订单
        LambdaQueryWrapper<OmsOrders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OmsOrders::getId, request.getOrderId())
                   .eq(OmsOrders::getUserId, userId)
                   .eq(OmsOrders::getIsDeleted, 0);
        
        OmsOrders order = omsOrdersMapper.selectOne(queryWrapper);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        // 检查订单状态，只有已发货状态的订单才能确认收货
        if (order.getStatus() != 2) { // 假设2为已发货状态
            throw new RuntimeException("订单状态不允许确认收货");
        }
        
        // 更新订单状态为已完成
        order.setStatus(3); // 假设3为已完成状态
        order.setReceiveTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        omsOrdersMapper.updateById(order);
        
        // 添加订单状态历史
        saveOrderStatusHistory(order.getId(), 3, "用户确认收货");
        
        // 记录订单日志
        saveOrderLog(order.getId(), "确认收货", String.format("用户ID：%d 确认收货", userId));
        
        return true;
    }
    
    /**
     * 删除订单
     *
     * @param request 删除订单请求
     * @return 操作结果
     */
    @Override
    public boolean deleteOrder(DeleteOrderRequest request) {
        Long userId = getCurrentUserId();
        
        // 查询订单
        LambdaQueryWrapper<OmsOrders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OmsOrders::getId, request.getOrderId())
                   .eq(OmsOrders::getUserId, userId)
                   .eq(OmsOrders::getIsDeleted, 0);
        
        OmsOrders order = omsOrdersMapper.selectOne(queryWrapper);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        // 只有已完成或已取消的订单才能删除
        if (order.getStatus() != 3 && order.getStatus() != 4) { // 假设3为已完成，4为已取消
            throw new RuntimeException("订单状态不允许删除");
        }
        
        // 逻辑删除订单
        order.setIsDeleted(1);
        order.setUpdateTime(LocalDateTime.now());
        omsOrdersMapper.updateById(order);
        
        // 记录订单日志
        saveOrderLog(order.getId(), "删除订单", String.format("用户ID：%d 删除订单", userId));
        
        return true;
    }

    /**
     * 支付宝订单状态查询
     *
     * @param request 支付宝查询请求
     * @return
     */
    @Override
    public AlipayQueryResponse queryAlipayStatus(AlipayQueryRequest request) {
        return null;
    }

    /**
     * 支付成功回调
     *
     * @param request 支付成功回调请求
     * @return 支付成功回调响应
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaySuccessResponse paySuccess(PaySuccessRequest request) {
        // 查询订单
        LambdaQueryWrapper<OmsOrders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OmsOrders::getId, request.getOrderId());
        OmsOrders order = omsOrdersMapper.selectOne(queryWrapper);
        
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        // 当前时间
        LocalDateTime now = LocalDateTime.now();
        
        // 1. 更新订单状态为已支付
        order.setStatus(1); // 状态1为已支付状态
        order.setPayType(request.getPayType());
        order.setPaymentTime(now);
        order.setUpdateTime(now);
        omsOrdersMapper.updateById(order);
        
        // 记录订单状态历史和日志
        saveOrderStatusHistory(order.getId(), 1, "订单支付成功");
        saveOrderLog(order.getId(), "支付成功", "订单支付成功");
        
        // 记录支付信息
        OmsPayments payment = new OmsPayments();
        payment.setOrderId(order.getId());
        payment.setOrderSn(order.getOrderSn());
        payment.setPaymentMethod(request.getPayType());
        payment.setAmount(order.getPayAmount());
        payment.setCreateTime(now);
        payment.setUserId(order.getUserId()); // 设置用户ID
        // 生成支付流水号（使用时间戳+随机数）
        String paymentSn = "PAY" + System.currentTimeMillis() + String.format("%04d", new Random().nextInt(10000));
        payment.setPaymentSn(paymentSn);
        payment.setStatus(2); // 设置为支付成功状态
        omsPaymentsMapper.insert(payment);
        
        // 2. 模拟物流发货过程 - 自动将订单状态设置为已发货（待收货）
        try {
            // 更新订单状态为已发货（待收货）
            order.setStatus(2); // 状态2为已发货状态（待收货）
            order.setDeliveryTime(LocalDateTime.now()); // 设置发货时间
            order.setUpdateTime(LocalDateTime.now());
            omsOrdersMapper.updateById(order);
            
            // 记录订单状态历史和日志
            saveOrderStatusHistory(order.getId(), 2, "商品已发货");
            saveOrderLog(order.getId(), "商品发货", "商家已发货，等待用户收货");
            
            // 模拟创建物流信息
            OmsOrderDeliveries delivery = new OmsOrderDeliveries();
            delivery.setOrderId(order.getId());
            delivery.setOrderSn(order.getOrderSn());
            delivery.setDeliveryCompany("模拟快递公司");
            delivery.setDeliverySn("SF" + System.currentTimeMillis());
            delivery.setDeliveryTime(LocalDateTime.now());
            delivery.setCreateTime(LocalDateTime.now());
            delivery.setDeliveryStatus(1); // 已发货状态
            delivery.setUserId(order.getUserId()); // 设置用户ID
            omsOrderDeliveriesMapper.insert(delivery);
            
            log.info("订单 {} 已自动设置为已发货状态", order.getId());
        } catch (Exception e) {
            log.error("模拟发货过程失败", e);
            // 发货过程失败不影响支付成功的处理
        }
        
        // 构建响应
        PaySuccessResponse response = new PaySuccessResponse();
        response.setOrderId(order.getId());
        response.setOrderSn(order.getOrderSn());
        response.setPayAmount(order.getPayAmount());
        response.setStatus(order.getStatus());
        
        return response;
    }

    /**
     * 获取当前用户ID
     * 实际项目中应该从当前请求中获取
     * @return 当前用户ID
     */
    private Long getCurrentUserId() {
        // 从请求中获取当前用户ID
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
            
            // 如果获取失败，抛出异常或返回默认值
            log.warn("无法获取当前用户ID，请检查用户是否已登录");
            throw new RuntimeException("用户未登录");
        } catch (Exception e) {
            log.error("获取当前用户ID失败", e);
            throw new RuntimeException("获取用户信息失败", e);
        }
    }

    /**
     * 将收货地址转换为响应对象
     * @param addresses 收货地址列表
     * @return 收货地址响应列表
     */
    private List<ConfirmOrderResponse.AddressResponse> convertAddresses(List<UmsUserAddresses> addresses) {
        if (CollectionUtils.isEmpty(addresses)) {
            return Collections.emptyList();
        }
        
        return addresses.stream().map(address -> {
            ConfirmOrderResponse.AddressResponse response = new ConfirmOrderResponse.AddressResponse();
            response.setId(address.getId());
            // 收货人姓名
            Long userId = address.getUserId();
            String name  = umsUsersMapper.selectById(userId).getUsername();
            String phone = umsUsersMapper.selectById(userId).getPhone();
            response.setName(name);
            response.setPhoneNumber(phone);
            response.setDefaultStatus(address.getDefaultStatus());
            response.setProvince(address.getProvince());
            response.setCity(address.getCity());
            response.setDistrict(address.getDistrict());
            response.setDetailAddress(address.getDetailAddress());
            
            return response;
        }).collect(Collectors.toList());
    }

    /**
     * 获取用户可用优惠券列表
     * @param userId 用户ID
     * @param totalAmount 订单总金额
     * @return 优惠券列表
     */
    private List<ConfirmOrderResponse.CouponResponse> getUserAvailableCoupons(Long userId, BigDecimal totalAmount) {
        // 查询用户可用优惠券
        LambdaQueryWrapper<UmsUserCoupons> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UmsUserCoupons::getUserId, userId)
                   .eq(UmsUserCoupons::getStatus, 0) // 未使用状态
                   .le(UmsUserCoupons::getMinPoint, totalAmount) // 满足最低消费金额
                   .gt(UmsUserCoupons::getEndTime, LocalDateTime.now()); // 未过期
        
        List<UmsUserCoupons> coupons = umsUserCouponsMapper.selectList(queryWrapper);
        
        // 转换为响应对象
        return coupons.stream().map(coupon -> {
            ConfirmOrderResponse.CouponResponse response = new ConfirmOrderResponse.CouponResponse();
            response.setId(coupon.getId());
            response.setName(coupon.getCouponName());
            response.setAmount(coupon.getAmount());
            response.setMinPoint(coupon.getMinPoint());
            response.setUseType(coupon.getGetType()); // 使用getType替代useType
            // 转换LocalDateTime为Date
            response.setStartTime(Date.from(coupon.getStartTime().atZone(ZoneId.systemDefault()).toInstant()));
            response.setEndTime(Date.from(coupon.getEndTime().atZone(ZoneId.systemDefault()).toInstant()));
            return response;
        }).collect(Collectors.toList());
    }

    /**
     * 直接购买商品
     *
     * @param request 直接购买请求
     * @return 临时购物车项ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long directBuy(DirectBuyRequest request) {
        log.info("直接购买商品: {}", request);
        
        // 参数验证
        if (request == null || request.getProductId() == null || request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new IllegalArgumentException("商品参数不正确");
        }
        
        // 获取当前用户ID
        Long userId = getCurrentUserId();
        
        // 查询商品信息
        PmsProducts product = pmsProductsMapper.selectById(request.getProductId());
        if (product == null) {
            throw new ProductNotFoundException("商品不存在: " + request.getProductId());
        }
        
        // 创建购物车项
        OmsCartItems cartItem = new OmsCartItems();
        cartItem.setProductId(request.getProductId());
        cartItem.setSkuId(request.getProductSkuId());
        cartItem.setUserId(userId);
        cartItem.setQuantity(request.getQuantity());
        cartItem.setPrice(product.getPrice());
        cartItem.setProductImage(product.getMainImage()); // 设置商品图片
        cartItem.setProductName(product.getName());
        cartItem.setSkuSpecs(""); // 临时商品无规格
        cartItem.setChecked(1); // 设置为选中状态
        cartItem.setCreateTime(LocalDateTime.now());
        cartItem.setUpdateTime(LocalDateTime.now());
        
        // 保存购物车项
        omsCartItemsMapper.insert(cartItem);
        
        log.info("直接购买商品成功, cartItemId: {}", cartItem.getId());
        
        // 返回购物车项ID
        return cartItem.getId();
    }
} 