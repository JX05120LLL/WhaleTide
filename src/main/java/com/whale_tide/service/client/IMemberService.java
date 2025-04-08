package com.whale_tide.service.client;

import com.whale_tide.dto.client.member.AvatarUploadResponse;
import com.whale_tide.dto.client.member.IntegrationDetailResponse;
import com.whale_tide.dto.client.member.MemberInfoUpdateRequest;
import com.whale_tide.dto.client.member.PasswordUpdateRequest;
import org.springframework.web.multipart.MultipartFile;
/**
 * 个人中心服务接口
 */
public interface IMemberService {
    /**
     * 修改用户信息
     * @param request
     */
    void memberInfoUpdate(MemberInfoUpdateRequest request);
    /**
     * 修改密码
     * @param request
     */
    void PasswordUpdate(PasswordUpdateRequest request);
    /**
     * 上传头像
     * @param file 头像文件
     * @return
     */
    String avatarUpload(MultipartFile file);
    /**
     * 获取积分详情
     * @return
     */
    IntegrationDetailResponse getIntegrationDetail();
}
