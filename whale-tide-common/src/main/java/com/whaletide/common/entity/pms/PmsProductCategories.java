package com.whaletide.common.entity.pms;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 商品分类表
 * </p>
 *
 * @author BroRem
 * @since 2025-05-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pms_product_categories")
public class PmsProductCategories implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分类ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 父分类ID
     */
    private Long parentId;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类层级：1-一级分类，2-二级分类，3-三级分类
     */
    private Integer level;

    /**
     * 分类图标
     */
    private String icon;

    /**
     * 是否在导航栏显示：0-否，1-是
     */
    private Integer isNav;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 分类描述
     */
    private String description;

    /**
     * 关键词
     * 数据库中不存在此字段，标记为非持久化
     */
    @TableField(exist = false)
    private String keywords;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 是否显示导航栏 0-否，1-是
     * 数据库中字段名为is_nav
     */
    @TableField("is_nav")
    private Integer navStatus;

    /**
     * 是否推荐：0-否，1-是
     * 数据库中不存在此字段，标记为非持久化
     */
    @TableField(exist = false)
    private Integer isFeatured;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
