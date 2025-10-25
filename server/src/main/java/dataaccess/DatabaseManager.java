package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.ListGamesData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.*;


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
    public static String createStatementGame(){
        return """
                CREATE TABLE IF NOT EXISTS  Game( gameID int,
                 whiteUsername varchar(256),
                blackUsername varchar(256),
                gameName varchar(256),
                game longtext NOT NULL)""";
    }


    public static String createStatementAuth(){
        return """
                CREATE TABLE IF NOT EXISTS  Auth(username varchar(256) NOT NULL,
                authToken varchar(256) NOT NULL,
                FOREIGN KEY (username) REFERENCES User(username) )""";
    }
    public static String createStatementUser(){
        return """
        CREATE TABLE IF NOT EXISTS  User( username varchar(256) NOT NULL,
        password varchar(256) NOT NULL,
        email varchar(256) NOT NULL,
        PRIMARY KEY (username) )""";

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
            Statement statement = conn.createStatement();
            String authTables = createStatementAuth();
            PreparedStatement statement1 = conn.prepareStatement(authTables);
            statement.executeUpdate(authTables);
            String gameTables = createStatementGame();
            PreparedStatement statement2 = conn.prepareStatement(gameTables);
            statement.executeUpdate(gameTables);
            String userTables = createStatementUser();
            PreparedStatement statement3 = conn.prepareStatement(userTables);
            statement.executeUpdate(gameTables);
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    public void insertUserToUserTable(UserData User) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            String newPassword = storeUserPassword(User.password());
            String statementToBeExecuted = "INSERT INTO User(username, password, email) VALUES (?, ?, ?)";
            PreparedStatement newStatement = conn.prepareStatement(statementToBeExecuted);
            newStatement.setString(1,User.username());
            newStatement.setString(2,newPassword);
            newStatement.setString(3,User.email());
            newStatement.executeUpdate();
            String sql = "SELECT * FROM User";
            PreparedStatement sqlExecution = conn.prepareStatement(sql);
            ResultSet results = sqlExecution.executeQuery();
            ResultSet cookies = results;
        } catch (SQLException ex){
            throw new DataAccessException(ex.getMessage());
        }
    }

    public void insertAuth(AuthData Auth) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();){
            String statementToBeExecuted = "INSERT INTO Auth (username, authToken) VALUES (?,?)";
            PreparedStatement newStatement = conn.prepareStatement(statementToBeExecuted);
            newStatement.setString(1,Auth.username());
            newStatement.setString(2,Auth.authToken());
            newStatement.executeUpdate();
        } catch (SQLException ex){
            throw new DataAccessException(ex.getMessage());
        }
    }


    public void createGame(GameData Game) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            String statementToBeExecuted = "INSERT into Game(gameID,whiteUsername,blackUsername,gameName,game) VALUES (?, ?, ?, ?, ?)";
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
            throw new DataAccessException(ex.getMessage());
        }
    }

    public void makeGame(GameData Game) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();){

            String statementToBeExecuted = "INSERT into Game(gameID,whiteUsername,blackUsername,gameName,game) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement newStatement = conn.prepareStatement(statementToBeExecuted);
            newStatement.setInt(1,Game.gameID());
            newStatement.setString(2,Game.whiteUsername());
            newStatement.setString(3,Game.blackUsername());
            newStatement.setString(4,Game.gameName());
            String json = serializeGame(Game.game());
            newStatement.setString(5, json);
            newStatement.executeUpdate();
        } catch (SQLException ex){
            throw new DataAccessException(ex.getMessage());
        }
    }

    public GameData findGame(String gameName) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            PreparedStatement statementToBeExecuted = conn.prepareStatement("SELECT * FROM Game ");
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
            throw new DataAccessException(ex.getMessage());
        }
        return null;
    }

    public GameData findGameByID(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            PreparedStatement statementToBeExecuted = conn.prepareStatement("SELECT * FROM Game ");
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
            throw new DataAccessException(ex.getMessage());
        }
        return null;
    }


    public void deleteGameByID(int gameID) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()) {
            PreparedStatement statementToBeExecuted = conn.prepareStatement("DELETE FROM Game WHERE gameID = ? ");
            GameData gameToBeDeleted = findGameByID(gameID);
            statementToBeExecuted.setInt(1,gameToBeDeleted.gameID());
            statementToBeExecuted.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }



    public String listGames() throws DataAccessException {
        ArrayList<GameData> PlaceholderList = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection();) {
            PreparedStatement statementToBeExecuted = conn.prepareStatement("SELECT * FROM Game");
            ResultSet result = statementToBeExecuted.executeQuery();
            String listGamesResult = "{}";
            while (result.next()) {
                int gameID = result.getInt("gameID");
                String whiteUsername = result.getString("whiteUsername");
                String blackUsername = result.getString("blackUsername");
                String gameName = result.getString("gameName");
                String game = result.getString("game");
                ChessGame resultGame = new Gson().fromJson(game, ChessGame.class);
                PlaceholderList.add(new GameData(gameID, whiteUsername, blackUsername, gameName, resultGame));

            }
            Gson gson = new Gson();
            listGamesResult = gson.toJson(Map.of("games", PlaceholderList));

            return listGamesResult;
        } catch (SQLException ex){
            throw new DataAccessException(ex.getMessage());
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
            throw new DataAccessException(ex.getMessage());
        }
return false;
    }



    public String logoutAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            PreparedStatement statementToBeExecuted = conn.prepareStatement("DELETE FROM Auth WHERE authToken = ? ");
            statementToBeExecuted.setString(1,authToken);
            statementToBeExecuted.executeUpdate();
            return "{}";
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
        //throw new DataAccessException("{\"message\": \"Error: bad request\"}");
    }
    public String findUser(String authToken) throws DataAccessException {
        ArrayList<ListGamesData> PlaceholderList = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection()){
            PreparedStatement statementToBeExecuted = conn.prepareStatement("SELECT username, authToken FROM Auth ");
            ResultSet result = statementToBeExecuted.executeQuery();
            while(result.next()){
                if (Objects.equals(result.getString("authToken"), authToken)) {
                    return result.getString("username");
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
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
            throw new DataAccessException(ex.getMessage());
        }
        return true;
    }

    public boolean checkLogin(String username, String password) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            PreparedStatement statementToBeExecuted = conn.prepareStatement("SELECT username, password FROM User WHERE username = ? ");
            statementToBeExecuted.setString(1,username);

            String sql = "SELECT * FROM User";
            ResultSet result = statementToBeExecuted.executeQuery();
            return(result.next() && BCrypt.checkpw(password, result.getString("password")));
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }


    public void clearDB() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            Statement statementToBeExecutedGame = conn.createStatement();
            statementToBeExecutedGame.executeUpdate("DELETE FROM Game");
            Statement statementToBeExecutedAuth = conn.createStatement();
            statementToBeExecutedAuth.executeUpdate("DELETE FROM Auth");
            Statement statementToBeExecutedUser = conn.createStatement();
            statementToBeExecutedUser.executeUpdate("DELETE FROM User");
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
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
