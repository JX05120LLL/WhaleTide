package com.whale_tide.controller;

import com.whale_tide.common.api.CommonResult;
import com.whale_tide.dto.AdminLoginParam;
import com.whale_tide.dto.AdminInfoResult;
import com.whale_tide.dto.AdminLoginResult;
import com.whale_tide.entity.AmsAdmins;
import com.whale_tide.service.IAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AdminController {
    private final IAdminService adminService;
//    @Autowired
//    private final JwtTokenUtil jwtTokenUtil;

    /**
     * 管理员登录接口
     *
     * @param admin 用户名和密码
     * @return 返回登录结果2
     */
    @PostMapping("/login")
    public CommonResult<AdminLoginResult> login(@RequestBody AdminLoginParam admin) {
        CommonResult<AdminLoginResult> commonResult;

        String token = adminService.login(admin.getUsername(), admin.getPassword());
        if (token == null) {
            commonResult = CommonResult.failed("?");
        } else {
            //
            AdminLoginResult result = new AdminLoginResult();
            result.setToken(token);
            result.setTokenHead("JWT");
            //
            commonResult = CommonResult.success(result);
        }
        return commonResult;
    }


    @GetMapping("/info")
    public CommonResult<AdminInfoResult> info(@RequestHeader("Authorization") String authHeader) {
        //获取admin
//        String token = authHeader.replace("Bearer ", "");
//        String username = jwtTokenUtil.getUserNameFromToken(token);
        String username = "jwtTokenUtil.getUserNameFromToken(token)";
        AmsAdmins amsAdmins = adminService.getAdminByUsername(username);
        CommonResult<AdminInfoResult> commonResult;
        AdminInfoResult infoResult = new AdminInfoResult();

        if (amsAdmins == null) {
            infoResult = null;
            commonResult = CommonResult.failed("未查询到信息");
        } else {
            infoResult.setUsername(username);
            List<Long> roleIds = adminService.getRoleIdList(amsAdmins.getId());
            //获取角色列表
            List<String> roles = adminService.getRoleNameList(roleIds);
            infoResult.setRoles(roles);

            //获取菜单列表
            List<String> menus = adminService.getMenuNameList(adminService.getMenuIdListByRoleIds(roleIds));
            infoResult.setMenus(menus);

            //获取头像URL
            infoResult.setIcon(amsAdmins.getAvatar());

            //获取权限列表
            commonResult = CommonResult.success(null);
        }
        return commonResult;
    }


}
