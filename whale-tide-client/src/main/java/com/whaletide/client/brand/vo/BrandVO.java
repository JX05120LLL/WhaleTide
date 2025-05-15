package com.whaletide.client.brand.vo;

import com.whaletide.client.product.vo.ProductVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 品牌视图对象
 */
@Data
@Schema(description = "品牌信息")
public class BrandVO {
    
    @Schema(description = "品牌ID")
    private Long id;
    
    @Schema(description = "品牌名称")
    private String name;
    
    @Schema(description = "品牌Logo")
    private String logo;
    
    @Schema(description = "品牌描述")
    private String description;
    
    @Schema(description = "是否推荐：0-否，1-是")
    private Integer isFeatured;
    
    @Schema(description = "品牌相关产品")
    private List<ProductVO> products;
} 