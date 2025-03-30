package com.whale_tide.controller.client;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale_tide.common.api.CommonResult;
import com.whale_tide.dto.client.member.*;
import com.whale_tide.entity.ums.UmsUserBrandAttentions;
import com.whale_tide.entity.ums.UmsUserFavorites;
import com.whale_tide.service.client.IMemberService;
import com.whale_tide.service.client.IUserService;
import com.whale_tide.util.JwtUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Api(tags = "Member")
@RestController("clientMemberController")
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private IMemberService memberService;

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
    @RequestMapping("/productCollect/add")
    public CommonResult addProductCollect(@RequestBody ProductCollectionRequest productCollectionRequest, @RequestHeader("Authorization") String token) {
        UmsUserFavorites userFavorites = new UmsUserFavorites();
        userFavorites.setUserId(userService.getUserInfo(jwtUtil.getUsernameFromToken(token)).getId());
        userFavorites.setProductId(productCollectionRequest.getProductId());
        userFavorites.setProductName(productCollectionRequest.getProductName());
        userFavorites.setProductImage(productCollectionRequest.getProductPic());
        userFavorites.setProductPrice(productCollectionRequest.getProductPrice());
        userFavorites.setNote(productCollectionRequest.getProductSubTitle());
        int result = memberService.addProductFavorite(userFavorites, userFavorites.getUserId());

        if (result == 1) {
            return CommonResult.success(null);
        } else {
            return CommonResult.failed();
        }
    }

    /**
     * 删除商品收藏
     */
    @RequestMapping("/productCollect/delete")
    public CommonResult deleteProductCollect(@RequestBody Long productId, @RequestHeader("Authorization") String token) {
        int result = memberService.deleteProductFavorite(productId, userService.getUserInfo(jwtUtil.getUsernameFromToken(token)).getId());

        if (result > 0) {
            return CommonResult.success(null);
        } else {
            return CommonResult.failed();
        }
    }

    /**
     * 获取用户收藏列表
     */
    @RequestMapping("/productCollect/list")
    public CommonResult<ProductCollectionPageResponse> getProductCollectList(@RequestBody(required = false) Long pageNum,
                                                                             @RequestBody(required = false) Long pageSize,
                                                                             @RequestHeader("Authorization") String token) {
        Page<UmsUserFavorites> page = memberService.getProductFavorites(userService.getUserInfo(jwtUtil.getUsernameFromToken(token)).getId(), pageNum, pageSize);
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
    @RequestMapping("/productCollect/detail")
    public CommonResult<ProductCollectionResponse> getProductCollectDetail(@RequestBody Long productId,
                                                                           @RequestHeader("Authorization") String token) {
        UmsUserFavorites userFavorites = memberService.getProductFavoriteDetail(productId, userService.getUserInfo(jwtUtil.getUsernameFromToken(token)).getId());
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
    @RequestMapping("/productCollect/clear")
    public CommonResult clearProductCollect(@RequestHeader("Authorization") String token) {
        int result = memberService.clearProductFavorites(userService.getUserInfo(jwtUtil.getUsernameFromToken(token)).getId());

        if (result > 0) {
            return CommonResult.success(null);
        } else {
            return CommonResult.failed();
        }
    }

    /**
     * 关注品牌
     */
    @RequestMapping("/brand/attention/add")
    public CommonResult addBrandAttention(@RequestBody BrandAttentionRequest request, @RequestHeader("Authorization") String token) {
        UmsUserBrandAttentions userBrandAttentions = new UmsUserBrandAttentions();
        userBrandAttentions.setUserId(userService.getUserInfo(jwtUtil.getUsernameFromToken(token)).getId());
        userBrandAttentions.setBrandId(request.getBrandId());
        userBrandAttentions.setBrandName(request.getBrandName());
        userBrandAttentions.setBrandLogo(request.getBrandLogo());
        userBrandAttentions.setNote(request.getBrandDesc());
        userBrandAttentions.setCreateTime(LocalDateTime.now());
        userBrandAttentions.setUpdateTime(LocalDateTime.now());

        int result = memberService.addBrandAttention(userBrandAttentions);

        if (result == 1) {
            return CommonResult.success(null);
        } else if (result == -1) {
            return CommonResult.failed("参数错误");
        } else {
            return CommonResult.failed();
        }
    }

    /**
     * 取消关注品牌
     */
    @RequestMapping("/brand/attention/delete")
    public CommonResult deleteBrandAttention(@RequestBody Long brandId, @RequestHeader("Authorization") String token) {
        int result = memberService.deleteBrandAttention(userService.getUserInfo(jwtUtil.getUsernameFromToken(token)).getId(), brandId);

        if (result > 0) {
            return CommonResult.success(null);
        } else if (result == -1) {
            return CommonResult.failed("参数错误");
        } else {
            return CommonResult.failed();
        }
    }


    /**
     * 关注品牌列表
     */
    @RequestMapping("/brand/attention/list")
    public CommonResult<BrandAttentionPageResponse> getBrandAttentionList(@RequestBody Long pageNum,
                                                                          @RequestBody Long pageSize,
                                                                          @RequestHeader("Authorization") String token) {

        Page<UmsUserBrandAttentions> userBrandAttentionsList =
                memberService.getBrandAttentionList(userService.getUserInfo(jwtUtil.getUsernameFromToken(token)).getId(), pageNum, pageSize);

        if (userBrandAttentionsList == null) {
            return CommonResult.failed();
        }

        List<BrandAttentionResponse> list = new ArrayList<>();
        for (UmsUserBrandAttentions userBrandAttentions : userBrandAttentionsList.getRecords()) {
            BrandAttentionResponse brandAttentionResponse = getBrandAttentionResponse(userBrandAttentions);
            list.add(brandAttentionResponse);
        }

        BrandAttentionPageResponse response = new BrandAttentionPageResponse();
        response.setList(list);
        return CommonResult.success(response);
    }

    /**
     * 获取品牌详情
     */
    @RequestMapping("/brand/attention/detail")
    public CommonResult<BrandAttentionResponse> getBrandAttentionDetail(@RequestBody Long brandId,
                                                                        @RequestHeader("Authorization") String token) {
        UmsUserBrandAttentions userBrandAttentions = memberService.getBrandAttentionDetail(brandId, userService.getUserInfo(jwtUtil.getUsernameFromToken(token)).getId());
        if (userBrandAttentions == null) {
            return CommonResult.failed();
        } else {
            BrandAttentionResponse response = getBrandAttentionResponse(userBrandAttentions);
            return CommonResult.success(response);
        }
    }

    /**
     * 清空用户关注品牌
     */
    @RequestMapping("/brand/attention/clear")
    public CommonResult clearBrandAttention(@RequestHeader("Authorization") String token) {
        int result = memberService.clearBrandAttention(userService.getUserInfo(jwtUtil.getUsernameFromToken(token)).getId());

        if (result > 0) {
            return CommonResult.success(null);
        } else if (result == 0) {
            return CommonResult.failed("没有关注的品牌");
        } else if (result == -1) {
            return CommonResult.failed("参数错误");
        } else {
            return CommonResult.failed();
        }
    }


    /**
     * 品牌关注响应转换
     *
     * @param userBrandAttentions 用户关注品牌信息
     * @return 品牌关注响应体
     */
    private static BrandAttentionResponse getBrandAttentionResponse(UmsUserBrandAttentions userBrandAttentions) {
        BrandAttentionResponse brandAttentionResponse = new BrandAttentionResponse();
        brandAttentionResponse.setId(userBrandAttentions.getId());
        brandAttentionResponse.setBrandId(userBrandAttentions.getBrandId());
        brandAttentionResponse.setBrandName(userBrandAttentions.getBrandName());
        brandAttentionResponse.setBrandLogo(userBrandAttentions.getBrandLogo());
        brandAttentionResponse.setBrandDesc(userBrandAttentions.getNote());
        brandAttentionResponse.setCreatedTime(userBrandAttentions.getCreateTime());
        return brandAttentionResponse;
    }

}
