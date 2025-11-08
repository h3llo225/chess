package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;
import model.GameData;
import model.TransitoryGameData;
import model.UserData;
import serverFacade.serverFacade;
import service.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class postLoginUI {
    public static String authToken;
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
                case "list games", "List games", "List Games", "list Games" -> {
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

    public String listGamesPostLogin() throws DataAccessException, IOException, InterruptedException {
        System.out.println(new serverFacade().listGame(authToken));
        return new serverFacade().listGame(authToken);
    }

    public String createGamePostLogin() throws DataAccessException, IOException, InterruptedException {
        String createGameInputs = Arrays.toString(new preloginUI().getInput());
        GameData newGameChess = new GameData(0,null,null,createGameInputs,new ChessGame());

        new serverFacade().createGame(newGameChess,authToken);
        return "game created!";
    }
    public String playGamePostLogin() throws DataAccessException, IOException, InterruptedException {
        String[] playGameInputs = new preloginUI().getInput();
        int stringToInt = Integer.parseInt(playGameInputs[1]);
        TransitoryGameData newTeamColorAndID = new TransitoryGameData(stringToInt, playGameInputs[0]);
        if (Objects.equals(newTeamColorAndID.playerColor(), "WHITE")){
            String[][] chessboardWhite= new String[8][8];
            chessboardWhite[0][1]= EscapeSequences.BLACK_ROOK;
            System.out.println(chessboardWhite);
        }
        new serverFacade().playGame(newTeamColorAndID, authToken);
        return "game joined!";
    }

    public String logoutUser() throws DataAccessException, IOException, InterruptedException {
        new serverFacade().logoutUser(authToken);
        return "You are now logged out";
    }
}
