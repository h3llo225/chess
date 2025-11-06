package ui;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;
import model.GameData;
import model.TransitoryGameData;
import model.UserData;
import service.Service;

import java.util.Scanner;

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

    public String getChoicePostLogin(){
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            return switch (line) {
                case "logout", "Logout" -> {
                    System.out.println("You have chosen to Logout!");
                    yield "logout";
                }
                case "create game", "Create game", "Create Game", "create Game" -> {
                    System.out.println("You have chosen to create a game! Please" +
                            "input a game name.");
                    yield "create game";
                }
                case "list game", "List game", "List Game", "list Game" -> {
                    System.out.println("You have chosen to list all games!");
                    yield "list games";
                }
                case "play game", "Play game", "Play Game", "play Game" -> {
                    System.out.println("You have chosen to play a game please input a " +
                            " team color and game id (in that order)");
                    yield "play game";
                }
                case "observe game", "Observe game", "Observe Game", "observe Game" -> {
                    System.out.println("You have chosen to observe a game please input a game id");
                    yield "observe games";
                }
                default -> "invalid choice";
            };
        }

    }


    static public void displayOptionsPostLogin(){
        System.out.println("""
                logout
                create game
                list games
                Play games
                Observe game
                """);
    }

//    public String createGamePostLogin(String gameName) throws DataAccessException {
//
//    }

    public String joinGamePostLogin(String playerColor, int gameID) throws DataAccessException {
        AuthData authorized = new DatabaseManager().findAuthByUsername(username);
        new Service().joinGame(authorized, new TransitoryGameData(gameID, playerColor));
        return "game joined!";
    }

    public String listGamesPostLogin() throws DataAccessException {
        AuthData authorized = new DatabaseManager().findAuthByUsername(username);
        return new Service().listGame(authorized);
    }

    public String createGamePostLogin() throws DataAccessException {
        AuthData authorized = new DatabaseManager().findAuthByUsername(username);
        String[] createGameInputs = new preloginUI().getInput();
        GameData gameCreate = new GameData(0,null,null,createGameInputs[0],new ChessGame());
        new Service().createGame(authorized, gameCreate);
        return "game created!";
    }
    public String playGamePostLogin() throws DataAccessException {
        AuthData authorized = new DatabaseManager().findAuthByUsername(username);
        String[] playGameInputs = new preloginUI().getInput();
        int stringToInt = Integer.parseInt(playGameInputs[1]);
        TransitoryGameData newTeamColorAndID = new TransitoryGameData(stringToInt, playGameInputs[0]);
        new Service().joinGame(authorized, newTeamColorAndID);
        return "game joined!";
    }
    public String listGamePostLogin() throws DataAccessException {
        AuthData authorized = new DatabaseManager().findAuthByUsername(username);
        return new Service().listGame(authorized);
    }

    public String logoutUser() throws DataAccessException {
        AuthData authorized = new DatabaseManager().findAuthByUsername(username);
        new Service().logout(authorized);
        return "You are now logged out";
    }
}
