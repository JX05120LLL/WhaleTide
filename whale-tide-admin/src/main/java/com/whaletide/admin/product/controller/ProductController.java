package com.whaletide.admin.product.controller;

import com.whaletide.admin.product.dto.ProductAddDTO;
import com.whaletide.admin.product.dto.ProductListDTO;
import com.whaletide.admin.product.dto.ProductUpdataStatusDTO;
import com.whaletide.admin.product.dto.ProductUpdateDTO;
import com.whaletide.admin.product.service.IProductService;
import com.whaletide.admin.product.vo.ProductDetailOV;
import com.whaletide.admin.product.vo.ProductListItemVO;
import com.whaletide.admin.product.vo.ProductListVO;
import com.whaletide.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品控制器
 */
@Slf4j
@RestController
@RequestMapping("/product")
@Tag(name = "商品相关接口")
public class ProductController {

    @Autowired
    private IProductService productService;


    /**
     * 商品列表
     */
    @Operation(summary = "商品列表")
    @GetMapping("/list")
    public CommonResult<ProductListVO> getProductList(@RequestParam(defaultValue = "1") int pageNum,
                                                      @RequestParam(defaultValue = "10") int pageSize,
                                                      @RequestParam(required = false) String keyword,
                                                      @RequestParam(required = false, defaultValue = "") String status) {
        ProductListDTO productListDTO = new ProductListDTO();
        productListDTO.setPage(Long.valueOf(pageNum));
        productListDTO.setPageSize(Long.valueOf(pageSize));
        productListDTO.setKeyword(keyword);

        if (status.equals("true"))
            productListDTO.setStatus(1);
        else if (status.equals("false"))
            productListDTO.setStatus(0);
        else
            productListDTO.setStatus(null);

        ProductListVO productList = productService.getProductListByKeyword(productListDTO);
        return CommonResult.success(productList);
    }

    /**
     * 获取商品详情
     */
    @Operation(summary = "获取商品详情")
    @GetMapping("/detail/{id}")
    public CommonResult<ProductDetailOV> getProductDetail(@PathVariable("id") Long id) {
        ProductDetailOV productDetail = productService.getProductDetail(id);
        return CommonResult.success(productDetail);
    }

    /**
     * 添加商品
     */
    @Operation(summary = "添加商品")
    @PostMapping("/add")
    public CommonResult addProduct(@RequestBody ProductAddDTO productAddDTO) {
        Integer result = productService.addProduct(productAddDTO);
        if (result == 1)
            return CommonResult.success("商品添加成功");
        else
            return CommonResult.failed();
    }

    /**
     * 更新商品
     */
    @Operation(summary = "更新商品")
    @PutMapping("/update")
    public CommonResult updateProduct(@RequestBody ProductUpdateDTO productUpdateDTO) {
        Integer result = productService.updateProduct(productUpdateDTO);
        if (result >= 1)
            return CommonResult.success("商品更新成功");
        else if (result == 0)
            return CommonResult.failed("没有任何更新");
        else if (result == -1)
            return CommonResult.failed("商品不存在");
        else
            return CommonResult.failed("????");
    }

    /**
     * 更改商品上架状态
     */
    @Operation(summary = "更改商品上架状态")
    @PutMapping("/updateStatus")
    public CommonResult updateProductStatus(@RequestBody ProductUpdataStatusDTO productUpdataStatusDTO) {
        int result = productService.updateProductStatus(productUpdataStatusDTO);
        if (result >= 1)
            return CommonResult.success("商品上架状态更新成功");
        else if (result == 0)
            return CommonResult.failed("没有任何更新");
        else if (result == -1)
            return CommonResult.failed("商品不存在");
        else
            return CommonResult.failed("????");
    }

    /**
     * 删除商品
     */
    @Operation(summary = "删除商品")
    @DeleteMapping("/delete/{id}")
    public CommonResult deleteProduct(@PathVariable("id") Long id) {
        int result = productService.deleteProduct(id);
        if (result >= 1)
            return CommonResult.success("商品删除成功");
        else if (result == 0)
            return CommonResult.failed("商品不存在");
        else
            return CommonResult.failed("????");
    }

}