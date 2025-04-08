package com.whale_tide.service.client.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.whale_tide.dto.client.member.IntegrationDetailResponse;
import com.whale_tide.dto.client.member.MemberInfoUpdateRequest;
import com.whale_tide.dto.client.member.PasswordUpdateRequest;
import com.whale_tide.entity.ums.UmsUsers;
import com.whale_tide.mapper.ums.UmsUsersMapper;
import com.whale_tide.service.client.IMemberService;
import com.whale_tide.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
/**
 * 个人中心服务实现类
 */
@Service
public class MemberServiceImpl implements IMemberService {
    @Autowired
    private UmsUsersMapper umsUsersMapper;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
   /**
     * 个人中心-修改个人信息
     * @param request
     */
    @Override
    public void memberInfoUpdate(MemberInfoUpdateRequest request) {
        // 从请求中获取当前用户ID

        // 获取当前请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest req = attributes.getRequest();
            // 从请求头中获取token
            String token = req.getHeader("Authorization");
            if (token != null) {
                // 使用JwtUtil解析token获取用户名
                String username = jwtUtil.getUsernameFromToken(token);
                // 查询用户
                UmsUsers user = umsUsersMapper.selectOne(Wrappers.<UmsUsers>lambdaQuery().eq(UmsUsers::getUsername, username));
                // 更新用户信息
                user.setNickname(request.getNickname());
                user.setPhone(request.getPhone());
                user.setGender(request.getGender());
                user.setBirth(request.getBirthday());
                user.setRegion(request.getCity());

                // 更新用户信息
                umsUsersMapper.updateById(user);


            }
        }
    }

    /**
     * 个人中心-修改密码
     * @param request
     */
    @Override
    public void PasswordUpdate(PasswordUpdateRequest request) {
        // 从请求中获取当前用户ID
        // 获取当前请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest req = attributes.getRequest();
            // 从请求头中获取token
            String token = req.getHeader("Authorization");
            if (token != null) {
                // 使用JwtUtil解析token获取用户名
                String username = jwtUtil.getUsernameFromToken(token);
                // 查询用户
                UmsUsers user = umsUsersMapper.selectOne(Wrappers.<UmsUsers>lambdaQuery().eq(UmsUsers::getUsername, username));
                // 验证旧密码
                if (passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
                    // 更新密码
                    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
                    // 更新用户信息
                    umsUsersMapper.updateById(user);
                }
                else {
                    // 旧密码错误
                    throw new IllegalArgumentException("旧密码错误");
            }

            }
        }
    }
    /**
     * 个人中心-上传头像
     * @param file 头像文件
     * @return
     */
    @Override
    public String avatarUpload(MultipartFile file) {
        // 待开发
        return null;
    }
    /**
     * 个人中心-获取积分详情
     * @return
     */
    @Override
    public IntegrationDetailResponse getIntegrationDetail() {
        // 从请求中获取当前用户ID
        // 获取当前请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest req = attributes.getRequest();
            // 从请求头中获取token
            String token = req.getHeader("Authorization");
            if (token != null) {
                // 使用JwtUtil解析token获取用户名
                String username = jwtUtil.getUsernameFromToken(token);
                // 查询用户
                UmsUsers user = umsUsersMapper.selectOne(Wrappers.<UmsUsers>lambdaQuery().eq(UmsUsers::getUsername, username));
                // 返回积分详情
                IntegrationDetailResponse response = new IntegrationDetailResponse();
                response.setIntegration(user.getIntegration());
                response.setHistoryIntegration(1);
                response.setConsumePerIntegration(new BigDecimal(100));
                response.setUseIntegrationLimit(1);
                return response;
            }
        }
        return null;
    }
}


