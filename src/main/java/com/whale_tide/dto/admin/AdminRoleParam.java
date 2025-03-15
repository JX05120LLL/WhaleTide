package com.whale_tide.dto.admin;

import lombok.Data;

import java.util.List;

@Data
public class AdminRoleParam {
    long admin_id;
    List<Long> role_ids;
}
