package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.User;
import dataaccess.createUser;
import dataaccess.loginUser;
import model.AuthData;
import io.javalin.*;
import Service.*;
import io.javalin.http.Context;
import model.UserData;

import java.util.Objects;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import static Service.Service.login;
import static Service.Service.logout;
import static dataaccess.loginUser.listOfAuth;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        //javalin.post("/register/{name}", this::register);
        javalin.get("/hello", ctx -> ctx.result("Hello, Javalin!"));
        javalin.delete("/db", ctx -> ctx.result("{}"));
        javalin.post("/user", this::handleregister);
        javalin.post("/session", this::handleLogin);
        javalin.delete("/session", this::handleLogout);

    }

    //var serializer = new Gson:  var req = serializer.fromJson(ctx.body(), Map.class); var res = serializer.toJson(res)ctx.result(res)


    public void handleregister(Context ctx) throws Exception {
        Service.register(ctx);
    }

    public void handleLogin(Context ctx){
        login(ctx);

    }
    public void handleLogout(Context ctx){
        logout(ctx);
    }


    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }


    public void stop() {
        javalin.stop();
    }

}
