package com.whaletide.client.product.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 品牌VO
 */
@Data
@Schema(description = "品牌信息")
public class BrandVO {
    
    @Schema(description = "品牌ID")
    private Long id;
    
    @Schema(description = "品牌名称")
    private String name;
    
    @Schema(description = "品牌logo")
    private String logo;
    
    @Schema(description = "品牌描述")
    private String description;
    
    @Schema(description = "品牌首字母")
    private String firstLetter;
    
    @Schema(description = "排序")
    private Integer sort;
    
    @Schema(description = "是否为品牌制造商")
    private Integer factoryStatus;
    
    @Schema(description = "是否显示")
    private Integer showStatus;
    
    @Schema(description = "产品数量")
    private Integer productCount;
    
    @Schema(description = "产品评论数量")
    private Integer productCommentCount;
} 