package com.whale_tide.service.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale_tide.entity.ums.UmsUserBrandAttentions;

public interface IBrandAttentionService {
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
