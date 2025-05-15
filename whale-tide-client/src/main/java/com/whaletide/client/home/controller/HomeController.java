package com.whaletide.client.home.controller;

import com.whaletide.client.home.service.IHomeService;
import com.whaletide.client.home.vo.BannerVO;
import com.whaletide.client.product.vo.ProductListItemVO;
import com.whaletide.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 首页相关控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/home")
@Tag(name = "首页相关接口")
public class HomeController {

    private final IHomeService homeService;

    public HomeController(IHomeService homeService) {
        this.homeService = homeService;
    }

    /**
     * 获取首页轮播图
     */
    @Operation(summary = "获取首页轮播图")
    @GetMapping("/banners")
    public CommonResult<List<BannerVO>> getBanners() {
        List<BannerVO> banners = homeService.getBanners();
        return CommonResult.success(banners);
    }

    /**
     * 获取热门商品
     */
    @Operation(summary = "获取热门商品")
    @GetMapping("/hot")
    public CommonResult<List<ProductListItemVO>> getHotProducts(
            @Parameter(description = "数量限制") @RequestParam(value = "limit", defaultValue = "8") Integer limit) {
        List<ProductListItemVO> hotProducts = homeService.getHotProducts(limit);
        return CommonResult.success(hotProducts);
    }

    /**
     * 获取新品上市
     */
    @Operation(summary = "获取新品上市")
    @GetMapping("/new")
    public CommonResult<List<ProductListItemVO>> getNewProducts(
            @Parameter(description = "数量限制") @RequestParam(value = "limit", defaultValue = "8") Integer limit) {
        List<ProductListItemVO> newProducts = homeService.getNewProducts(limit);
        return CommonResult.success(newProducts);
    }

    /**
     * 获取促销商品
     */
    @Operation(summary = "获取促销商品")
    @GetMapping("/promotion")
    public CommonResult<List<ProductListItemVO>> getPromotionProducts(
            @Parameter(description = "数量限制") @RequestParam(value = "limit", defaultValue = "8") Integer limit) {
        List<ProductListItemVO> promotionProducts = homeService.getPromotionProducts(limit);
        return CommonResult.success(promotionProducts);
    }

    /**
     * 获取限时折扣商品
     */
    @Operation(summary = "获取限时折扣商品")
    @GetMapping("/discount")
    public CommonResult<List<ProductListItemVO>> getDiscountProducts(
            @Parameter(description = "数量限制") @RequestParam(value = "limit", defaultValue = "8") Integer limit) {
        List<ProductListItemVO> discountProducts = homeService.getDiscountProducts(limit);
        return CommonResult.success(discountProducts);
    }

    /**
     * 获取优选好物商品
     */
    @Operation(summary = "获取优选好物商品")
    @GetMapping("/selected")
    public CommonResult<List<ProductListItemVO>> getSelectedProducts(
            @Parameter(description = "数量限制") @RequestParam(value = "limit", defaultValue = "8") Integer limit) {
        List<ProductListItemVO> selectedProducts = homeService.getSelectedProducts(limit);
        return CommonResult.success(selectedProducts);
    }
} 