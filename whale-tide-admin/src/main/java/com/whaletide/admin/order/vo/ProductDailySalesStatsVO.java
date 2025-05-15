package com.whaletide.admin.order.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDailySalesStatsVO {
    private String date;
    private BigDecimal amount;
    private Integer count;
}
