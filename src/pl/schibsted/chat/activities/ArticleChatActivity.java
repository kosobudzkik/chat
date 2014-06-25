package pl.schibsted.chat.activities;

import android.app.Activity;
import android.os.Bundle;
import pl.schibsted.chat.AppCommom;
import pl.schibsted.chat.R;
import pl.schibsted.chat.xmpp.ChatClient;
import pl.schibsted.chat.xmpp.ChatCredentials;

/**
 * Article view with chat
 */
public class ArticleChatActivity extends Activity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_article_chat);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppCommom.EventBus.register(this);
        ChatClient ct = new ChatClient();
        ct.connectAsync(new ChatCredentials("fronilse", "test"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            AppCommom.EventBus.unregister(this);
        } catch(Exception ex){}
    }
}
