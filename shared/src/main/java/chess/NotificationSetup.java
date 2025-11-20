package chess;

public record NotificationSetup(Type type, String message) {

public enum Type{
    loadGame,
    error,
    notification
}
public Type getType(){
    return this.type;
}
public String getString(){
    return this.message;
}
}
