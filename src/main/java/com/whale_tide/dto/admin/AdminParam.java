package com.whale_tide.dto.admin;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminParam {
    String username;
    String password;
    String email;
    String note;
    byte status;
}
