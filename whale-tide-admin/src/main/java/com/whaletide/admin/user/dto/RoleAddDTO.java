package com.whaletide.admin.user.dto;

import lombok.Data;

@Data
public class RoleAddDTO {
    String name;
    String description;
    Boolean status;
}
