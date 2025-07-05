package com.skmj.server.util;

import lombok.Data;

/**
 * 统一返回结果类
 * @param <T> 返回数据类型
 */
@Data
public class Result<T> {
    /**
     * 是否成功
     */
    private boolean success;
    
    /**
     * 状态码
     */
    private int code;
    
    /**
     * 提示信息
     */
    private String message;
    
    /**
     * 返回数据
     */
    private T data;
    
    private Result() {}
    
    /**
     * 成功时返回数据
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setSuccess(true);
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }
    
    /**
     * 成功时返回数据
     */
    public static <T> Result<T> success(int code, T data) {
        Result<T> result = new Result<>();
        result.setSuccess(true);
        result.setCode(code);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }
    
    /**
     * 成功时返回提示信息
     */
    public static <T> Result<T> success(String message) {
        Result<T> result = new Result<>();
        result.setSuccess(true);
        result.setCode(200);
        result.setMessage(message);
        return result;
    }
    
    /**
     * 失败时返回错误信息
     */
    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.setSuccess(false);
        result.setCode(500);
        result.setMessage(message);
        return result;
    }
    
    /**
     * 自定义返回结果
     */
    public static <T> Result<T> custom(boolean success, int code, String message, T data) {
        Result<T> result = new Result<>();
        result.setSuccess(success);
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }
}