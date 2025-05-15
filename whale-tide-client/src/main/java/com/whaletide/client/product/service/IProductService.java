package com.whaletide.client.product.service;

import com.whaletide.client.product.dto.ProductSearchDTO;
import com.whaletide.client.product.vo.CategoryTreeVO;
import com.whaletide.client.product.vo.ProductDetailVO;
import com.whaletide.client.product.vo.ProductListItemVO;
import com.whaletide.client.product.vo.ProductSuggestionVO;
import com.whaletide.common.api.CommonPage;
import com.whaletide.common.api.CommonResult;
import com.whaletide.client.product.vo.BrandVO;

import java.util.List;

/**
 * 商品服务接口
 */
public interface IProductService {
    
    /**
     * 根据关键词搜索商品
     * @param searchDTO 搜索条件
     * @return 商品列表
     */
    List<ProductListItemVO> getProductsByKeyword(ProductSearchDTO searchDTO);
    
    /**
     * 获取商品分类树
     * @return 分类树数据
     */
    CategoryTreeVO getCategoryTree();
    
    /**
     * 获取商品详情
     * @param id 商品ID
     * @return 商品详情
     */
    ProductDetailVO getProductDetail(Long id);
    
    /**
     * 获取热门搜索关键词
     * @return 热门关键词列表
     */
    List<String> getHotKeywords();
    
    /**
     * 获取搜索建议
     * @param keyword 关键词
     * @return 搜索建议列表
     */
    List<ProductSuggestionVO> getSuggestions(String keyword);

    /**
     * 获取品牌列表
     * @param limit 数量限制
     * @return 品牌列表
     */
    List<BrandVO> getBrands(Integer limit);
    
    /**
     * 获取分类商品列表
     * @param categoryId 分类ID
     * @param page 页码
     * @param pageSize 每页记录数
     * @param sort 排序字段
     * @return 分类商品列表
     */
    List<ProductListItemVO> getCategoryProducts(Long categoryId, Integer page, Integer pageSize, String sort);
    
    /**
     * 获取相关商品推荐
     * @param productId 商品ID
     * @param limit 数量限制
     * @return 相关商品列表
     */
    List<ProductListItemVO> getRelatedProducts(Long productId, Integer limit);
} 