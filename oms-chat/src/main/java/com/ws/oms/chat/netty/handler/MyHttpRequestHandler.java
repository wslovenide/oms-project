package com.ws.oms.chat.netty.handler;

import com.alibaba.fastjson.JSON;
import com.ws.oms.chat.netty.handler.dto.ChatMsg;
import com.ws.oms.chat.netty.service.ServiceContext;
import com.ws.oms.chat.netty.service.api.IChannelService;
import com.ws.oms.chat.netty.util.Constant;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.ClientCookieEncoder;
import io.netty.handler.codec.http.cookie.*;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.DefaultCookie;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * Created by gongmei on 2018/3/5.
 */
public class MyHttpRequestHandler extends ChannelInboundHandlerAdapter {

    private String queryOnlineCountUrl = "/query/onlineCount";

    private ServiceContext serviceContext;

    public MyHttpRequestHandler(ServiceContext serviceContext){
        this.serviceContext = serviceContext;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest){
            HttpRequest request = (HttpRequest)msg;

            String url = request.uri();
            System.out.println("请求的 url = " + url + " , " + ctx.channel());
//
//            Iterator<Map.Entry<String, String>> iterator = request.headers().iteratorAsString();
//            while (iterator.hasNext()){
//                Map.Entry<String, String> entry = iterator.next();
//                System.out.println(entry.getKey() + " = " + entry.getValue());
//            }
//            System.out.println("========================");

            if (request.uri().endsWith(queryOnlineCountUrl)){
                ChatMsg chatMsg = new ChatMsg(serviceContext.getOnlineChannelMap().size()+"");
                chatMsg.setMsgType(Constant.MSG_ONLINE_OFFLINE);
                String jsonMsg = JSON.toJSONString(chatMsg);

                ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
                byteBuf.writeBytes(jsonMsg.getBytes(Charset.forName("utf-8")));

                DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,byteBuf);
                response.headers().set(HttpHeaderNames.CONTENT_TYPE,"application/json");
                response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
                response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN,request.headers().get("Origin"));
                response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_CREDENTIALS,"true");

                String cookie = request.headers().get("Cookie");
                System.out.println("请求查询的channel : " + ctx.channel() + " , cookie : " + cookie);

                if (cookie == null || "".equals(cookie.trim())){
                    Cookie newCookie = new DefaultCookie("sessionid",UUID.randomUUID().toString());
                    newCookie.setHttpOnly(true);
                    newCookie.setMaxAge(60 * 60 * 2400);
                    response.headers().set("Set-Cookie", newCookie);
                }
                ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
                return;
            }
        }
        super.channelRead(ctx, msg);
    }
}
