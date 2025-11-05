package ui;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;
import model.GameData;
import model.TransitoryGameData;
import service.Service;

public class postLoginUI {
    public String username = null;
    public String helpPostLogin(){
        return """
                Logout
                Create Game(game name)
                List Games
                Play Game(WHITE/BLACK, gameID)
                Observe Game(?, gameID)
                """;
    }

    public String createGamePostLogin(String gameName) throws DataAccessException {
        AuthData authorized = new DatabaseManager().findAuthByUsername(username);
        GameData gameCreate = new GameData(0,null,null,gameName,new ChessGame());
        new Service().createGame(authorized, gameCreate);
        return "game created!";
    }

    public String joinGamePostLogin(String playerColor, int gameID) throws DataAccessException {
        AuthData authorized = new DatabaseManager().findAuthByUsername(username);
        new Service().joinGame(authorized, new TransitoryGameData(gameID, playerColor));
        return "game joined!";
    }

    public String listGamesPostLogin() throws DataAccessException {
        AuthData authorized = new DatabaseManager().findAuthByUsername(username);
        return new Service().listGame(authorized);

    }
}
