package pl.schibsted.chat.activities;


import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import pl.schibsted.chat.AppCommom;
import pl.schibsted.chat.R;
import pl.schibsted.chat.adapter.ChatMessageAdapter;
import pl.schibsted.chat.events.IncomingChatMessageEvent;
import pl.schibsted.chat.model.ChatMessage;

public class ChatView extends LinearLayout {
    public ChatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_chat, this, true);
        setReady(false);
    }

    public void setOnEditorActionListener(TextView.OnEditorActionListener al) {
        EditText et = (EditText) findViewById(R.id.chatInput);
        et.setOnEditorActionListener(al);
    }

    public void setReady(boolean ready) {
        if (ready) {
            ((EditText) findViewById(R.id.chatInput)).setEnabled(ready);
        }
    }

    public void initAdapter() {
        Context context = getContext();
        ChatMessageAdapter adapter = new ChatMessageAdapter(context);

        ((ListView) findViewById(R.id.chatview_messagelist)).setAdapter(adapter);

        ChatMessage m1 = new ChatMessage();
        m1.From = "Jim";
        m1.Message ="Hi";
        ChatMessage m2 = new ChatMessage();
        m2.From = "Joe";
        m2.Message = "Hello";

        AppCommom.EventBus.post(new IncomingChatMessageEvent(m1));
        AppCommom.EventBus.post(new IncomingChatMessageEvent(m2));


    }
}
