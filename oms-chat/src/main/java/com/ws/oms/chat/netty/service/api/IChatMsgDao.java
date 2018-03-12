package com.ws.oms.chat.netty.service.api;

import com.ws.oms.chat.netty.handler.dto.ChatMsgItemResp;

import java.util.List;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-03-07 16:24
 */
public interface IChatMsgDao {


    void save(ChatMsgItemResp itemR);


    List<ChatMsgItemResp> getChatMsgByGroup(String groupId, String sessionId);

}
