package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import io.javalin.*;
import model.AuthData;
import model.GameData;
import model.TransitoryGameData;
import model.UserData;
import service.*;
import io.javalin.http.Context;

import java.util.Objects;

public class Server {

    private final Javalin javalin;

    public Server() {

        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        // Register your endpoints and exception handlers here.
        //javalin.post("/register/{name}", this::register);
        javalin.get("/hello", ctx -> ctx.result("Hello, Javalin!"));
        javalin.delete("/db", this::handleClear);
        javalin.post("/user", this::handleregister);
        javalin.post("/session", this::handleLogin);
        javalin.delete("/session", this::handleLogout);
        javalin.post("/game", this::handleCreateGame);
        javalin.put("/game", this::handleJoinGame);
        javalin.get("/game", this::handleListGame);
    }



    //var serializer = new Gson:  var req = serializer.fromJson(ctx.body(), Map.class); var res = serializer.toJson(res)ctx.result(res)


    public void handleregister(Context ctx) {
        try {
            UserData registerRequest = new Gson().fromJson(ctx.body(), UserData.class);
            ctx.result(new Service().register(registerRequest));
        }catch(DataAccessException ex){

            ctx.result(ex.getMessage());
            if (ex.getMessage() == "{\"message\": \"Error: already taken\"}"){
                ctx.status(403);
            }
            if (ex.getMessage() == "{\"message\": \"Error: bad request\"}"){
                ctx.status(400);
            }

        }
    }

    public void handleLogin(Context ctx){
        try {
            UserData registerRequest = new Gson().fromJson(ctx.body(), UserData.class);
            ctx.result(new Service().login(registerRequest));
        }catch(DataAccessException ex){

            ctx.result(ex.getMessage());
            if (ex.getMessage() == "{\"message\": \"Error: unauthorized\"}"){
                ctx.status(401);
            }
            if (ex.getMessage() == "{\"message\": \"Error: bad request\"}"){
                ctx.status(400);
            }

        }

    }
    public void handleLogout(Context ctx){

        try {
            AuthData authTokenRequest = new AuthData("", ctx.header("authorization")) ;
            ctx.result(new Service().logout(authTokenRequest));
        }catch(DataAccessException ex){

            ctx.result(ex.getMessage());
            helperFunctionForCodeQuality(ctx,ex);

        }


        //new Service().logout(ctx);
    }
    public void handleClear(Context ctx){
        new Service().clear();
    }
    private void handleCreateGame(Context ctx) {
        try {
            AuthData authTokenRequest = new AuthData("", ctx.header("authorization")) ;
            GameData registerRequestGame = new Gson().fromJson(ctx.body(), GameData.class);
            ctx.result(new Service().createGame(authTokenRequest, registerRequestGame));
        }catch(DataAccessException ex){

            ctx.result(ex.getMessage());
            if (Objects.equals(ex.getMessage(), "{\"message\": \"Error: unauthorized\"}")){
                ctx.status(401);
            }
            if (ex.getMessage() == "{\"message\": \"Error: no game found\"}"){
                ctx.status(400);
            }
            if (Objects.equals(ex.getMessage(), "{\"message\": \"Error: bad request\"}")){
                ctx.status(400);
            }

        }
    }

    private void handleJoinGame(Context ctx){
        try {
            AuthData authTokenRequest = new AuthData("", ctx.header("authorization")) ;
            TransitoryGameData registerRequestGame = new Gson().fromJson(ctx.body(), TransitoryGameData.class);
            ctx.result(new Service().joinGame(authTokenRequest, registerRequestGame));
        }catch(DataAccessException ex){

            ctx.result(ex.getMessage());
            if (Objects.equals(ex.getMessage(), "{\"message\": \"Error: unauthorized\"}")){
                ctx.status(401);
            }
            if (ex.getMessage() == "{\"message\": \"Error: no game found\"}"){
                ctx.status(400);
            }
            if (Objects.equals(ex.getMessage(), "{\"message\": \"Error: bad request\"}")){
                ctx.status(400);
            }
            if (Objects.equals(ex.getMessage(), "{\"message\": \"Error: already taken\"}")){
                ctx.status(403);
            }



        }
    }

    private void handleListGame(Context ctx){
        try {
            AuthData authTokenRequest = new AuthData("", ctx.header("authorization")) ;
            ctx.result(new Service().listGame(authTokenRequest));
        }catch(DataAccessException ex){

            ctx.result(ex.getMessage());
            helperFunctionForCodeQuality(ctx,ex);

        }
    }

    public void helperFunctionForCodeQuality(Context ctx, DataAccessException ex){
        if (Objects.equals(ex.getMessage(), "{\"message\": \"Error: unauthorized\"}")){
            ctx.status(401);
        }
        if (Objects.equals(ex.getMessage(), "{\"message\": \"Error: bad request\"}")){
            ctx.status(400);
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
