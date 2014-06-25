package pl.schibsted.chat.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import com.squareup.otto.Subscribe;
import pl.schibsted.chat.AppCommom;
import pl.schibsted.chat.R;
import pl.schibsted.chat.adapter.ChatMessageAdapter;
import pl.schibsted.chat.events.ClientConnectEvent;
import pl.schibsted.chat.events.IncomingChatMessageEvent;
import pl.schibsted.chat.model.ChatMessage;
import pl.schibsted.chat.utils.DialogFactory;
import pl.schibsted.chat.utils.SimpleLog;
import pl.schibsted.chat.xmpp.ChatClient;
import pl.schibsted.chat.xmpp.ChatCredentials;

/**
 * Article view with chat
 */
public class ArticleChatActivity extends Activity {
    ChatClient _chatClient;
   // private String _currentRoom = "111";
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

        Intent inent = getIntent();
        String artId = inent.getStringExtra("ArticleId");
        if (artId != null) {
            _currentRoom = artId;
            Log.d("", "Intent received - artcleId: " + artId);
        }
        ((ChatView) findViewById(R.id.chatView)).initAdapter();
    }

    public static Intent createIntent(android.content.Context context, String articleId) {
        Intent i = new Intent(context, ArticleChatActivity.class);
        i.putExtra("ArticleId", articleId);
        return i;
    }

    @Subscribe
    public void onConnected(ClientConnectEvent e) {
        if (e.Success) {
            ((ChatView) findViewById(R.id.chatView)).setReady(true);
            _chatClient.joinArticleChatAsync(_currentRoom);
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
        if (_chatClient == null) {
            _chatClient = new ChatClient();
            _chatClient.connectAsync(this, new ChatCredentials("test1", "test"));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            AppCommom.EventBus.unregister(this);
        } catch(Exception ex){}
    }

    private void sendChatMessage(TextView textView) {
        try {
            String text = textView.getText().toString();
            if(text.length() > 0) {
                _chatClient.sendMessage(text);
                textView.setText("");

                ChatMessage msg = new ChatMessage();
                msg.From = "me";
                msg.Message = text;
                AppCommom.EventBus.post(new IncomingChatMessageEvent(msg));
            }
        } catch(Exception e){
            new DialogFactory().showOkMessage(this, "Error when sending msg: " + e.getMessage());
            Log.e("", e.getMessage(), e);}
    }

}
