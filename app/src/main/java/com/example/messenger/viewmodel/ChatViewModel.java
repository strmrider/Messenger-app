package com.example.messenger.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Looper;
import android.provider.MediaStore;

import com.example.messenger.interfaces.ChatViewModelListener;
import com.example.messenger.models.Contacts.Contact;
import com.example.messenger.models.DataModel.DataModel;
import com.example.messenger.models.chats.Chat;
import com.example.messenger.models.chats.GroupChat;
import com.example.messenger.models.messages.MediaMessage;
import com.example.messenger.models.messages.Message;
import com.example.messenger.models.messages.MessageBody;
import com.example.messenger.models.messages.MessageDate;
import com.example.messenger.models.messages.MessageStatusUpdate;
import com.example.messenger.models.messages.MessageType;
import com.example.messenger.models.messages.MessagesList;
import com.example.messenger.models.messages.SentMessage;
import com.example.messenger.models.messages.TextMessage;
import com.example.messenger.models.others.ListItemsSelector;
import com.example.messenger.view.activities.ContactsSelectionActivity;
import com.example.messenger.view.activities.GroupDetailsActivity;
import com.example.messenger.view.adapters.MessagesArrayAdapter;
import com.example.messenger.viewmodel.HomeViewModel.ChatUpdateType;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class ChatViewModel {
    private DataModel dataModel;
    private Context context;
    private Chat currentChat;
    private boolean isAdmin;
    private MessagesList messages;
    private String attachedFile;
    private ListItemsSelector selectedMessages;
    private MessagesArrayAdapter messagesArrayAdapter;
    private boolean isActive;

    public ChatViewModel(Context context, String chatUsername, String displayName)
    {
        dataModel = DataModel.getModel();
        this.context = context;
        defineListener();
        currentChat = dataModel.getListeners().getHomeViewModelListener().callGetChat(chatUsername);

        if (currentChat == null)
            currentChat = new Chat(chatUsername, displayName);

        setAdminStatus();
        messages = dataModel.getDatabase().getMessagesList(DataModel.getUsername(), chatUsername);
        selectedMessages = new ListItemsSelector(messages.getArrayList().size());
        messagesArrayAdapter = new MessagesArrayAdapter(context,
                0, messages.getArrayList());
    }

    private void setAdminStatus()
    {
        if(currentChat.isGroup()) {
            GroupChat groupChat = (GroupChat)currentChat;
            isAdmin = groupChat.isMemberAdmin(DataModel.getUsername());
        }
        else
            isAdmin = false;
    }

    public boolean isAdmin()
    {
        return isAdmin;
    }

    public boolean isGroup()
    {
        return currentChat.isGroup();
    }

    public Chat getCurrentChat(){return currentChat;}

    private void setCurrentChat(Chat chat)
    {
        currentChat = chat;
    }

    public MessagesArrayAdapter getMsgArrayAdapter()
    {
        return messagesArrayAdapter;
    }

    public void selectMember(int index)
    {
        selectedMessages.selectMember(index);
    }

    private boolean isActive()
    {
        return isActive;
    }

    public void active (boolean status)
    {
        isActive = status;
    }

   public boolean isSelected(int index)
    {
        return selectedMessages.isSelected(index);
    }

   public boolean atLeastOneSelected()
    {
        return selectedMessages.atLeastOneSelected();
    }

    private void notifyPropertyChanged()
    {
        final MessagesArrayAdapter adapter = messagesArrayAdapter;
        new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void selectFileFromGallery(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            if (selectedImage != null) {
                Cursor cursor = context.getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                attachedFile = cursor.getString(columnIndex);
                cursor.close();
            }
        }
    }

    private ArrayList<Message> getSelectedMessages()
    {
        ArrayList<Boolean> selected = selectedMessages.getSelectedItems();
        ArrayList<Message> selectedMessages = new ArrayList<>();
        for (int i=0; i<selected.size(); i++)
        {
            if (selected.get(i) )
                selectedMessages.add(messages.getMessage(i));
        }

        return selectedMessages;
    }

    public String getChatDisplayName()
    {
        return dataModel.getDatabase().getContactsList().getContact(
                currentChat.getUsername()).getDisplayName();
    }

    private void addNewMessage(Message message)
    {
        messages.add(message);
        notifyPropertyChanged();
    }

    private void commitSentMessage(SentMessage message)
    {
        dataModel.getDatabase().insertMessage(message);
        // add chat to home activity chats list
        dataModel.addChatByMessage(message.actualContactPerson);
        dataModel.getListeners().getHomeViewModelListener().callUpdateChatProperties(
                message.recipient, message.toLastMessage(), ChatUpdateType.LAST_MESSAGE);
        dataModel.getServer().sendDataToServer(message.xml());
    }

    // add chat to home activity by new sent message
    private void addChatByMessage(String username)
    {
        if (dataModel.getListeners().getHomeViewModelListener().callGetChat(username) == null)
        {
            Contact contact = dataModel.getProfile().getContactsList().getContact(username);
            String displayName;
            if (contact != null)
                displayName = dataModel.getProfile().getContactsList().getContact(username).getDisplayName();
            else
                displayName = username;

            dataModel.getDatabase().addNewChat(username, displayName);
            dataModel.getListeners().getHomeViewModelListener().callAddNewChat(
                    new Chat(username, displayName, null,
                    false,0, false));
        }
    }

    public void newMessage(String text) {
        MessageBody body;
        String newId = dataModel.randomID(currentChat.getUsername());
        if (attachedFile != null)
            body = new MediaMessage(MessageType.IMAGE, text, attachedFile, true);
        else
            body = new TextMessage(text);

        SentMessage sentMessage = new SentMessage(dataModel.randomID(newId), DataModel.getUsername(),
                currentChat.getUsername(), body, new MessageDate());
        addNewMessage(sentMessage);
        commitSentMessage(sentMessage);
        attachedFile = null;
        selectedMessages.expand();
        selectedMessages.init();
    }

    public void cleanChat()
    {
        messages.clean();
        dataModel.getDatabase().cleanMessages(currentChat.getUsername());
        dataModel.getListeners().getHomeViewModelListener().callUpdateChatProperties(
                currentChat.getUsername(), null, ChatUpdateType.CLEAN_MESSAGES);
        notifyPropertyChanged();
    }

    public void loadGroupInvitation()
    {
        Intent intent = new Intent(context, ContactsSelectionActivity.class);
        intent.putExtra("action", "1");
        intent.putExtra("username", currentChat.getUsername());
        intent.putExtra("displayName", currentChat.getDisplayName());
        context.startActivity(intent);
    }

    public void loadGroupDetails()
    {
        Intent intent = new Intent(context, GroupDetailsActivity.class);
        intent.putExtra("username", currentChat.getUsername());
        context.startActivity(intent);
    }

    public void updateStatus(MessageStatusUpdate status)
    {
        messages.changeStatus(status);
        notifyPropertyChanged();
    }

    // toolbar operations //
    public void removeSelectedMessages()
    {
        ArrayList<Boolean> selected = selectedMessages.getSelectedItems();
        for (int i=0; i<selected.size(); i++)
        {
            if(selected.get(i))
            {
                dataModel.getDatabase().removeMessage(messages.getMessage(i).id);
                messages.remove(i);
            }
        }
        selectedMessages.init(messages.getArrayList().size()-1);
    }

    public void forwardMessage()
    {
        Intent intent = new Intent((Activity)context, ContactsSelectionActivity.class);
        intent.putExtra("action", "3");
        intent.putExtra("username", currentChat.getUsername());
        context.startActivity(intent);
    }

    public void silenceChat()
    {
        currentChat.setSilenced(!currentChat.isSilenced());
        dataModel.getDatabase().updateChatSilence(currentChat.getUsername(), currentChat.isSilenced());
    }

    private void defineListener()
    {
        ChatViewModelListener listener = new ChatViewModelListener() {
            @Override
            public void callAddChatByMessage(String text) {
                addChatByMessage(text);
            }
            @Override
            public ArrayList<Message> callGetSelectedMessages() {
                return getSelectedMessages();
            }
            @Override
            public boolean callIsActive()
            {
                return isActive();
            }
            @Override
            public Chat callGetCurrentChat()
            {
                return getCurrentChat();
            }
            public void callAddNewMessage(Message message)
            {
                addNewMessage(message);
            }
            @Override
            public void callUpdateStatus(MessageStatusUpdate status)
            {
                updateStatus(status);
            }
            @Override
            public void callSetCurrentChat(Chat chat)
            {
                setCurrentChat(chat);
            }
        };

        dataModel.getListeners().setChatViewModelListener(listener);
    }

}
