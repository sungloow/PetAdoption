package com.sunhongbing.petadoption.backstage.result;

import lombok.Data;

import java.io.Serializable;

/**
 * @className: ResponseResult
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-02-15 09:57
 */
@Data
public class ResultVO implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final Integer CODE = 0;

    private Integer code;

    private String msg;

    private Object result;

    public ResultVO() {

    }

    public ResultVO(Integer code) {
        this.code = code;
    }

    public ResultVO(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultVO(Integer code, Object result) {
        this.code = code;
        this.result = result;
    }

    public ResultVO(Integer code, String msg, Object result) {
        this.code = code;
        this.msg = msg;
        this.result = result;
    }

    public ResultVO(Object result) {
        this.result = result;
    }

    public static ResultVO error() {
        return error(500, "未知异常，请联系管理员");
    }

    public static ResultVO error(String msg) {
        return error(500, msg);
    }

    public static ResultVO error(Integer code, String msg) {
        return new ResultVO(code, msg);
    }

    /**

     带通用返回数据
     @param msg
     @param result
     @return
     */
    public static ResultVO ok(String msg, Object result) {
        return new ResultVO(CODE, msg, result);
    }
    /**

     不带数据
     @param msg
     @return
     */
    public static ResultVO ok(String msg) {
        return new ResultVO(CODE, msg);
    }
    /**

     带数据
     @param result
     @return
     */
    public static ResultVO ok(Object result) {
        return new ResultVO(CODE, result);
    }
    /**

     只带状态码
     @return
     */
    public static ResultVO ok() {
        return new ResultVO(CODE);
    }

}
