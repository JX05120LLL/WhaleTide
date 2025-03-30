package com.whale_tide.dto.client.home;

import com.whale_tide.dto.client.product.ProductListItemResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 首页内容响应
 */
@Data
@ApiModel("首页内容响应")
public class HomeContentResponse {
    
    @ApiModelProperty("轮播广告列表")
    private List<HomeAdvertiseResponse> advertiseList;
    
    @ApiModelProperty("品牌推荐列表")
    private List<HomeBrandResponse> brandList;
    
    @ApiModelProperty("新品推荐列表")
    private List<ProductListItemResponse> newProductList;
    
    @ApiModelProperty("热销商品列表")
    private List<ProductListItemResponse> hotProductList;
    
    @ApiModelProperty("专题推荐列表")
    private List<HomeSubjectResponse> subjectList;
    
    /**
     * 首页轮播广告
     */
    @Data
    @ApiModel("首页轮播广告")
    public static class HomeAdvertiseResponse {
        @ApiModelProperty("广告ID")
        private Long id;
        
        @ApiModelProperty("广告图片")
        private String pic;
        
        @ApiModelProperty("广告链接")
        private String url;
        
        @ApiModelProperty("广告名称")
        private String name;
    }
    
    /**
     * 首页品牌推荐
     */
    @Data
    @ApiModel("首页品牌推荐")
    public static class HomeBrandResponse {
        @ApiModelProperty("品牌ID")
        private Long id;
        
        @ApiModelProperty("品牌名称")
        private String name;
        
        @ApiModelProperty("品牌logo")
        private String logo;
    }
    
    /**
     * 首页专题推荐
     */
    @Data
    @ApiModel("首页专题推荐")
    public static class HomeSubjectResponse {
        @ApiModelProperty("专题ID")
        private Long id;
        
        @ApiModelProperty("专题标题")
        private String title;
        
        @ApiModelProperty("专题图片")
        private String pic;
        
        @ApiModelProperty("专题分类ID")
        private Long categoryId;
        
        @ApiModelProperty("专题描述")
        private String description;
    }
} 