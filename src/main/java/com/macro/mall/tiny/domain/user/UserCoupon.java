package com.macro.mall.tiny.domain.user;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;

/**
 * 用户优惠券
 */
public class UserCoupon {
    @TableId(type = IdType.AUTO)
    private long id;
    private long userId;
    private long couponId;
    private int status;
    private Date acquireTime;
    private Date useTime;

}
