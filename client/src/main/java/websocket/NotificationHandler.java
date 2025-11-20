package websocket;

import chess.NotificationSetup;
import ui.EscapeSequences;

import static java.awt.Color.RED;
import static java.awt.Event.ESCAPE;

public class NotificationHandler {
    public static final String RED = "\u001B[31m";
        public void notifyAction(NotificationSetup notification){
            System.out.println( RED + String.format(notification.getType()+" "+notification.getString()));
            System.out.print(EscapeSequences.RESET_TEXT_COLOR);
        }

}
