package com.whaletide.admin.product.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whaletide.admin.product.dto.ProductAddDTO;
import com.whaletide.admin.product.dto.ProductListDTO;
import com.whaletide.admin.product.dto.ProductUpdataStatusDTO;
import com.whaletide.admin.product.dto.ProductUpdateDTO;
import com.whaletide.admin.product.service.IProductService;
import com.whaletide.admin.product.vo.ProductDetailOV;
import com.whaletide.admin.product.vo.ProductListItemVO;
import com.whaletide.admin.product.vo.ProductListVO;
import com.whaletide.common.entity.pms.PmsProducts;
import com.whaletide.common.mapper.pms.PmsProductCategoriesMapper;
import com.whaletide.common.mapper.pms.PmsProductsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 商品服务实现类
 */
@Service
@Slf4j
public class ProductServiceImpl implements IProductService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private PmsProductsMapper productsMapper;

    @Autowired
    private PmsProductCategoriesMapper categoriesMapper;


    //获取商品列表
    public ProductListVO getProductListByKeyword(ProductListDTO listDTO) {
        Page<PmsProducts> page = new Page<>(listDTO.getPage(), listDTO.getPageSize());

        //关键字搜索 分页
        if (listDTO.getKeyword() != null && !listDTO.getKeyword().equals("") && listDTO.getStatus() != null) {
            LambdaQueryWrapper<PmsProducts> wrapper = new LambdaQueryWrapper<>();
            wrapper.like(PmsProducts::getName, listDTO.getKeyword());
            wrapper.eq(PmsProducts::getProductStatus, listDTO.getStatus());
            page = productsMapper.selectPage(page, wrapper);
        } else if (listDTO.getStatus() != null) {
            LambdaQueryWrapper<PmsProducts> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(PmsProducts::getPublishStatus, listDTO.getStatus());
            page = productsMapper.selectPage(page, wrapper);
        } else if (listDTO.getKeyword() != null && !listDTO.getKeyword().equals("")) {
            LambdaQueryWrapper<PmsProducts> wrapper = new LambdaQueryWrapper<>();
            wrapper.like(PmsProducts::getName, listDTO.getKeyword());
            page = productsMapper.selectPage(page, wrapper);
        } else {
            //搜索条件为空，查询所有商品
            page = productsMapper.selectPage(page, null);
        }
        //转换
        List<ProductListItemVO> productList = new ArrayList<>();
        for (PmsProducts product : page.getRecords()) {
            ProductListItemVO item = new ProductListItemVO();
            item.setId(product.getId());
            item.setName(product.getName());
            item.setCategoryId(product.getCategoryId());
            item.setCategoryName(categoriesMapper.selectById(product.getCategoryId()).getName());
            item.setPrice(BigDecimal.valueOf(product.getPrice()));
//            item.setOriginalPrice(BigDecimal.valueOf(product.getPrice()));
            item.setCoverImage(product.getMainImage());
//            item.setSales(product.getSales());
            item.setStock(product.getStock());
            item.setStatus(product.getPublishStatus());
            item.setCreateTime(product.getCreateTime());
            item.setUpdateTime(product.getUpdateTime());
            productList.add(item);
        }

        ProductListVO productListVO = new ProductListVO();
        productListVO.setTotal(page.getTotal());
        productListVO.setList(productList);

        return productListVO;

    }

    @Override
    public ProductDetailOV getProductDetail(Long id) {
        PmsProducts product = productsMapper.selectById(id);
        if (product != null) {
            ProductDetailOV productDetailOV = new ProductDetailOV();
            productDetailOV.setId(product.getId());
            productDetailOV.setName(product.getName());
            productDetailOV.setDescription(product.getBrief());
            productDetailOV.setPrice(BigDecimal.valueOf(product.getPrice()));
            productDetailOV.setStock(product.getStock());
            productDetailOV.setInitialStock(product.getStock());
            productDetailOV.setStatus(product.getPublishStatus());
            productDetailOV.setCategory(categoriesMapper.selectById(product.getCategoryId()).getName());
            productDetailOV.setCategoryId(product.getCategoryId());
            productDetailOV.setImage(product.getMainImage());

            if (product.getProductImages() != null) {
                String[] images = product.getProductImages().toArray(new String[product.getProductImages().size()]);
                productDetailOV.setImages(images);
            }
            productDetailOV.setCreateTime(product.getCreateTime());
            productDetailOV.setUpdateTime(product.getUpdateTime());
            return productDetailOV;
        }
        return null;
    }

    @Override
    public Integer addProduct(ProductAddDTO productAddDTO) {
        PmsProducts product = new PmsProducts();

        product.setName(productAddDTO.getName());
        product.setBrief(productAddDTO.getDescription());
        product.setPrice(productAddDTO.getPrice().doubleValue());
        product.setStock(productAddDTO.getStock());
        product.setCategoryId(productAddDTO.getCategoryId());
        product.setMainImage(productAddDTO.getImage());

        if (productAddDTO.getImages() != null && productAddDTO.getImages().length > 0) {
            List<String> productImages = Arrays.asList(productAddDTO.getImages());
            product.setProductImages(productImages);
        }
        product.setPublishStatus(productAddDTO.getStatus());
        product.setProductStatus(1);
        product.setCreateTime(LocalDateTime.now());
        product.setUpdateTime(LocalDateTime.now());

        int result = productsMapper.insert(product);
        if (result > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public Integer updateProduct(ProductUpdateDTO productUpdateDTO) {
        PmsProducts product = productsMapper.selectById(productUpdateDTO.getId());

        if (product == null) {
            return -1;
        }

        product.setName(productUpdateDTO.getName());
        product.setBrief(productUpdateDTO.getDescription());
        product.setPrice(productUpdateDTO.getPrice().doubleValue());
        product.setStock(productUpdateDTO.getStock());
        product.setCategoryId(productUpdateDTO.getCategoryId());
        product.setProductStatus(productUpdateDTO.getStatus());
        product.setUpdateTime(LocalDateTime.now());

        int result = productsMapper.updateById(product);
        return result;
    }

    @Override
    public Integer updateProductStatus(ProductUpdataStatusDTO productUpdataStatusDTO) {
        PmsProducts product = productsMapper.selectById(productUpdataStatusDTO.getId());
        if (product == null) {
            return -1;
        }
        product.setPublishStatus(productUpdataStatusDTO.getStatus());
        product.setUpdateTime(LocalDateTime.now());
        int result = productsMapper.updateById(product);
        return result;
    }

    @Override
    public Integer deleteProduct(Long id) {
        if (id == null)
            return -1;

        int result = productsMapper.deleteById(id);
        return result;
    }
}