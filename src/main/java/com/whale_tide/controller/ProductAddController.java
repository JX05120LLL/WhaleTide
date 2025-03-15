package com.whale_tide.controller;

import com.whale_tide.common.api.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 添加商品页面控制器
 */
@Slf4j
@RestController
@Api(tags = "ProductAddController", description = "添加商品页面")
@RequestMapping("/pms/addProduct")
public class ProductAddController {

    /**
     * 获取添加商品页面初始数据
     */
    @ApiOperation("获取添加商品页面初始数据")
    @GetMapping
    public CommonResult<Object> getAddProductPageData() {
        log.info("获取添加商品页面初始数据");
        
        // 返回一个空对象，表示成功访问此接口
        return CommonResult.success(new Object());
    }
} 