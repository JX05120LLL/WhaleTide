package com.macro.mall.tiny.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@TableName("coupon")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Coupon {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;//优惠劵名称
    private Byte type;//类型（1-满减，2-折扣）
    private Double amount;//优惠金额/折扣比例
    private Double minAmount;//最低消费金额
    private Date startTime;//生效时间
    private Date endTime;//过期时间
    private Integer total;//发放总量
    private Integer used;//已使用数量
    private Byte status;//状态（0-禁用，1-启用）

}
