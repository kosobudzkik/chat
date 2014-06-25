package pl.schibsted.chat.events;

import pl.schibsted.chat.model.ChatMessage;

import java.util.Date;

/**
 * Created by fronilse on 25.06.2014.
 */
public class IncomingChatMessageEvent {
    public pl.schibsted.chat.model.ChatMessage ChatMessage;

    public IncomingChatMessageEvent(ChatMessage msg) {
        ChatMessage = msg;
    }
}
