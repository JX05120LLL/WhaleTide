package com.whale_tide.controller.client;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale_tide.common.api.CommonResult;
import com.whale_tide.dto.client.brandAttention.*;
import com.whale_tide.entity.ums.UmsUserBrandAttentions;
import com.whale_tide.service.client.IBrandAttentionService;
import com.whale_tide.service.client.IUserService;
import com.whale_tide.util.JwtUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Api(tags = "用户品牌关注接口")
@RestController("clientBrandAttentionController")
@RequestMapping("/member/attention")
public class BrandAttentionController {

    @Autowired
    private IBrandAttentionService memberService;

    @Autowired
    private IUserService userService;

    @Autowired
    private JwtUtil jwtUtil;



    /**
     * 关注品牌
     */
    @RequestMapping("/add")
    public CommonResult addBrandAttention(@RequestBody BrandAttentionRequest request, @RequestHeader("Authorization") String token) {
        UmsUserBrandAttentions userBrandAttentions = new UmsUserBrandAttentions();
        userBrandAttentions.setUserId(userService.getUserInfo(jwtUtil.getUsernameFromToken(token)).getId());
        userBrandAttentions.setBrandId(request.getBrandId());
        userBrandAttentions.setBrandName(request.getBrandName());
        userBrandAttentions.setBrandLogo(request.getBrandLogo());
        userBrandAttentions.setNote(request.getBrandDesc());
        userBrandAttentions.setCreateTime(LocalDateTime.now());
        userBrandAttentions.setUpdateTime(LocalDateTime.now());

        int result = memberService.addBrandAttention(userBrandAttentions);

        if (result == 1) {
            return CommonResult.success(null);
        } else if (result == -1) {
            return CommonResult.failed("参数错误");
        } else {
            return CommonResult.failed();
        }
    }

    /**
     * 取消关注品牌
     */
    @RequestMapping("/delete")
    public CommonResult deleteBrandAttention(@RequestParam Long brandId, @RequestHeader("Authorization") String token) {
        int result = memberService.deleteBrandAttention(userService.getUserInfo(jwtUtil.getUsernameFromToken(token)).getId(), brandId);

        if (result > 0) {
            return CommonResult.success(null);
        } else if (result == -1) {
            return CommonResult.failed("参数错误");
        } else {
            return CommonResult.failed();
        }
    }


    /**
     * 关注品牌列表
     */
    @RequestMapping("/list")
    public CommonResult<BrandAttentionPageResponse> getBrandAttentionList(@RequestParam(defaultValue = "1") Long pageNum,
                                                                          @RequestParam(defaultValue = "10") Long pageSize,
                                                                          @RequestHeader("Authorization") String token) {

        Page<UmsUserBrandAttentions> userBrandAttentionsList =
                memberService.getBrandAttentionList(userService.getUserInfo(jwtUtil.getUsernameFromToken(token)).getId(), pageNum, pageSize);

        if (userBrandAttentionsList == null) {
            return CommonResult.failed();
        }

        List<BrandAttentionResponse> list = new ArrayList<>();
        for (UmsUserBrandAttentions userBrandAttentions : userBrandAttentionsList.getRecords()) {
            BrandAttentionResponse brandAttentionResponse = getBrandAttentionResponse(userBrandAttentions);
            list.add(brandAttentionResponse);
        }

        BrandAttentionPageResponse response = new BrandAttentionPageResponse();
        response.setList(list);
        return CommonResult.success(response);
    }

    /**
     * 获取品牌详情
     */
    @RequestMapping("/detail")
    public CommonResult<BrandAttentionResponse> getBrandAttentionDetail(@RequestParam Long brandId,
                                                                        @RequestHeader("Authorization") String token) {
        UmsUserBrandAttentions userBrandAttentions = memberService.getBrandAttentionDetail(brandId, userService.getUserInfo(jwtUtil.getUsernameFromToken(token)).getId());
        if (userBrandAttentions == null) {
            return CommonResult.success(null);
        } else {
            BrandAttentionResponse response = getBrandAttentionResponse(userBrandAttentions);
            return CommonResult.success(response);
        }
    }

    /**
     * 清空用户关注品牌
     */
    @RequestMapping("/clear")
    public CommonResult clearBrandAttention(@RequestHeader("Authorization") String token) {
        int result = memberService.clearBrandAttention(userService.getUserInfo(jwtUtil.getUsernameFromToken(token)).getId());

        if (result > 0) {
            return CommonResult.success(null);
        } else if (result == 0) {
            return CommonResult.failed("没有关注的品牌");
        } else if (result == -1) {
            return CommonResult.failed("参数错误");
        } else {
            return CommonResult.failed();
        }
    }


    /**
     * 品牌关注响应转换
     *
     * @param userBrandAttentions 用户关注品牌信息
     * @return 品牌关注响应体
     */
    private static BrandAttentionResponse getBrandAttentionResponse(UmsUserBrandAttentions userBrandAttentions) {
        BrandAttentionResponse brandAttentionResponse = new BrandAttentionResponse();
        brandAttentionResponse.setId(userBrandAttentions.getId());
        brandAttentionResponse.setBrandId(userBrandAttentions.getBrandId());
        brandAttentionResponse.setBrandName(userBrandAttentions.getBrandName());
        brandAttentionResponse.setBrandLogo(userBrandAttentions.getBrandLogo());
        brandAttentionResponse.setBrandDesc(userBrandAttentions.getNote());
        brandAttentionResponse.setCreatedTime(userBrandAttentions.getCreateTime());
        return brandAttentionResponse;
    }

}
