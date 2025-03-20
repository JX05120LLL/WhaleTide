package com.whale_tide.controller.client;


import com.whale_tide.common.api.CommonPage;
import com.whale_tide.common.api.CommonResult;
import com.whale_tide.common.api.PageResponse;
import com.whale_tide.dto.client.product.BrandDetailResponse;
import com.whale_tide.dto.client.product.ProductDetailResponse;
import com.whale_tide.dto.client.product.ProductListItemResponse;
import com.whale_tide.dto.client.product.ProductSearchRequest;
import com.whale_tide.service.client.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("clientProductController")
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private IProductService productService;

    //商品关键词搜索
    @GetMapping("/search")
    public CommonResult<PageResponse<ProductListItemResponse>> getProductByKeyword(ProductSearchRequest request) {
        PageResponse<ProductListItemResponse> productsByKeyword = productService.getProductsByKeyword(request);
        if (productsByKeyword == null) {
            return CommonResult.failed("搜索失败");
        }
        return CommonResult.success(productsByKeyword);
    }

    //   获取商品分类树



    // 获取商品详情
    @GetMapping("/detail/{id}")
    public CommonResult<ProductDetailResponse> getProductDetail(@PathVariable Long id) {
        ProductDetailResponse productDetail = productService.getProductDetail(id);
        if (productDetail == null) {
            return CommonResult.failed("获取商品详情失败");
        }
        return CommonResult.success(productDetail);
    }






}
