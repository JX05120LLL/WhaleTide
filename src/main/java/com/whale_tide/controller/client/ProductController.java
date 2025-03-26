package com.whale_tide.controller.client;


import com.whale_tide.common.api.CommonResult;
import com.whale_tide.common.api.PageResponse;
import com.whale_tide.common.exception.base.ProductException;
import com.whale_tide.dto.client.product.CategoryTreeResponse;
import com.whale_tide.dto.client.product.ProductDetailResponse;
import com.whale_tide.dto.client.product.ProductListItemResponse;
import com.whale_tide.dto.client.product.ProductSearchRequest;
import com.whale_tide.service.client.IProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "商品相关接口")
@RestController("clientProductController")
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private IProductService productService;

    /**
     * 商品关键词搜索
     */
    @ApiOperation("商品关键词搜索")
    @GetMapping("/search")
    public CommonResult<PageResponse<ProductListItemResponse>> getProductByKeyword(ProductSearchRequest request) {
        try {
            PageResponse<ProductListItemResponse> productsByKeyword = productService.getProductsByKeyword(request);
            return CommonResult.success(productsByKeyword);
        } catch (Exception e) {
            log.error("商品搜索异常: {}", e.getMessage(), e);
            return CommonResult.failed("搜索失败: " + e.getMessage());
        }
    }

    /**
     * 获取商品分类树
     */
    @ApiOperation("获取商品分类树")
    @GetMapping("/categoryTreeList")
    public CommonResult<CategoryTreeResponse> getCategoryTree() {
        try {
            CategoryTreeResponse categoryTree = productService.getCategoryTree();
            return CommonResult.success(categoryTree);
        } catch (Exception e) {
            log.error("获取商品分类树异常: {}", e.getMessage(), e);
            return CommonResult.failed("获取商品分类树失败: " + e.getMessage());
        }
    }

    /**
     * 获取商品详情
     */
    @ApiOperation("获取商品详情")
    @GetMapping("/detail/{id}")
    public CommonResult<ProductDetailResponse> getProductDetail(
            @ApiParam(value = "商品ID", required = true) @PathVariable Long id) {
        try {
            ProductDetailResponse productDetail = productService.getProductDetail(id);
            return CommonResult.success(productDetail);
        } catch (ProductException e) {
            log.error("获取商品详情异常: {}", e.getMessage(), e);
            return CommonResult.failed(e.getMessage());
        } catch (Exception e) {
            log.error("获取商品详情异常: {}", e.getMessage(), e);
            return CommonResult.failed("获取商品详情失败: " + e.getMessage());
        }
    }
}
