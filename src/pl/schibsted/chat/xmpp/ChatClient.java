package pl.schibsted.chat.xmpp;

import android.os.AsyncTask;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;
import pl.schibsted.chat.AppCommom;
import pl.schibsted.chat.errorhandling.ChatAppException;
import pl.schibsted.chat.events.ClientConnectEvent;
import pl.schibsted.chat.events.IncomingChatMessageEvent;
import pl.schibsted.chat.model.ChatMessage;

import java.util.Date;

public class ChatClient implements MessageListener {
    public static final String HOST = "matcyburtest.int.vgnett.no";
    public static final int PORT = 5222;
    public static final String SERVICE = "";
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
        ChatMessage msg = new ChatMessage();
        msg.Message = message.getBody();
        msg.From = message.getFrom();
        msg.ThreadName = chat.getThreadID();
        IncomingChatMessageEvent event = new IncomingChatMessageEvent(msg);
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
                connection.addConnectionListener(new ConnectionListener() {
                    @Override
                    public void connectionClosed() {

                    }

                    @Override
                    public void connectionClosedOnError(Exception e) {
                        int a = 1;
                    }

                    @Override
                    public void reconnectingIn(int i) {
                        int a = 1;

                    }

                    @Override
                    public void reconnectionSuccessful() {
                        int a = 1;

                    }

                    @Override
                    public void reconnectionFailed(Exception e) {
                        int a = 1;

                    }
                });
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
