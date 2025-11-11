package client;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.TransitoryGameData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import serverFacade.serverFacade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;


public class ServerFacadeTests {

    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }
@BeforeEach
public void clearDB() throws IOException, InterruptedException, DataAccessException {
        new serverFacade().clearDB();
}

    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void testRegisterFacatePositive() throws IOException, InterruptedException, DataAccessException {
        UserData testUser = new UserData("registeringPerson", "registeringPassword","registeringEmail");
        assert(new serverFacade().registerUser(testUser) instanceof AuthData);
    }

    @Test
    public void testRegisterFacateNegative() throws IOException, InterruptedException, DataAccessException {
        try {
            UserData testUser = new UserData("registeringPerson", "registeringPassword", "registeringEmail");
            UserData testUser2 = new UserData("registeringPerson", "registeringPassword", "registeringEmail");
            new serverFacade().registerUser(testUser);
            new serverFacade().registerUser(testUser2);
        } catch (DataAccessException e) {
            if (Objects.equals(e.getMessage(), "already taken")){
                assert(true);
            }
        }
    }


    @Test
    public void testLogoutPositive() throws IOException, InterruptedException, DataAccessException {
        UserData testUser = new UserData("registeringPerson", "registeringPassword","registeringEmail");
        AuthData auth = new serverFacade().registerUser(testUser);
        assert(new serverFacade().logoutUser(auth.authToken()) instanceof String);
    }

    @Test
    public void testLogoutNegative() throws IOException, InterruptedException, DataAccessException {
        try {
            UserData testUser = new UserData("registeringPerson", "registeringPassword", "registeringEmail");
            AuthData auth = new serverFacade().registerUser(testUser);
            AuthData newAuth = auth;
            if (new serverFacade().logoutUser(auth.authToken()) instanceof String) {
                newAuth = new AuthData(auth.username(), "test");
            }
            new serverFacade().logoutUser(newAuth.authToken());
        } catch (DataAccessException e) {
            if (Objects.equals(e.getMessage(), "unauthorized")){
                assert(true);
            }
        }
    }


    @Test
    public void testLoginPositive() throws IOException, InterruptedException, DataAccessException {
        UserData testUser = new UserData("registeringPerson", "registeringPassword","registeringEmail");
        AuthData auth = new serverFacade().registerUser(testUser);
        new serverFacade().logoutUser(auth.authToken());
        assert(new serverFacade().loginUser(testUser) instanceof AuthData);
    }

    @Test
    public void testLoginNegative() throws IOException, InterruptedException, DataAccessException {
        try {
            UserData testUser = new UserData("registeringPerson", "registeringPassword", "registeringEmail");
            new serverFacade().loginUser(testUser);

        } catch (DataAccessException e) {
            if (Objects.equals(e.getMessage(), "unauthorized")){
                assert(true);
            }
        }
    }

    @Test
    public void testCreateGamePositive() throws IOException, InterruptedException, DataAccessException {
        UserData testUser = new UserData("registeringPerson", "registeringPassword", "registeringEmail");
        AuthData auth = new serverFacade().registerUser(testUser);
        GameData testGame = new GameData(0, null, null, "newTestGame", new ChessGame());
        assert (new serverFacade().createGame(testGame, auth.authToken()) instanceof String);
    }


    @Test
    public void testCreateGameNegative() throws IOException, InterruptedException, DataAccessException {
        try{UserData testUser = new UserData("registeringPerson", "registeringPassword", "registeringEmail");
        AuthData auth = new serverFacade().registerUser(testUser);
        GameData testGame = new GameData(0, null, null, null, new ChessGame());
        new serverFacade().createGame(testGame, auth.authToken());
        } catch (DataAccessException e) {
            if(Objects.equals(e.getMessage(), "bad request")){
                assert(true);
            }
        }
    }

    @Test
    public void testPlayGamePositive() throws IOException, InterruptedException, DataAccessException {
        UserData testUser = new UserData("registeringPerson", "registeringPassword", "registeringEmail");
        AuthData auth = new serverFacade().registerUser(testUser);
        GameData testGame = new GameData(0, null, null, "newTestGame", new ChessGame());
        new serverFacade().createGame(testGame, auth.authToken());
        String listofGames = new serverFacade().listGame(auth.authToken());
        Map gameDataInfoArray = new Gson().fromJson(listofGames, Map.class);
        ArrayList<LinkedTreeMap> gamesInGameList = (ArrayList<LinkedTreeMap>) gameDataInfoArray.get("games");
        LinkedTreeMap hopeGame = gamesInGameList.get(0);
        Object shouldbeID = hopeGame.get("gameID");
        double realID = (double) shouldbeID;
        TransitoryGameData joinGameData = new TransitoryGameData((int) realID,"WHITE");
        assert(new serverFacade().playGame(joinGameData,auth.authToken()) instanceof String);
        //assert(new serverFacade().playGame())
    }

    @Test
    public void testPlayGameNegative() throws IOException, InterruptedException, DataAccessException {

        try {
            UserData testUser = new UserData("registeringPerson", "registeringPassword", "registeringEmail");
            AuthData auth = new serverFacade().registerUser(testUser);
            GameData testGame = new GameData(0, null, null, "newTestGame", new ChessGame());
            new serverFacade().createGame(testGame, auth.authToken());
            String listofGames = new serverFacade().listGame(auth.authToken());
            Map gameDataInfoArray = new Gson().fromJson(listofGames, Map.class);
            ArrayList<LinkedTreeMap> gamesInGameList = (ArrayList<LinkedTreeMap>) gameDataInfoArray.get("games");
            LinkedTreeMap hopeGame = gamesInGameList.get(0);
            Object shouldbeID = hopeGame.get("gameID");
            double realID = (double) shouldbeID;
            TransitoryGameData joinGameData = new TransitoryGameData((int) realID, null);
        } catch (Exception e) {
            if (e.getMessage() == "bad request"){
                assert(true);
            }
        }
        //assert(new serverFacade().playGame())
    }


    @Test
    public void listGamesPositive() throws IOException, InterruptedException, DataAccessException {
        UserData testUser = new UserData("registeringPerson", "registeringPassword", "registeringEmail");
        AuthData auth = new serverFacade().registerUser(testUser);
        GameData testGame = new GameData(0, null, null, "newTestGame", new ChessGame());
        new serverFacade().createGame(testGame, auth.authToken());
        String listofGames = new serverFacade().listGame(auth.authToken());
        assert(listofGames != null);
        //assert(new serverFacade().playGame())
    }

    @Test
    public void listGamesNegative() throws IOException, InterruptedException, DataAccessException {
        try {
            UserData testUser = new UserData("registeringPerson", "registeringPassword", "registeringEmail");
            AuthData auth = new serverFacade().registerUser(testUser);
            GameData testGame = new GameData(0, null, null, "newTestGame", new ChessGame());
            new serverFacade().createGame(testGame, auth.authToken());
            String listofGames = new serverFacade().listGame("eeee");
            //assert(listofGames != null);
            //assert(new serverFacade().playGame())
        } catch (Exception e) {
            if (e.getMessage() == "unauthorized"){
                assert(true);
            }
        }
    }
    @Test
    public void clearDBPositive() throws IOException, InterruptedException, DataAccessException {

        try{assert(new serverFacade().clearDB() == null);} catch (DataAccessException e) {
           throw new DataAccessException(e.getMessage());
       }

        //assert(new serverFacade().playGame())
    }
}



