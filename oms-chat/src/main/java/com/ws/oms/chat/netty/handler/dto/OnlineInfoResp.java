package com.ws.oms.chat.netty.handler.dto;

/**
 * Created by gongmei on 2018/3/16.
 */
public class OnlineInfoResp {

    private String sessionId;
    private String nickName;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return "OnlineInfoResp{" +
                "sessionId='" + sessionId + '\'' +
                ", nickName='" + nickName + '\'' +
                '}';
    }
}
