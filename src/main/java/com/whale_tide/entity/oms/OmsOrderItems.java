package com.whale_tide.entity.oms;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单商品项表
 * </p>
 *
 * @author Bro_cat
 * @since 2025-03-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("oms_order_items")
public class OmsOrderItems implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单项ID
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
    private String productImage;

    /**
     * 品牌名称
     */
    private String productBrand;

    /**
     * 商品编号
     */
    private String productSn;

    /**
     * 商品分类ID
     */
    private Long productCategoryId;

    /**
     * SKU ID
     */
    private Long skuId;

    /**
     * SKU编码
     */
    private String skuCode;

    /**
     * 规格属性(文本表示)
     */
    private String skuSpecs;

    /**
     * 购买数量
     */
    private Integer quantity;

    /**
     * 商品单价
     */
    private BigDecimal price;

    /**
     * 实付金额
     */
    private BigDecimal realAmount;

    /**
     * 原始金额(无优惠)
     */
    private BigDecimal originalAmount;

    /**
     * 优惠券优惠分摊金额
     */
    private BigDecimal couponAmount;

    /**
     * 促销优惠分摊金额
     */
    private BigDecimal promotionAmount;

    /**
     * 促销活动信息
     */
    private String promotionName;

    /**
     * 赠送积分
     */
    private Integer giftIntegration;

    /**
     * 赠送成长值
     */
    private Integer giftGrowth;

    /**
     * 退款状态：0-未退款，1-已申请，2-已退款
     */
    private Integer refundStatus;

    /**
     * 评价状态：0-未评价，1-已评价
     */
    private Integer commentStatus;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
