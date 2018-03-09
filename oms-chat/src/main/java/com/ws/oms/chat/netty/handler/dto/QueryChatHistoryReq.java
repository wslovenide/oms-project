package com.ws.oms.chat.netty.handler.dto;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-03-09 14:58
 */
public class QueryChatHistoryReq extends BaseReq {

    private String groupId;         // 房间id

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "QueryChatHistoryReq{" +
                "groupId='" + groupId + '\'' +
                "} " + super.toString();
    }
}
