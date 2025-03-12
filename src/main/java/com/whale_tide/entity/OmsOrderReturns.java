package com.whale_tide.entity;
import java.math.BigDecimal;
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
 * 订单退货申请表
 * </p>
 *
 * @author Bro_cat
 * @since 2025-03-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("oms_order_returns")
public class OmsOrderReturns implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 退货申请ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单编号
     */
    private String orderSn;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 退货类型：0-仅退款，1-退货退款
     */
    private Integer returnType;

    /**
     * 退货原因
     */
    private String returnReason;

    /**
     * 退款金额
     */
    private BigDecimal returnAmount;

    /**
     * 退货凭证图片，多个以逗号分隔
     */
    private String returnImages;

    /**
     * 退货说明
     */
    private String returnDescription;

    /**
     * 联系人
     */
    private String contactPerson;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 申请状态：0-待处理，1-处理中，2-已同意，3-已拒绝，4-已完成
     */
    private Integer status;

    /**
     * 处理人ID
     */
    private Long handlerId;

    /**
     * 处理备注
     */
    private String handlerNote;

    /**
     * 处理时间
     */
    private LocalDateTime handlerTime;

    /**
     * 退货物流公司
     */
    private String deliveryCompany;

    /**
     * 退货物流单号
     */
    private String deliverySn;

    /**
     * 收货时间
     */
    private LocalDateTime receiveTime;

    /**
     * 申请时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
