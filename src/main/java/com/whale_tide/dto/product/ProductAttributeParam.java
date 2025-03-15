package com.whale_tide.dto.product;

import lombok.Data;
import java.io.Serializable;

/**
 * 商品属性参数，用于创建和更新属性
 */
@Data
public class ProductAttributeParam implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 所属分类ID
     */
    private Long productAttributeCategoryId;
    
    /**
     * 属性名称
     */
    private String name;
    
    /**
     * 属性类型：0-规格，1-参数
     */
    private Integer type;
    
    /**
     * 录入方式：0-手工输入，1-从列表选择，2-多行文本
     */
    private Integer inputType;
    
    /**
     * 可选值列表，以逗号分隔
     */
    private String inputList;
    
    /**
     * 检索类型：0-不需要，1-需要
     */
    private Integer filterType;
    
    /**
     * 搜索类型：0-不需要，1-关键字，2-范围
     */
    private Integer searchType;
    
    /**
     * 是否关联：0-不关联，1-关联
     */
    private Integer relatedStatus;
    
    /**
     * 是否支持手动添加：0-不支持，1-支持
     */
    private Integer handAddStatus;
    
    /**
     * 排序
     */
    private Integer sort;
} 