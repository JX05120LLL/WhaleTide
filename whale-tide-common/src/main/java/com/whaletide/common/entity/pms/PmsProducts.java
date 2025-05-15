package com.whaletide.common.entity.pms;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 商品信息表
 * </p>
 *
 * @author BroRem
 * @since 2025-05-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pms_products")
public class PmsProducts implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 品牌ID
     */
    private Long brandId;

    /**
     * 商品价格
     */
    private Double price;

    /**
     * 主图URL
     */
    private String mainImage;

    /**
     * 简短描述
     */
    private String brief;

    /**
     * 库存
     */
    private Integer stock;


    /**
     * 上架状态：0-下架，1-上架
     */
    private Integer publishStatus;

    /**
     * 新品状态：0-非新品，1-新品
     */
    private Integer newStatus;

    /**
     * 推荐状态：0-不推荐，1-推荐
     */
    private Integer recommendStatus;


    /**
     * 商品图片(副图)
     */
    private List<String> productImages;

    /**
     * 商品详情
     */
    private String productDetails;


    /**
     * 商品评分
     */
    private BigDecimal productRating;

    /**
     * 商品状态：0-下架，1-上架，2-删除
     */
    private Integer productStatus;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
