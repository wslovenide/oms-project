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
    private boolean success = true;

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "ChatMsgResp{" +
                "msg=" + msg +
                ", success=" + success +
                "} " + super.toString();
    }
}
