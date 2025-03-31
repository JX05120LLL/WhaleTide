package com.whale_tide.dto.client.brandAttention;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BrandAttentionResponse {
    private Long id;
    private Long brandId;
    private String brandName;
    private String brandLogo;
    private String brandCity;
    private String brandDesc;
    private LocalDateTime createdTime;
}
