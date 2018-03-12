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
public class ChatMsgItemResp implements Serializable , Cloneable{

    private String msg;
    private String time;
    private String date;
    private boolean self = false;
    private String sessionId;
    private String groupId;
    private String nickName;

    public ChatMsgItemResp(){
        String[] dateTime = DateUtil.getNowDateTimeStr().split(" ");
        this.date = dateTime[0];
        this.time = dateTime[1];
    }

    public String getMsg() {
        return msg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
    public ChatMsgItemResp clone()  {
        try {
            return (ChatMsgItemResp)super.clone();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "ChatMsgItemResp{" +
                "msg='" + msg + '\'' +
                ", time='" + time + '\'' +
                ", self=" + self +
                ", sessionId='" + sessionId + '\'' +
                ", groupId='" + groupId + '\'' +
                ", nickName='" + nickName + '\'' +
                '}';
    }
}
