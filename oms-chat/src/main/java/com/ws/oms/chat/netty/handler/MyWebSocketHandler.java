package com.ws.oms.chat.netty.handler;

import com.alibaba.fastjson.JSON;
import com.ws.oms.chat.netty.handler.dto.ChatMsg;
import com.ws.oms.chat.netty.service.ServiceContext;
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

    private ServiceContext serviceContext;

    public MyWebSocketHandler(ServiceContext serviceContext){
        this.serviceContext = serviceContext;
    }

//    @Override
//    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE){
//            serviceContext.add(ctx.channel());
//        }
//    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println(" message: " + msg.text() + " ,  remote:" + ctx.channel().remoteAddress() + " ,  local :" + ctx.channel().localAddress());
        ChatMsg fromMsg = JSON.parseObject(msg.text(),ChatMsg.class);
        System.out.println(fromMsg);
        if (fromMsg.getMsgType().equals(Constant.MSG_WEB_SOCKET_INIT)){
            if (fromMsg.getSessionId() == null || fromMsg.getSessionId().equals("")){
                // sessionid 为空
                fromMsg.setSessionId(UUID.randomUUID().toString());
            }
            // 查询聊天记录
            List<ChatMsg> chatMsgHis = serviceContext.getChatMsg();
            if (!chatMsgHis.isEmpty()){
                List<ChatMsg> nowList = new ArrayList<>(chatMsgHis.size());
                for (ChatMsg chatMsg : chatMsgHis){
                    ChatMsg newChat = chatMsg.clone();

                    newChat.setNickName(newChat.getSessionId().split("-")[0]);
                    newChat.setSelf(newChat.getSessionId().equals(fromMsg.getSessionId()));
                    nowList.add(newChat);
                }
                fromMsg.setMsg(nowList);
            }
            ctx.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(fromMsg)));
            serviceContext.attach(ctx.channel(),fromMsg.getSessionId());
        }else {
            ChatMsg chatMsg = new ChatMsg(fromMsg.getMsg().toString());
            chatMsg.setMsgType(Constant.MSG_CHAT);
            chatMsg.setSessionId(serviceContext.getSessionId(ctx.channel().id()));
            serviceContext.getOnlineChannelMap().forEach((key,value) -> {
                String sessionId = serviceContext.getSessionId(key);
                chatMsg.setSelf(sessionId.equals(chatMsg.getSessionId()));
                chatMsg.setNickName(chatMsg.getSessionId().split("-")[0]);
                value.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(chatMsg)));
            });

            serviceContext.save(chatMsg);
        }
    }



}
