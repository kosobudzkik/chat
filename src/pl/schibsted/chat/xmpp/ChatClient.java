package pl.schibsted.chat.xmpp;

import android.os.AsyncTask;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;
import pl.schibsted.chat.AppCommom;
import pl.schibsted.chat.errorhandling.ChatAppException;
import pl.schibsted.chat.events.ClientConnectEvent;
import pl.schibsted.chat.events.IncomingChatMessageEvent;

import java.util.Date;

public class ChatClient implements MessageListener {
    public static final String HOST = "matcyburtest.int.vgnett.no";
    public static final int PORT = 5222;
    public static final String SERVICE = "matcyburtest.int.vgnett.no";
    private XMPPConnection _connection;
    private ChatManager _cm;

    public void connectAsync(ChatCredentials credentials) {
        ConnectTask ct = new ConnectTask();
        ct.execute(credentials);
    }

    public void joinArticleChat(String articleId) {
        if (!_connection.isConnected()) throw new ChatAppException("You are not connected to the server");
        Chat chat = _cm.getThreadChat(articleId);
        chat.addMessageListener(this);
    }

    public void sendMessage(String articleId, String message) {
        Chat chat = _cm.getThreadChat(articleId);
        try {
            chat.sendMessage(message);
        } catch (XMPPException e) {
            throw new ChatAppException(e);
        }
    }

    @Override
    public void processMessage(Chat chat, Message message) {
        IncomingChatMessageEvent event = new IncomingChatMessageEvent();
        event.Message = message.getBody();
        event.From = message.getFrom();
        event.ThreadName = chat.getThreadID();
        AppCommom.EventBus.post(event);
    }

    public class ConnectTask extends AsyncTask<ChatCredentials, Void, ClientConnectEvent> {

        @Override
        protected ClientConnectEvent doInBackground(ChatCredentials... chatCredentials) {
            ConnectionConfiguration connConfig = new ConnectionConfiguration(HOST, PORT, SERVICE);
            XMPPConnection connection = new XMPPConnection(connConfig);
            ClientConnectEvent res = null;

            try {
                //Connect to the server
                connection.connect();
                for (int i = 0; i < 10000 && !connection.isConnected(); i += 100) {
                    Thread.sleep(100);
                }
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
