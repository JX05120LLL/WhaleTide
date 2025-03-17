package com.whale_tide.controller.management.product;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale_tide.common.api.CommonResult;
import com.whale_tide.dto.management.product.ProductCategoryDto;
import com.whale_tide.dto.management.product.ProductCategoryParam;
import com.whale_tide.service.management.IProductCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品分类管理Controller
 */
@Slf4j
@RestController
@RequestMapping("/productCategory")
public class ProductCategoryController {

    @Autowired
    private IProductCategoryService productCategoryService;

    /**
     * 获取所有一级和二级分类及其子分类
     */
    @ApiOperation("获取商品分类及其子分类")
    @GetMapping("/list/withChildren")
    public CommonResult<List<Map<String, Object>>> getListWithChildren() {
        log.info("获取商品分类及其子分类");
        
        try {
            List<Map<String, Object>> result = productCategoryService.getListWithChildren();
            return CommonResult.success(result);
        } catch (Exception e) {
            log.error("获取商品分类及其子分类失败", e);
            return CommonResult.failed("获取商品分类及其子分类失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取分页分类列表
     */
    @GetMapping("/list/{parentId}")
    public CommonResult<Map<String, Object>> getList(
            @PathVariable Long parentId,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        
        log.info("获取分类列表, 父类ID={}, pageNum={}, pageSize={}", 
                parentId, pageNum, pageSize);
        
        try {
            Page<ProductCategoryDto> pageResult = productCategoryService.getList(parentId, pageNum, pageSize);
            
            // 创建响应对象
            Map<String, Object> result = new HashMap<>();
            result.put("list", pageResult.getRecords());
            result.put("pageNum", pageResult.getCurrent());
            result.put("pageSize", pageResult.getSize());
            result.put("total", pageResult.getTotal());
            result.put("totalPage", pageResult.getPages());
            
            return CommonResult.success(result);
        } catch (Exception e) {
            log.error("获取分类列表失败", e);
            return CommonResult.failed("获取分类列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取单个分类详情
     */
    @GetMapping("/{id}")
    public CommonResult<ProductCategoryDto> getItem(@PathVariable Long id) {
        log.info("获取分类详情, id={}", id);
        
        try {
            ProductCategoryDto category = productCategoryService.getItem(id);
            if (category != null) {
                return CommonResult.success(category);
            } else {
                return CommonResult.failed("分类不存在");
            }
        } catch (Exception e) {
            log.error("获取分类详情失败", e);
            return CommonResult.failed("获取分类详情失败: " + e.getMessage());
        }
    }
    
    /**
     * 创建商品分类
     */
    @PostMapping("/create")
    public CommonResult<Long> create(@RequestBody ProductCategoryParam param) {
        log.info("创建商品分类");
        
        try {
            Long id = productCategoryService.create(param);
            if (id > 0) {
                return CommonResult.success(id);
            } else {
                return CommonResult.failed("创建分类失败");
            }
        } catch (Exception e) {
            log.error("创建分类失败", e);
            return CommonResult.failed("创建分类失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新商品分类
     */
    @PostMapping("/update/{id}")
    public CommonResult<Integer> update(
            @PathVariable Long id,
            @RequestBody ProductCategoryParam param) {
        log.info("更新商品分类, id={}", id);
        
        try {
            int count = productCategoryService.update(id, param);
            if (count > 0) {
                return CommonResult.success(count);
            } else {
                return CommonResult.failed("更新分类失败");
            }
        } catch (Exception e) {
            log.error("更新分类失败", e);
            return CommonResult.failed("更新分类失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除商品分类
     */
    @PostMapping("/delete/{id}")
    public CommonResult<Integer> delete(@PathVariable Long id) {
        log.info("删除商品分类, id={}", id);
        
        try {
            int count = productCategoryService.delete(id);
            if (count > 0) {
                return CommonResult.success(count);
            } else if (count == -1) {
                return CommonResult.failed("该分类下存在子分类，无法删除");
            } else {
                return CommonResult.failed("删除分类失败");
            }
        } catch (Exception e) {
            log.error("删除分类失败", e);
            return CommonResult.failed("删除分类失败: " + e.getMessage());
        }
    }
    
    /**
     * 批量更新导航状态
     */
    @PostMapping("/update/navStatus")
    public CommonResult<Integer> updateNavStatus(@RequestBody Map<String, Object> params) {
        try {
            List<Long> ids = ((List<Integer>) params.get("ids")).stream()
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
            Integer navStatus = (Integer) params.get("navStatus");
            
            int count = productCategoryService.updateNavStatus(ids, navStatus);
            if (count > 0) {
                return CommonResult.success(count);
            } else {
                return CommonResult.failed("更新导航状态失败");
            }
        } catch (Exception e) {
            log.error("更新导航状态失败", e);
            return CommonResult.failed("更新导航状态失败: " + e.getMessage());
        }
    }
    
    /**
     * 批量更新显示状态
     */
    @PostMapping("/update/showStatus")
    public CommonResult<Integer> updateShowStatus(@RequestBody Map<String, Object> params) {
        try {
            List<Long> ids = ((List<Integer>) params.get("ids")).stream()
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
            Integer showStatus = (Integer) params.get("showStatus");
            
            int count = productCategoryService.updateShowStatus(ids, showStatus);
            if (count > 0) {
                return CommonResult.success(count);
            } else {
                return CommonResult.failed("更新显示状态失败");
            }
        } catch (Exception e) {
            log.error("更新显示状态失败", e);
            return CommonResult.failed("更新显示状态失败: " + e.getMessage());
        }
    }
} 