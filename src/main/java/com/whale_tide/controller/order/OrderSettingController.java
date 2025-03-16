package com.whale_tide.controller.order;

import com.whale_tide.common.api.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 订单设置管理Controller
 */
@Slf4j
@RestController
@RequestMapping("/orderSetting")
public class OrderSettingController {

    /**
     * 获取订单设置
     */
    @GetMapping("/{id}")
    public CommonResult<Map<String, Object>> getItem(@PathVariable Long id) {
        log.info("获取订单设置信息, id={}", id);
        
        // 创建一个临时的订单设置数据作为示例
        Map<String, Object> orderSetting = new HashMap<>();
        orderSetting.put("id", id);
        orderSetting.put("flashOrderOvertime", 60); // 秒杀订单超时关闭时间（分钟）
        orderSetting.put("normalOrderOvertime", 120); // 正常订单超时时间（分钟）
        orderSetting.put("confirmOvertime", 15); // 发货后自动确认收货时间（天）
        orderSetting.put("finishOvertime", 7); // 自动完成交易时间（天）
        orderSetting.put("commentOvertime", 7); // 订单完成后自动好评时间（天）
        
        return CommonResult.success(orderSetting);
    }
    
    /**
     * 更新订单设置
     */
    @PostMapping("/update/{id}")
    public CommonResult<Integer> update(
            @PathVariable Long id,
            @RequestBody Map<String, Object> orderSetting) {
        
        log.info("更新订单设置, id={}, orderSetting={}", id, orderSetting);
        
        // 返回更新结果
        return CommonResult.success(1);
    }
} 