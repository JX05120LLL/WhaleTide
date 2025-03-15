package com.whale_tide.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel("订单详情返回结果")
public class OrderDetailResult {
    @ApiModelProperty("订单ID")
    private Long id;
    
    @ApiModelProperty("订单编号")
    private String orderSn;
    
    @ApiModelProperty("订单状态：0-待付款，1-待发货，2-已发货，3-已完成，4-已关闭，5-已取消")
    private Integer status;
    
    @ApiModelProperty("订单总金额")
    private BigDecimal totalAmount;
    
    @ApiModelProperty("实付金额")
    private BigDecimal payAmount;
    
    @ApiModelProperty("优惠金额")
    private BigDecimal discountAmount;
    
    @ApiModelProperty("运费金额")
    private BigDecimal freightAmount;
    
    @ApiModelProperty("促销优惠金额")
    private BigDecimal promotionAmount;
    
    @ApiModelProperty("积分抵扣金额")
    private BigDecimal integrationAmount;
    
    @ApiModelProperty("优惠券抵扣金额")
    private BigDecimal couponAmount;
    
    @ApiModelProperty("支付方式：0-未支付，1-支付宝，2-微信，3-银联")
    private Integer payType;
    
    @ApiModelProperty("订单来源：0-PC, 1-App, 2-小程序, 3-H5")
    private Integer sourceType;
    
    @ApiModelProperty("订单类型：0-普通订单，1-秒杀订单，2-团购订单")
    private Integer orderType;
    
    @ApiModelProperty("支付时间")
    private LocalDateTime paymentTime;
    
    @ApiModelProperty("发货时间")
    private LocalDateTime deliveryTime;
    
    @ApiModelProperty("收货时间")
    private LocalDateTime receiveTime;
    
    @ApiModelProperty("评论时间")
    private LocalDateTime commentTime;
    
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
    
    @ApiModelProperty("自动确认收货天数")
    private Integer autoConfirmDay;
    
    @ApiModelProperty("物流公司")
    private String deliveryCompany;
    
    @ApiModelProperty("物流单号")
    private String deliverySn;
    
    @ApiModelProperty("订单备注")
    private String orderNote;
    
    @ApiModelProperty("会员用户名")
    private String memberUsername;
    
    @ApiModelProperty("收货人姓名")
    private String receiverName;
    
    @ApiModelProperty("收货人电话")
    private String receiverPhone;
    
    @ApiModelProperty("收货人邮编")
    private String receiverPostCode;
    
    @ApiModelProperty("省份")
    private String receiverProvince;
    
    @ApiModelProperty("城市")
    private String receiverCity;
    
    @ApiModelProperty("区域")
    private String receiverRegion;
    
    @ApiModelProperty("详细地址")
    private String receiverDetailAddress;
    
    @ApiModelProperty("确认收货状态：0-未确认，1-已确认")
    private Integer confirmStatus;
    
    @ApiModelProperty("删除状态：0-未删除，1-已删除")
    private Integer deleteStatus;
    
    @ApiModelProperty("赠送的积分")
    private Integer integration;
    
    @ApiModelProperty("赠送的成长值")
    private Integer growth;
    
    @ApiModelProperty("订单商品列表")
    private List<OrderItemDTO> orderItemList;
    
    @ApiModelProperty("订单操作历史记录")
    private List<OrderHistoryDTO> historyList;
}

