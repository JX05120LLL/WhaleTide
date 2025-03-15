package com.whale_tide.controller;

import com.whale_tide.common.api.CommonResult;
import com.whale_tide.dto.product.ProductAttributeCategoryDto;
import com.whale_tide.dto.product.ProductAttributeCategoryParam;
import com.whale_tide.dto.product.ProductAttributeCategoryWithAttrDto;
import com.whale_tide.dto.product.ProductAttributeDto;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/productAttribute/category")
public class ProductAttributeCategoryController {

    /**
     * 获取属性分类列表
     */
    @GetMapping("/list")
    public CommonResult<Map<String, Object>> getList(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        
        log.info("获取属性分类列表, pageNum={}, pageSize={}", pageNum, pageSize);
        
        // 创建一个临时的属性分类列表数据作为示例
        List<ProductAttributeCategoryDto> categoryList = new ArrayList<>();
        
        // 添加一些示例属性分类数据
        for (int i = 1; i <= 5; i++) {
            ProductAttributeCategoryDto category = new ProductAttributeCategoryDto();
            category.setId((long)i);
            category.setName("属性分类" + i);
            category.setAttributeCount(10 + i);
            category.setParamCount(5 + i);
            
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
     * 获取单个属性分类信息
     */
    @GetMapping("/{id}")
    public CommonResult<ProductAttributeCategoryDto> getItem(@PathVariable Long id) {
        log.info("获取属性分类信息, id={}", id);
        
        // 创建一个临时的属性分类数据作为示例
        ProductAttributeCategoryDto category = new ProductAttributeCategoryDto();
        category.setId(id);
        category.setName("属性分类" + id);
        category.setAttributeCount(10 + id.intValue());
        category.setParamCount(5 + id.intValue());
        
        return CommonResult.success(category);
    }
    
    /**
     * 添加属性分类
     */
    @PostMapping("/create")
    public CommonResult<Long> create(@RequestBody ProductAttributeCategoryParam param) {
        log.info("添加属性分类: {}", param);
        
        // 返回模拟ID
        return CommonResult.success(101L);
    }
    
    /**
     * 更新属性分类
     */
    @PostMapping("/update/{id}")
    public CommonResult<Integer> update(
            @PathVariable Long id,
            @RequestBody ProductAttributeCategoryParam param) {
        
        log.info("更新属性分类, id={}, param={}", id, param);
        
        // 返回更新结果
        return CommonResult.success(1);
    }
    
    /**
     * 删除属性分类
     */
    @GetMapping("/delete/{id}")
    public CommonResult<Integer> delete(@PathVariable Long id) {
        log.info("删除属性分类, id={}", id);
        
        // 返回删除结果
        return CommonResult.success(1);
    }
    
    /**
     * 获取所有属性分类及其下属性
     */
    @GetMapping("/list/withAttr")
    public CommonResult<List<ProductAttributeCategoryWithAttrDto>> getListWithAttr() {
        log.info("获取所有属性分类及其下属性");
        
        // 创建一个临时的带属性的属性分类列表数据作为示例
        List<ProductAttributeCategoryWithAttrDto> categoryList = new ArrayList<>();
        
        // 添加一些示例属性分类数据
        for (int i = 1; i <= 3; i++) {
            ProductAttributeCategoryWithAttrDto category = new ProductAttributeCategoryWithAttrDto();
            category.setId((long)i);
            category.setName("属性分类" + i);
            
            // 添加属性列表
            List<ProductAttributeDto> attributeList = new ArrayList<>();
            for (int j = 1; j <= 3; j++) {
                ProductAttributeDto attribute = new ProductAttributeDto();
                int attrId = i * 10 + j;
                attribute.setId((long)attrId);
                attribute.setProductAttributeCategoryId((long)i);
                attribute.setName("属性" + attrId);
                attribute.setType(j % 2); // 0-规格, 1-参数
                attribute.setInputType(j % 3); // 0-手工输入, 1-从列表选择, 2-多行文本
                attribute.setInputList("选项1,选项2,选项3");
                attribute.setFilterType(j % 2); // 0-不需要, 1-需要
                attribute.setSearchType(j % 3); // 0-不需要, 1-关键字, 2-范围
                attribute.setRelatedStatus(j % 2); // 0-不关联, 1-关联
                attribute.setHandAddStatus(j % 2); // 0-不支持, 1-支持
                attribute.setSort(j);
                
                attributeList.add(attribute);
            }
            
            category.setProductAttributeList(attributeList);
            categoryList.add(category);
        }
        
        return CommonResult.success(categoryList);
    }
}
