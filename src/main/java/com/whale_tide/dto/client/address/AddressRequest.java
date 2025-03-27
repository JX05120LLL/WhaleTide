package com.whale_tide.dto.client.address;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 添加地址请求对象
 */

@Data
@ApiModel(description = "地址请求对象")
public class AddressRequest {

    @ApiModelProperty("收货人姓名")
    private String name;
    @ApiModelProperty("收货人手机号")
    private String phoneNumber;
    @ApiModelProperty("是否默认地址")
    private Integer defaultStatus; // 0->非默认；1->默认
    @ApiModelProperty("省份")
    private String province;
    @ApiModelProperty("城市")
    private String city;
    @ApiModelProperty("区县")
    private String district;
    @ApiModelProperty("详细地址")
    private String detailAddress;

}
