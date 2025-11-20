package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.NotificationSetup;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

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
    public String helpPrelogin(){
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
                case "help", "Help" -> "help";
                case "quit", "Quit" -> "quit";
                default -> "invalid choice";
            };
        }

    }
    public void displayPlayGame(String outputBoard){
        String resultOfChoice = "";
        DisplayLogicPlayGame item = new DisplayLogicPlayGame();
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        while(!Objects.equals(resultOfChoice, "resign")) {
            out.print(outputBoard);
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


    public String makeMove(makeMoveType startPos, makeMoveType endPos){

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
        ChessGame.makeMove(posStartPos,posEndPos);
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
    public String playGame(String resultOfChoice) {
        if (Objects.equals(resultOfChoice, "invalid choice")){
            //System.out.println("invalid choice");
        }
        if (Objects.equals(resultOfChoice, "make move")){
            makeMoveType startPos = getInputInt();
            while (startPos ==null){
                System.out.println("Please input valid integers");
                startPos = getInputInt();
            }
            makeMoveType endPos = getInputInt();
            while (endPos ==null){
                System.out.println("Please input valid integers");
                endPos = getInputInt();
            }
            makeMove(startPos,endPos);
        }

        if (Objects.equals(resultOfChoice, "help")){
            System.out.println(getChoicePlayGame());
            System.out.println("Please enter a new command! Here is the full list.");
        }
        if (Objects.equals(resultOfChoice, "quit")) {
            return "quit";
        }
        return resultOfChoice;
    }
}
