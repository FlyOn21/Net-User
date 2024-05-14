package org.example.app.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;
import java.util.concurrent.*;

public final class NettyClient {
    static final String HOST = ConfigClient.HOST;
    static final int PORT = ConfigClient.PORT;

    public static void main(String[] args) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            System.out.println("Client started and waiting for messages...");
            Bootstrap b = new Bootstrap();
            ClientHandler clientHandler = new ClientHandler();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new StringDecoder());
                            p.addLast(new StringEncoder());
                            p.addLast(clientHandler);
                        }
                    });
            ChannelFuture f = b.connect(HOST, PORT).sync();
            Scanner scanner = new Scanner(System.in);
            Channel channel = f.sync().channel();

            // Loop for continuous listening

            while (true) {
                String sendInput;

                CountDownLatch latch = new CountDownLatch(2);
                clientHandler.setLatch(latch);
                String input = scanner.nextLine();
                switch (input) {
                    case "1":
                        sendInput = input + "||" + "getAll";
                        break;
                    case "2":
                        System.out.println("Enter create data, pattern (String firstName, String lastName, String email, String phone), all fields are required: ");
                        String createParams = scanner.nextLine();
                        sendInput = input + "||" + createParams;
                        break;
                    case "3":
                        System.out.println("Enter get id, pattern (int id), id field is required: ");
                        String byIdParams = scanner.nextLine();
                        sendInput = input + "||" + byIdParams;
                        break;
                    case "4":
                        System.out.println("Enter update data, pattern (int id, String firstName, String lastName, String email, String phone), id is required\n"
                                + "if you do not want to change some parameter for the user, specify an empty line (example 1, , , , +380504444444): ");
                        String updateParams = scanner.nextLine();
                        sendInput = input + "||" + updateParams;
                        break;
                    case "5":
                        System.out.println("WARNING: You are about to delete a user from the database!");
                        System.out.println("Enter delete id, pattern (int id), id is required: ");
                        String deleteParams = scanner.nextLine();

                        sendInput = input + "||" + deleteParams;
                        break;
                    case "6":
                        System.out.println("Exit..");
                        sendInput = input + "||" + "exit";
                        channel.writeAndFlush(sendInput);
                        channel.close();
                        return;
                    default:
                        System.out.println("Invalid choice");
                        continue;
                }

                channel.writeAndFlush(sendInput);
                channel.flush();
                latch.await(2, TimeUnit.SECONDS);
            }
        } finally {
            group.shutdownGracefully();
        }
    }
}
