package com.whale_tide.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale_tide.common.api.CommonResult;
import com.whale_tide.dto.admin.*;
import com.whale_tide.entity.AmsAdmins;
import com.whale_tide.service.IAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
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

    @PostMapping("/logout")
    public CommonResult logout(@RequestHeader("Authorization") String authHeader) {
        String username = "jwtTokenUtil.getUserNameFromToken(token)";
        if (!adminService.logout(username))
            return CommonResult.failed("操作失败");

        return CommonResult.success(null);
    }

    @GetMapping("/list")
    public CommonResult<AdminListResult> list(@RequestBody AdminListParam adminListParam) {
        Page<AmsAdmins> page = adminService.list(adminListParam.getKeyword(), adminListParam.getPageNum(), adminListParam.getPageSize());
        AdminListResult result = new AdminListResult();

        //获取管理员列表并进行转换
        List<AmsAdmins> amsAdmins = page.getRecords();
        List<AdminResult> adminResults = new ArrayList<>();
        for (AmsAdmins admin : amsAdmins) {
            AdminResult adminResult = new AdminResult();
            adminResult.setUsername(admin.getUsername());
            adminResult.setId(admin.getId());
            adminResult.setEmail(admin.getEmail());
            adminResult.setCreateTime(admin.getCreateTime());
            adminResult.setStatus(admin.getStatus().byteValue());
            adminResult.setNote(admin.getNote());
            adminResults.add(adminResult);
        }

        //填充返回Data
        result.setList(adminResults);
        result.setTotal(page.getTotal());
        result.setPageNum(adminListParam.getPageNum());
        result.setPageSize(adminListParam.getPageSize());
        return CommonResult.success(result);
    }

    @PostMapping("/register")
    public CommonResult regiser(@RequestBody AdminParam adminParam) {
        AmsAdmins amsAdmins = new AmsAdmins();
        amsAdmins.setUsername(adminParam.getUsername());
        amsAdmins.setPassword(adminParam.getPassword());
        amsAdmins.setEmail(adminParam.getEmail());   //邮箱
        amsAdmins.setNote(adminParam.getNote());
        long result = adminService.register(amsAdmins);
        if (result == -1L) {
            return CommonResult.failed("注册失败");
        } else if (result == 0L) {
            return CommonResult.failed("用户名已存在");
        } else {
            return CommonResult.success(result, "注册成功");
        }
    }

    @PostMapping("/update/{id}")
    public CommonResult update(@RequestBody AdminParam adminParam, @PathVariable("id") Long id) {
        AmsAdmins amsAdmins = new AmsAdmins();
        amsAdmins.setId(id);
        amsAdmins.setUsername(adminParam.getUsername());
        amsAdmins.setPassword(adminParam.getPassword());
        amsAdmins.setEmail(adminParam.getEmail());   //邮箱
        amsAdmins.setNote(adminParam.getNote());
        int result = adminService.update(amsAdmins);
        switch (result) {
            case -1:
                return CommonResult.failed("更新失败,请检查参数");
            case -2:
                return CommonResult.failed("用户名不存在");
            case -3:
                return CommonResult.failed("服务器内部错误");
            default:
                return CommonResult.success(result, "更新成功");
        }
    }

    @PostMapping("/updateStatus/{id}")
    public CommonResult updataStatus(@RequestBody UpdataStatusParam updataStatusParam, @RequestBody Long id) {
        int result = adminService.updateStatus(id, updataStatusParam.getStatus());
        switch (result) {
            case -1:
                return CommonResult.failed("更新失败,请检查参数");
            case -2:
                return CommonResult.failed("管理员不存在");
            case 0:
                return CommonResult.success(result, "无需更新");
            case 1:
                return CommonResult.success(result, "更新成功");
            default:
                return CommonResult.failed("服务器内部错误");
        }
    }

    
}
