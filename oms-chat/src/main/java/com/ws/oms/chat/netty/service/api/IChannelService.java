package com.ws.oms.chat.netty.service.api;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;

import java.util.Map;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-03-06 13:13
 */
public interface IChannelService {

    void add(Channel channel);

    Channel remove(Channel channel);

    Map<ChannelId,Channel> getOnlineChannelMap();
}
