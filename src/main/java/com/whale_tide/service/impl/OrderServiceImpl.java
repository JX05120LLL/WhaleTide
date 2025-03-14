package com.whale_tide.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whale_tide.dto.OrderQueryParam;
import com.whale_tide.dto.OrderResult;
import com.whale_tide.entity.OmsOrders;
import com.whale_tide.mapper.OmsOrdersMapper;
import com.whale_tide.service.IOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单管理服务实现类
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OmsOrdersMapper, OmsOrders> implements IOrderService {

    @Autowired
    private OmsOrdersMapper ordersMapper;

    @Override
    public IPage<OrderResult> getOrderList(OrderQueryParam queryParam) {
        // 构建查询条件
        LambdaQueryWrapper<OmsOrders> queryWrapper = new LambdaQueryWrapper<>();
        
        // 订单编号查询
        if (queryParam.getOrderSn() != null && !queryParam.getOrderSn().trim().isEmpty()) {
            queryWrapper.like(OmsOrders::getOrderSn, queryParam.getOrderSn());
        }
        
        // 订单状态查询
        if (queryParam.getStatus() != null) {
            queryWrapper.eq(OmsOrders::getStatus, queryParam.getStatus());
        }
        
        // 订单类型查询
        if (queryParam.getOrderType() != null) {
            queryWrapper.eq(OmsOrders::getOrderType, queryParam.getOrderType());
        }
        
        // 订单来源查询
        if (queryParam.getSourceType() != null) {
            queryWrapper.eq(OmsOrders::getSourceType, queryParam.getSourceType());
        }
        
        // 创建时间查询
        if (StringUtils.hasText(queryParam.getCreateTime())) {
            queryWrapper.like(OmsOrders::getCreateTime, queryParam.getCreateTime());
        }
        
        // 设置分页参数
        Integer pageNum = queryParam.getPageNum() != null ? queryParam.getPageNum() : 1;
        Integer pageSize = queryParam.getPageSize() != null ? queryParam.getPageSize() : 10;
        Page<OmsOrders> page = new Page<>(pageNum, pageSize);
        
        // 执行查询
        Page<OmsOrders> orderPage = ordersMapper.selectPage(page, queryWrapper);
        
        // 转换为OrderResult
        List<OrderResult> resultList = orderPage.getRecords().stream().map(order -> {
            OrderResult result = new OrderResult();
            BeanUtils.copyProperties(order, result);
            return result;
        }).collect(Collectors.toList());
        
        // 封装结果
        Page<OrderResult> resultPage = new Page<>(orderPage.getCurrent(), orderPage.getSize(), orderPage.getTotal());
        resultPage.setRecords(resultList);
        
        return resultPage;
    }

    @Override
    public void closeOrder() {
        // 关闭订单实现
    }
} 