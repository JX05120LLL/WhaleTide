package com.whale_tide.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.whale_tide.common.api.CommonPage;
import com.whale_tide.common.api.CommonResult;
import com.whale_tide.dto.resource.ResourceParam;
import com.whale_tide.dto.resource.ResourceResult;
import com.whale_tide.service.IResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 资源管理Controller
 */
@Slf4j
@RestController
@Api(tags = "ResourceController", description = "资源管理")
@RequestMapping("/resource")
public class ResourceController {

    @Autowired
    private IResourceService resourceService;

    @ApiOperation("获取资源列表")
    @GetMapping("/list")
    public CommonResult<CommonPage<ResourceResult>> getList(
            @RequestParam(value = "nameKeyword", required = false) String nameKeyword,
            @RequestParam(value = "urlKeyword", required = false) String urlKeyword,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        
        log.info("获取资源列表, nameKeyword={}, urlKeyword={}, categoryId={}, pageNum={}, pageSize={}", 
                nameKeyword, urlKeyword, categoryId, pageNum, pageSize);
        
        IPage<ResourceResult> resourcePage = resourceService.list(nameKeyword, urlKeyword, categoryId, pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(resourcePage));
    }

    @ApiOperation("获取所有资源")
    @GetMapping("/listAll")
    public CommonResult<List<ResourceResult>> getAll() {
        log.info("获取所有资源");
        
        List<ResourceResult> resourceList = resourceService.listAll();
        return CommonResult.success(resourceList);
    }

    @ApiOperation("添加资源")
    @PostMapping("/create")
    public CommonResult<Boolean> create(@RequestBody ResourceParam resourceParam) {
        log.info("添加资源: {}", resourceParam);
        
        boolean success = resourceService.create(resourceParam);
        if (success) {
            return CommonResult.success(true);
        } else {
            return CommonResult.failed("添加资源失败");
        }
    }

    @ApiOperation("修改资源")
    @PostMapping("/update/{id}")
    public CommonResult<Boolean> update(@PathVariable Long id, @RequestBody ResourceParam resourceParam) {
        log.info("修改资源, id={}, param={}", id, resourceParam);
        
        boolean success = resourceService.update(id, resourceParam);
        if (success) {
            return CommonResult.success(true);
        } else {
            return CommonResult.failed("修改资源失败");
        }
    }

    @ApiOperation("删除资源")
    @PostMapping("/delete/{id}")
    public CommonResult<Boolean> delete(@PathVariable Long id) {
        log.info("删除资源, id={}", id);
        
        boolean success = resourceService.delete(id);
        if (success) {
            return CommonResult.success(true);
        } else {
            return CommonResult.failed("删除资源失败");
        }
    }
} 