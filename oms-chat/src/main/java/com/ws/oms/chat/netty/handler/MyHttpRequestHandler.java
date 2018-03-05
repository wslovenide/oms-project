package com.ws.oms.chat.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;

import java.net.InetSocketAddress;

/**
 * Created by gongmei on 2018/3/5.
 */
public class MyHttpRequestHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest){
            HttpRequest request = (HttpRequest)msg;

            String clientIP  = request.headers().get("X-Forwarded-For");
            if (clientIP == null) {
                InetSocketAddress insocket = (InetSocketAddress) ctx.channel()
                        .remoteAddress();
                clientIP = insocket.getAddress().getHostAddress();
            }

            String cookie = request.headers().get("Cookie");
            System.out.println(cookie);

            System.out.println("客户端ip为: " + clientIP);
        }
        super.channelRead(ctx, msg);
    }
}
