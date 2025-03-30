package com.whale_tide.service.client.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale_tide.entity.ums.UmsUserBrandAttentions;
import com.whale_tide.entity.ums.UmsUserFavorites;
import com.whale_tide.mapper.ums.UmsUserBrandAttentionsMapper;
import com.whale_tide.mapper.ums.UmsUserFavoritesMapper;
import com.whale_tide.service.client.IMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("clientMemberService")
public class MemberService implements IMemberService {

    @Autowired
    private UmsUserFavoritesMapper userFavoritesMapper;

    @Autowired
    private UmsUserBrandAttentionsMapper userBrandAttentionsMapper;


    @Override
    public int addProductFavorite(UmsUserFavorites userFavorites, Long userId) {
        int result = userFavoritesMapper.insert(userFavorites);
        return result;
    }

    public int deleteProductFavorite(Long userId, Long productId) {
        LambdaQueryWrapper<UmsUserFavorites> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UmsUserFavorites::getUserId, userId)
                .eq(UmsUserFavorites::getProductId, productId);
        int result = userFavoritesMapper.delete(wrapper);
        userFavoritesMapper.deleteById(productId);
        return result;
    }

    @Override
    public Page<UmsUserFavorites> getProductFavorites(Long userId, Long pageNum, Long pageSize) {
        LambdaQueryWrapper<UmsUserFavorites> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UmsUserFavorites::getUserId, userId);
        Page<UmsUserFavorites> page = new Page<>(pageNum, pageSize);
        Page<UmsUserFavorites> result = userFavoritesMapper.selectPage(page, wrapper);
        return result;
    }

    @Override
    public UmsUserFavorites getProductFavoriteDetail(Long userId, Long productId) {
        if (productId == null)
            return null;

        LambdaQueryWrapper<UmsUserFavorites> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UmsUserFavorites::getUserId, userId)
                .eq(UmsUserFavorites::getProductId, productId);
        UmsUserFavorites result = userFavoritesMapper.selectOne(wrapper);
        return result;
    }

    @Override
    public int clearProductFavorites(Long userId) {
        LambdaQueryWrapper<UmsUserFavorites> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UmsUserFavorites::getUserId, userId);
        int result = userFavoritesMapper.delete(wrapper);
        return result;
    }

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
