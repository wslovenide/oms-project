package com.ws.oms.chat.netty.service.room.impl;

import com.alibaba.fastjson.JSON;
import com.ws.oms.chat.netty.handler.dto.ChatMsgResp;
import com.ws.oms.chat.netty.handler.dto.group.CreateGroupReq;
import com.ws.oms.chat.netty.service.ServiceContext;
import com.ws.oms.chat.netty.service.room.IRoomService;
import com.ws.oms.chat.netty.util.Constant;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by gongmei on 2018/3/14.
 */
public class RoomService implements IRoomService{


    private ServiceContext serviceContext;


    public RoomService(ServiceContext serviceContext){
        this.serviceContext = serviceContext;
    }


    @Override
    public ChatMsgResp createChatRoom(String requestBody) {
        ChatMsgResp resp = new ChatMsgResp();
        CreateGroupReq validateResult = validateRequest(requestBody,resp);
        if (validateResult == null){
            resp.setSuccess(false);
            return resp;
        }
        String sessionId = validateResult.getSessionId();
        String toSessionId = validateResult.getToSessionId();
        if (serviceContext.containsSessionId(sessionId) && serviceContext.containsSessionId(toSessionId)){
            List<String> list = Arrays.asList(sessionId, toSessionId);
            list.sort(Comparator.naturalOrder());
            String orderString = list.stream().collect(Collectors.joining("-"));
            String groupId = DigestUtils.md5Hex("GROUP:" + orderString);

            if (!serviceContext.containsGroupId(groupId)){
                serviceContext.save(groupId,validateResult.getSessionId());
                serviceContext.save(groupId,validateResult.getToSessionId());
            }
            // 保持 session 与 channel 的对应关系
            serviceContext.attachToChannel(groupId,validateResult.getSessionId(),validateResult.getToSessionId());

            resp.setMsg(groupId);
        }else {
            resp.setMsg("sessionId不存在!");
        }
        return resp;
    }


    private CreateGroupReq validateRequest(String requestBody,ChatMsgResp resp){
        try {
            CreateGroupReq createGroupReq = JSON.parseObject(requestBody, CreateGroupReq.class);
            if (StringUtils.isAnyBlank(createGroupReq.getSessionId(),createGroupReq.getCommand(),createGroupReq.getToSessionId())){
                resp.setMsg("参数为空!");
                return null;
            }
            if (!createGroupReq.getCommand().equals(Constant.CREATE_GROUP_REQUEST)){
                resp.setMsg("参数错误!");
                return null;
            }
            return createGroupReq;
        }catch (Exception e){
            resp.setMsg("参数错误!");
            e.printStackTrace();
        }
        return null;
    }


}
