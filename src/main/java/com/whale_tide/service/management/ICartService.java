package com.whale_tide.service.management;

import com.whale_tide.dto.management.cart.*;

import java.util.List;

/**
 * 购物车接口
 */

public interface ICartService {
    /**
     * 添加商品到购物车
     * @param request
     * @return
     */
    long cartAdd(CartAddRequest request);

    /**
     * 获取购物车列表
     * @return
     */
    List<CartListResponse> getCartList();

    /**
     * 修改购物车中商品数量
     * @param request
     * @return
     */
    int cartUpdateQuantity(CartUpdateQuantityRequest request);
    /**
     * 修改购物车中商品选中状态
     * @param request
     * @return
     */
    int cartUpdateChecked(CartUpdateCheckedRequest request);
    /**
     * 删除购物车中的商品
     * @param request
     * @return
     */
    int cartDelete(CartDeleteRequest request);
    /**
     * 清空购物车
     * @return
     */
    int cartClear();
    /**
     * 获取购物车中的商品数量
     * @return
     */
    int getCartCount();
}
