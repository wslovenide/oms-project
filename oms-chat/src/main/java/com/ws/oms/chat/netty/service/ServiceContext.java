package com.ws.oms.chat.netty.service;

import com.ws.oms.chat.netty.handler.dto.ChatMsg;
import com.ws.oms.chat.netty.service.api.IChannelService;
import com.ws.oms.chat.netty.service.api.IChatMsgDao;
import io.netty.channel.Channel;
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
public class ServiceContext implements IChannelService, IChatMsgDao{

    private IChannelService channelService;
    private IChatMsgDao chatMsgDao;

    public ServiceContext(){
        channelService = new ChannelService();
        chatMsgDao = new ChatMsgDao();
    }
//
//    @Override
//    public void add(Channel channel) {
//        channelService.add(channel);
//    }

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
    public void save(ChatMsg chatMsg) {
        chatMsgDao.save(chatMsg);
    }

    @Override
    public List<ChatMsg> getChatMsg() {
        return chatMsgDao.getChatMsg();
    }
}
