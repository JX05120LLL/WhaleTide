package com.whaletide.admin.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.whaletide.admin.user.dto.RoleAddDTO;
import com.whaletide.admin.user.dto.RoleUpdateDTO;
import com.whaletide.admin.user.service.IRoleService;
import com.whaletide.admin.user.vo.RoleListVO;
import com.whaletide.common.entity.ams.AmsRoles;
import com.whaletide.common.mapper.ams.AmsRolesMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class RoleService implements IRoleService {

    @Autowired
    AmsRolesMapper rolesMapper;

    @Override
    public List<RoleListVO> list() {
        List<AmsRoles> roles = rolesMapper.selectList(null);
        List<RoleListVO> roleList = new ArrayList<>();
        for (AmsRoles role : roles) {
            roleList.add(RoleListVO.toVO(role));
        }
        return roleList;
    }

    @Override
    public Integer add(RoleAddDTO roleaddDTO) {
        LambdaQueryWrapper<AmsRoles> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AmsRoles::getName, roleaddDTO.getName());
        if (rolesMapper.selectOne(wrapper) == null) {
            AmsRoles amsRoles = new AmsRoles();
            amsRoles.setName(roleaddDTO.getName());
            amsRoles.setDescription(roleaddDTO.getDescription());
            if (roleaddDTO.getStatus() != null) {
                if (roleaddDTO.getStatus()) {
                    amsRoles.setStatus(1);
                } else {
                    amsRoles.setStatus(0);
                }
            } else {
                amsRoles.setStatus(0);
            }
            amsRoles.setCreateTime(LocalDateTime.now());
            amsRoles.setUpdateTime(LocalDateTime.now());

            rolesMapper.insert(amsRoles);
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public Integer delete(Long id) {
        int result = rolesMapper.deleteById(id);

        return result;
    }

    @Override
    public Integer update(RoleUpdateDTO roleUpdateDTO) {
        AmsRoles amsRoles = rolesMapper.selectById(roleUpdateDTO.getId());
        if (amsRoles != null) {
            //比较
            int status = roleUpdateDTO.getStatus() ? 1 : 0;
            if (amsRoles.getName().equals(roleUpdateDTO.getName())
                    && amsRoles.getDescription().equals(roleUpdateDTO.getDescription())
                    && amsRoles.getStatus() == status) {
                return 0;
            }


            amsRoles.setName(roleUpdateDTO.getName());
            amsRoles.setDescription(roleUpdateDTO.getDescription());
            if (roleUpdateDTO.getStatus() != null) {
                if (roleUpdateDTO.getStatus()) {
                    amsRoles.setStatus(1);
                } else {
                    amsRoles.setStatus(0);
                }
            }
            amsRoles.setUpdateTime(LocalDateTime.now());

            rolesMapper.updateById(amsRoles);
            return 1;
        } else {
            return -1;
        }
    }
}
