package com.whale_tide.service;

import com.whale_tide.entity.AmsAdmins;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 管理员服务接口
 */
public interface IAdminService {
    
    /**
     * 根据用户名获取管理员
     * @param username 用户名
     * @return 管理员信息
     */
    AmsAdmins getAdminByUsername(String username);
    
    /**
     * 注册管理员
     * @param admin 管理员信息
     * @return 注册成功的管理员
     */
    AmsAdmins register(AmsAdmins admin);
    
    /**
     * 登录功能
     * @param username 用户名
     * @param password 密码
     * @return 生成的JWT token
     */
    String login(String username, String password);
    
    /**
     * 刷新token
     * @param oldToken 旧token
     * @return 新token
     */
    String refreshToken(String oldToken);
    
    /**
     * 根据管理员ID获取管理员信息
     * @param id 管理员ID
     * @return 管理员信息
     */
    AmsAdmins getAdminById(Long id);
    
    /**
     * 根据管理员名称或昵称分页查询管理员列表
     * @param keyword 关键字
     * @param pageable 分页参数
     * @return 管理员分页列表
     */
    Page<AmsAdmins> list(String keyword, Pageable pageable);
    
    /**
     * 更新管理员信息
     * @param id 管理员ID
     * @param admin 管理员信息
     * @return 更新成功返回true
     */
    boolean update(Long id, AmsAdmins admin);
    
    /**
     * 删除指定管理员
     * @param id 管理员ID
     * @return 删除成功返回true
     */
    boolean delete(Long id);
    
    /**
     * 更新管理员状态
     * @param id 管理员ID
     * @param status 状态（0->禁用；1->启用）
     * @return 更新成功返回true
     */
    boolean updateStatus(Long id, Integer status);
    
    /**
     * 获取管理员拥有的角色ID列表
     * @param adminId 管理员ID
     * @return 角色ID列表
     */
    List<Long> getRoleList(Long adminId);

    /**
     * 获取角色ID列表的所有角色名称
     * @param rolesId 角色列表ID
     * @return 角色名称列表
     */
    List<String> getRoleNameList(List<Long> rolesId);

    List<Long> get

    /**
     * 为管理员分配角色
     * @param adminId 管理员ID
     * @param roleIds 角色ID列表
     * @return 分配成功返回true
     */
    boolean allocateRoles(Long adminId, List<Long> roleIds);
} 