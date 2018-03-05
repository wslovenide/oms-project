package com.ws.oms.chat.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-02-27 14:09
 */
public class NettyServer {

    private int port = 8888;

    private static String webSocketUrl = "/websocket/chat";

    private static Map<ChannelId,Channel> channelMap = new ConcurrentHashMap<>(512);

    public void start(){
        EventLoopGroup boss = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.group(boss,boss);
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new HttpServerCodec());
                ch.pipeline().addLast(new HttpObjectAggregator(65535));
                ch.pipeline().addLast(new WebSocketServerProtocolHandler(webSocketUrl));
                ch.pipeline().addLast(new WebSocketServerCompressionHandler());
                ch.pipeline().addLast(new MyWebSocketHandler());
            }
        });
        try {
            System.out.println("启动服务，端口为：" + port + " , url : " + webSocketUrl);
            serverBootstrap.bind(port).sync().channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        new NettyServer().start();

    }


    private static class MyWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            channelMap.put(ctx.channel().id(),ctx.channel());
            super.channelActive(ctx);
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            channelMap.remove(ctx.channel().id());
            super.channelInactive(ctx);
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
            System.out.println("===websocket ===" + msg.text());
            channelMap.forEach((key,value) -> {
                String prefix = "0";
                if (key == ctx.channel().id()){
                    prefix = "1";
                }
                value.writeAndFlush(new TextWebSocketFrame(prefix + msg.text()));
            });
        }
    }
}
