package pl.schibsted.chat.activities;


import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import pl.schibsted.chat.R;

public class ChatMessageView extends LinearLayout {
    public ChatMessageView(Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_chatmessage, this, true);
    }
}
