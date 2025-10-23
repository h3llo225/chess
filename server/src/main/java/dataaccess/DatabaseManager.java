package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.ListGamesData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;

import static dataaccess.Auth.listOfAuth;


public class DatabaseManager {
    private static String databaseName;
    private static String dbUsername;
    private static String dbPassword;
    private static String connectionUrl;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        loadPropertiesFromResources();
    }

    /**
     * Creates the database if it does not already exist.
     */
    static public void createDatabase() throws DataAccessException {
        var statement = "CREATE DATABASE IF NOT EXISTS " + databaseName;
        try (var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
             var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create database", ex);
        }
    }
    public static ArrayList<String> createStatementGame(ArrayList<String> createStatementsGameReal){
        String statement1 = "CREATE TABLE IF NOT EXISTS  Game ";
        String statement2 = "ALTER TABLE Game ADD 'gameID' int";
        String statement3 = "ALTER TABLE Game ADD 'whiteUsername' varchar(256)";
        String statement4 = "ALTER TABLE Game ADD 'blackUsername' varchar(256)";
        String statement5 = "ALTER TABLE Game ADD 'gameName' varchar(256)";
        String statement6 = "ALTER TABLE Game ADD 'game' longtext NOT NULL";
        createStatementsGameReal.add(statement1);
        createStatementsGameReal.add(statement2);
        createStatementsGameReal.add(statement3);
        createStatementsGameReal.add(statement4);
        createStatementsGameReal.add(statement5);
        createStatementsGameReal.add(statement6);
        return createStatementsGameReal;
    }


    public static ArrayList<String> createStatementAuth(ArrayList<String> createStatementsAuthReal){
        String statement1 = "CREATE TABLE IF NOT EXISTS  Auth ";
        String statement2 = "ALTER TABLE Auth ADD 'username' varchar(256) NOT NULL";
        String statement3 = "ALTER TABLE Auth ADD 'authToken' varchar(256) NOT NULL";
        String statement4 = "ALTER TABLE Auth ADD FOREIGN KEY ('username') REFERENCES Users('username');";
        createStatementsAuthReal.add(statement1);
        createStatementsAuthReal.add(statement2);
        createStatementsAuthReal.add(statement3);
        createStatementsAuthReal.add(statement4);
        return createStatementsAuthReal;
    }
    public static ArrayList<String> createStatementUser(ArrayList<String> createStatementsUserReal){
        String statement1 = "CREATE TABLE IF NOT EXISTS  User ";
        String statement2 = "ALTER TABLE User ADD 'username' varchar(256) NOT NULL";
        String statement3 = "ALTER TABLE User ADD 'password' varchar(256) NOT NULL";
        String statement4 = "ALTER TABLE User ADD 'email' varchar(256) NOT NULL";
        String statement5 = "ALTER TABLE User ADD PRIMARY KEY ('username');";
        createStatementsUserReal.add(statement1);
        createStatementsUserReal.add(statement2);
        createStatementsUserReal.add(statement3);
        createStatementsUserReal.add(statement4);
        createStatementsUserReal.add(statement5);

        return createStatementsUserReal;
    }

public String serializeGame(ChessGame game){
    String json = new Gson().toJson(game);
    return json;
}
    public String storeUserPassword(String clearTextPassword) {
        return BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());
    }

    public static void createTablesFunction() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();){
            ArrayList<String> PlaceholderTable = new ArrayList<>();
            ArrayList<String> createdUserTable = createStatementUser(PlaceholderTable);
            ArrayList<String> createdGameTable = createStatementGame(PlaceholderTable);
            ArrayList<String> createdAuthTable = createStatementAuth(PlaceholderTable);
            try (Statement newStatement = conn.createStatement()) {
            for (String statement : createdGameTable) {
                //PreparedStatement statement1 = conn.prepareStatement(statement);
                newStatement.executeUpdate(statement);

            }
            for (String statement : createdUserTable) {
                //conn.prepareStatement(statement);
                //PreparedStatement statement2 = conn.prepareStatement(statement);
                newStatement.executeUpdate(statement);
            }
            for (String statement : createdAuthTable) {
                //conn.prepareStatement(statement);
                //PreparedStatement statement3 = conn.prepareStatement(statement);
                newStatement.executeUpdate(statement);
            }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create database %s", ex);
        }
    }

    public void insertUserToUserTable(UserData User) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            String newPassword = storeUserPassword(User.password());
            String statementToBeExecuted = "INSERT into Users (?, ?, ?)";
            PreparedStatement newStatement = conn.prepareStatement(statementToBeExecuted);
            newStatement.setString(1,User.username());
            newStatement.setString(2,newPassword);
            newStatement.setString(3,User.email());
            newStatement.executeUpdate();
        } catch (SQLException ex){
            throw new DataAccessException("failed to create database %s", ex);
        }
    }

    public void insertAuth(AuthData Auth) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();){
            String statementToBeExecuted = "INSERT into Auth (?, ?)";
            PreparedStatement newStatement = conn.prepareStatement(statementToBeExecuted);
            newStatement.setString(1,Auth.username());
            newStatement.setString(2,Auth.authToken());
            newStatement.executeUpdate();
        } catch (SQLException ex){
            throw new DataAccessException("failed to create database %s", ex);
        }
    }


    public void createGame(GameData Game) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            String statementToBeExecuted = "INSERT into Game (?, ?, ?, ?, ?)";
            PreparedStatement newStatement = conn.prepareStatement(statementToBeExecuted);
            Random randomGameID = new Random();
            int newRandomGameID = randomGameID.nextInt();
            while (newRandomGameID < 0){
                newRandomGameID =randomGameID.nextInt();
            }
            newStatement.setInt(1,newRandomGameID);
            newStatement.setString(2,null);
            newStatement.setString(3,null);
            newStatement.setString(4,Game.gameName());
            String json = serializeGame(new ChessGame());
            newStatement.setString(5, json);
            newStatement.executeUpdate();
        } catch (SQLException ex){
            throw new DataAccessException("failed to create database %s", ex);
        }
    }

    public void makeGame(GameData Game) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();){

            String statementToBeExecuted = "INSERT into Game (?, ?, ?, ?, ?)";
            PreparedStatement newStatement = conn.prepareStatement(statementToBeExecuted);
            newStatement.setInt(1,Game.gameID());
            newStatement.setString(2,Game.whiteUsername());
            newStatement.setString(3,Game.whiteUsername());
            newStatement.setString(4,Game.gameName());
            String json = serializeGame(Game.game());
            newStatement.setString(5, json);
            newStatement.executeUpdate();
        } catch (SQLException ex){
            throw new DataAccessException("failed to create database %s", ex);
        }
    }

    public GameData findGame(String gameName) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            PreparedStatement statementToBeExecuted = conn.prepareStatement("SELECT gameName FROM Game ");
            ResultSet result = statementToBeExecuted.executeQuery();
            while(result.next()){
                if (Objects.equals(result.getString("gameName"), gameName)) {
                    ChessGame game = new Gson().fromJson(result.getString("game"), ChessGame.class);

                    return new GameData(result.getInt("gameID"),result.getString("whiteUsername"),
                            result.getString("blackUsername"), result.getString("gameName"),
                            game);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create database %s", ex);
        }
        return null;
    }

    public GameData findGameByID(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            PreparedStatement statementToBeExecuted = conn.prepareStatement("SELECT gameName FROM Game ");
            ResultSet result = statementToBeExecuted.executeQuery();
            while(result.next()){
                if (Objects.equals(result.getInt("gameID"), gameID)) {
                    ChessGame game = new Gson().fromJson(result.getString("game"), ChessGame.class);

                    return new GameData(result.getInt("gameID"),result.getString("whiteUsername"),
                            result.getString("blackUsername"), result.getString("gameName"),
                            game);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create database %s", ex);
        }
        return null;
    }


    public void DeleteGameByID(int gameID) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()) {
            PreparedStatement statementToBeExecuted = conn.prepareStatement("DELETE FROM Game WHERE 'gameID = ?' ");
            GameData gameToBeDeleted = findGameByID(gameID);
            statementToBeExecuted.setInt(1,gameToBeDeleted.gameID());
            statementToBeExecuted.executeQuery();
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create database %s", ex);
        }
    }



    public ArrayList<ListGamesData> listGames() throws DataAccessException {
        ArrayList<ListGamesData> PlaceholderList = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection();){
            PreparedStatement statementToBeExecuted = conn.prepareStatement("SELECT gameID, whiteUsername, blackUsername, gameName, game FROM Game");
            ResultSet result = statementToBeExecuted.executeQuery();
            while (result.next()){
                int gameID = result.getInt("gameID");
                String whiteUsername = result.getString("whiteUsername");
                String blackUsername = result.getString("blackUsername");
                String gameName = result.getString("gameName");
                String game = result.getString("game");
                game = new Gson().fromJson(game, String.class);
                PlaceholderList.add(new ListGamesData(gameID, whiteUsername, blackUsername, gameName, game));
            }

            return PlaceholderList;
        } catch (SQLException ex){
            throw new DataAccessException("failed to create database %s", ex);
        }
    }

    public boolean findAuth(String authToken) throws DataAccessException {
        ArrayList<ListGamesData> PlaceholderList = new ArrayList<>();
        try(var conn = DatabaseManager.getConnection()) {
            PreparedStatement statementToBeExecuted = conn.prepareStatement("SELECT authToken FROM Auth ");
            ResultSet result = statementToBeExecuted.executeQuery();
            while(result.next()){
                if (Objects.equals(result.getString("authToken"), authToken)) {
                    return true;
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create database %s", ex);
        }
return false;
    }



    public String logoutAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            PreparedStatement statementToBeExecuted = conn.prepareStatement("DELETE FROM Auth WHERE 'authToken = ?' ");
            statementToBeExecuted.setString(1,authToken);
            ResultSet result = statementToBeExecuted.executeQuery();
            return "{}";
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create database %s", ex);
        }
        //throw new DataAccessException("{\"message\": \"Error: bad request\"}");
    }
    public String findUser(String authToken) throws DataAccessException {
        ArrayList<ListGamesData> PlaceholderList = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection()){
            PreparedStatement statementToBeExecuted = conn.prepareStatement("SELECT authToken FROM Auth ");
            ResultSet result = statementToBeExecuted.executeQuery();
            while(result.next()){
                if (Objects.equals(result.getString("authToken"), authToken)) {
                    return result.getString("username");
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create database %s", ex);
        }
return null;
    }

    public boolean getUser(String username) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()) {
            PreparedStatement statementToBeExecuted = conn.prepareStatement("SELECT username FROM User ");
            ResultSet result = statementToBeExecuted.executeQuery();
            while(result.next()){
                if (Objects.equals(result.getString("username"), username)) {
                    return false;
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create database %s", ex);
        }
        return true;
    }

    public boolean checkLogin(String username, String password) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            PreparedStatement statementToBeExecuted = conn.prepareStatement("SELECT username FROM User ");
            ResultSet result = statementToBeExecuted.executeQuery();
            while(result.next()){
                if (Objects.equals(result.getString("username"), username)) {
                    if (Objects.equals(result.getString("password"), password)) {
                        return true;
                    }


                    }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create database %s", ex);
        }
        return false;
    }


    public String clearDB() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            PreparedStatement statementToBeExecutedUser = conn.prepareStatement("TRUNCATE TABLE User ");
            statementToBeExecutedUser.executeQuery();
            PreparedStatement statementToBeExecutedAuth = conn.prepareStatement("TRUNCATE TABLE Auth ");
            statementToBeExecutedAuth.executeQuery();
            PreparedStatement statementToBeExecutedGame = conn.prepareStatement("TRUNCATE TABLE Game ");
            statementToBeExecutedGame.executeQuery();
            return "{}";
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create database %s", ex);
        }
    }




    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DatabaseManager.getConnection()) {
     * // execute SQL statements.
     * }
     * </code>
     */
    static Connection getConnection() throws DataAccessException {
        try {
            //do not wrap the following line with a try-with-resources
            var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
            conn.setCatalog(databaseName);
            return conn;
        } catch (SQLException ex) {
            throw new DataAccessException("failed to get connection", ex);
        }
    }

    private static void loadPropertiesFromResources() {
        try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
            if (propStream == null) {
                throw new Exception("Unable to load db.properties");
            }
            Properties props = new Properties();
            props.load(propStream);
            loadProperties(props);
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties", ex);
        }
    }

    private static void loadProperties(Properties props) {
        databaseName = props.getProperty("db.name");
        dbUsername = props.getProperty("db.user");
        dbPassword = props.getProperty("db.password");

        var host = props.getProperty("db.host");
        var port = Integer.parseInt(props.getProperty("db.port"));
        connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);
    }
}
