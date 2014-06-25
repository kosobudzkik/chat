package pl.schibsted.chat.xmpp;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.RoomInfo;
import pl.schibsted.chat.AppCommom;
import pl.schibsted.chat.errorhandling.ChatAppException;
import pl.schibsted.chat.events.ClientConnectEvent;
import pl.schibsted.chat.events.IncomingChatMessageEvent;
import pl.schibsted.chat.model.ChatMessage;

import java.util.Collection;
import java.util.Date;

public class ChatClient implements MessageListener {
    public static final String HOST = "matcybur.vgnett.no";
    public static final int PORT = 5222;
    public static final String SERVICE = "matcybur.vgnett.no";
    private XMPPConnection _connection;
    private ChatManager _cm;
    private ChatCredentials _joinedAs;
    private MultiUserChat _chat;
    private Context context;
    private Activity _activity;

    public void connectAsync(Activity activity, ChatCredentials credentials) {
        _activity = activity;
        ConnectTask ct = new ConnectTask();
        ct.execute(credentials);
        _joinedAs = credentials;


    }
    public void joinArticleChatAsync(String currentRoom) {
        new AsyncJoinTask().doInBackground(currentRoom);
    }

    class AsyncJoinTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            joinArticleChat(strings[0]);
            return null;
        }
    }

    public void joinArticleChat(String articleId) {
        _chat = new MultiUserChat(_connection, articleId + "@conference." + HOST);
        try {
            //RoomInfo info = MultiUserChat.getRoomInfo(_connection, articleId + "@conference. " + HOST);

            // the amount of history to receive. In this example we are requesting the last 5 messages.
            DiscussionHistory history = new DiscussionHistory();
            history.setMaxStanzas(10);
            _chat.join(_joinedAs.Username, "", history, SmackConfiguration.getPacketReplyTimeout());
           _chat.addMessageListener(new PacketListener() {
               @Override
               public void processPacket(final Packet packet) {
                   if (packet instanceof Message) {
                       _activity.runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               processMessage((Message) packet);

                           }
                       });
                   }
               }
           });
        } catch (Exception e) {
            throw new ChatAppException(e);
        }
    }

    public void sendMessage(String message) {
        try {
            _chat.sendMessage(message);
        } catch (XMPPException e) {
            throw new ChatAppException(e);
        }
    }

    @Override
    public void processMessage(Chat chat, Message message) {
        ChatMessage msg = new ChatMessage();
        msg.Message = message.getBody();
        msg.From = message.getFrom();
        msg.ThreadName = chat.getThreadID();
        IncomingChatMessageEvent event = new IncomingChatMessageEvent(msg);
        AppCommom.EventBus.post(event);
    }

    public void processMessage(Message message) {
        ChatMessage msg = new ChatMessage();
        msg.Message = message.getBody();
        msg.From = message.getFrom();
        msg.ThreadName = "";
        IncomingChatMessageEvent event = new IncomingChatMessageEvent(msg);
        AppCommom.EventBus.post(event);
    }


    public class ConnectTask extends AsyncTask<ChatCredentials, Void, ClientConnectEvent> {

        @Override
        protected ClientConnectEvent doInBackground(ChatCredentials... chatCredentials) {
            ConnectionConfiguration connConfig = new ConnectionConfiguration(HOST, PORT, SERVICE);
            XMPPConnection connection = new XMPPConnection(connConfig);
            _connection = connection;
            ClientConnectEvent res = null;

            try {
                //Connect to the server
                connection.connect();
                for (int i = 0; i < 10000 && !connection.isConnected(); i += 100) {
                    Thread.sleep(100);
                }
                String s= connection.getServiceName();
                connection.login(chatCredentials[0].Username, chatCredentials[0].Password);
                _cm = _connection.getChatManager();

                res = new ClientConnectEvent();
            } catch (Exception ex) {
                res = new ClientConnectEvent(ex);
            }
            _connection = connection;
            return res;
        }

        @Override
        protected void onPostExecute(ClientConnectEvent res) {
            AppCommom.EventBus.post(res);
        }
    }
}
