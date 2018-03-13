package com.ws.oms.chat.netty.util;

import redis.clients.jedis.Jedis;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-03-13 13:36
 */
public interface JedisTemplete {

    Object doInJedis(Jedis jedis);
}
