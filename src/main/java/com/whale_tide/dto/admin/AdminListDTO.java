package com.whale_tide.dto.admin;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class AdminListDTO {
    long id;
    String username;
    String email;
    LocalDateTime createTime;
    byte status;
    String note;
}
