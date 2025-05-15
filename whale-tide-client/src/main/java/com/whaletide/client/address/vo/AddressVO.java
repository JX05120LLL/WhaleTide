package com.whaletide.client.address.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 地址VO
 */
@Data
@Schema(description = "收货地址响应")
public class AddressVO {
    
    @Schema(description = "地址ID")
    private Long id;
    
    @Schema(description = "收货人姓名")
    private String receiverName;
    
    @Schema(description = "收货人电话")
    private String receiverPhone;
    
    @Schema(description = "省份")
    private String province;
    
    @Schema(description = "城市")
    private String city;
    
    @Schema(description = "区县")
    private String district;
    
    @Schema(description = "详细地址")
    private String detailAddress;
    
    @Schema(description = "完整地址")
    private String fullAddress;
    
    @Schema(description = "邮政编码")
    private String postalCode;
    
    @Schema(description = "是否默认地址：0-否，1-是")
    private Integer defaultStatus;
    
    @Schema(description = "地址标签：家、公司等")
    private String tag;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
} 