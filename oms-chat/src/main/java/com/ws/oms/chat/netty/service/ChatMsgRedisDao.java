package com.ws.oms.chat.netty.service;

import com.alibaba.fastjson.JSON;
import com.ws.oms.chat.netty.handler.dto.ChatMsgItemResp;
import com.ws.oms.chat.netty.service.api.IChatMsgDao;
import com.ws.oms.chat.netty.util.RedisUtil;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gongmei on 2018/3/12.
 */
public class ChatMsgRedisDao implements IChatMsgDao {

    private static String CHAT_MSG_PREFIX = "chat_msg_prefix:";


    @Override
    public void save(ChatMsgItemResp itemR) {
        Jedis jedis = RedisUtil.getResource();
        try {
            String groupId = CHAT_MSG_PREFIX + itemR.getGroupId();
            System.out.println("save key = " + groupId);
            jedis.rpush(groupId,JSON.toJSONString(itemR));
//            jedis.ltrim(groupId,-1,2000);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            RedisUtil.close(jedis);
        }

    }

    @Override
    public List<ChatMsgItemResp> getChatMsgByGroup(String groupId, String sessionId) {
        Jedis jedis = RedisUtil.getResource();
        try {
            String key = CHAT_MSG_PREFIX + groupId;
            System.out.println("query key = " + key);
            List<String> chatList = jedis.lrange(key, 0, -1);

            List<ChatMsgItemResp> respList = new ArrayList<>(chatList.size());
            for (String chatMsg : chatList){
                ChatMsgItemResp itemResp = JSON.parseObject(chatMsg, ChatMsgItemResp.class);

                itemResp.setSelf(itemResp.getSessionId().equals(sessionId));
                respList.add(itemResp);
            }
            return respList;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            RedisUtil.close(jedis);
        }
        return new ArrayList<>();
    }
}
