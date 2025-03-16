package com.whale_tide.controller.resource;

import com.whale_tide.common.api.CommonResult;
import com.whale_tide.dto.resource.ResourceCategoryParam;
import com.whale_tide.dto.resource.ResourceCategoryResult;
import com.whale_tide.service.IResourceCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 资源分类管理Controller
 */
@Slf4j
@RestController
@Api(tags = "ResourceCategoryController", description = "资源分类管理")
@RequestMapping("/resourceCategory")
public class ResourceCategoryController {

    @Autowired
    private IResourceCategoryService resourceCategoryService;

    @ApiOperation("获取所有资源分类")
    @GetMapping("/listAll")
    public CommonResult<List<ResourceCategoryResult>> getAll() {
        log.info("获取所有资源分类");
        
        List<ResourceCategoryResult> categoryList = resourceCategoryService.listAll();
        return CommonResult.success(categoryList);
    }

    @ApiOperation("添加资源分类")
    @PostMapping("/create")
    public CommonResult<Boolean> create(@RequestBody ResourceCategoryParam resourceCategoryParam) {
        log.info("添加资源分类: {}", resourceCategoryParam);
        
        boolean success = resourceCategoryService.create(resourceCategoryParam);
        if (success) {
            return CommonResult.success(true);
        } else {
            return CommonResult.failed("添加资源分类失败");
        }
    }

    @ApiOperation("修改资源分类")
    @PostMapping("/update/{id}")
    public CommonResult<Boolean> update(@PathVariable Long id, @RequestBody ResourceCategoryParam resourceCategoryParam) {
        log.info("修改资源分类, id={}, param={}", id, resourceCategoryParam);
        
        boolean success = resourceCategoryService.update(id, resourceCategoryParam);
        if (success) {
            return CommonResult.success(true);
        } else {
            return CommonResult.failed("修改资源分类失败");
        }
    }

    @ApiOperation("删除资源分类")
    @PostMapping("/delete/{id}")
    public CommonResult<Boolean> delete(@PathVariable Long id) {
        log.info("删除资源分类, id={}", id);
        
        boolean success = resourceCategoryService.delete(id);
        if (success) {
            return CommonResult.success(true);
        } else {
            return CommonResult.failed("删除资源分类失败");
        }
    }
} 