package com.apisentinel.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 监控接口配置实体类
 *
 * @author ApiSentinel Team
 */
@Data
@TableName("monitor_api")
public class MonitorApi {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 接口URL
     */
    private String url;

    /**
     * 请求方法 (GET, POST, PUT, DELETE, etc.)
     */
    private String method;

    /**
     * 请求头 (JSON格式)
     */
    private String headers;

    /**
     * 请求体 (JSON格式，用于POST/PUT等)
     */
    private String body;

    /**
     * 检查间隔（分钟）
     */
    private Integer checkInterval;

    /**
     * 超时时间（秒）
     */
    private Integer timeout;

    /**
     * 连续失败次数阈值，达到后触发告警
     */
    private Integer failThreshold;

    /**
     * 钉钉Webhook地址
     */
    private String dingtalkWebhook;

    /**
     * 钉钉密钥
     */
    private String dingtalkSecret;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除标志
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;
}
