package websocket;

import websocket.messages.ServerMessage;

import static java.awt.Color.RED;

public class NotificationHandler {
        public void notifyAction(NotificationSetup notification){
            System.out.println( RED + String.format(notification.getType()+notification.getString()));
        }

}
