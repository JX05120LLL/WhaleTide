package com.whale_tide.dto.management.order;

import lombok.Data;

import java.util.List;

@Data
public class OrderRetuenListResult {
    List<OrderReturnResult> list;
    Long total;
    Long pageSize;
    Long pageNum;
}
