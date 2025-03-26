package com.whale_tide.controller.client;

import com.whale_tide.common.api.CommonResult;
import com.whale_tide.dto.client.user.LoginRequest;
import com.whale_tide.dto.client.user.LoginResponse;
import com.whale_tide.dto.client.user.RegisterRequest;
import com.whale_tide.dto.client.user.UserInfoResponse;
import com.whale_tide.service.client.IUserService;
import com.whale_tide.util.CookieUtil;
import com.whale_tide.util.JwtUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户接口控制器
 */
@Slf4j
@RestController("clientUserController")
@RequestMapping("/sso")
@Api(tags = "用户相关接口")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 用户登录接口
     *
     * @param loginRequest 登录请求参数
     * @return 登录结果
     */
    @PostMapping("/login")
    public CommonResult<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        log.info("收到登录请求: username={}", loginRequest.getUsername());
        
        LoginResponse loginResponse = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        String token = jwtUtil.generateToken(loginRequest.getUsername());// 生成token
        loginResponse.setToken(token);
        loginResponse.setTokenHead("Bearer");

        // 保存token到cookie中
        CookieUtil.addCookie(response, "token", token, 24 * 60 * 60); // 设置 Cookie 有效期为 1 天

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
    public CommonResult<UserInfoResponse> getUserInfo(HttpServletRequest request) {
        //header中获取token
        String token = request.getHeader("Authorization").replaceAll("Bearer:", "");
        String username = jwtUtil.getUsernameFromToken(token); // 从Token中解析用户名

        UserInfoResponse userInfoResponse = userService.getUserInfo(username);
        if(userInfoResponse == null) {
            return CommonResult.failed("获取用户信息失败");
        }
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