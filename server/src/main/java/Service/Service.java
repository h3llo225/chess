package Service;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.User;
import dataaccess.loginUser;
import io.javalin.http.Context;
import model.AuthData;
import model.UserData;
import dataaccess.createUser;

import java.util.Objects;
import java.util.UUID;

import static dataaccess.loginUser.listOfAuth;


public class Service {
    public static void register(Context ctx){
        UserData classPlaceHolder = new UserData("","","");
        UserData registerRequest = new Gson().fromJson(ctx.body(), classPlaceHolder.getClass());
        try{
            //createUser user = handleregister(json);
            if (registerRequest.username() == null || registerRequest.email() == null || registerRequest.password() == null) {
                ctx.status(400);
                throw new DataAccessException("{\"message\": \"Error: bad request\"}");
            }
            if (!User.getUser(registerRequest.username())){
                ctx.status(403);
                throw new DataAccessException("{\"message\": \"Error: already taken\"}");
            }
            if (User.getUser(registerRequest.username())){
                new createUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
                String uuIDLogin = UUID.randomUUID().toString();
                loginUser loggingIn = new loginUser(new AuthData(uuIDLogin, registerRequest.username()));
                Gson gson = new Gson();
                String jsonString = gson.toJson(loggingIn);
                ctx.result(jsonString);
                //"{\"username\":" "\"registerRequest.username()\" , \"authToken\":" \"uuIDLogin\" }"
            }


        }catch(DataAccessException ex){

            ctx.result(ex.getMessage());
        }


    }


    public static void login(Context ctx){
        UserData classPlaceHolder = new UserData("","",null);
        UserData registerRequest = new Gson().fromJson(ctx.body(), classPlaceHolder.getClass());

        try{
            //createUser user = handleregister(json);
            if (registerRequest.username() == null || registerRequest.password() == null) {
                ctx.status(400);
                throw new DataAccessException("{\"message\": \"Error: bad request\"}");
            }
            if (User.checkLogin(registerRequest.username(), registerRequest.password())){
                String uuIDLogin = UUID.randomUUID().toString();
                loginUser loggingIn = new loginUser(new AuthData(uuIDLogin, registerRequest.username()));
                Gson gson = new Gson();
                String jsonString = gson.toJson(loggingIn);
                ctx.result(jsonString);
                //"{\"username\":" "\"registerRequest.username()\" , \"authToken\":" \"uuIDLogin\" }"
            }
            if (!User.checkLogin(registerRequest.username(), registerRequest.password())){
                ctx.status(401);
                throw new DataAccessException("{\"message\": \"Error: unauthorized\"}");
            }

        }catch(DataAccessException ex){

            ctx.result(ex.getMessage());
        }
    }

    public static void logout(Context ctx){
        String test = ctx.header("authorization");
        //String jsonTest = "\"authorization\": \"test \"";
        String jsonTest = "{\"authorization\": \"%s\"}";
        String finalJsonString = String.format(jsonTest, test);
        AuthData classPlaceHolder = new AuthData("",null);
        AuthData oldTest = new Gson().fromJson(finalJsonString, classPlaceHolder.getClass());
        AuthData registerRequest = new AuthData(ctx.header("authorization"),null) ;
        String tester = registerRequest.authToken();
        String tester2 = registerRequest.username();


        try{
            //createUser user = handleregister(json);
            if (!User.findAuth(registerRequest.authToken())){
                ctx.status(401);
                throw new DataAccessException("{\"message\": \"Error: unauthorized\"}");
            }
            if (User.findAuth(registerRequest.authToken())){
                int count = 0;
                for (AuthData userAuth : listOfAuth) {
                    count++;
                    if (Objects.equals(userAuth.authToken(), registerRequest.authToken())) {
                        //loginUser.remove(userAuth);
                        ctx.result("{}");
                        break;
                    }
                }
                listOfAuth.remove(registerRequest.authToken().toString());
                //"{\"username\":" "\"registerRequest.username()\" , \"authToken\":" \"uuIDLogin\" }"
            }



        }catch(DataAccessException ex){

            ctx.result(ex.getMessage());
        }

    }
}
