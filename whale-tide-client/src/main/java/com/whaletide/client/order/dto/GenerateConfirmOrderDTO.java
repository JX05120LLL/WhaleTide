package com.whaletide.client.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * 生成确认订单DTO
 */
@Data
@Schema(description = "生成确认订单请求")
public class GenerateConfirmOrderDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**
     * 购物车项目ID列表
     */
    @NotEmpty(message = "购物车项目不能为空")
    @Schema(description = "购物车项目ID列表", required = true)
    private List<Long> cartItemIds;
} 