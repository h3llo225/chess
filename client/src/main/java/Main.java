import chess.*;
import model.UserData;
import ui.postLoginUI;
import ui.preloginUI;
import ui.signedInState;

import java.io.IOException;
import java.util.Objects;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println(" Welcome. You are not signed in. Here are your options. \n");
        preloginUI.displayOptions();

        String resultOfChoice = new preloginUI().getChoicePrelogin();
        while (Objects.equals(resultOfChoice, "invalid choice")){
            System.out.println("invalid choice");
            resultOfChoice = new preloginUI().getChoicePrelogin();
        }
        if (Objects.equals(resultOfChoice, "register")){
            new preloginUI().registerNewUser();
        }
        if (Objects.equals(resultOfChoice, "login")){
            new preloginUI().loginUser();
        }
        if (Objects.equals(resultOfChoice, "help")){
            System.out.println(new preloginUI().helpPrelogin());
        }
        if (Objects.equals(resultOfChoice, "quit")) {
            return;
        }

        if(signedInState.getSignedIn()){
            System.out.println("You are signed in! Here are your options. \n");
            postLoginUI.displayOptionsPostLogin();

            String resultOfChoicePostLogin = postLoginUI.getChoicePostLogin();
            while(!Objects.equals(resultOfChoicePostLogin, "logout")) {
                if (Objects.equals(resultOfChoicePostLogin, "create game")) {

                    System.out.println(new postLoginUI().createGamePostLogin());
                }
                if (Objects.equals(resultOfChoicePostLogin, "play game")) {
                    System.out.println(new postLoginUI().playGamePostLogin());
                }
                if (Objects.equals(resultOfChoicePostLogin, "list games")) {
                    new postLoginUI().listGamesPostLogin();
                }
                if (Objects.equals(resultOfChoicePostLogin, "observe game")) {
                    new postLoginUI().observeGame();
                }
                System.out.println("Please input your next command!");
                resultOfChoicePostLogin = postLoginUI.getChoicePostLogin();
            }
            {
                new postLoginUI().logoutUser();
            }

        }

var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
System.out.println("♕ 240 Chess Client: " + args[0]);
}
}