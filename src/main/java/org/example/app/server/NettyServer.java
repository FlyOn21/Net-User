package org.example.app.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public final class NettyServer {

    static final int PORT = ConfigServer.PORT;
    private static final Logger SERVER_LOGGER =
            LogManager.getLogger(NettyServer.class);
    private static final Logger CONSOLE_LOGGER =
            LogManager.getLogger("console_logger");

    public static void main(String[] args) throws Exception {

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();


        try {
                ServerBootstrap b = new ServerBootstrap();
                b.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(SocketChannel ch) {
                                ChannelPipeline p = ch.pipeline();
                                p.addLast(new StringDecoder());
                                p.addLast(new StringEncoder());
                                p.addLast(new ServerHandler());
                            }
                        });
                ChannelFuture f = b.bind(PORT).sync();
                String serverStartedMsg = "Server started and waiting for clients...";
                SERVER_LOGGER.info(serverStartedMsg);
                CONSOLE_LOGGER.info(serverStartedMsg);
                f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            SERVER_LOGGER.info("Server stopped");
        }
    }
}
