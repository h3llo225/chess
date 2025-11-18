package websocket;

import io.javalin.websocket.*;

import org.jetbrains.annotations.NotNull;

public class WebsocketHandlers implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    @Override
    public void handleClose(@NotNull WsCloseContext ctx){
        System.out.println("Websocket closed");

    }
    @Override
    public void handleMessage(@NotNull WsMessageContext ctx){
        System.out.println("Websocket message " + ctx.message());

    }
    @Override
    public void handleConnect(@NotNull WsConnectContext ctx){
        System.out.println("Connection ");
    }
}
