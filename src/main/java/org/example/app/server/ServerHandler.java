package org.example.app.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.app.app.controllers.DBController;
import org.example.app.app.db_connect.DbConnectInit;
import org.example.app.app.views.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ChannelHandler.Sharable
public class ServerHandler extends SimpleChannelInboundHandler<String> {


    static final List<Channel> channels = new ArrayList<>();

    private static final Logger CONSOLE_LOGGER =
            LogManager.getLogger("console_logger");
    private static final Logger SERVER_HANDLER_LOGGER =
            LogManager.getLogger(ServerHandler.class);

    private DbConnectInit connection;


    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        DBController appController = new DBController();
        connection = appController.connect();
        CONSOLE_LOGGER.info(String.format("Client joined - %s", ctx));
        SERVER_HANDLER_LOGGER.info(String.format("Client joined - %s", ctx));
        sendMenu(ctx);
        channels.add(ctx.channel());
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String request) {
        String response;

        List<String> requestParams = Arrays.stream(request.split("\\|\\|"))
                .map(String::strip)
                .toList();

        switch (requestParams.getFirst()) {
            case "1":
                GetUsersView getUsersView = new GetUsersView(connection);
                response = getUsersView.getUsersViewProcessing();
                break;
            case "2":
                CreateUserView createUserView = new CreateUserView(connection);
                System.out.println(requestParams.get(1));
                response = createUserView.createUserViewProcessing(requestParams.get(1));
                break;
            case "3":
                GetUserByIdView getUserByIdView = new GetUserByIdView(connection);
                response = getUserByIdView.getUserByIdProcessing(requestParams.get(1));
                break;
            case "4":
                UpdateUserView updateUserView = new UpdateUserView(connection);
                response = updateUserView.updateUserViewProcessing(requestParams.get(1));
                break;
            case "5":
                DeleteUserView deleteUserView = new DeleteUserView(connection);
                response = deleteUserView.deleteUserViewProcessing(requestParams.get(1));
                break;
            case "6":
                ctx.close();

                SERVER_HANDLER_LOGGER.info(String.format("Client connect close - %s", ctx));
                CONSOLE_LOGGER.info(String.format("Client connect close - %s", ctx));
                return;
            default:
                response = "Invalid Request";
        }
        ctx.writeAndFlush(response + '\n');
        sendMenu(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        SERVER_HANDLER_LOGGER.info(String.format("Closing connection for client - %s", ctx));
        CONSOLE_LOGGER.info(String.format("Closing connection for client - %s", ctx));
        ctx.writeAndFlush("Server connection to DB failed\n");
        ctx.close();
    }

    private void sendMenu(ChannelHandlerContext ctx) {
        String menu = """
                    1. Get all users
                    2. Create user
                    3. Find user by id
                    4. Update user
                    5. Delete user
                    6. Exit
                    """;
        ctx.writeAndFlush(menu);
    }
}
