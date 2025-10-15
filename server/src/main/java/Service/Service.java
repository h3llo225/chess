package Service;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.User;
import io.javalin.http.Context;
import model.AuthData;
import model.UserData;

import java.util.ArrayList;

import java.util.Objects;
import java.util.UUID;

import static dataaccess.User.listOfAuth;
import static dataaccess.User.listofUsers;


public class Service {
    public void register(Context ctx){
        UserData classPlaceHolder = new UserData("","","");
        UserData registerRequest = new Gson().fromJson(ctx.body(), classPlaceHolder.getClass());
        AuthData classPlaceHolder2 = new AuthData("","");

        try{
            //createUser user = handleregister(json);
            if (registerRequest.username() == null || registerRequest.email() == null || registerRequest.password() == null) {
                ctx.status(400);
                throw new DataAccessException("{\"message\": \"Error: bad request\"}");
            }
            if (!new User().getUser(registerRequest.username())){
                ctx.status(403);
                throw new DataAccessException("{\"message\": \"Error: already taken\"}");
            }
            if (new User().getUser(registerRequest.username())){
                new User().createUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
                String uuIDLogin = UUID.randomUUID().toString();
                AuthData result = new AuthData(registerRequest.username(), uuIDLogin);
                Gson gson = new Gson();
                new User().loginUser(new AuthData(registerRequest.username(),uuIDLogin));
                String jsonString = gson.toJson(result, classPlaceHolder2.getClass());
                ctx.result(jsonString);
                //"{\"username\":" "\"registerRequest.username()\" , \"authToken\":" \"uuIDLogin\" }"
            }


        }catch(DataAccessException ex){

            ctx.result(ex.getMessage());
        }


    }


    public void login(Context ctx){
        UserData classPlaceHolder = new UserData("","",null);
        UserData registerRequest = new Gson().fromJson(ctx.body(), classPlaceHolder.getClass());
        AuthData classPlaceHolder2 = new AuthData( "", "");
        try{
            //createUser user = handleregister(json);
            if (registerRequest.username() == null || registerRequest.password() == null) {
                ctx.status(400);
                throw new DataAccessException("{\"message\": \"Error: bad request\"}");
            }
            if (new User().checkLogin(registerRequest.username(), registerRequest.password())){
                String uuIDLogin = UUID.randomUUID().toString();
                new User().loginUser(new AuthData(registerRequest.username(), uuIDLogin));
                Gson gson = new Gson();
                AuthData result = new AuthData(registerRequest.username(), uuIDLogin);
                String jsonString = gson.toJson(result, classPlaceHolder2.getClass());
                ctx.result(jsonString);
                //"{\"username\":" "\"registerRequest.username()\" , \"authToken\":" \"uuIDLogin\" }"
            }
            if (!new User().checkLogin(registerRequest.username(), registerRequest.password())){
                ctx.status(401);
                throw new DataAccessException("{\"message\": \"Error: unauthorized\"}");
            }

        }catch(DataAccessException ex){

            ctx.result(ex.getMessage());
        }
    }

    public void logout(Context ctx){
        String test = ctx.header("authorization");
        //String jsonTest = "\"authorization\": \"test \"";
        String jsonTest = "{\"authorization\": \"%s\"}";
        String finalJsonString = String.format(jsonTest, test);
        AuthData classPlaceHolder = new AuthData(null,"");
        AuthData oldTest = new Gson().fromJson(finalJsonString, classPlaceHolder.getClass());
        AuthData registerRequest = new AuthData(null,ctx.header("authorization")) ;
        String tester = registerRequest.authToken();
        String tester2 = registerRequest.username();


        try{
            //createUser user = handleregister(json);
            if (!new User().findAuth(registerRequest.authToken())){
                ctx.status(401);
                throw new DataAccessException("{\"message\": \"Error: unauthorized\"}");
            }
            if (new User().findAuth(registerRequest.authToken())){
                int count = 0;
                for (AuthData userAuth : User.listOfAuth) {
                    count++;
                    if (Objects.equals(userAuth.authToken(), registerRequest.authToken())) {
                        //loginUser.remove(userAuth);
                        ctx.result("{}");

                        break;
                    }
                }
                User.listOfAuth.remove(count-1);

                //"{\"username\":" "\"registerRequest.username()\" , \"authToken\":" \"uuIDLogin\" }"
            }



        }catch(DataAccessException ex){

            ctx.result(ex.getMessage());
        }

    }
    public void clear(Context ctx){
        User.listOfAuth = new ArrayList<>();
        User.listofUsers = new ArrayList<>();
        ctx.result("{}");
    }
}
