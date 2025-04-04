package com.whale_tide.controller.client;

import com.whale_tide.common.api.CommonResult;
import com.whale_tide.dto.client.cart.*;

import com.whale_tide.service.client.ICartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
/**
 *购物车管理
 */
@RestController
@Api(tags = "购物车相关接口")
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private ICartService cartService;

    @ApiOperation("添加购物车")
    @PostMapping("/add")
    public CommonResult<Integer> cartAdd(@RequestBody CartAddRequest request){
        int i = cartService.cartAdd(request);
        return CommonResult.success(i);

    }

    @ApiOperation("获取购物车列表")
    @GetMapping("/list")
    public CommonResult<CartListResponse> getCartList(){
        CartListResponse cartList = cartService.getCartList();
        return CommonResult.success(cartList);
    }

    @ApiOperation("修改购物车中商品数量")
    @PostMapping("/update/quantity")
    public CommonResult<Void> cartUpdateQuantity(@RequestBody CartUpdateQuantityRequest request){
        try {
            cartService.cartUpdateQuantity(request);
            return CommonResult.success(null);
        } catch (Exception e) {
            return CommonResult.failed("更新购物车数量失败: " + e.getMessage());
        }
    }

    @ApiOperation("修改购物车中商品选中状态")
    @PostMapping("/update/checked")
    public CommonResult<Void> cartUpdateChecked(@RequestBody CartUpdateCheckedRequest request){
        cartService.cartUpdateChecked(request);
        return CommonResult.success(null);
    }
    @ApiOperation("删除购物车中商品")
    @PostMapping("/delete")
    public CommonResult<Void> cartDelete(@RequestBody CartDeleteRequest request){
        cartService.cartDelete(request);
        return CommonResult.success(null);
    }
    @ApiOperation("清空购物车")
    @PostMapping("/clear")
    public CommonResult<Void> cartClear(){
        cartService.cartClear();
        return CommonResult.success(null);
    }

    @ApiOperation("获取购物车中商品数量")
    @GetMapping("/count")
    public CommonResult<Integer> getCartProductCount(){
        int count = cartService.getCartProductCount();
        return CommonResult.success(count);
    }

}
