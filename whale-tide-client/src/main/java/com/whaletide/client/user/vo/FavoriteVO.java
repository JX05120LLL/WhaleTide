package com.whaletide.client.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "收藏VO")
public class FavoriteVO {

    @Schema(description = "收藏ID")
    private Long id;

    @Schema(description = "商品ID")
    private Long productId;

    @Schema(description = "商品名称")
    private String productName;

    @Schema(description = "商品图片")
    private String productPic;

    @Schema(description = "商品价格")
    private BigDecimal productPrice;

    @Schema(description = "收藏时间")
    private LocalDateTime createTime;
} 