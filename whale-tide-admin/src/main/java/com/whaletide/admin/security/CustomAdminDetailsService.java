package com.whaletide.admin.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.whaletide.common.entity.ams.AmsAdmins;
import com.whaletide.common.entity.ums.UmsUsers;
import com.whaletide.common.mapper.ams.AmsAdminsMapper;
import com.whaletide.common.mapper.ums.UmsUsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 自定义用户详情服务
 * 用于JWT验证时加载用户信息
 */
@Service
public class CustomAdminDetailsService implements UserDetailsService {
    
    @Autowired
    private AmsAdminsMapper adminsMapper;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 使用与UserServiceImpl中相同的查询方法
        LambdaQueryWrapper<AmsAdmins> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AmsAdmins::getUsername, username)
                .or().eq(AmsAdmins::getPhone, username)
                .or().eq(AmsAdmins::getEmail, username);

        AmsAdmins admin = adminsMapper.selectOne(queryWrapper);
        
        if (admin == null) {
            throw new UsernameNotFoundException("找不到用户: " + username);
        }
        
        // 使用相同的UserDetails创建逻辑
        return User.builder()
                .username(admin.getUsername())
                .password(admin.getPassword())
                .disabled(admin.getStatus() != 1) // 用户状态非1表示禁用
                .accountExpired(false)
                .credentialsExpired(false)
                .accountLocked(false)
                .authorities("ROLE_USER") // 添加默认的ROLE_USER权限
                .build();
    }
} 