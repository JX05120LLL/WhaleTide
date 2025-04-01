package com.whale_tide.controller.client;

import com.whale_tide.common.api.CommonResult;
import com.whale_tide.common.api.PageResponse;
import com.whale_tide.dto.client.home.HomeCategoryResponse;
import com.whale_tide.dto.client.home.HomeContentResponse;
import com.whale_tide.dto.client.home.HomeProductRequest;
import com.whale_tide.dto.client.product.ProductListItemResponse;
import com.whale_tide.service.client.IHomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 首页内容控制器
 */
@RestController
@Api(tags = "首页内容相关接口")
@RequestMapping("/home")
public class HomeController {
    
    @Autowired
    private IHomeService homeService;
    
    @ApiOperation("获取首页内容")
    @GetMapping("/content")
    public CommonResult<HomeContentResponse> getHomeContent() {
        HomeContentResponse content = homeService.getHomeContent();
        return CommonResult.success(content);
    }
    
    @ApiOperation("获取首页推荐商品")
    @GetMapping("/recommendProductList")
    public CommonResult<PageResponse<ProductListItemResponse>> getRecommendProductList(HomeProductRequest request) {
        PageResponse<ProductListItemResponse> response = homeService.getRecommendProductList(request);
        return CommonResult.success(response);
    }
    
    @ApiOperation("获取首页新品")
    @GetMapping("/newProductList")
    public CommonResult<PageResponse<ProductListItemResponse>> getNewProductList(HomeProductRequest request) {
        PageResponse<ProductListItemResponse> response = homeService.getNewProductList(request);
        return CommonResult.success(response);
    }
    
    @ApiOperation("获取首页热卖商品")
    @GetMapping("/hotProductList")
    public CommonResult<PageResponse<ProductListItemResponse>> getHotProductList(HomeProductRequest request) {
        PageResponse<ProductListItemResponse> response = homeService.getHotProductList(request);
        return CommonResult.success(response);
    }
    
    @ApiOperation("获取商品分类列表")
    @GetMapping("/categoryList")
    public CommonResult<List<HomeCategoryResponse>> getCategoryList(
            @ApiParam(value = "父分类ID", defaultValue = "0") 
            @RequestParam(value = "parentId", defaultValue = "0") Long parentId) {
        List<HomeCategoryResponse> categoryList = homeService.getCategoryList(parentId);
        return CommonResult.success(categoryList);
    }
    
    @ApiOperation("获取商品分类列表")
    @GetMapping("/productCateList/{parentId}")
    public CommonResult<List<HomeCategoryResponse>> getProductCateList(
            @ApiParam(value = "父分类ID", defaultValue = "0") 
            @PathVariable(value = "parentId") Long parentId) {
        List<HomeCategoryResponse> categoryList = homeService.getCategoryList(parentId);
        return CommonResult.success(categoryList);
    }
} 