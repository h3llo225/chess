package client;

import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import serverFacade.serverFacade;

import java.io.IOException;
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
    public void lestLoginPositive() throws IOException, InterruptedException, DataAccessException {
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
}


