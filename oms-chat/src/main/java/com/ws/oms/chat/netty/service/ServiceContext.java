package com.ws.oms.chat.netty.service;

import com.ws.oms.chat.netty.handler.dto.ChatMsgItemResp;
import com.ws.oms.chat.netty.handler.dto.ChatMsgResp;
import com.ws.oms.chat.netty.service.channel.IChannelService;
import com.ws.oms.chat.netty.service.channel.impl.ChannelService;
import com.ws.oms.chat.netty.service.msg.IChatMsgDao;
import com.ws.oms.chat.netty.service.msg.IChatMsgService;
import com.ws.oms.chat.netty.service.msg.impl.ChatMsgMapDao;
import com.ws.oms.chat.netty.service.msg.impl.ChatMsgService;
import com.ws.oms.chat.netty.service.usergroup.impl.UserGroupMapService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-03-07 17:09
 */
public class ServiceContext implements IChannelService,IChatMsgService,IChatMsgDao {

    private IChannelService channelService;
    private IChatMsgDao chatMsgDao;
    private IChatMsgService chatMsgService;

    public ServiceContext(){
        chatMsgDao = new ChatMsgMapDao();
//        chatMsgDao = new ChatMsgRedisDao();
        channelService = new ChannelService(new UserGroupMapService());
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
    public boolean containsSessionId(String sessionId) {
        return channelService.containsSessionId(sessionId);
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
