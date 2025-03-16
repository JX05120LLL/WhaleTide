package com.whale_tide.entity.pms;

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
 * 商品评论表
 * </p>
 *
 * @author Bro_cat
 * @since 2025-03-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pms_product_comments")
public class PmsProductComments implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评论ID
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
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单商品ID
     */
    private Long orderItemId;

    /**
     * 评论标题
     */
    private String title;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评分：1-5分
     */
    private Integer rating;

    /**
     * 评论图片，多个以逗号分隔
     */
    private String images;

    /**
     * 是否匿名：0-否，1-是
     */
    private Integer isAnonymous;

    /**
     * 是否显示：0-不显示，1-显示
     */
    private Integer isShow;

    /**
     * 评论时间
     */
    private LocalDateTime createTime;


}
