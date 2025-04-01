package com.whale_tide.dto.client.product;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
@ApiModel(description = "产品评论响应")
public class ProductCommentResponse {
    private Long id;
    private Long productId;
    private String memberNickName;
    private String memberIcon;
    private String content;
    private Integer star; // 评分，1-5星
    private List<String> pics; // 评论图片
    private Date createTime;
}
