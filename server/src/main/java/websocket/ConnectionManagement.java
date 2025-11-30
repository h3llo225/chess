package websocket;
import chess.ChessGame;
import chess.NotificationSetup;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManagement {
    public final ConcurrentHashMap<Session, Integer> connections = new ConcurrentHashMap<>();

    public void add(Session session, int gameID){
        connections.put(session, gameID);
    }

    public void delete(Session session){
        connections.remove(session);
    }

    public void broadcast(Session session, NotificationSetup notification, int gameID) throws IOException {
        String notif = notification.getString();
        for (Session connectionVal : connections.keySet()){
            if (connectionVal != session){
                if (connectionVal.isOpen() && gameID == connections.get(connectionVal)) {
                    String json = new Gson().toJson(notification, notification.getClass());
                    connectionVal.getRemote().sendString(json);
                }
            }
        }
    }

    public void broadcastPersonal(Session session, NotificationSetup notification) throws IOException {
        String notif = notification.getString();
        for (Session connectionVal : connections.keySet()){
        if (connectionVal == session){
            if (connectionVal.isOpen()) {
                String json = new Gson().toJson(notification, notification.getClass());
                connectionVal.getRemote().sendString(json);
            }
            }
        }
    }

}
