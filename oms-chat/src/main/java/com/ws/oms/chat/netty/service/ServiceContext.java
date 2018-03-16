package com.ws.oms.chat.netty.service;

import com.ws.oms.chat.netty.dao.msg.IChatMsgDao;
import com.ws.oms.chat.netty.dao.msg.impl.ChatMsgMapDao;
import com.ws.oms.chat.netty.dao.usergroup.IUserGroupService;
import com.ws.oms.chat.netty.dao.usergroup.impl.UserGroupMapService;
import com.ws.oms.chat.netty.handler.dto.ChatMsgItemResp;
import com.ws.oms.chat.netty.handler.dto.ChatMsgResp;
import com.ws.oms.chat.netty.handler.dto.OnlineInfoResp;
import com.ws.oms.chat.netty.service.channel.IChannelService;
import com.ws.oms.chat.netty.service.channel.impl.ChannelService;
import com.ws.oms.chat.netty.service.msg.IChatMsgService;
import com.ws.oms.chat.netty.service.msg.impl.ChatMsgService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;
import java.util.Set;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-03-07 17:09
 */
public class ServiceContext implements IChannelService,IChatMsgService,IChatMsgDao,IUserGroupService {

    private IChannelService channelService;
    private IChatMsgDao chatMsgDao;
    private IChatMsgService chatMsgService;
    private IUserGroupService userGroupService;

    public ServiceContext(){
        chatMsgDao = new ChatMsgMapDao();
//        chatMsgDao = new ChatMsgRedisDao();
//        userGroupService = new UserGroupRedisService();
        userGroupService = new UserGroupMapService();
        channelService = new ChannelService(userGroupService);
        chatMsgService = new ChatMsgService(this);
    }

    @Override
    public void attach(Channel channelId, String sesionid) {
        channelService.attach(channelId,sesionid);
    }

    @Override
    public String getSessionId(Channel channelId) {
        return channelService.getSessionId(channelId);
    }

    @Override
    public Channel remove(Channel channel) {
        return channelService.remove(channel);
    }

    @Override
    public void broadcastMessage(String sessionId, ChatMsgResp chatMsgResp,Channel channel) {
        channelService.broadcastMessage(sessionId,chatMsgResp,channel);
    }

    @Override
    public void broadcastMessage(String sessionId, String groupId, ChatMsgResp chatMsgResp) {
        channelService.broadcastMessage(sessionId,groupId,chatMsgResp);
    }

    @Override
    public int getOnlineNumber(String groupId) {
        return channelService.getOnlineNumber(groupId);
    }

    @Override
    public Set<String> getOnlineSessionIds() {
        return channelService.getOnlineSessionIds();
    }

    @Override
    public List<OnlineInfoResp> getOnlineInfoList() {
        return channelService.getOnlineInfoList();
    }

    @Override
    public void save(String groupId, String sessionId) {
        userGroupService.save(groupId,sessionId);
    }

    @Override
    public Set<String> getSessionList(String groupId) {
        return userGroupService.getSessionList(groupId);
    }

    @Override
    public Set<String> getGroupList(String sessionId) {
        return userGroupService.getGroupList(sessionId);
    }

    @Override
    public boolean containsSessionId(String sessionId) {
        return channelService.containsSessionId(sessionId);
    }

    @Override
    public boolean containsGroupId(String groupId) {
        return userGroupService.containsGroupId(groupId);
    }

    @Override
    public void attachToChannel(String groupId, String... sessionArray) {
        channelService.attachToChannel(groupId,sessionArray);
    }

    @Override
    public void handleMessage(ChannelHandlerContext ctx, String msg) {
        chatMsgService.handleMessage(ctx,msg);
    }


    @Override
    public void save(ChatMsgItemResp itemR) {
        chatMsgDao.save(itemR);
    }


    @Override
    public List<ChatMsgItemResp> getChatMsgByGroup(String groupId, String sessionId) {
        return chatMsgDao.getChatMsgByGroup(groupId,sessionId);
    }
}
