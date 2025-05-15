package com.whaletide.client.product.controller;

import com.whaletide.client.product.service.IHistoryService;
import com.whaletide.client.product.vo.ProductHistoryVO;
import com.whaletide.common.api.CommonPage;
import com.whaletide.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 浏览历史控制器
 */
@RestController
@RequestMapping("/api/history")
@Tag(name = "浏览历史管理", description = "浏览历史相关接口")
public class HistoryController {

    private final IHistoryService historyService;

    @Autowired
    public HistoryController(IHistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/list")
    @Operation(summary = "获取浏览历史列表")
    public CommonResult<CommonPage<ProductHistoryVO>> list(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return CommonResult.success(historyService.list(pageNum, pageSize));
    }

    @DeleteMapping("/clear")
    @Operation(summary = "清空浏览历史")
    public CommonResult<Boolean> clear() {
        return CommonResult.success(historyService.clear());
    }

    @DeleteMapping("/delete/{ids}")
    @Operation(summary = "删除指定浏览历史")
    public CommonResult<Boolean> delete(@PathVariable("ids") String ids) {
        return CommonResult.success(historyService.delete(ids));
    }

    @PostMapping("/add")
    @Operation(summary = "添加浏览历史")
    public CommonResult<Boolean> add(@RequestParam Long productId) {
        return CommonResult.success(historyService.add(productId));
    }

    @GetMapping("/search")
    @Operation(summary = "获取搜索历史列表")
    public CommonResult<List<String>> getSearchHistory(
            @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        return CommonResult.success(historyService.getSearchHistory(limit));
    }

    @PostMapping("/search/add")
    @Operation(summary = "添加搜索历史")
    public CommonResult<Boolean> addSearchHistory(@RequestParam String keyword) {
        return CommonResult.success(historyService.addSearchHistory(keyword));
    }

    @DeleteMapping("/search/clear")
    @Operation(summary = "清空搜索历史")
    public CommonResult<Boolean> clearSearchHistory() {
        return CommonResult.success(historyService.clearSearchHistory());
    }
} 