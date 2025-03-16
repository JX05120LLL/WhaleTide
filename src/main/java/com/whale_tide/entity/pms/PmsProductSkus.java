package com.whale_tide.entity.pms;

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
 * 商品SKU表
 * </p>
 *
 * @author Bro_cat
 * @since 2025-03-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pms_product_skus")
public class PmsProductSkus implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * SKU ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * SKU编码
     */
    private String skuCode;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 预警库存
     */
    private Integer lowStock;

    /**
     * SKU图片
     */
    private String image;

    /**
     * 规格属性（JSON格式）
     */
    private String specs;

    /**
     * 规格文本，用于展示
     */
    private String specsText;

    /**
     * 体积(m³)
     */
    private BigDecimal volume;

    /**
     * 重量(kg)
     */
    private BigDecimal weight;

    /**
     * 销量
     */
    private Integer sale;

    /**
     * 锁定库存(下单但未付款)
     */
    private Integer lockStock;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
