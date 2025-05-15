package com.whaletide.client.cart.service;

import com.whaletide.client.cart.dto.CartAddDTO;
import com.whaletide.client.cart.dto.CartUpdateDTO;
import com.whaletide.client.cart.vo.CartItemVO;
import com.whaletide.common.api.CommonResult;

import java.util.List;

/**
 * 购物车服务接口
 */
public interface ICartService {

    /**
     * 添加商品到购物车
     * @param cartAddDTO 购物车商品信息
     * @return 操作结果
     */
    Boolean add(CartAddDTO cartAddDTO);

    /**
     * 获取购物车列表
     * @return 购物车商品列表
     */
    List<CartItemVO> list();

    /**
     * 修改购物车商品数量
     * @param cartUpdateDTO 更新信息
     * @return 操作结果
     */
    Boolean updateQuantity(CartUpdateDTO cartUpdateDTO);

    /**
     * 修改购物车商品选中状态
     * @param cartUpdateDTO 更新信息
     * @return 操作结果
     */
    Boolean updateChecked(CartUpdateDTO cartUpdateDTO);

    /**
     * 删除购物车商品
     * @param ids 购物车商品ID列表
     * @return 操作结果
     */
    Boolean delete(List<Long> ids);

    /**
     * 清空购物车
     * @return 操作结果
     */
    Boolean clear();

    /**
     * 获取购物车商品数量
     * @return 商品数量
     */
    Integer count();
} 