package com.whale_tide.dto.client.home;

import com.whale_tide.common.api.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 首页商品列表请求参数
 */
@Data
@ApiModel("首页商品列表请求参数")
public class HomeProductRequest extends PageRequest {
    // 继承了PageRequest，已包含分页参数
} 