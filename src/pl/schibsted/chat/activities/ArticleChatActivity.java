package pl.schibsted.chat.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import com.squareup.otto.Subscribe;
import pl.schibsted.chat.AppCommom;
import pl.schibsted.chat.R;
import pl.schibsted.chat.adapter.ChatMessageAdapter;
import pl.schibsted.chat.events.ClientConnectEvent;
import pl.schibsted.chat.utils.DialogFactory;
import pl.schibsted.chat.utils.SimpleLog;
import pl.schibsted.chat.xmpp.ChatClient;
import pl.schibsted.chat.xmpp.ChatCredentials;

/**
 * Article view with chat
 */
public class ArticleChatActivity extends Activity {
    ChatClient _chatClient;
    private String _currentRoom = "23239282";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_article_chat);
        ((ChatView) findViewById(R.id.chatView)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                sendChatMessage(textView);
                return false;
            }
        });

        ((ChatView) findViewById(R.id.chatView)).initAdapter();
    }

    @Subscribe
    public void onConnected(ClientConnectEvent e) {
        if (e.Success) {
            ((ChatView) findViewById(R.id.chatView)).setReady(true);
        }
        else {
            new DialogFactory().showOkMessage(this, "Error with chat service: " + e.Error.getMessage());
            Log.e("", e.Error.getMessage(), e.Error);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppCommom.EventBus.register(this);
        _chatClient = new ChatClient();
        _chatClient.connectAsync(new ChatCredentials("fronilse@matcyburtest.int.vgnett.no", "test"));
        debug();
    }

    private void debug() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            AppCommom.EventBus.unregister(this);
        } catch(Exception ex){}
    }

    private void sendChatMessage(TextView textView) {
        String text = textView.getText().toString();
        if(text.length() > 0) {
            _chatClient.sendMessage(_currentRoom, text);
            textView.setText("");
        }
    }

}
