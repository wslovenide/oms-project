package com.ws.oms.result;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-03-08 13:37
 */
public enum WebResultCode implements ResultCode {

    VERIFY_CODE_NULL("1001","验证码为空"),
    VERIFY_CODE_ERROR("1002","验证码错误"),
    PARAM_NULL_ERROR("1003","必要字段为空"),
    PASSWORD_CONFIRM_ERROR("1004","确认密码错误"),
    USER_NAME_EXISTS("1005","用户名已存在"),

    ERROR("500","系统错误"),
    SUCCESS("200","操作成功")
    ;

    String code;
    String message;

    WebResultCode(String code,String message){
        this.code = code;
        this.message = message;
    }

    ;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
