package com.whale_tide.controller;

import com.whale_tide.common.api.CommonPage;
import com.whale_tide.common.api.CommonResult;
import com.whale_tide.entity.CmsSubject;
import com.whale_tide.mapper.CmsSubjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 专题管理Controller
 */
@Slf4j
@RestController
@Api(tags = "SubjectController", description = "专题管理")
@RequestMapping("/subject")
public class SubjectController {

    @Autowired
    private CmsSubjectMapper subjectMapper;

    @ApiOperation("获取专题列表")
    @GetMapping("/list")
    public CommonResult<CommonPage<CmsSubject>> list(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        
        log.info("获取专题列表, keyword={}, pageNum={}, pageSize={}", keyword, pageNum, pageSize);
        
        // 从数据库查询专题列表
        List<CmsSubject> subjectList = subjectMapper.selectList(null);
        if (subjectList == null) {
            subjectList = new ArrayList<>();
        }
        
        // 创建CommonPage对象
        CommonPage<CmsSubject> result = new CommonPage<>();
        result.setPageNum(Long.valueOf(pageNum));
        result.setPageSize(Long.valueOf(pageSize));
        result.setTotal((long) subjectList.size());
        result.setTotalPage((long) Math.ceil((double) subjectList.size() / pageSize));
        
        // 分页处理
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, subjectList.size());
        if (startIndex < subjectList.size()) {
            result.setList(subjectList.subList(startIndex, endIndex));
        } else {
            result.setList(new ArrayList<>());
        }
        
        return CommonResult.success(result);
    }
    
    @ApiOperation("获取全部专题")
    @GetMapping("/listAll")
    public CommonResult<List<CmsSubject>> listAll() {
        log.info("获取全部专题");
        
        // 从数据库查询所有专题
        List<CmsSubject> subjectList = subjectMapper.selectList(null);
        if (subjectList == null) {
            subjectList = new ArrayList<>();
        }
        
        return CommonResult.success(subjectList);
    }
} 