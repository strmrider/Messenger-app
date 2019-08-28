package com.example.messenger.models.DataModel;

import com.example.messenger.interfaces.ChatViewModelListener;
import com.example.messenger.interfaces.ContactsSelectionViewModelListener;
import com.example.messenger.interfaces.HomeViewModelListener;

public class Listeners {
    private HomeViewModelListener homeViewModelListener = null;
    private ContactsSelectionViewModelListener contactsSelectionViewModelListener = null;
    private ChatViewModelListener chatViewModelListener= null;

    public HomeViewModelListener getHomeViewModelListener()
    {
        return homeViewModelListener;
    }

    public void setHomeViewModelListener(HomeViewModelListener listener)
    {
        homeViewModelListener = listener;
    }

    public ContactsSelectionViewModelListener getContactsSelectionViewModelListener()
    {
        return contactsSelectionViewModelListener;
    }

    public void setContactsSelectionViewModelListener
            (ContactsSelectionViewModelListener listener)
    {
        contactsSelectionViewModelListener = listener;
    }

    public ChatViewModelListener getChatViewModelListener()
    {
        return chatViewModelListener;
    }

    public void setChatViewModelListener(ChatViewModelListener listener)
    {
        chatViewModelListener = listener;
    }
}
