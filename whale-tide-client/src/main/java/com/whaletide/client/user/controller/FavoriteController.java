package com.whaletide.client.user.controller;

import com.whaletide.client.user.dto.FavoriteAddDTO;
import com.whaletide.client.user.service.IFavoriteService;
import com.whaletide.client.user.vo.FavoriteVO;
import com.whaletide.common.api.CommonPage;
import com.whaletide.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户收藏控制器
 */
@RestController
@RequestMapping("/api/favorite")
@Tag(name = "用户收藏管理", description = "用户收藏相关接口")
public class FavoriteController {

    @Autowired
    private IFavoriteService favoriteService;

    @PostMapping("/add")
    @Operation(summary = "添加收藏")
    public CommonResult<Boolean> add(@RequestBody FavoriteAddDTO favoriteAddDTO) {
        return CommonResult.success(favoriteService.add(favoriteAddDTO));
    }

    @DeleteMapping("/delete/{productId}")
    @Operation(summary = "取消收藏")
    public CommonResult<Boolean> delete(@PathVariable("productId") Long productId) {
        return CommonResult.success(favoriteService.delete(productId));
    }

    @GetMapping("/check/{productId}")
    @Operation(summary = "检查是否已收藏")
    public CommonResult<Boolean> checkFavorite(@PathVariable("productId") Long productId) {
        return CommonResult.success(favoriteService.checkFavorite(productId));
    }

    @GetMapping("/list")
    @Operation(summary = "获取收藏列表")
    public CommonResult<CommonPage<FavoriteVO>> list(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return CommonResult.success(favoriteService.list(pageNum, pageSize));
    }
} 