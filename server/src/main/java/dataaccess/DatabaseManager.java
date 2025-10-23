package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;




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
    public ArrayList<String> createStatementGame(ArrayList<String> createStatementsGameReal){
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


    public ArrayList<String> createStatementAuth(ArrayList<String> createStatementsAuthReal){
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
    public ArrayList<String> createStatementUser(ArrayList<String> createStatementsUserReal){
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

    public void createTablesFunction() throws DataAccessException {
        try {
            ArrayList<String> PlaceholderTable = new ArrayList<>();
            ArrayList<String> createdUserTable = createStatementUser(PlaceholderTable);
            ArrayList<String> createdGameTable = createStatementGame(PlaceholderTable);
            ArrayList<String> createdAuthTable = createStatementAuth(PlaceholderTable);


            var conn = DatabaseManager.getConnection();
            for (String statement : createdGameTable) {
                conn.prepareStatement(statement);
            }
            for (String statement : createdUserTable) {
                conn.prepareStatement(statement);
            }
            for (String statement : createdAuthTable) {
                conn.prepareStatement(statement);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create database %s", ex);
        }
    }

    public void insertUserToUserTable(UserData User) throws DataAccessException {
        try {
            var conn = DatabaseManager.getConnection();
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

    public void insertUser(UserData User) throws DataAccessException {
        try {
            var conn = DatabaseManager.getConnection();
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
