package com.whale_tide.dto.client.member;

import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
public class ProductCollectionRequest {
    private @NonNull Long productId;
    private String productName;
    private String productPic;
    private BigDecimal productPrice;
    private String productSubTitle;
}
