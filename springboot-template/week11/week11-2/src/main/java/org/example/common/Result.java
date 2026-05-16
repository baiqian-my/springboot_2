package org.example.common;

import lombok.Data;

/**
 * 统一响应结果 - 匹配 maku-boot 前端规范
 * 成功 code = 0
 * 失败 code = 非0
 */
@Data
public class Result<T> {
    
    private Integer code;
    private String msg;
    private T data;
    
    public static <T> Result<T> success() {
        return success(null);
    }
    
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(0);
        result.setMsg("操作成功");
        result.setData(data);
        return result;
    }
    
    public static <T> Result<T> success(String msg, T data) {
        Result<T> result = new Result<>();
        result.setCode(0);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }
    
    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.setCode(500);
        result.setMsg(msg);
        return result;
    }
    
    public static <T> Result<T> error(Integer code, String msg) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
    
    /**
     * 未授权 - 401
     */
    public static <T> Result<T> unauthorized(String msg) {
        return error(401, msg);
    }
    
    /**
     * 参数错误 - 400
     */
    public static <T> Result<T> badRequest(String msg) {
        return error(400, msg);
    }
}
