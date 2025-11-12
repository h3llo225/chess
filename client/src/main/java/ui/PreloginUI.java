package ui;

import model.AuthData;
import model.UserData;
import serverfacade.ServerFacade;


import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

public class PreloginUI {

    public String helpPrelogin(){
        return """
                register(username, password, email)
                login(username, password)
                quit
                """;
    }

    public void helperPrelogin(String resultOfChoice) throws Exception {
        boolean isGood = false;
        System.out.println(new PreloginUI().helpPrelogin());
        resultOfChoice = new PreloginUI().getChoicePrelogin();

        while (Objects.equals(resultOfChoice, "invalid choice")){
            if (isGood == false){
                System.out.println("invalid choice");
                resultOfChoice = new PreloginUI().getChoicePrelogin();
            }
        }
        if (!Objects.equals(resultOfChoice, "invalid choice")){
            new PreloginUI().initialStart(resultOfChoice);
        }

    }


    public void initialStart(String resultOfChoice) throws Exception {
        while (Objects.equals(resultOfChoice, "invalid choice")){
            System.out.println("invalid choice");
            resultOfChoice = new PreloginUI().getChoicePrelogin();
        }
        if (Objects.equals(resultOfChoice, "register")){
            new PreloginUI().registerNewUser();
        }
        if (Objects.equals(resultOfChoice, "login")){
            new PreloginUI().loginUser();
        }
        if (Objects.equals(resultOfChoice, "help")){
            new PreloginUI().helperPrelogin(resultOfChoice);
        }
        if (Objects.equals(resultOfChoice, "quit")) {
            return;
        }
    }

    static public void displayOptions(){
        System.out.println("""
                register
                login
                help
                quit
                """);
    }

    public String getChoicePrelogin(){
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            return switch (line) {
                case "register", "Register" -> {
                    System.out.println("You have chosen to register! Please" +
                            " input a username, password and email. (In that order).");
                    yield "register";
                }
                case "login", "Login" -> {
                    System.out.println("You have chosen to Login! Please" +
                            "input a username and password. (In that order).");
                    yield "login";
                }
                case "help", "Help" -> "help";
                case "quit", "Quit" -> "quit";
                default -> "invalid choice";
            };
        }

    }

    public String[] getInput(){
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            return line.split(" ");
    }
    }

    public int getInputInt() throws InputMismatchException, IndexOutOfBoundsException {
        try{while (true) {
            Scanner scanner = new Scanner(System.in);
            return scanner.nextInt();
        }
        }catch (Exception e){
            System.out.println("Please input a valid integer");
            return getInputInt();
        }
    }
public void registerNewUser() throws  Exception {
    String[] registerInputs = new PreloginUI().getInput();
    try {
        while(registerInputs[0]==null || registerInputs[0]==""){
            registerInputs = new PreloginUI().getInput();
        }
        while(registerInputs.length != 3 ){
            System.out.println("wrong number of arguments!");
            registerInputs = new PreloginUI().getInput();
        }
        SignedInState.editSignedIn(true);
        AuthData authDataItem =  new ServerFacade().registerUser(new UserData(registerInputs[0], registerInputs[1], registerInputs[2]));
        PostLoginUI.authToken = authDataItem.authToken();
        //when signed in is true
    }catch(Exception ex){


        System.out.println(String.format("You are not allowed to register, please try again."));
        registerNewUser();
    }
}
    public void loginUser() throws Exception {
           try{ String[] loginInputs = new PreloginUI().getInput();
        while(loginInputs[0]==null || loginInputs[0]==""){
            loginInputs = new PreloginUI().getInput();
        }
               while (loginInputs.length != 2 ){
                   System.out.println("wrong number of arguments!");
                   loginInputs = new PreloginUI().getInput();
               }

        AuthData authDataItem =  new ServerFacade().loginUser(new UserData(loginInputs[0], loginInputs[1], null));
        PostLoginUI.authToken = authDataItem.authToken();
        SignedInState.editSignedIn(true);
    }catch(Exception ex){
        System.out.println(String.format("You are not allowed to login, please try again."));
        loginUser();
    }
    }
}
