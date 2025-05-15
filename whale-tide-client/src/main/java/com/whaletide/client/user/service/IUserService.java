package com.whaletide.client.user.service;

import com.whaletide.client.user.dto.UpdatePasswordDTO;
import com.whaletide.client.user.dto.UserRegisterDTO;
import com.whaletide.client.user.vo.LoginResponse;
import com.whaletide.client.user.vo.UserInfoResponse;
import com.whaletide.client.user.vo.UserVO;
import com.whaletide.common.api.CommonResult;

/**
 * 用户服务接口
 */
public interface IUserService {
    
    /**
     * 用户注册
     * @param userRegisterDTO 注册信息
     * @return 生成的JWT令牌
     */
    void register(UserRegisterDTO userRegisterDTO);
    
    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 生成的JWT令牌
     */
    String login(String username, String password);

    
    /**
     * 获取当前登录用户信息
     * @return 用户信息
     */
    UserVO getCurrentUser();
    
    /**
     * 用户登出
     */
    Boolean logout();
    
    /**
     * 更新用户信息
     * @param userVO 用户信息
     */
    Boolean updateUserInfo(UserVO userVO);
    
    /**
     * 修改密码
     * @param updatePasswordDTO 密码修改信息
     */
    Boolean updatePassword(UpdatePasswordDTO updatePasswordDTO);

    /**
     * 获取用户信息
     * @param username
     * @return
     */
    UserInfoResponse getUserInfo(String username);
} 