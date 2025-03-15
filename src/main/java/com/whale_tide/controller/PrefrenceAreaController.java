package com.whale_tide.controller;

import com.whale_tide.common.api.CommonResult;
import com.whale_tide.entity.CmsPrefrenceArea;
import com.whale_tide.mapper.CmsPrefrenceAreaMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 优选专区管理Controller
 */
@Slf4j
@RestController
@Api(tags = "PrefrenceAreaController", description = "优选专区管理")
@RequestMapping("/prefrenceArea")
public class PrefrenceAreaController {

    @Autowired
    private CmsPrefrenceAreaMapper prefrenceAreaMapper;

    @ApiOperation("获取优选专区列表")
    @GetMapping("/list")
    public CommonResult<List<CmsPrefrenceArea>> list() {
        log.info("获取优选专区列表");
        
        // 从数据库查询所有优选专区
        List<CmsPrefrenceArea> prefrenceAreaList = prefrenceAreaMapper.selectList(null);
        if (prefrenceAreaList == null) {
            prefrenceAreaList = new ArrayList<>();
        }
        
        return CommonResult.success(prefrenceAreaList);
    }
} 