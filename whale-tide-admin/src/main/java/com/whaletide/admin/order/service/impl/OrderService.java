package com.whaletide.admin.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whaletide.admin.order.service.IOrderService;
import com.whaletide.admin.order.vo.*;
import com.whaletide.common.entity.oms.OmsOrderItems;
import com.whaletide.common.entity.oms.OmsOrders;
import com.whaletide.common.entity.orderEntity.ProductDailySalesStats;
import com.whaletide.common.entity.orderEntity.ProductSales;
import com.whaletide.common.mapper.oms.OmsOrderItemsMapper;
import com.whaletide.common.mapper.oms.OmsOrdersMapper;
import com.whaletide.common.mapper.pms.PmsProductsMapper;
import com.whaletide.common.mapper.ums.UmsUsersMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService implements IOrderService {
    @Autowired
    private OmsOrdersMapper ordersMapper;

    @Autowired
    private OmsOrderItemsMapper orderItemsMapper;

    @Autowired
    private UmsUsersMapper userMapper;

    @Autowired
    private PmsProductsMapper productMapper;

    @Override
    public OrderListVO getOrderList(Integer pageNum, Integer pageSize, Long userId, Integer status) {
        OrderListVO orderListVO = new OrderListVO();

        Page<OmsOrders> page = new Page<>(pageNum, pageSize);
        page = ordersMapper.selectPage(page, null);
        List<OmsOrders> orders = page.getRecords();

        List<OrderListItemVO> list = new ArrayList<>();
        for (OmsOrders order : orders) {
            OrderListItemVO item = new OrderListItemVO();
            item.setId(order.getId());
            item.setOrderSn(order.getOrderSn());
            item.setUserName(order.getUserName());

            String userAvatar = userMapper.selectById(order.getUserId()).getAvatar();
            item.setUserAvatar(userAvatar);
            item.setStatus(order.getStatus());
            item.setTotalAmount(order.getTotalAmount());
            item.setPayType(order.getPayType());
            item.setCreateTime(order.getCreateTime());

            LambdaQueryWrapper<OmsOrderItems> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(OmsOrderItems::getOrderId, order.getId());
            List<OmsOrderItems> orderItems = orderItemsMapper.selectList(wrapper);
            List<OrderItemVO> orderItemVoList = new ArrayList<>();
            int totalQuantity = 0;
            for (OmsOrderItems orderItem : orderItems) {
                OrderItemVO orderItemVO = new OrderItemVO();
                orderItemVO.setId(orderItem.getId());
                orderItemVO.setProductId(orderItem.getProductId());
                orderItemVO.setProductName(orderItem.getProductName());
                orderItemVO.setProductImage(orderItem.getProductPic());
                orderItemVO.setPrice(BigDecimal.valueOf(orderItem.getProductPrice()));
                orderItemVO.setQuantity(orderItem.getProductQuantity());
                totalQuantity += orderItem.getProductQuantity();
                orderItemVoList.add(orderItemVO);
            }

            item.setOrderItemVoList(orderItemVoList);
            item.setItemCount(totalQuantity);
            list.add(item);
        }

        // 设置list和total到返回对象
        orderListVO.setList(list);
        orderListVO.setTotal((int)page.getTotal());

        return orderListVO;
    }

    @Override
    public OrderStatsticsVO statistics(LocalDate startTime, LocalDate endTime, String type) {
        OrderStatsticsVO orderStatsticsVO = new OrderStatsticsVO();

        //查询基础统计数据
        Map<String, Object> basicStatistics = ordersMapper.getBasicStatistics(startTime, endTime);
        orderStatsticsVO.setTotalOrders((Long) basicStatistics.get("totalOrders"));
        orderStatsticsVO.setTotalAmount((BigDecimal) basicStatistics.get("totalAmount"));
        orderStatsticsVO.setTotalItems((Long) basicStatistics.get("totalItems"));

        //计算平均订单值
        if (orderStatsticsVO.getTotalOrders() > 0) {
            orderStatsticsVO.setAverageOrderValue(orderStatsticsVO.getTotalAmount()
                    .divide(new BigDecimal(orderStatsticsVO.getTotalOrders()), 2, BigDecimal.ROUND_HALF_UP));
        }

        //查询订单状态分布
        orderStatsticsVO.setStatusDistribution(ordersMapper.getStatusDistribution(startTime, endTime));

        // 查询销售趋势
        SalesTrendVO salesTrend = new SalesTrendVO();
        List<ProductDailySalesStatsVO> trendData = new ArrayList<>();

        List<ProductDailySalesStats> trendData_;
        switch (type) {
            case "daily":
                trendData_ = ordersMapper.getDailySalesTrend(startTime, endTime);
                trendData_ = fillMissingDates(trendData_, startTime, endTime);
                break;
            case "weekly":
                trendData_ = ordersMapper.getWeeklySalesTrend(startTime, endTime);
                break;
            case "monthly":
                trendData_ = ordersMapper.getMonthlySalesTrend(startTime, endTime);
                break;
            default:
                trendData_ = new ArrayList<>();
                break;
        }
        for (ProductDailySalesStats stats : trendData_) {
            trendData.add(PDSStoVO(stats));
        }


        // 转换趋势数据为前端需要的格式
        List<String> labels = new ArrayList<>();
        List<BigDecimal> sales = new ArrayList<>();
        List<Integer> orders = new ArrayList<>();

        for (ProductDailySalesStatsVO item : trendData) {
            labels.add(item.getDate());
            sales.add(item.getAmount());
            orders.add(item.getCount());
        }

        salesTrend.setLabels(labels);
        salesTrend.setSales(sales);
        salesTrend.setOrders(orders);
        orderStatsticsVO.setSalesTrend(salesTrend);

        List<ProductSales> productSales = orderItemsMapper.getTopSellingProducts(startTime, endTime, 10);
        List<ProductSalesVO> productSalesVOs = new ArrayList<>();
        for (ProductSales ps : productSales) {
            productSalesVOs.add(PSToVO(ps));
        }
        // 查询热销商品Top 10
        orderStatsticsVO.setTopProducts(productSalesVOs);

        return orderStatsticsVO;
    }

    /**
     * 填充缺失的日期数据
     */
    private List<ProductDailySalesStats> fillMissingDates(
            List<ProductDailySalesStats> data,
            LocalDate startDate,
            LocalDate endDate) {

        // 将原始数据转为Map
        Map<String, ProductDailySalesStats> dataMap = data.stream()
                .collect(Collectors.toMap(ProductDailySalesStats::getDate, item -> item));

        List<ProductDailySalesStats> result = new ArrayList<>();
        LocalDate current = startDate;

        // 遍历每一天
        while (!current.isAfter(endDate)) {
            String dateStr = current.toString();
            if (dataMap.containsKey(dateStr)) {
                result.add(dataMap.get(dateStr));
            } else {
                // 添加空数据
                ProductDailySalesStats emptyData = new ProductDailySalesStats();
                emptyData.setDate(dateStr);
                emptyData.setAmount(BigDecimal.ZERO);
                emptyData.setCount(0);
                result.add(emptyData);
            }
            current = current.plusDays(1);
        }

        return result;
    }

    //转换ProductDailySalesStats->ProductDailySalesStatsVO
    private ProductDailySalesStatsVO PDSStoVO(ProductDailySalesStats stats) {
        ProductDailySalesStatsVO vo = new ProductDailySalesStatsVO();
        vo.setDate(stats.getDate());
        vo.setAmount(stats.getAmount());
        vo.setCount(stats.getCount());
        return vo;
    }

    //转换ProductSales->ProductSalesVO
    private ProductSalesVO PSToVO(ProductSales ps) {
        ProductSalesVO vo = new ProductSalesVO();
        vo.setId(ps.getId());
        vo.setName(ps.getName());
        vo.setImage(ps.getImage());
        vo.setPrice(ps.getPrice());
        vo.setSalesCount(ps.getSalesCount());
        return vo;
    }

    @Override
    public OrderDetailVO getOrderDetail(Long orderId) {
        // 查询订单基本信息
        OmsOrders order = ordersMapper.selectById(orderId);
        if (order == null) {
            log.error("订单不存在，ID: {}", orderId);
            return null;
        }
        
        // 封装订单详情VO
        OrderDetailVO orderDetailVO = new OrderDetailVO();
        
        // 设置基本信息
        orderDetailVO.setId(order.getId());
        orderDetailVO.setOrderSn(order.getOrderSn());
        orderDetailVO.setStatus(order.getStatus());
        orderDetailVO.setTotalAmount(order.getTotalAmount());
        orderDetailVO.setPayAmount(order.getPayAmount());
        orderDetailVO.setFreightAmount(order.getFreightAmount());
        orderDetailVO.setDiscountAmount(order.getDiscountAmount());
        orderDetailVO.setPayType(order.getPayType());
        orderDetailVO.setCreateTime(order.getCreateTime());
        orderDetailVO.setPaymentTime(order.getPaymentTime());
        orderDetailVO.setDeliveryTime(order.getDeliveryTime());
        orderDetailVO.setReceiveTime(order.getReceiveTime());
        
        // 设置收货信息
        orderDetailVO.setReceiverName(order.getReceiverName());
        orderDetailVO.setReceiverPhone(order.getReceiverPhone());
        orderDetailVO.setReceiverProvince(order.getReceiverProvince());
        orderDetailVO.setReceiverCity(order.getReceiverCity());
        orderDetailVO.setReceiverRegion(order.getReceiverRegion());
        orderDetailVO.setReceiverDetailAddress(order.getReceiverDetailAddress());
        orderDetailVO.setNote(order.getNote());
        
        // 设置物流信息
        orderDetailVO.setDeliveryCompany(order.getDeliveryCompany());
        orderDetailVO.setDeliverySn(order.getDeliverySn());
        
        // 设置用户信息
        orderDetailVO.setUserId(order.getUserId());
        orderDetailVO.setUserName(order.getUserName());
        
        // 获取用户头像
        try {
            String userAvatar = userMapper.selectById(order.getUserId()).getAvatar();
            orderDetailVO.setUserAvatar(userAvatar);
        } catch (Exception e) {
            log.error("获取用户头像失败，用户ID: {}", order.getUserId(), e);
        }
        
        // 设置状态和支付方式的描述
        orderDetailVO.setStatusDesc(getOrderStatusName(order.getStatus()));
        orderDetailVO.setPayTypeDesc(getPayTypeName(order.getPayType()));
        
        // 查询订单商品信息
        LambdaQueryWrapper<OmsOrderItems> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OmsOrderItems::getOrderId, orderId);
        List<OmsOrderItems> orderItems = orderItemsMapper.selectList(wrapper);
        
        // 转换为VO对象
        List<OrderItemVO> orderItemVOs = new ArrayList<>();
        for (OmsOrderItems item : orderItems) {
            OrderItemVO itemVO = new OrderItemVO();
            itemVO.setId(item.getId());
            itemVO.setProductId(item.getProductId());
            itemVO.setProductName(item.getProductName());
            itemVO.setProductImage(item.getProductPic());
            itemVO.setPrice(BigDecimal.valueOf(item.getProductPrice()));
            itemVO.setQuantity(item.getProductQuantity());
            orderItemVOs.add(itemVO);
        }
        
        orderDetailVO.setOrderItems(orderItemVOs);
        
        return orderDetailVO;
    }

    /**
     * 获取订单状态名称
     */
    private String getOrderStatusName(Integer status) {
        Map<Integer, String> statusMap = new HashMap<>();
        statusMap.put(0, "待支付");
        statusMap.put(1, "已支付");
        statusMap.put(2, "已发货");
        statusMap.put(3, "已完成");
        statusMap.put(4, "已取消");
        statusMap.put(5, "已退款");
        
        return statusMap.getOrDefault(status, "未知状态");
    }
    
    /**
     * 获取支付方式名称
     */
    private String getPayTypeName(Integer payType) {
        Map<Integer, String> payTypeMap = new HashMap<>();
        payTypeMap.put(0, "支付宝");
        payTypeMap.put(1, "微信支付");
        payTypeMap.put(2, "信用卡");
        payTypeMap.put(3, "现金");
        
        return payTypeMap.getOrDefault(payType, "未知支付方式");
    }
}
