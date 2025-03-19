package com.whale_tide.dto.management.order;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderReturnResult {
    Long id;
    Long orderId;
    LocalDateTime createTime;
    String memberUsername;
    BigDecimal returnAmount;
    Integer status;
    LocalDateTime handleTime;
}
