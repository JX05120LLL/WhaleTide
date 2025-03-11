package com.macro.mall.tiny.entity.product;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 商品SKU信息类
 */
@TableName("product_sku")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSku {
    @TableId(type = IdType.AUTO)
    long id;
    private long productId;//商品ID
    private String skuCode;//SKU编码
    private BigDecimal price;//价格
    private int stock;//库存数量  默认0
    private String specs;//规格属性-JSON格式
}
