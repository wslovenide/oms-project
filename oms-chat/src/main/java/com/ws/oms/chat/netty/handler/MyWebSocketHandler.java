package com.ws.oms.chat.netty.handler;

import com.alibaba.fastjson.JSON;
import com.ws.oms.chat.netty.handler.dto.ChatMsg;
import com.ws.oms.chat.netty.service.api.IChannelService;
import com.ws.oms.chat.netty.util.Constant;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;


/**
 * Created by gongmei on 2018/3/5.
 */
public class MyWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private IChannelService channelService;

    public MyWebSocketHandler(IChannelService channelService){
        this.channelService = channelService;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE){
            channelService.add(ctx.channel());
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        ChatMsg chatMsg = new ChatMsg(msg.text());
        chatMsg.setMsgType(Constant.MSG_CHAT);
        System.out.println("消息:"+chatMsg);
        channelService.getOnlineChannelMap().forEach((key,value) -> {
            chatMsg.setSelf(key == ctx.channel().id());
            chatMsg.setNickName(ctx.channel().id().toString());
            value.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(chatMsg)));
        });
    }



}
