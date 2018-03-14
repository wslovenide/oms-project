package com.ws.oms.chat.netty.service.msg.impl;

import com.alibaba.fastjson.JSON;
import com.ws.oms.chat.netty.handler.dto.ChatMsgItemResp;
import com.ws.oms.chat.netty.service.msg.IChatMsgDao;
import com.ws.oms.chat.netty.util.RedisUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by gongmei on 2018/3/12.
 */
public class ChatMsgRedisDao implements IChatMsgDao {

    private static String CHAT_MSG_PREFIX = "chat_msg_prefix:";

    @Override
    public void save(ChatMsgItemResp itemR) {
        RedisUtil.doInJedis(redis -> {
            String key = CHAT_MSG_PREFIX + itemR.getGroupId();
            redis.rpush(key,JSON.toJSONString(itemR));
            return null;
        });
    }

    @Override
    public List<ChatMsgItemResp> getChatMsgByGroup(String groupId, String sessionId) {
        List<ChatMsgItemResp> respList = new LinkedList<>();
        RedisUtil.doInJedis(redis -> {
            String key = CHAT_MSG_PREFIX + groupId;
            List<String> chatList = redis.lrange(key, 0, -1);

            for (String chatMsg : chatList){
                ChatMsgItemResp itemResp = JSON.parseObject(chatMsg, ChatMsgItemResp.class);
                itemResp.setSelf(itemResp.getSessionId().equals(sessionId));
                respList.add(itemResp);
            }
            return null;
        });
        return respList;
    }
}
