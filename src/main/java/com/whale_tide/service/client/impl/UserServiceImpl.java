package com.whale_tide.service.client.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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

@Service("clientUserService")
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

    @Autowired
    private AliyunSmsUtil aliyunSmsUtil;

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
            log.error("密码错误: {}", password);
            return null;
        }

        // 更新最后登录时间
        user.setLastLoginTime(LocalDateTime.now());
        umsUsersMapper.updateById(user);

        // 生成JWT Token
        String token = jwtUtil.generateToken(username);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        loginResponse.setTokenHead("Bearer ");

        log.info("用户登录成功: {}", username);
        return loginResponse;
    }

    // 获取用户信息
    @Override
    public UserInfoResponse getUserInfo(String username) {


        LambdaQueryWrapper<UmsUsers> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UmsUsers::getUsername, username);
        UmsUsers user = umsUsersMapper.selectOne(queryWrapper);
        UserInfoResponse response = null;
        if (user != null) {


            response = new UserInfoResponse();
            response.setId(user.getId());
            response.setUsername(user.getUsername());
            response.setNickname(user.getNickname());
            response.setIcon(user.getAvatar());
            response.setIntegration(user.getIntegration());
            response.setGrowth(user.getGrowth());
        }
        return response;
    }


    // 发送验证码
    @Override
    public String sendVerificationCode(String phone) {
        // 检查手机号格式
        if (!checkPhone(phone)) {
            throw new RuntimeException("手机号格式不正确");
        }

        // 检查发送频率
        String rateLimitKey = "SMS:LIMIT:" + phone;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(rateLimitKey))) {
            throw new RuntimeException("发送太频繁，请1分钟后再试");
        }

        try {
            // 生成6位随机数字验证码
            String code = generateVerificationCode(6);
            
            log.info("为手机号 {} 生成验证码: {}", phone, code);
            
            // 将验证码保存到Redis，设置5分钟过期
            redisTemplate.opsForValue().set("SMS:CODE:" + phone, code, 5, TimeUnit.MINUTES);
            // 设置发送频率限制
            redisTemplate.opsForValue().set(rateLimitKey, "1", 1, TimeUnit.MINUTES);
            
            // 调用阿里云短信服务发送验证码
            boolean sendResult = aliyunSmsUtil.sendVerificationCode(phone, code);
            if (!sendResult) {
                throw new RuntimeException("短信发送失败");
            }
            
            // 返回成功消息，不返回验证码
            return "验证码已发送";
        } catch (Exception e) {
            log.error("发送验证码异常: ", e);
            throw new RuntimeException("发送验证码失败: " + e.getMessage());
        }
    }

    @Override
    public void register(RegisterRequest registerRequest) {
        String phone = registerRequest.getMobile();
        String code = registerRequest.getCode();
        String password = registerRequest.getPassword();

        log.info("处理注册请求: 手机号={}, 验证码={}, 密码长度={}", phone, code, password.length());

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
        
        // 检查用户名是否已存在
        queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UmsUsers::getUsername, phone);
        existingUser = umsUsersMapper.selectOne(queryWrapper);
        if (existingUser != null) {
            throw new RuntimeException("该手机号已被用作用户名");
        }

        // 验证码校验
        String cachedCode = redisTemplate.opsForValue().get("SMS:CODE:" + phone);
        if (cachedCode == null || !cachedCode.equals(code)) {
            throw new RuntimeException("验证码错误");
        }

        try {
            // 创建新用户
            UmsUsers user = new UmsUsers();
            user.setPhone(phone);
            user.setUsername(phone); // 使用手机号作为用户名
            user.setPassword(passwordEncoder.encode(password));
            user.setStatus(1); // 设置为启用状态
            user.setCreateTime(LocalDateTime.now());
            user.setUpdateTime(LocalDateTime.now());

            // 保存用户
            int result = umsUsersMapper.insert(user);
            if (result > 0) {
                log.info("用户注册成功: {}", phone);
            } else {
                log.error("用户注册失败: 数据库插入错误");
                throw new RuntimeException("注册失败");
            }
        } catch (Exception e) {
            log.error("注册过程发生错误", e);
            throw new RuntimeException("注册失败: " + e.getMessage());
        }
    }

    /**
     * 检查手机号格式是否正确
     */
    private boolean checkPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return phone.matches("^1[3-9]\\d{9}$");
    }
    
    /**
     * 生成指定长度的随机数字验证码
     */
    private String generateVerificationCode(int length) {
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
}


