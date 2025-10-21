import chess.ChessGame;
import dataaccess.Auth;
import dataaccess.DataAccessException;
import dataaccess.Game;
import dataaccess.User;
import model.GameData;
import model.TransitoryGameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Service;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class service {
    @BeforeEach
    public void setup(){
        new Service().clear();
    }

    @Test
    public void registerTestPass() throws DataAccessException {
        UserData testUser = new UserData("testUser","pass","email@1");
        new Service().register(testUser);
        assert(!User.listofUsers.isEmpty());
    }
    @Test
    public void registerTestFail() throws DataAccessException {
        try {
            UserData testUser = new UserData("testUser", "pass", "email@1");
            new Service().register(testUser);
            UserData testUserAgain = new UserData("testUser", "ok", "email@1");
            new Service().register(testUserAgain);
        }catch(DataAccessException ex){
            if (ex.getMessage() == "{\"message\": \"Error: already taken\"}"){
                assertTrue(true);
            }

    }
    }
    @Test
    public void loginTestPass() throws DataAccessException {
        UserData testUser = new UserData("testUser","pass","email@1");
        new Service().register(testUser);
        new Service().logout(Auth.listOfAuth.get(0));
        new Service().login(testUser);
        assert(!Auth.listOfAuth.isEmpty());
    }
    @Test
    public void loginTestFail() throws DataAccessException {
        try {

            UserData testUser = new UserData("testUser", "pass", "email@1");
            new Service().register(testUser);
            UserData testUserUnAuthorized = new UserData("testUser2", "pass", "email@1");
            new Service().logout(Auth.listOfAuth.get(0));
            //assert (!Auth.listOfAuth.isEmpty());
            new Service().login(testUserUnAuthorized);
        } catch (DataAccessException ex) {
            if (ex.getMessage() == "{\"message\": \"Error: unauthorized\"}") {
                assertTrue(true);

            }
        }
    }
    @Test
    public void logoutTestPass() throws DataAccessException {
        UserData testUser = new UserData("testUser","pass","email@1");
        new Service().register(testUser);
        new Service().logout(Auth.listOfAuth.get(0));
        assert(Auth.listOfAuth.isEmpty());
    }
    @Test
    public void logoutTestFail() throws DataAccessException {
        try{
        UserData testUser = new UserData("testUser","pass","email@1");
        //UserData testUserUnAuthorized = new UserData("testUser2", "pass", "email@1");
        new Service().register(testUser);

        new Service().logout(Auth.listOfAuth.get(0));
        new Service().logout(Auth.listOfAuth.get(0));


        } catch (IndexOutOfBoundsException ex) {
            assertTrue(true);
    }
    }
    @Test
    public void createGamePass() throws DataAccessException{
        UserData testUser = new UserData("testUser","pass","email@1");
        new Service().register(testUser);
        GameData testGame = new GameData(5,"Bob", "Billy",
                "testGame", new ChessGame());
        new Service().createGame(Auth.listOfAuth.get(0), testGame);
        assert(!Game.listOfGames.isEmpty());
    }
    @Test
    public void createGameFail() throws DataAccessException {
        try{
        GameData testGame = new GameData(5,"Bob", "Billy",
                "testGame", null);
        new Service().createGame(Auth.listOfAuth.get(0), testGame);
        assert(Game.listOfGames.isEmpty());

    } catch (IndexOutOfBoundsException ex) {

            assertTrue(true);
    }
    }
    @Test
    public void joinGamePassWhiteUser() throws DataAccessException {
        UserData testUser = new UserData("testUser","pass","email@1");
        new Service().register(testUser);
        assert(!User.listofUsers.isEmpty());
        GameData testGame = new GameData(5,null, null,
                "testGame", new ChessGame());
        TransitoryGameData newTestGame = new TransitoryGameData(5,"WHITE");
        //new Service().createGame(Auth.listOfAuth.get(0), testGame);
        new Game().makeGame(testGame);

        assert(!Game.listOfGames.isEmpty());
        new Service().joinGame(Auth.listOfAuth.get(0), newTestGame);
        for (GameData game: Game.listOfGames){
            assert(Objects.equals(game.whiteUsername(), "testUser"));
        }
    }
    @Test
    public void joinGameFailWhiteUser() throws DataAccessException {
        try {
            UserData testUser = new UserData("testUser", "pass", "email@1");
            new Service().register(testUser);
            assert (!User.listofUsers.isEmpty());
            GameData testGame = new GameData(5, null, null,
                    "testGame", new ChessGame());
            TransitoryGameData newTestGame = new TransitoryGameData(5, null);
            new Game().makeGame(testGame);
            assert (!Game.listOfGames.isEmpty());
            new Service().joinGame(Auth.listOfAuth.get(0), newTestGame);
            for (GameData game : Game.listOfGames) {
                assert (!Objects.equals(game.whiteUsername(), "testUser"));
            }
        }catch (DataAccessException ex) {
            if (ex.getMessage() == "{\"message\": \"Error: bad request\"}") {
                assertTrue(true);

            }
        }
    }

    @Test
    public void joinGamePassBlackUser() throws DataAccessException {
        UserData testUser = new UserData("testUser","pass","email@1");
        new Service().register(testUser);
        assert(!User.listofUsers.isEmpty());
        GameData testGame = new GameData(5,null, null,
                "testGame", new ChessGame());
        TransitoryGameData newTestGame = new TransitoryGameData(5,"BLACK");
        new Game().makeGame(testGame);

        new Service().joinGame(Auth.listOfAuth.get(0), newTestGame);
        for (GameData game: Game.listOfGames){
            assert(Objects.equals(game.blackUsername(), "testUser"));
        }
    }
    @Test
    public void joinGameFailBlackUser() throws DataAccessException {
        try{
        UserData testUser = new UserData("testUser","pass","email@1");
        new Service().register(testUser);
        assert(!User.listofUsers.isEmpty());
        GameData testGame = new GameData(5,"", "",
                "testGame", new ChessGame());
        TransitoryGameData newTestGame = new TransitoryGameData(5,null);
        new Service().createGame(Auth.listOfAuth.get(0), testGame);
        assert(!Game.listOfGames.isEmpty());
        new Service().joinGame(Auth.listOfAuth.get(0), newTestGame);
        for (GameData game: Game.listOfGames){
            assert(!Objects.equals(game.blackUsername(), "testUser"));
        }
    }catch (DataAccessException ex) {
        if (ex.getMessage() == "{\"message\": \"Error: bad request\"}") {
            assertTrue(true);

        }
    }
    }
    @Test
    public void listGamePass() throws DataAccessException {
        UserData testUser = new UserData("testUser","pass","email@1");
        new Service().register(testUser);
        assert(!User.listofUsers.isEmpty());
        GameData testGame = new GameData(5,"", "",
                "testGame", new ChessGame());
        new Service().createGame(Auth.listOfAuth.get(0), testGame);
        assert(!Game.listOfGames.isEmpty());

    }
    @Test
    public void listGameFail() throws DataAccessException {
        try{
        UserData testUser = new UserData("testUser","pass","email@1");
        UserData testUserUnAuthorized = new UserData("testUser2","pass","email@1");

            new Service().register(testUserUnAuthorized);
        assert(!User.listofUsers.isEmpty());
        GameData testGame = new GameData(5,"", "",
                "testGame", new ChessGame());
        new Service().createGame(Auth.listOfAuth.get(0), testGame);
        assert(!Game.listOfGames.isEmpty());

    } catch (DataAccessException ex) {
        if (ex.getMessage() == "{\"message\": \"Error: unauthorized\"}") {
            assertTrue(true);

        }
    }
    }

    @Test
    public void clearTestPass() throws DataAccessException {
        UserData testUser = new UserData("testUser","pass","email@1");
        new Service().register(testUser);
        assert(!User.listofUsers.isEmpty());
        assert(!Auth.listOfAuth.isEmpty());
        GameData testGame = new GameData(5,"", "",
                "testGame", new ChessGame());
        new Service().createGame(Auth.listOfAuth.get(0), testGame );
        assert(!Game.listOfGames.isEmpty());
        new Service().clear();
        assert(User.listofUsers.isEmpty());
        assert(Auth.listOfAuth.isEmpty());
        assert(Game.listOfGames.isEmpty());

    }


}