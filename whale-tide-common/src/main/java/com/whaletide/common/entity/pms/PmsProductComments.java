package com.whaletide.common.entity.pms;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * 商品评价表
 * </p>
 *
 * @author BroRem
 * @since 2025-05-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pms_product_comments")
public class PmsProductComments implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评价ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 订单ID - 数据库中不存在此字段
     */
    @TableField(exist = false) 
    private Long orderId;

    /**
     * 评价内容
     */
    private String content;

    /**
     * 评价图片（JSON格式）
     */
    private String images;

    /**
     * 评分
     */
    private BigDecimal rating;

    /**
     * 是否显示：0-否，1-是
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
