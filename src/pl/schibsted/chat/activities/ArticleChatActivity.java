package pl.schibsted.chat.activities;

import android.app.Activity;
import android.os.Bundle;
import pl.schibsted.chat.R;

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
}
