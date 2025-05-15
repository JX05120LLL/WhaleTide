package com.whaletide.client.product.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Schema(description = "商品浏览历史VO")
public class ProductHistoryVO {

    @Schema(description = "浏览记录ID")
    private String id;

    @Schema(description = "商品ID")
    private Long productId;

    @Schema(description = "商品名称")
    private String productName;

    @Schema(description = "商品图片")
    private String productPic;

    @Schema(description = "商品价格")
    private BigDecimal productPrice;

    @Schema(description = "浏览时间")
    private Date viewTime;
} 