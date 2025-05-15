package com.whaletide.admin.order.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


@Data
public class SalesTrendVO {
    private List<String> labels;
    private List<BigDecimal> sales;
    private List<Integer> orders;
}
