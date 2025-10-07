package server;

import com.google.gson.Gson;
import dataaccess.createUser;
import io.javalin.*;
import Service.*;
import io.javalin.http.Context;
import model.UserData;
import org.jetbrains.annotations.NotNull;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        //javalin.post("/register/{name}", this::register);
        javalin.get("/hello", ctx -> ctx.result("Hello, Javalin!"));

        javalin.post("/register", ctx -> {
            UserData classPlaceHolder = new UserData("","","");
            UserData json = new Gson().fromJson(ctx.body(), classPlaceHolder.getClass());
            createUser user = handleregister(json);
        });
    }




    public createUser handleregister(UserData registerRequest) throws Exception {
        if (Service.getUser(registerRequest.username())){
            return new createUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
        }
        Exception InvalidUserException = new Exception();
        throw InvalidUserException;
    }


    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }


    public void stop() {
        javalin.stop();
    }

}
