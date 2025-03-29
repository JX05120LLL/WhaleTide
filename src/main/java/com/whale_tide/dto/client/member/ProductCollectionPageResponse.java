package com.whale_tide.dto.client.member;

import lombok.Data;

import java.util.List;

@Data
public class ProductCollectionPageResponse {
    List<ProductCollectionResponse> list;
    Long page;
    Long pageSize;
    Long total;
    Long totalPages;
}
