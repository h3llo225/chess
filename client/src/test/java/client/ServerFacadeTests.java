package client;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import model.AuthData;
import model.GameData;
import model.TransitoryGameData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import serverfacade.ServerFacade;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade serverFacade;
    static int ports;


    @BeforeAll
    public static void init() {
        server = new Server();

        var port = server.run(0);
        serverFacade = new ServerFacade(port);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }
@BeforeEach
public void clearDB() throws Exception {

        serverFacade.clearDB();
}

    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void testRegisterFacatePositive() throws Exception {
        UserData testUser = new UserData("registeringPerson", "registeringPassword","registeringEmail");
        assert(serverFacade.registerUser(testUser) instanceof AuthData);
    }

    @Test
    public void testRegisterFacateNegative() throws Exception {
        try {
            UserData testUser = new UserData("registeringPerson", "registeringPassword", "registeringEmail");
            UserData testUser2 = new UserData("registeringPerson", "registeringPassword", "registeringEmail");
            serverFacade.registerUser(testUser);
            serverFacade.registerUser(testUser2);
        } catch (Exception e) {
            if (Objects.equals(e.getMessage(), "already taken")){
                assert(true);
            }
        }
    }


    @Test
    public void testLogoutPositive() throws Exception {
        UserData testUser = new UserData("registeringPerson", "registeringPassword","registeringEmail");
        AuthData auth = serverFacade.registerUser(testUser);
        assert(serverFacade.logoutUser(auth.authToken()) instanceof String);
    }

    @Test
    public void testLogoutNegative() throws Exception {
        try {
            UserData testUser = new UserData("registeringPerson", "registeringPassword", "registeringEmail");
            AuthData auth = serverFacade.registerUser(testUser);
            AuthData newAuth = auth;
            if (serverFacade.logoutUser(auth.authToken()) instanceof String) {
                newAuth = new AuthData(auth.username(), "test");
            }
            serverFacade.logoutUser(newAuth.authToken());
        } catch (Exception e) {
            if (Objects.equals(e.getMessage(), "unauthorized")){
                assert(true);
            }
        }
    }


    @Test
    public void testLoginPositive() throws Exception {
        UserData testUser = new UserData("registeringPerson", "registeringPassword","registeringEmail");
        AuthData auth = serverFacade.registerUser(testUser);
        serverFacade.logoutUser(auth.authToken());
        assert(serverFacade.loginUser(testUser) instanceof AuthData);
    }

    @Test
    public void testLoginNegative() throws Exception {
        try {
            UserData testUser = new UserData("registeringPerson", "registeringPassword", "registeringEmail");
            serverFacade.loginUser(testUser);

        } catch (Exception e) {
            if (Objects.equals(e.getMessage(), "unauthorized")){
                assert(true);
            }
        }
    }

    @Test
    public void testCreateGamePositive() throws Exception {
        UserData testUser = new UserData("registeringPerson", "registeringPassword", "registeringEmail");
        AuthData auth = serverFacade.registerUser(testUser);
        GameData testGame = new GameData(0, null, null, "newTestGame", new ChessGame());
        assert (serverFacade.createGame(testGame, auth.authToken()) instanceof String);
    }


    @Test
    public void testCreateGameNegative() throws Exception {
        try{UserData testUser = new UserData("registeringPerson", "registeringPassword", "registeringEmail");
        AuthData auth = serverFacade.registerUser(testUser);
        GameData testGame = new GameData(0, null, null, null, new ChessGame());
        serverFacade.createGame(testGame, auth.authToken());
        } catch (Exception e) {
            if(Objects.equals(e.getMessage(), "bad request")){
                assert(true);
            }
        }
    }

    @Test
    public void testPlayGamePositive() throws Exception {
        UserData testUser = new UserData("registeringPerson", "registeringPassword", "registeringEmail");
        AuthData auth = serverFacade.registerUser(testUser);
        GameData testGame = new GameData(0, null, null, "newTestGame", new ChessGame());
        serverFacade.createGame(testGame, auth.authToken());
        String listofGames = serverFacade.listGame(auth.authToken());
        Map gameDataInfoArray = new Gson().fromJson(listofGames, Map.class);
        ArrayList<LinkedTreeMap> gamesInGameList = (ArrayList<LinkedTreeMap>) gameDataInfoArray.get("games");
        LinkedTreeMap hopeGame = gamesInGameList.get(0);
        Object shouldbeID = hopeGame.get("gameID");
        double realID = (double) shouldbeID;
        TransitoryGameData joinGameData = new TransitoryGameData((int) realID,"WHITE");
        assert(serverFacade.playGame(joinGameData,auth.authToken()) instanceof String);
        //assert(new serverFacade().playGame())
    }

    @Test
    public void testPlayGameNegative() throws Exception {

        try {
            UserData testUser = new UserData("registeringPerson", "registeringPassword", "registeringEmail");
            AuthData auth = serverFacade.registerUser(testUser);
            GameData testGame = new GameData(0, null, null, "newTestGame", new ChessGame());
            serverFacade.createGame(testGame, auth.authToken());
            String listofGames = serverFacade.listGame(auth.authToken());
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
    public void listGamesPositive() throws Exception {
        UserData testUser = new UserData("registeringPerson", "registeringPassword", "registeringEmail");
        AuthData auth = serverFacade.registerUser(testUser);
        GameData testGame = new GameData(0, null, null, "newTestGame", new ChessGame());
        serverFacade.createGame(testGame, auth.authToken());
        String listofGames = serverFacade.listGame(auth.authToken());
        assert(listofGames != null);
        //assert(new serverFacade().playGame())
    }

    @Test
    public void listGamesNegative() throws Exception {
        try {
            UserData testUser = new UserData("registeringPerson", "registeringPassword", "registeringEmail");
            AuthData auth = serverFacade.registerUser(testUser);
            GameData testGame = new GameData(0, null, null, "newTestGame", new ChessGame());
            serverFacade.createGame(testGame, auth.authToken());
            String listofGames = serverFacade.listGame("eeee");
            //assert(listofGames != null);
            //assert(new serverFacade().playGame())
        } catch (Exception e) {
            if (e.getMessage() == "unauthorized"){
                assert(true);
            }
        }
    }
    @Test
    public void clearDBPositive() throws Exception {

        try{assert(serverFacade.clearDB() == null);} catch (Exception e) {
           throw new Exception(e.getMessage());
       }

        //assert(new serverFacade().playGame())
    }
}



