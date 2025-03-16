package com.whale_tide.controller.role;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.whale_tide.common.api.CommonPage;
import com.whale_tide.common.api.CommonResult;
import com.whale_tide.dto.resource.ResourceResult;
import com.whale_tide.dto.role.*;
import com.whale_tide.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理Controller
 */
@Slf4j
@RestController
@Api(tags = "RoleController", description = "角色管理")
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private IAdminService adminService;

    @ApiOperation("获取角色列表")
    @GetMapping("/list")
    public CommonResult<CommonPage<RoleResult>> getList(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        
        log.info("获取角色列表, keyword={}, pageNum={}, pageSize={}", keyword, pageNum, pageSize);
        
        // 调用服务获取角色列表
        IPage<RoleResult> rolePage = adminService.listRoles(keyword, pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(rolePage));
    }

    @ApiOperation("获取所有角色")
    @GetMapping("/listAll")
    public CommonResult<List<RoleResult>> getAll() {
        log.info("获取所有角色");
        
        // 调用服务获取所有角色
        List<RoleResult> roleList = adminService.listAllRoles();
        return CommonResult.success(roleList);
    }

    @ApiOperation("添加角色")
    @PostMapping("/create")
    public CommonResult<Boolean> create(@RequestBody RoleParam roleParam) {
        log.info("添加角色: {}", roleParam);
        
        // 调用服务创建角色
        boolean success = adminService.createRole(roleParam);
        if (success) {
            return CommonResult.success(true);
        } else {
            return CommonResult.failed("添加角色失败");
        }
    }

    @ApiOperation("修改角色")
    @PostMapping("/update/{id}")
    public CommonResult<Boolean> update(@PathVariable Long id, @RequestBody RoleParam roleParam) {
        log.info("修改角色, id={}, param={}", id, roleParam);
        
        // 调用服务更新角色
        boolean success = adminService.updateRole(id, roleParam);
        if (success) {
            return CommonResult.success(true);
        } else {
            return CommonResult.failed("修改角色失败");
        }
    }

    @ApiOperation("批量删除角色")
    @PostMapping("/delete")
    public CommonResult<Boolean> delete(@RequestBody List<Long> ids) {
        log.info("批量删除角色, ids={}", ids);
        
        // 调用服务删除角色
        boolean success = adminService.deleteRoles(ids);
        if (success) {
            return CommonResult.success(true);
        } else {
            return CommonResult.failed("删除角色失败");
        }
    }

    @ApiOperation("修改角色状态")
    @PostMapping("/updateStatus/{id}")
    public CommonResult<Boolean> updateStatus(
            @PathVariable Long id,
            @RequestParam(value = "status") Integer status) {
        log.info("修改角色状态, id={}, status={}", id, status);
        
        // 调用服务更新角色状态
        boolean success = adminService.updateRoleStatus(id, status);
        if (success) {
            return CommonResult.success(true);
        } else {
            return CommonResult.failed("修改角色状态失败");
        }
    }

    @ApiOperation("获取角色相关菜单")
    @GetMapping("/listMenu/{roleId}")
    public CommonResult<List<MenuNodeResult>> listMenu(@PathVariable Long roleId) {
        log.info("获取角色相关菜单, roleId={}", roleId);
        
        // 调用服务获取角色菜单
        List<MenuNodeResult> menuList = adminService.listMenuByRole(roleId);
        return CommonResult.success(menuList);
    }

    @ApiOperation("获取角色相关资源")
    @GetMapping("/listResource/{roleId}")
    public CommonResult<List<ResourceResult>> listResource(@PathVariable Long roleId) {
        log.info("获取角色相关资源, roleId={}", roleId);
        
        // 调用服务获取角色资源
        List<ResourceResult> resourceList = adminService.listResourceByRole(roleId);
        return CommonResult.success(resourceList);
    }

    @ApiOperation("分配角色菜单")
    @PostMapping("/allocMenu")
    public CommonResult<Boolean> allocMenu(@RequestBody AllocMenuParam allocMenuParam) {
        log.info("分配角色菜单, roleId={}, menuIds={}", allocMenuParam.getRoleId(), allocMenuParam.getMenuIds());
        
        // 调用服务分配角色菜单
        boolean success = adminService.allocMenu(allocMenuParam.getRoleId(), allocMenuParam.getMenuIds());
        if (success) {
            return CommonResult.success(true);
        } else {
            return CommonResult.failed("分配角色菜单失败");
        }
    }

    @ApiOperation("分配角色资源")
    @PostMapping("/allocResource")
    public CommonResult<Boolean> allocResource(@RequestBody AllocResourceParam allocResourceParam) {
        log.info("分配角色资源, roleId={}, resourceIds={}", allocResourceParam.getRoleId(), allocResourceParam.getResourceIds());
        
        // 调用服务分配角色资源
        boolean success = adminService.allocResource(allocResourceParam.getRoleId(), allocResourceParam.getResourceIds());
        if (success) {
            return CommonResult.success(true);
        } else {
            return CommonResult.failed("分配角色资源失败");
        }
    }
} 