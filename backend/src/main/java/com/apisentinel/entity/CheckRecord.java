package com.apisentinel.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 接口检查记录实体类
 *
 * @author ApiSentinel Team
 */
@Data
@TableName("check_record")
public class CheckRecord {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 监控接口ID
     */
    private Long apiId;

    /**
     * 响应时间（毫秒）
     */
    private Long responseTime;

    /**
     * HTTP状态码
     */
    private Integer statusCode;

    /**
     * 是否成功：0-失败，1-成功
     */
    private Integer success;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 响应内容（可选，记录部分响应）
     */
    private String responseBody;

    /**
     * 检查时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime checkTime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
