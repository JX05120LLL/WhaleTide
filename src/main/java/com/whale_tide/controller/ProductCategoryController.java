package com.whale_tide.controller;

import com.whale_tide.common.api.CommonResult;
import com.whale_tide.dto.product.ProductCategoryDto;
import com.whale_tide.dto.product.ProductCategoryParam;
import com.whale_tide.dto.product.ProductCategoryWithChildrenDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品分类管理Controller
 */
@Slf4j
@RestController
@RequestMapping("/productCategory")
public class ProductCategoryController {

    /**
     * 获取所有一级和二级分类及其子分类
     */
    @ApiOperation("获取商品分类及其子分类")
    @GetMapping("/list/withChildren")
    public CommonResult<List<Map<String, Object>>> getListWithChildren() {
        log.info("获取商品分类及其子分类");
        
        // 创建一个临时的示例数据
        List<Map<String, Object>> result = new ArrayList<>();
        
        // 添加一些示例分类数据
        for (int i = 1; i <= 5; i++) {
            Map<String, Object> category = new HashMap<>();
            category.put("id", i);
            category.put("name", "分类" + i);
            
            // 添加子分类
            List<Map<String, Object>> children = new ArrayList<>();
            for (int j = 1; j <= 3; j++) {
                Map<String, Object> child = new HashMap<>();
                child.put("id", i * 10 + j);
                child.put("name", "子分类" + i + "-" + j);
                children.add(child);
            }
            category.put("children", children);
            
            result.add(category);
        }
        
        return CommonResult.success(result);
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
        
        // 创建一个临时的分类列表数据作为示例
        List<ProductCategoryDto> categoryList = new ArrayList<>();
        
        // 添加一些示例分类数据
        for (int i = 1; i <= 5; i++) {
            ProductCategoryDto category = new ProductCategoryDto();
            int id = parentId.intValue() * 10 + i;
            category.setId((long)id);
            category.setName("分类" + id);
            category.setLevel(parentId == 0 ? 0 : 1);
            category.setProductCount(100 + i * 10);
            category.setProductUnit("件");
            category.setNavStatus(1);
            category.setShowStatus(1);
            category.setSort(i);
            category.setIcon("https://example.com/icon" + id + ".jpg");
            category.setKeywords("关键词" + id);
            category.setDescription("描述" + id);
            
            if (parentId > 0) {
                category.setParentId(parentId);
            }
            
            categoryList.add(category);
        }
        
        // 创建响应对象
        Map<String, Object> result = new HashMap<>();
        result.put("list", categoryList);
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        result.put("total", 12); // 总条数
        result.put("totalPage", 3); // 总页数
        
        return CommonResult.success(result);
    }
    
    /**
     * 获取单个分类信息
     */
    @GetMapping("/{id}")
    public CommonResult<ProductCategoryDto> getItem(@PathVariable Long id) {
        log.info("获取分类信息, id={}", id);
        
        // 创建一个临时的分类数据作为示例
        ProductCategoryDto category = new ProductCategoryDto();
        category.setId(id);
        category.setName("分类" + id);
        category.setLevel(id < 10 ? 0 : 1);
        category.setProductCount(100 + id.intValue() * 10);
        category.setProductUnit("件");
        category.setNavStatus(1);
        category.setShowStatus(1);
        category.setSort(id.intValue());
        category.setIcon("https://example.com/icon" + id + ".jpg");
        category.setKeywords("关键词" + id);
        category.setDescription("描述" + id);
        
        if (id >= 10) {
            category.setParentId(id / 10);
        }
        
        return CommonResult.success(category);
    }
    
    /**
     * 创建产品分类
     */
    @PostMapping("/create")
    public CommonResult<Long> create(@RequestBody ProductCategoryParam param) {
        log.info("创建产品分类: {}", param);
        
        // 返回模拟ID
        return CommonResult.success(101L);
    }
    
    /**
     * 更新产品分类
     */
    @PostMapping("/update/{id}")
    public CommonResult<Integer> update(
            @PathVariable Long id,
            @RequestBody ProductCategoryParam param) {
        
        log.info("更新产品分类, id={}, param={}", id, param);
        
        // 返回更新结果
        return CommonResult.success(1);
    }
    
    /**
     * 删除产品分类
     */
    @PostMapping("/delete/{id}")
    public CommonResult<Integer> delete(@PathVariable Long id) {
        log.info("删除产品分类, id={}", id);
        
        // 返回删除结果
        return CommonResult.success(1);
    }
    
    /**
     * 更新导航栏显示状态
     */
    @PostMapping("/update/navStatus")
    public CommonResult<Integer> updateNavStatus(@RequestBody Map<String, Object> params) {
        List<Long> ids = (List<Long>) params.get("ids");
        Integer navStatus = (Integer) params.get("navStatus");
        
        log.info("更新分类导航栏显示状态, ids={}, navStatus={}", ids, navStatus);
        
        // 此处仅返回成功结果，实际应处理更新逻辑
        return CommonResult.success(ids.size());
    }
    
    /**
     * 更新显示状态
     */
    @PostMapping("/update/showStatus")
    public CommonResult<Integer> updateShowStatus(@RequestBody Map<String, Object> params) {
        List<Long> ids = (List<Long>) params.get("ids");
        Integer showStatus = (Integer) params.get("showStatus");
        
        log.info("更新分类显示状态, ids={}, showStatus={}", ids, showStatus);
        
        // 此处仅返回成功结果，实际应处理更新逻辑
        return CommonResult.success(ids.size());
    }
} 