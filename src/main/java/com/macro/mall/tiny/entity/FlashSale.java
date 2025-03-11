package com.macro.mall.tiny.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@TableName("flash_sale")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlashSale {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;//活动名称
    private Long productId;
    private Double price;//秒杀价
    private Integer stock;//秒杀库存
    private Date startTime;
    private Date endTime;
    private Byte status;//状态（0-未开始，1-进行中，2-已结束）


}
