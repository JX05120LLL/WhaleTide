package com.whale_tide.entity.pms;

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
 * 商品信息表
 * </p>
 *
 * @author Bro_cat
 * @since 2025-03-12
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
     * 商品编号
     */
    private String productSn;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 品牌ID
     */
    private Long brandId;

    /**
     * 商家ID
     */
    private Long merchantId;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 原价
     */
    private BigDecimal originalPrice;

    /**
     * 主图URL
     */
    private String mainImage;

    /**
     * 关键词
     */
    private String keywords;

    /**
     * 简短描述
     */
    private String brief;

    /**
     * 销量
     */
    private Integer sale;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 单位(件/kg等)
     */
    private String unit;

    /**
     * 重量(kg)
     */
    private BigDecimal weight;

    /**
     * 排序
     */
    private Integer sort;

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
     * 审核状态：0-未审核，1-审核通过，2-审核不通过
     */
    private Integer verifyStatus;

    /**
     * 是否删除：0-否，1-是
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
