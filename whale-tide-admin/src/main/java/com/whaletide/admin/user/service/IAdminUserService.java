package com.whaletide.admin.user.service;

import com.whaletide.admin.user.vo.AdminUserInfoVO;
import com.whaletide.admin.user.vo.AdminUserLoginVO;

public interface IAdminUserService {
    /**
     * 登录
     */
    AdminUserLoginVO login(String username, String password);

    /**
     * 查询用户信息
     */
    AdminUserInfoVO getUserInfo(String userName);

    /**
     * 登出
     */
    void logout(String token);


}
