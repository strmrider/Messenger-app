package com.example.messenger.viewmodel;

import android.content.Context;
import android.content.Intent;

import com.example.messenger.models.Contacts.Contact;
import com.example.messenger.models.DataModel.DataModel;
import com.example.messenger.view.activities.ChatActivity;
import com.example.messenger.view.adapters.ContactsSelectionArrayAdapter;

import java.util.ArrayList;


public class SelectNewChatViewModel {
    private DataModel dataModel;
    private Context context;
    private ContactsSelectionArrayAdapter contactsSelectionArrayAdapter;
    private ArrayList<Contact> contacts;

    public SelectNewChatViewModel(Context context)
    {
        this.context = context;
        dataModel = DataModel.getModel();
        contacts = dataModel.getProfile().getContactsList().toArrayList();
        contactsSelectionArrayAdapter =
                new ContactsSelectionArrayAdapter(context, 0 , contacts, 0);
    }

    public ContactsSelectionArrayAdapter getContactsSelectionArrayAdapter()
    {
        return contactsSelectionArrayAdapter;
    }

    public void startChat(int index)
    {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("username", contacts.get(index).getUsername());
        intent.putExtra("displayName", contacts.get(index).getDisplayName());
        context.startActivity(intent);
    }
}
