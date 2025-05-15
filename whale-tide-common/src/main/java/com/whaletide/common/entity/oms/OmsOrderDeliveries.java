package com.whaletide.common.entity.oms;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单物流信息表
 * </p>
 *
 * @author BroRem
 * @since 2025-05-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("oms_order_deliveries")
public class OmsOrderDeliveries implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单编号
     */
    private String orderSn;



    /**
     * 收件人姓名
     */
    private String receiverName;

    /**
     * 收件人电话
     */
    private String receiverPhone;

    /**
     * 省份
     */
    private String receiverProvince;

    /**
     * 城市
     */
    private String receiverCity;

    /**
     * 区县
     */
    private String receiverDistrict;

    /**
     * 详细地址
     */
    private String receiverAddress;

    /**
     * 物流状态：0-未发货，1-已发货，2-运输中，3-已签收，4-派送失败
     */
    private Integer deliveryStatus;

    /**
     * 发货时间
     */
    private LocalDateTime deliveryTime;

    /**
     * 签收时间
     */
    private LocalDateTime receiveTime;

    /**
     * 物流备注
     */
    private String deliveryNote;


    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
