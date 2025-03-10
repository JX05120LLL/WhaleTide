package com.macro.mall.tiny.domain.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * 用户地址
 */
public class UserAddress {
    @TableId(type = IdType.AUTO)
    private long id;
    private long userId;
    private String name;
    private String phone;
    private String province;
    private String city;
    private String district;
    private String detailAddress;
    private byte isDefault;
}
