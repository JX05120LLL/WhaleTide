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
     * 属性ID
     */
    private Long attributeId;

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
     * 画册图片，连产品图片限制为5张，以逗号分割
     */
    private String albumPics;

    /**
     * 商品详情标题
     */
    private String detailTitle;

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
     * 促销类型：0-无促销，1-特价促销，2-会员价格，3-阶梯价格，4-满减价格
     */
    private Integer promotionType;

    /**
     * 促销价格
     */
    private BigDecimal promotionPrice;

    /**
     * 促销开始时间
     */
    private LocalDateTime promotionStartTime;

    /**
     * 促销结束时间
     */
    private LocalDateTime promotionEndTime;

    /**
     * 促销限购数量
     */
    private Integer promotionLimit;

    /**
     * 服务保障（JSON格式）
     */
    private String serviceGuarantees;

    /**
     * 商品属性（JSON格式）
     */
    private String productAttributes;

    /**
     * 商品图片（JSON格式）
     */
    private String productImages;

    /**
     * 商品视频（JSON格式）
     */
    private String productVideos;

    /**
     * 商品详情
     */
    private String productDetails;

    /**
     * 商品规格（JSON格式）
     */
    private String productSpecs;

    /**
     * 商品专题（JSON格式）
     */
    private String productSubjects;

    /**
     * 商品标签（JSON格式）
     */
    private String productTags;

    /**
     * 相关商品（JSON格式）
     */
    private String productRelated;

    /**
     * 推荐商品（JSON格式）
     */
    private String productRecommend;

    /**
     * 商品评价（JSON格式）
     */
    private String productComment;

    /**
     * 商品评分
     */
    private Integer productRating;

    /**
     * 商品评分数量
     */
    private Integer productRatingCount;

    /**
     * 商品评价数量
     */
    private Integer productCommentCount;

    /**
     * 商品浏览量
     */
    private Integer productViewCount;

    /**
     * 商品收藏量
     */
    private Integer productFavoriteCount;

    /**
     * 商品分享量
     */
    private Integer productShareCount;

    /**
     * 商品销量
     */
    private Integer productSaleCount;

    /**
     * 商品退货量
     */
    private Integer productReturnCount;

    /**
     * 商品退款量
     */
    private Integer productRefundCount;

    /**
     * 商品投诉量
     */
    private Integer productComplaintCount;

    /**
     * 商品审核状态：0-待审核，1-审核通过，2-审核不通过
     */
    private Integer productAuditStatus;

    /**
     * 商品审核时间
     */
    private LocalDateTime productAuditTime;

    /**
     * 商品审核人
     */
    private String productAuditUser;

    /**
     * 商品审核备注
     */
    private String productAuditRemark;

    /**
     * 商品状态：0-下架，1-上架，2-删除
     */
    private Integer productStatus;

    /**
     * 商品状态变更时间
     */
    private LocalDateTime productStatusTime;

    /**
     * 商品状态变更人
     */
    private String productStatusUser;

    /**
     * 商品状态变更备注
     */
    private String productStatusRemark;

    /**
     * 商品版本号
     */
    private Integer productVersion;

    /**
     * 商品备注
     */
    private String productRemark;

    /**
     * 商品扩展信息（JSON格式）
     */
    private String productExtra;

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



