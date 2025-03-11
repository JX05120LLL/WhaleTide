package com.macro.mall.tiny.entity.product;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品分类信息类
 */
@TableName("product_category")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategory {
    @TableId(type = IdType.AUTO)
    private long id;
    private long parentId;//父级分类ID
    private String name;//分类名称
    private byte level;//分类层级  默认1
    private int sort;//排序？  默认0
}
