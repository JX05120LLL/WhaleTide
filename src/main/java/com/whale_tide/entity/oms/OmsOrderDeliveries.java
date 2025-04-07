package com.whale_tide.entity.oms;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单物流信息表
 * </p>
 *
 * @author Bro_cat
 * @since 2025-03-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("oms_order_deliveries")
public class OmsOrderDeliveries implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 物流ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 订单编号
     */
    private String orderSn;

    /**
     * 物流单号
     */
    private String deliverySn;

    /**
     * 物流公司
     */
    private String deliveryCompany;

    /**
     * 物流公司代码
     */
    private String deliveryCompanyCode;

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
     * 邮政编码
     */
    private String receiverPostalCode;

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
     * 是否拆单：0-否，1-是
     */
    private Integer isSplit;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
