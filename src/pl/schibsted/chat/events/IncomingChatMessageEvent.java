package pl.schibsted.chat.events;

import java.util.Date;

/**
 * Created by fronilse on 25.06.2014.
 */
public class IncomingChatMessageEvent {
    public String ThreadName;
    public String Message;
    public Date Timestamp = new Date();
    public String From;
}
