package com.ws.oms.chat.netty.handler.dto;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-03-09 15:16
 */
public class SendMessageReq extends BaseReq {

    private String msg;
    private String groupId;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "SendMessageReq{" +
                "msg='" + msg + '\'' +
                ", groupId='" + groupId + '\'' +
                "} " + super.toString();
    }
}
