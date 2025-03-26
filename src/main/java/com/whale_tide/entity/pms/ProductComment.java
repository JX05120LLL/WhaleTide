package com.whale_tide.entity.pms;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品评价实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pms_product_comments")
public class ProductComment {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long productId;
    
    private Long skuId;
    
    private Long userId;
    
    private Long orderId;
    
    private Long orderItemId;
    
    private String content;
    
    private String images;
    
    private String videos;
    
    private BigDecimal rating;
    
    private Integer isAnonymous;
    
    private Integer isShow;
    
    private Integer isReply;
    
    private String replyContent;
    
    private LocalDateTime replyTime;
    
    private String replyUser;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
} 