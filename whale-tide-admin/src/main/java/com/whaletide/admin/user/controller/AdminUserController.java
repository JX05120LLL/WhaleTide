package com.whaletide.admin.user.controller;

import com.whaletide.admin.dashboard.dto.AdminUserLoginDTO;
import com.whaletide.admin.user.vo.AdminUserInfoVO;
import com.whaletide.admin.user.vo.AdminUserLoginVO;
import com.whaletide.admin.user.service.IAdminUserService;
import com.whaletide.common.api.CommonResult;
import com.whaletide.common.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "管理员相关接口")
public class AdminUserController {
    @Autowired
    private IAdminUserService adminUserService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    //登录
    @Operation(summary = "管理员登录接口")
    @PostMapping("/login")
    public CommonResult<AdminUserLoginVO> login(@RequestBody AdminUserLoginDTO adminUserLoginDTO) {
        AdminUserLoginVO adminUserLoginVO = adminUserService.login(adminUserLoginDTO.getUsername(), adminUserLoginDTO.getPassword());
        if (adminUserLoginVO == null) {
            return CommonResult.failed("用户不存在");
        }
        if (adminUserLoginVO.getToken() == null) {
            return CommonResult.failed("用户名或密码错误");
        }
        return CommonResult.success(adminUserLoginVO, "登录成功");
    }

    /**
     * 查询用户信息
     */
    @Operation(summary = "查询用户信息接口")
    @GetMapping("/info")
    public CommonResult<AdminUserInfoVO> getUserInfo(@RequestParam("token") String token) {
        String username = jwtTokenProvider.getUsernameFromToken(token);
        if (username == null) {
            return CommonResult.unauthorized("用户未登录");
        }
        AdminUserInfoVO adminUserInfoVO = adminUserService.getUserInfo(username);

        return CommonResult.success(adminUserInfoVO, "查询成功");
    }

    /**
     * 退出登录
     */
    @Operation(summary = "退出登录接口")
    @PostMapping("/logout")
    public CommonResult<Void> logout() {



        return CommonResult.success(null, "退出成功");
    }


}
