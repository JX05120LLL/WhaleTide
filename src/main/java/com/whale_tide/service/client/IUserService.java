package com.whale_tide.service.client;


import com.whale_tide.common.api.CommonResult;
import com.whale_tide.dto.client.user.LoginResponse;
import com.whale_tide.dto.client.user.RegisterRequest;
import com.whale_tide.dto.client.user.UserInfoResponse;
import com.whale_tide.entity.ums.UmsUsers;

/**
 * 用户服务接口
 * @author Bro_cat
 * @date 2025.03.17
 */

public interface IUserService {

    //用户登录
    LoginResponse login(String username, String password);

    //获取用户信息
    UserInfoResponse getUserInfo(String username);

    //发送验证码
    String sendVerificationCode(String phone);

    //用户注册
    void register(RegisterRequest registerRequest);


}
