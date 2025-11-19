package chess;

public record NotificationSetup(Type type, String message) {

public enum Type{
    UserConnectedAsPlayerWhite,
    UserConnectedAsPlayerBlack,
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
