package pl.schibsted.chat.events;

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
