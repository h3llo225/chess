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
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
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

    public static String getChoicePostLogin(){
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


    public String[][] initializeBoardWhite(){
        String[][] chessboardWhite= new String[8][8];
        for (int i = 0; i <8; i++){

            for(int j = 0; j< 8; j++){
                chessboardWhite[i][j] = EscapeSequences.EMPTY;
            }
        }
        chessboardWhite[0][0]= EscapeSequences.WHITE_ROOK;
        chessboardWhite[0][1]= EscapeSequences.WHITE_KNIGHT;
        chessboardWhite[0][2]= EscapeSequences.WHITE_BISHOP;
        chessboardWhite[0][3]= EscapeSequences.WHITE_QUEEN;
        chessboardWhite[0][4]= EscapeSequences.WHITE_KING;
        chessboardWhite[0][5]= EscapeSequences.WHITE_BISHOP;
        chessboardWhite[0][6]= EscapeSequences.WHITE_KNIGHT;
        chessboardWhite[0][7]= EscapeSequences.WHITE_ROOK;
        for (int i = 0; i <8; i++){
            chessboardWhite[1][i]=EscapeSequences.WHITE_PAWN;
        }
        for (int i = 0; i <8; i++){
            chessboardWhite[6][i]=EscapeSequences.BLACK_PAWN;
        }
        chessboardWhite[7][0]= EscapeSequences.BLACK_ROOK;
        chessboardWhite[7][1]= EscapeSequences.BLACK_KNIGHT;
        chessboardWhite[7][2]= EscapeSequences.BLACK_BISHOP;
        chessboardWhite[7][3]= EscapeSequences.BLACK_QUEEN;
        chessboardWhite[7][4]= EscapeSequences.BLACK_KING;
        chessboardWhite[7][5]= EscapeSequences.BLACK_BISHOP;
        chessboardWhite[7][6]= EscapeSequences.BLACK_KNIGHT;
        chessboardWhite[7][7]= EscapeSequences.BLACK_ROOK;
        return chessboardWhite;
    }
    public String makeChessBoardWhite(String[][] chessBoardWhite){
        StringBuilder retVal = new StringBuilder();
        for (int i = 0; i <8; i++) {
            retVal.append("\n");
            for (int j = 0; j < 8; j ++){
                if ((i+j) %2==0){

                    retVal.append(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                    retVal.append(EscapeSequences.SET_TEXT_COLOR_DARK_GREY);
                    retVal.append(chessBoardWhite[i][j]);
                    retVal.append(EscapeSequences.RESET_TEXT_COLOR);
                    retVal.append(EscapeSequences.RESET_BG_COLOR);

                }else{
                    retVal.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                    retVal.append(EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY);
                    retVal.append(chessBoardWhite[i][j]);
                    retVal.append(EscapeSequences.RESET_TEXT_COLOR);
                    retVal.append(EscapeSequences.RESET_BG_COLOR);



                }
            }
        }
        retVal.append(EscapeSequences.RESET_TEXT_COLOR);
        retVal.append(EscapeSequences.RESET_BG_COLOR);
        return retVal.toString();

    }

    public String makeChessBoardBlack(String[][] chessBoardWhite){
        StringBuilder retVal = new StringBuilder();
        for (int i = 7; i >= 0; i--) {
            retVal.append("\n");
            for (int j = 0; j < 8; j ++){
                retVal.append(chessBoardWhite[i][j]);
            }
        }
        return retVal.toString();

    }
    public String playGamePostLogin() throws DataAccessException, IOException, InterruptedException {
        String[] playGameInputs = new preloginUI().getInput();
        int stringToInt = Integer.parseInt(playGameInputs[1]);
        TransitoryGameData newTeamColorAndID = new TransitoryGameData(stringToInt, playGameInputs[0]);
        if (Objects.equals(newTeamColorAndID.playerColor(), "WHITE")){
            var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
            out.print(makeChessBoardWhite(initializeBoardWhite()));
        }
        new serverFacade().playGame(newTeamColorAndID, authToken);
        return "game joined!";
    }

    public String logoutUser() throws DataAccessException, IOException, InterruptedException {
        new serverFacade().logoutUser(authToken);
        return "You are now logged out";
    }
}
