package chess;

import model.GameData;

public record NotificationSetup(ServerMessageType serverMessageType, String message, String errorMessage, GameData game) {

public enum ServerMessageType {
    ERROR,
    LOAD_GAME,
    NOTIFICATION
}
public ServerMessageType getType(){
    return this.serverMessageType;
}
public String getString(){
    return this.message;
}
public GameData getGame(){
        return this.game;
    }
    public String getError(){
        return this.errorMessage;
    }
}
