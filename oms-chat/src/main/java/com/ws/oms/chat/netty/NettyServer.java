package com.ws.oms.chat.netty;

import com.ws.oms.chat.netty.handler.MyHttpRequestHandler;
import com.ws.oms.chat.netty.handler.MyWebSocketHandler;
import com.ws.oms.chat.netty.service.ServiceContext;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;

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


    public void start(){
        ServiceContext serviceContext = new ServiceContext();

        EventLoopGroup boss = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.group(boss,boss);
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new HttpServerCodec());
                ch.pipeline().addLast(new HttpObjectAggregator(65535));
//                ch.pipeline().addLast(new MyHttpRequestHandler(serviceContext));
                ch.pipeline().addLast(new WebSocketServerProtocolHandler(webSocketUrl));
                ch.pipeline().addLast(new WebSocketServerCompressionHandler());
                ch.pipeline().addLast(new MyWebSocketHandler(serviceContext));
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

}
