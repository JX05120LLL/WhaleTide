package com.whaletide.admin.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.whaletide.admin.user.vo.AdminUserInfoVO;
import com.whaletide.admin.user.vo.AdminUserLoginVO;
import com.whaletide.admin.user.service.IAdminUserService;
import com.whaletide.common.entity.ams.AmsAdminRoleRelations;
import com.whaletide.common.entity.ams.AmsAdmins;
import com.whaletide.common.entity.ams.AmsRoles;
import com.whaletide.common.mapper.ams.AmsAdminRoleRelationsMapper;
import com.whaletide.common.mapper.ams.AmsAdminsMapper;
import com.whaletide.common.mapper.ams.AmsRolesMapper;
import com.whaletide.common.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class AdminUserService implements IAdminUserService {
    @Autowired
    private AmsAdminsMapper adminsMapper;

    @Autowired
    private AmsRolesMapper rolesMapper;

    @Autowired
    private AmsAdminRoleRelationsMapper adminRoleRelationsMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public AdminUserLoginVO login(String username, String password) {
        if (username == null || password == null) {
            log.error("用户名或密码为空");
            return null;
        }
        AdminUserLoginVO loginVO = new AdminUserLoginVO();

        //查询用户信息
        LambdaQueryWrapper<AmsAdmins> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AmsAdmins::getUsername, username);

        AmsAdmins admin = adminsMapper.selectOne(queryWrapper);
        if (admin == null) {
            log.error("用户不存在");
            return null;
        }

        //验证密码
        if (!admin.getPassword().equals(password)) {
            log.error("密码错误");
            loginVO.setUsername(username);
            return loginVO;
        }

        UserDetails userDetails = createUserDetails(admin);
        //生成token
        String token = jwtTokenProvider.generateToken(userDetails);

        //保存token到redis
        redisTemplate.opsForValue().set(username, token, jwtTokenProvider.getExpirationSeconds(), TimeUnit.SECONDS);

        //更新登录时间
        admin.setLoginTime(LocalDateTime.now());
        adminsMapper.updateById(admin);

        //返回结果
        loginVO.setUsername(username);
        loginVO.setUserId(admin.getId());
        loginVO.setToken(token);
        return loginVO;
    }

    @Override
    public AdminUserInfoVO getUserInfo(String userName) {
        if (userName == null) {
            log.error("用户ID为空");
            return null;
        }
        //查询用户信息
        LambdaQueryWrapper<AmsAdmins> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AmsAdmins::getUsername, userName);
        AmsAdmins admin = adminsMapper.selectOne(queryWrapper);
        if (admin == null) {
            log.error("用户不存在");
            return null;
        }
        //返回结果
        AdminUserInfoVO userInfoVO = new AdminUserInfoVO();
        userInfoVO.setName(admin.getRealName());
        userInfoVO.setUsername(admin.getUsername());
        userInfoVO.setAvatar(admin.getAvatar());
        userInfoVO.setEmail(admin.getEmail());
        userInfoVO.setPhone(admin.getPhone());
        userInfoVO.setCreatedTime(admin.getCreateTime());
        userInfoVO.setLastLoginTime(admin.getLoginTime());

        //获取用户角色
        userInfoVO.setRoles(new java.util.ArrayList<>());

        LambdaQueryWrapper<AmsAdminRoleRelations> roleQueryWrapper = new LambdaQueryWrapper<>();
        roleQueryWrapper.eq(AmsAdminRoleRelations::getAdminId, admin.getId());
        List<AmsAdminRoleRelations> roleRelations = adminRoleRelationsMapper.selectList(roleQueryWrapper);

        for (AmsAdminRoleRelations roleRelation : roleRelations) {
            AmsRoles role = rolesMapper.selectById(roleRelation.getRoleId());
            userInfoVO.getRoles().add(role.getName());
        }


        return userInfoVO;
    }

    @Override
    public void logout(String token) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String username = authentication.getName();
            // 清除redis中token
            redisTemplate.delete(username);
        }

    }

    /**
     * 创建UserDetails对象
     *
     * @param admin 用户实体
     * @return UserDetails对象
     */
    private UserDetails createUserDetails(AmsAdmins admin) {
        // 使用Spring Security提供的User类创建UserDetails对象
        // 为用户添加默认的ROLE_USER权限
        return User.builder()
                .username(admin.getUsername())
                .password(admin.getPassword()) // 已加密的密码
                .disabled(admin.getStatus() != 1) // 用户状态非1表示禁用
                .accountExpired(false)
                .credentialsExpired(false)
                .accountLocked(false)
                .authorities("ROLE_USER") // 添加默认的ROLE_USER权限
                .build();
    }
}
