package com.macro.mall.tiny.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户地址信息类
 */
@TableName("user_address")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAddress {
    @TableId(type = IdType.AUTO)
    private long id;
    private long userId;//用户ID
    private String name;//收货人称呼
    private String phone;
    private String province;//收货地址-省级
    private String city;//收货地址-市级
    private String district;//收货地址-区级
    private String detailAddress;//收货地址-详细地址
    private byte isDefault;//是否默认地址：0-否，1-是  默认0
}
