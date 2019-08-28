package com.example.messenger.view.adapters;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.messenger.R;
import com.example.messenger.models.DataModel.DataModel;
import com.example.messenger.models.chats.Chat;

import java.util.ArrayList;

public class ChatsListAdapter extends ArrayAdapter<Chat> {
    private Context context;
    private ArrayList<Chat> chatsList;
    private Chat currentChat;

    public ChatsListAdapter(Context context, int resource, ArrayList<Chat> chatsList)
    {
        super(context, resource, chatsList);
        this.context = context;
        this.chatsList = chatsList;
    }

    private View setLayout()
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.chats_list_item, null);
    }

    private void setUnreadMessages(View view)
    {
        TextView textView = view.findViewById(R.id.unreadMessagesTitle);

        if(currentChat.getUnread() > 0)
        {
            textView.setVisibility(View.VISIBLE);
            textView.setText(Html.fromHtml("<font color='#ffffff'>"+currentChat.getUnread()+"</font>"));
        }
        else
            textView.setVisibility(View.GONE);
    }

    private void setLastMessage(View view)
    {
        TextView lastMessage = view.findViewById(R.id.lastMessage);
        TextView lastMessageTime = view.findViewById(R.id.lastMessageTime);
        if(currentChat.getLastMessage() != null) {
            String messageText = "";
            if (currentChat.isGroup()) {
                if (!currentChat.getUsername().equals(DataModel.getUsername())) {
                    //messageText = getContact().getDisplayName + ": ";
                }
            }
            messageText += currentChat.getLastMessage().getText();
            lastMessage.setText(messageText);
            String lastMsgDate = currentChat.getLastMessage().sendingDate.getDateFormat();
            if(currentChat.getUnread() > 0)
                lastMsgDate = "<font color='red'>" + lastMsgDate + "</font>";
            lastMessageTime.setText(Html.fromHtml(lastMsgDate));
        }
        else {
            lastMessage.setText("");
            lastMessageTime.setText("");
        }
    }

    @NonNull
    public View getView(int position, View convertView, ViewGroup parent)
    {
        currentChat = chatsList.get(position);
        convertView = setLayout();

        TextView name = convertView.findViewById(R.id.contactName);
        name.setText(currentChat.getDisplayName());
        setUnreadMessages(convertView);
        setLastMessage(convertView);

        currentChat = null;
        return convertView;
    }
}
