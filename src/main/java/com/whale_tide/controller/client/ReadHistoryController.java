package com.whale_tide.controller.client;

import com.whale_tide.common.api.CommonResult;
import com.whale_tide.common.api.PageRequest;
import com.whale_tide.common.api.PageResponse;
import com.whale_tide.dto.client.readHistory.ReadHistoryCreateRequest;
import com.whale_tide.dto.client.readHistory.ReadHistoryDeleteRequest;
import com.whale_tide.dto.client.readHistory.ReadHistoryResponse;
import com.whale_tide.service.client.IReadHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 浏览历史记录控制器
 */
@Slf4j
@RestController
@Api(tags = "浏览历史相关接口")
@RequestMapping("/member/readHistory")
public class ReadHistoryController {

    @Autowired
    private IReadHistoryService readHistoryService;

    @ApiOperation("获取搜索历史记录")
    @GetMapping("/list")
    public CommonResult<PageResponse<ReadHistoryResponse>> getList(PageRequest pageRequest) {
        log.info("获取浏览历史记录, pageNum={}, pageSize={}", pageRequest.getPageNum(), pageRequest.getPageSize());
        try {
            PageResponse<ReadHistoryResponse> response = readHistoryService.getList(pageRequest);
            return CommonResult.success(response);
        } catch (Exception e) {
            log.error("获取浏览历史记录失败", e);
            return CommonResult.failed("获取浏览历史记录失败: " + e.getMessage());
        }
    }

    @ApiOperation("添加浏览历史记录")
    @PostMapping("/create")
    public CommonResult<Void> create(@Valid @RequestBody ReadHistoryCreateRequest request) {
        log.info("添加浏览历史记录, ProductId={}, ProductName={}", request.getProductId(), request.getProductName());
        try {
            readHistoryService.create(request);
            return CommonResult.success(null, "添加成功");
        } catch (Exception e) {
            log.error("添加浏览历史记录失败", e);
            return CommonResult.failed("添加浏览历史记录失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除浏览历史记录")
    @PostMapping("/delete")
    public CommonResult<Void> delete(@Valid @RequestBody ReadHistoryDeleteRequest request) {
        log.info("删除浏览历史记录, ids={}", request.getIds());
        try {
            readHistoryService.delete(request);
            return CommonResult.success(null, "删除成功");
        } catch (Exception e) {
            log.error("删除浏览历史记录失败", e);
            return CommonResult.failed("删除浏览历史记录失败: " + e.getMessage());
        }
    }

    @ApiOperation("清空浏览历史记录")
    @PostMapping("/clear")
    public CommonResult<Void> clear() {
        log.info("清空浏览历史记录");
        try {
            readHistoryService.clear();
            return CommonResult.success(null, "清空成功");
        } catch (Exception e) {
            log.error("清空浏览历史记录失败", e);
            return CommonResult.failed("清空浏览历史记录失败: " + e.getMessage());
        }
    }
} 