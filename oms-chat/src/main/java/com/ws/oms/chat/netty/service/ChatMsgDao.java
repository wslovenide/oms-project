package com.ws.oms.chat.netty.service;

import com.ws.oms.chat.netty.handler.dto.ChatMsg;
import com.ws.oms.chat.netty.handler.dto.ChatMsgItemResp;
import com.ws.oms.chat.netty.service.api.IChatMsgDao;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-03-07 16:25
 */
public class ChatMsgDao implements IChatMsgDao {

    private List<ChatMsg> msgList = new LinkedList<>();

    @Override
    public void save(ChatMsg chatMsg) {
        msgList.add(chatMsg);
        if (msgList.size() > 2000){
            msgList.remove(0);
        }
    }

    @Override
    public List<ChatMsg> getChatMsg() {
        return msgList;
    }

    @Override
    public List<ChatMsgItemResp> getChatMsgByGroup(String groupId, String sessionId) {
        return null;
    }
}
