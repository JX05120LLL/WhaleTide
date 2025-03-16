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
 * 订单状态变更历史表
 * </p>
 *
 * @author Bro_cat
 * @since 2025-03-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("oms_order_status_history")
public class OmsOrderStatusHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
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
     * 前置状态
     */
    private Integer previousStatus;

    /**
     * 当前状态
     */
    private Integer currentStatus;

    /**
     * 操作人ID
     */
    private Long operatorId;

    /**
     * 操作人类型：0-系统，1-用户，2-商家，3-管理员
     */
    private Integer operatorType;

    /**
     * 操作人名称
     */
    private String operatorName;

    /**
     * 状态变更备注
     */
    private String note;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
