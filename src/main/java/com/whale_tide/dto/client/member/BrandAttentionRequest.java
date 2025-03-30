package com.whale_tide.dto.client.member;

import lombok.Data;

@Data
public class BrandAttentionRequest {
    private Long brandId;
    private String brandName;
    private String brandLogo;
    private String brandCity;
    private String brandDesc;
}
