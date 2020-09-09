package pers.wmx.io.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author: wangmingxin03
 * @date: 2020-09-08
 */
public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        //boss线程组 处理客户端连接
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //worker线程组 处理客户端连接
        EventLoopGroup workGroup = new NioEventLoopGroup(1);

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        //在逻辑链pipeline添加自定义handler
                        channel.pipeline().addLast(new ServerHandler());
                    }
                });

        ChannelFuture channelFuture = bootstrap.bind(8888).sync(); //等待服务器启动完毕，才会进入下行代码
        System.out.println("Server Starting ...");

        //关闭通道、线程组
        channelFuture.channel().closeFuture().sync(); //等待服务端关闭socket
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }

}
