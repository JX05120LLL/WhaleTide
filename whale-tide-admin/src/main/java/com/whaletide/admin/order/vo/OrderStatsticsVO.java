package com.whaletide.admin.order.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class OrderStatsticsVO {
    private Long totalOrders;
    private BigDecimal totalAmount;
    private Long totalItems;
    private BigDecimal averageOrderValue;
    private Map<String, Integer> statusDistribution;
    private SalesTrendVO salesTrend;
    private List<ProductSalesVO> topProducts;
}
