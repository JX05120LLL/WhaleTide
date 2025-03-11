package com.macro.mall.tiny.domain.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;

/**
 * 用户
 */
public class User {
    @TableId(type = IdType.AUTO)
    private long id;
    private String username;
    private String password;
    private String phone;
    private String email;
    private String nickname;
    private byte gender;
    private String avater;
    private Date createTime;
    private byte isMerchant;
    private String merchantInfo;
}
