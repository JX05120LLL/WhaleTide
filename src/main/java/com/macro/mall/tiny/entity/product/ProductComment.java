package com.macro.mall.tiny.entity.product;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 商品评论信息类
 */
@TableName("product_comment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductComment {
    @TableId(type = IdType.AUTO)
    private long id;
    private long userId;//用户ID
    private long productId;//商品ID
    private String content;//评论内容
    private byte rating;//商品评分：1-5分，默认5分好评
    private Date createTime;//评论创建时间
}
