package pl.schibsted.chat.activities;


import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import pl.schibsted.chat.R;
import pl.schibsted.chat.components.LatoTextView;
import pl.schibsted.chat.model.ChatMessage;

public class ChatMessageView extends LinearLayout {
    public ChatMessageView(Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_chatmessage, this, true);
    }

    public void bindData(ChatMessage msg) {
        ((LatoTextView) findViewById(R.id.chat_name)).setText(msg.From);
        ((LatoTextView) findViewById(R.id.chat_text)).setText(msg.Message);
    }
}

