package com.whaletide.client.product.controller;

import com.whaletide.client.product.dto.ProductCommentAddDTO;
import com.whaletide.client.product.dto.ProductCommentQueryDTO;
import com.whaletide.client.product.dto.ProductSearchDTO;
import com.whaletide.client.product.service.IProductCommentService;
import com.whaletide.client.product.service.IProductService;
import com.whaletide.client.product.service.IHistoryService;
import com.whaletide.client.product.vo.BrandVO;
import com.whaletide.client.product.vo.CategoryTreeVO;
import com.whaletide.client.product.vo.ProductCommentVO;
import com.whaletide.client.product.vo.ProductDetailVO;
import com.whaletide.client.product.vo.ProductListItemVO;
import com.whaletide.client.product.vo.ProductSuggestionVO;
import com.whaletide.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/product")
@Tag(name = "商品相关接口")
public class ProductController {

    private final IProductService productService;
    private final IProductCommentService productCommentService;
    private final IHistoryService historyService;

    @Autowired
    public ProductController(IProductService productService, 
                           IProductCommentService productCommentService,
                           IHistoryService historyService) {
        this.productService = productService;
        this.productCommentService = productCommentService;
        this.historyService = historyService;
    }

    /**
     * 商品关键词搜索
     */
    @Operation(summary = "商品关键词搜索")
    @GetMapping("/search")
    public CommonResult<List<ProductListItemVO>> getProductByKeyword(ProductSearchDTO searchDTO) {
        List<ProductListItemVO> productList = productService.getProductsByKeyword(searchDTO);
        return CommonResult.success(productList);
    }

    /**
     * 获取商品分类树
     */
    @Operation(summary = "获取商品分类树")
    @GetMapping("/categoryTreeList")
    public CommonResult<CategoryTreeVO> getCategoryTree() {
        CategoryTreeVO categoryTree = productService.getCategoryTree();
        return CommonResult.success(categoryTree);
    }
    
    /**
     * 获取商品分类列表（兼容前端API请求路径）
     */
    @Operation(summary = "获取商品分类列表")
    @GetMapping("/categories")
    public CommonResult<List<CategoryTreeVO>> getCategories() {
        // 获取完整分类树
        CategoryTreeVO categoryTree = productService.getCategoryTree();
        
        // 返回一级分类列表（包含其子分类）
        List<CategoryTreeVO> categoryList = categoryTree.getChildren();
        
        // 如果分类树为空，返回一个空列表
        if (categoryList == null) {
            categoryList = new ArrayList<>();
        }
        
        return CommonResult.success(categoryList);
    }

    /**
     * 获取商品详情
     */
    @Operation(summary = "获取商品详情")
    @GetMapping("/detail/{id}")
    public CommonResult<ProductDetailVO> getProductDetail(
            @Parameter(description = "商品ID") @PathVariable Long id) {
        ProductDetailVO productDetail = productService.getProductDetail(id);
        return CommonResult.success(productDetail);
    }
    
    /**
     * 获取商品评论列表
     */
    @Operation(summary = "获取商品评论列表")
    @GetMapping("/comment/list/{productId}")
    public CommonResult<ProductCommentVO> getProductCommentList(
            @Parameter(description = "商品ID") @PathVariable Long productId,
            @Parameter(description = "页码") @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页记录数") @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        log.info("接收到商品评论查询请求，商品ID: {}, 页码: {}, 每页数量: {}", productId, pageNum, pageSize);
        
        ProductCommentQueryDTO queryDTO = new ProductCommentQueryDTO();
        queryDTO.setProductId(productId);
        queryDTO.setPageNum(pageNum);
        queryDTO.setPageSize(pageSize);
        
        try {
            ProductCommentVO productComment = productCommentService.getProductCommentList(queryDTO);
            log.info("查询到商品评论，ID: {}, 评论数量: {}", productId, 
                     (productComment != null && productComment.getList() != null) ? productComment.getList().size() : 0);
            return CommonResult.success(productComment);
        } catch (Exception e) {
            log.error("获取商品评论失败", e);
            // 返回空结果避免前端错误
            return CommonResult.success(new ProductCommentVO());
        }
    }
    
    /**
     * 添加商品评论
     */
    @Operation(summary = "添加商品评论")
    @PostMapping("/comment/add")
    public CommonResult<Void> addProductComment(@RequestBody ProductCommentAddDTO commentDTO) {
        productCommentService.addProductComment(commentDTO);
        return CommonResult.success(null, "评论成功");
    }
    
    /**
     * 获取热门搜索关键词
     */
    @Operation(summary = "获取热门搜索关键词")
    @GetMapping("/hotKeywords")
    public CommonResult<List<String>> getHotKeywords() {
        List<String> hotKeywords = productService.getHotKeywords();
        return CommonResult.success(hotKeywords);
    }

    /**
     * 获取搜索建议
     */
    @Operation(summary = "获取搜索建议")
    @GetMapping("/suggestions")
    public CommonResult<List<ProductSuggestionVO>> getSuggestions(
            @Parameter(description = "关键词") @RequestParam String keyword) {
        List<ProductSuggestionVO> suggestions = productService.getSuggestions(keyword);
        return CommonResult.success(suggestions);
    }
    
    /**
     * 获取品牌列表
     */
    @Operation(summary = "获取品牌列表")
    @GetMapping("/brands")
    public CommonResult<List<BrandVO>> getBrands(
            @Parameter(description = "数量限制") @RequestParam(value = "limit", defaultValue = "8") Integer limit) {
        List<BrandVO> brands = productService.getBrands(limit);
        return CommonResult.success(brands);
    }

    /**
     * 获取分类商品列表
     */
    @Operation(summary = "获取分类商品列表")
    @GetMapping("/category/{categoryId}/products")
    public CommonResult<List<ProductListItemVO>> getCategoryProducts(
            @Parameter(description = "分类ID") @PathVariable Long categoryId,
            @Parameter(description = "页码") @RequestParam(value = "page", defaultValue = "1") Integer page,
            @Parameter(description = "每页记录数") @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @Parameter(description = "排序字段") @RequestParam(value = "sort", required = false) String sort) {
        
        List<ProductListItemVO> productList = productService.getCategoryProducts(categoryId, page, pageSize, sort);
        return CommonResult.success(productList);
    }

    /**
     * 获取相关商品
     */
    @Operation(summary = "获取相关商品")
    @GetMapping("/related/{id}")
    public CommonResult<List<ProductListItemVO>> getRelatedProducts(
            @Parameter(description = "商品ID") @PathVariable Long id,
            @Parameter(description = "数量限制") @RequestParam(value = "limit", defaultValue = "5") Integer limit) {
        List<ProductListItemVO> relatedProducts = productService.getRelatedProducts(id, limit);
        return CommonResult.success(relatedProducts);
    }

    /**
     * 悬浮搜索页面的搜索功能
     */
    @Operation(summary = "悬浮搜索功能")
    @GetMapping("/floatSearch")
    public CommonResult<Map<String, Object>> floatSearch(
            @Parameter(description = "关键词") @RequestParam String keyword,
            @Parameter(description = "分类ID") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "页码") @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量") @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        
        log.info("悬浮搜索请求: 关键词={}, 分类={}, 页码={}, 每页数量={}", keyword, categoryId, pageNum, pageSize);
        
        Map<String, Object> result = new HashMap<>();
        
        // 构建搜索DTO
        ProductSearchDTO searchDTO = new ProductSearchDTO();
        searchDTO.setKeyword(keyword);
        searchDTO.setCategoryId(categoryId);
        searchDTO.setPageNum(pageNum);
        searchDTO.setPageSize(pageSize);
        
        // 获取搜索结果
        List<ProductListItemVO> products = productService.getProductsByKeyword(searchDTO);
        result.put("products", products);
        
        // 获取相关搜索建议
        List<ProductSuggestionVO> suggestions = productService.getSuggestions(keyword);
        result.put("suggestions", suggestions);
        
        // 记录搜索历史
        historyService.addSearchHistory(keyword);
        
        // 获取搜索历史
        List<String> searchHistory = historyService.getSearchHistory(10);
        result.put("history", searchHistory);
        
        return CommonResult.success(result);
    }
} 