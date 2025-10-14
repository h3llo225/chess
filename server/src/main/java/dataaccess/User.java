package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.ArrayList;
import java.util.Objects;

public class User {
    public String username;
    public String password;
    public String authToken;
    public String email;
    public static ArrayList<UserData> listofUsers = new ArrayList<>();
    public static ArrayList<AuthData> listOfAuth = new ArrayList<>();
    public static boolean getUser(String username) {
        for (UserData user : createUser.listofUsers) {
            if (Objects.equals(user.username(), username)) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkLogin(String username, String password) {
        for (UserData user : createUser.listofUsers) {
            if (Objects.equals(user.username(), username)) {
                if (Objects.equals(user.password(), password)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean findAuth(String requestAuthToken) {
        for (AuthData userAuth : loginUser.listOfAuth) {
            if (Objects.equals(userAuth.authToken(), requestAuthToken)) {
                return true;
            }
        }
        return false;
    }

    public void createUser(UserData newUser) {
        this.username = newUser.username();
        this.password = newUser.password();
        this.email = newUser.email();

        listofUsers.add(newUser);
    }
    public void loginUser(AuthData newUser) {
        this.username = newUser.username();
        this.authToken = newUser.authToken();
        listOfAuth.add(newUser);
    }
}
