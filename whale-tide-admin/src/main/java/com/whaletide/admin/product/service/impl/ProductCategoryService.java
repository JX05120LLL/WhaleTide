package com.whaletide.admin.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.whaletide.admin.product.dto.ProductCategoryAddDTO;
import com.whaletide.admin.product.dto.ProductCategoryUpdateDTO;
import com.whaletide.admin.product.service.IProductCategoryService;
import com.whaletide.admin.product.vo.ProductCategoryListVO;
import com.whaletide.common.entity.pms.PmsProductCategories;
import com.whaletide.common.mapper.pms.PmsProductCategoriesMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ProductCategoryService implements IProductCategoryService {

    @Autowired
    private PmsProductCategoriesMapper productCategoryMapper;


    @Override
    public List<ProductCategoryListVO> getProductCategoryList() {

        List<PmsProductCategories> list = productCategoryMapper.selectList(null);

        List<ProductCategoryListVO> result = null;
        if (list != null || list.size() != 0) {
            result = new ArrayList<>();
            for (PmsProductCategories category : list) {
                ProductCategoryListVO vo = new ProductCategoryListVO();
                vo.setId(category.getId());
                vo.setName(category.getName());
                vo.setDescription(category.getDescription());
                vo.setSort(category.getSort());
                vo.setCreateTime(category.getCreateTime());
                vo.setUpdateTime(category.getUpdateTime());
                result.add(vo);
            }
        }

        return result;
    }

    @Override
    public Integer addProductCategory(ProductCategoryAddDTO productCategoryAddDTO) {
        LambdaQueryWrapper<PmsProductCategories> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(PmsProductCategories::getName, productCategoryAddDTO.getName());
        if (productCategoryMapper.selectOne(lambdaQueryWrapper) != null)
            return -1;

        PmsProductCategories pmsProductCategories = new PmsProductCategories();
        pmsProductCategories.setName(productCategoryAddDTO.getName());
        pmsProductCategories.setDescription(productCategoryAddDTO.getDescription());
        pmsProductCategories.setSort(0);
        pmsProductCategories.setStatus(1);
        pmsProductCategories.setIsNav(1);
        pmsProductCategories.setCreateTime(LocalDateTime.now());
        pmsProductCategories.setUpdateTime(LocalDateTime.now());


        int result = productCategoryMapper.insert(pmsProductCategories);
        if (result == 1)
            return 1;
        else
            return -3;
    }

    @Override
    public Integer updateProductCategory(ProductCategoryUpdateDTO productCategoryUpdateDTO) {
        //查询名称是否存在;
        LambdaQueryWrapper<PmsProductCategories> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(PmsProductCategories::getName, productCategoryUpdateDTO.getName());
        if (productCategoryMapper.selectOne(lambdaQueryWrapper) != null && !productCategoryUpdateDTO.getId().equals(productCategoryMapper.selectOne(lambdaQueryWrapper).getId()))
            return -2;

        PmsProductCategories pmsProductCategories = productCategoryMapper.selectById(productCategoryUpdateDTO.getId());
        if (pmsProductCategories == null)
            return -1;

        pmsProductCategories.setName(productCategoryUpdateDTO.getName());
        pmsProductCategories.setDescription(productCategoryUpdateDTO.getDescription());
        pmsProductCategories.setUpdateTime(LocalDateTime.now());

        int result = productCategoryMapper.updateById(pmsProductCategories);
        return result;
    }

    @Override
    public Integer deleteProductCategory(Long id) {
        int result = productCategoryMapper.deleteById(id);

        return result;
    }
}
