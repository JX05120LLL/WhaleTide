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
 * 管理员操作日志表
 * </p>
 *
 * @author Bro_cat
 * @since 2025-03-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ams_admin_operation_logs")
public class AmsAdminOperationLogs implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 管理员ID
     */
    private Long adminId;

    /**
     * 管理员账号
     */
    private String username;

    /**
     * 操作模块
     */
    private String module;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求URL
     */
    private String url;

    /**
     * 操作IP
     */
    private String ip;

    /**
     * 浏览器类型
     */
    private String userAgent;

    /**
     * 请求参数
     */
    private String requestParam;

    /**
     * 响应数据
     */
    private String responseData;

    /**
     * 操作状态：0-失败，1-成功
     */
    private Integer status;

    /**
     * 错误消息
     */
    private String errorMessage;

    /**
     * 操作时间
     */
    private LocalDateTime operationTime;


}
