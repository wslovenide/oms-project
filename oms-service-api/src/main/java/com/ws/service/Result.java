package com.ws.service;

import com.ws.service.enums.ServiceResultCode;
import com.ws.service.enums.ResultCode;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-02-05 15:43
 */
public class Result<T> {

    private String code;
    private String message;
    private boolean success;
    private T value;

    public Result(){}

    public Result(String code,String message,boolean success,T t){
        this.code = code;
        this.message = message;
        this.success = success;
        this.value = t;
    }


    public static <T> Result<T> newSuccess(T t){
        ResultCode r = ServiceResultCode.SUCCESS;
        return new Result<>(r.getCode(),r.getDesc(),true,t);
    }

    public static <T> Result<T> newError(ResultCode r){
        return new Result<>(r.getCode(),r.getDesc(),false,null);
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Result{");
        sb.append("code='").append(code).append('\'');
        sb.append(", message='").append(message).append('\'');
        sb.append(", success=").append(success);
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
