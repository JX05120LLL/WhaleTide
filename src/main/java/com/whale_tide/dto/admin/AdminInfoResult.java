package com.whale_tide.dto.admin;

import com.whale_tide.entity.AmsMenus;
import lombok.Data;

import java.util.List;
import java.util.Map;

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
     * 菜单对象列表，包含更完整的菜单信息
     */
    private List<Map<String, Object>> menuDetails;
    /**
     * 头像URL
     */
    private String icon;
    /**
     * 权限列表
     */
    private List<String> permissions;
}
