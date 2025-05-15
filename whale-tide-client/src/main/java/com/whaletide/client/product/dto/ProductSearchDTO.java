package com.whaletide.client.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 商品搜索DTO
 */
@Data
@Schema(description = "商品搜索请求")
public class ProductSearchDTO {
    
    @Schema(description = "搜索关键词")
    private String keyword;
    
    @Schema(description = "分类ID")
    private Long categoryId;
    
    @Schema(description = "品牌ID")
    private Long brandId;
    
    @Schema(description = "价格区间-最小值")
    private Double minPrice;
    
    @Schema(description = "价格区间-最大值")
    private Double maxPrice;
    
    @Schema(description = "排序字段：0->默认；1->按新品；2->按销量；3->价格从低到高；4->价格从高到低")
    private Integer sort;
    
    @Schema(description = "页码", defaultValue = "1")
    private Integer pageNum = 1;
    
    @Schema(description = "每页记录数", defaultValue = "20")
    private Integer pageSize = 20;
} 