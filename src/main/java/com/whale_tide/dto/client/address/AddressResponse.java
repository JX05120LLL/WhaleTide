package com.whale_tide.dto.client.address;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "获取收获地址列表返回对象")
public class AddressResponse {

    @ApiModelProperty(value = "收获地址ID", required = true)
    private Long id;
    @ApiModelProperty("收货人姓名")
    private String name;
    @ApiModelProperty("收货人手机号")
    private String phoneNumber;
    @ApiModelProperty("是否默认地址")
    private Integer defaultStatus;// 0->非默认；1->默认
    @ApiModelProperty("省")
    private String province;
    @ApiModelProperty("城市")
    private String city;
    @ApiModelProperty("区县")
    private String district;
    @ApiModelProperty("详细地址")
    private String detailAddress;

}
