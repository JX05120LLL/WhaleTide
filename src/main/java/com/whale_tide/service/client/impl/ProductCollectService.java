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

    @Override
    public int deleteProductFavorite(Long userId, Long productId) {
        if (userId == null || productId == null) {
            return 0;
        }
        
        LambdaQueryWrapper<UmsUserFavorites> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UmsUserFavorites::getUserId, userId)
               .eq(UmsUserFavorites::getProductId, productId);
        
        return userFavoritesMapper.delete(wrapper);
    }

    @Override
    public Page<UmsUserFavorites> getProductFavorites(Long userId, Long pageNum, Long pageSize) {
        LambdaQueryWrapper<UmsUserFavorites> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UmsUserFavorites::getUserId, userId);
        
        // 设置默认分页参数
        if (pageNum == null) {
            pageNum = 1L;
        }
        if (pageSize == null) {
            pageSize = 10L;
        }
        
        Page<UmsUserFavorites> page = new Page<>(pageNum, pageSize);
        Page<UmsUserFavorites> result = userFavoritesMapper.selectPage(page, wrapper);
        return result;
    }

    @Override
    public UmsUserFavorites getProductFavoriteDetail(Long productId, Long userId) {
        LambdaQueryWrapper<UmsUserFavorites> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UmsUserFavorites::getUserId, userId)
                .eq(UmsUserFavorites::getProductId, productId);
        return userFavoritesMapper.selectOne(wrapper);
    }

    @Override
    public int clearProductFavorites(Long userId) {
        LambdaQueryWrapper<UmsUserFavorites> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UmsUserFavorites::getUserId, userId);
        int result = userFavoritesMapper.delete(wrapper);
        return result;
    }
}
