package com.macro.mall.tiny.domain.product;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品
 */
public class Product {
    @TableId(type = IdType.AUTO)
    private long id;
    private String name;
    private long categoryId;
    private long brandId;
    private long merchantId;
    private BigDecimal price;
    private int stock;
    private int sales;
    private String description;
    private byte status;
    private Date createTime;
}
