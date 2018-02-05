package com.ws.service.enums;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-02-05 15:57
 */
public enum ServiceResultCode implements ResultCode {

    SUCCESS("200","成功"),
    ERROR("500","失败");

    String code;
    String desc;

    ServiceResultCode(String code, String desc){
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
