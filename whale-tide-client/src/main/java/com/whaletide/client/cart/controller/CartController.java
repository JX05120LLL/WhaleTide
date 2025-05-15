package com.whaletide.client.cart.controller;

import com.whaletide.client.cart.dto.CartAddDTO;
import com.whaletide.client.cart.dto.CartUpdateDTO;
import com.whaletide.client.cart.service.ICartService;
import com.whaletide.client.cart.vo.CartItemVO;
import com.whaletide.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购物车控制器
 */
@RestController
@RequestMapping("/api/cart")
@Tag(name = "购物车管理", description = "购物车相关接口")
public class CartController {

    @Autowired
    private ICartService cartService;

    @PostMapping("/add")
    @Operation(summary = "添加商品到购物车")
    public CommonResult<Boolean> add(@RequestBody CartAddDTO cartAddDTO) {
        Boolean add = cartService.add(cartAddDTO);
        return CommonResult.success(add);
    }

    @GetMapping("/list")
    @Operation(summary = "获取购物车列表")
    public CommonResult<List<CartItemVO>> list() {
        List<CartItemVO> list = cartService.list();
        return CommonResult.success(list);
    }

    @PutMapping("/update/quantity")
    @Operation(summary = "修改购物车商品数量")
    public CommonResult<Boolean> updateQuantity(@RequestBody CartUpdateDTO cartUpdateDTO) {
        cartService.updateQuantity(cartUpdateDTO);
        return CommonResult.success(true);
    }

    @PutMapping("/update/checked")
    @Operation(summary = "修改购物车商品选中状态")
    public CommonResult<Boolean> updateChecked(@RequestBody CartUpdateDTO cartUpdateDTO) {
        Boolean b = cartService.updateChecked(cartUpdateDTO);
        return CommonResult.success(b);
    }

    @DeleteMapping("/delete/{ids}")
    @Operation(summary = "删除购物车商品")
    public CommonResult<Boolean> delete(@PathVariable("ids") List<Long> ids) {
        Boolean delete = cartService.delete(ids);
        return CommonResult.success(delete);
    }

    @DeleteMapping("/clear")
    @Operation(summary = "清空购物车")
    public CommonResult<Boolean> clear() {
        Boolean clear = cartService.clear();
        return CommonResult.success(clear);
    }

    @GetMapping("/count")
    @Operation(summary = "获取购物车商品数量")
    public CommonResult<Integer> count() {
        Integer count = cartService.count();
        return CommonResult.success(count);
    }
} 