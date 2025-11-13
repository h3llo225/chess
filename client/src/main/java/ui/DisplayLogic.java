package ui;

import java.util.Objects;

public class DisplayLogic {
    public void display() throws Exception {
        String resultOfChoice = "";
        while(!Objects.equals(resultOfChoice, "quit")){
            PreloginUI.displayOptions();
            resultOfChoice = new PreloginUI().getChoicePrelogin();
            if (Objects.equals(resultOfChoice, "invalid choice")){
                System.out.println("Invalid command, here are the commands again!");
            }
            if (Objects.equals(resultOfChoice, "login") || Objects.equals(resultOfChoice, "register")
                    || Objects.equals(resultOfChoice, "help")){
                new PreloginUI().initialStart(resultOfChoice);
            }


            if(SignedInState.getSignedIn()){
                System.out.println("You are signed in! Here are your options. \n");
                PostLoginUI.displayOptionsPostLogin();

                String resultOfChoicePostLogin = PostLoginUI.getChoicePostLogin();
                while(!Objects.equals(resultOfChoicePostLogin, "logout")) {
                    if (Objects.equals(resultOfChoicePostLogin, "create game")) {

                        System.out.println(new PostLoginUI().createGamePostLogin());
                        System.out.println("Please input your next command");

                        resultOfChoicePostLogin = PostLoginUI.getChoicePostLogin();

                    }
                    if (Objects.equals(resultOfChoicePostLogin, "play game")) {
                        System.out.println(new PostLoginUI().playGamePostLogin());
                        System.out.println("Please input your next command");

                        resultOfChoicePostLogin = PostLoginUI.getChoicePostLogin();

                    }
                    if (Objects.equals(resultOfChoicePostLogin, "list games")) {
                        new PostLoginUI().listGamesPostLogin();
                        System.out.println("Please input your next command");
                        resultOfChoicePostLogin = PostLoginUI.getChoicePostLogin();

                    }
                    if (Objects.equals(resultOfChoicePostLogin, "observe game")) {
                        new PostLoginUI().observeGame();
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
                    new PostLoginUI().logoutUser();
                    SignedInState.editSignedIn(false);
                }
            }


        }

    }
}
