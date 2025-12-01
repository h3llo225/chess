package chess;

import model.GameData;

public record NotificationSetup(serverMessageType serverMessageType, String message, String errorMessage, GameData game) {

public enum serverMessageType {
    ERROR,
    LOAD_GAME,
    NOTIFICATION
}
public serverMessageType getType(){
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
