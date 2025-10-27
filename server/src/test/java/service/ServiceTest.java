package service;
import chess.ChessGame;
import dataaccess.*;
import model.GameData;
import model.TransitoryGameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServiceTest {
    @BeforeEach
    public void setup() throws DataAccessException {
        new Service().clear();
    }

    @Test
    public void registerTestPass() throws DataAccessException {
        UserData testUser = new UserData("testUser","pass","email@1");
        new Service().register(testUser);
        assert(new DatabaseManager().checkAll("notnull",null,null));
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
        new Service().logout(new DatabaseManager().findAuthByUsername(testUser.username()));
        new Service().login(testUser);
        assert(new DatabaseManager().checkAll("notnull",null,null));
    }
    @Test
    public void loginTestFail() throws DataAccessException {
        try {

            UserData testUser = new UserData("testUser", "pass", "email@1");
            new Service().register(testUser);
            UserData testUserUnAuthorized = new UserData("testUser2", "pass", "email@1");
            new Service().logout(new DatabaseManager().findAuthByUsername(testUser.username()));
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
        new Service().logout(new DatabaseManager().findAuthByUsername(testUser.username()));
        assert(!new DatabaseManager().checkAll("notnull",null,null));
    }
    @Test
    public void logoutTestFail() throws DataAccessException {
        try{
        UserData testUser = new UserData("testUser","pass","email@1");
        //UserData testUserUnAuthorized = new UserData("testUser2", "pass", "email@1");
        new Service().register(testUser);

        new Service().logout(new DatabaseManager().findAuthByUsername(testUser.username()));
        new Service().logout(new DatabaseManager().findAuthByUsername(testUser.username()));
        } catch (DataAccessException ex) {
            assertTrue(true);
    }
    }
    @Test
    public void createGamePass() throws DataAccessException{
        UserData testUser = new UserData("testUser","pass","email@1");
        new Service().register(testUser);
        GameData testGame = new GameData(5,"Bob", "Billy",
                "testGame", new ChessGame());
        new Service().createGame(new DatabaseManager().findAuthByUsername(testUser.username()), testGame);
        assert(new DatabaseManager().checkAll(null,null,"notnull"));
    }
    @Test
    public void createGameFail() throws DataAccessException {
        try{
            UserData testUser = new UserData("testUser","pass","email@1");
            new Service().register(testUser);
        GameData testGame = new GameData(5,null, null,
                null, null);
        new Service().createGame(new DatabaseManager().findAuthByUsername(testUser.username()), testGame);
        assert(!new DatabaseManager().checkAll(null,null,"notnull"));

    } catch (DataAccessException ex) {

            assertTrue(true);
    }
    }
    @Test
    public void joinGamePassWhiteUser() throws DataAccessException {
        UserData testUser = new UserData("testUser","pass","email@1");
        new Service().register(testUser);
        GameData testGame = new GameData(5,null, null,
                "testGame", new ChessGame());
        TransitoryGameData newTestGame = new TransitoryGameData(5,"WHITE");
        //new Service().createGame(Auth.listOfAuth.get(0), testGame);
        new DatabaseManager().makeGame(testGame);
        assert(new DatabaseManager().checkAll(null,null,"notnull"));
        new Service().joinGame(new DatabaseManager().findAuthByUsername(testUser.username()), newTestGame);
        for (GameData game: new DatabaseManager().listGamesIntoArray()){
            assert(Objects.equals(game.whiteUsername(), "testUser"));
        }
    }
    @Test
    public void joinGameFailWhiteUser() throws DataAccessException {
        try {
            UserData testUser = new UserData("testUser", "pass", "email@1");
            new Service().register(testUser);
            GameData testGame = new GameData(5, null, null,
                    "testGame", new ChessGame());
            TransitoryGameData newTestGame = new TransitoryGameData(5, null);
            new DatabaseManager().makeGame(testGame);
            new Service().joinGame(new DatabaseManager().findAuthByUsername(testUser.username()), newTestGame);
            for (GameData game : new DatabaseManager().listGamesIntoArray()) {
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
        GameData testGame = new GameData(5,null, null,
                "testGame", new ChessGame());
        TransitoryGameData newTestGame = new TransitoryGameData(5,"BLACK");
        new DatabaseManager().makeGame(testGame);

        new Service().joinGame(new DatabaseManager().findAuthByUsername(testUser.username()), newTestGame);
        for (GameData game: new DatabaseManager().listGamesIntoArray()){
            assert(Objects.equals(game.blackUsername(), "testUser"));
        }
    }
    @Test
    public void joinGameFailBlackUser() throws DataAccessException {
        try{
        UserData testUser = new UserData("testUser","pass","email@1");
        new Service().register(testUser);
        GameData testGame = new GameData(5,"", "",
                "testGame", new ChessGame());
        TransitoryGameData newTestGame = new TransitoryGameData(5,null);
        new Service().createGame(new DatabaseManager().findAuthByUsername(testUser.username()), testGame);
        new Service().joinGame(new DatabaseManager().findAuthByUsername(testUser.username()), newTestGame);
        for (GameData game: new DatabaseManager().listGamesIntoArray()){
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
        GameData testGame = new GameData(5,"", "",
                "testGame", new ChessGame());
        new Service().createGame(new DatabaseManager().findAuthByUsername(testUser.username()), testGame);
        assert(new DatabaseManager().checkAll(null,null,"notnull"));

    }
    @Test
    public void listGameFail() throws DataAccessException {
        try{
        UserData testUser = new UserData("testUser","pass","email@1");
        UserData testUserUnAuthorized = new UserData("testUser2","pass","email@1");

        new Service().register(testUserUnAuthorized);
        GameData testGame = new GameData(5,"", "",
                "testGame", new ChessGame());
        new Service().createGame(new DatabaseManager().findAuthByUsername(testUserUnAuthorized.username()), testGame);
        assert(new DatabaseManager().checkAll(null,null,"notnull"));

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
        GameData testGame = new GameData(5,"", "",
                "testGame", new ChessGame());
        new Service().createGame(new DatabaseManager().findAuthByUsername(testUser.username()), testGame );
        new Service().clear();
        assert(!new DatabaseManager().checkAll("notnull",null,null));
        assert(!new DatabaseManager().checkAll(null,null,"notnull"));

    }

    @Test
    public void clearTestFail() throws DataAccessException {

        try{UserData testUser = new UserData("testUser","pass","email@1");
        new Service().register(testUser);
        GameData testGame = new GameData(5,"", "",
                "testGame", null);
        new Service().createGame(new DatabaseManager().findAuthByUsername(testUser.username()), testGame );
        new Service().clear();
        assert(!new DatabaseManager().checkAll("notnull",null,null));
        assert(!new DatabaseManager().checkAll(null,null,"notnull"));
        }
        catch (DataAccessException ex) {
                assertTrue(true);

            }
    }


}