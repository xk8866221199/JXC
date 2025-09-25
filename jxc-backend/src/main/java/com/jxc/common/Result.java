package com.jxc.common;

/**
 * 统一响应结果
 * 
 * @author JXC Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
public class Result<T> {
    
    private int code;
    private String message;
    private T data;
    private long timestamp;
    
    public Result() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public Result(int code, String message) {
        this();
        this.code = code;
        this.message = message;
    }
    
    public Result(int code, String message, T data) {
        this(code, message);
        this.data = data;
    }
    
    /**
     * 成功响应
     */
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功");
    }
    
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }
    
    public static <T> Result<T> success(T data, String message) {
        return new Result<>(200, message, data);
    }
    
    public static <T> Result<T> success(String message) {
        return new Result<>(200, message);
    }
    
    /**
     * 失败响应
     */
    public static <T> Result<T> error() {
        return new Result<>(500, "操作失败");
    }
    
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message);
    }
    
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message);
    }
    
    public static <T> Result<T> error(int code, String message, T data) {
        return new Result<>(code, message, data);
    }
    
    // Getters and Setters
    public int getCode() {
        return code;
    }
    
    public void setCode(int code) {
        this.code = code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return this.code == 200;
    }
}