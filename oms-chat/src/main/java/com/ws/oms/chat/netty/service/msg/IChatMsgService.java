package com.ws.oms.chat.netty.service.msg;

import io.netty.channel.ChannelHandlerContext;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-03-09 15:25
 */
public interface IChatMsgService {


    void  handleMessage(ChannelHandlerContext ctx, String msg);

}
