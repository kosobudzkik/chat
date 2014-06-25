package pl.schibsted.chat.xmpp;

import android.os.AsyncTask;
import android.os.Handler;
import org.apache.harmony.javax.security.sasl.SaslException;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Packet;
import pl.schibsted.chat.errorhandling.ChatAppException;

import java.io.IOException;

public class ChatClient {
    public static final String HOST = "matcyburtest.int.vgnett.no";
    public static final int PORT = 5222;
    public static final String SERVICE = "gmail.com";
    private XMPPConnection _connection;

    public void connectAsync(ChatCredentials credentials) {
        ConnectTask ct = new ConnectTask();
        ct.execute(credentials);
    }

    public class ConnectTask extends AsyncTask<ChatCredentials, Void, XMPPConnection> {

        @Override
        protected XMPPConnection doInBackground(ChatCredentials... chatCredentials) {
            ConnectionConfiguration connConfig = new ConnectionConfiguration(HOST, PORT, SERVICE);
            XMPPConnection connection = new XMPPConnection(connConfig);

            try {
                //Connect to the server
                connection.connect();
                connection.login(chatCredentials[0].Username, chatCredentials[0].Password);
            } catch (XMPPException ex) {
                throw new ChatAppException(ex);
            } catch (Exception ex) {
                throw new ChatAppException(ex);
            }
            _connection = connection;
            return connection;
        }

        @Override
        protected void onPostExecute(XMPPConnection done) {
            
        }
    }
}
