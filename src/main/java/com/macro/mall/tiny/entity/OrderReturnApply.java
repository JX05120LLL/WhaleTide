package com.macro.mall.tiny.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@TableName("order_return_apply")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderReturnApply {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long orderId;
    private Long userId;
    private String reason;//退货原因
    private Byte status;//处理状态：0-待处理，1-已同意，2-已拒绝
    private String handleNote;//处理备注
    private Date handleTime;//处理时间
    private Date createTime;//申请时间
}
