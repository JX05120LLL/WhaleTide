package com.whale_tide.controller.management.brand;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale_tide.common.api.CommonPage;
import com.whale_tide.common.api.CommonResult;
import com.whale_tide.dto.management.brand.BrandParam;
import com.whale_tide.entity.pms.PmsBrands;
import com.whale_tide.service.management.IBrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 品牌管理Controller
 */
@Slf4j
@RestController("managementBrandController")
@Api(tags = "BrandController", description = "品牌管理")
@RequestMapping("/brand")
public class BrandController {

    @Autowired
    private IBrandService brandService;

    @ApiOperation("获取品牌列表")
    @GetMapping("/list")
    public CommonResult<CommonPage<Map<String, Object>>> getBrandList(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        
        log.info("获取品牌列表, keyword={}, pageNum={}, pageSize={}", keyword, pageNum, pageSize);
        
        try {
            // 使用服务层方法获取真实数据
            Page<PmsBrands> brandPage = brandService.listBrand(keyword, pageNum, pageSize);
            
            // 转换为前端需要的数据格式
            List<Map<String, Object>> brandList = brandPage.getRecords().stream()
                .map(this::convertBrandToMap)
                .collect(Collectors.toList());
            
            // 创建CommonPage对象
            CommonPage<Map<String, Object>> result = new CommonPage<>();
            result.setPageNum(brandPage.getCurrent());
            result.setPageSize(brandPage.getSize());
            result.setTotal(brandPage.getTotal());
            result.setTotalPage(brandPage.getPages());
            result.setList(brandList);
            
            return CommonResult.success(result);
        } catch (Exception e) {
            log.error("获取品牌列表失败", e);
            return CommonResult.failed("获取品牌列表失败: " + e.getMessage());
        }
    }
    
    @ApiOperation("获取单个品牌")
    @GetMapping("/{id}")
    public CommonResult<Map<String, Object>> getBrand(@PathVariable Long id) {
        log.info("获取品牌详情, id={}", id);
        
        try {
            // 使用服务层方法获取真实数据
            PmsBrands brand = brandService.getBrand(id);
            
            if (brand != null) {
                return CommonResult.success(convertBrandToMap(brand));
            } else {
                return CommonResult.failed("品牌不存在");
            }
        } catch (Exception e) {
            log.error("获取品牌详情失败", e);
            return CommonResult.failed("获取品牌详情失败: " + e.getMessage());
        }
    }
    
    /**
     * 创建品牌
     */
    @PostMapping("/create")
    public CommonResult<Long> create(@RequestBody BrandParam param) {
        log.info("创建品牌: {}", param);
        
        try {
            Long id = brandService.createBrand(param);
            if (id > 0) {
                return CommonResult.success(id);
            } else {
                return CommonResult.failed("创建品牌失败");
            }
        } catch (Exception e) {
            log.error("创建品牌失败", e);
            return CommonResult.failed("创建品牌失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新品牌
     */
    @PostMapping("/update/{id}")
    public CommonResult<Integer> update(
            @PathVariable Long id,
            @RequestBody BrandParam param) {
        
        log.info("更新品牌, id={}, param={}", id, param);
        
        try {
            int count = brandService.updateBrand(id, param);
            if (count > 0) {
                return CommonResult.success(count);
            } else {
                return CommonResult.failed("更新品牌失败");
            }
        } catch (Exception e) {
            log.error("更新品牌失败", e);
            return CommonResult.failed("更新品牌失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除品牌
     */
    @GetMapping("/delete/{id}")
    public CommonResult<Integer> delete(@PathVariable Long id) {
        log.info("删除品牌, id={}", id);
        
        try {
            int count = brandService.deleteBrand(id);
            if (count > 0) {
                return CommonResult.success(count);
            } else {
                return CommonResult.failed("删除品牌失败");
            }
        } catch (Exception e) {
            log.error("删除品牌失败", e);
            return CommonResult.failed("删除品牌失败: " + e.getMessage());
        }
    }
    
    /**
     * 批量更新显示状态
     */
    @PostMapping(value = "/update/showStatus", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public CommonResult<Integer> updateShowStatus(
            @RequestParam("ids") String ids,
            @RequestParam("showStatus") Integer showStatus) {
        try {
            log.info("批量更新品牌显示状态, ids={}, showStatus={}", ids, showStatus);
            
            List<Long> idList = new ArrayList<>();
            if (ids.contains(",")) {
                String[] idArray = ids.split(",");
                for (String id : idArray) {
                    if (!id.trim().isEmpty()) {
                        idList.add(Long.valueOf(id.trim()));
                    }
                }
            } else {
                idList.add(Long.valueOf(ids));
            }
            
            if (idList.isEmpty()) {
                return CommonResult.failed("缺少id参数");
            }
            
            int count = brandService.updateShowStatus(idList, showStatus);
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
    
    /**
     * 批量更新厂家状态
     */
    @PostMapping(value = "/update/factoryStatus", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public CommonResult<Integer> updateFactoryStatus(
            @RequestParam("ids") String ids,
            @RequestParam("factoryStatus") Integer factoryStatus) {
        try {
            log.info("批量更新品牌厂家状态, ids={}, factoryStatus={}", ids, factoryStatus);
            
            List<Long> idList = new ArrayList<>();
            if (ids.contains(",")) {
                String[] idArray = ids.split(",");
                for (String id : idArray) {
                    if (!id.trim().isEmpty()) {
                        idList.add(Long.valueOf(id.trim()));
                    }
                }
            } else {
                idList.add(Long.valueOf(ids));
            }
            
            if (idList.isEmpty()) {
                return CommonResult.failed("缺少id参数");
            }
            
            int count = brandService.updateFactoryStatus(idList, factoryStatus);
            if (count > 0) {
                return CommonResult.success(count);
            } else {
                return CommonResult.failed("更新厂家状态失败");
            }
        } catch (Exception e) {
            log.error("更新厂家状态失败", e);
            return CommonResult.failed("更新厂家状态失败: " + e.getMessage());
        }
    }
    
    /**
     * 初始化品牌数据（仅供测试使用）
     */
    @ApiOperation("初始化品牌数据")
    @PostMapping("/init")
    public CommonResult<String> initData() {
        log.info("初始化品牌数据");
        
        try {
            boolean result = brandService.initData();
            if (result) {
                return CommonResult.success("初始化品牌数据成功");
            } else {
                return CommonResult.failed("初始化品牌数据失败");
            }
        } catch (Exception e) {
            log.error("初始化品牌数据失败", e);
            return CommonResult.failed("初始化品牌数据失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新单个品牌的显示状态
     */
    @ApiOperation("更新显示状态")
    @PostMapping("/{id}/showStatus/{status}")
    public CommonResult<Integer> updateShowStatus(
            @PathVariable Long id,
            @PathVariable Integer status) {
        
        log.info("更新品牌显示状态, id={}, status={}", id, status);
        
        try {
            List<Long> ids = Collections.singletonList(id);
            int count = brandService.updateShowStatus(ids, status);
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

    /**
     * 更新单个品牌的厂家状态
     */
    @ApiOperation("更新厂家状态")
    @PostMapping("/{id}/factoryStatus/{status}")
    public CommonResult<Integer> updateFactoryStatus(
            @PathVariable Long id,
            @PathVariable Integer status) {
        
        log.info("更新品牌厂家状态, id={}, status={}", id, status);
        
        try {
            List<Long> ids = Collections.singletonList(id);
            int count = brandService.updateFactoryStatus(ids, status);
            if (count > 0) {
                return CommonResult.success(count);
            } else {
                return CommonResult.failed("更新厂家状态失败");
            }
        } catch (Exception e) {
            log.error("更新厂家状态失败", e);
            return CommonResult.failed("更新厂家状态失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新单个品牌的显示状态 (GET请求)
     */
    @ApiOperation("更新显示状态(GET)")
    @GetMapping("/{id}/showStatus/{status}")
    public CommonResult<Integer> updateShowStatusGet(
            @PathVariable Long id,
            @PathVariable Integer status) {
        
        return updateShowStatus(id, status);
    }

    /**
     * 更新单个品牌的厂家状态 (GET请求)
     */
    @ApiOperation("更新厂家状态(GET)")
    @GetMapping("/{id}/factoryStatus/{status}")
    public CommonResult<Integer> updateFactoryStatusGet(
            @PathVariable Long id,
            @PathVariable Integer status) {
        
        return updateFactoryStatus(id, status);
    }
    
    /**
     * 将PmsBrands对象转换为Map
     */
    private Map<String, Object> convertBrandToMap(PmsBrands brand) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", brand.getId());
        result.put("name", brand.getName());
        result.put("firstLetter", brand.getFirstLetter());
        result.put("sort", brand.getSort());
        result.put("factoryStatus", brand.getIsFeatured());
        result.put("showStatus", brand.getStatus());
        // 以下是可能为空的值，使用可能的替代值
        result.put("productCount", 0); // 实际应该从product表中查询
        result.put("productCommentCount", 0); // 实际应该从comment表中查询
        result.put("logo", brand.getLogo());
        result.put("bigPic", brand.getLogo()); // 使用logo作为替代
        result.put("brandStory", brand.getDescription());
        
        return result;
    }
}

