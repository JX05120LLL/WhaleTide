package com.whaletide.common.mapper.ams;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.whaletide.common.entity.ams.AmsRoles;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author BroRem
 * @since 2025-05-10
 */



@Mapper
public interface AmsRolesMapper extends BaseMapper<AmsRoles> {
    
    /**
     * 自定义查询所有角色
     * 明确指定查询的字段，避免MyBatis自动生成的SQL中包含不存在的code字段
     */
    @Select("SELECT id, name, description, status, create_time, update_time FROM ams_roles")
    List<AmsRoles> selectAllRoles();
}
