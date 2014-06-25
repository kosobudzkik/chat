package pl.schibsted.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.squareup.otto.Subscribe;
import pl.schibsted.chat.AppCommom;
import pl.schibsted.chat.R;
import pl.schibsted.chat.activities.ChatMessageView;
import pl.schibsted.chat.components.LatoTextView;
import pl.schibsted.chat.events.IncomingChatMessageEvent;
import pl.schibsted.chat.model.Article;
import pl.schibsted.chat.model.ChatMessage;

import java.util.ArrayList;


public class ChatMessageAdapter extends BaseAdapter {
    private ArrayList<ChatMessage> _messages;
    private Context _context;
    public ChatMessageAdapter(Context context) {
        _messages = new ArrayList<ChatMessage>();
        _context = context;
        AppCommom.EventBus.register(this);

    }

    @Subscribe
    public void onMessageReceived(IncomingChatMessageEvent e) {
        _messages.add(e.ChatMessage);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return _messages.size();
    }

    @Override
    public Object getItem(int i) {
        return _messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return _messages.get(i).Message.hashCode();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        ChatMessageView v = (ChatMessageView) convertView;


        if (convertView == null) {
            v = new ChatMessageView(_context);
     //       LayoutInflater inflater = LayoutInflater.from(_context);
         //   v = inflater.inflate(R.layout.view_chatmessage, parent, false);
        }

        ChatMessage msg = _messages.get(i);
        v.bindData(msg);

        return v;
    }
}
