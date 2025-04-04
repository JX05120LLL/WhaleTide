package com.whale_tide.controller.client;

import com.whale_tide.common.api.CommonResult;
import com.whale_tide.dto.client.member.AvatarUploadResponse;
import com.whale_tide.dto.client.member.IntegrationDetailResponse;
import com.whale_tide.dto.client.member.MemberInfoUpdateRequest;
import com.whale_tide.dto.client.member.PasswordUpdateRequest;
import com.whale_tide.service.client.IMemberService;
import com.whale_tide.util.JwtUtil;
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
    
    @Autowired
    private JwtUtil jwtUtil;
    /**
     * 修改个人信息
     * @param request
     * @return
     */
    @ApiImplicitParam(name = "Authorization", value = "身份认证Token", required = true, paramType = "header")
    @PostMapping("/info/update")
    public CommonResult<Void> memberInfoUpdate(@RequestHeader(value = "Authorization", required = false) String authHeader, @RequestBody MemberInfoUpdateRequest request) {
        try {
            // 记录请求信息
            log.info("收到更新用户信息请求: {}", request);
            log.info("Authorization头: {}", authHeader);
            
            // 如果请求对象中没有用户名，尝试从token中获取
            if (request.getUsername() == null || request.getUsername().isEmpty()) {
                // 从请求头中获取token
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    String token = authHeader.substring(7);
                    log.info("提取的token: {}", token);
                    
                    try {
                        // 从token中提取用户名并设置到请求对象中
                        String username = jwtUtil.getUsernameFromToken(token);
                        log.info("从token解析出的用户名: {}", username);
                        request.setUsername(username);
                    } catch (Exception e) {
                        log.error("Token验证失败: {}", e.getMessage(), e);
                        return CommonResult.failed("Token验证失败: " + e.getMessage());
                    }
                } else {
                    log.warn("Authorization头格式不正确: {}", authHeader);
                    return CommonResult.failed("未授权，请先登录");
                }
            }
            
            // 执行更新操作
            memberService.memberInfoUpdate(request);
            log.info("用户信息更新成功: {}", request.getUsername());
            return CommonResult.success(null);
        } catch (Exception e) {
            log.error("更新用户信息失败: {}", e.getMessage(), e);
            return CommonResult.failed(e.getMessage());
        }
    }
    /**
     * 修改密码
     * @param request
     * @return
     */
    @ApiImplicitParam(name = "Authorization", value = "身份认证Token", required = true, paramType = "header")
    @PostMapping("/password/update")
    public CommonResult<Void> PasswordUpdate(@RequestHeader(value = "Authorization", required = false) String authHeader, @RequestBody PasswordUpdateRequest request) {
        try {
            // 验证token有效性
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return CommonResult.failed("未授权，请先登录");
            }
            
            String token = authHeader.substring(7);
            try {
                // 验证token有效性
                jwtUtil.getUsernameFromToken(token);
            } catch (Exception e) {
                return CommonResult.failed("Token验证失败: " + e.getMessage());
            }
            
            memberService.PasswordUpdate(request);
            return CommonResult.success(null);
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }
    /**
     * 上传头像
     * @param request
     * @return
     */
    @ApiImplicitParam(name = "Authorization", value = "身份认证Token", required = true, paramType = "header")
    @PostMapping("/avatar/upload")
    public CommonResult<String> avatarUpload(@RequestHeader(value = "Authorization", required = false) String authHeader, @RequestBody AvatarUploadResponse request) {
        try {
            // 验证token有效性
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return CommonResult.failed("未授权，请先登录");
            }
            
            String token = authHeader.substring(7);
            try {
                // 验证token有效性
                jwtUtil.getUsernameFromToken(token);
            } catch (Exception e) {
                return CommonResult.failed("Token验证失败: " + e.getMessage());
            }
            
            String url = memberService.avatarUpload(request);
            return CommonResult.success(url);
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }
    /**
     * 获取积分详情
     * @return
     */
    @ApiImplicitParam(name = "Authorization", value = "身份认证Token", required = true, paramType = "header")
    @GetMapping("/integration/detail")
    public CommonResult<IntegrationDetailResponse> getIntegrationDetail(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            // 验证token
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return CommonResult.failed("未授权，请先登录");
            }
            
            String token = authHeader.substring(7);
            try {
                // 验证token有效性
                jwtUtil.getUsernameFromToken(token);
            } catch (Exception e) {
                return CommonResult.failed("Token验证失败");
            }
            
            IntegrationDetailResponse response = memberService.getIntegrationDetail();
            return CommonResult.success(response);
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }
}
