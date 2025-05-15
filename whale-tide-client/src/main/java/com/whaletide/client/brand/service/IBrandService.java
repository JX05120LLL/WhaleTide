package com.whaletide.client.brand.service;

import com.whaletide.client.brand.vo.BrandVO;
import com.whaletide.common.api.CommonResult;

import java.util.List;

/**
 * 品牌服务接口
 */
public interface IBrandService {

    /**
     * 获取品牌列表
     * @return 品牌列表
     */
    CommonResult<List<BrandVO>> list();

    /**
     * 获取品牌详情
     * @param id 品牌ID
     * @return 品牌详情
     */
    CommonResult<BrandVO> getById(Long id);

    /**
     * 获取推荐品牌列表
     * @return 推荐品牌列表
     */
    CommonResult<List<BrandVO>> listFeatured();

    /**
     * 搜索品牌
     * @param keyword 关键词
     * @return 品牌列表
     */
    CommonResult<List<BrandVO>> search(String keyword);
} 