package com.ws.oms.chat.netty.service.channel.impl;

import com.alibaba.fastjson.JSON;
import com.ws.oms.chat.netty.dao.usergroup.IUserGroupService;
import com.ws.oms.chat.netty.handler.dto.ChatMsgItemResp;
import com.ws.oms.chat.netty.handler.dto.ChatMsgResp;
import com.ws.oms.chat.netty.service.channel.IChannelService;
import com.ws.oms.chat.netty.util.Constant;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-03-06 13:13
 */
public class ChannelService implements IChannelService {

    // 每个group里的channel
    private static Map<String,List<Channel>> groupChannelMap = new ConcurrentHashMap<>(512);

    // 当前channel和sessionId 映射
    private static Map<Channel,String> channelSessionMap = new ConcurrentHashMap<>(512);

    private IUserGroupService userGroupService;

    public ChannelService(IUserGroupService userGroupService){
        this.userGroupService = userGroupService;
    }

    @Override
    public void attach(Channel channel, String sessionId) {

        channelSessionMap.put(channel,sessionId);

        channel.closeFuture().addListener(future -> remove(channel));

        // 保存 session 到公共的房间
        userGroupService.save(Constant.PUBLIC_GROUP_ID,sessionId);

        // 保存 channel 到 所有的房间
        activeSessionGroup(channel, sessionId);

        ChatMsgResp resp = new ChatMsgResp();
        resp.setCommand(Constant.ONLINE_EVENT);
        resp.setSessionId(sessionId);

        // 广播消息
        broadcastMessage(sessionId,resp,channel);
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

        Set<String> groupList = userGroupService.getGroupList(sessionId);
        if (groupList != null){
            groupList.forEach(group -> {
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
        }
        // 广播消息
        broadcastMessage(sessionId,resp,channel);
        return channel;
    }

    @Override
    public void broadcastMessage(String sessionId, ChatMsgResp chatMsgResp,Channel current) {
        // 根据sessionId 获取所在的group列表，并通知下线
        Set<String> set = userGroupService.getGroupList(sessionId);
        if (set != null && set.size() > 0){
            set.forEach(groupId -> {
                // 这个用户所在每一个组均发送消息
                List<Channel> channelList = groupChannelMap.get(groupId);
                if (channelList != null && channelList.size() > 0){
                    channelList.forEach(ch -> {
                        ChatMsgItemResp itemResp = new ChatMsgItemResp();
                        itemResp.setMsg(channelList.size() + "");
                        itemResp.setGroupId(groupId);
                        itemResp.setNickName(sessionId.split("-")[0]);
                        chatMsgResp.setCount(channelList.size());
                        chatMsgResp.setMsg(itemResp);
                        TextWebSocketFrame respMsg = new TextWebSocketFrame(JSON.toJSONString(chatMsgResp));
                        if (ch != current){
                            if (ch.isOpen() && ch.isActive() && ch.isWritable()){
                                ch.writeAndFlush(respMsg);
                            }
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
                if (ch.isOpen() && ch.isWritable()){
                    String chSessionId = channelSessionMap.get(ch);
                    itemSelf.setSelf(chSessionId.equals(itemSelf.getSessionId()));
                    chatMsgResp.setMsg(itemSelf);
                    ch.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(chatMsgResp)));
                }
            });
        }
    }

    @Override
    public int getOnlineNumber(String groupId) {
        List<Channel> channels = groupChannelMap.get(groupId);
        return channels == null ? 1 : channels.size();
    }

    @Override
    public boolean containsSessionId(String sessionId) {
        return this.userGroupService.containsSessionId(sessionId);
    }

    @Override
    public void attachToChannel(String groupId,String... sessionArray) {
        List<Channel> list = new ArrayList<>(sessionArray.length);
        channelSessionMap.forEach((key,value) -> {
            for (String sessionId : sessionArray){
                if (sessionId.equals(value)){
                    list.add(key);
                }
            }
        });
        groupChannelMap.put(groupId,list);
    }

    /**
     *  当前用户所有组的channel需要更新为当前的channel
     */
    private void activeSessionGroup(Channel channel, String sessionId){
        Set<String> groupList = userGroupService.getGroupList(sessionId);
        if (groupList != null && !groupList.isEmpty()){
            groupList.forEach(groupId -> {
                List<Channel> channelList = groupChannelMap.get(groupId);
                if (channelList == null){
                    channelList = new ArrayList<>();
                    groupChannelMap.put(groupId,channelList);
                }
                channelList.add(channel);
            });
        }
    }
}
