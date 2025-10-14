package Service;

import model.UserData;
import dataaccess.createUser;

import java.util.Objects;


public class Service {
    public static boolean getUser(String username){
       for (UserData user : createUser.listofUsers){
           if (Objects.equals(user.username(), username)){
               return false;
           }
       }
       return true;
    }
    public static boolean checkLogin(String username, String password){
        for (UserData user : createUser.listofUsers){
            if (Objects.equals(user.username(), username)){
                if (Objects.equals(user.password(), password)){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean findAuth(String authToken){

    }
}