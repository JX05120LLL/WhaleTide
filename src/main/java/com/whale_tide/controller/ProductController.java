package com.whale_tide.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.whale_tide.common.api.CommonPage;
import com.whale_tide.common.api.CommonResult;
import com.whale_tide.dto.*;
import com.whale_tide.service.IProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
/*
产品管理控制器
 */

@RestController
@Api(tags = "ProductController",description = "产品管理")
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private IProductService productService;

    @ApiOperation("获取产品列表")
    @GetMapping("/list")
    public CommonResult<CommonPage<ProductListResult>> list(ProductQueryParam queryParam) {
        IPage<ProductListResult> productlist = productService.getProductList(queryParam);
        return CommonResult.success(CommonPage.restPage(productlist));
    }

    @ApiOperation("获取简单产品列表")
    @GetMapping("/simpleList")
    public ProductSimpleResult simpleList(String keyword) {
        return productService.getProductSimple(keyword);

    }
    @ApiOperation("更新删除状态")
    @PostMapping("/update/deleteStatus")
    public CommonResult<Integer> updateDeleteStatus(@RequestBody UpdateDeleteStatusParam updateDeleteStatusParam) {
        // 调用服务层方法，更新删除状态
        int count = productService.updateDeleteStatus(updateDeleteStatusParam);

        // 根据更新结果返回响应
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed("更新失败");
        }
    }
    @ApiOperation("更新新品状态")
    @PostMapping("/update/newStatus")
    public CommonResult<Integer> updateNewStatus(@RequestBody UpdateNewStatusParam updateNewStatusParam) {
        // 调用服务层方法，更新新品状态
        int count = productService.updateNewStatus(updateNewStatusParam);

        // 根据更新结果返回响应
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed("更新失败");
        }
    }
    @ApiOperation("更新推荐状态")
    @PostMapping("/update/recommendStatus")
    public CommonResult<Integer> updateRecommendStatus(@RequestBody UpdateRecommendStatusParam updateRecommendStatusParam) {
        // 调用服务层方法，更新推荐状态
        int count = productService.updateRecommendStatus(updateRecommendStatusParam);

        // 根据更新结果返回响应
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed("更新失败");
        }
    }
    @ApiOperation("更新上架状态")
    @PostMapping("/update/publishStatus")
    public CommonResult<Integer> updatePublishStatus(@RequestBody UpdatePublishStatusParam updatePublishStatusParam) {
        // 调用服务层方法，更新上架状态
        int count = productService.updatePublishStatusParam(updatePublishStatusParam);

        // 根据更新结果返回响应
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed("更新失败");
        }
    }


}
