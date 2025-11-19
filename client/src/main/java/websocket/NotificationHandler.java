package websocket;

import chess.NotificationSetup;

import static java.awt.Color.RED;

public class NotificationHandler {
        public void notifyAction(NotificationSetup notification){
            System.out.println( RED + String.format(notification.getType()+notification.getString()));
        }

}
