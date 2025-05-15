package com.whaletide.client.user.service;

import com.whaletide.client.user.dto.FavoriteAddDTO;
import com.whaletide.client.user.vo.FavoriteVO;
import com.whaletide.common.api.CommonPage;

/**
 * 用户收藏服务接口
 */
public interface IFavoriteService {
    
    /**
     * 添加收藏
     * @param favoriteAddDTO 收藏信息
     * @return 是否成功
     */
    Boolean add(FavoriteAddDTO favoriteAddDTO);
    
    /**
     * 取消收藏
     * @param productId 商品ID
     * @return 是否成功
     */
    Boolean delete(Long productId);
    
    /**
     * 检查商品是否已收藏
     * @param productId 商品ID
     * @return 是否已收藏
     */
    Boolean checkFavorite(Long productId);
    
    /**
     * 获取收藏列表
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 收藏分页列表
     */
    CommonPage<FavoriteVO> list(Integer pageNum, Integer pageSize);
} 