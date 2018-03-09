package com.ws.oms.chat.netty.service;

import com.ws.oms.chat.netty.handler.dto.BaseReq;
import com.ws.oms.chat.netty.handler.dto.ChatMsg;
import com.ws.oms.chat.netty.handler.dto.ChatMsgItemResp;
import com.ws.oms.chat.netty.service.api.IChannelService;
import com.ws.oms.chat.netty.service.api.IChatMsgDao;
import com.ws.oms.chat.netty.service.api.IChatMsgService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;

import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-03-07 17:09
 */
public class ServiceContext implements IChannelService,IChatMsgService,IChatMsgDao{

    private IChannelService channelService;
    private IChatMsgDao chatMsgDao;
    private IChatMsgService chatMsgService;

    public ServiceContext(){
        chatMsgDao = new ChatMsgDao();
        channelService = new ChannelService();
        chatMsgService = new ChatMsgService(this);
    }

    @Override
    public void attach(Channel channelId, String sesionid) {
        channelService.attach(channelId,sesionid);
    }

    @Override
    public String getSessionId(ChannelId channelId) {
        return channelService.getSessionId(channelId);
    }

    @Override
    public Channel remove(Channel channel) {
        return channelService.remove(channel);
    }

    @Override
    public Map<ChannelId, Channel> getOnlineChannelMap() {
        return channelService.getOnlineChannelMap();
    }

    @Override
    public void handleMessage(ChannelHandlerContext ctx, String msg) {
        chatMsgService.handleMessage(ctx,msg);
    }

    @Override
    public void save(ChatMsg chatMsg) {

    }

    @Override
    public List<ChatMsg> getChatMsg() {
        return null;
    }

    @Override
    public List<ChatMsgItemResp> getChatMsgByGroup(String groupId, String sessionId) {
        return null;
    }
}
