package com.macro.mall.tiny.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.macro.mall.tiny.entity.Cart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ICartMapper extends BaseMapper<Cart> {
}
