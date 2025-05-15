package com.whaletide.client.product.service;

import com.whaletide.client.product.vo.ProductHistoryVO;
import com.whaletide.common.api.CommonPage;

import java.util.List;

/**
 * 浏览历史服务接口
 */
public interface IHistoryService {
    
    /**
     * 获取浏览历史列表
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 浏览历史分页列表
     */
    CommonPage<ProductHistoryVO> list(Integer pageNum, Integer pageSize);
    
    /**
     * 清空浏览历史
     * @return 是否成功
     */
    Boolean clear();
    
    /**
     * 删除指定浏览历史
     * @param ids 历史记录ID集合，多个ID用逗号分隔
     * @return 是否成功
     */
    Boolean delete(String ids);
    
    /**
     * 添加浏览历史记录
     * @param productId 商品ID
     * @return 是否成功
     */
    Boolean add(Long productId);
    
    /**
     * 添加搜索历史记录
     * @param keyword 搜索关键词
     * @return 是否成功
     */
    Boolean addSearchHistory(String keyword);
    
    /**
     * 获取用户搜索历史列表
     * @param limit 限制数量
     * @return 搜索历史列表
     */
    List<String> getSearchHistory(Integer limit);
    
    /**
     * 清空搜索历史
     * @return 是否成功
     */
    Boolean clearSearchHistory();
} 