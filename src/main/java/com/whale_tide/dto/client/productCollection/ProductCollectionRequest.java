package com.whale_tide.dto.client.productCollection;

import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
public class ProductCollectionRequest {
    private Long productId;
    private String productName;
    private String productPic;
    private BigDecimal productPrice;
    private String productSubTitle;
    
    // 增加价格的setter方法，支持字符串转BigDecimal
    public void setProductPrice(String productPrice) {
        try {
            if (productPrice != null && !productPrice.isEmpty()) {
                this.productPrice = new BigDecimal(productPrice);
            }
        } catch (NumberFormatException e) {
            this.productPrice = BigDecimal.ZERO;
        }
    }
}
