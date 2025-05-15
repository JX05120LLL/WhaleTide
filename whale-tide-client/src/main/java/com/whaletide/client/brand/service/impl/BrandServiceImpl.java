package com.whaletide.client.brand.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.whaletide.client.brand.service.IBrandService;
import com.whaletide.client.brand.vo.BrandVO;
import com.whaletide.client.product.vo.ProductVO;
import com.whaletide.common.api.CommonResult;
import com.whaletide.common.entity.pms.PmsBrands;
import com.whaletide.common.entity.pms.PmsProducts;
import com.whaletide.common.exception.base.BusinessException;
import com.whaletide.common.exception.base.ResourceNotFoundException;
import com.whaletide.common.exception.base.ValidationException;
import com.whaletide.common.mapper.pms.PmsBrandsMapper;
import com.whaletide.common.mapper.pms.PmsProductsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 品牌服务实现类
 */
@Service
@Slf4j
public class BrandServiceImpl implements IBrandService {

    @Autowired
    private PmsBrandsMapper brandsMapper;
    
    @Autowired
    private PmsProductsMapper productsMapper;

    /**
     * 获取品牌列表
     */
    @Override
    public CommonResult<List<BrandVO>> list() {
        // 查询启用的品牌列表
        LambdaQueryWrapper<PmsBrands> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PmsBrands::getStatus, 1)
                .orderByDesc(PmsBrands::getIsFeatured)
                .orderByDesc(PmsBrands::getCreateTime);
        
        List<PmsBrands> brands = brandsMapper.selectList(queryWrapper);
        if (brands.isEmpty()) {
            return CommonResult.success(new ArrayList<>());
        }
        
        // 转换为VO
        List<BrandVO> result = brands.stream().map(this::convertToVO).collect(Collectors.toList());
        
        return CommonResult.success(result);
    }

    /**
     * 获取品牌详情
     */
    @Override
    public CommonResult<BrandVO> getById(Long id) {
        // 参数校验
        if (id == null) {
            throw new ValidationException("品牌ID不能为空");
        }
        
        // 查询品牌
        PmsBrands brand = brandsMapper.selectById(id);
        if (brand == null || brand.getStatus() != 1) {
            throw new ResourceNotFoundException("品牌不存在或已下架");
        }
        
        // 转换为VO
        BrandVO result = convertToVO(brand);
        
        // 查询该品牌下的产品
        LambdaQueryWrapper<PmsProducts> productQueryWrapper = new LambdaQueryWrapper<>();
        productQueryWrapper.eq(PmsProducts::getBrandId, id)
                .eq(PmsProducts::getPublishStatus, 1) // 只查询上架的产品
                .orderByDesc(PmsProducts::getCreateTime);
        
        List<PmsProducts> products = productsMapper.selectList(productQueryWrapper);
        
        // 将产品转换为ProductVO并设置到BrandVO中
        if (!products.isEmpty()) {
            List<ProductVO> productVOs = products.stream()
                    .map(this::convertToProductVO)
                    .collect(Collectors.toList());
            result.setProducts(productVOs);
        }
        
        return CommonResult.success(result);
    }

    /**
     * 将产品实体转换为ProductVO
     */
    private ProductVO convertToProductVO(PmsProducts product) {
        ProductVO vo = new ProductVO();
        vo.setId(product.getId());
        vo.setName(product.getName());
        vo.setSubTitle(product.getBrief());
        vo.setPic(product.getMainImage());
        vo.setPrice(product.getPrice());
        vo.setOriginalPrice(product.getPrice()); // 如果没有原价字段，使用当前价格
        vo.setSaleCount(0); // 如果没有销量字段，使用默认值
        vo.setStock(product.getStock());
        vo.setBrandId(product.getBrandId());
        vo.setCategoryId(product.getCategoryId());
        return vo;
    }

    /**
     * 获取推荐品牌列表
     */
    @Override
    public CommonResult<List<BrandVO>> listFeatured() {
        // 查询推荐的品牌列表
        LambdaQueryWrapper<PmsBrands> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PmsBrands::getStatus, 1)
                .eq(PmsBrands::getIsFeatured, 1)
                .orderByDesc(PmsBrands::getCreateTime);
        
        List<PmsBrands> brands = brandsMapper.selectList(queryWrapper);
        if (brands.isEmpty()) {
            return CommonResult.success(new ArrayList<>());
        }
        
        // 转换为VO
        List<BrandVO> result = brands.stream().map(this::convertToVO).collect(Collectors.toList());
        
        return CommonResult.success(result);
    }

    /**
     * 搜索品牌
     */
    @Override
    public CommonResult<List<BrandVO>> search(String keyword) {
        // 参数校验
        if (!StringUtils.hasText(keyword)) {
            throw new ValidationException("搜索关键词不能为空");
        }
        
        // 搜索品牌
        LambdaQueryWrapper<PmsBrands> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PmsBrands::getStatus, 1)
                .like(PmsBrands::getName, keyword)
                .orderByDesc(PmsBrands::getIsFeatured)
                .orderByDesc(PmsBrands::getCreateTime);
        
        List<PmsBrands> brands = brandsMapper.selectList(queryWrapper);
        if (brands.isEmpty()) {
            return CommonResult.success(new ArrayList<>());
        }
        
        // 转换为VO
        List<BrandVO> result = brands.stream().map(this::convertToVO).collect(Collectors.toList());
        
        return CommonResult.success(result);
    }

    /**
     * 转换为VO
     */
    private BrandVO convertToVO(PmsBrands brand) {
        BrandVO vo = new BrandVO();
        vo.setId(brand.getId());
        vo.setName(brand.getName());
        vo.setLogo(brand.getLogo());
        vo.setDescription(brand.getDescription());
        vo.setIsFeatured(brand.getIsFeatured());
        return vo;
    }
} 