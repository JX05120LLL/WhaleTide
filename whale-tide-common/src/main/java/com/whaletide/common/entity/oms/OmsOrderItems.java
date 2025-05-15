package com.whaletide.common.entity.oms;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 订单项
 */
@Data
@TableName("oms_order_items")
public class OmsOrderItems implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单编号
     */
    private String orderSn;

    /**
     * 商品ID
     */
    private Long productId;


    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品图片
     */
    private String productPic;

    /**
     * 商品价格
     */
    private Double productPrice;

    /**
     * 购买数量
     */
    private Integer productQuantity;

    /**
     * 商品分类ID
     */
    private Long productCategoryId;
} 