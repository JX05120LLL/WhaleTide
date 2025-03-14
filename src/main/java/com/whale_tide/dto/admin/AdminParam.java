package com.whale_tide.dto.admin;

import lombok.Data;

@Data
public class AdminParam {
    String username;
    String password;
    String email;
    String note;
    byte status;
}
