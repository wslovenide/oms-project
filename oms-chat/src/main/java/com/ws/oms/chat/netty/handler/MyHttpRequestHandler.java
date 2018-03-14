package com.ws.oms.chat.netty.handler;

import com.alibaba.fastjson.JSON;
import com.ws.oms.chat.netty.handler.dto.ChatMsgResp;
import com.ws.oms.chat.netty.handler.dto.group.CreateGroupReq;
import com.ws.oms.chat.netty.service.ServiceContext;
import com.ws.oms.chat.netty.util.Constant;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;
import java.util.UUID;

/**
 * Created by gongmei on 2018/3/14.
 */
public class MyHttpRequestHandler extends ChannelInboundHandlerAdapter {

    private static final String REQUEST_URL = "/room/create";

    private ServiceContext serviceContext;


    public MyHttpRequestHandler(ServiceContext serviceContext){
        this.serviceContext = serviceContext;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest){
            FullHttpRequest request = (FullHttpRequest)msg;

            String uri = request.uri();
            System.out.println("request_url = " + uri);
            if (uri.endsWith(REQUEST_URL)){
                CreateGroupReq validateResult = validateRequest(ctx, request);
                if (validateResult == null){
                    return;
                }
                if (serviceContext.containsSessionId(validateResult.getSessionId()) &&
                    serviceContext.containsSessionId(validateResult.getToSessionId())){

                    String groupId = "GROUP-" + UUID.randomUUID().toString().replaceAll("-","");
                    serviceContext.save(groupId,validateResult.getSessionId());
                    serviceContext.save(groupId,validateResult.getToSessionId());

                    // 保持session 与 channel 的对应关系
                    serviceContext.attachToChannel(groupId,validateResult.getSessionId(),validateResult.getToSessionId());

                    response(ctx,groupId,true);
                }else {
                    response(ctx,"sessionId不存在!",false);
                }
                return;
            }
        }
        super.channelRead(ctx, msg);
    }


    private void response(ChannelHandlerContext ctx,Object message,boolean success){
        ChatMsgResp resp = new ChatMsgResp();
        resp.setSuccess(success);
        resp.setMsg(message);

        String msg = JSON.toJSONString(resp);
        System.out.println("返回给前段的数据为: " + msg);
        ByteBuf byteBuf = ctx.alloc().buffer().writeBytes(msg.getBytes(Charset.forName("UTF-8")));

        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,byteBuf);
        response.headers().add(HttpHeaderNames.CONTENT_TYPE,HttpHeaderValues.APPLICATION_JSON);
        response.headers().add(HttpHeaderNames.CONTENT_LENGTH,response.content().readableBytes());
        response.headers().add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN,"*");
        ctx.writeAndFlush(response);
    }

    private CreateGroupReq validateRequest(ChannelHandlerContext ctx, FullHttpRequest request){
        String method = request.method().name();
        if (!method.toLowerCase().equals("post")){
            response(ctx,"请使用POST方法!",false);
            return null;
        }
        String requestBody = request.content().toString(Charset.forName("utf-8"));
        System.out.println("请求的数据为: " + requestBody);
        if (StringUtils.isBlank(requestBody)){
            response(ctx,"参数错误!",false);
            return null;
        }
        try {
            CreateGroupReq createGroupReq = JSON.parseObject(requestBody, CreateGroupReq.class);
            if (StringUtils.isAnyBlank(createGroupReq.getSessionId(),createGroupReq.getCommand(),createGroupReq.getToSessionId())){
                response(ctx,"参数为空!",false);
                return null;
            }
            if (!createGroupReq.getCommand().equals(Constant.CREATE_GROUP_REQUEST)){
                response(ctx,"参数错误!",false);
                return null;
            }
            return createGroupReq;
        }catch (Exception e){
            e.printStackTrace();
            response(ctx,"参数错误!",false);
        }
        return null;
    }


}
