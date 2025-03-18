package com.whale_tide.dto.client.user;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 获取用户信息响应参数
 */

@Data
public class UserInfoResponse {

    @ApiModelProperty("用户ID")
    private Long id;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("昵称")
    private String nickname;
    @ApiModelProperty("头像")
    private String icon;
    @ApiModelProperty("积分")
    private Integer integration; // 积分
    @ApiModelProperty("成长值")
    private Integer growth;// 成长值


}
