package service;

import com.google.gson.Gson;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import model.TransitoryGameData;

import java.util.Objects;
import java.util.UUID;



public class Service {

    public String register(UserData registerRequest) throws DataAccessException {
        AuthData classPlaceHolder2 = new AuthData("","");

            if (registerRequest.username() == null || registerRequest.email() == null
                    || registerRequest.password() == null) {
                throw new DataAccessException("bad request");
            }
            if (new DatabaseManager().getUser(registerRequest.username())){
                throw new DataAccessException("already taken");
            }
            if (!new DatabaseManager().getUser(registerRequest.username())){
                new DatabaseManager().insertUserToUserTable(new UserData(registerRequest.username(),
                        registerRequest.password(), registerRequest.email()));
                String uuIDLogin = UUID.randomUUID().toString();
                AuthData result = new AuthData(registerRequest.username(), uuIDLogin);
                Gson gson = new Gson();
                new DatabaseManager().insertAuth(new AuthData(registerRequest.username(),uuIDLogin));
                String jsonString = gson.toJson(result, classPlaceHolder2.getClass());
                return (jsonString);
            }

        throw new DataAccessException("bad request");
    }

    public String login(UserData registerRequest) throws DataAccessException{
        AuthData classPlaceHolder2 = new AuthData( "", "");

            if (registerRequest.username() == null || registerRequest.password() == null) {
                throw new DataAccessException("bad request");
            }
            if (new DatabaseManager().checkLogin(registerRequest.username(), registerRequest.password())){
                String uuIDLogin = UUID.randomUUID().toString();
                new DatabaseManager().insertAuth(new AuthData(registerRequest.username(), uuIDLogin));
                Gson gson = new Gson();
                AuthData result = new AuthData(registerRequest.username(), uuIDLogin);
                String jsonString = gson.toJson(result, classPlaceHolder2.getClass());
               return jsonString;
            }
            if (!new DatabaseManager().checkLogin(registerRequest.username(), registerRequest.password())){
                throw new DataAccessException("unauthorized");
            }
        throw new DataAccessException("bad request");

    }

    public String logout(AuthData authTokenRequest) throws DataAccessException{
        AuthData registerRequest = new AuthData(new DatabaseManager().findUser(authTokenRequest.authToken()),
                authTokenRequest.authToken());

            if (!new DatabaseManager().findAuth(registerRequest.authToken())){
                throw new DataAccessException("unauthorized");
            }
            if (new DatabaseManager().findAuth(registerRequest.authToken())){
                return new DatabaseManager().logoutAuth(registerRequest.authToken());
            }
        throw new DataAccessException("bad request");

    }
    public String clear() throws DataAccessException {
        new DatabaseManager().clearDB();
        return "{}";
    }

    public String createGame(AuthData authTokenRequest, GameData registerRequestGame) throws DataAccessException{
        AuthData registerRequest = new AuthData(new DatabaseManager().findUser(authTokenRequest.authToken()),
                authTokenRequest.authToken());
            if (!new DatabaseManager().findAuth(registerRequest.authToken())){
                throw new DataAccessException("unauthorized");
            }
            if (new DatabaseManager().findAuth(registerRequest.authToken())){
                if (registerRequestGame.gameName() == null){
                    throw new DataAccessException("bad request");
                }
                new DatabaseManager().createGame(registerRequestGame);
                GameData game = new DatabaseManager().findGame(registerRequestGame.gameName());
                if (game == null){
                    throw new DataAccessException("bad request");
                }
                Gson gson = new Gson();
                String jsonString = gson.toJson(game);
                return jsonString;

            }
        throw new DataAccessException("bad request");


    }

    public String joinGame(AuthData authTokenResponse, TransitoryGameData joinRequest) throws DataAccessException{

        AuthData registerRequest = new AuthData(new DatabaseManager().findUser(authTokenResponse.authToken()),
                authTokenResponse.authToken());


            if (!new DatabaseManager().findAuth(registerRequest.authToken())){
                throw new DataAccessException("unauthorized");
            }
                //TransitoryGameData joinRequest = new Gson().fromJson(ctx.body(), TransitoryGameData.class);
                GameData game = new DatabaseManager().findGameByID(joinRequest.gameID());
                if (joinRequest.gameID() != 0){
                    if (Objects.equals(joinRequest.playerColor(), "WHITE")){
                        if (game.whiteUsername() == null){
                            new DatabaseManager().deleteGameByID(joinRequest.gameID());
                            new DatabaseManager().makeGame(new GameData(joinRequest.gameID(),
                                    registerRequest.username(), game.blackUsername(), game.gameName(), game.game()));
                        } else {

                            throw new DataAccessException("already taken");
                        }
                    }
                    else if (Objects.equals(joinRequest.playerColor(), "BLACK")){
                        if (game.blackUsername() == null){
                            new DatabaseManager().deleteGameByID(joinRequest.gameID());
                            new DatabaseManager().makeGame(new GameData(joinRequest.gameID(),
                                    game.whiteUsername(),registerRequest.username(),
                                    game.gameName(), game.game()));
                        } else {

                            throw new DataAccessException("already taken");
                        }
                    }else{
                            throw new DataAccessException("bad request");
                    }

                }
                else {
                    throw new DataAccessException("bad request");
                }
                return "{}";
    }

    public String listGame(AuthData authTokenRequest) throws DataAccessException {

        //AuthData authTokenRequest = new AuthData("",ctx.header("authorization")) ;
        AuthData registerRequest = new AuthData(new DatabaseManager().findUser(authTokenRequest.authToken()),
                authTokenRequest.authToken());


            if (!new DatabaseManager().findAuth(registerRequest.authToken())){
                throw new DataAccessException("unauthorized");
            }
            if (new DatabaseManager().findAuth(registerRequest.authToken())){

                Gson gson = new Gson();
                //var games = listGamesResult;
                return new DatabaseManager().listGames();
            }

        throw new DataAccessException("bad request");

    }

}
