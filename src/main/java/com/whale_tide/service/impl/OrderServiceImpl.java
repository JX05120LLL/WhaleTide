package com.whale_tide.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whale_tide.dto.CloseOrderParam;
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
import java.util.Arrays;
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
    public int closeOrder(CloseOrderParam closeOrderParam) {
        // 1. 解析订单ID字符串，转换为List<Long>
        String ids = closeOrderParam.getIds();
        String note = closeOrderParam.getNote();

        if (StringUtils.isEmpty(ids)) {
            return 0;
        }

        // 将逗号分隔的id字符串转为Long类型的列表
        List<Long> orderIds = Arrays.stream(ids.split(","))
                .map(id -> Long.parseLong(id.trim()))
                .collect(Collectors.toList());

        if (orderIds.isEmpty()) {
            return 0;
        }

        // 2. 更新订单状态为已关闭(状态4)
        LambdaUpdateWrapper<OmsOrders> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(OmsOrders::getId, orderIds)
                // 只能关闭待付款的订单(状态0)
                .eq(OmsOrders::getStatus, 0)
                .set(OmsOrders::getStatus, 4)
                .set(StringUtils.hasText(note), OmsOrders::getOrderNote, note);

        int count = ordersMapper.update(null, updateWrapper);

        // 3. 记录订单操作日志
        if (count > 0) {
            // 如果有日志表，这里可以添加日志记录
            // saveOrderOperationLog(orderIds, 4, note);
        }

        return count;
    }
} 