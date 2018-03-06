package com.ws.oms.chat.netty.handler;

import com.alibaba.fastjson.JSON;
import com.ws.oms.chat.netty.handler.dto.ChatMsg;
import com.ws.oms.chat.netty.service.api.IChannelService;
import com.ws.oms.chat.netty.util.Constant;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

import java.nio.charset.Charset;

/**
 * Created by gongmei on 2018/3/5.
 */
public class MyHttpRequestHandler extends ChannelInboundHandlerAdapter {

    private String queryOnlineCountUrl = "/query/onlineCount";

    private IChannelService channelService;

    public MyHttpRequestHandler(IChannelService channelService){
        this.channelService = channelService;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest){
            HttpRequest request = (HttpRequest)msg;

            String url = request.uri();
            System.out.println("请求的 url = " + url);

            if (request.uri().endsWith(queryOnlineCountUrl)){
                ChatMsg chatMsg = new ChatMsg(channelService.getOnlineChannelMap().size()+"");
                chatMsg.setMsgType(Constant.MSG_ONLINE_OFFLINE);
                String jsonMsg = JSON.toJSONString(chatMsg);

                ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
                byteBuf.writeBytes(jsonMsg.getBytes(Charset.forName("utf-8")));

                DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,byteBuf);
                response.headers().set(HttpHeaders.Names.CONTENT_TYPE,"application/json");
                response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, response.content().readableBytes());
                response.headers().set(HttpHeaders.Names.ACCESS_CONTROL_ALLOW_ORIGIN,"*");
                ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
                return;
            }
        }
        super.channelRead(ctx, msg);
    }
}
