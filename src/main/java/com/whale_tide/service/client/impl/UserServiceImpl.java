package com.whale_tide.service.client.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.whale_tide.common.api.CommonResult;
import com.whale_tide.dto.client.user.LoginResponse;
import com.whale_tide.dto.client.user.RegisterRequest;
import com.whale_tide.dto.client.user.UserInfoResponse;
import com.whale_tide.entity.ums.UmsUsers;
import com.whale_tide.mapper.ums.UmsUsersMapper;
import com.whale_tide.service.client.IUserService;
import com.whale_tide.util.AliyunSmsUtil;
import com.whale_tide.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.TimeUnit;

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
    private JwtUtil jwtUtil;

    @Override
    public LoginResponse login(String username, String password) {
        // 用户登录时生成 token 并返回
        LambdaQueryWrapper<UmsUsers> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UmsUsers::getUsername, username);
        UmsUsers user = umsUsersMapper.selectOne(queryWrapper);
        if (user == null) {
            log.error("用户名不存在: {}", username);
            return null;
        }
        
        // 验证密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.error("密码错误: {}", username);
            return null;
        }
        
        // 更新最后登录时间
        user.setLastLoginTime(LocalDateTime.now());
        umsUsersMapper.updateById(user);
        
        // 生成token
        String token = jwtUtil.generateToken(username);
        
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        loginResponse.setTokenHead("Bearer");
        
        log.info("用户登录成功: {}", username);
        return loginResponse;
    }

    // 获取用户信息
    @Override
    public UserInfoResponse getUserInfo() {
        //header中获取token
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        String id = jwtUtil.getUsernameFromToken(token); // 从Token中解析用户名

        LambdaQueryWrapper<UmsUsers> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UmsUsers::getId, id);
        UmsUsers user = umsUsersMapper.selectOne(queryWrapper);

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        UserInfoResponse response = new UserInfoResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setNickname(user.getNickname());
        response.setIcon(user.getAvatar());
        response.setIntegration(user.getIntegration());
        response.setGrowth(user.getGrowth());
        return response;
    }


    // 发送验证码
    @Override
    public String sendVerificationCode(String phone) {
        // 检查手机号格式
        if (phone == null || phone.trim().isEmpty()) {
            throw new RuntimeException("手机号不能为空");
        }
        if (!phone.matches("^1[3-9]\\d{9}$")) {
            throw new RuntimeException("手机号格式错误");
        }

        // 检查发送频率
        String rateLimitKey = "SMS:LIMIT:" + phone;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(rateLimitKey))) {
            throw new RuntimeException("发送太频繁，请1分钟后再试");
        }

        // 生成6位随机验证码
        String code = String.format("%06d", new Random().nextInt(1000000));

        // 发送短信
        try {
            AliyunSmsUtil aliyunSmsUtil = new AliyunSmsUtil();
            aliyunSmsUtil.sendToPhone(phone, code);
            // 将验证码保存到Redis，设置5分钟过期
            redisTemplate.opsForValue().set("SMS:CODE:" + phone, code, 5, TimeUnit.MINUTES);
            // 设置发送频率限制
            redisTemplate.opsForValue().set(rateLimitKey, "1", 1, TimeUnit.MINUTES);
            return "验证码已发送";
        } catch (Exception e) {
            log.error("发送验证码失败: phone={}, error={}", phone, e.getMessage());
            throw new RuntimeException("发送验证码失败:" + e.getMessage());
        }
    }

    @Override
    public void register(RegisterRequest registerRequest) {
        String phone = registerRequest.getMobile();

        String code = registerRequest.getCode();
        String password = registerRequest.getPassword();

        // 密码长度至少6位
        if (registerRequest.getPassword().length() < 6) {
            throw new RuntimeException("密码长度需至少6位");
        }

        // 检查手机号是否已注册
        LambdaQueryWrapper<UmsUsers> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UmsUsers::getPhone, phone);
        UmsUsers existingUser = umsUsersMapper.selectOne(queryWrapper);
        if (existingUser != null) {
            throw new RuntimeException("手机号已注册");
        }

        // 验证码校验
        String cachedCode = redisTemplate.opsForValue().get("SMS:CODE:" + phone);
        if (cachedCode == null || !cachedCode.equals(code)) {
            throw new RuntimeException("验证码错误");
        }

        // 创建新用户
        UmsUsers user = new UmsUsers();
        user.setPhone(phone);
        user.setPassword(passwordEncoder.encode(password));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        // 保存用户
        umsUsersMapper.insert(user);

        // 删除验证码
        redisTemplate.delete("SMS:CODE:" + phone);

        log.info("用户注册成功: {}", phone);
    }
}


