package com.whale_tide.entity.pms;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 商品服务保障实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pms_product_service_guarantees")
public class ProductServiceGuarantee {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long productId;
    
    private Long skuId;
    
    private String serviceType;
    
    private String serviceName;
    
    private String serviceDesc;
    
    private String serviceRules;
    
    private Integer serviceStatus;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
} 