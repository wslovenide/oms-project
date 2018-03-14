package com.ws.oms.chat.netty.handler;

import com.alibaba.fastjson.JSON;
import com.ws.oms.chat.netty.handler.dto.ChatMsgResp;
import com.ws.oms.chat.netty.handler.dto.group.CreateGroupReq;
import com.ws.oms.chat.netty.service.ServiceContext;
import com.ws.oms.chat.netty.service.usergroup.impl.UserGroupRedisService;
import com.ws.oms.chat.netty.util.Constant;
import com.ws.oms.chat.netty.util.RedisUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;
import java.util.UUID;

/**
 * Created by gongmei on 2018/3/14.
 */
public class MyHttpRequestHandler2 extends ChannelInboundHandlerAdapter {

    private static final String REQUEST_URL = "/room/create";

    private ServiceContext serviceContext;


    public MyHttpRequestHandler2(ServiceContext serviceContext){
        this.serviceContext = serviceContext;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest){
            FullHttpRequest request = (FullHttpRequest)msg;

            String uri = request.uri();
            if (uri.endsWith(REQUEST_URL)){
                CreateGroupReq validateResult = validateRequest(ctx, request);
                if (validateResult == null){
                    return;
                }
                RedisUtil.doInJedis(redis -> {
                    String key1 = UserGroupRedisService.SESSION_GROUP_PREFIX + validateResult.getSessionId();
                    String key2 = UserGroupRedisService.SESSION_GROUP_PREFIX + validateResult.getToSessionId();

                    Long exists = redis.exists(key1, key2);
                    if (exists == 2){
                        // 保存session与group的对应关系
                        String groupId = "GROUP-" + UUID.randomUUID().toString().replaceAll("-","");
                        serviceContext.save(groupId,validateResult.getSessionId());
                        serviceContext.save(groupId,validateResult.getToSessionId());

                        // 保持session 与 channel 的对应关系
                        serviceContext.attachToChannel(groupId,validateResult.getSessionId(),validateResult.getToSessionId());
                    }
                    return null;
                });
                return;
            }
        }
        super.channelRead(ctx, msg);
    }


    private void errorResponse(ChannelHandlerContext ctx,String errorMsg){
        ChatMsgResp resp = new ChatMsgResp();
        resp.setSuccess(false);
        resp.setMsg(errorMsg);

        String msg = JSON.toJSONString(resp);
        ByteBuf byteBuf = ctx.alloc().buffer().writeBytes(msg.getBytes(Charset.forName("UTF-8")));

        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,byteBuf);
        response.headers().add(HttpHeaderNames.CONTENT_TYPE,HttpHeaderValues.APPLICATION_JSON);
        response.headers().add(HttpHeaderNames.CONTENT_LENGTH,response.content().readableBytes());
        ctx.writeAndFlush(response);
    }

    private CreateGroupReq validateRequest(ChannelHandlerContext ctx, FullHttpRequest request){
        String method = request.method().name();
        if (!method.toLowerCase().equals("post")){
            errorResponse(ctx,"请使用POST方法!");
            return null;
        }
        String requestBody = request.content().toString(Charset.forName("utf-8"));
        if (StringUtils.isBlank(requestBody)){
            errorResponse(ctx,"参数错误!");
            return null;
        }
        try {
            CreateGroupReq createGroupReq = JSON.parseObject(requestBody, CreateGroupReq.class);
            if (StringUtils.isAnyBlank(createGroupReq.getSessionId(),createGroupReq.getCommand(),createGroupReq.getToSessionId())){
                errorResponse(ctx,"参数为空!");
                return null;
            }
            if (createGroupReq.getCommand().equals(Constant.CREATE_GROUP_REQUEST)){
                errorResponse(ctx,"参数错误!");
                return null;
            }
            return createGroupReq;
        }catch (Exception e){
            e.printStackTrace();
            errorResponse(ctx,"参数错误!");
        }
        return null;
    }


}
