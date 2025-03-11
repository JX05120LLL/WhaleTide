package com.macro.mall.tiny.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户优惠券信息类
 */
@TableName("user_coupon")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCoupon {
    @TableId(type = IdType.AUTO)
    private long id;
    private long userId;//用户ID
    private long couponId;//优惠券ID
    private int status;//状态：0-未使用，1-已使用，2-已过期  默认0
    private Date acquireTime;//领取时间
    private Date useTime;//使用时间
}
