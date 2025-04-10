package com.whale_tide.service.client.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale_tide.common.api.PageRequest;
import com.whale_tide.common.api.PageResponse;
import com.whale_tide.common.exception.auth.CouponNotBelongToUserException;
import com.whale_tide.dto.client.history.ReadHistoryCreateRequest;
import com.whale_tide.dto.client.history.ReadHistoryDeleteRequest;
import com.whale_tide.dto.client.history.ReadHistoryResponse;
import com.whale_tide.entity.ums.UmsUserBrowseHistory;
import com.whale_tide.entity.ums.UmsUsers;
import com.whale_tide.mapper.ums.UmsUserBrowseHistoryMapper;
import com.whale_tide.mapper.ums.UmsUsersMapper;
import com.whale_tide.service.client.IReadHistoryService;
import com.whale_tide.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 搜索历史服务实现类
 */
@Slf4j
@Service
public class ReadHistoryServiceImpl implements IReadHistoryService {

    @Autowired
    private UmsUserBrowseHistoryMapper browseHistoryMapper;

    @Autowired
    private UmsUsersMapper umsUsersMapper;

    @Autowired
    private JwtUtil jwtUtil;

    // 获取浏览历史列表
    @Override
    public PageResponse<ReadHistoryResponse> getList(PageRequest request) {
        // 从PageRequest中获取页码和每页记录数
        Long pageNum = request.getPageNum();
        Long pageSize = request.getPageSize();
        // 设置默认值，防止空指针异常
        if (pageNum == null) pageNum = 1L;
        if (pageSize == null) pageSize = 10L;

        Long userId = getCurrentUserId();

        // 创建查询条件
        LambdaQueryWrapper<UmsUserBrowseHistory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UmsUserBrowseHistory::getUserId, userId);
        queryWrapper.orderByDesc(UmsUserBrowseHistory::getLastBrowseTime);

        // 分页查询
        Page<UmsUserBrowseHistory> page = new Page<>(pageNum, pageSize);
        Page<UmsUserBrowseHistory> resultPage = browseHistoryMapper.selectPage(page, queryWrapper);

        // 转换为DTO列表
        List<ReadHistoryResponse> list = resultPage.getRecords().stream().map(item -> {
            ReadHistoryResponse response = new ReadHistoryResponse();
            response.setId(item.getId());
            response.setProductId(item.getProductId());
            response.setProductName(item.getProductName());
            response.setBrowseCount(item.getBrowseCount());
            response.setLastBrowseTime(convertToDate(item.getLastBrowseTime()));
            response.setCreateTime(convertToDate(item.getCreateTime()));
            return response;
        }).collect(Collectors.toList());

        // 构建分页响应，安全地将Long转换为Integer
        return PageResponse.of(
                list,
                pageNum.intValue(), // 安全地将Long转换为int
                pageSize.intValue(), // 安全地将Long转换为int
                resultPage.getTotal(),
                (int) resultPage.getPages()
        );
    }

    // 创建浏览历史
    @Override
    public void create(ReadHistoryCreateRequest request) {
        Long userId = getCurrentUserId();
        Long productId = request.getProductId();
        String productName = request.getProductName();

        if (productId == null) {
            log.warn("商品为空，无法创建搜索历史");
            return;
        }


        // 检查是否已经有相同关键词的记录
        LambdaQueryWrapper<UmsUserBrowseHistory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UmsUserBrowseHistory::getUserId, userId);
        queryWrapper.eq(UmsUserBrowseHistory::getProductId, productId);
        UmsUserBrowseHistory existHistory = browseHistoryMapper.selectOne(queryWrapper);

        LocalDateTime now = LocalDateTime.now();

        if (existHistory != null) {
            // 如果已存在，更新搜索次数和最后搜索时间
            existHistory.setBrowseCount(existHistory.getBrowseCount() + 1);
            existHistory.setLastBrowseTime(now);
            browseHistoryMapper.updateById(existHistory);
        } else {
            // 如果不存在，则创建新的记录
            UmsUserBrowseHistory browseHistory = new UmsUserBrowseHistory();
            browseHistory.setUserId(userId);
            browseHistory.setProductId(productId);
            browseHistory.setProductName(productName);
            browseHistory.setBrowseCount(1);
            browseHistory.setLastBrowseTime(now);
            browseHistory.setCreateTime(now);
            browseHistoryMapper.insert(browseHistory);
        }
    }

    // 删除搜索历史
    @Override
    public void delete(ReadHistoryDeleteRequest request) {
        Long userId = getCurrentUserId();

        // 验证待删除记录是否都属于当前用户
        LambdaQueryWrapper<UmsUserBrowseHistory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UmsUserBrowseHistory::getUserId, userId);
        queryWrapper.in(UmsUserBrowseHistory::getId, request.getIds());
        List<UmsUserBrowseHistory> historyList = browseHistoryMapper.selectList(queryWrapper);

        if (historyList.size() != request.getIds().size()) {
            throw new CouponNotBelongToUserException("部分记录不属于当前用户");
        }

        // 删除记录
        browseHistoryMapper.deleteBatchIds(request.getIds());
    }

    // 清空搜索历史
    @Override
    public void clear() {
        Long userId = getCurrentUserId();

        // 删除当前用户的所有记录
        LambdaQueryWrapper<UmsUserBrowseHistory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UmsUserBrowseHistory::getUserId, userId);
        browseHistoryMapper.delete(queryWrapper);
    }

    /**
     * 将LocalDateTime转换为Date
     */
    private Date convertToDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return java.sql.Timestamp.valueOf(dateTime);
    }

    /**
     * 获取当前登录用户ID
     */
    private Long getCurrentUserId() {
        // 从请求中获取当前用户ID
        try {
            // 获取当前请求
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                // 从请求头中获取token
                String token = request.getHeader("Authorization");
                if (token != null) {
                    // 使用JwtUtil解析token获取用户名
                    String username = jwtUtil.getUsernameFromToken(token);

                    // 根据用户名查询用户信息
                    LambdaQueryWrapper<UmsUsers> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(UmsUsers::getUsername, username);
                    UmsUsers user = umsUsersMapper.selectOne(queryWrapper);

                    if (user != null) {
                        return user.getId();
                    }
                }
            }

            // 如果获取失败，抛出异常或返回默认值
            log.warn("无法获取当前用户ID，请检查用户是否已登录");
            throw new RuntimeException("用户未登录");
        } catch (Exception e) {
            log.error("获取当前用户ID失败", e);
            throw new RuntimeException("获取用户信息失败", e);
        }
    }
} 