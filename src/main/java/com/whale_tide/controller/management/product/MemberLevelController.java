package com.whale_tide.controller.management.product;

import com.whale_tide.common.api.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 会员等级管理Controller
 */
@Slf4j
@RestController
@Api(tags = "MemberLevelController", description = "会员等级管理")
@RequestMapping("/memberLevel")
public class MemberLevelController {

    @ApiOperation("获取会员等级列表")
    @GetMapping("/list")
    public CommonResult<List<Map<String, Object>>> list(@RequestParam(value = "defaultStatus", required = false) Integer defaultStatus) {
        log.info("获取会员等级列表, defaultStatus={}", defaultStatus);
        
        // 返回硬编码的会员等级列表
        List<Map<String, Object>> memberLevelList = new ArrayList<>();
        
        // 黄金会员
        Map<String, Object> goldMember = new HashMap<>();
        goldMember.put("id", 1);
        goldMember.put("name", "黄金会员");
        goldMember.put("growthPoint", 1000);
        goldMember.put("defaultStatus", 0);
        goldMember.put("freeFreightPoint", 199);
        goldMember.put("commentGrowthPoint", 5);
        goldMember.put("priviledgeFreeFreight", 1);
        goldMember.put("priviledgeSignIn", 1);
        goldMember.put("priviledgeComment", 1);
        goldMember.put("priviledgePromotion", 1);
        goldMember.put("priviledgeMemberPrice", 1);
        goldMember.put("priviledgeBirthday", 1);
        memberLevelList.add(goldMember);
        
        // 白金会员
        Map<String, Object> platinumMember = new HashMap<>();
        platinumMember.put("id", 2);
        platinumMember.put("name", "白金会员");
        platinumMember.put("growthPoint", 5000);
        platinumMember.put("defaultStatus", 0);
        platinumMember.put("freeFreightPoint", 99);
        platinumMember.put("commentGrowthPoint", 10);
        platinumMember.put("priviledgeFreeFreight", 1);
        platinumMember.put("priviledgeSignIn", 1);
        platinumMember.put("priviledgeComment", 1);
        platinumMember.put("priviledgePromotion", 1);
        platinumMember.put("priviledgeMemberPrice", 1);
        platinumMember.put("priviledgeBirthday", 1);
        memberLevelList.add(platinumMember);
        
        // 钻石会员
        Map<String, Object> diamondMember = new HashMap<>();
        diamondMember.put("id", 3);
        diamondMember.put("name", "钻石会员");
        diamondMember.put("growthPoint", 15000);
        diamondMember.put("defaultStatus", 0);
        diamondMember.put("freeFreightPoint", 0);
        diamondMember.put("commentGrowthPoint", 15);
        diamondMember.put("priviledgeFreeFreight", 1);
        diamondMember.put("priviledgeSignIn", 1);
        diamondMember.put("priviledgeComment", 1);
        diamondMember.put("priviledgePromotion", 1);
        diamondMember.put("priviledgeMemberPrice", 1);
        diamondMember.put("priviledgeBirthday", 1);
        memberLevelList.add(diamondMember);
        
        return CommonResult.success(memberLevelList);
    }
} 