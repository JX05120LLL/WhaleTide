package com.whale_tide.service.client.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.whale_tide.common.exception.address.AddressNotFoundException;
import com.whale_tide.dto.client.address.AddressRequest;
import com.whale_tide.dto.client.address.AddressResponse;
import com.whale_tide.entity.ums.UmsUserAddresses;
import com.whale_tide.entity.ums.UmsUsers;
import com.whale_tide.mapper.ums.UmsUserAddressesMapper;
import com.whale_tide.mapper.ums.UmsUsersMapper;
import com.whale_tide.service.client.IAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.whale_tide.util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户地址服务实现类
 */

@Service
@Slf4j

public class AddressServiceImpl implements IAddressService {

    @Autowired
    private UmsUserAddressesMapper userAddressMapper;

    @Autowired
    private UmsUsersMapper umsUsersMapper;
    @Autowired
    private JwtUtil jwtUtil;


    // 获取用户地址列表
    @Override
    public AddressResponse getAddressList() {
        // 从token中获取用户id
        Long userId = getCurrentUserId();
        // 根据用户id查询地址列表
        LambdaQueryWrapper<UmsUserAddresses> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UmsUserAddresses::getUserId, userId);
        UmsUserAddresses address = userAddressMapper.selectOne(queryWrapper);
        if (address == null) {
            throw new AddressNotFoundException("用户地址不存在");
        }

        // 封装 返回值
        AddressResponse response = new AddressResponse();
        response.setId(address.getId());
        response.setName(address.getReceiverName());
        response.setPhoneNumber(address.getReceiverPhone());
        response.setDefaultStatus(address.getDefaultStatus());
        response.setProvince(address.getProvince());
        response.setCity(address.getCity());
        response.setDistrict(address.getDistrict());
        response.setDetailAddress(address.getDetailAddress());

        return response;
    }

    // 获取用户地址详情
    @Override
    public AddressResponse getAddressDetails(long id) {
        // 根据地址id查询地址详情
        UmsUserAddresses address = userAddressMapper.selectById(id);
        if (address == null) {
            throw new AddressNotFoundException("用户地址不存在");
        }

        // 封装 返回值
        AddressResponse response = new AddressResponse();
        response.setId(address.getId());
        response.setName(address.getReceiverName());
        response.setPhoneNumber(address.getReceiverPhone());
        response.setDefaultStatus(address.getDefaultStatus());
        response.setProvince(address.getProvince());
        response.setCity(address.getCity());
        response.setDistrict(address.getDistrict());
        response.setDetailAddress(address.getDetailAddress());

        return response;
    }


    // 添加地址
    @Override
    public void addAddress(AddressRequest addressRequest) {

        // 解析参数,进行封装 address
        UmsUserAddresses address = new UmsUserAddresses();
        address.setUserId(getCurrentUserId());
        address.setReceiverName(addressRequest.getName());
        address.setReceiverPhone(addressRequest.getPhoneNumber());
        address.setDefaultStatus(addressRequest.getDefaultStatus());
        address.setProvince(addressRequest.getProvince());
        address.setCity(addressRequest.getCity());
        address.setDistrict(addressRequest.getDistrict());
        address.setDetailAddress(addressRequest.getDetailAddress());
        if (address == null){
            throw new AddressNotFoundException("用户地址不存在");
        }

        // 插入数据库
        userAddressMapper.insert(address);
        log.info("用户地址添加成功");
    }

    // 修改地址
    @Override
    public void modifyAddress(AddressRequest addressRequest, long id) {
        // 根据id更新地址信息
        UmsUserAddresses address = userAddressMapper.selectById(id);
        if (address == null) {
            throw new AddressNotFoundException("用户地址不存在");
        }
        address.setReceiverName(addressRequest.getName());
        address.setReceiverPhone(addressRequest.getPhoneNumber());
        address.setDefaultStatus(addressRequest.getDefaultStatus());
        address.setProvince(addressRequest.getProvince());
        address.setCity(addressRequest.getCity());
        address.setDistrict(addressRequest.getDistrict());
        address.setDetailAddress(addressRequest.getDetailAddress());
        userAddressMapper.updateById(address);
        log.info("用户地址修改成功");
    }

    // 删除地址
    @Override
    public void deleteAddress(long id) {
        // 根据id删除地址信息
        userAddressMapper.deleteById(id);
        log.info("用户地址删除成功");
    }

    // 设置默认地址
    @Override
    public void setDefaultAddress(long id) {
        // 根据id查询地址信息
        UmsUserAddresses address = userAddressMapper.selectById(id);
        if (address == null) {
            throw new AddressNotFoundException("用户地址不存在");
        }
        // 设置为默认地址，0非，1默认
        address.setDefaultStatus(1);
        userAddressMapper.updateById(address);
        log.info("用户默认地址设置成功");
    }


    /**
     * 获取当前用户ID
     * 实际项目中应该从当前请求中获取
     *
     * @return 当前用户ID
     */
    private Long getCurrentUserId() {
        // 从请求中获取当前用户ID
        try {
            // 获取当前请求
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                // 从请求头中获取token
                String token = request.getHeader("Authorization");
                if (token != null) {
                    // 使用JwtUtil解析token获取用户名
                    String username = jwtUtil.getUsernameFromToken(token);

                    // 根据用户名查询用户信息
                    LambdaQueryWrapper<UmsUsers> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(UmsUsers::getUsername, username);
                    UmsUsers user = umsUsersMapper.selectOne(queryWrapper);

                    if (user != null) {
                        return user.getId();
                    }
                }
            }

            // 如果获取失败，抛出异常或返回默认值
            log.warn("无法获取当前用户ID，请检查用户是否已登录");
            throw new RuntimeException("用户未登录");
        } catch (Exception e) {
            log.error("获取当前用户ID失败", e);
            throw new RuntimeException("获取用户信息失败", e);
        }
    }
}
