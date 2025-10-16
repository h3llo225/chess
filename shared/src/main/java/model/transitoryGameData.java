package model;

import chess.ChessGame;

public record transitoryGameData(int gameID, String white, String black, String gameName, ChessGame game )  {
}
