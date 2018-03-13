package com.ws.oms.chat.netty.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by gongmei on 2018/3/12.
 */
public class RedisUtil {

    private static JedisPool jedisPool;

    private static void init(){
        Properties properties = loadJedisProperties();
        String host = properties.getProperty("jedis.host");
        int timeout = Integer.parseInt(properties.getProperty("jedis.timeout"));
        String password = properties.getProperty("jedis.password");

        System.out.println(host + ", " + timeout + " , " + password);
        jedisPool = new JedisPool(new JedisPoolConfig(),host,6379,timeout,password);
    }

    private static Jedis getResource(){
        if (jedisPool == null){
            synchronized (RedisUtil.class){
                init();
            }
        }
        return jedisPool.getResource();
    }

    private static void close(Jedis jedis){
        jedis.close();
    }

    public static void doInJedis(JedisTemplete jedisTemplete){
        Jedis resource = getResource();
        try {
            jedisTemplete.doInJedis(resource);
        }finally {
            close(resource);
        }
    }


    private static Properties loadJedisProperties(){
        Properties properties = new Properties();
        try {
            InputStream resource = RedisUtil.class.getClassLoader().getResourceAsStream("redis.properties");
            if (resource != null){
                properties.load(resource);
                resource.close();
                return properties;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return properties;
    }

    public static void main(String[] args) {
        Jedis resource = RedisUtil.getResource();
        System.out.println(resource);
    }

}
