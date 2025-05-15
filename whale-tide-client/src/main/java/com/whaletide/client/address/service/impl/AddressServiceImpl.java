package com.whaletide.client.address.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.whaletide.client.address.dto.AddressDTO;
import com.whaletide.client.address.service.IAddressService;
import com.whaletide.client.address.vo.AddressVO;
import com.whaletide.common.api.CommonResult;
import com.whaletide.common.entity.ums.UmsUserAddresses;
import com.whaletide.common.exception.auth.AuthenticationException;
import com.whaletide.common.exception.base.BusinessException;
import com.whaletide.common.exception.base.ResourceNotFoundException;
import com.whaletide.common.exception.base.ValidationException;
import com.whaletide.common.mapper.ums.UmsUserAddressesMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户地址服务实现类
 */
@Service
@Slf4j
public class AddressServiceImpl implements IAddressService {

    @Autowired
    private UmsUserAddressesMapper addressesMapper;

    /**
     * 添加收货地址
     */
    @Override
    @Transactional
    public CommonResult<Boolean> add(AddressDTO addressDTO) {
        // 参数校验
        if (addressDTO == null) {
            throw new ValidationException("地址信息不能为空");
        }
        
        // 获取当前用户ID
        Long userId = getCurrentUserId();
        if (userId == null) {
            throw new AuthenticationException("用户未登录");
        }
        
        // 创建地址实体
        UmsUserAddresses address = new UmsUserAddresses();
        address.setUserId(userId);
        address.setReceiverName(addressDTO.getReceiverName());
        address.setReceiverPhone(addressDTO.getReceiverPhone());
        address.setProvince(addressDTO.getProvince());
        address.setCity(addressDTO.getCity());
        address.setDistrict(addressDTO.getDistrict());
        address.setDetailAddress(addressDTO.getDetailAddress());
        address.setPostalCode(addressDTO.getPostalCode());
        address.setTag(addressDTO.getTag());
        address.setIsDeleted(0); // 未删除
        address.setCreateTime(LocalDateTime.now());
        address.setUpdateTime(LocalDateTime.now());
        
        // 如果是第一个地址，设为默认地址
        int addressCount = getAddressCount(userId);
        if (addressCount == 0 || addressDTO.getDefaultStatus() != null && addressDTO.getDefaultStatus() == 1) {
            address.setDefaultStatus(1);
            // 取消其他默认地址
            if (addressCount > 0) {
                cancelDefaultAddress(userId);
            }
        } else {
            address.setDefaultStatus(0);
        }
        
        // 保存地址
        addressesMapper.insert(address);
        
        return CommonResult.success(true, "添加成功");
    }

    /**
     * 获取收货地址列表
     */
    @Override
    public CommonResult<List<AddressVO>> list() {
        // 获取当前用户ID
        Long userId = getCurrentUserId();
        if (userId == null) {
            throw new AuthenticationException("用户未登录");
        }
        
        // 查询用户的收货地址列表
        LambdaQueryWrapper<UmsUserAddresses> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UmsUserAddresses::getUserId, userId)
                .eq(UmsUserAddresses::getIsDeleted, 0)
                .orderByDesc(UmsUserAddresses::getDefaultStatus)
                .orderByDesc(UmsUserAddresses::getCreateTime);
        
        List<UmsUserAddresses> addresses = addressesMapper.selectList(queryWrapper);
        if (addresses.isEmpty()) {
            return CommonResult.success(new ArrayList<>());
        }
        
        // 转换为VO
        List<AddressVO> result = addresses.stream().map(this::convertToVO).collect(Collectors.toList());
        
        return CommonResult.success(result);
    }

    /**
     * 获取收货地址详情
     */
    @Override
    public CommonResult<AddressVO> getById(Long id) {
        // 参数校验
        if (id == null) {
            throw new ValidationException("地址ID不能为空");
        }
        
        // 获取当前用户ID
        Long userId = getCurrentUserId();
        if (userId == null) {
            throw new AuthenticationException("用户未登录");
        }
        
        // 查询收货地址
        UmsUserAddresses address = getAddressById(id, userId);
        if (address == null) {
            throw new ResourceNotFoundException("收货地址不存在");
        }
        
        // 转换为VO
        AddressVO result = convertToVO(address);
        
        return CommonResult.success(result);
    }

    /**
     * 更新收货地址
     */
    @Override
    @Transactional
    public CommonResult<Boolean> update(AddressDTO addressDTO) {
        // 参数校验
        if (addressDTO == null || addressDTO.getId() == null) {
            throw new ValidationException("地址信息不能为空");
        }
        
        // 获取当前用户ID
        Long userId = getCurrentUserId();
        if (userId == null) {
            throw new AuthenticationException("用户未登录");
        }
        
        // 查询收货地址
        UmsUserAddresses address = getAddressById(addressDTO.getId(), userId);
        if (address == null) {
            throw new ResourceNotFoundException("收货地址不存在");
        }
        
        // 更新地址信息
        address.setReceiverName(addressDTO.getReceiverName());
        address.setReceiverPhone(addressDTO.getReceiverPhone());
        address.setProvince(addressDTO.getProvince());
        address.setCity(addressDTO.getCity());
        address.setDistrict(addressDTO.getDistrict());
        address.setDetailAddress(addressDTO.getDetailAddress());
        address.setPostalCode(addressDTO.getPostalCode());
        address.setTag(addressDTO.getTag());
        address.setUpdateTime(LocalDateTime.now());
        
        // 处理默认地址
        if (addressDTO.getDefaultStatus() != null && addressDTO.getDefaultStatus() == 1 && address.getDefaultStatus() != 1) {
            address.setDefaultStatus(1);
            // 取消其他默认地址
            cancelDefaultAddress(userId);
        }
        
        // 更新地址
        addressesMapper.updateById(address);
        
        return CommonResult.success(true, "更新成功");
    }

    /**
     * 删除收货地址
     */
    @Override
    @Transactional
    public CommonResult<Boolean> delete(Long id) {
        // 参数校验
        if (id == null) {
            throw new ValidationException("地址ID不能为空");
        }
        
        // 获取当前用户ID
        Long userId = getCurrentUserId();
        if (userId == null) {
            throw new AuthenticationException("用户未登录");
        }
        
        // 查询收货地址
        UmsUserAddresses address = getAddressById(id, userId);
        if (address == null) {
            throw new ResourceNotFoundException("收货地址不存在");
        }
        
        // 逻辑删除
        address.setIsDeleted(1);
        address.setUpdateTime(LocalDateTime.now());
        addressesMapper.updateById(address);
        
        // 如果删除的是默认地址，设置一个新的默认地址
        if (address.getDefaultStatus() == 1) {
            setNewDefaultAddress(userId);
        }
        
        return CommonResult.success(true, "删除成功");
    }

    /**
     * 设置默认地址
     */
    @Override
    @Transactional
    public CommonResult<Boolean> setDefault(Long id) {
        // 参数校验
        if (id == null) {
            throw new ValidationException("地址ID不能为空");
        }
        
        // 获取当前用户ID
        Long userId = getCurrentUserId();
        if (userId == null) {
            throw new AuthenticationException("用户未登录");
        }
        
        // 查询收货地址
        UmsUserAddresses address = getAddressById(id, userId);
        if (address == null) {
            throw new ResourceNotFoundException("收货地址不存在");
        }
        
        // 取消其他默认地址
        cancelDefaultAddress(userId);
        
        // 设置为默认地址
        address.setDefaultStatus(1);
        address.setUpdateTime(LocalDateTime.now());
        addressesMapper.updateById(address);
        
        return CommonResult.success(true, "设置成功");
    }

    /**
     * 转换为VO
     */
    private AddressVO convertToVO(UmsUserAddresses address) {
        AddressVO vo = new AddressVO();
        vo.setId(address.getId());
        vo.setReceiverName(address.getReceiverName());
        vo.setReceiverPhone(address.getReceiverPhone());
        vo.setProvince(address.getProvince());
        vo.setCity(address.getCity());
        vo.setDistrict(address.getDistrict());
        vo.setDetailAddress(address.getDetailAddress());
        vo.setFullAddress(address.getProvince() + address.getCity() + address.getDistrict() + address.getDetailAddress());
        vo.setPostalCode(address.getPostalCode());
        vo.setDefaultStatus(address.getDefaultStatus());
        vo.setTag(address.getTag());
        vo.setCreateTime(address.getCreateTime());
        return vo;
    }

    /**
     * 获取当前用户ID
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        // 这里根据实际认证信息获取用户ID
        // 假设用户信息中包含了用户ID
        try {
            // 示例代码，实际情况可能不同
            Object principal = authentication.getPrincipal();
            if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
                String username = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
                // 根据用户名查询用户ID
                // 这里需要根据实际情况获取用户ID
                return 1L; // 示例，实际需要查询
            }
            return null;
        } catch (Exception e) {
            log.error("获取当前用户ID异常: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 查询用户地址数量
     */
    private int getAddressCount(Long userId) {
        LambdaQueryWrapper<UmsUserAddresses> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UmsUserAddresses::getUserId, userId)
                .eq(UmsUserAddresses::getIsDeleted, 0);
        
        return addressesMapper.selectCount(queryWrapper).intValue();
    }

    /**
     * 根据ID获取地址
     */
    private UmsUserAddresses getAddressById(Long id, Long userId) {
        LambdaQueryWrapper<UmsUserAddresses> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UmsUserAddresses::getId, id)
                .eq(UmsUserAddresses::getUserId, userId)
                .eq(UmsUserAddresses::getIsDeleted, 0);
        
        return addressesMapper.selectOne(queryWrapper);
    }

    /**
     * 取消用户的所有默认地址
     */
    private void cancelDefaultAddress(Long userId) {
        LambdaUpdateWrapper<UmsUserAddresses> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UmsUserAddresses::getUserId, userId)
                .eq(UmsUserAddresses::getDefaultStatus, 1)
                .eq(UmsUserAddresses::getIsDeleted, 0)
                .set(UmsUserAddresses::getDefaultStatus, 0)
                .set(UmsUserAddresses::getUpdateTime, LocalDateTime.now());
        
        addressesMapper.update(null, updateWrapper);
    }

    /**
     * 设置新的默认地址
     */
    private void setNewDefaultAddress(Long userId) {
        // 查找最近创建的地址设为默认
        LambdaQueryWrapper<UmsUserAddresses> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UmsUserAddresses::getUserId, userId)
                .eq(UmsUserAddresses::getIsDeleted, 0)
                .orderByDesc(UmsUserAddresses::getCreateTime)
                .last("LIMIT 1");
        
        UmsUserAddresses address = addressesMapper.selectOne(queryWrapper);
        if (address != null) {
            address.setDefaultStatus(1);
            address.setUpdateTime(LocalDateTime.now());
            addressesMapper.updateById(address);
        }
    }
} 