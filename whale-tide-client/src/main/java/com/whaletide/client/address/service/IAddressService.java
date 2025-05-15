package com.whaletide.client.address.service;

import com.whaletide.client.address.dto.AddressDTO;
import com.whaletide.client.address.vo.AddressVO;
import com.whaletide.common.api.CommonResult;

import java.util.List;

/**
 * 用户地址服务接口
 */
public interface IAddressService {

    /**
     * 添加收货地址
     * @param addressDTO 地址信息
     * @return 操作结果
     */
    CommonResult<Boolean> add(AddressDTO addressDTO);

    /**
     * 获取当前用户的收货地址列表
     * @return 地址列表
     */
    CommonResult<List<AddressVO>> list();

    /**
     * 根据ID获取收货地址详情
     * @param id 地址ID
     * @return 地址详情
     */
    CommonResult<AddressVO> getById(Long id);

    /**
     * 更新收货地址
     * @param addressDTO 地址信息
     * @return 操作结果
     */
    CommonResult<Boolean> update(AddressDTO addressDTO);

    /**
     * 删除收货地址
     * @param id 地址ID
     * @return 操作结果
     */
    CommonResult<Boolean> delete(Long id);

    /**
     * 设置默认地址
     * @param id 地址ID
     * @return 操作结果
     */
    CommonResult<Boolean> setDefault(Long id);
} 