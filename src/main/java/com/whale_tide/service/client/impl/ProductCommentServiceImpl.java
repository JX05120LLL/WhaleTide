package com.whale_tide.service.client.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whale_tide.common.api.PageResponse;
import com.whale_tide.common.exception.product.ProductNotFoundException;
import com.whale_tide.common.exception.user.UsernameNotExistException;
import com.whale_tide.dto.client.product.ProductCommentAddRequest;
import com.whale_tide.dto.client.product.ProductCommentResponse;
import com.whale_tide.dto.client.product.ProductCommentParam;
import com.whale_tide.entity.pms.PmsProductComments;
import com.whale_tide.entity.pms.PmsProductSkus;
import com.whale_tide.entity.pms.PmsProducts;
import com.whale_tide.mapper.pms.PmsProductCommentsMapper;
import com.whale_tide.mapper.pms.PmsProductSkusMapper;
import com.whale_tide.mapper.pms.PmsProductsMapper;
import com.whale_tide.mapper.ums.UmsUsersMapper;
import com.whale_tide.service.client.IProductCommentService;
import com.whale_tide.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.sun.deploy.util.SessionState.save;
import static net.sf.jsqlparser.parser.feature.Feature.comment;
import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;
/**
 * 商品评论服务实现
 */
@Slf4j
@Service
public class ProductCommentServiceImpl implements IProductCommentService {
    @Autowired
    private PmsProductCommentsMapper productCommentsMapper;
    @Autowired
    private PmsProductsMapper pmsProductsMapper;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UmsUsersMapper umsUsersMapper;

    /**
     * 获取商品评论列表
     * @param queryParam 查询参数
     * @return 商品评论列表
     */
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
    /**
     * 添加商品评论
     * @param request 评论请求
     */
    @Override
    public void addProductComment(ProductCommentAddRequest request) {
        // 参数验证
        Long productId = request.getProductId();
        if (productId == null) {
            throw new ProductNotFoundException("商品不存在");
        }
        String content = request.getContent();
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("评论内容不能为空");
        }
        Integer star = request.getStar();
        if (star == null || star < 1 || star > 5) {
            throw new IllegalArgumentException("星级必须在1-5之间");
        }
        List<String> pics = request.getPics();
        if (pics != null && pics.size() > 5) {
            throw new IllegalArgumentException("图片数量不能超过5张");
        }
        // 查询商品
        PmsProducts product = pmsProductsMapper.selectById(productId);
        if (product == null) {
            throw new ProductNotFoundException("商品不存在");
        }
        // 保存评论
        PmsProductComments productComment = new PmsProductComments();
        productComment.setProductId(productId);
        productComment.setUserId(1L);
        productComment.setOrderId(1L);
        productComment.setOrderItemId(1L);
        productComment.setTitle(product.getName());
        productComment.setContent(content);
        productComment.setRating(star);
        productComment.setImages(pics != null ? String.join(",", pics) : null);
        productComment.setIsAnonymous(0);
        productComment.setIsShow(1);
        productComment.setCreateTime(LocalDateTime.now());
        productCommentsMapper.insert(productComment);





    }
}

// Below is partial code of D:/maven-work/WhaleTide/src/main/java/com/whale_tide/entity/pms/PmsProductSkus.java:
