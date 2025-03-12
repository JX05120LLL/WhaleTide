package com.whale_tide.entity;

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
 * 商品属性表
 * </p>
 *
 * @author Bro_cat
 * @since 2025-03-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pms_product_attributes")
public class PmsProductAttributes implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 属性ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属分类ID
     */
    private Long categoryId;

    /**
     * 属性名称
     */
    private String name;

    /**
     * 属性类型：0-规格，1-参数
     */
    private Integer attributeType;

    /**
     * 录入方式：0-手工输入，1-从列表选择
     */
    private Integer inputType;

    /**
     * 可选值列表，以逗号分隔
     */
    private String inputOptions;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 检索类型：0-不需要，1-需要
     */
    private Integer filterType;

    /**
     * 搜索类型：0-不需要，1-需要
     */
    private Integer searchType;

    /**
     * 是否必填：0-否，1-是
     */
    private Integer isRequired;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
