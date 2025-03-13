package com.whale_tide.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.whale_tide.entity.AmsAdminRoleRelations;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 管理员与角色关系表 Mapper 接口
 * </p>
 *
 * @author Bro_cat
 * @since 2025-03-12
 */
@Mapper
public interface AmsAdminRoleRelationsMapper extends BaseMapper<AmsAdminRoleRelations> {

    /**
     * 根据管理员ID查询角色关系
     * @param adminId 管理员ID
     * @return 角色关系列表
     */
    List<AmsAdminRoleRelations> selectByAdminId(Long adminId);
    
    /**
     * 根据管理员ID删除角色关系
     * @param adminId 管理员ID
     * @return 影响行数
     */
    int deleteByAdminId(Long adminId);
    
    /**
     * 批量插入角色关系
     * @param relations 角色关系列表
     * @return 影响行数
     */
    int batchInsert(@Param("list") List<AmsAdminRoleRelations> relations);
}
