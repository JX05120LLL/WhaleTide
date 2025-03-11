package com.macro.mall.tiny.entity.product;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品信息类
 */
@TableName("product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @TableId(type = IdType.AUTO)
    private long id;
    private String name;//商品名称
    private long categoryId;//所属分类ID
    private long brandId;//所属品牌ID
    private long merchantId;//所属商家ID
    private BigDecimal price;//商品价格
    private int stock;//库存数量  默认0
    private int sales;//商品销量  默认0
    private String description;//商品描述
    private byte status;//商品状态：0-下架，1-上架  默认1
    private Date createTime;//创建时间
}
