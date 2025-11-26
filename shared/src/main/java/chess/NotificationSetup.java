package chess;

import model.GameData;

public record NotificationSetup(Type type, String message, GameData game) {

public enum Type{
    error,
    LOAD_GAME,
    notification
}
public Type getType(){
    return this.type;
}
public String getString(){
    return this.message;
}
public GameData getGame(){
        return this.game;
    }
}
