package com.whale_tide.controller.client;


import com.whale_tide.common.api.CommonResult;
import com.whale_tide.common.api.PageResponse;
import com.whale_tide.dto.client.product.*;
import com.whale_tide.service.client.IBrandService;
import com.whale_tide.service.client.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController("clientBrandController")
@RequestMapping("/brand")
public class BrandController {

    @Autowired
    private IBrandService brandService;

    // 获取品牌详情
    @GetMapping("/detail/{id}")
    public CommonResult<BrandDetailResponse> getBrandDetail(@PathVariable Long id) {
        BrandDetailResponse brandDetail = brandService.getBrandDetail(id);
        if (brandDetail == null) {
            return CommonResult.failed("获取品牌详情失败");
        }
        return CommonResult.success(brandDetail);
    }

    // 获取品牌商品列表，需要分页
    @GetMapping("/productList")
    public CommonResult<PageResponse<ProductListItemResponse>> getBrandProducts(BrandProductRequest request) {
        PageResponse<ProductListItemResponse> brandProducts = brandService.getBrandProducts(request);
        if (brandProducts == null) {
            return CommonResult.failed("获取品牌商品列表失败");
        }
        return CommonResult.success(brandProducts);
    }


    // 获取推荐品牌列表，需要分页
    @GetMapping("/recommendList")
    public CommonResult<PageResponse<BrandListItemResponse>> getRecommendBrands(RecommendBrandRequest request) {
        PageResponse<BrandListItemResponse> recommendBrands = brandService.getRecommendBrands(request);
        if (recommendBrands == null) {
            return CommonResult.failed("获取推荐品牌列表失败");
        }
        return CommonResult.success(recommendBrands);
    }



}
