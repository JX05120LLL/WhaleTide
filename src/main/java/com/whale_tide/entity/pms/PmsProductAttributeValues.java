package com.whale_tide.entity.pms;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品属性值表
 * </p>
 *
 * @author Bro_cat
 * @since 2025-03-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pms_product_attribute_values")
public class PmsProductAttributeValues implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 属性值ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 属性ID
     */
    private Long attributeId;

    /**
     * 属性值
     */
    private String value;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
