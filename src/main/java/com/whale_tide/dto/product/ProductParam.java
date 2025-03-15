package com.whale_tide.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 创建商品请求参数
 */
@Data
@ApiModel(description = "创建商品请求参数")
public class ProductParam {

    @ApiModelProperty(value = "商品基本信息", required = true)
    private ProductBasicParam productParam;

    @ApiModelProperty(value = "商品属性值列表")
    private List<ProductAttributeValueParam> productAttributeValueList;

    @ApiModelProperty(value = "商品满减价格列表")
    private List<ProductFullReductionParam> productFullReductionList;

    @ApiModelProperty(value = "商品阶梯价格列表")
    private List<ProductLadderParam> productLadderList;

    @ApiModelProperty(value = "商品SKU库存列表", required = true)
    private List<SkuStockParam> skuStockList;

    /**
     * 商品基本信息参数
     */
    @Data
    @ApiModel(description = "商品基本信息参数")
    public static class ProductBasicParam {
        
        @ApiModelProperty(value = "商品名称", required = true)
        private String name;
        
        @ApiModelProperty(value = "商品编号")
        private String productSn;
        
        @ApiModelProperty(value = "品牌ID")
        private Long brandId;
        
        @ApiModelProperty(value = "商户ID", required = true)
        private Long merchantId;
        
        @ApiModelProperty(value = "商品分类ID", required = true)
        private Long productCategoryId;
        
        @ApiModelProperty(value = "运费模板ID")
        private Long feightTemplateId;
        
        @ApiModelProperty(value = "商品属性分类ID")
        private Long productAttributeCategoryId;
        
        @ApiModelProperty(value = "商品价格", required = true)
        private BigDecimal price;
        
        @ApiModelProperty(value = "市场价")
        private BigDecimal originalPrice;
        
        @ApiModelProperty(value = "商品库存", required = true)
        private Integer stock;
        
        @ApiModelProperty(value = "计量单位")
        private String unit;
        
        @ApiModelProperty(value = "商品重量，默认为克")
        private BigDecimal weight;
        
        @ApiModelProperty(value = "排序", notes = "数值越小越靠前")
        private Integer sort;
        
        @ApiModelProperty(value = "商品描述")
        private String description;
        
        @ApiModelProperty(value = "商品副标题")
        private String subTitle;
        
        @ApiModelProperty(value = "商品相册图片，多张图片以逗号分隔")
        private String albumPics;
        
        @ApiModelProperty(value = "商品主图")
        private String pic;
        
        @ApiModelProperty(value = "详情页标题")
        private String detailTitle;
        
        @ApiModelProperty(value = "详情页描述")
        private String detailDesc;
        
        @ApiModelProperty(value = "商品详情HTML内容")
        private String detailHtml;
        
        @ApiModelProperty(value = "商品详情移动端内容")
        private String detailMobileContent;
        
        @ApiModelProperty(value = "促销类型：0->没有促销；1->单品促销；2->折扣促销")
        private Integer promotionType;
        
        @ApiModelProperty(value = "商品关键字")
        private String keywords;
        
        @ApiModelProperty(value = "商品备注")
        private String note;
        
        @ApiModelProperty(value = "促销开始时间")
        private LocalDateTime promotionStartTime;
        
        @ApiModelProperty(value = "促销结束时间")
        private LocalDateTime promotionEndTime;
        
        @ApiModelProperty(value = "活动限购数量")
        private Integer promotionPerLimit;
        
        @ApiModelProperty(value = "促销价格")
        private BigDecimal promotionPrice;
        
        @ApiModelProperty(value = "上架状态：0->下架；1->上架")
        private Integer publishStatus;
        
        @ApiModelProperty(value = "新品状态：0->不是新品；1->新品")
        private Integer newStatus;
        
        @ApiModelProperty(value = "推荐状态：0->不推荐；1->推荐")
        private Integer recommendStatus;
    }

    /**
     * 商品属性值参数
     */
    @Data
    @ApiModel(description = "商品属性值参数")
    public static class ProductAttributeValueParam {
        
        @ApiModelProperty(value = "商品属性ID", required = true)
        private Long productAttributeId;
        
        @ApiModelProperty(value = "商品属性值", required = true)
        private String value;
    }

    /**
     * 商品满减价格参数
     */
    @Data
    @ApiModel(description = "商品满减价格参数")
    public static class ProductFullReductionParam {
        
        @ApiModelProperty(value = "满减金额", required = true)
        private BigDecimal fullPrice;
        
        @ApiModelProperty(value = "减少金额", required = true)
        private BigDecimal reducePrice;
    }

    /**
     * 商品阶梯价格参数
     */
    @Data
    @ApiModel(description = "商品阶梯价格参数")
    public static class ProductLadderParam {
        
        @ApiModelProperty(value = "数量", required = true)
        private Integer count;
        
        @ApiModelProperty(value = "折扣", required = true)
        private BigDecimal discount;
        
        @ApiModelProperty(value = "价格", required = true)
        private BigDecimal price;
    }

    /**
     * 商品SKU库存参数
     */
    @Data
    @ApiModel(description = "商品SKU库存参数")
    public static class SkuStockParam {
        
        @ApiModelProperty(value = "SKU编码")
        private String skuCode;
        
        @ApiModelProperty(value = "SKU价格", required = true)
        private BigDecimal price;
        
        @ApiModelProperty(value = "SKU库存", required = true)
        private Integer stock;
        
        @ApiModelProperty(value = "销售属性组合，格式：[{\"key\":\"颜色\",\"value\":\"红色\"},{\"key\":\"尺寸\",\"value\":\"XL\"}]")
        private String spData;
        
        @ApiModelProperty(value = "SKU图片")
        private String pic;
    }
} 