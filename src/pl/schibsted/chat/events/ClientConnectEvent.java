package pl.schibsted.chat.events;

import com.sun.deploy.util.SessionState;

/**
 * When the chat client connects to server
 */
public class ClientConnectEvent {
    public boolean Success;
    public Exception Error;

    public ClientConnectEvent(Exception ex) {
        Success = false;
        Error =ex;
    }

    /** Use for successful connection */
    public ClientConnectEvent() {
        Success = true;
    }
}
