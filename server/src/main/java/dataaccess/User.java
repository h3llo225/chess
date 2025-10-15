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
    public boolean getUser(String username) {
        for (UserData user : listofUsers) {
            if (Objects.equals(user.username(), username)) {
                return false;
            }
        }
        return true;
    }

    public boolean checkLogin(String username, String password) {
        for (UserData user : listofUsers) {
            if (Objects.equals(user.username(), username)) {
                if (Objects.equals(user.password(), password)) {
                    return true;
                }
            }
        }
        return false;
    }


    public void createUser(UserData newUser) {


        listofUsers.add(newUser);
    }

}
