package com.whale_tide.dto.management.product;

import lombok.Data;
import java.io.Serializable;

/**
 * 商品属性分类DTO，用于返回属性分类信息
 */
@Data
public class ProductAttributeCategoryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    
    /**
     * 分类名称
     */
    private String name;
    
    /**
     * 规格数量
     */
    private Integer attributeCount;
    
    /**
     * 参数数量
     */
    private Integer paramCount;
} 