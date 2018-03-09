package com.ws.oms.chat.netty.handler.dto;

import com.ws.oms.chat.netty.util.DateUtil;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by gongmei on 2018/3/5.
 */
public class ChatMsg implements Serializable,Cloneable {

    private Object msg;
    private String msgType;
    private String dateTime;
    private boolean self = false;
    private String sessionId;
    private String nickName;
    private String remoteAddress;
    private String groupId;
    private String toSessionId;

    public ChatMsg(){
    }

    public ChatMsg(String msg){
        this.msg = msg;
        this.dateTime = DateUtil.getNowTimeStr();
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getToSessionId() {
        return toSessionId;
    }

    public void setToSessionId(String toSessionId) {
        this.toSessionId = toSessionId;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public ChatMsg clone() throws CloneNotSupportedException {
        return (ChatMsg)super.clone();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ChatMsg{");
        sb.append("msg=").append(msg);
        sb.append(", msgType='").append(msgType).append('\'');
        sb.append(", dateTime='").append(dateTime).append('\'');
        sb.append(", self=").append(self);
        sb.append(", sessionId='").append(sessionId).append('\'');
        sb.append(", nickName='").append(nickName).append('\'');
        sb.append(", remoteAddress='").append(remoteAddress).append('\'');
        sb.append(", groupId='").append(groupId).append('\'');
        sb.append(", toSessionId='").append(toSessionId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
