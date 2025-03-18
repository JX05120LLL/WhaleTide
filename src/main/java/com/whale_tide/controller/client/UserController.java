package com.whale_tide.controller.client;

import com.whale_tide.common.api.CommonResult;
import com.whale_tide.dto.client.user.LoginRequest;
import com.whale_tide.dto.client.user.LoginResponse;
import com.whale_tide.dto.client.user.RegisterRequest;
import com.whale_tide.dto.client.user.UserInfoResponse;
import com.whale_tide.service.client.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户接口控制器
 */
@Slf4j
@RestController
@RequestMapping("/sso")
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 用户登录接口
     *
     * @param loginRequest 登录请求参数
     * @return 登录结果
     */
    @PostMapping("/login")
    public CommonResult<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        log.info("收到登录请求: username={}", loginRequest.getUsername());
        
        LoginResponse loginResponse = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        if (loginResponse == null) {
            return CommonResult.failed("用户名或密码错误");
        }
        return CommonResult.success(loginResponse);
    }

    /**
     * 获取用户信息接口
     * @return 用户信息
     */
    @GetMapping("/info")
    public CommonResult<UserInfoResponse> getUserInfo() {
        UserInfoResponse userInfoResponse = userService.getUserInfo();
        return CommonResult.success(userInfoResponse);
    }

    /**
     * 发送验证码接口
     */
    @GetMapping("/sms/code")
    public CommonResult<String> sendVerificationCode(String phone) {
        try {
            String result = userService.sendVerificationCode(phone);
            return CommonResult.success(result);
        } catch (RuntimeException e) {
            return CommonResult.failed(e.getMessage());
        }
    }

    /**
     * 用户注册接口
     * @param registerRequest 注册请求参数
     * @return 注册结果
     */
    @PostMapping("/register")
    public CommonResult<String> register(@RequestBody RegisterRequest registerRequest) {
        log.info("收到注册请求: mobile={}", registerRequest.getMobile());

        userService.register(registerRequest);
        return CommonResult.success("注册成功");
    }
} 