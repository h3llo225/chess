package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static ui.DisplayLogic.game;
import static ui.DisplayLogic.post;
import static ui.DisplayLogic.game;
import static ui.PostLoginUI.*;

public class DisplayLogicPlayGame {

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
                Here is how to make a move.
                make move(username, password, email)
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
                    System.out.println("You have chosen to resign");
                    yield "resign";
                }
                case "make move", "Make move" -> {
                    System.out.println("You have chosen to make a move");
                    yield "make move";
                }
                case "help", "Help" -> "help";
                default -> "invalid choice";
            };
        }

    }
    public void displayPlayGame(String outputBoard) throws InvalidMoveException {
        String resultOfChoice = "";
        DisplayLogicPlayGame item = new DisplayLogicPlayGame();
        System.out.println("You have joined the game, here are your options!");
        displayOptions();
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.println();
        while(!Objects.equals(resultOfChoice, "resign")) {
            //out.print(outputBoard);
            resultOfChoice = item.getChoicePlayGame();
            if (Objects.equals(resultOfChoice, "invalid choice")) {
                System.out.println("Invalid command, here are the commands again!");
            }
            if (Objects.equals(resultOfChoice, "make move")
                    || Objects.equals(resultOfChoice, "help")) {
                item.playGame(resultOfChoice);
            }


        }
    }


    public String makeMove(makeMoveType startPos, makeMoveType endPos) throws InvalidMoveException {

        Map<String, Integer> translator = new HashMap<>();
        translator.put("a", 1);
        translator.put("b", 2);
        translator.put("c", 3);
        translator.put("d", 4);
        translator.put("e", 5);
        translator.put("f", 6);
        translator.put("g", 7);
        translator.put("h", 8);
        int translatedStartPos = 0;
        int translatedEndPos = 0;


        while(translator.get(startPos.col) == null){
                System.out.println("Please input valid coordinates");
                startPos = getInputInt();
            }
                translatedStartPos = translator.get(startPos.col);

        while(translator.get(endPos.col) == null){
            System.out.println("Please input valid coordinates");
            endPos = getInputInt();
        }
        translatedEndPos = translator.get(endPos.col);
        ChessPosition posStartPos = new ChessPosition(startPos.row,translatedStartPos);
        ChessPosition posEndPos = new ChessPosition(endPos.row,translatedEndPos);
        while(game.getBoard().getPiece(posStartPos) == null){
            System.out.println("Please input valid coordinates");
            startPos = getInputInt();
            while(translator.get(startPos.col) == null){
                System.out.println("Please input valid coordinates");
                startPos = getInputInt();
            }
            translatedStartPos = translator.get(startPos.col);
            posStartPos = new ChessPosition(startPos.row,translatedStartPos);
        }

        posStartPos = new ChessPosition(startPos.row,translatedStartPos);
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
            }
            if (Objects.equals(promotion[0].toUpperCase(), "KNIGHT")){
                game.makeMove(new ChessMove(posStartPos,posEndPos, ChessPiece.PieceType.KNIGHT));
            }
            if (Objects.equals(promotion[0].toUpperCase(), "ROOK")){
                game.makeMove(new ChessMove(posStartPos,posEndPos, ChessPiece.PieceType.ROOK));
            }
            if (Objects.equals(promotion[0].toUpperCase(), "BISHOP")){
                game.makeMove(new ChessMove(posStartPos,posEndPos, ChessPiece.PieceType.BISHOP));
            }
        }else{
        game.makeMove(new ChessMove(posStartPos,posEndPos,null));
        }
        }
        return "Move made";
    }
    public record makeMoveType(String col, int row) {
    }

    public makeMoveType getInputInt() throws InputMismatchException, IndexOutOfBoundsException {
        try{
            System.out.println("Please input a column and a row such as a 1 or d 6");
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
               if (startingArray.length == 2){
                   return new makeMoveType(startingArray[0],nums);
               }
                System.out.println("Please input the right number of vals");
        }catch (Exception e){
            System.out.println("Please input a valid integer");
            return null;
        }
        return null;
    }
    public String playGame(String resultOfChoice) throws InvalidMoveException {
        if (Objects.equals(resultOfChoice, "invalid choice")){
            //System.out.println("invalid choice");
        }
        if (Objects.equals(resultOfChoice, "make move")){
            System.out.println("This input will be the position of the piece you want to move.");
            makeMoveType startPos = getInputInt();
            while (startPos ==null){
                System.out.println("Please input valid integers");
                startPos = getInputInt();
            }
            System.out.println("This input will be the position of where you want to move the piece to.");
            makeMoveType endPos = getInputInt();
            while (endPos ==null){
                System.out.println("Please input valid integers");
                endPos = getInputInt();
            }
            var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
            makeMove(startPos,endPos);
            out.println(post.makeChessBoard(post.initializeBoardWhiteForCustomGame(game.board.getBoard())));
        }

        if (Objects.equals(resultOfChoice, "help")){
            System.out.println(helpPlayGame());
            System.out.println("Please enter a new command! Here is the full list.");
        }
        if (Objects.equals(resultOfChoice, "quit")) {
            return "quit";
        }
        return resultOfChoice;
    }
}
