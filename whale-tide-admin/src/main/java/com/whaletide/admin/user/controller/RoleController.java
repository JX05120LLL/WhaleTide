package com.whaletide.admin.user.controller;


import com.whaletide.admin.user.dto.RoleAddDTO;
import com.whaletide.admin.user.dto.RoleUpdateDTO;
import com.whaletide.admin.user.service.impl.RoleService;
import com.whaletide.admin.user.vo.RoleListVO;
import com.whaletide.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/role")
@Tag(name = "角色相关接口")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Operation(summary = "获取角色列表")
    @RequestMapping("/list")
    public CommonResult<List<RoleListVO>> list() {
        List<RoleListVO> roleList = roleService.list();
        return CommonResult.success(roleList);
    }

    @Operation(summary = "添加角色")
    @PostMapping("/add")
    public CommonResult add(@RequestBody RoleAddDTO roleAddDTO) {
        int result = roleService.add(roleAddDTO);

        return CommonResult.success("添加成功");
    }

    @Operation(summary = "删除角色")
    @DeleteMapping("/delete/{id}")
    public CommonResult delete(@PathVariable Long id) {
        int result = roleService.delete(id);
        if (result == 1) {
            return CommonResult.success("删除成功");
        } else {
            return CommonResult.failed("删除失败");
        }
    }

    @Operation(summary = "更新角色")
    @PutMapping("/update")
    public CommonResult update(@RequestBody RoleUpdateDTO roleUpdateDTO) {
        int result = roleService.update(roleUpdateDTO);

        if (result == 1)
            return CommonResult.success("更新成功");
        else if (result == 0)
            return CommonResult.failed("未做任何修改");
        else
            return CommonResult.failed("未查询到角色");
    }

}
