package com.whaletide.client.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.whaletide.client.user.dto.UpdatePasswordDTO;
import com.whaletide.client.user.dto.UserRegisterDTO;
import com.whaletide.client.user.service.IUserService;
import com.whaletide.client.user.vo.UserInfoResponse;
import com.whaletide.client.user.vo.UserVO;
import com.whaletide.common.api.CommonResult;

import com.whaletide.common.entity.ums.UmsUsers;
import com.whaletide.common.exception.auth.AccountDisabledException;
import com.whaletide.common.exception.auth.AuthenticationException;
import com.whaletide.common.exception.auth.CredentialsException;
import com.whaletide.common.exception.base.BusinessException;
import com.whaletide.common.exception.base.ValidationException;
import com.whaletide.common.exception.user.UserAlreadyExistsException;
import com.whaletide.common.exception.user.UserNotFoundException;
import com.whaletide.common.mapper.ums.UmsUsersMapper;
import com.whaletide.common.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 用户服务实现类
 */
@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    @Autowired
    private UmsUsersMapper umsUsersMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * 用户注册
     */
    @Override
    @Transactional
    public void register(UserRegisterDTO userRegisterDTO) {
        // 参数校验
        if (userRegisterDTO == null) {
            throw new ValidationException("注册信息不能为空");
        }
        
        if (!StringUtils.hasText(userRegisterDTO.getUsername())) {
            throw new ValidationException("用户名不能为空");
        }
        
        if (!StringUtils.hasText(userRegisterDTO.getPassword())) {
            throw new ValidationException("密码不能为空");
        }
        
        // 检查用户是否已存在
        if (isUserExist(userRegisterDTO.getUsername(), userRegisterDTO.getPhone(), userRegisterDTO.getEmail())) {
            throw new UserAlreadyExistsException("用户名、手机号或邮箱已被注册");
        }
        
        // 创建用户实体
        UmsUsers user = new UmsUsers();
        user.setUsername(userRegisterDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        user.setPhone(userRegisterDTO.getPhone());
        user.setEmail(userRegisterDTO.getEmail());

        // 此处图像应该使用 b64 编码处理
        user.setAvatar("https://whaletide-mall.oss-cn-beijing.aliyuncs.com/default-avatar.png");
        user.setGender(0);
        user.setStatus(1); // 正常状态
        user.setIntegration(0);
        user.setLastLoginTime(LocalDateTime.now());
        
        // 保存用户
        int result = umsUsersMapper.insert(user);
        if (result <= 0) {
            throw new BusinessException("用户注册失败");
        }
    }

    /**
     * 用户登录
     */
    @Override
    @Transactional
    public String login(String username, String password) {
        // 参数校验
        if (!StringUtils.hasText(username)) {
            throw new ValidationException("用户名不能为空");
        }
        
        if (!StringUtils.hasText(password)) {
            throw new ValidationException("密码不能为空");
        }
        
        // 查询用户
        UmsUsers user = getUserByIdentifier(username);
        if (user == null) {
            throw new UserNotFoundException("用户不存在: " + username);
        }
        
        // 检查用户状态
        if (user.getStatus() != 1) {
            throw new AccountDisabledException("账号已被禁用");
        }
        
        // 验证密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CredentialsException("密码错误");
        }
        
        // 更新登录时间
        user.setLastLoginTime(LocalDateTime.now());
        umsUsersMapper.updateById(user);
        
        // 生成并返回token
        UserDetails userDetails = createUserDetails(user);
        String token = jwtTokenProvider.generateToken(userDetails);
        
        // 将token信息存入Redis，用于后续验证
        redisTemplate.opsForValue().set("token:" + user.getUsername(), token, 7, TimeUnit.DAYS);
        log.info("用户登录成功: " + username);

        return token;
    }

    /**
     * 获取当前登录用户信息
     */
    @Override
    public UserVO getCurrentUser() {
        // 获取当前用户认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationException("用户未登录");
        }
        
        String username = authentication.getName();
        
        // 检查是否是匿名用户
        if ("anonymousUser".equals(username)) {
            throw new AuthenticationException("用户未登录或登录已过期，请重新登录");
        }
        
        // 查询用户
        UmsUsers user = getUserByIdentifier(username);
        if (user == null) {
            throw new UserNotFoundException("用户不存在: " + username);
        }
        
        // 转换为VO
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setUsername(user.getUsername());
        userVO.setNickname(user.getUsername());
        userVO.setPhone(user.getPhone());
        userVO.setEmail(user.getEmail());
        userVO.setAvatar(user.getAvatar());
        userVO.setMemberLevel(user.getLevel());
        
        return userVO;
    }

    /**
     * 用户登出
     */
    @Override
    public Boolean logout() {
        // 获取当前用户认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        String username = authentication.getName();
        
        // 从Redis中移除token
        redisTemplate.delete("token:" + username);

        log.info("用户登出成功: " + username);
        return true;
    }

    /**
     * 更新用户信息
     */
    @Override
    @Transactional
    public Boolean updateUserInfo(UserVO userVO) {
        // 参数校验
        if (userVO == null) {
            throw new ValidationException("用户信息不能为空");
        }
        
        // 获取当前用户认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationException("用户未登录");
        }
        
        String username = authentication.getName();
        
        // 查询用户
        UmsUsers user = getUserByIdentifier(username);
        if (user == null) {
            throw new UserNotFoundException("用户不存在: " + username);
        }
        
        // 检查是否是当前用户
        if (!Objects.equals(user.getId(), userVO.getId())) {
            throw new BusinessException("无权修改其他用户信息");
        }
        
        // 更新用户信息
        user.setPhone(userVO.getPhone());
        user.setEmail(userVO.getEmail());
        user.setAvatar(userVO.getAvatar());

        int result = umsUsersMapper.updateById(user);
        if (result <= 0) {
            throw new BusinessException("更新用户信息失败");
        }

        log.info("用户信息更新成功: " + username);
        return true;
    }

    @Override
    public Boolean updatePassword(UpdatePasswordDTO updatePasswordDTO) {
        // 参数校验
        if (updatePasswordDTO == null) {
            throw new ValidationException("更新密码信息不能为空");
        }
        
        if (!StringUtils.hasText(updatePasswordDTO.getOldPassword())) {
            throw new ValidationException("旧密码不能为空");
        }
        
        if (!StringUtils.hasText(updatePasswordDTO.getNewPassword())) {
            throw new ValidationException("新密码不能为空");
        }
        
        if (!StringUtils.hasText(updatePasswordDTO.getConfirmPassword())) {
            throw new ValidationException("确认密码不能为空");
        }
        
        // 确认新密码和确认密码一致
        if (!updatePasswordDTO.getNewPassword().equals(updatePasswordDTO.getConfirmPassword())) {
            throw new ValidationException("新密码与确认密码不一致");
        }
        
        // 获取当前用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationException("用户未登录");
        }
        
        String username = authentication.getName();
        
        // 查询用户
        UmsUsers user = getUserByIdentifier(username);
        if (user == null) {
            throw new UserNotFoundException("用户不存在: " + username);
        }
        
        // 验证旧密码
        if (!passwordEncoder.matches(updatePasswordDTO.getOldPassword(), user.getPassword())) {
            throw new CredentialsException("旧密码错误");
        }
        
        // 更新密码
        user.setPassword(passwordEncoder.encode(updatePasswordDTO.getNewPassword()));
        int result = umsUsersMapper.updateById(user);
        if (result <= 0) {
            throw new BusinessException("更新密码失败");
        }
        
        // 更新成功后可以清除该用户的Redis缓存
        redisTemplate.delete("token:" + username);
        
        log.info("用户密码更新成功: " + username);
        return true;
    }

    /**
     * 根据用户名获取用户信息
     */
    @Override
    public UserInfoResponse getUserInfo(String username) {
        if (!StringUtils.hasText(username)) {
            throw new ValidationException("用户名不能为空");
        }
        
        UmsUsers user = getUserByIdentifier(username);
        if (user == null) {
            throw new UserNotFoundException("用户不存在: " + username);
        }

        UserInfoResponse response = new UserInfoResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setIcon(user.getAvatar());
        response.setIntegration(user.getIntegration());
        response.setMobile(user.getPhone());
        response.setEmail(user.getEmail());

        log.info("获取用户信息成功: " + username);
        return response;
    }

    /**
     * 检查用户是否已存在（用户名、手机号或邮箱）
     */
    private boolean isUserExist(String username, String mobile, String email) {
        LambdaQueryWrapper<UmsUsers> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UmsUsers::getUsername, username)
                .or(StringUtils.hasText(mobile), wrapper -> wrapper.eq(UmsUsers::getPhone, mobile))
                .or(StringUtils.hasText(email), wrapper -> wrapper.eq(UmsUsers::getEmail, email));
        
        return umsUsersMapper.selectCount(queryWrapper) > 0;
    }

    /**
     * 根据用户名、手机号或邮箱查询用户
     */
    private UmsUsers getUserByIdentifier(String identifier) {
        LambdaQueryWrapper<UmsUsers> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UmsUsers::getUsername, identifier)
                .or().eq(UmsUsers::getPhone, identifier)
                .or().eq(UmsUsers::getEmail, identifier);
        
        return umsUsersMapper.selectOne(queryWrapper);
    }

    /**
     * 创建UserDetails对象
     * @param user 用户实体
     * @return UserDetails对象
     */
    private UserDetails createUserDetails(UmsUsers user) {
        // 使用Spring Security提供的User类创建UserDetails对象
        // 为用户添加默认的ROLE_USER权限
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) // 已加密的密码
                .disabled(user.getStatus() != 1) // 用户状态非1表示禁用
                .accountExpired(false)
                .credentialsExpired(false)
                .accountLocked(false)
                .authorities("ROLE_USER") // 添加默认的ROLE_USER权限
                .build();
    }
} 