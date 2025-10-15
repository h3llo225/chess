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
        GameData newGame = new GameData(newRandomGameID, null,null, game.gameName(), new ChessGame());
        listOfGames.add(newGame);
    }


    public int findGame(String gameName){
        int count = 0;
        for (GameData game : listOfGames){
            count++;
            if(Objects.equals(game.gameName(), gameName)){
                return game.gameID();

            }
        }
        return -1;
    }

    }
