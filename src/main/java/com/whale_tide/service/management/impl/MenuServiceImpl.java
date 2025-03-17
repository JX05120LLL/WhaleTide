package com.whale_tide.service.management.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whale_tide.dto.management.menu.MenuNode;
import com.whale_tide.dto.management.menu.MenuParam;
import com.whale_tide.entity.ams.AmsMenus;
import com.whale_tide.mapper.ams.AmsMenusMapper;
import com.whale_tide.service.management.IMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单服务实现类
 */
@Slf4j
@Service
public class MenuServiceImpl extends ServiceImpl<AmsMenusMapper, AmsMenus> implements IMenuService {

    @Autowired
    private AmsMenusMapper menusMapper;

    @Override
    public boolean create(MenuParam menuParam) {
        // 参数校验
        if (menuParam.getParentId() == null) {
            menuParam.setParentId(0L);
        }
        
        AmsMenus menu = new AmsMenus();
        BeanUtils.copyProperties(menuParam, menu);
        
        // 设置级别
        if (menu.getParentId() == 0L) {
            menu.setLevel(0);
        } else {
            AmsMenus parentMenu = getById(menu.getParentId());
            if (parentMenu != null) {
                menu.setLevel(parentMenu.getLevel() + 1);
            } else {
                menu.setLevel(0);
            }
        }
        
        // 设置创建时间
        menu.setCreateTime(LocalDateTime.now());
        menu.setUpdateTime(LocalDateTime.now());
        
        return save(menu);
    }

    @Override
    public boolean update(Long id, MenuParam menuParam) {
        AmsMenus menu = getById(id);
        if (menu == null) {
            return false;
        }
        
        BeanUtils.copyProperties(menuParam, menu);
        menu.setUpdateTime(LocalDateTime.now());
        
        return updateById(menu);
    }

    @Override
    public AmsMenus getItem(Long id) {
        return getById(id);
    }

    @Override
    public boolean delete(Long id) {
        // 检查是否有子菜单
        LambdaQueryWrapper<AmsMenus> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AmsMenus::getParentId, id);
        Long count = menusMapper.selectCount(queryWrapper);
        
        if (count > 0) {
            log.warn("菜单存在子菜单，无法删除");
            return false;
        }
        
        return removeById(id);
    }

    @Override
    public Page<AmsMenus> list(Long parentId, Integer pageNum, Integer pageSize) {
        Page<AmsMenus> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<AmsMenus> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AmsMenus::getParentId, parentId)
                .orderByAsc(AmsMenus::getSort);
        
        return page(page, queryWrapper);
    }

    @Override
    public List<AmsMenus> listByParent(Long parentId) {
        LambdaQueryWrapper<AmsMenus> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AmsMenus::getParentId, parentId)
                .eq(AmsMenus::getStatus, 1)
                .orderByAsc(AmsMenus::getSort);
        
        return list(queryWrapper);
    }

    @Override
    public boolean updateHidden(Long id, Integer hidden) {
        AmsMenus menu = getById(id);
        if (menu == null) {
            return false;
        }
        
        menu.setHidden(hidden);
        menu.setUpdateTime(LocalDateTime.now());
        
        return updateById(menu);
    }

    @Override
    public List<MenuNode> treeList() {
        // 获取所有菜单
        LambdaQueryWrapper<AmsMenus> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(AmsMenus::getSort);
        List<AmsMenus> allMenus = list(queryWrapper);
        
        // 转换为树形结构
        return convertMenuTree(allMenus);
    }
    
    /**
     * 将菜单转换为树形结构
     */
    private List<MenuNode> convertMenuTree(List<AmsMenus> allMenus) {
        // 找出所有的根菜单
        List<MenuNode> rootNodes = allMenus.stream()
                .filter(menu -> menu.getParentId() == 0)
                .map(this::convertToNode)
                .collect(Collectors.toList());
        
        // 为每个根节点设置子节点
        for (MenuNode rootNode : rootNodes) {
            buildChildren(rootNode, allMenus);
        }
        
        return rootNodes;
    }
    
    /**
     * 递归构建子节点
     */
    private void buildChildren(MenuNode parent, List<AmsMenus> allMenus) {
        List<MenuNode> children = allMenus.stream()
                .filter(menu -> menu.getParentId().equals(parent.getId()))
                .map(this::convertToNode)
                .collect(Collectors.toList());
        
        if (!children.isEmpty()) {
            parent.setChildren(children);
            // 递归设置子菜单的子菜单
            for (MenuNode child : children) {
                buildChildren(child, allMenus);
            }
        } else {
            parent.setChildren(Collections.emptyList());
        }
    }
    
    /**
     * 将AmsMenus转换为MenuNode
     */
    private MenuNode convertToNode(AmsMenus menu) {
        MenuNode node = new MenuNode();
        BeanUtils.copyProperties(menu, node);
        node.setChildren(new ArrayList<>());
        return node;
    }
} 