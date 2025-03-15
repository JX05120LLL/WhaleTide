package com.whale_tide.dto.product;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 带属性的商品属性分类DTO，用于返回分类及其下属性
 */
@Data
public class ProductAttributeCategoryWithAttrDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    
    /**
     * 分类名称
     */
    private String name;
    
    /**
     * 该分类下的商品属性列表
     */
    private List<ProductAttributeDto> productAttributeList;
} 