package com.whale_tide.controller;

import com.whale_tide.common.api.CommonPage;
import com.whale_tide.common.api.CommonResult;
import com.whale_tide.dto.product.BrandDto;
import com.whale_tide.dto.product.BrandParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 品牌管理Controller
 */
@Slf4j
@RestController
@Api(tags = "BrandController", description = "品牌管理")
@RequestMapping("/brand")
public class BrandController {

    @ApiOperation("获取品牌列表")
    @GetMapping("/list")
    public CommonResult<CommonPage<Map<String, Object>>> getBrandList(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        
        log.info("获取品牌列表, keyword={}, pageNum={}, pageSize={}", keyword, pageNum, pageSize);
        
        // 创建一个临时的品牌列表数据作为示例
        List<Map<String, Object>> brandList = new ArrayList<>();
        
        // 添加一些示例品牌数据
        for (int i = 1; i <= 10; i++) {
            Map<String, Object> brand = new HashMap<>();
            brand.put("id", i);
            brand.put("name", "品牌" + i);
            brand.put("firstLetter", (char)('A' + i - 1));
            brand.put("sort", i);
            brand.put("factoryStatus", 1);
            brand.put("showStatus", 1);
            brand.put("productCount", 100 + i * 10);
            brand.put("productCommentCount", 50 + i * 5);
            brand.put("logo", "https://example.com/logo" + i + ".jpg");
            brand.put("bigPic", "https://example.com/bigPic" + i + ".jpg");
            brand.put("brandStory", "品牌故事" + i);
            
            brandList.add(brand);
        }
        
        // 创建CommonPage对象
        CommonPage<Map<String, Object>> result = new CommonPage<>();
        result.setPageNum(Long.valueOf(pageNum));
        result.setPageSize(Long.valueOf(pageSize));
        result.setTotal(100L); // 假设总数是100
        result.setTotalPage(10L); // 假设总页数是10
        result.setList(brandList);
        
        return CommonResult.success(result);
    }
    
    @ApiOperation("获取单个品牌")
    @GetMapping("/{id}")
    public CommonResult<Map<String, Object>> getBrand(@PathVariable Long id) {
        log.info("获取品牌详情, id={}", id);
        
        // 创建一个示例品牌数据
        Map<String, Object> brand = new HashMap<>();
        brand.put("id", id);
        brand.put("name", "品牌" + id);
        brand.put("firstLetter", (char)('A' + id.intValue() - 1));
        brand.put("sort", id.intValue());
        brand.put("factoryStatus", 1);
        brand.put("showStatus", 1);
        brand.put("productCount", 100 + id.intValue() * 10);
        brand.put("productCommentCount", 50 + id.intValue() * 5);
        brand.put("logo", "https://example.com/logo" + id + ".jpg");
        brand.put("bigPic", "https://example.com/bigPic" + id + ".jpg");
        brand.put("brandStory", "品牌故事" + id);
        
        return CommonResult.success(brand);
    }
    
    /**
     * 创建品牌
     */
    @PostMapping("/create")
    public CommonResult<Long> create(@RequestBody BrandParam param) {
        log.info("创建品牌: {}", param);
        
        // 返回模拟ID
        return CommonResult.success(101L);
    }
    
    /**
     * 更新品牌
     */
    @PostMapping("/update/{id}")
    public CommonResult<Integer> update(
            @PathVariable Long id,
            @RequestBody BrandParam param) {
        
        log.info("更新品牌, id={}, param={}", id, param);
        
        // 返回更新结果
        return CommonResult.success(1);
    }
    
    /**
     * 删除品牌
     */
    @GetMapping("/delete/{id}")
    public CommonResult<Integer> delete(@PathVariable Long id) {
        log.info("删除品牌, id={}", id);
        
        // 返回删除结果
        return CommonResult.success(1);
    }
    
    /**
     * 批量更新显示状态
     */
    @PostMapping("/update/showStatus")
    public CommonResult<Integer> updateShowStatus(@RequestBody Map<String, Object> params) {
        List<Long> ids = (List<Long>) params.get("ids");
        Integer showStatus = (Integer) params.get("showStatus");
        
        log.info("批量更新品牌显示状态, ids={}, showStatus={}", ids, showStatus);
        
        // 返回更新结果
        return CommonResult.success(ids.size());
    }
    
    /**
     * 批量更新厂商制造商状态
     */
    @PostMapping("/update/factoryStatus")
    public CommonResult<Integer> updateFactoryStatus(@RequestBody Map<String, Object> params) {
        List<Long> ids = (List<Long>) params.get("ids");
        Integer factoryStatus = (Integer) params.get("factoryStatus");
        
        log.info("批量更新品牌厂商状态, ids={}, factoryStatus={}", ids, factoryStatus);
        
        // 返回更新结果
        return CommonResult.success(ids.size());
    }
}
