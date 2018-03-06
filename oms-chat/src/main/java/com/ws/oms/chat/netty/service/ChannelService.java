package com.ws.oms.chat.netty.service;

import com.alibaba.fastjson.JSON;
import com.ws.oms.chat.netty.handler.dto.ChatMsg;
import com.ws.oms.chat.netty.service.api.IChannelService;
import com.ws.oms.chat.netty.util.Constant;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.Map;
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

    private static Map<ChannelId,Channel> channelMap = new ConcurrentHashMap<>(512);

    @Override
    public void add(Channel channel) {
        channelMap.put(channel.id(),channel);
        channel.closeFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                remove(channel);
            }
        });
        onlineOfflineNotify();
    }

    @Override
    public Channel remove(Channel channel) {
        channelMap.remove(channel.id());
        onlineOfflineNotify();
        return channel;
    }

    @Override
    public Map<ChannelId, Channel> getOnlineChannelMap() {
        return channelMap;
    }

    private void onlineOfflineNotify(){
        if (channelMap.isEmpty()){
            System.out.println("当前无人在线，无需发送通知消息!");
            return;
        }
        System.out.println("当前在线的人数为: " + channelMap.size());
        ChatMsg chatMsg = new ChatMsg(String.valueOf(channelMap.size()));
        chatMsg.setMsgType(Constant.MSG_ONLINE_OFFLINE);

        String jsonMsg = JSON.toJSONString(chatMsg);

        channelMap.forEach((key,value) -> {
            value.writeAndFlush(new TextWebSocketFrame(jsonMsg));
        });
    }
}
