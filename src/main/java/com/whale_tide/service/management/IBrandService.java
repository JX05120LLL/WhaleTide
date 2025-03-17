package com.whale_tide.service.management;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale_tide.dto.management.brand.BrandParam;
import com.whale_tide.entity.pms.PmsBrands;

import java.util.List;

/**
 * 品牌管理Service
 */
public interface IBrandService {
    /**
     * 获取所有品牌
     */
    List<PmsBrands> listAllBrand();

    /**
     * 创建品牌
     */
    Long createBrand(BrandParam brandParam);

    /**
     * 更新品牌
     */
    int updateBrand(Long id, BrandParam brandParam);

    /**
     * 删除品牌
     */
    int deleteBrand(Long id);

    /**
     * 批量删除品牌
     */
    int deleteBrand(List<Long> ids);

    /**
     * 分页查询品牌
     */
    Page<PmsBrands> listBrand(String keyword, Integer pageNum, Integer pageSize);

    /**
     * 获取品牌
     */
    PmsBrands getBrand(Long id);

    /**
     * 批量更新显示状态
     */
    int updateShowStatus(List<Long> ids, Integer showStatus);

    /**
     * 批量更新厂家制造商状态
     */
    int updateFactoryStatus(List<Long> ids, Integer factoryStatus);

    /**
     * 初始化品牌测试数据
     */
    boolean initData();
} 