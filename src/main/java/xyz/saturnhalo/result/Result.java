package xyz.saturnhalo.result;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用响应结果
 */
@Data
public class Result<T> implements Serializable {
    private Integer code;    // 业务状态码
    private String msg;     // 提示信息
    private T data;         // 返回数据

    /**
     * 成功，无返回数据
     */
    public static <T> Result<T> ok() {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg("操作成功");
        return result;
    }

    /**
     * 成功，有返回数据
     */
    public static <T> Result<T> ok(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg("操作成功");
        result.setData(data);
        return result;
    }

    /**
     * 失败
     */
    public static <T> Result<T> fail(Integer code, String msg) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}