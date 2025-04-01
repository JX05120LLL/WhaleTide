package com.whale_tide.service.client;

import com.whale_tide.dto.client.member.AvatarUploadResponse;
import com.whale_tide.dto.client.member.IntegrationDetailResponse;
import com.whale_tide.dto.client.member.MemberInfoUpdateRequest;
import com.whale_tide.dto.client.member.PasswordUpdateRequest;

public interface IMemberService {
    void memberInfoUpdate(MemberInfoUpdateRequest request);

    void PasswordUpdate(PasswordUpdateRequest request);

    String avatarUpload(AvatarUploadResponse request);

    IntegrationDetailResponse getIntegrationDetail();
}
