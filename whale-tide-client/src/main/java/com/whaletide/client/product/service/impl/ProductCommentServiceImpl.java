package com.whaletide.client.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whaletide.client.product.dto.ProductCommentAddDTO;
import com.whaletide.client.product.dto.ProductCommentQueryDTO;
import com.whaletide.client.product.service.IProductCommentService;
import com.whaletide.client.product.vo.ProductCommentVO;
import com.whaletide.common.api.CommonPage;
import com.whaletide.common.api.CommonResult;
import com.whaletide.common.entity.pms.PmsProductComments;
import com.whaletide.common.entity.pms.PmsProducts;
import com.whaletide.common.exception.auth.AuthenticationException;
import com.whaletide.common.exception.base.BusinessException;
import com.whaletide.common.exception.product.ProductNotFoundException;
import com.whaletide.common.mapper.pms.PmsProductCommentsMapper;
import com.whaletide.common.mapper.pms.PmsProductsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 商品评论服务实现类
 */
@Service
@Slf4j
public class ProductCommentServiceImpl implements IProductCommentService {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Autowired
    private PmsProductCommentsMapper commentMapper;
    
    @Autowired
    private PmsProductsMapper productMapper;
    
    @Override
    public ProductCommentVO getProductCommentList(ProductCommentQueryDTO queryDTO) {
        log.info("获取商品评论列表, 商品ID: {}, 页码: {}, 每页数量: {}", 
            queryDTO.getProductId(), queryDTO.getPageNum(), queryDTO.getPageSize());
        
        // 创建默认空结果，确保即使出错也能返回有效对象
        ProductCommentVO resultVO = new ProductCommentVO();
        resultVO.setList(new ArrayList<>());
        resultVO.setTotal(0L);
        
        try {
            if (queryDTO.getProductId() == null) {
                log.warn("商品ID为空，返回空评论列表");
                return resultVO;
            }
            
            // 构建缓存Key
            String cacheKey = "product:comment:list:" + queryDTO.getProductId() + ":" 
                + queryDTO.getPageNum() + ":" + queryDTO.getPageSize();
            
            // 尝试从缓存获取
            try {
                ProductCommentVO cacheResult = (ProductCommentVO) redisTemplate.opsForValue().get(cacheKey);
                if (cacheResult != null) {
                    log.info("从缓存获取商品评论列表");
                    return cacheResult;
                }
            } catch (Exception e) {
                log.error("从缓存获取评论列表失败", e);
                // 缓存获取失败，继续后续逻辑
            }
            
            // 检查商品是否存在
            PmsProducts product = productMapper.selectById(queryDTO.getProductId());
            if (product == null) {
                log.warn("商品不存在: {}", queryDTO.getProductId());
                return resultVO;
            }
            
            // 从数据库查询 - 避免使用不存在的字段
            try {
                Page<PmsProductComments> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
                LambdaQueryWrapper<PmsProductComments> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(PmsProductComments::getProductId, queryDTO.getProductId())
                        .orderByDesc(PmsProductComments::getCreateTime);
                
                // 打印SQL查询前的参数
                log.debug("查询参数: productId={}, pageNum={}, pageSize={}", 
                         queryDTO.getProductId(), queryDTO.getPageNum(), queryDTO.getPageSize());
                
                Page<PmsProductComments> commentPage = commentMapper.selectPage(page, queryWrapper);
                
                log.info("查询到评论数量: {}", 
                         (commentPage != null && commentPage.getRecords() != null) ? 
                         commentPage.getRecords().size() : 0);
                
                // 如果有评论数据，则转换为VO列表
                if (commentPage != null && commentPage.getRecords() != null && !commentPage.getRecords().isEmpty()) {
                    List<ProductCommentVO> commentVOList = new ArrayList<>();
                    
                    for (PmsProductComments comment : commentPage.getRecords()) {
                        try {
                            commentVOList.add(convertToVO(comment));
                        } catch (Exception e) {
                            log.error("转换评论VO失败: {}", comment.getId(), e);
                            // 跳过转换失败的评论
                        }
                    }
                    
                    resultVO.setList(commentVOList);
                    resultVO.setTotal(commentPage.getTotal());
                    
                    // 如果有第一条评论，则设置到主VO
                    if (!commentVOList.isEmpty()) {
                        ProductCommentVO firstComment = commentVOList.get(0);
                        resultVO.setId(firstComment.getId());
                        resultVO.setProductId(firstComment.getProductId());
                        resultVO.setUserName(firstComment.getUserName());
                        resultVO.setUserIcon(firstComment.getUserIcon());
                        resultVO.setContent(firstComment.getContent());
                        resultVO.setStar(firstComment.getStar());
                        resultVO.setPics(firstComment.getPics());
                        resultVO.setCreateTime(firstComment.getCreateTime());
                        resultVO.setReplyContent(firstComment.getReplyContent());
                        resultVO.setReplyTime(firstComment.getReplyTime());
                    }
                } else {
                    log.info("商品 {} 没有评论数据", queryDTO.getProductId());
                }
            } catch (Exception e) {
                log.error("查询商品评论失败: {}", e.getMessage(), e);
                // 返回空结果而不是抛出异常
            }
            
            // 将结果放入缓存
            try {
                redisTemplate.opsForValue().set(cacheKey, resultVO, 10, TimeUnit.MINUTES);
            } catch (Exception e) {
                log.error("评论列表缓存失败", e);
                // 缓存失败不影响返回结果
            }
            
        } catch (Exception e) {
            log.error("获取商品评论列表发生异常: {}", queryDTO.getProductId(), e);
            // 出现异常时返回空结果，避免服务器错误
        }
        
        return resultVO;
    }


    // 添加评论
    @Override
    @Transactional
    public void addProductComment(ProductCommentAddDTO commentDTO) {
        // 获取当前用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationException("用户未登录");
        }
        
        String username = authentication.getName();
        Long userId = getUserIdFromUsername(username);
        
        log.info("添加商品评论, 用户: {}, 商品ID: {}", username, commentDTO.getProductId());
        
        if (commentDTO.getProductId() == null) {
            throw new ProductNotFoundException("商品ID不能为空");
        }
        
        // 检查商品是否存在
        PmsProducts product = productMapper.selectById(commentDTO.getProductId());
        if (product == null) {
            throw new ProductNotFoundException("商品不存在");
        }
        

        // 创建评论实体
        PmsProductComments comment = new PmsProductComments();
        comment.setProductId(commentDTO.getProductId());
        comment.setUserId(userId);
        comment.setContent(commentDTO.getContent());
        comment.setRating(new BigDecimal(commentDTO.getStar()));
        
        // 处理图片
        if (commentDTO.getPics() != null && !commentDTO.getPics().isEmpty()) {
            comment.setImages(String.join(",", commentDTO.getPics()));
        }
        
        // 设置状态为正常
        comment.setStatus(1);
        
        comment.setCreateTime(LocalDateTime.now());

        // 记录SQL运行前的参数
        log.info("准备插入评论数据: productId={}, userId={}, content={}, rating={}, status={}", 
                comment.getProductId(), comment.getUserId(), comment.getContent(), 
                comment.getRating(), comment.getStatus());
                
        try {
            // 保存评论
            commentMapper.insert(comment);
            log.info("评论数据插入成功: commentId={}", comment.getId());
        } catch (Exception e) {
            log.error("评论数据插入失败", e);
            throw e;
        }
        
        // 清除评论列表缓存
        String cacheKeyPattern = "product:comment:list:" + commentDTO.getProductId() + ":*";
        // 查找所有匹配的缓存键并删除
        redisTemplate.keys(cacheKeyPattern).forEach(key -> redisTemplate.delete(key));
        
        log.info("添加商品评论成功, 用户: {}, 商品ID: {}", username, commentDTO.getProductId());
    }
    
    /**
     * 将评论实体转换为VO
     */
    private ProductCommentVO convertToVO(PmsProductComments comment) {
        if (comment == null) {
            return null;
        }
        
        ProductCommentVO vo = new ProductCommentVO();
        vo.setId(comment.getId());
        vo.setProductId(comment.getProductId());
        vo.setUserName("用户" + (comment.getUserId() != null ? comment.getUserId() : "匿名"));
        vo.setUserIcon("/images/default-avatar.png"); // 设置默认头像
        vo.setContent(comment.getContent() != null ? comment.getContent() : "");
        vo.setStar(comment.getRating() != null ? comment.getRating().intValue() : 5);
        
        // 处理图片
        if (comment.getImages() != null && StringUtils.hasText(comment.getImages())) {
            vo.setPics(Arrays.asList(comment.getImages().split(",")));
        } else {
            vo.setPics(new ArrayList<>());
        }
        
        // 转换日期
        if (comment.getCreateTime() != null) {
            vo.setCreateTime(Date.from(comment.getCreateTime().atZone(ZoneId.systemDefault()).toInstant()));
        } else {
            vo.setCreateTime(new Date());
        }
        

        return vo;
    }
    
    /**
     * 根据用户名获取用户ID（示例方法，实际项目中应该查询用户表）
     */
    private Long getUserIdFromUsername(String username) {
        // 此处应该查询用户表获取ID
        // 示例实现，返回固定值
        return 1L;
    }
} 