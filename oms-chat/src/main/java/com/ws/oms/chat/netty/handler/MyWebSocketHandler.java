package com.ws.oms.chat.netty.handler;

import com.alibaba.fastjson.JSON;
import com.ws.oms.chat.netty.handler.dto.ChatMsg;
import com.ws.oms.chat.netty.service.api.IChannelService;
import com.ws.oms.chat.netty.util.Constant;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Created by gongmei on 2018/3/5.
 */
public class MyWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private List<ChatMsg> msgList = new ArrayList<>();


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
        ChatMsg fromMsg = JSON.parseObject(msg.text(),ChatMsg.class);
        System.out.println(fromMsg);
        if (fromMsg.getMsgType().equals(Constant.MSG_WEB_SOCKET_INIT)){
            if (fromMsg.getMsg() == null || fromMsg.getMsg().equals("")){
                // sessionid 为空
                fromMsg.setMsg(UUID.randomUUID().toString());
                ctx.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(fromMsg)));
            }
            channelService.attach(ctx.channel().id(),fromMsg.getMsg().toString());
        }else {
            ChatMsg chatMsg = new ChatMsg(fromMsg.getMsg().toString());
            chatMsg.setMsgType(Constant.MSG_CHAT);
            chatMsg.setSessionId(channelService.getSessionId(ctx.channel().id()));
            channelService.getOnlineChannelMap().forEach((key,value) -> {
                String sessionId = channelService.getSessionId(key);
                chatMsg.setSelf(sessionId.equals(chatMsg.getSessionId()));
                chatMsg.setNickName(sessionId.split("-")[0]);
                value.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(chatMsg)));
            });
        }
    }



}
