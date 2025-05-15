package com.whaletide.admin.product.controller;


import com.whaletide.admin.product.dto.ProductCategoryAddDTO;
import com.whaletide.admin.product.dto.ProductCategoryUpdateDTO;
import com.whaletide.admin.product.service.IProductCategoryService;
import com.whaletide.admin.product.vo.ProductCategoryListVO;
import com.whaletide.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品分类控制器
 */

@Slf4j
@RestController
@RequestMapping("/category")
@Tag(name = "商品分类相关接口")
public class ProductCategoryController {

    @Autowired
    private IProductCategoryService productCategoryService;

    /**
     * 获取商品分类列表
     */
    @Operation(summary = "获取商品分类列表")
    @GetMapping("/list")
    public CommonResult<List<ProductCategoryListVO>> getCategoryList() {
        List<ProductCategoryListVO> categoryList = productCategoryService.getProductCategoryList();

        return CommonResult.success(categoryList);
    }

    /**
     * 添加商品分类信息
     */
    @Operation(summary = "添加商品分类信息")
    @PostMapping("/add")
    public CommonResult addCategory(@RequestBody ProductCategoryAddDTO productCategoryAddDTO) {
        int result = productCategoryService.addProductCategory(productCategoryAddDTO);
        if (result == 1) {
            return CommonResult.success(1);
        } else if (result == -1) {
            return CommonResult.failed("分类名称已存在");
        } else {
            return CommonResult.failed("添加失败");
        }
    }

    /**
     * 更新商品分类信息
     */
    @Operation(summary = "更新商品分类信息")
    @PutMapping("/update")
    public CommonResult updateCategory(@RequestBody ProductCategoryUpdateDTO productCategoryUpdateDTO) {
        int result = productCategoryService.updateProductCategory(productCategoryUpdateDTO);
        if (result >= 1) {
            return CommonResult.success("更新成功");
        } else if (result == -1) {
            return CommonResult.failed("分类不存在");
        } else if (result == -2) {
            return CommonResult.failed("分类名称已存在");
        } else {
            return CommonResult.failed("更新失败");
        }
    }

    /**
     * 删除商品分类信息
     */
    @Operation(summary = "删除商品分类信息")
    @DeleteMapping("/delete/{id}")
    public CommonResult deleteCategory(@PathVariable("id") Long id) {
        int result = productCategoryService.deleteProductCategory(id);
        if (result == 1) {
            return CommonResult.success("删除成功");
        } else if (result == -1) {
            return CommonResult.failed("分类不存在");
        } else {
            return CommonResult.failed("删除失败");
        }
    }

}
