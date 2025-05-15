package com.whaletide.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 通用状态枚举
 */
@Getter
public enum StatusEnum {
    /**
     * 禁用
     */
    DISABLED(0, "禁用"),
    
    /**
     * 启用
     */
    ENABLED(1, "启用");
    
    @EnumValue
    @JsonValue
    private final Integer value;
    
    private final String desc;
    
    StatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
    
    /**
     * 根据值获取枚举
     */
    public static StatusEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (StatusEnum status : StatusEnum.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        return null;
    }
} 