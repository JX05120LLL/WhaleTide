package com.whale_tide.service.management;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale_tide.dto.management.resource.ResourceResult;
import com.whale_tide.dto.management.menu.MenuNodeResult;
import com.whale_tide.dto.management.role.RoleParam;
import com.whale_tide.dto.management.role.RoleResult;
import com.whale_tide.entity.ams.AmsAdmins;
import com.whale_tide.entity.ams.AmsRoles;

import java.util.List;
import java.util.Map;

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
     * 获取角色列表
     *
     * @param adminId 管理员ID
     * @return 角色列表
     */
    List<AmsRoles> getRoleListByAdminId(Long adminId);

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
     * 获取菜单详情列表
     *
     * @param menuIds 菜单ID列表
     * @return 菜单详情对象列表
     */
    List<Map<String, Object>> getMenuDetailList(List<Long> menuIds);

    /**
     * 为管理员分配角色
     *
     * @param adminId 管理员ID
     * @param roleIds 角色ID列表
     * @return 分配成功返回true
     */
    int allocateRoles(Long adminId, List<Long> roleIds);

    /**
     * 获取管理员的权限ID列表
     *
     * @param roleId 管理员ID
     * @return 权限ID列表
     */
    List<Long> getPermissionIdList(Long roleId);

    /**
     * 获取管理员的权限ID列表
     *
     * @param adminId 管理员ID
     * @return 权限ID列表
     */
    List<Long> getPermissionIdListByAdminId(Long adminId);

    /**
     * 获取权限ID列表的所有权限名称
     *
     * @param permissionIds 权限ID列表
     * @return 权限名称列表
     */
    List<String> getPermissionNameList(List<Long> permissionIds);

    /**
     * 获取管理员的权限名称列表
     *
     * @param adminId 管理员ID
     * @return 权限名称列表
     */
    List<String> getPermissionNameListByAdminId(Long adminId);
    
    /**
     * 获取角色列表（分页）
     *
     * @param keyword  查询关键词（模糊查询）
     * @param pageNum  页码
     * @param pageSize 分页大小
     * @return 查询结果
     */
    IPage<RoleResult> listRoles(String keyword, Integer pageNum, Integer pageSize);
    
    /**
     * 获取所有角色
     *
     * @return 角色列表
     */
    List<RoleResult> listAllRoles();
    
    /**
     * 创建角色
     *
     * @param roleParam 角色参数
     * @return 创建成功返回true
     */
    boolean createRole(RoleParam roleParam);
    
    /**
     * 更新角色
     *
     * @param id        角色ID
     * @param roleParam 角色参数
     * @return 更新成功返回true
     */
    boolean updateRole(Long id, RoleParam roleParam);
    
    /**
     * 批量删除角色
     *
     * @param ids 角色ID列表
     * @return 删除成功返回true
     */
    boolean deleteRoles(List<Long> ids);
    
    /**
     * 更新角色状态
     *
     * @param id     角色ID
     * @param status 状态（0->禁用；1->启用）
     * @return 更新成功返回true
     */
    boolean updateRoleStatus(Long id, Integer status);
    
    /**
     * 获取角色相关菜单
     *
     * @param roleId 角色ID
     * @return 菜单列表
     */
    List<MenuNodeResult> listMenuByRole(Long roleId);
    
    /**
     * 获取角色相关资源
     *
     * @param roleId 角色ID
     * @return 资源列表
     */
    List<ResourceResult> listResourceByRole(Long roleId);
    
    /**
     * 分配角色菜单
     *
     * @param roleId  角色ID
     * @param menuIds 菜单ID列表
     * @return 分配成功返回true
     */
    boolean allocMenu(Long roleId, List<Long> menuIds);
    
    /**
     * 分配角色资源
     *
     * @param roleId      角色ID
     * @param resourceIds 资源ID列表
     * @return 分配成功返回true
     */
    boolean allocResource(Long roleId, List<Long> resourceIds);
} 