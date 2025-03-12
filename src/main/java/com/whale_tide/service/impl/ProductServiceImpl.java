package com.whale_tide.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale_tide.entity.PmsProducts;
import com.whale_tide.mapper.PmsProductsMapper;
import com.whale_tide.service.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品服务实现类
 */
@Service
@Slf4j
public class ProductServiceImpl implements IProductService {

    @Autowired
    private PmsProductsMapper productsMapper;

    @Override
    @Transactional
    public PmsProducts create(PmsProducts product) {
        if (product == null) {
            return null;
        }
        
        log.info("开始创建商品: {}", product.getName());
        
        // 设置默认值
        product.setCreateTime(LocalDateTime.now());
        
        // 默认值设置
        if (product.getPublishStatus() == null) {
            product.setPublishStatus(0); // 默认下架
        }
        if (product.getRecommendStatus() == null) {
            product.setRecommendStatus(0); // 默认不推荐
        }
        if (product.getNewStatus() == null) {
            product.setNewStatus(0); // 默认不是新品
        }
        if (product.getVerifyStatus() == null) {
            product.setVerifyStatus(0); // 默认未审核
        }
        
        // 保存商品
        int result = productsMapper.insert(product);
        if (result > 0) {
            log.info("商品创建成功, ID: {}, 名称: {}", product.getId(), product.getName());
            return product;
        }
        
        log.error("商品创建失败: {}", product.getName());
        return null;
    }

    @Override
    public PmsProducts getById(Long id) {
        if (id == null) {
            return null;
        }
        PmsProducts product = productsMapper.selectById(id);
        if (product != null) {
            log.info("查询商品成功, ID: {}, 名称: {}", id, product.getName());
        } else {
            log.warn("未找到商品, ID: {}", id);
        }
        return product;
    }

    @Override
    @Transactional
    public boolean update(Long id, PmsProducts product) {
        if (id == null || product == null) {
            return false;
        }
        
        log.info("开始更新商品, ID: {}", id);
        
        // 查询现有商品
        PmsProducts existProduct = productsMapper.selectById(id);
        if (existProduct == null) {
            log.warn("商品更新失败，ID不存在: {}", id);
            return false;
        }
        
        // 设置ID以确保更新而不是新增
        product.setId(id);
        
        // 保留创建时间
        product.setCreateTime(existProduct.getCreateTime());
        
        // 更新修改时间
        product.setUpdateTime(LocalDateTime.now());
        
        // 更新
        int result = productsMapper.updateById(product);
        if (result > 0) {
            log.info("商品更新成功, ID: {}", id);
            return true;
        }
        
        log.warn("商品更新失败, ID: {}", id);
        return false;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        if (id == null) {
            return false;
        }
        
        // 先查询商品是否存在
        PmsProducts product = productsMapper.selectById(id);
        if (product == null) {
            log.warn("删除失败，商品不存在, ID: {}", id);
            return false;
        }
        
        // 删除商品
        int result = productsMapper.deleteById(id);
        log.info("删除商品, ID: {}, 名称: {}, 结果: {}", 
            id, product.getName(), result > 0);
        
        return result > 0;
    }

    @Override
    @Transactional
    public boolean deleteBatch(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        
        log.info("批量删除商品, 数量: {}", ids.size());
        
        // 批量删除商品
        int result = productsMapper.deleteBatchIds(ids);
        
        log.info("批量删除商品完成, 请求删除: {}, 实际删除: {}", ids.size(), result);
        return result > 0;
    }

    @Override
    public org.springframework.data.domain.Page<PmsProducts> list(String keyword, Long brandId, Long categoryId, Integer publishStatus, Pageable pageable) {
        log.info("查询商品列表, 关键字: {}, 品牌ID: {}, 分类ID: {}, 上架状态: {}, 页码: {}, 每页数量: {}", 
            keyword, brandId, categoryId, publishStatus, 
            pageable.getPageNumber(), pageable.getPageSize());
            
        // 创建MyBatis-Plus分页对象
        Page<PmsProducts> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        
        // 构建查询条件
        LambdaQueryWrapper<PmsProducts> queryWrapper = new LambdaQueryWrapper<>();
        
        // 设置条件
        if (brandId != null) {
            queryWrapper.eq(PmsProducts::getBrandId, brandId);
        }
        
        if (categoryId != null) {
            queryWrapper.eq(PmsProducts::getCategoryId, categoryId);
        }
        
        if (publishStatus != null) {
            queryWrapper.eq(PmsProducts::getPublishStatus, publishStatus);
        }

        // 关键字模糊查询
        if (StringUtils.hasText(keyword)) {
            queryWrapper.and(wrapper ->
                    wrapper.like(PmsProducts::getName, keyword)
                            // 下面这些字段根据实体类实际情况进行调整
                            .or().like(PmsProducts::getKeywords, keyword)
                            .or().like(PmsProducts::getBrief, keyword)
                            .or().like(PmsProducts::getName, keyword)
                            .or().like(PmsProducts::getProductSn, keyword)

            );
        }
        
        // 执行查询
        Page<PmsProducts> result = productsMapper.selectPage(page, queryWrapper);
        
        log.info("商品列表查询完成, 总数: {}", result.getTotal());
        
        // 转换为Spring Data Page
        return new org.springframework.data.domain.PageImpl<>(
            result.getRecords(), 
            pageable, 
            result.getTotal()
        );
    }

    @Override
    @Transactional
    public boolean updatePublishStatus(List<Long> ids, Integer publishStatus) {
        if (ids == null || ids.isEmpty() || publishStatus == null) {
            return false;
        }
        
        log.info("更新商品上架状态, 商品数: {}, 状态: {}", ids.size(), publishStatus);
        
        // 批量更新商品上架状态
        LambdaUpdateWrapper<PmsProducts> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(PmsProducts::getId, ids)
                     .set(PmsProducts::getPublishStatus, publishStatus);
        
        int result = productsMapper.update(null, updateWrapper);
        
        log.info("商品上架状态更新完成, 请求更新: {}, 实际更新: {}", ids.size(), result);
        return result > 0;
    }

    @Override
    @Transactional
    public boolean updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        if (ids == null || ids.isEmpty() || recommendStatus == null) {
            return false;
        }
        
        log.info("更新商品推荐状态, 商品数: {}, 状态: {}", ids.size(), recommendStatus);
        
        // 批量更新商品推荐状态
        LambdaUpdateWrapper<PmsProducts> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(PmsProducts::getId, ids)
                     .set(PmsProducts::getRecommendStatus, recommendStatus);
        
        int result = productsMapper.update(null, updateWrapper);
        
        log.info("商品推荐状态更新完成, 请求更新: {}, 实际更新: {}", ids.size(), result);
        return result > 0;
    }

    @Override
    @Transactional
    public boolean updateNewStatus(List<Long> ids, Integer newStatus) {
        if (ids == null || ids.isEmpty() || newStatus == null) {
            return false;
        }
        
        log.info("更新商品新品状态, 商品数: {}, 状态: {}", ids.size(), newStatus);
        
        // 批量更新商品新品状态
        LambdaUpdateWrapper<PmsProducts> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(PmsProducts::getId, ids)
                     .set(PmsProducts::getNewStatus, newStatus);
        
        int result = productsMapper.update(null, updateWrapper);
        
        log.info("商品新品状态更新完成, 请求更新: {}, 实际更新: {}", ids.size(), result);
        return result > 0;
    }

    @Override
    @Transactional
    public boolean updateDeleteStatus(List<Long> ids, Integer deleteStatus) {
        if (ids == null || ids.isEmpty() || deleteStatus == null) {
            return false;
        }
        
        log.info("更新商品删除状态, 商品数: {}, 状态: {}", ids.size(), deleteStatus);
        
        // 批量更新商品删除状态（逻辑删除）
        LambdaUpdateWrapper<PmsProducts> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(PmsProducts::getId, ids);
        
        // 使用批量更新方法，不依赖实体类中的getDeleteStatus方法
        if (deleteStatus == 1) {
            // 直接使用列名进行更新
            int result = productsMapper.updatePublishStatus(ids, 1);
            log.info("商品删除状态更新完成, 请求更新: {}, 实际更新: {}", ids.size(), result);
            return result > 0;
        } else {
            int result = productsMapper.updatePublishStatus(ids, 0);
            log.info("商品删除状态更新完成, 请求更新: {}, 实际更新: {}", ids.size(), result);
            return result > 0;
        }
    }
} 