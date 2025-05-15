package com.whaletide.client.order.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 优惠券VO
 */
@Data
@Schema(description = "优惠券")
public class CouponVO {
    
    @Schema(description = "优惠券ID")
    private Long id;
    
    @Schema(description = "优惠券名称")
    private String name;
    
    @Schema(description = "优惠券类型：0->全场赠券；1->会员赠券；2->购物赠券；3->注册赠券")
    private Integer type;
    
    @Schema(description = "优惠券面额")
    private Double amount;
    
    @Schema(description = "使用门槛：0->无门槛；其他->需要消费满足该值")
    private Double minPoint;
    
    @Schema(description = "有效期开始时间")
    private Date startTime;
    
    @Schema(description = "有效期结束时间")
    private Date endTime;
    
    @Schema(description = "使用说明")
    private String note;
} 