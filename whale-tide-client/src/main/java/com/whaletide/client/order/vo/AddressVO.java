package com.whaletide.client.order.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 收货地址VO
 */
@Data
@Schema(description = "收货地址信息")
public class AddressVO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**
     * 地址ID
     */
    @Schema(description = "地址ID")
    private Long id;
    
    /**
     * 收货人姓名
     */
    @Schema(description = "收货人姓名")
    private String name;
    
    /**
     * 收货人电话
     */
    @Schema(description = "收货人电话")
    private String phone;
    
    /**
     * 省份
     */
    @Schema(description = "省份")
    private String province;
    
    /**
     * 城市
     */
    @Schema(description = "城市")
    private String city;
    
    /**
     * 区县
     */
    @Schema(description = "区县")
    private String district;
    
    /**
     * 详细地址
     */
    @Schema(description = "详细地址")
    private String detail;
    
    /**
     * 是否为默认地址
     */
    @Schema(description = "是否为默认地址")
    private Boolean isDefault;
} 