import chess.*;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import ui.preloginUI;

import java.util.Objects;

public class Main {
    public static void main(String[] args) throws DataAccessException {
        System.out.println(" Welcome. You are not signed in. Here are your options. \n");
        preloginUI.displayOptions();
        String resultOfChoice = new preloginUI().getChoicePrelogin();
        while (Objects.equals(resultOfChoice, "invalid choice")){
            System.out.println("invalid choice");
            resultOfChoice = new preloginUI().getChoicePrelogin();
        }
        if (resultOfChoice == "register"){
            String[] registerInputs = new preloginUI().getInput();
            System.out.println(new preloginUI().registerUser(new UserData(registerInputs[0],registerInputs[1],registerInputs[2])));

        }
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + args[0]);
    }
}