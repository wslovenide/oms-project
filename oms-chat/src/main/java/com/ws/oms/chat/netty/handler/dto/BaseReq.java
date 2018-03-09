package com.ws.oms.chat.netty.handler.dto;

import java.io.Serializable;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-03-09 15:13
 */
public class BaseReq implements Serializable {

    private String command;         // 执行的命令
    private String sessionId;       // 当前用户的id

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BaseReq{");
        sb.append("command='").append(command).append('\'');
        sb.append(", sessionId='").append(sessionId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
