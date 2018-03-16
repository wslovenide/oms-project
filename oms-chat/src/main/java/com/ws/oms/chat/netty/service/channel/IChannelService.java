package com.ws.oms.chat.netty.service.channel;

import com.ws.oms.chat.netty.handler.dto.ChatMsgResp;
import com.ws.oms.chat.netty.handler.dto.OnlineInfoResp;
import io.netty.channel.Channel;

import java.util.List;
import java.util.Set;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-03-06 13:13
 */
public interface IChannelService {

    void attach(Channel channel, String sesionid);

    String getSessionId(Channel channel);

    Channel remove(Channel channel);

    void broadcastMessage(String sessionId, ChatMsgResp chatMsgResp, Channel current);

    void broadcastMessage(String sessionId, String groupId, ChatMsgResp chatMsgResp);

    int getOnlineNumber(String groupId);

    Set<String> getOnlineSessionIds();

    List<OnlineInfoResp> getOnlineInfoList();

    boolean containsSessionId(String sessionId);

    /**
     *  创建一个group后, 需要该group与对应的channel关联
     */
    void attachToChannel(String groupId,String... sessionArray);
}
