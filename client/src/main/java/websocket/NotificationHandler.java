package websocket;

import chess.NotificationSetup;
import ui.DisplayLogic;
import ui.EscapeSequences;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static java.awt.Color.RED;
import static java.awt.Event.ESCAPE;
import static ui.DisplayLogic.post;
import static ui.DisplayLogic.pre;

public class NotificationHandler {
    public static final String RED = "\u001B[31m";
        public void notifyAction(NotificationSetup notification){
            if (notification.getType() == NotificationSetup.Type.loadGame){
                var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
                out.print(post.makeChessBoard(post.initializeBoardWhiteForCustomGame(DisplayLogic.game.board.getBoard())));
            }else{
                System.out.println( RED + String.format(notification.getType()+" "+notification.getString()));
                System.out.print(EscapeSequences.RESET_TEXT_COLOR);
            }
        }

}



