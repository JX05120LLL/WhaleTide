package com.whaletide.common.mapper.oms;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.whaletide.common.entity.orderEntity.ProductDailySalesStats;
import com.whaletide.common.entity.oms.OmsOrders;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单主表 Mapper 接口
 * </p>
 *
 * @author BroRem
 * @since 2025-05-10
 */
public interface OmsOrdersMapper extends BaseMapper<OmsOrders> {


    /**
     * 查询基础统计数据
     */
    @Select("SELECT " +
            "COUNT(id) AS totalOrders, " +
            "COALESCE(SUM(total_amount), 0) AS totalAmount, " +
            "(SELECT COUNT(1) FROM `oms_orders` WHERE id IN (" +
            "  SELECT id FROM `oms_orders` WHERE create_time BETWEEN #{startDate} AND #{endDate} " +
            "  AND status != 'CANCELLED'" +
            ")) AS totalItems " +
            "FROM `oms_orders` " +
            "WHERE create_time BETWEEN #{startDate} AND DATE_ADD(#{endDate}, INTERVAL 1 DAY) " +
            "AND status != 'CANCELLED'")
    Map<String, Object> getBasicStatistics(@Param("startDate") LocalDate startDate,
                                           @Param("endDate") LocalDate endDate);

    /**
     * 查询订单状态分布
     */
    @Select("SELECT " +
            "status, COUNT(id) AS count " +
            "FROM `oms_orders` " +
            "WHERE create_time BETWEEN #{startDate} AND DATE_ADD(#{endDate}, INTERVAL 1 DAY) " +
            "GROUP BY status")
    @Results({
            @Result(property = "key", column = "status"),
            @Result(property = "value", column = "count")})
    Map<String, Integer> getStatusDistribution(@Param("startDate") LocalDate startDate,
                                               @Param("endDate") LocalDate endDate);


    /**
     * 查询每日销售趋势
     */
    @Select("SELECT " +
            "DATE_FORMAT(create_time, '%Y-%m-%d') AS date, " +
            "COALESCE(SUM(total_amount), 0) AS amount, " +
            "COUNT(id) AS count " +
            "FROM `oms_orders` " +
            "WHERE create_time BETWEEN #{startDate} AND DATE_ADD(#{endDate}, INTERVAL 1 DAY) " +
            "AND status != 4 " +
            "GROUP BY date " +
            "ORDER BY date ASC")
    List<ProductDailySalesStats> getDailySalesTrend(@Param("startDate") LocalDate startDate,
                                                      @Param("endDate") LocalDate endDate);


    /**
     * 查询每周销售趋势
     */
    @Select("SELECT " +
            "CONCAT(YEARWEEK(create_time), ' 周') AS date, " +
            "MIN(DATE_FORMAT(create_time, '%Y-%m-%d')) AS week_start, " +
            "COALESCE(SUM(total_amount), 0) AS amount, " +
            "COUNT(id) AS count " +
            "FROM `oms_orders` " +
            "WHERE create_time BETWEEN #{startDate} AND DATE_ADD(#{endDate}, INTERVAL 1 DAY) " +
            "AND status != 4 " +
            "GROUP BY date " +
            "ORDER BY week_start ASC")
    @Results({
            @Result(property = "date", column = "date"),
            @Result(property = "amount", column = "amount"),
            @Result(property = "count", column = "count")
    })
    List<ProductDailySalesStats> getWeeklySalesTrend(@Param("startDate") LocalDate startDate,
                                                       @Param("endDate") LocalDate endDate);

    /**
     * 查询每月销售趋势
     */
    @Select("SELECT " +
            "DATE_FORMAT(create_time, '%Y-%m') AS date, " +
            "COALESCE(SUM(total_amount), 0) AS amount, " +
            "COUNT(id) AS count " +
            "FROM `oms_orders` " +
            "WHERE create_time BETWEEN #{startDate} AND DATE_ADD(#{endDate}, INTERVAL 1 DAY) " +
            "AND status != 4 " +
            "GROUP BY date " +
            "ORDER BY date ASC")
    List<ProductDailySalesStats> getMonthlySalesTrend(@Param("startDate") LocalDate startDate,
                                                        @Param("endDate") LocalDate endDate);
}
