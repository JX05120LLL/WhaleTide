package com.whale_tide.entity;
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
 * 订单操作日志表
 * </p>
 *
 * @author Bro_cat
 * @since 2025-03-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("oms_order_logs")
public class OmsOrderLogs implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志ID
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
     * 操作类型
     */
    private String action;

    /**
     * 操作备注
     */
    private String note;

    /**
     * 操作IP
     */
    private String ip;

    /**
     * 操作时间
     */
    private LocalDateTime createTime;


}
