package com.whale_tide.entity.ums;

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
 * 用户登录日志表
 * </p>
 *
 * @author Bro_cat
 * @since 2025-03-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ums_user_login_logs")
public class UmsUserLoginLogs implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 登录IP
     */
    private String ip;

    /**
     * 登录地点
     */
    private String location;

    /**
     * 登录设备
     */
    private String device;

    /**
     * 浏览器类型
     */
    private String userAgent;

    /**
     * 登录类型：0-普通登录，1-短信登录，2-第三方登录
     */
    private Integer loginType;

    /**
     * 登录来源：0-PC，1-APP，2-小程序，3-H5
     */
    private Integer loginSource;

    /**
     * 登录状态：0-失败，1-成功
     */
    private Integer status;

    /**
     * 提示消息
     */
    private String message;

    /**
     * 登录时间
     */
    private LocalDateTime loginTime;


}
