package ui;

import chess.*;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import model.GameData;
import websocket.ServerFacadeWebsocket;
import websocket.commands.MakeMoveGameCommand;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static ui.DisplayLogic.*;

public class DisplayLogicPlayGame {
    public int gameInfo;
    public boolean resignedGame = false;
    public ChessGame.TeamColor playerColor;

    static public void displayOptions(){
        System.out.println("""
                leave
                resign
                make move
                help
                draw board
                highlight moves
                """);
    }
    public String[] getInputPlayGame(){
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            return line.split(" ");
        }
    }
    public String helpPlayGame(){
        return """

                make move(startPos, endPos)
                leave
                resign
                help
                (Observers may not do make move or resign)
                """;
    }

    public String getChoicePlayGame(){
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            return switch (line) {
                case "leave", "Leave" -> {
                    System.out.println("You have chosen to leave!");
                    yield "leave";
                }
                case "resign", "Resign" -> {
                    //System.out.println("You have chosen to resign");
                    yield "resign";
                }
                case "make move", "Make move" -> {
                    //System.out.println("You have chosen to make a move");
                    yield "make move";
                }
                case "highlight board", "Highlight board" -> "highlight board";
                case "draw board", "Draw board" -> "draw board";
                case "help", "Help" -> "help";
                default -> "invalid choice";
            };
        }

    }
    public void displayPlayGame(int newCorrectGameID, String gameDataInfo) throws InvalidMoveException, IOException {
        String resultOfChoice = "";
        if (gameDataInfo != null){
        if (Objects.equals(gameDataInfo.toUpperCase(), "WHITE")) {
            this.playerColor = ChessGame.TeamColor.WHITE;
        }else if(Objects.equals(gameDataInfo.toUpperCase(), "BLACK")){
            this.playerColor = ChessGame.TeamColor.BLACK;
        }
        }
        this.gameInfo = newCorrectGameID;
        //DisplayLogicPlayGame item = new DisplayLogicPlayGame();
        System.out.println("You have joined the game, here are your options! (Observers may only do help or leave.)");
        displayOptions();
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.println();
        while(!Objects.equals(resultOfChoice, "leave")) {
            //out.print(outputBoard);
            resultOfChoice = gameUI.getChoicePlayGame();
            if (Objects.equals(resultOfChoice, "invalid choice")) {
                System.out.println("Invalid command, here are the commands again!");
            }
            if (DisplayLogic.isObserver == false){
            if (Objects.equals(resultOfChoice, "make move")
                    || Objects.equals(resultOfChoice, "help") || Objects.equals(resultOfChoice, "resign") ||
                    Objects.equals(resultOfChoice, "draw board") || Objects.equals(resultOfChoice, "highlight board")
                    ){
                if (game.isInCheckmate(game.getTeamTurn()) || game.isInStalemate(game.getTeamTurn())){
                    stateGameCheckMate = true;
                    //System.out.println("The game is over now.");
                }
                if (resignedGame || stateGameCheckMate == true){
                    System.out.println("The game is over now.");
                }
                else{
                    gameUI.playGame(resultOfChoice);
                }
            }
        }else {
                if (Objects.equals(resultOfChoice, "help") || Objects.equals(resultOfChoice, "draw board")
                        || Objects.equals(resultOfChoice, "highlight board")){
                    gameUI.playGame(resultOfChoice);
                }else{
                    System.out.println("As an observer you do not have access to commands besides leave or help");
                }
            }
        }
        UserGameCommand leaving = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameInfo);
        ServerFacadeWebsocket.session.getBasicRemote().sendText(new Gson().toJson(leaving));
        System.out.println("You have left the game!");
    }
    public void drawBoard(){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        if (playerColor == ChessGame.TeamColor.BLACK) {
            out.print(post.makeChessBoardBlack(post.initializeBoardBlackForCustomGame
                    (game.getBoard().getBoard()), null));
        } else if (playerColor == ChessGame.TeamColor.WHITE) {
            out.print(post.makeChessBoardWhite(post.initializeBoardWhiteForCustomGame(game.getBoard().getBoard()), null));
        }else{
            out.print(post.makeChessBoardWhite(post.initializeBoardWhiteForCustomGame(game.getBoard().getBoard()), null));
        }
    }
    public void highlightMoves(MakeMoveType startPos){
        Map<String, Integer> translatorCol = new HashMap<>();
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        translatorCol.put("a", 1);
        translatorCol.put("b", 2);
        translatorCol.put("c", 3);
        translatorCol.put("d", 4);
        translatorCol.put("e", 5);
        translatorCol.put("f", 6);
        translatorCol.put("g", 7);
        translatorCol.put("h", 8);
        ChessPosition pos = new ChessPosition(startPos.row, translatorCol.get(startPos.col));
        if (playerColor == ChessGame.TeamColor.BLACK) {
            out.print(post.makeChessBoardBlack(post.initializeBoardBlackForCustomGame
                    (game.getBoard().getBoard()), game.validMoves(pos)));
        } else if ((playerColor == ChessGame.TeamColor.WHITE)) {
            out.print(post.makeChessBoardWhite(post.initializeBoardWhiteForCustomGame(game.getBoard().getBoard()), game.validMoves(pos)));
        }else{
            out.print(post.makeChessBoardWhite(post.initializeBoardWhiteForCustomGame(game.getBoard().getBoard()), game.validMoves(pos)));
        }
    }
    public String makeMove(MakeMoveType startPos, MakeMoveType endPos) throws InvalidMoveException, IOException {
        Map<String, Integer> translatorCol = new HashMap<>();
        translatorCol.put("a", 1);
        translatorCol.put("b", 2);
        translatorCol.put("c", 3);
        translatorCol.put("d", 4);
        translatorCol.put("e", 5);
        translatorCol.put("f", 6);
        translatorCol.put("g", 7);
        translatorCol.put("h", 8);
        int translatedStartPosCol = 0;
        int translatedEndPosCol = 0;
        translatedStartPosCol = translatorCol.get(startPos.col);
        translatedEndPosCol = translatorCol.get(endPos.col);
        ChessPosition posStartPos = new ChessPosition(startPos.row,translatedStartPosCol);
        ChessPosition posEndPos = new ChessPosition(endPos.row,translatedEndPosCol);
        if (game.getBoard().getPiece(posStartPos) != null){
        if ((posEndPos.getRow() == 8 && game.getBoard().getPiece(posStartPos).pieceType ==
                ChessPiece.PieceType.PAWN && game.getBoard().getPiece(posStartPos).getTeamColor() ==
                ChessGame.TeamColor.WHITE) || posEndPos.getRow() == 1 && game.getBoard().getPiece(posStartPos).pieceType ==
                ChessPiece.PieceType.PAWN && game.getBoard().getPiece(posStartPos).getTeamColor() ==
                ChessGame.TeamColor.BLACK){
            String[] promotion = null;
            for (ChessPiece.PieceType typeOfPiece : ChessPiece.PieceType.values()) {
                if (typeOfPiece != ChessPiece.PieceType.KING && typeOfPiece != ChessPiece.PieceType.PAWN) {
                    System.out.println("Please select a piece to promote to: " + typeOfPiece);
                }
            }
                promotion = getInputPlayGame();
            while(!Objects.equals(promotion[0].toUpperCase(), "BISHOP") && !Objects.equals(promotion[0].toUpperCase(), "ROOK") &&
                    !Objects.equals(promotion[0].toUpperCase(), "KNIGHT") && !Objects.equals(promotion[0].toUpperCase(), "QUEEN")){
                System.out.println("Please input a different piece type");
                promotion = getInputPlayGame();
            }
            if (Objects.equals(promotion[0].toUpperCase(), "QUEEN")){
                game.makeMove(new ChessMove(posStartPos,posEndPos, ChessPiece.PieceType.QUEEN));
                                MakeMoveGameCommand makeMove = new MakeMoveGameCommand(MakeMoveGameCommand.CommandType.MAKE_MOVE,
                                        authToken, gameInfo,new ChessMove(posStartPos,posEndPos,ChessPiece.PieceType.QUEEN));
                ServerFacadeWebsocket.session.getBasicRemote().sendText(new Gson().toJson(makeMove));
            }
            if (Objects.equals(promotion[0].toUpperCase(), "KNIGHT")){
                game.makeMove(new ChessMove(posStartPos,posEndPos, ChessPiece.PieceType.KNIGHT));
                         MakeMoveGameCommand makeMove = new MakeMoveGameCommand(MakeMoveGameCommand.CommandType.MAKE_MOVE,
                                 authToken, gameInfo,new ChessMove(posStartPos,posEndPos,ChessPiece.PieceType.KNIGHT));
                ServerFacadeWebsocket.session.getBasicRemote().sendText(new Gson().toJson(makeMove));
            }
            if (Objects.equals(promotion[0].toUpperCase(), "ROOK")){
                game.makeMove(new ChessMove(posStartPos,posEndPos, ChessPiece.PieceType.ROOK));
                MakeMoveGameCommand makeMove = new MakeMoveGameCommand(MakeMoveGameCommand.CommandType.MAKE_MOVE,
                        authToken, gameInfo,new ChessMove(posStartPos,posEndPos,ChessPiece.PieceType.ROOK));
                ServerFacadeWebsocket.session.getBasicRemote().sendText(new Gson().toJson(makeMove));
            }
            if (Objects.equals(promotion[0].toUpperCase(), "BISHOP")){
                game.makeMove(new ChessMove(posStartPos,posEndPos, ChessPiece.PieceType.BISHOP));             MakeMoveGameCommand makeMove
                        = new MakeMoveGameCommand(MakeMoveGameCommand.CommandType.MAKE_MOVE, authToken, gameInfo,
                        new ChessMove(posStartPos,posEndPos,ChessPiece.PieceType.BISHOP));
                ServerFacadeWebsocket.session.getBasicRemote().sendText(new Gson().toJson(makeMove));
            }
        }else{
        try{game.makeMove(new ChessMove(posStartPos,posEndPos,null));} catch (InvalidMoveException e) {
            return null;
        }
            MakeMoveGameCommand makeMove = new MakeMoveGameCommand(MakeMoveGameCommand.CommandType.MAKE_MOVE,
                    authToken, gameInfo,new ChessMove(posStartPos,posEndPos,null));
            ServerFacadeWebsocket.session.getBasicRemote().sendText(new Gson().toJson(makeMove));
        }
        }
        return "Move made";
    }
    public record MakeMoveType(String col, int row) {
    }
    public MakeMoveType getInputIntStart(String isHighlight) throws InputMismatchException,
            IndexOutOfBoundsException {
        Map<String, Integer> translatorCol = new HashMap<>();
        translatorCol.put("a", 1);
        translatorCol.put("b", 2);
        translatorCol.put("c", 3);
        translatorCol.put("d", 4);
        translatorCol.put("e", 5);
        translatorCol.put("f", 6);
        translatorCol.put("g", 7);
        translatorCol.put("h", 8);
        try{
            System.out.println("Please input a column and a row such as a 1 or d 6 (start pos)");
            String[] startingArray = new String[2];
            Scanner scanner = new Scanner(System.in);
               String input = scanner.nextLine();
               startingArray = input.split(" ");
               int nums = 0;
               try{if (startingArray.length == 2){
                   nums = Integer.parseInt(startingArray[1]);
               }else{
                   System.out.println("Wrong num of args");
                   return null;
               }} catch (NumberFormatException e) {
                   System.out.println("please input an int");
                   return null;
               }
            if (Objects.equals(startingArray[0], "quit") ||nums == 0){
                return new MakeMoveType("quit", 0);
            }
            int translatedPosCol = translatorCol.get(startingArray[0]);
            ChessPosition positionGeneral = new ChessPosition(nums,translatedPosCol);
            if (isHighlight == null){
            while(game.getBoard().getPiece(positionGeneral) == null || (game.getBoard().getPiece(positionGeneral).getTeamColor()
                    != game.getTeamTurn() ) && !isObserver ||
                    (playerColor == ChessGame.TeamColor.WHITE &&
                            game.getBoard().getPiece(positionGeneral).getTeamColor()!= ChessGame.TeamColor.WHITE ||
                            (playerColor == ChessGame.TeamColor.BLACK &&
                                    game.getBoard().getPiece(positionGeneral).getTeamColor()!= ChessGame.TeamColor.BLACK))) {
                System.out.println("Please make sure it is your turn or that you grabbed a piece on the board.");
                 return null;}
            }else if (isHighlight.equals("highlight")){
                while(game.getBoard().getPiece(positionGeneral) == null){
                    System.out.println("Please make sure that you grabbed a piece on the board.");
                    return null;
            }
            }
            try{while(translatorCol.get(startingArray[0]) == null){
                System.out.println("Please input a coordinate with a piece.");
                return null;
            }
            } catch (Exception e) {
                return null;
            }
            if (startingArray.length == 2){
                   return new MakeMoveType(startingArray[0],nums);
               }
        }catch (Exception e){
            System.out.println("Please input a valid integer");
            return null;
        }
        return null;
    }
    public MakeMoveType getInputIntEnd() throws InputMismatchException, IndexOutOfBoundsException {
        Map<String, Integer> translatorCol = new HashMap<>();
        translatorCol.put("a", 1);
        translatorCol.put("b", 2);
        translatorCol.put("c", 3);
        translatorCol.put("d", 4);
        translatorCol.put("e", 5);
        translatorCol.put("f", 6);
        translatorCol.put("g", 7);
        translatorCol.put("h", 8);
        try{
            System.out.println("Please input a column and a row such as a 1 or d 6 (end pos)");
            String[] startingArray = new String[2];
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            startingArray = input.split(" ");
            int nums = 0;
            try{if (startingArray.length == 2){
                nums = Integer.parseInt(startingArray[1]);
            }} catch (NumberFormatException e) {
                System.out.println("please input an int");
                return null;
            }
            if (Objects.equals(startingArray[0], "quit") || nums == 0){
                return new MakeMoveType("quit", 0);
            }
            int translatedPosCol = translatorCol.get(startingArray[0]);
            ChessPosition positionGeneral = new ChessPosition(nums,translatedPosCol);
                if (game.getBoard().getPiece(positionGeneral) != null) {
                    while (game.getBoard().getPiece(positionGeneral).getTeamColor() == game.getTeamTurn()) {
                        System.out.println("Please make sure it is your turn.");
                        return null;
                    }
                }
            try{while(translatorCol.get(startingArray[0]) == null){
                System.out.println("Please input valid coordinates");
                return null;
            }} catch (Exception e) {
                return null;
            }
            if (startingArray.length == 2){
                return new MakeMoveType(startingArray[0],nums);
            }
        }catch (Exception e){
            System.out.println("Please input a valid integer");
            return null;
        }
        return null;
    }
    public String playGame(String resultOfChoice) throws InvalidMoveException, IOException {
        Map<String, Integer> translatorCol = new HashMap<>();
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        translatorCol.put("a", 1);
        translatorCol.put("b", 2);
        translatorCol.put("c", 3);
        translatorCol.put("d", 4);
        translatorCol.put("e", 5);
        translatorCol.put("f", 6);
        translatorCol.put("g", 7);
        translatorCol.put("h", 8);
        if (Objects.equals(resultOfChoice, "invalid choice")){
            //System.out.println("invalid choice");
        }
        if (Objects.equals(resultOfChoice, "make move")) {
            System.out.println("This input will be the position of the piece you want to move.");
            MakeMoveType startPos = null;
            MakeMoveType endPos = null;
            boolean valid = false;
            while (!valid) {
                startPos = playHelper(startPos, "start");
                    if(startPos == null){
                        break;
                    }
            if (startPos != null){
                ChessPosition pos = new ChessPosition(startPos.row, translatorCol.get(startPos.col));
                if (game.getTeamTurn() == ChessGame.TeamColor.BLACK) {
                    out.print(post.makeChessBoardBlack(post.initializeBoardBlackForCustomGame
                            (game.getBoard().getBoard()), game.validMoves(pos)));
                } else if (game.getTeamTurn() == ChessGame.TeamColor.WHITE) {
                    out.print(post.makeChessBoardWhite(post.initializeBoardWhiteForCustomGame
                            (game.getBoard().getBoard()), game.validMoves(pos)));
                }
                endPos = playHelper(endPos, "end");
                if ( endPos == null){
                    break;
                }
                if (endPos != null && Objects.equals(endPos.col, "quit")) {
                    break ;
                } else if (endPos != null && endPos.row == 0) {
                    break;
                }
                if(!checkHelper(endPos,startPos,valid)){
                    endPos = null;
                    startPos = null;
                }else{
                    valid = true;
                }
            }
        }
        }
        resultOfChoice = handleResignAndHelp(resultOfChoice);
        resultOfChoice = new PostLoginUIHelperClasses().handleHighlightAndDrawBoard(resultOfChoice);
        return resultOfChoice;
    }
    public String handleResignAndHelp(String resultOfChoice) throws IOException {
        if (Objects.equals(resultOfChoice, "help")){
            System.out.println(helpPlayGame());
            System.out.println("Please enter a new command! Here is the full list.");
        }
        if (Objects.equals(resultOfChoice, "resign")) {
            System.out.println("Please confirm that you want to resign the game by typing \"confirm\"");
            String[] confirm = getInputPlayGame();
            while (!Objects.equals(confirm[0], "confirm")){
                if (Objects.equals(confirm[0], "quit")){
                    break;
                }
                if (confirm.length != 1){
                    System.out.println("Wrong number of arguments!");
                }
                else{
                    System.out.println("Please try again!");
                    confirm = getInputPlayGame();
                }
            }
            if (confirm[0].equals("confirm")){
                System.out.println("You have resigned the game!");
                UserGameCommand resign = new UserGameCommand(UserGameCommand.CommandType.RESIGN,
                        authToken, gameInfo);
                ServerFacadeWebsocket.session.getBasicRemote().sendText(new Gson().toJson(resign));
                resignedGame = true;
            }
        }
        return resultOfChoice;
    }
public MakeMoveType playHelper(MakeMoveType pos, String startOrEnd){
    while (pos == null){
        System.out.println("Please input valid integers");
        if (Objects.equals(startOrEnd, "start")){
            pos = getInputIntStart(null);
        }
        else if (Objects.equals(startOrEnd, "startHighlight")){
            pos = getInputIntStart("highlight");
        }else{
            pos = getInputIntEnd();
        }
        if (pos != null) {
            if (Objects.equals(pos.col, "quit")) {
                return null;
            }
            else if (pos.row == 0) {
                return null;
            }
        }
    }
    return pos;
}
public boolean checkHelper(MakeMoveType endPos, MakeMoveType startPos, boolean valid)
        throws IOException, InvalidMoveException {
        if (endPos != null && startPos != null){
        ChessBoard prevGame = game.board;
        ChessGame.TeamColor moverColor = game.getTeamTurn();
        if (makeMove(startPos, endPos) == null) {
            ChessGame.TeamColor newColor = game.getTeamTurn();
        }
        else{
            valid = true;
        }
        if (game.isInCheck(moverColor)){
            System.out.println("It looks like you may be in check please make a different move. ");
            valid = false;
            game.board = prevGame;
            game.setTeamTurn(moverColor);
        }
    }
        return valid;
}
}
