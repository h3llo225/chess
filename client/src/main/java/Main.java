import chess.*;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;
import model.UserData;
import ui.postLoginUI;
import ui.preloginUI;

import java.util.Objects;

public class Main {
    public static void main(String[] args) throws DataAccessException {
        System.out.println(" Welcome. You are not signed in. Here are your options. \n");
        preloginUI.displayOptions();
        boolean signedIn = false;
        String resultOfChoice = new preloginUI().getChoicePrelogin();
        while (Objects.equals(resultOfChoice, "invalid choice")){
            System.out.println("invalid choice");
            resultOfChoice = new preloginUI().getChoicePrelogin();
        }
        if (resultOfChoice == "register"){
            String[] registerInputs = new preloginUI().getInput();
            String registerResponse = new preloginUI().registerUser(new UserData(registerInputs[0], registerInputs[1], registerInputs[2]));
            if(registerResponse != "already taken" && registerResponse != "bad request"){
                System.out.println(registerResponse);
                signedIn = true;
                new postLoginUI().username = registerInputs[0];
                //when signed in is true
            }
            else{
                System.out.println("The request did not contain all paramaters or the username was already taken");
            }
        }

        if (new preloginUI().assertLoggedIn(signedIn)){
            //postloginUI
        }

        if (resultOfChoice == "login"){
            String[] loginInputs = new preloginUI().getInput();
            while (new DatabaseManager().getUser(loginInputs[0])){
                System.out.println("User not found, try again.");
                loginInputs = new preloginUI().getInput();
            };
            loginInputs = new DatabaseManager().findUserByUserNameReturnString(loginInputs[0]);
            if (!Objects.equals(new preloginUI().signIn(new UserData(loginInputs[0], loginInputs[1], loginInputs[2])), "You are not permitted to register as this username")){
                System.out.println(new preloginUI().signIn(new UserData(loginInputs[0],loginInputs[1],loginInputs[2])));
            }
            else if (Objects.equals(new preloginUI().registerUser(new UserData(loginInputs[0], loginInputs[1], loginInputs[2])), "You are not permitted to register as this username")){
                System.out.println("You are not permitted to register as this username");
            }
        }

        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + args[0]);
    }
}