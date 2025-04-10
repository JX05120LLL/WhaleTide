package com.whale_tide.entity.ums;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 用户浏览历史表
 * </p>
 *
 * @author YeSKY
 * @since 2025-04-10
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ums_user_browse_history")
public class UmsUserBrowseHistory {

    private static final long serialVersionUID = 1L;

    /**
     * 浏览历史ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 商品名称
     *
     * @return
     */
    private String productName;

    /**
     * 浏览次数
     */
    private Integer browseCount;

    /**
     * 最后浏览时间
     */
    private LocalDateTime lastBrowseTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
