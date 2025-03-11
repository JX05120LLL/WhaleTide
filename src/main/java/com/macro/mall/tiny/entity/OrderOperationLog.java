package com.macro.mall.tiny.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@TableName("order_operation_log")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderOperationLog {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long orderId;
    private String operator;//操作人（用户或管理员）
    private String action;//操作内容（如修改地址、取消订单）
    private String note;//备注
    private Date createTime;//操作时间

}
