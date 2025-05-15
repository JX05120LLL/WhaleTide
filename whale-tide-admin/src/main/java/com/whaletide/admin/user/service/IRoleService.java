package com.whaletide.admin.user.service;

import com.whaletide.admin.user.dto.RoleAddDTO;
import com.whaletide.admin.user.dto.RoleUpdateDTO;
import com.whaletide.admin.user.vo.RoleListVO;

import java.util.List;

public interface IRoleService {

    /**
     * 获取所有角色
     */
    List<RoleListVO> list();

    /**
     * 添加角色
     */
    Integer add(RoleAddDTO roleAddDTO);

    /**
     * 删除角色
     */
    Integer delete(Long id);

    /**
     * 更新角色
     */
    Integer update(RoleUpdateDTO roleUpdateDTO);
}
