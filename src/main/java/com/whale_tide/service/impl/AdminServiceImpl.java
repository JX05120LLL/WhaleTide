package com.whale_tide.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale_tide.util.JwtUtil;
import com.whale_tide.dto.resource.ResourceResult;
import com.whale_tide.dto.role.MenuNodeResult;
import com.whale_tide.dto.role.RoleParam;
import com.whale_tide.dto.role.RoleResult;
import com.whale_tide.entity.ams.*;
import com.whale_tide.mapper.ams.*;
import com.whale_tide.service.IAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 管理员服务实现类
 */
@Slf4j
@Service
public class AdminServiceImpl implements IAdminService {

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    @Autowired
    private AmsResourcesMapper resourcesMapper;

    @Autowired
    private AmsRoleResourceRelationsMapper roleResourceRelationsMapper;

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

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
        String encodePassword = passwordEncoder.encode(admin.getPassword());
        admin.setPassword(encodePassword);

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
            log.warn("登录失败，参数错误");
            return "-2";
        }

        // 验证密码
        if (!passwordEncoder.matches(password, admin.getPassword())) {
            log.warn("登录失败，密码不正确，用户名: {}", username);
            return "0";
        }

        // 更新登录时间
        admin.setLoginTime(LocalDateTime.now());
        adminsMapper.updateById(admin);

        log.info("管理员登录成功: {}", username);

        return jwtUtil.generateToken(username);
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
            log.warn("获取菜单名称列表时，菜单ID列表为空");
            return new ArrayList<>();
        }
        List<String> menuNameList = new ArrayList<>();
        for (Long menuId : menuIds) {
            AmsMenus menu = menusMapper.selectById(menuId);
            if (menu != null) {
                menuNameList.add(menu.getTitle());
                log.info("添加菜单到名称列表: {}, ID: {}", menu.getTitle(), menuId);
            } else {
                log.warn("未找到ID为{}的菜单", menuId);
            }
        }
        return menuNameList;
    }

    /**
     * 获取菜单详情列表
     *
     * @param menuIds 菜单ID列表
     * @return 菜单详情对象列表
     */
    @Override
    public List<Map<String, Object>> getMenuDetailList(List<Long> menuIds) {
        if (menuIds == null || menuIds.size() == 0) {
            log.warn("获取菜单详情列表时，菜单ID列表为空");
            return new ArrayList<>();
        }

        // 获取所有菜单对象(包括指定ID的菜单和它们的子菜单)
        List<AmsMenus> allMenus = new ArrayList<>();
        Set<Long> menuIdSet = new HashSet<>(menuIds);

        // 1. 先获取指定的菜单项
        for (Long menuId : menuIds) {
            AmsMenus menu = menusMapper.selectById(menuId);
            if (menu != null) {
                allMenus.add(menu);
                log.info("找到主菜单: {}, ID: {}, 路径: {}, 组件: {}",
                        menu.getTitle(), menuId, menu.getPath(), menu.getComponent());
            } else {
                log.warn("未找到ID为{}的菜单", menuId);
            }
        }

        // 2. 获取所有子菜单 - 即使子菜单ID不在menuIds中
        for (AmsMenus menu : new ArrayList<>(allMenus)) {
            if (menu.getParentId() == 0 || menu.getParentId() == null) { // 一级菜单
                // 查询所有属于此父菜单的子菜单
                LambdaQueryWrapper<AmsMenus> childrenQuery = new LambdaQueryWrapper<>();
                childrenQuery.eq(AmsMenus::getParentId, menu.getId());
                List<AmsMenus> children = menusMapper.selectList(childrenQuery);

                for (AmsMenus child : children) {
                    if (!menuIdSet.contains(child.getId())) {
                        allMenus.add(child);
                        log.info("添加子菜单: {}, ID: {}, 父ID: {}, 路径: {}, 组件: {}",
                                child.getTitle(), child.getId(), child.getParentId(),
                                child.getPath(), child.getComponent());
                    }
                }
            }
        }

        // 3. 构建树形结构
        List<Map<String, Object>> menuTree = new ArrayList<>();
        Map<Long, Map<String, Object>> menuMap = new HashMap<>();

        // 先创建所有菜单节点
        for (AmsMenus menu : allMenus) {
            menuMap.put(menu.getId(), buildMenuMap(menu));
        }

        // 构建树形结构
        for (AmsMenus menu : allMenus) {
            Map<String, Object> node = menuMap.get(menu.getId());

            if (menu.getParentId() == 0 || menu.getParentId() == null) {
                // 顶级菜单
                menuTree.add(node);
            } else {
                // 子菜单，添加到父菜单的children列表中
                Map<String, Object> parent = menuMap.get(menu.getParentId());
                if (parent != null) {
                    if (!parent.containsKey("children")) {
                        parent.put("children", new ArrayList<>());
                    }

                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> children = (List<Map<String, Object>>) parent.get("children");
                    children.add(node);
                } else {
                    // 如果找不到父菜单，就作为顶级菜单添加
                    menuTree.add(node);
                }
            }
        }

        log.info("构建菜单树完成，共有{}个顶级菜单", menuTree.size());
        return menuTree;
    }

    /**
     * 将菜单对象转换为Map
     */
    private Map<String, Object> buildMenuMap(AmsMenus menu) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", menu.getId());
        map.put("parentId", menu.getParentId());
        map.put("title", menu.getTitle());
        map.put("level", menu.getLevel());
        map.put("sort", menu.getSort());
        map.put("name", menu.getPath()); // 前端路由名称使用path
        map.put("icon", menu.getIcon());

        // 确保path字段格式正确 - 如果不是以/开头的相对路径，添加/
        String path = menu.getPath();
        if (path != null && !path.startsWith("/") && menu.getParentId() != 0) {
            path = path; // 子路由不需要前导斜杠
        }
        map.put("path", path);

        map.put("component", menu.getComponent());
        map.put("redirect", menu.getRedirect());
        map.put("hidden", menu.getHidden() == 1);
        return map;
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

    @Override
    public IPage<RoleResult> listRoles(String keyword, Integer pageNum, Integer pageSize) {
        // 创建分页对象
        Page<AmsRoles> page = new Page<>(pageNum, pageSize);

        // 构建查询条件
        LambdaQueryWrapper<AmsRoles> queryWrapper = new LambdaQueryWrapper<>();

        // 添加关键字查询条件
        if (!StringUtils.isEmpty(keyword)) {
            queryWrapper.like(AmsRoles::getName, keyword)
                    .or()
                    .like(AmsRoles::getDescription, keyword);
        }

        // 按排序字段排序
        queryWrapper.orderByAsc(AmsRoles::getSort);

        // 执行查询
        Page<AmsRoles> rolePage = rolesMapper.selectPage(page, queryWrapper);

        // 创建结果页对象
        Page<RoleResult> resultPage = new Page<>(pageNum, pageSize);
        resultPage.setTotal(rolePage.getTotal());

        // 转换结果
        List<RoleResult> resultList = rolePage.getRecords().stream().map(role -> {
            RoleResult result = new RoleResult();
            BeanUtils.copyProperties(role, result);
            return result;
        }).collect(Collectors.toList());

        resultPage.setRecords(resultList);

        return resultPage;
    }

    @Override
    public List<RoleResult> listAllRoles() {
        // 查询所有角色
        LambdaQueryWrapper<AmsRoles> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(AmsRoles::getSort);
        List<AmsRoles> roleList = rolesMapper.selectList(queryWrapper);

        // 转换结果
        return roleList.stream().map(role -> {
            RoleResult result = new RoleResult();
            BeanUtils.copyProperties(role, result);
            return result;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean createRole(RoleParam roleParam) {
        AmsRoles role = new AmsRoles();
        BeanUtils.copyProperties(roleParam, role);

        // 设置创建时间
        role.setCreateTime(LocalDateTime.now());
        role.setUpdateTime(LocalDateTime.now());

        // 执行插入
        int count = rolesMapper.insert(role);
        return count > 0;
    }

    @Override
    public boolean updateRole(Long id, RoleParam roleParam) {
        // 查询角色是否存在
        AmsRoles role = rolesMapper.selectById(id);
        if (role == null) {
            return false;
        }

        // 更新角色信息
        BeanUtils.copyProperties(roleParam, role);
        role.setUpdateTime(LocalDateTime.now());

        // 执行更新
        int count = rolesMapper.updateById(role);
        return count > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRoles(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        try {
            // 删除角色
            int count = rolesMapper.deleteBatchIds(ids);

            // 删除角色关联的菜单关系
            LambdaQueryWrapper<AmsRoleMenuRelations> menuRelationQueryWrapper = new LambdaQueryWrapper<>();
            menuRelationQueryWrapper.in(AmsRoleMenuRelations::getRoleId, ids);
            roleMenuRelationsMapper.delete(menuRelationQueryWrapper);

            // 删除角色关联的资源关系
            LambdaQueryWrapper<AmsRoleResourceRelations> resourceRelationQueryWrapper = new LambdaQueryWrapper<>();
            resourceRelationQueryWrapper.in(AmsRoleResourceRelations::getRoleId, ids);
            roleResourceRelationsMapper.delete(resourceRelationQueryWrapper);

            // 删除角色关联的权限关系
            LambdaQueryWrapper<AmsRolePermissionRelation> permissionRelationQueryWrapper = new LambdaQueryWrapper<>();
            permissionRelationQueryWrapper.in(AmsRolePermissionRelation::getRoleId, ids);
            rolePermissionRelationsMapper.delete(permissionRelationQueryWrapper);

            return count > 0;
        } catch (Exception e) {
            log.error("删除角色失败", e);
            throw e;
        }
    }

    @Override
    public boolean updateRoleStatus(Long id, Integer status) {
        // 查询角色是否存在
        AmsRoles role = rolesMapper.selectById(id);
        if (role == null) {
            return false;
        }

        // 更新角色状态
        role.setStatus(status);
        role.setUpdateTime(LocalDateTime.now());

        // 执行更新
        int count = rolesMapper.updateById(role);
        return count > 0;
    }

    @Override
    public List<MenuNodeResult> listMenuByRole(Long roleId) {
        // 获取角色关联的菜单ID列表
        List<Long> menuIds = getMenuIdList(roleId);

        // 查询所有菜单
        List<AmsMenus> allMenus = menusMapper.selectList(null);

        // 构建菜单树
        List<MenuNodeResult> result = new ArrayList<>();

        // 获取一级菜单
        List<AmsMenus> rootMenus = allMenus.stream()
                .filter(menu -> menu.getParentId() == 0)
                .collect(Collectors.toList());

        // 构建菜单树
        for (AmsMenus rootMenu : rootMenus) {
            MenuNodeResult node = new MenuNodeResult();
            BeanUtils.copyProperties(rootMenu, node);
            node.setTitle(rootMenu.getTitle());
            node.setChecked(menuIds.contains(rootMenu.getId()));

            // 递归构建子菜单
            node.setChildren(buildMenuTree(allMenus, rootMenu.getId(), menuIds));

            result.add(node);
        }

        return result;
    }

    /**
     * 递归构建菜单树
     */
    private List<MenuNodeResult> buildMenuTree(List<AmsMenus> allMenus, Long parentId, List<Long> menuIds) {
        // 获取子菜单
        List<AmsMenus> childMenus = allMenus.stream()
                .filter(menu -> Objects.equals(menu.getParentId(), parentId))
                .collect(Collectors.toList());

        // 构建子菜单树
        List<MenuNodeResult> children = new ArrayList<>();
        for (AmsMenus childMenu : childMenus) {
            MenuNodeResult node = new MenuNodeResult();
            BeanUtils.copyProperties(childMenu, node);
            node.setTitle(childMenu.getTitle());
            node.setChecked(menuIds.contains(childMenu.getId()));

            // 递归构建子菜单
            node.setChildren(buildMenuTree(allMenus, childMenu.getId(), menuIds));

            children.add(node);
        }

        return children;
    }

    @Override
    public List<ResourceResult> listResourceByRole(Long roleId) {
        // 获取角色关联的资源ID列表
        List<Long> resourceIds = getResourceIdList(roleId);

        // 查询所有资源
        List<AmsResources> allResources = resourcesMapper.selectList(null);

        // 转换结果
        List<ResourceResult> result = allResources.stream().map(resource -> {
            ResourceResult resourceResult = new ResourceResult();
            BeanUtils.copyProperties(resource, resourceResult);
            resourceResult.setChecked(resourceIds.contains(resource.getId()));
            return resourceResult;
        }).collect(Collectors.toList());

        return result;
    }

    /**
     * 获取角色关联的资源ID列表
     */
    private List<Long> getResourceIdList(Long roleId) {
        LambdaQueryWrapper<AmsRoleResourceRelations> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AmsRoleResourceRelations::getRoleId, roleId);
        List<AmsRoleResourceRelations> relations = roleResourceRelationsMapper.selectList(queryWrapper);

        return relations.stream()
                .map(AmsRoleResourceRelations::getResourceId)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean allocMenu(Long roleId, List<Long> menuIds) {
        try {
            // 删除原有关联
            LambdaQueryWrapper<AmsRoleMenuRelations> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AmsRoleMenuRelations::getRoleId, roleId);
            roleMenuRelationsMapper.delete(queryWrapper);

            // 添加新关联
            if (menuIds != null && !menuIds.isEmpty()) {
                List<AmsRoleMenuRelations> relations = new ArrayList<>();
                for (Long menuId : menuIds) {
                    AmsRoleMenuRelations relation = new AmsRoleMenuRelations();
                    relation.setRoleId(roleId);
                    relation.setMenuId(menuId);
                    relations.add(relation);
                }

                // 批量插入
                for (AmsRoleMenuRelations relation : relations) {
                    roleMenuRelationsMapper.insert(relation);
                }
            }

            return true;
        } catch (Exception e) {
            log.error("分配角色菜单失败", e);
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean allocResource(Long roleId, List<Long> resourceIds) {
        try {
            // 删除原有关联
            LambdaQueryWrapper<AmsRoleResourceRelations> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AmsRoleResourceRelations::getRoleId, roleId);
            roleResourceRelationsMapper.delete(queryWrapper);

            // 添加新关联
            if (resourceIds != null && !resourceIds.isEmpty()) {
                List<AmsRoleResourceRelations> relations = new ArrayList<>();
                for (Long resourceId : resourceIds) {
                    AmsRoleResourceRelations relation = new AmsRoleResourceRelations();
                    relation.setRoleId(roleId);
                    relation.setResourceId(resourceId);
                    relations.add(relation);
                }

                // 批量插入
                for (AmsRoleResourceRelations relation : relations) {
                    roleResourceRelationsMapper.insert(relation);
                }
            }

            return true;
        } catch (Exception e) {
            log.error("分配角色资源失败", e);
            throw e;
        }
    }
}