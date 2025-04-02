package com.whale_tide.controller.client;


import com.whale_tide.common.api.CommonResult;
import com.whale_tide.common.api.PageResponse;
import com.whale_tide.common.exception.base.ProductException;
import com.whale_tide.dto.client.product.*;
import com.whale_tide.service.client.IProductService;
import com.whale_tide.service.client.IProductCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Api(tags = "商品相关接口")
@RestController("clientProductController")
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private IProductService productService;

    @Autowired
    private IProductCommentService commentService;

    @Autowired
    private IProductCommentService productCommentService;

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
    /**
     * 获取商品评论列表
     */

    @ApiImplicitParam(name = "Authorization", value = "身份认证Token", required = true, paramType = "header")
    @ApiOperation("获取商品评论列表")
    @GetMapping("/comment/list/{productId}")
    public CommonResult<PageResponse<ProductCommentResponse>> getProductCommentList(
            @ApiParam(value = "商品ID", required = true) @PathVariable Long productId,
            @ApiParam(value = "页码", required = true) @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页记录数", required = true) @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        try {
            ProductCommentParam queryParam = new ProductCommentParam();
            queryParam.setProductId(productId);
            queryParam.setPageNum(pageNum);
            queryParam.setPageSize(pageSize);
            PageResponse<ProductCommentResponse> productCommentList = productCommentService.getProductCommentList(queryParam);
            return CommonResult.success(productCommentList);
        } catch (Exception e) {
            log.error("获取商品评论列表异常: {}", e.getMessage(), e);
            return CommonResult.failed("获取商品评论列表失败: " + e.getMessage());
        }
    }
    /**
     * 添加商品评论
     */

    @ApiImplicitParam(name = "Authorization", value = "身份认证Token", required = true, paramType = "header")
    @ApiOperation("添加商品评论")
    @PostMapping("/comment/add")
    public CommonResult<Void> addProductComment(ProductCommentAddRequest request) {
        try {
            productCommentService.addProductComment(request);
            return CommonResult.success(null);
        } catch (Exception e) {
            log.error("添加商品评论异常: {}", e.getMessage(), e);
            return CommonResult.failed("添加商品评论失败: " + e.getMessage());
        }
    }
}





