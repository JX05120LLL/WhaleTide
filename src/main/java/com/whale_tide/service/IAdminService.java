package com.whale_tide.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale_tide.entity.AmsAdmins;

import java.util.List;

/**
 * 管理员服务接口
 */
public interface IAdminService {

    /**
     * 根据用户名获取管理员
     *
     * @param username 用户名
     * @return 管理员信息
     */
    AmsAdmins getAdminByUsername(String username);

    /**
     * 注册管理员
     *
     * @param admin 管理员信息
     * @return 注册成功的管理员
     */
    long register(AmsAdmins admin);

    /**
     * 登录功能
     *
     * @param username 用户名
     * @param password 密码
     * @return 生成的JWT token
     */
    String login(String username, String password);

    /**
     * 登出操作
     *
     * @param username 用户名
     * @return 登出结果
     */
    boolean logout(String username);

    /**
     * 获取管理员列表（分页）
     *
     * @param keyword  查询关键词（模糊查询）
     * @param pageNum  页码
     * @param pageSize 分页大小
     * @return 查询结果
     */
    Page<AmsAdmins> list(String keyword, long pageNum, long pageSize);

    /**
     * 刷新token
     *
     * @param oldToken 旧token
     * @return 新token
     */
    String refreshToken(String oldToken);

    /**
     * 根据管理员ID获取管理员信息
     *
     * @param id 管理员ID
     * @return 管理员信息
     */
    AmsAdmins getAdminById(Long id);

    /**
     * 更新管理员信息
     *
     * @param id    管理员ID
     * @param admin 管理员信息
     * @return 更新成功返回true
     */
    int update(AmsAdmins admin);

    /**
     * 删除指定管理员
     *
     * @param id 管理员ID
     * @return 删除成功返回true
     */
    int delete(Long id);

    /**
     * 更新管理员状态
     *
     * @param id     管理员ID
     * @param status 状态（0->禁用；1->启用）
     * @return 更新成功返回1，未更新返回0,参数错误返回-2，查找不到管理员返回-1
     */
    int updateStatus(Long id, Integer status);

    /**
     * 获取管理员拥有的角色ID列表
     *
     * @param adminId 管理员ID
     * @return 角色ID列表
     */
    List<Long> getRoleIdList(Long adminId);

    /**
     * 获取角色ID列表的所有角色名称
     *
     * @param rolesId 角色ID列表
     * @return 角色名称列表
     */
    List<String> getRoleNameList(List<Long> rolesId);

    /**
     * 获取角色对应的菜单ID表
     *
     * @param roleId 角色ID
     * @return 菜单ID表
     */
    List<Long> getMenuIdList(Long roleId);

    /**
     * 根据角色列表获取所有菜单
     *
     * @param roleIds 角色ID列表
     * @return 菜单ID列表
     */
    List<Long> getMenuIdListByRoleIds(List<Long> roleIds);

    /**
     * 获取菜单ID列表的所有菜单名称
     *
     * @param menuIds 菜单ID列表
     * @return 菜单名称列表
     */
    List<String> getMenuNameList(List<Long> menuIds);

    /**
     * 为管理员分配角色
     *
     * @param adminId 管理员ID
     * @param roleIds 角色ID列表
     * @return 分配成功返回true
     */
    boolean allocateRoles(Long adminId, List<Long> roleIds);
} 