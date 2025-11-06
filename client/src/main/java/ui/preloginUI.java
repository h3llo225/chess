package ui;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.UserData;
import serverFacade.serverFacade;
import service.Service;


import java.io.IOException;
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
    public String signIn(boolean val, UserData registerRequest) throws DataAccessException {
        if (assertLoggedIn(val)){
            return String.format("You signed in as %s", registerRequest.username());
        }
        else if (!assertLoggedIn(val)){
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
public void registerNewUser() throws DataAccessException, IOException, InterruptedException {
    String[] registerInputs = new preloginUI().getInput();
    try {
        while(registerInputs[0]==null || registerInputs[0]==""){
            registerInputs = new preloginUI().getInput();
        }
        new serverFacade().registerUser(new UserData(registerInputs[0], registerInputs[1], registerInputs[2]));
        signedInState.editSignedIn(true);
        new postLoginUI().username = registerInputs[0];
        //when signed in is true
    }catch(DataAccessException ex){
        System.out.println(String.format("You are not allowed to register, %s", ex));
    }
}
    public void loginUser() throws DataAccessException {

            String[] loginInputs = new preloginUI().getInput();
            while (!new DatabaseManager().getUser(loginInputs[0])){
                System.out.println("User not found, try again.");
                loginInputs = new preloginUI().getInput();
            };
            loginInputs = new DatabaseManager().findUserByUserNameReturnString(loginInputs[0]);
            String retVal = new preloginUI().signIn(signedInState.getSignedIn(), new UserData(loginInputs[0], loginInputs[1], loginInputs[2]));
            signedInState.editSignedIn(true);
            System.out.println(retVal);

    }}
