package websocket;

import chess.*;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static chess.NotificationSetup.ServerMessageType.*;

public class WebsocketHandlers implements WsConnectHandler, WsMessageHandler, WsCloseHandler {


    private final ConnectionManagement connections = new ConnectionManagement();

    @Override
    public void handleClose(@NotNull WsCloseContext ctx) {
        System.out.println("Websocket closed");

    }

    @Override
    public void handleMessage(@NotNull WsMessageContext ctx) throws IOException, InvalidMoveException,
            DataAccessException {
        UserGameCommand message = new Gson().fromJson(ctx.message(), UserGameCommand.class);
        MakeMoveGameCommand moveMessage = new Gson().fromJson(ctx.message(), MakeMoveGameCommand.class);
        //UserGameCommand er = e;

        if (message.getCommandType() == UserGameCommand.CommandType.CONNECT) {
            joinGame(message, ctx.session);
        }
        if (message.getCommandType() == UserGameCommand.CommandType.LEAVE) {
            leaveGame(message, ctx.session);
        }
        if (message.getCommandType() == UserGameCommand.CommandType.RESIGN) {
            resignGame(message, ctx.session);
        }
        if (moveMessage.getCommandType() == MakeMoveGameCommand.CommandType.MAKE_MOVE) {
            makeMove(moveMessage, ctx.session);
        }
        System.out.println("Websocket message " + ctx.message());

    }

    @Override
    public void handleConnect(@NotNull WsConnectContext ctx) {
        ctx.enableAutomaticPings();
        System.out.println("Websocket Connected");

    }

    public void joinGame(UserGameCommand message, Session session) throws IOException, DataAccessException {

        String user = new DatabaseManager().findUser(message.getAuthToken());
        GameData game = new DatabaseManager().findGameByID(message.getGameID());
        connections.add(session, message.getGameID());
        NotificationSetup notif;
        NotificationSetup notifPersonal;
        if (game != null && user != null) {
            if (Objects.equals(game.whiteUsername(), user)) {
                notif = new NotificationSetup(NOTIFICATION, "User " + user + " connected to the game as white",
                        null, null);
                notifPersonal = new NotificationSetup(LOAD_GAME, null, null, game);
            } else if (Objects.equals(game.blackUsername(), user)) {
                notif = new NotificationSetup(NOTIFICATION, "User " + user +
                        " connected to the game as black",
                        null, null);
                notifPersonal = new NotificationSetup(LOAD_GAME, null, null, game);

            } else {
                notif = new NotificationSetup(NOTIFICATION, "User " + user +
                        " connected to the game as observer", null, null);
                notifPersonal = new NotificationSetup(LOAD_GAME, null, null, game);

            }
            connections.broadcast(session, notif, game.gameID());
            connections.broadcastPersonal(session, notifPersonal);

        } else {
            notifPersonal = new NotificationSetup(ERROR, null, "You made a bad request", null);
            connections.broadcastPersonal(session, notifPersonal);

        }


    }
    public void leaveGameHelper(Session session, UserGameCommand message, String user) throws IOException {
        NotificationSetup notif;
        notif = new NotificationSetup(NOTIFICATION, "User " + user + " " +
                "has left the game", null, null);
        connections.broadcast(session, notif,message.getGameID());
        connections.delete(session);
    }

    public void leaveGame(UserGameCommand message, Session session) throws IOException, DataAccessException {
        String user = new DatabaseManager().findUser(message.getAuthToken());
        GameData game = new DatabaseManager().findGameByID(message.getGameID());
        if (game == null){

            leaveGameHelper(session, message, user);
        }else if (Objects.equals(game.blackUsername(), user)){
            new DatabaseManager().deleteGameByID(message.getGameID());
            new DatabaseManager().makeGame(new GameData(message.getGameID(), game.whiteUsername(), null,
                    game.gameName(),game.game()));

            leaveGameHelper(session, message, user);
        }else if (Objects.equals(game.whiteUsername(), user)){
            new DatabaseManager().deleteGameByID(message.getGameID());
            new DatabaseManager().makeGame(new GameData(message.getGameID(), null, game.blackUsername(),
                    game.gameName(),game.game()));

            leaveGameHelper(session, message, user);
        }else{

            leaveGameHelper(session, message, user);
        }

    }

    public void resignGame(UserGameCommand message, Session session) throws IOException, DataAccessException {
        String user = new DatabaseManager().findUser(message.getAuthToken());
        GameData game = new DatabaseManager().findGameByID(message.getGameID());
        NotificationSetup notif;
        NotificationSetup notifPersonal;
        if (game != null){
            if (Objects.equals(user, game.whiteUsername()) || Objects.equals(user, game.blackUsername())){
                notif = new NotificationSetup(NOTIFICATION, "User " + user + " has resigned.",
                        null, null);
            notifPersonal = new NotificationSetup(NOTIFICATION, "You have resigned the game",
                    null, null);
            connections.broadcast(session, notif,message.getGameID());
            connections.broadcastPersonal(session, notifPersonal);
            new DatabaseManager().deleteGameByID(message.getGameID());
        }else{
            notifPersonal = new NotificationSetup(ERROR, null, "Only players can resign.", null);
            connections.broadcastPersonal(session, notifPersonal);
        }
        }else{
                notifPersonal = new NotificationSetup(ERROR, null, "Please make sure the " +
                        "game is still ongoing.", null);
                connections.broadcastPersonal(session, notifPersonal);

        }

    }

    public void makeMove(MakeMoveGameCommand message, Session session) throws IOException, DataAccessException, InvalidMoveException {
        String user = new DatabaseManager().findUser(message.getAuthToken());
        ChessMove move = message.getMakeMove();
        GameData game = new DatabaseManager().findGameByID(message.getGameID());
        NotificationSetup notif;
        NotificationSetup notifPersonal;
        NotificationSetup notifAll;
        if (game != null){
//having an issue where it evaluates move != null and then the first item = move white username and then ignores everything else
            if (move != null && ((Objects.equals(user, game.whiteUsername()) && game.game().getTeamTurn() == ChessGame.TeamColor.WHITE) ||
                    (Objects.equals(user, game.blackUsername()) && game.game().getTeamTurn()
                            == ChessGame.TeamColor.BLACK))) {
                try {
                    game.game().makeMove(move);
                } catch (InvalidMoveException e) {
                    //error handling
                    notifPersonal = new NotificationSetup(ERROR, null,
                            "Please make sure your move is valid", null);
                    connections.broadcastPersonal(session, notifPersonal);
                    return;
                }

                if (game.game().isInCheckmate(ChessGame.TeamColor.BLACK) && Objects.equals(user, game.whiteUsername()))
                { //check if black is mated
                    notifAll = new NotificationSetup(LOAD_GAME, null, null, game);
                    notifPersonal = new NotificationSetup(LOAD_GAME, null, null, game);
                    connections.broadcast(session, notifAll,message.getGameID());
                    notifAll = new NotificationSetup(NOTIFICATION, "The game is now over via checkmate " +
                            "or stalemate on black team.",
                            null, null);
                    connections.broadcast(session, notifAll,message.getGameID());

                    connections.broadcastPersonal(session, notifPersonal);
                } else if ((game.game().isInCheckmate(ChessGame.TeamColor.WHITE) ||
                        game.game().isInStalemate(ChessGame.TeamColor.WHITE))
                        && Objects.equals(user, game.blackUsername())) {
                    notifAll = new NotificationSetup(LOAD_GAME, null, null, game);
                    notifPersonal = new NotificationSetup(LOAD_GAME, null, null, game);
                    connections.broadcast(session, notifAll,message.getGameID());
                    notifAll = new NotificationSetup(NOTIFICATION, "The game is now over via " +
                            "checkmate or stalemate on white team." + user + "has won",
                            null, null);
                    connections.broadcast(session, notifAll,message.getGameID());
                    connections.broadcastPersonal(session, notifPersonal);

                } else if (!game.game().isInCheckmate(ChessGame.TeamColor.BLACK) &&
                        !game.game().isInCheckmate(ChessGame.TeamColor.WHITE)) {

                    new DatabaseManager().deleteGameByID(message.getGameID());
                    new DatabaseManager().makeGame(new GameData(message.getGameID(),
                            game.whiteUsername(), game.blackUsername(), game.gameName(), game.game()));
                    Map<Integer, String> translatorCol = new HashMap<>();
                    translatorCol.put(1,"a");
                    translatorCol.put(2,"b");
                    translatorCol.put( 3,"c");
                    translatorCol.put(4,"d");
                    translatorCol.put(5,"e");
                    translatorCol.put(6,"f");
                    translatorCol.put(7,"g");
                    translatorCol.put(8,"h");
                    notifAll = new NotificationSetup(NOTIFICATION, "user " + user + " made move to " +
                            translatorCol.get(move.getEndPosition().getColumn())  +" "+ move.getEndPosition().getRow() ,
                            null, null);
                    connections.broadcast(session, notifAll,message.getGameID());


                    notif = new NotificationSetup(LOAD_GAME, null, null, game);
                    notifPersonal = new NotificationSetup(LOAD_GAME, null, null, game);
                    connections.broadcast(session, notif,message.getGameID());
                    connections.broadcastPersonal(session, notifPersonal);
                }

            }else {
                notifPersonal = new NotificationSetup(ERROR, null, "Please make sure your move exists " +
                        "or that it is your turn.",
                        null);
                connections.broadcastPersonal(session, notifPersonal);
            }
    }else {
            notifPersonal = new NotificationSetup(ERROR, null, "You made a bad request", null);
            connections.broadcastPersonal(session, notifPersonal);
        }
    }
}

//how to get playername, team color in the websocket session and make proper notifications for it.
// when i try to make a notification trying to find the team color and the player.
//how to collect what chess pieces are being clicked on in the client and stuff?
//how to send that updated board to the server to send to the other client connected?
// do i need to do 2 join games for both players to join one game
