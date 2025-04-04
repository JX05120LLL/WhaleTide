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
import com.whale_tide.entity.ums.UmsUsers;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
            // 手动映射字段，解决字段不匹配问题
            response.setId(order.getId());
            response.setProductId(order.getProductId());
            response.setContent(order.getContent());
            response.setStar(order.getRating()); // 将rating映射到star
            
            // 设置用户昵称和头像，可以根据userId查询用户信息
            try {
                LambdaQueryWrapper<UmsUsers> userWrapper = new LambdaQueryWrapper<>();
                userWrapper.eq(UmsUsers::getId, order.getUserId());
                UmsUsers user = umsUsersMapper.selectOne(userWrapper);
                if (user != null) {
                    response.setMemberNickName(user.getNickname());
                    response.setMemberIcon(user.getAvatar());
                } else {
                    response.setMemberNickName("匿名用户");
                }
            } catch (Exception e) {
                log.error("查询用户信息失败", e);
                response.setMemberNickName("匿名用户");
            }
            
            // 处理图片列表
            if (order.getImages() != null && !order.getImages().isEmpty()) {
                List<String> pics = Arrays.asList(order.getImages().split(","));
                response.setPics(pics);
            } else {
                response.setPics(new ArrayList<>());
            }
            
            // 设置创建时间
            if (order.getCreateTime() != null) {
                response.setCreateTime(java.sql.Timestamp.valueOf(order.getCreateTime()));
            }
            
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
     *
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

        // 创建评论对象
        PmsProductComments productComment = new PmsProductComments();
        productComment.setProductId(productId);
        productComment.setUserId(getCurrentUserId());
        productComment.setContent(content);
        productComment.setRating(star);
        productComment.setIsAnonymous(0); // 默认不匿名
        productComment.setIsShow(1); // 默认显示
        productComment.setCreateTime(LocalDateTime.now());

        // 插入评论
        productCommentsMapper.insert(productComment);

        // 更新商品评论统计信息
        product.setProductCommentCount(product.getProductCommentCount() + 1);

        product.setProductRating(star);

        product.setProductRatingCount(product.getProductRatingCount() + 1);
        // 更新商品
        LambdaQueryWrapper<PmsProducts> updateWrapper = new LambdaQueryWrapper<>();
        updateWrapper.eq(PmsProducts::getId, productId);
        pmsProductsMapper.update(product, updateWrapper);


        log.info("添加商品评论成功");
    }


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
