package com.whale_tide.dto.client.product;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;
/**
 * 添加商品评论请求
 */
@Data
@ApiModel(description = "添加商品评论请求")
public class ProductCommentAddRequest {
    private Long productId;

    private Long orderId;

    private Integer star;

    private String content;

    private List<String> pics;
}
