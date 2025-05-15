package com.whaletide.client.user.controller;

import com.whaletide.client.user.dto.UserLoginDTO;
import com.whaletide.client.user.dto.UserRegisterDTO;
import com.whaletide.client.user.dto.UpdatePasswordDTO;
import com.whaletide.client.user.service.IUserService;
import com.whaletide.client.user.vo.UserVO;
import com.whaletide.common.api.CommonResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.AuthenticationException;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public CommonResult<String> register(@Validated @RequestBody UserRegisterDTO userRegisterDTO) {
        userService.register(userRegisterDTO);
        return CommonResult.success( "注册成功");
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public CommonResult<String> login(@Validated @RequestBody UserLoginDTO userLoginDTO) {
        String token = userService.login(userLoginDTO.getUsername(), userLoginDTO.getPassword());
        return CommonResult.success(token, "登录成功");
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/info")
    public CommonResult<UserVO> getUserInfo() {
        try {
            UserVO userInfo = userService.getCurrentUser();
            return CommonResult.success(userInfo);
        } catch (AuthenticationException e) {
            return CommonResult.unauthorized(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public CommonResult<String> logout() {
        userService.logout();
        return CommonResult.success(null, "登出成功");
    }

    /**
     * 修改用户信息
     */
    @PutMapping("/info")
    public CommonResult<String> updateUserInfo(@RequestBody UserVO userVO) {
        try {
            userService.updateUserInfo(userVO);
            return CommonResult.success(null, "修改成功");
        } catch (AuthenticationException e) {
            return CommonResult.unauthorized(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }
    
    /**
     * 修改密码
     */
    @PostMapping("/password/update")
    public CommonResult<String> updatePassword(@Validated @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        try {
            userService.updatePassword(updatePasswordDTO);
            return CommonResult.success(null, "密码修改成功");
        } catch (AuthenticationException e) {
            return CommonResult.unauthorized(e.getMessage());
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }
} 