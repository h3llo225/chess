package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.Objects;

public class Auth {
    public static ArrayList<AuthData> listOfAuth = new ArrayList<>();
    public void loginUser(AuthData newUser) {
        listOfAuth.add(newUser);
    }
    public boolean findAuth(String requestAuthToken) {
        for (AuthData userAuth : listOfAuth) {
            if (Objects.equals(userAuth.authToken(), requestAuthToken)) {
                return true;
            }
        }
        return false;
    }

    public String findUser(String requestAuthToken) {
        for (AuthData userAuth : listOfAuth) {
            if (Objects.equals(userAuth.authToken(), requestAuthToken)) {
                return userAuth.username();
            }
        }
        return null;
    }
}


