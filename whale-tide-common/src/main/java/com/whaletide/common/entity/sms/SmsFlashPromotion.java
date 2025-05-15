package com.whaletide.common.entity.sms;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 限时秒杀活动表
 * </p>
 *
 * @author BroRem
 * @since 2025-05-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sms_flash_promotion")
public class SmsFlashPromotion implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 秒杀ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 活动标题
     */
    private String title;

    /**
     * 开始日期
     */
    private LocalDate startDate;

    /**
     * 结束日期
     */
    private LocalDate endDate;

    /**
     * 上下线状态：0-下线，1-上线
     */
    private Integer status;

    /**
     * 创建者
     */
    private String createAdmin;

    /**
     * 活动描述
     */
    private String description;

    /**
     * 活动图标
     */
    private String iconUrl;

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
