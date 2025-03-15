package com.whale_tide.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.whale_tide.common.api.CommonPage;
import com.whale_tide.common.api.CommonResult;
import com.whale_tide.dto.product.*;
import com.whale_tide.service.IProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 商品管理Controller
 */
@Slf4j
@RestController
@Api(tags = "ProductController",description = "产品管理")
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private IProductService productService;

    /**
     * 获取商品列表
     */
    @GetMapping("/list")
    public CommonResult<Map<String, Object>> getList(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "productSn", required = false) String productSn,
            @RequestParam(value = "brandId", required = false) Long brandId,
            @RequestParam(value = "productCategoryId", required = false) Long productCategoryId,
            @RequestParam(value = "publishStatus", required = false) Integer publishStatus,
            @RequestParam(value = "verifyStatus", required = false) Integer verifyStatus,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        
        log.info("获取商品列表, keyword={}, productSn={}, brandId={}, productCategoryId={}, publishStatus={}, pageNum={}, pageSize={}", 
                keyword, productSn, brandId, productCategoryId, publishStatus, pageNum, pageSize);
        
        // 创建一个临时的商品列表数据作为示例
        List<Map<String, Object>> productList = new ArrayList<>();
        
        // 添加一些示例商品数据
        for (int i = 1; i <= 10; i++) {
            Map<String, Object> product = new HashMap<>();
            product.put("id", i);
            product.put("name", "商品" + i);
            product.put("productSn", "SN" + (100000 + i));
            product.put("price", 99.0 + i);
            product.put("stock", 100 + i * 10);
            product.put("sale", 10 + i);
            product.put("brandId", i % 5 + 1);
            product.put("brandName", "品牌" + (i % 5 + 1));
            product.put("productCategoryId", i % 3 + 1);
            product.put("productCategoryName", "分类" + (i % 3 + 1));
            product.put("pic", "https://example.com/product" + i + ".jpg");
            product.put("verifyStatus", verifyStatus != null ? verifyStatus : 1);
            product.put("publishStatus", publishStatus != null ? publishStatus : 1);
            product.put("newStatus", 1);
            product.put("recommandStatus", 1);
            
            // 如果设置了关键词过滤，则只添加包含关键词的商品
            if (keyword == null || product.get("name").toString().contains(keyword)) {
                // 如果设置了货号过滤，则只添加匹配货号的商品
                if (productSn == null || product.get("productSn").toString().equals(productSn)) {
                    // 如果设置了品牌ID过滤，则只添加匹配品牌ID的商品
                    if (brandId == null || product.get("brandId").equals(brandId)) {
                        // 如果设置了分类ID过滤，则只添加匹配分类ID的商品
                        if (productCategoryId == null || product.get("productCategoryId").equals(productCategoryId)) {
                            productList.add(product);
                        }
                    }
                }
            }
        }
        
        // 创建响应对象
        Map<String, Object> result = new HashMap<>();
        result.put("list", productList);
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        result.put("total", 100); // 总条数
        result.put("totalPage", 10); // 总页数
        
        return CommonResult.success(result);
    }
    
    /**
     * 获取单个商品详情
     */
    @GetMapping("/{id}")
    public CommonResult<Map<String, Object>> getItem(@PathVariable Long id) {
        log.info("获取商品详情, id={}", id);
        
        // 创建一个临时的商品数据作为示例
        Map<String, Object> product = new HashMap<>();
        product.put("id", id);
        product.put("name", "商品" + id);
        product.put("productSn", "SN" + (100000 + id));
        product.put("price", 99.0 + id);
        product.put("originalPrice", 199.0 + id);
        product.put("stock", 100 + id.intValue() * 10);
        product.put("lowStock", 10);
        product.put("unit", "件");
        product.put("weight", 0.5 + id.intValue() * 0.1);
        product.put("sort", id.intValue());
        product.put("brandId", id.intValue() % 5 + 1);
        product.put("brandName", "品牌" + (id.intValue() % 5 + 1));
        product.put("productCategoryId", id.intValue() % 3 + 1);
        product.put("productCategoryName", "分类" + (id.intValue() % 3 + 1));
        product.put("pic", "https://example.com/product" + id + ".jpg");
        product.put("albumPics", "https://example.com/product" + id + "_1.jpg,https://example.com/product" + id + "_2.jpg");
        product.put("description", "这是商品" + id + "的描述...");
        product.put("detailDesc", "这是商品" + id + "的详细描述...");
        
        return CommonResult.success(product);
    }

    @ApiOperation("获取产品列表(分页)")
    @GetMapping("/pageList")
    public CommonResult<CommonPage<ProductListResult>> pageList(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "productSn", required = false) String productSn,
            @RequestParam(value = "brandId", required = false) Long brandId,
            @RequestParam(value = "productCategoryId", required = false) Long productCategoryId,
            @RequestParam(value = "publishStatus", required = false) Integer publishStatus,
            @RequestParam(value = "verifyStatus", required = false) Integer verifyStatus,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        
        log.info("获取产品列表(分页), keyword={}, productSn={}, brandId={}, productCategoryId={}, publishStatus={}, pageNum={}, pageSize={}", 
                keyword, productSn, brandId, productCategoryId, publishStatus, pageNum, pageSize);
                
        // 创建查询参数对象
        ProductQueryParam queryParam = new ProductQueryParam();
        queryParam.setKeyword(keyword);
        queryParam.setProductSn(productSn);
        queryParam.setBrandId(brandId);
        queryParam.setProductCategoryId(productCategoryId);
        queryParam.setPublishStatus(publishStatus);
        queryParam.setVerifyStatus(verifyStatus);
        queryParam.setPageNum(pageNum);
        queryParam.setPageSize(pageSize);
        
        // 调用服务层方法
        IPage<ProductListResult> productlist = productService.getProductList(queryParam);
        return CommonResult.success(CommonPage.restPage(productlist));
    }

    @ApiOperation("获取简单产品列表")
    @GetMapping("/simpleList")
    public CommonResult<List<ProductSimpleResult>> simpleList(@RequestParam(value = "keyword", required = false) String keyword) {
        log.info("获取简单产品列表, keyword={}", keyword);
        
        // 创建临时示例数据
        List<ProductSimpleResult> resultList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            ProductSimpleResult result = new ProductSimpleResult();
            result.setId((long)i);
            result.setName("商品" + i);
            // 如果有关键词且不匹配，则跳过
            if (keyword != null && !keyword.isEmpty() && !result.getName().contains(keyword)) {
                continue;
            }
            resultList.add(result);
        }
        
        return CommonResult.success(resultList);
    }
    
    @ApiOperation("更新删除状态")
    @PostMapping("/update/deleteStatus")
    public CommonResult<Integer> updateDeleteStatus(
            @RequestParam("ids") String ids,
            @RequestParam("deleteStatus") Integer deleteStatus) {
        log.info("更新删除状态, ids={}, deleteStatus={}", ids, deleteStatus);
        
        // 创建参数对象
        UpdateDeleteStatusParam param = new UpdateDeleteStatusParam();
        param.setIds(ids);
        param.setDeleteStatus(deleteStatus);
        
        // 调用服务层方法，更新删除状态
        int count = productService.updateDeleteStatus(param);

        // 根据更新结果返回响应
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed("更新失败");
        }
    }
    
    @ApiOperation("更新新品状态")
    @PostMapping("/update/newStatus")
    public CommonResult<Integer> updateNewStatus(
            @RequestParam("ids") String ids,
            @RequestParam("newStatus") Integer newStatus) {
        log.info("更新新品状态, ids={}, newStatus={}", ids, newStatus);
        
        // 创建参数对象
        UpdateNewStatusParam param = new UpdateNewStatusParam();
        param.setIds(ids);
        param.setNewStatus(newStatus);
        
        // 调用服务层方法，更新新品状态
        int count = productService.updateNewStatus(param);

        // 根据更新结果返回响应
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed("更新失败");
        }
    }

    @ApiOperation("更新推荐状态")
    @PostMapping("/update/recommendStatus")
    public CommonResult<Integer> updateRecommendStatus(
            @RequestParam("ids") String ids,
            @RequestParam("recommendStatus") Integer recommendStatus) {
        log.info("更新推荐状态, ids={}, recommendStatus={}", ids, recommendStatus);
        
        // 创建参数对象
        UpdateRecommendStatusParam param = new UpdateRecommendStatusParam();
        param.setIds(ids);
        param.setRecommendStatus(recommendStatus);
        
        // 调用服务层方法，更新推荐状态
        int count = productService.updateRecommendStatus(param);

        // 根据更新结果返回响应
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed("更新失败");
        }
    }

    @ApiOperation("更新上架状态")
    @PostMapping("/update/publishStatus")
    public CommonResult<Integer> updatePublishStatus(
            @RequestParam("ids") String ids,
            @RequestParam("publishStatus") Integer publishStatus) {
        log.info("更新上架状态, ids={}, publishStatus={}", ids, publishStatus);
        
        // 创建参数对象
        UpdatePublishStatusParam param = new UpdatePublishStatusParam();
        param.setIds(ids);
        param.setPublishStatus(publishStatus);
        
        // 调用服务层方法，更新上架状态
        int count = productService.updatePublishStatusParam(param);

        // 根据更新结果返回响应
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed("更新失败");
        }
    }

    @ApiOperation("创建商品")
    @PostMapping("/create")
    public CommonResult<Integer> createProduct(@RequestBody ProductParam productParam) {
        // 调用服务层方法，创建商品
        int count = productService.createProduct(productParam);

        // 根据创建结果返回响应
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed("创建商品失败，请检查参数是否完整");
        }
    }

    @ApiOperation("获取商品编辑信息")
    @GetMapping("/updateInfo/{id}")
    public CommonResult<ProductParam> getUpdateInfo(@PathVariable Long id) {
        log.info("获取商品编辑信息, id={}", id);
        
        try {
            // 调用服务层方法，获取商品编辑信息
            ProductParam productParam = productService.getUpdateInfo(id);
            if (productParam != null) {
                return CommonResult.success(productParam);
            } else {
                // 如果服务层返回null，创建一个基本的ProductParam对象
                ProductParam demoParam = new ProductParam();
                demoParam.setProductParam(new ProductParam.ProductBasicParam());
                demoParam.getProductParam().setName("示例商品" + id);
                demoParam.getProductParam().setProductSn("SN" + (100000 + id));
                
                // 使用BigDecimal而不是double
                demoParam.getProductParam().setPrice(new java.math.BigDecimal("99.00"));
                demoParam.getProductParam().setStock(100);
                demoParam.getProductParam().setDescription("这是一个示例商品描述");
                demoParam.setSkuStockList(new ArrayList<>());
                demoParam.setProductAttributeValueList(new ArrayList<>());
                
                return CommonResult.success(demoParam);
            }
        } catch (Exception e) {
            log.error("获取商品编辑信息失败", e);
            return CommonResult.failed("获取商品信息失败: " + e.getMessage());
        }
    }

    @ApiOperation("更新商品")
    @PostMapping("/update/{id}")
    public CommonResult<Integer> updateProduct(@PathVariable Long id, @RequestBody ProductParam productParam) {
        // 调用服务层方法，更新商品
        int count = productService.updateProduct(id, productParam);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed("更新商品失败");
        }
    }

}
