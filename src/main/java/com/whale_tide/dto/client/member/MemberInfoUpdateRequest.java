package com.whale_tide.dto.client.member;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
/**
 * 修改用户信息请求对象
 */
@Data
@ApiModel(description = " 用户信息更新请求对象  ")
public class MemberInfoUpdateRequest {
    private String username;
    private String nickname;
    private String phone;
    private Integer gender;
    private LocalDate birthday;
    private String city;
    private String job;
}
