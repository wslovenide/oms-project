package com.ws.oms.chat.netty.handler.dto.group;

import com.ws.oms.chat.netty.handler.dto.BaseReq;

/**
 * Created by gongmei on 2018/3/13.
 */
public class CreateGroupReq extends BaseReq {

    private String toSessionId;

    public String getToSessionId() {
        return toSessionId;
    }

    public void setToSessionId(String toSessionId) {
        this.toSessionId = toSessionId;
    }

    @Override
    public String toString() {
        return "CreateGroupReq{" +
                "toSessionId='" + toSessionId + '\'' +
                "} " + super.toString();
    }
}
