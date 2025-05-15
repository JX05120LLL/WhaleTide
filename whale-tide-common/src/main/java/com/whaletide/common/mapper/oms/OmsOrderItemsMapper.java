package com.whaletide.common.mapper.oms;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.whaletide.common.entity.orderEntity.ProductSales;
import com.whaletide.common.entity.oms.OmsOrderItems;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单项Mapper
 */
@Mapper
public interface OmsOrderItemsMapper extends BaseMapper<OmsOrderItems> {

    /**
     * 查询热销商品
     */
    @Select("SELECT " +
            "p.id, p.name, p.main_image, p.price, " +
            "SUM(oi.product_quantity) AS salesCount " +
            "FROM oms_order_items oi " +
            "JOIN pms_products p ON oi.product_id = p.id " +
            "JOIN `oms_orders` o ON oi.order_id = o.id " +
            "WHERE o.create_time BETWEEN #{startDate} AND DATE_ADD(#{endDate}, INTERVAL 1 DAY) " +
            "AND o.status != 4 " +
            "GROUP BY p.id " +
            "ORDER BY salesCount DESC " +
            "LIMIT #{limit}")
    List<ProductSales> getTopSellingProducts(@Param("startDate") LocalDate startDate,
                                               @Param("endDate") LocalDate endDate,
                                               @Param("limit") int limit);
} 