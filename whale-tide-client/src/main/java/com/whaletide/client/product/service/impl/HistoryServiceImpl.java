package com.whaletide.client.product.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whaletide.client.product.service.IHistoryService;
import com.whaletide.client.product.service.IProductService;
import com.whaletide.client.product.vo.ProductDetailVO;
import com.whaletide.client.product.vo.ProductHistoryVO;
import com.whaletide.client.user.service.IUserService;
import com.whaletide.client.user.vo.UserVO;
import com.whaletide.common.api.CommonPage;
import com.whaletide.common.exception.NotFoundException;
import com.whaletide.common.exception.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 浏览历史服务实现
 * 使用Redis存储浏览历史
 */
@Service
public class HistoryServiceImpl implements IHistoryService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(HistoryServiceImpl.class);
    private static final String HISTORY_KEY_PREFIX = "user:history:";
    private static final int HISTORY_MAX_SIZE = 100; // 最多保存100条浏览记录
    private static final long HISTORY_EXPIRE_DAYS = 30; // 历史记录保存30天
    
    private static final String SEARCH_HISTORY_KEY_PREFIX = "user:search_history:";
    private static final int SEARCH_HISTORY_MAX_SIZE = 10; // 最多保存10条搜索历史
    
    private final IUserService userService;
    private final IProductService productService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisTemplate<String, String> historyRedisTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public HistoryServiceImpl(IUserService userService, 
                             IProductService productService, 
                             RedisTemplate<String, Object> redisTemplate,
                             @Qualifier("historyRedisTemplate") RedisTemplate<String, String> historyRedisTemplate) {
        this.userService = userService;
        this.productService = productService;
        this.redisTemplate = redisTemplate;
        this.historyRedisTemplate = historyRedisTemplate;
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * 获取浏览历史列表
     *
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return 浏览历史分页列表
     */
    @Override
    public CommonPage<ProductHistoryVO> list(Integer pageNum, Integer pageSize) {
        // 获取当前登录用户
        UserVO currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new UnauthorizedException("用户未登录");
        }
        
        String redisKey = getHistoryRedisKey(currentUser.getId());
        
        try {
            // 从Redis中获取浏览历史 - 使用historyRedisTemplate
            Set<ZSetOperations.TypedTuple<String>> typedTuples = 
                historyRedisTemplate.opsForZSet().reverseRangeWithScores(redisKey, 0, -1);
            
            if (typedTuples == null || typedTuples.isEmpty()) {
                return new CommonPage<>();
            }
            
            List<ProductHistoryVO> historyList = new ArrayList<>();
            
            for (ZSetOperations.TypedTuple<String> tuple : typedTuples) {
                String historyJson = tuple.getValue();
                if (historyJson != null) {
                    try {
                        ProductHistoryVO history = objectMapper.readValue(historyJson, ProductHistoryVO.class);
                        history.setViewTime(new Date((long) tuple.getScore().doubleValue()));
                        historyList.add(history);
                    } catch (JsonProcessingException e) {
                        LOGGER.error("解析浏览历史JSON失败: {}", e.getMessage(), e);
                    }
                }
            }
            
            // 分页处理
            int startIndex = (pageNum - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, historyList.size());
            
            List<ProductHistoryVO> pageList;
            if (startIndex < historyList.size()) {
                pageList = historyList.subList(startIndex, endIndex);
            } else {
                pageList = new ArrayList<>();
            }
            
            // 创建分页对象
            CommonPage<ProductHistoryVO> result = new CommonPage<>();
            result.setList(pageList);
            result.setPageNum(pageNum);
            result.setPageSize(pageSize);
            result.setTotal((long)historyList.size());
            result.setTotalPage((int) Math.ceil(historyList.size() / (double) pageSize));
            
            return result;
        } catch (Exception e) {
            LOGGER.error("获取浏览历史失败: {}", e.getMessage(), e);
            return new CommonPage<>();
        }
    }
    
    /**
     * 清空浏览历史
     *
     * @return 是否成功
     */
    @Override
    public Boolean clear() {
        // 获取当前登录用户
        UserVO currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new UnauthorizedException("用户未登录");
        }
        
        String redisKey = getHistoryRedisKey(currentUser.getId());
        
        try {
            // 删除Redis中的浏览历史记录 - 使用historyRedisTemplate
            historyRedisTemplate.delete(redisKey);
            LOGGER.info("用户:{} 清空浏览历史成功", currentUser.getId());
            return true;
        } catch (Exception e) {
            LOGGER.error("清空浏览历史失败: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 删除指定浏览历史
     *
     * @param ids 历史记录ID集合，多个ID用逗号分隔
     * @return 是否成功
     */
    @Override
    public Boolean delete(String ids) {
        // 获取当前登录用户
        UserVO currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new UnauthorizedException("用户未登录");
        }
        
        if (ids == null || ids.isEmpty()) {
            return true;
        }
        
        // 解析ID集合
        String[] idArray = ids.split(",");
        Set<String> idSet = new HashSet<>(Arrays.asList(idArray));
        
        String redisKey = getHistoryRedisKey(currentUser.getId());
        
        try {
            // 从Redis中获取所有浏览历史 - 使用historyRedisTemplate
            Set<ZSetOperations.TypedTuple<String>> typedTuples = 
                historyRedisTemplate.opsForZSet().rangeWithScores(redisKey, 0, -1);
            
            if (typedTuples == null || typedTuples.isEmpty()) {
                return true;
            }
            
            // 找出要删除的记录
            for (ZSetOperations.TypedTuple<String> tuple : typedTuples) {
                String historyJson = tuple.getValue();
                if (historyJson != null) {
                    try {
                        ProductHistoryVO history = objectMapper.readValue(historyJson, ProductHistoryVO.class);
                        if (idSet.contains(history.getId())) {
                            // 从Redis中删除记录 - 使用historyRedisTemplate
                            historyRedisTemplate.opsForZSet().remove(redisKey, historyJson);
                        }
                    } catch (JsonProcessingException e) {
                        LOGGER.error("解析浏览历史JSON失败: {}", e.getMessage(), e);
                    }
                }
            }
            
            LOGGER.info("用户:{} 删除浏览历史:{} 成功", currentUser.getId(), ids);
            return true;
        } catch (Exception e) {
            LOGGER.error("删除浏览历史失败: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 添加浏览历史记录
     *
     * @param productId 商品ID
     * @return 是否成功
     */
    @Override
    public Boolean add(Long productId) {
        // 获取当前登录用户
        UserVO currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            LOGGER.info("用户未登录，不记录浏览历史");
            return false;
        }
        
        // 检查商品是否存在
        try {
            // 获取商品详情
            ProductDetailVO product = productService.getProductDetail(productId);
            if (product == null) {
                LOGGER.error("添加浏览历史失败，商品不存在: {}", productId);
                throw new NotFoundException("商品不存在");
            }
            
            String redisKey = getHistoryRedisKey(currentUser.getId());
            
            // 创建浏览历史记录
            ProductHistoryVO history = new ProductHistoryVO();
            history.setId(UUID.randomUUID().toString());
            history.setProductId(productId);
            history.setProductName(product.getName());
            history.setProductPic(product.getPic());
            history.setProductPrice(new BigDecimal(String.valueOf(product.getPrice())));
            // 视图时间在保存时使用score值
            history.setViewTime(new Date());
            
            // 先移除已有的相同商品浏览记录 - 使用historyRedisTemplate
            Set<ZSetOperations.TypedTuple<String>> existingRecords = 
                historyRedisTemplate.opsForZSet().rangeWithScores(redisKey, 0, -1);
            
            if (existingRecords != null) {
                for (ZSetOperations.TypedTuple<String> tuple : existingRecords) {
                    String historyJson = tuple.getValue();
                    try {
                        ProductHistoryVO existingHistory = objectMapper.readValue(historyJson, ProductHistoryVO.class);
                        if (existingHistory.getProductId().equals(productId)) {
                            historyRedisTemplate.opsForZSet().remove(redisKey, historyJson);
                        }
                    } catch (Exception e) {
                        LOGGER.error("解析历史记录失败: {}", e.getMessage());
                    }
                }
            }
            
            // 将历史记录转换为JSON
            String historyJson = objectMapper.writeValueAsString(history);
            
            // 添加新记录到Redis中，使用时间戳作为得分(score) - 使用historyRedisTemplate
            double score = System.currentTimeMillis();
            historyRedisTemplate.opsForZSet().add(redisKey, historyJson, score);
            
            // 检查是否超过最大数量限制
            Long size = historyRedisTemplate.opsForZSet().size(redisKey);
            if (size != null && size > HISTORY_MAX_SIZE) {
                // 移除最早的记录，保留最近的HISTORY_MAX_SIZE条
                historyRedisTemplate.opsForZSet().removeRange(redisKey, 0, size - HISTORY_MAX_SIZE - 1);
            }
            
            // 设置过期时间
            historyRedisTemplate.expire(redisKey, HISTORY_EXPIRE_DAYS, TimeUnit.DAYS);
            
            LOGGER.info("用户:{} 添加浏览历史:{} 成功", currentUser.getId(), productId);
            return true;
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("添加浏览历史失败: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 获取用户浏览历史的Redis键
     *
     * @param userId 用户ID
     * @return Redis键
     */
    private String getHistoryRedisKey(Long userId) {
        return HISTORY_KEY_PREFIX + userId;
    }
    
    /**
     * 添加搜索历史记录
     *
     * @param keyword 搜索关键词
     * @return 是否成功
     */
    @Override
    public Boolean addSearchHistory(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return false;
        }
        
        UserVO currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            LOGGER.debug("用户未登录，不记录搜索历史");
            return false;
        }
        
        String redisKey = getSearchHistoryRedisKey(currentUser.getId());
        try {
            // 检查是否已存在相同的搜索词，如果有则先删除 - 使用historyRedisTemplate
            Double score = historyRedisTemplate.opsForZSet().score(redisKey, keyword);
            if (score != null) {
                historyRedisTemplate.opsForZSet().remove(redisKey, keyword);
            }
            
            // 添加新的搜索词，使用时间戳作为分数
            double currentTime = System.currentTimeMillis();
            historyRedisTemplate.opsForZSet().add(redisKey, keyword, currentTime);
            
            // 检查是否超过数量限制
            Long size = historyRedisTemplate.opsForZSet().size(redisKey);
            if (size != null && size > SEARCH_HISTORY_MAX_SIZE) {
                // 移除最早的搜索记录
                historyRedisTemplate.opsForZSet().removeRange(redisKey, 0, size - SEARCH_HISTORY_MAX_SIZE - 1);
            }
            
            // 设置过期时间
            historyRedisTemplate.expire(redisKey, HISTORY_EXPIRE_DAYS, TimeUnit.DAYS);
            
            LOGGER.debug("用户:{} 添加搜索历史:{} 成功", currentUser.getId(), keyword);
            return true;
        } catch (Exception e) {
            LOGGER.error("添加搜索历史失败: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 获取用户搜索历史列表
     *
     * @param limit 限制数量
     * @return 搜索历史列表
     */
    @Override
    public List<String> getSearchHistory(Integer limit) {
        UserVO currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return new ArrayList<>();
        }
        
        if (limit == null || limit <= 0) {
            limit = SEARCH_HISTORY_MAX_SIZE;
        }
        
        String redisKey = getSearchHistoryRedisKey(currentUser.getId());
        try {
            // 获取最近的搜索记录 - 使用historyRedisTemplate
            Set<String> keywords = historyRedisTemplate.opsForZSet().reverseRange(redisKey, 0, limit - 1);
            if (keywords == null || keywords.isEmpty()) {
                return new ArrayList<>();
            }
            
            // 转换为列表返回
            return new ArrayList<>(keywords);
        } catch (Exception e) {
            LOGGER.error("获取搜索历史失败: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }
    
    /**
     * 清空搜索历史
     *
     * @return 是否成功
     */
    @Override
    public Boolean clearSearchHistory() {
        UserVO currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return false;
        }
        
        String redisKey = getSearchHistoryRedisKey(currentUser.getId());
        try {
            // 删除整个键 - 使用historyRedisTemplate
            historyRedisTemplate.delete(redisKey);
            
            LOGGER.info("用户:{} 清空搜索历史成功", currentUser.getId());
            return true;
        } catch (Exception e) {
            LOGGER.error("清空搜索历史失败: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 获取用户搜索历史的Redis键
     *
     * @param userId 用户ID
     * @return Redis键
     */
    private String getSearchHistoryRedisKey(Long userId) {
        return SEARCH_HISTORY_KEY_PREFIX + userId;
    }
} 