package com.macro.mall.tiny.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@TableName("payment_record")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long orderId;
    private String paymentSn;//支付流水号
    private Double amount;//支付金额
    private Integer payType;//支付方式：1-支付宝，2-微信
    private Integer status;//状态：0-未支付，1-支付成功，2-支付失败
    private Date createTime;
    private Date payTime;

}
