package com.macro.mall.tiny.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@TableName("order")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderSn;//订单号
    private Long userId;
    private Double totalAmount;//订单总金额
    private Byte status;//订单状态：0-待付款，1-待发货，2-已发货，3-已完成，4-已关闭
    private Byte payType;//支付方式：0-未支付，1-支付宝，2-微信
    private Integer receiverAddress;//关联收获地址主键
    private Date createTime;
    private Date paymentTime;
    private Long merchantId;//商家ID（关联user.id）

}
