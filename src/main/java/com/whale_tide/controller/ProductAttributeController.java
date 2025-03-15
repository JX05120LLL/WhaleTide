package com.whale_tide.controller;

import com.whale_tide.common.api.CommonResult;
import com.whale_tide.dto.product.ProductAttributeDto;
import com.whale_tide.dto.product.ProductAttributeParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品属性管理Controller
 */
@Slf4j
@RestController
@RequestMapping("/productAttribute")
public class ProductAttributeController {

    /**
     * 根据分类获取属性列表
     */
    @GetMapping("/list/{cid}")
    public CommonResult<Map<String, Object>> getListByCategoryId(
            @PathVariable Long cid,
            @RequestParam(value = "type", defaultValue = "0") Integer type,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        
        log.info("获取属性列表, 分类ID={}, 类型={}, pageNum={}, pageSize={}", 
                cid, type, pageNum, pageSize);
        
        // 创建一个临时的属性列表数据作为示例
        List<Map<String, Object>> attributeList = new ArrayList<>();
        
        // 添加一些示例属性数据
        for (int i = 1; i <= 5; i++) {
            Map<String, Object> attribute = new HashMap<>();
            int id = cid.intValue() * 100 + i;
            attribute.put("id", id);
            attribute.put("productAttributeCategoryId", cid);
            
            if (type == 0) {
                // 规格属性
                attribute.put("name", "规格" + id);
                attribute.put("selectType", i % 2); // 0:唯一, 1:单选, 2:多选
                attribute.put("inputType", i % 3); // 0:手工录入, 1:从列表选取, 2:多行文本
                attribute.put("inputList", "选项1,选项2,选项3");
                attribute.put("filterType", i % 2); // 0:不需要, 1:需要
                attribute.put("searchType", i % 3); // 0:不需要, 1:关键字, 2:范围
                attribute.put("relatedStatus", i % 2); // 0:不关联, 1:关联
                attribute.put("handAddStatus", i % 2); // 0:不支持, 1:支持
            } else {
                // 参数属性
                attribute.put("name", "参数" + id);
                attribute.put("selectType", 0); // 参数都是唯一的
                attribute.put("inputType", i % 3); // 0:手工录入, 1:从列表选取, 2:多行文本
                attribute.put("inputList", "参数值1,参数值2,参数值3");
                attribute.put("filterType", 0); // 参数不需要筛选
                attribute.put("searchType", 0); // 参数不需要搜索
                attribute.put("relatedStatus", 0); // 参数不关联
                attribute.put("handAddStatus", 0); // 参数不支持手动添加
            }
            
            attribute.put("type", type); // 0:规格, 1:参数
            attribute.put("sort", i);
            
            attributeList.add(attribute);
        }
        
        // 创建响应对象
        Map<String, Object> result = new HashMap<>();
        result.put("list", attributeList);
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        result.put("total", 12); // 总条数
        result.put("totalPage", 3); // 总页数
        
        return CommonResult.success(result);
    }
    
    /**
     * 获取单个商品属性信息
     */
    @GetMapping("/{id}")
    public CommonResult<Map<String, Object>> getItem(@PathVariable Long id) {
        log.info("获取属性信息, id={}", id);
        
        // 创建一个临时的属性数据作为示例
        Map<String, Object> attribute = new HashMap<>();
        attribute.put("id", id);
        attribute.put("productAttributeCategoryId", id / 100);
        attribute.put("name", "属性" + id);
        attribute.put("selectType", id.intValue() % 2); // 0:唯一, 1:单选, 2:多选
        attribute.put("inputType", id.intValue() % 3); // 0:手工录入, 1:从列表选取, 2:多行文本
        attribute.put("inputList", "选项1,选项2,选项3");
        attribute.put("filterType", id.intValue() % 2); // 0:不需要, 1:需要
        attribute.put("searchType", id.intValue() % 3); // 0:不需要, 1:关键字, 2:范围
        attribute.put("relatedStatus", id.intValue() % 2); // 0:不关联, 1:关联
        attribute.put("handAddStatus", id.intValue() % 2); // 0:不支持, 1:支持
        attribute.put("type", id.intValue() % 2); // 0:规格, 1:参数
        attribute.put("sort", id.intValue() % 10);
        
        return CommonResult.success(attribute);
    }
    
    /**
     * 添加商品属性
     */
    @PostMapping("/create")
    public CommonResult<Integer> create(@RequestBody Map<String, Object> attribute) {
        log.info("添加商品属性: {}", attribute);
        
        // 返回模拟ID
        return CommonResult.success(101);
    }
    
    /**
     * 批量删除商品属性
     */
    @PostMapping("/delete")
    public CommonResult<Integer> delete(@RequestBody List<Long> ids) {
        log.info("批量删除商品属性, ids={}", ids);
        
        // 返回删除数量
        return CommonResult.success(ids.size());
    }
    
    /**
     * 更新商品属性
     */
    @PostMapping("/update/{id}")
    public CommonResult<Integer> update(
            @PathVariable Long id,
            @RequestBody Map<String, Object> attribute) {
        
        log.info("更新商品属性, id={}, attribute={}", id, attribute);
        
        // 返回更新结果
        return CommonResult.success(1);
    }
    
    /**
     * 获取商品分类下属性信息
     */
    @GetMapping("/attrInfo/{productCategoryId}")
    public CommonResult<List<ProductAttributeDto>> getAttrInfo(@PathVariable Long productCategoryId) {
        log.info("获取商品分类下属性信息, productCategoryId={}", productCategoryId);
        
        // 创建一个临时的属性列表数据作为示例
        List<ProductAttributeDto> attributeList = new ArrayList<>();
        
        // 添加一些示例属性数据
        for (int i = 1; i <= 5; i++) {
            ProductAttributeDto attribute = new ProductAttributeDto();
            attribute.setId((long)i);
            attribute.setProductAttributeCategoryId(productCategoryId);
            attribute.setName("属性" + i);
            attribute.setType(i % 2);  // 0-规格, 1-参数
            attribute.setInputType(i % 3);  // 0-手工输入, 1-从列表选择, 2-多行文本
            attribute.setInputList("选项1,选项2,选项3");
            attribute.setFilterType(i % 2);  // 0-不需要, 1-需要
            attribute.setSearchType(i % 3);  // 0-不需要, 1-关键字, 2-范围
            attribute.setRelatedStatus(i % 2);  // 0-不关联, 1-关联
            attribute.setHandAddStatus(i % 2);  // 0-不支持, 1-支持
            attribute.setSort(i);
            
            attributeList.add(attribute);
        }
        
        return CommonResult.success(attributeList);
    }
} 