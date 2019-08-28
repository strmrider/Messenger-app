package com.example.messenger.viewmodel;


import android.content.Context;
import android.content.Intent;

import com.example.messenger.models.Contacts.Contact;
import com.example.messenger.models.DataModel.DataModel;
import com.example.messenger.models.chats.GroupChat;
import com.example.messenger.models.chats.GroupMember;
import com.example.messenger.view.activities.ChatActivity;
import com.example.messenger.view.adapters.GroupMembersArrayAdapter;

import java.util.ArrayList;

public class GroupDetailsViewModel {
    private DataModel dataModel;
    private Context context;

    private GroupChat groupChat;
    private ArrayList<GroupMember> members;
    private GroupMembersArrayAdapter groupMembersArrayAdapter;
    private boolean[] selectedMembers;
    private int selectedMembersCounter;

    public GroupDetailsViewModel(Context context, String groupUsername)
    {
        dataModel = DataModel.getModel();
        this.context = context;

        groupChat = (GroupChat)dataModel.getListeners().getHomeViewModelListener()
                .callGetChat(groupUsername);
        members = groupChat.getMembers().getMembersList();
        setContactsInMembersList(members);
        groupMembersArrayAdapter = new GroupMembersArrayAdapter(context, 0, members);

        selectedMembers = new boolean[members.size()];
        selectedMembersCounter = 0;
    }

    private void setContactsInMembersList(ArrayList<GroupMember> membersList)
    {
        Contact contact;
        for (int i=0; i<membersList.size(); i++)
        {
            if (membersList.get(i).username.equals(DataModel.getUsername())) {
                membersList.add(new GroupMember(membersList.get(i).username, membersList.get(i).isAdmin));
                membersList.remove(i);
            }
            contact = dataModel.getProfile().getContactsList().getContact(membersList.get(i).username);
            membersList.get(i).contact = contact;
        }
    }

    public GroupMembersArrayAdapter getGroupMembersArrayAdapter()
    {
        return groupMembersArrayAdapter;
    }

    public boolean isAdmin()
    {
        return groupChat.isMemberAdmin(DataModel.getUsername());
    }

    public boolean isAdmin(int index)
    {
        return members.get(index).isAdmin;
    }

    public boolean isThereAnotherAdmin()
    {
        return groupChat.getMembers().getList(true).size() > 1;
    }

    public void selectMember(int index)
    {
        if(selectedMembers[index])
            selectedMembersCounter--;
        else
            selectedMembersCounter++;

        selectedMembers[index] = !selectedMembers[index];
    }

    public boolean myself(int index)
    {
        return members.get(index).username.equals(DataModel.getUsername());
    }

    public void removeMembers()
    {
        ArrayList<String> removedMembers = new ArrayList<>();
        for (int i=0; i<selectedMembers.length; i++)
        {
            if (selectedMembers[i]) {
                if (!myself(i) || (myself(i)&& !isThereAnotherAdmin()))
                removedMembers.add(members.get(i).username);
                members.remove(i);
            }
        }
        removeMembersFromGroup(groupChat.getUsername(), removedMembers);

        selectedMembers = new boolean[members.size()];
        selectedMembersCounter = 0;
        groupMembersArrayAdapter.notifyDataSetChanged();
    }

    private void removeMembersFromGroup(String chatUserName, ArrayList<String> members)
    {
        GroupChat groupChat = (GroupChat)dataModel.getListeners().getHomeViewModelListener().
                callGetChat(chatUserName);
        String request = groupChat.removeMembersRequest(members);
        groupChat.removeMembers(members);
        dataModel.getDatabase().updateGroupMembers(chatUserName, groupChat.serializeForDB());
        dataModel.getServer().sendDataToServer(request);
    }

    public boolean atLeastOneSelected()
    {
        return selectedMembersCounter > 0;
    }

    public void startChat(int index)
    {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("username", members.get(index).username);
        context.startActivity(intent);
    }

    public void addToContacts(int index)
    {

    }
}
