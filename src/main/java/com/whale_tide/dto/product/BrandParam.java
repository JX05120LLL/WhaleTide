package com.whale_tide.dto.product;

import lombok.Data;
import java.io.Serializable;

/**
 * 品牌参数，用于创建和更新品牌
 */
@Data
public class BrandParam implements Serializable {
    private static final long serialVersionUID = 1L;
    
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