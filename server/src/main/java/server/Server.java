package server;

import io.javalin.*;
import service.*;
import io.javalin.http.Context;

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


    public void handleregister(Context ctx) throws Exception {
        new Service().register(ctx);
    }

    public void handleLogin(Context ctx){
        new Service().login(ctx);

    }
    public void handleLogout(Context ctx){
        new Service().logout(ctx);
    }
    public void handleClear(Context ctx){
        new Service().clear(ctx);
    }
    private void handleCreateGame(Context ctx) {
        new Service().createGame(ctx);
    }

    private void handleJoinGame(Context ctx){
        new Service().joinGame(ctx);
    }

    private void handleListGame(Context ctx){
        new Service().listGame(ctx);
    }


    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }


    public void stop() {
        javalin.stop();
    }

}
