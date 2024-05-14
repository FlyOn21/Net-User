package org.example.app.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.CountDownLatch;


public class ClientHandler extends SimpleChannelInboundHandler<String> {

    private volatile CountDownLatch latch;

    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        if (msg.startsWith("Server connection to DB failed")) {
            System.out.println("Connection to DB failed, closing client");
            ctx.close();
            System.exit(0);
            return;
        }
        System.out.println(msg);
        System.out.println("Enter choice: ");
        if (this.latch != null) {
            latch.countDown();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("Some problem, check. Client stop");
        System.exit(1);
        ctx.close();
    }
}