package com.whale_tide.dto.product;

import lombok.Data;
import java.io.Serializable;

/**
 * 商品属性分类参数，用于创建和更新属性分类
 */
@Data
public class ProductAttributeCategoryParam implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 分类名称
     */
    private String name;
} 