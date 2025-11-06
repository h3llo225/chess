import chess.*;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.UserData;
import ui.postLoginUI;
import ui.preloginUI;
import ui.signedInState;

import java.io.IOException;
import java.util.Objects;

public class Main {

    public static void main(String[] args) throws DataAccessException, IOException, InterruptedException {
        System.out.println(" Welcome. You are not signed in. Here are your options. \n");
        preloginUI.displayOptions();

        String resultOfChoice = new preloginUI().getChoicePrelogin();
        while (Objects.equals(resultOfChoice, "invalid choice")){
            System.out.println("invalid choice");
            resultOfChoice = new preloginUI().getChoicePrelogin();
        }
        if (resultOfChoice == "register"){
            new preloginUI().registerNewUser();
        }
        if (resultOfChoice == "login"){
            new preloginUI().loginUser();
        }
        if (resultOfChoice == "help"){
            System.out.println(new preloginUI().helpPrelogin());
        }
        if (resultOfChoice == "quit") {
            return;
        }

        if(signedInState.getSignedIn()){
            System.out.println("You are signed in! Here are your options. \n");
            postLoginUI.displayOptionsPostLogin();
            String resultOfChoicePostLogin = new postLoginUI().getChoicePostLogin();
            if (resultOfChoicePostLogin == "logout"){
                new postLoginUI().logoutUser();
            }
            if (resultOfChoicePostLogin == "create game"){
                new postLoginUI().createGamePostLogin();
            }
            if (resultOfChoicePostLogin == "play game"){
                new postLoginUI().playGamePostLogin();
            }
            if (resultOfChoicePostLogin == "list games") {
                new postLoginUI().listGamePostLogin();
            }
            if (resultOfChoicePostLogin == "observe game") {
                return;
            }
        }

var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
System.out.println("♕ 240 Chess Client: " + args[0]);
}
}