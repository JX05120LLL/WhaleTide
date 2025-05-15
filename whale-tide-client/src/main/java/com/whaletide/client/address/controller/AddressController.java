package com.whaletide.client.address.controller;

import com.whaletide.client.address.dto.AddressDTO;
import com.whaletide.client.address.service.IAddressService;
import com.whaletide.client.address.vo.AddressVO;
import com.whaletide.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户地址控制器
 */
@RestController
@RequestMapping("/api/address")
@Tag(name = "用户地址管理", description = "用户收货地址相关接口")
public class AddressController {

    @Autowired
    private IAddressService addressService;

    @PostMapping("/add")
    @Operation(summary = "添加收货地址")
    public CommonResult<Boolean> add(@RequestBody AddressDTO addressDTO) {
        return addressService.add(addressDTO);
    }

    @GetMapping("/list")
    @Operation(summary = "获取收货地址列表")
    public CommonResult<List<AddressVO>> list() {
        return addressService.list();
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "获取收货地址详情")
    public CommonResult<AddressVO> getById(@PathVariable("id") Long id) {
        return addressService.getById(id);
    }

    @PutMapping("/update")
    @Operation(summary = "更新收货地址")
    public CommonResult<Boolean> update(@RequestBody AddressDTO addressDTO) {
        return addressService.update(addressDTO);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除收货地址")
    public CommonResult<Boolean> delete(@PathVariable("id") Long id) {
        return addressService.delete(id);
    }

    @PutMapping("/default/{id}")
    @Operation(summary = "设置默认地址")
    public CommonResult<Boolean> setDefault(@PathVariable("id") Long id) {
        return addressService.setDefault(id);
    }
} 