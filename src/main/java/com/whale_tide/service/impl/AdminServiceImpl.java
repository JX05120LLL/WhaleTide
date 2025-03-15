package com.whale_tide.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale_tide.entity.*;
import com.whale_tide.mapper.*;
import com.whale_tide.service.IAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 管理员服务实现类
 */
@Service
@Slf4j
public class AdminServiceImpl implements IAdminService {

    @Autowired
    private AmsAdminsMapper adminsMapper;

    @Autowired
    private AmsRolesMapper rolesMapper;

    @Autowired
    private AmsMenusMapper menusMapper;

    @Autowired
    private AmsPermissionsMapper permissionsMapper;

    @Autowired
    private AmsAdminRoleRelationsMapper adminRoleRelationsMapper;

    @Autowired
    private AmsRoleMenuRelationsMapper roleMenuRelationsMapper;

    @Autowired
    private AmsRolePermissionRelationsMapper rolePermissionRelationsMapper;

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    // JWT相关服务会在后续实现
    // @Autowired
    // private JwtTokenUtil jwtTokenUtil;

    @Override
    public AmsAdmins getAdminByUsername(String username) {
        if (!StringUtils.hasText(username)) {
            return null;
        }

        // 使用LambdaQueryWrapper查询
        LambdaQueryWrapper<AmsAdmins> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AmsAdmins::getUsername, username);

        return adminsMapper.selectOne(queryWrapper);
    }

    @Override
    public long register(AmsAdmins admin) {
        AmsAdmins existAdmin = getAdminByUsername(admin.getUsername());
        if (existAdmin != null) {
            // 用户名已存在
            log.warn("注册失败，用户名已存在: {}", admin.getUsername());
            return 0;
        }

        // 设置默认值
        admin.setCreateTime(LocalDateTime.now());
        admin.setStatus(1); // 默认启用

        // 加密密码
        String encodePassword = admin.getPassword();          //待修改-------密码加密---------------------------------------------------------------------------------------
//        admin.setPassword(encodePassword);

        // 保存管理员
        int result = adminsMapper.insert(admin);
        if (result > 0) {
            log.info("管理员注册成功: {}", admin.getUsername());
            LambdaQueryWrapper<AmsAdmins> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AmsAdmins::getUsername, admin.getUsername());
            queryWrapper.select(AmsAdmins::getId);
            AmsAdmins amsAdmins = adminsMapper.selectOne(queryWrapper);
            return amsAdmins.getId();
        }

        return -1L;
    }

    @Override
    public String login(String username, String password) {
        // 获取管理员信息
        AmsAdmins admin = getAdminByUsername(username);
        if (admin == null) {
            log.warn("登录失败，用户名不存在: {}", username);
            return "-2";
        }

        // 验证密码
        if (!admin.getPassword().equals(password)) {
            log.warn("登录失败，密码不正确，用户名: {}", username);
            return "0";
        }
//        if (!passwordEncoder.matches(password, admin.getPassword())) {                   待修改------------------------------------------------------------------------------------------
//            log.warn("登录失败，密码不正确，用户名: {}", username);
//            return null;
//        }

        // 更新登录时间
        admin.setLoginTime(LocalDateTime.now());
        adminsMapper.updateById(admin);

        log.info("管理员登录成功: {}", username);

        // 生成JWT token
        // 由于JWT实现可能需要额外设置，这里先返回模拟token
        // return jwtTokenUtil.generateToken(username);
        return "token:" + username;
    }

    @Override
    public boolean logout(String username) {

        log.info("管理员登出：{}", username);

        return true;
    }

    @Override
    public Page<AmsAdmins> list(String keyword, long pageNum, long pageSize) {
        log.info("查询管理员列表，关键字: {}, 页码: {}, 每页数量: {}",
                keyword, pageNum, pageSize);
        // 创建MyBatis-Plus分页对象
        Page<AmsAdmins> page = Page.of(pageNum, pageSize);
        page.addOrder(new OrderItem("username", true));
        LambdaQueryWrapper<AmsAdmins> queryWrapper = new LambdaQueryWrapper<>();
        if (!(keyword == null || keyword == "")) {
            queryWrapper.like(AmsAdmins::getUsername, keyword);
        }

        //获取查询
        page = adminsMapper.selectPage(page, queryWrapper);
        return page;
    }

    @Override
    public String refreshToken(String oldToken) {
        // 刷新JWT token
        // return jwtTokenUtil.refreshToken(oldToken);
        log.info("刷新令牌: {}", oldToken);
        return "refreshed_" + oldToken;
    }

    @Override
    public AmsAdmins getAdminById(Long id) {
        if (id == null) {
            return null;
        }
        return adminsMapper.selectById(id);
    }

    @Override
    public int update(AmsAdmins admin) {
        if (admin.getUsername() == null || admin == null) {
            return -1;
        }

        // 查询现有管理员
        AmsAdmins existAdmin = getAdminById(admin.getId());
        if (existAdmin == null) {
            log.warn("更新管理员失败，用户ID不存在: {}", admin.getId());
            return -2;
        }

        int result_ = 0;
        // 如果没有设置密码，使用原密码
        if (!StringUtils.hasText(admin.getPassword())) {
            admin.setPassword(existAdmin.getPassword());
        } else {
            // 如果设置了新密码，需要加密
            admin.setPassword(admin.getPassword());       //待修改-----------------------加密-----------------------------------------------------------------------------------
            result_++;
        }

        // 保留创建时间
        admin.setCreateTime(existAdmin.getCreateTime());

        // 更新
        int result = adminsMapper.updateById(admin);
        if (result > 0) {
            log.info("管理员信息更新成功，ID: {}", admin.getId());
            if (!admin.getUsername().equals(existAdmin.getUsername()))
                result_++;
            if (!admin.getEmail().equals(existAdmin.getEmail()))
                result_++;
            if (!admin.getNote().equals(existAdmin.getNote()))
                result_++;
            if (!admin.getStatus().equals(existAdmin.getStatus()))
                result_++;
            return result_;
        }

        log.warn("管理员信息更新失败，ID: {}", admin.getId());
        return -3;
    }

    @Override
    public int delete(Long id) {
        if (id == null) {
            return -2;
        }

        // 查询现有管理员
        AmsAdmins existAdmin = getAdminById(id);
        if (existAdmin == null) {
            log.warn("删除管理员失败，ID不存在: {}", id);
            return -1;
        }

        // 删除管理员
        int result = adminsMapper.deleteById(id);

        // 删除关联的角色关系
        LambdaQueryWrapper<AmsAdminRoleRelations> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AmsAdminRoleRelations::getAdminId, id);
        adminRoleRelationsMapper.delete(queryWrapper);

        log.info("删除管理员及其角色关系，ID: {}, 结果: {}", id, result > 0);
        return result;
    }

    @Override
    public int updateStatus(Long id, Integer status) {
        if (id == null || status == null) {
            return -2;
        }

        // 查询现有管理员
        AmsAdmins existAdmin = getAdminById(id);
        if (existAdmin == null) {
            log.warn("更新管理员状态失败，ID不存在: {}", id);
            return -1;
        }

        // 更新状态
        existAdmin.setStatus(status);
        int result = adminsMapper.updateById(existAdmin);

        log.info("更新管理员状态，ID: {}, 新状态: {}, 结果: {}", id, status, result > 0);
        return result;
    }

    @Override
    public List<Long> getMenuIdList(Long roleId) {
        if (roleId == null) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<AmsRoleMenuRelations> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AmsRoleMenuRelations::getRoleId, roleId);
        List<AmsRoleMenuRelations> relations = roleMenuRelationsMapper.selectList(queryWrapper);

        List<Long> menuIds = new ArrayList<>();
        for (AmsRoleMenuRelations amsRoleMenuRelations_ : relations) {
            menuIds.add(amsRoleMenuRelations_.getMenuId());
        }

        return menuIds;
    }

    @Override
    public List<Long> getMenuIdListByRoleIds(List<Long> roleIds) {
        Set<Long> retMenuIds = new HashSet<>();
        for (long roleId : roleIds) {
            List<Long> menuIds = getMenuIdList(roleId);
            retMenuIds.addAll(menuIds);
        }
        return new ArrayList<>(retMenuIds);
    }

    @Override
    public List<String> getMenuNameList(List<Long> menuIds) {
        if (menuIds == null || menuIds.size() == 0) {
            return new ArrayList<>();
        }
        List<String> menuNameList = new ArrayList<>();
        for (Long menuId : menuIds) {
            menuNameList.add(menusMapper.selectById(menuId).getTitle());
        }
        return menuNameList;
    }

    @Override
    public List<AmsRoles> getRoleListByAdminId(Long adminId) {
        if (adminId == null) {
            return null;
        }

        //查询管理员ID是否存在
        AmsAdmins admin = adminsMapper.selectById(adminId);
        if (admin == null) {
            log.warn("管理员ID不存在: {}", adminId);
            return null;
        }

        //查询管理员角色关系
        List<Long> roleIds = getRoleIdList(adminId);

        //查询角色信息
        List<AmsRoles> roles = new ArrayList<>();
        for (Long roleId : roleIds) {
            roles.add(rolesMapper.selectById(roleId));
        }

        log.info("获取管理员角色列表，管理员ID: {}, 角色数量: {}", adminId, roles.size());
        return roles;
    }

    @Override
    public List<Long> getRoleIdList(Long adminId) {
        if (adminId == null) {
            return new ArrayList<>();
        }

        // 查询管理员角色关系
        LambdaQueryWrapper<AmsAdminRoleRelations> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AmsAdminRoleRelations::getAdminId, adminId);

        List<AmsAdminRoleRelations> relations = adminRoleRelationsMapper.selectList(queryWrapper);

        // 转换为角色ID列表
        List<Long> roleIds = relations.stream()
                .map(AmsAdminRoleRelations::getRoleId)
                .collect(Collectors.toList());

        log.info("获取管理员角色列表，管理员ID: {}, 角色数量: {}", adminId, roleIds.size());
        return roleIds;
    }

    @Override
    public List<String> getRoleNameList(List<Long> rolesId) {
        List<String> roles = new ArrayList<>();
        for (long roleId : rolesId) {
            roles.add(rolesMapper.selectById(roleId).getName());
        }
        log.info("获取管理员角色列表名称，角色ID数量: {}, 角色数量: {}", rolesId.size(), roles.size());
        return roles;
    }

    @Override
    @Transactional
    public int allocateRoles(Long adminId, List<Long> roleIds) {
        if (adminId == null) {
            return -2;
        }

        //查询管理员ID是否存在
        AmsAdmins admin = adminsMapper.selectById(adminId);
        if (admin == null) {
            log.warn("管理员ID不存在: {}", adminId);
            return -1;
        }

        //查询角色ID是否存在
        List<AmsRoles> roles = new ArrayList<>();
        for (Long roleId : roleIds) {
            AmsRoles role = rolesMapper.selectById(roleId);
            if (role == null) {
                log.warn("角色ID不存在: {}", roleId);
                return -3;
            }
            roles.add(role);
        }


        log.info("开始为管理员分配角色，管理员ID: {}, 角色数量: {}",
                adminId, roleIds == null ? 0 : roleIds.size());

        // 先删除原有关系
        LambdaQueryWrapper<AmsAdminRoleRelations> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AmsAdminRoleRelations::getAdminId, adminId);
        adminRoleRelationsMapper.delete(queryWrapper);

        // 如果没有新的角色，直接返回
        if (roleIds == null || roleIds.isEmpty()) {
            log.info("管理员角色已清空，管理员ID: {}", adminId);
            return -3;
        }

        // 批量添加新关系
        int successCount = 0;
        for (Long roleId : roleIds) {
            AmsAdminRoleRelations relation = new AmsAdminRoleRelations();
            relation.setAdminId(adminId);
            relation.setRoleId(roleId);
            successCount += adminRoleRelationsMapper.insert(relation);
        }

        log.info("完成角色分配，管理员ID: {}, 分配角色数: {}, 成功: {}",
                adminId, roleIds.size(), successCount == roleIds.size());

        return roleIds.size();
    }

    @Override
    public List<Long> getPermissionIdList(Long roledId) {
        LambdaQueryWrapper<AmsRolePermissionRelation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AmsRolePermissionRelation::getRoleId, roledId);

        List<Long> permissionIds = rolePermissionRelationsMapper.selectList(queryWrapper).stream()
                .map(AmsRolePermissionRelation::getPermissionId)
                .collect(Collectors.toList());
        return permissionIds;
    }

    @Override
    public List<Long> getPermissionIdListByAdminId(Long adminId) {
        if (adminId == null) {
            return new ArrayList<>();
        }

        //查询管理员是否存在
        AmsAdmins admin = adminsMapper.selectById(adminId);
        if (admin == null) {
            log.warn("管理员ID不存在: {}", adminId);
            return new ArrayList<>();
        }

        //查询角色ID列表
        List<Long> roleIds = getRoleIdList(adminId);

        //查询角色权限关系
        Set<Long> permissionIds = new HashSet<>();
        for (Long roleId : roleIds) {
            List<Long> permissionId = getPermissionIdList(roleId);
            permissionIds.addAll(permissionId);
        }

        return new ArrayList<>(permissionIds);
    }

    @Override
    public List<String> getPermissionNameList(List<Long> permissionIds) {
        if (permissionIds == null || permissionIds.size() == 0) {
            return new ArrayList<>();
        }
        List<String> permissionNameList = new ArrayList<>();
        for (Long permissionId : permissionIds) {
            //查询ID是否存在
            AmsPermissions permission = permissionsMapper.selectById(permissionId);
            if (permission == null) {
                log.warn("权限ID不存在: {}", permissionId);
            } else {
                permissionNameList.add(permissionsMapper.selectById(permissionId).getName());
            }
        }
        return permissionNameList;
    }

    @Override
    public List<String> getPermissionNameListByAdminId(Long adminId) {
        if (adminId == null) {
            return new ArrayList<>();
        }

        //查询管理员是否存在
        AmsAdmins admin = adminsMapper.selectById(adminId);
        if (admin == null) {
            log.warn("管理员ID不存在: {}", adminId);
            return new ArrayList<>();
        }

        //查询权限ID列表
        List<Long> permissionIds = getPermissionIdListByAdminId(adminId);

        //查询权限名称
        List<String> permissionNameList = getPermissionNameList(permissionIds);

        return permissionNameList;
    }
}