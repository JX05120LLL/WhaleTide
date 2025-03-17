package com.whale_tide.dto.management.admin;

import lombok.Data;

import java.util.List;

@Data
public class AdminRoleParam {
    long adminId;
    List<Long> roleIds;
}
