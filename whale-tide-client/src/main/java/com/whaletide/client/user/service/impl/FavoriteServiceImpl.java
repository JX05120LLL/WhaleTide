package com.whaletide.client.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whaletide.client.product.service.IProductService;
import com.whaletide.client.product.vo.ProductDetailVO;
import com.whaletide.client.user.dto.FavoriteAddDTO;
import com.whaletide.client.user.service.IFavoriteService;
import com.whaletide.client.user.service.IUserService;
import com.whaletide.client.user.vo.FavoriteVO;
import com.whaletide.client.user.vo.UserVO;
import com.whaletide.common.api.CommonPage;
import com.whaletide.common.entity.ums.UmsUserFavorites;
import com.whaletide.common.exception.BusinessException;
import com.whaletide.common.exception.NotFoundException;
import com.whaletide.common.exception.UnauthorizedException;
import com.whaletide.common.mapper.ums.UmsUserFavoritesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户收藏服务实现
 */
@Service
public class FavoriteServiceImpl implements IFavoriteService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(FavoriteServiceImpl.class);
    
    @Autowired
    private IUserService userService;
    
    @Autowired
    private IProductService productService;
    
    @Autowired
    private UmsUserFavoritesMapper userFavoritesMapper;
    
    /**
     * 添加收藏
     * 
     * @param favoriteAddDTO 收藏信息
     * @return 是否成功
     */
    @Override
    @Transactional
    public Boolean add(FavoriteAddDTO favoriteAddDTO) {
        // 获取当前登录用户
        UserVO currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new UnauthorizedException("用户未登录");
        }
        
        // 检查商品是否已收藏
        if (checkFavorite(favoriteAddDTO.getProductId())) {
            LOGGER.info("用户:{} 已收藏商品:{}", currentUser.getId(), favoriteAddDTO.getProductId());
            return true;
        }
        
        // 获取商品详情，检查商品是否存在
        ProductDetailVO productDetail;
        try {
            productDetail = productService.getProductDetail(favoriteAddDTO.getProductId());
            if (productDetail == null) {
                throw new NotFoundException("商品不存在");
            }
        } catch (Exception e) {
            LOGGER.error("添加收藏失败，商品不存在: {}", favoriteAddDTO.getProductId(), e);
            throw new NotFoundException("商品不存在");
        }
        
        try {
            // 创建收藏记录
            UmsUserFavorites favorite = new UmsUserFavorites();
            favorite.setUserId(currentUser.getId());
            favorite.setProductId(favoriteAddDTO.getProductId());
            favorite.setProductName(productDetail.getName());
            favorite.setProductPic(productDetail.getPic());
            favorite.setPrice(new BigDecimal(String.valueOf(productDetail.getPrice())));
            favorite.setCreateTime(LocalDateTime.now());
            
            // 保存到数据库
            int result = userFavoritesMapper.insert(favorite);
            if (result <= 0) {
                throw new BusinessException("添加收藏失败");
            }
            
            LOGGER.info("用户:{} 成功收藏商品:{}", currentUser.getId(), favoriteAddDTO.getProductId());
            return true;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("添加收藏失败: {}", e.getMessage(), e);
            throw new BusinessException("添加收藏失败: " + e.getMessage());
        }
    }
    
    /**
     * 取消收藏
     * 
     * @param productId 商品ID
     * @return 是否成功
     */
    @Override
    @Transactional
    public Boolean delete(Long productId) {
        // 获取当前登录用户
        UserVO currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new UnauthorizedException("用户未登录");
        }
        
        try {
            // 查询收藏记录
            LambdaQueryWrapper<UmsUserFavorites> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UmsUserFavorites::getUserId, currentUser.getId())
                    .eq(UmsUserFavorites::getProductId, productId);
            
            // 删除收藏记录
            int result = userFavoritesMapper.delete(queryWrapper);
            
            LOGGER.info("用户:{} 成功取消收藏商品:{}, 删除记录数:{}", currentUser.getId(), productId, result);
            return true;
        } catch (Exception e) {
            LOGGER.error("取消收藏失败: {}", e.getMessage(), e);
            throw new BusinessException("取消收藏失败: " + e.getMessage());
        }
    }
    
    /**
     * 检查商品是否已收藏
     * 
     * @param productId 商品ID
     * @return 是否已收藏
     */
    @Override
    public Boolean checkFavorite(Long productId) {
        // 获取当前登录用户
        UserVO currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new UnauthorizedException("用户未登录");
        }
        
        try {
            // 查询收藏记录
            LambdaQueryWrapper<UmsUserFavorites> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UmsUserFavorites::getUserId, currentUser.getId())
                    .eq(UmsUserFavorites::getProductId, productId);
            
            // 统计记录数
            Long count = userFavoritesMapper.selectCount(queryWrapper);
            
            return count > 0;
        } catch (Exception e) {
            LOGGER.error("检查收藏状态失败: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 获取收藏列表
     * 
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return 收藏分页列表
     */
    @Override
    public CommonPage<FavoriteVO> list(Integer pageNum, Integer pageSize) {
        // 获取当前登录用户
        UserVO currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new UnauthorizedException("用户未登录");
        }
        
        try {
            // 构建查询条件
            LambdaQueryWrapper<UmsUserFavorites> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UmsUserFavorites::getUserId, currentUser.getId())
                    .orderByDesc(UmsUserFavorites::getCreateTime);
            
            // 分页查询
            Page<UmsUserFavorites> page = new Page<>(pageNum, pageSize);
            Page<UmsUserFavorites> favoritesPage = userFavoritesMapper.selectPage(page, queryWrapper);
            
            // 转换为VO
            List<FavoriteVO> favoriteVOList = favoritesPage.getRecords().stream().map(item -> {
                FavoriteVO vo = new FavoriteVO();
                vo.setId(item.getId());
                vo.setProductId(item.getProductId());
                vo.setProductName(item.getProductName());
                vo.setProductPic(item.getProductPic());
                vo.setProductPrice(item.getPrice());
                vo.setCreateTime(item.getCreateTime());
                return vo;
            }).collect(Collectors.toList());
            
            // 构建分页结果
            CommonPage<FavoriteVO> result = new CommonPage<>();
            result.setList(favoriteVOList);
            result.setPageNum((int)favoritesPage.getCurrent());
            result.setPageSize((int)favoritesPage.getSize());
            result.setTotal(favoritesPage.getTotal());
            result.setTotalPage((int)favoritesPage.getPages());
            
            return result;
        } catch (Exception e) {
            LOGGER.error("获取收藏列表失败: {}", e.getMessage(), e);
            throw new BusinessException("获取收藏列表失败: " + e.getMessage());
        }
    }
} 