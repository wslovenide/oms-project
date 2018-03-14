package com.ws.oms.chat.netty.handler.dto;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-03-09 16:13
 */
public class ChatMsgResp extends BaseReq{

    private Object msg;
    private int count;              // 在线人数
    private String groupId;         // 所在组
    private boolean success = true;


    public ChatMsgResp(){}

    public ChatMsgResp(BaseReq baseReq){
        this.setSessionId(baseReq.getSessionId());
        this.setCommand(baseReq.getCommand());
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ChatMsgResp{");
        sb.append("msg=").append(msg);
        sb.append(", count=").append(count);
        sb.append(", success=").append(success);
        sb.append('}');
        return sb.toString();
    }
}
