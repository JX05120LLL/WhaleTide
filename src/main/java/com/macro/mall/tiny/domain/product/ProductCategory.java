package com.macro.mall.tiny.domain.product;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * 商品分类
 */
public class ProductCategory {
    @TableId(type = IdType.AUTO)
    private long id;
    private long productId;
    private String name;
    private byte level;
    private int sort;
}
