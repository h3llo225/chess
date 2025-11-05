package ui;

import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import service.Service;

import java.util.Objects;
import java.util.Scanner;

public class preloginUI {
    public boolean assertLoggedOut(boolean val) throws DataAccessException {
        if(!val){
            return true;
        }else{
            return false;
        }
    }
    public boolean assertLoggedIn(boolean val) throws DataAccessException {
        return val;
    }

    public boolean assertRegisterUser(UserData registerRequest) throws DataAccessException {
        if(new Service().register(registerRequest) != "already taken"){
            return true;
        }
        else{
            return false;
        }
    }
    public String signIn(UserData registerRequest) throws DataAccessException {
        if (assertLoggedIn(registerRequest)){
            return String.format("You signed in as %s", registerRequest.username());
        }
        else if (!assertLoggedIn(registerRequest)){
            new Service().login(registerRequest);
            return String.format("You signed in as %s", registerRequest.username());
        }
        return "You are not authorized, please register an account";
    }

    public String registerUser(UserData registerRequest) throws DataAccessException {
        if (assertRegisterUser(registerRequest)){
            return String.format("You have registered as %s", registerRequest.username());
        }
        return "You are not permitted to register as this username";
    }

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
}
