package com.example.messenger.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.messenger.R;
import com.example.messenger.models.Contacts.ContactsList;
import com.example.messenger.models.DataModel.DataModel;
import com.example.messenger.models.messages.MediaMessage;
import com.example.messenger.models.messages.Message;
import com.example.messenger.models.messages.MessageType;

import java.util.ArrayList;

public class MessagesArrayAdapter extends ArrayAdapter<Message> {
    private ArrayList<Message> messagesList;
    private Context context;
    private Message currentMessage;

    public MessagesArrayAdapter(Context context, int resource, ArrayList<Message> messagesList)
    {
        super(context, resource, messagesList);
        this.messagesList = messagesList;
        this.context = context;
    }

    @Nullable
    private View setLayout(MessageType type)
    {
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (type == MessageType.TEXT) {
            if (currentMessage.sender.equals(DataModel.getUsername()))
                return inflater.inflate(R.layout.sent_text_message_list_item, null);
            else
                return inflater.inflate(R.layout.income_text_message_list_item, null);
        }
        else if (type == MessageType.IMAGE) {
            if (currentMessage.sender.equals(DataModel.getUsername())) {
                return inflater.inflate(R.layout.sent_image_message_list_item, null);
            }
            else
                return inflater.inflate(R.layout.income_image_message_list_item, null);
        }
        else
            return null;
    }

    private void setMessage(View view)
    {
        TextView text = view.findViewById(R.id.messageTextView);
        TextView date = view.findViewById(R.id.dateTextView);
        String messageText = "";
        if(currentMessage.fromGroup && !currentMessage.sender.equals(DataModel.getUsername()))
        {
            String name = DataModel.getModel().getProfile().getContactsList().getContact(
                    currentMessage.sender).getDisplayName();
            messageText = ("<small>" + name + "</small><br>");
        }
        messageText += currentMessage.messageText();
        text.setText(Html.fromHtml(messageText));
        date.setText(currentMessage.sendingDate.getDateFormat());

        if(currentMessage.getType() != MessageType.TEXT)
        {
            MediaMessage msg = (MediaMessage)currentMessage.getBody();
            Bitmap bmImg = BitmapFactory.decodeFile(msg.getFullPath());
            ImageView imageView = view.findViewById(R.id.messageImage);
            imageView.setImageBitmap(bmImg);
        }

    }

    private void setStatus(View view)
    {
        ImageView status = view.findViewById(R.id.statusImageView);
        switch (currentMessage.status)
        {
            case PENDING:
                status.setVisibility(View.INVISIBLE);
            case SENT:
                status.setImageResource(R.drawable.baseline_done_black_18dp);
                break;
            case RECEIVED:
                status.setImageResource(R.drawable.baseline_done_all_black_18dp);
                break;
            case READ:
                //status = "<font color='blue'>VV</font>";
                break;
        }
    }

    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
        currentMessage = messagesList.get(position);
        convertView = setLayout(currentMessage.getType());

        if (convertView != null) {
            setMessage(convertView);
            if (currentMessage.sender.equals(DataModel.getUsername()))
                setStatus(convertView);

            currentMessage = null;
        }
        return convertView;
    }
}
