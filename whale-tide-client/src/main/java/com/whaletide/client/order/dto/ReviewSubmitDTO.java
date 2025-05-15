package com.whaletide.client.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Schema(description = "评价提交DTO")
public class ReviewSubmitDTO {

    @NotNull(message = "订单ID不能为空")
    @Schema(description = "订单ID")
    private Long orderId;

    @NotNull(message = "订单商品ID不能为空")
    @Schema(description = "订单商品ID")
    private Long orderItemId;

    @NotNull(message = "商品ID不能为空")
    @Schema(description = "商品ID")
    private Long productId;

    @NotEmpty(message = "评价内容不能为空")
    @Schema(description = "评价内容")
    private String content;

    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分最小为1")
    @Max(value = 5, message = "评分最大为5")
    @Schema(description = "评分(1-5)")
    private Integer star;

    @Schema(description = "是否匿名")
    private Boolean anonymous;

    @Schema(description = "图片列表")
    private List<String> pics;
} 