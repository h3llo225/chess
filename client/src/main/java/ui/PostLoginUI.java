package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;
import model.GameData;
import model.TransitoryGameData;
import websocket.ServerFacadeWebsocket;
import websocket.commands.UserGameCommand;


import java.util.*;

import static ui.DisplayLogic.*;

public class PostLoginUI {


    public static String helpPostLogin(){
        return """
                Logout
                Create Game(game name)
                List Games
                Play Game(WHITE/BLACK, gameID)
                Observe Game(?, gameID)
                """;
    }

    public static String getChoicePostLogin() throws Exception {
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
                case "help", "Help"->{
                    System.out.println(helpPostLogin());
                    yield "help";
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
        //System.out.println(serverFacade.listGame(authToken));
        String json = serverFacade.listGame(authToken);
        JsonParser parser = new JsonParser();
        JsonElement jsonItem = parser.parseString(json);
        JsonObject newJsonObject = jsonItem.getAsJsonObject();
        JsonArray gamesList = newJsonObject.get("games").getAsJsonArray();
        ArrayList<GameData> newGameDataList = null;
        for (int i = 0; i < gamesList.size(); i++){
            JsonObject placeholder = gamesList.get(i).getAsJsonObject();
            System.out.print(" Game name: " + placeholder.get("gameName").getAsString());
            if (placeholder.get("whiteUsername")== null && placeholder.get("blackUsername")== null){
                System.out.println(" Game ID: " +placeholder.get("gameID").getAsInt());
            } else if ((placeholder.get("whiteUsername")== null && placeholder.get("blackUsername")!= null)) {
                System.out.print(" Black player: "+ placeholder.get("blackUsername").getAsString());
                System.out.println(" Game ID: " +placeholder.get("gameID").getAsInt());
            }
            else if ((placeholder.get("whiteUsername")!= null && placeholder.get("blackUsername")== null)) {
                System.out.print(" White player: "+ placeholder.get("whiteUsername").getAsString());
                System.out.println(" Game ID: " +placeholder.get("gameID").getAsInt());
            }
            else if ((placeholder.get("whiteUsername")!= null && placeholder.get("blackUsername")!= null)) {
                System.out.print(" White player: "+ placeholder.get("whiteUsername").getAsString());
                System.out.print(" Black player: "+ placeholder.get("blackUsername").getAsString());
                System.out.println(" Game ID: " +placeholder.get("gameID").getAsInt());
            }
        }

        return serverFacade.listGame(authToken);
    }

    public String createGamePostLogin() throws Exception {
        String createGameInputs = Arrays.toString(new PreloginUI().getInput());

        GameData newGameChess = new GameData(0,null,null,createGameInputs,new ChessGame());

        serverFacade.createGame(newGameChess,authToken);
        return "game created!";
    }








public String translator(ChessPiece piece){
        if (piece != null){
        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK){
            if (piece.getPieceType() == ChessPiece.PieceType.BISHOP){
                return EscapeSequences.BLACK_BISHOP;
            }
            if (piece.getPieceType() == ChessPiece.PieceType.KING){
                return EscapeSequences.BLACK_KING;
            }
            if (piece.getPieceType() == ChessPiece.PieceType.QUEEN){
                return EscapeSequences.BLACK_QUEEN;
            }
            if (piece.getPieceType() == ChessPiece.PieceType.ROOK){
                return EscapeSequences.BLACK_ROOK;
            }
            if (piece.getPieceType() == ChessPiece.PieceType.PAWN){
                return EscapeSequences.BLACK_PAWN;
            }
            if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT){
                return EscapeSequences.BLACK_KNIGHT;
            }
        }
    if (piece.getTeamColor() == ChessGame.TeamColor.WHITE){
        if (piece.getPieceType() == ChessPiece.PieceType.BISHOP){
            return EscapeSequences.WHITE_BISHOP;
        }
        if (piece.getPieceType() == ChessPiece.PieceType.KING){
            return EscapeSequences.WHITE_KING;
        }
        if (piece.getPieceType() == ChessPiece.PieceType.QUEEN){
            return EscapeSequences.WHITE_QUEEN;
        }
        if (piece.getPieceType() == ChessPiece.PieceType.ROOK){
            return EscapeSequences.WHITE_ROOK;
        }
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN){
            return EscapeSequences.WHITE_PAWN;
        }
        if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT){
            return EscapeSequences.WHITE_KNIGHT;
        }
    }
        }
    return EscapeSequences.EMPTY;
}


    public String[][] initializeBoardWhiteForCustomGame(ChessPiece[][] game){
        String[][] chessboardWhite= new String[10][10];
        String[] labels = {"","  a  ", " b  ", " c  ", "d  ", " e  ","f  "," g  "," h  ",""};
        String[] ranks = {"","8", "7", "6", "5", "4","3","2","1",""};
        for (int i = 9; i >=0; i--){
            chessboardWhite[i][0]=ranks[i];
            chessboardWhite[i][9]=ranks[i];
            for(int j = 0; j< 10; j++){
                chessboardWhite[0][j] = labels[j];
                chessboardWhite[9][j]=labels[j];

                if (i != 0 && i != 9 && j != 0 && j!= 9){
                    chessboardWhite[i][j] = translator(game[8-i][j-1]);
                    //chessboardWhite[i][j] = EscapeSequences.EMPTY;
                    // get piece here for custom board maybe
                }
            }
        }



        return chessboardWhite;
    }


    public String[][] initializeBoardBlackForCustomGame(ChessPiece[][] game){
        String[][] chessboardBlack= new String[10][10];
        String[] labels = {"","  h  ", " g  ", " f  ", "e  ", " d  ","c  "," b  "," a  ",""};
        String[] ranks = {"","1", "2", "3", "4", "5","6","7","8",""};
        for (int i = 9; i >=0; i--){
            chessboardBlack[i][0]=ranks[i];
            chessboardBlack[i][9]=ranks[i];
            for(int j = 0; j< 10; j++){
                chessboardBlack[0][j] = labels[j];
                chessboardBlack[9][j]=labels[j];

                if (i != 0 && i != 9 && j != 0 && j!= 9){
                    chessboardBlack[i][j] = translator(game[i-1][8-j]);
                    //chessboardWhite[i][j] = EscapeSequences.EMPTY;
                    // get piece here for custom board maybe
                }
            }
        }
        return chessboardBlack;
    }

    public void makeChessBoardHelperHelper(int i, int j, StringBuilder retVal, String[][] chessBoardWhite){
        if (i == 0 || i == 9 || j == 0 || j == 9){
            retVal.append(chessBoardWhite[i][j]);
        }
    }

    public void makeChessBoardHelperHelperHelper(int i, int j, StringBuilder retVal,
                                                 String[][] chessBoardWhite, int[][] pos, String isLightGreyOrNot){

        if (pos[i][j] == 1){
                retVal.append(EscapeSequences.SET_BG_COLOR_YELLOW);
                retVal.append(chessBoardWhite[i][j]);
                retVal.append(EscapeSequences.RESET_TEXT_COLOR);
                retVal.append(EscapeSequences.RESET_BG_COLOR);
            }else if(Objects.equals(isLightGreyOrNot, "LightGrey")){
                retVal.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                retVal.append(chessBoardWhite[i][j]);
                retVal.append(EscapeSequences.RESET_TEXT_COLOR);
                retVal.append(EscapeSequences.RESET_BG_COLOR);
            } else if(Objects.equals(isLightGreyOrNot, "DarkGreen")){
                retVal.append(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                retVal.append(chessBoardWhite[i][j]);
                retVal.append(EscapeSequences.RESET_TEXT_COLOR);
                retVal.append(EscapeSequences.RESET_BG_COLOR);

            }

    }
    public void makeChessBoardHelperHelperHelperHelper(int i, int j, StringBuilder retVal, String[][] chessBoardWhite,
                                                       Collection<ChessMove> moves){
        if (moves == null) {
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

public StringBuilder makeChessBoardHelper(StringBuilder retVal, String[][] chessBoardWhite,
                                          int[][] pos, Collection<ChessMove> moves){
    for (int i = 0; i <10; i++) {
        retVal.append("\n");
        for (int j = 0; j < 10; j ++){

            makeChessBoardHelperHelper(i,j,retVal,chessBoardWhite);
            if (i != 0 && i != 9 && j != 0 && j != 9) {
                makeChessBoardHelperHelperHelperHelper(i, j, retVal, chessBoardWhite, moves);

                  if (moves != null && (i + j) % 2 == 0) {
                    makeChessBoardHelperHelperHelper(i, j, retVal, chessBoardWhite, pos, "LightGrey");

                } else if (moves != null && !((i + j) % 2 == 0)){
                    makeChessBoardHelperHelperHelper(i, j, retVal, chessBoardWhite, pos, "DarkGreen");
                }
            }
        }
    }

    return retVal;
}
    public String makeChessBoardWhite(String[][] chessBoardWhite, Collection<ChessMove> moves){
        StringBuilder retVal = new StringBuilder();
        int[][] pos = new int[10][10];
        if (moves != null){
        for (ChessMove move : moves){
            pos[9-move.getEndPosition().getRow()][move.getEndPosition().getColumn()]= 1;
        }
        }
        makeChessBoardHelper(retVal, chessBoardWhite, pos, moves);
        retVal.append(EscapeSequences.RESET_TEXT_COLOR);
        retVal.append(EscapeSequences.RESET_BG_COLOR);
        return retVal.toString();
    }

    public String makeChessBoardBlack(String[][] chessBoardWhite, Collection<ChessMove> moves){
        StringBuilder retVal = new StringBuilder();
        int[][] pos = new int[10][10];
        if (moves != null){
            for (ChessMove move : moves){
                pos[move.getEndPosition().getRow()][9-move.getEndPosition().getColumn()]= 1;
            }
        }
        makeChessBoardHelper(retVal, chessBoardWhite, pos, moves);
        retVal.append(EscapeSequences.RESET_TEXT_COLOR);
        retVal.append(EscapeSequences.RESET_BG_COLOR);
        return retVal.toString();
    }


    public TransitoryGameData findIDPlayHelper() throws Exception {
        String listofGames = serverFacade.listGame(authToken);
        Map gameDataInfoArray = new Gson().fromJson(listofGames, Map.class);
        ArrayList<LinkedTreeMap> gamesInGameList = (ArrayList<LinkedTreeMap>) gameDataInfoArray.get("games");
        System.out.println("Here are the games!");
        String[] color = null;
        int playGameInputs = 0;
        new PostLoginUIHelperClasses().findIDPlayHelperHelper(gamesInGameList);
        System.out.println("Please input the game number you would like. Then the chess color. One at a time.");
        try{playGameInputs = new PreloginUI().getInputInt();
            while(gamesInGameList.get(playGameInputs-1).get("whiteUsername") != null &&
                    gamesInGameList.get(playGameInputs-1).get("blackUsername") != null){
                System.out.println("Please enter a new integer, both white and black are taken.");
                playGameInputs = new PreloginUI().getInputInt();
                if (playGameInputs == 0){
                    return null;
                }
            }
        } catch (Exception e) {
            System.out.println("Please input a valid integer");
            playGameInputs = new PreloginUI().getInputInt();
        }
        color = new PreloginUI().getInput();
        if (color.length != 1) {
            System.out.println("Wrong num of values.");
            color = new PreloginUI().getInput();
        }
        TransitoryGameData ret = new TransitoryGameData(playGameInputs, color[0]);
        return ret;
    }

    public String playGamePostLogin() throws Exception {
        TransitoryGameData retted = findIDPlayHelper();

        if (retted == null || retted.gameID()== 0 || Objects.equals(retted.playerColor(), "quit")){
            return "Execution failed";
        }
        String listofGames = serverFacade.listGame(authToken);
        Map gameDataInfoArray = new Gson().fromJson(listofGames, Map.class);
        ArrayList<LinkedTreeMap> gamesInGameList = (ArrayList<LinkedTreeMap>) gameDataInfoArray.get("games");
        LinkedTreeMap correctGame = null;
        boolean validInput = false;
        try{
            correctGame = new PostLoginUIHelperClasses().helperFuncForCodeQuality(validInput, retted.gameID(), correctGame,gamesInGameList);
        } catch (Exception e) {
                System.out.println("Please input a valid integer");
        }
        Object correctGameID = correctGame.get("gameID");
        double newCorrectGameID = (double)correctGameID;
        TransitoryGameData newGameDataReal = new TransitoryGameData((int) newCorrectGameID, retted.playerColor().toUpperCase());
        String tempMap = new Gson().toJson(correctGame.get("game"));
        JsonParser parser = new JsonParser();
        JsonElement jsonItem = parser.parseString(tempMap);
        JsonObject object = jsonItem.getAsJsonObject();
        ChessGame game = new Gson().fromJson(object, ChessGame.class);
        DisplayLogic.game = game;
        ChessPiece[][] boardPieces = new ChessPiece[8][8];
        for (int i =0; i<8; i++){
            for (int j =0; j<8; j++){
                boardPieces[i][j] = game.getBoard().getPiece(new ChessPosition(i+1,j+1));
            }
        }
        try{
        } catch (Exception e) {
            boolean newValidInput = false;
            while (!newValidInput){
            System.out.println("Please input a different color");
           String[] newColor = new PreloginUI().getInput();
           while (newColor.length != 1){
               System.out.println("Please input a different color");
               newColor = new PreloginUI().getInput();
           }
           if((correctGame.get("blackUsername") == null || correctGame.get("blackUsername") == username)
                   && newColor[0].toUpperCase().equals("BLACK")){
               newValidInput = true;
               newGameDataReal = new TransitoryGameData((int) newCorrectGameID, newColor[0].toUpperCase());
           }

           if((correctGame.get("whiteUsername") == null || correctGame.get("whiteUsername") == username)
                        && newColor[0].toUpperCase().equals("WHITE")){
                    newValidInput = true;
                    newGameDataReal = new TransitoryGameData((int) newCorrectGameID, newColor[0].toUpperCase());
                }
        }
        }
        serverFacade.playGame(newGameDataReal, authToken);
        UserGameCommand joining = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, newGameDataReal.gameID());
        ServerFacadeWebsocket.session.getBasicRemote().sendText(new Gson().toJson(joining));

        gameUI.displayPlayGame((int) newCorrectGameID);
        return "";
    }

    public String logoutUser() throws Exception {
        serverFacade.logoutUser(authToken);
        return "You are now logged out";
    }

    public void observeGame() throws Exception {
        int id = new PostLoginUIHelperClasses().findIDObserver();
        DisplayLogic.isObserver = true;
        gameUI.displayPlayGame(id);
    }
}
