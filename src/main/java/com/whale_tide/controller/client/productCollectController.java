package com.whale_tide.controller.client;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale_tide.common.api.CommonResult;
import com.whale_tide.dto.client.member.ProductCollectionPageResponse;
import com.whale_tide.dto.client.member.ProductCollectionRequest;
import com.whale_tide.dto.client.member.ProductCollectionResponse;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Api(tags = "用户产品收藏")
@RestController("clientProductCollectController")
@RequestMapping("/member/productCollect")
public class productCollectController {

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
            return CommonResult.failed();
        }
    }

    /**
     * 删除商品收藏
     */
    @RequestMapping("/delete")
    public CommonResult deleteProductCollect(@RequestBody Long productId, @RequestHeader("Authorization") String token) {
        int result = productCollectService.deleteProductFavorite(productId, userService.getUserInfo(jwtUtil.getUsernameFromToken(token)).getId());

        if (result > 0) {
            return CommonResult.success(null);
        } else {
            return CommonResult.failed();
        }
    }

    /**
     * 获取用户收藏列表
     */
    @RequestMapping("/list")
    public CommonResult<ProductCollectionPageResponse> getProductCollectList(@RequestBody(required = false) Long pageNum,
                                                                             @RequestBody(required = false) Long pageSize,
                                                                             @RequestHeader("Authorization") String token) {
        Page<UmsUserFavorites> page = productCollectService.getProductFavorites(userService.getUserInfo(jwtUtil.getUsernameFromToken(token)).getId(), pageNum, pageSize);
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
    }

    /**
     * 获取商品收藏详情
     */
    @RequestMapping("/detail")
    public CommonResult<ProductCollectionResponse> getProductCollectDetail(@RequestBody Long productId,
                                                                           @RequestHeader("Authorization") String token) {
        UmsUserFavorites userFavorites = productCollectService.getProductFavoriteDetail(productId, userService.getUserInfo(jwtUtil.getUsernameFromToken(token)).getId());
        if (userFavorites == null) {
            return CommonResult.failed();
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
