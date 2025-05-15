package com.whaletide.client.address.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 地址DTO
 */
@Data
@Schema(description = "收货地址请求")
public class AddressDTO {
    
    @Schema(description = "地址ID，新增时不需要传")
    private Long id;
    
    @Schema(description = "收货人姓名", required = true)
    @NotBlank(message = "收货人姓名不能为空")
    @Size(max = 20, message = "收货人姓名长度不能超过20个字符")
    private String receiverName;
    
    @Schema(description = "收货人电话", required = true)
    @NotBlank(message = "收货人电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String receiverPhone;
    
    @Schema(description = "省份", required = true)
    @NotBlank(message = "省份不能为空")
    private String province;
    
    @Schema(description = "城市", required = true)
    @NotBlank(message = "城市不能为空")
    private String city;
    
    @Schema(description = "区县", required = true)
    @NotBlank(message = "区县不能为空")
    private String district;
    
    @Schema(description = "详细地址", required = true)
    @NotBlank(message = "详细地址不能为空")
    @Size(max = 200, message = "详细地址长度不能超过200个字符")
    private String detailAddress;
    
    @Schema(description = "邮政编码")
    @Pattern(regexp = "^[0-9]{6}$", message = "邮政编码格式不正确")
    private String postalCode;
    
    @Schema(description = "是否默认地址：0-否，1-是")
    private Integer defaultStatus;
    
    @Schema(description = "地址标签：家、公司等")
    @Size(max = 10, message = "地址标签长度不能超过10个字符")
    private String tag;
} 