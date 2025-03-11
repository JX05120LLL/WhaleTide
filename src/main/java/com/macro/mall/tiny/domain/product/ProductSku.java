package com.macro.mall.tiny.domain.product;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.math.BigDecimal;

/**
 *  商品SKU
 */
public class ProductSku {
    @TableId(type = IdType.AUTO)
     long id;
    private long productId;
    private String skuCode;
    private BigDecimal price;
    private int stock;
    private String specs;
}
