package com.whale_tide.dto.product;

import lombok.Data;
import java.io.Serializable;

/**
 * 商品分类参数，用于创建和更新分类
 */
@Data
public class ProductCategoryParam implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 上级分类ID
     */
    private Long parentId;
    
    /**
     * 分类名称
     */
    private String name;
    
    /**
     * 分类级别：0-一级，1-二级
     */
    private Integer level;
    
    /**
     * 商品数量
     */
    private Integer productCount;
    
    /**
     * 商品单位
     */
    private String productUnit;
    
    /**
     * 是否显示在导航栏：0-不显示，1-显示
     */
    private Integer navStatus;
    
    /**
     * 显示状态：0-不显示，1-显示
     */
    private Integer showStatus;
    
    /**
     * 排序
     */
    private Integer sort;
    
    /**
     * 图标
     */
    private String icon;
    
    /**
     * 关键字
     */
    private String keywords;
    
    /**
     * 描述
     */
    private String description;
} 