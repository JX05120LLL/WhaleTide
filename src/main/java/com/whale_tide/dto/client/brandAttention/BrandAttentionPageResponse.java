package com.whale_tide.dto.client.brandAttention;

import lombok.Data;

import java.util.List;

@Data
public class BrandAttentionPageResponse {
    List<BrandAttentionResponse> list;
    Long pageNum;
    Long pageSize;
    Long total;
    Long totalPage;
}
