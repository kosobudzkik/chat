package pl.schibsted.chat.model;

import java.util.Date;

public class ChatMessage {
    public String ThreadName;
    public String Message;
    public Date Timestamp = new Date();
    public String From;
}
