package com.whale_tide.controller.product;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale_tide.common.api.CommonPage;
import com.whale_tide.common.api.CommonResult;
import com.whale_tide.dto.product.ProductAttributeCategoryDto;
import com.whale_tide.dto.product.ProductAttributeCategoryParam;
import com.whale_tide.dto.product.ProductAttributeCategoryWithAttrDto;
import com.whale_tide.dto.product.ProductAttributeDto;
import com.whale_tide.service.IProductAttributeCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品属性分类管理Controller
 */
@Slf4j
@RestController
@Api(tags = "ProductAttributeCategoryController", description = "商品属性分类管理")
@RequestMapping("/productAttribute/category")
public class ProductAttributeCategoryController {

    @Autowired
    private IProductAttributeCategoryService productAttributeCategoryService;

    /**
     * 获取属性分类列表
     */
    @ApiOperation("获取属性分类列表")
    @GetMapping("/list")
    public CommonResult<Map<String, Object>> getList(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        
        log.info("获取属性分类列表, pageNum={}, pageSize={}", pageNum, pageSize);
        
        try {
            // 获取属性分类列表
            Page<ProductAttributeCategoryDto> pageResult = productAttributeCategoryService.getList(pageNum, pageSize);
            
            // 创建响应对象 - 保持与前端期望一致的格式
            Map<String, Object> result = new HashMap<>();
            result.put("list", pageResult.getRecords());
            result.put("pageNum", pageResult.getCurrent());
            result.put("pageSize", pageResult.getSize());
            result.put("total", pageResult.getTotal());
            result.put("totalPage", pageResult.getPages());
            
            return CommonResult.success(result);
        } catch (Exception e) {
            log.error("获取属性分类列表出错", e);
            return CommonResult.failed("获取属性分类列表出错: " + e.getMessage());
        }
    }
    
    /**
     * 获取单个属性分类信息
     */
    @ApiOperation("获取单个属性分类信息")
    @GetMapping("/{id}")
    public CommonResult<ProductAttributeCategoryDto> getItem(@PathVariable Long id) {
        log.info("获取单个属性分类信息, id={}", id);
        
        try {
            ProductAttributeCategoryDto category = productAttributeCategoryService.getItem(id);
            if (category != null) {
                return CommonResult.success(category);
            } else {
                return CommonResult.failed("属性分类不存在");
            }
        } catch (Exception e) {
            log.error("获取属性分类信息出错", e);
            return CommonResult.failed("获取属性分类信息出错: " + e.getMessage());
        }
    }
    
    /**
     * 添加属性分类
     */
    @ApiOperation("添加属性分类")
    @PostMapping("/create")
    public CommonResult<Long> create(@RequestBody ProductAttributeCategoryParam param) {
        log.info("添加属性分类, param={}", param);
        
        try {
            Long id = productAttributeCategoryService.create(param);
            if (id > 0) {
                return CommonResult.success(id);
            } else {
                return CommonResult.failed("创建属性分类失败");
            }
        } catch (Exception e) {
            log.error("创建属性分类出错", e);
            return CommonResult.failed("创建属性分类出错: " + e.getMessage());
        }
    }
    
    /**
     * 修改属性分类
     */
    @ApiOperation("修改属性分类")
    @PostMapping("/update/{id}")
    public CommonResult<Integer> update(
            @PathVariable Long id,
            @RequestBody ProductAttributeCategoryParam param) {
        log.info("修改属性分类, id={}, param={}", id, param);
        
        try {
            int count = productAttributeCategoryService.update(id, param);
            if (count > 0) {
                return CommonResult.success(count);
            } else {
                return CommonResult.failed("修改属性分类失败");
            }
        } catch (Exception e) {
            log.error("修改属性分类出错", e);
            return CommonResult.failed("修改属性分类出错: " + e.getMessage());
        }
    }
    
    /**
     * 删除属性分类
     */
    @ApiOperation("删除属性分类")
    @GetMapping("/delete/{id}")
    public CommonResult<Integer> delete(@PathVariable Long id) {
        log.info("删除属性分类, id={}", id);
        
        try {
            int count = productAttributeCategoryService.delete(id);
            if (count > 0) {
                return CommonResult.success(count);
            } else {
                return CommonResult.failed("删除属性分类失败");
            }
        } catch (Exception e) {
            log.error("删除属性分类出错", e);
            return CommonResult.failed("删除属性分类出错: " + e.getMessage());
        }
    }
    
    /**
     * 获取所有属性分类及其下属性
     */
    @ApiOperation("获取所有属性分类及其下属性")
    @GetMapping("/list/withAttr")
    public CommonResult<List<ProductAttributeCategoryWithAttrDto>> getListWithAttr() {
        log.info("获取所有属性分类及其下属性");
        
        try {
            List<ProductAttributeCategoryWithAttrDto> list = productAttributeCategoryService.getListWithAttr();
            return CommonResult.success(list);
        } catch (Exception e) {
            log.error("获取属性分类及属性列表出错", e);
            return CommonResult.failed("获取属性分类及属性列表出错: " + e.getMessage());
        }
    }


}
