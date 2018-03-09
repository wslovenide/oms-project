package com.ws.oms.chat.netty.service;

import com.alibaba.fastjson.JSON;
import com.ws.oms.chat.netty.handler.dto.ChatMsgItemResp;
import com.ws.oms.chat.netty.handler.dto.ChatMsgResp;
import com.ws.oms.chat.netty.service.api.IChannelService;
import com.ws.oms.chat.netty.util.Constant;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-03-06 13:13
 */
public class ChannelService implements IChannelService {

    private final Object groupChannelLock = new Object();

    private final Object groupSessionLock = new Object();

    // 每个group里的channel
    private static Map<String,List<Channel>> groupChannelMap = new ConcurrentHashMap<>(512);

    // 每个group里面有多少人
    private static Map<String,Set<String>> groupSessionMap = new ConcurrentHashMap<>(512);

    // 当前channel和sessionId 映射
    private static Map<Channel,String> channelSessionMap = new ConcurrentHashMap<>(512);

    // 每个用户对应的group列表
    private static Map<String,Set<String>> sessionGroupMap = new ConcurrentHashMap<>(512);

    private ExecutorService executorService = Executors.newFixedThreadPool(5);


    @Override
    public void attach(Channel channel, String sessionId) {

        channel.closeFuture().addListener(future -> remove(channel));

        // 保存channel 到公共的房间
        getOrInitGroupChannelMap(Constant.PUBLIC_GROUP_ID).add(channel);

        // 保存session 到公共的房间
        getOrInitGroupSessionMap(Constant.PUBLIC_GROUP_ID).add(sessionId);

        // 保存session 所在的group
        getOrInitSessionGroupMap(sessionId).add(Constant.PUBLIC_GROUP_ID);

        ChatMsgResp resp = new ChatMsgResp();
        resp.setCommand(Constant.ONLINE_EVENT);
        resp.setSessionId(sessionId);
        resp.setMsg(sessionId);

        // 广播消息
        broadcastMessage(sessionId,resp);
    }

    @Override
    public String getSessionId(Channel channel) {
        return channelSessionMap.get(channel);
    }

    @Override
    public Channel remove(Channel channel) {
        // 下线或关闭时删除当前channel
        String sessionId = channelSessionMap.remove(channel);

        ChatMsgResp resp = new ChatMsgResp();
        resp.setCommand(Constant.OFFLINE_EVENT);
        resp.setSessionId(sessionId);
        resp.setMsg(sessionId);

        executorService.submit(() -> {
            sessionGroupMap.get(sessionId).forEach(group -> {
                Iterator<Channel> iterator = groupChannelMap.get(group).iterator();
                while (iterator.hasNext()){
                    Channel next = iterator.next();
                    if (!next.isOpen() || !next.isActive()){
                        iterator.remove();
                        continue;
                    }
                    if (next == channel){
                        iterator.remove();
                        break;
                    }
                }
            });
        });
        // 广播消息
        broadcastMessage(sessionId,resp);
        return channel;
    }

    @Override
    public void broadcastMessage(String sessionId, ChatMsgResp chatMsgResp) {
        // 根据sessionId 获取所在的group列表，并通知下线
        Set<String> set = sessionGroupMap.get(sessionId);
        if (set != null && set.size() > 0){
            TextWebSocketFrame respMsg = new TextWebSocketFrame(JSON.toJSONString(chatMsgResp));
            set.forEach(groupId -> {
                // 这个用户所在每一个组均发送消息
                List<Channel> channelList = groupChannelMap.get(groupId);
                if (channelList != null && channelList.size() > 0){
                    channelList.forEach(ch -> {
                        if (ch.isOpen() && ch.isActive() && ch.isWritable()){
                            ch.writeAndFlush(respMsg);
                        }
                    });
                }
            });
        }
    }

    @Override
    public void broadcastMessage(String sessionId, String groupId, ChatMsgResp chatMsgResp) {
        List<Channel> channelList = groupChannelMap.get(groupId);
        if (channelList != null && channelList.size() > 0){
            channelList.forEach(ch -> {
                ChatMsgItemResp itemSelf = (ChatMsgItemResp)chatMsgResp.getMsg();
                if (ch.isOpen() && ch.isActive() && ch.isWritable()){
                    String chSessionId = channelSessionMap.get(ch);
                    itemSelf.setSelf(chSessionId.equals(itemSelf.getSessionId()));
                    chatMsgResp.setMsg(itemSelf);
                    ch.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(chatMsgResp)));
                }
            });
        }
    }

    private List<Channel> getOrInitGroupChannelMap(String groupId){
        List<Channel> channelList = groupChannelMap.get(groupId);
        if (channelList == null){
            synchronized (groupChannelLock){
                channelList = new LinkedList<>();
                groupChannelMap.put(Constant.PUBLIC_GROUP_ID,channelList);
            }
        }
        return channelList;
    }

    private Set<String> getOrInitGroupSessionMap(String groupId){
        Set<String> sessionList = groupSessionMap.get(groupId);
        if (sessionList == null){
            synchronized (groupSessionLock){
                sessionList = new HashSet<>();
                groupSessionMap.put(Constant.PUBLIC_GROUP_ID,sessionList);
            }
        }
        return sessionList;
    }

    private Set<String> getOrInitSessionGroupMap(String sessionId){
        Set<String> groupSet = sessionGroupMap.get(sessionId);
        if (groupSet == null){
            groupSet = new HashSet<>(64);
            sessionGroupMap.put(sessionId,groupSet);
        }
        return groupSet;
    }

}
