package com.ws.oms.chat.netty.handler;

import com.alibaba.fastjson.JSON;
import com.ws.oms.chat.netty.handler.dto.ChatMsgResp;
import com.ws.oms.chat.netty.service.ServiceContext;
import com.ws.oms.chat.netty.service.room.IRoomService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import java.nio.charset.Charset;

/**
 * Created by gongmei on 2018/3/14.
 */
public class MyHttpRequestHandler extends ChannelInboundHandlerAdapter {

    private static final String CREATE_CHAT_ROOM_URL = "/room/create";


    private IRoomService roomService;

    public MyHttpRequestHandler(IRoomService roomService){
        this.roomService = roomService;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest){
            FullHttpRequest request = (FullHttpRequest)msg;
            String uri = request.uri();
            System.out.println("请求的URL为 ： " + uri);

            String requestBody = request.content().toString(Charset.forName("utf-8"));
            System.out.println("请求的数据为: " + requestBody);
            Object result = null;
            switch (uri){
                case CREATE_CHAT_ROOM_URL:
                    if (!validateRequest(ctx,request)){
                        return;
                    }
                    result = roomService.createChatRoom(requestBody);
                    break;
            }
            if (result != null){
                response(ctx,result);
                return;
            }
        }
        super.channelRead(ctx, msg);
    }

    private void response(ChannelHandlerContext ctx, Object message){
        String msg = JSON.toJSONString(message);
        System.out.println("返回给前段的数据为: " + msg);
        ByteBuf byteBuf = ctx.alloc().buffer().writeBytes(msg.getBytes(Charset.forName("UTF-8")));

        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,byteBuf);
        response.headers().add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
        response.headers().add(HttpHeaderNames.CONTENT_LENGTH,response.content().readableBytes());
        response.headers().add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN,"*");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private boolean validateRequest(ChannelHandlerContext ctx,FullHttpRequest request){
        String method = request.method().name();
        String errorMessage = null;
        if (!method.toLowerCase().equals("post")){
            errorMessage = "请使用post方法!";
        }
        if (request.content().readableBytes() < 1){
            errorMessage = "请求内容为空!";
        }
        if (errorMessage != null){
            ChatMsgResp resp = new ChatMsgResp();
            resp.setSuccess(false);
            resp.setMsg(errorMessage);
            response(ctx,resp);
        }
        return errorMessage == null;
    }



}
