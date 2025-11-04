package ui;

import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import service.Service;

import java.util.Objects;

public class preloginUI {
    boolean assertLoggedOut(AuthData registerRequest) throws DataAccessException {
        if(Objects.equals(new Service().logout(registerRequest), "{}")){
            return true;
        }else{
            return false;
        }
    }
    boolean assertLoggedIn(UserData registerRequest) throws DataAccessException {
        if(new Service().login(registerRequest) != null){
            return true;
        }else{
            return false;
        }
    }
    public String signIn(UserData registerRequest) throws DataAccessException {
        if (assertLoggedIn(registerRequest)){
            return String.format("You signed in as %s", registerRequest.username());
        }
        return "You are not authorized, please register an account";
    }
}
