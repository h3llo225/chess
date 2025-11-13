package ui;

import java.util.Objects;

public class DisplayLogic {
    public void Display() throws Exception {
        String resultOfChoice = "";
        while(!Objects.equals(resultOfChoice, "quit")){
            PreloginUI.displayOptions();
            resultOfChoice = new PreloginUI().getChoicePrelogin();
            if (Objects.equals(resultOfChoice, "invalid choice")){
                resultOfChoice = new PreloginUI().initialStart(resultOfChoice);
            } else if (Objects.equals(resultOfChoice, "help")) {
                resultOfChoice = new PreloginUI().initialStart(resultOfChoice);
                resultOfChoice = new PreloginUI().getChoicePrelogin();
            } else{
                resultOfChoice = new PreloginUI().initialStart(resultOfChoice);
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
