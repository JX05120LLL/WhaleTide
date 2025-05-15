package com.whaletide.admin.user.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AdminUserInfoVO {
    String name;    //昵称
    String username;//用户名
    String avatar;  //头像
    String email;   //邮箱
    String phone;   //手机号
    LocalDateTime createdTime;  //创建时间
    LocalDateTime lastLoginTime; //最后登录时间
    List<String> roles;  //角色
}
