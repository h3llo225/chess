package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import model.GameData;
import model.TransitoryGameData;
import serverFacade.serverFacade;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

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
                    System.out.println();
                    yield "observe game";
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

    public String listGamesPostLogin() throws Exception {
        System.out.println(new serverFacade().listGame(authToken));
        return new serverFacade().listGame(authToken);
    }

    public String createGamePostLogin() throws Exception {
        String createGameInputs = Arrays.toString(new preloginUI().getInput());

        GameData newGameChess = new GameData(0,null,null,createGameInputs,new ChessGame());

        new serverFacade().createGame(newGameChess,authToken);
        return "game created!";
    }


    public String[][] initializeBoardBlack(){
        String[][] chessboardWhite= new String[10][10];
        String[] labels = {"","  a  ", " b  ", " c  ", "d  ", " e  ","f  "," g  "," h  ",""};
        String[] ranks = {"","1", "2", "3", "4", "5","6","7","8",""};
        for (int i = 0; i < 10; i++){
            chessboardWhite[i][0]=ranks[i];
            chessboardWhite[i][9]=ranks[i];
            for(int j = 0; j< 10; j++){
                chessboardWhite[0][j] = labels[j];
                chessboardWhite[9][j]=labels[j];

                if (i != 0 && i != 9 && j != 0 && j!= 9){
                    chessboardWhite[i][j] = EscapeSequences.EMPTY;
                }

            }

        }
        chessboardWhite[8][1]= EscapeSequences.WHITE_ROOK;
        chessboardWhite[8][2]= EscapeSequences.WHITE_KNIGHT;
        chessboardWhite[8][3]= EscapeSequences.WHITE_BISHOP;
        chessboardWhite[8][4]= EscapeSequences.WHITE_QUEEN;
        chessboardWhite[8][5]= EscapeSequences.WHITE_KING;
        chessboardWhite[8][6]= EscapeSequences.WHITE_BISHOP;
        chessboardWhite[8][7]= EscapeSequences.WHITE_KNIGHT;
        chessboardWhite[8][8]= EscapeSequences.WHITE_ROOK;
        for (int i = 1; i <9; i++){
            chessboardWhite[7][i]=EscapeSequences.WHITE_PAWN;
        }
        for (int i = 1; i <9; i++){
            chessboardWhite[2][i]=EscapeSequences.BLACK_PAWN;
        }
        chessboardWhite[1][1]= EscapeSequences.BLACK_ROOK;
        chessboardWhite[1][2]= EscapeSequences.BLACK_KNIGHT;
        chessboardWhite[1][3]= EscapeSequences.BLACK_BISHOP;
        chessboardWhite[1][4]= EscapeSequences.BLACK_QUEEN;
        chessboardWhite[1][5]= EscapeSequences.BLACK_KING;
        chessboardWhite[1][6]= EscapeSequences.BLACK_BISHOP;
        chessboardWhite[1][7]= EscapeSequences.BLACK_KNIGHT;
        chessboardWhite[1][8]= EscapeSequences.BLACK_ROOK;
        return chessboardWhite;
    }

public ChessPiece[][] getRealBoard(){
    ChessPiece[][] chessboard= new ChessPiece[8][8];
    for(int i = 0; i <8; i++){
        for(int j = 0; j <8; j++){
            chessboard[i][j]= new chess.ChessBoard().getPiece(new ChessPosition(i,j));
        }
    }
    return chessboard;
}

//public String translator(ChessPiece piece){
//        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK){
//            if (piece.getPieceType() == ChessPiece.PieceType.BISHOP){
//                return EscapeSequences.BLACK_BISHOP;
//            }
//        }
//}

//    public String[][] initializeBoardWhiteForCustomGame(){
//        String[][] chessboardWhite= new String[10][10];
//        String[] labels = {"","  a  ", " b  ", " c  ", "d  ", " e  ","f  "," g  "," h  ",""};
//        String[] ranks = {"","8", "7", "6", "5", "4","3","2","1",""};
//        ChessPiece[][] realBoardHere = getRealBoard();
//        for (int i = 0; i < 10; i++){
//            chessboardWhite[i][0]=ranks[i];
//            chessboardWhite[i][9]=ranks[i];
//            for(int j = 0; j< 10; j++){
//                chessboardWhite[0][j] = labels[j];
//                chessboardWhite[9][j]=labels[j];
//                if (i != 0 && i != 9 && j != 0 && j!= 9){
//
//                    chessboardWhite[i][j] = translator(realBoardHere[i][j]);
//
//                    chessboardWhite[i][j] = EscapeSequences.EMPTY;
//                    // get piece here for custom board maybe
//                }
//            }
//        }
//
//
//
//        return chessboardWhite;
//    }

    public String[][] initializeBoardWhite(){
        String[][] chessboardWhite= new String[10][10];
        String[] labels = {"","  a  ", " b  ", " c  ", "d  ", " e  ","f  "," g  "," h  ",""};
        String[] ranks = {"","8", "7", "6", "5", "4","3","2","1",""};
        for (int i = 0; i < 10; i++){
            chessboardWhite[i][0]=ranks[i];
            chessboardWhite[i][9]=ranks[i];
            for(int j = 0; j< 10; j++){
                chessboardWhite[0][j] = labels[j];
                chessboardWhite[9][j]=labels[j];

                if (i != 0 && i != 9 && j != 0 && j!= 9){
                    chessboardWhite[i][j] = EscapeSequences.EMPTY;
                    // get piece here for custom board maybe
                }

            }

        }
        chessboardWhite[1][1]= EscapeSequences.WHITE_ROOK;
        chessboardWhite[1][2]= EscapeSequences.WHITE_KNIGHT;
        chessboardWhite[1][3]= EscapeSequences.WHITE_BISHOP;
        chessboardWhite[1][4]= EscapeSequences.WHITE_QUEEN;
        chessboardWhite[1][5]= EscapeSequences.WHITE_KING;
        chessboardWhite[1][6]= EscapeSequences.WHITE_BISHOP;
        chessboardWhite[1][7]= EscapeSequences.WHITE_KNIGHT;
        chessboardWhite[1][8]= EscapeSequences.WHITE_ROOK;
        for (int i = 1; i <9; i++){
            chessboardWhite[2][i]=EscapeSequences.WHITE_PAWN;
        }
        for (int i = 1; i <9; i++){
            chessboardWhite[7][i]=EscapeSequences.BLACK_PAWN;
        }
        chessboardWhite[8][1]= EscapeSequences.BLACK_ROOK;
        chessboardWhite[8][2]= EscapeSequences.BLACK_KNIGHT;
        chessboardWhite[8][3]= EscapeSequences.BLACK_BISHOP;
        chessboardWhite[8][4]= EscapeSequences.BLACK_QUEEN;
        chessboardWhite[8][5]= EscapeSequences.BLACK_KING;
        chessboardWhite[8][6]= EscapeSequences.BLACK_BISHOP;
        chessboardWhite[8][7]= EscapeSequences.BLACK_KNIGHT;
        chessboardWhite[8][8]= EscapeSequences.BLACK_ROOK;
        return chessboardWhite;
    }
    public String makeChessBoard(String[][] chessBoardWhite){
        StringBuilder retVal = new StringBuilder();
        for (int i = 0; i <10; i++) {
            retVal.append("\n");
            for (int j = 0; j < 10; j ++){
                if (i == 0 || i == 9 || j == 0 || j == 9){
                    retVal.append(chessBoardWhite[i][j]);
                }
                if (i != 0 && i != 9 && j != 0 && j != 9) {
                    if ((i + j) % 2 == 0) {
                        retVal.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                        retVal.append(chessBoardWhite[i][j]);
                        retVal.append(EscapeSequences.RESET_TEXT_COLOR);
                        retVal.append(EscapeSequences.RESET_BG_COLOR);
                    } else {
                        retVal.append(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                        retVal.append(chessBoardWhite[i][j]);
                        retVal.append(EscapeSequences.RESET_TEXT_COLOR);
                        retVal.append(EscapeSequences.RESET_BG_COLOR);
                    }
                }
            }
        }
        retVal.append(EscapeSequences.RESET_TEXT_COLOR);
        retVal.append(EscapeSequences.RESET_BG_COLOR);
        return retVal.toString();
    }
public void findIDPlayHelperHelper(ArrayList<LinkedTreeMap> gamesInGameList ){
    for (int i = 0; i < gamesInGameList.size(); i++) {
        LinkedTreeMap hopeGame = gamesInGameList.get(i);
        Object shouldbeName = hopeGame.get("gameName");
        String newShouldBeName = (String) shouldbeName;
        System.out.println((i + 1) +" "+ newShouldBeName);
        if (hopeGame.get("whiteUsername") != null) {
            Object whiteUsername = hopeGame.get("whiteUsername");
            String newWhiteUsername = (String) whiteUsername;
            System.out.println((i + 1)+" " + newWhiteUsername);
        } else {
            System.out.println((i + 1) +" "+ "You can be registered as the white player in this game.");
        }
        if (hopeGame.get("blackUsername") != null) {
            Object blackUsername = hopeGame.get("blackUsername");
            String newBlackUsername = (String) blackUsername;
            System.out.println((i + 1) +" "+ newBlackUsername);
        } else {
            System.out.println((i + 1) +" "+ "You can be registered as the black player in this game.");
        }
    }
}
    public TransitoryGameData findIDPlayHelper() throws Exception {
        String listofGames = new serverFacade().listGame(authToken);
        Map gameDataInfoArray = new Gson().fromJson(listofGames, Map.class);
        ArrayList<LinkedTreeMap> gamesInGameList = (ArrayList<LinkedTreeMap>) gameDataInfoArray.get("games");
        System.out.println("Here are the games!");
        String[] color = null;
        int playGameInputs = 0;
        findIDPlayHelperHelper(gamesInGameList);
        System.out.println("Please input the game number you would like. Then the chess color. One at a time.");
        try{playGameInputs = new preloginUI().getInputInt();} catch (Exception e) {
            System.out.println("Please input a valid integer");
            playGameInputs = new preloginUI().getInputInt();
        }
        color = new preloginUI().getInput();
        if (color.length != 1) {
            System.out.println("Wrong num of values.");
            color = new preloginUI().getInput();
        }
        TransitoryGameData ret = new TransitoryGameData(playGameInputs, color[0]);
        return ret;
    }
    public int findIDObserver() throws Exception {
        String listofGames = new serverFacade().listGame(authToken);
        Map gameDataInfoArray = new Gson().fromJson(listofGames, Map.class);
        ArrayList<LinkedTreeMap> gamesInGameList = (ArrayList<LinkedTreeMap>) gameDataInfoArray.get("games");
        System.out.println("Here are the games!");
        int playGameInputs = 0;
        findIDPlayHelperHelper(gamesInGameList);
        System.out.println("Please input the game number you would like. Then the chess color. One at a time.");
        try{playGameInputs = new preloginUI().getInputInt();} catch (Exception e) {
            System.out.println("Please input a valid integer");
            playGameInputs = new preloginUI().getInputInt();
        }
        return playGameInputs;

    }

    public String playGamePostLogin() throws Exception {
        TransitoryGameData retted = findIDPlayHelper();

        String listofGames = new serverFacade().listGame(authToken);
        Map gameDataInfoArray = new Gson().fromJson(listofGames, Map.class);
        ArrayList<LinkedTreeMap> gamesInGameList = (ArrayList<LinkedTreeMap>) gameDataInfoArray.get("games");
        LinkedTreeMap correctGame = null;
        boolean validInput = false;

        try{
            while(!validInput) {
                if (retted.gameID() >= 1 && retted.gameID() <= gamesInGameList.size()) {
                    validInput = true;
                    correctGame = gamesInGameList.get(retted.gameID() - 1);
                } else {
                    System.out.println("Please input a valid integer");
                    int retry = new preloginUI().getInputInt();
                    if(retry >= 1 && retry <= gamesInGameList.size()){
                        validInput = true;
                        correctGame = gamesInGameList.get(retry - 1);
                    }


                }
            }
        } catch (Exception e) {
                System.out.println("Please input a valid integer");

        }

        Object correctGameID = correctGame.get("gameID");
        double newCorrectGameID = (double)correctGameID;
        TransitoryGameData newGameDataReal = new TransitoryGameData((int) newCorrectGameID, retted.playerColor().toUpperCase());
    if(correctGame.get("whiteUsername") == null){
        if (Objects.equals(newGameDataReal.playerColor(), "WHITE")){
            var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
            out.print(makeChessBoard(initializeBoardWhite()));
        }
    }
        if(correctGame.get("blackUsername") == null){
         if (Objects.equals(newGameDataReal.playerColor(), "BLACK")){
            var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
            out.print(makeChessBoard(initializeBoardBlack()));
        }
        }
        try{new serverFacade().playGame(newGameDataReal, authToken);} catch (Exception e) {
            boolean newValidInput = false;
            while (!newValidInput){
            System.out.println("Please input a different color");
           String[] newColor = new preloginUI().getInput();
           while (newColor.length != 1){
               System.out.println("Please input a different color");
               newColor = new preloginUI().getInput();
           }
           if(correctGame.get("blackUsername") == null && newColor[0].toUpperCase().equals("BLACK")){
               newValidInput = true;
               var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
               out.print(makeChessBoard(initializeBoardBlack()));
               newGameDataReal = new TransitoryGameData((int) newCorrectGameID, newColor[0].toUpperCase());
               new serverFacade().playGame(newGameDataReal, authToken);
           }
                if(correctGame.get("whiteUsername") == null && newColor[0].toUpperCase().equals("WHITE")){
                    newValidInput = true;
                    var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
                    out.print(makeChessBoard(initializeBoardWhite()));
                    newGameDataReal = new TransitoryGameData((int) newCorrectGameID, newColor[0].toUpperCase());
                    new serverFacade().playGame(newGameDataReal, authToken);
                }


        }
        }
        return "game joined!";
    }

    public String logoutUser() throws Exception {
        new serverFacade().logoutUser(authToken);
        return "You are now logged out";
    }

    public void observeGame() throws Exception {
        int input = findIDObserver();
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(makeChessBoard(initializeBoardWhite()));
    }
}
