package com.macro.mall.tiny.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@TableName("stock_log")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockLog {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long productId;
    private Long skuId;
    private Integer changeType;//变更类型：1-销售扣减，2-退货增加，3-手动调整
    private Integer changeAmount;//变更数量
    private String remark;//备注
    private String operator;//操作人
    private Date createTime;//操作时间

}
