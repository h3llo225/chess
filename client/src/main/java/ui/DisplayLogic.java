package ui;

import chess.ChessGame;
import serverfacade.ServerFacade;

import java.util.Objects;

public class DisplayLogic {
    public static String authToken;
    public static ServerFacade serverFacade = new ServerFacade(8080);
    public static ChessGame game;
    public static PostLoginUI post = new PostLoginUI();
    public static PreloginUI pre = new PreloginUI();
    public static DisplayLogicPlayGame gameUI = new DisplayLogicPlayGame();

    public void display() throws Exception {
        String resultOfChoice = "";
        while(!Objects.equals(resultOfChoice, "quit")){
            PreloginUI.displayOptions();
            resultOfChoice = pre.getChoicePrelogin();
            if (Objects.equals(resultOfChoice, "invalid choice")){
                System.out.println("Invalid command, here are the commands again!");
            }
            if (Objects.equals(resultOfChoice, "login") || Objects.equals(resultOfChoice, "register")
                    || Objects.equals(resultOfChoice, "help")){
                pre.initialStart(resultOfChoice);
            }


            if(SignedInState.getSignedIn()){
                System.out.println("You are signed in! Here are your options. \n");
                PostLoginUI.displayOptionsPostLogin();

                String resultOfChoicePostLogin = PostLoginUI.getChoicePostLogin();
                while(!Objects.equals(resultOfChoicePostLogin, "logout")) {
                    if (Objects.equals(resultOfChoicePostLogin, "create game")) {

                        System.out.println(post.createGamePostLogin());
                        System.out.println("Please input your next command");

                        resultOfChoicePostLogin = PostLoginUI.getChoicePostLogin();

                    }
                    if (Objects.equals(resultOfChoicePostLogin, "play game")) {
                        System.out.println(post.playGamePostLogin());
                        System.out.println("Please input your next command");

                        resultOfChoicePostLogin = PostLoginUI.getChoicePostLogin();

                    }
                    if (Objects.equals(resultOfChoicePostLogin, "list games")) {
                        post.listGamesPostLogin();
                        System.out.println("Please input your next command");
                        resultOfChoicePostLogin = PostLoginUI.getChoicePostLogin();

                    }
                    if (Objects.equals(resultOfChoicePostLogin, "observe game")) {
                        post.observeGame();
                        System.out.println("Please input your next command");

                        resultOfChoicePostLogin = PostLoginUI.getChoicePostLogin();

                    }
                    if (Objects.equals(resultOfChoicePostLogin, "help")) {

                        System.out.println("Please input your next command");
                        resultOfChoicePostLogin = PostLoginUI.getChoicePostLogin();
                    }
                    if (Objects.equals(resultOfChoicePostLogin, "invalid choice")) {

                        System.out.println("invalid command, please try again");
                        resultOfChoicePostLogin = PostLoginUI.getChoicePostLogin();
                    }
                }
                {
                    post.logoutUser();
                    SignedInState.editSignedIn(false);
                }
            }


        }

    }
}
