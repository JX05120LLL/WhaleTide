package com.whaletide.client.order.dto;

import com.whaletide.common.api.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 订单列表查询参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "订单列表查询参数")
public class OrderListQueryDTO extends BasePageQuery {
    private static final long serialVersionUID = 1L;

    @Schema(description = "订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单")
    private Integer status;
} 