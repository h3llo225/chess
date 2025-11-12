package ui;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;
import model.UserData;
import serverFacade.serverFacade;
import service.Service;


import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class preloginUI {

    public String helpPrelogin(){
        return """
                register(username, password, email)
                login(username, password)
                quit
                """;
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
public void registerNewUser() throws DataAccessException, IOException, InterruptedException {
    String[] registerInputs = new preloginUI().getInput();
    try {
        while(registerInputs[0]==null || registerInputs[0]==""){
            registerInputs = new preloginUI().getInput();
        }
        while(registerInputs.length != 3 ){
            System.out.println("wrong number of arguments!");
            registerInputs = new preloginUI().getInput();
        }
        signedInState.editSignedIn(true);
        AuthData authDataItem =  new serverFacade().registerUser(new UserData(registerInputs[0], registerInputs[1], registerInputs[2]));
        postLoginUI.authToken = authDataItem.authToken();
        //when signed in is true
    }catch(DataAccessException ex){
        System.out.println(String.format("You are not allowed to register, %s", ex));
    }
}
    public void loginUser() throws DataAccessException, IOException, InterruptedException {
           try{ String[] loginInputs = new preloginUI().getInput();
        while(loginInputs[0]==null || loginInputs[0]==""){
            loginInputs = new preloginUI().getInput();
        }
               while (loginInputs.length != 2 ){
                   System.out.println("wrong number of arguments!");
                   loginInputs = new preloginUI().getInput();
               }

        AuthData authDataItem =  new serverFacade().loginUser(new UserData(loginInputs[0], loginInputs[1], null));
        postLoginUI.authToken = authDataItem.authToken();
        signedInState.editSignedIn(true);
    }catch(DataAccessException ex){
        System.out.println(String.format("You are not allowed to login, %s", ex));
        loginUser();
    }
    }
}
