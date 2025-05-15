package com.whaletide.client.home.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 首页轮播图VO
 */
@Data
@Schema(description = "首页轮播图")
public class BannerVO {
    
    @Schema(description = "轮播图ID")
    private Long id;
    
    @Schema(description = "轮播图标题")
    private String title;
    
    @Schema(description = "轮播图副标题")
    private String subtitle;
    
    @Schema(description = "轮播图图片地址")
    private String image;
    
    @Schema(description = "链接地址")
    private String link;
    
    @Schema(description = "排序")
    private Integer sort;
} 