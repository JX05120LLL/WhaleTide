package com.whale_tide.mapper.ams;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.whale_tide.entity.ams.AmsAdmins;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * <p>
 * 管理员表 Mapper 接口
 * </p>
 *
 * @author Bro_cat
 * @since 2025-03-12
 */
@Mapper
public interface AmsAdminsMapper extends BaseMapper<AmsAdmins> {

    /**
     * 根据用户名查询管理员
     * @param username 用户名
     * @return 管理员信息
     */
    AmsAdmins selectByUsername(String username);
    
    /**
     * 根据关键字分页查询管理员列表
     * @param keyword 关键字
     * @param pageable 分页参数
     * @return 管理员分页列表
     */
    Page<AmsAdmins> selectByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
