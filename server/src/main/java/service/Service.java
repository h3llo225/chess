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
    public String register(UserData registerRequest) throws DataAccessException {
        AuthData classPlaceHolder2 = new AuthData("","");

            if (registerRequest.username() == null || registerRequest.email() == null || registerRequest.password() == null) {
                throw new DataAccessException("{\"message\": \"Error: bad request\"}");
            }
            if (!new User().getUser(registerRequest.username())){
                throw new DataAccessException("{\"message\": \"Error: already taken\"}");
            }
            if (new User().getUser(registerRequest.username())){
                new User().createUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
                String uuIDLogin = UUID.randomUUID().toString();
                AuthData result = new AuthData(registerRequest.username(), uuIDLogin);
                Gson gson = new Gson();
                new Auth().loginUser(new AuthData(registerRequest.username(),uuIDLogin));
                String jsonString = gson.toJson(result, classPlaceHolder2.getClass());
                return (jsonString);
            }

        throw new DataAccessException("{\"message\": \"Error: bad request\"}");
    }

    public String login(UserData registerRequest) throws DataAccessException{
        AuthData classPlaceHolder2 = new AuthData( "", "");

            if (registerRequest.username() == null || registerRequest.password() == null) {
                throw new DataAccessException("{\"message\": \"Error: bad request\"}");
            }
            if (new User().checkLogin(registerRequest.username(), registerRequest.password())){
                String uuIDLogin = UUID.randomUUID().toString();
                new Auth().loginUser(new AuthData(registerRequest.username(), uuIDLogin));
                Gson gson = new Gson();
                AuthData result = new AuthData(registerRequest.username(), uuIDLogin);
                String jsonString = gson.toJson(result, classPlaceHolder2.getClass());
               return jsonString;
            }
            if (!new User().checkLogin(registerRequest.username(), registerRequest.password())){
                throw new DataAccessException("{\"message\": \"Error: unauthorized\"}");
            }
        throw new DataAccessException("{\"message\": \"Error: bad request\"}");

    }

    public String logout(AuthData authTokenRequest) throws DataAccessException{
        AuthData registerRequest = new AuthData(new Auth().findUser(authTokenRequest.authToken()), authTokenRequest.authToken());

            if (!new Auth().findAuth(registerRequest.authToken())){
                throw new DataAccessException("{\"message\": \"Error: unauthorized\"}");
            }
            if (new Auth().findAuth(registerRequest.authToken())){
                int count = 0;
                for (AuthData userAuth : listOfAuth) {
                    count++;
                    if (Objects.equals(userAuth.authToken(), registerRequest.authToken())) {
                        listOfAuth.remove(count-1);
                        return "{}";
                    }
                }

            }
        throw new DataAccessException("{\"message\": \"Error: bad request\"}");

    }
    public String clear(){
        Auth.listOfAuth = new ArrayList<>();
        User.listofUsers = new ArrayList<>();
        Game.listOfGames = new ArrayList<>();
        return "{}";
    }

    public String createGame(AuthData authTokenRequest, GameData registerRequestGame) throws DataAccessException{
        AuthData registerRequest = new AuthData(new Auth().findUser(authTokenRequest.authToken()), authTokenRequest.authToken());



            if (!new Auth().findAuth(registerRequest.authToken())){
                throw new DataAccessException("{\"message\": \"Error: unauthorized\"}");
            }
            if (new Auth().findAuth(registerRequest.authToken())){
                if (registerRequestGame.gameName() == null){
                    throw new DataAccessException("{\"message\": \"Error: bad request\"}");
                }
                new Game().createGame(registerRequestGame);
                GameData game = new Game().findGame(registerRequestGame.gameName());
                if (game == null){
                    throw new DataAccessException("{\"message\": \"Error: no game found\"}");
                }
                Gson gson = new Gson();
                String jsonString = gson.toJson(game);
                return jsonString;

            }
        throw new DataAccessException("{\"message\": \"Error: bad request\"}");


    }

    public String joinGame(AuthData authTokenResponse, TransitoryGameData joinRequest) throws DataAccessException{

        AuthData registerRequest = new AuthData(new Auth().findUser(authTokenResponse.authToken()), authTokenResponse.authToken());


            if (!new Auth().findAuth(registerRequest.authToken())){
                throw new DataAccessException("{\"message\": \"Error: unauthorized\"}");
            }
                //TransitoryGameData joinRequest = new Gson().fromJson(ctx.body(), TransitoryGameData.class);
                GameData game = new Game().findGameByID(joinRequest.gameID());
                if (joinRequest.gameID() != 0){
                    if (Objects.equals(joinRequest.playerColor(), "WHITE")){
                        if (game.whiteUsername() == null){
                            new Game().deleteGameByID(joinRequest.gameID());
                            new Game().makeGame(new GameData(joinRequest.gameID(),
                                    registerRequest.username(), game.blackUsername(), game.gameName(), game.game()));
                        } else {

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

                            throw new DataAccessException("{\"message\": \"Error: already taken\"}");
                        }
                    }else{
                            throw new DataAccessException("{\"message\": \"Error: bad request\"}");
                    }

                }
                else {
                    throw new DataAccessException("{\"message\": \"Error: bad request\"}");
                }

                return "{}";



    }

    public String listGame(AuthData authTokenRequest) throws DataAccessException {

        //AuthData authTokenRequest = new AuthData("",ctx.header("authorization")) ;
        AuthData registerRequest = new AuthData(new Auth().findUser(authTokenRequest.authToken()), authTokenRequest.authToken());


            if (!new Auth().findAuth(registerRequest.authToken())){
                throw new DataAccessException("{\"message\": \"Error: unauthorized\"}");
            }
            if (new Auth().findAuth(registerRequest.authToken())){

                Gson gson = new Gson();
                 String listGamesResult = gson.toJson(Map.of("games",Game.listOfGames));
                 //var games = listGamesResult;
                return listGamesResult;
            }

        throw new DataAccessException("{\"message\": \"Error: bad request\"}");

    }


}
