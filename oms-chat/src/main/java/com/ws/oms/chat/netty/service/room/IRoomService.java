package com.ws.oms.chat.netty.service.room;

import com.ws.oms.chat.netty.handler.dto.ChatMsgResp;

/**
 * Created by gongmei on 2018/3/14.
 */
public interface IRoomService {


    ChatMsgResp createChatRoom(String requestBody);



}
