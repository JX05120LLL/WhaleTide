package com.whale_tide.controller.client;


import com.whale_tide.common.api.CommonResult;
import com.whale_tide.dto.client.address.AddressRequest;
import com.whale_tide.dto.client.address.AddressResponse;
import com.whale_tide.service.client.IAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member")
@Api(tags = "用户地址管理")
public class AddressController {

    @Autowired
    private IAddressService addressService;

    // 获取用户地址列表
    @GetMapping("/address/list")
    @ApiOperation(value = "getAddressList", notes = "获取用户地址列表")
    public CommonResult<List<AddressResponse>> getAddressList() {
        List<AddressResponse> addressList = addressService.getAddressList();
        return CommonResult.success(addressList);
    }

    // 获取收货地址详情
    @GetMapping("/address/{id}")
    @ApiOperation(value = "getAddressDetail", notes = "获取收货地址详情")
    public CommonResult<AddressResponse> getAddressDetail(@PathVariable long id) {
        AddressResponse addressDetail = addressService.getAddressDetails(id);
        return CommonResult.success(addressDetail);
    }

    // 添加收货地址
    @PostMapping("/address/add")
    public CommonResult<Void> addAddress(@RequestBody AddressRequest addressRequest) {
        addressService.addAddress(addressRequest);
        return CommonResult.success(null);
    }

    // 修改收货地址
    @PostMapping("/address/update/{id}")
    @ApiOperation(value = "updateAddress", notes = "修改收货地址")
    public CommonResult<Void> updateAddress(@RequestBody AddressRequest addressRequest,@PathVariable long id) {
        addressService.modifyAddress(addressRequest,id);
        return CommonResult.success(null);
    }
    // 删除收货地址
    @PostMapping("/address/delete/{id}")
    public CommonResult<Void> deleteAddress(@PathVariable long id) {
        addressService.deleteAddress(id);
        return CommonResult.success(null);
    }


    // 设置默认收货地址
    @PostMapping("/address/setDefault/{id}")
    public CommonResult<Void> setDefaultAddress(@PathVariable long id) {
        addressService.setDefaultAddress(id);
        return CommonResult.success(null);
    }

}
