package ui;

import chess.*;
import com.google.gson.Gson;
import model.GameData;
import websocket.ServerFacadeWebsocket;
import websocket.commands.MakeMoveGameCommand;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static ui.DisplayLogic.*;
import static ui.PostLoginUI.*;

public class DisplayLogicPlayGame {
    public int gameInfo;
    public boolean resignedGame = false;

    static public void displayOptions(){
        System.out.println("""
                leave
                resign
                make move
                help
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

                make move(username, password, email)
                leave
                resign
                help
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
                case "help", "Help" -> "help";
                default -> "invalid choice";
            };
        }

    }
    public void displayPlayGame(int newCorrectGameID) throws InvalidMoveException, IOException {
        String resultOfChoice = "";
        this.gameInfo = newCorrectGameID;
        //DisplayLogicPlayGame item = new DisplayLogicPlayGame();
        System.out.println("You have joined the game, here are your options!");
        displayOptions();
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.println();
        while(!Objects.equals(resultOfChoice, "leave")) {
            //out.print(outputBoard);
            resultOfChoice = gameUI.getChoicePlayGame();
            if (Objects.equals(resultOfChoice, "invalid choice")) {
                System.out.println("Invalid command, here are the commands again!");
            }
            if (Objects.equals(resultOfChoice, "make move")
                    || Objects.equals(resultOfChoice, "help") || Objects.equals(resultOfChoice, "resign")){
                if (resignedGame || stateGameCheckMate == true){
                    System.out.println("The game is over now.");
                }
                else{
                    if (game.isInCheckmate(ChessGame.TeamColor.WHITE) || game.isInStalemate(ChessGame.TeamColor.WHITE) || game.isInCheckmate(ChessGame.TeamColor.BLACK) || game.isInStalemate(ChessGame.TeamColor.BLACK)){
                        stateGameCheckMate = true;
                        System.out.println("The game is over now.");
                    }else{
                        gameUI.playGame(resultOfChoice);
                    }
                }
            }
        }
        UserGameCommand leaving = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameInfo);
        ServerFacadeWebsocket.session.getBasicRemote().sendText(new Gson().toJson(leaving));
        System.out.println("You have left the game!");
    }


    public String makeMove(makeMoveType startPos, makeMoveType endPos) throws InvalidMoveException, IOException {
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




//        if(endPos != null){
//            while(translatorCol.get(endPos.col) == null){
//            System.out.println("Please input valid coordinates");
//            endPos = getInputInt();
//        }
//        }

        //start of promotion logic
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
                                MakeMoveGameCommand make_move = new MakeMoveGameCommand(MakeMoveGameCommand.CommandType.MAKE_MOVE, authToken, gameInfo,new ChessMove(posStartPos,posEndPos,ChessPiece.PieceType.QUEEN));
                ServerFacadeWebsocket.session.getBasicRemote().sendText(new Gson().toJson(make_move));
            }
            if (Objects.equals(promotion[0].toUpperCase(), "KNIGHT")){
                game.makeMove(new ChessMove(posStartPos,posEndPos, ChessPiece.PieceType.KNIGHT));
                    //System.out.println("You inputted invalid moves! Please type help for more information");
                         MakeMoveGameCommand make_move = new MakeMoveGameCommand(MakeMoveGameCommand.CommandType.MAKE_MOVE, authToken, gameInfo,new ChessMove(posStartPos,posEndPos,ChessPiece.PieceType.KNIGHT));
                ServerFacadeWebsocket.session.getBasicRemote().sendText(new Gson().toJson(make_move));
            }
            if (Objects.equals(promotion[0].toUpperCase(), "ROOK")){
                game.makeMove(new ChessMove(posStartPos,posEndPos, ChessPiece.PieceType.ROOK));
                MakeMoveGameCommand make_move = new MakeMoveGameCommand(MakeMoveGameCommand.CommandType.MAKE_MOVE, authToken, gameInfo,new ChessMove(posStartPos,posEndPos,ChessPiece.PieceType.ROOK));
                ServerFacadeWebsocket.session.getBasicRemote().sendText(new Gson().toJson(make_move));
            }
            if (Objects.equals(promotion[0].toUpperCase(), "BISHOP")){
                game.makeMove(new ChessMove(posStartPos,posEndPos, ChessPiece.PieceType.BISHOP));             MakeMoveGameCommand make_move = new MakeMoveGameCommand(MakeMoveGameCommand.CommandType.MAKE_MOVE, authToken, gameInfo,new ChessMove(posStartPos,posEndPos,ChessPiece.PieceType.BISHOP));
                ServerFacadeWebsocket.session.getBasicRemote().sendText(new Gson().toJson(make_move));
            }
        }else{
        try{game.makeMove(new ChessMove(posStartPos,posEndPos,null));} catch (InvalidMoveException e) {
            return null;
        }

            MakeMoveGameCommand make_move = new MakeMoveGameCommand(MakeMoveGameCommand.CommandType.MAKE_MOVE, authToken, gameInfo,new ChessMove(posStartPos,posEndPos,null));
            ServerFacadeWebsocket.session.getBasicRemote().sendText(new Gson().toJson(make_move));
        }
        }
        return "Move made";
    }
    public record makeMoveType(String col, int row) {
    }

    public makeMoveType getInputIntStart() throws InputMismatchException, IndexOutOfBoundsException {
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
               }} catch (NumberFormatException e) {
                   System.out.println("please input an int");
                   return null;
               }
            if (Objects.equals(startingArray[0], "quit") ||nums == 0){
                return new makeMoveType("quit", 0);
            }

            int translatedPosCol = translatorCol.get(startingArray[0]);
            ChessPosition positionGeneral = new ChessPosition(nums,translatedPosCol);
            while(game.getBoard().getPiece(positionGeneral) == null ||game.getBoard().getPiece(positionGeneral).getTeamColor() != game.getTeamTurn() ){
                System.out.println("Please input valid coordinates");
                 return null;
            }

            try{while(translatorCol.get(startingArray[0]) == null){
                System.out.println("Please input valid coordinates");
                return null;
            }


            } catch (Exception e) {
                return null;
            }
            if (startingArray.length == 2){
                   return new makeMoveType(startingArray[0],nums);
               }
                //System.out.println("Please input the right number of vals");
        }catch (Exception e){
            System.out.println("Please input a valid integer");
            return null;
        }
        return null;
    }

    public makeMoveType getInputIntEnd() throws InputMismatchException, IndexOutOfBoundsException {
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
                return new makeMoveType("quit", 0);
            }


            int translatedPosCol = translatorCol.get(startingArray[0]);
            ChessPosition positionGeneral = new ChessPosition(nums,translatedPosCol);

                if (game.getBoard().getPiece(positionGeneral) != null) {
                    while (game.getBoard().getPiece(positionGeneral).getTeamColor() == game.getTeamTurn()) {
                        System.out.println("Please input valid coordinates");
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
                return new makeMoveType(startingArray[0],nums);
            }
            //System.out.println("Please input the right number of vals");
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
            makeMoveType startPos = getInputIntStart();
            makeMoveType endPos = null;
            boolean valid = false;
            while (!valid) {
                if (startPos == null) {
                    System.out.println("Please input valid integers");
                    startPos = getInputIntStart();
                    System.out.println("This input will be the position of where you want to move the piece to.");
                    if (startPos != null) {
                        if (Objects.equals(startPos.col, "quit")) {
                            break;
                        } else if (startPos.row == 0) {
                            break;
                        }
                    }
                }
                if (startPos != null && Objects.equals(startPos.col, "quit")) {
                    break;
                } else if (startPos != null && startPos.row == 0) {
                    break;
                }
                if (startPos != null){
                ChessPosition pos = new ChessPosition(startPos.row, translatorCol.get(startPos.col));
                if (game.getTeamTurn() == ChessGame.TeamColor.BLACK) {
                    out.print(post.makeChessBoardBlack(post.initializeBoardBlackForCustomGame(game.getBoard().getBoard()), game.validMoves(pos)));
                } else if (game.getTeamTurn() == ChessGame.TeamColor.WHITE) {
                    out.print(post.makeChessBoardWhite(post.initializeBoardWhiteForCustomGame(game.getBoard().getBoard()), game.validMoves(pos)));
                }
                }else{
                    while (startPos == null){startPos = getInputIntStart();}
                }
                endPos = getInputIntEnd();
                while(endPos == null) {
                    System.out.println("Please input valid integers");
                    endPos = getInputIntEnd();
                    if (endPos != null) {
                        if (Objects.equals(endPos.col, "quit")) {
                            break;
                        } else if (endPos.row == 0) {
                            break;
                        }
                    }
                }
                if (endPos != null && Objects.equals(endPos.col, "quit")) {
                    break;
                } else if (endPos != null && endPos.row == 0) {
                    break;
                }
            else if (endPos != null && startPos != null){
                    ChessBoard prevGame = game.board;
                    if (makeMove(startPos, endPos) == null) {
                    }else{
                        valid = true;
                    }
                    if (game.getTeamTurn() == ChessGame.TeamColor.BLACK && game.isInCheck(ChessGame.TeamColor.BLACK)){
                        System.out.println("It looks like you may be in check please make a different move. ");
                        endPos = null;
                        startPos = null;
                        valid = false;
                        game.board = prevGame;
                        game.setTeamTurn(ChessGame.TeamColor.BLACK);
                    }if (game.getTeamTurn() == ChessGame.TeamColor.WHITE && game.isInCheck(ChessGame.TeamColor.WHITE)){
                        System.out.println("Please it looks like you may be in check please make a different move. ");
                        endPos = null;
                        startPos = null;
                        valid = false;
                        game.board = prevGame;
                        game.setTeamTurn(ChessGame.TeamColor.WHITE);
                    }
                }
            }
        }

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
                UserGameCommand resign = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameInfo);
                ServerFacadeWebsocket.session.getBasicRemote().sendText(new Gson().toJson(resign));
                resignedGame = true;
            }

        }
        return resultOfChoice;
    }
}
