package com.whaletide.client.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 商品评论查询DTO
 */
@Data
@Schema(description = "商品评论查询")
public class ProductCommentQueryDTO {
    
    @Schema(description = "商品ID", required = true)
    private Long productId;
    
    @Schema(description = "评分过滤：1-5分")
    private Integer star;
    
    @Schema(description = "是否有图片：0->全部；1->有图")
    private Integer hasPicture;
    
    @Schema(description = "页码", defaultValue = "1")
    private Integer pageNum = 1;
    
    @Schema(description = "每页记录数", defaultValue = "10")
    private Integer pageSize = 10;
} 