package com.whaletide.admin.product.service;

import com.whaletide.admin.product.dto.ProductAddDTO;
import com.whaletide.admin.product.dto.ProductListDTO;
import com.whaletide.admin.product.dto.ProductUpdataStatusDTO;
import com.whaletide.admin.product.dto.ProductUpdateDTO;
import com.whaletide.admin.product.vo.ProductDetailOV;
import com.whaletide.admin.product.vo.ProductListItemVO;
import com.whaletide.admin.product.vo.ProductListVO;


import java.util.List;

/**
 * 商品服务接口
 */
public interface IProductService {

    /**
     * 获取商品列表
     */
    ProductListVO getProductListByKeyword(ProductListDTO listDTO);

    /**
     * 获取商品详情
     */
    ProductDetailOV getProductDetail(Long id);

    /**
     * 新增商品
     */
    Integer addProduct(ProductAddDTO productAddDTO);

    /**
     * 更新商品
     */
    Integer updateProduct(ProductUpdateDTO productUpdateDTO);

    /**
     * 更新商品上架状态
     */
    Integer updateProductStatus(ProductUpdataStatusDTO productUpdataStatusDTO);

    /**
     * 删除商品
     */
    Integer deleteProduct(Long id);

} 