package com.whaletide.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 支付类型枚举
 */
@Getter
public enum PayTypeEnum {
    /**
     * 微信支付
     */
    WECHAT_PAY(1, "微信支付"),
    
    /**
     * 支付宝
     */
    ALIPAY(2, "支付宝"),
    
    /**
     * 银联
     */
    UNION_PAY(3, "银联"),
    
    /**
     * 余额支付
     */
    BALANCE_PAY(4, "余额支付");
    
    @EnumValue
    @JsonValue
    private final Integer value;
    
    private final String desc;
    
    PayTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
    
    /**
     * 根据值获取枚举
     */
    public static PayTypeEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (PayTypeEnum type : PayTypeEnum.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }
} 