package com.whaletide.common.entity.sms;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 优惠券表
 * </p>
 *
 * @author BroRem
 * @since 2025-05-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sms_coupons")
public class SmsCoupons implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 优惠券ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 优惠券名称
     */
    private String name;

    /**
     * 优惠券类型：0-全场通用，1-指定商品，2-指定分类
     */
    private Integer type;

    /**
     * 优惠券码
     */
    private String code;

    /**
     * 使用平台：0-全平台，1-移动端，2-PC端
     */
    private Integer platform;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 折扣
     */
    private BigDecimal discount;

    /**
     * 使用门槛，0表示无门槛
     */
    private BigDecimal minPoint;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 发行数量
     */
    private Integer totalCount;

    /**
     * 剩余数量
     */
    private Integer remainingCount;

    /**
     * 已领取数量
     */
    private Integer receiveCount;

    /**
     * 已使用数量
     */
    private Integer usedCount;

    /**
     * 每人限领张数
     */
    private Integer perLimit;

    /**
     * 优惠券状态，0->未使用；1->已使用；2->已过期
     */
    private Integer useStatus;

    /**
     * 备注
     */
    private String note;

    /**
     * 发布状态：0-未发布，1-已发布
     */
    private Integer publishStatus;

    /**
     * 是否删除：0-否，1-是
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
