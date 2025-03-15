package com.whale_tide.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 优选专区表
 */
@Data
@TableName("cms_prefrence_area")
public class CmsPrefrenceArea {
    
    /**
     * 专区ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 专区名称
     */
    private String name;
    
    /**
     * 专区子标题
     */
    private String subTitle;
    
    /**
     * 排序
     */
    private Integer sort;
    
    /**
     * 显示状态：0->不显示；1->显示
     */
    private Integer showStatus;
    
    /**
     * 专区主图
     */
    private String pic;
} 