package com.whaletide.common.entity.orderEntity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDailySalesStats {
    private String date;
    private BigDecimal amount;
    private Integer count;
}
