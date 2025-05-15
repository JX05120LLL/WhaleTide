package com.whaletide.admin.user.vo;

import lombok.Data;

@Data
public class AdminUserLoginVO {
    String username;
    Long userId;
    String token;
}
