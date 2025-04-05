package com.whale_tide.dto.client.productCollection;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductCollectionResponse {
    private Long id;
    private Long productId;
    private String productName;
    private String productPic;
    private BigDecimal productPrice;
    private String productSubTitle;
    private LocalDateTime createTime;
}
