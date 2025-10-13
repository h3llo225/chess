package dataaccess;

import model.AuthData;
import java.util.UUID;
import model.UserData;

import java.util.ArrayList;

public class loginUser {
    public String username;
    public String authToken;
    //createUser.listofUsers

    //UserData newUser = new UserData(username, password, email);
    public loginUser(AuthData newUser) {
        this.username = newUser.username();
        this.authToken = newUser.authToken();

    }
}
