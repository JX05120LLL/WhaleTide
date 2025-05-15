package com.whaletide.client.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 生成订单请求DTO
 */
@Data
@Schema(description = "生成订单请求")
public class GenerateOrderDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**
     * 购物车项目ID列表
     */
    @NotEmpty(message = "购物车项目不能为空")
    @Schema(description = "购物车项目ID列表", required = true)
    private List<Long> cartItemIds;
    
    /**
     * 收货地址ID
     */
    @NotNull(message = "收货地址不能为空")
    @Schema(description = "收货地址ID", required = true)
    private Long addressId;
    
    /**
     * 支付方式：1-支付宝；2-微信支付；3-银联支付
     */
    @Schema(description = "支付方式：1-支付宝；2-微信支付；3-银联支付")
    private Integer payType = 1;
    
    /**
     * 订单备注
     */
    @Schema(description = "订单备注")
    private String note;
} 