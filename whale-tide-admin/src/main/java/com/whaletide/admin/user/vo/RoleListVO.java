package com.whaletide.admin.user.vo;

import com.whaletide.common.entity.ams.AmsRoles;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RoleListVO {
    Long id;
    String name;
    String description;
    Integer status;
    LocalDateTime createdDate;
    LocalDateTime updatedDate;


    //转换方法
    public static RoleListVO toVO(AmsRoles role) {
        RoleListVO vo = new RoleListVO();
        vo.setId(role.getId());
        vo.setName(role.getName());
        vo.setDescription(role.getDescription());
        vo.setStatus(role.getStatus());
        vo.setCreatedDate(role.getCreateTime());
        vo.setUpdatedDate(role.getUpdateTime());
        return vo;
    }
}
