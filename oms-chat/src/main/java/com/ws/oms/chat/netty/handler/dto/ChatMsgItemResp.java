package com.ws.oms.chat.netty.handler.dto;

import com.ws.oms.chat.netty.util.DateUtil;

import java.io.Serializable;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-03-09 16:09
 */
public class ChatMsgItemResp implements Serializable{

    private String msg;
    private String dateTime;
    private boolean self = false;
    private String sessionId;
    private String groupId;
    private String nickName;

    public ChatMsgItemResp(){
        this.dateTime = DateUtil.getNowTimeStr();
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isSelf() {
        return self;
    }

    public void setSelf(boolean self) {
        this.self = self;
    }

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

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "ChatMsgItemResp{" +
                "msg='" + msg + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", self=" + self +
                ", sessionId='" + sessionId + '\'' +
                ", groupId='" + groupId + '\'' +
                ", nickName='" + nickName + '\'' +
                '}';
    }
}
