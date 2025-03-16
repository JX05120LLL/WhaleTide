package com.whale_tide.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale_tide.dto.order.*;
import com.whale_tide.entity.oms.OmsOrderDeliveries;
import com.whale_tide.entity.oms.OmsOrderItems;
import com.whale_tide.entity.oms.OmsOrderStatusHistory;
import com.whale_tide.entity.oms.OmsOrders;
import com.whale_tide.entity.ums.UmsUserAddresses;
import com.whale_tide.entity.sms.mapper.oms.OmsOrderDeliveriesMapper;
import com.whale_tide.entity.sms.mapper.oms.OmsOrderItemsMapper;
import com.whale_tide.entity.sms.mapper.oms.OmsOrderStatusHistoryMapper;
import com.whale_tide.entity.sms.mapper.oms.OmsOrdersMapper;
import com.whale_tide.entity.sms.mapper.ums.UmsUserAddressesMapper;
import com.whale_tide.service.IOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单管理服务实现类
 */
@Service
public class OrderServiceImpl  implements IOrderService {

    @Autowired
    private OmsOrdersMapper ordersMapper;

    @Autowired
    private OmsOrderDeliveriesMapper orderDeliveriesMapper;
    
    @Autowired
    private OmsOrderItemsMapper orderItemsMapper;
    
    @Autowired
    private OmsOrderStatusHistoryMapper orderStatusHistoryMapper;

    @Autowired
    private UmsUserAddressesMapper userAddressesMapper;

    @Override
    public IPage<OrderResult> getOrderList(OrderQueryParam queryParam) {
        // 构建查询条件
        LambdaQueryWrapper<OmsOrders> queryWrapper = new LambdaQueryWrapper<>();
        
        // 订单编号查询
        if (StringUtils.hasText(queryParam.getOrderSn())) {
            queryWrapper.like(OmsOrders::getOrderSn, queryParam.getOrderSn());
        }
        
        // 订单状态查询
        if (queryParam.getStatus() != null) {
            queryWrapper.eq(OmsOrders::getStatus, queryParam.getStatus());
        }
        
        // 订单类型查询
        if (queryParam.getOrderType() != null) {
            queryWrapper.eq(OmsOrders::getOrderType, queryParam.getOrderType());
        }
        
        // 订单来源查询
        if (queryParam.getSourceType() != null) {
            queryWrapper.eq(OmsOrders::getSourceType, queryParam.getSourceType());
        }
        
        // 创建时间查询
        if (StringUtils.hasText(queryParam.getCreateTime())) {
            queryWrapper.like(OmsOrders::getCreateTime, queryParam.getCreateTime());
        }
        
        // 设置分页参数
        Integer pageNum = queryParam.getPageNum() != null ? queryParam.getPageNum() : 1;
        Integer pageSize = queryParam.getPageSize() != null ? queryParam.getPageSize() : 10;
        Page<OmsOrders> page = new Page<>(pageNum, pageSize);
        
        // 执行查询
        Page<OmsOrders> orderPage = ordersMapper.selectPage(page, queryWrapper);
        
        // 转换为OrderResult
        List<OrderResult> resultList = orderPage.getRecords().stream().map(order -> {
            OrderResult result = new OrderResult();
            BeanUtils.copyProperties(order, result);
            return result;
        }).collect(Collectors.toList());
        
        // 封装结果
        Page<OrderResult> resultPage = new Page<>(orderPage.getCurrent(), orderPage.getSize(), orderPage.getTotal());
        resultPage.setRecords(resultList);
        
        return resultPage;
    }

    @Override
    public int closeOrder(CloseOrderParam closeOrderParam) {
        // 1. 解析订单ID字符串，转换为List<Long>
        String ids = closeOrderParam.getIds();
        String note = closeOrderParam.getNote();
        
        if (StringUtils.isEmpty(ids)) {
            return 0;
        }
        
        // 将逗号分隔的id字符串转为Long类型的列表
        List<Long> orderIds = Arrays.stream(ids.split(","))
                .map(id -> Long.parseLong(id.trim()))
                .collect(Collectors.toList());
        
        if (orderIds.isEmpty()) {
            return 0;
        }
        
        // 2. 更新订单状态为已关闭(状态4)
        LambdaUpdateWrapper<OmsOrders> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(OmsOrders::getId, orderIds)
                // 只能关闭待付款的订单(状态0)
                .eq(OmsOrders::getStatus, 0)
                .set(OmsOrders::getStatus, 4)
                .set(StringUtils.hasText(note), OmsOrders::getOrderNote, note);
        
        int count = ordersMapper.update(null, updateWrapper);

        return count;
    }

    @Override
    public int deleteOrder(DeleteOrderParam deleteOrderParam) {
        // 1. 解析订单ID字符串，转换为List<Long>
        String ids = deleteOrderParam.getIds();
        if (ids == null) {
            return 0;
        }
        // 将逗号分隔的id字符串转为Long类型列表
        List<Long> orderIds = Arrays.stream(ids.split(","))
                .map(id -> Long.parseLong(id.trim()))
                .collect(Collectors.toList());
        //删除订单
        if (orderIds.isEmpty()) {
            return 0;
        }
        LambdaUpdateWrapper<OmsOrders> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(OmsOrders::getId, orderIds)
                .set(OmsOrders::getIsDeleted, 1);
        int count = ordersMapper.update(null, updateWrapper);


        return count;
    }

    @Override
    public int deliver(OrderDeliveryParam orderDeliveryParam) {
        //解析获取订单ID，物流公司，物流单号，商品项ID列表
        Long orderId = orderDeliveryParam.getOrderId();
        String deliveryCompany = orderDeliveryParam.getDeliveryCompany();
        String deliverySn = orderDeliveryParam.getDeliverySn();
        List<String> orderItemIds = orderDeliveryParam.getOrderItemIds();
        // 更新订单物流信息
        LambdaUpdateWrapper<OmsOrderDeliveries> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(OmsOrderDeliveries::getOrderId, orderId)
                .set(OmsOrderDeliveries::getDeliverySn, deliverySn)
                .set(OmsOrderDeliveries::getDeliveryCompany, deliveryCompany)
                .set(OmsOrderDeliveries::getDeliveryCompanyCode, "default")
                .set(OmsOrderDeliveries::getDeliveryStatus, 1)
                .set(OmsOrderDeliveries::getDeliveryTime, LocalDateTime.now());
        int count = orderDeliveriesMapper.update(null, updateWrapper);

        // 更新订单状态
        if (count > 0) {
            LambdaUpdateWrapper<OmsOrders> orderUpdateWrapper = new LambdaUpdateWrapper<>();
            orderUpdateWrapper.eq(OmsOrders::getId, orderId)
                    .set(OmsOrders::getStatus, 2)
                    .set(OmsOrders::getDeliveryTime, LocalDateTime.now());
            ordersMapper.update(null, orderUpdateWrapper);
        }
        return count;
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDetailResult getOrderDetail(Long orderId) {
        // 1. 获取订单基本信息
        OmsOrders order = ordersMapper.selectById(orderId);
        if (order == null) {
            return null;
        }
        
        // 2. 转换为DTO
        OrderDetailResult result = new OrderDetailResult();
        BeanUtils.copyProperties(order, result);
        
        // 订单编号
        result.setOrderSn(order.getOrderSn());
        
        // 订单备注
        result.setOrderNote(order.getOrderNote());
        
        // 3. 获取订单商品列表
        LambdaQueryWrapper<OmsOrderItems> itemQueryWrapper = new LambdaQueryWrapper<>();
        itemQueryWrapper.eq(OmsOrderItems::getOrderId, orderId);
        List<OmsOrderItems> orderItems = orderItemsMapper.selectList(itemQueryWrapper);
        
        // 转换订单商品
        List<OrderItemDTO> orderItemDTOs = orderItems.stream().map(item -> {
            OrderItemDTO itemDTO = new OrderItemDTO();
            BeanUtils.copyProperties(item, itemDTO);
            // 设置SKU价格
            itemDTO.setSkuPrice(item.getPrice());
            // 设置商品总价
            itemDTO.setTotalAmount(item.getRealAmount());
            return itemDTO;
        }).collect(Collectors.toList());
        
        result.setOrderItemList(orderItemDTOs);
        
        // 4. 获取订单物流信息
        LambdaQueryWrapper<OmsOrderDeliveries> deliveryQueryWrapper = new LambdaQueryWrapper<>();
        deliveryQueryWrapper.eq(OmsOrderDeliveries::getOrderId, orderId);
        OmsOrderDeliveries delivery = orderDeliveriesMapper.selectOne(deliveryQueryWrapper);
        
        if (delivery != null) {
            result.setDeliveryCompany(delivery.getDeliveryCompany());
            result.setDeliverySn(delivery.getDeliverySn());
        }
        
        // 5. 获取订单状态历史
        LambdaQueryWrapper<OmsOrderStatusHistory> historyQueryWrapper = new LambdaQueryWrapper<>();
        historyQueryWrapper.eq(OmsOrderStatusHistory::getOrderId, orderId)
                .orderByDesc(OmsOrderStatusHistory::getCreateTime);
        List<OmsOrderStatusHistory> statusHistories = orderStatusHistoryMapper.selectList(historyQueryWrapper);
        
        // 转换状态历史
        List<OrderHistoryDTO> historyDTOs = statusHistories.stream().map(history -> {
            OrderHistoryDTO historyDTO = new OrderHistoryDTO();
            BeanUtils.copyProperties(history, historyDTO);
            historyDTO.setOrderStatus(history.getCurrentStatus());
            return historyDTO;
        }).collect(Collectors.toList());
        
        result.setHistoryList(historyDTOs);
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateReceiverInfo(ReceiverInfoParam receiverInfoParam) {
        // 1. 解析参数
        String receiverName = receiverInfoParam.getReceiverName();
        String receiverPhone = receiverInfoParam.getReceiverPhone();
        String receiverPostCode = receiverInfoParam.getReceiverPostCode();
        String receiverProvince = receiverInfoParam.getReceiverProvince();
        String receiverCity = receiverInfoParam.getReceiverCity();
        String receiverRegion = receiverInfoParam.getReceiverRegion();
        String receiverDetailAddress = receiverInfoParam.getReceiverDetailAddress();
        Integer status = receiverInfoParam.getStatus();

        // 2. 更新订单表中的用户ID
        LambdaUpdateWrapper<OmsOrders> orderUpdateWrapper = new LambdaUpdateWrapper<>();
        orderUpdateWrapper.eq(OmsOrders::getId, receiverInfoParam.getOrderId())
                .set(OmsOrders::getUserId, receiverInfoParam.getUserId());
        
        // 如果状态不为空，则同时更新订单状态
        if (status != null) {
            orderUpdateWrapper.set(OmsOrders::getStatus, status);
        }
        
        int orderCount = ordersMapper.update(null, orderUpdateWrapper);
        if (orderCount == 0) {
            return 0;
        }

        // 3. 更新用户地址信息
        LambdaUpdateWrapper<UmsUserAddresses> addressUpdateWrapper = new LambdaUpdateWrapper<>();
        addressUpdateWrapper.eq(UmsUserAddresses::getUserId, receiverInfoParam.getUserId())
                .eq(UmsUserAddresses::getIsDeleted, 0)
                .set(UmsUserAddresses::getReceiverName, receiverName)
                .set(UmsUserAddresses::getReceiverPhone, receiverPhone)
                .set(UmsUserAddresses::getProvince, receiverProvince)
                .set(UmsUserAddresses::getCity, receiverCity)
                .set(UmsUserAddresses::getDistrict, receiverRegion)
                .set(UmsUserAddresses::getDetailAddress, receiverDetailAddress)
                .set(UmsUserAddresses::getPostalCode, receiverPostCode);

        // 4. 执行地址更新
        int addressCount = userAddressesMapper.update(null, addressUpdateWrapper);

        // 5. 如果更新成功，记录状态变更历史
        if (addressCount > 0) {
            OmsOrderStatusHistory history = new OmsOrderStatusHistory();
            history.setOrderId(receiverInfoParam.getOrderId());
            history.setOrderSn(ordersMapper.selectById(receiverInfoParam.getOrderId()).getOrderSn());
            history.setPreviousStatus(ordersMapper.selectById(receiverInfoParam.getOrderId()).getStatus());
            history.setCurrentStatus(status != null ? status : ordersMapper.selectById(receiverInfoParam.getOrderId()).getStatus());
            history.setOperatorType(3); // 3-管理员
            history.setOperatorName("系统管理员");
            history.setNote("更新收货人信息");
            history.setCreateTime(LocalDateTime.now());
            orderStatusHistoryMapper.insert(history);
        }

        return addressCount;
    }

    @Override
    public int updateOrderAmount(MoneyInfoParam moneyInfoParam) {

        if (moneyInfoParam == null) {
            return 0;
        }
        // 1. 解析参数
        Long orderId = moneyInfoParam.getOrderId();
        Double freightAmount = moneyInfoParam.getFreightAmount();
        Double discountAmount = moneyInfoParam.getDiscountAmount();
        int status = moneyInfoParam.getStatus();

        // 2. 更新订单表中的金额信息
        LambdaUpdateWrapper<OmsOrders> orderUpdateWrapper = new LambdaUpdateWrapper<>();
        orderUpdateWrapper.eq(OmsOrders::getId, orderId)
                .set(OmsOrders::getFreightAmount, freightAmount)
                .set(OmsOrders::getDiscountAmount, discountAmount)
                .set(OmsOrders::getStatus, status);

        int orderCount = ordersMapper.update(null, orderUpdateWrapper);

        return orderCount;
    }

    @Override
    public int updateOrderNote(OrderNoteParam orderNoteParam) {
        // 1. 解析参数
        Long orderId = orderNoteParam.getOrderId();
        String note = orderNoteParam.getNote();
        int status = orderNoteParam.getStatus();

        // 2. 更新订单表中的备注信息
        LambdaUpdateWrapper<OmsOrders> orderUpdateWrapper = new LambdaUpdateWrapper<>();
        orderUpdateWrapper.eq(OmsOrders::getId, orderId)
                .set(OmsOrders::getOrderNote, note)
                .set(OmsOrders::getStatus, status);

        int orderCount = ordersMapper.update(null, orderUpdateWrapper);

        return orderCount;

    }


}