package com.macro.mall.tiny.domain.product;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;

/**
 * 商品评论
 */
public class ProductComment {
    @TableId(type = IdType.AUTO)
    private long id;
    private long userId;
    private long productId;
    private String content;
    private byte rating;
    private Date createTime;
}
