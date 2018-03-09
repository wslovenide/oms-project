package com.ws.oms.chat.netty.handler;

import com.ws.oms.chat.netty.service.ServiceContext;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;


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
