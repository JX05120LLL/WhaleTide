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
 * 限时秒杀商品关联表
 * </p>
 *
 * @author Bro_cat
 * @since 2025-03-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sms_flash_promotion_products")
public class SmsFlashPromotionProducts implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 秒杀活动ID
     */
    private Long flashPromotionId;

    /**
     * 秒杀场次ID
     */
    private Long flashSessionId;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 秒杀价格
     */
    private BigDecimal flashPrice;

    /**
     * 原价
     */
    private BigDecimal originalPrice;

    /**
     * 秒杀库存
     */
    private Integer flashStock;

    /**
     * 当前库存
     */
    private Integer currentStock;

    /**
     * 已售数量
     */
    private Integer soldCount;

    /**
     * 每人限购数量
     */
    private Integer limitCount;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 上架状态：0-下架，1-上架
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
