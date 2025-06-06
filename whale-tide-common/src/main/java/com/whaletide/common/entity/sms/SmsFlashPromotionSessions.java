package com.whaletide.common.entity.sms;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * <p>
 * 限时秒杀场次表
 * </p>
 *
 * @author BroRem
 * @since 2025-05-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sms_flash_promotion_sessions")
public class SmsFlashPromotionSessions implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 场次ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 秒杀活动ID
     */
    private Long flashPromotionId;

    /**
     * 场次名称
     */
    private String name;

    /**
     * 每日开始时间
     */
    private LocalTime startTime;

    /**
     * 每日结束时间
     */
    private LocalTime endTime;

    /**
     * 启用状态：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer orderSort;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
