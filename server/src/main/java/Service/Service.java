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
}