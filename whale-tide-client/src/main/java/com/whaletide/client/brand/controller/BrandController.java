package com.whaletide.client.brand.controller;

import com.whaletide.client.brand.service.IBrandService;
import com.whaletide.client.brand.vo.BrandVO;
import com.whaletide.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 品牌控制器
 */
@RestController
@RequestMapping("/api/brand")
@Tag(name = "品牌管理", description = "品牌相关接口")
public class BrandController {

    @Autowired
    private IBrandService brandService;

    @GetMapping("/list")
    @Operation(summary = "获取品牌列表")
    public CommonResult<List<BrandVO>> list() {
        return brandService.list();
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "获取品牌详情")
    public CommonResult<BrandVO> getById(@PathVariable("id") 
                                        @Parameter(description = "品牌ID", required = true) Long id) {
        return brandService.getById(id);
    }

    @GetMapping("/featured")
    @Operation(summary = "获取推荐品牌列表")
    public CommonResult<List<BrandVO>> listFeatured() {
        return brandService.listFeatured();
    }

    @GetMapping("/search")
    @Operation(summary = "搜索品牌")
    public CommonResult<List<BrandVO>> search(@RequestParam("keyword") 
                                             @Parameter(description = "搜索关键词", required = true) String keyword) {
        return brandService.search(keyword);
    }
} 