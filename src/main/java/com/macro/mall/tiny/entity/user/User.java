package com.macro.mall.tiny.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户信息类
 */
@TableName("user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @TableId(type = IdType.AUTO)
    private long id;
    private String username;//用户名
    private String password;
    private String phone;
    private String email;
    private String nickname;//昵称
    private byte gender;//性别：0-未知，1-男，2-女 默认0
    private String avater;//头像-URL
    private byte status;//账户状态：0-禁用，1-启用  默认1
    private Date createTime;//注册时间
    private byte isMerchant;//是否为商家：0-否，1-是  默认0
    private String merchantInfo;//商家信息-JSON格式
}
