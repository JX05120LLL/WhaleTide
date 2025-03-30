package com.whale_tide.service.client.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale_tide.entity.ums.UmsUserFavorites;
import com.whale_tide.mapper.ums.UmsUserFavoritesMapper;
import com.whale_tide.service.client.IProductCollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("clientProductCollectService")
public class ProductCollectService implements IProductCollectService {

    @Autowired
    private UmsUserFavoritesMapper userFavoritesMapper;

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
}
