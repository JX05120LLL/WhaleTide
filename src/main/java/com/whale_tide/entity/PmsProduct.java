package com.whale_tide.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品信息表
 */
@Data
@TableName("pms_product")
public class PmsProduct {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 商品名称
     */
    private String name;
    
    /**
     * 商品副标题
     */
    private String subTitle;
    
    /**
     * 商品编号
     */
    private String productSn;
    
    /**
     * 商品分类ID
     */
    private Long categoryId;
    
    /**
     * 商品品牌ID
     */
    private Long brandId;
    
    /**
     * 商品主图
     */
    private String pic;
    
    /**
     * 商品相册图片，多张图片以逗号分隔
     */
    private String albumPics;
    
    /**
     * 详情页标题
     */
    private String detailTitle;
    
    /**
     * 详情页描述
     */
    private String detailDesc;
    
    /**
     * 商品详情内容
     */
    private String detailContent;
    
    /**
     * 商品详情移动端内容
     */
    private String detailMobileContent;
    
    /**
     * 商品价格
     */
    private BigDecimal price;
    
    /**
     * 市场价
     */
    private BigDecimal originalPrice;
    
    /**
     * 商品库存
     */
    private Integer stock;
    
    /**
     * 商品预警库存
     */
    private Integer lowStock;
    
    /**
     * 计量单位
     */
    private String unit;
    
    /**
     * 商品重量，默认为克
     */
    private BigDecimal weight;
    
    /**
     * 排序，数值越小越靠前
     */
    private Integer sort;

    /**
     * 上架状态：0->下架；1->上架
     */
    private Integer publishStatus;
    
    /**
     * 新品状态：0->不是新品；1->新品
     */
    private Integer newStatus;
    
    /**
     * 推荐状态：0->不推荐；1->推荐
     */
    private Integer recommandStatus;
    
    /**
     * 审核状态：0->未审核；1->审核通过
     */
    private Integer verifyStatus;
    
    /**
     * 销量
     */
    private Integer sale;
    
    /**
     * 是否为预告商品：0->不是；1->是
     */
    private Integer previewStatus;
    
    /**
     * 是否删除：0->未删除；1->已删除
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