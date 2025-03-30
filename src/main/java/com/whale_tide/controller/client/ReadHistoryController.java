package com.whale_tide.controller.client;

import com.whale_tide.common.api.CommonResult;
import com.whale_tide.common.api.PageRequest;
import com.whale_tide.common.api.PageResponse;
import com.whale_tide.dto.client.history.ReadHistoryCreateRequest;
import com.whale_tide.dto.client.history.ReadHistoryDeleteRequest;
import com.whale_tide.dto.client.history.ReadHistoryResponse;
import com.whale_tide.service.client.IHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 搜索历史记录控制器
 */
@Slf4j
@RestController
@Api(tags = "搜索历史相关接口")
@RequestMapping("/member/readHistory")
public class ReadHistoryController {

    @Autowired
    private IHistoryService historyService;

    @ApiOperation("获取搜索历史记录")
    @GetMapping("/list")
    public CommonResult<PageResponse<ReadHistoryResponse>> getList(PageRequest pageRequest) {
        log.info("获取搜索历史记录, pageNum={}, pageSize={}", pageRequest.getPageNum(), pageRequest.getPageSize());
        try {
            PageResponse<ReadHistoryResponse> response = historyService.getList(pageRequest);
            return CommonResult.success(response);
        } catch (Exception e) {
            log.error("获取搜索历史记录失败", e);
            return CommonResult.failed("获取搜索历史记录失败: " + e.getMessage());
        }
    }

    @ApiOperation("添加搜索历史记录")
    @PostMapping("/create")
    public CommonResult<Void> create(@Valid @RequestBody ReadHistoryCreateRequest request) {
        log.info("添加搜索历史记录, keyword={}", request.getKeyword());
        try {
            historyService.create(request);
            return CommonResult.success(null, "添加成功");
        } catch (Exception e) {
            log.error("添加搜索历史记录失败", e);
            return CommonResult.failed("添加搜索历史记录失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除搜索历史记录")
    @PostMapping("/delete")
    public CommonResult<Void> delete(@Valid @RequestBody ReadHistoryDeleteRequest request) {
        log.info("删除搜索历史记录, ids={}", request.getIds());
        try {
            historyService.delete(request);
            return CommonResult.success(null, "删除成功");
        } catch (Exception e) {
            log.error("删除搜索历史记录失败", e);
            return CommonResult.failed("删除搜索历史记录失败: " + e.getMessage());
        }
    }

    @ApiOperation("清空搜索历史记录")
    @PostMapping("/clear")
    public CommonResult<Void> clear() {
        log.info("清空搜索历史记录");
        try {
            historyService.clear();
            return CommonResult.success(null, "清空成功");
        } catch (Exception e) {
            log.error("清空搜索历史记录失败", e);
            return CommonResult.failed("清空搜索历史记录失败: " + e.getMessage());
        }
    }
} 