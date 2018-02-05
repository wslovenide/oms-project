package com.ws.oms.web.enums;

import com.ws.service.enums.ResultCode;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-02-05 16:00
 */
public enum WebResultCode implements ResultCode{

    USERNAME_PASSWORD_PARAM("1000","用户名或密码为空"),
    USERNAME_PASSWORD_ERROR("1001","用户名或密码错误"),


    SUCCESS("0000","成功"),
    ERROR("500","失败");

    String code;
    String desc;

    WebResultCode(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getCode() {
        return null;
    }

    @Override
    public String getDesc() {
        return null;
    }

}
