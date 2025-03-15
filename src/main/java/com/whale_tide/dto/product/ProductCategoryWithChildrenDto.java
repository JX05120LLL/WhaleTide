package com.whale_tide.dto.product;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 带子分类的商品分类DTO，用于返回分类及其下子分类
 */
@Data
public class ProductCategoryWithChildrenDto extends ProductCategoryDto {
    private static final long serialVersionUID = 1L;
    
    /**
     * 子分类列表
     */
    private List<ProductCategoryDto> children;
} 