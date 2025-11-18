package websocket;

public record NotificationSetup(Type type, String message) {

public enum Type{
    UserConnectedAsPlayer,
    UserConnectedAsObserver,
    PlayerMadeMove,
    PlayerLeftGame,
    PlayerResigned,
    PlayerIsInCheck,
    PlayerIsInCheckMate
}
public Type getType(){
    return this.type;
}
public String getString(){
    return this.message;
}
}
