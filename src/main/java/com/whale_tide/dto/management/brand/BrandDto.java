package com.whale_tide.dto.management.brand;

import lombok.Data;
import java.io.Serializable;

/**
 * 品牌DTO，用于返回品牌信息
 */
@Data
public class BrandDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    
    /**
     * 品牌名称
     */
    private String name;
    
    /**
     * 首字母
     */
    private String firstLetter;
    
    /**
     * 排序
     */
    private Integer sort;
    
    /**
     * 是否为品牌制造商：0-不是，1-是
     */
    private Integer factoryStatus;
    
    /**
     * 显示状态：0-不显示，1-显示
     */
    private Integer showStatus;
    
    /**
     * 产品数量
     */
    private Integer productCount;
    
    /**
     * 产品评论数量
     */
    private Integer productCommentCount;
    
    /**
     * 品牌logo
     */
    private String logo;
    
    /**
     * 专区大图
     */
    private String bigPic;
    
    /**
     * 品牌故事
     */
    private String brandStory;
} 