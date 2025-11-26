package websocket;

import chess.ChessMove;
import chess.InvalidMoveException;
import chess.NotificationSetup;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import io.javalin.websocket.*;

import model.GameData;
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
    public void handleMessage(@NotNull WsMessageContext ctx) throws IOException, DataAccessException, InvalidMoveException {
        UserGameCommand message = new Gson().fromJson(ctx.message(), UserGameCommand.class);
        MakeMoveGameCommand moveMessage = new Gson().fromJson(ctx.message(), MakeMoveGameCommand.class);
        //UserGameCommand er = e;

        if (message.getCommandType()== UserGameCommand.CommandType.CONNECT){
            joinGame(message, ctx.session);
        }
        if (message.getCommandType()== UserGameCommand.CommandType.LEAVE){
            leaveGame(message,ctx.session);
        }
        if (message.getCommandType() == UserGameCommand.CommandType.RESIGN){
            resignGame(message,ctx.session);
        }
        if ( moveMessage.getCommandType() ==MakeMoveGameCommand.CommandType.MAKE_MOVE){
            makeMove(moveMessage,ctx.session);
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
            notif = new NotificationSetup(LOAD_GAME,"User connected as " + user + " on the white team", game);
            notifPersonal = new NotificationSetup(LOAD_GAME,"User connected as " + user + " on the white team", game);
        }
        else if (Objects.equals(game.blackUsername(), user)){
            notif = new NotificationSetup(LOAD_GAME,"User connected as " + user + " on the black team", null);
            notifPersonal = new NotificationSetup(LOAD_GAME,"User connected as " + user + " on the black team", null);

        }else{
            notif = new NotificationSetup(LOAD_GAME,"User connected as " + user + " as an observer", null);
            notifPersonal = new NotificationSetup(LOAD_GAME,"User connected as " + user + " as an observer", null);

        }
        connections.broadcast(session,notif);
        connections.broadcastPersonal(session,notifPersonal);


    }

    public void leaveGame(UserGameCommand message, Session session) throws IOException, DataAccessException {
        String user = new DatabaseManager().findUser(message.getAuthToken());
        NotificationSetup notif;
        NotificationSetup notifPersonal;
        notif = new NotificationSetup(notification,"User " + user + " has left the game", null);
        notifPersonal = new NotificationSetup(notification,"You have left the game!", null);
        connections.broadcast(session,notif);
        connections.broadcastPersonal(session,notifPersonal);
        connections.delete(session);
    }

    public void resignGame(UserGameCommand message, Session session) throws IOException, DataAccessException {
        String user = new DatabaseManager().findUser(message.getAuthToken());
        GameData game =new DatabaseManager().findGameByID(message.getGameID());
        NotificationSetup notif;
        NotificationSetup notifPersonal;
        notif = new NotificationSetup(notification,"User " + user + " has resigned.", null);
        notifPersonal = new NotificationSetup(notification,"You have resigned the game", null);
        connections.broadcast(session,notif);
        connections.broadcastPersonal(session,notifPersonal);
    }

    public void makeMove(MakeMoveGameCommand message, Session session) throws IOException, DataAccessException, InvalidMoveException {
        String user = new DatabaseManager().findUser(message.getAuthToken());
        ChessMove move = message.getMakeMove();
        GameData game =new DatabaseManager().findGameByID(message.getGameID());
        NotificationSetup notif;
        NotificationSetup notifPersonal;
        game.game().makeMove(move);
        notif = new NotificationSetup(LOAD_GAME, "user "+ user +" made a move shown below", game);
         notifPersonal = new NotificationSetup(LOAD_GAME,"You made a move!",game);
        connections.broadcast(session,notif);
        connections.broadcastPersonal(session,notifPersonal);
    }
}

//how to get playername, team color in the websocket session and make proper notifications for it.
// when i try to make a notification trying to find the team color and the player.
//how to collect what chess pieces are being clicked on in the client and stuff?
//how to send that updated board to the server to send to the other client connected?
// do i need to do 2 join games for both players to join one game
