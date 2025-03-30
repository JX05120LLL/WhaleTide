package com.whale_tide.service.client.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale_tide.entity.ums.UmsUserBrandAttentions;
import com.whale_tide.mapper.ums.UmsUserBrandAttentionsMapper;
import com.whale_tide.service.client.IBrandAttentionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("clientMemberService")
public class BrandAttentionService implements IBrandAttentionService {

    @Autowired
    private UmsUserBrandAttentionsMapper userBrandAttentionsMapper;


    @Override
    public int addBrandAttention(UmsUserBrandAttentions userBrandAttentions) {
        if (userBrandAttentions == null || userBrandAttentions.getBrandId() == null)
            return -1;

        int result = userBrandAttentionsMapper.insert(userBrandAttentions);
        return result;
    }

    @Override
    public int deleteBrandAttention(Long userId, Long brandId) {
        if (brandId == null)
            return -1;
        LambdaQueryWrapper<UmsUserBrandAttentions> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UmsUserBrandAttentions::getUserId, userId)
                .eq(UmsUserBrandAttentions::getBrandId, brandId);
        int result = userBrandAttentionsMapper.delete(wrapper);
        return result;
    }

    @Override
    public Page<UmsUserBrandAttentions> getBrandAttentionList(Long userId, Long pageNum, Long pageSize) {
        if (userId == null)
            return null;
        LambdaQueryWrapper<UmsUserBrandAttentions> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UmsUserBrandAttentions::getUserId, userId);
        Page<UmsUserBrandAttentions> page = new Page<>(pageNum, pageSize);
        Page<UmsUserBrandAttentions> result = userBrandAttentionsMapper.selectPage(page, wrapper);
        return result;
    }

    @Override
    public UmsUserBrandAttentions getBrandAttentionDetail(Long userId, Long brandId) {
        if (brandId == null)
            return null;

        LambdaQueryWrapper<UmsUserBrandAttentions> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UmsUserBrandAttentions::getUserId, userId)
                .eq(UmsUserBrandAttentions::getBrandId, brandId);
        UmsUserBrandAttentions result = userBrandAttentionsMapper.selectOne(wrapper);
        return result;
    }

    @Override
    public int clearBrandAttention(Long userId) {
        if (userId == null)
            return -1;
        LambdaQueryWrapper<UmsUserBrandAttentions> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UmsUserBrandAttentions::getUserId, userId);
        int result = userBrandAttentionsMapper.delete(wrapper);
        return result;
    }
}
