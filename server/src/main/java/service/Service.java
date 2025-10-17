package service;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.Auth;
import dataaccess.DataAccessException;
import dataaccess.Game;
import dataaccess.User;
import io.javalin.http.Context;
import model.AuthData;
import model.GameData;
import model.UserData;
import model.TransitoryGameData;

import java.util.ArrayList;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static dataaccess.Auth.listOfAuth;


public class Service {
    public void register(Context ctx){
        UserData registerRequest = new Gson().fromJson(ctx.body(), UserData.class);
        AuthData classPlaceHolder2 = new AuthData("","");
        try{
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
                new Auth().loginUser(new AuthData(registerRequest.username(),uuIDLogin));
                String jsonString = gson.toJson(result, classPlaceHolder2.getClass());
                ctx.result(jsonString);
            }
        }catch(DataAccessException ex){

            ctx.result(ex.getMessage());
        }


    }

    public void login(Context ctx){
        UserData registerRequest = new Gson().fromJson(ctx.body(), UserData.class);
        AuthData classPlaceHolder2 = new AuthData( "", "");
        try{
            if (registerRequest.username() == null || registerRequest.password() == null) {
                ctx.status(400);
                throw new DataAccessException("{\"message\": \"Error: bad request\"}");
            }
            if (new User().checkLogin(registerRequest.username(), registerRequest.password())){
                String uuIDLogin = UUID.randomUUID().toString();
                new Auth().loginUser(new AuthData(registerRequest.username(), uuIDLogin));
                Gson gson = new Gson();
                AuthData result = new AuthData(registerRequest.username(), uuIDLogin);
                String jsonString = gson.toJson(result, classPlaceHolder2.getClass());
                ctx.result(jsonString);
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
        AuthData authTokenRequest = new AuthData("", ctx.header("authorization")) ;
        AuthData registerRequest = new AuthData(new Auth().findUser(authTokenRequest.authToken()), authTokenRequest.authToken());

        try{
            if (!new Auth().findAuth(registerRequest.authToken())){
                ctx.status(401);
                throw new DataAccessException("{\"message\": \"Error: unauthorized\"}");
            }
            if (new Auth().findAuth(registerRequest.authToken())){
                int count = 0;
                for (AuthData userAuth : listOfAuth) {
                    count++;
                    if (Objects.equals(userAuth.authToken(), registerRequest.authToken())) {
                        ctx.result("{}");
                        break;
                    }
                }
                listOfAuth.remove(count-1);
            }
        }catch(DataAccessException ex){

            ctx.result(ex.getMessage());
        }

    }
    public void clear(Context ctx){
        Auth.listOfAuth = new ArrayList<>();
        User.listofUsers = new ArrayList<>();
        Game.listOfGames = new ArrayList<>();
        ctx.result("{}");
    }

    public void createGame(Context ctx){
        AuthData authTokenRequest = new AuthData("",ctx.header("authorization"));
        AuthData registerRequest = new AuthData(new Auth().findUser(authTokenRequest.authToken()), authTokenRequest.authToken());

        try{

            if (!new Auth().findAuth(registerRequest.authToken())){
                ctx.status(401);
                throw new DataAccessException("{\"message\": \"Error: unauthorized\"}");
            }
            if (new Auth().findAuth(registerRequest.authToken())){
                GameData classPlaceHolderString = new GameData(-1, null, null, null, new ChessGame());
                GameData registerRequestGame = new Gson().fromJson(ctx.body(), classPlaceHolderString.getClass());
                if (registerRequestGame.gameName() == null){
                    ctx.status(400);
                    throw new DataAccessException("{\"message\": \"Error: bad request\"}");
                }
                new Game().createGame(registerRequestGame);
                GameData game = new Game().findGame(registerRequestGame.gameName());
                if (game == null){
                    ctx.status(400);
                    throw new DataAccessException("{\"message\": \"Error: no game found\"}");
                }
                Gson gson = new Gson();
                String jsonString = gson.toJson(game);
                ctx.result(jsonString);

            }

        }catch(DataAccessException ex){

            ctx.result(ex.getMessage());
        }
    }

    public void joinGame(Context ctx){

        AuthData authTokenResponse = new AuthData("",ctx.header("authorization")) ;
        AuthData registerRequest = new AuthData(new Auth().findUser(authTokenResponse.authToken()), authTokenResponse.authToken());

        try{
            if (!new Auth().findAuth(registerRequest.authToken())){
                ctx.status(401);
                throw new DataAccessException("{\"message\": \"Error: unauthorized\"}");
            }
                TransitoryGameData joinRequest = new Gson().fromJson(ctx.body(), TransitoryGameData.class);
                GameData game = new Game().findGameByID(joinRequest.gameID());
                if (joinRequest.gameID() != 0){
                    if (Objects.equals(joinRequest.playerColor(), "WHITE")){
                        if (game.whiteUsername() == null){
                            new Game().deleteGameByID(joinRequest.gameID());
                            new Game().makeGame(new GameData(joinRequest.gameID(),
                                    registerRequest.username(), game.blackUsername(), game.gameName(), game.game()));
                        } else {
                            ctx.status(403);
                            throw new DataAccessException("{\"message\": \"Error: already taken\"}");
                        }
                    }
                    else if (Objects.equals(joinRequest.playerColor(), "BLACK")){
                        if (game.blackUsername() == null){
                            new Game().deleteGameByID(joinRequest.gameID());
                            new Game().makeGame(new GameData(joinRequest.gameID(),
                                    game.whiteUsername(),registerRequest.username(),
                                    game.gameName(), game.game()));
                        } else {
                            ctx.status(403);
                            throw new DataAccessException("{\"message\": \"Error: already taken\"}");
                        }
                    }else{
                            ctx.status(400);
                            throw new DataAccessException("{\"message\": \"Error: bad request\"}");
                    }

                }
                else {
                    ctx.status(400);
                    throw new DataAccessException("{\"message\": \"Error: bad request\"}");
                }

                ctx.result("{}");

        }catch(DataAccessException ex){

            ctx.result(ex.getMessage());
        }

    }

    public void listGame(Context ctx) {

        AuthData authTokenRequest = new AuthData("",ctx.header("authorization")) ;
        AuthData registerRequest = new AuthData(new Auth().findUser(authTokenRequest.authToken()), authTokenRequest.authToken());

        try{
            if (!new Auth().findAuth(registerRequest.authToken())){
                ctx.status(401);
                throw new DataAccessException("{\"message\": \"Error: unauthorized\"}");
            }
            if (new Auth().findAuth(registerRequest.authToken())){

                Gson gson = new Gson();
                 String listGamesResult = gson.toJson(Map.of("games",Game.listOfGames));
                 //var games = listGamesResult;
                ctx.result(listGamesResult);
            }
        }catch(DataAccessException ex){
            ctx.result(ex.getMessage());
        }

    }
}
