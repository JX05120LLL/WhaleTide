package com.whale_tide.dto.management.admin;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class AdminResult {
    long id;
    String username;
    String email;
    LocalDateTime createTime;
    byte status;
    String note;
}
