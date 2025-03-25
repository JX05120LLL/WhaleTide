package com.whale_tide.controller.management.product;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale_tide.common.api.CommonPage;
import com.whale_tide.common.api.CommonResult;
import com.whale_tide.dto.management.product.*;
import com.whale_tide.entity.pms.PmsBrands;
import com.whale_tide.entity.pms.PmsProductCategories;
import com.whale_tide.entity.pms.PmsProductSkus;
import com.whale_tide.mapper.pms.PmsBrandsMapper;
import com.whale_tide.mapper.pms.PmsProductCategoriesMapper;
import com.whale_tide.service.management.IProductService;
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
@RestController("managementProductController")
@Api(tags = "ProductController", description = "产品管理")
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private IProductService productService;

    @Autowired
    private PmsBrandsMapper brandsMapper;

    @Autowired
    private PmsProductCategoriesMapper productCategoriesMapper;

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

        // 调用服务层方法获取真实数据
        IPage<ProductListResult> productPage = productService.getProductList(queryParam);

        // 创建响应对象
        Map<String, Object> result = new HashMap<>();
        result.put("list", productPage.getRecords());
        result.put("pageNum", productPage.getCurrent());
        result.put("pageSize", productPage.getSize());
        result.put("total", productPage.getTotal());
        result.put("totalPage", productPage.getPages());

        return CommonResult.success(result);
    }

    /**
     * 获取单个商品详情
     */
    @GetMapping("/{id}")
    public CommonResult<Map<String, Object>> getItem(@PathVariable Long id) {
        log.info("获取商品详情, id={}", id);

        // 获取商品详情
        ProductParam productDetail = productService.getUpdateInfo(id);
        if (productDetail == null) {
            return CommonResult.failed("商品不存在");
        }

        // 从ProductParam中获取基本信息
        ProductParam.ProductBasicParam basicInfo = productDetail.getProductParam();
        if (basicInfo == null) {
            return CommonResult.failed("商品基本信息不存在");
        }

        // 转换为Map格式
        Map<String, Object> product = new HashMap<>();
        product.put("id", id);
        product.put("name", basicInfo.getName());
        product.put("productSn", basicInfo.getProductSn());
        product.put("price", basicInfo.getPrice());
        product.put("originalPrice", basicInfo.getOriginalPrice());

        // 获取SKU信息
        if (productDetail.getSkuStockList() != null && !productDetail.getSkuStockList().isEmpty()) {
            int totalStock = productDetail.getSkuStockList().stream()
                    .mapToInt(sku -> sku.getStock() != null ? sku.getStock() : 0)
                    .sum();
            product.put("stock", totalStock);
        } else {
            product.put("stock", basicInfo.getStock());
        }

        product.put("lowStock", 10); // 预警库存
        product.put("unit", basicInfo.getUnit());
        product.put("weight", basicInfo.getWeight());
        product.put("sort", basicInfo.getSort());

        // 获取品牌信息
        if (basicInfo.getBrandId() != null) {
            product.put("brandId", basicInfo.getBrandId());
            try {
                PmsBrands brand = brandsMapper.selectById(basicInfo.getBrandId());
                product.put("brandName", brand != null ? brand.getName() : "未知品牌");
            } catch (Exception e) {
                log.error("获取品牌信息失败", e);
                product.put("brandName", "未知品牌");
            }
        }

        // 获取分类信息
        if (basicInfo.getCategoryId() != null) {
            product.put("productCategoryId", basicInfo.getCategoryId());
            try {
                PmsProductCategories category = productCategoriesMapper.selectById(basicInfo.getCategoryId());
                product.put("productCategoryName", category != null ? category.getName() : "未知分类");
            } catch (Exception e) {
                log.error("获取分类信息失败", e);
                product.put("productCategoryName", "未知分类");
            }
        }

        product.put("pic", basicInfo.getPic());
        product.put("albumPics", basicInfo.getAlbumPics());
        product.put("description", basicInfo.getDescription());
        product.put("detailDesc", basicInfo.getDetailDesc());

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
        IPage<ProductListResult> productPage = productService.getProductList(queryParam);
        CommonPage<ProductListResult> commonPage = CommonPage.restPage((Page<ProductListResult>) productPage);

        return CommonResult.success(commonPage);
    }

    @ApiOperation("获取简单产品列表")
    @GetMapping("/simpleList")
    public CommonResult<List<ProductSimpleResult>> simpleList(@RequestParam(value = "keyword", required = false) String keyword) {
        log.info("获取简单产品列表, keyword={}", keyword);

        // 调用服务层获取真实数据
        List<ProductSimpleResult> resultList = productService.getProductSimple(keyword);
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
        // 添加详细日志记录
        log.info("接收到创建商品请求，参数详情:");
        log.info("productParam是否为null: {}", productParam == null);
        
        if (productParam != null) {
            log.info("productParam.productParam是否为null: {}", productParam.getProductParam() == null);
            if (productParam.getProductParam() != null) {
                log.info("基本信息: name={}, categoryId={}, pic={}, price={}", 
                    productParam.getProductParam().getName(),
                    productParam.getProductParam().getCategoryId(),
                    productParam.getProductParam().getPic(),
                    productParam.getProductParam().getPrice());
            }
            
            log.info("skuStockList是否为null: {}", productParam.getSkuStockList() == null);
            if (productParam.getSkuStockList() != null) {
                log.info("skuStockList大小: {}", productParam.getSkuStockList().size());
            }
            
            log.info("attributeValueList是否为null: {}", productParam.getProductAttributeValueList() == null);
            if (productParam.getProductAttributeValueList() != null) {
                log.info("attributeValueList大小: {}", productParam.getProductAttributeValueList().size());
            }
        }
        
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
                // 如果服务层返回null，返回错误信息
                return CommonResult.failed("未找到ID为" + id + "的商品信息");
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

    @ApiOperation("获取商品SKU")
    @GetMapping("/sku/{id}")
    public CommonResult<List<ProductSkuResult>> getProductSkus(@PathVariable Long id) {
        List<PmsProductSkus> productSkuses = productService.getProductSkus(id);
        List<ProductSkuResult> results = new ArrayList<>();
        for (PmsProductSkus productSkus : productSkuses) {
            ProductSkuResult result = new ProductSkuResult();
            result.setSkuCode(productSkus.getSkuCode());
            result.setPrice(productSkus.getPrice());
            result.setStock(productSkus.getStock());
            result.setLowStock(productSkus.getLowStock());

            results.add(result);
        }

        return CommonResult.success(results);
    }
}
