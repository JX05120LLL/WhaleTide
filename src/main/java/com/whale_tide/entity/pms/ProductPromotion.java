package com.whale_tide.entity.pms;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品促销实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pms_product_promotions")
public class ProductPromotion {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long productId;
    
    private Long skuId;
    
    private Integer promotionType;
    
    private BigDecimal promotionPrice;
    
    private LocalDateTime promotionStartTime;
    
    private LocalDateTime promotionEndTime;
    
    private Integer promotionLimit;
    
    private String promotionRules;
    
    private Integer promotionStatus;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
} 