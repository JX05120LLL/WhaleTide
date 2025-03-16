package com.whale_tide.entity.sms;

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
 * 促销活动表
 * </p>
 *
 * @author Bro_cat
 * @since 2025-03-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sms_promotions")
public class SmsPromotions implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 促销活动ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 活动名称
     */
    private String name;

    /**
     * 活动类型：0-满减，1-满折，2-特价活动，3-团购活动，4-赠品活动
     */
    private Integer type;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 活动状态：0-未开始，1-进行中，2-已结束，3-已取消
     */
    private Integer status;

    /**
     * 活动描述
     */
    private String description;

    /**
     * 活动横幅图
     */
    private String bannerImage;

    /**
     * 活动规则，JSON格式
     */
    private String rules;

    /**
     * 目标类型：0-全场，1-指定商品，2-指定分类
     */
    private Integer targetType;

    /**
     * 使用平台：0-全平台，1-移动端，2-PC端
     */
    private Integer platform;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 创建者
     */
    private String createAdmin;

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
