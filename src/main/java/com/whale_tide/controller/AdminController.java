package com.whale_tide.controller;

import com.whale_tide.common.dto.HttpResult;
import com.whale_tide.dto.AdminLoginParam;
import com.whale_tide.dto.AdminInfoResult;
import com.whale_tide.dto.AdminLoginResult;
import com.whale_tide.entity.AmsAdmins;
import com.whale_tide.security.util.JwtTokenUtil;
import com.whale_tide.service.IAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AdminController {
    private final IAdminService adminService;
    @Autowired
    private final JwtTokenUtil jwtTokenUtil;

    /**
     * 管理员登录接口
     *
     * @param admin 用户名和密码
     * @return 返回登录结果2
     */
    @PostMapping("/login")
    public HttpResult<AdminLoginResult> login(@RequestBody AdminLoginParam admin) {
        HttpResult<AdminLoginResult> httpResult = new HttpResult<>();

        String token = adminService.login(admin.getUsername(), admin.getPassword());
        if (token == null) {
            httpResult.setCode(403);
            httpResult.setMessage("?");
            httpResult.setResult(null);
            httpResult.setTime(new Date());
        } else {
            //
            AdminLoginResult result = new AdminLoginResult();
            result.setToken(token);
            result.setTokenHead("JWT");
            //
            httpResult.setCode(200);
            httpResult.setMessage("Login success");
            httpResult.setResult(result);
            httpResult.setTime(new Date());
        }
        return httpResult;
    }


    @GetMapping("/info")
    public HttpResult<AdminInfoResult> info(@RequestHeader("Authorization") String authHeader) {
        //获取admin
        String token = authHeader.replace("Bearer ", "");
        String username = jwtTokenUtil.getUserNameFromToken(token);
        AmsAdmins amsAdmins = adminService.getAdminByUsername(username);
        HttpResult<AdminInfoResult> result = new HttpResult<>();
        AdminInfoResult infoResult = new AdminInfoResult();

        if (amsAdmins == null) {
            infoResult = null;
            result.setResult(infoResult);
        } else {
            infoResult.setUsername(username);
            //获取角色列表
            List<String> roles = adminService.getRoleNameList(adminService.getRoleList(amsAdmins.getId()));
            infoResult.setRoles(roles);

            List<String> menus =

        }
        return result;
    }


}
