package com.whaletide.common.entity.pms;

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
 * 商品评分表
 * </p>
 *
 * @author BroRem
 * @since 2025-05-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pms_ratings")
public class PmsRatings implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评分ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 评分：1-5分
     */
    private Integer rating;

    /**
     * 评分类型：0-综合，1-服务，2-质量，3-物流
     */
    private Integer ratingType;

    /**
     * 评分时间
     */
    private LocalDateTime createTime;


}
