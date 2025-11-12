import chess.*;
import ui.PostLoginUI;
import ui.PreloginUI;
import ui.SignedInState;

import java.util.Objects;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println(" Welcome. You are not signed in. Here are your options. \n");
        PreloginUI.displayOptions();

        String resultOfChoice = new PreloginUI().getChoicePrelogin();
        new PreloginUI().initialStart(resultOfChoice);
        if(SignedInState.getSignedIn()){
            System.out.println("You are signed in! Here are your options. \n");
            PostLoginUI.displayOptionsPostLogin();

            String resultOfChoicePostLogin = PostLoginUI.getChoicePostLogin();
            while(!Objects.equals(resultOfChoicePostLogin, "logout")) {
                if (Objects.equals(resultOfChoicePostLogin, "create game")) {

                    System.out.println(new PostLoginUI().createGamePostLogin());
                }
                if (Objects.equals(resultOfChoicePostLogin, "play game")) {
                    System.out.println(new PostLoginUI().playGamePostLogin());
                }
                if (Objects.equals(resultOfChoicePostLogin, "list games")) {
                    new PostLoginUI().listGamesPostLogin();
                }
                if (Objects.equals(resultOfChoicePostLogin, "observe game")) {
                    new PostLoginUI().observeGame();
                }

                System.out.println("Please input your next command!");
                resultOfChoicePostLogin = PostLoginUI.getChoicePostLogin();
            }
            {
                new PostLoginUI().logoutUser();
            }

        }

var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
System.out.println("♕ 240 Chess Client: " + args[0]);
}
}