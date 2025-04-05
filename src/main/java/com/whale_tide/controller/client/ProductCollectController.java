package com.whale_tide.controller.client;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale_tide.common.api.CommonResult;
import com.whale_tide.dto.client.productCollection.ProductCollectionPageResponse;
import com.whale_tide.dto.client.productCollection.ProductCollectionRequest;
import com.whale_tide.dto.client.productCollection.ProductCollectionResponse;
import com.whale_tide.entity.ums.UmsUserFavorites;
import com.whale_tide.service.client.IProductCollectService;
import com.whale_tide.service.client.IUserService;
import com.whale_tide.util.JwtUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Api(tags = "用户产品收藏")
@RestController("clientProductCollectionController")
@RequestMapping("/member/productCollection")
public class ProductCollectController {

    @Autowired
    private IProductCollectService productCollectService;

    @Autowired
    private IUserService userService;

    @Autowired
    private JwtUtil jwtUtil;


    /**
     * 添加商品收藏
     *
     * @param productCollectionRequest 商品收藏请求
     * @param token                    用户token
     * @return CommonResult
     */
    @RequestMapping("/add")
    public CommonResult addProductCollect(@RequestBody ProductCollectionRequest productCollectionRequest, @RequestHeader("Authorization") String token) {
        try {
            // 验证必要的参数
            if (productCollectionRequest == null || productCollectionRequest.getProductId() == null) {
                return CommonResult.failed("商品ID不能为空");
            }
            
            UmsUserFavorites userFavorites = new UmsUserFavorites();
            userFavorites.setUserId(userService.getUserInfo(jwtUtil.getUsernameFromToken(token)).getId());
            userFavorites.setProductId(productCollectionRequest.getProductId());
            userFavorites.setProductName(productCollectionRequest.getProductName());
            userFavorites.setProductImage(productCollectionRequest.getProductPic());
            userFavorites.setProductPrice(productCollectionRequest.getProductPrice());
            userFavorites.setNote(productCollectionRequest.getProductSubTitle());
            
            int result = productCollectService.addProductFavorite(userFavorites, userFavorites.getUserId());

            if (result == 1) {
                return CommonResult.success(null);
            } else {
                return CommonResult.failed("添加收藏失败");
            }
        } catch (Exception e) {
            return CommonResult.failed("添加收藏异常: " + e.getMessage());
        }
    }

    /**
     * 删除商品收藏
     */
    @RequestMapping("/delete")
    public CommonResult deleteProductCollect(@RequestParam("productId") Long productId, @RequestHeader("Authorization") String token) {
        Long userId = userService.getUserInfo(jwtUtil.getUsernameFromToken(token)).getId();
        try {
            int result = productCollectService.deleteProductFavorite(userId, productId);
            if (result > 0) {
                return CommonResult.success(null);
            } else {
                return CommonResult.failed("未找到收藏记录");
            }
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }

    /**
     * 获取用户收藏列表
     */
    @RequestMapping("/list")
    public CommonResult<ProductCollectionPageResponse> getProductCollectList(
        @RequestParam(value = "pageNum", required = false, defaultValue = "1") Long pageNum,
        @RequestParam(value = "pageSize", required = false, defaultValue = "10") Long pageSize,
        @RequestHeader("Authorization") String token) {
        
        try {
            Long userId = userService.getUserInfo(jwtUtil.getUsernameFromToken(token)).getId();
            Page<UmsUserFavorites> page = productCollectService.getProductFavorites(userId, pageNum, pageSize);
            
            ProductCollectionPageResponse response = new ProductCollectionPageResponse();
            List<ProductCollectionResponse> list = new ArrayList<>();
            
            for (UmsUserFavorites userFavorites : page.getRecords()) {
                ProductCollectionResponse productCollectionResponse = new ProductCollectionResponse();
                productCollectionResponse.setProductId(userFavorites.getProductId());
                productCollectionResponse.setProductName(userFavorites.getProductName());
                productCollectionResponse.setProductPic(userFavorites.getProductImage());
                productCollectionResponse.setProductPrice(userFavorites.getProductPrice());
                productCollectionResponse.setProductSubTitle(userFavorites.getNote());
                productCollectionResponse.setCreateTime(userFavorites.getCreateTime());

                list.add(productCollectionResponse);
            }
            
            response.setList(list);
            response.setPage(page.getCurrent());
            response.setPageSize(page.getSize());
            response.setTotal(page.getTotal());
            response.setTotalPages(page.getPages());
            
            return CommonResult.success(response);
        } catch (Exception e) {
            return CommonResult.failed("获取收藏列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取商品收藏详情
     */
    @RequestMapping("/detail")
    public CommonResult<ProductCollectionResponse> getProductCollectDetail(@RequestParam("productId") Long productId,
                                                                           @RequestHeader("Authorization") String token) {
        Long userId = userService.getUserInfo(jwtUtil.getUsernameFromToken(token)).getId();
        try {
            UmsUserFavorites userFavorites = productCollectService.getProductFavoriteDetail(productId, userId);
            if (userFavorites == null) {
                return CommonResult.success(null);
            } else {
                ProductCollectionResponse response = new ProductCollectionResponse();
                response.setProductId(userFavorites.getProductId());
                response.setProductName(userFavorites.getProductName());
                response.setProductPic(userFavorites.getProductImage());
                response.setProductPrice(userFavorites.getProductPrice());
                response.setProductSubTitle(userFavorites.getNote());
                response.setCreateTime(userFavorites.getCreateTime());
                return CommonResult.success(response);
            }
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }

    /**
     * 清空用户商品收藏
     */
    @RequestMapping("/clear")
    public CommonResult clearProductCollect(@RequestHeader("Authorization") String token) {
        int result = productCollectService.clearProductFavorites(userService.getUserInfo(jwtUtil.getUsernameFromToken(token)).getId());

        if (result > 0) {
            return CommonResult.success(null);
        } else {
            return CommonResult.failed();
        }
    }
}
