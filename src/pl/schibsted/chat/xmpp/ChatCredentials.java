package pl.schibsted.chat.xmpp;

/**
 * Credentials for chat server
 */
public class ChatCredentials {
    public String Password;
    public String Username;

    public ChatCredentials(String username, String pwd) {
        Username = username;
        Password = pwd;
    }
}
