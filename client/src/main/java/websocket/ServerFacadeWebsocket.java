package websocket;

import chess.NotificationSetup;
import com.google.gson.Gson;
import jakarta.websocket.*;
import serverfacade.ServerFacade;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ServerFacadeWebsocket extends Endpoint {


    public static Session session;



public ServerFacadeWebsocket() throws URISyntaxException, DeploymentException, IOException {
    URI uri = new URI("ws://localhost:"+ ServerFacade.port+"/ws");
    NotificationHandler notifs = new NotificationHandler();
    WebSocketContainer containerItem = ContainerProvider.getWebSocketContainer();

    session = containerItem.connectToServer(this,uri);

    session.addMessageHandler(new MessageHandler.Whole<String>() {
        @Override
        public void onMessage(String m) {
            NotificationSetup e = new Gson().fromJson(m, NotificationSetup.class);
            notifs.notifyAction(e);
        }
    });
}

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }
}
