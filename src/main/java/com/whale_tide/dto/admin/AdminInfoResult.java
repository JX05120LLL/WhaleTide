package com.whale_tide.dto.admin;

import lombok.Data;

import java.util.List;

@Data
public class AdminInfoResult {
    private String username;
    /**
     * 角色列表
     */
    private List<String> roles;
    /**
     * 可访问菜单列表
     */
    private List<String> menus;
    /**
     * 头像URL
     */
    private String icon;
    /**
     * 权限列表
     */
    private List<String> permissions;

}
