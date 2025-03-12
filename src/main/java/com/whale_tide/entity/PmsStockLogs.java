package com.whale_tide.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 库存变更日志表
 * </p>
 *
 * @author Bro_cat
 * @since 2025-03-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pms_stock_logs")
public class PmsStockLogs implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * SKU ID
     */
    private Long skuId;

    /**
     * 变更类型：1-销售扣减，2-退货增加，3-后台调整
     */
    private Integer changeType;

    /**
     * 变更数量
     */
    private Integer changeAmount;

    /**
     * 变更前库存
     */
    private Integer beforeStock;

    /**
     * 变更后库存
     */
    private Integer afterStock;

    /**
     * 关联订单ID
     */
    private Long orderId;

    /**
     * 关联订单项ID
     */
    private Long orderItemId;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 备注
     */
    private String remark;

    /**
     * 操作时间
     */
    private LocalDateTime createTime;


}
