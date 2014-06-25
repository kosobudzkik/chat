package pl.schibsted.chat.xmpp;

import android.os.AsyncTask;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import pl.schibsted.chat.AppCommom;
import pl.schibsted.chat.events.ClientConnectEvent;

public class ChatClient {
    public static final String HOST = "matcyburtest.int.vgnett.no";
    public static final int PORT = 5222;
    public static final String SERVICE = "gmail.com";
    private XMPPConnection _connection;

    public void connectAsync(ChatCredentials credentials) {
        ConnectTask ct = new ConnectTask();
        ct.execute(credentials);
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
                connection.login(chatCredentials[0].Username, chatCredentials[0].Password);
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
