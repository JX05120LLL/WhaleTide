package com.whaletide.admin.order.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderListItemVO {
    Long id;
    String orderSn;
    String userName;
    String userAvatar;
    Integer status;
    Double totalAmount;
    Integer payType;
    LocalDateTime createTime;
    List<OrderItemVO> orderItemVoList;
    Integer itemCount;
}
