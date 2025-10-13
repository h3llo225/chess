package dataaccess;

import model.UserData;

import java.util.ArrayList;

public class createUser {
    public String username;
    public String password;
    public String email;
    public static ArrayList<UserData> listofUsers = new ArrayList<>();


    //UserData newUser = new UserData(username, password, email);
    public createUser(UserData newUser) {
        this.username = newUser.username();
        this.password = newUser.password();
        this.email = newUser.email();

        listofUsers.add(newUser);


    }
}