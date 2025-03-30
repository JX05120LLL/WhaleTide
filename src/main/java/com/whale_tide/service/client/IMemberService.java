package com.whale_tide.service.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale_tide.entity.ums.UmsUserBrandAttentions;
import com.whale_tide.entity.ums.UmsUserFavorites;

public interface IMemberService {
    //添加商品收藏
    int addProductFavorite(UmsUserFavorites userFavorites, Long userId);

    //删除商品收藏
    int deleteProductFavorite(Long userId, Long productId);

    //获取用户收藏列表
    Page<UmsUserFavorites> getProductFavorites(Long userId, Long pageNum, Long pageSize);

    //获取商品收藏详情
    UmsUserFavorites getProductFavoriteDetail(Long userId, Long productId);

    //清空用户商品收藏
    int clearProductFavorites(Long userId);

    //关注品牌
    int addBrandAttention(UmsUserBrandAttentions userBrandAttentions);

    //取消关注品牌
    int deleteBrandAttention(Long userId, Long brandId);

    //获取用户关注品牌列表
    Page<UmsUserBrandAttentions> getBrandAttentionList(Long userId, Long pageNum, Long pageSize);

    //获取品牌关注详情
    UmsUserBrandAttentions getBrandAttentionDetail(Long userId, Long brandId);

    //清空用户品牌关注
    int clearBrandAttention(Long userId);
}
