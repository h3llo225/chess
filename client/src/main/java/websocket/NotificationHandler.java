package websocket;

import chess.NotificationSetup;
import ui.DisplayLogic;
import ui.EscapeSequences;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static ui.DisplayLogic.post;

public class NotificationHandler {
    public static final String RED = "\u001B[31m";
        public void notifyAction(NotificationSetup notification){
            if (notification.getType() == NotificationSetup.Type.LOAD_GAME){

                var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
                if (Objects.equals(notification.getGame().blackUsername(), DisplayLogic.username)){
                    System.out.println(notification.getString());
                    out.print(post.makeChessBoard(post.initializeBoardBlackForCustomGame(notification.getGame().game().getBoard().getBoard())));
                }
                else if (Objects.equals(notification.getGame().whiteUsername(), DisplayLogic.username)){
                    System.out.println(notification.getString());
                    out.print(post.makeChessBoard(post.initializeBoardWhiteForCustomGame(notification.getGame().game().getBoard().getBoard())));
                }
                else{
                    System.out.println(notification.getString());
                    out.print(post.makeChessBoard(post.initializeBoardWhiteForCustomGame(notification.getGame().game().getBoard().getBoard())));
                }
                //out.print(post.makeChessBoard(post.initializeBoardWhiteForCustomGame(notification.getGame().getBoard().getBoard())));
                DisplayLogic.game = notification.getGame().game();
            }else{
                System.out.println( RED + String.format(notification.getType()+" "+notification.getString()));
                System.out.print(EscapeSequences.RESET_TEXT_COLOR);
            }
        }

}



