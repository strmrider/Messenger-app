package com.example.messenger.viewmodel;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.messenger.interfaces.ContactsSelectionViewModelListener;
import com.example.messenger.models.Contacts.Contact;
import com.example.messenger.models.DataModel.DataModel;
import com.example.messenger.models.chats.GroupChat;
import com.example.messenger.models.messages.Message;
import com.example.messenger.models.others.ListItemsSelector;
import com.example.messenger.view.activities.CreateGroupActivity;
import com.example.messenger.view.adapters.ContactsSelectionArrayAdapter;

import java.util.ArrayList;

public class ContactsSelectionViewModel {
    private DataModel dataModel;
    private ContactsSelectionViewModel viewModel;
    private Context context;
    private ArrayList<Contact> contacts;
    private ContactsSelectionArrayAdapter contactsSelectionArrayAdapter;
    private ListItemsSelector selectedMembers;
    private int action;
    private String groupUsername;

    // invite to new group
    // full contacts list
    public ContactsSelectionViewModel(Context context)
    {
        dataModel = DataModel.getModel();
        contacts = dataModel.getProfile().getContactsList().toArrayList();
        action = 0;
        init(context, "");
    }

    // invite to existed group
    public ContactsSelectionViewModel(Context context, String groupUsername)
    {
        dataModel = DataModel.getModel();
        GroupChat groupChat = (GroupChat)dataModel.getListeners().getHomeViewModelListener().callGetChat(groupUsername);
        contacts =
                dataModel.getDatabase().getContactsList().eliminateSharedContacts(
                        groupChat.getMembers().getAllMembersArrayList());
        init(context, groupUsername);
        action = 1;
        this.groupUsername = groupUsername;

    }

    public ContactsSelectionViewModel(Context context, String username, int action)
    {
        dataModel = DataModel.getModel();
        GroupChat groupChat = (GroupChat)dataModel.getListeners().getHomeViewModelListener().callGetChat(username);
        ArrayList<String> members = groupChat.getMembers().getAllMembersArrayList();

        contacts = dataModel.getProfile().getContactsList().eliminateSharedContacts(members);
        this.action = action;
        init(context, username);
    }

    private void init(Context context, String groupUsername)
    {
        this.context = context;
        contactsSelectionArrayAdapter =
                new ContactsSelectionArrayAdapter(context, 0, contacts, 0);
        selectedMembers = new ListItemsSelector(contacts.size());
        this.groupUsername = groupUsername;
        defineListener();
    }


    public ContactsSelectionArrayAdapter getContactsSelectionArrayAdapter()
    {
        return contactsSelectionArrayAdapter;
    }

    public void selectMember(int index)
    {
        selectedMembers.selectMember(index);
    }

    public boolean isSelected(int index)
    {
        return selectedMembers.isSelected(index);
    }

    private void setSelectedContacts()
    {
        ArrayList<Contact> selected = new ArrayList<>();
        for (int i=0; i< selectedMembers.getSelectedItems().size(); i++)
        {
            if (selectedMembers.isSelected(i))
                selected.add(contacts.get(i));
        }
        dataModel.setSelectedContacts(selected);
    }

    private void inviteMembers()
    {
        setSelectedContacts();
        if (action == 0) {
            Intent intent = new Intent(context, CreateGroupActivity.class);
            context.startActivity(intent);
        }
        else if (action == 1)
        {
            GroupChat group = (GroupChat)dataModel.getListeners().getHomeViewModelListener().
                    callGetChat(groupUsername);
            group.addMembers(dataModel.getRecentSelectedContacts(), false);
            dataModel.getDatabase().updateGroupMembers(group.getUsername(), group.serializeForDB());
        }
    }

    public void finishActivity()
    {
        ((Activity)context).finish();
    }

    public void done()
    {
        if (action < 3)
            inviteMembers();
        else
            forwardMessages();

        finishActivity();
    }

    private void defineListener()
    {
        ContactsSelectionViewModelListener listener = new ContactsSelectionViewModelListener() {
            @Override
            public void callFinishActivity() {
                finishActivity();
            }
        };

        dataModel.getListeners().setContactsSelectionViewModelListener(listener);
    }

    private void forwardMessages()
    {
        setSelectedContacts();
        ArrayList<Message> messages =
                dataModel.getListeners().getChatViewModelListener().callGetSelectedMessages();
        for(int i=0; i<messages.size(); i++) {
            for(int j=0; j<dataModel.getRecentSelectedContacts().size(); j++) {
                dataModel.getListeners().getChatViewModelListener().callAddChatByMessage(
                        dataModel.getRecentSelectedContacts().get(j).getUsername());
                dataModel.getServer().sendDataToServer(messages.get(i).toSentMessage
                        (dataModel.getRecentSelectedContacts().get(j).getUsername()).xml());
            }
        }
    }
}
