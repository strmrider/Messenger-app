package com.example.messenger.viewmodel.HomeViewModel;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;

import com.example.messenger.interfaces.HomeViewModelListener;
import com.example.messenger.models.Contacts.Contact;
import com.example.messenger.models.DataModel.DataModel;
import com.example.messenger.models.chats.Chat;
import com.example.messenger.models.chats.ChatsList;
import com.example.messenger.models.chats.GroupChat;
import com.example.messenger.models.messages.LastMessage;
import com.example.messenger.view.activities.ChatActivity;
import com.example.messenger.view.activities.ContactsSelectionActivity;
import com.example.messenger.view.activities.ProfileActivity;
import com.example.messenger.view.activities.SelectNewChatActivity;
import com.example.messenger.view.activities.SettingsActivity;
import com.example.messenger.view.adapters.ChatsListAdapter;


import java.util.ArrayList;

public class HomeViewModel {
    private DataModel dataModel;
    private Context context;

    private ChatsList chatsList;
    private ChatsListAdapter chatsListAdapter;
    private HomeViewModelListener listener;

    public HomeViewModel(Context context)
    {
        this.context = context;
        // created for the first time
        dataModel = DataModel.getModel();
        defineListener();
        chatsList = dataModel.getDatabase().getChatsList(DataModel.getUsername());

        chatsListAdapter = new ChatsListAdapter(context, 0, chatsList.getArrayList());

        dataModel.bindToServer();
        dataModel.initContacts();
    }

    public void reloadNewUserName(String username)
    {
        chatsListAdapter = new ChatsListAdapter(context, 0, chatsList.getArrayList());
        notifyPropertyChanged();
        dataModel.initContacts();
    }

    public ChatsListAdapter getChatsListAdapter()
    {
        return chatsListAdapter;
    }

    public Chat getChat(String chatUsername)
    {
        return chatsList.getChatPerUsername(chatUsername);
    }

    public void addNewChat(Chat chat)
    {
        chatsList.add(chat);
        notifyPropertyChanged();
    }

    public void updateChatFromIncomeMessage(String chatUsername, LastMessage lastMessage, int unread)
    {
        updateChatProperties(chatUsername, lastMessage, ChatUpdateType.LAST_MESSAGE);
        updateChatProperties(chatUsername, unread, ChatUpdateType.INCREASE_UNREAD);

    }

    public void updateChatProperties(String chatUsername, Object o, ChatUpdateType type)
    {
        synchronized(this) {
            Chat chat = chatsList.getChatPerUsername(chatUsername);
            if (chat != null) {
                switch (type) {
                    case CLEAN_MESSAGES:
                        chat.setLastMessage(null);
                        break;
                    case SET_UNREAD:
                        chat.setUnread((int) o);
                        break;
                    case INCREASE_UNREAD:
                        chat.setUnread(chat.getUnread() + (int)o);
                        break;
                    case LAST_MESSAGE:
                        chat.setLastMessage((LastMessage) o);
                        break;
                    case CHANGE_DISPLAY_NAME:
                        chat.setDisplayName((String) o);
                        break;
                    case ADD_MEMBERS_TO_GROUP:
                    case REMOVE_MEMBERS_FROM_GROUP:
                        GroupChat groupChat = (GroupChat) chat;
                        ArrayList<String> members = (ArrayList<String>) o;
                        if (type == ChatUpdateType.ADD_MEMBERS_TO_GROUP)
                            groupChat.addMembers(members);
                        else
                            groupChat.removeMembers(members);
                        break;
                    default:
                        break;
                }
            }
        }
        notifyPropertyChanged();
    }

    private void notifyPropertyChanged()
    {
        final ChatsListAdapter adapter = chatsListAdapter;
        new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }
    public void startSelectNewChatActivity()
    {
        Intent intent = new Intent(context, SelectNewChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivity(intent);
    }

    public void startChat(int index)
    {
        // set to most recent added chat
        if (index < 0 )
            index = chatsList.getArrayList().size() -1;

        Chat selectedChat = chatsList.getArrayList().get(index);
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("username", selectedChat.getUsername());
        intent.putExtra("displayName", selectedChat.getDisplayName());
        context.startActivity(intent);
        chatsList.getArrayList().get(index).setUnread(0);
        notifyPropertyChanged();
    }

    public void startContactsSelection()
    {
        Intent intent = new Intent(context, ContactsSelectionActivity.class);
        intent.putExtra("action", "0");
        context.startActivity(intent);
    }

    private void updateChatDisplayName(String username, String newDisplayName)
    {
        Chat chat = chatsList.getChatPerUsername(username);
        if (chat != null)
            chat.setDisplayName(newDisplayName);
    }

    public void startEditProfileActivity()
    {
        Intent intent = new Intent((Activity)context, ProfileActivity.class);
        context.startActivity(intent);
    }

    public void startSettingsActivity()
    {
        context.startActivity(new Intent((Activity)context, SettingsActivity.class));
    }

    private void defineListener()
    {
        listener = new HomeViewModelListener() {
            @Override
            public void callUpdateChatDisplayName(String username, String newDisplayName) {
                updateChatDisplayName(username, newDisplayName);
            }

            @Override
            public Chat callGetChat(String chatUsername) {
                return getChat(chatUsername);
            }

            @Override
            public void callUpdateChatFromIncomeMessage(String chatUsername, LastMessage lastMessage, int unread) {
                updateChatFromIncomeMessage(chatUsername, lastMessage, unread);
            }

            @Override
            public void callAddNewChat(Chat chat) {
                addNewChat(chat);
            }

            @Override
            public void callStartChat(int index) {
                startChat(index);
            }

            @Override
            public void callUpdateChatProperties(String chatUsername, Object o, ChatUpdateType type) {
                updateChatProperties(chatUsername, o, type);
            }
        };
        dataModel.getListeners().setHomeViewModelListener(listener);
    }
}
