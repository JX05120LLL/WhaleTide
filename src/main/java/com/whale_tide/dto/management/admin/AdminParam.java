package com.whale_tide.dto.management.admin;

import lombok.Data;

@Data
public class AdminParam {
    String username;
    String password;
    String email;
    String note;
    byte status;
}
