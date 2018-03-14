package com.ws.oms.chat.netty.dao.msg.impl;

import com.ws.oms.chat.netty.dao.msg.IChatMsgDao;
import com.ws.oms.chat.netty.handler.dto.ChatMsgItemResp;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-03-07 16:25
 */
public class ChatMsgMapDao implements IChatMsgDao {


    private Map<String,List<ChatMsgItemResp>> groupMessageMap = new HashMap<>(512);


    @Override
    public void save(ChatMsgItemResp itemR) {
        List<ChatMsgItemResp> list = groupMessageMap.get(itemR.getGroupId());
        if (list == null){
            list = new LinkedList<>();
            groupMessageMap.put(itemR.getGroupId(),list);
        }
        list.add(itemR);
        if (list.size() > 2000){
            list.remove(0);
        }
    }


    @Override
    public List<ChatMsgItemResp> getChatMsgByGroup(String groupId, String sessionId) {
        List<ChatMsgItemResp> respList = groupMessageMap.get(groupId);
        if (respList != null && respList.size() > 0){
            List<ChatMsgItemResp> collect = respList.parallelStream().map(item -> {
                ChatMsgItemResp newItem = item.clone();
                newItem.setSelf(newItem.getSessionId().equals(sessionId));
                return newItem;
            }).collect(Collectors.toList());
            return collect;
        }
        return null;
    }
}
