package com.whale_tide.controller.client;

import com.whale_tide.common.api.CommonResult;
import com.whale_tide.dto.client.member.AvatarUploadResponse;
import com.whale_tide.dto.client.member.IntegrationDetailResponse;
import com.whale_tide.dto.client.member.MemberInfoUpdateRequest;
import com.whale_tide.dto.client.member.PasswordUpdateRequest;
import com.whale_tide.service.client.IMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
/**
 * 个人中心相关接口
 */
@Slf4j
@Api(tags = "个人中心相关接口")
@RestController("memberController")
@RequestMapping("/member")

public class MemberController {
    @Autowired
    private IMemberService memberService;
    /**
     * 修改个人信息
     * @param request
     * @return
     */
    @ApiImplicitParam(name = "Authorization", value = "身份认证Token", required = true, paramType = "header")
    @PostMapping("/info/update")
    public CommonResult<Void> memberInfoUpdate( @RequestBody MemberInfoUpdateRequest request) {
        memberService.memberInfoUpdate(request);
        return CommonResult.success(null);
    }
    /**
     * 修改密码
     * @param request
     * @return
     */
    @ApiImplicitParam(name = "Authorization", value = "身份认证Token", required = true, paramType = "header")
    @PostMapping("/password/update")
    public CommonResult<Void> PasswordUpdate(@RequestBody PasswordUpdateRequest request) {
        memberService.PasswordUpdate(request);
        return CommonResult.success(null);
    }
    /**
     * 上传头像
     * @param request
     * @return
     */
    @ApiImplicitParam(name = "Authorization", value = "身份认证Token", required = true, paramType = "header")
    @PostMapping("/avatar/upload")
    public CommonResult<String> avatarUpload(@RequestBody AvatarUploadResponse request) {
        String url = memberService.avatarUpload(request);
        return CommonResult.success(url);
    }
    /**
     * 获取积分详情
     * @return
     */
    @ApiImplicitParam(name = "Authorization", value = "身份认证Token", required = true, paramType = "header")
    @GetMapping("/integration/detail")
    public CommonResult<IntegrationDetailResponse> getIntegrationDetail() {
        IntegrationDetailResponse response = memberService.getIntegrationDetail();
        return CommonResult.success(response);
    }
}
