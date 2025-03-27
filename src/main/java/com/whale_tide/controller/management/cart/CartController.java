package com.whale_tide.controller.management.cart;

import com.whale_tide.common.api.CommonResult;
import com.whale_tide.dto.management.cart.CartAddRequest;

import com.whale_tide.dto.management.cart.CartDeleteRequest;
import com.whale_tide.dto.management.cart.CartUpdateCheckedRequest;
import com.whale_tide.dto.management.cart.CartUpdateQuantityRequest;
import com.whale_tide.dto.management.product.UpdateDeleteStatusParam;
import com.whale_tide.service.management.ICartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
/**
 *购物车管理
 */
@RestController
@Api(tags = "CartController",description = "购物车管理")
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private ICartService cartService;

    @ApiOperation("添加购物车")
    @PostMapping("/add")
    public CommonResult<Long> cartAdd(@RequestBody CartAddRequest request){
        return CommonResult.success(cartService.cartAdd(request));

    }
    @ApiOperation("修改购物车中商品数量")
    @PostMapping("/update/quantity")
    public CommonResult<Integer> cartUpdateQuantity(@RequestBody CartUpdateQuantityRequest request){
        return CommonResult.success(cartService.cartUpdateQuantity(request));
    }
    @ApiOperation("修改购物车中商品选中状态")
    @PostMapping("/update/checked")
    public CommonResult<Integer> cartUpdateChecked(@RequestBody CartUpdateCheckedRequest request){
        return CommonResult.success(cartService.cartUpdateChecked(request));

    }
    @ApiOperation("删除购物车中商品")
    @PostMapping("/delete")
    public CommonResult<Integer> cartDelete(@RequestBody CartDeleteRequest request){
        return CommonResult.success(cartService.cartDelete(request));
    }
    @ApiOperation("清空购物车")
    @PostMapping("/clear")
    public CommonResult<Integer> cartClear(){
        return CommonResult.success(cartService.cartClear());
    }

}
