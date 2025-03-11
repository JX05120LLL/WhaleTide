package com.macro.mall.tiny.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.macro.mall.tiny.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IOderItemMapper extends BaseMapper<OrderItem> {
}
