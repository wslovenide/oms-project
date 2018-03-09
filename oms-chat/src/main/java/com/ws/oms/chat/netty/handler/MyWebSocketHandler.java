package com.ws.oms.chat.netty.handler;

import com.alibaba.fastjson.JSON;
import com.ws.oms.chat.netty.handler.dto.BaseReq;
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

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println(" message: " + msg.text() + " ,  remote:" + ctx.channel().remoteAddress());

        serviceContext.handleMessage(ctx,msg.text());

    }



}
