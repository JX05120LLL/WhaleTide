package com.whaletide.client.product.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 商品评论VO
 */
@Data
@Schema(description = "商品评论")
public class ProductCommentVO {
    
    @Schema(description = "评论ID")
    private Long id;
    
    @Schema(description = "商品ID")
    private Long productId;
    
    @Schema(description = "用户名")
    private String userName;
    
    @Schema(description = "用户头像")
    private String userIcon;
    
    @Schema(description = "评论内容")
    private String content;
    
    @Schema(description = "评分：1-5")
    private Integer star;
    
    @Schema(description = "评论图片")
    private List<String> pics;
    
    @Schema(description = "创建时间")
    private Date createTime;
    
    @Schema(description = "回复内容")
    private String replyContent;
    
    @Schema(description = "回复时间")
    private Date replyTime;
    
    @Schema(description = "评论列表")
    private List<ProductCommentVO> list = new ArrayList<>();
    
    @Schema(description = "评论总数")
    private Long total = 0L;
} 