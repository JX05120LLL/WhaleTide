package com.whale_tide.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale_tide.common.api.CommonPage;
import com.whale_tide.common.api.CommonResult;
import com.whale_tide.dto.menu.MenuParam;
import com.whale_tide.entity.ams.AmsMenus;
import com.whale_tide.service.IMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单管理Controller
 */
@Slf4j
@RestController
@Api(tags = "MenuController", description = "菜单管理")
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private IMenuService menuService;

    @ApiOperation("添加菜单")
    @PostMapping("/create")
    public CommonResult<Boolean> create(@RequestBody MenuParam menuParam) {
        log.info("添加菜单: {}", menuParam);
        boolean success = menuService.create(menuParam);
        if (success) {
            return CommonResult.success(true);
        } else {
            return CommonResult.failed("添加菜单失败");
        }
    }

    @ApiOperation("修改菜单")
    @PostMapping("/update/{id}")
    public CommonResult<Boolean> update(@PathVariable Long id, @RequestBody MenuParam menuParam) {
        log.info("修改菜单，ID: {}, 参数: {}", id, menuParam);
        boolean success = menuService.update(id, menuParam);
        if (success) {
            return CommonResult.success(true);
        } else {
            return CommonResult.failed("修改菜单失败");
        }
    }

    @ApiOperation("根据ID获取菜单详情")
    @GetMapping("/{id}")
    public CommonResult<AmsMenus> getItem(@PathVariable Long id) {
        log.info("获取菜单详情，ID: {}", id);
        AmsMenus menu = menuService.getItem(id);
        return CommonResult.success(menu);
    }

    @ApiOperation("根据ID删除菜单")
    @PostMapping("/delete/{id}")
    public CommonResult<Boolean> delete(@PathVariable Long id) {
        log.info("删除菜单，ID: {}", id);
        boolean success = menuService.delete(id);
        if (success) {
            return CommonResult.success(true);
        } else {
            return CommonResult.failed("删除菜单失败");
        }
    }

    @ApiOperation("分页查询菜单")
    @GetMapping("/list/{parentId}")
    public CommonResult<CommonPage<AmsMenus>> list(
            @PathVariable Long parentId,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        log.info("分页查询菜单，父级ID: {}, 页码: {}, 每页数量: {}", parentId, pageNum, pageSize);
        Page<AmsMenus> menuList = menuService.list(parentId, pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(menuList));
    }

    @ApiOperation("根据父ID查询菜单列表")
    @GetMapping("/listByParent/{parentId}")
    public CommonResult<List<AmsMenus>> listByParent(@PathVariable Long parentId) {
        log.info("根据父ID查询菜单列表，父级ID: {}", parentId);
        List<AmsMenus> menuList = menuService.listByParent(parentId);
        return CommonResult.success(menuList);
    }

    @ApiOperation("修改菜单显示状态")
    @PostMapping("/updateHidden/{id}")
    public CommonResult<Boolean> updateHidden(
            @PathVariable Long id,
            @RequestParam("hidden") Integer hidden) {
        log.info("修改菜单显示状态，ID: {}, 显示状态: {}", id, hidden);
        boolean success = menuService.updateHidden(id, hidden);
        if (success) {
            return CommonResult.success(true);
        } else {
            return CommonResult.failed("修改菜单显示状态失败");
        }
    }

    @ApiOperation("获取所有菜单的树形结构")
    @GetMapping("/treeList")
    public CommonResult<List<AmsMenus>> treeList() {
        log.info("获取所有菜单的树形结构");
        List<AmsMenus> menuList = menuService.treeList();
        return CommonResult.success(menuList);
    }
} 