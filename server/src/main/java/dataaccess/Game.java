package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Game {
    //public record GameData(int gameID, String whiteUsername, String blackUsername, String GameName, ChessGame game )
    public static ArrayList<GameData> listOfGames = new ArrayList<>();
    public void createGame(GameData game){
        Random randomGameID = new Random();
        int newRandomGameID = randomGameID.nextInt();
        while (newRandomGameID < 0){
            newRandomGameID =randomGameID.nextInt();
        }
        GameData newGame = new GameData(newRandomGameID, null,null, game.gameName(), new ChessGame());
        listOfGames.add(newGame);
    }
    public void makeGame(GameData game){

        GameData newGame = new GameData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), new ChessGame());
        listOfGames.add(newGame);
    }


    public GameData findGame(String gameName){
        int count = 0;
        for (GameData game : listOfGames){
            count++;
            if(Objects.equals(game.gameName(), gameName)){
                return game;

            }
        }
        return null;
    }

    public GameData findGameByID(int gameID){

        for (GameData game : listOfGames){

            if(Objects.equals(game.gameID(), gameID)){
                return game;

            }
        }
        return null;
    }

    public void deleteGameByID(int gameID){
        for (GameData game : listOfGames){

            if(Objects.equals(game.gameID(), gameID)){
                listOfGames.remove(findGameByID(gameID));
                break;
            }
        }
    }

    }
