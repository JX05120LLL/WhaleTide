package com.whale_tide.service.management;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale_tide.dto.management.menu.MenuNode;
import com.whale_tide.dto.management.menu.MenuParam;
import com.whale_tide.entity.ams.AmsMenus;

import java.util.List;

/**
 * 菜单服务接口
 */
public interface IMenuService {
    
    /**
     * 创建菜单
     *
     * @param menuParam 菜单参数
     * @return 创建成功返回true
     */
    boolean create(MenuParam menuParam);
    
    /**
     * 更新菜单
     *
     * @param id        菜单ID
     * @param menuParam 菜单参数
     * @return 更新成功返回true
     */
    boolean update(Long id, MenuParam menuParam);
    
    /**
     * 根据ID获取菜单详情
     *
     * @param id 菜单ID
     * @return 菜单详情
     */
    AmsMenus getItem(Long id);
    
    /**
     * 根据ID删除菜单
     *
     * @param id 菜单ID
     * @return 删除成功返回true
     */
    boolean delete(Long id);
    
    /**
     * 分页查询菜单
     *
     * @param parentId  父级ID
     * @param pageNum   页码
     * @param pageSize  每页数量
     * @return 菜单分页数据
     */
    Page<AmsMenus> list(Long parentId, Integer pageNum, Integer pageSize);
    
    /**
     * 根据父ID查询菜单列表，不分页
     * 
     * @param parentId 父级ID
     * @return 菜单列表
     */
    List<AmsMenus> listByParent(Long parentId);
    
    /**
     * 修改菜单显示状态
     *
     * @param id     菜单ID
     * @param hidden 是否隐藏：0-否，1-是
     * @return 更新成功返回true
     */
    boolean updateHidden(Long id, Integer hidden);
    
    /**
     * 获取所有菜单的树形结构
     * 
     * @return 树形菜单列表
     */
    List<MenuNode> treeList();
} 