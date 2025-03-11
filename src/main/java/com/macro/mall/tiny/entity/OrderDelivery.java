package com.macro.mall.tiny.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@TableName("order_delivery")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDelivery {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long orderId;//订单ID
    private String deliverySn;//物流单号
    private String deliveryCompany;//物流公司
    private Integer addressId;//收货地址id
    private Byte deliveryStatus;//物流状态：0-未发货，1-运输中，2-已签收
    private Date deliveryTime;//发货时间
}
