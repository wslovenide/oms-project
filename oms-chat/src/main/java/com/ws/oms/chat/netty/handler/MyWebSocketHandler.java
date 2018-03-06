package com.ws.oms.chat.netty.handler;

import com.alibaba.fastjson.JSON;
import com.ws.oms.chat.netty.handler.dto.ChatMsg;
import com.ws.oms.chat.netty.util.Constant;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by gongmei on 2018/3/5.
 */
public class MyWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    public static Map<ChannelId,Channel> channelMap = new ConcurrentHashMap<>(512);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel());
        channelMap.put(ctx.channel().id(),ctx.channel());
        System.out.println("有人上线，在线人数 ： " + channelMap.size());

        onlineOfflineNotify();
        super.channelActive(ctx);
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        channelMap.remove(ctx.channel().id());
        System.out.println("有人下线，在线人数 ： " + channelMap.size());

        onlineOfflineNotify();
        super.channelInactive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        ChatMsg chatMsg = new ChatMsg(msg.text());
        chatMsg.setMsgType(Constant.MSG_CHAT);
        System.out.println(chatMsg);
        channelMap.forEach((key,value) -> {
            chatMsg.setSelf(key == ctx.channel().id());
            chatMsg.setNickName(ctx.channel().id().toString());
            value.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(chatMsg)));
        });
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
