package com.example.messenger.viewmodel;


import android.content.Context;

import com.example.messenger.models.Contacts.Contact;
import com.example.messenger.models.DataModel.DataModel;
import com.example.messenger.models.DataModel.Listeners;
import com.example.messenger.models.chats.GroupChat;
import com.example.messenger.view.adapters.ContactsSelectionArrayAdapter;

import java.util.ArrayList;

public class CreateGroupViewModel {
    private DataModel dataModel;
    private ArrayList<Contact> selectedContacts;
    private ContactsSelectionArrayAdapter contactsSelectionArrayAdapter;
    private Context context;

    public CreateGroupViewModel(Context context)
    {
        dataModel = DataModel.getModel();
        selectedContacts = dataModel.getRecentSelectedContacts();
        contactsSelectionArrayAdapter =
                new ContactsSelectionArrayAdapter(context, 0, selectedContacts, 1);
    }

    public ContactsSelectionArrayAdapter getContactsSelectionArrayAdapter()
    {
        return contactsSelectionArrayAdapter;
    }

    public void createGroup(String name)
    {
        GroupChat newGroup = new GroupChat(dataModel.randomID(name), name, selectedContacts);
        newGroup(newGroup);
    }

    private void newGroup(GroupChat group)
    {
        Listeners listeners = dataModel.getListeners();
        listeners.getHomeViewModelListener().callAddNewChat(group);
        dataModel.getDatabase().addNewGroup(group);
        listeners.getHomeViewModelListener().callStartChat(-1);
        dataModel.getServer().sendDataToServer(
                group.invitationRequest(dataModel.getRecentSelectedContacts()));
    }

}
