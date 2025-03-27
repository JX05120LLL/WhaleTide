package com.whale_tide.service.management.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.whale_tide.dto.management.cart.*;
import com.whale_tide.entity.oms.OmsCartItems;
import com.whale_tide.mapper.oms.OmsCartItemsMapper;
import com.whale_tide.service.management.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 购物车实现类
 */
@Service
public class CartServiceImpl implements ICartService {
    @Autowired
    private OmsCartItemsMapper omsCartItemsMapper;

    @Override
    public long cartAdd(CartAddRequest request) {
        // 创建购物车项
        OmsCartItems cartItem = new OmsCartItems();

        // 设置必要的属性
        cartItem.setUserId(1L);
        cartItem.setProductId(request.getProductId());
        cartItem.setQuantity(request.getQuantity());
        cartItem.setSkuId(request.getProductSkuId());

        // 其他字段设置（如价格、商品名称、时间戳等）
        cartItem.setCreateTime(LocalDateTime.now());
        cartItem.setUpdateTime(LocalDateTime.now());

        // 插入数据到数据库
        // 这里省略了具体的插入操作，假设插入成功，返回插入的id
        return omsCartItemsMapper.insert(cartItem);
    }

    @Override
    public List<CartListResponse> getCartList() {

        return null;
    }

    @Override
    public int cartUpdateQuantity(CartUpdateQuantityRequest request) {
      //解析请求参数
        Long id = request.getId();
        Integer quantity = request.getQuantity();

        // 根据id查询购物车项
        OmsCartItems cartItem = omsCartItemsMapper.selectById(id);

        // 更新购物车项的数量
        cartItem.setQuantity(quantity);

        // 更新数据库
        return omsCartItemsMapper.updateById(cartItem);

    }

    @Override
    public int cartUpdateChecked(CartUpdateCheckedRequest request) {
        //解析请求参数
        String ids = request.getIds();
        Integer checked = request.getChecked();

        //解析参数
        List<Long> idList = Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());

        // 创建更新包装器
        UpdateWrapper<OmsCartItems> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", idList);
        // 执行更新
        return omsCartItemsMapper.update(null, updateWrapper);   // 返回受影响的行数
    }

    @Override
    public int cartDelete(CartDeleteRequest request) {
        //解析请求参数
        String ids = request.getIds();

        //解析参数
        List<Long> idList = Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());

        // 创建更新包装器
        UpdateWrapper<OmsCartItems> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", idList);
        // 执行更新
        return omsCartItemsMapper.delete(updateWrapper);   // 返回受影响的行数
    }

    @Override
    public int cartClear() {
        // 创建更新包装器
        UpdateWrapper<OmsCartItems> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", 1L);
        // 执行更新
        return omsCartItemsMapper.delete(updateWrapper);   // 返回受影响的行数
    }

    @Override
    public int getCartCount() {
        return 0;
    }


}
