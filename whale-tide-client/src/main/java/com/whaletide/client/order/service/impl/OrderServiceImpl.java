package com.whaletide.client.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whaletide.client.order.dto.*;
import com.whaletide.client.order.service.IOrderService;
import com.whaletide.client.order.vo.*;
import com.whaletide.common.api.CommonPage;
import com.whaletide.common.entity.oms.OmsCartItems;
import com.whaletide.common.entity.oms.OmsOrderItems;
import com.whaletide.common.entity.oms.OmsOrders;
import com.whaletide.common.entity.pms.PmsProducts;
import com.whaletide.common.entity.ums.UmsUserAddresses;
import com.whaletide.common.entity.ums.UmsUsers;
import com.whaletide.common.exception.auth.AuthenticationException;
import com.whaletide.common.exception.base.BusinessException;
import com.whaletide.common.exception.base.ValidationException;
import com.whaletide.common.exception.order.OrderNotFoundException;
import com.whaletide.common.exception.order.OrderStatusException;
import com.whaletide.common.exception.product.ProductNotFoundException;
import com.whaletide.common.exception.product.ProductStockException;
import com.whaletide.common.mapper.oms.OmsCartItemsMapper;
import com.whaletide.common.mapper.oms.OmsOrderItemsMapper;
import com.whaletide.common.mapper.oms.OmsOrdersMapper;
import com.whaletide.common.mapper.pms.PmsProductsMapper;
import com.whaletide.common.mapper.ums.UmsUserAddressesMapper;
import com.whaletide.common.mapper.ums.UmsUsersMapper;
import com.whaletide.common.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 订单服务实现类
 */
@Service
@Slf4j
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Autowired
    private OmsOrdersMapper ordersMapper;
    
    @Autowired
    private OmsOrderItemsMapper orderItemsMapper;
    

    
    @Autowired
    private OmsCartItemsMapper cartItemsMapper;
    
    @Autowired
    private PmsProductsMapper productsMapper;
    
    @Autowired
    private UmsUserAddressesMapper addressesMapper;
    
    @Autowired
    private UmsUsersMapper usersMapper;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 生成确认订单
     */
    @Override
    @Transactional
    public ConfirmOrderVO generateConfirmOrder(GenerateConfirmOrderDTO request) {
        // 获取当前用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationException("用户未登录");
        }
        
        String username = authentication.getName();
        log.info("生成确认订单, 用户: {}", username);
        
        // 校验参数
        if (request == null || CollectionUtils.isEmpty(request.getCartItemIds())) {
            throw new ValidationException("购物项不能为空");
        }
        
        // 查询用户信息
        UmsUsers user = getUserByUsername(username);
        if (user == null) {
            throw new AuthenticationException("用户不存在");
        }
        
        // 构建缓存Key
        String cacheKey = "order:confirm:" + username + ":" + String.join(",", request.getCartItemIds().toString());
        
        // 尝试从缓存获取
        @SuppressWarnings("unchecked")
        ConfirmOrderVO cacheResult = (ConfirmOrderVO) redisTemplate.opsForValue().get(cacheKey);
        if (cacheResult != null) {
            log.info("从缓存获取确认订单信息");
            return cacheResult;
        }
        
        // 查询购物车项目
        List<OmsCartItems> cartItems = getCartItems(user.getId(), request.getCartItemIds());
        if (CollectionUtils.isEmpty(cartItems)) {
            throw new ValidationException("购物车项目不存在");
        }
        
        // 查询商品信息
        List<Long> productIds = cartItems.stream()
            .map(OmsCartItems::getProductId)
            .collect(Collectors.toList());
        Map<Long, PmsProducts> productMap = getProductMap(productIds);
        
        // 转换为订单项目
        List<OrderItemVO> orderItems = new ArrayList<>();
        double totalAmount = 0;
        
        for (OmsCartItems cartItem : cartItems) {
            PmsProducts product = productMap.get(cartItem.getProductId());
            if (product == null) {
                continue;
            }
            
            // 校验库存
            if (product.getStock() < cartItem.getQuantity()) {
                throw new ProductStockException("商品库存不足: " + product.getName());
            }
            
            OrderItemVO item = new OrderItemVO();
            item.setId(cartItem.getId());
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setProductPic(product.getMainImage());
            item.setQuantity(cartItem.getQuantity());
            item.setPrice(product.getPrice());
            item.setSubtotal(product.getPrice() * cartItem.getQuantity());
            
            orderItems.add(item);
            totalAmount += item.getSubtotal();
        }
        
        // 查询用户收货地址
        List<UmsUserAddresses> userAddresses = getUserAddresses(user.getId());
        
        // 转换地址列表
        List<AddressVO> addresses = userAddresses.stream().map(address -> {
            AddressVO vo = new AddressVO();
            vo.setId(address.getId());
            vo.setName(address.getReceiverName());
            vo.setPhone(address.getReceiverPhone());
            vo.setProvince(address.getProvince());
            vo.setCity(address.getCity());
            vo.setDistrict(address.getDistrict());
            vo.setDetail(address.getDetailAddress());
            vo.setIsDefault(address.getDefaultStatus() != null && address.getDefaultStatus() == 1);
            return vo;
        }).collect(Collectors.toList());
        
        // 构建确认订单VO
        ConfirmOrderVO result = new ConfirmOrderVO();
        result.setOrderItems(orderItems);
        result.setAddresses(addresses);
        result.setTotalAmount(totalAmount);
        
        // 将结果放入缓存
        redisTemplate.opsForValue().set(cacheKey, result, 10, TimeUnit.MINUTES);
        
        return result;
    }

    /**
     * 生成订单
     */
    @Override
    @Transactional
    public GenerateOrderVO generateOrder(GenerateOrderDTO request) {
        try {
            // 获取当前用户
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new AuthenticationException("用户未登录");
            }
            
            String username = authentication.getName();
            log.info("生成订单, 用户: {}", username);
            
            // 校验参数
            if (request == null || CollectionUtils.isEmpty(request.getCartItemIds())) {
                throw new ValidationException("购物项不能为空");
            }
            
            if (request.getAddressId() == null) {
                throw new ValidationException("收货地址不能为空");
            }
            
            // 查询用户信息
            UmsUsers user = getUserByUsername(username);
            if (user == null) {
                throw new AuthenticationException("用户不存在");
            }
            
            // 查询购物车项目
            List<OmsCartItems> cartItems = getCartItems(user.getId(), request.getCartItemIds());
            if (CollectionUtils.isEmpty(cartItems)) {
                throw new ValidationException("购物车项目不存在");
            }
            
            // 查询商品信息
            List<Long> productIds = cartItems.stream()
                .map(OmsCartItems::getProductId)
                .collect(Collectors.toList());
            Map<Long, PmsProducts> productMap = getProductMap(productIds);
            
            // 查询收货地址
            UmsUserAddresses address = addressesMapper.selectById(request.getAddressId());
            if (address == null || !Objects.equals(address.getUserId(), user.getId())) {
                throw new ValidationException("收货地址不存在或不属于当前用户");
            }
            
            // 计算订单金额
            double totalAmount = 0;
            for (OmsCartItems cartItem : cartItems) {
                PmsProducts product = productMap.get(cartItem.getProductId());
                if (product == null) {
                    continue;
                }
                
                // 校验库存
                if (product.getStock() < cartItem.getQuantity()) {
                    throw new ProductStockException("商品库存不足: " + product.getName());
                }
                
                totalAmount += product.getPrice() * cartItem.getQuantity();
            }
            
            // 优惠金额计算（这里简化处理，实际应该根据优惠规则计算）
            double discountAmount = 0;
            double payAmount = totalAmount - discountAmount;
            
            // 生成订单号
            String orderSn = generateOrderSn();
            
            // 创建订单
            OmsOrders order = new OmsOrders();
            order.setOrderSn(orderSn);
            order.setUserId(user.getId());
            order.setUserName(user.getUsername());
            order.setTotalAmount(payAmount);
            order.setPayAmount(payAmount);
            order.setFreightAmount(0.0); // 运费
            order.setDiscountAmount(discountAmount);
            order.setPayType(request.getPayType());
            order.setSourceType(1); // 来源：1-APP端
            order.setStatus(0); // 待付款
            order.setOrderType(0); // 普通订单
            order.setReceiverName(address.getReceiverName());
            order.setReceiverPhone(address.getReceiverPhone());
            order.setReceiverProvince(address.getProvince());
            order.setReceiverCity(address.getCity());
            order.setReceiverRegion(address.getDistrict());
            order.setReceiverDetailAddress(address.getDetailAddress());
            order.setNote(request.getNote());
            order.setCreateTime(LocalDateTime.now());
            
            // 保存订单
            ordersMapper.insert(order);
            
            // 创建订单项
            List<OmsOrderItems> orderItems = new ArrayList<>();
            for (OmsCartItems cartItem : cartItems) {
                PmsProducts product = productMap.get(cartItem.getProductId());
                if (product == null) {
                    continue;
                }
                
                OmsOrderItems orderItem = new OmsOrderItems();
                orderItem.setOrderId(order.getId());
                orderItem.setOrderSn(orderSn);
                orderItem.setProductId(product.getId());
                orderItem.setProductName(product.getName());
                orderItem.setProductPic(product.getMainImage());
                orderItem.setProductPrice(product.getPrice());
                orderItem.setProductQuantity(cartItem.getQuantity());
                orderItem.setProductCategoryId(product.getCategoryId());
                
                orderItems.add(orderItem);
            }
            
            // 批量保存订单项
            for (OmsOrderItems orderItem : orderItems) {
                orderItemsMapper.insert(orderItem);
            }
            
            // 删除购物车项
            for (OmsCartItems cartItem : cartItems) {
                cartItemsMapper.deleteById(cartItem.getId());
            }
            
            // 减少库存（实际应该使用消息队列异步处理）
            for (OmsCartItems cartItem : cartItems) {
                PmsProducts product = productMap.get(cartItem.getProductId());
                if (product != null) {
                    product.setStock(product.getStock() - cartItem.getQuantity());
                    productsMapper.updateById(product);
                }
            }
            

            
            // 构建返回结果
            GenerateOrderVO result = new GenerateOrderVO();
            result.setOrderId(order.getId());
            result.setOrderSn(orderSn);
            result.setPayAmount(payAmount);
            
            return result;
            
        } catch (AuthenticationException | ValidationException | ProductStockException e) {
            log.error("生成订单失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("生成订单异常: {}", e.getMessage(), e);
            throw new BusinessException("生成订单失败: " + e.getMessage());
        }
    }

    /**
     * 获取订单列表
     */
    @Override
    public CommonPage<OrderListItemVO> getOrderList(OrderListQueryDTO request) {
        try {
            // 获取当前用户
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new AuthenticationException("用户未登录");
            }
            
            String username = authentication.getName();
            log.info("获取订单列表, 用户: {}, 页码: {}, 每页数量: {}", username, request.getPageNum(), request.getPageSize());
            
            // 查询用户信息
            UmsUsers user = getUserByUsername(username);
            if (user == null) {
                throw new AuthenticationException("用户不存在");
            }
            
            // 构建缓存Key
            String cacheKey = "order:list:" + username + ":" + request.getStatus() + ":" 
                + request.getPageNum() + ":" + request.getPageSize();
            
            // 强制清除缓存
            redisTemplate.delete(cacheKey);
            
            // 构建查询条件
            LambdaQueryWrapper<OmsOrders> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(OmsOrders::getUserId, user.getId());
            
            // 按状态筛选
            if (request.getStatus() != null) {
                queryWrapper.eq(OmsOrders::getStatus, request.getStatus());
            }
            
            // 按创建时间倒序
            queryWrapper.orderByDesc(OmsOrders::getCreateTime);
            
            // 分页查询
            Page<OmsOrders> page = new Page<>(request.getPageNum(), request.getPageSize());
            Page<OmsOrders> orderPage = ordersMapper.selectPage(page, queryWrapper);
            
            // 获取订单项
            List<OrderListItemVO> orderList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(orderPage.getRecords())) {
                // 获取所有订单ID
                List<Long> orderIds = orderPage.getRecords().stream()
                    .map(OmsOrders::getId)
                    .collect(Collectors.toList());
                
                // 查询所有订单项
                Map<Long, List<OmsOrderItems>> orderItemsMap = getOrderItemsMap(orderIds);
                
                // 转换为VO
                for (OmsOrders order : orderPage.getRecords()) {
                    OrderListItemVO vo = new OrderListItemVO();
                    vo.setId(order.getId());
                    vo.setOrderSn(order.getOrderSn());
                    vo.setCreateTime(order.getCreateTime());
                    
                    // 确保显示订单的总金额
                    double totalAmount = order.getTotalAmount() != null && order.getTotalAmount() > 0 ? 
                        order.getTotalAmount() : 0.0;
                    vo.setTotalAmount(totalAmount);
                    
                    vo.setStatus(order.getStatus());
                    vo.setStatusText(getOrderStatusText(order.getStatus()));
                    
                    // 设置订单项
                    List<OmsOrderItems> items = orderItemsMap.getOrDefault(order.getId(), new ArrayList<>());
                    List<OrderItemVO> orderItems = new ArrayList<>();
                    double calculatedTotal = 0.0;  // 用于重新计算订单总额
                    
                    for (OmsOrderItems item : items) {
                        OrderItemVO itemVO = new OrderItemVO();
                        itemVO.setId(item.getId());
                        itemVO.setProductId(item.getProductId());
                        itemVO.setProductName(item.getProductName());
                        itemVO.setProductPic(item.getProductPic());
                        itemVO.setQuantity(item.getProductQuantity());
                        
                        // 强制从产品表获取价格
                        PmsProducts product = productsMapper.selectById(item.getProductId());
                        Double productPrice = 0.0;
                        
                        if (product != null && product.getPrice() != null && product.getPrice() > 0) {
                            productPrice = product.getPrice();
                            // 更新订单项中的价格
                            item.setProductPrice(productPrice);
                            orderItemsMapper.updateById(item);
                            log.info("已更新订单项{}的价格: {}", item.getId(), productPrice);
                        } else if (item.getProductPrice() != null && item.getProductPrice() > 0) {
                            productPrice = item.getProductPrice();
                        }
                        
                        itemVO.setPrice(productPrice);
                        
                        // 计算小计金额
                        double subtotal = 0.0;
                        if (itemVO.getQuantity() != null) {
                            subtotal = productPrice * itemVO.getQuantity();
                            calculatedTotal += subtotal;
                        }
                        itemVO.setSubtotal(subtotal);
                        
                        orderItems.add(itemVO);
                    }
                    
                    // 如果订单总额为0，但计算出的总额大于0，则使用计算出的总额
                    if ((totalAmount == 0 || totalAmount == 0.0) && calculatedTotal > 0) {
                        vo.setTotalAmount(calculatedTotal);
                        // 更新订单表中的总额
                        order.setTotalAmount(calculatedTotal);
                        ordersMapper.updateById(order);
                        log.info("已更新订单{}的总金额: {}", order.getId(), calculatedTotal);
                    }
                    
                    vo.setOrderItems(orderItems);
                    orderList.add(vo);
                }
            }
            
            // 构建分页结果
            CommonPage<OrderListItemVO> result = new CommonPage<>();
            result.setList(orderList);
            result.setTotal(orderPage.getTotal());
            result.setPageNum((int)orderPage.getCurrent());
            result.setPageSize((int)orderPage.getSize());
            result.setTotalPage((int)orderPage.getPages());
            
            // 将结果放入缓存
            redisTemplate.opsForValue().set(cacheKey, result, 5, TimeUnit.MINUTES);
            
            return result;
            
        } catch (AuthenticationException e) {
            log.error("获取订单列表失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("获取订单列表异常: {}", e.getMessage(), e);
            throw new BusinessException("获取订单列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取订单详情
     */
    @Override
    public OrderDetailVO getOrderDetail(Long orderId) {
        try {
            // 参数校验
            if (orderId == null) {
                throw new ValidationException("订单ID不能为空");
            }
            
            // 获取当前用户
            Long userId = jwtTokenUtil.getCurrentUserId();
            String username = jwtTokenUtil.getCurrentUsername();
            
            // 如果无法直接获取用户ID，则通过用户名查询
            if (userId == null && username != null) {
                log.debug("通过getCurrentUserId()未获取到用户ID，尝试通过用户名查询: {}", username);
                UmsUsers user = getUserByUsername(username);
                if (user != null) {
                    userId = user.getId();
                    log.debug("通过用户名查询到用户ID: {}", userId);
                }
            }
            
            if (userId == null) {
                throw new AuthenticationException("用户未登录");
            }
            
            log.info("获取订单详情, 用户: {}, 订单ID: {}", username, orderId);
            
            // 构建缓存Key
            String cacheKey = "order:detail:" + username + ":" + orderId;
            
            // 强制清除缓存，确保总是读取最新数据
            redisTemplate.delete(cacheKey);
            
            // 查询订单
            OmsOrders order = getOrderById(orderId, userId);
            if (order == null) {
                throw new OrderNotFoundException("订单不存在: " + orderId);
            }
            
            // 查询订单项
            List<OmsOrderItems> orderItems = getOrderItems(order.getId());
            
            // 转换为VO
            OrderDetailVO detail = new OrderDetailVO();
            detail.setId(order.getId());
            detail.setOrderSn(order.getOrderSn());
            detail.setCreateTime(order.getCreateTime());
            detail.setPayType(order.getPayType());
            detail.setPayTypeText(getPayTypeText(order.getPayType()));
            detail.setStatus(order.getStatus());
            detail.setStatusText(getOrderStatusText(order.getStatus()));
            
            // 确保显示订单的总金额
            double totalAmount = order.getTotalAmount() != null && order.getTotalAmount() > 0 ? 
                order.getTotalAmount() : 0.0;
            detail.setTotalAmount(totalAmount);
            
            detail.setPayAmount(order.getPayAmount());
            detail.setFreightAmount(order.getFreightAmount());
            detail.setDiscountAmount(order.getDiscountAmount());
            detail.setPayTime(order.getPaymentTime());
            detail.setDeliveryTime(order.getDeliveryTime());
            detail.setReceiveTime(order.getReceiveTime());
            detail.setNote(order.getNote());
            
            // 设置收货人信息
            detail.setReceiverName(order.getReceiverName());
            detail.setReceiverPhone(order.getReceiverPhone());
            
            // 修复收货地址显示为空的问题
            StringBuilder addressBuilder = new StringBuilder();
            if (order.getReceiverProvince() != null && !order.getReceiverProvince().trim().isEmpty()) {
                addressBuilder.append(order.getReceiverProvince().trim()).append(" ");
            }
            if (order.getReceiverCity() != null && !order.getReceiverCity().trim().isEmpty()) {
                addressBuilder.append(order.getReceiverCity().trim()).append(" ");
            }
            if (order.getReceiverRegion() != null && !order.getReceiverRegion().trim().isEmpty()) {
                addressBuilder.append(order.getReceiverRegion().trim()).append(" ");
            }
            if (order.getReceiverDetailAddress() != null && !order.getReceiverDetailAddress().trim().isEmpty()) {
                addressBuilder.append(order.getReceiverDetailAddress().trim());
            }
            
            String receiverAddress = addressBuilder.toString().trim();
            if (receiverAddress.isEmpty()) {
                receiverAddress = "地址信息不完整";
            }
            
            detail.setReceiverAddress(receiverAddress);
            
            // 设置订单项
            List<OrderItemVO> itemVOList = new ArrayList<>();
            double calculatedTotal = 0.0;  // 用于重新计算订单总额
            
            for (OmsOrderItems item : orderItems) {
                OrderItemVO itemVO = new OrderItemVO();
                itemVO.setId(item.getId());
                itemVO.setProductId(item.getProductId());
                itemVO.setProductName(item.getProductName());
                itemVO.setProductPic(item.getProductPic());
                itemVO.setQuantity(item.getProductQuantity());
                
                // 强制从产品表获取价格
                PmsProducts product = productsMapper.selectById(item.getProductId());
                Double productPrice = 0.0;
                
                if (product != null && product.getPrice() != null && product.getPrice() > 0) {
                    productPrice = product.getPrice();
                    // 更新订单项中的价格
                    item.setProductPrice(productPrice);
                    orderItemsMapper.updateById(item);
                    log.info("已更新订单项{}的价格: {}", item.getId(), productPrice);
                } else if (item.getProductPrice() != null && item.getProductPrice() > 0) {
                    productPrice = item.getProductPrice();
                }
                
                itemVO.setPrice(productPrice);
                
                // 计算小计金额
                double subtotal = 0.0;
                if (itemVO.getQuantity() != null) {
                    subtotal = productPrice * itemVO.getQuantity();
                    calculatedTotal += subtotal;
                }
                itemVO.setSubtotal(subtotal);
                
                itemVOList.add(itemVO);
            }
            
            // 如果订单总额为0，但计算出的总额大于0，则使用计算出的总额
            if ((totalAmount == 0 || totalAmount == 0.0) && calculatedTotal > 0) {
                detail.setTotalAmount(calculatedTotal);
                // 更新订单表中的总额
                order.setTotalAmount(calculatedTotal);
                ordersMapper.updateById(order);
                log.info("已更新订单{}的总金额: {}", order.getId(), calculatedTotal);
            }
            
            detail.setOrderItems(itemVOList);
            
            // 将结果放入缓存
            redisTemplate.opsForValue().set(cacheKey, detail, 10, TimeUnit.MINUTES);
            
            return detail;
            
        } catch (OrderNotFoundException | AuthenticationException | ValidationException e) {
            log.error("获取订单详情失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("获取订单详情异常: {}", e.getMessage(), e);
            throw new BusinessException("获取订单详情失败: " + e.getMessage());
        }
    }

    /**
     * 取消订单
     */
    @Override
    @Transactional
    public Boolean cancelOrder(CancelOrderDTO request) {
        try {
            // 参数校验
            if (request == null || request.getOrderId() == null) {
                throw new ValidationException("订单ID不能为空");
            }
            
            // 获取当前用户
            Long userId = jwtTokenUtil.getCurrentUserId();
            String username = jwtTokenUtil.getCurrentUsername();
            
            // 如果无法直接获取用户ID，则通过用户名查询
            if (userId == null && username != null) {
                UmsUsers user = getUserByUsername(username);
                if (user != null) {
                    userId = user.getId();
                }
            }
            
            if (userId == null) {
                throw new AuthenticationException("用户未登录");
            }
            
            log.info("取消订单, 用户: {}, 订单ID: {}", username, request.getOrderId());
            
            // 查询订单
            OmsOrders order = getOrderById(request.getOrderId(), userId);
            if (order == null) {
                throw new OrderNotFoundException("订单不存在: " + request.getOrderId());
            }
            
            // 校验订单状态
            if (order.getStatus() != 0) {
                throw new OrderStatusException("订单状态不支持取消");
            }
            
            // 更新订单状态
            order.setStatus(4); // 已取消
            order.setCancelTime(LocalDateTime.now());
            order.setCancelReason(request.getReason());
            ordersMapper.updateById(order);
            
            // 恢复库存
            List<OmsOrderItems> orderItems = getOrderItems(order.getId());
            for (OmsOrderItems item : orderItems) {
                PmsProducts product = productsMapper.selectById(item.getProductId());
                if (product != null) {
                    product.setStock(product.getStock() + item.getProductQuantity());
                    productsMapper.updateById(product);
                }
            }
            

            
            // 删除缓存
            clearOrderCache(username, order.getId());
            
            return Boolean.TRUE;
            
        } catch (OrderNotFoundException | OrderStatusException | AuthenticationException | ValidationException e) {
            log.error("取消订单失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("取消订单异常: {}", e.getMessage(), e);
            throw new BusinessException("取消订单失败: " + e.getMessage());
        }
    }

    /**
     * 确认收货
     */
    @Override
    @Transactional
    public Boolean confirmReceiveOrder(ConfirmReceiveDTO request) {
        try {
            // 参数校验
            if (request == null || request.getOrderId() == null) {
                throw new ValidationException("订单ID不能为空");
            }
            
            // 获取当前用户
            Long userId = jwtTokenUtil.getCurrentUserId();
            String username = jwtTokenUtil.getCurrentUsername();
            
            // 如果无法直接获取用户ID，则通过用户名查询
            if (userId == null && username != null) {
                UmsUsers user = getUserByUsername(username);
                if (user != null) {
                    userId = user.getId();
                }
            }
            
            if (userId == null) {
                throw new AuthenticationException("用户未登录");
            }
            
            log.info("确认收货, 用户: {}, 订单ID: {}", username, request.getOrderId());
            
            // 查询订单
            OmsOrders order = getOrderById(request.getOrderId(), userId);
            if (order == null) {
                throw new OrderNotFoundException("订单不存在: " + request.getOrderId());
            }
            
            // 校验订单状态
            if (order.getStatus() != 2) {
                throw new OrderStatusException("订单状态不支持确认收货");
            }
            
            // 更新订单状态
            order.setStatus(3); // 已完成
            order.setReceiveTime(LocalDateTime.now());
            ordersMapper.updateById(order);
            

            
            // 删除缓存
            clearOrderCache(username, order.getId());
            
            return Boolean.TRUE;
            
        } catch (OrderNotFoundException | OrderStatusException | AuthenticationException | ValidationException e) {
            log.error("确认收货失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("确认收货异常: {}", e.getMessage(), e);
            throw new BusinessException("确认收货失败: " + e.getMessage());
        }
    }

    /**
     * 删除订单
     */
    @Override
    @Transactional
    public Boolean deleteOrder(DeleteOrderDTO request) {
        try {
            // 参数校验
            if (request == null || request.getOrderId() == null) {
                throw new ValidationException("订单ID不能为空");
            }
            
            // 获取当前用户
            Long userId = jwtTokenUtil.getCurrentUserId();
            String username = jwtTokenUtil.getCurrentUsername();
            
            // 如果无法直接获取用户ID，则通过用户名查询
            if (userId == null && username != null) {
                UmsUsers user = getUserByUsername(username);
                if (user != null) {
                    userId = user.getId();
                }
            }
            
            if (userId == null) {
                throw new AuthenticationException("用户未登录");
            }
            
            log.info("删除订单, 用户: {}, 订单ID: {}", username, request.getOrderId());
            
            // 查询订单
            OmsOrders order = getOrderById(request.getOrderId(), userId);
            if (order == null) {
                throw new OrderNotFoundException("订单不存在: " + request.getOrderId());
            }
            
            // 校验订单状态（只有已完成或已取消的订单才能删除）
            if (order.getStatus() != 3 && order.getStatus() != 4) {
                throw new OrderStatusException("只有已完成或已取消的订单才能删除");
            }
            
            // 标记订单为已删除（逻辑删除）
            order.setDeleteStatus(1);
            ordersMapper.updateById(order);
            

            
            // 删除缓存
            clearOrderCache(username, order.getId());
            
            return Boolean.TRUE;
            
        } catch (OrderNotFoundException | OrderStatusException | AuthenticationException | ValidationException e) {
            log.error("删除订单失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("删除订单异常: {}", e.getMessage(), e);
            throw new BusinessException("删除订单失败: " + e.getMessage());
        }
    }

    /**
     * 支付成功处理（简化版）
     */
    @Override
    @Transactional
    public PaySuccessVO paySuccess(PaySuccessDTO request) {
        try {
            // 参数校验
            if (request == null || request.getOrderId() == null) {
                throw new ValidationException("订单ID不能为空");
            }
            
            // 获取当前用户
            Long userId = jwtTokenUtil.getCurrentUserId();
            String username = jwtTokenUtil.getCurrentUsername();
            
            // 如果无法直接获取用户ID，则通过用户名查询
            if (userId == null && username != null) {
                UmsUsers user = getUserByUsername(username);
                if (user != null) {
                    userId = user.getId();
                }
            }
            
            if (userId == null) {
                throw new AuthenticationException("用户未登录");
            }
            
            log.info("支付成功处理, 用户: {}, 订单ID: {}", username, request.getOrderId());
            
            // 查询订单
            OmsOrders order = getOrderById(request.getOrderId(), userId);
            if (order == null) {
                throw new OrderNotFoundException("订单不存在: " + request.getOrderId());
            }
            
            // 校验订单状态
            if (order.getStatus() != 0) {
                throw new OrderStatusException("订单状态不支持支付");
            }
            
            // 更新订单状态
            order.setStatus(1); // 已支付，待发货
            order.setPaymentTime(LocalDateTime.now());
            order.setPayType(request.getPayType());
            ordersMapper.updateById(order);
            

            
            // 删除缓存
            clearOrderCache(username, order.getId());
            
            // 构建返回结果
            PaySuccessVO result = new PaySuccessVO();
            result.setOrderId(order.getId());
            result.setOrderSn(order.getOrderSn());
            result.setSuccess(true);
            
            return result;
            
        } catch (OrderNotFoundException | OrderStatusException | AuthenticationException | ValidationException e) {
            log.error("支付成功处理失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("支付成功处理异常: {}", e.getMessage(), e);
            throw new BusinessException("支付成功处理失败: " + e.getMessage());
        }
    }


    /**
     * 直接购买商品（简化版）
     */
    @Override
    @Transactional
    public Long directBuy(DirectBuyDTO request) {
        try {
            // 参数校验
            if (request == null || request.getProductId() == null || request.getQuantity() == null || request.getQuantity() <= 0) {
                throw new ValidationException("商品ID和数量不能为空");
            }
            
            // 获取用户ID，优先使用请求中的用户ID
            Long userId = null;
            if (request.getUserId() != null) {
                // 如果请求中包含用户ID，优先使用该ID
                userId = request.getUserId();
                log.info("使用请求中的用户ID: {}", userId);
            } else {
                // 否则尝试从JWT中获取
                userId = jwtTokenUtil.getCurrentUserId();
                log.info("从JWT获取用户ID: {}", userId);
            }
            
            // 验证用户ID
            if (userId == null) {
                throw new AuthenticationException("用户未登录");
            }
            
            // 验证用户是否存在
            UmsUsers user = usersMapper.selectById(userId);
            if (user == null) {
                throw new AuthenticationException("用户不存在: " + userId);
            }
            
            String username = user.getUsername();
            log.info("直接购买, 用户: {}(ID:{}), 商品ID: {}, 数量: {}", 
                    username, userId, request.getProductId(), request.getQuantity());
            
            // 查询商品信息
            PmsProducts product = productsMapper.selectById(request.getProductId());
            if (product == null) {
                throw new ProductNotFoundException("商品不存在: " + request.getProductId());
            }
            
            // 校验库存
            if (product.getStock() < request.getQuantity()) {
                throw new ProductStockException("商品库存不足: " + product.getName());
            }
            
            // 创建临时购物车项
            OmsCartItems cartItem = new OmsCartItems();
            cartItem.setProductId(product.getId());
            cartItem.setProductName(product.getName());
            cartItem.setProductImage(product.getMainImage());
            cartItem.setPrice(new BigDecimal(product.getPrice().toString()));
            cartItem.setQuantity(request.getQuantity());
            cartItem.setUserId(userId);
            cartItem.setCreateTime(LocalDateTime.now());
            cartItem.setUpdateTime(LocalDateTime.now());
            cartItem.setChecked(1); // 默认选中
            
            // 保存购物车项
            cartItemsMapper.insert(cartItem);
            
            // 返回购物车项ID
            return cartItem.getId();
            
        } catch (ProductNotFoundException | ProductStockException | AuthenticationException | ValidationException e) {
            log.error("直接购买失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("直接购买异常: {}", e.getMessage(), e);
            throw new BusinessException("直接购买失败: " + e.getMessage());
        }
    }

    /**
     * 获取订单状态文本
     */
    private String getOrderStatusText(Integer status) {
        if (status == null) {
            return "未知状态";
        }
        
        switch (status) {
            case 0:
                return "待付款";
            case 1:
                return "待发货";
            case 2:
                return "待收货";
            case 3:
                return "已完成";
            case 4:
                return "已取消";
            case 5:
                return "已关闭";
            default:
                return "未知状态";
        }
    }
    
    /**
     * 获取支付类型文本
     */
    private String getPayTypeText(Integer payType) {
        if (payType == null) {
            return "未知方式";
        }
        
        switch (payType) {
            case 0:
                return "未支付";
            case 1:
                return "支付宝";
            case 2:
                return "微信支付";
            case 3:
                return "银联支付";
            default:
                return "其他方式";
        }
    }
    
    /**
     * 根据用户名获取用户
     */
    private UmsUsers getUserByUsername(String username) {
        LambdaQueryWrapper<UmsUsers> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UmsUsers::getUsername, username);
        return usersMapper.selectOne(queryWrapper);
    }
    
    /**
     * 获取用户的购物车项
     */
    private List<OmsCartItems> getCartItems(Long userId, List<Long> cartItemIds) {
        LambdaQueryWrapper<OmsCartItems> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OmsCartItems::getUserId, userId)
            .in(OmsCartItems::getId, cartItemIds);
        return cartItemsMapper.selectList(queryWrapper);
    }
    
    /**
     * 获取商品映射
     */
    private Map<Long, PmsProducts> getProductMap(List<Long> productIds) {
        if (CollectionUtils.isEmpty(productIds)) {
            return new HashMap<>();
        }
        
        LambdaQueryWrapper<PmsProducts> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(PmsProducts::getId, productIds);
        List<PmsProducts> products = productsMapper.selectList(queryWrapper);
        
        return products.stream().collect(Collectors.toMap(PmsProducts::getId, product -> product));
    }
    
    /**
     * 获取用户地址列表
     */
    private List<UmsUserAddresses> getUserAddresses(Long userId) {
        LambdaQueryWrapper<UmsUserAddresses> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UmsUserAddresses::getUserId, userId)
            .orderByDesc(UmsUserAddresses::getDefaultStatus, UmsUserAddresses::getUpdateTime);
        return addressesMapper.selectList(queryWrapper);
    }
    
    /**
     * 获取订单
     */
    private OmsOrders getOrderById(Long orderId, Long userId) {
        LambdaQueryWrapper<OmsOrders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OmsOrders::getId, orderId)
            .eq(OmsOrders::getUserId, userId)
            .eq(OmsOrders::getDeleteStatus, 0);
        return ordersMapper.selectOne(queryWrapper);
    }
    
    /**
     * 获取订单项
     */
    private List<OmsOrderItems> getOrderItems(Long orderId) {
        LambdaQueryWrapper<OmsOrderItems> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OmsOrderItems::getOrderId, orderId);
        return orderItemsMapper.selectList(queryWrapper);
    }
    
    /**
     * 获取订单项映射
     */
    private Map<Long, List<OmsOrderItems>> getOrderItemsMap(List<Long> orderIds) {
        if (CollectionUtils.isEmpty(orderIds)) {
            return new HashMap<>();
        }
        
        LambdaQueryWrapper<OmsOrderItems> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(OmsOrderItems::getOrderId, orderIds);
        List<OmsOrderItems> orderItems = orderItemsMapper.selectList(queryWrapper);
        
        return orderItems.stream().collect(Collectors.groupingBy(OmsOrderItems::getOrderId));
    }
    

    /**
     * 清除订单缓存
     */
    private void clearOrderCache(String username, Long orderId) {
        redisTemplate.delete("order:detail:" + username + ":" + orderId);
        // 删除列表缓存
        Set<String> keys = redisTemplate.keys("order:list:" + username + ":*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
    
    /**
     * 生成订单编号
     */
    private String generateOrderSn() {
        return "WT" + System.currentTimeMillis() + String.format("%04d", new Random().nextInt(10000));
    }

} 