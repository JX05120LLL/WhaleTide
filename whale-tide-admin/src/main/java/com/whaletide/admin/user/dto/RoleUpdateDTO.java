package com.whaletide.admin.user.dto;

import lombok.Data;

@Data
public class RoleUpdateDTO {
    Long id;
    String name;
    String description;
    Boolean status;
}
