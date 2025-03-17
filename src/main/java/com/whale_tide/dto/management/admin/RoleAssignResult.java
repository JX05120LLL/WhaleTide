package com.whale_tide.dto.management.admin;


import lombok.Data;

@Data
public class RoleAssignResult {
    long id;
    String name;
    String description;
    byte status;
    int sort;
    boolean checked;
}
