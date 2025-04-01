package com.whale_tide.service.client.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale_tide.common.api.PageResponse;
import com.whale_tide.dto.client.product.ProductCommentAddRequest;
import com.whale_tide.dto.client.product.ProductCommentResponse;
import com.whale_tide.dto.client.product.ProductCommentParam;
import com.whale_tide.entity.pms.PmsProductComments;
import com.whale_tide.mapper.pms.PmsProductCommentsMapper;
import com.whale_tide.service.client.IProductCommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class ProductCommentServiceImpl implements IProductCommentService {
   @Autowired
   private PmsProductCommentsMapper productCommentsMapper;
    @Override
    public PageResponse<ProductCommentResponse> getProductCommentList(ProductCommentParam queryParam) {
        // 参数验证
        if (queryParam.getPageNum() == null || queryParam.getPageNum() < 1) {
            queryParam.setPageNum(1);
        }
        if (queryParam.getPageSize() == null || queryParam.getPageSize() < 1) {
            queryParam.setPageSize(10);
        }
        //解析参数
        Long productId = queryParam.getProductId();
        Integer pageNum = queryParam.getPageNum();
        Integer pageSize = queryParam.getPageSize();
        //构建查询条件
        QueryWrapper<PmsProductComments> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PmsProductComments::getProductId, productId);
        //分页查询
        IPage<PmsProductComments> orderPageResult = productCommentsMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);
       //转换成ProductCommentResponse
        List<ProductCommentResponse> resultList = orderPageResult.getRecords().stream().map(order -> {
            ProductCommentResponse response = new ProductCommentResponse();
            BeanUtils.copyProperties(order, response);
            return response;
        }).collect(Collectors.toList());
       //计算总页数
            long total = orderPageResult.getTotal();
            int totalPage = (int) (total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
            //封装分页结果
            return PageResponse.of(resultList, pageNum, pageSize, total, totalPage);

    }

    @Override
    public void addProductComment(ProductCommentAddRequest request) {

    }

}
