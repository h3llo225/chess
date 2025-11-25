package websocket;

import chess.ChessMove;
import chess.NotificationSetup;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import io.javalin.websocket.*;

import model.GameData;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import websocket.commands.MakeMoveGameCommand;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.util.Objects;

import static chess.NotificationSetup.Type.*;

public class WebsocketHandlers implements WsConnectHandler, WsMessageHandler, WsCloseHandler {


    private final ConnectionManagement connections = new ConnectionManagement();

    @Override
    public void handleClose(@NotNull WsCloseContext ctx){
        System.out.println("Websocket closed");

    }
    @Override
    public void handleMessage(@NotNull WsMessageContext ctx) throws IOException, DataAccessException {
        UserGameCommand message = new Gson().fromJson(ctx.message(), UserGameCommand.class);
        //UserGameCommand er = e;

        if (message.getCommandType()== UserGameCommand.CommandType.CONNECT){
            joinGame(message, ctx.session);
        }
        if (message.getCommandType()== UserGameCommand.CommandType.LEAVE){
            leaveGame(message,ctx.session);
        }
        System.out.println("Websocket message " + ctx.message());

    }
    @Override
    public void handleConnect(@NotNull WsConnectContext ctx){
        ctx.enableAutomaticPings();
        System.out.println("Websocket Connected");

    }
    public void joinGame(UserGameCommand message, Session session) throws IOException, DataAccessException {
        connections.add(session);
        String user = new DatabaseManager().findUser(message.getAuthToken());
        GameData game =new DatabaseManager().findGameByID(message.getGameID());
        NotificationSetup notif;
        NotificationSetup notifPersonal;

        if (Objects.equals(game.whiteUsername(), user)){
            notif = new NotificationSetup(notification,"User connected as " + user + " on the white team");
            notifPersonal = new NotificationSetup(notification,"User connected as " + user + " on the white team");
        }
        else if (Objects.equals(game.blackUsername(), user)){
            notif = new NotificationSetup(notification,"User connected as " + user + " on the black team");
            notifPersonal = new NotificationSetup(notification,"User connected as " + user + " on the white team");

        }else{
            notif = new NotificationSetup(notification,"User connected as " + user + " as an observer");
            notifPersonal = new NotificationSetup(notification,"User connected as " + user + " on the white team");

        }
        connections.broadcast(session,notif);
        connections.broadcastPersonal(session,notifPersonal);


    }

    public void leaveGame(UserGameCommand message, Session session) throws IOException, DataAccessException {
        String user = new DatabaseManager().findUser(message.getAuthToken());
        NotificationSetup notif;
        NotificationSetup notifPersonal;
        notif = new NotificationSetup(notification,"User " + user + " has left the game");
        notifPersonal = new NotificationSetup(notification,"You have left the game!");
        connections.broadcast(session,notif);
        connections.broadcastPersonal(session,notifPersonal);
        connections.delete(session);
    }

    public void resignGame(UserGameCommand message, Session session) throws IOException, DataAccessException {
        String user = new DatabaseManager().findUser(message.getAuthToken());
        GameData game =new DatabaseManager().findGameByID(message.getGameID());
        NotificationSetup notif;
        NotificationSetup notifPersonal;
        notif = new NotificationSetup(notification,"User " + user + " has resigned.");
        notifPersonal = new NotificationSetup(notification,"You have resigned the game");
        connections.broadcast(session,notif);
        connections.broadcastPersonal(session,notifPersonal);
    }

    public void makeMove(MakeMoveGameCommand message, Session session) throws IOException, DataAccessException {
        String user = new DatabaseManager().findUser(message.getAuthToken());
        ChessMove move = message.getMakeMove();
        GameData game =new DatabaseManager().findGameByID(message.getGameID());
        NotificationSetup notif;
        NotificationSetup notifPersonal;
        notif = new NotificationSetup(loadGame,"User " + user + " has made a move." + move);
        notifPersonal = new NotificationSetup(loadGame,"You have resigned the game");
        connections.broadcast(session,notif);
        connections.broadcastPersonal(session,notifPersonal);
    }
}

//how to get playername, team color in the websocket session and make proper notifications for it.
// when i try to make a notification trying to find the team color and the player.
//how to collect what chess pieces are being clicked on in the client and stuff?
//how to send that updated board to the server to send to the other client connected?
// do i need to do 2 join games for both players to join one game
