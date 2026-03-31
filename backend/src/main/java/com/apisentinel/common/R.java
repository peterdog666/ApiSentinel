package com.apisentinel.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回结果封装类
 *
 * @param <T> 数据类型
 * @author ApiSentinel Team
 */
@Data
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 返回消息
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 时间戳
     */
    private Long timestamp;

    public R() {
        this.timestamp = System.currentTimeMillis();
    }

    public R(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 成功返回结果
     */
    public static <T> R<T> ok() {
        return new R<>(200, "操作成功", null);
    }

    /**
     * 成功返回结果
     *
     * @param data 返回数据
     */
    public static <T> R<T> ok(T data) {
        return new R<>(200, "操作成功", data);
    }

    /**
     * 成功返回结果
     *
     * @param message 返回消息
     * @param data    返回数据
     */
    public static <T> R<T> ok(String message, T data) {
        return new R<>(200, message, data);
    }

    /**
     * 失败返回结果
     */
    public static <T> R<T> error() {
        return new R<>(500, "操作失败", null);
    }

    /**
     * 失败返回结果
     *
     * @param message 返回消息
     */
    public static <T> R<T> error(String message) {
        return new R<>(500, message, null);
    }

    /**
     * 失败返回结果
     *
     * @param code    状态码
     * @param message 返回消息
     */
    public static <T> R<T> error(Integer code, String message) {
        return new R<>(code, message, null);
    }

    /**
     * 参数错误
     *
     * @param message 错误消息
     */
    public static <T> R<T> badRequest(String message) {
        return new R<>(400, message, null);
    }

    /**
     * 未找到资源
     *
     * @param message 错误消息
     */
    public static <T> R<T> notFound(String message) {
        return new R<>(404, message, null);
    }
}
