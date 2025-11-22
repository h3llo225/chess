package ui;

import model.AuthData;
import model.UserData;
import serverfacade.ServerFacade;
import websocket.ServerFacadeWebsocket;


import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;
import static ui.DisplayLogic.pre;
import static ui.DisplayLogic.serverFacade;

public class PreloginUI {

    //private ServerFacade serverFacade = new ServerFacade(8080);

    public String helpPrelogin(){
        return """
                Here are the command parameters!
                register(username, password, email)
                login(username, password)
                """;
    }






    public String initialStart(String resultOfChoice) throws Exception {

        if (Objects.equals(resultOfChoice, "invalid choice")){
            //System.out.println("invalid choice");
        }
        if (Objects.equals(resultOfChoice, "register")){
            registerNewUser();
        }
        if (Objects.equals(resultOfChoice, "login")){
            loginUser();
        }
        if (Objects.equals(resultOfChoice, "help")){
            System.out.println(helpPrelogin());
            System.out.println("Please enter a new command! Here is the full list.");
        }
        if (Objects.equals(resultOfChoice, "quit")) {
            return "quit";
        }
        return resultOfChoice;
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
    String[] registerInputs = pre.getInput();
    try {
        while(registerInputs[0]==null || registerInputs[0]==""){
            registerInputs = pre.getInput();
        }
        if (Objects.equals(registerInputs[0], "quit")){
            return;
        }
        while(registerInputs.length != 3 ){
            System.out.println("wrong number of arguments!");
            registerInputs = pre.getInput();
        }
        SignedInState.editSignedIn(true);
        ServerFacadeWebsocket websocket = new ServerFacadeWebsocket();
        AuthData authDataItem =  serverFacade.registerUser(new UserData(registerInputs[0], registerInputs[1], registerInputs[2]));
        DisplayLogic.authToken = authDataItem.authToken();
        //when signed in is true

    }catch(Exception ex){


        System.out.println(String.format("You are not allowed to register, please try again."));
        registerNewUser();
    }
}
    public void loginUser() throws Exception {
           try{ String[] loginInputs = pre.getInput();
        while(loginInputs[0]==null || loginInputs[0]==""){
            loginInputs = pre.getInput();
        }
               if (Objects.equals(loginInputs[0], "quit")){
                   return;
               }
               while (loginInputs.length != 2 ){
                   System.out.println("wrong number of arguments!");
                   loginInputs = pre.getInput();
               }
               ServerFacadeWebsocket websocket = new ServerFacadeWebsocket();
        AuthData authDataItem =  serverFacade.loginUser(new UserData(loginInputs[0], loginInputs[1], null));
        DisplayLogic.authToken = authDataItem.authToken();
        SignedInState.editSignedIn(true);
    }catch(Exception ex){
        System.out.println(String.format("You are not allowed to login, please try again."));
        loginUser();
    }
    }
}
