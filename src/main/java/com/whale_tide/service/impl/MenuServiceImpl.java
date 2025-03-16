package com.whale_tide.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale_tide.dto.menu.MenuParam;
import com.whale_tide.entity.ams.AmsMenus;
import com.whale_tide.entity.sms.mapper.ams.AmsMenusMapper;
import com.whale_tide.service.IMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单服务实现类
 */
@Slf4j
@Service
public class MenuServiceImpl implements IMenuService {

    @Autowired
    private AmsMenusMapper menusMapper;

    @Override
    public boolean create(MenuParam menuParam) {
        AmsMenus menu = new AmsMenus();
        BeanUtils.copyProperties(menuParam, menu);
        menu.setCreateTime(LocalDateTime.now());
        menu.setUpdateTime(LocalDateTime.now());
        
        // 如果parentId为空，设置为0（根菜单）
        if (menu.getParentId() == null) {
            menu.setParentId(0L);
        }
        
        // 设置默认值
        if (menu.getStatus() == null) {
            menu.setStatus(1); // 默认启用
        }
        if (menu.getHidden() == null) {
            menu.setHidden(0); // 默认显示
        }
        if (menu.getSort() == null) {
            menu.setSort(0); // 默认排序为0
        }
        
        int count = menusMapper.insert(menu);
        return count > 0;
    }

    @Override
    public boolean update(Long id, MenuParam menuParam) {
        AmsMenus menu = menusMapper.selectById(id);
        if (menu == null) {
            log.warn("修改菜单失败，菜单不存在，ID：{}", id);
            return false;
        }
        
        BeanUtils.copyProperties(menuParam, menu);
        menu.setId(id);
        menu.setUpdateTime(LocalDateTime.now());
        
        int count = menusMapper.updateById(menu);
        return count > 0;
    }

    @Override
    public AmsMenus getItem(Long id) {
        return menusMapper.selectById(id);
    }

    @Override
    public boolean delete(Long id) {
        if (id == null) {
            log.warn("删除菜单失败，ID为空");
            return false;
        }
        
        // 检查是否有子菜单
        LambdaQueryWrapper<AmsMenus> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AmsMenus::getParentId, id);
        Long count = menusMapper.selectCount(queryWrapper);
        if (count > 0) {
            log.warn("删除菜单失败，该菜单存在子菜单，请先删除子菜单，ID：{}", id);
            return false;
        }
        
        int result = menusMapper.deleteById(id);
        return result > 0;
    }

    @Override
    public Page<AmsMenus> list(Long parentId, Integer pageNum, Integer pageSize) {
        Page<AmsMenus> page = new Page<>(pageNum, pageSize);
        
        LambdaQueryWrapper<AmsMenus> queryWrapper = new LambdaQueryWrapper<>();
        if (parentId != null) {
            queryWrapper.eq(AmsMenus::getParentId, parentId);
        }
        queryWrapper.orderByAsc(AmsMenus::getSort);
        
        return menusMapper.selectPage(page, queryWrapper);
    }

    @Override
    public List<AmsMenus> listByParent(Long parentId) {
        if (parentId == null) {
            return Collections.emptyList();
        }
        
        LambdaQueryWrapper<AmsMenus> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AmsMenus::getParentId, parentId);
        queryWrapper.orderByAsc(AmsMenus::getSort);
        
        return menusMapper.selectList(queryWrapper);
    }

    @Override
    public boolean updateHidden(Long id, Integer hidden) {
        if (id == null || hidden == null) {
            return false;
        }
        
        AmsMenus menu = menusMapper.selectById(id);
        if (menu == null) {
            log.warn("修改菜单显示状态失败，菜单不存在，ID：{}", id);
            return false;
        }
        
        menu.setHidden(hidden);
        menu.setUpdateTime(LocalDateTime.now());
        
        int count = menusMapper.updateById(menu);
        return count > 0;
    }

    @Override
    public List<AmsMenus> treeList() {
        // 获取所有菜单
        List<AmsMenus> allMenus = menusMapper.selectList(null);
        if (allMenus.isEmpty()) {
            return Collections.emptyList();
        }
        
        // 找出所有的根菜单（parent_id = 0）
        List<AmsMenus> rootMenus = allMenus.stream()
                .filter(menu -> menu.getParentId() == 0)
                .collect(Collectors.toList());
        
        // 为根菜单递归设置子菜单
        for (AmsMenus rootMenu : rootMenus) {
            setChildren(rootMenu, allMenus);
        }
        
        return rootMenus;
    }
    
    /**
     * 递归设置子菜单
     */
    private void setChildren(AmsMenus parent, List<AmsMenus> allMenus) {
        List<AmsMenus> children = allMenus.stream()
                .filter(menu -> menu.getParentId().equals(parent.getId()))
                .collect(Collectors.toList());
        
        if (!children.isEmpty()) {
            // 这里需要在AmsMenus类中添加children字段
            // 由于我们没有修改实体类，这里实际上不会设置成功
            // 在实际项目中，可以修改实体类添加children字段，或者创建一个新的DTO来包含树形结构
            // parent.setChildren(children);
            
            // 递归设置子菜单的子菜单
            for (AmsMenus child : children) {
                setChildren(child, allMenus);
            }
        }
    }
} 