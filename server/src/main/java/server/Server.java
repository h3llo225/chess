package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.createUser;
import dataaccess.loginUser;
import model.AuthData;
import io.javalin.*;
import Service.*;
import io.javalin.http.Context;
import model.UserData;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        //javalin.post("/register/{name}", this::register);
        javalin.get("/hello", ctx -> ctx.result("Hello, Javalin!"));
        javalin.delete("/db", ctx -> ctx.result("{}"));
        javalin.post("/user", this::handleregister);
    }

    //var serializer = new Gson:  var req = serializer.fromJson(ctx.body(), Map.class); var res = serializer.toJson(res)ctx.result(res)


    public void handleregister(Context ctx) throws Exception {
        UserData classPlaceHolder = new UserData("","","");
        UserData registerRequest = new Gson().fromJson(ctx.body(), classPlaceHolder.getClass());

        try{
            //createUser user = handleregister(json);



        if (registerRequest.username() == null || registerRequest.email() == null || registerRequest.password() == null) {
            ctx.status(400);
            throw new DataAccessException("{\"message\": \"Error: bad request\"}");
        }
        if (Service.getUser(registerRequest.username())){
            new createUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
            String uuIDLogin = UUID.randomUUID().toString();
            loginUser loggingIn = new loginUser(new AuthData(uuIDLogin, registerRequest.username()));
            Gson gson = new Gson();
            String jsonString = gson.toJson(loggingIn);
            ctx.result(jsonString);
            //"{\"username\":" "\"registerRequest.username()\" , \"authToken\":" \"uuIDLogin\" }"
        }
        ctx.status(403);
        throw new DataAccessException("{\"message\": \"Error: already taken\"}");
        }catch(DataAccessException ex){

            ctx.result(ex.getMessage());
        }
    }


    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }


    public void stop() {
        javalin.stop();
    }

}
