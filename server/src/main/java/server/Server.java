package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
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
        //Javalin.delete(/"db", ctx -> ctx.result("{}")))
        javalin.post("/register", ctx -> {
            UserData classPlaceHolder = new UserData("","","");
            UserData json = new Gson().fromJson(ctx.body(), classPlaceHolder.getClass());

            try{
                handleregister(json);
            }catch(DataAccessException ex){
                ctx.result(ex.getMessage());
            }
        });
    }

    //var serializer = new Gson:  var req = serializer.fromJson(ctx.body(), Map.class); var res = serializer.toJson(res)ctx.result(res)


    public createUser handleregister(UserData registerRequest) throws Exception {
        if (Service.getUser(registerRequest.username())){
            return new createUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
        }

        throw new DataAccessException("{\"message\": \"Error: already taken\"}");
    }


    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }


    public void stop() {
        javalin.stop();
    }

}
