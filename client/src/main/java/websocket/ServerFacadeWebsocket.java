package websocket;

import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.Session;
import serverfacade.ServerFacade;

import java.net.URI;

public class ServerFacadeWebsocket extends Endpoint {


    public Session session;
    public NotificationHandler notifs;






//    URI uri = new URI("ws://localhost:"+ ServerFacade.port+"/ws");



    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }
}
