package com.whaletide.admin.order.vo;

import lombok.Data;

import java.util.List;

@Data
public class OrderListVO {
    Integer total;
    List<OrderListItemVO> list;
}
