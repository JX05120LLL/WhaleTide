package com.whale_tide.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whale_tide.dto.brand.BrandParam;
import com.whale_tide.entity.pms.PmsBrands;
import com.whale_tide.mapper.pms.PmsBrandsMapper;
import com.whale_tide.service.IBrandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 品牌管理Service实现类
 */
@Service
@Slf4j
public class BrandServiceImpl extends ServiceImpl<PmsBrandsMapper, PmsBrands> implements IBrandService {

    @Override
    public List<PmsBrands> listAllBrand() {
        return list();
    }

    @Override
    public Long createBrand(BrandParam brandParam) {
        PmsBrands brand = new PmsBrands();
        BeanUtils.copyProperties(brandParam, brand);
        // 品牌的初始值设置
        if (brand.getStatus() == null) {
            brand.setStatus(1);
        }
        if (brand.getIsFeatured() == null) {
            brand.setIsFeatured(1);
        }
        brand.setCreateTime(LocalDateTime.now());
        brand.setUpdateTime(LocalDateTime.now());
        save(brand);
        return brand.getId();
    }

    @Override
    public int updateBrand(Long id, BrandParam brandParam) {
        PmsBrands brand = getById(id);
        if (brand == null) {
            return 0;
        }
        BeanUtils.copyProperties(brandParam, brand);
        brand.setId(id);
        brand.setUpdateTime(LocalDateTime.now());
        if (updateById(brand)) {
            return 1;
        }
        return 0;
    }

    @Override
    public int deleteBrand(Long id) {
        if (removeById(id)) {
            return 1;
        }
        return 0;
    }

    @Override
    public int deleteBrand(List<Long> ids) {
        if (removeByIds(ids)) {
            return ids.size();
        }
        return 0;
    }

    @Override
    public Page<PmsBrands> listBrand(String keyword, Integer pageNum, Integer pageSize) {
        Page<PmsBrands> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PmsBrands> queryWrapper = new LambdaQueryWrapper<>();
        
        if (!StringUtils.isEmpty(keyword)) {
            queryWrapper.like(PmsBrands::getName, keyword);
        }
        queryWrapper.orderByAsc(PmsBrands::getSort);
        
        return page(page, queryWrapper);
    }

    @Override
    public PmsBrands getBrand(Long id) {
        return getById(id);
    }

    @Override
    public int updateShowStatus(List<Long> ids, Integer showStatus) {
        UpdateWrapper<PmsBrands> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", showStatus)
                     .in("id", ids);
        
        if (update(updateWrapper)) {
            return ids.size();
        }
        return 0;
    }

    @Override
    public int updateFactoryStatus(List<Long> ids, Integer factoryStatus) {
        UpdateWrapper<PmsBrands> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("is_featured", factoryStatus)
                     .in("id", ids);
        
        if (update(updateWrapper)) {
            return ids.size();
        }
        return 0;
    }

    /**
     * 初始化品牌测试数据
     */
    @Override
    public boolean initData() {
        try {
            // 清空现有数据
            List<PmsBrands> existingBrands = list();
            if (!existingBrands.isEmpty()) {
                List<Long> ids = existingBrands.stream()
                    .map(PmsBrands::getId)
                    .collect(Collectors.toList());
                removeByIds(ids);
            }
            
            // 添加示例品牌数据
            List<PmsBrands> brands = new ArrayList<>();
            
            PmsBrands brand1 = new PmsBrands();
            brand1.setName("耐克");
            brand1.setFirstLetter("N");
            brand1.setSort(1);
            brand1.setIsFeatured(1);
            brand1.setStatus(1);
            brand1.setLogo("https://example.com/nike.jpg");
            brand1.setDescription("Just Do It");
            brand1.setCreateTime(LocalDateTime.now());
            brand1.setUpdateTime(LocalDateTime.now());
            brands.add(brand1);
            
            PmsBrands brand2 = new PmsBrands();
            brand2.setName("阿迪达斯");
            brand2.setFirstLetter("A");
            brand2.setSort(2);
            brand2.setIsFeatured(1);
            brand2.setStatus(1);
            brand2.setLogo("https://example.com/adidas.jpg");
            brand2.setDescription("Impossible Is Nothing");
            brand2.setCreateTime(LocalDateTime.now());
            brand2.setUpdateTime(LocalDateTime.now());
            brands.add(brand2);
            
            PmsBrands brand3 = new PmsBrands();
            brand3.setName("华为");
            brand3.setFirstLetter("H");
            brand3.setSort(3);
            brand3.setIsFeatured(1);
            brand3.setStatus(1);
            brand3.setLogo("https://example.com/huawei.jpg");
            brand3.setDescription("科技让生活更美好");
            brand3.setCreateTime(LocalDateTime.now());
            brand3.setUpdateTime(LocalDateTime.now());
            brands.add(brand3);
            
            PmsBrands brand4 = new PmsBrands();
            brand4.setName("苹果");
            brand4.setFirstLetter("P");
            brand4.setSort(4);
            brand4.setIsFeatured(1);
            brand4.setStatus(1);
            brand4.setLogo("https://example.com/apple.jpg");
            brand4.setDescription("Think Different");
            brand4.setCreateTime(LocalDateTime.now());
            brand4.setUpdateTime(LocalDateTime.now());
            brands.add(brand4);
            
            PmsBrands brand5 = new PmsBrands();
            brand5.setName("小米");
            brand5.setFirstLetter("X");
            brand5.setSort(5);
            brand5.setIsFeatured(1);
            brand5.setStatus(1);
            brand5.setLogo("https://example.com/xiaomi.jpg");
            brand5.setDescription("为发烧而生");
            brand5.setCreateTime(LocalDateTime.now());
            brand5.setUpdateTime(LocalDateTime.now());
            brands.add(brand5);
            
            return saveBatch(brands);
        } catch (Exception e) {
            log.error("初始化品牌数据失败", e);
            return false;
        }
    }
} 