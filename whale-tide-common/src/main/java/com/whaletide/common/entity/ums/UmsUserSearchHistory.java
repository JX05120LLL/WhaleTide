package com.whaletide.common.entity.ums;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户搜索历史表
 * </p>
 *
 * @author BroRem
 * @since 2025-05-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ums_user_search_history")
public class UmsUserSearchHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 搜索ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 搜索关键词
     */
    private String keyword;

    /**
     * 搜索次数
     */
    private Integer searchCount;

    /**
     * 最后搜索时间
     */
    private LocalDateTime lastSearchTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
