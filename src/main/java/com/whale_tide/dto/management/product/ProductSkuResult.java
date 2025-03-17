package com.whale_tide.dto.management.product;


import lombok.Data;

import java.math.BigDecimal;

/**
 *
 */

@Data
public class ProductSkuResult {
    String skuCode;
    BigDecimal price;
    Integer stock;
    Integer lowStock;
}
