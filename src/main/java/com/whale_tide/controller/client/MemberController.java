package com.whale_tide.controller.client;

import com.whale_tide.common.api.CommonResult;
import com.whale_tide.dto.client.member.AvatarUploadResponse;
import com.whale_tide.dto.client.member.IntegrationDetailResponse;
import com.whale_tide.dto.client.member.MemberInfoUpdateRequest;
import com.whale_tide.dto.client.member.PasswordUpdateRequest;
import com.whale_tide.service.client.IMemberService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Api(tags = "个人中心相关接口")
@RestController("memberController")
@RequestMapping("/member")

public class MemberController {
    @Autowired
    private IMemberService memberService;
    @PostMapping("/info/update")
    public CommonResult<Void> memberInfoUpdate( @RequestBody MemberInfoUpdateRequest request) {
        memberService.memberInfoUpdate(request);
        return CommonResult.success(null);
    }
    @PostMapping("/password/update")
    public CommonResult<Void> PasswordUpdate(@RequestBody PasswordUpdateRequest request) {
        memberService.PasswordUpdate(request);
        return CommonResult.success(null);
    }
    @PostMapping("/avatar/upload")
    public CommonResult<String> avatarUpload(@RequestBody AvatarUploadResponse request) {
        String url = memberService.avatarUpload(request);
        return CommonResult.success(url);
    }
    @GetMapping("/integration/detail")
    public CommonResult<IntegrationDetailResponse> getIntegrationDetail() {
        IntegrationDetailResponse response = memberService.getIntegrationDetail();
        return CommonResult.success(response);
    }
}
