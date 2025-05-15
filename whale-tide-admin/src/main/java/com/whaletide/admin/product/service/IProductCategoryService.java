package com.whaletide.admin.product.service;

import com.whaletide.admin.product.dto.ProductCategoryAddDTO;
import com.whaletide.admin.product.dto.ProductCategoryUpdateDTO;
import com.whaletide.admin.product.vo.ProductCategoryListVO;

import java.util.List;

/**
 * 商品分类服务接口
 */
public interface IProductCategoryService {
    /**
     * 获取商品分类列表
     */
    List<ProductCategoryListVO> getProductCategoryList();

    /**
     * 新增商品分类
     */
    Integer addProductCategory(ProductCategoryAddDTO productCategoryAddDTO);

    /**
     * 更新商品分类
     */
    Integer updateProductCategory(ProductCategoryUpdateDTO productCategoryUpdateDTO);

    /**
     * 删除商品分类
     */
    Integer deleteProductCategory(Long id);
}
